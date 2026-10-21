package utils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 极简 JSON 序列化工具。教学项目够用，生产环境请换 Jackson / Gson。
 */
public class JsonUtils {

    public static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) return "\"" + escape((String) obj) + "\"";
        if (obj instanceof Number || obj instanceof Boolean) return obj.toString();
        if (obj instanceof LocalDate || obj instanceof LocalDateTime) return "\"" + obj + "\"";
        if (obj instanceof List) {
            StringBuilder sb = new StringBuilder("[");
            List<?> list = (List<?>) obj;
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(list.get(i)));
            }
            return sb.append("]").toString();
        }
        StringBuilder sb = new StringBuilder("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean first = true;
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object v = f.get(obj);
                if (v == null) continue;
                if (!first) sb.append(",");
                sb.append("\"").append(f.getName()).append("\":").append(toJson(v));
                first = false;
            } catch (IllegalAccessException ignored) {}
        }
        return sb.append("}").toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}