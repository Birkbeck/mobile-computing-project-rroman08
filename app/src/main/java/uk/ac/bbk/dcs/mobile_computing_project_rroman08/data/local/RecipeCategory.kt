package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

/**
 * Enum class pre-defined categories a recipe can belong to.
 */
enum class RecipeCategory {
    BREAKFAST,
    BRUNCH,
    LUNCH,
    DINNER,
    DESSERT,
    OTHER;

    /**
     * Returns the enum name (value) in a ui-friendly format.
     * E.g., "BREAKFAST" becomes "Breakfast".
     */
    override fun toString(): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }
}