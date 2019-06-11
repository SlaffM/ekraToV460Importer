package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.ekra.EkraVariable;
import sample.ekra.EkraVariables;
import sample.helpers.LogInfo;
import sample.v460.FieldProperty;
import sample.v460.V460CsvWriter;
import sample.v460.V460Variable;
import sample.v460.V460Variables;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {

    public TextField panel;
    public TextField driverName;
    public TextField tagnamePrefix;
    public TextField netAddr;
    public Button startBtn;
    public ImageView imgExample;
    public Button btnLoadEkra;
    public Label txtPathEkra;
    public ListView listLog;
    public Label lblTooltip;
    private Stage stage;
    private File ekraCsv;

    private String readyV460Headers;

    public void startGenerate(ActionEvent actionEvent) throws IOException {

        ClassLoader loaderTemplate = Thread.currentThread().getContextClassLoader();
        InputStream isTemplate = loaderTemplate.getResourceAsStream("template.xlsx");
        //InputStream isEkra = loaderTemplate.getResourceAsStream("ekra.csv");
        //InputStreamReader streamReader = new InputStreamReader(isEkra, "CP1251");

        XSSFWorkbook workbook = new XSSFWorkbook(isTemplate);

        V460Variables v460Variables = V460Variables.buildVariables(workbook);
        LogInfo.setLogDataWithTitle("Загружены перменные V460 из шаблона", v460Variables.getVariables().size());

        EkraVariables.buildEkraVariables(ekraCsv.getAbsolutePath());
        LogInfo.setLogDataWithTitle("Сформированы перменные Ekra", EkraVariable.getEkraVariablesCount());

        ArrayList<String> readyV460Variables = createReadyV460Variables(EkraVariable.getAllEkravariables(), v460Variables);

        V460CsvWriter.write(readyV460Variables, readyV460Headers, ekraCsv);

    }

    private ArrayList<String> createReadyV460Variables(ArrayList<EkraVariable> ekraVariables, V460Variables v460Variables) {
        ArrayList<String> listV460Variables = new ArrayList<>();

        int rowNumber = 0;

        Collections.sort(ekraVariables);

        for(EkraVariable ekraVariable : ekraVariables){

            V460Variable newV460Variable = v460Variables.findVariableByType(ekraVariable.getType());

            for(Map.Entry<Integer, FieldProperty> entry : newV460Variable.getFields().entrySet()){

                FieldProperty fieldProperty = entry.getValue();

                if (fieldProperty.getName().equals("VariableName"))     fieldProperty.setValue(ekraVariable.getVarnamePostfixByTypeVariable(panel.getText()));
                if (fieldProperty.getName().equals("DriverName"))       fieldProperty.setValue(driverName.getText());
                if (fieldProperty.getName().equals("Matrix"))           fieldProperty.setValue(ekraVariable.getMatrixByTypeVariable());
                if (fieldProperty.getName().equals("Tagname"))          fieldProperty.setValue(tagnamePrefix.getText() + ekraVariable.getFormattedTagnameForV460());
                if (fieldProperty.getName().equals("Recourceslabel"))   fieldProperty.setValue(ekraVariable.getEkraAddress());
                if (fieldProperty.getName().equals("NetAddr"))          fieldProperty.setValue(netAddr.getText());
                if (fieldProperty.getName().equals("SymbAddr"))         fieldProperty.setValue(panel.getText()+"!" + ekraVariable.getMmsAddress());
                if (fieldProperty.getName().equals("IsRemaActiv"))      fieldProperty.setValue(ekraVariable.isMatrixActive());

            }

            if(rowNumber == 0) readyV460Headers = newV460Variable.getHeadersToCsv();

            listV460Variables.add(newV460Variable.toStringForWriteCSV());

            rowNumber++;
        }

        return listV460Variables;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listLog.itemsProperty().bind(LogInfo.logDataProperty());

        LogInfo.logDataProperty().addListener((observableValue, observableList, t1) ->
                listLog.scrollTo(LogInfo.logDataProperty().getSize()));

        ClassLoader loaderImage = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loaderImage.getResourceAsStream("primer.png");

        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        //imgExample.setImage(image);

        panel.textProperty().setValue("p80.rza");
        driverName.textProperty().setValue("driver");
        tagnamePrefix.textProperty().setValue("tagname_");
        netAddr.textProperty().setValue("135");

        Tooltip imgTooltip = new Tooltip();
        imgTooltip.setGraphic(imageView);

        lblTooltip.setTooltip(imgTooltip);


    }

    public void btnLoadPathToEkraClick(ActionEvent event) {
        FileChooser fileopen = new FileChooser();
        fileopen.setInitialDirectory(new File("."));
        fileopen.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        ekraCsv = fileopen.showOpenDialog(stage.getScene().getWindow());
        txtPathEkra.textProperty().setValue(ekraCsv.getAbsolutePath());
    }


    public void lblTooltipClick(MouseEvent mouseEvent) {
        lblTooltip.getTooltip().show(stage.getScene().getWindow());
    }

    public void lblTooltipExit(MouseEvent mouseEvent) {
        lblTooltip.getTooltip().hide();
    }
}
