package me.renzy.annotations.datas;

import javax.annotation.Nonnull;

public interface FieldData extends ActionData{

    @Nonnull
    String getFieldName();

    @Nonnull
    String getFieldType();
}
