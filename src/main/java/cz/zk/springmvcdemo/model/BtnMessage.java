package cz.zk.springmvcdemo.model;

import lombok.Data;

@Data
public class BtnMessage {

    private String Name;
    private int Code;

    public BtnMessage(String name, int code) {
        this.Name = name;
        this.Code = code;
    }
}
