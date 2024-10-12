package com.servlet_ordering_system.models.daos.contracts;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RelationalMapping<T> {

    T objectRelationalMapping(ResultSet rs) throws SQLException;
}
