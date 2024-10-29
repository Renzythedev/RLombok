package me.renzy;

import me.renzy.compiler.Compiler;
import me.renzy.utils.LogManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.jar.JarFile;

public final class Start {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LogManager.info("Enter Jar Path: ");
        String jar = scanner.nextLine();
        LogManager.info("Enter Jar OutPath: ");
        String outputPath = scanner.nextLine();

        try {
            JarFile jarFile = new JarFile(jar);
            Compiler.compile(jarFile,outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
