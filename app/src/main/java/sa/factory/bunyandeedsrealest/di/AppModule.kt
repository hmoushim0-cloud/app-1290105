package sa.factory.bunyandeedsrealest.di

import sa.factory.bunyandeedsrealest.data.repository.BunyanDeedsRepositoryImpl
import sa.factory.bunyandeedsrealest.domain.repository.BunyanDeedsRepository
import sa.factory.bunyandeedsrealest.domain.usecase.*
import sa.factory.bunyandeedsrealest.hardware.HardwareSensorDriver
import sa.factory.bunyandeedsrealest.ui.viewmodel.BunyanDeedsViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: BunyanDeedsRepository = BunyanDeedsRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): BunyanDeedsViewModel = BunyanDeedsViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}
