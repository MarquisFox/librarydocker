package ru.lab.librarydocker.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class SqlUtils {

    private SqlUtils() {
    }

    public static Long getLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    public static Integer getInt(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    public static LocalDate getLocalDate(ResultSet rs, String columnName) throws SQLException {
        java.sql.Date date = rs.getDate(columnName);
        return date != null ? date.toLocalDate() : null;
    }
}
