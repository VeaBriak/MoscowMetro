import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
/* Написать код парсинга страницы Википедии “Список станций Московского метрополитена”, который будет на основе этой страницы создавать JSON-файл со списком станций по линиям и списком линий по формату JSON-файла из проекта SPBMetro (файл map.json, приложен к домашнему заданию)
 * Также пропарсить и вывести в JSON-файл переходы между станциями.
 * Написать код, который прочитает созданный JSON-файл и напечатает количества станций на каждой линии.*/


public class Main
{
    public static void main(String[] args) throws IOException, ParseException {
        String address = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Document doc = Jsoup.connect(address).maxBodySize(0).get();
        Element elements = doc.select("table").get(3);
        Elements stations = elements.select("tr");
        stations.stream().skip(1).forEach(station -> {
            Elements columns = station.select("td");
            String name = columns.get(1).text();
            String line = columns.get(0).child(1).attr("title");
            List<String> lineNumber = columns.get(0).children().eachText();
            List<String> connectionsLine = columns.get(0).children().eachAttr("title");
            List<String> connectionsNumber = columns.get(3).children().eachText();
            Parse.parseStations(name, lineNumber, connectionsLine);
            Parse.parseLines(line, lineNumber);
            if (connectionsNumber.size() != 0) {
                Parse.parseConnections(columns, name);
            }
        });

        Parse.createJsonFile();
        Parse.jsonParser();
    }
}
