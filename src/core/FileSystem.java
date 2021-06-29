package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileSystem {
    public static void copy(String source,String out){
        try{
            Files.copy(Path.of(source), Path.of("img/"+out), StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException e){
            System.out.println("IO Exception");
        }
    }
}
