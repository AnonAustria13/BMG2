package app.datatypes;

import java.util.regex.Pattern;

import app.utils.HL7Type;
import app.utils.HL7Utils;

/**
 * Java representation of datatype "XTN" from HL7 protocol. Visit <a href=
 * "http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7+v2.5.1&dataType=XTN">HL7
 * documentation</a> for more info.
 * 
 * @author Thomas Biedermann
 * @author Daniel Karner
 * @version 1.1
 * @since 2019-10-27
 */

public class XTN extends HL7Type
{
    private String telephoneNumber;
    private String telecommunicationUseCode;
    private String telecommunicationEquipmentType;
    private String emailAddress;
    private double countryCode;
    private double areaCityCode;
    private double localNumber;
    private double extension;
    private String anyText;
    private String extensionPrefix;
    private String speedDialCode;
    private String unformattedTelephoneNumber;

    /**
     * @param HL7String Part of a HL7 Message to be formatted to Java
     * @param delimiter Type-specific delimiter (e.g. '|', '^')
     */
    public XTN(String HL7String, String delimiter) {
        super(HL7String, delimiter);
    }

    @Override
    public void setParamsToHL7(String HL7String, String delimiter) {
        String[] tokens = HL7Utils.tokenizeHL7(HL7String, delimiter, 12);
        this.telephoneNumber = tokens[0];
        this.telecommunicationUseCode = tokens[1];
        this.telecommunicationEquipmentType = tokens[2];
        this.emailAddress = tokens[3];
        this.countryCode = (double) HL7Utils.toObject(tokens[4], Double.class);
        this.areaCityCode = (double) HL7Utils.toObject(tokens[5], Double.class);
        this.localNumber = (double) HL7Utils.toObject(tokens[6], Double.class);
        this.extension = (double) HL7Utils.toObject(tokens[7], Double.class);
        this.anyText = tokens[8];
        this.extensionPrefix = tokens[9];
        this.speedDialCode = tokens[10];
        this.unformattedTelephoneNumber = tokens[11];
    }

    @Override
    public String parseToHL7(String delim) {
        return String.format(
                // Insert one value after another with delim (val 13) inbetween
                "%1$s%13$s%2$s%13$s%3$s%13$s%4$s%13$s%5$s%13$s%6$s%13$s%7$s%13$s%8$s%13$s%9$s%13$s%10$s%13$s%11$s%13$s%12$s%13$s",
                HL7Utils.stringify(this.telephoneNumber),
                HL7Utils.stringify(this.telecommunicationUseCode),
                HL7Utils.stringify(this.telecommunicationEquipmentType),
                HL7Utils.stringify(this.emailAddress),
                HL7Utils.stringify(this.countryCode),
                HL7Utils.stringify(this.areaCityCode),
                HL7Utils.stringify(this.localNumber),
                HL7Utils.stringify(this.extension),
                HL7Utils.stringify(this.anyText),
                HL7Utils.stringify(this.extensionPrefix),
                HL7Utils.stringify(this.speedDialCode),
                HL7Utils.stringify(this.unformattedTelephoneNumber),
                HL7Utils.stringify(delim))
                .replaceAll(Pattern.quote(delim) + "+$", "");
    }

    @Override
    public String toString() {
        return parseToHL7("^");
    }
}