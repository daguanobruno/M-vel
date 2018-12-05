package com.example.usuario.trabalhojoaopaulo.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.usuario.trabalhojoaopaulo.R;
import com.example.usuario.trabalhojoaopaulo.modelo.Gastos;
import com.example.usuario.trabalhojoaopaulo.modelo.Locais;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME    = "trabalhoDispositivosMoveis.db";
    private static final int    DB_VERSION = 8;

    private static DatabaseHelper instance;

    private Context context;
    private Dao<Gastos, Integer> gastosDao;
    private Dao<Locais, Integer> locaisDao;

    public static DatabaseHelper getInstance(Context contexto){

        if (instance == null){
            instance = new DatabaseHelper(contexto);
        }

        return instance;
    }

    private DatabaseHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
        context = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Locais.class);

            String[] tiposBasicos = context.getResources().getStringArray(R.array.tipos_locais);

            List<Locais> lista = new ArrayList<Locais>();

            for(int cont = 0; cont < tiposBasicos.length; cont++){

                Locais locais= new Locais(tiposBasicos[cont]);
                lista.add(locais);
            }

            getLocaisDao().create(lista);

            TableUtils.createTable(connectionSource, Gastos.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "onCreate", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Gastos.class, true);
            TableUtils.dropTable(connectionSource, Locais.class, true);

            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Gastos, Integer> getGastosDao() throws SQLException {

        if (gastosDao == null) {
            gastosDao = getDao(Gastos.class);
        }

        return gastosDao;
    }

    public Dao<Locais, Integer> getLocaisDao() throws SQLException {

        if (locaisDao == null) {
            locaisDao = getDao(Locais.class);
        }

        return locaisDao;
    }
}
