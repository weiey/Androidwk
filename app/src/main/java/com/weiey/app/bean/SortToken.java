package com.weiey.app.bean;

import com.weiey.app.utils.PinYin4j;

import java.io.Serializable;

public class SortToken implements Serializable {
    public int pos;
    public String tag;
    public String simpleSpell = "";//简拼
    public String wholeSpell = "";//全拼

    public SortToken() {
    }

    public SortToken(int pos, String tag) {

        this.pos = pos;
        this.tag = tag;
        String sp  = "";
        if(tag.length() >10){
            sp = tag.substring(0,10);
        }else{
            sp = tag;
        }
        this.simpleSpell = PinYin4j.getInstance().getPinYinHeadChar(sp);
        this.wholeSpell = PinYin4j.getInstance().getAllPinyinChar(sp);

    }

    public String getSimpleSpell() {
        return simpleSpell;
    }

    public void setSimpleSpell(String simpleSpell) {
        this.simpleSpell = simpleSpell;
    }

    public String getWholeSpell() {
        return wholeSpell;
    }

    public void setWholeSpell(String wholeSpell) {
        this.wholeSpell = wholeSpell;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
