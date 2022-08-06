package io.github.qingguox.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author wangqingwei <wangqingwei@kuaishou.com>
 * Created on 2022-08-06
 */
@Repository
public class UserDao {

    private static final String TABLE = "user";

    private final BeanPropertyRowMapper<User> rowMapper = new AdvancedBeanPropertyRowMapper<>(User.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(User user) {
        String sql = "insert into " + TABLE + "(id, name) values (:id, :name)";
        return jdbcTemplate.update(sql, new AdvancedBeanPropertySqlParameterSource(user));
    }

    public List<User> geByLimit(int limit) {
        String sql = "select * from " + TABLE + " limit :limit";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("limit", limit), rowMapper);
    }
}
