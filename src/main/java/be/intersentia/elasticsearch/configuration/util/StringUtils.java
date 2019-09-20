package be.intersentia.elasticsearch.configuration.util;

import org.apache.commons.collections4.MapUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringUtils {

    public static <T> String join(Collection<T> list, Function<? super T, String> mapper, String delimiter) {
        return list.stream().map(mapper).collect(Collectors.joining(delimiter));
    }

    public static String prettyPrint(String label, Map<String, Object> map) {
        ByteArrayOutputStream is = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(is);
        MapUtils.verbosePrint(stream, label, map);
        stream.close();
        return is.toString();
    }
}
