package com.g4s8.amigrator;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Test split statements.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class,
    sdk = 23,
    manifest = Config.NONE,
    assetDir = Config.DEFAULT_ASSET_FOLDER
)
public final class SplitStatementsTest {

    @Test
    public void removeSingleLineCommentTest() {
        final String statement = "STATEMENT1";
        MatcherAssert.assertThat(
            new SplitStatements("-- single line comment\n" + statement + ";").iterator().next(),
            Matchers.equalTo(statement)
        );
    }

    @Test
    public void removeManySingleLineCommentsTest() {
        final String statement = "STATEMENT2";
        MatcherAssert.assertThat(
            new SplitStatements("-- single line comment\n" + statement + "\n--one more comment\n-- and last comment").iterator().next(),
            Matchers.equalTo(statement)
        );
    }

    @Test
    public void removeMultiLineCommentTest() {
        final String statement = "STATEMENT3";
        MatcherAssert.assertThat(
            new SplitStatements("/* multi \n line comment */" + statement + ";").iterator().next(),
            Matchers.equalTo(statement)
        );
    }

    @Test
    public void removeManyMultiLineCommentTest() {
        final String statement = "STATEMENT4";
        MatcherAssert.assertThat(
            new SplitStatements("/* multi \n line \n comment \r\n */" + statement + ";\n/* another \n comment */").iterator().next(),
            Matchers.equalTo(statement)
        );
    }
}