package ch.kbenz.polariondemo.io;

import ch.kbenz.polariondemo.model.Requirement;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequirementLoader {

    public List<Requirement> loadDirectory(Path directory) throws IOException {
        var result = new ArrayList<Requirement>();

        try (var paths = Files.list(directory)) {
            for (Path path : paths.filter(p -> p.toString().endsWith(".yaml")).sorted().toList()) {
                result.add(load(path));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Requirement load(Path path) throws IOException {
        var yaml = new Yaml();
        Map<String, Object> data = yaml.load(Files.readString(path));

        return new Requirement(
                asString(data.get("id")),
                asString(data.get("title")),
                asString(data.get("status")),
                asString(data.get("description")),
                asStringList(data.get("acceptanceCriteria")),
                asStringList(data.get("testIds"))
        );
    }

    private static String asString(Object value) {
        return value == null ? "" : value.toString();
    }

    private static List<String> asStringList(Object value) {
        if (value == null) return List.of();
        return ((List<Object>) value).stream().map(Object::toString).toList();
    }
}
