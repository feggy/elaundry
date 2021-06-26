package net.zero.three.ui.Example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import net.zero.three.api.repository.AuthRepository
import net.zero.three.persistant.AppExecutors

class ExampleViewModel(application: Application) : AndroidViewModel(application) {

    /*
    .
    Contoh hubungan ViewModel dengan Repository
    .
    private var authRepository: AuthRepository = AuthRepository(application, AppExecutors.instance)

    fun eXample(reqExample: ReqExample) = authRepository.eXample(reqExample)
    */

}