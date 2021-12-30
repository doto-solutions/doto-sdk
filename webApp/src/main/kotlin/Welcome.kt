import com.baseio.kmm.domain.model.GithubReposItem
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.html.classes
import kotlinx.html.style
import react.*
import react.dom.*

external interface TrendingProps : Props

val scope = MainScope()

val TrendingUI = fc<TrendingProps> {
    var trendingRepos: List<GithubReposItem> by useState(emptyList())
    var exception: String by useState("")
    useEffectOnce {
        scope.launch {
            withContext(Dispatchers.Default) {
                delay(2500)
                if(sharedComponent.provideGithubTrendingLocal().driver == null){
                    exception = "driver is null"
                }else{
                    exception = "driver is not null"
                }
                useCasesComponent.provideGetLocalReposUseCase().perform(null).collectLatest {
                    withContext(Dispatchers.Main) {
                        trendingRepos = it
                    }
                }
            }
        }
    }

    useEffectOnce {
        scope.launch {
            withContext(Dispatchers.Default) {
                val trendingReposLocal =
                    useCasesComponent.provideFetchTrendingReposUseCase().perform("kotlin")
                if(sharedComponent.provideGithubTrendingLocal().driver == null){
                    exception = "driver is null"
                }else{
                    exception = "driver is not null"
                }
                try {
                    useCasesComponent.provideSaveTrendingReposUseCase().perform(trendingReposLocal)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    exception = ex.message ?: ""
                }
            }
        }
    }

    h1 {
        +"Trending Kotlin Repositories"
        +exception
    }
    div {
        for (repo in trendingRepos) {
            img(src = repo.avatar, classes = "img") {
                this.attrs.height = "100"
                this.attrs.width = "100"
            }
            div {
                p {
                    key = repo.url
                    +"${repo.name}: ${repo.author}"
                }
            }

        }
    }

}
