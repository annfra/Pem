package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.MediaStore.Images.Media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pg.autyzm.przyjazneemocje.lib.Level;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;


public class MainActivity extends Activity implements View.OnClickListener {

    List<Level> levels = new ArrayList<>();
    private int currentLevelNumber;
    private int currentSubLevelNumber;


    int amountOfSublevelsLeft;
    // for example [1, 2, 2, 1] - sublevel when you have to guess emotion with id = 1, sublevel [...] id = 2 etc.
    List<Integer> sublevelsList;

    List<String> photosWithEmotionSelected;
    List<String> photosWithRestOfEmotions;
    List<String> photosToUseInSublevel;
    String goodAnswer;
    SqlliteManager sqlm;
    int wrongAnswers;
    int rightAnswers;
    int wrongAnswersSublevel;
    int rightAnswersSublevel;
    int timeout;
    int timeoutSubLevel;
    String commandText;
    boolean animationEnds = true;
    Level currentLevel;
    CountDownTimer timer;
    public Speaker speaker;

    private final int REQUEST_CODE_SUBLEVEL_END = 1;
    private final int REQUEST_CODE_GAME_END = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        sqlm = getInstance(this);
        loadLevelsFromDatabase();


        if(levels.size() != 0){
            getNextSublevel();
        }else {
            startEndActivity(true);
        }
        //JG

        speaker = Speaker.getInstance(MainActivity.this);

