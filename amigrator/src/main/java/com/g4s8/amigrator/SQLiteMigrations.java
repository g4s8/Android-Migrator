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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

/**
 * SQLite migrations, located in provided context assets folder in .sql files.
 * <p>
 * Default folder for migrations is 'migrations'.
 * <p>
 */
@SuppressWarnings("WeakerAccess")
@Keep
public final class SQLiteMigrations {

    private static final String DEFAULT_FOLDER = "migrations";

    private static final String LTAG = SQLiteMigrations.class.getName();

    private final Context context;
    private final String folder;

    /**
     * New migrations from default assets folder.
     *
     * @param context current application context.
     */
    @Keep
    public SQLiteMigrations(
        @NonNull final Context context
    ) {
        this(context, DEFAULT_FOLDER);
    }

    /**
     * New migrations from custom assets folder.
     *
     * @param context current application content.
     * @param folder  custom folder.
     */
    @Keep
    public SQLiteMigrations(
        @NonNull final Context context,
        @NonNull final String folder
    ) {
        this.context = context;
        this.folder = folder;
    }

    /**
     * Apply migrations to provided database.
     *
     * @param database where migrations will be applied.
     * @param from     current db version
     * @param to       new db version
     * @throws MigrationException if migrations failed.
     */
    @Keep
    public void apply(
        @NonNull final SQLiteDatabase database,
        final int from,
        final int to
    ) throws MigrationException {
        Log.i(LTAG, String.format("Apply migration: %d -> %d", from, to));
        database.beginTransaction();
        try {
            final MigrationsFilter migrations;
            try {
                migrations = new MigrationsFilter(
                    new OrderedByVersionFiles(context.getAssets(), folder),
                    from,
                    to
                );
            } catch (IOException e) {
                throw new MigrationException("Failed to read migrations", e);
            }
            for (MigrationFile migrationFile : migrations) {
                try {
                    for (Migration migration : migrationFile.migrations()) {
                        migration.apply(database);
                    }
                } catch (IOException e) {
                    throw new MigrationException("Failed to apply migration", e);
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
