package io.github.qingguox.id.sequence.dao;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import io.github.qingguox.id.sequence.model.IdGen;

/**
 * @author wangqingwei
 * Created on 2022-08-06
 */
@Repository
public class IdGenDAO {

    private static final String TABLE = "`id_gen`";

    private final BeanPropertyRowMapper<IdGen> rowMapper = new BeanPropertyRowMapper<>(IdGen.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(IdGen idGen) {
        String sql = "insert into " + TABLE
                + "(id, max_id, step, version, biz_id) values (:id, :maxId, :step, :version, :bizId)";
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(idGen));
    }

    public IdGen getByBizId(long bizId) {
        String sql = "select * from " + TABLE + " where biz_id=:bizId";
        final List<IdGen> idGenList = jdbcTemplate.query(sql, new MapSqlParameterSource("bizId", bizId), rowMapper);
        return CollectionUtils.isNotEmpty(idGenList) ? idGenList.get(0) : null;
    }

    public int updateByIdAndVersion(long id, long version, long nextMaxId) {
        String sql = "update " + TABLE + " set version=:newVersion, max_id =:newMaxId where id=:id and version=:version";
        return jdbcTemplate.update(sql, new MapSqlParameterSource("id", id)
                .addValue("version", version)
                .addValue("newVersion", version + 1)
                .addValue("newMaxId", nextMaxId));
    }
}