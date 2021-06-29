package core;

import core.database.DB;

import java.util.HashMap;

public class Application {
    private final HashMap<String, Object> instances = new HashMap<>();
    private final HashMap<String, String> paths = new HashMap<>();

    public Application(){
        bindPath();
        bindServices();
    }

    private void bindPath(){
        paths.put("env",".env");
        paths.put("session",".session");
    }

    private void bindServices(){
        Facades.app = this;

        this.bind("env",new Environment(this));
        Facades.env = (Environment) this.resolve("env");

        this.bind("db",new DB((Environment) this.resolve("env")));
        Models.db = (DB) this.resolve("db");

        this.bind("session",Session.getSessionFromFile(this));
        Facades.session = (Session) this.resolve("session");

        this.bind("fm",new FormsManager());
        Facades.fm = (FormsManager) this.resolve("fm");
    }

    public static void main(String... args){
        new Application().boot();
    }

    public String getPath(String key){
        return this.paths.get(key);
    }

    public void bind(String key,Object value){
        instances.put(key,value);
    }

    public Object resolve(String key){
        return this.instances.get(key);
    }

    public void boot(){
        javafx.application.Application.launch(core.BaseForm.class);
        ((Session) resolve("session")).writeSessionToFile();
    }

}
