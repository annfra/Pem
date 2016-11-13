package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.AppCompatDelegate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener {


    List<String> photosListWithEmotionSelected;
    List<String> photosListWithRestOfEmotions;
    List<String> selectedPhotosListWithRestOfEmotions;
    String goodAnswer;
    Cursor cur0;
    SqlliteManager sqlm;
    int wrongAnswers;
    int rightAnswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);




        sqlm = new SqlliteManager(this, "przyjazneemocje");

        sqlm.getReadableDatabase();

        // Birgiel

        cur0 = sqlm.giveAllLevels();
        findNextActiveLevel();


    }


        boolean findNextActiveLevel(){



            while(cur0.moveToNext()){

                if(! loadLevel()){
                    continue;
                }
                else{
                    return true;
                }



            }

            return false;

        }





        boolean loadLevel(){

            System.out.println("---------------Nowy poziom-------------------");

            int levelId = 0;
            int photosPerLvL = 0;
            Level l = null;

            System.out.println(cur0.getCount());




            levelId = cur0.getInt(cur0.getColumnIndex("id"));

            Cursor cur2 = sqlm.giveLevel(levelId);
            Cursor cur3 = sqlm.givePhotosInLevel(levelId);
            Cursor cur4 = sqlm.giveEmotionsInLevel(levelId);

            l = new Level(cur2, cur3, cur4);

            photosPerLvL = l.pvPerLevel;


            if(! l.isLevelActive) return false;



            // nizej kod napisany 11.11.16


            // wylosuj emocje z wybranych emocji, odczytaj jej imie (bo mamy liste id)
            int emotionIndexInList = selectEmotionToChoose(l);
            //System.out.println("Wybrana emocja indeks " + emotionIndexInList);
            //System.out.println("Wybrana emocja id " + l.emotions.get(emotionIndexInList));
            Cursor emotionCur = sqlm.giveEmotionName(l.emotions.get(emotionIndexInList));

            emotionCur.moveToFirst();
            String selectedEmotionName = emotionCur.getString(emotionCur.getColumnIndex("emotion"));
            System.out.println("Wybrana emocja name " + selectedEmotionName);
            // po kolei czytaj nazwy emocji wybranych zdjec, jesli ich emocja = wybranej emocji, idzie do listy a, jesli nie, lista b

            photosListWithEmotionSelected = new ArrayList<String>();
            photosListWithRestOfEmotions = new ArrayList<String>();
            selectedPhotosListWithRestOfEmotions = new ArrayList<String>();



            for(int e : l.photosOrVideosList){

                System.out.println("Id zdjecia: " + e);
                Cursor curEmotion = sqlm.givePhotoWithId(e);



                curEmotion.moveToFirst();
                String photoEmotionName = curEmotion.getString(curEmotion.getColumnIndex("emotion"));
                String photoName = curEmotion.getString(curEmotion.getColumnIndex("name"));


                System.out.println(photoEmotionName + " " + selectedEmotionName);

                if(photoEmotionName.equals(selectedEmotionName)){
                    photosListWithEmotionSelected.add(photoName);
                }
                else{
                    photosListWithRestOfEmotions.add(photoName);
                    System.out.println("Dodano cos do reszty zdjec " + photosListWithRestOfEmotions.size());

                }

            }

            // z listy a wybieramy jedno zdjecie, ktore bedzie prawidlowa odpowiedzia

            goodAnswer = selectPhotoWithSelectedEmotion();

            // z listy b wybieramy zdjecia nieprawidlowe

            selectPhotoWithNotSelectedEmotions(l.pvPerLevel);

            // laczymy dobra odpowiedz z reszta wybranych zdjec i przekazujemy to dalej
            // do zrobienia - by nie zawsze poprawna odpowiedz byla na koncu

            selectedPhotosListWithRestOfEmotions.add(goodAnswer);
            List<String> photosList = selectedPhotosListWithRestOfEmotions;

            // z tego co rozumiem w photosList powinny byc name wszystkich zdjec, jakie maja sie pojawic w lvl (czyli - 3 pozycje)


            generateView(photosList);
            System.out.println("Wygenerowano view");


            return true;

        // /birgiel

    }

    void generateView(List<String> photosList){


        TextView txt = (TextView) findViewById(R.id.rightEmotion);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
        String rightEm = goodAnswer.replace(".jpg","").replaceAll("[0-9.]", "");
        String rightEmotionLang = getResources().getString(getResources().getIdentifier("emotion_" + rightEm, "string", getPackageName()));
        txt.setText(getResources().getString(R.string.label_show_emotion) + " " + rightEmotionLang);

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.imageGallery);

        linearLayout1.removeAllViews();


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(350, 350);
        lp.setMargins(0, 0, 30, 0);
        lp.gravity = Gravity.CENTER;
        for(String photoName:photosList)
        {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
            File fileOut = new File(root + "Emotions" + File.separator + photoName);
            System.out.println(root + "Emotions" + File.separator + photoName);
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


        //System.out.println(v.getId());


        if(v.getId() == 1) {
            Intent i = new Intent(this, AnimationActivity.class);
            startActivity(i);
            rightAnswers++;

            if(! findNextActiveLevel()){
                System.out.println("Skonczyly sie poziomy");
                Intent in = new Intent(this, EndActivity.class);
                in.putExtra("WRONG", wrongAnswers);
                in.putExtra("RIGHT", rightAnswers);
                startActivity(in);
            }


        }
        else //jesli nie wybrano wlasciwej
        {
            wrongAnswers++;
        }




    }

    int selectEmotionToChoose(Level l){

        Random rand = new Random();

        int emotionIndexInList = rand.nextInt(l.emotions.size());

        return emotionIndexInList;
    }

    String selectPhotoWithSelectedEmotion(){

        Random rand = new Random();

        int photoWithSelectedEmotionIndex = rand.nextInt(photosListWithEmotionSelected.size());

        String name = photosListWithEmotionSelected.get(photoWithSelectedEmotionIndex);

        return name;
    }

    void selectPhotoWithNotSelectedEmotions(int howMany){

        for(int i = 0; i < howMany - 1; i++) {

            Random rand = new Random();

            int photoWithSelectedEmotionIndex = rand.nextInt(photosListWithRestOfEmotions.size());

            String name = photosListWithRestOfEmotions.get(photoWithSelectedEmotionIndex);

            selectedPhotosListWithRestOfEmotions.add(name);
            photosListWithRestOfEmotions.remove(name);

        }
    }
}
