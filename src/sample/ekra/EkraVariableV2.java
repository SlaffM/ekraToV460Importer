package sample.ekra;

public class EkraVariableV2 extends EkraVariable {

    public EkraVariableV2(String mmsAddress, String type, String ekraAddress, String tagname, String srcEkraAddressAndTag) {
        super(mmsAddress, type, ekraAddress, tagname, srcEkraAddressAndTag);
    }

    //Matrix
    @Override
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
            case SRAB: case SRAB_OTKL:
                return "АС_Сигнал_норма/1_0";
            case LAN:
                return "ПС2_Неисправность_норма/0_1";
            case DS:
                return "ПС2_Сигнал_норма/1_0";
            case CONNECT: default:
                return "";
        }
    }

    @Override
    public String getFormattedTagnameForV460() {
        String formattedTag = getTagname().trim();
        if(isTagnameEmptyOrSlash(formattedTag))     formattedTag = getTagnameByEkraVariableType(getFilterType());
        if(isLengthTagnameNotValid(formattedTag))   formattedTag = tryGetShortTagname(formattedTag);
        if(isTagnameLed(getFilterType()))           formattedTag = "Светодиод: " + formattedTag;
        return formattedTag;
    }

}
