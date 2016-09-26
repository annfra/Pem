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
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path text," + "emotion text);" + "");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void addPhoto(String path, String emotion)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        db.insertOrThrow("photos", null, values);
    }

    public Cursor givePhotosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("photos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
        return cursor;
    }


}
