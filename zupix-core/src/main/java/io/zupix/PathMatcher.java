package io.zupix;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Matches literal route segments and {name} path parameters. */
final class PathMatcher {
    private final Pattern pattern;
    private final String[] parameterNames;

    PathMatcher(String path) {
        var names = new java.util.ArrayList<String>();
        StringBuilder expression = new StringBuilder("^");
        for (String segment : path.substring(1).split("/", -1)) {
            expression.append('/');
            if (segment.startsWith("{") && segment.endsWith("}") && segment.length() > 2) {
                names.add(segment.substring(1, segment.length() - 1));
                expression.append("([^/]+)");
            } else {
                expression.append(Pattern.quote(segment));
            }
        }
        expression.append("$");
        parameterNames = names.toArray(String[]::new);
        pattern = Pattern.compile(expression);
    }

    Map<String, String> match(String path) {
        Matcher matcher = pattern.matcher(path);
        if (!matcher.matches()) {
            return null;
        }
        Map<String, String> values = new LinkedHashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            values.put(parameterNames[i], matcher.group(i + 1));
        }
        return values;
    }
}
