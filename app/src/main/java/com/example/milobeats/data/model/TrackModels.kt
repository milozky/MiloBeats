package com.example.milobeats.data.model

import com.google.gson.annotations.SerializedName

data class TrackSearchResponse(
    val results: Results
)

data class Results(
    @SerializedName("opensearch:Query")
    val opensearchQuery: OpensearchQuery?,
    @SerializedName("opensearch:totalResults")
    val opensearchTotalResults: Long?,
    @SerializedName("opensearch:startIndex")
    val opensearchStartIndex: Long?,
    @SerializedName("opensearch:itemsPerPage")
    val opensearchItemsPerPage: Long?,
    @SerializedName("trackmatches")
    val trackMatches: TrackMatches?,
    @SerializedName("@attr")
    val attr: Attr?
)

data class OpensearchQuery(
    @SerializedName("#text")
    val text: String?,
    val role: String?,
    val startPage: String?
)

data class TrackMatches(
    val track: List<Track>?
)

data class Track(
    val name: String?,
    val artist: String?,
    val url: String?,
    val streamable: String?,
    val listeners: String?,
    val image: List<Image>?,
    val previewUrl: String? = null
)

data class Image(
    @SerializedName("#text")
    val url: String?,
    val size: String?
)

class Attr(
    // Add fields inside @attr if needed, based on your JSON example
    // For now, it's an empty object in your provided JSON, so we can leave it empty or add specific fields if they appear later
) 