import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class CarEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CarEntity>(CarTable)
    var model: String by CarTable.model
    var colour: Colour by CarTable.colour
    var year: Int by CarTable.year
    var engineCapacity: Int by CarTable.engineCapacity
    var mileage: Int by CarTable.mileage

    fun toVm() : CarVM {
        return CarVM(id.value, model, enumToString(colour), year, engineCapacity/10.0, mileage)
    }

    // The following constructor doesn't work:
    // IllegalStateException: Property klass should be initialized before get. (at CarEntity.setModel(CarEntity.kt:7))
/*    constructor(_id: EntityID<Long>, _model: String, _colour: Colour, _year: Int, _engineCapacity: Int, _mileage: Int) : this(_id) {
        model = _model
        colour = _colour
        year = _year
        engineCapacity = _engineCapacity
        mileage = _mileage
    }*/
}