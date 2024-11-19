package com.itssky.report.domain;

import com.itssky.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ITSSKY
 * 高速收费报表信息
 */
@Data
@Accessors(chain = true)
public class ReportChargeInfo {

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 统计金额
     */
    private String tjje;

    /**
     * 应缴金额
     */
    private String yjje;

    /**
     * 实缴金额
     */
    private String sjje;

    /**
     * 金额差异
     */
    private String jecy;

    /**
     * ---------欠款---------
     */

    /**
     * 车次
     */
    private String qkcc;

    /**
     * 金额
     */
    private String qkje;

    /**
     * ---------------------
     */

    /**
     * ---------加收款---------
     */

    /**
     * 现金
     */
    private String jsxj;

    /**
     * 金额
     */
    private String jsje;

    /**
     * 合计
     */
    private String jshj;

    /**
     * 次数
     */
    private String jscs;

    /**
     * ---------------------
     */

    /**
     * 移动支付
     */
    private String ydzf;

    /**
     * ---------电子支付消费额---------
     */

    /**
     * 储值卡
     */
    private String dzczk;

    /**
     * 记账卡
     */
    private String dzjzk;

    /**
     * 合计
     */
    private String dzhj;

    /**
     * -----------------------------
     */

    /**
     * 打印票据现金张数
     */
    private String xjpjzs;

    /**
     * 打印票据现金打票金额
     */
    private String xjdpje;

    /**
     * 打印票据移动支付张数
     */
    private String ydzfzs;

    /**
     * 打印票据移动支付金额
     */
    private String ydzfdpje;

    /**
     * 打印票据合计
     */
    private String dyphj;

    /**
     * 定额票据张数
     */
    private String depjzs;

    /**
     * 定额票据金额
     */
    private String depjje;

    /**
     * 废票张数
     */
    private String fpzs;

    /**
     * 废票金额
     */
    private String fpje;

    /**
     * 公务IC卡
     */
    private String gwic;

    /**
     * 军车IC卡
     */
    private String jcic;

    /**
     * 免费IC卡
     */
    private String mfic;

    /**
     * 应缴IC卡
     */
    private String yjic;
}
