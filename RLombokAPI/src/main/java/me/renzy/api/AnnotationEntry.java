package me.renzy.api;

import me.renzy.api.type.AnnotationType;

import java.lang.annotation.Annotation;
import java.util.Map;

public final class AnnotationEntry implements Map.Entry<AnnotationType,Class<? extends Annotation>> {

    private final Class<? extends Annotation> annotationClass;
    private final AnnotationType type;

    public AnnotationEntry(AnnotationType type,Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
        this.type = type;
    }

    @Override
    public AnnotationType getKey() {
        return this.type;
    }

    @Override
    public Class<? extends Annotation> getValue() {
        return this.annotationClass;
    }

    @Override
    public Class<? extends Annotation> setValue(Class<? extends Annotation> value) {
        throw new RuntimeException("AnnotationEntry cannot be modified.");
    }
}
