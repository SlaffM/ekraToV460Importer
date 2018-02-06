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
        EkraRulesFilter ekraRulesFilter = new EkraRulesFilter(this);
        return ekraRulesFilter.getTypeVariable() != EkraVariableType.NO_USE;
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
    private String getGroupByMmsaddress(){
        return getGroup(getMmsAddress(), "(/\\w+/)");
    }
    private String getGroup(String str, String pattern){
        String result = "";

        Pattern ptrn = Pattern.compile(pattern);
        Matcher matcher = ptrn.matcher(str);

        if(matcher.find()){ result = matcher.group(1).trim();}
        return result;
    }

    private EkraVariableType getFilterType(){
        EkraRulesFilter ekraRulesFilter = new EkraRulesFilter(this);
        return ekraRulesFilter.getTypeVariable();
    }

    //nameV460
    public String getVarnamePostfixForV460(String panel){
        String rzaText = ".rza";
        switch (getFilterType()){
            case LED:
                return panel + rzaText + ".led" + getLedNumber();
            case LED_CMD:
                return panel + rzaText + ".led.cmd";
            case TI:
                return panel + rzaText + ".TI" + "." + getEkraAddress();
            case KEY:
                return panel + rzaText + ".SA" + getEkraAddress();
            case CONNECT:
                return panel + rzaText + ".diagn";
            case FAULT:
                return panel + rzaText + ".fault_rza";
            case SRAB:
                return panel + rzaText + ".srab_rza";
            case LAN:
                return panel + rzaText + ".lan" + "." + getEkraAddress();
            case DS:
                return panel + rzaText + ".ds" + getEkraAddress();
            default:
                return panel + rzaText + ".no_import";
        }
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
        switch (getFilterType()){
            case LED:
                return "Горит/Погас_ВМ";
            case LED_CMD:
                return "ОС_Сигнал/1";
            case TI:
                return "";
            case KEY:
                return "Вывод/Работа_BM";
            case CONNECT:
                return "";
            case FAULT:
                return "ПС2_Неисправность_норма/1_0";
            case SRAB:
                return "ПС1_Срабатывание_снято/1_0";
            case LAN:
                return "Норма/Неисправность_GM3";
            case DS:
                return "ПС2_Сигнал_норма/1_0";
            default:
                return "ПС2_Сигнал_норма/1_0";
        }
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
