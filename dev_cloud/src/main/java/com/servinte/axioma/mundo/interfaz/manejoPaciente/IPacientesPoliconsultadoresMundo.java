package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.ViasIngreso;

public interface IPacientesPoliconsultadoresMundo {
	
	/**
	 * Metodo para obtener los convenios segun la institucion
	 * @param reporte
	 * @return Lista de los convenios del sistema
	 */
	public ArrayList<com.princetonsa.dto.facturacion.DtoConvenio> obtenerConveniosPorInstitucion(int institucion);
	
	/**	
	 * Consultar los tipos de servicios en el sistema
	 * 
	 * @return TiposServicio
	 */
	public ArrayList<com.servinte.axioma.orm.TiposServicio> obtenerTiposServicio();
	
	/**	
	 * Consultar las unidades de Consulta
	 * 
	 * @return DtoUnidadesConsulta
	 */
	public ArrayList<DtoUnidadesConsulta> obtenerUnidadesConsulta();
	
	/**	
	 * Consultar los tipos de Identificacion
	 * 
	 * @return TiposIdentificacion
	 */
	public ArrayList<TiposIdentificacion> obtenerTipoIdentificacion(String[] listaTipo);
	
	/**	
	 * Consultar las vias de ingreso
	 * 
	 * @return ViasIngreso
	 */
	public ArrayList<ViasIngreso> obtenerViasIngreso();
	
	/**	
	 * Obtener paciente
	 * 
	 * @return DtoPersonas
	 */
	public DtoPersonas obtenerPaciente( String tipoIdentificacion, String numero );
	
	public ArrayList<DtoPacientesPoliconsultadores> obtenerConveniosPoliconsultadores( 
			String fechaInicial, 
			String fechaFinal,
			int idConvenio,
			String tipoIdentificacion, 
			String numeroIdentificacion,
			int idViaIngreso,
			int idEspecialidad,
			int idUnidadAgenda,
			String tipoServicio,Integer codigiPersona );
}
