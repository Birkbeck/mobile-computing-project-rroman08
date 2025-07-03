package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.converters.Converters

/**
 * Singleton Room database class for persistence storage of [Recipe] entities.
 *
 * - Uses [RecipeDao] for data access.
 * - Applies [Converters] to handle custom types (e.g., List<String>, RecipeCategory).
 */
@Database(entities = [Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    /**
     * Provides access to the [RecipeDao] interface for db operations.
     */
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        /**
         * Retrieves a singleton instance of [RecipeDatabase].
         * If it doesn't exist/hasn't been created, the db is built using Room's builder.
         *
         * @param context Application context used to initialise the db.
         * @return The singleton [RecipeDatabase] instance.
         */
        fun getInstance(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}