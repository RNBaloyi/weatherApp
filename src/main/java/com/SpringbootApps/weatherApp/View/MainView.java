package com.SpringbootApps.weatherApp.View;


import com.SpringbootApps.weatherApp.Controller.WeatherService;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.button.Button;


import javax.xml.stream.util.EventReaderDelegate;
import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;



@PageTitle("list")
@Route(value = "")
@CssImport("./style.css")
public class MainView extends VerticalLayout {
@Autowired
private WeatherService weatherService = new WeatherService();


private  TextField cityTextField = new TextField();
private Button showWeatherButton;
private H4 CurrentLocationTittle;
private H1 CurrentTemperature;
private H3 weatherDescription;
private H3 weatherMin;
private H3 weatherMax;
private H3 pressureLabel;
private H3 humidityLabel;
private H3 windSpeedLabel;
private H3 sunRiseLabel;
private H3 sunSetLabel;
private String EnteredCityName = "Paris";
private String Apikey="C7c1a7e2a537c8e6f04ce8615bdd0656";


private  String imageUrl;
private  String CurrentWeatherIcon ="09d";

    Image WeatherIcon = new Image();
    HorizontalLayout dashboardMain;
    HorizontalLayout mainDescriptionLayout;
    HorizontalLayout formLayout;


    public MainView() throws IOException {
        setMargin(false);
        setPadding(false);
        setHeader();
        setLogo();
        setUpForm();
        dashboard();
        mainLayout();
        dashboardDescription();

        setSpacing(false);

        setSizeFull();

        showWeatherButton.addClickListener(buttonClickEvent -> {

            if(!cityTextField.getValue().equals("")){
                try {
                    dashboardMain.setVisible(true);
                    mainDescriptionLayout.setVisible(true);
                    UpdateUI();


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{ Notification.show("Please enter a city");
            }

        });
    }
    private String convertTimestampToString(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



    private void UpdateUI() throws IOException {
        EnteredCityName = cityTextField.getValue();
        JSONArray weatherArray = weatherService.returnWeatherArray(EnteredCityName, Apikey);
        JSONObject currentTemp = weatherService.returnMain(EnteredCityName, Apikey);
        JSONObject currentWindSpeed = weatherService.returnWind(EnteredCityName, Apikey);
        JSONObject SunSetAndSunRise = weatherService.returnSys(EnteredCityName, Apikey);





        Double minTemperatureF = currentTemp.getDouble("temp_min");
        Double maxTemperatureF = currentTemp.getDouble("temp_max");
        Double currentTemperatureF = currentTemp.getDouble("temp");

        // Convert temperatures from Fahrenheit to Celsius
        Double minTemperatureC = minTemperatureF;
        Double maxTemperatureC = maxTemperatureF;
        Double currentTemperatureC = currentTemperatureF;

        weatherMin.setText("Min: " + String.format("%.2f", minTemperatureC) + "°C");
        weatherMax.setText("Max: " + String.format("%.2f", maxTemperatureC) + "°C");
        CurrentTemperature.setText(String.format("%.2f", currentTemperatureC) + "°C");


        int Humidity = currentTemp.getInt("humidity");
        humidityLabel.setText("Humidity: " + String.valueOf(Humidity));


        int pressure = currentTemp.getInt("pressure");
        pressureLabel.setText("Pressure: " + String.valueOf(pressure) + " kPa");

        double windspeed = currentWindSpeed.getDouble("speed");
        windSpeedLabel.setText("WindSpeed: "+String.valueOf(windspeed) +"km/hr");

        int sunset = SunSetAndSunRise.getInt("sunset");
        sunSetLabel.setText("Sunset: " + convertTimestampToString(sunset));

        int sunrise = SunSetAndSunRise.getInt("sunrise");
        sunRiseLabel.setText("Sunrise: " + convertTimestampToString(sunrise));



        if (weatherArray.length() > 0) {
            JSONObject weatherData = weatherArray.getJSONObject(0);
            CurrentLocationTittle.setText("Currently in " + EnteredCityName);
            CurrentWeatherIcon = weatherData.getString("icon");
            String weatherDescription = weatherData.getString("description");

            System.out.println(weatherDescription);
            System.out.println("Weather Icon: " + CurrentWeatherIcon);
            imageUrl = "https://openweathermap.org/img/wn/" + CurrentWeatherIcon + "@2x.png";
            WeatherIcon.setSrc(imageUrl);

        } else {
            CurrentLocationTittle.setText("No weather for the entered City :" + EnteredCityName);

        }
    }


    private void dashboardDescription() {
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        VerticalLayout descrptionLayout = new VerticalLayout();
        descrptionLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);



        Div descriptionBox = new Div();

        weatherDescription = new H3("Description: Clear Skies");
        weatherDescription.getStyle()
                .set("color", "white")
                .set("margin-bottom", "15px");

        descriptionBox.add(weatherDescription);

        weatherMin = new H3("Min: 56F");
        weatherMin.getStyle()
                .set("color", "white")
                .set("margin-bottom", "15px");
        descriptionBox.add(weatherMin);

        weatherMax = new H3("Max: 89F");
        weatherMax.getStyle()
                .set("color", "white")
                .set("margin-bottom", "15px");
        descriptionBox.add(weatherMax);

        descriptionBox.getStyle()

                .set("background-color", "rgba(224, 224, 224, 0.7)")
                .set("padding", "55px")
                .setWidth("250px")
                .setHeight("125px")
                .set("border-radius", "10px");


        descrptionLayout.add(descriptionBox);



        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.getStyle()
                .set("background-color", "rgba(224, 224, 224, 0.7)")
                .set("padding", "15px")
                .setWidth("550px")
                .set("border-radius", "10px");


        pressureLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        pressureLabel = new H3("Pressure: 123 kPa");
        pressureLabel.getStyle().set("color", "white"); // Set color
        pressureLayout.add(pressureLabel);

        humidityLabel = new H3("Humidity: 28%");
        humidityLabel.getStyle().set("color", "white"); // Set color
        pressureLayout.add(humidityLabel);

        windSpeedLabel = new H3("Wind Speed: 100 km/hr");
        windSpeedLabel.getStyle().set("color", "white"); // Set color
        pressureLayout.add(windSpeedLabel);

        sunRiseLabel = new H3("Sunrise: ");
        sunRiseLabel.getStyle().set("color", "white"); // Set color
        pressureLayout.add(sunRiseLabel);

        sunSetLabel = new H3("Sunset: ");
        sunSetLabel.getStyle().set("color", "white"); // Set color
        pressureLayout.add(sunSetLabel);



        mainDescriptionLayout.add(descrptionLayout);
        mainDescriptionLayout.add(pressureLayout);
        mainDescriptionLayout.setVisible(false);
        add(mainDescriptionLayout);


    }

    private void mainLayout() {
    }

    private void dashboard() {
        dashboardMain = new HorizontalLayout();
        dashboardMain.setDefaultVerticalComponentAlignment(Alignment.CENTER);



        //Current location
        CurrentLocationTittle = new H4("Currently in Hatfield");
        CurrentLocationTittle.setClassName("CurrentLocationTittleStyle");

        //Current Temperature
        CurrentTemperature = new H1("19F");
        CurrentTemperature.setClassName("CurrentTemperatureStyle");


        imageUrl ="https://openweathermap.org/img/wn/"+CurrentWeatherIcon+"@2x.png";
        WeatherIcon = new Image(imageUrl, "CurrentWeatherIcon");
        WeatherIcon.setHeight("150px");
        WeatherIcon.setWidth("150px");
        System.out.println("new" +CurrentWeatherIcon);

        dashboardMain.add(CurrentLocationTittle);
        dashboardMain.add(WeatherIcon);
        dashboardMain.add(CurrentTemperature);
        dashboardMain.setVisible(false);
        add(dashboardMain);
    }


    private void setUpForm() {
        formLayout = new HorizontalLayout();
        formLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        formLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);


        //addTextField
        cityTextField.setWidth("90%");
        cityTextField.setHeight("30px");
        cityTextField.getStyle().set("background-color", "rgba(255, 255, 255, 0.8)")
                                                       .set("border-radius", "10px");

        //addButton
        showWeatherButton = new Button();
        showWeatherButton.setIcon(VaadinIcon.SEARCH.create());
        showWeatherButton.getStyle().set("background-color", "white")
                                        .set("border-radius", "8px");



        formLayout.add(cityTextField);
        formLayout.add(showWeatherButton);


        add(formLayout);
    }

    private void setLogo() {
        HorizontalLayout logolayout = new HorizontalLayout();
        logolayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        logolayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logolayout.setSizeFull(); // Make the layout take up the entire width

        Image icon = new Image("weather.png", "logo");
        icon.setHeight("240px");
        icon.setWidth("280px");



        logolayout.add(icon);
        add(logolayout);


    }


    private void setHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(Alignment.START);
        headerLayout.setWidthFull();
        headerLayout.getStyle().set("text-align", "center");

        H1 title = new H1("Weather");
        title.setClassName("titleStyle");


        headerLayout.add(title);
        add(headerLayout);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }


}








/*
 JSONArray jsonArray = weatherService.returnWeatherArray("Paris","C7c1a7e2a537c8e6f04ce8615bdd0656");
        for(int i=0; i<jsonArray.length();i++){
            JSONObject weatherObject = jsonArray.getJSONObject(i);
            System.out.println("WeatherDescription :"+ weatherObject.getString("description"));
            System.out.println("WeatherID :"+ weatherObject.getInt("id"));
            System.out.println("Weather_Main :"+ weatherObject.getString("main"));

        }

        JSONObject OBJ = weatherService.returnSys("Paris","C7c1a7e2a537c8e6f04ce8615bdd0656");
        int temp = OBJ.getInt("sunset");
        System.out.println(temp);

        System.out.println(weatherService.returnCountryName("Paris","C7c1a7e2a537c8e6f04ce8615bdd0656"));


 */