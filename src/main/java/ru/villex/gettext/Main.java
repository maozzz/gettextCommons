package ru.villex.gettext;


import ru.villex.gettext.plurals.ExpressionPlurable;
import ru.villex.gettext.plurals.Plurable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
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

        fromFile();
        String[] locales = {"en", "it", "ru"};

        List<GettextResourceBundle> bundles = new ArrayList<>();
        for (String locale : locales) {
            String url = String.format("https://mywed.com/%s/service/get-mo-file/", locale);
            InputStream is = new URL(url).openStream();
            MoParser moParser = new MoParser();
            List<Entry> entries = moParser.read(is);

            Plurable plurable = new ExpressionPlurable("((((n % 10) == 1) && ((n % 100) != 11)) ? 0 : (((((n % 10) >= 2) && ((n % 10) <= 4)) && (((n % 100) < 12) || ((n % 100) > 14))) ? 1 : (((((n % 10) == 0) || (((n % 10) >= 5) && ((n % 10) <= 9))) || (((n % 100) >= 11) && ((n % 100) <= 14))) ? 2 : 3)));");
            GettextResourceBundle bundle = new GettextResourceBundle(entries);
            bundles.add(bundle);
        }
        Gettext gettext = new Gettext();
        gettext.setBundles(bundles);
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 0L));
        System.out.println(gettext._n(new Locale("en"), "%s photo", "%s photos", 0L));
        System.out.println(gettext._n(new Locale("it"), "%s photo", 0L));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 1L, 1));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 2L, "2"));
        System.out.println(gettext._n(new Locale("it"), "%s photo", 4L, "four"));
        System.out.println(gettext._n(new Locale("en"), "%s photo", "%s photos", 17L));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 21L, 21));

        long start = System.currentTimeMillis();
        Locale ru = new Locale("ru");
        for (int i=0; i<1000000; i++) {
            gettext._n(ru, "%s photo", 0L);
        }
        System.out.println(System.currentTimeMillis() - start);
        String about = gettext._(Locale.ITALIAN, "About");
        System.out.println(about);
    }

    public static void fromFile() throws IOException {

        List<GettextResourceBundle> resources = new ArrayList<>();

        InputStream is = Main.class.getResourceAsStream("/ru.mo");

        MoParser moParser = new MoParser();
        List<Entry> entries = moParser.read(is);

        Locale ru = Locale.forLanguageTag("ru");
        resources.add(new GettextResourceBundle(entries));

        Gettext gettext = new Gettext();
        gettext.setBundles(resources);

        System.out.println(gettext._(ru, "About"));
        System.out.println(gettext._n(new Locale("ru"), "%s photo", 17L));
    }
}
