package sample.ekra;

public class EkraRulesFilter {
    private EkraVariable ekraVariable;

    EkraRulesFilter(EkraVariable ekraVariable){
        this.ekraVariable = ekraVariable;
    }
    public EkraVariable getEkraVariable() {
        return ekraVariable;
    }

    public EkraVariableType getTypeVariable(){
        if (isVariableNoUsing())                return EkraVariableType.NO_USE;
        else if (isVariableTS_led())            return EkraVariableType.LED;
        else if (isVariableTI())                return EkraVariableType.NO_USE;
        else if (isVariableTS_key())            return EkraVariableType.KEY;
        else if (isVariableTS_connect())        return EkraVariableType.CONNECT;
        else if (isvariableTS_fault())          return EkraVariableType.FAULT;
        else if (isvariableTS_srab())           return EkraVariableType.SRAB;
        else if (isVariableTU_ledCmd())         return EkraVariableType.LED_CMD;
        else if (isVariableTS_lan())            return EkraVariableType.LAN;
        else if (isVariableTS_ds())             return EkraVariableType.DS;
        else                                    return EkraVariableType.NO_USE;
    }

    private boolean isVariableTS(){
        return getEkraVariable().getType().equals("BOOLEAN");
    }
    private boolean isVariableTS_ds(){
        return isVariableTS() && !getEkraVariable().getEkraAddress().isEmpty();
    }
    private boolean isvariableTS_srab(){
        return isVariableTS() && getEkraVariable().getMmsAddress().endsWith("CALH1/GrWrn/stVal[ST]");
    }
    private boolean isvariableTS_fault(){
        return isVariableTS() && getEkraVariable().getMmsAddress().endsWith("CALH1/GrAlm/stVal[ST]");
    }
    private boolean isVariableTS_led(){
        return isVariableTS() && getEkraVariable().getMmsAddress().contains("ledGG");
    }
    private boolean isVariableTU_ledCmd(){
        return getEkraVariable().getMmsAddress().endsWith("LLN0/LEDRs/Oper.ctlVal[CO]");
    }
    private boolean isVariableTS_connect(){
        return getEkraVariable().getMmsAddress().endsWith("CALH1/Health/stVal[ST]");
    }
    private boolean isVariableTS_lan(){
        return (getEkraVariable().getMmsAddress().endsWith("LCCH1/ChLiv/stVal[ST]") || getEkraVariable().getMmsAddress().endsWith("LCCH1/RedChLiv/stVal[ST]")) &&
                (getEkraVariable().getEkraAddress().equals("214") || getEkraVariable().getEkraAddress().equals("215"));
    }
    private boolean isVariableTS_key(){
        return getEkraVariable().getMmsAddress().endsWith("/LLN0/Mod/stVal[ST]") ||
                (getEkraVariable().getTagname().contains("SA") &&
                        (getEkraVariable().getTagname().contains("Вывод") || getEkraVariable().getTagname().contains("Работа")));
    }
    private boolean isVariableTI(){
        return  (getEkraVariable().getType().equals("FLOAT32") ||
                getEkraVariable().getMmsAddress().endsWith("/cVal.mag.f[MX]")) && !getEkraVariable().getEkraAddress().isEmpty();
    }
    private boolean isSrcTagContainsSlashOrEmpty(){
        return getEkraVariable().getSrcEkraAddressAndTag().equals("///") || getEkraVariable().getSrcEkraAddressAndTag().isEmpty() ||
                getEkraVariable().getSrcEkraAddressAndTag().startsWith("///{");
    }
    private boolean isVariableNoUsing(){
        return  getEkraVariable().getTagname().contains("Реле") ||
                getEkraVariable().getTagname().startsWith("ПО") ||
                getEkraVariable().getTagname().startsWith("Электронный ключ") ||
                getEkraVariable().getTagname().contains("SET_D") ||
                getEkraVariable().getTagname().startsWith("Вход №") ||
                getEkraVariable().getTagname().startsWith("Вход N") ||
                getEkraVariable().getTagname().startsWith("ИО") ||
                getEkraVariable().getTagname().contains("вторичная величина") ||
                getEkraVariable().getTagname().contains("VIRT") ||
                (getEkraVariable().getTagname().contains("GOOSEIN") || getEkraVariable().getTagname().contains("GOOSEOUT"));
    }

}
