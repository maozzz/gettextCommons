package ru.villex.gettext;

import ru.villex.gettext.plurals.Plurable;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 15:22
 */
public interface GettextableResourceBundle extends Plurable {

    Object lookup(String msgid);

    String plural(long num, String... forms);
}
