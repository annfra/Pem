package pg.autyzm.graprzyjazneemocje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Ann on 01.11.2016.
 */
public class SqlliteManager extends SQLiteOpenHelper {

    public SqlliteManager(final Context context, String databaseName)  {
        super(new DatabaseContext(context), databaseName, null, 2);
    }

    public void onCreate(SQLiteDatabase db)
    {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public Cursor givePhotosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
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
        String[] columns = {"id", "photos_or_videos", "is_level_active"};
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
