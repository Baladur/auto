package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.nio.file.Path;

/**
 * Created by roman on 05.05.2017.
 */
public class JsonSerializer {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T deserialize(Path path, Class<T> type) throws JsonSyntaxException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            return gson.fromJson(br, type);
        }
    }

    public static void serialize(Object o, Path path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()))) {
            gson.toJson(o, bw);
        }
    }
}
