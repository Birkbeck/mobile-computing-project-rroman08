package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import androidx.room.TypeConverter

class Converters {
    // My List<String> to String converter
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString("%%%")
    }

    // My String to List<String> converter
    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split("%%%")
    }

    // My Enum to String converter
    @TypeConverter
    fun fromCategory(category: RecipeCategory): String = category.name

    // My String to Enum converter
    @TypeConverter
    fun toCategory(name: String): RecipeCategory = RecipeCategory.valueOf(name)
}