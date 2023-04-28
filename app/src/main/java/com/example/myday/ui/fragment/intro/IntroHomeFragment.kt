package com.example.myday.ui.fragment.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myday.R
import com.example.myday.databinding.FragmentIntroBinding
import com.example.myday.ui.activity.MyIntroActivity
import com.example.myday.util.formatting.ImageUtils.Companion.setupGlide

class IntroHomeFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {

        binding.btnPrevious.visibility = View.GONE
        binding.tvDescription.text = getString(R.string.intro_txt_home)

        binding.ivIntro.setupGlide(R.drawable.home_layout)
        binding.ivNextButton.setupGlide(R.mipmap.ic_next_intro)

        binding.btnNext.setOnClickListener { onNextPressed() }
        binding.btnSkip.setOnClickListener { onSkipPressed() }
    }

    private fun onNextPressed() {
        val introActivity = activity as MyIntroActivity
        introActivity.personGoToNextSlide()
    }

    private fun onSkipPressed() {
        val introActivity = activity as MyIntroActivity
        introActivity.personSkipSlide()
    }
}