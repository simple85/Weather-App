import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

    WeatherAppGui() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiComponents();

        setVisible(true);


    }

    private void addGuiComponents() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 350, 40);
        searchTextField.setFont(new Font("New Times Roman", Font.PLAIN, 20));
        add(searchTextField);


        JLabel weatherConditionImage = new JLabel(loadImage("clouds-3.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        JLabel temperatureText = new JLabel("10 C°");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Ink Free", Font.BOLD, 48));

        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        JLabel weatherConditionDescription = new JLabel("Cloudy");
        weatherConditionDescription.setBounds(0, 405, 450, 36);
        weatherConditionDescription.setFont(new Font("Ink Free", Font.PLAIN, 32));
        weatherConditionDescription.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDescription);

        JLabel humidityImage = new JLabel(loadImage("humidity.png"));
        humidityImage.setBounds(10, 530, 74, 66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(80, 530, 85, 55);
        humidityText.setFont(new Font("Ink Free", Font.PLAIN, 16));
        add(humidityText);

        JLabel windspeedImage = new JLabel(loadImage("wind-2.png"));
        windspeedImage.setBounds(220, 530, 74, 65);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(300, 530, 90, 60);
        windspeedText.setFont(new Font("Ink Free", Font.PLAIN, 16));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 45, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get location from user
                String userInput = searchTextField.getText();
                userInput = userInput.trim();

                //retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                //update gui

                if (weatherData == null) {
                    JOptionPane.showMessageDialog(WeatherAppGui.this,
                            "Could not find weather for \"" + userInput + "\"", "Weather App",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");
                weatherConditionImage.setIcon(loadImage(iconFor(weatherCondition)));

                //update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "°C");

                //update weather condition text
                weatherConditionDescription.setText(weatherCondition);

                //update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                //update windspeed text
                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + " km/h</html>");
            }
        });
        add(searchButton);
    }

    //map a weather condition to the icon that represents it
    static String iconFor(String weatherCondition) {
        return switch (weatherCondition) {
            case "Clear", "Mainly Clear" -> "sun.png";
            case "Light Drizzle", "Moderate Drizzle", "Dense Drizzle",
                 "Light Freezing Drizzle", "Dense Freezing Drizzle",
                 "Slight Rain", "Moderate Rain", "Heavy Rain",
                 "Light Freezing Rain", "Heavy Freezing Rain",
                 "Slight Rain Showers", "Moderate Rain Showers",
                 "Violent Rain Showers" -> "rain.png";
            case "Slight Snow Fall", "Moderate Snow Fall",
                 "Heavy Snow Fall", "Snow Grains", "Slight Snow Showers",
                 "Heavy Snow Showers" -> "snowy.png";
            case "Thunderstorm" -> "thunder.png";
            case "Thunderstorm With Slight Hail", "Thunderstorm With Heavy Hail" -> "thunderstorm.png";
            default -> "clouds-3.png";
        };
    }

    //images are bundled on the classpath so the app also runs from a jar
    private ImageIcon loadImage(String fileName) {

        try (InputStream in = getClass().getResourceAsStream("/" + fileName)) {
            if (in != null) {
                BufferedImage image = ImageIO.read(in);
                return new ImageIcon(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Cannot find the file " + fileName);
        return null;
    }
}
