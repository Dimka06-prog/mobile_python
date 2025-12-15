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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication12345678.data.Course
import com.example.myapplication12345678.data.Lesson
import com.example.myapplication12345678.data.LocalDatabase
import com.example.myapplication12345678.ui.theme.AccentOrange
import com.example.myapplication12345678.ui.theme.AccentPink
import com.example.myapplication12345678.ui.theme.GradientEnd
import com.example.myapplication12345678.ui.theme.GradientStart
import com.example.myapplication12345678.ui.theme.SecondaryBlue
import com.example.myapplication12345678.ui.theme.SuccessGreen

@Composable
fun CoursesTabContentNew(
    courses: List<Course>,
    isDark: Boolean,
    onCourseClick: (Course) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var levelFilter by remember { mutableStateOf("Все") }
    var sortAZ by remember { mutableStateOf(false) }
    val textColor = if (isDark) Color.White else Color.Black
    val bgColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.06f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f)

    Text(
        text = "ProPython",
        color = textColor,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

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

    Column(modifier = Modifier.fillMaxWidth()) {
        val isAllSelected = levelFilter == "Все"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isAllSelected) Brush.linearGradient(listOf(GradientStart, GradientEnd))
                    else Brush.linearGradient(listOf(bgColor, bgColor))
                )
                .clickable { levelFilter = "Все" }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Все курсы",
                color = if (isAllSelected) Color.White else textColor.copy(alpha = 0.6f),
                fontSize = 13.sp,
                fontWeight = if (isAllSelected) FontWeight.Medium else FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val otherLevels = listOf("Начальный", "Средний", "Продвинутый")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            otherLevels.forEach { lvl ->
                val isSelected = levelFilter == lvl
                val lvlColor = when (lvl) {
                    "Начальный" -> SuccessGreen
                    "Средний" -> AccentOrange
                    else -> AccentPink
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) lvlColor.copy(alpha = 0.3f) else bgColor)
                        .border(
                            width = if (isSelected) 1.dp else 0.dp,
                            color = if (isSelected) lvlColor else Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { levelFilter = lvl }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lvl,
                        color = if (isSelected) lvlColor else textColor.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (sortAZ) SecondaryBlue.copy(alpha = 0.3f) else bgColor)
                .border(
                    width = if (sortAZ) 1.dp else 0.dp,
                    color = if (sortAZ) SecondaryBlue else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { sortAZ = !sortAZ }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (sortAZ) "Сортировка: А → Я ✓" else "Сортировка: А → Я",
                color = if (sortAZ) SecondaryBlue else textColor.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = if (sortAZ) FontWeight.Medium else FontWeight.Normal
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    val filtered = courses
        .filter { course ->
            (query.isBlank() || course.title.contains(query, ignoreCase = true) || course.description.contains(query, ignoreCase = true)) &&
                (levelFilter == "Все" || course.level.equals(levelFilter, ignoreCase = true))
        }
        .let { list -> if (sortAZ) list.sortedBy { it.title.lowercase() } else list }

    filtered.forEach { course ->
        CourseCardNew(course = course, isDark = isDark, onClick = { onCourseClick(course) })
        Spacer(modifier = Modifier.height(12.dp))
    }

    if (filtered.isEmpty()) {
        Text(
            text = "Курсы не найдены",
            color = textColor.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CourseCardNew(course: Course, isDark: Boolean, onClick: () -> Unit) {
    val levelColor = when (course.level) {
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
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(levelColor.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = course.level,
                        color = levelColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = course.title,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = course.description,
                color = textColor.copy(alpha = 0.6f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📖 ${course.lessonsCount} уроков",
                    color = textColor.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Открыть →",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CourseLessonsScreen(
    db: LocalDatabase,
    course: Course,
    login: String?,
    isDark: Boolean,
    onBack: () -> Unit,
    onLessonClick: (Lesson) -> Unit,
    onFavoriteChanged: () -> Unit = {}
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    val lessons = remember { db.getLessonsForCourse(course.id) }
    var isFavorite by remember { mutableStateOf(login != null && db.isFavorite(login, course.id)) }
    var completedCount by remember { mutableStateOf(if (login != null) db.getCompletedLessonsCount(login, course.id) else 0) }
    val totalLessons = lessons.size
    val progress = if (totalLessons > 0) completedCount.toFloat() / totalLessons else 0f
    val allCompleted = completedCount == totalLessons && totalLessons > 0
    var showTest by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardBg)
                    .clickable { onBack() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(text = "← Назад к курсам", color = textColor, fontSize = 14.sp)
            }

            if (login != null) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isFavorite) AccentOrange.copy(alpha = 0.2f) else cardBg)
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
                    Text(text = if (isFavorite) "⭐" else "☆", fontSize = 22.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = course.title,
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = course.description,
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (login != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Прогресс", color = textColor.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(text = "$completedCount / $totalLessons уроков", color = textColor.copy(alpha = 0.7f), fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(cardBg)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        if (allCompleted) SuccessGreen else GradientStart,
                                        if (allCompleted) SuccessGreen else GradientEnd
                                    )
                                )
                            )
                    )
                }
                if (allCompleted) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✅ Все уроки пройдены!",
                        color = SuccessGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (allCompleted && login != null) {
            if (showTest) {
                CourseTestScreen(
                    course = course,
                    isDark = isDark,
                    onBack = { showTest = false },
                    onTestCompleted = { showTest = false }
                )
                return
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(SuccessGreen, SuccessGreen.copy(alpha = 0.8f))))
                    .clickable { showTest = true }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📝 Пройти тест",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        lessons.forEach { lesson ->
            val isCompleted = login != null && db.isLessonCompleted(login, lesson.id)
            LessonCardWithProgress(
                lesson = lesson,
                isDark = isDark,
                isCompleted = isCompleted,
                onClick = { onLessonClick(lesson) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun LessonCardWithProgress(
    lesson: Lesson,
    isDark: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isCompleted) SuccessGreen.copy(alpha = 0.5f) else if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.1f) else cardBg)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) Brush.linearGradient(listOf(SuccessGreen, SuccessGreen))
                        else Brush.linearGradient(listOf(GradientStart, GradientEnd))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCompleted) "✓" else "${lesson.orderNum}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isCompleted) "✅ Пройден" else "⏱ ${lesson.durationMinutes} мин",
                    color = if (isCompleted) SuccessGreen else textColor.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }

            Text(text = "→", color = textColor.copy(alpha = 0.5f), fontSize = 18.sp)
        }
    }
}

