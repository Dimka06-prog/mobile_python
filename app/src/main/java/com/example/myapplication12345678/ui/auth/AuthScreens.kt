package com.example.myapplication12345678.ui.auth

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication12345678.data.CodeSnippet
import com.example.myapplication12345678.data.Course
import com.example.myapplication12345678.data.ExcelExporter
import com.example.myapplication12345678.data.Lesson
import com.example.myapplication12345678.data.LocalDatabase
import com.example.myapplication12345678.data.UserStats
import com.example.myapplication12345678.ui.theme.AccentOrange
import com.example.myapplication12345678.ui.theme.AccentPink
import com.example.myapplication12345678.ui.theme.DarkBackground
import com.example.myapplication12345678.ui.theme.GradientEnd
import com.example.myapplication12345678.ui.theme.GradientStart
import com.example.myapplication12345678.ui.theme.LightBackground
import com.example.myapplication12345678.ui.theme.SecondaryBlue
import com.example.myapplication12345678.ui.theme.SuccessGreen
import com.example.myapplication12345678.ui.theme.ThemeState

@Composable
fun AuthRoot() {
    var selectedTab by remember { mutableStateOf(0) }
    var currentScreen by remember { mutableStateOf("auth") }
    var currentUserLogin by remember { mutableStateOf<String?>(null) }
    var isGuestMode by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val db = remember { LocalDatabase(context) }

    val isDark = ThemeState.isDarkTheme
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.05f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.1f)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (currentScreen == "auth") Modifier.verticalScroll(rememberScrollState()) else Modifier)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = if (currentScreen == "user") 16.dp else 32.dp,
                        bottom = if (currentScreen == "user") 0.dp else 32.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Переключатель темы в углу
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(cardBg)
                            .clickable { ThemeState.isDarkTheme = !ThemeState.isDarkTheme }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isDark) "Светлая" else "Тёмная",
                            color = textColor,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (currentScreen == "user") 8.dp else 20.dp))

                // Шапка только для авторизации, чтобы на вкладках пользователя было больше места под контент
                if (currentScreen == "auth") {
                    Text(
                        text = "ProPython",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Text(
                        text = if (selectedTab == 0) "Войдите в аккаунт" else "Создайте новый аккаунт",
                        fontSize = 14.sp,
                        color = textColor.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }

                when (currentScreen) {
                    "auth" -> {
                        AuthTabs(selectedTab = selectedTab, onTabSelected = { index ->
                            Log.d("Auth", "Tab changed to index=$index")
                            errorMessage = null
                            selectedTab = index
                        }, isDark = isDark)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Сообщение об ошибке
                        errorMessage?.let { msg ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AccentPink.copy(alpha = 0.2f))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = msg,
                                    color = AccentPink,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Стеклянная карточка
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(cardBg)
                                .border(
                                    width = 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(28.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (selectedTab == 0) {
                                    LoginScreen(
                                        isDark = isDark,
                                        onLoginSuccess = { isAdmin, login ->
                                            errorMessage = null
                                            isGuestMode = false
                                            if (!isAdmin) {
                                                currentUserLogin = login
                                            }
                                            currentScreen = if (isAdmin) "admin" else "user"
                                        },
                                        onError = { errorMessage = it },
                                        onGuestLogin = {
                                            errorMessage = null
                                            isGuestMode = true
                                            currentUserLogin = null
                                            currentScreen = "user"
                                        }
                                    )
                                } else {
                                    RegistrationScreen(
                                        isDark = isDark,
                                        onRegistered = { login ->
                                            errorMessage = null
                                            isGuestMode = false
                                            currentUserLogin = null
                                            selectedTab = 0
                                            currentScreen = "auth"
                                        },
                                        onError = { errorMessage = it }
                                    )
                                }
                            }
                        }

                    }

                    "user" -> {
                        UserMainScreen(
                            db = db,
                            login = currentUserLogin,
                            isGuest = isGuestMode,
                            isDark = isDark,
                            onLogout = {
                                isGuestMode = false
                                currentScreen = "auth"
                                selectedTab = 0
                            }
                        )
                    }

                    "admin" -> {
                        AdminScreen(
                            isDark = isDark,
                            onLogout = {
                                currentScreen = "auth"
                                selectedTab = 0
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthTabs(selectedTab: Int, onTabSelected: (Int) -> Unit, isDark: Boolean) {
    val titles = listOf("Вход", "Регистрация")
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.05f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        titles.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) SecondaryBlue.copy(alpha = 0.35f)
                        else Color.Transparent
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (isSelected) Color.White else textColor.copy(alpha = 0.6f),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    isDark: Boolean,
    onLoginSuccess: (isAdmin: Boolean, login: String?) -> Unit,
    onError: (String) -> Unit,
    onGuestLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = remember { LocalDatabase(context) }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val textColor = if (isDark) Color.White else Color.Black

    Column {
        ModernTextField(
            value = login,
            onValueChange = { login = it },
            label = "Логин",
            icon = "",
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            icon = "",
            isPassword = true,
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = "Войти",
            onClick = {
                Log.d("Auth", "Login clicked: login=$login")

                if (login.isBlank() || password.isBlank()) {
                    onError("Заполните все поля")
                    return@GradientButton
                }

                if (login == "admin" && password == "admin123") {
                    onLoginSuccess(true, login)
                    return@GradientButton
                }

                try {
                    val cursor = db.readableDatabase.rawQuery(
                        "SELECT first_name, last_name, login, email FROM users WHERE login = ? AND password = ?",
                        arrayOf(login, password)
                    )
                    val success = cursor.use { it.moveToFirst() }

                    if (success) {
                        Log.d("Auth", "User login success: login=$login")
                        onLoginSuccess(false, login)
                    } else {
                        Log.d("Auth", "User login failed for login=$login")
                        onError("Неверный логин или пароль")
                    }
                } catch (e: Exception) {
                    Log.e("Auth", "Login error: ${e.message}")
                    onError("Ошибка входа: ${e.message}")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка гостевого входа
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f))
                .clickable { onGuestLogin() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Войти как гость",
                color = textColor.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопка забыли пароль
        var showForgotPassword by remember { mutableStateOf(false) }
        var forgotLogin by remember { mutableStateOf("") }
        var forgotSecretWord by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var verificationSuccess by remember { mutableStateOf(false) }
        var forgotError by remember { mutableStateOf("") }

        if (!showForgotPassword) {
            Text(
                text = "Забыли пароль?",
                color = SecondaryBlue,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showForgotPassword = true },
                textAlign = TextAlign.Center
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (verificationSuccess) "Новый пароль" else "Восстановление",
                            color = textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "",
                            color = textColor.copy(alpha = 0.5f),
                            fontSize = 18.sp,
                            modifier = Modifier.clickable {
                                showForgotPassword = false
                                verificationSuccess = false
                                forgotLogin = ""
                                forgotSecretWord = ""
                                newPassword = ""
                                confirmPassword = ""
                                forgotError = ""
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!verificationSuccess) {
                        ModernTextField(
                            value = forgotLogin,
                            onValueChange = { forgotLogin = it },
                            label = "Логин",
                            icon = "",
                            isDark = isDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ModernTextField(
                            value = forgotSecretWord,
                            onValueChange = { forgotSecretWord = it },
                            label = "Секретное слово",
                            icon = "",
                            isDark = isDark
                        )

                        if (forgotError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = forgotError,
                                color = AccentPink,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        GradientButton(
                            text = "Проверить",
                            onClick = {
                                if (forgotLogin.isBlank() || forgotSecretWord.isBlank()) {
                                    forgotError = "Заполните все поля"
                                    return@GradientButton
                                }
                                if (db.verifySecretWord(forgotLogin, forgotSecretWord)) {
                                    verificationSuccess = true
                                    forgotError = ""
                                } else {
                                    forgotError = "Неверный логин или секретное слово"
                                }
                            }
                        )
                    } else {
                        ModernTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = "Новый пароль",
                            icon = "",
                            isPassword = true,
                            isDark = isDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ModernTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Подтвердите пароль",
                            icon = "",
                            isPassword = true,
                            isDark = isDark
                        )

                        if (forgotError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = forgotError,
                                color = AccentPink,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        GradientButton(
                            text = "Сохранить пароль",
                            onClick = {
                                if (newPassword.isBlank() || confirmPassword.isBlank()) {
                                    forgotError = "Заполните все поля"
                                    return@GradientButton
                                }
                                if (newPassword != confirmPassword) {
                                    forgotError = "Пароли не совпадают"
                                    return@GradientButton
                                }
                                if (db.updatePassword(forgotLogin, newPassword)) {
                                    showForgotPassword = false
                                    verificationSuccess = false
                                    forgotLogin = ""
                                    forgotSecretWord = ""
                                    newPassword = ""
                                    confirmPassword = ""
                                    forgotError = ""
                                } else {
                                    forgotError = "Ошибка сохранения пароля"
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(
    isDark: Boolean,
    onRegistered: (login: String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val db = remember { LocalDatabase(context) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var secretWord by remember { mutableStateOf("") }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var avatarPath by remember { mutableStateOf<String?>(null) }

    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    // Launcher для выбора изображения из галереи
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                avatarBitmap = bitmap
                
                // Временно сохраняем путь (будет обновлён после регистрации)
                val fileName = "avatar_temp_${System.currentTimeMillis()}.jpg"
                val file = java.io.File(context.filesDir, fileName)
                val outputStream = java.io.FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.close()
                avatarPath = file.absolutePath
            } catch (e: Exception) {
                Log.e("Registration", "Error loading avatar: ${e.message}")
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Аватарка с возможностью загрузки из галереи
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    color = if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.06f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap!!.asImageBitmap(),
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Text(
                    text = "Фото",
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Нажмите для загрузки фото",
            color = textColor.copy(alpha = 0.4f),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        ModernTextField(value = firstName, onValueChange = { firstName = it }, label = "Имя", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = lastName, onValueChange = { lastName = it }, label = "Фамилия", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = login, onValueChange = { login = it }, label = "Логин", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = email, onValueChange = { email = it }, label = "Email", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = password, onValueChange = { password = it }, label = "Пароль", icon = "", isPassword = true, isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = secretWord, onValueChange = { secretWord = it }, label = "Секретное слово для восстановления", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = "Зарегистрироваться",
            onClick = {
                if (firstName.isBlank() || lastName.isBlank() || login.isBlank() || email.isBlank() || password.isBlank() || secretWord.isBlank()) {
                    onError("Заполните все поля")
                    return@GradientButton
                }

                try {
                    // Проверка, есть ли уже такой логин
                    val checkCursor = db.readableDatabase.rawQuery(
                        "SELECT login FROM users WHERE login = ?",
                        arrayOf(login)
                    )
                    val exists = checkCursor.use { it.moveToFirst() }

                    if (exists) {
                        onError("Пользователь с таким логином уже существует")
                        return@GradientButton
                    }

                    // Если есть аватар, переименовываем файл с правильным именем
                    val finalAvatarPath = if (avatarPath != null) {
                        val oldFile = java.io.File(avatarPath!!)
                        val newFileName = "avatar_${login}_${System.currentTimeMillis()}.jpg"
                        val newFile = java.io.File(context.filesDir, newFileName)
                        oldFile.renameTo(newFile)
                        newFile.absolutePath
                    } else {
                        "" // Пустая строка если аватар не загружен
                    }

                    db.writableDatabase.execSQL(
                        "INSERT INTO users (first_name, last_name, login, email, password, avatar, secret_word) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        arrayOf(firstName, lastName, login, email, password, finalAvatarPath, secretWord)
                    )
                    Log.d("Auth", "User registered: $login")
                    onRegistered(login)
                } catch (e: Exception) {
                    Log.e("Auth", "Registration error: ${e.message}")
                    onError("Ошибка регистрации: ${e.message}")
                }
            }
        )
    }
}

@Composable
fun UserMainScreen(
    db: LocalDatabase,
    login: String?,
    isGuest: Boolean,
    isDark: Boolean,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    var courses by remember { mutableStateOf(db.getAllCourses()) }
    var favoriteCourses by remember { mutableStateOf(if (login != null) db.getFavoriteCourses(login) else emptyList()) }

    var showGuestCourseMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (selectedLesson != null) {
            LessonDetailScreen(
                lesson = selectedLesson!!,
                isDark = isDark,
                onBack = { selectedLesson = null },
                onComplete = {
                    if (login != null) {
                        db.markLessonCompleted(login, selectedLesson!!.id)
                    }
                }
            )
        } else if (showGuestCourseMessage && isGuest) {
            GuestCourseContent(
                isDark = isDark,
                onBack = { showGuestCourseMessage = false }
            )
        } else if (selectedCourse != null) {
            CourseLessonsScreen(
                db = db,
                course = selectedCourse!!,
                login = login,
                isDark = isDark,
                onBack = { selectedCourse = null },
                onLessonClick = { selectedLesson = it },
                onFavoriteChanged = { if (login != null) favoriteCourses = db.getFavoriteCourses(login) }
            )
        } else {
            val tabs = if (isGuest) {
                listOf("courses", "profile", "stats")
            } else {
                listOf("courses", "favorites", "profile", "stats")
            }

            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    NavigationBar(containerColor = cardBg) {
                        tabs.forEachIndexed { index, key ->
                            val icon = when (key) {
                                "courses" -> Icons.Filled.School
                                "favorites" -> Icons.Filled.Star
                                "profile" -> Icons.Filled.Person
                                else -> Icons.Filled.BarChart
                            }
                            val label = when (key) {
                                "courses" -> "Курсы"
                                "favorites" -> "Избранное"
                                "profile" -> "Профиль"
                                else -> "Статистика"
                            }

                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (selectedTab == index) GradientEnd else textColor.copy(alpha = 0.7f)
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        color = if (selectedTab == index) GradientEnd else textColor.copy(alpha = 0.7f)
                                    )
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(cardBg)
                            .border(
                                width = 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            when (tabs.getOrNull(selectedTab)) {
                                "courses" -> CoursesTabContentNew(
                                    courses = courses,
                                    isDark = isDark,
                                    onCourseClick = { course ->
                                        if (isGuest) {
                                            showGuestCourseMessage = true
                                        } else {
                                            selectedCourse = course
                                        }
                                    }
                                )

                                "favorites" -> FavoritesTabContent(
                                    courses = favoriteCourses,
                                    isDark = isDark,
                                    onCourseClick = { selectedCourse = it }
                                )

                                "profile" -> if (isGuest) {
                                    GuestProfileContent(isDark = isDark)
                                } else {
                                    ProfileTabContent(
                                        db = db,
                                        login = login,
                                        isDark = isDark,
                                        onLogout = onLogout
                                    )
                                }

                                else -> if (isGuest) {
                                    GuestStatsContent(isDark = isDark)
                                } else {
                                    StatsTabContent(db = db, login = login, isDark = isDark)
                                }
                            }
                        }
                    }

                    if (isGuest) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(cardBg)
                                .border(
                                    width = 1.dp,
                                    color = AccentPink.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onLogout() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Выйти из гостевого режима",
                                color = AccentPink,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotebookTabContent(
    db: LocalDatabase,
    userLogin: String,
    isDark: Boolean
) {
    var query by remember { mutableStateOf("") }
    var snippets by remember { mutableStateOf(db.searchSnippets(userLogin, "")) }

    var editorOpen by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<CodeSnippet?>(null) }
    var confirmDelete by remember { mutableStateOf<CodeSnippet?>(null) }

    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.06f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)

    fun refresh() {
        snippets = db.searchSnippets(userLogin, query)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Блокнот кода",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                    .clickable {
                        editing = null
                        editorOpen = true
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = "+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                refresh()
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Поиск по сниппетам", color = textColor.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(Icons.Filled.EditNote, contentDescription = null, tint = textColor.copy(alpha = 0.7f)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GradientStart,
                unfocusedBorderColor = borderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = GradientStart
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (snippets.isEmpty()) {
            Text(
                text = "Сниппетов пока нет. Нажмите + чтобы добавить первый.",
                color = textColor.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(snippets) { s ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable {
                                editing = s
                                editorOpen = true
                            }
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(text = s.title, color = textColor, fontWeight = FontWeight.SemiBold)
                            if (s.tags.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = s.tags, color = textColor.copy(alpha = 0.6f), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = s.code.take(140) + if (s.code.length > 140) "…" else "",
                                color = textColor.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${s.language} · ${s.updatedAt.take(16).replace('T', ' ')}".trim(),
                                    color = textColor.copy(alpha = 0.55f),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Удалить",
                                    color = AccentPink,
                                    fontSize = 12.sp,
                                    modifier = Modifier.clickable { confirmDelete = s }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (editorOpen) {
        SnippetEditorDialog(
            isDark = isDark,
            snippet = editing,
            onDismiss = {
                editorOpen = false
                editing = null
            },
            onSave = { title, tags, language, code ->
                if (editing == null) {
                    db.addSnippet(userLogin = userLogin, title = title, code = code, language = language, tags = tags)
                } else {
                    db.updateSnippet(id = editing!!.id, title = title, code = code, language = language, tags = tags)
                }
                editorOpen = false
                editing = null
                refresh()
            }
        )
    }

    confirmDelete?.let { s ->
        AlertDialog(
            onDismissRequest = { confirmDelete = null },
            title = { Text(text = "Удалить сниппет?", color = textColor) },
            text = { Text(text = s.title, color = textColor.copy(alpha = 0.8f)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        db.deleteSnippet(s.id)
                        confirmDelete = null
                        refresh()
                    }
                ) { Text("Удалить", color = AccentPink) }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = null }) { Text("Отмена", color = textColor.copy(alpha = 0.7f)) }
            }
        )
    }
}

@Composable
private fun SnippetEditorDialog(
    isDark: Boolean,
    snippet: CodeSnippet?,
    onDismiss: () -> Unit,
    onSave: (title: String, tags: String, language: String, code: String) -> Unit
) {
    var title by remember { mutableStateOf(snippet?.title ?: "") }
    var tags by remember { mutableStateOf(snippet?.tags ?: "") }
    var language by remember { mutableStateOf(snippet?.language ?: "python") }
    var code by remember { mutableStateOf(snippet?.code ?: "") }
    val textColor = if (isDark) Color.White else Color.Black

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (snippet == null) "Новый сниппет" else "Редактирование",
                color = textColor
            )
        },
        text = {
            Column {
                ModernTextField(value = title, onValueChange = { title = it }, label = "Заголовок", icon = "", isDark = isDark)
                Spacer(modifier = Modifier.height(10.dp))
                ModernTextField(value = tags, onValueChange = { tags = it }, label = "Теги (через запятую)", icon = "", isDark = isDark)
                Spacer(modifier = Modifier.height(10.dp))
                ModernTextField(value = language, onValueChange = { language = it }, label = "Язык", icon = "", isDark = isDark)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    label = { Text("Код", color = textColor.copy(alpha = 0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        cursorColor = GradientStart,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && code.isNotBlank()) {
                        onSave(title.trim(), tags.trim(), language.trim().ifBlank { "python" }, code)
                    }
                }
            ) {
                Text(text = "Сохранить", color = SecondaryBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Отмена", color = textColor.copy(alpha = 0.7f))
            }
        }
    )
}

@Composable
private fun MainMenuTabs(selectedTab: Int, onTabSelected: (Int) -> Unit, isDark: Boolean) {
    val tabs = listOf(
        Pair("", "Курсы"),
        Pair("", "Профиль"),
        Pair("", "Статистика")
    )
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEachIndexed { index, (icon, title) ->
            val isSelected = selectedTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) Brush.linearGradient(listOf(GradientStart, GradientEnd))
                        else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = icon, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else textColor.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun MainMenuTabsWithFavorites(selectedTab: Int, onTabSelected: (Int) -> Unit, isDark: Boolean, isGuest: Boolean) {
    val tabs = if (isGuest) {
        listOf(
            Pair("", "Курсы"),
            Pair("", "Профиль"),
            Pair("", "Статистика")
        )
    } else {
        listOf(
            Pair("", "Курсы"),
            Pair("", "Избранное"),
            Pair("", "Профиль"),
            Pair("", "Статистика")
        )
    }
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        tabs.forEachIndexed { index, (icon, title) ->
            val isSelected = selectedTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) Brush.linearGradient(listOf(GradientStart, GradientEnd))
                        else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = icon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else textColor.copy(alpha = 0.5f),
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun CoursesTabContentWithFavorites(
    db: LocalDatabase,
    courses: List<Course>,
    login: String?,
    isDark: Boolean,
    onCourseClick: (Course) -> Unit,
    onFavoriteChanged: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var levelFilter by remember { mutableStateOf("Все") }
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.06f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Поиск курсов", color = textColor.copy(alpha = 0.6f)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GradientStart,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = GradientStart
        ),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Фильтры
    val levels = listOf("Все", "Начальный", "Средний", "Продвинутый")
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (levelFilter == "Все") Brush.linearGradient(listOf(GradientStart, GradientEnd)) else Brush.linearGradient(listOf(bgColor, bgColor)))
                .clickable { levelFilter = "Все" }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Все уровни",
                color = if (levelFilter == "Все") Color.White else textColor.copy(alpha = 0.7f),
                fontWeight = if (levelFilter == "Все") FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            levels.drop(1).forEach { level ->
                val isSelected = levelFilter == level
                val levelColor = when (level) {
                    "Начальный" -> SuccessGreen
                    "Средний" -> AccentOrange
                    else -> AccentPink
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) levelColor.copy(alpha = 0.3f) else bgColor)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) levelColor else borderColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { levelFilter = level }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level,
                        color = if (isSelected) levelColor else textColor.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    val filteredCourses = courses.filter { course ->
        (levelFilter == "Все" || course.level == levelFilter) &&
        (query.isBlank() || course.title.contains(query, ignoreCase = true))
    }

    if (filteredCourses.isEmpty()) {
        Text(
            text = "Курсы не найдены",
            color = textColor.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    } else {
        filteredCourses.forEach { course ->
            CourseCardWithFavorite(
                db = db,
                course = course,
                login = login,
                isDark = isDark,
                onClick = { onCourseClick(course) },
                onFavoriteChanged = onFavoriteChanged
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun CourseCardWithFavorite(
    db: LocalDatabase,
    course: Course,
    login: String?,
    isDark: Boolean,
    onClick: () -> Unit,
    onFavoriteChanged: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.04f))
    else listOf(Color.Black.copy(alpha = 0.04f), Color.Black.copy(alpha = 0.02f))
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    val levelColor = when (course.level) {
        "Начальный" -> SuccessGreen
        "Средний" -> AccentOrange
        else -> AccentPink
    }

    var isFavorite by remember { mutableStateOf(login != null && db.isFavorite(login, course.id)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(colors = cardBg))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.title,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(levelColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = course.level, color = levelColor, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${course.lessonsCount} уроков",
                        color = textColor.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }
            }

            // Кнопка избранного
            if (login != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isFavorite) AccentOrange.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable {
                            if (isFavorite) {
                                db.removeFromFavorites(login, course.id)
                            } else {
                                db.addToFavorites(login, course.id)
                            }
                            isFavorite = !isFavorite
                            onFavoriteChanged()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isFavorite) "" else "",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesTabContent(
    courses: List<Course>,
    isDark: Boolean,
    onCourseClick: (Course) -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black

    Text(
        text = "Избранные курсы",
        color = textColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (courses.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Нет избранных курсов",
                color = textColor.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
            Text(
                text = "Добавьте курсы в избранное,\nнажав на звёздочку",
                color = textColor.copy(alpha = 0.4f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        courses.forEach { course ->
            CourseCardNew(course = course, isDark = isDark, onClick = { onCourseClick(course) })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun CoursesTabContent(courses: List<Triple<String, String, String>>, isDark: Boolean = true) {
    var query by remember { mutableStateOf("") }
    var levelFilter by remember { mutableStateOf("Все") }
    var sortAsc by remember { mutableStateOf(true) }
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.06f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)

    // Поиск
    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Поиск курсов", color = textColor.copy(alpha = 0.6f)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GradientStart,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = GradientStart
        ),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Фильтры по уровню
    val levels = listOf("Все", "Начальный", "Средний")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        levels.forEach { lvl ->
            val isSelected = levelFilter == lvl
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) Brush.linearGradient(listOf(GradientStart, GradientEnd))
                        else Brush.linearGradient(listOf(bgColor, bgColor))
                    )
                    .clickable { levelFilter = lvl }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = lvl,
                    color = if (isSelected) Color.White else textColor.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Сортировка
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { sortAsc = !sortAsc }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (sortAsc) "Сортировка: А-Я" else "Сортировка: Я-А",
            color = textColor.copy(alpha = 0.8f),
            fontSize = 13.sp
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    val filtered = courses
        .filter { (title, desc, level) ->
            (query.isBlank() || title.contains(query, ignoreCase = true) || desc.contains(query, ignoreCase = true)) &&
                    (levelFilter == "Все" || level.equals(levelFilter, ignoreCase = true))
        }
        .sortedBy { it.first.lowercase() }
        .let { if (sortAsc) it else it.reversed() }

    // Карточки курсов
    filtered.forEach { (title, desc, level) ->
        CourseCard(title = title, description = desc, level = level, isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun CourseCard(title: String, description: String, level: String, isDark: Boolean = true) {
    val levelColor = when (level) {
        "Начальный" -> SuccessGreen
        "Средний" -> AccentOrange
        else -> AccentPink
    }
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.04f))
                 else listOf(Color.Black.copy(alpha = 0.04f), Color.Black.copy(alpha = 0.02f))
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = cardBg))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "", fontSize = 28.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(levelColor.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = level,
                        color = levelColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                color = textColor.copy(alpha = 0.6f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Начать обучение",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileTabContent(
    db: LocalDatabase,
    login: String?,
    isDark: Boolean = true,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    if (login == null) {
        Text("Пользователь не определён", color = textColor)
        return
    }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userLogin by remember { mutableStateOf(login) }
    var password by remember { mutableStateOf("") }
    var avatarPath by remember { mutableStateOf<String?>(null) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }

    // Launcher для выбора изображения из галереи
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                
                // Сохраняем изображение в локальное хранилище
                val fileName = "avatar_${login}_${System.currentTimeMillis()}.jpg"
                val file = java.io.File(context.filesDir, fileName)
                val outputStream = java.io.FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.close()
                
                // Обновляем путь в БД
                db.updateUserAvatar(login, file.absolutePath)
                avatarPath = file.absolutePath
                avatarBitmap = bitmap
            } catch (e: Exception) {
                Log.e("Profile", "Error loading avatar: ${e.message}")
            }
        }
    }

    if (!loaded) {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT first_name, last_name, email, password, avatar FROM users WHERE login = ?",
            arrayOf(login)
        )
        cursor.use {
            if (it.moveToFirst()) {
                firstName = it.getString(0) ?: ""
                lastName = it.getString(1) ?: ""
                email = it.getString(2) ?: ""
                password = it.getString(3) ?: ""
                avatarPath = it.getString(4)
            }
        }
        // Загружаем bitmap если есть путь к файлу
        avatarPath?.let { path ->
            if (path.startsWith("/")) {
                try {
                    val file = java.io.File(path)
                    if (file.exists()) {
                        avatarBitmap = BitmapFactory.decodeFile(path)
                    }
                } catch (e: Exception) {
                    Log.e("Profile", "Error loading avatar bitmap: ${e.message}")
                }
            }
        }
        loaded = true
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Аватар с возможностью загрузки из галереи
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(GradientStart, GradientEnd))),
            contentAlignment = Alignment.Center
        ) {
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap!!.asImageBitmap(),
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                // Показываем первую букву имени если нет аватарки
                Text(
                    text = firstName.firstOrNull()?.uppercase() ?: login.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 40.sp,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Иконка камеры поверх
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SecondaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Нажмите для загрузки фото",
            color = textColor.copy(alpha = 0.4f),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "@$login",
            color = textColor.copy(alpha = 0.5f),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        ModernTextField(value = firstName, onValueChange = { firstName = it }, label = "Имя", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(10.dp))

        ModernTextField(value = lastName, onValueChange = { lastName = it }, label = "Фамилия", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(10.dp))

        ModernTextField(value = email, onValueChange = { email = it }, label = "Email", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(10.dp))

        ModernTextField(value = userLogin, onValueChange = { userLogin = it }, label = "Логин", icon = "", isDark = isDark)
        Spacer(modifier = Modifier.height(10.dp))

        ModernTextField(value = password, onValueChange = { password = it }, label = "Пароль", icon = "", isPassword = true, isDark = isDark)
        Spacer(modifier = Modifier.height(16.dp))

        GradientButton(
            text = "Сохранить изменения",
            onClick = {
                // Обновляем данные пользователя включая логин
                db.writableDatabase.execSQL(
                    "UPDATE users SET first_name = ?, last_name = ?, email = ?, login = ?, password = ? WHERE login = ?",
                    arrayOf(firstName, lastName, email, userLogin, password, login)
                )
                // Обновляем связанные таблицы если логин изменился
                if (userLogin != login) {
                    db.writableDatabase.execSQL(
                        "UPDATE user_stats SET user_login = ? WHERE user_login = ?",
                        arrayOf(userLogin, login)
                    )
                    db.writableDatabase.execSQL(
                        "UPDATE favorites SET user_login = ? WHERE user_login = ?",
                        arrayOf(userLogin, login)
                    )
                }
                Log.d("Profile", "Profile updated for $login -> $userLogin")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопка выхода
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(cardBg)
                .border(width = 1.dp, color = AccentOrange.copy(alpha = 0.5f), shape = RoundedCornerShape(14.dp))
                .clickable { onLogout() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Выйти из аккаунта",
                color = AccentOrange,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопка удаления аккаунта
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(AccentPink.copy(alpha = 0.1f))
                .border(width = 1.dp, color = AccentPink.copy(alpha = 0.5f), shape = RoundedCornerShape(14.dp))
                .clickable { showDeleteConfirmation = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Удалить аккаунт",
                color = AccentPink,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

    }

    // Всплывающий диалог подтверждения удаления
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    text = "Удаление аккаунта",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(text = "Вы точно хотите удалить свой аккаунт?")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Это действие нельзя отменить",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        db.writableDatabase.execSQL("DELETE FROM favorites WHERE user_login = ?", arrayOf(login))
                        db.writableDatabase.execSQL("DELETE FROM user_stats WHERE user_login = ?", arrayOf(login))
                        db.writableDatabase.execSQL("DELETE FROM users WHERE login = ?", arrayOf(login))
                        showDeleteConfirmation = false
                        onLogout()
                    }
                ) {
                    Text(text = "Да", color = AccentPink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(text = "Нет")
                }
            }
        )
    }
}

@Composable
private fun GuestProfileContent(isDark: Boolean) {
    val textColor = if (isDark) Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Гостевой режим",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Зарегистрируйтесь, чтобы \nсохранять прогресс обучения",
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GuestCourseContent(isDark: Boolean, onBack: () -> Unit) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Кнопка назад
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = "← Назад", color = textColor, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Гостевой режим",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Зарегистрируйтесь, чтобы\nпройти данный курс",
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

