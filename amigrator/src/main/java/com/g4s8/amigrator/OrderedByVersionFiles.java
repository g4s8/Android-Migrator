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

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Assets migration script files, ordered by version.
 *
 * @see MigrationFileAsset
 */
final class OrderedByVersionFiles implements Iterable<MigrationFileAsset> {

    private static final Comparator<MigrationFileAsset> BY_VERSION_COMPARATOR = new Comparator<MigrationFileAsset>() {
        @Override
        public int compare(MigrationFileAsset o1, MigrationFileAsset o2) {
            return o1.version() - o2.version();
        }
    };

    private static List<MigrationFileAsset> toMigrationFiles(
        final AssetManager assets,
        final String folder,
        final List<String> names
    ) {
        List<MigrationFileAsset> files = new LinkedList<>();
        for (String name : names) {
            files.add(new MigrationFileAsset(assets, new File(folder, name)));
        }
        return files;
    }

    private static List<MigrationFileAsset> orderByVersion(final List<MigrationFileAsset> files) {
        final List<MigrationFileAsset> out = new ArrayList<>(files);
        Collections.sort(out, BY_VERSION_COMPARATOR);
        return out;
    }

    private final List<MigrationFileAsset> files;

    OrderedByVersionFiles(
        @NonNull final AssetManager assets,
        @NonNull final String folder
    ) throws IOException {
        files = orderByVersion(
            toMigrationFiles(
                assets,
                folder,
                Arrays.asList(assets.list(folder))
            )
        );
    }

    @Override
    public Iterator<MigrationFileAsset> iterator() {
        return files.iterator();
    }
}
