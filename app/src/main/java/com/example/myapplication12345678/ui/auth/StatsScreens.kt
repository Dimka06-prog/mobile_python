package com.example.myapplication12345678.ui.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication12345678.data.ExcelExporter
import com.example.myapplication12345678.data.LocalDatabase
import com.example.myapplication12345678.ui.theme.SecondaryBlue
import com.example.myapplication12345678.ui.theme.SuccessGreen

@Composable
private fun DonutStatCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    ratio: Float,
    color: Color,
    isDark: Boolean
) {
    val textColor = if (isDark) Color.White else Color.Black
    val secondary = textColor.copy(alpha = 0.65f)
    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.08f)
    val trackColor = if (isDark) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.08f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = subtitle, color = secondary, fontSize = 12.sp)
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val sweep = 360f * ratio.coerceIn(0f, 1f)
                    Canvas(modifier = Modifier.fillMaxWidth()) {
                        val strokeWidth = size.minDimension * 0.16f
                        drawArc(
                            color = trackColor,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        if (sweep > 0.5f) {
                            drawArc(
                                color = color,
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                    }

                    Text(
                        text = "${(ratio.coerceIn(0f, 1f) * 100).toInt()}%",
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StatsTabContent(db: LocalDatabase, login: String?, isDark: Boolean = true) {
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = textColor.copy(alpha = 0.65f)

    if (login == null) {
        Text("Пользователь не определён", color = textColor)
        return
    }

    var completed by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var totalCourses by remember { mutableStateOf(0) }
    var loaded by remember { mutableStateOf(false) }

    if (!loaded) {
        val statsCursor = db.readableDatabase.rawQuery(
            "SELECT completed_courses, total_time_minutes FROM user_stats WHERE user_login = ?",
            arrayOf(login)
        )
        statsCursor.use {
            if (it.moveToFirst()) {
                completed = it.getInt(0)
                minutes = it.getInt(1)
            } else {
                db.writableDatabase.execSQL(
                    "INSERT OR IGNORE INTO user_stats (user_login, completed_courses, total_time_minutes) VALUES (?, 0, 0)",
                    arrayOf(login)
                )
            }
        }

        val courseCursor = db.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM courses",
            null
        )
        courseCursor.use {
            if (it.moveToFirst()) {
                totalCourses = it.getInt(0)
            }
        }

        loaded = true
    }

    val completionRatio = if (totalCourses <= 0) 0f else (completed.toFloat() / totalCourses.toFloat()).coerceIn(0f, 1f)
    val minutesRatio = (minutes.toFloat() / 600f).coerceIn(0f, 1f)

    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.04f)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.08f)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Статистика",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "@${login}",
            color = secondaryTextColor,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DonutStatCard(
                modifier = Modifier.weight(1f),
                title = "Курсы",
                subtitle = "$completed из $totalCourses",
                ratio = completionRatio,
                color = SuccessGreen,
                isDark = isDark
            )
            DonutStatCard(
                modifier = Modifier.weight(1f),
                title = "Время",
                subtitle = "$minutes мин",
                ratio = minutesRatio,
                color = SecondaryBlue,
                isDark = isDark
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(cardBg)
                .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Сводка",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Завершено курсов: $completed",
                    color = secondaryTextColor,
                    fontSize = 13.sp
                )
                Text(
                    text = "Время в приложении: $minutes минут",
                    color = secondaryTextColor,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SecondaryBlue.copy(alpha = if (isDark) 0.22f else 0.18f))
                .border(width = 1.dp, color = SecondaryBlue.copy(alpha = 0.5f), shape = RoundedCornerShape(14.dp))
                .clickable {
                    val stats = db.getAllUsersStats().filter { it.login == login }
                    val file = ExcelExporter.exportStatsToCSV(context, stats)
                    file?.let { ExcelExporter.shareCSVFile(context, it) }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Экспорт статистики",
                color = if (isDark) Color.White else SecondaryBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun GuestStatsContent(isDark: Boolean) {
    val textColor = if (isDark) Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Статистика недоступна",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Зарегистрируйтесь, чтобы \nотслеживать свой прогресс",
            color = textColor.copy(alpha = 0.6f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String,
    color: Color,
    isDark: Boolean = true
) {
    val labelColor = (if (isDark) Color.White else Color.Black).copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.15f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = color,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = labelColor,
                fontSize = 12.sp
            )
        }
    }
}
