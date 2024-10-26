package me.renzy.annotations.datas;

import javax.annotation.Nonnull;

public interface ActionData {

    @Nonnull
    String getClassName();

    @Nonnull
    String getPackageName();

    @Nonnull
    String getFullName();

    boolean isStatic();

    boolean isFinal();
}
