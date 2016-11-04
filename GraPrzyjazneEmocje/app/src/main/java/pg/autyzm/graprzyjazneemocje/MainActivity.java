package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.Gravity;
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


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        TextView txt = (TextView)findViewById(R.id.txt);

        SqlliteManager sqlm = new SqlliteManager(this,"przyjazneemocje");

        sqlm.getReadableDatabase();

        int lvl = 0;
        int photosPerLvL = 3;

        List<String> photosList = new ArrayList<String>();
        Cursor cur = sqlm.givePhotosWithEmotion("happy");
        photosList = giveRandomPhotosFromTable(cur, photosPerLvL);

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.imageGallery);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(350, 350);
        lp.setMargins(0, 0, 30, 0);
        lp.gravity = Gravity.CENTER;
        for(String photoName:photosList)
        {
                String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
                File fileOut = new File(root + "Emotions" + File.separator + photoName +".jpg");
                try {

                    ImageView image = new ImageView(MainActivity.this);
                    image.setLayoutParams(lp);
                    Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(fileOut));
                    image.setImageBitmap(captureBmp);
                    linearLayout1.addView(image);
                }
                catch(IOException e) {
                    System.out.println("hohoh");
                }
        }
    }

    public List<String> giveRandomPhotosFromTable(Cursor cur, int howMany)
    {
        String[] tmp = new String[cur.getCount()];
        int i = 0;
        while(cur.moveToNext())
        {
            String name = cur.getString(3);
            tmp[i++] = name;
        }
        List<String> photosList = new ArrayList<String>();

        for(int j=0; j<howMany; j++)
        {
            Random rand = new Random();
            int k = rand.nextInt(i);
            photosList.add(tmp[k]);
        }

        return photosList;
    }
}
