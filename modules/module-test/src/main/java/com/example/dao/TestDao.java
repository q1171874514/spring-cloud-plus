package com.example.dao;

import com.example.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDao extends BaseDao<TestEntity> {
}
