package com.example.bottomsheetbehaviorproblem

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bottomsheetbehaviorproblem.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var originalFabTranslationY: Float = 0f
    private var isFabMoved = false

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController // Get NavController from NavHostFragment

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Store the original translationY when the layout is first created
        binding.fab.post {
            originalFabTranslationY = binding.fab.translationY
        }

        binding.fab.setOnClickListener { view -> showSnackbarAndMoveFab(view) }

        adjustEdgeToEdge()
    }

    private fun showSnackbarAndMoveFab(view: View) {
        val fabText = getString(R.string.replace_with_your_own_action)
        val approximateSnackbarHeight = resources.getDimensionPixelSize(R.dimen.button_width)

        // Move FAB up before Snackbar is shown
        if (!(snackbar?.isShown ?: false)) {
            isFabMoved = true
            binding.fab.animate()
                .translationY(originalFabTranslationY - approximateSnackbarHeight - 3 * resources.getDimensionPixelSize(R.dimen.margin)) // Add a small margin
                .setDuration(250)
                .withStartAction {
                    binding.fab.isEnabled = false
                } .withEndAction {
                    binding.fab.isEnabled = true
                }
                .start()
        } else {
            snackbar?.dismiss()
            snackbar = null
            isFabMoved = true
        }

        snackbar = Snackbar.make(view, fabText, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.action), null)
            .setAnchorView(R.id.viewer_toolbar)
            .addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)

                    // Snackbar is dismissed, move FAB back down if it was moved
                    if (!(snackbar?.isShown ?: false)) {
                        if (isFabMoved) {
                            binding.fab.animate()
                                .translationY(originalFabTranslationY) // Move back to original position
                                .setStartDelay(250)
                                .setDuration(250)
                                .withStartAction {
                                    binding.fab.isEnabled = false
                                }
                                .withEndAction {
                                    isFabMoved = false
                                    binding.fab.isEnabled = true
                                    snackbar = null
                                }
                                .start()
                        } else {
                            snackbar = null
                        }
                    } else {
                        snackbar = null
                    }
                }
            })

        snackbar?.show()
    }

    private fun adjustEdgeToEdge() {
        // Enable edge-to-edge
        // For API 34+ this is typically handled by themes (e.g., Theme.Material3.DayNight.NoActionBar)
        // and android:windowSoftInputMode="adjustResize" or by default.
        // The explicit decorFitsSystemWindows(false) is good for older APIs.
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // Enable edge-to-edge
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }

        // Use binding.root as the view for the listener if it's the outermost view
        // that should react to window insets for left/right padding.
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            // Handle the insets (e.g., adjust padding or margins)
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply left and right padding to the root view
            view.updatePadding(
                systemBarsInsets.left,
                systemBarsInsets.right
                // Do not set top/bottom here for the root view,
                // as they will be handled by the toolbar and viewer_toolbar
            )

            // Apply top padding to the toolbar
            binding.toolbar.updatePadding(top = systemBarsInsets.top)
            binding.toolbar.updateLayoutParams<AppBarLayout.LayoutParams> {
                height = systemBarsInsets.top + getActionBarSizeInPixels()
            }

            // Apply bottom padding to the viewer_toolbar
            binding.viewerToolbar.updatePadding(bottom = systemBarsInsets.bottom)

            val bottomBarHeightPixels = resources.getDimensionPixelSize(R.dimen.bottom_bar_height)
            val marginPixels = resources.getDimensionPixelSize(R.dimen.margin)
            binding.fab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                bottomMargin = systemBarsInsets.bottom + bottomBarHeightPixels + 2 * marginPixels
            }

            // Return the insets to indicate they have been consumed or handled
            // If you want other views to also receive these insets, you might return insets.consumeSystemWindowInsets()
            // or just insets if you've only partially consumed them.

            WindowInsetsCompat.CONSUMED // Or return 'insets' if other views might need them
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun getActionBarSizeInPixels(): Int {
        val typedValue = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        }
        // Fallback or error handling if the attribute is not found (shouldn't happen for actionBarSize)
        return 0 // Or throw an exception, or return a default value
    }
}