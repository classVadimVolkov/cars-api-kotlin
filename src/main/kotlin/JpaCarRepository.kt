import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class JpaCarRepository : CarRepository {
    override fun create(carVM: CarVM): CarVM {
        return transaction {
            CarEntity.new {
                model = carVM.model
                colour = stringToEnum(carVM.colour)
                year = carVM.year
                engineCapacity = (carVM.capacity * 10).toInt()
                mileage = carVM.mileage
            }.toVm()
        }
    }

    override fun update(carVM: CarVM): CarVM? {
        return transaction {
            CarTable.update({ CarTable.id eq carVM.id }) {
                it[model] = carVM.model
                it[colour] = stringToEnum(carVM.colour)
                it[year] = carVM.year
                it[engineCapacity] = (carVM.capacity * 10).toInt()
                it[mileage] = carVM.mileage
            }
            CarEntity.reload(Entity(EntityID(carVM.id, CarTable)))?.toVm()
        }
    }

    override fun deleteById(id: Long) {
        transaction { CarTable.deleteWhere { CarTable.id eq id } }
    }

    override fun getById(id: Long): CarVM {
        return transaction { CarEntity.findById(id)!!.toVm() }
    }

    override fun getAll(): List<CarVM> {
        return transaction { CarEntity.all().map { it.toVm() } }
    }

    override fun getAllByColour(colour: Colour): List<CarVM> {
        return transaction {
            CarTable.select { CarTable.colour eq colour }.map {
                CarVM(
                    it[CarTable.id].value,
                    it[CarTable.model],
                    enumToString(it[CarTable.colour]),
                    it[CarTable.year],
                    it[CarTable.engineCapacity] / 10.0,
                    it[CarTable.mileage]
                )
            }
        }
    }
}