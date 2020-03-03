package gr.tsiskos;

import com.google.common.hash.Hashing;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;

public class App 
{

    public static void main( String[] args ) {
        ArrayList<String> paths = new ArrayList<>();
        paths.add("C:\\Users\\tsiskos\\Pictures\\Phone 23_7_2017");
        paths.add("C:\\Users\\tsiskos\\Pictures\\Phone 23_7_2017 - Copy");
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
