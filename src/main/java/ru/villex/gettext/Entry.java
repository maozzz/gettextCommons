package ru.villex.gettext;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 16.11.2018
 * Time: 17:36
 */

class Entry {
    private int keyPos;
    private int keyLen;
    private int trPos;
    private int trLen;

    private String[] key;
    private String[] tr;

    public int getKeyPos() {
        return keyPos;
    }

    public void setKeyPos(int keyPos) {
        this.keyPos = keyPos;
    }

    public int getKeyLen() {
        return keyLen;
    }

    public void setKeyLen(int keyLen) {
        this.keyLen = keyLen;
    }

    public int getTrPos() {
        return trPos;
    }

    public void setTrPos(int trPos) {
        this.trPos = trPos;
    }

    public int getTrLen() {
        return trLen;
    }

    public void setTrLen(int trLen) {
        this.trLen = trLen;
    }

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    public String[] getTr() {
        return tr;
    }

    public void setTr(String[] tr) {
        this.tr = tr;
    }
}