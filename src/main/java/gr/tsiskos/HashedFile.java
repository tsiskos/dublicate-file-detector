package gr.tsiskos;

import java.nio.file.Path;

public class HashedFile {
    private Path path;


    public HashedFile(Path path) {
        this.path = path;

    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String toString(){
        return new StringBuilder().append(this.path.getFileName()).append('|')
                        .append(this.path.toString()).toString();
    }
}
