package com.itssky.common.utils;

import com.itssky.common.enums.DealStatusEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易状态判断工具类
 */
public class DealStatusEvaluator {

    /**
     * 根据参数获取匹配的交易状态描述
     */
    public static String getDealStatusName(int dealstatus, int vehicleStatus,
                                           int IdentifyStatus, int ECardType,
                                           int cardtype, int recordtype) {

        StringBuilder resultStr = new StringBuilder();

        for (DealStatusEnum status : DealStatusEnum.values()) {
            if (evaluateCondition(status.getCondition(), dealstatus, vehicleStatus,
                    IdentifyStatus, ECardType, cardtype, recordtype)) {
                resultStr.append(status.getResult()).append(" ");
            }
        }

        return resultStr.toString().trim();
    }

    /**
     * 条件判断核心逻辑
     */
    private static boolean evaluateCondition(String condition, int dealstatus,
                                             int vehicleStatus, int IdentifyStatus,
                                             int ECardType, int cardtype, int recordtype) {

        // 替换条件表达式中的变量为实际值
        String evaluatedCondition = condition
                .replace("dealstatus", String.valueOf(dealstatus))
                .replace("vehicleStatus", String.valueOf(vehicleStatus))
                .replace("IdentifyStatus", String.valueOf(IdentifyStatus))
                .replace("ECardType", String.valueOf(ECardType))
                .replace("cardtype", String.valueOf(cardtype))
                .replace("recordtype", String.valueOf(recordtype));

        try {
            // 使用JavaScript引擎进行表达式求值（简单实现）
            return evaluateExpression(evaluatedCondition);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean evaluateExpression(String expression) {
        // 简化的表达式求值，实际项目中可以使用Expression解析库
        if (expression.contains("AND")) {
            String[] parts = expression.split("AND");
            for (String part : parts) {
                if (!evaluateSimpleCondition(part.trim())) {
                    return false;
                }
            }
            return true;
        } else if (expression.contains("OR")) {
            String[] parts = expression.split("OR");
            for (String part : parts) {
                if (evaluateSimpleCondition(part.trim())) {
                    return true;
                }
            }
            return false;
        } else {
            return evaluateSimpleCondition(expression);
        }
    }

    private static boolean evaluateSimpleCondition(String condition) {
        // 处理基本条件判断
        if (condition.contains("&")) {
            String[] parts = condition.split("&");
            int left = Integer.parseInt(parts[0].trim());
            String[] rightParts = parts[1].split("=");
            int right = Integer.parseInt(rightParts[0].trim());
            int expected = Integer.parseInt(rightParts[1].trim());

            return (left & right) == expected;
        } else if (condition.contains("=")) {
            String[] parts = condition.split("=");
            int left = Integer.parseInt(parts[0].trim());
            int right = Integer.parseInt(parts[1].trim());
            return left == right;
        } else if (condition.contains("<>")) {
            String[] parts = condition.split("<>");
            int left = Integer.parseInt(parts[0].trim());
            int right = Integer.parseInt(parts[1].trim());
            return left != right;
        }
        return false;
    }

    /**
     * 获取特定类型的交易状态
     */
    public static List<DealStatusEnum> getMatchingStatuses(int dealstatus, int vehicleStatus,
                                                           int IdentifyStatus, int ECardType,
                                                           int cardtype, int recordtype) {
        return Arrays.stream(DealStatusEnum.values())
                .filter(status -> evaluateCondition(status.getCondition(), dealstatus,
                        vehicleStatus, IdentifyStatus, ECardType, cardtype, recordtype))
                .collect(Collectors.toList());
    }
}
