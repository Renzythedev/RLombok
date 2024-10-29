package me.renzy.api.data;

public record FieldData(ClassData classData, String fieldName, String fieldType, boolean isStatic, boolean isFinal) implements IData{
}
