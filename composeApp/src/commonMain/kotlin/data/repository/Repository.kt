package data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.example.compose.data.network.models.MealItem
import org.example.compose.data.network.models.Meals
import utils.K
import utils.Response

class Repository(
    private val client: HttpClient
) {
    fun fetchMeals(location: String? = null): Flow<Response<Meals>> = flow {
        emit(Response.Loading())

        val mealDto = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = K.Host
                path(K.Path)
                location?.let { parameters.append("a", it) } // Add only if not null
            }
        }.body<Meals>()

        emit(Response.Success(mealDto))
    }.catch { error ->
//        emit(Response.Error)
    }

    fun fetchMealById(id: String): Flow<Response<MealItem>> = flow {
        emit(Response.Loading())

        val mealDto = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = K.Host
                path(K.LookUpPath)
                parameters.append("i", id)
            }
        }.body<MealItem>()

        emit(Response.Success(mealDto))
    }.catch { error ->
//        emit(Response.Error(error.localizedMessage ?: "An unknown error occurred"))
    }
}