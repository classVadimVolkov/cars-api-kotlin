import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DbParams {
    const val URL: String = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    const val DRIVER: String = "org.h2.Driver"
}

fun initAndPopulateDB() {
    Database.connect(DbParams.URL, DbParams.DRIVER)
    return transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(CarTable)
        val repository: CarRepository = JpaCarRepository()
        repository.create(CarVM(null, "Toyota", "Blue", 2010, 1.8, 122000))
        repository.create(CarVM(null, "Mercedes", "Green", 2015, 2.5, 55100))
    }
}