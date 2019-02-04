import java.io.File;
import java.util.ArrayList;

public class AnalyzerCatalog {

    private FileWithHash main;
    private String name;
    private String description;

    public FileWithHash getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AnalyzerCatalog(String pathMainFolder) {
        File file = new File(pathMainFolder);
        if (file.exists()) {
            System.out.println("Найдено! AnalyzerCatalog создан");
            main = new FileWithHash(file);
            main.generateChilds();
            setName(Main.getCurrentTime());
        }
    }

    public void generateHash() {
        main.generateHash();
    }

    public void printHash() {
        main.printHash("");
    }

    public void compare(AnalyzerCatalog analyzerCatalog) {
        main.compare(analyzerCatalog.main);
    }

    public void printCompared() {
        main.printCompared("");
    }
}
