package com.liaoyun.mes.service;

import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.vo.MesInstockVo;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.bo.MesProductBo;
import com.liaoyun.mes.domain.vo.MesStockVo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ③产品管理Service接口
 *
 * @author Kylin W
 * @date 2024-05-11
 */
public interface IMesProductService {

    /**
     * 查询③产品管理
     */
    MesProductVo queryById(Long id);


    TableDataInfo<MesProductVo> queryPageList(MesProductBo bo, PageQuery pageQuery) ;
    /**
     * 查询③产品管理列表
     */
    List<MesProductVo> queryList(MesProductBo bo);

    List<MesStockVo> getbom(Long bo);


    List<MesProductVo> getboml(Long bo);

    /**
     * 新增③产品管理
     */
    Boolean insertByBo(MesProductBo bo);

    Boolean insertsByBo(MesProductBo bo,List<MesProductBo> list);


    List<MesProductVo> getallstock(Long id,String type);
    /**
     * 修改③产品管理
     */
    Boolean updateByBo(MesProductBo bo);

    /**
     * 校验并批量删除③产品管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
