import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {

        startTesting();

    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String help_Str = "analyze - Создаёт AnalyzeCatalog, и создаются hash значения\n" +
            "viewAll - выводит name и desc всех текущих AnalyzerCatalog\n" +
            "setDescLast - предоставляется поле для ввода, тем самым задать описание\n" +
            "printHashLast - выводит hash для последнего анализа\n" +
            "printHash - предоставляет поле для ввода, а затем выводит соответсвующий AnalyzeCatalog\n" +
            "comapreTwoLast - сравнивает два последних AnalyzerCatalog";

    private static ArrayList<AnalyzerCatalog> analyzerCatalogs = new ArrayList<>();
    public static void startTesting() {
        System.out.println(help_Str);
        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.next();
            if (command.equals("exit")) {
                break;
            }
            switch (command) {
                case "analyze" :
                    AnalyzerCatalog new_analyzerCatalog = analyze();
                    analyzerCatalogs.add(new_analyzerCatalog);
                    break;
                case "viewAll" :
                    for (AnalyzerCatalog analyzerCatalog : analyzerCatalogs) {
                        System.out.println(analyzerCatalog.getName());
                        System.out.println(analyzerCatalog.getDescription());
                        System.out.println();
                    }
                    break;
                case "setDescLast":
                    if (analyzerCatalogs.size() > 0) {
                        System.out.println("New description");
                        String new_desc = in.next();
                        analyzerCatalogs.get(analyzerCatalogs.size() - 1).setDescription(new_desc);
                    } else {
                        System.out.println("Не создано ещё AnalyzeCatalog");
                    }
                    break;
                case "printHashLast" :
                    if (analyzerCatalogs.size() > 0) {
                        analyzerCatalogs.get(analyzerCatalogs.size() - 1).printHash();
                    } else {
                        System.out.println("Не создано ещё AnalyzeCatalog");
                    }
                    break;
                case "printHash" :
                    String index_str = in.next();
                    if (analyzeIndex(index_str)) {
                        analyzerCatalogs.get(Integer.parseInt(index_str)).printHash();
                    }
                    break;
                case "compareTwoLast" :
                    if (analyzerCatalogs.size() > 1) {
                        int size = analyzerCatalogs.size();
                        analyzerCatalogs.get(size - 1).compare(analyzerCatalogs.get(size - 2));
                        analyzerCatalogs.get(size - 1).printCompared();
                    } else {
                        System.out.println("Мало данных");
                    }
                    break;
                case "compare" :
                    int index2 = Integer.valueOf(in.next());
                    analyzerCatalogs.get(index2).printCompared();
                default:
                    System.out.println("Какая-то левая команда -_-");
            }
        }
    }

    private static boolean analyzeIndex (String index_str) {
        try {
            int index = Integer.parseInt(index_str);
            if (index > (analyzerCatalogs.size() - 1) && (index < 0)) {
                System.out.println("Это чёт много. или мало. Ну вы поняли.");
                return false;
            }
            return true;
        } catch (NumberFormatException ex) {
            System.out.println("Неправильное какое-то число. Как текст");
            return false;
        }
    }

    private static String path = "/home/kanumba/.winegames";

    private static AnalyzerCatalog analyze() {
        AnalyzerCatalog analyzerCatalog = new AnalyzerCatalog(path);
        analyzerCatalog.generateHash();
        //analyzerCatalog.printHash();
        return analyzerCatalog;
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    public static String getCurrentTime() {
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }
}
