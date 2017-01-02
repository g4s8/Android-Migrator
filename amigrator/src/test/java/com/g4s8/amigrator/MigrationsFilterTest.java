package com.g4s8.amigrator;
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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MigrationsFilterTest {

    private static final class MigrationFileFake implements MigrationFile {

        private final int version;

        private MigrationFileFake(final int version) {
            this.version = version;
        }

        @Override
        public int version() {
            return version;
        }

        @Override
        public List<Migration> migrations() throws IOException {
            return Collections.emptyList();
        }
    }

    @Test
    public void filter() {
        final Iterator<MigrationFile> iter = new MigrationsFilter(
            Arrays.asList(
                new MigrationFileFake(2),
                new MigrationFileFake(3),
                new MigrationFileFake(4),
                new MigrationFileFake(5)
            ),
            3,
            5
        ).iterator();
        MatcherAssert.assertThat(
            iter.next().version(),
            Matchers.equalTo(4)
        );
        MatcherAssert.assertThat(
            iter.next().version(),
            Matchers.equalTo(5)
        );
        MatcherAssert.assertThat(
            iter.hasNext(),
            Matchers.is(false)
        );
    }
}