package core;

import java.util.Objects;

public class Line {
    private String index;
    private String name;

    public Line(String index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(index, line.index) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name);
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }


}
