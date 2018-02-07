package sample.v460;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;

public class V460CsvWriter {

    public static void write(ArrayList<String> listVariables, String headers) throws IOException {
        String csv = "./dataV460.txt";
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csv), "cp1251"), '\t', CSVWriter.NO_QUOTE_CHARACTER);

        writeRecord(writer, headers);
        for(String lineValues : listVariables){ writeRecord(writer, lineValues); }

        writer.close();
    }

    private static void writeRecord(CSVWriter writer, String line){
        String [] headersRecord = line.split("\t");
        writer.writeNext(headersRecord);
    }

}
