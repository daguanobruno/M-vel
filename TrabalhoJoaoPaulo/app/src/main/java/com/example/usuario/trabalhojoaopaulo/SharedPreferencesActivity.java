package com.example.usuario.trabalhojoaopaulo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SharedPreferencesActivity  extends AppCompatActivity {

    public static final String MODO = "MODO";
    public static final int NOVO = 1;

    private EditText editTextArea;
    private TextView texto;


    public static void info(Activity activity) {

        Intent intent = new Intent(activity, SharedPreferencesActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, NOVO);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preference);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextArea = findViewById(R.id.editTextArea);
        texto = findViewById(R.id.textViewShared);
        View v = null;

        ver(v);
    }

    public void salvar (View v){
        SharedPreferences shard = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shard.edit();
        editor.putString("curso", editTextArea.getText().toString());

        editor.apply();

        Toast.makeText(getApplicationContext(), R.string.salvo, Toast.LENGTH_SHORT).show();
    }

    public void ver(View v){
        SharedPreferences shard = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String curso = shard.getString("curso", "");
        texto.setText(curso + " ");
    }
}
