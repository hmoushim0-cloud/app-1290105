package sa.factory.bunyandeedsrealest.data.repository

import sa.factory.bunyandeedsrealest.domain.repository.BunyanDeedsRepository
import sa.factory.bunyandeedsrealest.hardware.HardwareSensorDriver
import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem
import sa.factory.bunyandeedsrealest.model.BunyanDeedsOrder
import sa.factory.bunyandeedsrealest.model.BunyanDeedsTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BunyanDeedsRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : BunyanDeedsRepository {

    private val _catalog = MutableStateFlow<List<BunyanDeedsItem>>(listOf(
        BunyanDeedsItem("1", "الباقة التنفيذية المعتمدة", "تكامل مباشر مع 360 LiDAR Spatial Scanner + Matterport Pro3", 180.0, "معتمد", "الخدمات الرئيسية"),
        BunyanDeedsItem("2", "خدمة المعالجة الفورية", "أداء أصيل بسرعة 60 إطار في الثانية", 95.0, "فوري", "المراقبة الحية"),
        BunyanDeedsItem("3", "باقة الصيانة والدعم السحابي", "تشفير كامل للبيانات المحلية ومزامنة آمنة", 250.0, "شامل", "التقارير الضريبية")
    ))
    override val catalogFlow: Flow<List<BunyanDeedsItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<BunyanDeedsItem>>(emptyList())
    override val activeCart: StateFlow<List<BunyanDeedsItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<BunyanDeedsTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            BunyanDeedsTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: BunyanDeedsItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: BunyanDeedsItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): BunyanDeedsOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = BunyanDeedsOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
