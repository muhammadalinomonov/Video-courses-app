package uz.gita.videocoursesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.videocoursesapp.domain.repository.AppRepository
import uz.gita.videocoursesapp.domain.repository.impl.AppRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @[Binds Singleton]
    fun bindRepo(impl: AppRepositoryImpl): AppRepository
}