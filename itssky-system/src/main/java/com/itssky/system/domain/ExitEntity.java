package com.itssky.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "exit")
public class ExitEntity implements Serializable {

    @TableField(value = "TransID")
    @JsonProperty("TransID")
    private Integer TransID;

    @TableField(value = "Network")
    @JsonProperty("Network")
    private Short Network;

    @TableField(value = "StationID")
    @JsonProperty("StationID")
    private Integer StationID;

    @TableField(value = "LaneID")
    @JsonProperty("LaneID")
    private Short LaneID;

    @TableField(value = "ExitTime")
    @JsonProperty("ExitTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ExitTime;

    @TableField(value = "ProgramTime")
    @JsonProperty("ProgramTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ProgramTime;

    @TableId(value = "ID")
    @JsonProperty("ID")
    private String ID;

    @TableField(value = "CardNetwork")
    @JsonProperty("CardNetwork")
    private Short CardNetwork;

    @TableField(value = "CardIssue")
    @JsonProperty("CardIssue")
    private String CardIssue;

    @TableField(value = "CardMAC")
    @JsonProperty("CardMAC")
    private String CardMAC;

    @TableField(value = "CardVer")
    @JsonProperty("CardVer")
    private Integer CardVer;

    @TableField(value = "CardType")
    @JsonProperty("CardType")
    private Integer CardType;

    @TableField(value = "CardID")
    @JsonProperty("CardID")
    private String CardID;

    @TableField(value = "CardBAT")
    @JsonProperty("CardBAT")
    private Integer CardBAT;

    @TableField(value = "ICCount")
    @JsonProperty("ICCount")
    private Integer ICCount;

    @TableField(value = "CardBoxID")
    @JsonProperty("CardBoxID")
    private Integer CardBoxID;

    @TableField(value = "EntryNetwork")
    @JsonProperty("EntryNetwork")
    private Short EntryNetwork;

    @TableField(value = "EntryStation")
    @JsonProperty("EntryStation")
    private Integer EntryStation;

    @TableField(value = "EntryLane")
    @JsonProperty("EntryLane")
    private Short EntryLane;

    @TableField(value = "EntryOperator")
    @JsonProperty("EntryOperator")
    private Integer EntryOperator;

    @TableField(value = "EntryShift")
    @JsonProperty("EntryShift")
    private Integer EntryShift;

    @TableField(value = "RVehicleClass")
    @JsonProperty("RVehicleClass")
    private Integer RVehicleClass;

    @TableField(value = "RVehicleStatus")
    @JsonProperty("RVehicleStatus")
    private Integer RVehicleStatus;

    @TableField(value = "RVehicleLicense")
    @JsonProperty("RVehicleLicense")
    private String RVehicleLicense;

    @TableField(value = "EntryTime")
    @JsonProperty("EntryTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date EntryTime;

    @TableField(value = "StdStationID")
    @JsonProperty("StdStationID")
    private String StdStationID;

    @TableField(value = "HexStationID")
    @JsonProperty("HexStationID")
    private String HexStationID;

    @TableField(value = "StdLaneID")
    @JsonProperty("StdLaneID")
    private String StdLaneID;

    @TableField(value = "HexLaneID")
    @JsonProperty("HexLaneID")
    private String HexLaneID;

    @TableField(value = "ExitDate")
    @JsonProperty("ExitDate")
    private Integer ExitDate;

    @TableField(value = "OperatorID")
    @JsonProperty("OperatorID")
    private Integer OperatorID;

    @TableField(value = "ShiftID")
    @JsonProperty("ShiftID")
    private Integer ShiftID;

    @TableField(value = "TeamID")
    @JsonProperty("TeamID")
    private Integer TeamID;

    @TableField(value = "VehicleClass")
    @JsonProperty("VehicleClass")
    private Integer VehicleClass;

    @TableField(value = "PVehicleClass")
    @JsonProperty("PVehicleClass")
    private Integer PVehicleClass;

    @TableField(value = "VehicleStatus")
    @JsonProperty("VehicleStatus")
    private Integer VehicleStatus;

    @TableField(value = "SpecialType")
    @JsonProperty("SpecialType")
    private String SpecialType;

    @TableField(value = "DealStatus")
    @JsonProperty("DealStatus")
    private Integer DealStatus;

    @TableField(value = "RecordType")
    @JsonProperty("RecordType")
    private Integer RecordType;

    @TableField(value = "DeviceStatus")
    @JsonProperty("DeviceStatus")
    private Integer DeviceStatus;

    @TableField(value = "VehicleLicense")
    @JsonProperty("VehicleLicense")
    private String VehicleLicense;

    @TableField(value = "LicensePlate")
    @JsonProperty("LicensePlate")
    private String LicensePlate;

    @TableField(value = "Toll")
    @JsonProperty("Toll")
    private Integer Toll;

    @TableField(value = "Arrearage")
    @JsonProperty("Arrearage")
    private Integer Arrearage;

    @TableField(value = "TollFee")
    @JsonProperty("TollFee")
    private Integer TollFee;

    @TableField(value = "FlagStation")
    @JsonProperty("FlagStation")
    private String FlagStation;

    @TableField(value = "MidStation")
    @JsonProperty("MidStation")
    private Integer MidStation;

    @TableField(value = "InvoiceID")
    @JsonProperty("InvoiceID")
    private String InvoiceID;

    @TableField(value = "PrintTimes")
    @JsonProperty("PrintTimes")
    private Integer PrintTimes;

    @TableField(value = "ECardID")
    @JsonProperty("ECardID")
    private String ECardID;

    @TableField(value = "ECardType")
    @JsonProperty("ECardType")
    private Integer ECardType;

    @TableField(value = "EBBalance")
    @JsonProperty("EBBalance")
    private Integer EBBalance;

    @TableField(value = "EABalance")
    @JsonProperty("EABalance")
    private Integer EABalance;

    @TableField(value = "TermCode")
    @JsonProperty("TermCode")
    private String TermCode;

    @TableField(value = "OBUID")
    @JsonProperty("OBUID")
    private String OBUID;

    @TableField(value = "OBUVersion")
    @JsonProperty("OBUVersion")
    private Integer OBUVersion;

    @TableField(value = "TAC")
    @JsonProperty("TAC")
    private String TAC;

    @TableField(value = "ETradNo")
    @JsonProperty("ETradNo")
    private String ETradNo;

    @TableField(value = "TermTradNo")
    @JsonProperty("TermTradNo")
    private String TermTradNo;

    @TableField(value = "IdentifyStatus")
    @JsonProperty("IdentifyStatus")
    private Integer IdentifyStatus;

    @TableField(value = "ImagePath")
    @JsonProperty("ImagePath")
    private String ImagePath;

    @TableField(value = "ProgramVer")
    @JsonProperty("ProgramVer")
    private String ProgramVer;

    @TableField(value = "FeeVer")
    @JsonProperty("FeeVer")
    private String FeeVer;

    @TableField(value = "VerifyCode")
    @JsonProperty("VerifyCode")
    private Integer VerifyCode;

    @TableField(value = "CancelFlag")
    @JsonProperty("CancelFlag")
    private Integer CancelFlag;

    @TableField(value = "AxisType")
    @JsonProperty("AxisType")
    private Integer AxisType;

    @TableField(value = "AxisNum")
    @JsonProperty("AxisNum")
    private Integer AxisNum;

    @TableField(value = "TotalWeight")
    @JsonProperty("TotalWeight")
    private Integer TotalWeight;

    @TableField(value = "LimitWeight")
    @JsonProperty("LimitWeight")
    private Integer LimitWeight;

    @TableField(value = "OverLoadRate")
    @JsonProperty("OverLoadRate")
    private Integer OverLoadRate;

    @TableField(value = "AxisInfo")
    @JsonProperty("AxisInfo")
    private String AxisInfo;

    @TableField(value = "OwnerInfo")
    @JsonProperty("OwnerInfo")
    private String OwnerInfo;

    @TableField(value = "Transfermark")
    @JsonProperty("Transfermark")
    private Integer Transfermark;

    @TableField(value = "PSAMID")
    @JsonProperty("PSAMID")
    private String PSAMID;

    @TableField(value = "TransType")
    @JsonProperty("TransType")
    private Integer TransType;

    @TableField(value = "AlgorithmID")
    @JsonProperty("AlgorithmID")
    private Integer AlgorithmID;

    @TableField(value = "DurationSec")
    @JsonProperty("DurationSec")
    private Integer DurationSec;

    @TableField(value = "CEntryNet")
    @JsonProperty("CEntryNet")
    private Integer CEntryNet;

    @TableField(value = "CEntryID")
    @JsonProperty("CEntryID")
    private String CEntryID;

    @TableField(value = "CEntryTime")
    @JsonProperty("CEntryTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CEntryTime;

    @TableField(value = "RAxisType")
    @JsonProperty("RAxisType")
    private Integer RAxisType;

    @TableField(value = "RTotalWeight")
    @JsonProperty("RTotalWeight")
    private Integer RTotalWeight;

    @TableField(value = "RLimitWeight")
    @JsonProperty("RLimitWeight")
    private Integer RLimitWeight;

    @TableField(value = "CFlagID")
    @JsonProperty("CFlagID")
    private String CFlagID;

    @TableField(value = "CCalcFeeInfo")
    @JsonProperty("CCalcFeeInfo")
    private String CCalcFeeInfo;

    @TableField(value = "CPCCalcInfo")
    @JsonProperty("CPCCalcInfo")
    private String CPCCalcInfo;

    @TableField(value = "StdRStationID")
    @JsonProperty("StdRStationID")
    private String StdRStationID;

    @TableField(value = "HexRtationID")
    @JsonProperty("HexRtationID")
    private String HexRtationID;

    @TableField(value = "HexRLaneID")
    @JsonProperty("HexRLaneID")
    private String HexRLaneID;

    @TableField(value = "StdEStationID")
    @JsonProperty("StdEStationID")
    private String StdEStationID;

    @TableField(value = "AssignPFFee")
    @JsonProperty("AssignPFFee")
    private String AssignPFFee;

    @TableField(value = "AssignPDFee")
    @JsonProperty("AssignPDFee")
    private String AssignPDFee;

    @TableField(value = "POwnerInfo")
    @JsonProperty("POwnerInfo")
    private String POwnerInfo;

    @TableField(value = "CalcFeeVer")
    @JsonProperty("CalcFeeVer")
    private String CalcFeeVer;

    @TableField(value = "PBStationID")
    @JsonProperty("PBStationID")
    private String PBStationID;

    @TableField(value = "CalcMode")
    @JsonProperty("CalcMode")
    private String CalcMode;

    @TableField(value = "Spare1")
    @JsonProperty("Spare1")
    private Integer Spare1;

    @TableField(value = "Spare2")
    @JsonProperty("Spare2")
    private Integer Spare2;

    @TableField(value = "Spare3")
    @JsonProperty("Spare3")
    private String Spare3;

    @TableField(value = "Spare4")
    @JsonProperty("Spare4")
    private Integer Spare4;

    @TableField(value = "Spare5")
    @JsonProperty("Spare5")
    private String Spare5;

    @TableField(value = "Spare6")
    @JsonProperty("Spare6")
    private String Spare6;

    @TableField(value = "LoadWeight")
    @JsonProperty("LoadWeight")
    private Integer LoadWeight;

    @TableField(value = "OBUVehicleDim")
    @JsonProperty("OBUVehicleDim")
    private String OBUVehicleDim;

    @TableField(value = "OBUType")
    @JsonProperty("OBUType")
    private Integer OBUType;

    @TableField(value = "GantryHex")
    @JsonProperty("GantryHex")
    private String GantryHex;

    @TableField(value = "OBUIssue")
    @JsonProperty("OBUIssue")
    private String OBUIssue;

    @TableField(value = "OBUVehicleClass")
    @JsonProperty("OBUVehicleClass")
    private Integer OBUVehicleClass;

    @TableField(value = "OBUVehiclePlate")
    @JsonProperty("OBUVehiclePlate")
    private String OBUVehiclePlate;

    @TableField(value = "OBUSerialID")
    @JsonProperty("OBUSerialID")
    private String OBUSerialID;

    @TableField(value = "KeyVersion")
    @JsonProperty("KeyVersion")
    private String KeyVersion;

    @TableField(value = "mobTermNo")
    @JsonProperty("mobTermNo")
    private String mobTermNo;

    @TableField(value = "PayIdentifier")
    @JsonProperty("PayIdentifier")
    private String PayIdentifier;

    @TableField(value = "ReplayTime")
    @JsonProperty("ReplayTime")
    private String ReplayTime;

    @TableField(value = "SettleDate")
    @JsonProperty("SettleDate")
    private String SettleDate;

    @TableField(value = "MobFee")
    @JsonProperty("MobFee")
    private Integer MobFee;

    @TableField(value = "MblPayPlatNumber")
    @JsonProperty("MblPayPlatNumber")
    private String MblPayPlatNumber;

    @TableField(value = "PayType")
    @JsonProperty("PayType")
    private Integer PayType;

    @TableField(value = "RecordClass")
    @JsonProperty("RecordClass")
    private Integer RecordClass;

    @TableField(value = "CardFee")
    @JsonProperty("CardFee")
    private Integer CardFee;

    @TableField(value = "OBUFee")
    @JsonProperty("OBUFee")
    private Integer OBUFee;

    @TableField(value = "minProvFee")
    @JsonProperty("minProvFee")
    private Integer minProvFee;

    @TableField(value = "selfFee")
    @JsonProperty("selfFee")
    private Integer selfFee;

    @TableField(value = "minFeeVer")
    @JsonProperty("minFeeVer")
    private String minFeeVer;

    @TableField(value = "OBUDiscount")
    @JsonProperty("OBUDiscount")
    private Integer OBUDiscount;

    @TableField(value = "GantPayFee")
    @JsonProperty("GantPayFee")
    private Integer GantPayFee;

    @TableField(value = "GantDiscount")
    @JsonProperty("GantDiscount")
    private Integer GantDiscount;

    @TableField(value = "StatsFlag")
    @JsonProperty("StatsFlag")
    private Integer StatsFlag;

    @TableField(value = "OBUDistance")
    @JsonProperty("OBUDistance")
    private Integer OBUDistance;

    @TableField(value = "FeeRate")
    @JsonProperty("FeeRate")
    private Integer FeeRate;

    @TableField(value = "CollDistance")
    @JsonProperty("CollDistance")
    private Integer CollDistance;

    @TableField(value = "minProvVMT")
    @JsonProperty("minProvVMT")
    private Integer minProvVMT;

    @TableField(value = "tradeGroup")
    @JsonProperty("tradeGroup")
    private String tradeGroup;

    @TableField(value = "selfTollFee")
    @JsonProperty("selfTollFee")
    private Integer selfTollFee;

    @TableField(value = "seflSumFee")
    @JsonProperty("seflSumFee")
    private Integer seflSumFee;

    @TableField(value = "selfSumPayFee")
    @JsonProperty("selfSumPayFee")
    private Integer selfSumPayFee;

    @TableField(value = "specialCode")
    @JsonProperty("specialCode")
    private String specialCode;

    @TableField(value = "AVehicleClass")
    @JsonProperty("AVehicleClass")
    private Integer AVehicleClass;

    @TableField(exist = false)
    @JsonIgnore
    private Date ListenTime;

    @TableField(exist = false)
    private String listenTableName;

    private static final long serialVersionUID = 1L;
}