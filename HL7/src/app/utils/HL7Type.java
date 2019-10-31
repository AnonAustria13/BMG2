package app.utils;

/**
 * @author Daniel Karner
 * @version 1.0
 * @since 2019-10-30
 */

public abstract class HL7Type 
{

    /**
     * @param HL7String Type (message, segment, datatype) in HL7 format
     * @param delimiter Delimiter for specific type ('|', '^', ...)
     */
    public abstract void setParamsToHL7(String HL7String, String delimiter);

    /**
     * @param HL7String Type (message, segment, datatype) in HL7 format
     * @param delimiter Delimiter for specific type ('|', '^', ...)
     */
    public HL7Type(String HL7String, String delimiter) 
    {
        setParamsToHL7(HL7String, delimiter);
    }

    /**
     * @param delim Delimiter for specific parse back (e.g. '|', '^')
     * @return HL7 String for specific layer
     */
    public abstract String parseToHL7(String delim);

    public abstract String toString();
}