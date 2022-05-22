package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.api.ApiUtilities;
import com.example.myapplication.api.CountryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Countryactivity extends AppCompatActivity {

     private RecyclerView recyclerView;
     private List<CountryData> list;
     private ProgressDialog dialog;
     private EditText searchBar;
     private CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countryactivity);

        recyclerView= findViewById(R.id.countries);
        list= new ArrayList<>();
        searchBar= findViewById(R.id.searchBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

         adapter= new CountryAdapter(this,list);
        recyclerView.setAdapter(adapter);

        dialog= new ProgressDialog(this);
        dialog.setMessage("Loding.....");
        dialog.setCancelable(false);
        dialog.show();

        ApiUtilities.getapiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                dialog.dismiss();

                Toast.makeText(Countryactivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());

            }
        });
    }

    private void filter(String text) {

        List<CountryData> filterList=  new ArrayList<>();
        for(CountryData items : list){
            if(items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }

        }
        adapter.filterList(filterList);
    }
}