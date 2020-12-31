package com.school.server.tool;

import lombok.extern.log4j.Log4j;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
@Log4j
public class byteToolTest {

    @Test
    public void byteToHex() {
    }

    @Test
    public void bytesToHex() {
    }

    @Test
    public void intToByteArray() {
    }

    @Test
    public void hexToByte() {
    }

    @Test
    public void hexToByteArray() {
    }

    @Test
    public void bytesToDate() {
    }

    @Test
    public void bytesToInt() {
    }

    @Test
    public void byteMergerAll() {
    }

    @Test
    public void getValueFromApplication() {
        log.info(new Date().getTime());
    }

    @Test
    public void getCurrentData() {
        for(byte temp:byteTool.getCurrentData()){
            log.info(temp);
        }
    }
}