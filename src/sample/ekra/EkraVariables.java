package sample.ekra;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;

public class EkraVariables {

    public static ArrayList<EkraVariable> buildEkraVariables(String file) throws IOException {

        CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream(file), "CP1251"),
                ';' ,
                '"' ,
                0);

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {

            String mms = nextLine[0];
            String type = nextLine[1];
            String ekraAddress = nextLine[3];
            String tagName = nextLine[3];
            String srcAddressAndTag = nextLine[3];

            new EkraVariableV2(mms, type, ekraAddress, tagName, srcAddressAndTag);
        }

        return EkraVariable.getAllEkravariables();
    }

}
