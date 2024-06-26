package com.tirexmurina.util.features.fragments.formatters

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDateForDetails(date: LocalDateTime): String =
    date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(RUSSIAN_LOCALE))

fun formatDateForList(date: LocalDateTime): String =
    date.format(DateTimeFormatter.ofPattern("d MMMM, E", RUSSIAN_LOCALE))