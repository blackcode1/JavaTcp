package com.school.server.entity;

import com.school.server.tool.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.ByteBuffer;

@Getter
@Setter
public class responsePackageEntity implements Serializable {
    private byte[] start = {122,123};
    private byte func;
    private byte dataType;
    private byte[] line=new byte[6];
    private byte[] car=new byte[4];
    private byte[] cmd=new byte[4];
    private byte[] startDate=new byte[6];
    private byte[] endDate=new byte[6];
    private byte[] packageAllCount=new byte[4];
    private byte[] packageNowCount=new byte[4];
    private byte[] packageLength=new byte[4];
    private byte[] reserve=new byte[22];
    private byte[] packageContent;
    private byte[] end = {127,127};

    public responsePackageEntity(){}

    @Override
    public String toString() {
        return  byteTool.bytesToHex(start)+
                byteTool.byteToHex(this.func) +
                byteTool.byteToHex(this.dataType) +
                byteTool.bytesToHex(this.line) +
                byteTool.bytesToHex(this.car) +
                byteTool.bytesToHex(this.cmd) +
                byteTool.bytesToHex(this.startDate) +
                byteTool.bytesToHex(this.endDate) +
                byteTool.bytesToHex(this.packageAllCount)+
                byteTool.bytesToHex(this.packageNowCount)+
                byteTool.bytesToHex(this.packageLength)+
                byteTool.bytesToHex(this.reserve)+
                byteTool.bytesToHex(this.packageContent)+
                byteTool.bytesToHex(end);
    }

    public responsePackageEntity(byte func,byte dataType,byte[] line,byte[]car,byte[]cmd,byte[]startDate,byte[] endDate,String packageContent){
        this.func=func;
        this.dataType=dataType;
        this.line=line;
        this.car=car;
        this.cmd=cmd;
        this.startDate=startDate;
        this.endDate=endDate;
        this.packageContent=byteTool.hexToByteArray(packageContent);
    }
    public responsePackageEntity(requestPackageEntity rEntity, String packageContent){
        this.func = rEntity.getFunc();
        this.dataType = rEntity.getDataType();
        this.line=rEntity.getLine();
        this.car=rEntity.getCar();
        this.cmd=rEntity.getCmd();
        this.startDate=rEntity.getStartDate();
        this.endDate=rEntity.getEndDate();
        this.packageContent=byteTool.hexToByteArray(packageContent);
    }

    public byte[] toByteArray(){
        byte[] t1 = new byte[1];
        t1[0]=this.func;
        byte[] t2 = new byte[1];
        t2[0]=this.dataType;
        return byteTool.byteMergerAll(
                this.start,
                t1,
                t2,
                this.line,
                this.car,
                this.cmd,
                this.startDate,
                this.endDate,
                this.packageAllCount,
                this.packageNowCount,
                this.packageLength,
                this.reserve,
                this.packageContent,
                this.end
        );
    }
}
