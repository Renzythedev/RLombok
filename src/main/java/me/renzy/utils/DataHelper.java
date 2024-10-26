package me.renzy.utils;

import me.renzy.annotations.datas.ActionData;
import me.renzy.annotations.datas.FieldData;

import javax.annotation.Nonnull;

public final class DataHelper {

    @Nonnull
    public static ActionData newActionData(@Nonnull String className, @Nonnull String packageName, @Nonnull String fullName, boolean isStatic, boolean isFinal) {
        return new AbstractActionData(className,packageName,fullName,isStatic,isFinal) {};
    }

    @Nonnull
    public static FieldData newFieldData(@Nonnull ActionData data, @Nonnull String fieldName, @Nonnull String fieldType) {
        return new FieldDataImpl(data.getClassName(), data.getPackageName(), data.getFullName(), data.isStatic(), data.isFinal(), fieldName,fieldType);
    }

    private static abstract class AbstractActionData implements ActionData {

        private final String className;
        private final String packageName;
        private final String fullName;
        private final boolean isStatic;
        private final boolean isFinal;

        private AbstractActionData(String className, String packageName, String fullName, boolean isStatic, boolean isFinal) {
            this.className = className;
            this.packageName = packageName;
            this.fullName = fullName;
            this.isStatic = isStatic;
            this.isFinal = isFinal;
        }


        @Nonnull
        @Override
        public String getClassName() {
            return this.className;
        }

        @Nonnull
        @Override
        public String getPackageName() {
            return this.packageName;
        }

        @Nonnull
        @Override
        public String getFullName() {
            return this.fullName;
        }

        @Override
        public boolean isStatic() {
            return this.isStatic;
        }

        @Override
        public boolean isFinal() {
            return this.isFinal;
        }
    }

    private static final class FieldDataImpl extends AbstractActionData implements FieldData {

        private final String fieldName;
        private final String fieldType;

        private FieldDataImpl(String className, String packageName, String fullName, boolean isStatic, boolean isFinal, @Nonnull String fieldName, @Nonnull String fieldType) {
            super(className, packageName, fullName, isStatic, isFinal);
            this.fieldName = fieldName;
            this.fieldType = fieldType;
        }

        @Nonnull
        @Override
        public String getFieldName() {
            return this.fieldName;
        }

        @Nonnull
        @Override
        public String getFieldType() {
            return this.fieldType;
        }
    }

    private DataHelper() {}
}
