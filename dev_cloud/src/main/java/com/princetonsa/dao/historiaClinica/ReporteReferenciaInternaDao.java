package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Anexo 678
 * Creado el 12 de Septiembre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public interface ReporteReferenciaInternaDao
{
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
	public HashMap consultaReporteReferenciaInterna (Connection connection, HashMap criterios);
	
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
	 * 
	 * @param criterios
	 * @return
	 */
	public String obtenerWhere (HashMap criterios);
}