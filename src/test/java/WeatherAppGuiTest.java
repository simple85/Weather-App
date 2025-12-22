import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherAppGuiTest {

    @ParameterizedTest
    @CsvSource({
            "Clear, sun.png",
            "Mainly Clear, sun.png",
            "Overcast, clouds-3.png",
            "Fog, clouds-3.png",
            "Moderate Rain, rain.png",
            "Violent Rain Showers, rain.png",
            "Heavy Snow Fall, snowy.png",
            "Snow Grains, snowy.png",
            "Thunderstorm, thunder.png",
            "Thunderstorm With Slight Hail, thunderstorm.png",
            "Error, clouds-3.png"
    })
    void mapsConditionsToIcons(String condition, String icon) {
        assertThat(WeatherAppGui.iconFor(condition)).isEqualTo(icon);
    }

    @ParameterizedTest
    @CsvSource({"sun.png", "rain.png", "snowy.png", "thunder.png", "thunderstorm.png", "clouds-3.png",
            "humidity.png", "wind-2.png", "search.png"})
    void everyIconIsBundledOnTheClasspath(String icon) {
        assertThat(WeatherAppGui.class.getResource("/" + icon)).isNotNull();
    }
}
