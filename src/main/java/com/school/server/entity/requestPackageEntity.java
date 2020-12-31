package com.school.server.entity;

import com.school.server.tool.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class requestPackageEntity implements Serializable {
    private byte[] start = {122,123};
    private byte func;
    private byte dataType;
    private byte[] line = new byte[6];
    private byte[] car = new byte[4];
    private byte[] cmd = new byte[4];
    private byte[] startDate = new byte[6];
    private byte[] endDate = new byte[6];
    private byte[] ack = new byte[4];
    private byte[] reserve=new byte[28];
    private byte[] end = {127,127};

    @Override
    public String toString() {
        return
                byteTool.bytesToHex(start)+
                byteTool.byteToHex(this.func) +
                byteTool.byteToHex(this.dataType) +
                byteTool.bytesToHex(this.line) +
                byteTool.bytesToHex(this.car) +
                byteTool.bytesToHex(this.cmd) +
                byteTool.bytesToHex(this.startDate) +
                byteTool.bytesToHex(this.endDate) +
                byteTool.bytesToHex(this.ack)+
                byteTool.bytesToHex(this.reserve)+
                byteTool.bytesToHex(end);
    }
}