package com.example.usuario.trabalhojoaopaulo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.trabalhojoaopaulo.modelo.Locais;
import com.example.usuario.trabalhojoaopaulo.persistencia.DatabaseHelper;
import com.example.usuario.trabalhojoaopaulo.utils.UtilsGUI;

import java.sql.SQLException;
import java.util.List;

public class LocalActivity extends AppCompatActivity {

    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTextLocal;

    private int  modo;
    private Locais locais;

    public static void novo(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, LocalActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Locais locais){

        Intent intent = new Intent(activity, LocalActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, locais.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locais);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextLocal = findViewById(R.id.editTextLocais);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO);

        if (modo == ALTERAR){

            int id = bundle.getInt(ID);

            try {

                DatabaseHelper conexao = DatabaseHelper.getInstance(this);
                locais =  conexao.getLocaisDao().queryForId(id);

                editTextLocal.setText(locais.getLocal());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            setTitle(R.string.alterar_local);

        }else{

            locais = new Locais();

            setTitle(R.string.novo_local);
        }
    }

    private void salvar(){

        String local  = UtilsGUI.validaCampoTexto(this,
                editTextLocal,
                R.string.local_vazio);

        if (local == null){
            return;
        }

        try {

            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            List<Locais> lista = conexao.getLocaisDao()
                    .queryBuilder()
                    .where().eq(Locais.LOCAIS, local)
                    .query();

            if (modo == NOVO) {

                if (lista.size() > 0){
                    UtilsGUI.avisoErro(this, R.string.local_usado);
                    return;
                }

                locais.setLocal(local);

                conexao.getLocaisDao().create(locais);

            } else {

                if (!locais.equals(locais.getLocal())){

                    if (lista.size() >= 1){
                        UtilsGUI.avisoErro(this, R.string.local_usado);
                        return;
                    }

                    locais.setLocal(local);

                    conexao.getLocaisDao().update(locais);
                }
            }

            setResult(Activity.RESULT_OK);
            Toast.makeText(getApplicationContext(), R.string.local_salvo, Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
