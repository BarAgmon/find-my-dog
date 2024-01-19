package com.idz.find_my_dog.Modules.Posts
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.idz.find_my_dog.Model.Locations
import com.idz.find_my_dog.R

class SearchFragment : Fragment() {
    private var searchAutoComplete: AutoCompleteTextView? = null
    private var citiesAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the AutoCompleteTextView
        searchAutoComplete = view.findViewById(R.id.searchAutoCompleteTextView)

        val cities = Locations.instance.locations

        // Setup the adapter
        citiesAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        searchAutoComplete?.setAdapter(citiesAdapter)

        // Set the item click listener
        searchAutoComplete?.setOnItemClickListener { parent, _, position, _ ->
            val selectedCity = parent.adapter.getItem(position) as String
            Log.i("search bar",selectedCity)
        }

        searchAutoComplete?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(typedString: Editable) {
                // Filter the adapter based on the input text
                citiesAdapter?.filter?.filter(typedString)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
