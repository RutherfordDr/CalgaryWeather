package sample;
import java.util.Scanner;


/*
    Mostly used for debugging purposes. Now that the GUI is complete it doesn't really serve a purpose.
 */
public class TextUserInterface {
    private static WeatherAPI APIConnector = new WeatherAPI();
    private static DataBaseConnector DBConnector;
    /*
        Connects to the weatherMapAPI, and extracts and parses the date into a format ready for the SQL database.
        Prompts user if they would like to add the displayed data. If yes, the data is added. If anything else the
        program stops.
     */
    public static void main(String[] args) {

        APIConnector.run();
        System.out.println(APIConnector.getData());
        APIConnector.parseValues(APIConnector.getData());
        System.out.println(APIConnector.getParsedData());
        System.out.println(	APIConnector.getParsedData().get(0));
        System.out.println(APIConnector.getParsedData().get(1));
        System.out.println(APIConnector.getParsedData().get(2));
        System.out.println(APIConnector.getParsedData().get(3));
        System.out.println("Would you like to add this result to the database? Yes/No");
        Scanner input = new Scanner(System.in);
        String response = input.next();

        DBConnector = new DataBaseConnector();
        if (response.equals("Yes")) {

            DBConnector.writeToDatabase(APIConnector.getParsedData().get(0), APIConnector.getParsedData().get(1),
                    APIConnector.getParsedData().get(2), APIConnector.getParsedData().get(3));

        }


}}
