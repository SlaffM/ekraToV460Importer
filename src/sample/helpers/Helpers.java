package sample.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static String getTextWithPattern(String text, String pattern){

        if (text == null) {return "";}

        Pattern p = Pattern.compile(pattern, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = p.matcher(text);

        if(matcher.find()){
            return matcher.group(1);
        }else{
            return "";
        }
    }

    public static boolean tryParseInt(String value){
        try{
            String num = Helpers.getTextWithPattern(value, "(\\d+)");
            if (num.isEmpty()) { return false;}
            Integer.parseInt(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }



}
