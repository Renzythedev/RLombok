package me.renzy.annotations;

import me.renzy.annotations.datas.ActionData;
import me.renzy.annotations.datas.FieldData;
import me.renzy.utils.TypeHelper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

import javax.annotation.Nonnull;

public enum EAnnotations implements AnnotationConsumer {

    GETTER {
        @Override
        public void consume(@Nonnull ClassWriter classWriter, @Nonnull ActionData actionData) {
            FieldData data = (FieldData) actionData;

            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | (data.isStatic() ? ACC_STATIC : 0), "get" + data.getFieldName(), TypeHelper.getReturnType(data.getFieldType()), null, null);
            methodVisitor.visitCode();

            if (!data.isStatic()) methodVisitor.visitVarInsn(ALOAD, 0);

            methodVisitor.visitFieldInsn(data.isStatic() ? GETSTATIC : GETFIELD, data.getFullName(), data.getFieldName(), data.getFieldType());
            methodVisitor.visitInsn(TypeHelper.getReturnCode(TypeHelper.getReturnType(data.getFieldType())));
            methodVisitor.visitEnd();
        }
    },

    SETTER {
        @Override
        public void consume(@Nonnull ClassWriter classWriter, @Nonnull ActionData actionData) {
            FieldData data = (FieldData) actionData;
            if (data.isFinal()) throw new RuntimeException("A field with a setter cannot be final");

            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | (data.isStatic() ? ACC_STATIC : 0), "set" + data.getFieldName(), TypeHelper.getParameterType(data.getFieldType()), null, null);
            methodVisitor.visitCode();

            if (!data.isStatic()) methodVisitor.visitVarInsn(ALOAD, 0);

            methodVisitor.visitVarInsn(TypeHelper.getLoadCode(data.getFieldType()), data.isStatic() ? 0 : 1);
            methodVisitor.visitVarInsn(TypeHelper.getLoadCode(data.getFieldType()), data.isStatic() ? 0 : 1);
            methodVisitor.visitFieldInsn(data.isStatic() ? PUTSTATIC : PUTFIELD, data.getFullName(), data.getFieldName(), data.getFieldType());

            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitEnd();
        }
    };
}

