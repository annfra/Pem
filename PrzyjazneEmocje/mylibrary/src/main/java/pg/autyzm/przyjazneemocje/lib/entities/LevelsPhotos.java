package pg.autyzm.przyjazneemocje.lib.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by user on 16.11.2017.
 */

@DatabaseTable(tableName = "levels_photos")
public class LevelsPhotos {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField(foreign = true)
    private Level level;
    @DatabaseField(foreign = true)
    private Photo photo;

    public LevelsPhotos(){

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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
