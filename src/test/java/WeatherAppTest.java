import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeatherAppTest {

    @Test
    void parsesValuesForTheCurrentHour() throws Exception {
        JSONObject weather = WeatherApp.parseWeatherData(fixture("forecast.json"), "2026-01-12T10:00");

        assertThat(weather)
                .containsEntry("temperature", 0.5)
                .containsEntry("weather_condition", "Slight Snow Fall")
                .containsEntry("humidity", 88L)
                .containsEntry("windspeed", 13.0);
    }

    @Test
    void acceptsWholeNumbersForDecimalFields() throws Exception {
        // json-simple parses "2" as Long, which used to break the (double) casts
        JSONObject weather = WeatherApp.parseWeatherData(fixture("forecast.json"), "2026-01-12T11:00");

        assertThat(weather)
                .containsEntry("temperature", 2.0)
                .containsEntry("windspeed", 9.0)
                .containsEntry("weather_condition", "Clear");
    }

    @Test
    void fallsBackToFirstHourWhenCurrentHourIsMissing() throws Exception {
        JSONObject weather = WeatherApp.parseWeatherData(fixture("forecast.json"), "1999-01-01T00:00");

        assertThat(weather).containsEntry("temperature", -1.2);
    }

    @Test
    void rejectsMalformedJson() {
        assertThatThrownBy(() -> WeatherApp.parseWeatherData("{not json", "2026-01-12T10:00"))
                .isInstanceOf(ParseException.class);
    }

    @Test
    void parsesGeocodingResults() throws Exception {
        JSONArray locations = WeatherApp.parseLocationData(fixture("geocoding.json"));

        assertThat(locations).hasSize(2);
        JSONObject first = (JSONObject) locations.get(0);
        assertThat(first).containsEntry("name", "Berlin").containsEntry("country", "Germany");
    }

    @Test
    void unknownLocationYieldsEmptyResults() throws Exception {
        assertThat(WeatherApp.parseLocationData("{\"generationtime_ms\":0.2}")).isEmpty();
    }

    @Test
    void findsIndexOfMatchingHour() {
        JSONArray times = new JSONArray();
        times.addAll(List.of("2026-01-12T09:00", "2026-01-12T10:00"));

        assertThat(WeatherApp.findIndexOfTime(times, "2026-01-12T10:00")).isEqualTo(1);
    }

    @Test
    void currentTimeMatchesApiHourFormat() {
        assertThat(WeatherApp.getCurrentTime()).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:00");
    }

    @ParameterizedTest
    @CsvSource({
            "0, Clear",
            "2, Partly Cloudy",
            "45, Fog",
            "55, Dense Drizzle",
            "65, Heavy Rain",
            "77, Snow Grains",
            "82, Violent Rain Showers",
            "95, Thunderstorm",
            "99, Thunderstorm With Heavy Hail",
            "42, Error"
    })
    void convertsWmoWeatherCodes(long code, String expected) {
        assertThat(WeatherApp.convertWeatherCode(code)).isEqualTo(expected);
    }

    private static String fixture(String name) throws IOException {
        try (var in = Objects.requireNonNull(WeatherAppTest.class.getResourceAsStream("/" + name))) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
