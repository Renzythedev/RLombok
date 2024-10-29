package me.renzy.api;

import me.renzy.annotations.Getter;
import me.renzy.annotations.Setter;
import me.renzy.api.type.AnnotationType;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Utility class for managing and retrieving annotation types and classes.
 * This class provides methods for mapping between custom {@link EAnnotations} enums,
 * annotation classes, and {@link AnnotationType}s.
 * <p>
 * It allows you to:
 * <ul>
 *     <li>Retrieve the {@link AnnotationType} of a given {@link EAnnotations} or annotation class.</li>
 *     <li>Retrieve the annotation class associated with a specific {@link EAnnotations} enum.</li>
 *     <li>Map an annotation class back to its corresponding {@link EAnnotations} enum.</li>
 *     <li>Find annotations of a specific type based on descriptor strings.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Note: This class is designed to be used statically and cannot be instantiated.
 * </p>
 *
 * @author Renzy
 */
public final class Annotations {

    /**
     * Holds the mapping between {@link EAnnotations} enums and {@link AnnotationEntry} objects.
     * Each entry in the map represents an annotation type and its associated class.
     */
    private static final Map<EAnnotations, AnnotationEntry> ANNOTATIONS = new HashMap<>();

    static {
        ANNOTATIONS.put(EAnnotations.GETTER,new AnnotationEntry(AnnotationType.FIELD, Getter.class));
        ANNOTATIONS.put(EAnnotations.SETTER,new AnnotationEntry(AnnotationType.FIELD, Setter.class));
    }

    /**
     * Returns the {@link AnnotationType} associated with a given {@link EAnnotations} enum.
     *
     * @param annotation the {@link EAnnotations} enum representing the annotation
     * @return the {@link AnnotationType} associated with the specified enum
     * @throws NullPointerException if the provided {@code annotation} is null
     */
    public static AnnotationType getAnnotationType(EAnnotations annotation) {
        Objects.requireNonNull(annotation, "eannotation");
        return ANNOTATIONS.get(annotation).getKey();
    }

    /**
     * Returns the {@link AnnotationType} associated with a specific annotation class.
     *
     * @param annotation the annotation class
     * @return the {@link AnnotationType} associated with the specified annotation class
     * @throws NullPointerException if the provided {@code annotation} class is null
     */
    public static AnnotationType getAnnotationType(Class<? extends Annotation> annotation) {
        Objects.requireNonNull(annotation, "annotation class");
        return getAnnotationType(getAnnotation(annotation));
    }

    /**
     * Returns the annotation class associated with a specific {@link EAnnotations} enum.
     *
     * @param annotation the {@link EAnnotations} enum representing the annotation
     * @return the annotation class associated with the specified enum
     * @throws NullPointerException if the provided {@code annotation} is null
     */
    public static Class<? extends Annotation> getAnnotationClass(EAnnotations annotation) {
        Objects.requireNonNull(annotation, "eannotation");
        return ANNOTATIONS
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() == annotation)
                .findFirst()
                .orElse(null)
                .getValue().getValue();
    }

    /**
     * Returns the {@link EAnnotations} enum associated with a given annotation class.
     *
     * @param annotationClass the annotation class
     * @return the {@link EAnnotations} enum associated with the specified annotation class
     * @throws NullPointerException if the provided {@code annotationClass} is null
     */
    public static EAnnotations getAnnotation(Class<? extends Annotation> annotationClass) {
        Objects.requireNonNull(annotationClass, "annotation class");
        return ANNOTATIONS
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getValue().equals(annotationClass))
                .findFirst()
                .orElse(null)
                .getKey();
    }

    /**
     * Searches for an annotation of a specific {@link AnnotationType} and descriptor.
     *
     * @param type       the {@link AnnotationType} to search for
     * @param descriptor the descriptor string of the annotation class
     * @return the {@link EAnnotations} enum corresponding to the matching annotation
     * @throws IllegalArgumentException if no matching annotation is found
     */
    public static EAnnotations searchAnnotation(AnnotationType type, String descriptor) {
        Class<? extends Annotation> clazz = getAnnotations(type)
                .stream()
                .filter(annotation -> (annotation.getName().replace('.', '/').equals(descriptor)))
                .findFirst()
                .orElse(null);

        return clazz == null ? null : getAnnotation(clazz);
    }

    /**
     * Returns a set of annotation classes associated with a specific {@link AnnotationType}.
     *
     * @param type the {@link AnnotationType} to search for
     * @return a set of annotation classes associated with the specified type
     */
    public static Set<Class<? extends Annotation>> getAnnotations(AnnotationType type) {
        Set<Class<? extends Annotation>> set = new HashSet<>();

        for (Map.Entry<EAnnotations, AnnotationEntry> entry : ANNOTATIONS.entrySet())
            if (entry.getValue().getKey() == type)
                set.add(entry.getValue().getValue());

        return set;
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Annotations() {}
}
