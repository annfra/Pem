package pg.autyzm.przyjazneemocje;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Handler;
import android.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pg.autyzm.przyjazneemocje.chooseImages.ChooseImages;

public class LevelConfiguration extends AppCompatActivity implements View.OnClickListener{


    List<String> emotionsList = new ArrayList<String>();
    List<Integer> emotionsIdsList = new ArrayList<Integer>();
    public SqlliteManager sqlm;
    int editedLevelId;
    Level l;

    ArrayList<Integer> photosOrVideosList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_configuration);

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("key");
        System.out.println(value + " to zostalo przekazane do konfiguracji");


       // TextView textPhotos = (TextView) findViewById(R.id.photos);

        //this.deleteDatabase("friendly_emotions.db");

        sqlm = new SqlliteManager(this,"przyjazneemocje");

        Map<String, String> mapEmo = new ArrayMap<>();
        mapEmo.put("happy",getResources().getString(R.string.emotion_happy));
        mapEmo.put("sad",getResources().getString(R.string.emotion_sad));
        System.out.println("Dodano emocje");
        mapEmo.put("angry",getResources().getString(R.string.emotion_angry));
        mapEmo.put("scared",getResources().getString(R.string.emotion_scared));
        mapEmo.put("surprised",getResources().getString(R.string.emotion_surprised));
        mapEmo.put("bored",getResources().getString(R.string.emotion_bored));


        Cursor cur = sqlm.giveAllEmotions();
        while(cur.moveToNext())
        {

            String emotionId = "emotion" + cur.getInt(0);

            int resID = getResources().getIdentifier(emotionId, "id", getPackageName());
            if(resID == 0) break;

            CheckBox checkBox = (CheckBox)findViewById(resID);
            checkBox.setText(mapEmo.get(cur.getString(1)));
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

        ImageButton buttonCamera = (ImageButton) findViewById(R.id.button_take_photo);
        buttonCamera.setOnClickListener(this);

        //updateLevelList();

        // jesli zostal przechwycony jakis id, to znaczy ze jestemy w trybie edycji poziomu, a nie jego tworzenia
        // ladujemy do interfejsu wartosci z rekordu tabeli Level
        if(value > 0){

            loadLevel(value);

        }


    }


    void loadLevel(int value){


        editedLevelId = value;

        Cursor cur2 = sqlm.giveLevel(editedLevelId);
        Cursor cur3 = sqlm.givePhotosInLevel(editedLevelId);
        Cursor cur4 = sqlm.giveEmotionsInLevel(editedLevelId);

        l = new Level(cur2, cur3, cur4);

        EditText timeLimit = (EditText)findViewById(R.id.time_limit);
        EditText vpPerLevel = (EditText)findViewById(R.id.pv_per_level);
        EditText correctness = (EditText)findViewById(R.id.correctness);
        EditText sublevels = (EditText)findViewById(R.id.sublevels);
        EditText levelName  = (EditText)findViewById(R.id.level_name);
//        Spinner photosOrVideos = (Spinner)findViewById(R.id.spinner2);
//        TextView tv = (TextView) findViewById(R.id.imagesCount);



        timeLimit.setText(Integer.toString(l.timeLimit));
        vpPerLevel.setText(Integer.toString(l.pvPerLevel));
        correctness.setText(Integer.toString(l.correctness));
        sublevels.setText(Integer.toString(l.sublevels));



        for(Integer i : l.photosOrVideosList){

            photosOrVideosList.add(i);

        }


        levelName.setText(l.name);
//        String str = getResources().getString(R.string.select);
//        tv.setText(str + ": " + l.photosOrVideosList.size());

        updateListSize();

//        if(l.photosOrVideos.equals("Videos")){
//            photosOrVideos.setSelection(1);
//        }
//        else{
//            photosOrVideos.setSelection(0);
//        }


        for(int i : l.emotions){

            int id = 0;

            switch (i) {
                case 1:
                    id = R.id.emotion1;
                    break;
                case 2:
                    id = R.id.emotion2;
                    break;
                case 3:
                    id = R.id.emotion3;
                    break;
                case 4:
                    id = R.id.emotion4;
                    break;
                case 5:
                    id = R.id.emotion5;
                    break;
                case 6:
                    id = R.id.emotion6;
                    break;
            }

            CheckBox checkBox = (CheckBox)findViewById(id);
            checkBox.setChecked(true);

        }



    }





    //jako glowna emocja pokazuje sie stara (mimo usuniecia), az do ponownego wyboru
    //trzeba cos zmienic w spinnerze?
    class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener
    {


        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
        {

            String emotionNameInLanguage = buttonView.getText().toString();

            Map<String, String> mapEmo = new android.util.ArrayMap<>();
            mapEmo.put(getResources().getString(R.string.emotion_happy),"happy");
            mapEmo.put(getResources().getString(R.string.emotion_sad),"sad");
            mapEmo.put(getResources().getString(R.string.emotion_angry),"angry");
            mapEmo.put(getResources().getString(R.string.emotion_scared),"scared");
            mapEmo.put(getResources().getString(R.string.emotion_surprised),"surprised");
            mapEmo.put(getResources().getString(R.string.emotion_bored),"bored");

            String emotionName = mapEmo.get(emotionNameInLanguage);


            Cursor cc = sqlm.giveEmotionId(emotionName);
            int emotionId = -1;

            while(cc.moveToNext()){
                emotionId = cc.getInt(cc.getColumnIndex("id"));
            }

            if(isChecked) {
                emotionsList.add(emotionNameInLanguage);
                System.out.println(">>>" + emotionId);
                emotionsIdsList.add(emotionId);
            }
            if(!isChecked) {
                emotionsList.remove(emotionNameInLanguage);
                System.out.println(">>>" + emotionId);
                emotionsIdsList.remove((Object)emotionId);
                removeImgEmo(emotionName);
            }

            Spinner spinner = (Spinner)findViewById(R.id.spinner);
            updateEmotionsList(spinner, emotionsList);
            updateListSize();
        }
    }


    private void removeImgEmo(String emotion) {
        Cursor cursor = sqlm.givePhotosWithEmotion(emotion);
        while(cursor.moveToNext()) {
            if (photosOrVideosList.contains(cursor.getInt(0))) {
                photosOrVideosList.remove((Object) cursor.getInt(0));
            }
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



        Level l = new Level();

//        Spinner spinner = (Spinner)findViewById(R.id.spinner2);

//        l.photosOrVideos = spinner.getSelectedItem().toString();

        //System.out.println(l.photosOrVideos);


        EditText timeLimit = (EditText)findViewById(R.id.time_limit);
        EditText correctness = (EditText)findViewById(R.id.correctness);
        EditText sublevels = (EditText)findViewById(R.id.sublevels);
        EditText levelName = (EditText)findViewById(R.id.level_name);
        EditText vpPerLevel = (EditText)findViewById(R.id.pv_per_level);

        l.timeLimit = Integer.parseInt(timeLimit.getText() + "");
        l.pvPerLevel = Integer.parseInt(vpPerLevel.getText() + "");
        l.correctness = Integer.parseInt(correctness.getText() + "");
        l.sublevels = Integer.parseInt(sublevels.getText() + "");

        l.name = levelName.getText().toString();

        if(editedLevelId > 0){
            l.id = editedLevelId;


            // po przekazaniu informacji, ze mamy juz jakies id (czyli jest to edycja i jakis rekord ma byc nadpisany), zerujemy id, na wypadek,
            // gdyby user jeszcze raz wlaczyl zapisz - wtedy z braku id-ka uzna, ze to tworzenie nowego poziomu i stworzy nowy rekord
            editedLevelId = 0;
        }


        l.photosOrVideosList = photosOrVideosList;

        // przerabiamy te dlugie id na krotkie

//        for(Integer photoPath : photosOrVideosList){
//
//            Cursor pp = sqlm.givePhotoWithPath(Integer.toString(photoPath));
//            pp.moveToFirst();
//            int realId = pp.getInt(pp.getColumnIndex("id"));
//            l.photosOrVideosList.add(realId);
//
//        }





        l.emotions = emotionsIdsList;



        sqlm.addLevel(l);

        final TextView msg = (TextView)findViewById(R.id.saveMessage);
        msg.setVisibility(View.VISIBLE);
        msg.postDelayed(new Runnable() {
            public void run() {
                msg.setVisibility(View.INVISIBLE);
            }
        }, 2000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_choose_images:
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                Bundle bundle = new Bundle();
                bundle.putString("SpinnerValue", spinner.getSelectedItem().toString());      //TODO gdy mamy zaznaczone wszystkie i jedno się odznaczy to przesyła ostatnio zaznaczoną emocję

                // Birgiel. Dodanie emocji wybranych, gdy lvl byl tworzony

                // przerabiamy krotkie id na dlugie (path)

//                ArrayList<Integer> list = new ArrayList<Integer>();

//                if (l != null){
//
//                    for (Integer photoPath : l.photosOrVideosList) {
//
//                        Cursor pp = sqlm.givePhotoWithId(photoPath);
//                        pp.moveToFirst();
//                        int path = pp.getInt(pp.getColumnIndex("path"));
//                        list.add(path);
//
//                    }
//                }
//
                if(l != null) {
                    bundle.putIntegerArrayList("selected_photos", ( ArrayList<Integer>)l.photosOrVideosList);
                }
                //
                else {
                    bundle.putIntegerArrayList("selected_photos", photosOrVideosList);
                }

                Intent i = new Intent(this,ChooseImages.class);
                i.putExtras(bundle);
                startActivityForResult(i,1);
                break;
            case R.id.button_take_photo:
                Spinner spinner2 = (Spinner) findViewById(R.id.spinner);
                Bundle bundle2 = new Bundle();
                bundle2.putString("SpinnerValue", spinner2.getSelectedItem().toString());
                Intent in = new Intent(this, CameraActivity.class);
                in.putExtras(bundle2);
                startActivityForResult(in,1);
                break;
        }
    }

    private void updateListSize(){
        TextView tv = (TextView) findViewById(R.id.imagesCount);
        String str = getResources().getString(R.string.select);
        tv.setText(str + ": " + photosOrVideosList.size());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String choosenImg="";
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                photosOrVideosList = bundle.getIntegerArrayList("selected_photos");
                //photosOrVideosList.addAll(bundle.getIntegerArrayList("selected_photos"));
                updateListSize();
            }
        }

    }
    public void closee(View view) {
        finish();
    }
}
