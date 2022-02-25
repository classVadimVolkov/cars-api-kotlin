import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class JpaCarRepository : CarRepository {
    override fun create(carVM: CarVM): CarVM {
        return CarDAO.new {
            model = carVM.model
            colour = Colour.valueOf(carVM.colour.uppercase())
            year = carVM.year
            engineCapacity = (carVM.engineCapacity * 10).toInt()
        }.toVm()
    }

    override fun update(carVM: CarVM): CarVM? {
        CarTable.update({ CarTable.id eq carVM.id }) {
            it[model] = carVM.model
            it[colour] = Colour.valueOf(carVM.colour.uppercase())
            it[year] = carVM.year
            it[engineCapacity] = (carVM.engineCapacity * 10).toInt()
        }
        return CarDAO.reload(Entity(EntityID(carVM.id, CarTable)))?.toVm()
    }

    override fun deleteById(id: Long) {
        CarTable.deleteWhere { CarTable.id eq id }
    }

    override fun getById(id: Long): CarVM {
        return CarDAO.findById(id)!!.toVm()
    }

    override fun getAll(): List<CarVM> {
        return transaction { CarDAO.all().map { it.toVm() } }
    }

    override fun getAllByColour(colour: Colour): List<CarVM> {
        return CarTable.select { CarTable.colour eq colour }.map {
            CarVM(
                it[CarTable.id].value,
                it[CarTable.model],
                it[CarTable.colour].toString().lowercase().replaceFirstChar { char -> char.uppercase() },
                it[CarTable.year],
                it[CarTable.engineCapacity] / 10.0
            )
        }
    }
}