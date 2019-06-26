package pd.net.uri;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import pd.encoding.UriCodec;

/**
 * query is a string<br/>
 * https://tools.ietf.org/rfc/rfc3986.txt<br/>
 */
public final class Query {

    public static LinkedHashMap<String, String> parse(String querystring) {
        if (querystring == null) {
            return null;
        }

        LinkedHashMap<String, String> queries = new LinkedHashMap<>();
        for (String entryString : querystring.split("&")) {
            String key = null;
            String value = null;
            int i = entryString.indexOf('=');
            if (i < 0) {
                // a&b=1 => a=&b=1
                key = entryString;
                value = "";
            } else {
                key = entryString.substring(0, i);
                value = entryString.substring(i + 1);
            }
            queries.put(UriCodec.decode(key).toString(),
                    UriCodec.decode(value).toString());
        }
        return queries;
    }

    public static String toQueryString(Iterator<Map.Entry<String, String>> it) {
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            sb.append(UriCodec.encode(entry.getKey()));
            sb.append("=");
            sb.append(UriCodec.encode(entry.getValue()));
            sb.append("&");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String toQueryString(Map<String, String> queryMap) {
        if (queryMap == null) {
            return null;
        }

        Iterator<Map.Entry<String, String>> it = queryMap.entrySet().iterator();
        return toQueryString(it);
    }

    private Query() {
        // dummy
    }
}
