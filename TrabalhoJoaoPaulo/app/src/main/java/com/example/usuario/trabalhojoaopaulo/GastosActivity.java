package com.example.usuario.trabalhojoaopaulo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.usuario.trabalhojoaopaulo.modelo.Gastos;
import com.example.usuario.trabalhojoaopaulo.modelo.Locais;
import com.example.usuario.trabalhojoaopaulo.persistencia.DatabaseHelper;
import com.example.usuario.trabalhojoaopaulo.utils.UtilsGUI;

import java.sql.SQLException;
import java.util.List;

public class GastosActivity extends AppCompatActivity {

    public static final String MODO    = "MODO";
    public static final String ID1      = "ID1";

    public static final String KEY_ITEM = "item";


    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTextItem;
    private EditText editTextQuantidade;
    private EditText editTextValor;

    private Spinner spinnerLocais;

    private List<Locais> listaLocais;

    private int    modo;
    private Gastos gastos;

    public static void nova(Activity activity, int requestCode){

        Intent intent = new Intent(activity, GastosActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Gastos gastos){

        Intent intent = new Intent(activity, GastosActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID1, gastos.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextItem         = findViewById(R.id.editText);
        editTextQuantidade   = findViewById(R.id.editTextQuantidade);
        editTextValor        = findViewById(R.id.editTextValor);

        spinnerLocais           = findViewById(R.id.spinnerLocal);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        popularSpinnerLocais();

        modo = bundle.getInt(MODO);

        if (modo == ALTERAR){
            int id1 = bundle.getInt(ID1);

            try {
                DatabaseHelper conexao = DatabaseHelper.getInstance(this);

                gastos = conexao.getGastosDao().queryForId(id1);

                editTextItem.setText(gastos.getItem());
                editTextQuantidade.setText(String.valueOf(gastos.getQuantidade()));
                editTextValor.setText(String.valueOf(gastos.getValor()));

                conexao.getLocaisDao().refresh(gastos.getLocais());


            } catch (SQLException e) {
                e.printStackTrace();
            }

            int posicao1 = posicaoLocais(gastos.getLocais());
            spinnerLocais.setSelection(posicao1);

            setTitle(R.string.alterar_gasto);

        }else{

            gastos = new Gastos();

            setTitle(R.string.novo_gastos);
        }
    }

    private int posicaoLocais(Locais local){

        for (int pos = 0; pos < listaLocais.size(); pos++){

            Locais l = listaLocais.get(pos);

            if (l.getId() == local.getId()){
                return pos;
            }
        }

        return -1;
    }

    private void popularSpinnerLocais(){

        listaLocais = null;

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            listaLocais= conexao.getLocaisDao()
                    .queryBuilder()
                    .orderBy(Locais.LOCAIS, true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        ArrayAdapter<Locais> spinnerAdapterDisciplina = new ArrayAdapter<Locais>(this,
                android.R.layout.simple_list_item_1,
                listaLocais);

        spinnerLocais.setAdapter(spinnerAdapterDisciplina);
    }

    private void salvar() {

        String produto = UtilsGUI.validaCampoTexto(this,
                editTextItem,
                R.string.produto_vazio);

        if (produto == null) {
            return;
        }

        String txt_quantidade = UtilsGUI.validaCampoTexto(this,
                editTextQuantidade,
                R.string.produto_vazio);

        if (txt_quantidade == null) {
            return;
        }

        int quantidade = Integer.parseInt(txt_quantidade);

        if (quantidade <= 0){
            UtilsGUI.avisoErro(this, R.string.aviso);
            editTextQuantidade.requestFocus();
            return;
        }

        String txt_valor= UtilsGUI.validaCampoTexto(this,
                editTextValor,
                R.string.aviso);

        if (txt_valor == null) {
            return;
        }

        int valor = Integer.parseInt(txt_valor);

        if (valor <= 0){
            UtilsGUI.avisoErro(this, R.string.aviso);
            editTextValor.requestFocus();
            return;
        }

        Locais local = (Locais) spinnerLocais.getSelectedItem();
        if (local != null) {
            gastos.setLocais(local);
        }

        gastos.setItem(produto);
        gastos.setQuantidade(quantidade);
        gastos.setValor(valor);

        try {

            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            List<Gastos> lista = conexao.getGastosDao()
                    .queryBuilder()
                    .where().eq(Gastos.ITEM, produto)
                    .query();

            if (modo == NOVO) {

                conexao.getGastosDao().create(gastos);

            }
            if (modo == ALTERAR) {

                conexao.getGastosDao().update(gastos);
            }

            setResult(Activity.RESULT_OK);
            Toast.makeText(getApplicationContext(), R.string.produto_salvo, Toast.LENGTH_SHORT).show();
            finish();

        } catch (SQLException e) {
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
