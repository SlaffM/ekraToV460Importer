package sample.ekra;

import au.com.bytecode.opencsv.CSVReader;
import java.io.*;
import java.util.ArrayList;

public class EkraVariables {

    private static String fileCsv;
    private ArrayList<EkraVariable> variables;

    public EkraVariables(String file, ArrayList<EkraVariable> ekraVariables){
        fileCsv = file;
        variables = ekraVariables;
    }
    public EkraVariables(String file){
        this(file, new ArrayList<>());
    }
    public EkraVariables(){
        this(fileCsv, new ArrayList<>());
    }

    public static EkraVariables buildEkraVariables(String file) throws IOException {

        EkraVariables ekraVariables = new EkraVariables();

        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file),"CP1251"), ';' , '"' , 0);
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                EkraVariable ekraVariable = new EkraVariable();
                ekraVariable.setMmsAddress(nextLine[0]);
                ekraVariable.setType(nextLine[1]);
                ekraVariable.setEkraAddress(nextLine[3]);
                ekraVariable.setTagname(nextLine[3]);
                ekraVariable.setSrcEkraAddressAndTag(nextLine[3]);

                if (ekraVariable.isValidVariable()) {
                    ekraVariables.addVariable(ekraVariable);
                }
            }

        }
        return ekraVariables;

    }

    private void addVariable(EkraVariable ekraVariable){
        variables.add(ekraVariable);
    }

    public ArrayList<EkraVariable> getVariables() {
        return variables;
    }

    public void printVariables(){
        for(EkraVariable ekraVariable : getVariables()){
            System.out.println(ekraVariable.toString());
        }
    }

}
