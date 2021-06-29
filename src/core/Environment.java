package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Environment {
    private Application app;
    private HashMap<String, String> mapEnv = new HashMap<>();
    private String envFile;


    public Environment(Application app){
        this.app=app;
        this.envFile=this.app.getPath("env");
        this.readEnvFromFile();
    }

    private void readEnvFromFile() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(this.envFile));
            String line="";
            while ((line = br.readLine()) != null) {
                mapEnv.put(line.split("=")[0], line.split("=")[1]);
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            return;
        }
        catch (IOException e) {
            return;
        }
    }

    public String env(String key) {
        return this.mapEnv.get(key);
    }
    public String env(String key,String valueDefault) {
        return this.mapEnv.get(key)==null ? valueDefault : this.mapEnv.get(key);
    }
}
