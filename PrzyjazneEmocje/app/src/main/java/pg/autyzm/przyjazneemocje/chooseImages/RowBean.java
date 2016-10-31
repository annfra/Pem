package pg.autyzm.przyjazneemocje.chooseImages;

/**
 * Created by Joanna on 2016-10-11.
 */

public class RowBean {

    int icon;
    boolean selected;


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public RowBean(){

    }

    public RowBean(int icon, boolean selected) {

        this.icon = icon;
        this.selected = selected;
    }
}