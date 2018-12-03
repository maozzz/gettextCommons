package ru.villex.gettext.test;

import junit.framework.TestCase;
import org.junit.Test;
import ru.villex.gettext.GettextResourceBundle;
import ru.villex.gettext.GettextTranslator;
import ru.villex.gettext.MoParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 19.11.2018
 * Time: 17:55
 */
public class GettextTest extends TestCase {

    private static final String testFiles = "/languages/%s/default.mo";
    private String[] locales = {"en", "ru", "el"};

    private Locale ru = Locale.forLanguageTag("ru");
    private Locale en = Locale.forLanguageTag("en");
    private Locale el = Locale.forLanguageTag("el");


    @Test
    public void testParse() throws IOException {
        GettextTranslator translator = new GettextTranslator();

        MoParser parser = new MoParser();
        for (String locale : locales) {
            InputStream res = getClass().getResourceAsStream(String.format(testFiles, locale));
            GettextResourceBundle bundle = new GettextResourceBundle(parser.read(res));
            translator.addBundle(bundle);
        }
        String t = translator._(ru.getLanguage(), "About us");
        assertEquals(t, "О нас");
        t = translator._n(ru, "Buying Pro for %s year", 5L);
        System.out.println(t);
        assertEquals(t, "Покупка Pro на 5 лет");
        t = translator._(ru, "Editing the interview with %1$s, a wedding photographer from %2$s", "Катя", "Москва");
        System.out.println(t);
        assertEquals(t, "Редактирование интервью со свадебным фотографом Катя, Москва");
        t = translator._n(ru, "Discover <a href=\"%s\">%s more wedding photographer</a> or <a %s>create</a> the Photographer Wanted Ad", 21L, "https://ya.ru", 21, "https://google.com");
        System.out.println(t);
        assertEquals(t, "Посмотреть еще <a href=\"https://ya.ru\">21 фотографа на свадьбу</a> или <a https://google.com>оставить объявление</a> о поиске фотографа");
        t = translator._n("ru", "message", 21);
        System.out.println(t);
        assertEquals(t, "сообщение");
        t = translator._n(ru, "If you complete the next %1$s step we will add you %2$s rating points for it.", 5L, 5, 500);
        System.out.println(t);
        assertEquals(t, "Если вы выполните 5 шагов, то мы добавим вам 500 единиц рейтинга за каждый из них.");
        t = translator._n(ru, "Join <a href=\"%s\" class=\"link-def\">MyWed Pro Professionals Club</a><br> and upload up to %s photo per week! And that is not all:", 23L, "https://ya.ru", 23);
        System.out.println(t);
        assertEquals(t, "Вступайте в <a href=\"https://ya.ru\" class=\"link-def\">Клуб профессионалов MyWed Pro</a><br> и загружайте по 23 фотографии в неделю! И это еще не все:");
    }
}
