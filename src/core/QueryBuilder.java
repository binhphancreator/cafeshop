package core;

import java.util.ArrayList;

public class QueryBuilder {
    private final String table;
    private String sql="";
    private String where="";
    private String select="";

    private ArrayList<String> insertCols = new ArrayList<>();
    private ArrayList<String> insertVals = new ArrayList<>();

    private ArrayList<String> updateCols = new ArrayList<>();
    private ArrayList<String> updateVals = new ArrayList<>();

    private QueryBuilder(String table){
        this.table = table;
    }

    public static QueryBuilder table(String table){
        return new QueryBuilder(table);
    }

    private void buildInsert(){
        sql = "INSERT INTO " + table;
        String cols = "";
        String vals = "";
        for (int i = 0; i < insertCols.size(); i++)
        {
            cols += insertCols.get(i);
            vals += "'" + insertVals.get(i) + "'";
            if (i != insertCols.size() - 1) {
                cols += ",";
                vals += ",";
            }
        }
        cols = "(" + cols + ")";
        vals = "(" + vals + ")";

        sql += " " + cols + " VALUES " + vals;
    }

    private void buildUpdate(){
        String set = "";
        for (int i = 0; i < updateCols.size(); i++) {
            if(updateVals.get(i)!=null)
                set += updateCols.get(i) + "=" + "'" + updateVals.get(i) + "'";
            else set += updateCols.get(i) + "=null";
            if (i != updateCols.size() - 1) set += ",";
        }
        sql = "UPDATE " + table + " SET " + set;
        if(!where.isEmpty())
            sql+= " WHERE " + where;
    }

    public QueryBuilder select(String... columns){
        for (int i = 0; i < columns.length; i++)
        {
            this.select += columns[i];
            if (i != columns.length - 1) this.select += ",";
        }
        return this;
    }

    public QueryBuilder where(String column,String condition,String value){
        this.where += column + " " + condition + " " + "'" + value + "'";
        return this;
    }

    public QueryBuilder and(){
        this.where+= " AND ";
        return this;
    }

    public QueryBuilder or(){
        this.where+= " OR ";
        return this;
    }

    public QueryBuilder insert(String column,String value){
        insertCols.add(column);
        insertVals.add(value);
        return this;
    }

    public QueryBuilder update(String column,String value){
        updateCols.add(column);
        updateVals.add(value);
        return this;
    }

    public QueryBuilder delete(){
        sql = "DELETE FROM " + table;
        if(!where.isEmpty())
            sql+= " WHERE " + where;
        return this;
    }

    public String get(){
        if(!select.isEmpty()){
            sql = "SELECT " + select + " FROM " + table;

            if(!where.isEmpty()){
                sql+= " WHERE " + where;
            }
        }

        if(!insertCols.isEmpty()){
            this.buildInsert();
        }

        if(!updateCols.isEmpty()){
            this.buildUpdate();
        }

        return sql;
    }
}
