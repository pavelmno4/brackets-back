package ru.pkozlov.brackets.auth.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.EnumerationNameColumnType
import ru.pkozlov.brackets.auth.enumeration.Role
import java.util.*

object UserTable : UUIDTable("usr") {
    val login: Column<String> = varchar("login", 255).uniqueIndex("user_login_idx")
    val password: Column<String> = varchar("password", 255)
    val roles: Column<List<Role>> = array(name = "roles", columnType = EnumerationNameColumnType(Role::class, 10))
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(UserTable)

    val login: String by UserTable.login
    val password: String by UserTable.password
    val roles: List<Role> by UserTable.roles
}