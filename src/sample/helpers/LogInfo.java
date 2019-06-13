package sample.helpers;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.List;

public final class LogInfo {

    private static SimpleListProperty logData = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static SimpleListProperty logDataProperty() {
        return logData;
    }

    @SuppressWarnings("unchecked")
    private static void setLogData(String data) {
        logData.add(new SimpleStringProperty(data).getValue());
    }

    public static void setLogDataWithTitle(String title, Object data){
        setLogData(title + ":\t" + data.toString());
    }

    public static void setErrorData(String data){
        setLogDataWithTitle("Ошибка", data);
    }

}
