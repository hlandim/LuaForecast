package com.hlandim.luas.util

import com.hlandim.luas.data.FakeRepositoryImp
import com.hlandim.luas.data.Repository
import com.hlandim.luas.di.DataModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [DataModule::class]
)
@Module
class FakeDataModule {
    @Provides
    fun getRepository(): Repository = FakeRepositoryImp()
}