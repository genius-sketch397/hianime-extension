package eu.kanade.tachiyomi.animeextension.english.hianime

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceScreen
import eu.kanade.tachiyomi.animesource.ConfigurableAnimeSource
import eu.kanade.tachiyomi.animesource.model.AnimeFilterList
import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.SEpisode
import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.animesource.online.ParsedAnimeHttpSource
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.util.asJsoup
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Document
import org.jsoup.nodes.Element
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class HiAnime : ConfigurableAnimeSource, ParsedAnimeHttpSource() {

    override val name = "HiAnime"
    override val baseUrl = "https://hianime.ms"
    override val lang = "en"
    override val supportsLatest = true

    private val preferences: SharedPreferences by lazy {
        Injekt.get<Application>().getSharedPreferences("source_$id", 0)
    }

    override val client: OkHttpClient = network.cloudflareClient

    // ============== Popular ==============
    override fun popularAnimeSelector(): String = "div.col"
    override fun popularAnimeRequest(page: Int): Request = GET("$baseUrl/home", headers = headers)
    override fun popularAnimeFromElement(element: Element): SAnime = SAnime.create().apply {
        setUrlWithoutDomain(element.selectFirst("a")?.attr("href") ?: return@apply)
        title = element.selectFirst("h3")?.text() ?: ""
        thumbnail_url = element.selectFirst("img")?.attr("src")
    }

    override fun popularAnimeNextPageSelector(): String = "a.next"

    // ============== Latest ==============
    override fun latestUpdatesSelector(): String = "div.col"
    override fun latestUpdatesRequest(page: Int): Request = GET("$baseUrl/recently-updated", headers = headers)
    override fun latestUpdatesFromElement(element: Element): SAnime = popularAnimeFromElement(element)
    override fun latestUpdatesNextPageSelector(): String = "a.next"

    // ============== Search ==============
    override fun searchAnimeRequest(page: Int, query: String, filters: AnimeFilterList): Request {
        return GET("$baseUrl/search?keyword=$query&page=$page", headers = headers)
    }

    override fun searchAnimeSelector(): String = "div.col"
    override fun searchAnimeFromElement(element: Element): SAnime = popularAnimeFromElement(element)
    override fun searchAnimeNextPageSelector(): String = "a.next"

    // ============== Details ==============
    override fun animeDetailsParse(document: Document): SAnime = SAnime.create().apply {
        title = document.selectFirst("h1")?.text() ?: ""
        author = document.selectFirst("div.producer")?.text() ?: ""
        description = document.selectFirst("div.synopsis")?.text() ?: ""
        genre = document.select("div.genre a").joinToString(", ") { it.text() }
        status = parseStatus(document.selectFirst("div.status")?.text() ?: "")
        thumbnail_url = document.selectFirst("img.poster")?.attr("src")
    }

    private fun parseStatus(status: String?): Int = when {
        status?.contains("Airing", ignoreCase = true) == true -> SAnime.ONGOING
        status?.contains("Completed", ignoreCase = true) == true -> SAnime.COMPLETED
        else -> SAnime.UNKNOWN
    }

    // ============== Episodes ==============
    override fun episodeListSelector(): String = "div.episodes li"
    override fun episodeFromElement(element: Element): SEpisode = SEpisode.create().apply {
        setUrlWithoutDomain(element.selectFirst("a")?.attr("href") ?: return@apply)
        episode_number = element.selectFirst("span.number")?.text()?.toFloatOrNull() ?: 0f
        name = element.selectFirst("span.title")?.text() ?: ""
    }

    // ============== Video Links ==============
    override fun videoListParse(response: Response): List<Video> {
        val document = response.asJsoup()
        val videos = mutableListOf<Video>()
        
        // Parse video sources from document
        // Analyze HiAnime's video player structure
        document.select("source").forEach { source ->
            val url = source.attr("src")
            val quality = source.attr("title") ?: "Unknown"
            if (url.isNotEmpty()) {
                videos.add(Video(url, quality, url))
            }
        }
        
        return videos
    }

    override fun videoListSelector(): String = "source"
    override fun videoFromElement(element: Element): Video {
        val url = element.attr("src")
        val quality = element.attr("title") ?: "Unknown"
        return Video(url, quality, url)
    }

    override fun List<Video>.sortedWith(other: Comparator<Video>?): List<Video> = this.sortedBy { it.quality }

    // ============== Filters ==============
    override fun getFilterList(): AnimeFilterList = AnimeFilterList()

    // ============== Settings ==============
    override fun setupPreferenceScreen(screen: PreferenceScreen) {
        // Add preferences if needed in future
    }
}
