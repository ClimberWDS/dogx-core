package com.dogx.core.base.utils;

/**
 * @auther:hxl
 * @Date:2021/12/24-12-24 10:43
 * @Version:1.0
 */
public class IntOrByteUtils {
    /***
     * int数字转byte数组
     * @param num
     * @return
     */
    public static byte[] int2ByteArray(int num){
        byte[] result = new byte[4];
        //取最高8位放到0下标
        result[0] = (byte)(num >>> 24);
        //取次高8为放到1下标
        result[1] = (byte)(num >>> 16);
        //取次低8位放到2下标
        result[2] = (byte)(num >>> 8);
        //取最低8位放到3下标
        result[3] = (byte)(num);
        return result;
    }

    /***
     * byte数组转int数字
     * @param array
     * @return
     */
    public static int byteArray2Int(byte[] array){
        byte[] a = new byte[4];
        int i = a.length - 1,j = array.length - 1;
        for (; i >= 0 ; i--,j--) {
            //从b的尾部(即int值的低位)开始copy数据
            if(j >= 0) {
                a[i] = array[j];
            }
            else {
                //如果b.length不足4,则将高位补0
                a[i] = 0;
            }
        }
        //&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v0 = (a[0] & 0xff) << 24;
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff) ;
        return v0 + v1 + v2 + v3;
    }

    public static void main(String[] args) {
        int i = 62440882;
        byte[] a = int2ByteArray(i);
        System.out.println(byteArray2Int(a));
    }
}
