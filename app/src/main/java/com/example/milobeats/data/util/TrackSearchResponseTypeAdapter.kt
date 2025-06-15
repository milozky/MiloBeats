package com.example.milobeats.data.util

import com.example.milobeats.data.model.Attr
import com.example.milobeats.data.model.Image
import com.example.milobeats.data.model.OpensearchQuery
import com.example.milobeats.data.model.Results
import com.example.milobeats.data.model.Track
import com.example.milobeats.data.model.TrackMatches
import com.example.milobeats.data.model.TrackSearchResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class TrackSearchResponseTypeAdapter : JsonDeserializer<TrackSearchResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): TrackSearchResponse {
        try {
            val jsonObject = json.asJsonObject
            val results = jsonObject.getAsJsonObject("results")
            
            return TrackSearchResponse(
                results = ResultsDeserializer.deserialize(results)
            )
        } catch (e: Exception) {
            throw JsonParseException("Error parsing TrackSearchResponse", e)
        }
    }
}

private object ResultsDeserializer {
    fun deserialize(results: JsonObject): Results {
        return Results(
            opensearchQuery = OpensearchQueryDeserializer.deserialize(results),
            opensearchTotalResults = results.get("opensearch:totalResults")?.asString?.toLongOrNull(),
            opensearchStartIndex = results.get("opensearch:startIndex")?.asString?.toLongOrNull(),
            opensearchItemsPerPage = results.get("opensearch:itemsPerPage")?.asString?.toLongOrNull(),
            trackMatches = TrackMatchesDeserializer.deserialize(results),
            attr = Attr()
        )
    }
}

private object OpensearchQueryDeserializer {
    fun deserialize(results: JsonObject): OpensearchQuery? {
        return results.getAsJsonObject("opensearch:Query")?.let { queryObj ->
            OpensearchQuery(
                text = queryObj.get("#text")?.asString,
                role = queryObj.get("role")?.asString,
                startPage = queryObj.get("startPage")?.asString
            )
        }
    }
}

private object TrackMatchesDeserializer {
    fun deserialize(results: JsonObject): TrackMatches? {
        return results.getAsJsonObject("trackmatches")?.let { matchesObj ->
            val tracks = matchesObj.getAsJsonArray("track")?.map { trackElement ->
                TrackDeserializer.deserialize(trackElement.asJsonObject)
            }
            TrackMatches(track = tracks)
        }
    }
}

private object TrackDeserializer {
    fun deserialize(trackObject: JsonObject): Track {
        return Track(
            name = trackObject.get("name")?.asString,
            artist = trackObject.get("artist")?.asString,
            url = trackObject.get("url")?.asString,
            streamable = trackObject.get("streamable")?.asString,
            listeners = trackObject.get("listeners")?.asString,
            image = ImageDeserializer.deserialize(trackObject),
            previewUrl = trackObject.get("previewUrl")?.asString
        )
    }
}

private object ImageDeserializer {
    fun deserialize(trackObject: JsonObject): List<Image>? {
        return trackObject.getAsJsonArray("image")?.map { imageElement ->
            val imageObject = imageElement.asJsonObject
            Image(
                url = imageObject.get("#text")?.asString,
                size = imageObject.get("size")?.asString
            )
        }
    }
} 