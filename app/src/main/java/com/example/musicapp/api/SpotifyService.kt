import com.example.musicapp.model.SpotifyAlbum
import com.example.musicapp.model.SpotifyAlbumsResponse
import com.example.musicapp.model.SpotifyArtistsResponse
import com.example.musicapp.model.SpotifyRecommendationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyService {
    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 40
    ): Response<SpotifyAlbumsResponse>

    @GET("v1/recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") token: String,
        @Query("seed_genres") genres: String,
        @Query("limit") limit: Int = 50
    ): Response<SpotifyRecommendationsResponse>

    @GET("v1/recommendations")
    suspend fun getSlides(
        @Header("Authorization") token: String,
        @Query("seed_genres") genres: String = "pop, hip-hop, country, rock, indie",
        @Query("target_popularity") popularity: Int = 90,
        @Query("limit") limit: Int = 50
    ): Response<SpotifyRecommendationsResponse>

    @GET("v1/albums/{id}")
    suspend fun getAlbumDetails(
        @Header("Authorization") token: String,
        @Path("id") albumId: String
    ): Response<SpotifyAlbum>

    @GET("v1/search")
    suspend fun search(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("type") type: String = "album",
    ): Response<SpotifyAlbumsResponse>
}
