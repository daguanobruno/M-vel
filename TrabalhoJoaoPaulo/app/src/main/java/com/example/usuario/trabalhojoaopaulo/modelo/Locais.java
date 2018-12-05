package com.example.usuario.trabalhojoaopaulo.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "locais")
public class Locais {

    public static final String ID = "id";
    public static final String LOCAIS = "local";
    //public static final Double VALOR = 0.0;

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(canBeNull = false, unique = true, columnName = LOCAIS)
    private String locais;

    // @DatabaseField(columnName = VALOR)
    // private double 0.0;

    public Locais(){
    }

    public Locais(String txt){
        setLocal(txt);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getLocal() {
        return locais;
    }

    public void setLocal(String local) {
        this.locais= local;
    }

    @Override
    public String toString(){
        return getLocal();
    }


}
