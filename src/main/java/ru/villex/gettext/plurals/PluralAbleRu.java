package ru.villex.gettext.plurals;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 18:30
 */
public class PluralAbleRu implements PluralAble {
    @Override
    public int pluralEval(long n) {
        return ((((n % 10) == 1) && ((n % 100) != 11)) ? 0 : (((((n % 10) >= 2) && ((n % 10) <= 4)) && (((n % 100) < 12) || ((n % 100) > 14))) ? 1 : (((((n % 10) == 0) || (((n % 10) >= 5) && ((n % 10) <= 9))) || (((n % 100) >= 11) && ((n % 100) <= 14))) ? 2 : 3)));
    }
}
