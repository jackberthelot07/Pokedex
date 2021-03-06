package com.melono.pokedex;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        moveTaskToBack(true);

        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        new Handler().postDelayed(runnable, 9000);
    }

}