package com.chngc.job.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.chngc.job.util.JsonUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;


/**
 * 用于{@link }的cfgData属性。
 * Java类型为Map&lt;String, Object&gt;，数据库字段为text，存储Map对象的JSON字符串。
 * JobCfgDataTypeHandler用于数据存、取时进行 Map&lt;String, Object&gt; &lt;=&gt; JSON字符串 之间转换。
 * @version 1.0
 */
@MappedTypes(value = Map.class)
@MappedJdbcTypes(value = { JdbcType.VARBINARY, JdbcType.LONGVARCHAR })
public class SysJobCfgDataTypeHandler implements TypeHandler<Map<String, Object>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Map<String, Object> parameter,
                             JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, JdbcType.VARCHAR.TYPE_CODE);
            return;
        }
        ps.setString(i, JsonUtil.toJson(parameter));
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.fromJson(rs.getString(columnName));
    }

    @Override
    public Map<String, Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.fromJson(cs.getString(columnIndex));
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.fromJson(rs.getString(columnIndex));
    }

}
