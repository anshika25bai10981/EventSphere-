package util;

import java.io.*;
import java.time.LocalDateTime;

public class Logger {

    public static void log(String message) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("logs/application.log", true))) {

            writer.write(LocalDateTime.now() + " - " + message);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Logging error: " + e.getMessage());
        }
    }
}
