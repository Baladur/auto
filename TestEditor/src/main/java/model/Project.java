package model;

import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by roman on 05.05.2017.
 */
@Setter
@Getter
public class Project {
    private String name;
    private String packageName;
    @Setter(AccessLevel.NONE) private String srcPath;

    private transient Path root;

    public void setSrc(Path src) {
        srcPath = src.toString();
    }

    public Path getElementsPath() {
        String packagePath = packageName.replace(".", File.separator).concat("elements");
        return Paths.get(root.toAbsolutePath().toString() + File.separator + srcPath + File.separator + packagePath);
    }

    public Path getTestsPath() {
        String packagePath = packageName.replace(".", File.separator).concat("tests");
        return Paths.get(root.toAbsolutePath().toString() + File.separator + srcPath + File.separator + packagePath);
    }

    public String getElementsPackage() {
        return packageName.concat(".elements");
    }

    public String getTestsPackage() {
        return packageName.concat(".tests");
    }
}
