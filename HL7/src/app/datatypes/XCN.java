package app.datatypes;

import java.time.LocalDate;
import java.util.regex.Pattern;

import app.utils.HL7Type;
import app.utils.HL7Utils;

/**
 * Java representation of datatype "XCN" from HL7 protocol. Visit <a href=
 * "http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7+v2.5.1&dataType=XCN">HL7
 * documentation</a> for more info.
 * 
 * @author Thomas Biedermann
 * @author Daniel Karner
 * @version 1.1
 * @since 2019-10-27
 */

public class XCN extends HL7Type
{
    private String idNumber;
    private String familyName;
    private String givenName;
    private String secondAndFurtherGivenNamesOrInitialsThereof;
    private String suffixegJrOrIii;
    private String prefixegDr;
    private String degreeegMd;
    private String sourceTable;
    private String assigningAuthority;
    private String nameTypeCode;
    private String identifierCheckDigit;
    private String checkDigitScheme;
    private String identifierTypeCode;
    private String assigningFacility;
    private String nameRepresentationCode;
    private String nameContext;
    private Object nameValidityRange;
    private String nameAssemblyOrder;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private String professionalSuffix;
    private String assigningJurisdiction;
    private String assigningAgencyOrDepartment;

    /**
     * @param idNumber
     * @param familyName
     * @param givenName
     * @param secondAndFurtherGivenNamesOrInitialsThereof
     * @param suffixegJrOrIii
     * @param prefixegDr
     * @param degreeegMd
     * @param sourceTable
     * @param assigningAuthority
     * @param nameTypeCode
     * @param identifierCheckDigit
     * @param checkDigitScheme
     * @param identifierTypeCode
     * @param assigningFacility
     * @param nameRepresentationCode
     * @param nameContext
     * @param nameValidityRange
     * @param nameAssemblyOrder
     * @param effectiveDate
     * @param expirationDate
     * @param professionalSuffix
     * @param assigningJurisdiction
     * @param assigningAgencyOrDepartment
     * @return XCN instance
     */

     /**
     * @param HL7String Part of a HL7 Message to be formatted to Java
     * @param delimiter Type-specific delimiter (e.g. '|', '^')
     */
    public XCN(String HL7String, String delimiter) {
        super(HL7String, delimiter);
    }

    @Override
    public void setParamsToHL7(String HL7String, String delimiter) {
        String[] tokens = HL7Utils.tokenizeHL7(HL7String, delimiter, 23);
        this.idNumber = tokens[0];
        this.familyName = tokens[1];
        this.givenName = tokens[2];
        this.secondAndFurtherGivenNamesOrInitialsThereof = tokens[3];
        this.suffixegJrOrIii = tokens[4];
        this.prefixegDr = tokens[5];
        this.degreeegMd = tokens[6];
        this.sourceTable = tokens[7];
        this.assigningAuthority = tokens[8];
        this.nameTypeCode = tokens[9];
        this.identifierCheckDigit = tokens[10];
        this.checkDigitScheme = tokens[11];
        this.identifierTypeCode = tokens[12];
        this.assigningFacility = tokens[13];
        this.nameRepresentationCode = tokens[14];
        this.nameContext = tokens[15];
        this.nameValidityRange = HL7Utils.toObject(tokens[16], Object.class);
        this.nameAssemblyOrder = tokens[17];
        this.effectiveDate = (LocalDate) HL7Utils.toObject(tokens[18], LocalDate.class);
        this.expirationDate = (LocalDate) HL7Utils.toObject(tokens[19], LocalDate.class);
        this.professionalSuffix = tokens[20];
        this.assigningJurisdiction = tokens[21];
        this.assigningAgencyOrDepartment = tokens[22];
    }

    @Override
    public String parseToHL7(String delim) {
        return String.format(
                // Insert one value after another with delim (val 24) inbetween
                "%1$s%24$s%2$s%24$s%3$s%24$s%4$s%24$s%5$s%24$s%6$s%24$s%7$s%24$s%8$s%24$s%9$s%24$s%10$s%24$s%11$s%24$s%12$s%24$s%13$s%24$s%14$s%24$s%15$s%24$s%16$s%24$s%17$s%24$s%18$s%24$s%19$s%24$s%20$s%24$s%21$s%24$s%22$s%24$s%23$s%24$s",
                HL7Utils.stringify(this.idNumber),
                HL7Utils.stringify(this.familyName),
                HL7Utils.stringify(this.givenName),
                HL7Utils.stringify(this.secondAndFurtherGivenNamesOrInitialsThereof),
                HL7Utils.stringify(this.suffixegJrOrIii),
                HL7Utils.stringify(this.prefixegDr),
                HL7Utils.stringify(this.degreeegMd),
                HL7Utils.stringify(this.sourceTable),
                HL7Utils.stringify(this.assigningAuthority),
                HL7Utils.stringify(this.nameTypeCode),
                HL7Utils.stringify(this.identifierCheckDigit),
                HL7Utils.stringify(this.checkDigitScheme),
                HL7Utils.stringify(this.identifierTypeCode),
                HL7Utils.stringify(this.assigningFacility),
                HL7Utils.stringify(this.nameRepresentationCode),
                HL7Utils.stringify(this.nameContext),
                HL7Utils.stringify(this.nameValidityRange),
                HL7Utils.stringify(this.nameAssemblyOrder),
                HL7Utils.stringify(this.effectiveDate),
                HL7Utils.stringify(this.expirationDate),
                HL7Utils.stringify(this.professionalSuffix),
                HL7Utils.stringify(this.assigningJurisdiction),
                HL7Utils.stringify(this.assigningAgencyOrDepartment),
                HL7Utils.stringify(delim)
                ).replaceAll(Pattern.quote(delim) + "+$", "");
    }

    @Override
    public String toString() {
        return parseToHL7("^");
    }
}