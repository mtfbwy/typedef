package cc.typedef.io;

import cc.typedef.basic.Blob;
import cc.typedef.primitive.Ctype;

public final class Factory {

    /**
     * accept a quoted string
     */
    public static CharSequence parseString(Nextable it) {
        return parseString(it, '\"', '\"');
    }

    public static CharSequence parseString(Nextable it, final int start,
            final int end) {
        int ch = -1;
        if (start >= 0) {
            // there is a start delimiter, taste it
            ch = it.next();
            if (ch != start) {
                throw new ParsingException(start, ch);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (true) {
            ch = it.next();
            if (ch == end) {
                // consume the end delimiter then exit
                return sb;
            }

            if (ch == '\\') {
                ch = Codec.Glyph.decode(ch, it);
            }
            sb.appendCodePoint(ch);
        }
    }

    public static void serialize(CharSequence s, InstallmentByteBuffer w) {
        w.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (Ctype.isAlphanum(c)) {
                w.append(c);
                continue;
            }

            int ch = c;
            if (Character.isHighSurrogate(c)) {
                char chLow = s.charAt(++i);
                if (Character.isLowSurrogate(chLow)) {
                    ch = Character.toCodePoint(c, chLow);
                } else {
                    throw new ParsingException();
                }
            } else if (Character.isLowSurrogate(c)) {
                throw new ParsingException();
            }
            // faster than String.getBytes("UTF-8") with exception handled
            // faster than toUtf8() then toHexText()
            Blob blob = new Blob();
            Codec.Glyph.encode(ch, blob);
            w.append(blob.a);
        }
        w.append('\"');
    }

    private Factory() {
        // dummy
    }
}
