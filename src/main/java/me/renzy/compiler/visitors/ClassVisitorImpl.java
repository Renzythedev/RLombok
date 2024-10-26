package me.renzy.compiler.visitors;

import me.renzy.annotations.Annotations;
import me.renzy.annotations.EAnnotations;
import me.renzy.annotations.datas.ActionData;
import me.renzy.annotations.datas.FieldData;
import me.renzy.annotations.types.AnnotationType;
import me.renzy.utils.DataHelper;
import me.renzy.utils.LogManager;
import org.objectweb.asm.*;

import javax.annotation.Nonnull;
import java.util.Arrays;

final class ClassVisitorImpl extends ClassVisitor {

    private final ClassWriter writer;

    // Class data
    private String className;
    private String packageName;
    private String fullName;
    private boolean isStatic;
    private boolean isFinal;

    ClassVisitorImpl(int api, @Nonnull ClassWriter classWriter) {
        super(api, classWriter);
        this.writer = classWriter;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        extractClassNameDetails(name);
        this.isStatic = (access & Opcodes.ACC_STATIC) != 0;
        this.isFinal = (access & Opcodes.ACC_FINAL) != 0;
        this.fullName = name;

        super.visit(version, access, name, signature, superName, interfaces);
    }

    private void extractClassNameDetails(String name) {
        String[] nameParts = name.split("/");
        this.className = nameParts[nameParts.length - 1];
        this.packageName = String.join("/", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        EAnnotations annotation = Annotations.getEAnnotation(AnnotationType.CLASS, descriptor);
        if (annotation != null) {
            Annotations.consumeAnnotation(annotation, this.writer, DataHelper.newActionData(className, packageName, fullName, isStatic, isFinal));
            LogManager.info("Annotation found in class " + className + ": " + descriptor);
        }

        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldVisitor fieldVisitor = super.visitField(access, name, descriptor, signature, value);
        boolean isStaticField = (access & Opcodes.ACC_STATIC) != 0;
        boolean isFinalField = (access & Opcodes.ACC_FINAL) != 0;

        ActionData classData = DataHelper.newActionData(className, packageName, fullName, isStaticField, isFinalField);

        return VisitorHelper.newFieldVisitor(fieldVisitor, this.writer, DataHelper.newFieldData(classData, name, descriptor));
    }
}
