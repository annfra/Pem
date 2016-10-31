package pg.autyzm.przyjazneemocje.chooseImages;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannedString;
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

    public void saveImagesToList(View view) {
        String out = "";
        for (RowBean el : tabPhotos) {
            if (el.selected) {
                out += el.icon + ";";
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("choosenImages", out);
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

        SqlliteManager sqlm = new SqlliteManager(this);
        sqlm.cleanTable("photos"); //TODO not clean and add, but only update
        Field[] drawables = pg.autyzm.przyjazneemocje.R.drawable.class.getFields();
        for (Field f : drawables) {
            try {
                String emotName = f.getName();
                int resID = getResources().getIdentifier(emotName, "drawable", getPackageName()); //zamiast resID po prostu od 0 iteracja
                if (emotName.contains("happy"))
                    sqlm.addPhoto(resID, "happy");
                else if (emotName.contains("angry"))
                    sqlm.addPhoto(resID, "angry");
                else if (emotName.contains("surprised"))
                    sqlm.addPhoto(resID, "surprised");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = getIntent().getExtras();
        choosenEmotion = bundle.getString("SpinnerValue");

        textView = (TextView) findViewById(R.id.TextViewChoose);
        textView.setText(choosenEmotion + " wybrano: 0");

        Cursor cursor = sqlm.givePhotosWithEmotion(choosenEmotion);
        int n = cursor.getCount();
        tabPhotos = new RowBean[n];
        while (cursor.moveToNext()) {
            tabPhotos[--n] = (new RowBean(cursor.getInt(1), false));
        }

        RowAdapter adapter = new RowAdapter(this, R.layout.item, tabPhotos);
        listView = (ListView) findViewById(R.id.image_list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            int pos = listView.getPositionForView(buttonView);
            if (pos != ListView.INVALID_POSITION) {
                if (isChecked) {
                    tabPhotos[pos].setSelected(true);
                } else {
                    tabPhotos[pos].setSelected(false);
                }
            }
            int numberOfPhotos = 0;
            for (RowBean el : tabPhotos) {
                if (el.selected) {
                    numberOfPhotos++;
                }
            }
            textView.setText(choosenEmotion + " wybrano: " + numberOfPhotos);
        } catch ( Exception e ){
            e.printStackTrace();
        }

    }
}
