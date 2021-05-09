package com.plancer.dao;

import com.plancer.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

/**
 * @author plancer16
 * @create 2021/5/9 17:40
 */
@Service
@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " id, name, password, salt, head_Url ";//跟db中的字段名一致
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{id},#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id =#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name =#{name}"})
    User selectByName(String name);

    @Update({"update", TABLE_NAME, "set password = #{password} where id=#{id}"})
    int updatePassword(User user);

    @Delete({"delete from", TABLE_NAME, "where id = #{id}"})
    int deleteById(int id);
}
