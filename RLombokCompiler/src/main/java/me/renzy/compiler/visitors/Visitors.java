package me.renzy.compiler.visitors;

import me.renzy.api.data.FieldData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public final class Visitors {

    public static final int API = Opcodes.ASM9;

    public static ClassVisitor newClassVisitor(ClassWriter classWriter) {
        return new ClassVisitorImpl(API,classWriter);
    }

    public static FieldVisitor newFieldVisitor(FieldVisitor visitor, ClassWriter writer, FieldData data) {
        return new FieldVisitorImpl(API,visitor,data,writer);
    }

    private Visitors() {}
}
