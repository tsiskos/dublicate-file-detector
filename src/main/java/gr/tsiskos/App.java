package gr.tsiskos;

import io.reactivex.Flowable;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class App 
{

    public static void main( String[] args ) {
        ArrayList<String> paths = new ArrayList<>();
        paths.add("C:\\Users\\xxxx\\Pictures\\1");
        paths.add("C:\\Users\\xxxx\\Pictures\\2");
        Flowable<Path> source = DublicateFileDetector.detectDublicates(paths);

        source.subscribe(e-> {
            System.out.println("Deleting "+e.toString());
            try {
                File dublicateFound = e.toFile();

                if (!dublicateFound.delete()) {
                    System.out.println("Failed to delete.");
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        System.out.println("Done!");


    }
}
