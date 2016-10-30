package pg.autyzm.przyjazneemocje;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import pg.autyzm.przyjazneemocje.chooseImages.ChooseImages;

public class LevelConfiguration extends AppCompatActivity implements View.OnClickListener{


    List<String> emotionsList = new ArrayList<String>();
    public SqlliteManager sqlm;
    int editedLevelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_configuration);

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("key");
        System.out.println(value + " to zostalo przekazane do konfiguracji");





        TextView textPhotos = (TextView) findViewById(R.id.photos);

        //this.deleteDatabase("friendly_emotions.db");

        sqlm = new SqlliteManager(this);
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
        // sqlm.addEmotion("sad");
        sqlm.addEmotion("happy");
        sqlm.addEmotion("angry");
        // sqlm.addEmotion("scared");
        sqlm.addEmotion("surprised");



        Cursor cur = sqlm.giveAllEmotions();
        while(cur.moveToNext())
        {
            String emotionId = "emotion" + cur.getInt(0);
            int resID = getResources().getIdentifier(emotionId, "id", getPackageName());
            if(resID == 0) break;

            CheckBox checkBox = (CheckBox)findViewById(resID);
            checkBox.setText(cur.getString(1));
            checkBox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        }


        //Wywolanie kamery - nie dziala na emulatorze
        /*final Camera camera = CameraOptions.getCameraInstance();
        final CameraPreview cameraPreview = new CameraPreview(this, camera);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        Button captureButton = (Button) findViewById(R.id.cameraButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null,  CameraOptions.getCameraPhoto());
            }
        });*/

        View buttonChoose = findViewById(R.id.button_choose_images);
        buttonChoose.setOnClickListener(this);


        //updateLevelList();

        // jesli zostal przechwycony jakis id, to znaczy ze jestemy w trybie edycji poziomu, a nie jego tworzenia
        // ladujemy do interfejsu wartosci z rekordu tabeli Level
        if(value > 0){

            editedLevelId = value;

            Cursor cur2 = sqlm.giveLevel(editedLevelId);
            Cursor cur3 = sqlm.giveEmotionsInLevel(editedLevelId);

            Level l = new Level(cur2, cur3);

            EditText timeLimit = (EditText)findViewById(R.id.time_limit);
            EditText vpPerLevel = (EditText)findViewById(R.id.pv_per_level);
            Spinner photosOrVideos = (Spinner)findViewById(R.id.spinner2);



            timeLimit.setText(Integer.toString(l.timeLimit));
            vpPerLevel.setText(Integer.toString(l.pvPerLevel));


            if(l.photosOrVideos.equals("Videos")){
                photosOrVideos.setSelection(0);
            }
            else{
                photosOrVideos.setSelection(1);
            }

            // Do zrobienia: zaladowac z Level.photosOrVideosList zdjecia do interfejsu

        }


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


    public void updateLevelList(){


        Cursor cur = sqlm.giveAllLevels();

        ArrayAdapter<String> adapter;
        Spinner spinner = (Spinner)findViewById(R.id.spinner3);
        List<String> list;
        list = new ArrayList<String>();

        while(cur.moveToNext())
        {
            String levelId = "Level " + cur.getInt(0);
            list.add(levelId);
            System.out.println("Dodano nowy element do listy");

        }

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        System.out.println("Item list updated");

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

        Level l = new Level();

        Spinner spinner = (Spinner)findViewById(R.id.spinner2);

        l.photosOrVideos = spinner.getSelectedItem().toString();

        //System.out.println(l.photosOrVideos);


        EditText timeLimit = (EditText)findViewById(R.id.time_limit);
        EditText vpPerLevel = (EditText)findViewById(R.id.pv_per_level);

        l.timeLimit = Integer.parseInt(timeLimit.getText() + "");
        l.pvPerLevel = Integer.parseInt(vpPerLevel.getText() + "");

        if(editedLevelId > 0){
            l.id = editedLevelId;
        }


        /*

            Do zrobienia: tutaj trzeba zrobić rozpoznanie, które zdjęcia zostały zaznaczone w interfejsie, a następnie
             ich podpiecie do pola photosOrVideosList w obiektcie Level, by potem zapisac to do bazy

         */



        sqlm.addLevel(l);

        updateLevelList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_choose_images:
                Intent i = new Intent(this,ChooseImages.class);

                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                Bundle bundle = new Bundle();
                bundle.putString("SpinnerValue",spinner.getSelectedItem().toString());
                i.putExtras(bundle);


                startActivity(i);
                break;
        }
    }

}
