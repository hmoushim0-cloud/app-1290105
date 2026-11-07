package sa.factory.bunyandeedsrealest.domain.repository

import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem
import sa.factory.bunyandeedsrealest.model.BunyanDeedsOrder
import sa.factory.bunyandeedsrealest.model.BunyanDeedsTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BunyanDeedsRepository {
    val catalogFlow: Flow<List<BunyanDeedsItem>>
    val activeCart: StateFlow<List<BunyanDeedsItem>>
    val telemetryFlow: Flow<BunyanDeedsTelemetry>
    
    fun addToCart(item: BunyanDeedsItem)
    fun removeFromCart(item: BunyanDeedsItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): BunyanDeedsOrder
    suspend fun refreshCatalog()
}
