package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Session implements Serializable {

    private static final long serialVersionUID = 7231396767807169880L;
    private transient Application app;
    private HashMap<String, String> session = new HashMap<>();
    private transient Object paramFXMLController;

    private Session(Application app) {
        this.app = app;
    }

    public String get(String key) {
        return this.session.get(key);
    }

    public void clear(){
        this.session.clear();
    }

    public void add(String key,String value){
        this.session.put(key, value);
    }

    public void loginWith(String email,String password){
        ArrayList<HashMap<String,String>> result = Models.db.excuteQuery(QueryBuilder.table("user").select("*").where("email","=",email).and().where("password","=",password).get()).get();
        if(result.size()>0){
            result.get(0).forEach((column,value)->{
                session.put(column,value);
            });
        }
    }

    public boolean logined(){
        return session.get("email") !=null;
    }

    public void logout(){
        clear();
    }

    public static Session getSessionFromFile(Application app) {
        String sessionPath = app.getPath("session");
        try {
            ObjectInputStream sessionFileObject = new ObjectInputStream(new FileInputStream(new File(sessionPath)));
            Session sessionCurrent = (Session) sessionFileObject.readObject();
            sessionCurrent.app = app;
            sessionFileObject.close();

            return sessionCurrent instanceof Session? sessionCurrent: null;
        } catch (IOException | ClassNotFoundException e) {
            return new Session(app);
        }
    }

    public void writeSessionToFile(){
        String sessionPath = this.app.getPath("session");
        try {
            ObjectOutputStream sessionFileObject = new ObjectOutputStream(new FileOutputStream(new File(sessionPath)));
            sessionFileObject.writeObject(this);
            sessionFileObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session setParamsFXMLController(Object param){
        paramFXMLController = param;
        return this;
    }

    public Object getParamFXMLController(){
        return paramFXMLController;
    }
}
