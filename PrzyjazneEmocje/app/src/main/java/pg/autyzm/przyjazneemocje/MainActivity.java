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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlm = new SqlliteManager(this);


        updateLevelList();
        //generate list


        //instantiate custom adapter
        CustomList adapter = new CustomList(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(adapter);
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

        while(cur.moveToNext())
        {
            String levelId = "Level " + cur.getInt(0);
            list.add(levelId);
            System.out.println("Dodano nowy element do listy");

        }


        System.out.println("Item list updated");

    }
}