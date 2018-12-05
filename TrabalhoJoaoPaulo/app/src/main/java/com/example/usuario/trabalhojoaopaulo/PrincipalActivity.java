package com.example.usuario.trabalhojoaopaulo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usuario.trabalhojoaopaulo.modelo.Gastos;
import com.example.usuario.trabalhojoaopaulo.persistencia.DatabaseHelper;
import com.example.usuario.trabalhojoaopaulo.utils.UtilsGUI;

import java.sql.SQLException;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private static final int REQUEST_NOVO_GASTO= 1;
    private static final int REQUEST_ALTERAR_GASTO= 2;

    private ListView listViewGastos;
    private ArrayAdapter<Gastos> listaAdapterGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listViewGastos = findViewById(R.id.listViewGastos);

        listViewGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Gastos gasto = (Gastos) parent.getItemAtPosition(position);
                GastosActivity.alterar(PrincipalActivity.this,
                        REQUEST_ALTERAR_GASTO,
                        gasto);
            }
        });

        popularListaGastos();

        registerForContextMenu(listViewGastos);
    }

    private void popularListaGastos(){

        List<Gastos> lista = null;

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            lista = conexao.getGastosDao()
                    .queryBuilder()
                    .orderBy(Gastos.ITEM, true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        listaAdapterGastos = new ArrayAdapter<Gastos>(this,
                android.R.layout.simple_list_item_1,
                lista);

        listViewGastos.setAdapter(listaAdapterGastos);
    }

    private void excluirGastos(final Gastos gastos){

        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + gastos.getItem();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                try {
                                    DatabaseHelper conexao =
                                            DatabaseHelper.getInstance(PrincipalActivity.this);

                                    conexao.getGastosDao().delete(gastos);

                                    listaAdapterGastos.remove(gastos);

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_NOVO_GASTO || requestCode == REQUEST_ALTERAR_GASTO
                && resultCode == Activity.RESULT_OK){
        popularListaGastos();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){

            case R.id.menuItemSalvar:
                GastosActivity.nova(this, REQUEST_NOVO_GASTO);
                return true;

            case R.id.menuItemLocais:
                LocaisActivity.abrir(this);
                return true;

            case R.id.menuItemInfo:
                InformacoesActivity.info(this);
                return true;

            case R.id.MenuSharedPreferences:
                SharedPreferencesActivity.info(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Gastos gastos = (Gastos) listViewGastos.getItemAtPosition(info.position);

        switch(item.getItemId()){

            case R.id.menuItemEditar:
                GastosActivity.alterar(this,
                        REQUEST_ALTERAR_GASTO,
                        gastos);
                return true;

            case R.id.menuItemDeletar:
                excluirGastos(gastos);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
