package pg.autyzm.graprzyjazneemocje;

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

    String name;

    List<Integer> photosOrVideosList;
    List<Integer> emotions;




    public Level(Cursor cur, Cursor cur2, Cursor cur3){

        photosOrVideosList = new ArrayList<Integer>();
        emotions = new ArrayList<Integer>();

        while(cur.moveToNext())
        {
            id = cur.getInt(cur.getColumnIndex("id"));
            photosOrVideos = cur.getString(cur.getColumnIndex("photos_or_videos"));
            timeLimit = cur.getInt(cur.getColumnIndex("time_limit"));
            pvPerLevel = cur.getInt(cur.getColumnIndex("photos_or_videos_per_level"));
            int active = cur.getInt(cur.getColumnIndex("is_level_active"));

            isLevelActive = (active != 0);
            name = cur.getString(cur.getColumnIndex("name"));
        }

        if(cur2 != null){

            while(cur2.moveToNext()){
                System.out.println("Cos sie zadzialo");
                photosOrVideosList.add(cur2.getInt(cur2.getColumnIndex("photoid")));

            }
        }

        if(cur3 != null){

            System.out.println("+++");

            while(cur3.moveToNext()){

                emotions.add(cur3.getInt(cur3.getColumnIndex("emotionid")));
                System.out.println("+++++" + cur3.getInt(cur3.getColumnIndex("emotionid")));

            }
        }

    }


    public Level(){

        photosOrVideosList = new ArrayList<Integer>();
        emotions = new ArrayList<Integer>();

        isLevelActive = true;
        id = 0;

    }


}
