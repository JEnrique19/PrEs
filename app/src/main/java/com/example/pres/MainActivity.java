package com.example.pres;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnGoToEscanear;
    TextView textPrincipal;
    int salir = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToEscanear = (Button) findViewById(R.id.btnGoToEscanear);
        textPrincipal = (TextView) findViewById(R.id.textPrincipal);

        obtenerDatos();

        btnGoToEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent (MainActivity.this, fotoActivity.class);
                    GlobalClass globalClass = (GlobalClass)getApplicationContext();
                    globalClass.setTextoPrueba("Hello Only");
                    startActivityForResult(intent, 0);
                }catch (Exception e){
                    Log.w("ErrorMA",e.getMessage());
                }
            }
        });




    }

    private void obtenerDatos() {
        GlobalClass globalClass = (GlobalClass)getApplicationContext();
        Double latH = globalClass.getLat();
        Double lonH = globalClass.getLon();
        String grados = globalClass.getGrados();

        if (latH == null || lonH == null){
            textPrincipal.setText("PrEs");
        }else{
            textPrincipal.setText("Datos:\nLat: "+latH+"\nLon: "+lonH+"\nGrados: "+grados);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerDatos();
    }

    @Override
    public void onBackPressed() {
        /*if (salir == 1){
            finish();
        }else if (salir == 0){
            Toast.makeText(MainActivity.this,"Presiona de nuevo para salir",Toast.LENGTH_LONG).show();
            salir = 1;
        }*/
    }
}
