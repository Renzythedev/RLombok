package me.renzy.compiler;

import me.renzy.compiler.visitors.Visitors;
import me.renzy.utils.LogManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public final class Compiler {

    public static void compile(JarFile inputJar, String outputJarPath) throws IOException {
        LogManager.info("Compiling " + inputJar.getName() + "...");

        try (JarOutputStream outputJar = new JarOutputStream(new FileOutputStream(outputJarPath))) {

            Enumeration<? extends JarEntry> enumeration = inputJar.entries();

            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();

                if (!entry.getName().endsWith(".class")) {
                    outputJar.putNextEntry(new JarEntry(entry.getName()));
                    try (InputStream entryStream = inputJar.getInputStream(entry)) {
                        entryStream.transferTo(outputJar);
                    }
                    outputJar.closeEntry();
                    continue;
                }

                ClassReader reader = new ClassReader(inputJar.getInputStream(entry));
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

                ClassVisitor visitor = Visitors.newClassVisitor(writer);
                reader.accept(visitor, 0);

                byte[] bytecode = writer.toByteArray();

                JarEntry newEntry = new JarEntry(entry.getName());
                outputJar.putNextEntry(newEntry);
                outputJar.write(bytecode);
                outputJar.closeEntry();
            }
        }

        inputJar.close();
        LogManager.info("Compilation complete. Output written to " + outputJarPath);
    }

    private Compiler() {}
}
