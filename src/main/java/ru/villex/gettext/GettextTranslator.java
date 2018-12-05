package ru.villex.gettext;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 19:29
 */
public class GettextTranslator implements Gettextable {

    private boolean useMessageFormat = false;

    Map<Locale, GettextResourceBundle> bundles = new Hashtable<>();

    public GettextTranslator() {

    }

    public void setBundles(List<GettextResourceBundle> bundles) {
        for (GettextResourceBundle bundle : bundles) {
            this.bundles.put(bundle.getLocale(), bundle);
        }
    }

    public void addBundle(GettextResourceBundle bundle) {
        this.bundles.put(bundle.getLocale(), bundle);
    }

    public boolean isUseMessageFormat() {
        return useMessageFormat;
    }

    public void setUseMessageFormat(boolean useMessageFormat) {
        this.useMessageFormat = useMessageFormat;
    }

    @Override
    public String _(String lang, String key) {
        return _(Locale.forLanguageTag(lang), key, null);
    }

    @Override
    public String _(Locale locale, String key) {
        return _(locale, key, null);
    }

    @Override
    public String _(String lang, String key, Object... args) {
        return _(Locale.forLanguageTag(lang), key, args);
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
    public String _n(String lang, String key, long num, Object... args) {
        return _n(Locale.forLanguageTag(lang), new String[]{key}, num, args);
    }

    @Override
    public String _n(Locale locale, String key, long num, Object... args) {
        return _n(locale, new String[]{key}, num, args);
    }

    @Override
    public String _n(Locale locale, String key, String plural, long num, Object... args) {
        return _n(locale, new String[]{key, plural}, num, args);
    }

    @Override
    public String _n(Locale locale, String[] forms, long num, Object... args) {
        GettextResourceBundle bundle = bundles.get(locale);
        // @TODO сделать дефолтную локаль
        String tr = bundle.plural(num, forms);
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
