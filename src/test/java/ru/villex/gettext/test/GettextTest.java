package ru.villex.gettext.test;

import org.junit.Assert;
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
public class GettextTest {

    private static final String testFiles = "/languages/%s/default.mo";
    private final String[] locales = {"en", "ru", "el", "zh", "tw"};

    private final Locale ru = Locale.forLanguageTag("ru");
    private final Locale en = Locale.forLanguageTag("en");
    private final Locale el = Locale.forLanguageTag("el");
    private final Locale zh = Locale.forLanguageTag("zh");
    private final Locale tw = Locale.forLanguageTag("tw");


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
        Assert.assertEquals("О нас", t);
        t = translator._n(en, "%1$s photographers in %2$s – %3$s best photographer", "%1$s photographers in %2$s – %3$s best photographers", 3L, "Portrait", "Belgrade", "3");
        System.out.println(t);
        Assert.assertEquals("Portrait photographers in Belgrade – 3 best photographers", t);
        t = translator._(ru.getLanguage(), "The best family and wedding photos of the week");
        Assert.assertEquals("Лучшие семейные и свадебные фотографии этой недели", t);
        t = translator._n(ru, "Must contain at least %s character", 5L);
        System.out.println(t);
        Assert.assertEquals("Значение должно быть длинной не менее 5 символов", t);
        t = translator._n(ru, "Buying Pro for %s year", 5L);
        System.out.println(t);
        Assert.assertEquals("Покупка Pro на 5 лет", t);
        t = translator._(ru, "Editing the interview with %1$s, a wedding photographer from %2$s", "Катя", "Москва");
        System.out.println(t);
        Assert.assertEquals("Редактирование интервью со свадебным фотографом Катя, Москва", t);
        t = translator._n(ru, "Please note that starting this year, photos taken within the last %1$s year, from %2$s to %3$s, may be uploaded for additional nominations. However, the shooting date period won't change for the main nominations — we accept photos taken in %4$s and %5$s.", 2L, "2", 2023, "2025", "2022", 2025);
        System.out.println(t);
        Assert.assertEquals("Обратите внимание, что начиная с этого года в дополнительных номинациях могут участвовать фотографии, снятые за последние 2 года, с 2023 по 2025. Для основных номинаций все остается по-прежнему — принимаются фотографии, снятые в 2022 и 2025 годах.", t);
        t = translator._n("ru", "message", 21);
        System.out.println(t);
        Assert.assertEquals("сообщение", t);
        t = translator._n(ru, "If you complete the next %1$s step we will add you %2$s rating points for it.", 5L, 5, 500);
        System.out.println(t);
        Assert.assertEquals("Если вы ответите на 5 вопросов, то мы добавим вам 500 единиц рейтинга за каждый из них.", t);
        t = translator._n(ru, "Join <a href=\"%s\" class=\"link-def\">MyWed Pro Professionals Club</a><br> and upload up to %s photo per week! And that is not all:", 23L, "https://ya.ru", 23);
        System.out.println(t);
        Assert.assertEquals("Вступайте в <a href=\"https://ya.ru\" class=\"link-def\">Клуб профессионалов MyWed Pro</a><br> и загружайте по 23 фотографии в неделю! И это еще не все:", t);
        t = translator._n(zh, "Join <a href=\"%s\" class=\"link-def\">MyWed Pro Professionals Club</a><br> and upload up to %s photo per week! And that is not all:", 23L, "https://ya.ru", 23);
        System.out.println(t);
        Assert.assertEquals("加入<a href=\"https://ya.ru\" class=\"link-def\">MyWed Pro精英俱乐部</a><br>即可每星期上传多达23张图片！此外：", t);

    }

    @Test
    public void testParseChinese() throws IOException {
        GettextTranslator translator = new GettextTranslator();

        MoParser parser = new MoParser();
        for (String locale : locales) {
            InputStream res = getClass().getResourceAsStream(String.format(testFiles, locale));
            GettextResourceBundle bundle = new GettextResourceBundle(parser.read(res));
            translator.addBundle(bundle);
        }
        String t = translator._(tw.getLanguage(), "About us");
        Assert.assertEquals(t, "關於我們");

        t = translator._(zh.getLanguage(), "About us");
        Assert.assertEquals(t, "关于我们");
    }
}
