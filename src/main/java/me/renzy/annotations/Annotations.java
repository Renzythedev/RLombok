package me.renzy.annotations;

import com.google.common.base.Preconditions;
import me.renzy.annotations.types.AnnotationType;
import me.renzy.annotations.types.Getter;
import me.renzy.annotations.types.Setter;
import me.renzy.annotations.datas.ActionData;
import me.renzy.utils.LogManager;
import org.objectweb.asm.ClassWriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Annotations {

    private static final EnumMap<EAnnotations, AnnotationEntry> ANNOTATIONS = new EnumMap<>(EAnnotations.class);

    static {
        ANNOTATIONS.put(EAnnotations.GETTER, new AnnotationEntry(AnnotationType.FIELD, Getter.class));
        ANNOTATIONS.put(EAnnotations.SETTER, new AnnotationEntry(AnnotationType.FIELD, Setter.class));

    }

    @Nonnull
    public static Class<? extends Annotation> getAnnotation(@Nonnull EAnnotations annotation) {
        Preconditions.checkNotNull(annotation,"annotation");
        return ANNOTATIONS.get(annotation).getValue();
    }

    @Nonnull
    public static AnnotationType getAnnotationType(@Nonnull EAnnotations annotation) {
        Preconditions.checkNotNull(annotation,"annotation");
        return ANNOTATIONS.get(annotation).getKey();
    }

    @Nonnull
    public static Set<Class<? extends Annotation>> getAnnotations(@Nonnull AnnotationType type) {
        Set<Class<? extends Annotation>> set = new HashSet<>();
        for (Map.Entry<EAnnotations, AnnotationEntry> entry : ANNOTATIONS.entrySet()) {
            if (type == entry.getValue().getKey())
                set.add(entry.getValue().getValue());
        }

        return set;
    }

    @Nullable
    public static EAnnotations getEAnnotation(@Nonnull AnnotationType type,@Nonnull String desc) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(desc, "descriptor");

        Class<? extends Annotation> annotationClass = null;

        for (Class<? extends Annotation> clazz : getAnnotations(type)) {
            if (clazz.getName().equals(desc.replace('/','.'))) {
                annotationClass= clazz;
            }
        }

        if (annotationClass == null) {
            return null;
        }

        return getAnnotation(annotationClass);
    }

    @Nullable
    public static EAnnotations getAnnotation(@Nonnull Class<? extends Annotation> annotationClass) {
        Preconditions.checkNotNull(annotationClass,"Annotation class");

        for (Map.Entry<EAnnotations, AnnotationEntry> entry : ANNOTATIONS.entrySet()) {
            AnnotationEntry annEntry = entry.getValue();
            if (annotationClass.equals(annEntry.getValue()))
                return entry.getKey();
        }

        LogManager.err("This class not an annotation.");
        return null;
    }


    public static void consumeAnnotation(@Nonnull EAnnotations annotation, @Nonnull ClassWriter classWriter, @Nonnull ActionData actionData) {
        Preconditions.checkNotNull(annotation,"annotation");
        Preconditions.checkNotNull(classWriter,"class writer");
        Preconditions.checkNotNull(actionData,"action data");
        annotation.consume(classWriter,actionData);
    }

    private Annotations() {}

}
