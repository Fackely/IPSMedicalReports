package com.princetonsa.dao.oracle.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.glosas.RegistrarConciliacionDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarConciliacionDao;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoConceptoRespuesta;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;

import java.sql.Connection;


public class OracleRegistrarConciliacionDao implements RegistrarConciliacionDao
{
	public ArrayList<DtoConceptoRespuesta> consultarTiposConcepto()
	{
		return SqlBaseRegistrarConciliacionDao.consultarTiposConcepto();
	}
	
	public int guardarConciliacion(HashMap<String, Object> mapa)
	{
		return SqlBaseRegistrarConciliacionDao.guardarConciliacion(mapa);
	}
	
	public ArrayList<DtoFacturaGlosa> consultarFacturasGlosa(HashMap<String, Object> mapa)
	{
		return SqlBaseRegistrarConciliacionDao.consultarFacturasGlosa(mapa);
	}
	
	public String insertarFacturasRespGlosa(DtoRespuestaFacturaGlosa dtoRespuestaFacturaGlosa, int institucion)
	{
		return SqlBaseRegistrarConciliacionDao.insertarFacturasRespGlosa(dtoRespuestaFacturaGlosa, institucion);
	}
	
	public ArrayList<DtoDetalleFacturaGlosa> consultaConceptosGlosa(String auditoria, String codigoTarifario)
	{
		return SqlBaseRegistrarConciliacionDao.consultaConceptosGlosa(auditoria, codigoTarifario);
	}
	
	public String insertarDetalleFacturaConciliacion(Connection con, DtoDetalleFacturaGlosa dto, int institucion, DtoRespuestaFacturaGlosa dtoFactResp, double ajuste) 
	{
		return SqlBaseRegistrarConciliacionDao.insertarDetalleFacturaConciliacion(con, dto, institucion, dtoFactResp, ajuste);
	}
	
	public ArrayList<DtoDetalleAsociosGlosa> consultarAsociosDetFacturaResp(int detAuditoriaGlosa)
	{
		return SqlBaseRegistrarConciliacionDao.consultarAsociosDetFacturaResp(detAuditoriaGlosa);
	}
	
	public ArrayList<DtoRespuestaFacturaGlosa> consultaFacturasConciliacion(String codconciliacion)
	{
		return SqlBaseRegistrarConciliacionDao.consultaFacturasConciliacion(codconciliacion);
	}
	
	public ArrayList<DtoRespuestaSolicitudGlosa> consultaSolResp(int respuesta, String codTarifario, int institucion)
	{
		return SqlBaseRegistrarConciliacionDao.consultaSolResp(respuesta, codTarifario, institucion);
	}
	
	public ArrayList<DtoFacturaGlosa> consultarConceptosGlosaFact(String auditoria)
	{
		return SqlBaseRegistrarConciliacionDao.consultarConceptosGlosaFact(auditoria);
	}
}