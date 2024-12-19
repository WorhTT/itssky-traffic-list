package com.itssky.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbstationinfo")
public class TbStationInfo implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "StationID")
    private Integer stationid;

    @TableField(value = "StationHex")
    private String stationhex;

    @TableField(value = "StationName")
    private String stationname;

    @TableField(value = "DBIP")
    private String dbip;

    @TableField(value = "DBUser")
    private String dbuser;

    @TableField(value = "DBPasswd")
    private String dbpasswd;

    @TableField(value = "EdgeIP")
    private String edgeip;

    @TableField(value = "IsUsed")
    private Byte isused;

    @TableField(value = "Indexes")
    private Byte indexes;

    @TableField(value = "CorpNo")
    private String corpno;

    @TableField(value = "Spare1")
    private Integer spare1;

    @TableField(value = "Spare2")
    private String spare2;

    private static final long serialVersionUID = 1L;
}