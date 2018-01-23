package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Command;
import io.cess.comm.tcp.annotation.Command;
import io.cess.util.ByteUtil;

/**
 * Created by lin on 1/28/16.
 */
public abstract class CommandResponsePackage extends ResponsePackage implements CommandPackage{
    private int command = 0;

    protected CommandResponsePackage()
    {
        Class<?> cls = this.getClass();
        Command commandAnnon = cls.getAnnotation(Command.class);
        if(commandAnnon != null){
            this.command = commandAnnon.value();
        }

        if(command == 0){
            command = CommandPackageManager.request(this.getClass());
        }
    }


    protected int getBodySize()
    {
        return 0;
    }

    protected void bodyWrite(byte[] bs, int offset)
    {
    }
    protected void bodyWrite(byte[] bs)
    {
        bodyWrite(bs,0);
    }

    public int getSize()
    {
        return this.getBodySize() + 11;
    }

    @Override
    public final byte[] write()
    {
        byte[] bs = new byte[this.getSize()];
        ByteUtil.writeByte(bs, this.major, 0);
        ByteUtil.writeByte(bs, this.minor, 1);
        ByteUtil.writeByte(bs, this.revise, 2);
        ByteUtil.writeInt(bs, this.command, 3);
        ByteUtil.writeInt(bs, this.getSize(), 7);
        this.bodyWrite(bs, 11);
        return bs;
    }

    public int getCommand()
    {
        return this.command;
    }

    private byte major;
    private byte minor;
    private byte revise;

    public byte getMajor() {
        return major;
    }

    void setMajor(byte major) {
        this.major = major;
    }

    public byte getMinor() {
        return minor;
    }

    void setMinor(byte minor) {
        this.minor = minor;
    }

    public byte getRevise() {
        return revise;
    }

    void setRevise(byte revise) {
        this.revise = revise;
    }
    @Override
    public final byte getType()
    {
        return 1;
    }


    public final void parse(byte[] bs){
        parse(bs,0);
    }

    public abstract void parse(byte[] bs,int offset);
}
