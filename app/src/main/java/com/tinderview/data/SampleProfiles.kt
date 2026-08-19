package com.tinderview.data

import androidx.compose.ui.graphics.Color

data class Profile(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val city: String,
    val gradient: List<Color>,
    val isMatch: Boolean = false,
)

val SampleProfiles: List<Profile> = listOf(
    Profile(
        id = "nova",
        name = "Nova",
        age = 27,
        bio = "Designs typefaces, climbs granite, never skips golden hour.",
        city = "Lisbon",
        gradient = listOf(Color(0xFFFF7A59), Color(0xFFFFC371)),
        isMatch = true,
    ),
    Profile(
        id = "julian",
        name = "Julian",
        age = 31,
        bio = "Jazz pianist who makes a serious case for weeknight dancing.",
        city = "Brooklyn",
        gradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
    ),
    Profile(
        id = "mira",
        name = "Mira",
        age = 24,
        bio = "Marine biologist. Will talk about tide pools until the sun comes up.",
        city = "San Diego",
        gradient = listOf(Color(0xFF2AF598), Color(0xFF009EFD)),
    ),
    Profile(
        id = "theo",
        name = "Theo",
        age = 29,
        bio = "Chef. Makes pasta from scratch and playlists that start too loud.",
        city = "Austin",
        gradient = listOf(Color(0xFFF7971E), Color(0xFFFFD200)),
    ),
    Profile(
        id = "sienna",
        name = "Sienna",
        age = 26,
        bio = "Photo essays, thrifted denim, and a motorcycle named Pearl.",
        city = "Portland",
        gradient = listOf(Color(0xFFEE9CA7), Color(0xFFFFDDE1)),
    ),
    Profile(
        id = "kai",
        name = "Kai",
        age = 28,
        bio = "Builds furniture by day, stargazes from rooftops by night.",
        city = "Denver",
        gradient = listOf(Color(0xFF2193B0), Color(0xFF6DD5ED)),
    ),
    Profile(
        id = "lina",
        name = "Lina",
        age = 25,
        bio = "Runs sunrise 10Ks and still has energy for a second espresso.",
        city = "Berlin",
        gradient = listOf(Color(0xFFC33764), Color(0xFF1D2671)),
    ),
    Profile(
        id = "omar",
        name = "Omar",
        age = 33,
        bio = "Documentary editor. Best first date is a flea market plus tacos.",
        city = "Chicago",
        gradient = listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
    ),
)
