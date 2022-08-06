package io.github.qingguox.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author wangqingwei
 * Created on 2022-08-06
 */
@Repository
public class GroupDao {

    private static final String TABLE = "`group`";

    private final BeanPropertyRowMapper<Group> rowMapper = new AdvancedBeanPropertyRowMapper<>(Group.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(Group group) {
        String sql = "insert into " + TABLE + "(id, name, users) values (:id, :name, :users)";
        return jdbcTemplate.update(sql, new AdvancedBeanPropertySqlParameterSource(group));
    }

    public List<Group> geByLimit(int limit) {
        String sql = "select * from " + TABLE + " limit :limit";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("limit", limit), rowMapper);
    }
}