@Composable
private fun CourseTestScreen(
    course: Course,
    isDark: Boolean,
    onBack: () -> Unit,
    onTestCompleted: (Boolean) -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

    val questions = listOf(
        DefaultTestQuestion("Что такое Python?", listOf("Язык программирования", "База данных", "Операционная система", "Браузер"), 0),
        DefaultTestQuestion("Какой символ используется для комментариев в Python?", listOf("//", "#", "/*", "--"), 1),
        DefaultTestQuestion("Как объявить переменную в Python?", listOf("var x = 5", "int x = 5", "x = 5", "let x = 5"), 2),
        DefaultTestQuestion("Какой тип данных для целых чисел?", listOf("float", "str", "int", "bool"), 2),
        DefaultTestQuestion("Как вывести текст в консоль?", listOf("console.log()", "print()", "echo()", "System.out.println()"), 1)
    )

    var currentQuestion by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf(-1) }
    var correctAnswers by remember { mutableStateOf(0) }
    var testFinished by remember { mutableStateOf(false) }

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

        val totalQuestions = questions.size
        val passThreshold = (totalQuestions * 0.6).toInt().coerceAtLeast(1)

        if (testFinished) {
            val passed = correctAnswers >= passThreshold
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = if (passed) "🎉" else "📚", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (passed) "Тест пройден!" else "Попробуйте ещё раз",
                    color = if (passed) SuccessGreen else AccentOrange,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Правильных ответов: $correctAnswers из $totalQuestions",
                    color = textColor.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                GradientButton(text = "Готово", onClick = { onTestCompleted(passed) })
            }
        } else {
            Text(
                text = "📝 Тест",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Вопрос ${currentQuestion + 1} из $totalQuestions",
                color = textColor.copy(alpha = 0.6f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            val question = questions[currentQuestion]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .padding(16.dp)
            ) {
                Text(
                    text = question.text,
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) SecondaryBlue.copy(alpha = 0.2f) else cardBg)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) SecondaryBlue else if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedAnswer = index }
                        .padding(16.dp)
                ) {
                    Text(text = option, color = textColor, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedAnswer >= 0) {
                GradientButton(
                    text = if (currentQuestion < totalQuestions - 1) "Следующий вопрос" else "Завершить тест",
                    onClick = {
                        if (selectedAnswer == question.correctIndex) {
                            correctAnswers++
                        }
                        if (currentQuestion < totalQuestions - 1) {
                            currentQuestion++
                            selectedAnswer = -1
                        } else {
                            testFinished = true
                        }
                    }
                )
            }
        }
    }
}

private data class DefaultTestQuestion(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

@Composable
fun LessonDetailScreen(
    lesson: Lesson,
    isDark: Boolean,
    onBack: () -> Unit,
    onComplete: () -> Unit = {}
) {
    val textColor = if (isDark) Color.White else Color.Black
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)

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

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(GradientStart, GradientEnd))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${lesson.orderNum}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Урок ${lesson.orderNum}",
                    color = textColor.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
                Text(
                    text = lesson.title,
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "⏱ ${lesson.durationMinutes} минут",
            color = SecondaryBlue,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg)
                .padding(20.dp)
        ) {
            Text(
                text = lesson.content,
                color = textColor,
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(
            text = "✓ Урок завершён",
            onClick = {
                onComplete()
                onBack()
            }
        )
    }
}
