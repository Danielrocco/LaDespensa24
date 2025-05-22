package com.example.ladespensa24.viewmodel

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ladespensa24.models.InCartProduct
import com.example.ladespensa24.models.Product
import com.example.ladespensa24.models.ProductCategories
import com.example.ladespensa24.models.Purchase
import com.example.ladespensa24.R
import com.example.ladespensa24.managers.CloudStorageManager
import com.example.ladespensa24.managers.FirestoreManager
import com.example.ladespensa24.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val firestoreManager = FirestoreManager()
    private val cloudStorageManager = CloudStorageManager()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _imageUrls = MutableStateFlow<Map<String, String?>>(emptyMap())
    val imageUrls: StateFlow<Map<String, String?>> = _imageUrls

    init {
        viewModelScope.launch {
            firestoreManager.getAllProductsFlow().collect { productList ->
                _products.value = productList

                productList.forEach { product ->
                    cloudStorageManager.getProductImage(product) { url ->
                        _imageUrls.update { currentMap ->
                            currentMap + (product.id to url)
                        }
                    }
                }
            }
        }
    }

    private val userDefault: User = User(
        "",
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        null
    )


    //    private val productsInStorage = listOf(
//        Product(
//            "Pan",
//            "1 barra de pan",
//            12.02,
//            ProductCategories.Horno,
//            R.drawable.pan,
//            true,
//            true,
//            50,
//            false
//        ),
//        Product(
//            "Manzana",
//            "180g ",
//            12.02,
//            ProductCategories.Frutería,
//            R.drawable.manzana,
//            true,
//            false,
//            0,
//            true
//        ),
//        Product(
//            "Lechuga",
//            "200g",
//            12.02,
//            ProductCategories.Frutería,
//            R.drawable.lechuga,
//            true,
//            true,
//            60,
//            false
//        ),
//        Product(
//            "Carne",
//            "200g",
//            12.02,
//            ProductCategories.Carnicería,
//            R.drawable.carne,
//            true,
//            true,
//            60,
//            false
//        )
//    )
//
//    fun getAllProducts(): List<Product> = productsInStorage
    private var userEnUso: User = userDefault

    private val _selectedIcon = mutableStateOf("homeScreen")
    val selectedIcon: State<String> = _selectedIcon

    fun selectIcon(screen: String) {
        _selectedIcon.value = screen
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _surname = MutableLiveData<String>()
    val surname: LiveData<String> = _surname

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _payCard = MutableLiveData<String>()
    val payCard: LiveData<String> = _payCard

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _passwd = MutableLiveData<String>()
    val passwd: LiveData<String> = _passwd

    private val _isLogged = MutableLiveData<Boolean>(false)
    val isLogged: LiveData<Boolean> = _isLogged

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _isAlertVisible = MutableLiveData<Boolean>()
    val isAlertVisible: LiveData<Boolean> = _isAlertVisible

    private val _entireAmount = MutableLiveData<Double>(0.0)
    var entireAmount: LiveData<Double> = _entireAmount

    fun setEntireAmount(user: User) {
        _entireAmount.value = user.getEntireAmountOfCart()
    }

    val isNameValid = MutableLiveData(false)
    val isSurnameValid = MutableLiveData(false)
    val isAddressValid = MutableLiveData(false)
    val isEmailValid = MutableLiveData(false)
    val isPasswordValid = MutableLiveData(false)
    val isCardValid = MutableLiveData(false)
    val isEmailExist = MutableLiveData(false)

    fun setUsuarioEnUso(user: User) {
        userEnUso = user
    }

    fun getUsuarioEnUso(): User {
        return userEnUso
    }

    private val users = mutableListOf(
        User(
            "Daniel",
            "Rocco",
            "danielrocco04@gmail.com",
            "hola1234",
            "Calle Gimenez y costa 36",
            "1000200030004000",
            cartProducts = mutableListOf<InCartProduct>(),
            favouriteProducts = mutableListOf<Product>(),
            purchases = mutableListOf<Purchase>()
        )
    )

    fun getUsuarios(): List<User> {
        return users
    }

    val isRegisterEnable: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(isEmailValid) { updateRegisterButtonState() }
        addSource(isPasswordValid) { updateRegisterButtonState() }
        addSource(isCardValid) { updateRegisterButtonState() }
        addSource(name) { updateRegisterButtonState() }
        addSource(surname) { updateRegisterButtonState() }
        addSource(address) { updateRegisterButtonState() }
    }

    private fun updateRegisterButtonState() {
        (isRegisterEnable as MediatorLiveData<Boolean>).value = !name.value.isNullOrEmpty() &&
                !surname.value.isNullOrEmpty() &&
                !address.value.isNullOrEmpty() &&
                isEmailValid.value == true &&
                isPasswordValid.value == true &&
                isCardValid.value == true
    }

    fun register(navController: NavController) {
        val newUser = User(
            name = _name.value ?: "",
            surname = _surname.value ?: "",
            email = _email.value ?: "",
            passwd = _passwd.value ?: "",
            address = _address.value ?: "",
            payCard = _payCard.value ?: "",
            cartProducts = mutableListOf(),
            favouriteProducts = mutableListOf(),
            purchases = mutableListOf()
        )
        if (users.none { it.getEmail() == newUser.getEmail() }) {
            _isLogged.value = true
            users.add(newUser)
            setUsuarioEnUso(newUser)
            clearFormData()
            navController.navigate("userScreen") {
                popUpTo("registerScreen") { inclusive = true }
            }
        } else {
            _isAlertVisible.value = true
        }
    }

    fun onRegisterDataChanged(
        name: String,
        surname: String,
        email: String,
        passwd: String,
        address: String,
        payCard: String
    ) {
        this._name.value = name
        this._surname.value = surname
        this._email.value = email
        this._passwd.value = passwd
        this._address.value = address
        this._payCard.value = payCard

        isNameValid.value = name.isNotBlank()
        isSurnameValid.value = surname.isNotBlank()
        isAddressValid.value = address.isNotBlank()
        isEmailValid.value =
            isValidEmail(email) && !checkIfEmailExists(email)
        isEmailExist.value = checkIfEmailExists(email)
        isPasswordValid.value = isValidPassword(passwd)
        isCardValid.value = isValidCard(payCard)
    }

    fun checkIfEmailExists(email: String): Boolean {
        return users.any { it.getEmail() == email }
    }

    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidPassword(passwd: String): Boolean = passwd.length > 7
    fun isValidCard(card: String): Boolean = card.length == 16 && card.all { it.isDigit() }

    fun login(navController: NavController) {
        val emailValue = email.value.orEmpty()
        val passwdValue = passwd.value.orEmpty()

        if (emailValue.isBlank() || passwdValue.isBlank()) {
            _isAlertVisible.value = true
            return
        }

        val user = users.find { it.getEmail() == emailValue }

        if (user != null && user.getPasswd() == passwdValue) {
            _isLogged.value = true
            setUsuarioEnUso(user)
            clearFormData()
            navController.navigate("userScreen") {
                popUpTo("loginScreen") { inclusive = true }
            }

        } else {
            _isAlertVisible.value = true
        }
    }

    fun logout(navController: NavController) {
        setUsuarioEnUso(userDefault)
        _isLogged.value = false
        clearFormData()
        navController.navigate("homeScreen")
    }

    fun onLoginChanged(email: String, passwd: String) {
        _email.value = email
        _passwd.value = passwd
        _isLoginEnable.value = isValidEmail(email) && isValidPassword(passwd)
    }

    fun clearFormData() {
        _name.value = ""
        _surname.value = ""
        _email.value = ""
        _passwd.value = ""
        _address.value = ""
        _payCard.value = ""
        _isAlertVisible.value = false
    }

    fun toggleFavorite(product: Product) {
        if (_isLogged.value == true) {
            if (userEnUso.isFavorite(product)) {
                userEnUso.removeFromFavorites(product)
            } else {
                userEnUso.addToFavorites(product)
            }
        }
    }

    fun isProductFavorite(product: Product): Boolean {
        return userEnUso.isFavorite(product)
    }
}