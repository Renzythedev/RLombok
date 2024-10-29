package me.renzy.compiler.visitors;

import me.renzy.api.Annotations;
import me.renzy.api.EAnnotations;
import me.renzy.api.data.FieldData;
import me.renzy.api.type.AnnotationType;
import me.renzy.utils.LogManager;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

import java.util.concurrent.atomic.AtomicLong;

final class FieldVisitorImpl extends FieldVisitor {

    private final FieldData fieldData;
    private final ClassWriter classWriter;

    private final AtomicLong counter = new AtomicLong(0);

    FieldVisitorImpl(int api, FieldVisitor fieldVisitor, FieldData fieldData, ClassWriter classWriter) {
        super(api, fieldVisitor);
        this.fieldData = fieldData;
        this.classWriter = classWriter;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        EAnnotations annotation = Annotations.searchAnnotation(AnnotationType.FIELD, descriptor.substring(1,descriptor.length() -1));
        if (annotation != null) {
            annotation.consume(this.classWriter, this.fieldData);
            LogManager.info(String.format("Annotation '%s' processed successfully for field in class '%s'",
                    annotation, this.fieldData.classData().fullName()));
            this.counter.incrementAndGet();
        }

        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitEnd() {
        LogManager.info(String.format("Field '%s' in class '%s' has finished processing with %s annotations applied.",
                this.fieldData.fieldName(), this.fieldData.classData().fullName(), this.counter.get()));
        super.visitEnd();
    }

}
