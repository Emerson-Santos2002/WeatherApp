package com.example.myday.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myday.R
import com.example.myday.databinding.FragmentFavoriteBinding
import com.example.myday.domain.viewmodel.FavoriteViewModel
import com.example.myday.domain.viewmodel.LocationViewModel
import com.example.myday.ui.adapter.CustomAutoCompleteAdapter
import com.example.myday.ui.adapter.FavoriteCityAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val binding: FragmentFavoriteBinding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }
    private val locationViewModel : LocationViewModel by viewModel()
    private val favoriteViewModel : FavoriteViewModel by viewModel()

    private lateinit var adapter: FavoriteCityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationViewModel.initializePlaces(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        searchCityList()

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                favoriteViewModel.listFavoriteCities.collect { list ->

                    adapter.setData(list)
                    adapter.setOnItemClickListener(
                        object : FavoriteCityAdapter.OnItemClickListener {

                            override fun onItemCLick(position: Int) {
                                val cityName = list[position].city_name
                                locationViewModel.saveDefaultCity(cityName)
                                this@FavoriteFragment.findNavController().popBackStack()
                            }
                        }
                    )
                    adapter.setOnLongItemCLickListener(
                        object : FavoriteCityAdapter.OnItemLongClickListener {
                            override fun onLongItemClick(position: Int) {

                                val cityName = list[position].city_name
                                val cityNameDefault: String = when (position) {
                                    list.size - 1 -> list[position - 1].city_name
                                    else -> list[position + 1].city_name
                                }
                                notifyExcludeItem(cityName, cityNameDefault)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun notifyExcludeItem(cityName: String, cityNameDefault: String) {

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_delete_48dp)
            .setTitle("${getString(R.string.delete_favorite_city)} $cityName")
            .setCancelable(true)
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                favoriteViewModel.deleteCityFromFavorites(cityName)
                locationViewModel.saveDefaultCity(cityNameDefault)
            }
            .create()

        alertDialog.show()
    }

    private fun setupUI(){

        adapter = FavoriteCityAdapter()

        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            this.adapter = this@FavoriteFragment.adapter
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) binding.editText.hint = null
            else binding.editText.hint = getString(R.string.search)
        }
    }

    private fun searchCityList() {

        val autoCompleteAdapter = CustomAutoCompleteAdapter(requireContext())
        val textWatcher = customTextWatcher()

        binding.editText.setAdapter(autoCompleteAdapter)
        binding.editText.addTextChangedListener(textWatcher)

        autoCompleteAdapter.setClickListener {

            favoriteViewModel.saveInFavorites(it)
            with(binding.editText){
                setText("")
                clearFocus()
                WindowInsetsControllerCompat(requireActivity().window, this)
                    .hide(WindowInsetsCompat.Type.ime())
            }
        }

        locationViewModel.suggestions.observe(viewLifecycleOwner) { suggestionsList ->
            binding.editText.addTextChangedListener(textWatcher)
            autoCompleteAdapter.setItems(suggestionsList)
        }
    }

    private fun customTextWatcher(): TextWatcher {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    if (it.isNotEmpty() && it.length > 1) {
                        binding.editText.removeTextChangedListener(this)
                        locationViewModel.getSuggestionsList(it.toString())
                    }
                }
            }

        }
        return textWatcher
    }
}