package pg.autyzm.przyjazneemocje.lib;

/**
 * Created by Ann on 26.09.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlliteManager extends SQLiteOpenHelper {


    public SqlliteManager (final Context context, String databaseName)
    {
        super(new DatabaseContext(context), databaseName, null, 2);
    }

    public void onCreate(SQLiteDatabase db)
    {
        System.out.println("Tworze tablee");
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path int," + "emotion text," + "name text);" + "");
        db.execSQL("create table emotions(" + "id integer primary key autoincrement," + "emotion text);" + "");
        db.execSQL("create table levels(" + "id integer primary key autoincrement, photos_or_videos text, photos_or_videos_per_level int, " +
                "time_limit int, is_level_active boolean, name text, correctness int, sublevels int);" + "");
        db.execSQL("create table levels_photos(" + "id integer primary key autoincrement,"  + "levelid integer references levels(id)," + "photoid integer references photos(id));" + "");
        db.execSQL("create table levels_emotions(" + "id integer primary key autoincrement," + "levelid integer references levels(id),"  + "emotionid integer references emotions(id));" + "");


        addEmotion("happy");
        addEmotion("sad");
        addEmotion("angry");
        addEmotion("scared");
        addEmotion("surprised");
        addEmotion("bored");

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

        //System.out.println("path " + path);
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
        values.put("photos_or_videos", level.getPhotosOrVideos());
        values.put("name", level.getName());
        values.put("photos_or_videos_per_level", level.getPvPerLevel());
        values.put("time_limit", level.getTimeLimit());
        values.put("is_level_active", level.isLevelActive());
        values.put("correctness", level.getCorrectness());
        values.put("sublevels", level.getSublevels());


        if(level.getId() != 0) {
            //values.put("id", level.id);
            System.out.println("Update, time limit " + level.getTimeLimit());
            db.update("levels", values, "id=" + level.getId(), null);

            /*
                usunac wszystkie rekordy polaczone z tym poziomem.

            */

            delete("levels_photos", "levelid", String.valueOf(level.getId()));
            delete("levels_emotions", "levelid", String.valueOf(level.getId()));

        }
        else {
            long longId = db.insertOrThrow("levels", null, values);
            level.setId((int) longId);
        }

        // Dodaj rekordy wiele do wielu ze zdjeciami/video



        System.out.println("addLevel");
        /*
        wyswietlenie co jest w obiekcie level przed jego zapisaniem do bazy danych

         */
        System.out.println("Level name " + level.getName());
        System.out.println("Is level active " + level.isLevelActive());
        System.out.println("Time limit " + level.getTimeLimit());
        System.out.println("Photos per level " + level.getPvPerLevel());
        System.out.println("Photos or videos " + level.getPhotosOrVideos());


        for(Integer photoOrVideo : level.getPhotosOrVideosList()){


            System.out.println("Photo id " + photoOrVideo);

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("photoid",photoOrVideo);

            db.insertOrThrow("levels_photos", null, values);
        }

        for(Integer emotion : level.getEmotions()){


            System.out.println("Emotion id " + emotion);

            values = new ContentValues();
            values.put("levelid", level.getId());
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

    public Cursor givePhotoWithPath(String path)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,"path like " + "'%" + path + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotoWithId(int id)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,"id like " + "'%" + id + "%'", null, null, null, null);
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

    public Cursor giveEmotionName(int id){

        String[] columns = {"id", "emotion"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("emotions", columns,"id like " + "'%" + id + "%'", null, null, null, null);
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


    public String giveNameOfEmotionFromPhoto(String nameOfPhoto)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,null, null, null, null, null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(3);
            if(name.equals(nameOfPhoto))
                return cursor.getString(2);
        }
        return "Fail";
    }
}
