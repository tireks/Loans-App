package com.tirexmurina.shared.user.core.domain.entity

//вообще конечно по-хорошему, надо бы получать ретрофитом модельку, а потом уже конвертить в entity, но не плодим сущности
data class User(
    val name: String,
    val role: String
)
