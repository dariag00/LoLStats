package com.example.klost.lolstats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialActivity extends AppCompatActivity {


    public static final String EXTRA_SUMMONER_NAME = "com.example.klost.lolstats.SUMMONER_NAME";

    private EditText summonerNameView;
    private Button searchButton;
    private Button testButton;
    private Button testButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        summonerNameView = findViewById(R.id.et_summoner_name);
        searchButton = findViewById(R.id.bt_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSearch();
            }
        });

        testButton = findViewById(R.id.bt_test);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest();
            }
        });

        testButton2 = findViewById(R.id.bt_test2);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest2();
            }
        });

    }

    private void toTest(){
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    private void toTest2(){
        Intent intent = new Intent(this, TestMainLayoutActivity.class);
        startActivity(intent);
    }

    private void attemptSearch(){

        summonerNameView.setError(null);

        String summonerName = summonerNameView.getText().toString();
        if (BuildConfig.DEBUG) {
            Log.d("IA.summonerName", summonerName);
        }

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(summonerName)){
            if (BuildConfig.DEBUG) {
                Log.d("IA.summonerName", "Se ha introducido un summoner Name vacio");
            }
            summonerNameView.setError(getString(R.string.error_empty_summoner_name));
            focusView=  summonerNameView;
            cancel = true;
        } else if(!isSummonerNameValid(summonerName)){
            if (BuildConfig.DEBUG) {
                Log.d("IA.summonerName", "Se ha introducido un summoner Name incorrecto " + summonerName);
            }
            summonerNameView.setError(getString(R.string.error_invalid_summoner_name));
            focusView=  summonerNameView;
            cancel = true;
        }

        if(cancel){
            //Se ha producido un error y se ha de cancelar el proceso
            focusView.requestFocus();
        }else{
            //Creamos un intent que iniciará la activity MainActivity y le pasamos el Summoner Name que hemos procesado
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_SUMMONER_NAME, summonerName);
            startActivity(intent);
        }
    }

    private boolean isSummonerNameValid(String summonerName){
        Pattern pattern = Pattern.compile("^[\\p{L} 0-9_\\.]+$");
        Matcher matcher = pattern.matcher(summonerName);
        return matcher.find();
    }
}
