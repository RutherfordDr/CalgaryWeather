package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * Steps to get to SQL databases on computer:
 * 		1) Open MYSQL 8.0 command line
 * 		2) Type in password
 * 		3) USE example
 * 		4) select * from example
 *
 * Does everything regarding connecting the to SQL database. Allows one to view,
 * write, delete, etc... from/to the database.
 */
public class DataBaseConnector {
    private String url;
    private String username;
    private String password;
    private ArrayList<Float> temperatureList = new ArrayList<Float>();
    private ArrayList<String> dateList = new ArrayList<String>();



    public DataBaseConnector() {
        url ="";
        username = "";
        password = "";
    }

    /***
     *  Connects to database and adds every element in the currentTemp column to the temperatureList
     * @return temperatureList
     */
    public ArrayList<Float> getTemperatureList(){
        try {
            Connection conn = DriverManager.getConnection(url,username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT currentTemp FROM Weather");
            int i = 0;
            while (rs.next()) {
                temperatureList.add(rs.getFloat("currentTemp"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccessful.");
        }
        return temperatureList;
    }

    public ArrayList<String> getDateList(){
        try {
            Connection conn = DriverManager.getConnection(url,username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT date FROM Weather");
            int i = 0;
            while (rs.next()) {
                dateList.add(rs.getString("date"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccessful.");
        }
            return dateList;
    }

    public void writeToDatabase(String city, String temp, String tempMaxError, String tempMinError) {
        try {
            Connection conn = DriverManager.getConnection(url,username, password);
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO Weather (city, currentTemp, highTemp, lowTemp, date) " +
                    "VALUES ('" + city + "','" + temp + "','" + tempMaxError + "','" + tempMinError + "', NOW())");
            conn.close();
        } catch (Exception e) {
            System.out.println("Unsuccessful.");
            System.out.println(e.getMessage());
        }
    }

    public void getTable() {
        try {
            Connection conn = DriverManager.getConnection(url,username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Weather");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                String columnName = rsmd.getColumnName(i);
                System.out.format(columnName + "     ");
            }
            System.out.println();
            while (rs.next()) {
                for (int j = 1; j <= columnsNumber; j++) {
                    System.out.print(rs.getString(j) + "        ");
                }
                System.out.println();

            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccessful.");
        }


    }

    public void deleteFromDatabase(Integer id) {
        try {
            Connection conn = DriverManager.getConnection(url,username, password);
            Statement st = conn.createStatement();

            st.executeUpdate("DELETE FROM Weather " +
                    "WHERE " +
                    "id = " +id);
            conn.close();
        } catch (Exception e) {
            System.out.println("Unsuccessful.");
            System.out.println(e.getMessage());
        }

    }

    public double getTop(){
        double topValue = -999;
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(currentTemp) AS currentTemp FROM Weather");
            while (rs.next()) {
                topValue = rs.getDouble("currentTemp");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccesful");
        }

        return topValue;
    }

    public double getMin(){
        double minValue = 999;
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MIN(currentTemp) AS currentTemp FROM Weather");
            while (rs.next()) {
                minValue = rs.getDouble("currentTemp");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccesful");
        }

        return minValue;
    }

    public double getAverageTemp(){
        double avgValue = -999;
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT AVG(currentTemp) AS currentTemp FROM Weather");
            while (rs.next()) {
                avgValue = rs.getDouble("currentTemp");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccesful");
        }

        return avgValue;
    }

    public double getWeeklyAverageTemp() {
        double avgWeeklyValue = -999;
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT AVG(currentTemp) AS currentTemp FROM Weather " +
                                               "WHERE date >= DATE_ADD(CURDATE(),INTERVAL -7 DAY);");
            while (rs.next()) {
                avgWeeklyValue = rs.getDouble("currentTemp");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccesful");
        }

        return avgWeeklyValue;

    }

    public double get30DayAverageTemp() {
        double avg30Value = -999;
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT AVG(currentTemp) AS currentTemp FROM Weather " +
                                               "WHERE date >= DATE_ADD(CURDATE(),INTERVAL -30 DAY);");
            while (rs.next()) {
                avg30Value = rs.getDouble("currentTemp");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccesful");
        }

        return avg30Value;

    }

    public ObservableList<DataPiece> getDataPiece() {
        ObservableList<DataPiece> dataList = FXCollections.observableArrayList();

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Weather");
            while (rs.next()) {
                DataPiece dataRow = new DataPiece(rs.getString(1), rs.getDouble(2), rs.getDouble(3),
                        rs.getDouble(4), rs.getString(5), rs.getInt(6));

                dataList.add(dataRow);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unsuccesful");
        }
        return dataList;
    }
}
