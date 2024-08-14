package com.tirexmurina.shared.user.source.data.models

import com.tirexmurina.shared.user.core.data.models.AuthModel

class UserModelAdapter {
    fun convert(from: AuthModel): UserLocalModel =
        with(from) {
            UserLocalModel(
                name = name,
                password = password
            )
        }
}