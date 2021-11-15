package com.wang.mongodb.repository;

import com.wang.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author wangyu
 * @date 2021/11/15 12:54
 */
public interface UserRespository extends MongoRepository<User,String> {
}
