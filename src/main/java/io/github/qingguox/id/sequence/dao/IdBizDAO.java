package io.github.qingguox.id.sequence.dao;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import io.github.qingguox.id.sequence.model.IdBiz;

/**
 * @author wangqingwei
 * Created on 2022-08-06
 */
@Repository
public class IdBizDAO {

    private static final String TABLE = "`id_biz`";

    private final BeanPropertyRowMapper<IdBiz> rowMapper = new BeanPropertyRowMapper<>(IdBiz.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(IdBiz biz) {
        String sql = "insert into " + TABLE + "(id, biz_type, rule) values (:id, :bizType, :rule)";
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(biz));
    }

    public IdBiz geByBizType(String bizType) {
        String sql = "select * from " + TABLE + " where biz_type=:biz_type";
        final List<IdBiz> idBizList =
                jdbcTemplate.query(sql, new MapSqlParameterSource("biz_type", bizType), rowMapper);
        return CollectionUtils.isNotEmpty(idBizList) ? idBizList.get(0) : null;
    }
}