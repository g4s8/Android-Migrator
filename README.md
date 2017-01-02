# Android-Migrator
Android migrator will help you to apply migrations from assets files via default database framework (`SQLiteOpenHelper`).

## Setup
Add migration files to assets folder "migrations" (folder name can be customized). 
Each migration file should be named with format: `<version>.sql`, where visersion is integer number.
```
assets
  migrations
    1.sql
    2.sql
    3.sql
```

Migration files should be filled with sqlite statements, ended with semicolon: ';'. Migrator ignores empty lines and spaces.
Comments ('--') are not supported yet.
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    name TEXT
);

INSERT INTO users (name) VALUES ('Jimmy');
```

Apply migrations in you android sqlite-helper.
```java
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DBAdapter extends SQLiteOpenHelper {

    private final SQLiteMigrations migrations;
    private final int version;
    
    public DBAdapter(final Context ctx, final int version) {
        super(context, DBAdapter.class.getName(), null, version);
        migrations = new SQLiteMigrations(ctx); // or provide custom assets folder as second parameter
        this.version = version;
    }
    
    @Override
    public void onCreate(final SQLiteDatabase db) {
        migrations.apply(db, 0, version);
    }
    
    @Override
    public void onUpgrade(
        final SQLiteDatabase db,
        final int oldVersion,
        final int newVersion
    ) {
        migrations.apply(db, oldVersion, newVersion);
    }
}
```