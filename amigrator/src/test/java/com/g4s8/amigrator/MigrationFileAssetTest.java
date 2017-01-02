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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, manifest = Config.NONE, assetDir = Config.DEFAULT_ASSET_FOLDER)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class MigrationFileAssetTest {

    @Test
    public void version() {
        MatcherAssert.assertThat(
            new MigrationFileAsset(
                RuntimeEnvironment.application.getAssets(), new File("42.sql")
            ).version(),
            Matchers.equalTo(42)
        );
    }

    @Test
    public void migrations() throws IOException {
        final List<Migration> migrations = new MigrationFileAsset(
            RuntimeEnvironment.application.getAssets(), new File("files", "88.sql")
        ).migrations();
        MatcherAssert.assertThat(
            migrations,
            Matchers.hasSize(2)
        );
    }

    @Test
    public void cornerCases() throws Exception {
        MatcherAssert.assertThat(
            "11.sql",
            new MigrationFileAsset(
                RuntimeEnvironment.application.getAssets(), new File("files", "11.sql")
            ).migrations(),
            Matchers.hasSize(2)
        );
        MatcherAssert.assertThat(
            "22.sql",
            new MigrationFileAsset(
                RuntimeEnvironment.application.getAssets(), new File("files", "22.sql")
            ).migrations(),
            Matchers.hasSize(2)
        );
        MatcherAssert.assertThat(
            "33.sql",
            new MigrationFileAsset(
                RuntimeEnvironment.application.getAssets(), new File("files", "33.sql")
            ).migrations(),
            Matchers.hasSize(0)
        );
        MatcherAssert.assertThat(
            "44.sql",
            new MigrationFileAsset(
                RuntimeEnvironment.application.getAssets(), new File("files", "44.sql")
            ).migrations(),
            Matchers.hasSize(0)
        );
        MatcherAssert.assertThat(
            "55.sql",
            new MigrationFileAsset(
                RuntimeEnvironment.application.getAssets(), new File("files", "55.sql")
            ).migrations(),
            Matchers.hasSize(2)
        );
    }
}