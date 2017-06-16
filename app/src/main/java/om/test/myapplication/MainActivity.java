package om.test.myapplication;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import om.test.virtusalib.*;
public class MainActivity extends AppCompatActivity implements Notifiable{
    Button gob;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    ImageView iml;
    EditText ed1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ed1 = (EditText) findViewById(R.id.editText);
        gob = (Button) findViewById(R.id.button);
        tv1 = (TextView)findViewById(R.id.textView2);
        tv2 = (TextView)findViewById(R.id.textView3);
        tv3 = (TextView)findViewById(R.id.textView4);
        tv4 = (TextView)findViewById(R.id.textView5);
        tv5 =  (TextView)findViewById(R.id.textView6);
        tv6 =  (TextView)findViewById(R.id.textView7);
        iml = (ImageView) findViewById(R.id.imageView);


        gob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed1.getText().toString().isEmpty())
                    WeatherManager.getWeatherManager().getWeatherData(ed1.getText().toString(),MainActivity.this);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData(WeatherData wd) {
             if(wd != null){
                 float temp =  9/5 * (Float.parseFloat(wd.temp) - 273) + 32;

                 tv1.setText(wd.city);
                 tv2.setText(wd.condition );
                 tv3.setText(wd.desc);
                 tv4.setText("Temperature " + temp + " F");
                 tv5.setText("Windspeed " + wd.windspeed + " mph");
                 tv6.setText( "Pressure " +  wd.pressure + " mb");
                 //tv7.setText(wd.desc);
                 //tv7.setText("  ");
                 iml.setVisibility(View.VISIBLE);
                 iml.setImageBitmap(wd.bitmap);
             } else {
                 iml.setVisibility(View.INVISIBLE);
             }
    }
}
