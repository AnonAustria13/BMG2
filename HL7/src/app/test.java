package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class test {
    public static void main(String [] args){
        String s = "2019023";
        s = s.replaceAll("\\D", "").replaceAll("(?<=\\d{8}).", "");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(s, formatter);
        System.out.println(date);
    }
}