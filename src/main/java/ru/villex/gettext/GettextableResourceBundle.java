package ru.villex.gettext;

import ru.villex.gettext.plurals.PluralAble;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 15:22
 */
public interface GettextableResourceBundle extends PluralAble {

    Object lookup(String msgid);

    String plural(String key, long num);
}
