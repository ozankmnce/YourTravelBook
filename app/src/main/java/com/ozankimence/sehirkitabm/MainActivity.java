package com.ozankimence.sehirkitabm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView l1;
    static Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l1 = findViewById(R.id.listView);

        final ArrayList<String> putText = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,putText);
        l1.setAdapter(adapter);

        final ArrayList<Bitmap> putImage = new ArrayList<Bitmap>();

        try{

            Main2Activity.db= this.openOrCreateDatabase("SEHIRDATABASE",MODE_PRIVATE,null);
            Main2Activity.db.execSQL("CREATE TABLE IF NOT EXISTS SEHIR (text VARCHAR, image BLOB)");
            Cursor cursor =Main2Activity.db.rawQuery("SELECT * FROM SEHIR",null);
            int textIndex = cursor.getColumnIndex("text");
            int imageIndex = cursor.getColumnIndex("image");
            cursor.moveToFirst();

            while (null!=cursor) {

                putText.add(cursor.getString(textIndex));

                byte[] byteArray = cursor.getBlob(imageIndex);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                putImage.add(bitmap);
                cursor.moveToNext();
                adapter.notifyDataSetChanged();
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("text",putText.get(position));
                selectedImage = putImage.get(position);
                startActivity(intent);

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menum,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
