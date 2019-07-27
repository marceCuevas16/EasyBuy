package com.marcecuevas.easybuy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcecuevas.easybuy.data.model.DTO.ProductDTO
import com.marcecuevas.easybuy.data.model.DTO.ProductDetailDTO
import com.marcecuevas.easybuy.data.model.DTO.ReviewDTO
import com.marcecuevas.easybuy.data.repository.ProductRepository
import com.marcecuevas.hotelsapp.data.model.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductViewModel(val repository: ProductRepository): ViewModel() {

    private val error = MutableLiveData<String>()

    private val products = MutableLiveData<ProductDTO>()
    private val productDetail = MutableLiveData<ProductDetailDTO>()
    private val productReviews = MutableLiveData<ReviewDTO>()

    private var job: Job? = null

    init {
        this.initGetProductsCall()
    }

    val productsLivedata: LiveData<ProductDTO>
        get() = products

    val productDetailLiveData: LiveData<ProductDetailDTO>
        get() = productDetail

    val productReviewsLiveData: LiveData<ReviewDTO>
        get() = productReviews

    val errorLiveData: MutableLiveData<String>
        get() = error

    private fun initGetProductsCall(){
        job = GlobalScope.launch {
            val value = repository.getProducts()
            when(value){
                is Result.Success -> products.postValue(value.data)
                is Result.Error -> error.postValue(value.message.message)
            }
        }
    }

    fun getProductDetail(id: String){
        job = GlobalScope.launch {
            val value = repository.getProductDetail(id)
            when(value){
                is Result.Success -> productDetail.postValue(value.data)
                is Result.Error -> error.postValue(value.message.message)
            }
        }
    }

    fun getReviewsFromProduct(id: String){
        job = GlobalScope.launch {
            val value = repository.getReviewsFromProduct(id)
            when(value){
                is Result.Success -> productReviews.postValue(value.data)
                is Result.Error -> error.postValue(value.message.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}