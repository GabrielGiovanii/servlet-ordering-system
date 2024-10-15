package com.servlet_ordering_system.models.daos.contracts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface RelationalMapping<T> {

    T objectRelationalMapping(ResultSet rs) throws SQLException;

    T objectRelationalMapping(ResultSet rs, Set<T> objs) throws SQLException;
}
