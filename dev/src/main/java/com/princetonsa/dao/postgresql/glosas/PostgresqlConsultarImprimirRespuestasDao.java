package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.princetonsa.dao.glosas.ConsultarImprimirRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConsultarImprimirRespuestasDao;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;

public class PostgresqlConsultarImprimirRespuestasDao implements ConsultarImprimirRespuestasDao
{
	/**
	 * Metodo para consultar la informaci�n de las glosas
	 * 
	 */
	public ArrayList<DtoGlosa> listarGlosas(Connection con,HashMap<String, Object> listadoGlosasMap){
		return SqlBaseConsultarImprimirRespuestasDao.listarGlosas(con, listadoGlosasMap);
	}
	
	/**
	 * M�todo para consultar las respuestas de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public DtoGlosa consultarRespuestaFacturasGlosa(Connection con, DtoGlosa glosa){
		return SqlBaseConsultarImprimirRespuestasDao.consultarRespuestaFacturasGlosa(con, glosa);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoRespuestaFactura
	 * @return
	 */
	public DtoRespuestaFacturaGlosa consultarRespuestaSolicitudesGlosa(Connection con, DtoRespuestaFacturaGlosa dtoRespuestaFactura, int institucion){
		return SqlBaseConsultarImprimirRespuestasDao.consultarRespuestaSolicitudesGlosa(con, dtoRespuestaFactura, institucion);
	}
}