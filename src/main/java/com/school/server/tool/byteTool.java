package com.school.server.tool;

import lombok.extern.log4j.Log4j;

import java.io.*;
import java.util.*;

@Log4j
public class byteTool {
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 整数转换成字节数组 关键技术：ByteArrayOutputStream和DataOutputStream
     *
     * @param n
     * 需要转换整数
     * @return 字节数组
     */
    public static byte[] intToByteArray(int n) {
        byte[] byteArray = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            dataOut.writeInt(n);
            byteArray = byteOut.toByteArray();
        } catch (IOException e) {
            log.error(e.toString());
        }
        return byteArray;
    }

    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            inHex="0"+inHex;
        }
        result = new byte[(hexlen/2)];
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

    /**
     *
     * @param bytes 长度为6的byte数组
     * @return 返回一个 "yyyy-MM-ddTHH:mm:ss.SSS" 格式的时间字符串
     */
    public static String bytesToDate(byte[] bytes){
        int year=2000+bytes[0];
        return String.format("%s-%s-%sT%s:%s:%s.000", Integer.toString(year),
                Byte.toString(bytes[1]).length()<2?'0'+Byte.toString(bytes[1]):Byte.toString(bytes[1]),
                Byte.toString(bytes[2]).length()<2?'0'+Byte.toString(bytes[2]):Byte.toString(bytes[2]),
                Byte.toString(bytes[3]).length()<2?'0'+Byte.toString(bytes[3]):Byte.toString(bytes[3]),
                Byte.toString(bytes[4]).length()<2?'0'+Byte.toString(bytes[4]):Byte.toString(bytes[4]),
                Byte.toString(bytes[5]).length()<2?'0'+Byte.toString(bytes[5]):Byte.toString(bytes[5]));
    }

    /**
     * 4字节的比特数组转换成int
     * @param bytes 需要4字节的长度。由高位到低位
     * @return int 类型
     */
    public static int bytesToInt(byte[] bytes){
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * 合并多个byte数组为一个数组
     * @param values 多个数组
     * @return 一个数组
     */
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (byte[] value : values) {
            length_byte += value.length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp=new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }
    public static byte[] getCurrentData(){
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(new Date());
        byte[] nowTimeBytes = new byte[6];
        nowTimeBytes[0]= (byte) (nowTime.get(Calendar.YEAR)-2000);
        nowTimeBytes[1]= (byte) nowTime.get(Calendar.MONTH);
        nowTimeBytes[2]= (byte) nowTime.get(Calendar.DAY_OF_MONTH);
        nowTimeBytes[3]= (byte) nowTime.get(Calendar.HOUR_OF_DAY);
        nowTimeBytes[4]= (byte) nowTime.get(Calendar.MINUTE);
        nowTimeBytes[5]= (byte) nowTime.get(Calendar.SECOND);
        return nowTimeBytes;
    }

    public static String bytesToHex(Byte[] result) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : result) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
