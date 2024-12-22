package com.itssky.system.domain;


import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "tbstationinfo")
public class TbStationInfo {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer stationid;

    private String stationhex;

    private String stationname;

    private String dbip;

    private String dbuser;

    private String dbpasswd;

    private String edgeip;

    private Byte isused;

    private Byte indexes;

    private String corpno;

    private Integer spare1;

    private String spare2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStationid() {
        return stationid;
    }

    public void setStationid(Integer stationid) {
        this.stationid = stationid;
    }

    public String getStationhex() {
        return stationhex;
    }

    public void setStationhex(String stationhex) {
        this.stationhex = stationhex;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getDbip() {
        return dbip;
    }

    public void setDbip(String dbip) {
        this.dbip = dbip;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbpasswd() {
        return dbpasswd;
    }

    public void setDbpasswd(String dbpasswd) {
        this.dbpasswd = dbpasswd;
    }

    public String getEdgeip() {
        return edgeip;
    }

    public void setEdgeip(String edgeip) {
        this.edgeip = edgeip;
    }

    public Byte getIsused() {
        return isused;
    }

    public void setIsused(Byte isused) {
        this.isused = isused;
    }

    public Byte getIndexes() {
        return indexes;
    }

    public void setIndexes(Byte indexes) {
        this.indexes = indexes;
    }

    public String getCorpno() {
        return corpno;
    }

    public void setCorpno(String corpno) {
        this.corpno = corpno;
    }

    public Integer getSpare1() {
        return spare1;
    }

    public void setSpare1(Integer spare1) {
        this.spare1 = spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }
}