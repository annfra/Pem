package pg.autyzm.przyjazneemocje.lib.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by user on 16.11.2017.
 */
@DatabaseTable(tableName = "emotions")
public class Emotion {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String text;

    public Emotion(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
