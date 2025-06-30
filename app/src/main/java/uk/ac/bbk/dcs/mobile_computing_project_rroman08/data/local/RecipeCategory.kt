package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

enum class RecipeCategory {
    BREAKFAST,
    BRUNCH,
    LUNCH,
    DINNER,
    DESSERT,
    OTHER;

    override fun toString(): String {
        // E.g., BREAKFAST displays as Breakfast in the UI
        // 'it' is the default name for a single parameter in kt lambda expressions
        // Here it refers to the first character of the string
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }

    companion object {
        // Converts spinner input back to an enum
        fun fromString(value: String): RecipeCategory {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: OTHER
        }
    }
}