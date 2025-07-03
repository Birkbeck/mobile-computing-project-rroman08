package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.RecipeItemBinding

/**
 * RecyclerView.Adapter that binds a list of recipes
 * to the UI, with a button to view each recipe in detail.
 *
 * @param recipes Initial list of recipes.
 * @param onViewClick Callback invoked with recipe ID when "See Recipe" is clicked.
 */
class RecipeAdapter(
    private var recipes: List<Recipe> = listOf(),
    private val onViewClick: (Long) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    /**
     * Replace adapter data and refresh list.
     */
    fun updateRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    /**
     * ViewHolder inner class for displaying a single recipe item in the RecyclerView.
     *
     * An instance represents one card in the recipe list, and is responsible
     * for binding the recipe data to the corresponding views.
     */
    inner class RecipeViewHolder(
        private val binding: RecipeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds Recipe data to UI and sets click listener.
         */
        fun bind(recipe: Recipe) {
            binding.recipe = recipe
            binding.executePendingBindings()
            binding.buttonSeeRecipe.setOnClickListener {
                onViewClick(recipe.id)
            }
        }
    }
}