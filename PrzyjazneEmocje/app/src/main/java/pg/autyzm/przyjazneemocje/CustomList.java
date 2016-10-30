package pg.autyzm.przyjazneemocje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CustomList extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    public SqlliteManager sqlm;


    public CustomList(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        sqlm = new SqlliteManager(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_single, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);
        Button activeBtn = (Button)view.findViewById(R.id.active_btn);


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                sqlm.delete("levels", "id", String.valueOf(findLevelId(position)));
                sqlm.delete("levels_photos", "levelid", String.valueOf(findLevelId(position)));
                list.remove(position); //or some other task


                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something


                Intent intent = new Intent(context, LevelConfiguration.class);
                //intent.putExtra(EXTRA_MESSAGE, findLevelId(position));

                Bundle b = new Bundle();
                b.putInt("key", findLevelId(position)); //Your id

                System.out.println("przeslij " + findLevelId(position));

                intent.putExtras(b);

                context.startActivity(intent);



                notifyDataSetChanged();
            }
        });

        activeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                /*

                    1. znajdz id poziomu
                    2. pobierz poziom z bazy
                    3. edytuj go
                    4. zapisz go do bazy

                    */

                Cursor cur = sqlm.giveLevel(findLevelId(position));

                //

                Level l = new Level(cur, null);


                l.isLevelActive = ! l.isLevelActive;


                //

                sqlm.addLevel(l);

                notifyDataSetChanged();
            }
        });


        return view;
    }


    int findLevelId(int position){

        String levelString = list.get(position);


        String[] splittedLevelString = levelString.split(" ");
        int levelId = Integer.parseInt(splittedLevelString[1]);


        return levelId;

    }

}