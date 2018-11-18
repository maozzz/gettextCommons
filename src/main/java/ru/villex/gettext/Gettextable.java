package ru.villex.gettext;

import java.util.Locale;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 19:26
 */
public interface Gettextable {
    String _(Locale locale, String key);

    String _(Locale locale, String key, Object... args);

    String _n(Locale locale, String key, long num, Object... args);

    String _n(Locale locale, String key, String plural, long num, Object... args);

    String _n(Locale locale, String[] forms, long num, Object... args);
}
