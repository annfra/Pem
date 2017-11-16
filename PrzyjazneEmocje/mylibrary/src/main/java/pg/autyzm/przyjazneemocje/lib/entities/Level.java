package pg.autyzm.przyjazneemocje.lib.entities;

import android.database.Cursor;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 25.10.2016.
 */
@DatabaseTable(tableName = "levels")
public class Level {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String photosOrVideos;
    @DatabaseField
    private int timeLimit;
    @DatabaseField
    private int pvPerLevel;
    @DatabaseField
    private int isLevelActiveInt;
    @DatabaseField
    private int sublevels;
    @DatabaseField
    private int correctness;
    @DatabaseField
    private String name;

    @ForeignCollectionField
    private List<LevelsEmotions> levelsEmotionsList;
    @ForeignCollectionField
    private List<LevelsPhotos> levelsPhotosList;

    private List<Emotion> emotionsList;
    private List<Photo> photosOrVideosList;
    private boolean isLevelActive;

    public Level(){

        setPhotosOrVideosList(new ArrayList<Photo>());
        setEmotions(new ArrayList<Emotion>());

        isLevelActive = (isLevelActiveInt == 1 ? true : false);


        setLevelActive(true);
        setId(0);

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotosOrVideos() {
        return photosOrVideos;
    }

    public void setPhotosOrVideos(String photosOrVideos) {
        this.photosOrVideos = photosOrVideos;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPvPerLevel() {
        return pvPerLevel;
    }

    public void setPvPerLevel(int pvPerLevel) {
        this.pvPerLevel = pvPerLevel;
    }

    public boolean isLevelActive() {
        return isLevelActive;
    }

    public void setLevelActive(boolean levelActive) {
        isLevelActive = levelActive;
    }

    public int getSublevels() {
        return sublevels;
    }

    public void setSublevels(int sublevels) {
        this.sublevels = sublevels;
    }

    public int getCorrectness() {
        return correctness;
    }

    public void setCorrectness(int correctness) {
        this.correctness = correctness;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotosOrVideosList() {
        return photosOrVideosList;
    }

    public void setPhotosOrVideosList(List<Photo> photosOrVideosList) {
        this.photosOrVideosList = photosOrVideosList;
    }

    public List<Emotion> getEmotions() {
        return emotionsList;
    }

    public void setEmotions(List<Emotion> emotions) {
        this.emotionsList = emotions;
    }

    public int getIsLevelActiveInt() {
        return isLevelActiveInt;
    }

    public void setIsLevelActiveInt(int isLevelActiveInt) {
        this.isLevelActiveInt = isLevelActiveInt;
    }
}
