package com.bendeng.presentation.ui

import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bendeng.presentation.R
import com.bendeng.presentation.base.BaseActivity
import com.bendeng.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val viewModel: MainSharedViewModel by viewModels()

    override fun initView() {
        setNavigationController()
    }

    override fun initEventObserver() {

    }

    private fun setNavigationController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        with(binding) {
            bnvNavBar.setupWithNavController(navController)
            bnvNavBar.setOnItemReselectedListener { menuItem ->
                //TODO : 맨위 스크롤 가게 하기 설정
                // enum으로 그게 맞다면 그것에 대한 맨위로 올리게 하기
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }
}