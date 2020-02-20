package gr.tsiskos;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        ArrayList<String> sourcePaths;
        sourcePaths = new ArrayList<>();
        sourcePaths.add("D:\\Photos");
        sourcePaths.add("E:\\DATA\\Photos");

        Hashtable<String, HashedFile>  uniqueFiles = new Hashtable<>();

        List<HashedFile> hashValues = sourcePaths.stream().flatMap(path -> {
            try {
                return Files.walk(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(p -> p.toFile().isFile())
                .filter(p->!p.toString().endsWith(".rar"))
                .filter(p->!p.toString().endsWith(".war"))
                .filter(p->!p.toString().endsWith(".zip"))
               .map(file->  new HashedFile(file))

               .map( file -> {
                   try {

                       byte[] bytes = Files.readAllBytes(file.getPath()); //.toByteArray(file.getPath().toFile());
                       String hashValue = Hashing.sha256().hashBytes(bytes).toString();
                       if(uniqueFiles.containsKey(hashValue)){
                           System.out.println("Duplicate file found: "+file.toString()+". Same with: "+ uniqueFiles.get(hashValue).toString());
                       }else{
                           uniqueFiles.put(hashValue,file);
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   return file;
               })
               .collect(Collectors.toList());


    }
}
