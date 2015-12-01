package com.princetonsa.dao.postgresql.historiaClinica;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ReporteReferenciaExternaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseReporteReferenciaExternaDao;

/**
 * @author Felipe Perez
 * Fecha: Septiembre de 2008
 */

public class PostgresqlReporteReferenciaExternaDao implements ReporteReferenciaExternaDao
{
	/**
	 * Metodo encargado de consultar la informacion pedida en
	 * Reporte Referencia Externa en Historia Clinica, en las tablas:
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
	public HashMap consultaReporteReferenciaExterna (Connection connection, HashMap criterios)
	{
		return SqlBaseReporteReferenciaExternaDao.consultaReporteReferenciaExterna(connection, criterios);
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
	public String obtenerWhere (HashMap criterios, int bandera)
	{
		return SqlBaseReporteReferenciaExternaDao.obtenerWhere(criterios, bandera);
	}
	
	/**
	 * Metodo encargado de generar la cadena de la consulta completa
	 * 
	 * @param criterios
	 * @param connection
	 * @return String cadena
	 */
	public String obtenerConsulta(Connection connection, HashMap criterios)
	{
		return SqlBaseReporteReferenciaExternaDao.obtenerConsulta(connection, criterios);
	}
	
	public String obtenerConsultaConEstado(Connection connection, HashMap criterios)
	{
		return SqlBaseReporteReferenciaExternaDao.obtenerConsultaConEstado(connection, criterios);
	}
	
	public HashMap consultaReporteReferenciaExternaConEstado (Connection connection, HashMap criterios)
	{
		return SqlBaseReporteReferenciaExternaDao.consultaReporteReferenciaExternaConEstado(connection, criterios);
	}
	
}