package ru.villex.gettext;

import ru.villex.gettext.plurals.PluralAble;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 15:21
 */
public class GettextResourceBundle extends ResourceBundle implements GettextableResourceBundle {
    private final Locale locale;
    private PluralAble pluralAble;

    private Hashtable<String, Entry> table = new Hashtable<>();

    private String header;

    public GettextResourceBundle(Iterable<Entry> entries, Locale locale, PluralAble pluralAble) {
        setEntries(entries);
        this.locale = locale;
        setPluralAble(pluralAble);
    }

    @Override
    protected Object handleGetObject(String key) {
        Entry entry = table.get(key);
        if (entry == null) return entry;
        String[] tr = entry.getTr();
        return tr.length > 0 ? tr[0] : null;
    }

    @Override
    public Enumeration<String> getKeys() {
        return table.keys();
    }

    @Override
    public int pluralEval(long n) {
        return pluralAble.pluralEval(n);
    }

    @Override
    public Object lookup(String key) {
        String[] tr = table.get(key).getTr();
        return tr.length == 1 ? tr[0] : tr.length > 1 ? tr : "";
    }

    @Override
    public String plural(String key, long num) {
        Entry entry = table.get(key);
        if (entry == null) return key;
        return entry.getTr()[pluralEval(num)];
    }

    public void setEntries(Iterable<Entry> entries) {
        table.clear();
        for (Entry entry : entries) {
            if (entry.getKey() != null) {
                table.put(entry.getKey()[0], entry);
            } else {
                header = entry.getTr()[0];
            }
        }
    }

    private void setPluralAble(PluralAble pluralAble) {
        this.pluralAble = pluralAble;
    }
}
