package com.princetonsa.dao.oracle.glosas;

import com.princetonsa.dao.glosas.ReporteEstadoCarteraGlosasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseReporteEstadisticoConsultaExDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseReporteEstadoCarteraGlosasDao;

import java.sql.SQLException;
import java.util.HashMap;

public class OracleReporteEstadoCarteraGlosasDao implements ReporteEstadoCarteraGlosasDao
{
	public HashMap ejecutarQuery(String consulta)
	{
		return SqlBaseReporteEstadoCarteraGlosasDao.ejecutarQuery(consulta);
	}
}