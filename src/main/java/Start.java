import me.renzy.annotations.types.Getter;
import me.renzy.compiler.Compiler;
import me.renzy.utils.LogManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.jar.JarFile;

public class Start {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LogManager.info("Enter location: ");
        String loc = scanner.nextLine();
        LogManager.info("Jar name: ");
        String jarName = scanner.nextLine();
        try {
            JarFile file = new JarFile(loc + "\\" + jarName);

            Compiler.compile(file,jarName,loc + "\\temp_"+jarName.substring(0,jarName.length()-4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
