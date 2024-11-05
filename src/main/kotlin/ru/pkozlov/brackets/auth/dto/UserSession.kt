package ru.pkozlov.brackets.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val login: String)