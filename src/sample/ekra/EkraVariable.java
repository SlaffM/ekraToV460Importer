package sample.ekra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EkraVariable {

    private String mmsAddress;
    private String type;
    private String ekraAddress;
    private String tagname;
    private String srcEkraAddressAndTag;

    public String getMmsAddress() {
        return mmsAddress;
    }

    public void setMmsAddress(String mmsAddress) {
        this.mmsAddress = mmsAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEkraAddress() {
        return ekraAddress;
    }

    public void setEkraAddress(String ekraAddress) {
        this.ekraAddress = getFormattedEkraAddress(ekraAddress);
    }

    public String getSrcEkraAddressAndTag() {
        return srcEkraAddressAndTag;
    }

    public void setSrcEkraAddressAndTag(String srcEkraAddressAndTag) {
        this.srcEkraAddressAndTag = srcEkraAddressAndTag;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = getFormattedTagname(tagname);
    }

    public boolean isValidVariable(){
        return !(isVariableBad() || getSrcEkraAddressAndTag().equals("///") || getSrcEkraAddressAndTag().isEmpty() || getSrcEkraAddressAndTag().startsWith("///{"));
    }

    private String getFormattedEkraAddress(String address){
        return getGroup(address, "///(\\d+),");
    }

    private String getFormattedTagname(String tagname){
        String formattedTag = getGroup(tagname, "///\\d+,(.*)").replaceAll("\'", "");

        if(isLengthTagnameNotValid(formattedTag)) formattedTag = tryGetShortTagname(formattedTag);

        return formattedTag;
    }
    private String tryGetShortTagname(String tagname){
        return tagname.replaceAll("Откл\\w+", "Откл.");
    }
    private boolean isLengthTagnameNotValid(String tagname){
        return tagname.length() <= 50;
    }

    private String getGroup(String str, String pattern){
        String result = "";

        Pattern ptrn = Pattern.compile(pattern);
        Matcher matcher = ptrn.matcher(str);

        if(matcher.find()){ result = matcher.group(1).trim();}
        return result;
    }

    //nameV460
    public String getVarnamePostfixForV460(String panel){
        String postfix = "";
        String rzaText = ".rza";
        if      (isVariableTS_led())            postfix = panel + rzaText + ".led" + getLedNumber();
        else if (isVariableTI())                postfix = panel + rzaText + ".TI" + "." + getEkraAddress();
        else if (isVariableTS_key())            postfix = panel + rzaText + ".SA" + getEkraAddress();
        else if (isVariableTS_connect())        postfix = panel + rzaText + ".diagn";
        else if (isvariableTS_fault())          postfix = panel + rzaText + ".fault_rza";
        else if (isvariableTS_srab())           postfix = panel + rzaText + ".srab_rza";
        else if (isVariableTU_ledCmd())         postfix = panel + rzaText + ".led.cmd";
        else if (isVariableTS_lan())            postfix = panel + rzaText + ".lan" + "." + getEkraAddress();
        else                                    postfix = panel + rzaText + ".ds" + getEkraAddress();
        return postfix;
    }

    private boolean isVariableTS(){
        return getType().equals("BOOLEAN");
    }
    private boolean isvariableTS_srab(){
        return getMmsAddress().contains("CALH1/GrWrn");
    }
    private boolean isvariableTS_fault(){
        return getMmsAddress().contains("CALH1/GrAlm");
    }
    private boolean isVariableTS_led(){
        return isVariableTS() && getMmsAddress().contains("ledGG");
    }
    private boolean isVariableTU_ledCmd(){
        return getMmsAddress().endsWith("LLN0/LEDRs/Oper.ctlVal[CO]");
    }
    private boolean isVariableTS_connect(){
        return getMmsAddress().endsWith("CALH1/Health/stVal[ST]");
    }
    private boolean isVariableTS_lan(){
        return (getMmsAddress().endsWith("LCCH1/ChLiv/stVal[ST]") || getMmsAddress().endsWith("LCCH1/RedChLiv/stVal[ST]")) &&
                (getEkraAddress().equals("214") || getEkraAddress().equals("215"));
    }
    private boolean isVariableTS_key(){
        return isVariableTS() &&
                ((getType().equals("Enum.Mod") && getMmsAddress().endsWith("Mod/stVal[ST]")) ||
                        (getTagname().contains("SA") || getTagname().contains("Вывод")));
    }
    private boolean isVariableTI(){
        return getType().equals("FLOAT32");
    }
    private boolean isVariableBad(){
        return (getTagname().contains("Реле")) ||
                (getTagname().startsWith("ПО")) ||
                (getTagname().startsWith("Электронный ключ")) ||
                (getTagname().contains("SET_D")) ||
                (getTagname().startsWith("Вход №")) ||
                (getTagname().startsWith("Вход N")) ||
                (getTagname().startsWith("ИО")) ||
                (getTagname().contains("вторичная величина")) ||
                (getTagname().contains("VIRT")) ||
                (getTagname().contains("GOOSEIN") || getTagname().contains("GOOSEOUT"));

    }

    private String getLedNumber(){
        String result = "";
        String pattern = "/Ind(\\d+)/";

        Pattern ptrn = Pattern.compile(pattern);
        Matcher matcher = ptrn.matcher(getMmsAddress());

        if(matcher.find()){ result = matcher.group(1).trim();}
        return result;
    }

    //Matrix
    public String getMatrixByTypeVariable(){
        String resultMatrix = "";
        if     (isVariableTS_led())             resultMatrix = "Горит/Погас_ВМ";
        else if(isVariableTS_key())             resultMatrix = "Вывод/Работа_BM";
        else if(isvariableTS_srab())            resultMatrix = "ПС1_Срабатывание_снято/1_0";
        else if(isvariableTS_fault())           resultMatrix = "ПС2_Неисправность_норма/1_0";
        else if(isVariableTS_connect())         resultMatrix = "";
        else if(isVariableTU_ledCmd())          resultMatrix = "ОС_Сигнал/1";
        else if(isVariableTS_lan())             resultMatrix = "Норма/Неисправность_GM3";
        else if(isVariableTS())                 resultMatrix = "ПС2_Сигнал_норма/1_0";
        else                                    resultMatrix = "";
        return resultMatrix;
    }
    public String isMatrixActive(){
        return getMatrixByTypeVariable().isEmpty() ? "FALSE" : "TRUE";
    }


    @Override
    public String toString()
    {
        return "EkraVariable [mms=" + getMmsAddress() + ", type=" + getType() +
                ", ekraAddress=" + getEkraAddress() + ", tagname=" + getTagname() + "]";
    }

}
