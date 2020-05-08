package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import com.google.gson.*;

/*
    This class is used to connect to the weathermap API.
 */
public class WeatherAPI {
    private String api_Key = "";
    private String location = "Calgary,CA";
    private String urlString;
    private String data;
    private ArrayList<String> parsedData;

    public WeatherAPI() {
        urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + api_Key +"&units=metric";
    }

    public WeatherAPI(String aLocation) {
        setLocation(aLocation);
        urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + aLocation + "&appid=" + api_Key +"&units=metric";
    }

    public void run() {
        callAPI(api_Key, location);
    }

    public String getAPI_KEY() {
        return api_Key;
    }

    public String getLocation() {
        return location;
    }

    public String getData() {
        return data;
    }

    public ArrayList<String> getParsedData(){
        return parsedData;
    }

    public void setData(String aData) {
        data = aData;
    }

    public void setParsedData(ArrayList<String> aParsedData) {
        parsedData = aParsedData;
    }

    public void setLocation(String alocation) {
        location = alocation;
    }
    /*
     * Calls weathermapAPI and sets the data variable to the APIString. If can't connect to server, data value is set to unavailable.
     */
    public void callAPI(String API_Key, String location) {

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            setData(result.toString());

        } catch(IOException e) {
            System.out.println(e.getMessage());
            setData("Unavailable");
        }
    }

    /*
     * Parses the API call string and returns the data in a list of strings. The order of the list is the order it will
     *  be entered into the example table.
     */
    public ArrayList<String> parseValues(String data) {
        ArrayList<String> dataList = new ArrayList<>();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(data.toString());
        JsonObject rootobj = root.getAsJsonObject();

        JsonElement city = rootobj.get("name");
        String cityString = city.toString();
        cityString = cityString.replace("\"", "");


        JsonElement temp = rootobj.get("main");
        JsonElement tempMinE = rootobj.get("main");
        JsonElement tempMaxE = rootobj.get("main");
        temp = ((JsonObject) temp).get("temp");
        tempMinE = ((JsonObject) tempMinE).get("temp_min");
        tempMaxE = ((JsonObject) tempMaxE).get("temp_max");
        String tempString = temp.toString();

        dataList.add(cityString);
        dataList.add(tempString);
        dataList.add(tempMaxE.toString());
        dataList.add(tempMinE.toString());

        setParsedData(dataList);
        return dataList;


    }

}


