package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.ekra.EkraVariable;
import sample.ekra.EkraVariables;
import sample.v460.FieldProperty;
import sample.v460.V460CsvWriter;
import sample.v460.V460Variable;
import sample.v460.V460Variables;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class Controller {

    public TextField panel;
    public TextField driverName;
    public TextField tagnamePrefix;
    public TextField netAddr;
    public Button startBtn;

    private String readyV460Headers;

    public void startGenerate(ActionEvent actionEvent) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("./template.xlsx"));

        V460Variables v460Variables = V460Variables.buildVariables(workbook);

        EkraVariables ekraVariables = EkraVariables.buildEkraVariables("./ekra.csv");

        ArrayList readyV460Variables = createReadyV460Variables(ekraVariables, v460Variables);

        V460CsvWriter.write(readyV460Variables, readyV460Headers);

    }

    private ArrayList<String> createReadyV460Variables(EkraVariables ekraVariables, V460Variables v460Variables) {
        ArrayList listV460Variables = new ArrayList();

        int rowNumber = 0;

        for(EkraVariable ekraVariable : ekraVariables.getVariables()){

            V460Variable newV460Variable = v460Variables.findVariableByType(ekraVariable.getType());

            for(Map.Entry<Integer, FieldProperty> entry : newV460Variable.getFields().entrySet()){

                FieldProperty fieldProperty = entry.getValue();

                if (fieldProperty.getName().equals("VariableName"))     fieldProperty.setValue(ekraVariable.getVarnamePostfixForV460(panel.getText()));
                if (fieldProperty.getName().equals("DriverName"))       fieldProperty.setValue(driverName.getText());
                if (fieldProperty.getName().equals("Matrix"))           fieldProperty.setValue(ekraVariable.getMatrixByTypeVariable());
                if (fieldProperty.getName().equals("Tagname"))          fieldProperty.setValue(tagnamePrefix.getText() + ekraVariable.getTagname());
                if (fieldProperty.getName().equals("Recourceslabel"))   fieldProperty.setValue(ekraVariable.getEkraAddress());
                if (fieldProperty.getName().equals("NetAddr"))          fieldProperty.setValue(netAddr.getText());
                if (fieldProperty.getName().equals("SymbAddr"))         fieldProperty.setValue(panel.getText()+"!" + ekraVariable.getMmsAddress());
                if (fieldProperty.getName().equals("IsRemaActiv"))      fieldProperty.setValue(ekraVariable.isMatrixActive());

            }

            if(rowNumber == 0) readyV460Headers = newV460Variable.getHeadersToCsv();

            listV460Variables.add(newV460Variable.toStringForWriteCSV());
        }

        return listV460Variables;
    }

}
