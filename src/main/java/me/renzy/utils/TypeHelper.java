package me.renzy.utils;

import com.google.common.base.Preconditions;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nonnull;

public final class TypeHelper {

    @Nonnull
    public static String getReturnType(@Nonnull String fieldType) {
        Preconditions.checkArgument(!fieldType.isEmpty(),"field type");
        FieldType type = FieldType.from(fieldType);

        if (type == FieldType.OBJECT) {
            return "()"+String.format(type.getDescriptor(), fieldType);
        }else {
            return "()" + type.getDescriptor();
        }
    }

    @Nonnull
    public static String getParameterType(@Nonnull String fieldType) {
        Preconditions.checkArgument(!fieldType.isEmpty(),"field type");
        FieldType type = FieldType.from(fieldType);

        if (type == FieldType.OBJECT) {
            return "("+String.format(type.getDescriptor(), fieldType)+")V";
        }else {
            return "("+type.getDescriptor()+")V";
        }
    }

    public static int getLoadCode(@Nonnull String fieldType) {
        Preconditions.checkArgument(!fieldType.isEmpty(),"field type");
        FieldType type = FieldType.from(fieldType);

        return type.getLoadOpcode();
    }

    public static int getReturnCode(@Nonnull String fieldType) {
        Preconditions.checkArgument(!fieldType.isEmpty(),"field type");
        FieldType type = FieldType.from(fieldType);

        return type.getReturnOpcode();
    }

    private enum FieldType {

        INT("I", Opcodes.ILOAD,Opcodes.IRETURN),
        FLOAT("F", Opcodes.FLOAD,Opcodes.FRETURN),
        DOUBLE("D", Opcodes.DLOAD,Opcodes.DLOAD),
        LONG("J", Opcodes.LLOAD,Opcodes.LRETURN),
        OBJECT("%s",Opcodes.ALOAD, Opcodes.ARETURN);

        private final String descriptor;
        private final int loadOpcode;
        private final int returnOpcode;

        FieldType(String descriptor, int loadOpcode, int returnOpcode) {
            this.descriptor = descriptor;
            this.loadOpcode = loadOpcode;
            this.returnOpcode = returnOpcode;
        }

        public int getLoadOpcode() {
            return loadOpcode;
        }

        public int getReturnOpcode() {
            return returnOpcode;
        }

        public String getDescriptor() {
            return descriptor;
        }

        @Nonnull
        public static FieldType from(@Nonnull String fromType) {
            return switch (fromType) {
                case "I" -> INT;
                case "L" -> LONG;
                case "F" -> FLOAT;
                case "D" -> DOUBLE;
                default -> OBJECT;
            };
        }
    }

    private TypeHelper() {
    }
}
