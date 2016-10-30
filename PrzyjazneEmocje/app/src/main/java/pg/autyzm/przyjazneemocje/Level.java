package pg.autyzm.przyjazneemocje;

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

    List<String> photosOrVideosList;
    List<String> emotions;

    boolean isLevelActive;


    public Level(){

        photosOrVideosList = new ArrayList<String>();
        emotions = new ArrayList<String>();

        isLevelActive = true;

    }


}
