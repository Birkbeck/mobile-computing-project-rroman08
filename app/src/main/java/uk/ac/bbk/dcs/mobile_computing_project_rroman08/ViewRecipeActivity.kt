package uk.ac.bbk.dcs.mobile_computing_project_rroman08

//import android.util.log
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityViewRecipeBinding

class ViewRecipeActivity : AppCompatActivity() {

    private val viewModel: ViewRecipeViewModel by viewModels()
    private lateinit var binding: ActivityViewRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recipeId = intent.getLongExtra("RECIPE_ID", -1L)

        if (recipeId != -1L) {
            // Use ViewModel to read recipe from database with given id
            viewModel.readRecipeById(recipeId)
        } else {
//            Long.e("ViewRecipeActivity", "Invalid recipe ID: $recipeId")
        }

        // Observe LiveData and bind recipe to the view
        viewModel.recipe.observe(this) { recipe ->
            binding.recipe = recipe
        }
    }
}