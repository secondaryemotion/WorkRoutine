import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    public static String readFile(String path){
        try {
            return Files.readString(Path.of(path));
        } catch (IOException E) {
            System.out.println("There is no such file. Please, check again.");
            return null;
        }
    }

    public static String getPnALineByIndex(Integer index, WBTable table){
        return table.printLineByIndex(index);
    }

    public static Integer askForColumnNumber(String column, String[] columnNames) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер колонки для " + column);
        int index = 0;
        for (String name : columnNames){
            System.out.println(index +" "+ name);
            index++;
        }
        return scanner.nextInt();
    }

    public static <T> void writeArrayListToFile(ArrayList<T> outputArray, String path) throws IOException {
        File fout = new File(path);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (T line : outputArray) {
            bw.write(line.toString());
            bw.newLine();
        }
        bw.close();
    }

    public static Integer parseIntForNA(String value){
        if (value.equals("#N/A")){
            return 0;
        }
        return Integer.parseInt(value);
    }

}
