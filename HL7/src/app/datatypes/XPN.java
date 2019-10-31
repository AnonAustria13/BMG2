package app.datatypes;

import java.time.LocalDate;
import java.util.regex.Pattern;

import app.utils.HL7Type;
import app.utils.HL7Utils;

/**
 * Java representation of datatype "XPN" from HL7 protocol. Visit <a href=
 * "http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7+v2.5.1&dataType=XPN">HL7
 * documentation</a> for more info.
 * 
 * @author Thomas Biedermann
 * @author Daniel Karner
 * @version 1.1
 * @since 2019-10-27
 */

public class XPN extends HL7Type
{
    private String familyName;
    private String givenName;
    private String secondAndFurtherGivenNamesOrInitialsThereof;
    private String suffixegJrOrIii;
    private String prefixegDr;
    private String degreeegMd;
    private String nameTypeCode;
    private String nameRepresentationCode;
    private String nameContext;
    private Object nameValidityRange;
    private String nameAssemblyOrder;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private String professionalSuffix;

    /**
     * @param HL7String Part of a HL7 Message to be formatted to Java
     * @param delimiter Type-specific delimiter (e.g. '|', '^')
     */
    public XPN(String HL7String, String delimiter) {
        super(HL7String, delimiter);
    }

    @Override
    public void setParamsToHL7(String HL7String, String delimiter) {
        String[] tokens = HL7Utils.tokenizeHL7(HL7String, delimiter, 14);
        this.familyName = tokens[0];
        this.givenName = tokens[1];
        this.secondAndFurtherGivenNamesOrInitialsThereof = tokens[2];
        this.suffixegJrOrIii = tokens[3];
        this.prefixegDr = tokens[4];
        this.degreeegMd = tokens[5];
        this.nameTypeCode = tokens[6];
        this.nameRepresentationCode = tokens[7];
        this.nameContext = tokens[8];
        this.nameValidityRange = HL7Utils.toObject(tokens[9], Object.class);
        this.nameAssemblyOrder = tokens[10];
        this.effectiveDate = (LocalDate) HL7Utils.toObject(tokens[11], LocalDate.class);
        this.expirationDate = (LocalDate) HL7Utils.toObject(tokens[12], LocalDate.class);
        this.professionalSuffix = tokens[13];

    }

    @Override
    public String parseToHL7(String delim) {
        return String.format(
                // Insert one value after another with delim (val 15) inbetween
                "%1$s%15$s%2$s%15$s%3$s%15$s%4$s%15$s%5$s%15$s%6$s%15$s%7$s%15$s%8$s%15$s%9$s%15$s%10$s%15$s%11$s%15$s%12$s%15$s%13$s%15$s%14$s%15$s",
                HL7Utils.stringify(this.familyName),
                HL7Utils.stringify(this.givenName),
                HL7Utils.stringify(this.secondAndFurtherGivenNamesOrInitialsThereof),
                HL7Utils.stringify(this.suffixegJrOrIii),
                HL7Utils.stringify(this.prefixegDr),
                HL7Utils.stringify(this.degreeegMd),
                HL7Utils.stringify(this.nameTypeCode),
                HL7Utils.stringify(this.nameRepresentationCode),
                HL7Utils.stringify(this.nameContext),
                HL7Utils.stringify(this.nameValidityRange),
                HL7Utils.stringify(this.nameAssemblyOrder),
                HL7Utils.stringify(this.effectiveDate),
                HL7Utils.stringify(this.expirationDate),
                HL7Utils.stringify(this.professionalSuffix),
                HL7Utils.stringify(delim)
                ).replaceAll(Pattern.quote(delim) + "+$", "");
    }

    @Override
    public String toString() {
        return parseToHL7("^");
    }
}