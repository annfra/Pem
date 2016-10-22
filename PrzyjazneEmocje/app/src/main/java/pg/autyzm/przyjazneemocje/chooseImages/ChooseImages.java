package pg.autyzm.przyjazneemocje.chooseImages;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannedString;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
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

        //TODO
        sqlm.addPhoto(R.drawable.happy1,"happy");
        sqlm.addPhoto(R.drawable.happy2,"happy");
        sqlm.addPhoto(R.drawable.happy3,"happy");
        sqlm.addPhoto(R.drawable.happy4,"happy");
        sqlm.addPhoto(R.drawable.happy5,"happy");
        sqlm.addPhoto(R.drawable.happy6,"happy");
        sqlm.addPhoto(R.drawable.happy7,"happy");
        sqlm.addPhoto(R.drawable.angry,"angry");
        sqlm.addPhoto(R.drawable.angry1,"angry");
        sqlm.addPhoto(R.drawable.angry2,"angry");
        sqlm.addPhoto(R.drawable.scared1,"scared");
        sqlm.addPhoto(R.drawable.surprised1,"surprised");
        sqlm.addPhoto(R.drawable.surprised2,"surprised");
        sqlm.addPhoto(R.drawable.surprised3,"surprised");

        List<RowBean> elem = new ArrayList<RowBean>();
        Spinner ss = (Spinner)findViewById(R.id.spinner);
      //  String sss = ss.getSelectedItem().toString();

        String choosenEmotion = "happy";
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
