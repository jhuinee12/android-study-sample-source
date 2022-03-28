package com.example.navigation_graph_tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.navigation_graph_tutorial.databinding.ActivityMainBinding

/**
 * 1. 연결할 두개의 프래그먼트 생성 (MainFragment, SecondFragment)
 * 2. res > new resource file > Value:Navigation, Name:nav_graph
 * 3. activity_main.xml에 NavHostFragment 추가 (자동 연결됨)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    // ex. KoahealthApp
    /*private val navHostFragment: NavHostFragment by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
    }
    private val graph: NavGraph by lazy { navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph) }
    private val navController: NavController by lazy { navHostFragment.navController }*/
    private val navHostFragment: NavHostFragment by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
    }
    private val graph: NavGraph by lazy { navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph) }
    private val navController: NavController by lazy { navHostFragment.navController }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO : Navigation Graph 수정 필요!!
        // Navigation 사용 시, 상단 Appbar 맞춰주기
        // Caused by: java.lang.IllegalStateException: Activity com.example.navigation_graph_tutorial.MainActivity@359c0b
        //            does not have a NavController set on 2131230917
        // extensions를 사용한 소스이기 때문 :: val navController = findNavController(R.id.fragmentContainerView)
//        val navController = findNavController(R.id.fragmentContainerView)
//        val navController = (binding.fragmentContainerView as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Navigation 사용 시, 상단 Appbar 맞춰주기 :: Override
    override fun onSupportNavigateUp(): Boolean {
        // ex. KonaHealthApp
        // findNavController().navigate(R.id.actionSearchResultToSearchHistory)

        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}