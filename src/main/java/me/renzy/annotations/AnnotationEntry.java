package me.renzy.annotations;

import me.renzy.annotations.types.AnnotationType;

import java.lang.annotation.Annotation;
import java.util.Map;

public final class AnnotationEntry implements Map.Entry<AnnotationType,Class<? extends Annotation>> {

    private final AnnotationType type;
    private final Class<? extends Annotation> annotation;

    public AnnotationEntry(AnnotationType type, Class<? extends Annotation> annotation) {
        this.type = type;
        this.annotation = annotation;
    }

    @Override
    public AnnotationType getKey() {
        return this.type;
    }

    @Override
    public Class<? extends Annotation> getValue() {
        return this.annotation;
    }

    @Override
    public Class<? extends Annotation> setValue(Class<? extends Annotation> value) {
        throw new RuntimeException("AnnotationEntry cannot be modified.");
    }
}
