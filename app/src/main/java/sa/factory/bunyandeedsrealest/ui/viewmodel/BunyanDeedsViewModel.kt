package sa.factory.bunyandeedsrealest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.bunyandeedsrealest.domain.usecase.*
import sa.factory.bunyandeedsrealest.model.BunyanDeedsItem
import sa.factory.bunyandeedsrealest.model.BunyanDeedsOrder
import sa.factory.bunyandeedsrealest.model.BunyanDeedsTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BunyanDeedsUiState(
    val catalog: List<BunyanDeedsItem> = emptyList(),
    val filteredCatalog: List<BunyanDeedsItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<BunyanDeedsItem> = emptyList(),
    val telemetry: BunyanDeedsTelemetry? = null,
    val lastOrder: BunyanDeedsOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class BunyanDeedsViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BunyanDeedsUiState())
    val uiState: StateFlow<BunyanDeedsUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: BunyanDeedsItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: BunyanDeedsItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
