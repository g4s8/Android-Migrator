package com.g4s8.amigrator;

import android.support.annotation.NonNull;
import com.g4s8.amigrator.misc.IterableWrap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Iterable split sql statements
 */
final class SplitStatements extends IterableWrap<String> {

    private static final List<Pattern> PTN_IGNORE = Arrays.asList(
        Pattern.compile("(?s)/\\\\*.*?\\\\*/"),
        Pattern.compile("(?:--.*\\R?)")
    );

    public SplitStatements(@NonNull final String text) {
        super(split(text));
    }

    private static Iterable<String> split(final String text) {
        String tmp = text;
        for (final Pattern ptn : SplitStatements.PTN_IGNORE) {
            tmp = ptn.matcher(tmp).replaceAll("");
        }
        System.out.println(tmp);
        final List<String> split = new LinkedList<>();
        for (final String statement : tmp.trim().split(";")) {
            final String trimmed = statement.trim();
            if (trimmed.length() > 0) {
                split.add(trimmed);
            }
        }
        return split;
    }
}
