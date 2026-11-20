package zed.rainxch.core.data.local.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import zed.rainxch.core.data.local.IosAppDataPaths
import zed.rainxch.core.data.local.db.migrations.MIGRATIONS

fun initDatabase(): AppDatabase {
    IosAppDataPaths.ensureDir()
    return Room
        .databaseBuilder<AppDatabase>(
            name = IosAppDataPaths.filePath("github_store.db"),
        ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .addMigrations(*MIGRATIONS)
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .build()
}
