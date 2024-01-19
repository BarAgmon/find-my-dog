package com.idz.find_my_dog.Models

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class Locations {
    private val citiesInIsraelUrl = "https://data.gov.il/api/3/action/datastore_search?resource_id=8f714b6f-c35c-4b40-a0e7-547b675eee0e"
    val locations: MutableList<String> = ArrayList()

    companion object {
        val instance: Locations by lazy { Locations() }
    }
    private fun fetchCities(callback: (List<String>) -> Unit) {
        val request = Request.Builder().url(citiesInIsraelUrl).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val cities = parseCitiesFromJson(jsonString)
                    callback(cities)
                }
            }
        })
    }

    private fun parseCitiesFromJson(jsonString: String): List<String> {
        val gson = Gson()
        val resultsFieldName = "result"
        val citiesRecordsFieldName = "records"
        val rootObj = gson.fromJson(jsonString, JsonObject::class.java)
        val records = rootObj.getAsJsonObject(resultsFieldName).getAsJsonArray(citiesRecordsFieldName)
        val locationsEnNameField = "city_name_en"
        val locationsNamesListEn = records.map {
            it.asJsonObject.get(locationsEnNameField).asString.trim()
        }.filter {
            it.isNotEmpty()  // Filter out any empty city names
        }
        return locationsNamesListEn
    }

    init {
        fetchCities { cities ->
            locations.clear()
            locations.addAll(cities)
        }
    }
}