package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.IndicativoSolicitudGrupoServiciosDao;
import com.princetonsa.dao.sqlbase.SqlBaseIndicativoSolicitudGrupoServiciosDao;

public class OracleIndicativoSolicitudGrupoServiciosDao implements
		IndicativoSolicitudGrupoServiciosDao
{

	public HashMap consultarGruposServiciosInstitucion(Connection con, String institucion, String tipo)
	{
		return SqlBaseIndicativoSolicitudGrupoServiciosDao.consultarGruposServiciosInstitucion(con,institucion,tipo);
	}

	public HashMap consultarServiciosGrupoServicioTipo(Connection con, String grupoServicio, String tipoServicio)
	{
		return SqlBaseIndicativoSolicitudGrupoServiciosDao.consultarServiciosGrupoServicioTipo(con,grupoServicio,tipoServicio);
	}

	public boolean actualizarServicioProcedimiento(Connection con, String codigoServicio, String tomaMuestra, String respuestaMultiple)
	{
		return SqlBaseIndicativoSolicitudGrupoServiciosDao.actualizarServicioProcedimiento(con,codigoServicio,tomaMuestra,respuestaMultiple);
	}

}
