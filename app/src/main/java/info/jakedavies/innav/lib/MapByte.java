package info.jakedavies.innav.lib;

/**
 * Created by jakedavies on 2015-11-26.
 */
public class MapByte {
    byte b;
    public MapByte(){
        b = (byte)0;
    }
    // bit 0
    public void setWall(boolean value){
        setBit(0, value);
    }
    //bit 1
    public void setObstacle(boolean value){
        setBit(1, value);
    }
    public boolean isObstacle(){
        return bitVal(1);
    }
    //bit 2
    public void setIsle(boolean value){
        setBit(2, value);
    }
    public boolean isIsle(){
        return bitVal(2);
    }
    //bit 3
    public void setPath(boolean value){
        setBit(3, value);
    }
    public boolean isPath(){
        return bitVal(3);
    }
    //bit 4
    public void setBit4(boolean value){
        setBit(4, value);
    }
    private void setBit(int bitNum, boolean value){
        if(value){
            b |= 1 << bitNum;
        }else{
            b &= ~(1 << bitNum);
        }
    }
    private boolean bitVal(int bitNum){
        return (b & (1L << bitNum)) != 0;
    }
    public byte getByte(){
        return b;
    }

}
