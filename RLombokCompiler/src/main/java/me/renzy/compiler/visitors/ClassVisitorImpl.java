package me.renzy.compiler.visitors;

import me.renzy.api.Annotations;
import me.renzy.api.EAnnotations;
import me.renzy.api.data.ClassData;
import me.renzy.api.data.FieldData;
import me.renzy.api.type.AnnotationType;
import me.renzy.utils.LogManager;
import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

final class ClassVisitorImpl extends ClassVisitor {

    private final ClassWriter classWriter;
    private ClassData data;

    private final AtomicLong counter = new AtomicLong(0);

    ClassVisitorImpl(int api, ClassWriter classWriter) {
        super(api,classWriter);
        this.classWriter = classWriter;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        LogManager.info(String.format("Visiting class '%s'", name));
        String[] nameParts = name.split("/");

        this.data = new ClassData(nameParts[0], getPackageName(nameParts), name);
        super.visit(version, access, name, signature, superName, interfaces);
    }


    private String getPackageName(String[] name) {
        StringBuilder packageName = new StringBuilder();
        for (String s : Arrays.copyOfRange(name,0,name.length -1)) {
            packageName.append(s).append("/");
        }
        return packageName.toString();
    }


    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        EAnnotations annotation = Annotations.searchAnnotation(AnnotationType.CLASS, descriptor.substring(1,descriptor.length() -1));
        if (annotation != null) {
            annotation.consume(this.classWriter, this.data);
            LogManager.info(String.format("Annotation '%s' processed successfully for class '%s'",
                    annotation, this.data.fullName()));
            this.counter.incrementAndGet();
        }

        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return Visitors.newFieldVisitor(super.visitField(access, name, descriptor, signature, value),this.classWriter,new FieldData(this.data,name,descriptor,(access & Opcodes.ACC_STATIC) != 0,(access & Opcodes.ACC_FINAL) != 0));
    }

    @Override
    public void visitEnd() {
        LogManager.info(String.format("Finished processing class '%s'. Total annotations applied: %s",
                this.data.fullName(), this.counter.get()));
        super.visitEnd();
    }

}
