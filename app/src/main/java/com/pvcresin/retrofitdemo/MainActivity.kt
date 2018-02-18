package com.pvcresin.retrofitdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "GitHub"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()

        val service = retrofit.create(GitHubService::class.java)

        val repos = service.listRepos("pvcresin")

        // synchronous call
        // repos.execute()   // cannot execute on UI thread on Android

        // asynchronous call
        repos.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                val list = response?.body()

                list?.forEach {
                    Log.d(TAG, it.toString())
                }
            }

            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }
}

interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}

data class Repo(
        val full_name: String,
        val html_url: String,
        val description: String
)