package app.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Daniel Karner
 * @version 1.0
 * @since 2019-10-30
 */

public class HL7Utils {
    /**
     * @param date Object of class LocalDate or LocalDateTime
     * @return Date/DateTime String in HL7 format, null in fail case
     */
    public static String toHL7Date(Object date) {
        // Check if date is of class LocalDate
        if (date.getClass() == LocalDate.class) {
            LocalDateTime ldt = (LocalDateTime) date;
            return String.format("%04d%02d%02d%02d%02d", ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(),
                    ldt.getHour(), ldt.getMinute());
        }
        // Check if date is of class LocalDateTime
        if (date.getClass() == LocalDateTime.class) {
            LocalDate ldt = (LocalDate) date;
            return String.format("%04d%02d%02d", ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth());
        }
        // Fail case
        return null;
    }

    /**
     * 
     * @param obj       Object to be made into String
     * @param delimiter Optional String for parsing 'HL7Type'
     * @return String representation of 'obj'
     */
    public static String stringify(Object obj, String... delimiter) {
        // Null check
        if (obj == null)
            return "";

        // Check for String
        if (obj.getClass() == String.class)
            return (String) obj;

        // Check for HL7Type (implements parse method)
        if (obj.getClass() == HL7Type.class && delimiter.length > 0 && delimiter[0] != null)
            return ((HL7Type) obj).parseToHL7(delimiter[0]);

        // Check for Date/DateTime (parsed by 'toHL7Date')
        if (obj.getClass() == LocalDateTime.class || obj.getClass() == LocalDate.class)
            return toHL7Date(obj);

        // In any other case, use valueOf
        return String.valueOf(obj);
    }

    /**
     * @param objs           Set of objects to be concat-stringyfied
     * @param repetitionChar Delimiter for repetitions ('~', '\n')
     * @param delimiter      Delimiter for parsing objects of type 'HL7Type'
     * @return String of Set formatted to fit HL7
     */
    public static String stringifyRepetitions(Set<Object> objs, String repetitionChar, String... delimiter) {
        String retSt = "";
        for (Object obj : objs) {
            if (delimiter.length > 0 && delimiter[0] != null)
                retSt += stringify(obj, delimiter[0]) + repetitionChar;
            else
                retSt += stringify(obj) + repetitionChar;
        }
        return retSt.replaceAll(delimiter + "$", "");
    }

    /**
     * @param data        String representation of object
     * @param targetClass Type to be converted to
     * @return Object of specified type or null in fail case
     */
    public static Object toObject(String data, Class<?> targetClass) {
        // Null check
        if (data == null || targetClass == null)
            return null;

        if (targetClass == String.class)
            return data;

        if (targetClass == Byte.class)
            return Byte.parseByte("0" + data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", ""));

        if (targetClass == Short.class)
            return Short.parseShort("0" + data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", ""));

        if (targetClass == Integer.class)
            return Integer.parseInt("0" + data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", ""));

        if (targetClass == Long.class)
            return Long.parseLong("0" + data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", ""));

        if (targetClass == Double.class)
            // replaceAll leaves only valid double format (max. one '-' in the front, one
            // '.' in the middle)
            return Double.parseDouble("0" + data.replaceAll(
                    // Dashes not followed by decimals (in case only dash is sent)
                    "\\-(?=\\D*$)|" +
                    // Any character before first decimal that is not a '-' directly infront
                            "^[^\\d\\-]*?(?=(\\-?\\d+))|" +
                            // Any non-decimals except '.' following a decimal (excluding first characters
                            // from above line)
                            "(?<=\\d)[^\\d\\.]+|" +
                            // Any dot followed by another dot that is - in turn - followed by a decimal
                            "\\.(?=([\\s\\S]*\\.(?=([\\s\\S]*?\\d))))|" +
                            // Any dot not followed by a decimal (removes last dot in case it would stand at
                            // the end of the number)
                            "\\.(?=\\D*$)",
                    ""));

        if (targetClass == Float.class)
            // replaceAll leaves only valid float format (max. one '-' in the front, one '.'
            // in the middle)
            return Double.parseDouble("0" + data.replaceAll(
                    // Dashes not followed by decimals (in case only dash s sent)
                    "\\-(?=\\D*$)|" +
                    // Any character before first decimal that is not a '-' directly infront
                            "^[^\\d\\-]*?(?=(\\-?\\d+))|" +
                            // Any non-decimals except '.' following a decimal (excluding first characters
                            // from above line)
                            "(?<=\\d)[^\\d\\.]+|" +
                            // Any dot followed by another dot that is - in turn - followed by a decimal
                            "\\.(?=([\\s\\S]*\\.(?=([\\s\\S]*?\\d))))|" +
                            // Any dot not followed by a decimal (removes last dot in case it would stand at
                            // the end of the number)
                            "\\.(?=\\D*$)",
                    ""));

        if (targetClass == Character.class)
            return data.charAt(0);

        if (targetClass == LocalDate.class){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(data.substring(0, 8), formatter);
        }

        if (targetClass == LocalDate.class) {
            data = data.replaceAll("\\D", "").replaceAll("(?<=\\d{8}).", "");
            if(data.length() < 8) 
                return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(data.substring(0, 8), formatter);
        }

        if (targetClass == LocalDate.class) {
            data = data.replaceAll("\\D", "").replaceAll("(?<=\\d{8}).", "");
            if (data.length() < 8)
                return null;
            DateTimeFormatter formatter;
            if (data.length() < 10)
                formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            else if (data.length() < 12)
                formatter = DateTimeFormatter.ofPattern("yyyyMMddhh");
            else if (data.length() > 14)
                formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
            else
                formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmm");
            
            return LocalDate.parse(data, formatter);
        }   
        // Fail case
        return null;
    }

    /**
     * @param HL7String    String to be tokenized
     * @param delimiter    Delimiter
     * @param targetLength Size of returned array
     * @return String array of specified size containing tokens an "" fillers
     */
    public static String[] tokenizeHL7(String HL7String, String delimiter, int targetLength) {
        String[] avTok = HL7String.replaceAll("^\\s+|\\s+$", "").split(Pattern.quote(delimiter));
        String[] tokens = new String[targetLength];
        System.arraycopy(avTok, 0, tokens, 0, avTok.length < targetLength ? avTok.length : targetLength);
        return tokens;
    }
}