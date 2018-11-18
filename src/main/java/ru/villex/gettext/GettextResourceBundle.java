package ru.villex.gettext;

import ru.villex.gettext.plurals.Plurable;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import static ru.villex.gettext.plurals.ExpressionPlurable.generatePlurableClass;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 15:21
 */
public class GettextResourceBundle extends ResourceBundle implements GettextableResourceBundle {
    private Locale locale;
    private Plurable plurable;

    private Hashtable<String, String[]> table = new Hashtable<>();

    private String[] headers;
    private Hashtable<String, String> meta = new Hashtable<>();
    private String language;
    private String plural;

    public GettextResourceBundle(Iterable<Entry> entries) {
        this(entries, null);
    }

    public GettextResourceBundle(Iterable<Entry> entries, Locale locale) {
        this(entries, locale, null);
    }

    public GettextResourceBundle(Iterable<Entry> entries, Locale locale, Plurable plurable) {
        this.locale = locale;
        setPlurable(plurable);
        setEntries(entries);
    }

    @Override
    protected Object handleGetObject(String key) {
        String[] trs = table.get(key);
        if (trs == null) return null;
        return trs.length > 0 ? trs[0] : null;
    }

    @Override
    public Enumeration<String> getKeys() {
        return table.keys();
    }

    @Override
    public int pluralEval(long n) {
        return plurable.pluralEval(n);
    }

    @Override
    public Object lookup(String key) {
        String[] tr = table.get(key);
        return tr.length == 1 ? tr[0] : tr.length > 1 ? tr : "";
    }

    @Override
    public String plural(long num, String... forms) {
        try {
            String[] trs = table.get(forms[0]);
            if (trs == null) trs = forms;
            String s = trs[pluralEval(num)];
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
            return forms[0];
        }
    }

    public void setEntries(Iterable<Entry> entries) {
        table.clear();
        for (Entry entry : entries) {
            if (entry.getKey() != null) {
                table.put(entry.getKey()[0], entry.getTr());
            } else {
                parseHeaders(entry.getTr()[0]);
            }
        }
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    private void parseHeaders(String s) {
        headers = s.split("\n");
        for (String header : headers) {
            String[] split = header.split(":");
            if (split.length > 1) {
                meta.put(split[0].trim(), split[1].trim());
                if (split[0].toLowerCase().equals("language")) {
                    language = split[1].trim();
                    if (locale == null) {
                        locale = Locale.forLanguageTag(language);
                    }
                }
                if (split[0].toLowerCase().equals("plural-forms")) {
                    int i = header.toLowerCase().indexOf("plural=");
                    if (i != 0) {
                        plural = header.substring(i, header.length()).replace("plural=", "");
                        if (plurable == null) {
//                            plurable = new ExpressionPlurable(plural);
                            plurable = generatePlurableClass(plural);
                        }
                    }
                }
            }
        }
    }


    private void setPlurable(Plurable plurable) {
        this.plurable = plurable;
    }
}
