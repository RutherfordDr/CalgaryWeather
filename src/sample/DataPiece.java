package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class DataPiece {
    private SimpleStringProperty city, date;
    private SimpleDoubleProperty currentTemp, lowTemp, highTemp;
    private SimpleIntegerProperty id;

    public DataPiece(String city, Double currentTemp, Double lowTemp,
                      Double highTemp, String date, Integer id){
        this.city = new SimpleStringProperty(city);
        this.currentTemp = new SimpleDoubleProperty(currentTemp);
        this.lowTemp = new SimpleDoubleProperty(lowTemp);
        this.highTemp = new SimpleDoubleProperty(highTemp);
        this.date = new SimpleStringProperty(date);
        this.id = new SimpleIntegerProperty(id);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setID(Integer id) {
        this.id.set(id);
    }

    public Integer getID() {
        return id.get();
    }

    public Double getCurrentTemp() {
        return currentTemp.get();
    }

    public void setCurrentTemp(Double currentTemp) {
        this.currentTemp = new SimpleDoubleProperty(currentTemp);
    }

    public Double getLowTemp() {
        return lowTemp.get();
    }

    public SimpleDoubleProperty lowTempProperty() {
        return lowTemp;
    }

    public void setLowTemp(Double lowTemp) {
        this.lowTemp.set(lowTemp);
    }

    public Double getHighTemp() {
        return highTemp.get();
    }

    public SimpleDoubleProperty highTempProperty() {
        return highTemp;
    }

    public void setHighTemp(Double highTemp) {
        this.highTemp.set(highTemp);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
