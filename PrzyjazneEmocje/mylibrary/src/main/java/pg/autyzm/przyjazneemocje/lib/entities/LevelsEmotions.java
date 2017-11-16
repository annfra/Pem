package pg.autyzm.przyjazneemocje.lib.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by user on 16.11.2017.
 */
@DatabaseTable(tableName = "levels_emotions")
public class LevelsEmotions {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField(foreign = true)
    private Level level;
    @DatabaseField(foreign = true)
    private Emotion emotion;

    public LevelsEmotions(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}
