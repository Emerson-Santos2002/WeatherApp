package com.example.myday.ui.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.myday.R
import com.example.myday.ui.fragment.intro.IntroFavoriteFragment
import com.example.myday.ui.fragment.intro.IntroHomeFragment
import com.example.myday.ui.fragment.intro.IntroLocalityFragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroPageTransformerType
import com.github.appintro.R as S

class MyIntroActivity : AppIntro2(){

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager = findViewById(S.id.view_pager)

        setTheme(R.style.IntroTheme)
        addSlide( IntroHomeFragment() )
        addSlide( IntroFavoriteFragment() )
        addSlide( IntroLocalityFragment() )
        configAppIntro()
    }

    private fun configAppIntro(){
        
        isSkipButtonEnabled = false
        isButtonsEnabled = false
        isIndicatorEnabled = true

        setTransformer(AppIntroPageTransformerType.Fade)
        setIndicatorColor(
            selectedIndicatorColor = ContextCompat.getColor(this, R.color.white),
            unselectedIndicatorColor = ContextCompat.getColor(this, R.color.background))
    }

    fun personGoToNextSlide() {
        viewPager.currentItem += 1
    }
    fun personPreviousSlide() {
        viewPager.currentItem -= 1
    }
    fun personSkipSlide() {
        viewPager.currentItem = 2
    }
}

