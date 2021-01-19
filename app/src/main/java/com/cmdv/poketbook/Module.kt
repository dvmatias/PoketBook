package com.cmdv.poketbook

import com.cmdv.core.navigator.Navigator
import com.cmdv.data.providers.FilesProviderImpl
import com.cmdv.data.repositories.FileRepositoryImpl
import com.cmdv.domain.providers.FilesProvider
import com.cmdv.domain.repositories.FilesRepository
import com.cmdv.feature_landing.ui.ui.booksanddocuments.BooksAndDocumentsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { NavigatorImpl() }
}
val viewModelModule = module {
    viewModel { BooksAndDocumentsViewModel(get()) }
}

val repositoryModule = module {
    single<FilesRepository> { FileRepositoryImpl(get()) }
}

val providerModule = module {
    single<FilesProvider> { FilesProviderImpl() }
}


//val dataSourceModule = module {
//    single<AuthFirebaseSource> { AuthFirebaseSourceImpl() }
//    single<UserFirebaseSource> { FirebaseUserSourceImpl() }
//}
//
//val repositoryModule = module {
//    single<AuthRepository> { AuthRepositoryImpl(get()) }
//    single<UserRepository> { UserRepositoryImpl(get()) }
//    single<ProductRepository> { ProductRepositoryImpl() }
//    single<ShopCartRepository> { ShopCartRepositoryImpl(ShopCartDatabase.getInstance(get()).shopCartDAO) }
//    single<SaleRepository> { SaleRepositoryImpl() }
//}


//val adapterModule = module {
//    single { ProductRecyclerAdapter(get()) }
//}

//val itemDecorationModule = module {
//    single {
//        ItemProductDecoration(
//            androidContext()
//        )
//    }
//}
//
//val librariesModule = module {
//    single { Gson() }
//}