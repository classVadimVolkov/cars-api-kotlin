import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DbParams {
    const val URL: String = "jdbc:h2:mem:cars"
    const val DRIVER: String = "org.h2.Driver"
    const val USER: String = ""
    const val PASSWORD: String = ""
}

fun main() {
    Database.connect(url = DbParams.URL, driver = DbParams.DRIVER, user = DbParams.USER, password = DbParams.PASSWORD)
    val repository: CarRepository = JpaCarRepository()

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(CarTable)

        val created1 = repository.create(CarVM(null, "Toyota", "Blue", 2010, 1.8))
        println("Created: $created1")

        val created2 = repository.create(CarVM(null, "Mercedes", "Green", 2015, 2.5))
        println("Created: $created2")

        val updated = repository.update(CarVM(1, "Ford", "Red", 2020, 2.0))
        println("Updated: $updated")

        val all = repository.getAll()
        println("Get all: $all")

        val allByColour = repository.getAllByColour(Colour.GREEN)
        println("Get all by colour: $allByColour")

        repository.deleteById(2)
        println("After deleting by id: ${repository.getAll()}")
    }
}