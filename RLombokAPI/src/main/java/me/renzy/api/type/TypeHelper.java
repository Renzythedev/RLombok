package me.renzy.api.type;

import org.objectweb.asm.Opcodes;

public final class TypeHelper {

    public enum Context {
        RETURN,PARAM,DESCRIPTOR
    }

    public static String getDescriptor(Context context,String type) {
        Type type0 = Type.from(type);
        String descriptor = type0.getDescriptor();
        if (type0 == Type.OBJECT)
            descriptor = String.format(descriptor,type);

        switch (context) {
            case DESCRIPTOR:
                return descriptor;
            case PARAM:
                return "(" + descriptor + ")V";
            case RETURN:
                return "()" + descriptor;
            default:
                throw new AssertionError();
        }
    }

    public static int getLoadCode(String type) {
        return Type.from(type).getLoadOpcode();
    }

    public static int getReturnCode(String type) {
        return Type.from(type).getReturnOpcode();
    }

    private enum Type {

        INT("I", Opcodes.ILOAD,Opcodes.IRETURN),
        FLOAT("F", Opcodes.FLOAD,Opcodes.FRETURN),
        DOUBLE("D", Opcodes.DLOAD,Opcodes.DLOAD),
        LONG("J", Opcodes.LLOAD,Opcodes.LRETURN),
        OBJECT("%s",Opcodes.ALOAD, Opcodes.ARETURN);

        private final String descriptor;
        private final int loadOpcode;
        private final int returnOpcode;

        Type(String descriptor, int loadOpcode, int returnOpcode) {
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

        public static Type from(String fromType) {
            return switch (fromType) {
                case "I" -> INT;
                case "L" -> LONG;
                case "F" -> FLOAT;
                case "D" -> DOUBLE;
                default -> OBJECT;
            };
        }
    }

    private TypeHelper(){}
}
