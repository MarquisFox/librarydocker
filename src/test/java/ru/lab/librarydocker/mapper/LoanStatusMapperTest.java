package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.entity.LoanStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanStatusMapperTest {

    @Mock
    private ResultSet rs;

    private final LoanStatusMapper mapper = new LoanStatusMapper();

    @Test
    void mapRow_shouldMapIdAndName() throws SQLException {
        when(rs.getLong("id")).thenReturn(2L);
        when(rs.getString("name")).thenReturn("RETURNED");

        LoanStatus status = mapper.mapRow(rs, 1);

        assertThat(status.getId()).isEqualTo(2L);
        assertThat(status.getName()).isEqualTo("RETURNED");
    }
}