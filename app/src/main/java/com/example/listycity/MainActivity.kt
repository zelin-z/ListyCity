package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.listycity.ui.theme.ListyCityTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ListyCityTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    CityListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


/*
 * Stores and modifies the list of cities.
 */
class CityRepository {

    private val _cities = mutableStateListOf(
        "Edmonton",
        "Vancouver",
        "Calgary",
        "Toronto",
        "Montreal"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}


@Composable
fun CityListScreen(
    modifier: Modifier = Modifier
) {

    val cityRepository = remember {
        CityRepository()
    }

    var newCityName by remember {
        mutableStateOf("")
    }

    var showAddCityInput by remember {
        mutableStateOf(false)
    }

    var selectedCity by remember {
        mutableStateOf<String?>(null)
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "ListyCity",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        /*
         * ADD CITY and DELETE CITY buttons
         */
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    showAddCityInput = true
                }
            ) {
                Text("ADD CITY")
            }


            Button(
                onClick = {

                    selectedCity?.let { city ->

                        cityRepository.deleteCity(city)

                        selectedCity = null
                    }
                }
            ) {
                Text("DELETE CITY")
            }
        }


        /*
         * Show the text box after ADD CITY is pressed.
         */
        if (showAddCityInput) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedTextField(
                value = newCityName,

                onValueChange = {
                    newCityName = it
                },

                label = {
                    Text("City name")
                },

                modifier = Modifier.fillMaxWidth()
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Button(
                onClick = {

                    val cityName = newCityName.trim()

                    if (cityName.isNotEmpty()) {

                        cityRepository.addCity(cityName)

                        newCityName = ""

                        showAddCityInput = false
                    }
                }
            ) {
                Text("CONFIRM")
            }
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        /*
         * Display the city list.
         */
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(cityRepository.cities) { city ->

                Text(
                    text =
                        if (city == selectedCity) {
                            "✓ $city"
                        } else {
                            city
                        },

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedCity = city
                        }
                        .padding(12.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CityListPreview() {

    ListyCityTheme {
        CityListScreen()
    }
}