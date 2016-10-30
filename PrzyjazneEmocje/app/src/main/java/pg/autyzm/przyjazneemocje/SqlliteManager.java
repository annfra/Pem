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

    public SqlliteManager (Context context)
    {
        super(context, "friendly_emotions.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path int," + "emotion text);" + "");
        db.execSQL("create table emotions(" + "id integer primary key autoincrement," + "emotion text);" + "");
        db.execSQL("create table levels(" + "id integer primary key autoincrement, photos_or_videos text, photos_or_videos_per_level int, time_limit int, is_level_active boolean);" + "");
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
        db.insertOrThrow("emotions", null, values);
    }

    public void addPhoto(int path, String emotion)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        db.insertOrThrow("photos", null, values);
    }


    public void addLevel(Level level)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("photos_or_videos",level.photosOrVideos);
        values.put("photos_or_videos_per_level",level.pvPerLevel);
        values.put("time_limit",level.timeLimit);
        values.put("is_level_active", level.isLevelActive);

        System.out.println("Do bazy idzie " + level.isLevelActive);

        if(level.id != 0) {
            //values.put("id", level.id);
            System.out.println("Update, time limit " + level.timeLimit);
            db.update("levels", values, "id=" + level.id, null);
        }
        else {

            db.insertOrThrow("levels", null, values);

        }
    }

    public void delete(String tableName, int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {"" + id};
        db.delete(tableName,"id=?",args);
    }

    public void cleanTable(String tableName)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ tableName);
    }

    public Cursor givePhotosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
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
        String[] columns = {"id", "photos_or_videos"};
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
