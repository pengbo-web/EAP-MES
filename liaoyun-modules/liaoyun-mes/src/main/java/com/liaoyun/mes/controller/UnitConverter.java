package com.liaoyun.mes.controller;

import com.liaoyun.mes.domain.MesUnitmeasure;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitConverter {

    /**
     * 单位换算方法
     * @param sourceUnit 源单位
     * @param targetUnit 目标单位
     * @param amount 数量
     * @param unitList 单位列表
     * @return 换算后的数量，如果无法换算则返回null
     */
    public static BigDecimal convertUnit(String sourceUnit, String targetUnit, BigDecimal amount, List<MesUnitmeasure> unitList) {
        if (sourceUnit.equals(targetUnit)) {
            return amount;
        }

        // 查找源单位和目标单位的信息
        MesUnitmeasure sourceUnitEntity = findUnitByName(sourceUnit, unitList);
        MesUnitmeasure targetUnitEntity = findUnitByName(targetUnit, unitList);

        if (sourceUnitEntity == null || targetUnitEntity == null) {
            throw new RuntimeException("未找到单位信息");
        }

        // 寻找共同的上级单位
        List<List<MesUnitmeasure>> conversionPath = findConversionPath(sourceUnitEntity, targetUnitEntity, unitList);
        if (conversionPath == null) {
            return null; // 表示这两个单位之间无法进行换算
        }

        List<MesUnitmeasure> sourcePath = conversionPath.get(0);
        List<MesUnitmeasure> targetPath = conversionPath.get(1);

        // 计算换算率
        BigDecimal result = amount;

        // 向上换算
        for (MesUnitmeasure unit : sourcePath) {
            if (unit.getGrow() != null && BigDecimal.valueOf(unit.getGrow()).compareTo(BigDecimal.ZERO) != 0) {
                result = result.divide(BigDecimal.valueOf(unit.getGrow()), 10, RoundingMode.HALF_UP);
            }
        }

        // 向下换算
        for (int i = targetPath.size() - 1; i >= 0; i--) {
            MesUnitmeasure unit = targetPath.get(i);
            if (unit.getGrow() != null && BigDecimal.valueOf(unit.getGrow()).compareTo(BigDecimal.ZERO) != 0) {
                result = result.multiply(BigDecimal.valueOf(unit.getGrow()));
            }
        }

        return result.setScale(6, RoundingMode.HALF_UP);
    }

    /**
     * 找到两个单位之间的换算路径
     */
    private static List<List<MesUnitmeasure>> findConversionPath(MesUnitmeasure source, MesUnitmeasure target, List<MesUnitmeasure> unitList) {
        // 获取源单位到顶级单位的路径
        List<MesUnitmeasure> sourcePath = new ArrayList<>();
        MesUnitmeasure currentSource = source;
        while (currentSource != null) {
            sourcePath.add(currentSource);
            if (currentSource.getRemark() == null) {
                break;
            }
            currentSource = findUnitById(Long.valueOf(currentSource.getRemark()), unitList);
        }

        // 获取目标单位到顶级单位的路径
        List<MesUnitmeasure> targetPath = new ArrayList<>();
        MesUnitmeasure currentTarget = target;
        while (currentTarget != null) {
            targetPath.add(currentTarget);
            if (currentTarget.getRemark() == null ) {
                break;
            }
            currentTarget = findUnitById(Long.valueOf(currentTarget.getRemark()), unitList);
        }

        // 找到共同的顶级单位
        MesUnitmeasure sourceTop = sourcePath.get(sourcePath.size() - 1);
        MesUnitmeasure targetTop = targetPath.get(targetPath.size() - 1);

        if (!sourceTop.getId().equals(targetTop.getId())) {
            return null; // 没有共同的顶级单位，无法换算
        }

        return Arrays.asList(sourcePath, targetPath);
    }

    private static MesUnitmeasure findUnitByName(String unitName, List<MesUnitmeasure> unitList) {
        return unitList.stream()
            .filter(unit -> unit.getTitle().equals(unitName))
            .findFirst()
            .orElse(null);
    }

    private static MesUnitmeasure findUnitById(Long id, List<MesUnitmeasure> unitList) {
        return unitList.stream()
            .filter(unit -> unit.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

}
