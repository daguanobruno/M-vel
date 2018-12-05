package com.example.usuario.trabalhojoaopaulo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usuario.trabalhojoaopaulo.modelo.Gastos;
import com.example.usuario.trabalhojoaopaulo.modelo.Locais;
import com.example.usuario.trabalhojoaopaulo.persistencia.DatabaseHelper;
import com.example.usuario.trabalhojoaopaulo.utils.UtilsGUI;


import java.sql.SQLException;
import java.util.List;

public class LocaisActivity extends AppCompatActivity {

    private ListView listViewLocal;
    private ArrayAdapter<Locais> listaAdapter;

    private static final int REQUEST_NOVO_LOCAIS= 1;
    private static final int REQUEST_ALTERAR_LOCAIS= 2;

    public static void abrir(Activity activity){

        Intent intent = new Intent(activity, LocaisActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listViewLocal = findViewById(R.id.listViewGastos);

        listViewLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Locais locais = (Locais) parent.getItemAtPosition(position);

                LocalActivity.alterar(LocaisActivity.this,
                        REQUEST_ALTERAR_LOCAIS,
                        locais);
            }
        });

        popularLista();

        registerForContextMenu(listViewLocal);

        setTitle(R.string.local);

    }

    private void popularLista(){

        List<Locais> lista = null;

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            lista = conexao.getLocaisDao()
                    .queryBuilder()
                    .orderBy(Locais.LOCAIS, true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaAdapter = new ArrayAdapter<Locais>(this,
                android.R.layout.simple_list_item_1,
                lista);

        listViewLocal.setAdapter(listaAdapter);
    }

    private void excluirLocal(final Locais local){

        try {

            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            List<Gastos> lista = conexao.getGastosDao()
                    .queryBuilder()
                    .where().eq(Gastos.LOCAL_ID, local.getId())
                    .query();

            // .where().eq(Gastos.Local_ID, local.getId())

            if (lista != null && lista.size() > 0){
                UtilsGUI.avisoErro(this, R.string.local_usado);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + local.getLocal();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                try {
                                    DatabaseHelper conexao =
                                            DatabaseHelper.getInstance(LocaisActivity.this);

                                    conexao.getLocaisDao().delete(local);

                                    listaAdapter.remove(local);

                                } catch (Exception e) {
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

        if ((requestCode == REQUEST_NOVO_LOCAIS || requestCode == REQUEST_ALTERAR_LOCAIS)
                && resultCode == Activity.RESULT_OK){

            popularLista();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_locais, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.novo_local:
                LocalActivity.novo(this, REQUEST_NOVO_LOCAIS);
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

        Locais local= (Locais) listViewLocal.getItemAtPosition(info.position);

        switch(item.getItemId()){

                case R.id.menuItemEditar:
                LocalActivity.alterar(this,
                REQUEST_ALTERAR_LOCAIS,
                local);
                return true;

                case R.id.menuItemDeletar:
                excluirLocal(local);
                return true;

                default:
                return super.onContextItemSelected(item);
        }
    }
}