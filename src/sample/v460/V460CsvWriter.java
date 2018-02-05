package sample.v460;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;

public class V460CsvWriter {

    public static void write(ArrayList<String> list) throws IOException {
        String csv = "./dataV460.txt";
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csv), "cp1251"), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        for(String line : list){
            String [] record = line.split("\t");
            writer.writeNext(record);
        }
        writer.close();
    }

}
