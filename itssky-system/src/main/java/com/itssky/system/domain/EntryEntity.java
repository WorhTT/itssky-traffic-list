package com.itssky.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "entry")
public class EntryEntity implements Serializable {

    @JsonProperty("TransID")
    @TableField(value = "TransID")
    private Integer TransID;

    @JsonProperty("Network")
    @TableField(value = "Network")
    private Short Network;

    @JsonProperty("StationID")
    @TableField(value = "StationID")
    private Integer StationID;

    @JsonProperty("LaneID")
    @TableField(value = "LaneID")
    private Short LaneID;

    @JsonProperty("EntryTime")
    @TableField(value = "EntryTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date EntryTime;

    @JsonProperty("ProgramTime")
    @TableField(value = "ProgramTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ProgramTime;

    @JsonProperty("ID")
    @TableId(value = "ID")
    private String ID;

    @JsonProperty("CardNetwork")
    @TableField(value = "CardNetwork")
    private Short CardNetwork;

    @JsonProperty("CardIssue")
    @TableField(value = "CardIssue")
    private String CardIssue;

    @JsonProperty("CardMAC")
    @TableField(value = "CardMAC")
    private String CardMAC;

    @JsonProperty("CardVer")
    @TableField(value = "CardVer")
    private Integer CardVer;

    @JsonProperty("CardType")
    @TableField(value = "CardType")
    private Integer CardType;

    @JsonProperty("CardID")
    @TableField(value = "CardID")
    private String CardID;

    @JsonProperty("ICCount")
    @TableField(value = "ICCount")
    private Integer ICCount;

    @JsonProperty("CardBoxID")
    @TableField(value = "CardBoxID")
    private String CardBoxID;

    @JsonProperty("CardBAT")
    @TableField(value = "CardBAT")
    private Integer CardBAT;

    @JsonProperty("StdStationID")
    @TableField(value = "StdStationID")
    private String StdStationID;

    @JsonProperty("HexStationID")
    @TableField(value = "HexStationID")
    private String HexStationID;

    @JsonProperty("StdLaneID")
    @TableField(value = "StdLaneID")
    private String StdLaneID;

    @JsonProperty("HexLaneID")
    @TableField(value = "HexLaneID")
    private String HexLaneID;

    @JsonProperty("EntryDate")
    @TableField(value = "EntryDate")
    private Integer EntryDate;

    @JsonProperty("OperatorID")
    @TableField(value = "OperatorID")
    private Integer OperatorID;

    @JsonProperty("ShiftID")
    @TableField(value = "ShiftID")
    private Integer ShiftID;

    @JsonProperty("TeamID")
    @TableField(value = "TeamID")
    private Integer TeamID;

    @JsonProperty("VehicleClass")
    @TableField(value = "VehicleClass")
    private Integer VehicleClass;

    @JsonProperty("PVehicleClass")
    @TableField(value = "PVehicleClass")
    private Integer PVehicleClass;

    @JsonProperty("VehicleStatus")
    @TableField(value = "VehicleStatus")
    private Integer VehicleStatus;

    @JsonProperty("DealStatus")
    @TableField(value = "DealStatus")
    private Integer DealStatus;

    @JsonProperty("RecordType")
    @TableField(value = "RecordType")
    private Integer RecordType;

    @JsonProperty("DeviceStatus")
    @TableField(value = "DeviceStatus")
    private Integer DeviceStatus;

    @JsonProperty("VehicleLicense")
    @TableField(value = "VehicleLicense")
    private String VehicleLicense;

    @JsonProperty("LicensePlate")
    @TableField(value = "LicensePlate")
    private String LicensePlate;

    @JsonProperty("TermCode")
    @TableField(value = "TermCode")
    private String TermCode;

    @JsonProperty("OBUID")
    @TableField(value = "OBUID")
    private String OBUID;

    @JsonProperty("OBUVersion")
    @TableField(value = "OBUVersion")
    private Integer OBUVersion;

    @JsonProperty("TAC")
    @TableField(value = "TAC")
    private String TAC;

    @JsonProperty("ETradNo")
    @TableField(value = "ETradNo")
    private String ETradNo;

    @JsonProperty("TermTradNo")
    @TableField(value = "TermTradNo")
    private String TermTradNo;

    @JsonProperty("IdentifyStatus")
    @TableField(value = "IdentifyStatus")
    private Integer IdentifyStatus;

    @JsonProperty("ImagePath")
    @TableField(value = "ImagePath")
    private String ImagePath;

    @JsonProperty("ProgramVer")
    @TableField(value = "ProgramVer")
    private String ProgramVer;

    @JsonProperty("VerifyCode")
    @TableField(value = "VerifyCode")
    private Integer VerifyCode;

    @JsonProperty("CancelFlag")
    @TableField(value = "CancelFlag")
    private Integer CancelFlag;

    @JsonProperty("Toll")
    @TableField(value = "Toll")
    private Integer Toll;

    @JsonProperty("AxisType")
    @TableField(value = "AxisType")
    private Integer AxisType;

    @JsonProperty("TotalWeight")
    @TableField(value = "TotalWeight")
    private Integer TotalWeight;

    @JsonProperty("LimitWeight")
    @TableField(value = "LimitWeight")
    private Integer LimitWeight;

    @JsonProperty("OverLoadRate")
    @TableField(value = "OverLoadRate")
    private Integer OverLoadRate;

    @JsonProperty("AxisInfo")
    @TableField(value = "AxisInfo")
    private String AxisInfo;

    @JsonProperty("PSAMID")
    @TableField(value = "PSAMID")
    private String PSAMID;

    @JsonProperty("TransType")
    @TableField(value = "TransType")
    private Integer TransType;

    @JsonProperty("AlgorithmID")
    @TableField(value = "AlgorithmID")
    private Integer AlgorithmID;

    @JsonProperty("DurationSec")
    @TableField(value = "DurationSec")
    private Integer DurationSec;

    @JsonProperty("Transfermark")
    @TableField(value = "Transfermark")
    private Integer Transfermark;

    @JsonProperty("Spare1")
    @TableField(value = "Spare1")
    private Integer Spare1;

    @JsonProperty("Spare2")
    @TableField(value = "Spare2")
    private Integer Spare2;

    @JsonProperty("Spare3")
    @TableField(value = "Spare3")
    private String Spare3;

    @JsonProperty("Spare4")
    @TableField(value = "Spare4")
    private Integer Spare4;

    @JsonProperty("Spare5")
    @TableField(value = "Spare5")
    private String Spare5;

    @JsonProperty("Spare6")
    @TableField(value = "Spare6")
    private String Spare6;

    @JsonProperty("LoadWeight")
    @TableField(value = "LoadWeight")
    private Integer LoadWeight;

    @JsonProperty("AxisNum")
    @TableField(value = "AxisNum")
    private Integer AxisNum;

    @JsonProperty("OBUVehicleDim")
    @TableField(value = "OBUVehicleDim")
    private String OBUVehicleDim;

    @JsonProperty("OBUType")
    @TableField(value = "OBUType")
    private Integer OBUType;

    @JsonProperty("GantryHex")
    @TableField(value = "GantryHex")
    private String GantryHex;

    @JsonProperty("TollFee")
    @TableField(value = "TollFee")
    private Integer TollFee;

    @JsonProperty("EBBalance")
    @TableField(value = "EBBalance")
    private Integer EBBalance;

    @JsonProperty("EABalance")
    @TableField(value = "EABalance")
    private Integer EABalance;

    @JsonProperty("OBUIssue")
    @TableField(value = "OBUIssue")
    private String OBUIssue;

    @JsonProperty("OBUVehicleClass")
    @TableField(value = "OBUVehicleClass")
    private Integer OBUVehicleClass;

    @JsonProperty("OBUVehiclePlate")
    @TableField(value = "OBUVehiclePlate")
    private String OBUVehiclePlate;

    @JsonProperty("OBUSerialID")
    @TableField(value = "OBUSerialID")
    private String OBUSerialID;

    @JsonProperty("KeyVersion")
    @TableField(value = "KeyVersion")
    private String KeyVersion;

    @JsonProperty("feeUnitGroup")
    @TableField(value = "feeUnitGroup")
    private String feeUnitGroup;

    @JsonProperty("chargeGroup")
    @TableField(value = "chargeGroup")
    private String chargeGroup;

    @JsonProperty("StatsFlag")
    @TableField(value = "StatsFlag")
    private Integer StatsFlag;

    @JsonProperty("Distance")
    @TableField(value = "Distance")
    private Integer Distance;

    @JsonProperty("CalcFeeVer")
    @TableField(value = "CalcFeeVer")
    private String CalcFeeVer;

    @JsonProperty("selfSumPayFee")
    @TableField(value = "selfSumPayFee")
    private Integer selfSumPayFee;

    @JsonProperty("specialCode")
    @TableField(value = "specialCode")
    private String specialCode;

    @JsonProperty("AVehicleClass")
    @TableField(value = "AVehicleClass")
    private Integer AVehicleClass;

    @TableField(exist = false)
    @JsonIgnore
    private Date ListenTime;

    @TableField(exist = false)
    @JsonIgnore
    private String listenTableName;

    private static final long serialVersionUID = 1L;
}
