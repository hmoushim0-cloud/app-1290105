package sa.factory.bunyandeedsrealest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem

@Entity(tableName = "real_estate_items_cache")
data class BunyanDeedsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priceSar: Double,
    val badge: String,
    val category: String,
    val status: String
) {
    fun toDomain(): BunyanDeedsItem = BunyanDeedsItem(
        id = id,
        title = title,
        description = description,
        priceSar = priceSar,
        badge = badge,
        category = category,
        status = status
    )
}
