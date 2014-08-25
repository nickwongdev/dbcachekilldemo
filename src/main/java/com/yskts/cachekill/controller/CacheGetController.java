package com.yskts.cachekill.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yskts.cachekill.manager.RedisManager;
import com.yskts.cachekill.model.User;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleDriver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Nick on 8/24/2014.
 */
@Controller
public class CacheGetController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private RedisManager redisManager;

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String doGet(
            @PathVariable("userId") final String userId
    ) {
        // Check to see if it is in the Redis cache
        final String cachePayload = redisManager.get("USER", userId);
        if (cachePayload != null) {
            System.out.println("Cache hit!");
            return cachePayload;
        }
        System.out.println("Cache miss!");


        // Create the connection
        Connection conn = null;
        Properties props = new Properties();
        props.put("user", "sandbox");
        props.put("password", "sand123");

        try {
            OracleDriver drv = new OracleDriver();
            conn = drv.connect("jdbc:oracle:thin:@localhost:1521/orcl",
                    props);
            String select = "SELECT ID, USERNAME, PASSWORD, DESCRIPTION FROM SANDBOX.APPUSER WHERE ID = " + userId;
            final OracleCallableStatement cs =(OracleCallableStatement) conn.prepareCall(select);
            cs.setRowPrefetch(1);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                final User u = new User();
                u.setUserId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setDescription(rs.getString(4));
                String payload = OBJECT_MAPPER.writeValueAsString(u);
                redisManager.put("USER", userId, payload);
                return payload;
            }
        } catch (final SQLException | JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
