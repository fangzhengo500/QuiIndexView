package com.test.mtypelist;

/**
 * @project： ListDemo
 * @package： com.test.mtypelist
 * @class: ItemBean
 * @author: 陆伟
 * @Date: 2017/2/14 11:04
 * @desc： TODO
 */
public class ItemBean {
    private static final String TAG = "ItemBean";
    public int itemType;
    public String str;

    public ItemBean(int itemType, String str) {
        this.itemType = itemType;
        this.str = str;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "itemType=" + itemType +
                ", str='" + str + '\'' +
                '}';
    }
}
