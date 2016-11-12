package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Ann on 12.11.2016.
 */
public class EndActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);


        TextView txt = (TextView) findViewById(R.id.endTextMain);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
        txt.setTextColor(Color.parseColor("#00802b"));
        txt.setText("Gratulacje!");

        Bundle extras = getIntent().getExtras();
        int wrongAnswers = extras.getInt("WRONG");
        int rightAnswers = extras.getInt("RIGHT");
        TextView right = (TextView) findViewById(R.id.rightAnswers);
        right.setTextSize(TypedValue.COMPLEX_UNIT_PX,50);
        right.setText("Poprawne odpowiedzi: " + rightAnswers);
        TextView wrong = (TextView) findViewById(R.id.wrongAnswers);
        wrong.setTextSize(TypedValue.COMPLEX_UNIT_PX,50);
        wrong.setText("Niepoprawne odpowiedzi: " + wrongAnswers);




    }

}
