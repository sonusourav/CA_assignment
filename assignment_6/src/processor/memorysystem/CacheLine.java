package processor.memorysystem;

public class CacheLine{

    int tag,data;
    boolean state=false;

    CacheLine(){
        this.tag=0;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isState() {
        return this.state;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }


}