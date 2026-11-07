package sa.factory.bunyandeedsrealest.domain.usecase

import sa.factory.bunyandeedsrealest.domain.repository.BunyanDeedsRepository
import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem
import sa.factory.bunyandeedsrealest.model.BunyanDeedsOrder
import sa.factory.bunyandeedsrealest.model.BunyanDeedsTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: BunyanDeedsRepository) {
    operator fun invoke(): Flow<List<BunyanDeedsItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: BunyanDeedsRepository) {
    val cart = repository.activeCart
    fun add(item: BunyanDeedsItem) = repository.addToCart(item)
    fun remove(item: BunyanDeedsItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: BunyanDeedsRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): BunyanDeedsOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: BunyanDeedsRepository) {
    operator fun invoke(): Flow<BunyanDeedsTelemetry> = repository.telemetryFlow
}
