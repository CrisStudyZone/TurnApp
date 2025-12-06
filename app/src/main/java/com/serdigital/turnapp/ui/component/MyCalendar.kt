package com.serdigital.turnapp.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.core.DayPosition
import com.serdigital.turnapp.data.di.DayOfWeek
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCalendar() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = currentMonth.minusMonths(1)
    val endMonth = currentMonth.plusMonths(1)
    val firstDayOfWeek = remember { DayOfWeek.MONDAY }

    VerticalCalendar(
        modifier = Modifier,
        dayContent = { day ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (
                            day.position == DayPosition.MonthDate
                            ) MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.1f
                        )
                        else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AppText(day.date.dayOfMonth.toString())
            }
        }
    )
}
