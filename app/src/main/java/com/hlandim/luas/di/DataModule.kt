package com.hlandim.luas.di

import com.hlandim.luas.data.Repository
import com.hlandim.luas.data.RepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindRepository(repositoryImp: RepositoryImp): Repository
}