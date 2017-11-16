package pg.autyzm.przyjazneemocje.lib.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by user on 16.11.2017.
 */

@DatabaseTable(tableName = "photos")
public class Photo {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int path;
    @DatabaseField
    private String emotion;
    @DatabaseField
    private String name;

    @ForeignCollectionField
    private List<LevelsPhotos> levelsPhotosList;

    public Photo(){

    }

    public Photo(int path, String emotion, String name){
        this.path = path;
        this.emotion = emotion;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LevelsPhotos> getLevelsPhotosList() {
        return levelsPhotosList;
    }

    public void setLevelsPhotosList(List<LevelsPhotos> levelsPhotosList) {
        this.levelsPhotosList = levelsPhotosList;
    }
}
