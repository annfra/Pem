package pg.autyzm.przyjazneemocje;

/**
 * Created by Ann on 26.09.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlliteManager extends SQLiteOpenHelper {


    public SqlliteManager (final Context context, String databaseName)
    {
        super(new DatabaseContext(context), databaseName, null, 2);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path int," + "emotion text," + "name text);" + "");
        db.execSQL("create table emotions(" + "id integer primary key autoincrement," + "emotion text);" + "");
        db.execSQL("create table levels(" + "id integer primary key autoincrement, photos_or_videos text, photos_or_videos_per_level int, time_limit int, is_level_active boolean, name text);" + "");
        db.execSQL("create table levels_photos(" + "id integer primary key autoincrement,"  + "levelid integer references levels(id)," + "photoid integer references photos(id));" + "");
        db.execSQL("create table levels_emotions(" + "id integer primary key autoincrement," + "levelid integer references levels(id),"  + "emotionid integer references emotions(id));" + "");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void addEmotion(String emotion)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("emotion",emotion);
        System.out.println(db.insertOrThrow("emotions", null, values) + " addEmotion");
    }

    public void addPhoto(int path, String emotion, String fileName)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        values.put("name",fileName);
        db.insertOrThrow("photos", null, values);
    }


    public void addLevel(Level level)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("photos_or_videos",level.photosOrVideos);
        values.put("name",level.name);
        values.put("photos_or_videos_per_level",level.pvPerLevel);
        values.put("time_limit",level.timeLimit);
        values.put("is_level_active", level.isLevelActive);


        if(level.id != 0) {
            //values.put("id", level.id);
            System.out.println("Update, time limit " + level.timeLimit);
            db.update("levels", values, "id=" + level.id, null);

            /*
                usunac wszystkie rekordy polaczone z tym poziomem.

            */

            delete("levels_photos", "levelid", String.valueOf(level.id));
            delete("levels_emotions", "levelid", String.valueOf(level.id));

        }
        else {
            long longId = db.insertOrThrow("levels", null, values);
            level.id = (int) longId;
        }

        // Dodaj rekordy wiele do wielu ze zdjeciami/video



        System.out.println("addLevel");

        for(Integer photoOrVideo : level.photosOrVideosList){


            System.out.println("^^" + photoOrVideo);

            values = new ContentValues();
            values.put("levelid",level.id);
            values.put("photoid",photoOrVideo);

            db.insertOrThrow("levels_photos", null, values);
        }

        for(Integer emotion : level.emotions){


            System.out.println("^^" + emotion);

            values = new ContentValues();
            values.put("levelid",level.id);
            values.put("emotionid",emotion);

            db.insertOrThrow("levels_emotions", null, values);
        }


    }

    public void delete(String tableName, String columnName, String value)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {"" + value};
        db.delete(tableName, columnName + "=?",args);
    }

    public void cleanTable(String tableName)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ tableName);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + tableName +"'");
    }

    public Cursor givePhotosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotosInLevel(int levelId)
    {
        String[] columns = {"id", "levelid", "photoid"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("levels_photos", columns,"levelid like " + "'%" + levelId + "%'", null, null, null, null);
        return cursor;
    }


    public Cursor giveEmotionsInLevel(int levelId)
    {
        String[] columns = {"id", "levelid", "emotionid"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("levels_emotions", columns,"levelid like " + "'%" + levelId + "%'", null, null, null, null);
        return cursor;
    }


    public Cursor giveEmotionId(String name){

        String[] columns = {"id", "emotion"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("emotions", columns,"emotion like " + "'%" + name + "%'", null, null, null, null);
        return cursor;

    }



    public Cursor giveAllEmotions()
    {
        String[] columns = {"id","emotion"};//"photos_or_videos", "photos_or_videos_per", "time_limit"
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("emotions", columns,null, null, null, null, null);
        return cursor;
    }

    public Cursor giveAllLevels()
    {
        String[] columns = {"id", "photos_or_videos", "is_level_active", "name"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("levels", columns,null, null, null, null, null);

        return cursor;
    }

    public Cursor giveLevel(int id)
    {
        String[] columns = {"id", "photos_or_videos"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("levels", columns,null, null, null, null, null);


        cursor =  db.rawQuery("select * from levels where id='" + id + "'" , null);


        return cursor;
    }




}
