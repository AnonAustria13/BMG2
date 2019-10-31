package app.datatypes;

import java.time.LocalDate;
import java.util.regex.Pattern;

import app.utils.*;

/**
 * Java representation of datatype "XAD" from HL7 protocol. Visit <a href=
 * "http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7+v2.5.1&dataType=XAD">HL7
 * documentation</a> for more info.
 * 
 * @author Thomas Biedermann
 * @author Daniel Karner
 * @version 1.2
 * @since 2019-10-27
 */

public class XAD extends HL7Type {
    private String streetAddress;
    private String otherDesignation;
    private String city;
    private String stateOrProvince;
    private String zipOrPostalCode;
    private String country;
    private String addressType;
    private String otherGeographicDesignation;
    private String countyParishCode;
    private String censusTract;
    private String addressRepresentationCode;
    private Object addressValidityRange;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;

    /**
     * @param HL7String Part of a HL7 Message to be formatted to Java
     * @param delimiter Type-specific delimiter (e.g. '|', '^')
     */
    public XAD(String HL7String, String delimiter) {
        super(HL7String, delimiter);
    }

    @Override
    public void setParamsToHL7(String HL7String, String delimiter) {
        String[] tokens = HL7Utils.tokenizeHL7(HL7String, delimiter, 14);
        this.streetAddress = tokens[0];
        this.otherDesignation = tokens[1];
        this.city = tokens[2];
        this.stateOrProvince = tokens[3];
        this.zipOrPostalCode = tokens[4];
        this.country = tokens[5];
        this.addressType = tokens[6];
        this.otherGeographicDesignation = tokens[7];
        this.countyParishCode = tokens[8];
        this.censusTract = tokens[9];
        this.addressRepresentationCode = tokens[10];
        this.addressValidityRange = HL7Utils.toObject(tokens[11], Object.class);
        this.effectiveDate = (LocalDate) HL7Utils.toObject(tokens[12], LocalDate.class);
        this.expirationDate = (LocalDate) HL7Utils.toObject(tokens[13], LocalDate.class);
    }

    @Override
    public String parseToHL7(String delim) {
        return String.format(
                // Insert one value after another with delim (val 15) inbetween
                "%1$s%15$s%2$s%15$s%3$s%15$s%4$s%15$s%5$s%15$s%6$s%15$s%7$s%15$s%8$s%15$s%9$s%15$s%10$s%15$s%11$s%15$s%12$s%15$s%13$s%15$s%14$s",
                HL7Utils.stringify(this.streetAddress), HL7Utils.stringify(this.otherDesignation),
                HL7Utils.stringify(this.city), HL7Utils.stringify(this.stateOrProvince),
                HL7Utils.stringify(this.zipOrPostalCode), HL7Utils.stringify(this.country),
                HL7Utils.stringify(this.addressType), HL7Utils.stringify(this.otherGeographicDesignation),
                HL7Utils.stringify(this.countyParishCode), HL7Utils.stringify(this.censusTract),
                HL7Utils.stringify(this.addressRepresentationCode), HL7Utils.stringify(this.addressValidityRange),
                HL7Utils.stringify(this.effectiveDate), HL7Utils.stringify(this.expirationDate),
                HL7Utils.stringify(delim)).replaceAll(Pattern.quote(delim) + "+$", "");

    }

    @Override
    public String toString() {
        return parseToHL7("^");
    }
}