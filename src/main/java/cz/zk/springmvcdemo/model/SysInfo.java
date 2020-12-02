package cz.zk.springmvcdemo.model;

import lombok.Data;

@Data
public class SysInfo {
    private String systemVersion;
    private String buildDate;

    public SysInfo(String ver, String datum) {
        this.systemVersion = ver;
        this.buildDate = datum;
    }
}

