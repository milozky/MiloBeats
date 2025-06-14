package com.example.milobeats.data.model

data class TrackSearchResponse(
    val results: Results
)

data class Results(
    val trackmatches: TrackMatches
)

data class TrackMatches(
    val track: List<Track>
)

data class Track(
    val name: String,
    val artist: String,
    val url: String
) 