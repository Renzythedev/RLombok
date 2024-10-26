package me.renzy.annotations;

import me.renzy.annotations.datas.ActionData;
import org.objectweb.asm.ClassWriter;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface AnnotationConsumer {

    void consume(@Nonnull ClassWriter classWriter,@Nonnull ActionData data);
}
