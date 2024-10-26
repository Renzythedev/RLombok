package me.renzy.compiler;

import me.renzy.compiler.visitors.VisitorHelper;
import me.renzy.utils.LogManager;
import org.objectweb.asm.*;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public final class Compiler {

    public static void compile(@Nonnull JarFile jar,@Nonnull String jarName, @Nonnull String outputJarPath) throws IOException {
        LogManager.info("Compiling " + jar.getName() + "...");
        Enumeration<JarEntry> enumeration = jar.entries();

        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();

            if (!entry.getName().endsWith(".class")) {

                try(InputStream entryStream = jar.getInputStream(entry)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = entryStream.read(buffer)) != -1)
                        createFile(outputJarPath,entry, Arrays.copyOf(buffer,bytesRead));

                    continue;
                }

            }

            LogManager.info("Processing class: " + entry.getName());
            ClassReader reader = new ClassReader(jar.getInputStream(entry));
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            ClassVisitor classVisitor = VisitorHelper.newClassVisitor(writer);
            reader.accept(classVisitor, 0);

            byte[] bytecode = writer.toByteArray();
            createFile(outputJarPath,entry,bytecode);
        }

        jar.close();

        createJarFile(outputJarPath + "\\" + jarName, new File(outputJarPath));
    }

    private static void createFile(@Nonnull String outputPath, @Nonnull JarEntry entry, @Nonnull byte[] bytes) throws IOException {
        Path path = Paths.get(outputPath, entry.getName());
        File file = new File(path.toUri());

        createParentFiles(file,entry);

        if (file.exists()) return;

        if (file.createNewFile()) {
            LogManager.info("File created: " + file.getName());
        } else {
            LogManager.err("Failed to create file: " + file.getName());
            return;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            LogManager.err("Error writing to file: " + file.getName() + " - " + e.getMessage());
        }
    }

    private static void createParentFiles(@Nonnull File file, @Nonnull JarEntry entry) {
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                LogManager.info("Parent directories created: " + parentDir.getAbsolutePath());
            } else {
                LogManager.err("Failed to create parent directories: " + parentDir.getAbsolutePath());
            }
        }
    }

    private static void createJarFile(@Nonnull String outputJarPath, @Nonnull File tempDir) throws IOException {
        try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJarPath))) {
            addDirectoryToJar(tempDir, jarOutputStream, tempDir.getAbsolutePath().length() + 1); // Temel dizini kaldırmak için +1
        }
        LogManager.info("JAR file created at: " + outputJarPath);
    }

    private static void addDirectoryToJar(File source, JarOutputStream jarOutputStream, int basePathLength) throws IOException {
        File[] files = source.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                addDirectoryToJar(file, jarOutputStream, basePathLength); // Alt dizinleri eklemek için
            } else {
                addFileToJar(file, jarOutputStream, basePathLength);
            }
        }
    }

    private static void addFileToJar(File file, JarOutputStream jarOutputStream, int basePathLength) throws IOException {
        String entryName = file.getAbsolutePath().substring(basePathLength).replace(File.separatorChar, '/'); // Dizin bölücüyü değiştiriyoruz.
        LogManager.info("Adding file to JAR: " + entryName);

        JarEntry jarEntry = new JarEntry(entryName);
        jarOutputStream.putNextEntry(jarEntry);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                jarOutputStream.write(buffer, 0, bytesRead);
            }
        }
        jarOutputStream.closeEntry();
    }

    private Compiler() {}
}
