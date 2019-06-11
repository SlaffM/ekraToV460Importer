package sample.v460;

import au.com.bytecode.opencsv.CSVWriter;
import sample.helpers.LogInfo;

import java.io.*;
import java.util.ArrayList;

public class V460CsvWriter {

    public static void write(ArrayList<String> listVariables, String headers, File file) throws IOException {

        String csv = file.getAbsolutePath().replace(".csv", "_data_v460.txt");

        try (CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(new FileOutputStream(csv), "cp1251"),
                '\t',
                CSVWriter.NO_QUOTE_CHARACTER)) {
            writeRecord(writer, headers);
            listVariables.forEach(s -> writeRecord(writer, s));
            LogInfo.setLogDataWithTitle("Создан файл", csv);
        } catch (Exception e) {
            LogInfo.setErrorData(e.getMessage());
        }
    }

    private static void writeRecord(CSVWriter writer, String line){
        String [] headersRecord = line.split("\t");
        writer.writeNext(headersRecord);
    }

}
