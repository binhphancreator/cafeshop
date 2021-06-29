package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Loader {
    public static Parent loadParent(String view){
        FXMLLoader loader = null;
        Parent root;

        try {
            loader = new FXMLLoader(getURL(view));
            root = loader.load();
        } catch (MalformedURLException e) {
            root = new AnchorPane();
        } catch (IOException e) {
            root = new AnchorPane();
        }

        return root;
    }

    private static URL getURL(String view) throws MalformedURLException {
        String path = System.getProperty("user.dir")+"/src/app/forms/"+view+".fxml";
        return new URL("file",null,path);
    }
}