        final ImageButton speakerButton = (ImageButton) findViewById(R.id.matchEmotionsSpeakerButton);
        speakerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                speaker.speak(commandText);
            }
        });



    }

    void loadLevelsFromDatabase(){

        Cursor cursorLevels = sqlm.giveAllLevels();

        while(cursorLevels.moveToNext()){

            int levelId = cursorLevels.getInt(cursorLevels.getColumnIndex("id"));
            Cursor cursorLevelTemp = sqlm.giveLevel(levelId);
            Cursor cursorPhotos = sqlm.givePhotosInLevel(levelId);
            Cursor cursorEmotions = sqlm.giveEmotionsInLevel(levelId);

            Level newLevel = new Level(cursorLevelTemp, cursorPhotos, cursorEmotions);

            if(newLevel.isLevelActive())
                levels.add(newLevel);
        }

    }

    void prepareLevel(){

        currentLevel = levels.get(currentLevelNumber);


        wrongAnswersSublevel = 0;
        rightAnswersSublevel = 0;
        timeoutSubLevel = 0;

        // tworzymy tablice do permutowania

        amountOfSublevelsLeft = currentLevel.getEmotions().size() * currentLevel.getSublevels();

        sublevelsList = new ArrayList<Integer>();

        for(int i = 0; i < currentLevel.getEmotions().size(); i++){

            for(int j = 0; j < currentLevel.getSublevels(); j++){

                sublevelsList.add(currentLevel.getEmotions().get(i));

            }

        }

        java.util.Collections.shuffle(sublevelsList);

    }





    void generateSublevel(int emotionIndexInList){


        Cursor emotionCur = sqlm.giveEmotionName(emotionIndexInList);

        emotionCur.moveToFirst();
        String selectedEmotionName = emotionCur.getString(emotionCur.getColumnIndex("emotion"));
        // po kolei czytaj nazwy emocji wybranych zdjec, jesli ich emocja = wybranej emocji, idzie do listy a, jesli nie, lista b

        photosWithEmotionSelected = new ArrayList<String>();
        photosWithRestOfEmotions = new ArrayList<String>();
        photosToUseInSublevel = new ArrayList<String>();



        for(int e : currentLevel.getPhotosOrVideosList()){

            //System.out.println("Id zdjecia: " + e);
            Cursor curEmotion = sqlm.givePhotoWithId(e);



            curEmotion.moveToFirst();
            String photoEmotionName = curEmotion.getString(curEmotion.getColumnIndex("emotion"));
            String photoName = curEmotion.getString(curEmotion.getColumnIndex("name"));



            if(photoEmotionName.equals(selectedEmotionName)){
                photosWithEmotionSelected.add(photoName);
            }
            else{
                photosWithRestOfEmotions.add(photoName);

            }

        }

        // z listy a wybieramy jedno zdjecie, ktore bedzie prawidlowa odpowiedzia

        goodAnswer = selectPhotoWithSelectedEmotion();

        // z listy b wybieramy zdjecia nieprawidlowe

        selectPhotoWithNotSelectedEmotions(currentLevel.getPhotosOrVideosPerLevel());

        // laczymy dobra odpowiedz z reszta wybranych zdjec i przekazujemy to dalej

        photosToUseInSublevel.add(goodAnswer);

        java.util.Collections.shuffle(photosToUseInSublevel);


        // z tego co rozumiem w photosList powinny byc name wszystkich zdjec, jakie maja sie pojawic w lvl (czyli - 3 pozycje)

        StartTimer(currentLevel);

    }

    void displaySublevel(List<String> photosList){


        TextView txt = (TextView) findViewById(R.id.rightEmotion);
        // txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
        String rightEm = goodAnswer.replace(".jpg","").replaceAll("[0-9.]", "");
        String rightEmotionLang = getResources().getString(getResources().getIdentifier("emotion_" + rightEm, "string", getPackageName()));
        commandText = getResources().getString(R.string.label_show_emotion) + " " + rightEmotionLang;
        txt.setText(commandText);

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.imageGallery);

        linearLayout1.removeAllViews();
        int listSize=photosList.size();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 790/listSize, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50-(150/listSize), getResources().getDisplayMetrics());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(height, height);
        lp.setMargins(45/listSize, 10, 45/listSize, margin);
        lp.gravity = Gravity.CENTER;
        for(String photoName:photosList)
        {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
            File fileOut = new File(root + "Emotions" + File.separator + photoName);
            try {

                ImageView image = new ImageView(MainActivity.this);
                image.setLayoutParams(lp);

                if(photoName.equals(goodAnswer)){
                    image.setId(1);
                }
                else{
                    image.setId(0);
                }


                image.setOnClickListener(this);
                Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(fileOut));
                image.setImageBitmap(captureBmp);
                linearLayout1.addView(image);
            }
            catch(IOException e) {
                System.out.println("IO Exception " + photoName);
            }
        }
    }


    public void onClick(View v) {


        if(v.getId() == 1) {
            animationEnds = false;
            amountOfSublevelsLeft--;
            rightAnswers++;
            rightAnswersSublevel++;
            timer.cancel();

            boolean correctness = true;

            if(amountOfSublevelsLeft == 0) {
                correctness = checkCorrectness();
            }

            if(correctness) {
                Intent i = new Intent(this, AnimationActivity.class);
                startActivityForResult(i, REQUEST_CODE_SUBLEVEL_END);

            }
            else{
                startEndActivity(false);


            }
        }
        else //jesli nie wybrano wlasciwej
        {
            wrongAnswers++;
            wrongAnswersSublevel++;
        }




    }

    boolean checkCorrectness(){



        if(wrongAnswersSublevel > currentLevel.getCorrectness()){

            return false;

        }


        return true;

    }


    String selectPhotoWithSelectedEmotion(){

        Random rand = new Random();

        int photoWithSelectedEmotionIndex = rand.nextInt(photosWithEmotionSelected.size());

        String name = photosWithEmotionSelected.get(photoWithSelectedEmotionIndex);

        return name;
    }

    void selectPhotoWithNotSelectedEmotions(int howMany){

        Random rand = new Random();

        for(int i = 0; i < howMany - 1; i++) {

            if(photosWithRestOfEmotions.size() > 0){
                int photoWithSelectedEmotionIndex = rand.nextInt(photosWithRestOfEmotions.size());
                String name = photosWithRestOfEmotions.get(photoWithSelectedEmotionIndex);

                photosToUseInSublevel.add(name);
                photosWithRestOfEmotions.remove(name);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        animationEnds = true;

        if(currentLevelNumber == levels.size() - 1) {
            startEndActivity(true);
        }

        getNextSublevel();
    }

    private void startEndActivity(boolean pass){
        Intent in = new Intent(this, EndActivity.class);
        in.putExtra("PASS", pass);
        in.putExtra("WRONG", wrongAnswers);
        in.putExtra("RIGHT", rightAnswers);
        in.putExtra("TIMEOUT", timeout);


        startActivityForResult(in, REQUEST_CODE_GAME_END);

        //startActivity(in);
    }

    private void StartTimer(Level l)
    {
        //timer! seconds * 1000
        if(l.getTimeLimit() != 0)
        {
            timer = new CountDownTimer(l.getTimeLimit() * 1000, 1000) {

                public void onTick(long millisUntilFinished) {


                }
                public void onFinish() {
                    LinearLayout imagesLinear = (LinearLayout)findViewById(R.id.imageGallery);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation((float)0.1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                    final int childcount = imagesLinear.getChildCount();
                    for (int i = 0; i < childcount; i++)
                    {
                        ImageView image = (ImageView) imagesLinear.getChildAt(i);
                        if(image.getId() != 1)
                        {
                            image.setColorFilter(filter);
                        }
                        else
                        {
                            image.setPadding(40,40,40,40);
                            image.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        }

                    }
                    timeout ++;
                    timeoutSubLevel++;
                }
            }.start();

        }
    }

    private void getNextSublevel(){

        // when starting or restarting game, reset counters
        if(currentLevelNumber == levels.size() || currentLevelNumber == -1) {
            currentLevelNumber = -1;
            wrongAnswers = 0;
            rightAnswers = 0;
            timeout = 0;
        }

        if(amountOfSublevelsLeft == 0) {
            currentLevelNumber++;
            prepareLevel();
        }

        generateSublevel(sublevelsList.get(amountOfSublevelsLeft - 1));
        displaySublevel(photosToUseInSublevel);


    }

}
