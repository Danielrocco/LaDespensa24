package com.example.ladespensa24.viewmodel

import android.util.Log
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
import com.example.ladespensa24.models.Purchase
import com.example.ladespensa24.managers.CloudStorageManager
import com.example.ladespensa24.managers.FirestoreManager
import com.example.ladespensa24.models.ProductCategories
import com.example.ladespensa24.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    // === Firebase ===
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val firestoreManager = FirestoreManager()
    private val cloudStorageManager = CloudStorageManager()

    // === UI State ===
    private val _selectedIcon = mutableStateOf("homeScreen")
    val selectedIcon: State<String> = _selectedIcon

    fun selectIcon(screen: String) {
        _selectedIcon.value = screen
    }

    // === Productos ===
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _imageUrls = MutableStateFlow<Map<String, String?>>(emptyMap())
    val imageUrls: StateFlow<Map<String, String?>> = _imageUrls

    // === Usuario en uso ===
    private val _userEnUso = MutableStateFlow<User>(User())
    val userEnUso: StateFlow<User> = _userEnUso

    fun getUsuarioEnUso(): User = _userEnUso.value

    private val _entireAmount = MutableLiveData<Double>(0.0)
    val entireAmount: LiveData<Double> = _entireAmount

    fun setEntireAmount(user: User) {
        _entireAmount.value = user.getEntireAmountOfCart()
    }

    fun actualizarUsuarioEnFirestore(user: User) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).set(user)
    }

    // === Formularios ===
    private val _name = MutableLiveData<String>()
    private val _surname = MutableLiveData<String>()
    private val _address = MutableLiveData<String>()
    private val _payCard = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _passwd = MutableLiveData<String>()

    val name: LiveData<String> = _name
    val surname: LiveData<String> = _surname
    val address: LiveData<String> = _address
    val payCard: LiveData<String> = _payCard
    val email: LiveData<String> = _email
    val passwd: LiveData<String> = _passwd

    private val _isAlertVisible = MutableLiveData<Boolean>()
    val isAlertVisible: LiveData<Boolean> = _isAlertVisible

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    // === Validaciones ===
    val isNameValid = MutableLiveData(false)
    val isSurnameValid = MutableLiveData(false)
    val isAddressValid = MutableLiveData(false)
    val isEmailValid = MutableLiveData(false)
    val isPasswordValid = MutableLiveData(false)
    val isCardValid = MutableLiveData(false)
    val isEmailExist = MutableLiveData(false)

    val isRegisterEnable: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(isEmailValid) { updateRegisterButtonState() }
        addSource(isEmailExist) { updateRegisterButtonState() }
        addSource(isPasswordValid) { updateRegisterButtonState() }
        addSource(isCardValid) { updateRegisterButtonState() }
        addSource(name) { updateRegisterButtonState() }
        addSource(surname) { updateRegisterButtonState() }
        addSource(address) { updateRegisterButtonState() }
    }

    private fun updateRegisterButtonState() {
        (isRegisterEnable as MediatorLiveData<Boolean>).value =
            !name.value.isNullOrEmpty() &&
                    !surname.value.isNullOrEmpty() &&
                    !address.value.isNullOrEmpty() &&
                    isEmailValid.value == true &&
                    isEmailExist.value == false &&
                    isPasswordValid.value == true &&
                    isCardValid.value == true
    }

    private val _isLogged = mutableStateOf(false)
    val isLogged: State<Boolean> = _isLogged

    private val _isUserLoading = mutableStateOf(true)
    val isUserLoading: State<Boolean> = _isUserLoading

    init {
        Log.d("ViewModel", "🔥 Iniciando ViewModel")
        collectProducts()
        setupFirebaseAuthListener()
        ensureInitialUser()
    }

    private fun setupFirebaseAuthListener() {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d(
                "AuthListener",
                "🎯 Estado de autenticación cambiado. Current user: ${user?.uid}, isAnonymous: ${user?.isAnonymous}"
            )

            if (user == null) {
                _isLogged.value = false
                _userEnUso.value = User(name = "No autenticado")
                _isUserLoading.value = false
                Log.d("AuthListener", "🚫 No hay usuario autenticado. isLogged: false.")
            } else {
                _isLogged.value = !user.isAnonymous
                Log.d(
                    "AuthListener",
                    "✔️ Usuario detectado: UID=${user.uid}, isAnonymous=${user.isAnonymous}, isLogged: ${_isLogged.value}"
                )
                verifyCurrentUser(user.uid, user.isAnonymous)
                _isUserLoading.value = false
            }
        }
    }

    private fun ensureInitialUser() {
        _isUserLoading.value = true

        val currentUser = auth.currentUser
        if (currentUser == null) {
            auth.signInAnonymously()
                .addOnSuccessListener { anonResult ->
                    Log.d(
                        "InitialUser",
                        "✅ Usuario anónimo creado/restaurado inicialmente: ${anonResult.user?.uid}"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "InitialUser",
                        "❌ Error al iniciar sesión anónima al inicio: ${e.message}",
                        e
                    )
                    _isUserLoading.value = false
                    _isLogged.value = false
                    _userEnUso.value = User(name = "Error inicial")
                }
        } else {
            Log.d(
                "InitialUser",
                "👌 Ya hay un usuario al inicio: ${currentUser.uid}, isAnonymous: ${currentUser.isAnonymous}"
            )
            _isUserLoading.value = false
        }
    }

    private fun collectProducts() {
        viewModelScope.launch {
            firestoreManager.getAllProductsFlow().collect { productList ->
                _products.value = productList
                productList.forEach { product ->
                    cloudStorageManager.getProductImage(product) { url ->
                        _imageUrls.update { it + (product.id to url) }
                    }
                }
            }
        }
    }

    fun verifyCurrentUser(uid: String, isAnonymous: Boolean) {
        if (uid.isEmpty()) {
            Log.e("Firestore", "❌ verifyCurrentUser: UID vacío, no se puede obtener el usuario.")
            _userEnUso.value = User(
                name = "Anonimo Default",
                surname = "Error",
                address = "",
                payCard = "",
                cartProducts = mutableListOf(),
                favouriteProducts = mutableListOf(),
                purchases = mutableListOf()
            )
            return
        }

        Log.d("Firestore", "✅ Obteniendo usuario de Firestore para UID: $uid")

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val user = doc.toObject(User::class.java)
                    user?.cartProducts =
                        (doc["cartProducts"] as? List<Map<String, Any>>)?.map { it.toInCartProduct() }
                            ?.toMutableList() ?: mutableListOf()
                    user?.favouriteProducts =
                        (doc["favouriteProducts"] as? List<Map<String, Any>>)?.map { it.toProduct() }
                            ?.toMutableList() ?: mutableListOf()
                    user?.purchases =
                        (doc["purchases"] as? List<Map<String, Any>>)?.map { it.toPurchase() }
                            ?.toMutableList() ?: mutableListOf()

                    _userEnUso.value = user ?: User(name = "Usuario Incompleto")
                    Log.d(
                        "Firestore",
                        "✅ Usuario de Firestore obtenido: ${user?.name ?: "No encontrado"}, es anónimo: $isAnonymous"
                    )
                } else {
                    Log.d(
                        "Firestore",
                        "ℹ️ Documento de usuario no existe en Firestore para UID: $uid. Creando perfil por defecto."
                    )
                    val newUserProfile = if (isAnonymous) {
                        createAnonUser(uid)
                    } else {
                        User(
                            name = auth.currentUser?.email?.substringBefore("@") ?: "Usuario",
                            surname = "",
                            address = "",
                            payCard = "",
                            cartProducts = mutableListOf(),
                            favouriteProducts = mutableListOf(),
                            purchases = mutableListOf()
                        )
                    }
                    firestore.collection("users").document(uid).set(newUserProfile)
                        .addOnSuccessListener {
                            _userEnUso.value = newUserProfile
                            Log.d(
                                "Firestore",
                                "✅ Nuevo perfil creado y guardado en Firestore para UID: $uid"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                "Firestore",
                                "❌ Error al crear perfil en Firestore para UID: $uid",
                                e
                            )
                            _userEnUso.value = User(name = "Error en Firestore")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Error al obtener documento de Firestore para UID $uid", e)
                _userEnUso.value = User(name = "Error de conexión")
            }
    }

    fun Map<String, Any>.toInCartProduct(): InCartProduct {
        return InCartProduct(
            id = this["id"] as String,
            title = this["title"] as String,
            description = this["description"] as String,
            price = this["price"] as Double,
            category = ProductCategories.valueOf(this["category"] as String),
            isFeatured = this["isFeatured"] as? Boolean ?: false,
            isDiscounted = this["isDiscounted"] as? Boolean ?: false,
            discount = (this["discount"] as? Long)?.toInt() ?: (this["discount"] as? Int ?: 0),
            isNew = this["isNew"] as? Boolean ?: false,
            units = (this["units"] as? Long)?.toInt() ?: 0
        )
    }

    fun Map<String, Any>.toProduct(): Product {
        return Product(
            id = this["id"] as String,
            title = this["title"] as String,
            description = this["description"] as String,
            price = this["price"] as Double,
            category = ProductCategories.valueOf(this["category"] as String),
            isFeatured = this["isFeatured"] as? Boolean ?: false,
            isDiscounted = this["isDiscounted"] as? Boolean ?: false,
            discount = (this["discount"] as? Long)?.toInt() ?: (this["discount"] as? Int ?: 0),
            isNew = this["isNew"] as? Boolean ?: false,
        )
    }

    fun Map<String, Any>.toPurchase(): Purchase {
        return Purchase(
            inCartProducts = (this["inCartProducts"] as List<Map<String, Any>>).map { it.toInCartProduct() },
            date = this["date"] as String,
            cardNumber = this["cardNumber"] as String,
            purchaseId = this["purchaseId"] as String
        )
    }

    private fun createAnonUser(uid: String): User {
        Log.d("Firestore", "✅ Creando objeto User anónimo para UID: $uid")
        return User(
            name = "Anonimo",
            surname = "",
            address = "",
            payCard = "",
            cartProducts = mutableListOf(),
            favouriteProducts = mutableListOf(),
            purchases = mutableListOf()
        )
    }

    fun login(navController: NavController) {
        val emailValue = _email.value.orEmpty()
        val passwdValue = _passwd.value.orEmpty()

        if (emailValue.isBlank() || passwdValue.isBlank()) {
            _isAlertVisible.value = true
            return
        }

        auth.signInWithEmailAndPassword(emailValue, passwdValue)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                Log.d("Auth", "✅ Inicio de sesión exitoso para UID: $uid")
                _isLogged.value = true
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        val user = doc.toObject(User::class.java)
                        if (user != null) {
                            user.cartProducts =
                                (doc["cartProducts"] as? List<Map<String, Any>>)?.map { it.toInCartProduct() }
                                    ?.toMutableList() ?: mutableListOf()
                            user.favouriteProducts =
                                (doc["favouriteProducts"] as? List<Map<String, Any>>)?.map { it.toProduct() }
                                    ?.toMutableList() ?: mutableListOf()
                            user.purchases =
                                (doc["purchases"] as? List<Map<String, Any>>)?.map { it.toPurchase() }
                                    ?.toMutableList() ?: mutableListOf()
                            _userEnUso.value = user
                            clearFormData()
                            navController.navigate("userScreen") {
                                popUpTo("loginScreen") { inclusive = true }
                            }
                        } else {
                            Log.w(
                                "Firestore",
                                "⚠️ Usuario loggeado pero sin documento en Firestore: $uid"
                            )
                            _isAlertVisible.value = true
                            auth.signOut()
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "❌ Error al obtener perfil de usuario tras login", it)
                        _isAlertVisible.value = true
                        auth.signOut()
                    }
            }
            .addOnFailureListener {
                Log.e("Auth", "❌ Error al iniciar sesión", it)
                _isAlertVisible.value = true
            }
    }

    fun register(navController: NavController) {
        val email = _email.value ?: ""
        val password = _passwd.value ?: ""

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                val newUser = User(
                    name = _name.value ?: "",
                    surname = _surname.value ?: "",
                    address = _address.value ?: "",
                    payCard = _payCard.value ?: "",
                    cartProducts = mutableListOf(),
                    favouriteProducts = mutableListOf(),
                    purchases = mutableListOf(),
                )
                firestore.collection("users").document(uid).set(newUser)
                    .addOnSuccessListener {
                        _userEnUso.value = newUser
                        _isLogged.value = true
                        clearFormData()
                        navController.navigate("userScreen") {
                            popUpTo("registerScreen") { inclusive = true }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "❌ Error al guardar nuevo usuario en Firestore", e)
                        authResult.user?.delete()
                        _isAlertVisible.value = true
                    }
            }
            .addOnFailureListener {
                Log.e("Auth", "❌ Error al registrar usuario", it)
                if (it is FirebaseAuthUserCollisionException) {
                    isEmailExist.value = true
                } else {
                    _isAlertVisible.value = true
                }
            }
    }

    fun logout(navController: NavController, viewModel: MyViewModel) {
        Log.d("Logout", "🚫 Cerrando sesión. UID actual: ${auth.currentUser?.uid}")
        auth.signOut()
        viewModel.limpiarUsuario()
        _isLogged.value = false
        Log.d(
            "Logout",
            "✅ Sesión cerrada. isLogged se establece en false. Firebase AuthListener manejará el estado anónimo."
        )

        navController.navigate("loginScreen") {
            popUpTo("loginScreen") { inclusive = true }
            launchSingleTop = true
        }
    }

    fun limpiarUsuario() {
        _userEnUso.value = User(
            name = "",
            surname = "",
            address = "",
            payCard = "",
            cartProducts = mutableListOf(),
            favouriteProducts = mutableListOf(),
            purchases = mutableListOf()
        )
        Log.d("Firestore", "✅ Usuario limpiado correctamente")
    }

    // === Otros ===
    fun onLoginChanged(email: String, passwd: String) {
        _email.value = email
        _passwd.value = passwd
        _isLoginEnable.value = isValidEmail(email) && isValidPassword(passwd)
    }

    fun onRegisterDataChanged(
        name: String, surname: String, email: String,
        passwd: String, address: String, payCard: String
    ) {
        _name.value = name
        _surname.value = surname
        _email.value = email
        _passwd.value = passwd
        _address.value = address
        _payCard.value = payCard

        isNameValid.value = name.isNotBlank()
        isSurnameValid.value = surname.isNotBlank()
        isAddressValid.value = address.isNotBlank()
        isEmailValid.value = isValidEmail(email)
        isPasswordValid.value = isValidPassword(passwd)
        isCardValid.value = isValidCard(payCard)
        isEmailExist.value = false
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

    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidPassword(passwd: String): Boolean = passwd.length > 7
    fun isValidCard(card: String): Boolean = card.length == 16 && card.all { it.isDigit() }

    fun toggleFavorite(product: Product, viewModel: MyViewModel) {
        val user = _userEnUso.value ?: return
        if (!auth.currentUser?.isAnonymous!!) {
            if (user.isFavorite(product)) {
                user.removeFromFavorites(product, viewModel)
            } else {
                user.addToFavorites(product, viewModel)
            }
            _userEnUso.value = user
        }
    }

    fun isProductFavorite(product: Product): Boolean {
        return _userEnUso.value?.isFavorite(product) ?: false
    }
}