package com.wang.mongodb;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wang.mongodb.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SpringBootTest
class MongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void create(){
        User user = new User();
        user.setAge(23);
        user.setName("test2");
        user.setEmail("321@qq.com");

        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }


    @Test
    public void findAll(){
        List<User> all = mongoTemplate.findAll(User.class);
        System.out.println(all);
    }

    @Test
    public void findById(){
        User byId = mongoTemplate.findById("6191e13731a45b5e98621c61", User.class);
        System.out.println(byId);
    }

    @Test
    public void findByNameAndAge(){
        Query query = new Query(Criteria.where("name").is("test").and("age").is(21));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    @Test
    public void findByLike(){
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    @Test
    public void findUsersPage() {
        String name = "est";
        int pageNo = 1;
        int pageSize = 10;

        Query query = new Query();
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("name").regex(pattern));
        int totalCount = (int) mongoTemplate.count(query, User.class);
        List<User> userList = mongoTemplate.find(query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("list", userList);
        pageMap.put("totalCount",totalCount);
        System.out.println(pageMap);
    }

    //修改
    @Test
    public void updateUser() {
        User user = mongoTemplate.findById("6191e13731a45b5e98621c61", User.class);
        user.setName("test_1");
        user.setAge(25);
        user.setEmail("493220990@qq.com");
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("email", user.getEmail());
        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        long count = result.getModifiedCount();
        System.out.println(count);
    }

    //删除操作
    @Test
    public void delete() {
        Query query =
                new Query(Criteria.where("_id").is("6191e13731a45b5e98621c61"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        long count = result.getDeletedCount();
        System.out.println(count);
    }


}
