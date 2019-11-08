import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Line;
import core.Metro;
import core.Station;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parse {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static List<Line> lines = new LinkedList<>();
    private static Map<String, List<String>> stations = new TreeMap<>();
    private static List<List<Station>> connection = new ArrayList<>();
    private static Metro metro;

    static void createJsonFile() throws IOException{
        metro = new Metro(Parse.lines, Parse.stations, Parse.connection);
        try(FileWriter fileWriter = new FileWriter("src/main/res/map.json")) {
            fileWriter.write(GSON.toJson(metro));
        }
    }

    static void parseStations(String name, List<String> lineNumber, List<String> connectionsLine) {
        String lineIndex = lineNumber.get(0);
        if (!stations.containsKey(lineIndex)) {
            stations.put(lineIndex, new ArrayList<>());
            stations.get(lineIndex).add(name);
        } else
            stations.get(lineIndex).add(name);

        if (connectionsLine.size() == 2) {
            if (!stations.containsKey(lineNumber.get(1))) {
                stations.put(lineNumber.get(1), new ArrayList<>());
            } else
                stations.get(lineNumber.get(1)).add(name);
        }
    }

    static void parseLines(String line, List<String> lineNumber) {
        Line line1 = new Line(lineNumber.get(0), line);
        if (!lines.contains(line1)) {
            lines.add(line1);
        }
    }

    static void parseConnections(Elements columns, String name) {
        List<String> connectionLines = columns.get(3).children().eachAttr("title");
        List<String> connectionNumbers = columns.get(3).children().eachText();
        List<String> lineNumbers = columns.get(0).children().eachText();
        String lineIndex = lineNumbers.get(1);
        if (connectionNumbers.size() != 0) {
            List<Station> stationList = new ArrayList<>();
            stationList.add(new Station(lineIndex, name));
            for (int i = 0; i < connectionNumbers.size(); i++) {
                stationList.add(new Station(connectionNumbers.get(i), connectionLines.get(i)));
            }
            connection.add(stationList);
        }
    }

    static void jsonParser() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(parseFile("src/main/res/map.json"));

        Map<String, List<String>> stations = (Map<String, List<String>>) object.get("stations");
        for (String lineIndex : stations.keySet()) {
            JSONArray stationsArray = (JSONArray) stations.get(lineIndex);
            for (Line line : metro.getLines()) {
                if (line.getIndex().equals(lineIndex)) {
                    System.out.println("Линия " + lineIndex + " '" + line.getName()
                            + "' - колличество станций на линии: " + stationsArray.size());
                }
            }
        }
    }

    private static String parseFile(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(s));
            lines.forEach(stringBuilder::append);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
