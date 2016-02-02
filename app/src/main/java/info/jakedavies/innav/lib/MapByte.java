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
    public void setIntersection(boolean value){
        setBit(4, value);
    }
    public boolean isIntersection() { return bitVal(4); }
    //bit 5
    public void setBeacon(boolean value){
        setBit(5, value);
    }
    public boolean isBeacon() { return bitVal(5); }

    // helper methods
    private void setBit(int bitNum, boolean value){
        if(value){
            b |= 1 << bitNum;
        }else{
            b &= ~(1 << bitNum);
        }
    }
    public boolean isWalkable(){
        return !isIsle() && !isObstacle();
    }
    public boolean setType(String type){
        if(type.equals("obstacle")){
            setObstacle(true);
        }
        else if (type.equals("isle")) {
            setIsle(true);
        }
        else if(type.equals("wall")) {
            setWall(true);
        }
        else {
            return false;
        }
        return true;
    }
    private boolean bitVal(int bitNum){
        return (b & (1L << bitNum)) != 0;
    }
    public byte getByte(){
        return b;
    }

}
