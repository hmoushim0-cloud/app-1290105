package sa.factory.bunyandeedsrealest

import sa.factory.bunyandeedsrealest.data.repository.BunyanDeedsRepositoryImpl
import sa.factory.bunyandeedsrealest.hardware.HardwareSensorDriver
import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class BunyanDeedsRegressionTest {

    @Test
    fun testCheckoutCalculationAndTax() = runBlocking {
        val sensorDriver = HardwareSensorDriver()
        val repository = BunyanDeedsRepositoryImpl(sensorDriver)

        val item1 = BunyanDeedsItem("t1", "خدمة تجريبية أ", "الوصف", 100.0, "معتمد", "عام")
        val item2 = BunyanDeedsItem("t2", "خدمة تجريبية ب", "الوصف", 200.0, "معتمد", "عام")

        repository.addToCart(item1)
        repository.addToCart(item2)

        val order = repository.checkout("Mada EMV")

        assertEquals(300.0, order.subtotalSar, 0.01)
        assertEquals(45.0, order.vatSar, 0.01)
        assertEquals(345.0, order.totalSar, 0.01)
        assertEquals(0, repository.activeCart.value.size)
    }
}
