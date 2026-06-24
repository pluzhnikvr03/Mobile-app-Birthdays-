@file:OptIn(ExperimentalMaterial3Api::class)

package ru.hse.mobilepractice.birthdays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val AppAccent = Color(0xFF1E90FF)  // цвет системы
val CakeDone = Color(0xFF3CB371) // цвет купленного подарка


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val birthdays = remember {
        mutableStateListOf(
            Birthday(id = 1, name = "Мама", date = "15 июля", isCongratulated = true),
            Birthday(id = 2, name = "Антон", date = "3 августа", isCongratulated = false),
            Birthday(id = 3, name = "Вика", date = "15 марта", isCongratulated = false),
            Birthday(id = 4, name = "Маша", date = "29 января", isCongratulated = false),
            Birthday(id = 5, name = "Папа", date = "8 сентября", isCongratulated = true)
        )
    }
    var selectedId by remember { mutableStateOf<Int?>(null) }
    val current = birthdays.find { it.id == selectedId }
    if (current == null) {
        ListScreen(birthdays, onSelect = { selectedId = it.id }, onAdd = {})
    } else {
        DetailScreen(
            birthday = current,
            onBack = { selectedId = null },
            onToggle = {
                val idx = birthdays.indexOfFirst { it.id == current.id }
                if (idx >= 0) birthdays[idx] = birthdays[idx].copy(isCongratulated = !birthdays[idx].isCongratulated)
            },
            onDelete = {
                birthdays.removeAll { it.id == current.id }
                selectedId = null
            }
        )
    }
}

@Composable
fun ListScreen(birthdays: List<Birthday>, onSelect: (Birthday) -> Unit, onAdd: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дни рождения") },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(birthdays, key = { it.id }) { birthday ->
                RowItem(birthday) { onSelect(birthday) }
            }
        }
    }
}

@Composable
fun RowItem(birthday: Birthday, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.WineBar, contentDescription = null, tint = Color.White)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(birthday.name, fontWeight = FontWeight.SemiBold)
            Text(birthday.date, color = Color.Gray)
        }
        Icon(
            Icons.Default.Cake,
            contentDescription = null,
            tint = if (birthday.isCongratulated) CakeDone else Color.LightGray
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, color = Color.Gray)
        Spacer(Modifier.weight(1f))
        Text(value)
    }
}

@Composable
fun DetailScreen(birthday: Birthday, onBack: () -> Unit, onToggle: () -> Unit, onDelete: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(birthday.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Назад") }
                },
                actions = {
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Удалить") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(76.dp).clip(RoundedCornerShape(22.dp)).background(AppAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.WineBar, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(24.dp))
            InfoRow("Дата рождения", birthday.date)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Подарок куплен")
                Spacer(Modifier.weight(1f))
                Switch(checked = birthday.isCongratulated, onCheckedChange = { onToggle() }, colors  = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFF0FFF0),
                    checkedTrackColor = Color(0xFF90EE90),
                    uncheckedThumbColor = Color(0xFFFFFFFF),
                    uncheckedTrackColor = Color(0xFFDCDCDC)
                )
                )
            }
        }
    }
}
