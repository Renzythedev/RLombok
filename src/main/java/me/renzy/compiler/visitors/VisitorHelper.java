package me.renzy.compiler.visitors;

import me.renzy.annotations.datas.ActionData;
import me.renzy.annotations.datas.FieldData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nonnull;

public final class VisitorHelper {

    public static final int API = Opcodes.ASM9;

    @Nonnull
    public static ClassVisitor newClassVisitor(@Nonnull ClassWriter classWriter) {
        return new ClassVisitorImpl(API,classWriter);
    }

    @Nonnull
    public static FieldVisitor newFieldVisitor(@Nonnull FieldVisitor visitor,@Nonnull ClassWriter writer, @Nonnull FieldData data) {
        return new FieldVisitorImpl(API,visitor,data,writer);
    }

    private VisitorHelper() {

    }
}
