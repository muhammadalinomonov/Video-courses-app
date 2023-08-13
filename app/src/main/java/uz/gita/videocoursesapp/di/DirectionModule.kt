package uz.gita.videocoursesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.videocoursesapp.presentation.screen.pages.home.HomeScreenContract
import uz.gita.videocoursesapp.presentation.screen.pages.home.HomeScreenDirectionImpl
import uz.gita.videocoursesapp.presentation.screen.pages.purchased.PurchasedContract
import uz.gita.videocoursesapp.presentation.screen.pages.purchased.PurchasedDirections

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {
    @Binds
    fun bindHomeDirections(impl: HomeScreenDirectionImpl): HomeScreenContract.Directions

    @Binds
    fun bindPurchasedDirections(impl: PurchasedDirections): PurchasedContract.Directions
}