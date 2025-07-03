package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.converters

import androidx.room.TypeConverter
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory

/**
 * Custom type converters for Room db.
 * These converters allow Room to persist types such as [List] and [RecipeCategory],
 * which are not native to Room. Room only supports primitive types and strings by default.
 */
class Converters {

    /**
     * Converts a [List] of [String] into a single [String] using the '%%%' a delimiter.
     *
     * This is used to store lists (ingredients or instructions) as a single db entry.
     *
     * @param list The list of strings to convert.
     * @return A single string representation of the list.
     */
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString("%%%")
    }

    /**
     * Converts a delimited [String] back into a [List] of [String].
     *
     * This reverses the operation of [fromList]. If the input is empty, returns an empty list.
     *
     * @param data The delimited string to convert.
     * @return The resulting list of strings.
     */
    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split("%%%")
    }

    /**
     * Converts a [RecipeCategory] enum into a [String] for database storage.
     *
     * @param category The enum value to convert.
     * @return The name of the enum as a string.
     */
    @TypeConverter
    fun fromCategory(category: RecipeCategory): String = category.name

    /**
     * Converts a [String] back into a [RecipeCategory] enum.
     *
     * @param name The name of the enum value.
     * @return The corresponding [RecipeCategory] enum.
     */
    @TypeConverter
    fun toCategory(name: String): RecipeCategory = RecipeCategory.valueOf(name)
}