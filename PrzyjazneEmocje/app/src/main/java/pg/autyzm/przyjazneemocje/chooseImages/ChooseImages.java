package pg.autyzm.przyjazneemocje.chooseImages;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannedString;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pg.autyzm.przyjazneemocje.LevelConfiguration;
import pg.autyzm.przyjazneemocje.R;
import pg.autyzm.przyjazneemocje.SqlliteManager;

/**
 * Created by Joanna on 2016-10-08.
 */

public class ChooseImages extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener {

    private ListView listView;
    private String choosenEmotion;
    private RowBean[] tabPhotos;
    private TextView textView;
    private String emoInLanguage;
    private ArrayList<Integer> listSelectedPhotos;

    public void saveImagesToList(View view) {

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("selected_photos", listSelectedPhotos);
        Intent returnIntent = new Intent();
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    public void close(View view) {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_images);

        SqlliteManager sqlm = new SqlliteManager(this, "przyjazneemocje");

        Bundle bundle = getIntent().getExtras();
        emoInLanguage = bundle.getString("SpinnerValue");

        Map<String, String> mapEmo = new ArrayMap<>();
        mapEmo.put(getResources().getString(R.string.emotion_happy), "happy");
        mapEmo.put(getResources().getString(R.string.emotion_sad), "sad");
        mapEmo.put(getResources().getString(R.string.emotion_angry), "angry");
        mapEmo.put(getResources().getString(R.string.emotion_scared), "scared");
        mapEmo.put(getResources().getString(R.string.emotion_surprised), "surprised");
        mapEmo.put(getResources().getString(R.string.emotion_bored), "bored");

        choosenEmotion = mapEmo.get(emoInLanguage);

        textView = (TextView) findViewById(R.id.TextViewChoose);
        String str = getResources().getString(R.string.select);

        Cursor cursor = sqlm.givePhotosWithEmotion(choosenEmotion);
        int n = cursor.getCount();
        tabPhotos = new RowBean[n];
        while (cursor.moveToNext()) {
            tabPhotos[--n] = (new RowBean(cursor.getInt(1), false));
        }

        //wybrane wczesniej
        listSelectedPhotos = bundle.getIntegerArrayList("selected_photos");
        for (int selected : listSelectedPhotos) {
            for (RowBean el : tabPhotos) {
                if (el.getIcon() == selected) {
                    el.setSelected(true);
                }
            }
        }

        textView.setText(emoInLanguage + " " + str + ": " + countSelectedPhotos());

        RowAdapter adapter = new RowAdapter(this, R.layout.item, tabPhotos);
        listView = (ListView) findViewById(R.id.image_list);
        listView.setAdapter(adapter);
    }

    private int countSelectedPhotos() {
        int numberOfPhotos = 0;
        for (RowBean el : tabPhotos) {
            if (el.selected) {
                numberOfPhotos++;
            }
        }
        return numberOfPhotos;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            int pos = listView.getPositionForView(buttonView);
            if (pos != ListView.INVALID_POSITION) {
                if (isChecked) {
                    tabPhotos[pos].setSelected(true);
                    listSelectedPhotos.add(tabPhotos[pos].getIcon());
                } else {
                    tabPhotos[pos].setSelected(false);
                     listSelectedPhotos.remove((Object)tabPhotos[pos].getIcon());
                }
            }

            String str = getResources().getString(R.string.select);
            textView.setText(emoInLanguage + " " + str + ": " + countSelectedPhotos());
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
