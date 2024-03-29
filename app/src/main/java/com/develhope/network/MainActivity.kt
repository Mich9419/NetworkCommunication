package com.example.myapplication.github.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.develhope.network.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): RepoResult
}

class RepoResult : ArrayList<Repo>()

data class Repo(
    val allow_forking: Boolean,
    val archive_url: String,
    val archived: Boolean,
    val assignees_url: String,
    val blobs_url: String,
    val branches_url: String,
    val clone_url: String,
    val collaborators_url: String,
    val comments_url: String,
    val commits_url: String,
    val compare_url: String,
    val contents_url: String,
    val contributors_url: String,
    val created_at: String,
    val default_branch: String,
    val deployments_url: String,
    val description: String,
    val disabled: Boolean,
    val downloads_url: String,
    val events_url: String,
    val fork: Boolean,
    val forks: Int,
    val forks_count: Int,
    val forks_url: String,
    val full_name: String,
    val git_commits_url: String,
    val git_refs_url: String,
    val git_tags_url: String,
    val git_url: String,
    val has_downloads: Boolean,
    val has_issues: Boolean,
    val has_pages: Boolean,
    val has_projects: Boolean,
    val has_wiki: Boolean,
    val homepage: Any,
    val hooks_url: String,
    val html_url: String,
    val id: Int,
    val is_template: Boolean,
    val issue_comment_url: String,
    val issue_events_url: String,
    val issues_url: String,
    val keys_url: String,
    val labels_url: String,
    val language: String,
    val languages_url: String,
    val license: License,
    val merges_url: String,
    val milestones_url: String,
    val mirror_url: Any,
    val name: String,
    val node_id: String,
    val notifications_url: String,
    val open_issues: Int,
    val open_issues_count: Int,
    val owner: Owner,
    val `private`: Boolean,
    val pulls_url: String,
    val pushed_at: String,
    val releases_url: String,
    val size: Int,
    val ssh_url: String,
    val stargazers_count: Int,
    val stargazers_url: String,
    val statuses_url: String,
    val subscribers_url: String,
    val subscription_url: String,
    val svn_url: String,
    val tags_url: String,
    val teams_url: String,
    val topics: List<Any>,
    val trees_url: String,
    val updated_at: String,
    val url: String,
    val visibility: String,
    val watchers: Int,
    val watchers_count: Int
)

data class License(
    val key: String,
    val name: String,
    val node_id: String,
    val spdx_id: String,
    val url: String
)

data class Owner(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val id: Int,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
)


class MainActivity : AppCompatActivity() {

    val retrofit = Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(
        GsonConverterFactory.create()
    ).build()
    val githubService = retrofit.create(GithubService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrieveRepos()
    }

    fun retrieveRepos() {
        val progress = findViewById<ContentLoadingProgressBar>(R.id.repo_loading_indicator)

        lifecycleScope.launch {
            try {
                progress.show()
                val repos = githubService.listRepos("mike")
                progress.hide()
                showRepos(repos)
            } catch (e: Exception) {
                Log.e("MainActivity", "error retrieving repos: $e")
                progress.hide()
                Snackbar.make(
                    findViewById(R.id.main_view),
                    "Error retrieving repos",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") { retrieveRepos() }.show()
            }
        }
    }

    fun showRepos(repoResults: List<Repo>) {
        Log.d("MainActivity", "list of repos received, size: ${repoResults.size}")

        val list = findViewById<RecyclerView>(R.id.repo_list)
        list.visibility = View.VISIBLE
        list.adapter = list.adapter
        list.layoutManager = LinearLayoutManager(this)
    }
}