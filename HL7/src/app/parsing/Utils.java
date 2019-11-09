package app.parsing;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import app.hl7_types.datatype.XAD;
import app.hl7_types.segment.*;

/**
 * @author Daniel Karner
 */
public class Utils 
{
	/**
	 * @param HL7String
	 * @return DCO with encoding characters
	 */
	public static EncodingCharacters getEncodingCharacters(String HL7String) 
	{
		String[] tokens = tokenizeHL7(HL7String, "|", "\\", 4);
		return new EncodingCharacters("|", tokens[0], tokens[1], tokens[2], tokens[3]);
	}

	/**
	 * @param data
	 * @param delimiter
	 * @param escapeCharacter
	 * @param targetLength
	 * @return Array of tokens, comparable to String.split()
	 */
	public static String[] tokenizeHL7(String data, String delimiter, String escapeCharacter, int targetLength) 
	{
		String[] avTok = data.replaceAll("^\\s+|\\s+$", "").split("(?<!" + Pattern.quote(escapeCharacter) + ")" + Pattern.quote(delimiter));
		String[] tokens = new String[targetLength];
		System.arraycopy(avTok, 0, tokens, 0, avTok.length < targetLength ? avTok.length : targetLength);
		return tokens;
	}

	/**
	 * @param messageLine
	 * @param targetId
	 * @param encodingCharacters
	 * @return Object representation of HL7String
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object objectFromMessageLine(String messageLine, String targetId, EncodingCharacters... encodingCharacters) 
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
	{
		if (messageLine == null || messageLine.length() < 3)
			return null;
		String segmentId = messageLine.substring(0, 3);
		if (!segmentId.contentEquals(targetId))
			return null;
		Object object;
		if (segmentIdMap.containsKey(segmentId)) 
		{
			Constructor<?> cont = segmentIdMap.get(segmentId).getConstructor(String.class, EncodingCharacters.class);
			cont.setAccessible(true);
			object = cont.newInstance(new Object[] { messageLine,
					encodingCharacters != null && encodingCharacters.length > 0 ? encodingCharacters[0]
							: new EncodingCharacters() });
		} else 
		{
			object = messageLine;
		}
		return object;
	}

	/**
	 * @param segmentStrings
	 * @param startingIndex
	 * @param targetId
	 * @return Set of Objects from MessageLine
	 * @see app.parsing.Utils.objectFromMessageLine
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Set<Object> allSegmentsFromGroup(String[] segmentStrings, int startingIndex, String targetId)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException 
	{
		if (segmentStrings == null || segmentStrings.length <= startingIndex
				|| segmentStrings[startingIndex].length() < 3)
			return new HashSet<>();
		String segmentId = segmentStrings[startingIndex].substring(0, 3);
		if (!segmentId.contentEquals(targetId))
			return new HashSet<>();
		Set<Object> segments = new HashSet<>();
		while (segmentStrings.length > startingIndex && segmentStrings[startingIndex].length() >= 3
				&& segmentStrings[startingIndex].substring(0, 3).equals(segmentId)) {
			segments.add(objectFromMessageLine(segmentStrings[startingIndex], targetId));
			startingIndex++;
		}
		return segments;
	}
	
	public static final Map<String, Class<?>> segmentIdMap = new HashMap<String, Class<?>>() 
	{
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		{
			put("MSH", MSH.class);
			put("DG1", DG1.class);
			put("EVN", EVN.class);
			put("NK1", NK1.class);
			put("PID", PID.class);
			put("PV1", PV1.class);
		}
	};

	public static Object toObject(String data, Class<?> targetClass, EncodingCharacters... encodingCharacters){
		if (targetClass == null)
		{
			return null;
		}

		if (data == null) {
			// Check for primitives
			if (targetClass == Short.class)
				return (short) 0;
			if (targetClass == Byte.class)
				return (byte) 0;
			if (targetClass == Integer.class)
				return (int) 0;
			if (targetClass == Long.class)
				return (long) 0;
			if (targetClass == Double.class)
				return (double) 0;
			if (targetClass == Float.class)
				return (float) 0;
			if (targetClass == Character.class)
				return (char) 0;
			// Else, return null
			return null;
		}

		if (targetClass == String.class)
			return data;

		if (targetClass == Byte.class)
			return Byte.parseByte(data.matches("[\\s\\S]*\\d[\\s\\S]*")
					? data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", "")
					: "0");

		if (targetClass == Short.class)
			return Short.parseShort(data.matches("[\\s\\S]*\\d[\\s\\S]*")
					? data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", "")
					: "0");

		if (targetClass == Integer.class)
			return Integer.parseInt(data.matches("[\\s\\S]*\\d[\\s\\S]*")
					? data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", "")
					: "0");

		if (targetClass == Long.class) {
			return Long.parseLong(data.matches("[\\s\\S]*\\d[\\s\\S]*")
					? data.replaceAll("^[^\\d\\-]*?(?=(\\-?\\d+))|(?<=\\d)\\D+", "")
					: "0");
		}

		if (targetClass == Double.class)
			// replaceAll leaves only valid double format (max. one '-' in the front, one
			// '.' in the middle)
			return Double.parseDouble(data.matches("[\\s\\S]*\\d[\\s\\S]*") ? data.replaceAll(
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
					"") : "0");

		if (targetClass == Float.class)
			// replaceAll leaves only valid float format (max. one '-' in the front, one '.'
			// in the middle)
			return Double.parseDouble(data.matches("[\\s\\S]*\\d[\\s\\S]*") ? data.replaceAll(
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
					"") : "0");

		if (targetClass == Character.class)
			return data.charAt(0);

		if (targetClass == LocalDate.class) {
			data = data.replaceAll("\\D", "").replaceAll("(?<=\\d{8}).", "");
			if (data.length() < 8)
				return null;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			return LocalDate.parse(data.substring(0, 8), formatter);
		}

		if (targetClass == LocalDateTime.class) {
			data = data.replaceAll("\\D", "").replaceAll("(?<=\\d{8}).", "");
			if (data.length() < 8)
				return null;
			DateTimeFormatter formatter;
			if (data.length() < 10) {
				formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
				data += "00";
			} else if (data.length() < 12)
				formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
			else if (data.length() > 14)
				formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			else
				formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

			return LocalDateTime.parse(data, formatter);
		}

		if (targetClass == XAD.class) {
			if (encodingCharacters == null || encodingCharacters.length < 1)
				return new XAD(data, new EncodingCharacters());
			return new XAD(data, encodingCharacters[0]);
		}
		return null;
	}

	
	public static Object toObject(String data, Field target, EncodingCharacters encodingCharacters, String... setDelim) 
	{
		if (target == null)
			return null;

		target.setAccessible(true);
		Class<?> targetClass = target.getClass();	

		if (targetClass == Set.class)
		{
			if(setDelim == null || setDelim.length < 1) 
				return null;
			ParameterizedType setType = (ParameterizedType) target.getGenericType();
			Class<?> generic = (Class<?>) setType.getActualTypeArguments()[0];
			return toSet(data, generic, setDelim[0], encodingCharacters);
		}
		return toObject(data, targetClass, encodingCharacters);
	}

	/**
	 * @param data
	 * @param targetGeneric
	 * @param delimiter
	 * @param encodingCharacters
	 * @return Multiple Objects seperated by delimiter (~)
	 * @see app.parsing.Utils.toObject
	 */
	public static Set<Object> toSet(String data, Class<?> targetGeneric, String delimiter,
			EncodingCharacters... encodingCharacters) 
	{
		if (data == null)
			return new HashSet<>();
		String[] splitString = data.split(Pattern.quote(delimiter));
		Set<Object> returnSet = new HashSet<>();
		EncodingCharacters ec = null;
		if (encodingCharacters != null && encodingCharacters.length > 0)
			ec = encodingCharacters[0];
		for (String st : splitString) 
			returnSet.add(toObject(st, targetGeneric, ec));
		return returnSet;
	}

}
