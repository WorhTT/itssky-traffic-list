package com.itssky.common.enums;

import lombok.Getter;

/**
 * 交易状态枚举
 * 对应MySQL函数: fn_getdealstatusname
 */
@Getter
public enum DealStatusEnum {

    // ==================== 支付方式相关 ====================
    CASH("现金", "IdentifyStatus=0"),
    RECEIPT_CODE("收款码", "IdentifyStatus=0 AND ECardType=99"),
    TRAFFIC_CARD_DISCOUNT("运政卡优惠", "(IdentifyStatus=2 OR recordtype&16777216=16777216) AND vehicleStatus=105"),
    PREPAID_ACCOUNT("预付记帐", "(IdentifyStatus=2 OR recordtype&16777216=16777216) AND vehicleStatus<>105 AND (cardtype=23 OR (cardtype=26 AND ECardType=23))"),
    PREPAID_STORED("预付储值", "(IdentifyStatus=2 OR recordtype&16777216=16777216) AND vehicleStatus<>105 AND (cardtype=22 OR (cardtype=26 AND ECardType=22))"),

    // ==================== 电子支付方式 ====================
    UNION_PAY("银联支付", "IdentifyStatus=16 AND ECardType=1"),
    WECHAT_PAY("微信支付", "IdentifyStatus=16 AND ECardType=2"),
    ALIPAY("支付宝支付", "IdentifyStatus=16 AND ECardType=3"),
    BAIDU_PAY("百度支付", "IdentifyStatus=16 AND ECardType=4"),
    JD_PAY("京东支付", "IdentifyStatus=16 AND ECardType=5"),
    PASS_TREASURE_PAY("通行宝支付", "IdentifyStatus=16 AND ECardType=32"),
    DIGITAL_RMB_PAY("数字人民币支付", "IdentifyStatus=16 AND ECardType=33"),

    // ==================== 业务状态类型 ====================
    ARREARS("欠款", "dealstatus&14=4"),
    OFFICIAL_BUS("公务运营管理工作的公务客车", "dealstatus&14=6 AND vehicleStatus=1"),
    OFFICIAL_MAINTENANCE_INTERNAL("养护车以及辅助生产车(内部)", "dealstatus&14=6 AND vehicleStatus=2"),
    OFFICIAL_OTHER_PROVINCE("外省公务车辆", "dealstatus&14=6 AND vehicleStatus=3"),
    OFFICIAL_MAINTENANCE_EXTERNAL("维护单位车(外部)", "dealstatus&14=6 AND vehicleStatus=4"),
    OFFICIAL_POLICE("交警、路政工作车", "dealstatus&14=6 AND vehicleStatus=5"),
    MILITARY("军车", "dealstatus&14=8"),

    // ==================== 优惠类型 ====================
    DISCOUNT_CONTAINER("优惠集装箱", "dealstatus&14=10 AND vehicleStatus=1"),
    DISCOUNT_EMERGENCY("优惠应急车", "dealstatus&14=10 AND vehicleStatus=26"),
    DISCOUNT_VACCINE("优惠疫苗", "dealstatus&14=10 AND vehicleStatus=35"),
    DISCOUNT_DISASTER_RELIEF("优惠救灾应急", "dealstatus&14=10 AND vehicleStatus=36"),
    DISCOUNT_POSTAL("优惠邮政", "dealstatus&14=10 AND vehicleStatus=101"),
    DISCOUNT_RESCUE("优惠抢险救灾", "dealstatus&14=10 AND vehicleStatus=104"),
    DISCOUNT_HARVESTER("优惠收割机", "dealstatus&14=10 AND vehicleStatus=106"),
    DISCOUNT_STRAW("优惠秸杆车", "dealstatus&14=10 AND vehicleStatus=107"),
    DISCOUNT_TRAIN("优惠中欧班列", "dealstatus&14=10 AND vehicleStatus=111"),
    DISCOUNT_AMBULANCE("优惠急救车", "dealstatus&14=10 AND vehicleStatus=120"),
    GREEN_CHANNEL("绿色通道", "dealstatus&14=10 AND vehicleStatus=102"),
    GREEN_CHANNEL_NO_CHECK("绿优免检", "dealstatus&14=10 AND vehicleStatus=102 AND dealstatus&1073741824=1073741824"),

    // ==================== 特殊处理类型 ====================
    FREE_CONGESTION("免费拥堵", "dealstatus&14=12 AND vehicleStatus=2"),
    FLEET_NORMAL("车队普通", "dealstatus&14=14 AND vehicleStatus=0"),
    FLEET_CONGESTION("车队拥堵", "dealstatus&14=14 AND vehicleStatus=2"),
    FLEET_HOLIDAY("车队节假日", "dealstatus&14=14 AND vehicleStatus=22"),

    // ==================== 技术异常类型 ====================
    GEAR_SHIFT_PASSENGER_UP("变档客升档", "dealstatus&16=16 AND recordtype&229376=524288"),
    GEAR_SHIFT_PASSENGER_DOWN("变档客降档", "dealstatus&16=16 AND recordtype&229376=1048576"),
    GEAR_SHIFT_TO_CARGO("变档客变货", "dealstatus&16=16 AND recordtype&229376=2097152"),
    GEAR_SHIFT_TO_PASSENGER("变档货变客", "dealstatus&16=16 AND recordtype&229376=2621440"),
    GEAR_SHIFT_CARGO_UP("变档货升档", "dealstatus&16=16 AND recordtype&229376=3145728"),
    GEAR_SHIFT_CARGO_DOWN("变档货降档", "dealstatus&16=16 AND recordtype&229376=3670016"),

    NO_CARD("无卡", "dealstatus&32=32"),
    U_TURN("U型", "dealstatus&64=64"),
    NO_WEIGHT("无重", "dealstatus&512=512"),
    AXLE_WEIGHT_MODIFY("轴重修改", "dealstatus&1024=1024"),
    BAD_CARD("坏卡", "dealstatus&2048=2048"),
    MODIFIED("更改", "dealstatus&4096=4096"),
    CARD_VEHICLE_MISMATCH("车卡不符", "dealstatus&8192=8192"),
    OVER_LIMIT("超限", "dealstatus&16384=16384"),
    VIOLATION("违章", "dealstatus&65536=65536"),
    BOTTOM_CHARGING("兜底计费", "dealstatus&131072=131072"),
    BLACKLIST_ON_SITE_PAY("黑名单现场补缴", "dealstatus&524288=524288"),
    MANUAL_DROP("手动落杆", "dealstatus&1048576=1048576"),
    HIGH_FREQ_VEHICLE("高频车", "dealstatus&8388608=8388608"),
    ETC_TO_MANUAL("ETC交易转人工刷卡", "dealstatus&67108864=67108864"),
    OTHER_OVER_LIMIT_PASS("其他超限放行", "dealstatus&536870912=536870912"),
    JASMINE_PASS("茉莉畅行", "dealstatus&1073741824=1073741824 AND NOT(dealstatus&14=10 AND vehicleStatus=102)"),

    // ==================== 特殊车辆类型 ====================
    HAZARDOUS_CHEMICAL("危化品", "vehicleStatus=213"),
    CHARGE_FLEET("收费车队", "vehicleStatus=254"),
    OVERSIZE_TRANSPORT("大件运输", "vehicleStatus=212");

    private final String result;
    private final String condition;

    DealStatusEnum(String result, String condition) {
        this.result = result;
        this.condition = condition;
    }

}
