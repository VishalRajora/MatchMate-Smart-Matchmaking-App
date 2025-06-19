package com.example.matchmate.modelclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

data class MateMatchResult(
    val uuid: String,
    val cell: String,
    val dob: DateInfo,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val status: MatchStatus? = MatchStatus.NONE,
    val matchScore: Int? = 0,
)

@Entity(tableName = "user_profiles")
data class MateMatchResultRoom(
    @PrimaryKey val uuid: String,
    val cell: String,
    val email: String,
    val gender: String,
    val nat: String,
    val phone: String,
    val status: MatchStatus?,
    val matchScore: Int?,
)

class Converters {
    @TypeConverter
    fun fromStatus(status: MatchStatus?): String = status?.name.toString()

    @TypeConverter
    fun toStatus(value: String): MatchStatus = MatchStatus.valueOf(value)
}

enum class MatchStatus {
    ACCEPTED, DECLINED, NONE
}

data class Name(
    val title: String,
    val first: String,
    val last: String,
)

data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: Any,
    val coordinates: Coordinates,
    val timezone: Timezone,
)

data class Street(
    val number: Int,
    val name: String,
)

data class Coordinates(
    val latitude: String,
    val longitude: String,
)

data class Timezone(
    val offset: String,
    val description: String,
)

data class Login(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String,
)

data class DateInfo(
    val date: String,
    val age: Int,
)

data class Id(
    val name: String,
    val value: String?,
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String,
)

data class Info(
    val seed: String,
    val results: Int,
    val page: Int,
    val version: String,
)
