package com.thomaskioko.paybillmanager.presentation.viewmodel.category

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thomaskioko.paybillmanager.domain.interactor.category.CreateCategory
import com.thomaskioko.paybillmanager.domain.interactor.category.UpdateCategory
import com.thomaskioko.paybillmanager.domain.model.Category
import com.thomaskioko.paybillmanager.presentation.model.CategoryView
import com.thomaskioko.paybillmanager.presentation.state.Resource
import com.thomaskioko.paybillmanager.presentation.state.ResourceState
import io.reactivex.observers.DisposableCompletableObserver
import javax.inject.Inject

@VisibleForTesting
class CreateCategoryViewModel @Inject internal constructor(
        private val createCategory: CreateCategory,
        private val updateCategory: UpdateCategory
) : ViewModel() {

    @VisibleForTesting
    private val liveData: MutableLiveData<Resource<CategoryView>> = MutableLiveData()


    override fun onCleared() {
        createCategory.dispose()
        updateCategory.dispose()
        super.onCleared()
    }

    @VisibleForTesting
    fun getCategory(): LiveData<Resource<CategoryView>> {
        return liveData
    }


    fun createCategory(category: Category) {
        liveData.postValue(Resource(ResourceState.LOADING, null, null))
        return createCategory.execute(CreateCategorySubscriber(),
                CreateCategory.Params.forCategory(category)
        )
    }

    fun updateCategory(category: Category) {
        liveData.postValue(Resource(ResourceState.LOADING, null, null))
        return updateCategory.execute(UpdateCategorySubscriber(),
                UpdateCategory.Params.forCategory(category)
        )
    }


    inner class CreateCategorySubscriber : DisposableCompletableObserver() {
        override fun onComplete() {
            liveData.postValue(Resource(ResourceState.SUCCESS, liveData.value?.data, null))
        }

        override fun onError(e: Throwable) {
            liveData.postValue(Resource(ResourceState.ERROR, liveData.value?.data, e.localizedMessage))
        }
    }


    inner class UpdateCategorySubscriber : DisposableCompletableObserver() {
        override fun onComplete() {
            liveData.postValue(Resource(ResourceState.SUCCESS, liveData.value?.data, null))
        }

        override fun onError(e: Throwable) {
            liveData.postValue(Resource(ResourceState.ERROR, liveData.value?.data, e.localizedMessage))
        }
    }
}