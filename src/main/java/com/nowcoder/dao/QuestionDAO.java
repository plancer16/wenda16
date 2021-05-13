package com.nowcoder.dao;

import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author plancer16
 * @create 2021/5/11 17:15
 */
@Service
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = "title, content, user_id, created_date, comment_count";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title}, #{content}, #{userId}, #{createdData}, #{commentCount})"})
    int addQuestion(Question question);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id = #{id}"})
    Question selectById(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update", TABLE_NAME, "set comment_count = #{commentCount} where id = #{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int CommentCount);//入参为2-5时用@Param，进service调用
}

