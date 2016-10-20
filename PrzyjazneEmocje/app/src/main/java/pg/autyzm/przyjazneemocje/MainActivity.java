package pg.autyzm.przyjazneemocje;

import android.database.Cursor;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> emotionsList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textPhotos = (TextView) findViewById(R.id.photos);

        this.deleteDatabase("friendly_emotions.db");

        SqlliteManager sqlm = new SqlliteManager(this);
        /*sqlm.addPhoto("D:nagiefotki:asia.png", "gniew");
        sqlm.addPhoto("D:nagiefotki:pawel.png", "smutek");
        sqlm.addPhoto("D:nagiefotki:ania.png", "radosc");


        //for(int i=0; i<9; i++)
        //    sqlm.delete("photos",i);

        Cursor cursor = sqlm.givePhotosWithEmotion("gniew");
        while(cursor.moveToNext())
        {
            String path = cursor.getString(1);
            textPhotos.setText(textPhotos.getText() + "\n" + path);
        }*/

        //na sztywno dodajemy?
        sqlm.addEmotion("sad");
        sqlm.addEmotion("happy");
        sqlm.addEmotion("angry");

        Cursor cur = sqlm.giveAllEmotions();
        while(cur.moveToNext())
        {
            String emotionId = "emotion" + cur.getInt(0);
            int resID = getResources().getIdentifier(emotionId, "id", getPackageName());
            CheckBox checkBox = (CheckBox)findViewById(resID);
            checkBox.setText(cur.getString(1));
            checkBox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        }


        final Camera camera = CameraOptions.getCameraInstance();
        Button captureButton = (Button) findViewById(R.id.cameraButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, CameraOptions.getCameraPhoto());
            }
        });

    }
    //jako glowna emocja pokazuje sie stara (mimo usuniecia), az do ponownego wyboru
    //trzeba cos zmienic w spinnerze?
    class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
        {
            if(isChecked)
                emotionsList.add(buttonView.getText().toString());
            if(!isChecked)
                emotionsList.remove(buttonView.getText().toString());

            Spinner spinner = (Spinner)findViewById(R.id.spinner);
            updateEmotionsList(spinner, emotionsList);
        }
    }

    public static void updateEmotionsList(Spinner spinner, List<String> emotionsList)
    {
        if (emotionsList.size() != 0)
        {
            SpinnerAdapter oldAdapter = spinner.getAdapter();
            boolean changed = true;
            if (oldAdapter != null && oldAdapter.getCount() == emotionsList.size())
            {
                changed = false;
                for (int i = 0; i < emotionsList.size(); i++)
                {
                    if (!emotionsList.get(i).equals(oldAdapter.getItem(i))) {
                        changed = true;
                        break;
                    }
                }
            }
            if (changed)
            {

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, emotionsList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }
        else
        {
            spinner.setAdapter(null);
        }
    }


    public void addLevel(View view) {
        System.out.println("addLevel");






    }

}
