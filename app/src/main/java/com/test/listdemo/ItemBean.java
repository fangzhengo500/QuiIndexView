package com.test.listdemo;

/**
 * @project： ListDemo
 * @package： com.test.listdemo
 * @class: ItemBean
 * @author: 陆伟
 * @Date: 2017/2/15 16:55
 * @desc： TODO
 */
public class ItemBean {
    private static final String TAG = "ItemBean";
    public int type;
    public String name;
    public String phone;

    public ItemBean(int type, String name, String phone) {
        this.type = type;
        this.name = name;
        this.phone = phone;
    }
}
