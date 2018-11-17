package ru.villex.gettext;


import ru.villex.gettext.plurals.PluralAbleRu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 0:26
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String[] locales = {"en", "it", "ru"};

        Hashtable<Locale, GettextResourceBundle> resources = new Hashtable<>();
        for (String locale : locales) {
            String url = String.format("https://mywed.com/%s/service/get-mo-file/", locale);
            InputStream is = new URL(url).openStream();
            MoParser moParser = new MoParser();
            List<Entry> entries = moParser.read(is);

            resources.put(Locale.forLanguageTag(locale), new GettextResourceBundle(entries, new Locale(locale), new PluralAbleRu()));
        }
        GettextResourceBundle ru = resources.get("ru");
        Gettext gettext = new Gettext();
        gettext.setBundles(resources);
        System.out.println(gettext._n(new Locale("ru"), "%s photoss", 0L));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 1L, 1));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 2L, "2"));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 4L, "four"));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 17L));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 21L, 21));

        String about = gettext._(Locale.ITALIAN, "About");
        System.out.println(about);
    }
}
