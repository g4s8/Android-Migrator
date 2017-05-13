package com.g4s8.amigrator;

import com.g4s8.amigrator.misc.IterableEnum;
import java.io.File;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Test for migrations with comments.<br/>
 * <a href="https://github.com/g4s8/Android-Migrator/issues/1">Guthub issue</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class,
    sdk = 23,
    manifest = Config.NONE,
    assetDir = Config.DEFAULT_ASSET_FOLDER
)
public class CommentsTest {

    /**
     * Test single line comments
     *
     * @throws Exception if failed
     */
    @Test
    public void ignoreSingleLineComments() throws Exception {
        MatcherAssert.assertThat(
            Collections.list(
                new IterableEnum<>(
                    new MigrationFileAsset(
                        RuntimeEnvironment.application.getAssets(),
                        new File("comments", "1.sql")
                    ).migrations()
                )
            ),
            Matchers.hasSize(1)
        );
    }

    /**
     * Test mixed comments
     *
     * @throws Exception if failed
     */
    @Test
    public void ignoreMixedComments() throws Exception {
        MatcherAssert.assertThat(
            Collections.list(
                new IterableEnum<>(
                    new MigrationFileAsset(
                        RuntimeEnvironment.application.getAssets(),
                        new File("comments", "2.sql")
                    ).migrations()
                )
            ),
            Matchers.hasSize(2)
        );
    }
}
