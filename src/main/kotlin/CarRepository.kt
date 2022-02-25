interface CarRepository {
    fun create(carVM: CarVM): CarVM
    fun update(carVM: CarVM): CarVM?
    fun deleteById(id: Long)
    fun getById(id: Long): CarVM
    fun getAll(): List<CarVM>
    fun getAllByColour(colour: Colour): List<CarVM>
}