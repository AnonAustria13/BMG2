package app.hl7_parser;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * XmlSerializer
 * @author Thomas Biedermann
 */
public class XmlSerializer {
    public static String fileOut = "";

    public static String parse(Object o){
        String out = "";
        out += XmlSerializer.serialize(o);
        fileOut = out;
        return out;
    }

    private static String serialize(Object object) {
        String out = "";
        
        Class<?> obj = object.getClass();

        if (obj.isAnnotationPresent(XmlSerializeable.class)) {
            Field[] fs = obj.getDeclaredFields();

            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                if (f.isAnnotationPresent(XmlField.class)) {
                    f.setAccessible(true);

                    try {
                        out += XmlSerializer.retriveValue(f, object);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    f.setAccessible(false);
                }
            }
        }
        obj = null;
        return out;
    }

    private static String retriveValue(Field field, Object o0) throws IllegalArgumentException, IllegalAccessException, NullPointerException {

        String out = "";
        Type t = field.getType();
        Object o1 = field.get(o0);

        field.setAccessible(true);
        if(o1 != null){
            if (t.equals(Set.class)) {
                Set<?> s = (Set<?>) o1;
                
                for(Object o2 : s){
                    if (o2 != null) {
                        out += XmlSerializer.serialize(o2);
                    }
                }
                
            } else if(!o1.getClass().getName().startsWith("java.")){
                out += XmlSerializer.serialize(o1);

            } else {
                out += String.format("Name: %s, Value: %s\n", field.getName(), o1);
            }
        }
        return out;
    }
}