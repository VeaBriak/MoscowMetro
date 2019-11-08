package core;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Metro {
    @SerializedName("lines")
    private List<Line> lines;
    @SerializedName("stations")
    private Map<String, List<String>> stations;
    @SerializedName("connections")
    private List<List<Station>> connections;

    public Metro(List<Line> lines, Map<String, List<String>> stations, List<List<Station>> connections) {
        this.lines = lines;
        this.stations = stations;
        this.connections = connections;
    }

    public List<Line> getLines(){
        return lines;
    }
}
