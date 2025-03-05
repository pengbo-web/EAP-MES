package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.MesMobileRole;
import com.liaoyun.mes.domain.vo.MesMURVo;
import com.liaoyun.mes.domain.vo.MesMobileRoleVo;
import com.liaoyun.mes.mapper.MesMURMapper;
import com.liaoyun.mes.mapper.MesMobileRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesMobileMenuBo;
import com.liaoyun.mes.domain.vo.MesMobileMenuVo;
import com.liaoyun.mes.domain.MesMobileMenu;
import com.liaoyun.mes.mapper.MesMobileMenuMapper;
import com.liaoyun.mes.service.IMesMobileMenuService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 移动端目录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@RequiredArgsConstructor
@Service
public class MesMobileMenuServiceImpl implements IMesMobileMenuService {

    private final MesMobileMenuMapper baseMapper;
    private final MesMURServiceImpl murMapper;
    private final MesMobileRoleMapper roleMapper;

    /**
     * 查询移动端目录
     *
     * @param id 主键
     * @return 移动端目录
     */
    @Override
    public MesMobileMenuVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询移动端目录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 移动端目录分页列表
     */
    @Override
    public TableDataInfo<MesMobileMenuVo> queryPageList(MesMobileMenuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesMobileMenu> lqw = buildQueryWrapper(bo);
        Page<MesMobileMenuVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的移动端目录列表
     *
     * @param bo 查询条件
     * @return 移动端目录列表
     */
    @Override
    public List<MesMobileMenuVo> queryList(MesMobileMenuBo bo) {
        LambdaQueryWrapper<MesMobileMenu> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }
    @Override
    public List<MesMobileMenuVo> getmylist() {
        Long userId = LoginHelper.getUserId();
        // 查询 MesMobileRole 列表
        List<MesMobileRole> roleList = roleMapper.selectByUserIdt(userId.toString());
        List<Long> ms = new ArrayList<>();
        for (MesMobileRole role : roleList) {
            Long menuId = Long.valueOf(role.getMenuids());
            // 查询 MesMURVo 对象
            MesMURVo vo = murMapper.queryById(menuId);
            if (vo != null && vo.getIdsarray() != null) {
                for (String id : vo.getIdsarray()) {
                    ms.add(Long.parseLong(id));
                }
            }
        }
        List<MesMobileMenuVo> list=new ArrayList<>();
        for (Long id:ms){
            list.add(baseMapper.selectVoById(id));
        }
        List<MesMobileMenuVo> distinctList = list.stream()
            .distinct()
            .sorted(Comparator.comparing(MesMobileMenuVo::getCreateTime).reversed())
            .collect(Collectors.toList());
        return distinctList;
    }

    private LambdaQueryWrapper<MesMobileMenu> buildQueryWrapper(MesMobileMenuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesMobileMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesMobileMenu::getTitle, bo.getTitle());
        lqw.eq(bo.getType() != null, MesMobileMenu::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增移动端目录
     *
     * @param bo 移动端目录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesMobileMenuBo bo) {
        MesMobileMenu add = MapstructUtils.convert(bo, MesMobileMenu.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改移动端目录
     *
     * @param bo 移动端目录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesMobileMenuBo bo) {
        MesMobileMenu update = MapstructUtils.convert(bo, MesMobileMenu.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesMobileMenu entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除移动端目录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
