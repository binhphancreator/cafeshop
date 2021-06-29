package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class View extends Stage {
    String view;
    String title;

    public View(String title,String view) {
        this.view = view;
        this.title = title;

        initStyle();

        setScene(new Scene(Loader.loadParent(view)));

        show();
    }

    private void initStyle(){
        setTitle(title);
        setMaximized(false);
        setFullScreen(false);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
    }

    public View replaceMainPane(String nameComp){
        VBox vBox = (VBox) getScene().getRoot();
        vBox.getChildren().remove(1);
        vBox.getChildren().add(Loader.loadParent(nameComp));
        return this;
    }

    public View addComponents(String nameComp){
        VBox vBox = (VBox) getScene().getRoot();
        vBox.getChildren().add(Loader.loadParent(nameComp));
        return this;
    }
}
