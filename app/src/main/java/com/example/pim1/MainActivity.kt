//Michał Bernacki-Janson
package com.example.pim1

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.pim1.ui.theme.AppTheme
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Tabs()
                }
            }
        }
    }
}


@Composable
fun Tabs(modifier: Modifier = Modifier){
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val titles = listOf("Różnica dat", "Dodaj lub odejmij dni")

    Column (
        modifier = Modifier.padding(top = 40.dp)/*.background(color = Color(0xffffe3eb))*/,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        //Surface(Modifier.height(40.dp)){}
        TabRow(selectedTabIndex = selectedTabIndex,
            /*containerColor = Color(0xffffe3eb),*/
            contentColor = Color.Black,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFF65558F)
                )

            }) {
            titles.forEachIndexed{
                index, title -> Tab(
                    text = { Text(title)},
                    selected = (index == selectedTabIndex),
                    onClick = {selectedTabIndex = index}
                )
            }
        }
        when(selectedTabIndex){
            0 -> DateDifference()
            1 -> AddDays()
        }
    }
}

@Composable
fun DatePickerTextField(text : String, selectedDate: String, onDataChange: (String) -> Unit, modifier: Modifier = Modifier){

    //var selectedDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val datePicker = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDataChange(if(dayOfMonth > 9){
                if(month > 9){
                    "${dayOfMonth}/${month + 1}/$year"
                }else{
                    "${dayOfMonth}/0${month + 1}/$year"
                }
            } else if(month > 9){
                "0${dayOfMonth}/${month + 1}/$year"
            }else{
                "0${dayOfMonth}/0${month + 1}/$year"
            })
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(text) },
        readOnly = true,
        modifier = Modifier
            .padding(top = 10.dp)
            .onFocusChanged { focusState ->
                if (focusState.hasFocus) {
                    isFocused = true
                    datePicker.show()
                    focusManager.clearFocus()
                }
            }
    )
    /*var hideDialog by remember { mutableStateOf(false) }
    if(selectedDate == "08/04/2024" && !hideDialog){
        AlertDialog(
            onDismissRequest = { hideDialog = true },
            title = {Text("")},
            text = {Text("Kocham Cię Aguniu ❤\uFE0F")},
            confirmButton = {}

        )
    }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateOutput(text : String, modifier: Modifier = Modifier){
    TextField(
        value = text,
        onValueChange = {},
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.LightGray,
            unfocusedBorderColor = Color(0xFF65558F),
            focusedBorderColor = Color(0xFF65558F)
        ),
        modifier = Modifier.padding(top = 20.dp),
    )
}

fun dateDifferenceOutput(start: String, end: String): String{
    val dateFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var outputString = ""
    if(start.isNotBlank() && end.isNotBlank()) {
        val startDate = LocalDate.parse(start, dateFormater)
        val endDate = LocalDate.parse(end, dateFormater)
        val period = Period.between(startDate, endDate)
        if(period.years > 0) {
            outputString += if (period.years < 4) {
                if (period.years == 1) {
                    "${period.years} rok, "
                } else {
                    "${period.years} lata, "
                }
            } else {
                "${period.years} lat, "
            }
        }
        if(period.months > 0) {
            outputString += if (period.months < 4) {
                if (period.months == 1) {
                    "${period.months} miesiąc, "
                } else {
                    "${period.months} miesiące, "
                }
            } else {
                "${period.months} miesięcy, "
            }
        }

        outputString += if (period.days == 1){
            "${period.days} dzień"
        }else{
            "${period.days} dni"
        }

        //in other units

        val years = ChronoUnit.YEARS.between(startDate, endDate).toInt()
        val months = ChronoUnit.MONTHS.between(startDate, endDate).toInt()

        if (months > 0) outputString += "\n"

        if(years > 0) {
            outputString += if (months < 4) {
                if (months == 1) {
                    "$months miesiąc lub "
                } else {
                    "$months miesiące lub "
                }
            } else {
                "$months miesięcy lub "
            }
        }

        val days = ChronoUnit.DAYS.between(startDate, endDate).toInt()
        if (months > 0) {
            outputString +=
                if (days == 1) {
                    "$days dzień"
                } else {
                    "$days dni"
                }
        }

    }else ""
    return outputString
}

@Composable
fun DateDifference(modifier: Modifier = Modifier){
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 106.dp, bottom = 156.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        DatePickerTextField("Początek", start, {n -> start = n})
        DatePickerTextField("Koniec", end, {n -> end = n})
        val dateFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        DateOutput(dateDifferenceOutput(start, end))
    }
}



@Composable
fun AddDays(modifier: Modifier = Modifier){
    val focusManager = LocalFocusManager.current
    var start by remember { mutableStateOf("") }
    Box(modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                focusManager.clearFocus()
            }
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 106.dp, bottom = 156.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            DatePickerTextField("Początek", start, { n -> start = n })
            var expanded by remember { mutableStateOf(false) }
            val focusManager2 = LocalFocusManager.current
            var pickedText by remember { mutableStateOf("Dodaj") }
            Box {
                OutlinedTextField(
                    value = pickedText,
                    readOnly = true,
                    onValueChange = {},
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            expanded = true
                            focusManager2.clearFocus()
                        }
                    }
                )
                if (expanded) {
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },  modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
                        DropdownMenuItem(
                            text = { Text("Dodaj") },
                            onClick = { pickedText = "Dodaj"; expanded = false },

                            )
                        DropdownMenuItem(
                            text = { Text("Odejmij") },
                            onClick = { pickedText = "Odejmij"; expanded = false }
                        )
                    }
                }
            }
            var days by remember { mutableStateOf("") }
            OutlinedTextField(
                value = days,
                onValueChange = { if(it.toIntOrNull() != null)days = it else days = ""},
                label = { Text("Liczba dni") },
                singleLine = true,
                modifier = modifier

            )
            val dateFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            if (pickedText == "Dodaj") {
                DateOutput(
                    if (start.isNotBlank() && days.isNotBlank())
                        LocalDate.parse(start, dateFormater)
                            .plusDays(days.toLong()).format(dateFormater).toString() else ""
                )
            } else {
                DateOutput(
                    if (start.isNotBlank() && days.isNotBlank())
                        LocalDate.parse(start, dateFormater)
                            .minusDays(days.toLong()).format(dateFormater).toString() else ""
                )
            }
        }
    }
}
