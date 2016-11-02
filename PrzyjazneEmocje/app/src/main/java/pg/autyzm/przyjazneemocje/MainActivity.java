package pg.autyzm.przyjazneemocje;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends Activity {

    public SqlliteManager sqlm;
    ArrayList<String> list;
    ArrayList<Boolean> active_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlm = new SqlliteManager(this);


        updateLevelList();
        //generate list



    }

    // napisuje, bo chce, by po dodaniu poziomu lista poziomow w main activity automatycznie sie odswiezala - Pawel
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here

        updateLevelList();


    }


    public void sendMessage(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, LevelConfiguration.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "String";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void updateLevelList(){


        Cursor cur = sqlm.giveAllLevels();
        list = new ArrayList<String>();
        active_list = new ArrayList<Boolean>();

        while(cur.moveToNext())
        {
            String levelId = "Level " + cur.getInt(0);

            int active = cur.getInt(cur.getColumnIndex("is_level_active"));
            boolean isLevelActive = (active != 0);
            active_list.add(isLevelActive);


            list.add(levelId);
            System.out.println("Dodano nowy element do listy");

        }

        //instantiate custom adapter
        CustomList adapter = new CustomList(list, active_list, this);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(adapter);



        System.out.println("Item list updated");

    }
}