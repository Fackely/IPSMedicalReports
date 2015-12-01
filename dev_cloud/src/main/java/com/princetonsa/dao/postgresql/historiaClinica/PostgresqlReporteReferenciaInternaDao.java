package com.princetonsa.dao.postgresql.historiaClinica;


import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dao.historiaClinica.ReporteReferenciaInternaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseReporteReferenciaInternaDao;

/**
 * @author Felipe Perez
 * Fecha: Septiembre de 2008
 */

public class PostgresqlReporteReferenciaInternaDao implements ReporteReferenciaInternaDao
{
	/**
	 * Consulta genérica de reporte referencia interna
	 */
	public static final String strConsultaRefenciaInterna =
		" SELECT DISTINCT" +
			" getnombrepersona(r.codigo_paciente) As paciente, " +
			" getedadsimpleresumido(current_date,to_date(getfechanacimientopaciente(r.codigo_paciente), 'DD/MM/YYYY')::Date) AS edad, "+
			" getDiagPrinValSircSinTipocie(r.numero_referencia) As diagnostico, " +
			" administracion.getnombremedico (r.codigo_profesional) As medico_remite, " +
			" getnombreconvenio(r.convenio) As convenio, " +
			" getnomconvcontrato(getconvenioxingreso(r.ingreso)) As convenio_contrato, " +
			" getdescripcioninstitucionsirc(itr.institucion_referir,itr.institucion) As institucion_recibe, " +
			" r.estado As estado, " +
			" to_char(r.fecha_referencia,'DD/MM/YYYY') As fecha_solicitud, " +
			" r.hora_referencia As hora_solicitud," +
			" case when (r.estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"' " +
					"OR r.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') then '' else " +
							" to_char(sir.fecha_tramite,'DD/MM/YYYY') END As fecha_aceptacion, "+
			" case when (r.estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"' " +
					"OR r.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') then '' else " +
							" sir.hora_tramite END As hora_aceptacion, "+
			" coalesce(to_char(e.fecha_egreso,'DD/MM/YYYY'),'') As fecha_salida, " +
			" coalesce(substring(e.hora_egreso||'',0,6),'') As hora_salida, " +
			" getdescripcioncamaphc(getultimacama(c.id,c.via_ingreso)) As cama, " +
			" getnomcentrocosto(c.area) As especialidad, " +
			" getcodigopropservicio2(dss.servicio,"+ConstantesBD.codigoTarifarioCups+") ||'-'|| " +
			" getdescripcionserviciosirc(sr.codigo_servicio_sirc,sir.institucion)||'-'|| sr.codigo_servicio_sirc As examen " +
			" FROM referencia r " +
			" LEFT OUTER JOIN instit_tramite_referencia itr ON (itr.numero_referencia_tramite=r.numero_referencia ) " +
			" LEFT OUTER JOIN servic_instit_referencia sir ON " +
			" (sir.numero_referencia_tramite=r.numero_referencia AND sir.institucion=r.institucion " +
			" AND sir.institucion_referir=itr.institucion_referir) " +
			" LEFT OUTER JOIN cuentas c ON (c.id_ingreso=r.ingreso)" +
			" LEFT OUTER JOIN egresos e ON (e.cuenta=c.id) " +
			" LEFT OUTER JOIN det_servicios_sirc dss ON (dss.servicio_sirc=sir.codigo_servicio_sirc AND dss.institucion=sir.institucion) " +
			" LEFT OUTER JOIN centros_costo cc ON (cc.codigo=c.area) "+
			" LEFT OUTER JOIN servicios_referencia sr ON (sr.numero_referencia = r.numero_referencia) ";
								
	
	/**
	 * Metodo encargado de consultar la informacion pedida en
	 * Reporte Referencia Interna en Historia Clinica, en las tablas:
	 * "referencia"
	 * "instit_tramite_referencia"
	 * "servic_instit_referencia"
	 * "cuentas"
	 * "egresos"
	 * "det_servicios_sirc"
	 * 
	 * @author Felipe Perez
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- centroAtencion		--> Requerido
	 * -- institucionSolicitada --> No Requerido
	 * -- fechaInicial			--> Requerido
	 * -- fechaFinal			--> Requerido
	 * -- tipoReporte			--> Requerido
	 * -- estadoReferencia		--> No Requerido
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * paciente,
	 * fechaNacimiento,
	 * diagPpal,
	 * medicoRemite,
	 * convenio,
	 * convenioContrato,
	 * institucionRecibe,
	 * estado,
	 * fechaSolicitud,
	 * horaSolicitud,
	 * fechaAceptacion,
	 * horaAceptacion,
	 * fechaSalida,
	 * horaSalida,
	 * cama,
	 * areaPaciente,
	 * examen,
	 */
	public HashMap consultaReporteReferenciaInterna (Connection connection, HashMap criterios)
	{
		return SqlBaseReporteReferenciaInternaDao.consultaReporteReferenciaInterna(connection, criterios,strConsultaRefenciaInterna);
	}
	
	/**
	 * Metodo encargado de generar una cadena where
	 * 
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- centroAtencion		--> Requerido
	 * -- institucionSolicitada --> No Requerido
	 * -- fechaInicial			--> Requerido
	 * -- fechaFinal			--> Requerido
	 * -- tipoReporte			--> Requerido
	 * -- estadoReferencia		--> No Requerido
	 * @param criterios
	 * @return
	 */
	public String obtenerWhere (HashMap criterios)
	{
		return SqlBaseReporteReferenciaInternaDao.obtenerWhere(criterios);
	}
}