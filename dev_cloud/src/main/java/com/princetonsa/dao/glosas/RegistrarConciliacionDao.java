package com.princetonsa.dao.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarConciliacionDao;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoConceptoRespuesta;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;

import java.sql.Connection;


public interface RegistrarConciliacionDao
{
	public ArrayList<DtoConceptoRespuesta> consultarTiposConcepto();

	public int guardarConciliacion(HashMap<String, Object> mapa);

	public ArrayList<DtoFacturaGlosa> consultarFacturasGlosa(HashMap<String, Object> mapa);

	public String insertarFacturasRespGlosa(DtoRespuestaFacturaGlosa dtoRespuestaFacturaGlosa, int institucion);

	public ArrayList<DtoDetalleFacturaGlosa> consultaConceptosGlosa(String auditoria, String codigoTarifario);
	
	public String insertarDetalleFacturaConciliacion(Connection con, DtoDetalleFacturaGlosa dto, int institucion, DtoRespuestaFacturaGlosa dtoFactResp, double ajuste);
	
	public ArrayList<DtoDetalleAsociosGlosa> consultarAsociosDetFacturaResp(int detAuditoriaGlosa);

	public ArrayList<DtoRespuestaFacturaGlosa> consultaFacturasConciliacion(String codconciliacion);

	public ArrayList<DtoRespuestaSolicitudGlosa> consultaSolResp(int respuesta, String codTarifario, int institucion);

	public ArrayList<DtoFacturaGlosa> consultarConceptosGlosaFact(String auditoria);
}
