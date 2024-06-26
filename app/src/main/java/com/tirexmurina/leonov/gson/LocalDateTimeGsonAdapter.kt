package com.tirexmurina.leonov.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeGsonAdapter : JsonDeserializer<LocalDateTime> {

	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
		val stringDate = json.asString
		return LocalDateTime.parse(stringDate, DateTimeFormatter.ISO_DATE_TIME)
	}
}