package pg.autyzm.przyjazneemocje;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 25.10.2016.
 */
public class Level {

    int id;

    String photosOrVideos;
    int timeLimit;
    int pvPerLevel;
    boolean isLevelActive;

    List<String> photosOrVideosList;
    List<String> emotions;




    public Level(Cursor cur){

        while(cur.moveToNext())
        {
            id = cur.getInt(cur.getColumnIndex("id"));
            photosOrVideos = cur.getString(cur.getColumnIndex("photos_or_videos"));
            timeLimit = cur.getInt(cur.getColumnIndex("time_limit"));
            pvPerLevel = cur.getInt(cur.getColumnIndex("photos_or_videos_per_level"));
            int active = cur.getInt(cur.getColumnIndex("is_level_active"));

            isLevelActive = (active != 0);

        }

    }





    public Level(){

        photosOrVideosList = new ArrayList<String>();
        emotions = new ArrayList<String>();

        isLevelActive = true;
        id = 0;

    }


}
