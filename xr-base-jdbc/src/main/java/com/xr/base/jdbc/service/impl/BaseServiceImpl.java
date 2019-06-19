package com.xr.base.jdbc.service.impl;

import com.xr.base.core.enums.Cluster;
import com.xr.base.core.model.BaseModel;
import com.xr.base.core.page.PageData;
import com.xr.base.core.util.AssertUtils;
import com.xr.base.core.util.CollectionUtils;
import com.xr.base.core.util.DateUtil;
import com.xr.base.core.util.MapUtils;
import com.xr.base.core.util.ReflectUtils;
import com.xr.base.core.util.StringUtils;
import com.xr.base.jdbc.mapper.IBaseMapper;
import com.xr.base.jdbc.service.IBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据基础操作实现
 * Created by forvoyager@outlook.com on 2019-01-31 14:01.
 */
@Transactional(propagation = Propagation.REQUIRED)
public abstract class BaseServiceImpl<M extends IBaseMapper<T>, T> implements IBaseService<T> {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  private final String dbType = "MYSQL";

  @Autowired
  protected M baseMapper;

  @Override
  public T insert(T entity) throws Exception {

    AssertUtils.notNull(entity, "insert failed, with invalid param value.");
    BaseModel baseModel = (BaseModel)entity;
    if(baseModel.getCreate_time() == null){
      baseModel.setCreate_time(DateUtil.currentTimeInSecond());
    }
    baseModel.setVersion(0);
    baseModel.setUpdate_time(baseModel.getCreate_time());

    this.baseMapper.insert(entity);
    return entity;
  }

  @Override
  public int insertBatch(List<T> entityList) throws Exception {

    AssertUtils.notEmpty(entityList, "insert batch failed, with invalid param value.");

    this.baseMapper.insertBatch(entityList);
    return entityList.size();
  }

  @Override
  public T insertOrUpdate(T entity) throws Exception {

    AssertUtils.notNull(entity, "insert or update failed, with invalid param value.");

    Object idVal = ReflectUtils.getMethodValue(entity, this.getPrimaryKeyName());
    if (StringUtils.isEmpty(idVal)) {
      entity = insert(entity);
    } else {
      if (1 == update(entity)) {
        entity = selectOne(MapUtils.newHashMap(this.getPrimaryKeyName(), idVal), Cluster.master);
      } else {
        entity = insert(entity);
      }
    }

    return entity;
  }

  @Override
  public int deleteById(Serializable id) throws Exception {

    AssertUtils.notNull(id, "delete failed, with invalid primary key id.");

    return (int)this.deleteByMap(MapUtils.newHashMap(this.getPrimaryKeyName(), id));
  }

  @Override
  public long deleteByIds(Collection<? extends Serializable> idList) throws Exception {

    AssertUtils.notEmpty(idList, "delete batch by id failed, with invalid param value.");

    return this.deleteByMap(MapUtils.newHashMap("idList", idList));
  }

  @Override
  public long deleteByMap(Map<String, Object> condition) throws Exception {

    AssertUtils.notEmpty(condition, "delete failed, with invalid condition.");

    return this.baseMapper.delete(condition);
  }

  @Override
  public long update(T entity) throws Exception {

    AssertUtils.notNull(entity, "update failed, with invalid param value.");
    BaseModel baseModel = (BaseModel)entity;
    if(baseModel.getUpdate_time() == null){
      baseModel.setUpdate_time(DateUtil.currentTimeInSecond());
    }

    return this.updateByMap(ReflectUtils.javaBeanToMap(entity));
  }

  @Override
  public long updateByMap(Map<String, Object> columnMap) throws Exception {

    AssertUtils.notEmpty(columnMap, "update failed, with invalid condition.");

    return this.baseMapper.update(columnMap);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public T selectById(Serializable id, Cluster cluster) throws Exception {

    if(id == null){ return null; }

    return this.selectOne(MapUtils.newHashMap(this.getPrimaryKeyName(), id), cluster);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public List<T> selectByIds(Collection<? extends Serializable> idList, Cluster cluster) throws Exception {
    if(CollectionUtils.isEmpty(idList)){
      return Collections.EMPTY_LIST;
    }

    return this.selectList(MapUtils.newHashMap("idList", idList), cluster);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public T selectOne(Map<String, Object> condition, Cluster cluster) throws Exception {
    List<T> data = this.baseMapper.selectList(condition);
    return CollectionUtils.isEmpty(data) ? null : data.get(0);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public List<T> selectList(Map<String, Object> condition, Cluster cluster) throws Exception {

    AssertUtils.notEmpty(condition, "select failed, with invalid condition.");

    return this.baseMapper.selectList(condition);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public Map<String, T> selectMap(Map<String, Object> condition, Cluster cluster) throws Exception {

    Map<String, T> primaryKeyMapData = new HashMap<String, T>();

    List<T> datas = this.selectList(condition, cluster);
    for(T data : datas){
      primaryKeyMapData.put(ReflectUtils.getMethodValue(data, this.getPrimaryKeyName()).toString(), data);
    }

    return primaryKeyMapData;
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public long selectCount(Map<String, Object> condition, Cluster cluster) throws Exception {

    AssertUtils.notEmpty(condition, "select failed, with invalid condition.");

    return this.baseMapper.selectCount(condition);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public PageData<T> selectPage(int page, int size, Map<String, Object> condition, Cluster cluster) throws Exception {

    // 默认返回第1页
    page = page < 1 ? 1 : page;

    // 默认每页10条
    int pageSize = size < 1 ? 10 : size;

    // 计算page页的起始行位置
    long pageStartIndex = (page -1)*size;

    if(condition == null){
      condition = new HashMap<>();
    }

    condition.put("pagesize", pageSize);
    condition.put("pagestartindex", pageStartIndex);
    condition.put("dbType", dbType);

    // 查询总记录数
    long records = this.selectCount(condition, cluster);

    // 查询当前页数据
    List<T> data = this.selectList(condition, cluster);

    PageData<T> pageData = new PageData<T>();
    pageData.setPage(page);
    pageData.setSize(pageSize);
    pageData.setCondition(condition);
    pageData.setRecords(records);
    pageData.setPages( (int) (records/size) + (records%size > 0 ? 1 : 0) );
    pageData.setData(data);

    return pageData;
  }


  /**
   * 主键名称
   * @return
   */
  protected abstract String getPrimaryKeyName();
}
