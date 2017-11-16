package pg.autyzm.przyjazneemocje.lib.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by user on 16.11.2017.
 */
@DatabaseTable(tableName = "emotions")
public class Emotion {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String emotion;

    @ForeignCollectionField
    private List<LevelsEmotions> levelsEmotionsList;

    public Emotion(String emotion){
        this.emotion = emotion;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String text) {
        this.emotion = emotion;
    }

    public List<LevelsEmotions> getLevelsEmotionsList() {
        return levelsEmotionsList;
    }

    public void setLevelsEmotionsList(List<LevelsEmotions> levelsEmotionsList) {
        this.levelsEmotionsList = levelsEmotionsList;
    }
}
