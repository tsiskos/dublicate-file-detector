package gr.tsiskos;


import com.google.common.hash.Hashing;
import io.reactivex.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class DublicateFileDetector {

    public static Flowable<Path> detectDublicates(ArrayList<String> paths){
        Flowable<Path> detector = Flowable.create(new FlowableOnSubscribe<Path>() {
            @Override
            public void subscribe(FlowableEmitter<Path> emitter) throws Exception {
                Hashtable<String, HashedFile> uniqueFiles = new Hashtable<>();
                paths.stream().flatMap(path -> {
                    try {
                        return Files.walk(Paths.get(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }) .filter(p -> p.toFile().isFile())
                        .filter(p->!p.toString().endsWith(".rar"))
                        .filter(p->!p.toString().endsWith(".war"))
                        .filter(p->!p.toString().endsWith(".zip"))
                        .filter(p->!p.toString().endsWith(".mp4"))
                        .map(file->  new HashedFile(file))
                        .forEach(file -> {
                            try {
                                byte[] bytes = new byte[0];
                                System.out.println("Calculating hash for: "+file.toString());
                                bytes = Files.readAllBytes(file.getPath());
                                String hashValue = Hashing.sha256().hashBytes(bytes).toString();
                                if(uniqueFiles.containsKey(hashValue)){
                                      System.out.println("Duplicate file found: "+file.toString()+". Same with: "+ uniqueFiles.get(hashValue).toString());
                                    emitter.onNext(file.getPath());
                                }else{
                                    uniqueFiles.put(hashValue,file);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                // signal an item

                System.out.println("End of sequence");
                // the end-of-sequence has to be signaled, otherwise the
                // consumers may never finish
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        return detector;
    }

}
