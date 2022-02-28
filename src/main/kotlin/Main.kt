import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson.auto
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.string
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    initAndPopulateDB()
    val repository: CarRepository = JpaCarRepository()

    val listCarLens = Body.auto<List<CarVM>>().toLens()
    val carLens = Body.auto<CarVM>().toLens()
    val idPathLens = Path.string().of("id")
    val colourParamLens = Query.required("colour")

    DebuggingFilters
        .PrintRequestAndResponse()
        .then(ServerFilters.CatchLensFailure)
        .then(
            routes(
                "/cars" bind Method.POST to {
                    val carVM: CarVM = carLens(it)
                    repository.create(carVM)
                    Response(Status.CREATED)
                },

                "/cars" bind Method.PUT to {
                    val carVM: CarVM = carLens(it)
                    repository.update(carVM)
                    Response(Status.NO_CONTENT)
                },

                "/cars/{id}" bind Method.DELETE to {
                    val id: Long = idPathLens(it).toLong()
                    repository.deleteById(id)
                    Response(Status.NO_CONTENT)
                },

                "/cars/{id}" bind Method.GET to {
                    val id: Long = idPathLens(it).toLong()
                    carLens(repository.getById(id), Response(Status.OK))
                },

                "cars/search/by-colour" bind Method.GET to {
                    val colour: String = colourParamLens(it)
                    listCarLens(repository.getAllByColour(stringToEnum(colour)), Response(Status.OK))
                },

                "/cars" bind Method.GET to {
                    listCarLens(repository.getAll(), Response(Status.OK))
                }
            )
        )
        .asServer(SunHttp(8080)).start()
}