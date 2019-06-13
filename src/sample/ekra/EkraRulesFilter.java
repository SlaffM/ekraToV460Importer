package sample.ekra;

public class EkraRulesFilter {
    private EkraVariable ekraVariable;

    EkraRulesFilter(EkraVariable ekraVariable){
        this.ekraVariable = ekraVariable;
    }
    private EkraVariable getEkraVariable() {
        return ekraVariable;
    }

    EkraVariableType getTypeVariable(){
        if (isVariableNoUsing())                return EkraVariableType.NO_USE;
        else if (isVariableTS_led())            return EkraVariableType.LED;
        else if (isVariableTU_led_cmd())        return EkraVariableType.LED_CMD;
        else if (isVariableTU_led_test())       return EkraVariableType.LED_TEST;
        else if (isVariableTI())                return EkraVariableType.NO_USE;
        else if (isVariableTS_key_local())      return EkraVariableType.KEY_LOCAL;
        else if (isVariableTS_key_vyvod())      return EkraVariableType.KEY_VYVOD;
        else if (isVariableTS_key_rabota())     return EkraVariableType.KEY_RABOTA;
        else if (isVariableTS_key_other())      return EkraVariableType.KEY_OTHER;
        else if (isVariableTS_sg())             return EkraVariableType.SG;
        else if (isVariableTS_connect())        return EkraVariableType.CONNECT;
        else if (isvariableTS_fault())          return EkraVariableType.FAULT;
        else if (isVariableTS_srab())           return EkraVariableType.SRAB;
        else if (isVariableTS_srabOtkl())       return EkraVariableType.SRAB_OTKL;
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
    private boolean isVariableTS_srab(){
        return isVariableTS() && getEkraVariable().getMmsAddress().endsWith("CALH1/GrWrn/stVal[ST]");
    }
    private boolean isVariableTS_srabOtkl(){
        return isVariableTS() && getEkraVariable().getTagname().contains("Откл");
    }
    private boolean isvariableTS_fault(){
        return isVariableTS() && getEkraVariable().getMmsAddress().endsWith("CALH1/GrAlm/stVal[ST]");
    }
    private boolean isVariableTS_led(){
        return isVariableTS() && getEkraVariable().getMmsAddress().contains("ledGG");
    }
    private boolean isVariableTU_led_cmd(){
        return getEkraVariable().getMmsAddress().endsWith("LLN0/LEDRs/Oper.ctlVal[CO]");
    }
    private boolean isVariableTU_led_test(){
        return getEkraVariable().getEkraAddress().equals("480");
    }
    private boolean isVariableTS_connect(){
        return getEkraVariable().getMmsAddress().endsWith("CALH1/Health/stVal[ST]");
    }
    private boolean isVariableTS_lan(){
        return (getEkraVariable().getMmsAddress().endsWith("LCCH1/ChLiv/stVal[ST]") || getEkraVariable().getMmsAddress().endsWith("LCCH1/RedChLiv/stVal[ST]")) &&
                (getEkraVariable().getEkraAddress().equals("214") || getEkraVariable().getEkraAddress().equals("215"));
    }
    private boolean isVariableTS_key_vyvod(){
        return getEkraVariable().getTagname().contains("Вывод") ||
                getEkraVariable().getTagname().contains("Выв");
    }
    private boolean isVariableTS_key_rabota(){
        return (getEkraVariable().getTagname().contains("SA") &&
                (getEkraVariable().getTagname().contains("Контроль") ||
                        getEkraVariable().getTagname().contains("Работа") ||
                        getEkraVariable().getTagname().contains("работе") ||
                        getEkraVariable().getTagname().contains("работа")));
    }
    private boolean isVariableTS_key_local(){
        return getEkraVariable().getMmsAddress().endsWith("/LLN0/Mod/stVal[ST]");
    }
    private boolean isVariableTS_key_other(){
        return  getEkraVariable().getTagname().contains("SA");
    }
    private boolean isVariableTS_sg(){
        return (getEkraVariable().getTagname().contains("SG"));
    }
    private boolean isVariableTI(){
        return  (getEkraVariable().getType().equals("FLOAT32") ||
                getEkraVariable().getMmsAddress().endsWith("/cVal.mag.f[MX]")) && !getEkraVariable().getEkraAddress().isEmpty();
    }
    protected boolean isSrcTagContainsSlashOrEmpty(){
        return getEkraVariable().getSrcEkraAddressAndTag().equals("///") || getEkraVariable().getSrcEkraAddressAndTag().isEmpty() ||
                getEkraVariable().getSrcEkraAddressAndTag().startsWith("///{");
    }
    private boolean isVariableNoUsing(){
        return  //getEkraVariable().getTagname().startsWith("ПО") ||
                getEkraVariable().getTagname().startsWith("Электронный ключ") ||
                getEkraVariable().getTagname().contains("SET_D") ||
                //getEkraVariable().getTagname().startsWith("Вход №") ||
                //getEkraVariable().getTagname().startsWith("Вход N") ||
                //getEkraVariable().getTagname().startsWith("Выход") ||
                //getEkraVariable().getTagname().startsWith("ИО") ||
                getEkraVariable().getTagname().contains("вторичная величина") ||
                getEkraVariable().getTagname().contains("VIRT") ||
                //getEkraVariable().getTagname().contains("Реле") ||
                (getEkraVariable().getTagname().contains("GOOSEIN") || getEkraVariable().getTagname().contains("GOOSEOUT"));
    }

}
