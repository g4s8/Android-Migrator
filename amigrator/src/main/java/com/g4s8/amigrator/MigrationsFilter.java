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
package com.g4s8.amigrator;

import android.support.annotation.NonNull;

import com.android.internal.util.Predicate;
import com.g4s8.amigrator.misc.IterableEnum;
import com.g4s8.amigrator.misc.ListFilter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Filter migrations on version.
 * <p>
 * Filter from 3-th to 5-th version means, that 3-th version has been already applied,
 * we should select 4-th and 5-th versions.
 */
final class MigrationsFilter implements Iterable<MigrationFile> {

    private final List<MigrationFile> origin;

    MigrationsFilter(
        @NonNull final Iterable<? extends MigrationFile> origin,
        final int versionStart,
        final int versionEnd
    ) {
        this.origin = new ListFilter<>(
            Collections.list(new IterableEnum<>(origin)),
            new MigrationFileVersionPredicate(versionStart, versionEnd)
        );
    }

    @Override
    public Iterator<MigrationFile> iterator() {
        return origin.iterator();
    }

    private static final class MigrationFileVersionPredicate implements Predicate<MigrationFile> {

        private final int vStart;
        private final int vEnd;

        private MigrationFileVersionPredicate(int vStart, int vEnd) {
            this.vStart = vStart;
            this.vEnd = vEnd;
        }

        @Override
        public boolean apply(MigrationFile migrationFile) {
            return apply(migrationFile.version());
        }

        private boolean apply(final int version) {
            return version > vStart && version <= vEnd;
        }
    }
}
