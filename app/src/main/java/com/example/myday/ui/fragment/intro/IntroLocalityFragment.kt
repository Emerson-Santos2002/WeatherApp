package com.example.myday.ui.fragment.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.WindowMetricsCalculator
import com.example.myday.R
import com.example.myday.databinding.FragmentIntroLocalityBinding
import com.example.myday.ui.activity.MainActivity
import com.example.myday.ui.activity.MyIntroActivity
import com.example.myday.ui.activity.NetworkErrorActivity
import com.example.myday.ui.adapter.CustomAutoCompleteAdapter
import com.example.myday.domain.viewmodel.LocationViewModel
import com.example.myday.util.dataUtils.IntroLocalityUIState
import com.example.myday.util.formatting.ImageUtils.Companion.setupGlide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroLocalityFragment : Fragment() {

    private var _binding: FragmentIntroLocalityBinding? = null
    private val binding get() = _binding!!

    private val locationViewModel: LocationViewModel by viewModel()

    private lateinit var alertDialog : AlertDialog
    private var alertDialogInitialized = false
    private var locationDisabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroLocalityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationViewModel.initializePlaces(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        locationViewModel.createRequestLocationPermission(view.findFragment(), requireContext())

        locationViewModel.introSearchState.observe(viewLifecycleOwner) { uiState ->

            when (uiState) {

                IntroLocalityUIState.NetworkDisabled -> {
                    warnNetworkDisabled()
                }

                IntroLocalityUIState.LocationDisabled -> {
                    warnLocationIsDisabled()
                }

                IntroLocalityUIState.AutoSearchError -> {
                    warnErrorWarningAutoSearch()
                }

                IntroLocalityUIState.LoadingSelectedCity -> {
                    informSelectedCityIsLoading()
                }

                is IntroLocalityUIState.SelectedCityLoaded -> {
                    finalizeIntroductionForUser(uiState.city)
                }

                else -> {}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (alertDialogInitialized && locationDisabled){
            alertDialog.dismiss()
            locationViewModel
                .checkLocationEnabled(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.requestLocationPermission(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {

        binding.ivPreviousButton.setupGlide(R.mipmap.ic_previous_icon)
        binding.ivSearchAutoComplete.setupGlide(R.drawable.ic_search_24dp)
        binding.ivStateAutoComplete.setupGlide(R.drawable.ic_error_24dp)
        binding.btnPrevious.setOnClickListener { onPreviousPressed() }

        val windowsMetric = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        binding.backgroundCentral.layoutParams.apply {
            height = (windowsMetric.bounds.height() * 0.07).toInt()
        }
    }

    private fun warnNetworkDisabled(){
        startActivity(Intent(requireContext(), NetworkErrorActivity::class.java))
    }

    private fun warnLocationIsDisabled() {

        alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_favorite)
            .setTitle(getString(R.string.intro_text_alert_dialog))
            .setMessage(getString(R.string.intro_text_alert_dialog))
            .setCancelable(false)
            .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ ->
                searchCityManually()
            }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                enableLocation()
            }
            .create()

        if (!alertDialogInitialized) {
            alertDialog.show()
            locationDisabled = true
            alertDialogInitialized = true
            locationViewModel.awaitResultLocationActivation()
        } else {
            searchCityManually()
        }
    }

    private fun enableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        requireContext().startActivity(intent)
    }

    private fun warnErrorWarningAutoSearch() {
        Toast.makeText(requireContext(), getString(R.string.intro_toast_error_auto_search), Toast.LENGTH_LONG).show()
        binding.ivStateAutoComplete.setupGlide(R.drawable.ic_error_24dp)
        searchCityManually()
    }

    private fun informSelectedCityIsLoading() {
        binding.ivStateAutoComplete.setupGlide(R.raw.ic_loading)
    }

    private fun searchCityManually() =
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {

        delay(Toast.LENGTH_LONG + 1000L)

        val autoCompleteAdapter = CustomAutoCompleteAdapter(requireContext())
        val textWatcher = customTextWatcher()

        with(binding.textFieldSearchCity) {

            setAdapter(autoCompleteAdapter)
            addTextChangedListener(textWatcher)
            requestFocus()
            WindowInsetsControllerCompat(requireActivity().window, this)
                .show(WindowInsetsCompat.Type.ime())
        }

        autoCompleteAdapter.setClickListener { result ->
            informSelectedCityIsLoading()
            locationViewModel.saveDefaultCity(result)
            finalizeIntroductionForUser(result)

            WindowInsetsControllerCompat(requireActivity().window, binding.textFieldSearchCity)
                .hide(WindowInsetsCompat.Type.ime())
        }

        locationViewModel.suggestions.observe(viewLifecycleOwner) { suggestionsList ->
            binding.textFieldSearchCity.addTextChangedListener(textWatcher)
            autoCompleteAdapter.setItems(suggestionsList)
        }
    }

    private fun customTextWatcher(): TextWatcher{

        return object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    if (it.isNotEmpty() && it.length > 1) {
                        Log.i("emerson", it.toString())
                        binding.textFieldSearchCity.removeTextChangedListener(this)
                        locationViewModel.getSuggestionsList(it.toString())
                    }
                }
            }
        }
    }

    private fun finalizeIntroductionForUser(cityName: String) {

        viewLifecycleOwner.lifecycleScope.launch {

            delay(500)
            binding.textFieldSearchCity.setText(cityName)
            binding.ivStateAutoComplete.setupGlide(R.drawable.ic_confirm_24dp)

            delay(1000)
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun onPreviousPressed() {
        val introActivity = activity as MyIntroActivity
        introActivity.personPreviousSlide()
    }
}