# Weather App

[![CI](https://github.com/simple85/Weather-App/actions/workflows/ci.yml/badge.svg)](https://github.com/simple85/Weather-App/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-5382a1)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A desktop weather app built with Java Swing. Type any city name and it shows the current temperature, conditions, humidity and wind speed, with an icon that matches the weather.

Weather data comes from the free [Open-Meteo](https://open-meteo.com/) API, so no API key is required.

## Features

- Search weather by city name (worldwide, via Open-Meteo geocoding)
- Current-hour temperature, humidity and wind speed
- All [WMO weather codes](https://open-meteo.com/en/docs#weathervariables) translated to readable conditions
- Condition-specific icons: sun, clouds, rain, snow, thunderstorm
- Friendly message when a location can't be found

## Tech Stack

| Area | Tools |
|------|-------|
| Language | Java 21 |
| UI | Java Swing |
| Data | Open-Meteo Forecast + Geocoding APIs |
| JSON | json-simple |
| Build | Maven, Maven Shade (runnable fat jar) |
| Testing | JUnit 5 (parameterized tests), AssertJ, JaCoCo |
| CI | GitHub Actions (headless) |

## Getting Started

**Prerequisites:** JDK 21+ and Maven 3.9+

```bash
git clone https://github.com/simple85/Weather-App.git
cd Weather-App
mvn package
java -jar target/weather-app.jar
```

You can also run `AppLauncher` directly from your IDE.

## How It Works

```text
WeatherAppGui ──search──► WeatherApp.getWeatherData(city)
                               │
                               ├─► Geocoding API   → latitude / longitude
                               ├─► Forecast API    → hourly forecast
                               └─► parseWeatherData → values for the current hour
```

The HTTP calls are kept separate from the parsing logic. `parseWeatherData`, `parseLocationData`, `convertWeatherCode` and `iconFor` are pure functions, unit-tested against recorded API responses in `src/test/resources`.

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   ├── AppLauncher.java     # entry point
│   │   ├── WeatherApp.java      # API calls + response parsing
│   │   └── WeatherAppGui.java   # Swing UI
│   └── resources/               # weather icons
└── test/
    ├── java/                    # JUnit 5 tests
    └── resources/               # recorded API responses
```

## Running Tests

```bash
mvn verify
```

Tests run headless and never call the real API. The coverage report is written to `target/site/jacoco/index.html`.

## License

Released under the [MIT License](LICENSE).
