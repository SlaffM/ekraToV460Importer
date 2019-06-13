package sample.ekra;

import sample.helpers.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EkraVariable implements Comparable<EkraVariable>{

    private String mmsAddress;
    private String type;
    private String ekraAddress;
    private String tagname;
    private String srcEkraAddressAndTag;
    private Integer ekraAddressInt;

    private int id;
    private static Map<Integer, EkraVariable> allEkraVariables;
    private static int countId = 0;

    public EkraVariable(EkraVariable ekraVariable){
        new EkraVariable(
                ekraVariable.mmsAddress,
                ekraVariable.type,
                ekraVariable.ekraAddress,
                ekraVariable.tagname,
                ekraVariable.srcEkraAddressAndTag
        );
    }


    public EkraVariable(
            String mmsAddress,
            String type,
            String ekraAddress,
            String tagname,
            String srcEkraAddressAndTag
    ){
        setMmsAddress(mmsAddress);
        setType(type);
        setEkraAddress(ekraAddress);
        setTagname(tagname);
        setSrcEkraAddressAndTag(srcEkraAddressAndTag);
        setEkraAddressInt(this.ekraAddress);

        if (!hasEkraVariable()){
            countId++;
            this.id = countId;
            if(isValidVariable())
                allEkraVariables.put(id, this);
        }
    }


    private boolean hasEkraVariable(){
        for(EkraVariable ekraVariable: getAllEkravariables()){
            if(ekraVariable.equals(this) && ekraVariable.hashCode() == this.hashCode()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EkraVariable that = (EkraVariable) o;
        return  getMmsAddress().equals(that.getMmsAddress());

    }

    @Override
    public int hashCode() {
        return Objects.hash(mmsAddress, ekraAddress);
    }

    public static ArrayList<EkraVariable> getAllEkravariables(){
        if (allEkraVariables == null){
            allEkraVariables = new HashMap<>();
        }
        return new ArrayList<>(allEkraVariables.values());
    }

    public static int getEkraVariablesCount(){
        return getAllEkravariables().size();
    }


    public String getMmsAddress() {
        return mmsAddress;
    }
    void setMmsAddress(String mmsAddress) {
        this.mmsAddress = mmsAddress;
    }

    public String getType() {
        return type;
    }
    void setType(String type) {
        this.type = type;
    }

    public String getEkraAddress() {
        return ekraAddress;
    }
    void setEkraAddress(String ekraAddress) {
        this.ekraAddress = getFormattedEkraAddress(ekraAddress);
        setEkraAddressInt(this.ekraAddress);
    }

    public void setEkraAddressInt(String ekraAddress) {
        String findInt = Helpers.getTextWithPattern(ekraAddress, "(\\d+)");
        if(!findInt.isEmpty())
            this.ekraAddressInt = Integer.parseInt(findInt);
        else
            this.ekraAddressInt = 0;
    }

    public Integer getEkraAddressInt() {
        return ekraAddressInt;
    }

    String getSrcEkraAddressAndTag() {
        return srcEkraAddressAndTag;
    }
    void setSrcEkraAddressAndTag(String srcEkraAddressAndTag) {
        this.srcEkraAddressAndTag = srcEkraAddressAndTag;
    }

    String getTagname() {
        return tagname;
    }
    void setTagname(String tagname) {
        this.tagname = getFormattedTagname(tagname);
    }

    boolean isValidVariable(){
        EkraRulesFilter ekraRulesFilter = new EkraRulesFilter(this);
        return ekraRulesFilter.getTypeVariable() != EkraVariableType.NO_USE;
    }

    private String getFormattedEkraAddress(String address){
        return getGroup(address, "///(\\d+),");
    }
    private String getFormattedTagname(String tagname){
        return getGroup(tagname, "///\\d+,(.*)").replaceAll("\'", "");
    }
    public String getFormattedTagnameForV460(){
        String formattedTag = getTagname().trim();
        if(isTagnameEmptyOrSlash(formattedTag))     formattedTag = getTagnameByEkraVariableType(getFilterType());
        if(isLengthTagnameNotValid(formattedTag))   formattedTag = tryGetShortTagname(formattedTag);
        return formattedTag;
    }
    String tryGetShortTagname(String tagname){
        return tagname.replaceAll("Откл\\w+", "Откл.");
    }
    boolean isLengthTagnameNotValid(String tagname){
        return tagname.length() <= 50;
    }
    protected String getGroupByMmsaddress(){
        return getGroup(getMmsAddress(), "(/\\w+/)");
    }
    private String getGroup(String str, String pattern){
        String result = "";

        Pattern ptrn = Pattern.compile(pattern);
        Matcher matcher = ptrn.matcher(str);

        if(matcher.find()){ result = matcher.group(1).trim();}
        return result;
    }

    EkraVariableType getFilterType(){
        EkraRulesFilter ekraRulesFilter = new EkraRulesFilter(this);
        return ekraRulesFilter.getTypeVariable();
    }

    boolean isTagnameLed(EkraVariableType varType){
        return varType == EkraVariableType.LED || varType == EkraVariableType.LED_TEST;
    }


    //nameV460
    public String getVarnamePostfixByTypeVariable(String panel){
        String textPanelAndRza = panel + ".rza";
        switch (getFilterType()){
            case LED:
                return textPanelAndRza + ".Led" + getLedNumber();
            case LED_CMD:
                return textPanelAndRza + ".Led.cmd";
            case LED_TEST:
                return textPanelAndRza + ".Led16";
            case TI:
                return textPanelAndRza + ".TI" + "." + getEkraAddress();
            case KEY_LOCAL: case KEY_VYVOD: case KEY_RABOTA: case KEY_OTHER:
                return textPanelAndRza + ".SA" + getEkraAddress();
            case SG:
                return textPanelAndRza + ".SG" + getEkraAddress();
            case CONNECT:
                return textPanelAndRza + ".Connect";
            case FAULT:
                return textPanelAndRza + ".Failure_rza";
            case SRAB:
                return textPanelAndRza + ".Srab_rza";
            case LAN:
                return textPanelAndRza + ".lan" + "." + getEkraAddress();
            case DS: case SRAB_OTKL:
                return textPanelAndRza + ".ds" + getEkraAddress();
            default:
                return textPanelAndRza + ".no_import";
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
            case LED: case LED_TEST:
                return "Горит/Погас_BM";
            case LED_CMD:
                return "ОС_Сигнал/1";
            case KEY_RABOTA: case KEY_OTHER: case SG:
                return "ОС_Введено_выведено/1_0";
            case KEY_VYVOD:
                return "ОС_Введено_выведено/0_1";
            case KEY_LOCAL:
                return "РЗА_Комплект_Вывод/Работа_BM";
            case FAULT:
                return "ПС1_Неисправность_норма/1_0";
            case SRAB:
                return "ПС1_Сигнал_норма/1_0";
            case SRAB_OTKL:
                return "АС_Сигнал_норма/1_0";
            case LAN:
                return "ПС2_Неисправность_норма/0_1";
            case DS:
                return "ПС2_Сигнал_норма/1_0";
            case CONNECT:
                return "Потеряна/Восстановлена_IV_GM3";
            default:
                return "";
        }
    }
    public String isMatrixActive(){
        return getMatrixByTypeVariable().isEmpty() ? "FALSE" : "TRUE";
    }

    String getTagnameByEkraVariableType(EkraVariableType ekraVariableType){
        switch (ekraVariableType){
            case LED_CMD:
                return "Квитировать светодиоды";
            case CONNECT:
                return "Состояние связи";
            default:
                return "Сигнал не определен";
        }
    }
    boolean isTagnameEmptyOrSlash(String tagname){
        return tagname.isEmpty() || tagname.equals("///");
    }

    @Override
    public String toString()
    {
        return "EkraVariable [mms=" + getMmsAddress() + ", type=" + getType() +
                ", ekraAddress=" + getEkraAddress() + ", tagname=" + getTagname() + "]";
    }

    public int compareTo(EkraVariable ekraVariable) {
        return this.getEkraAddressInt().compareTo(ekraVariable.getEkraAddressInt());
    }


}
