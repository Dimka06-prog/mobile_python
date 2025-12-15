package com.example.myapplication12345678.ui.auth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication12345678.data.Course
import com.example.myapplication12345678.data.ExcelExporter
import com.example.myapplication12345678.data.Lesson
import com.example.myapplication12345678.data.LocalDatabase
import com.example.myapplication12345678.data.UserStats
import com.example.myapplication12345678.ui.theme.AccentOrange
import com.example.myapplication12345678.ui.theme.AccentPink
import com.example.myapplication12345678.ui.theme.GradientEnd
import com.example.myapplication12345678.ui.theme.GradientStart
import com.example.myapplication12345678.ui.theme.SecondaryBlue
import com.example.myapplication12345678.ui.theme.SuccessGreen

@Composable
fun AdminScreen(
    isDark: Boolean,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { LocalDatabase(context) }
    var currentView by remember { mutableStateOf("menu") }
    var courses by remember { mutableStateOf(db.getAllCourses()) }
    var editingCourse by remember { mutableStateOf<Course?>(null) }

    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (currentView) {
            "menu" -> {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(AccentPink, AccentOrange))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "A", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Панель администратора",
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "@admin",
                    color = textColor.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(cardBg)
                        .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        AdminMenuItem(
                            icon = "🗂️",
                            title = "Курсы",
                            subtitle = "Добавление, редактирование, удаление",
                            isDark = isDark,
                            onClick = { currentView = "courses" }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AdminMenuItem(
                            icon = "🧑‍💻",
                            title = "Пользователи",
                            subtitle = "Просмотр зарегистрированных",
                            isDark = isDark,
                            onClick = { currentView = "users" }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AdminMenuItem(
                            icon = "📊",
                            title = "Статистика",
                            subtitle = "Сводка по пользователям и экспорт",
                            isDark = isDark,
                            onClick = { currentView = "stats" }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .border(width = 1.dp, color = AccentPink.copy(alpha = 0.5f), shape = RoundedCornerShape(14.dp))
                        .clickable { onLogout() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Выйти из аккаунта", color = AccentPink, fontWeight = FontWeight.Medium)
                }
            }

            "courses" -> {
                AdminCoursesScreen(
                    db = db,
                    courses = courses,
                    isDark = isDark,
                    onBack = { currentView = "menu" },
                    onRefresh = { courses = db.getAllCourses() },
                    onEdit = { course ->
                        editingCourse = course
                        currentView = "editCourse"
                    }
                )
            }

            "editCourse" -> {
                AdminEditCourseScreen(
                    db = db,
                    course = editingCourse,
                    isDark = isDark,
                    onBack = {
                        editingCourse = null
                        courses = db.getAllCourses()
                        currentView = "courses"
                    }
                )
            }

            "users" -> {
                AdminUsersScreen(
                    db = db,
                    isDark = isDark,
                    onBack = { currentView = "menu" }
                )
            }

            "stats" -> {
                AdminStatsScreen(
                    db = db,
                    isDark = isDark,
                    onBack = { currentView = "menu" }
                )
            }

        }
    }
}

@Composable
private fun AdminMenuItem(
    icon: String,
    title: String,
    subtitle: String,
    isDark: Boolean = true,
    onClick: () -> Unit = {}
) {
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.03f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                color = textColor.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun AdminCoursesScreen(
    db: LocalDatabase,
    courses: List<Course>,
    isDark: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onEdit: (Course) -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = "← Назад", color = textColor, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "📚 Управление курсами",
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Всего курсов: ${courses.size}",
            color = textColor.copy(alpha = 0.5f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        GradientButton(
            text = "+ Добавить курс",
            onClick = { onEdit(Course(0, "", "", "Начальный", "", 0)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        courses.forEach { course ->
            AdminCourseItem(
                course = course,
                isDark = isDark,
                onEdit = { onEdit(course) },
                onDelete = {
                    db.deleteCourse(course.id)
                    onRefresh()
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun AdminCourseItem(
    course: Course,
    isDark: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    val levelColor = when (course.level) {
        "Начальный" -> SuccessGreen
        "Средний" -> AccentOrange
        else -> AccentPink
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = course.icon, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = course.title,
                            color = textColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${course.level} • ${course.lessonsCount} уроков",
                            color = levelColor,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SecondaryBlue.copy(alpha = 0.2f))
                        .clickable { onEdit() }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✏️ Редактировать", color = SecondaryBlue, fontSize = 13.sp)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AccentPink.copy(alpha = 0.2f))
                        .clickable { onDelete() }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🗑️ Удалить", color = AccentPink, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun AdminEditCourseScreen(
    db: LocalDatabase,
    course: Course?,
    isDark: Boolean,
    onBack: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    var title by remember { mutableStateOf(course?.title ?: "") }
    var description by remember { mutableStateOf(course?.description ?: "") }
    var level by remember { mutableStateOf(course?.level ?: "Начальный") }
    val isNew = course?.id == 0

    var showLessons by remember { mutableStateOf(false) }
    var lessons by remember { mutableStateOf(if (course != null && course.id != 0) db.getLessonsForCourse(course.id) else emptyList()) }
    var editingLesson by remember { mutableStateOf<Lesson?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = "← Отмена", color = textColor, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isNew) "➕ Новый курс" else "✏️ Редактирование",
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        ModernTextField(
            value = title,
            onValueChange = { title = it },
            label = "Название курса",
            icon = "📚",
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(
            value = description,
            onValueChange = { description = it },
            label = "Описание",
            icon = "📝",
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Уровень сложности",
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val levels = listOf("Начальный", "Средний", "Продвинутый")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            levels.forEach { lvl ->
                val isSelected = level == lvl
                val lvlColor = when (lvl) {
                    "Начальный" -> SuccessGreen
                    "Средний" -> AccentOrange
                    else -> AccentPink
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) lvlColor.copy(alpha = 0.3f) else cardBg)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) lvlColor else textColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { level = lvl }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lvl,
                        color = if (isSelected) lvlColor else textColor.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = if (isNew) "✓ Создать курс" else "✓ Сохранить",
            onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    if (isNew) {
                        db.addCourse(title, description, level)
                    } else {
                        db.updateCourse(course!!.id, title, description, level)
                    }
                    onBack()
                }
            }
        )

        if (!isNew && course != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(borderColor)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (editingLesson != null) {
                AdminEditLessonScreen(
                    db = db,
                    courseId = course.id,
                    lesson = editingLesson,
                    isDark = isDark,
                    onBack = {
                        editingLesson = null
                        lessons = db.getLessonsForCourse(course.id)
                    }
                )
            } else if (showLessons) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📖 Уроки курса (${lessons.size})",
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(cardBg)
                            .clickable { showLessons = false }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Свернуть", color = textColor.copy(alpha = 0.6f), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SuccessGreen.copy(alpha = 0.2f))
                        .border(width = 1.dp, color = SuccessGreen.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                        .clickable { editingLesson = Lesson(0, course.id, "", "", lessons.size + 1, 10) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "+ Добавить урок", color = SuccessGreen, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(12.dp))

                lessons.forEach { lesson ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(cardBg)
                            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(GradientStart, GradientEnd))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${lesson.orderNum}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = lesson.title,
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "⏱ ${lesson.durationMinutes} мин",
                                        color = textColor.copy(alpha = 0.5f),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SecondaryBlue.copy(alpha = 0.2f))
                                        .clickable { editingLesson = lesson }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "✏️ Изменить", color = SecondaryBlue, fontSize = 11.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(AccentPink.copy(alpha = 0.2f))
                                        .clickable {
                                            db.deleteLesson(lesson.id)
                                            lessons = db.getLessonsForCourse(course.id)
                                        }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🗑️ Удалить", color = AccentPink, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (lessons.isEmpty()) {
                    Text(
                        text = "Уроков пока нет",
                        color = textColor.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SecondaryBlue.copy(alpha = 0.2f))
                        .border(width = 1.dp, color = SecondaryBlue.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                        .clickable { showLessons = true }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📖 Управление уроками (${lessons.size})",
                        color = SecondaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminEditLessonScreen(
    db: LocalDatabase,
    courseId: Int,
    lesson: Lesson?,
    isDark: Boolean,
    onBack: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    var lessonTitle by remember { mutableStateOf(lesson?.title ?: "") }
    var lessonContent by remember { mutableStateOf(lesson?.content ?: "") }
    var duration by remember { mutableStateOf((lesson?.durationMinutes ?: 10).toString()) }
    val isNewLesson = lesson?.id == 0

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = "← Назад к урокам", color = textColor, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isNewLesson) "➕ Новый урок" else "✏️ Редактирование урока",
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        ModernTextField(
            value = lessonTitle,
            onValueChange = { lessonTitle = it },
            label = "Название урока",
            icon = "📖",
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(
            value = duration,
            onValueChange = { duration = it.filter { c -> c.isDigit() } },
            label = "Длительность (минут)",
            icon = "⏱",
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "📝 Содержание урока",
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lessonContent,
            onValueChange = { lessonContent = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GradientStart,
                unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f),
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = GradientStart
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        GradientButton(
            text = if (isNewLesson) "✓ Создать урок" else "✓ Сохранить",
            onClick = {
                if (lessonTitle.isNotBlank() && lessonContent.isNotBlank()) {
                    val durationInt = duration.toIntOrNull() ?: 10
                    if (isNewLesson) {
                        db.addLesson(courseId, lessonTitle, lessonContent, lesson?.orderNum ?: 1, durationInt)
                    } else {
                        db.updateLesson(lesson!!.id, lessonTitle, lessonContent, durationInt)
                    }
                    onBack()
                }
            }
        )
    }
}

@Composable
private fun AdminUsersScreen(
    db: LocalDatabase,
    isDark: Boolean,
    onBack: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    var users by remember { mutableStateOf(db.getAllUsersStats()) }
    var editingUser by remember { mutableStateOf<UserStats?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (editingUser != null) {
            AdminEditUserScreen(
                db = db,
                user = editingUser!!,
                isDark = isDark,
                onBack = {
                    editingUser = null
                    users = db.getAllUsersStats()
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardBg)
                    .clickable { onBack() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(text = "← Назад", color = textColor, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "👥 Пользователи",
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Зарегистрировано: ${users.size}",
                color = textColor.copy(alpha = 0.5f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (users.isEmpty()) {
                Text(
                    text = "Пользователей пока нет",
                    color = textColor.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                users.forEach { user ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBg)
                            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(GradientStart, GradientEnd))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.firstName.firstOrNull()?.uppercase() ?: "?",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${user.firstName} ${user.lastName}",
                                        color = textColor,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "@${user.login}",
                                        color = textColor.copy(alpha = 0.5f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = user.email,
                                        color = textColor.copy(alpha = 0.4f),
                                        fontSize = 11.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "✅ ${user.completedCourses}",
                                        color = SuccessGreen,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "⏱ ${user.totalTimeMinutes} мин",
                                        color = SecondaryBlue,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SecondaryBlue.copy(alpha = 0.2f))
                                        .clickable { editingUser = user }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "✏️ Редактировать", color = SecondaryBlue, fontSize = 12.sp)
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentPink.copy(alpha = 0.2f))
                                        .clickable {
                                            db.writableDatabase.execSQL("DELETE FROM user_stats WHERE user_login = ?", arrayOf(user.login))
                                            db.writableDatabase.execSQL("DELETE FROM users WHERE login = ?", arrayOf(user.login))
                                            users = db.getAllUsersStats()
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🗑️ Удалить", color = AccentPink, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun AdminStatsScreen(
    db: LocalDatabase,
    isDark: Boolean,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    var users by remember { mutableStateOf(db.getAllUsersStats()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = "← Назад", color = textColor, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "📊 Статистика пользователей",
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Пользователей: ${users.size}",
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SecondaryBlue.copy(alpha = if (isDark) 0.22f else 0.18f))
                .border(width = 1.dp, color = SecondaryBlue.copy(alpha = 0.5f), shape = RoundedCornerShape(14.dp))
                .clickable {
                    val file = ExcelExporter.exportStatsToCSV(context, users)
                    file?.let { ExcelExporter.shareCSVFile(context, it) }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Экспорт CSV",
                color = if (isDark) Color.White else SecondaryBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        users.forEach { u ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "@${u.login}", color = textColor, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "${u.firstName} ${u.lastName} · ${u.email}",
                        color = textColor.copy(alpha = 0.65f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Курсы: ${u.completedCourses} · Время: ${u.totalTimeMinutes} мин",
                        color = textColor.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun AdminEditUserScreen(
    db: LocalDatabase,
    user: UserStats,
    isDark: Boolean,
    onBack: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var email by remember { mutableStateOf(user.email) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(cardBg)
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = "← Отмена", color = textColor, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "✏️ Редактирование пользователя",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "@${user.login}",
            color = textColor.copy(alpha = 0.5f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        ModernTextField(value = firstName, onValueChange = { firstName = it }, label = "Имя", icon = "👤", isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = lastName, onValueChange = { lastName = it }, label = "Фамилия", icon = "👥", isDark = isDark)
        Spacer(modifier = Modifier.height(12.dp))

        ModernTextField(value = email, onValueChange = { email = it }, label = "Email", icon = "📧", isDark = isDark)
        Spacer(modifier = Modifier.height(20.dp))

        GradientButton(
            text = "✓ Сохранить изменения",
            onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()) {
                    db.writableDatabase.execSQL(
                        "UPDATE users SET first_name = ?, last_name = ?, email = ? WHERE login = ?",
                        arrayOf(firstName, lastName, email, user.login)
                    )
                    onBack()
                }
            }
        )
    }
}
