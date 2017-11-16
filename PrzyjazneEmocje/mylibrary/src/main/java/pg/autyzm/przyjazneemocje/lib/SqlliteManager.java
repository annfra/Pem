package pg.autyzm.przyjazneemocje.lib;

/**
 * Created by Ann on 26.09.2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pg.autyzm.przyjazneemocje.lib.entities.Emotion;
import pg.autyzm.przyjazneemocje.lib.entities.Level;
import pg.autyzm.przyjazneemocje.lib.entities.LevelsEmotions;
import pg.autyzm.przyjazneemocje.lib.entities.LevelsPhotos;
import pg.autyzm.przyjazneemocje.lib.entities.Photo;


public class SqlliteManager extends SQLiteOpenHelper {

    private static SqlliteManager sInstance;

    private static final String DATABASE_NAME = "przyjazneemocje";

    private Dao<Level,Integer> levelDao;
    private Dao<Emotion,Integer> emotionDao;
    private Dao<Photo,Integer> photoDao;
    private SQLiteDatabase db;



    public static synchronized SqlliteManager getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SqlliteManager(context.getApplicationContext());
        }
        return sInstance;
    }


    private SqlliteManager (final Context context)
    {
        super(new DatabaseContext(context), DATABASE_NAME, null, 2);
        db = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db)
    {

        this.db = db;

        System.out.println("Tworze tablee");
        ConnectionSource connectionSource =
                new AndroidConnectionSource(this);

        try {
            TableUtils.createTable(connectionSource, Level.class);
            TableUtils.createTable(connectionSource, Photo.class);
            TableUtils.createTable(connectionSource, Emotion.class);
            TableUtils.createTable(connectionSource, LevelsPhotos.class);
            TableUtils.createTable(connectionSource, LevelsEmotions.class);

            addEmotion("happy");
            addEmotion("sad");
            addEmotion("angry");
            addEmotion("scared");
            addEmotion("surprised");
            addEmotion("bored");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void onOpen(SQLiteDatabase db){

        this.db = db;

        // initialize daos
        ConnectionSource connectionSource =
                new AndroidConnectionSource(this);
        try {
            levelDao = DaoManager.createDao(connectionSource, Level.class);
            emotionDao = DaoManager.createDao(connectionSource, Emotion.class);
            photoDao = DaoManager.createDao(connectionSource, Photo.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void addEmotion(String emotion){
        try {
            emotionDao.create(new Emotion(emotion));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPhoto(int path, String emotion, String fileName){
        try {
            photoDao.create(new Photo(path, emotion, fileName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLevel(int id){
        try {
            levelDao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanPhotosTable() {
        ConnectionSource connectionSource =
                new AndroidConnectionSource(this);

        try {
            TableUtils.clearTable(connectionSource, Photo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Photo> givePhotosWithEmotion(String emotion){
        List<Photo> photos = new ArrayList<>();
        try {
            photos.addAll(photoDao.queryForEq("emotion", emotion));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }

    public Photo givePhotoWithPath(String path){
        try {
            return photoDao.queryForEq("path", path).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Photo givePhotoWithId(int id){
        try {
            return photoDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int giveEmotionId(String name){
        try {
            return emotionDao.queryForEq("name", name).get(0).getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String giveEmotionName(int id){
        try {
            return emotionDao.queryForId(id).getEmotion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Emotion> giveAllEmotions(){

        List<Emotion> emotions = new ArrayList<>();

        try {
            emotions.addAll(emotionDao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emotions;
    }

    public List<Level> giveAllLevels(){
        List<Level> levels = new ArrayList<>();
        try {
            levels.addAll(levelDao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return levels;
    }

    public Level giveLevel(int id){
        try {
            return levelDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addLevel(Level l){
        try {
            levelDao.create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
