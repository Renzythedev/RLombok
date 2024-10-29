package me.renzy.api;

import me.renzy.api.data.IData;
import org.objectweb.asm.ClassWriter;

public interface AnnotationConsumer {

    void consume(ClassWriter writer, IData data);
}
