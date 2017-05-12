package com.g4s8.amigrator.misc;

import android.support.annotation.NonNull;
import java.util.Iterator;

/**
 * Mapped items from S to D.
 *
 * @param <S> source type
 * @param <D> destination type
 */
public final class MappedIterable<S, D> implements Iterable<D> {

    private final Iterable<S> origin;
    private final Map<S, D> map;

    /**
     * Ctor.
     *
     * @param origin source iterable
     * @param map    map function
     */
    public MappedIterable(@NonNull final Iterable<S> origin, @NonNull final Map<S, D> map) {
        this.origin = origin;
        this.map = map;
    }

    @Override
    public Iterator<D> iterator() {
        return new Iterator<D>() {
            private final Iterator<S> origin = MappedIterable.this.origin.iterator();

            @Override
            public boolean hasNext() {
                return this.origin.hasNext();
            }

            @Override
            public D next() {
                return MappedIterable.this.map.apply(this.origin.next());
            }
        };
    }

    /**
     * Map function.
     *
     * @param <S> source
     * @param <D> destination
     */
    @SuppressWarnings("PMD.ShortClassName")
    public interface Map<S, D> {

        /**
         * Apply map function.
         *
         * @param src source
         * @return destination
         */
        @NonNull
        D apply(@NonNull final S src);
    }
}
