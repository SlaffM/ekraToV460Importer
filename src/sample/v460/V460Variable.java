package sample.v460;

import org.apache.poi.ss.usermodel.Cell;
import java.util.*;

public class V460Variable {
    private TreeMap<Integer, FieldProperty> fields;

    private V460Variable(TreeMap<Integer, FieldProperty> fields){
        this.fields = fields;
    }
    V460Variable(){
        this(new TreeMap<>());
    }
    private V460Variable(V460Variable otherV460Variable){
        this(otherV460Variable.getFields());
    }

    public TreeMap<Integer, FieldProperty> getFields() { return fields; }

    void addProperty(int key, FieldProperty prop){
        fields.put(key, prop);
    }

    V460Variable copy(){
        return new V460Variable(this);
    }

    FieldProperty buildVariableField(Cell dataCell, Map<Integer, String> headers){

        FieldProperty fieldProperty = new FieldProperty();

        String dataValue = "";

        switch (dataCell.getCellTypeEnum()){
            case NUMERIC:
                dataValue = String.valueOf(Double.valueOf(dataCell.getNumericCellValue()).intValue());
                break;
            case STRING:
                dataValue = dataCell.getStringCellValue();
                break;
            case BLANK:
                dataValue = "";
                break;
            default:
                System.out.println("Тип неопределенной ячейки - " + dataCell.getCellTypeEnum());
                break;
        }

        fieldProperty.setName(headers.get(dataCell.getColumnIndex()));
        fieldProperty.setValue(dataValue);

        return fieldProperty;
    }

    @Override
    public String toString() {
        return "V460Variable{" +
                "fields=" + fields +
                '}';
    }

    public String getHeadersToCsv(){
        StringBuilder result = new StringBuilder();

        for(Map.Entry<Integer, FieldProperty> entry : getFields().entrySet()){
            FieldProperty fieldProperty = entry.getValue();

            if(fieldProperty.getName().equals("EkraTypeIgnore")) continue;
            result.append(fieldProperty.getName()).append("\t");
        }
        return result.toString();
    }

    public String toStringForWriteCSV(){
        StringBuilder result = new StringBuilder();

        for(Map.Entry<Integer, FieldProperty> entry : getFields().entrySet()){
            FieldProperty fieldProperty = entry.getValue();

            if(fieldProperty.getName().equals("EkraTypeIgnore")) continue;
            result.append(fieldProperty.getValue()).append("\t");
        }
        return result.toString();
    }
}