import org.jetbrains.exposed.dao.LongIdTable

object CarTable : LongIdTable() {
    val model = varchar("model", 255)
    val colour = enumeration("colour", Colour::class)
    val year = integer("year")
    val engineCapacity = integer("capacity")
}