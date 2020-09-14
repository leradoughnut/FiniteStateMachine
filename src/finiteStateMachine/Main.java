package finiteStateMachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter filename:");

        String filename = sc.nextLine();
        Path path = Paths.get(filename);
        File file = path.toFile();

        try {
            FiniteStateMachine finiteStateMachine = new FiniteStateMachine(file);
            System.out.println("All words:");
            finiteStateMachine.printAllWords();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
