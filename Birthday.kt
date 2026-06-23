package ru.hse.mobilepractice.birthdays

data class Birthday(
    val id: Int,
    val name: String,
    val date: String,
    val isCongratulated: Boolean = false
)
