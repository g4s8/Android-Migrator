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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, manifest = Config.NONE, assetDir = Config.DEFAULT_ASSET_FOLDER)
public class SQLiteMigrationsTest {

    private static void assertVersions(final Cursor cursor, final int... expected) {
        Assert.assertTrue(cursor.moveToFirst());
        if (expected.length > 0) {
            Assert.assertEquals(expected[0], cursor.getInt(0));
        }
        for (int i = 1; i < expected.length; i++) {
            Assert.assertTrue(cursor.moveToNext());
            Assert.assertEquals(expected[i], cursor.getInt(0));
        }
        Assert.assertFalse(cursor.moveToNext());
    }

    @Test
    public void fullCreate() throws IOException {
        final DBUnit unit = new DBUnit(RuntimeEnvironment.application, 3);
        final SQLiteDatabase database = unit.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT value FROM versions", new String[0]);
            assertVersions(cursor, 1, 2, 3);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
            unit.close();
        }
    }

    @Test
    public void sequentialUpgrade() {
        final Context ctx = RuntimeEnvironment.application.getApplicationContext();
        final int[] VERSIONS = new int[]{1, 2, 3};
        for (int i = 1; i <= 3; i++) {
            final SQLiteOpenHelper unit = new DBUnit(ctx, i);
            final SQLiteDatabase db = unit.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT value FROM versions", new String[0]);
                assertVersions(cursor, Arrays.copyOf(VERSIONS, i));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
            unit.close();
        }
    }

    @Test
    public void customFolder() {
        final Context ctx = RuntimeEnvironment.application;

        final SQLiteOpenHelper unit = new DBUnit(ctx, 1, "custom_migrations");
        final SQLiteDatabase database = unit.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT value FROM custom_versions", new String[0]);
            assertVersions(cursor, 1);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
    }
}