package com.example.usuario.trabalhojoaopaulo.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gastos")
public class Gastos {


    public static final String ID = "id";
    public static final String ITEM = "item";
    public static final String QUANTIDADE   = "quantidade";
    public static final String VALOR   = "valor";
    public static final String LOCAL_ID = "local_id";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(canBeNull = false, columnName = QUANTIDADE)
    private int quantidade;

    @DatabaseField(canBeNull = false, columnName = VALOR)
    private int valor;

    @DatabaseField(columnName = ITEM)
    private String item;

    @DatabaseField(foreign = true, columnName = LOCAL_ID)
    private Locais locais;

    public Gastos(){
    }

    public Gastos(String item){
        setItem(item);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item= item;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getValor(){
        return valor;
    }

    public void setValor(int valor) {
        this.valor= valor;
    }

    public Locais getLocais(){
        return locais;
    }

    public void setLocais(Locais locais){
        this.locais = locais;
    }

    @Override
    public String toString(){
        return getItem();
    }
}
