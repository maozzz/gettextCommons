package ru.villex.gettext;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 19:29
 */
public class Gettext implements Gettextable {

    private boolean useMessageFormat = false;

    Map<Locale, GettextResourceBundle> bundles = new Hashtable<>();

    public Gettext() {

    }

    public void setBundles(Map<Locale, GettextResourceBundle> bundles) {
        this.bundles = bundles;
    }

    public boolean isUseMessageFormat() {
        return useMessageFormat;
    }

    public void setUseMessageFormat(boolean useMessageFormat) {
        this.useMessageFormat = useMessageFormat;
    }

    @Override
    public String _(Locale locale, String key) {
        return _(locale, key, null);
    }

    @Override
    public String _(Locale locale, String key, Object... args) {
        GettextResourceBundle bundle = bundles.get(locale);
        // @TODO сделать дефолтную локаль
        String tr = bundle.getString(key);
        if (useMessageFormat) {
            return MessageFormat.format(tr, args);
        } else {
            return String.format(tr, args);
        }
    }

    @Override
    public String _n(Locale locale, String key, long num, Object... args) {
        GettextResourceBundle bundle = bundles.get(locale);
        // @TODO сделать дефолтную локаль
        String tr = bundle.plural(key, num);
        if (args.length == 0) {
            args = new Object[]{num};
        }
        if (useMessageFormat) {
            return MessageFormat.format(tr, args);
        } else {
                return String.format(tr, args);
        }
    }
}
