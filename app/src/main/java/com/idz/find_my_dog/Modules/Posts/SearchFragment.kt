package com.idz.find_my_dog.Modules.Posts
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
class SearchFragment : Fragment() {
    private var searchAutoComplete: AutoCompleteTextView? = null
    private var citiesAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the AutoCompleteTextView
        searchAutoComplete = view.findViewById(R.id.searchAutoCompleteTextView)

        // Assume 'cities' is your list of cities
        val cities = Locations.instance.locations

        // Setup the adapter
        citiesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        searchAutoComplete?.setAdapter(citiesAdapter)

        // Set the item click listener
        searchAutoComplete?.setOnItemClickListener { parent, _, position, _ ->
            val selectedCity = parent.adapter.getItem(position) as String
            // Handle the city selection
        }

        // Optional: Add a text changed listener for filtering
        searchAutoComplete?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Filter the adapter based on the input text
                citiesAdapter?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
