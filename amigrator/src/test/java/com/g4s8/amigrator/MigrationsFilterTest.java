package com.g4s8.amigrator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test migrations filters.
 */
public final class MigrationsFilterTest {

    /**
     * Skip low versions
     */
    @Test
    public void skipLo() {
        final MigrationsFilter filter = new MigrationsFilter(
            Collections.singletonList(
                new MigrationFileFake(4)
            ),
            10,
            12
        );
        MatcherAssert.assertThat(
            filter.iterator().hasNext(),
            Matchers.is(false)
        );
    }

    /**
     * Skip high version
     */
    @Test
    public void skipHi() {
        final MigrationsFilter filter = new MigrationsFilter(
            Collections.singletonList(
                new MigrationFileFake(42)
            ),
            10,
            12
        );
        MatcherAssert.assertThat(
            filter.iterator().hasNext(),
            Matchers.is(false)
        );
    }

    /**
     * Filter migrations.
     */
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

    /**
     * Fake for migration file.
     */
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
}
