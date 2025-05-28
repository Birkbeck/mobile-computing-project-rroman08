package uk.ac.bbk.dcs.mobile_computing_project_rroman08

data class RecipeItem(
    val id: Int,
    val title: String,
    val category: RecipeCategory,
    val ingredients: List<String>,
    val instructions: List<String>
)
