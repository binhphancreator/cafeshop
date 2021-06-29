package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.database.DB;

public class Models implements Cloneable {
    public static DB db;

    protected String primaryKey = "id";
    protected String tableName = getClass().getSimpleName().toLowerCase();

    protected String id = null;

    private ArrayList<HashMap<String,String>> result = null;
    protected HashMap<String,String> data = null;

    protected ArrayList<String> columns = new ArrayList<>();

    protected void setColumns(){
        ArrayList<HashMap<String,String>> columnsMeta = db.excuteQuery("SHOW COLUMNS FROM "+tableName).get();
        for(HashMap<String,String> columnMeta : columnsMeta){
            columns.add(columnMeta.get("Field"));
        }
    }

    public Models(){
        setColumns();
    }

    private Models(String tableName){
        this.tableName = tableName;
        setColumns();
    }

    public static Models table(String tableName){
        return new Models(tableName);
    }

    public Models find(){
        this.result = db.excuteQuery( QueryBuilder.table(tableName).select("*").where(primaryKey,"=",id).get() ).get();
        this.data = this.result.isEmpty() ? null : this.result.get(0);
        return this;
    }

    public Models insert(){
        QueryBuilder builder = QueryBuilder.table(tableName);

        data.forEach((column,value)->{
            builder.insert(column,value);
        });

        db.execute(builder.get());

        this.id = db.getLastID();
        data.put(primaryKey,this.id);

        return this;
    }

    public boolean exist(){
        return !this.result.isEmpty();
    }

    public Models all(){
        this.result = db.excuteQuery("SELECT * from " + tableName).get();
        return this;
    }

    public Models delete(){

        db.execute(QueryBuilder.table(tableName).where(primaryKey,"=",id).delete().get());
        return this;
    }

    public Models softDelete(){
        set("active","0").update();
        return this;
    }

    public Models update(){
        QueryBuilder builder = QueryBuilder.table(tableName);
        for (String column : columns) {
            if(!column.equals(primaryKey))
                builder.update(column,data.get(column));
        }

        db.execute(builder.where(primaryKey,"=",id).get());

        return this;
    }

    public <T> List<T> getModels(){
        List<T> modelsArr = new ArrayList<>();

        result.stream().map((rs) -> {
            return clone().set(rs);
        }).forEach((model) -> {
            modelsArr.add((T) model);
        });

        return modelsArr;
    }

    public String get(String column){
        return data.get(column);
    }

    public HashMap<String,String> get(){
        return data;
    }

    public Models set(String column , String value){
        if(data.containsKey("birth"))
            data.replace(column, value);
        else data.put(column,value);
        
        return this;
    }

    public Models set(HashMap<String,String> data){
        this.data = data;
        this.id = data.get(primaryKey);
        return this;
    }

    public Models addColumn(String columnName,String value){
        columns.add(columnName);
        data.put(columnName,value);
        return this;
    }

    public ArrayList<String> getColumns(){
        return columns;
    }

    @Override
    public String toString(){
        return data.toString();
    }

    @Override
    public Models clone(){
        try {
            Models model = (Models) super.clone();
            model.columns = (ArrayList<String>) columns.clone();
            return model;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
