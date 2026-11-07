package sa.factory.bunyandeedsrealest.data.remote

import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem
import sa.factory.bunyandeedsrealest.model.BunyanDeedsOrder

interface BunyanDeedsApiService {
    suspend fun fetchCatalog(): List<BunyanDeedsItem>
    suspend fun submitOrder(order: BunyanDeedsOrder): Boolean
}
