package chronos.engine.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.dataconversion.DataConversion
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.util.Locale

fun Application.installDataConversion() {
  install(DataConversion) {
    convert<LocalDate> { // this: DelegatingConversionService
      val formatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4, 4, SignStyle.NEVER)
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .toFormatter(Locale.ROOT)

      decode { values -> // converter: (values: List<String>) -> Any?
        LocalDate.from(formatter.parse(values.single()))
      }

      encode { value -> // converter: (value: Any?) -> List<String>
        listOf(SimpleDateFormat.getInstance().format(value))
      }
    }
  }
}
