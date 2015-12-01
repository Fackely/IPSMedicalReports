package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.sysmedica.dao.FichasAnterioresDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichasAnterioresDao;

public class OracleFichasAnterioresDao implements FichasAnterioresDao {

	public Collection consultaFichasPorPaciente(Connection con, 
												int codigoPaciente, 
												String diagnostico, 
												String codigoDx)
	{
		return SqlBaseFichasAnterioresDao.consultaFichasPorPaciente(con,codigoPaciente,diagnostico,codigoDx);
	}
}
