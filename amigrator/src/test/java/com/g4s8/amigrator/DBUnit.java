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
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import org.junit.Assert;


final class DBUnit extends SQLiteOpenHelper {

    private final int version;
    private final SQLiteMigrations migrations;

    DBUnit(
        @NonNull final Context context,
        @NonNull final Integer version
    ) {
        this(context, new SQLiteMigrations(context), version);
    }

    DBUnit(
        @NonNull final Context context,
        @NonNull final Integer version,
        @NonNull final String customFolder
    ) {
        this(context, new SQLiteMigrations(context, customFolder), version);
    }

    private DBUnit(
        @NonNull final Context context,
        @NonNull final SQLiteMigrations migrations,
        @NonNull final Integer version
    ) {
        super(context, DBUnit.class.getName(), null, version);
        this.migrations = migrations;
        this.version = version;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            migrations.apply(db, 0, version);
        } catch (MigrationException e) {
            e.printStackTrace();
            Assert.fail("onCreate MigrationException " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            migrations.apply(db, oldVersion, newVersion);
        } catch (MigrationException e) {
            e.printStackTrace();
            Assert.fail("onUpgrade MigrationException " + e.getMessage());
        }
    }
}
