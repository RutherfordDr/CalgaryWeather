package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.print.attribute.standard.MediaSize;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;




public class Controller implements Initializable {
    private static WeatherAPI APIConnector = new WeatherAPI();
    private static DataBaseConnector DBConnector = new DataBaseConnector();
    private String currentWeather;
    private String highTemp;
    private String lowTemp;
    private String city;
    private DataPiece selectedData;
    private XYChart.Series<String, Double> series1;
    private ArrayList<Float> tempList;
    private ArrayList<String> dateList;

    private void setCurrentWeather(String currentWeather){
        this.currentWeather = currentWeather;
    }
    private void setHighTemp(String hightTemp){
        this.highTemp = highTemp;
    }
    private void setLowTemp(String lowTemp){
        this.lowTemp = lowTemp;
    }
    private void setCity(String city){
        this.city = city;
    }
    private void setTempList(){

        tempList = DBConnector.getTemperatureList();

    }
    private void setDateList(){

        dateList = DBConnector.getDateList();

    }

    @FXML
    private Label HighestTemp;

    @FXML
    private Label LowestTemp;

    @FXML
    private Button DeleteSelectedButton;


    @FXML
    private ScatterChart<String, Double> sc;

    @FXML
    private TableView<DataPiece> dataBase;

    @FXML
    private TableColumn<DataPiece,String> CityCol;

    @FXML
    private TableColumn<DataPiece, Double> CurrentTempCol;

    @FXML
    private TableColumn<DataPiece, Double> LowTempCol;

    @FXML
    private TableColumn<DataPiece, Double> HighTempCol;

    @FXML
    private TableColumn<DataPiece, LocalDate> DateCol;


    @FXML
    private Button PVButton;

    @FXML
    private Button DBVButton;

    @FXML
    private Button GetCWButton;

    @FXML
    private Label CurrentWeather;

    @FXML
    private Label OtherProperties;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        series1 = new XYChart.Series<>();
        series1.setName("Temperature");
        setPlotData();

        CityCol.setCellValueFactory(new PropertyValueFactory<DataPiece,String>("City"));
        CurrentTempCol.setCellValueFactory(new PropertyValueFactory<DataPiece,Double>("CurrentTemp"));
        HighTempCol.setCellValueFactory(new PropertyValueFactory<DataPiece,Double>("LowTemp"));
        LowTempCol.setCellValueFactory(new PropertyValueFactory<DataPiece,Double>("HighTemp"));
        DateCol.setCellValueFactory(new PropertyValueFactory<DataPiece,LocalDate>("Date"));
        ObservableList<DataPiece> dataList = DBConnector.getDataPiece();
        dataBase.setItems(dataList);
    }


    @FXML
    private void handlePVButton(final ActionEvent event) {
        sc.visibleProperty().setValue(true);
        dataBase.visibleProperty().set(false);
        DeleteSelectedButton.visibleProperty().set(false);
    }

    @FXML
    private void handleDBVButton(final ActionEvent event) {
        sc.visibleProperty().setValue(false);
        dataBase.visibleProperty().setValue(true);
        DeleteSelectedButton.visibleProperty().set(true);

    }

    @FXML
    private void handleADDButton(final ActionEvent event) {
        if (currentWeather != null) {
            // Write the current data to the SQL database
            DBConnector.writeToDatabase(APIConnector.getParsedData().get(0), APIConnector.getParsedData().get(1),
                    APIConnector.getParsedData().get(2), APIConnector.getParsedData().get(3));
            // Update the database view
            ObservableList<DataPiece> dataList = DBConnector.getDataPiece();
            dataBase.setItems(dataList);
            // Updates the tempList and the DateList, then uses that to update the plot view
            setTempList();
            setDateList();
            series1.getData().add(new XYChart.Data(dateList.get(dateList.size() - 1), tempList.get(tempList.size() - 1)));
        }
    }

    @FXML
    private void handleDeleteButton(final ActionEvent event){
        if(dataBase.getSelectionModel().getSelectedItem() != null) {
            DataPiece datarow = dataBase.getSelectionModel().getSelectedItem();
            DBConnector.deleteFromDatabase(datarow.getID());
            // Update database view
            ObservableList<DataPiece> dataList = DBConnector.getDataPiece();
            dataBase.setItems(dataList);

            // Update chart view
            series1.getData().clear();
            tempList.clear();
            dateList.clear();
            setTempList();
            setDateList();
            int i = 0;
            for (Float aFloat : tempList) {
                series1.getData().add(new XYChart.Data(dateList.get(i), aFloat));
                i += 1;
            }
        }








    }
    /*
    Connects to the API and gets all the data. Updates the current weather variables. Updates the text field.
     */
    @FXML
    private void handleGetCurrentWeather(final ActionEvent event) {
        APIConnector.run();
        APIConnector.parseValues(APIConnector.getData());
        setCity(APIConnector.getParsedData().get(0));
        setCurrentWeather(APIConnector.getParsedData().get(1));
        setLowTemp(APIConnector.getParsedData().get(2));
        setHighTemp(APIConnector.getParsedData().get(3));
        CurrentWeather.setText("CurrentWeather:             " + currentWeather);
    }

    @FXML
    private void handleGetTopValue(final ActionEvent event) {

        HighestTemp.setText("Highest Temp:                  " + Double.toString(DBConnector.getTop()));
    }

    @FXML
    private void handleGetMinValue(final ActionEvent event) {
        LowestTemp.setText("Lowest Temp:                   " + Double.toString(DBConnector.getMin()));
    }

    @FXML
    private void handleUpdateButton(final ActionEvent event) {
        OtherProperties.setText("Other Properties:  \n" +
                                "                    Average Temp:   " + String.format("%.2f", DBConnector.getAverageTemp()) +
                                "\n\n                    Weekly Average: " + String.format("%.2f", DBConnector.getWeeklyAverageTemp()) +
                                "\n\n                    30 day Average:  " + String.format("%.2f", DBConnector.get30DayAverageTemp()));

    }
    /*
    Sets the plot data to be graphed
     */
    private void setPlotData() {
        setTempList();
        setDateList();
        int i=0;
        for (Float aFloat : tempList) {
            series1.getData().add(new XYChart.Data(dateList.get(i), aFloat));
            i += 1;
        }

        sc.getData().add(series1);
    }
}
