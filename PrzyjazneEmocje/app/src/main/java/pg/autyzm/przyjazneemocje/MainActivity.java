package pg.autyzm.przyjazneemocje;

import android.database.Cursor;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textPhotos = (TextView) findViewById(R.id.photos);

        SqlliteManager sqlm = new SqlliteManager(this);
        sqlm.addPhoto("D:nagiefotki:asia.png", "gniew");
        sqlm.addPhoto("D:nagiefotki:pawel.png", "smutek");
        sqlm.addPhoto("D:nagiefotki:ania.png", "radosc");

        Cursor cursor = sqlm.givePhotosWithEmotion("gniew");
        while(cursor.moveToNext())
        {
            String path = cursor.getString(1);
            textPhotos.setText(textPhotos.getText() + "\n" + path);
        }



    }


}
