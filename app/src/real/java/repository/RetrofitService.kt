package repository

import com.geekbrains.tests.view.search.MainActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

     fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}