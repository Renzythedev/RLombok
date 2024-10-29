package me.renzy.api;

import me.renzy.api.data.FieldData;
import me.renzy.api.data.IData;
import me.renzy.api.type.TypeHelper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public enum EAnnotations implements AnnotationConsumer{

    GETTER {
        @Override
        public void consume(ClassWriter writer, IData data) {
            FieldData fieldData = (FieldData) data;

            MethodVisitor methodVisitor = writer.visitMethod( Opcodes.ACC_PUBLIC | (fieldData.isStatic() ? Opcodes.ACC_STATIC : 0), "get" + fieldData.fieldName(), TypeHelper.getDescriptor(TypeHelper.Context.RETURN, fieldData.fieldType()),null,null);
            methodVisitor.visitCode();
            if (!fieldData.isStatic())
              methodVisitor.visitVarInsn(TypeHelper.getLoadCode(fieldData.fieldType()),0);
            methodVisitor.visitFieldInsn(fieldData.isStatic() ? Opcodes.GETSTATIC : Opcodes.GETFIELD,fieldData.classData().fullName(), fieldData.fieldName(),fieldData.fieldType());
            methodVisitor.visitInsn(TypeHelper.getReturnCode(fieldData.fieldType()));
            methodVisitor.visitEnd();


        }
    },

    SETTER {
        @Override
        public void consume(ClassWriter writer, IData data) {
            FieldData fieldData = (FieldData) data;

            if (fieldData.isFinal())
                throw new RuntimeException("A field with a setter cannot be final");

            MethodVisitor methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC | (fieldData.isStatic() ? Opcodes.ACC_STATIC : 0), "set" + fieldData.fieldName(), TypeHelper.getDescriptor(TypeHelper.Context.PARAM, fieldData.fieldType()),null,null);
            methodVisitor.visitCode();
            if (!fieldData.isStatic())
                methodVisitor.visitVarInsn(TypeHelper.getLoadCode(fieldData.fieldType()),0);

            methodVisitor.visitVarInsn(TypeHelper.getLoadCode(fieldData.fieldType()),fieldData.isStatic() ? 0 : 1);
            methodVisitor.visitFieldInsn(fieldData.isStatic() ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD,fieldData.classData().fullName(), fieldData.fieldName(),fieldData.fieldType());
            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitEnd();

        }
    }
}
