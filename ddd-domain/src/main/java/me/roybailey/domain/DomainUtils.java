package me.roybailey.domain;

import java.util.List;
import java.util.stream.Collectors;


public class DomainUtils {

    public static String multiline(List<?> objects) {
        if(objects == null || objects.isEmpty()) {
            return "";
        }
        return objects.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    public static String multiline(String prefix, List<?> objects) {
        return prefix + multiline(objects);
    }
}
