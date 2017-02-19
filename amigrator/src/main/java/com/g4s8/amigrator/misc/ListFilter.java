/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 g4s8 (g4s8.public@gmail.com)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.g4s8.amigrator.misc;

import android.support.annotation.NonNull;
import com.android.internal.util.Predicate;
import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

/**
 * Filter list with predicate.
 *
 * @param <T> list item type.
 */
public final class ListFilter<T> extends AbstractList<T> {

    private final List<T> origin;

    /**
     * Ctor.
     *
     * @param source    source list.
     * @param predicate predicate to apply.
     */
    public ListFilter(@NonNull final List<T> source, @NonNull final Predicate<T> predicate) {
        origin = filter(source, predicate);
    }

    @Override
    public T get(int index) {
        return origin.get(index);
    }

    @Override
    public int size() {
        return origin.size();
    }

    private static <T> List<T> filter(
        @NonNull final List<T> origin,
        @NonNull final Predicate<T> predicate
    ) {
        final List<T> res = new LinkedList<>();
        for (T item : origin) {
            if (predicate.apply(item)) {
                res.add(item);
            }
        }
        return res;
    }
}
