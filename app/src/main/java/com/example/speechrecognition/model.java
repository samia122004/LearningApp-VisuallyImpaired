package com.example.speechrecognition;

public class model {
    private String chpNo;
    private String chpName;
public model(){

}
public model(String chpNo, String chpName){
    this.chpNo=chpNo;
    this.chpName=chpName;
}
    public String getChpNo() {
        return chpNo;
    }

    public void setChpNo(String chpNo) {
        this.chpNo = chpNo;
    }
    public String getChpName() {
        return chpName;
    }

    public void setChpName(String chpName) {
        this.chpName = chpName;
    }


}
