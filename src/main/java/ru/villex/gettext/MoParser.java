package ru.villex.gettext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 0:27
 */
public class MoParser {
    // вспомогательная переменная для хранения в ней временных данных
    ByteArrayOutputStream bs = new ByteArrayOutputStream(1000);

    private static final int START = 0x950412de;
    private static final int ALT_START = 0xde120495;

    private int startWord;

    byte[] buff = new byte[4];
    private int fileFormatVersion;
    private int numberOfStrings;
    private int offsetOriginal;
    private int offsetTranslations;
    private int sizeOfHastable;
    private int offsetOfHashtable;

    private LinkedList<Entry> entries = new LinkedList<Entry>();


    public List<Entry> read(InputStream inputStream) throws IOException {
        try {
            // парсим заголовочные данные
            readStream(buff, inputStream);
            startWord = new BigInteger(reverse(buff)).intValue();
            readStream(buff, inputStream);
            fileFormatVersion = new BigInteger(reverse(buff)).intValue();
            readStream(buff, inputStream);
            numberOfStrings = new BigInteger(reverse(buff)).intValue();
            readStream(buff, inputStream);
            offsetOriginal = new BigInteger(reverse(buff)).intValue();
            readStream(buff, inputStream);
            offsetTranslations = new BigInteger(reverse(buff)).intValue();
            readStream(buff, inputStream);
            sizeOfHastable = new BigInteger(reverse(buff)).intValue();
            readStream(buff, inputStream);
            offsetOfHashtable = new BigInteger(reverse(buff)).intValue();
            int position = 28;

            // проматываем до карты ключей
            while (position < offsetOriginal) {
                inputStream.read();
                position++;
            }

            // из карты ключей проставляем положение ключей
            while (position < offsetTranslations) {
                Entry entry = new Entry();
                readStream(buff, inputStream);
                int length = new BigInteger(reverse(buff)).intValue();
                readStream(buff, inputStream);
                int offset = new BigInteger(reverse(buff)).intValue();
                entry.setKeyLen(length);
                entry.setKeyPos(offset);
                entries.add(entry);
                position += 8;
            }

            // Далее идет карта переводов. Проставляем расположение переводов
            Iterator<Entry> iterator = entries.iterator();
            while (position < offsetOfHashtable) {
                Entry entry = iterator.next();
                readStream(buff, inputStream);
                int length = new BigInteger(reverse(buff)).intValue();
                readStream(buff, inputStream);
                int offset = new BigInteger(reverse(buff)).intValue();

                entry.setTrLen(length);
                entry.setTrPos(offset);
                position += 8;
            }

            while (position < (offsetOfHashtable + sizeOfHastable * 4)) {
                inputStream.read();
                position++;
            }


            //перебираем полученную карты и для нее заполняем ключи
            byte[] temp;
            for (Entry entry : entries) {
                if (entry.getKeyLen() < 1) continue;
                // идем до начала ключа
                while (position < entry.getKeyPos()) {
                    inputStream.read();
                    position++;
                }
                // буфер соответствующей длинны
                // +1 для последнего 0 байта
                temp = new byte[entry.getKeyLen() + 1];
                readStream(temp, inputStream);
                position += temp.length;
                entry.setKey(parseElement(temp));
            }

            // заполняем переводы
            for (Entry entry : entries) {
                if (entry.getTrLen() < 1) continue;
                // идем до начала перевода
                while (position < entry.getTrPos()) {
                    inputStream.read();
                    position++;
                }

                // буфер соответствующей длинны
                temp = new byte[entry.getTrLen() + 1];
                // читаем ключ в буфер
                readStream(temp, inputStream);
                position += temp.length;
                entry.setTr(parseElement(temp));
            }
            if (numberOfStrings != entries.size())
                Logger.getLogger("MoParser").severe("Etries must be: " + this.numberOfStrings + ", parsed: " + entries.size());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return entries;
    }

    /**
     * Парсим элемент. Если встречаем байт 0 - то создаем следуюущую строку.
     * 0 попадается для переводов с плюрализацией. В итоге имеем несколько строк.
     *
     * @param input входные данные для элемента
     * @return
     */
    private String[] parseElement(byte[] input) {
        byte delim = 0;
        List<String> strings = new ArrayList<>();
        bs.reset();

        for (byte b : input) {
            if (b == delim) {
                strings.add(new String(bs.toByteArray()));
                bs.reset();
                continue;
            }
            bs.write(b);
        }
        String[] strs = new String[strings.size()];
        return strings.toArray(strs);
    }

    /**
     * Если использовать InputStream::read(buff) - то он может не полностью заполнять его данными,
     * если они еще не готовы. Поэтому читаю по одному и заполняю буфер вручну.
     *
     * @param buff
     * @param is
     * @throws IOException
     */
    private void readStream(byte[] buff, InputStream is) throws IOException {
        for (int i = 0; i < buff.length; i++) {
            int read = is.read();

            if (read == -1) {
                throw new RuntimeException("unexpected end of file");
            }

            buff[i] = (byte) read;
        }
    }

    private byte[] reverse(byte[] src) {
        byte[] res = new byte[src.length];

        for (int i = 0; i < src.length; i++) {
            res[src.length - 1 - i] = src[i];
        }
        return res;
    }
}
