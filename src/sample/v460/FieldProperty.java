package sample.v460;

public class FieldProperty {

    private String Name;
    private String Value;

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }
    public void setValue(String value) {
        Value = value;
    }

    @Override
    public String toString() {
        return "FieldProperty{" +
                "Name='" + Name + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}
