package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Anexo 679
 * Creado el 12 de Septiembre de 2008
 * @author Ing. Felipe Perez
 * @mail lfperez@princetonsa.com
 */
public interface ReporteReferenciaExternaDao
{
	/**
	 * Metodo encargado de consultar la informacion pedida en
	 * Reporte Referencia Externa en Historia Clinica, en las tablas:
	 * "referencia"
	 * "servic_instit_referencia"
	 * "cuentas"
	 * "egresos"
	 * "det_servicios_sirc"
	 * "contrarreferencia"
	 * 
	 * @author Felipe Perez
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- centroAtencion		--> Requerido
	 * -- institucionSolicita	--> No Requerido
	 * -- fechaInicial			--> Requerido
	 * -- fechaFinal			--> Requerido
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
	public HashMap consultaReporteReferenciaExterna (Connection connection, HashMap criterios);
	
	/**
	 * Metodo encargado de generar una cadena where
	 * 
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- centroAtencion		--> Requerido
	 * -- institucionSolicita 	--> No Requerido
	 * -- fechaInicial			--> Requerido
	 * -- fechaFinal			--> Requerido
	 * -- estadoReferencia		--> No Requerido
	 * 
	 * @param criterios
	 * @return
	 */
	public String obtenerWhere (HashMap criterios, int bandera);

	/**
	 * Método para obtener la consulta
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public String obtenerConsulta(Connection connection, HashMap criterios);

	/**
	 * Método que consulta el reporte referencia externa con estado
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public HashMap consultaReporteReferenciaExternaConEstado (Connection connection, HashMap criterios);

	/**
	 * Método obtener la consulta con estado genérico
	 * Esto se realiza para cuando el campo estado, queda en seleccione o ha escogido todos
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public String obtenerConsultaConEstado(Connection connection, HashMap criterios);
}