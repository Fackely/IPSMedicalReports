package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.ITiposIdentificacionDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposServicioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IViasIngresoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesPoliconsultadoresMundo;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.ViasIngreso;


public class PacientesPoliconsultadoresMundo implements
		IPacientesPoliconsultadoresMundo {

	/**
	 * Dao con las Operaciones Basicas
	 */
	private IConvenioDAO convenioDAO;
	private ITiposServicioDAO tiposServicioDAO;
	private IUnidadesConsultaDAO unidadesConsultaDAO;
	private ITiposIdentificacionDAO tipoIdentificacionDAO;
	private IViasIngresoDAO viasIngresoDAO;
	private IPacientesDAO pacientesDAO;
	private ISolicitudesDAO solicitudesDAO;

	/**
	 * Contructor de componente
	 */
	public PacientesPoliconsultadoresMundo() {
		convenioDAO = AdministracionFabricaDAO.crearConvenioDAO();
		tiposServicioDAO = AdministracionFabricaDAO.crearTipoServicioDAO();
		unidadesConsultaDAO = AdministracionFabricaDAO.crearUnidadesConsultaDAO();
		tipoIdentificacionDAO = AdministracionFabricaDAO.crearTipoIdentificacionDAO();		
		viasIngresoDAO = AdministracionFabricaDAO.crearViasIngresoDAO();
		pacientesDAO = AdministracionFabricaDAO.crearPacientesDAO();
		solicitudesDAO = AdministracionFabricaDAO.crearSolicitudesDAO();
	}

	/**
	 * Consultar los tipos de convenios por tipo de contrado  = capitado
	 */
	public ArrayList<com.princetonsa.dto.facturacion.DtoConvenio> obtenerConveniosPorInstitucion(
			int institucion) {
		return convenioDAO.listarTodosConveniosCapitadosPorInstitucion(institucion);
	}
	
	/**	
	 * Consultar los tipos de servicios en el sistema
	 */
	public ArrayList<com.servinte.axioma.orm.TiposServicio> obtenerTiposServicio(){		
		return tiposServicioDAO.buscarTiposServicio();
	}
		
	/**	
	 * Consultar todos las unidades de Consulta
	 */
	public ArrayList<DtoUnidadesConsulta> obtenerUnidadesConsulta(){
		return unidadesConsultaDAO.listaTodoUnidadesConsulta();
	}
	
	
	/**	
	 * Consultar todos los tipos de Identificacion por Tipo
	 */
	public ArrayList<TiposIdentificacion> obtenerTipoIdentificacion(String[] listaTipo){
		return tipoIdentificacionDAO.listarTiposIdentificacionPorTipo(listaTipo);
	}
	
	/**	
	 * Consultar todas las vias de ingreso
	 */
	public ArrayList<ViasIngreso> obtenerViasIngreso(){
		return viasIngresoDAO.buscarViasIngreso();
	}
	
	/**	
	 * Obtener el paciente segun el numero de identificacion
	 */
	public DtoPersonas obtenerPaciente( String tipoIdentificacion, String numero ){
		return pacientesDAO.obtenerPaciente(numero, tipoIdentificacion);
	}
	
	/**	
	 * Obtener el paciente segun el numero de identificacion
	 */
	public ArrayList<DtoPacientesPoliconsultadores> obtenerConveniosPoliconsultadores( 
			String fechaInicial, 
			String fechaFinal,
			int idConvenio,
			String tipoIdentificacion, 
			String numeroIdentificacion,
			int idViaIngreso,
			int idEspecialidad,
			int idUnidadAgenda,
			String tipoServicio,Integer codigoPersona ){
		return solicitudesDAO.obtenerReportePoliconsultadores(fechaInicial, fechaFinal, idConvenio, tipoIdentificacion, numeroIdentificacion, idViaIngreso, idEspecialidad, idUnidadAgenda, tipoServicio,codigoPersona);
	}
}
