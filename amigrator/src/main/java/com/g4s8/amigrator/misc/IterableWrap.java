package com.g4s8.amigrator.misc;

import android.support.annotation.NonNull;
import java.util.Iterator;

/**
 * Default decorator for {@link Iterable}.
 *
 * @param <T> item type
 */
@SuppressWarnings({"PMD.AbstractNaming", "checkstyle:javadoc.unknownTag"})
public abstract class IterableWrap<T> implements Iterable<T> {

    /**
     * Origin iterable.
     */
    private final Iterable<T> origin;

    /**
     * Ctor.
     *
     * @param origin iterable
     */
    protected IterableWrap(@NonNull final Iterable<T> origin) {
        this.origin = origin;
    }

    @Override
    public final Iterator<T> iterator() {
        return this.origin.iterator();
    }
}
