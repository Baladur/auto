package model;

import com.google.gson.JsonSyntaxException;
import com.sun.istack.internal.NotNull;
import util.DataException;
import util.JsonSerializer;
import util.Messages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by roman on 07.05.2017.
 */
public interface DataManager {
    default void saveProjectFile(Project project) throws DataException {
        try {
            JsonSerializer.serialize(project, Paths.get(project.getRoot().toString() + File.separator + project.getName() + ".json"));
        } catch (IOException e) {
            throw new DataException(Messages.PROJECT_SAVING_ERROR, e);
        }
    }

    default Project readProjectFile(@NotNull Path path) throws DataException {
        Project project;
        try {
            project = JsonSerializer.deserialize(path, Project.class);

            //set root path as it is not serialized
            project.setRoot(path);
        } catch (JsonSyntaxException jse) {
            //TODO: change error message
            throw new DataException(Messages.PROJECT_READING_ERROR, jse);
        } catch (IOException e) {
            throw new DataException(Messages.PROJECT_READING_ERROR, e);
        }
        return project;
    }
}
