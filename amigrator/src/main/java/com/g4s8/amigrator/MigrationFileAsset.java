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
import com.g4s8.amigrator.misc.MappedIterable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * {@link MigrationFile} from android assets.
 */
final class MigrationFileAsset implements MigrationFile {

    private final int version;
    private final AssetManager assets;
    private final File file;

    MigrationFileAsset(@NonNull final AssetManager assets, @NonNull final File file) {
        this.version = version(file.getName());
        this.assets = assets;
        this.file = file;
    }

    public int version() {
        return version;
    }

    public Iterable<Migration> migrations() throws IOException {
        final InputStream stream = assets.open(file.getPath());
        //noinspection TryFinallyCanBeTryWithResources
        try {
            return new MappedIterable<>(
                new SplitStatements(
                    IOUtils.toString(stream)
                ),
                new MappedIterable.Map<String, Migration>() {
                    @NonNull
                    @Override
                    public Migration apply(@NonNull final String statement) {
                        return new Migration(statement);
                    }
                }
            );
        } finally {
            //noinspection ThrowFromFinallyBlock
            stream.close();
        }
    }

    private static int version(String name) {
        final int s = name.indexOf('.');
        final String base = name.substring(0, s > 0 ? s : name.length());
        return Integer.parseInt(base);
    }
}
