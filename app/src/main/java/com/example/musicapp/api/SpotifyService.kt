import com.example.musicapp.model.SpotifyAlbumsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyService {
    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 30
    ): Response<SpotifyAlbumsResponse>
}
