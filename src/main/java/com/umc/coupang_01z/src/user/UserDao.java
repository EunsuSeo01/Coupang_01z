package com.umc.coupang_01z.src.user;

import com.umc.coupang_01z.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // [POST] 회원가입
    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User (email, pw, name, phoneNum) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[] {
                postUserReq.getEmail(), postUserReq.getPw(), postUserReq.getName(), postUserReq.getPhoneNum()
        };

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // Validation : 이미 가입된 이메일인지 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
    }
}
