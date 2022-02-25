import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class CarDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CarDAO>(CarTable)
    var model: String by CarTable.model
    var colour: Colour by CarTable.colour
    var year: Int by CarTable.year
    var engineCapacity: Int by CarTable.engineCapacity

    fun toVm() : CarVM {
        return CarVM(id.value, model, colour.toString().lowercase().replaceFirstChar { it.uppercase() }, year, engineCapacity/10.0)
    }
}