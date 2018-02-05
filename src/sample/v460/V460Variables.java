package sample.v460;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class V460Variables {

    private ArrayList<V460Variable> variables;

    private static Map<Integer, String> headers;

    public V460Variables(){
        variables = new ArrayList<>();
    }

    public ArrayList<V460Variable> getVariables() { return variables; }

    public void addVariable(V460Variable v460Variable){
        variables.add(v460Variable);
    }

    public static V460Variables buildVariables(XSSFWorkbook workbook){

        V460Variables v460Variables = new V460Variables();

         outer: for (Row rowData : workbook.getSheet("template")) {

            V460Variable v460Variable = new V460Variable();

            if (isBuildHeadersReady(rowData)) { continue; }

            for (Map.Entry<Integer, String> entry : headers.entrySet()){
                rowData.getCell(entry.getKey().intValue(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            }

            for (Cell dataCell : rowData) {

                int columnIndex = dataCell.getColumnIndex();

                FieldProperty fieldProperty = v460Variable.buildVariableField(dataCell, headers);

                if(fieldProperty.getName().equals("TypeName") && fieldProperty.getValue().equals("")) {
                    continue outer;
                }

                v460Variable.addProperty(columnIndex, fieldProperty);

            }

            v460Variables.addVariable(v460Variable);
        }

        return v460Variables;
    }

    public V460Variable findVariableByType(String type){
        V460Variable v460Variable = new V460Variable();
        for(V460Variable v460Var : getVariables()){
            for(Map.Entry<Integer, FieldProperty> entry : v460Var.getFields().entrySet()){
                FieldProperty fieldProperty = entry.getValue();
                if(fieldProperty.getName().equals("EkraTypeIgnore") && fieldProperty.getValue().equals(type)){
                    v460Variable = v460Var.copy();
                }
            }
        }
        return v460Variable;
    }


    private static boolean isBuildHeadersReady(Row rowHeaders){
        boolean result = false;
        if (rowHeaders.getRowNum() == 0) {
            headers = new Hashtable<>();
            for (Cell headerName : rowHeaders) {
                headers.put(headerName.getColumnIndex(), headerName.getStringCellValue());
            }
            result = true;
        }
        return result;
    }

    public void printVariablesFields(){
        for(V460Variable v460Variable : getVariables()) {

            for (Map.Entry<Integer, FieldProperty> entryObj : v460Variable.getFields().entrySet()){
                Map.Entry<Integer, FieldProperty> entry = entryObj;

                System.out.print(entry.getValue().getName() + " = " + entry.getValue().getValue() + ", ");
            }
            System.out.println("");
        }
    }
}
