package pg.autyzm.przyjazneemocje.chooseImages;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannedString;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pg.autyzm.przyjazneemocje.R;
import pg.autyzm.przyjazneemocje.SqlliteManager;


/**
 * Created by Joanna on 2016-10-08.
 */

public class ChooseImages extends Activity {

    private ListView list ;
    private ArrayAdapter<ImageView> adapter ;

    private ListView listView1;
    int a;

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
                int resID = getResources().getIdentifier(emotName , "drawable", getPackageName()); //zamiast resID po prostu od 0 iteracja
                if(emotName.contains("happy"))
                    sqlm.addPhoto(resID,"happy");
                else if(emotName.contains("angry"))
                    sqlm.addPhoto(resID,"angry");
                else if(emotName.contains("surprised"))
                    sqlm.addPhoto(resID,"surprised");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<RowBean> elem = new ArrayList<RowBean>();

        Bundle bundle = getIntent().getExtras();
        String choosenEmotion = bundle.getString("SpinnerValue");

        Cursor cursor = sqlm.givePhotosWithEmotion(choosenEmotion);

        TextView tv = (TextView)findViewById(R.id.TextViewChoose);
        //  tv.setText(sss);//choosenEmotion);

        int n = cursor.getCount();
        RowBean[] tab = new RowBean[n];
        while(cursor.moveToNext()) {
            tab[--n]=(new RowBean(cursor.getInt(1),cursor.getString(1)));
        }

        RowAdapter adapter = new RowAdapter(this,
                R.layout.item, tab);

        listView1 = (ListView)findViewById(R.id.Lista);

        listView1.setAdapter(adapter);
    }
}
