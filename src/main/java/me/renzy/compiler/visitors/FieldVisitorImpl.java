package me.renzy.compiler.visitors;

import me.renzy.annotations.Annotations;
import me.renzy.annotations.EAnnotations;
import me.renzy.annotations.datas.FieldData;
import me.renzy.annotations.types.AnnotationType;
import me.renzy.utils.LogManager;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

final class FieldVisitorImpl extends FieldVisitor {

    private final FieldData data;
    private final ClassWriter writer;

    FieldVisitorImpl(int api, FieldVisitor fieldVisitor, FieldData data, ClassWriter writer) {
        super(api, fieldVisitor);
        this.data = data;
        this.writer = writer;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String desc = descriptor.substring(1, descriptor.length() - 1);
        EAnnotations annotation = Annotations.getEAnnotation(AnnotationType.FIELD,desc);


        if (annotation !=null) {
            Annotations.consumeAnnotation(annotation,this.writer,this.data);
            LogManager.info(String.format("Found annotation '%s' on field '%s' (type: '%s') in class '%s'.", desc, data.getFieldName(), data.getFieldType(), data.getClassName()));
        }

        return super.visitAnnotation(descriptor, visible);
    }
}
