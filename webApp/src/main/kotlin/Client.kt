import com.baseio.kmm.db.DriverFactory
import com.baseio.kmm.di.SharedComponent
import com.baseio.kmm.di.UseCasesComponent
import com.baseio.kmm.di.initJSDependencies
import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

val sharedComponent = SharedComponent()
val useCasesComponent = UseCasesComponent()

fun main() {
    MainScope().launch {
        prepSQliteDriver()
    }
    window.onload = {
        initJSDependencies()
        val rootDiv = document.getElementById("root")
        render(rootDiv!!) {
            child(TrendingUI)
        }
    }
}

private suspend fun prepSQliteDriver() {
    try {
        val driver = DriverFactory().createDriverBlocking()
        val trendingLocal = sharedComponent.provideGithubTrendingLocal()
        trendingLocal.driver = driver
    } catch (ex: Exception) {
        ex.printStackTrace()
        console.log(ex)
    }
}