package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.api.ApiUtilities;
import com.example.myapplication.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity<list> extends AppCompatActivity {

    private TextView totalConfirm, todayConfirm, totalActive, totalRecovered, todayRecovered, totalDeaths, todayDeaths, totalTests;
    private PieChart pieChart;
    private List<CountryData> list;
    private MediaPlayer mediaPlayer;
    String country="India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list= new ArrayList<>();

        if(getIntent().getStringExtra("country")!=null)
        {
            country=getIntent().getStringExtra("country");
        }

        init();
         TextView cname= findViewById(R.id.cname);
         cname.setText(country);
        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Countryactivity.class)));


         ApiUtilities.getapiInterface().getCountryData()
                 .enqueue(new Callback<List<CountryData>>() {
                     @Override
                     public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                       list.addAll(response.body());
                         for(int i=0;i<list.size();i++)
                         {
                             if(list.get(i).getCountry().equals(country)){
                                 int confirm= Integer.parseInt(list.get(i).getCases());
                                 int active= Integer.parseInt(list.get(i).getActive());
                                 int recovered= Integer.parseInt(list.get(i).getRecovered());
                                 int deaths= Integer.parseInt(list.get(i).getDeaths());
                                 int tests= Integer.parseInt(list.get(i).getTests());

                                 totalActive.setText(NumberFormat.getInstance().format(active));
                                 totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                 totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                 totalDeaths.setText(NumberFormat.getInstance().format(deaths));
                                 totalTests.setText(NumberFormat.getInstance().format(tests));



                                 todayDeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                 todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                 todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));

                                 mediaPlayer.start();




                                 pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                                 pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue)));
                                 pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green)));
                                 pieChart.addPieSlice(new PieModel("Death", deaths, getResources().getColor(R.color.red_pie)));

                                 pieChart.startAnimation();
                             }
                         }
                     }

                     @Override
                     public void onFailure(Call<List<CountryData>> call, Throwable t) {
                         Toast.makeText(MainActivity.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                         mediaPlayer.stop();
                     }

                 });


    }

    private void init()
    {
        totalConfirm= findViewById(R.id.totalConfirm);
        todayConfirm= findViewById(R.id.todayConfirm);
        totalActive= findViewById(R.id.totalActive);
        totalRecovered= findViewById(R.id.totalRecovered);
        todayRecovered=findViewById(R.id.todayRecovered);
        totalDeaths=findViewById(R.id.totalDeaths);
        todayDeaths=findViewById(R.id.todayDeath);
        totalTests=findViewById(R.id.totalTests);
        pieChart=findViewById(R.id.pieChart);
        mediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.music2);

        mediaPlayer.setLooping(true);
        mediaPlayer.start();



    }


}