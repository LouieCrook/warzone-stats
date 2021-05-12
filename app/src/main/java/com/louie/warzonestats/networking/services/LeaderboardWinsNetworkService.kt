package com.louie.warzonestats.networking.services

import com.google.gson.GsonBuilder
import com.louie.warzonestats.models.leaderboard.wins.LeaderboardWinsModel
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch

/**
 * This is a Singleton that allows the calling class instance access to a single instance of the
 * MatchNetworkService object.
 */
object LeaderboardWinsNetworkService {
    // assign client variable to OkHttpClient
    val client: OkHttpClient = OkHttpClient()

    // function to get leaderboard kills & wins data
    fun getLeaderboardWinsData() : LeaderboardWinsModel?{
        var result : LeaderboardWinsModel? = null

        val url = "https://app.wzstats.gg/leaderboard/?leaderboardType=1&leaderboardRangeType=0&leaderboardCategory=0"
        val request = Request.Builder().url(url).build()

        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                result = gson.fromJson(body, LeaderboardWinsModel::class.java)
                countDownLatch.countDown()

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("\n\n\n Failed API Request \n\n\n")
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return result
    }
}