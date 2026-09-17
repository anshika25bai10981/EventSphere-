package util;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    public static void writeToFile(String fileName, String data) {

        try (FileWriter writer = new FileWriter(fileName, true)) {

            writer.write(data);
            writer.write(System.lineSeparator());

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static ArrayList<String> readFile(String fileName) {

        ArrayList<String> data = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {
                data.add(line);
            }

        } catch (FileNotFoundException e) {
            // File will be created when data is first saved.
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return data;
    }
}