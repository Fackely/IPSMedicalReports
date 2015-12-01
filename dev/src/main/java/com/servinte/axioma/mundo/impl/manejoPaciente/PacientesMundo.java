package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.helper.manejopaciente.PacienteMundoHelper;
import com.servinte.axioma.mundo.interfaz.administracion.IPersonas;
import com.servinte.axioma.mundo.interfaz.facturacion.convenio.IAutorizacionConvIngPac;
import com.servinte.axioma.mundo.interfaz.facturacion.convenio.IValidacionesBdConvIngPacMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.orm.AutorizacionConvIngPac;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


public class PacientesMundo implements IPacientesMundo {
	
	private IPacientesDAO	pacientesDAO;
	
	public PacientesMundo() {
		inicializar();
	}

	private void inicializar() {
		pacientesDAO	= ManejoPacienteDAOFabrica.crearPacientesDAO();
	}
	
	
	@Override
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion) {
		return pacientesDAO.obtenerPaciente(identificacionBuscar, acronimoTipoIdentificacion);
	}

	@Override
	public boolean tienePacientePresupuestoEnEstados(int idPaciente, String[] listaEstadosPresupuesto) {
		return pacientesDAO.tienePacientePresupuestoEnEstados(idPaciente, listaEstadosPresupuesto);
	}

	
	@Override
	public DtoPersonas obtenerDatosPaciente(int codPaciente) {
		return pacientesDAO.obtenerDatosPaciente(codPaciente);
	}
	
	public ArrayList<DtoIntegridadDominio> listarSexoPaciente(){
		
		String[] listadoSexo = new String[]{ConstantesIntegridadDominio.acronimoFemenino,
				ConstantesIntegridadDominio.acronimoMasculino};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listadoSexoPaciente=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoSexo, false);
		
		UtilidadBD.closeConnection(con);
		
		return listadoSexoPaciente;
		
	}
	
	/**
	 * 
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DtoPaciente cargarPacienteCompleto(String identificacionBuscar, String acronimoTipoIdentificacion){
		
		
		IPersonas mundoPersona=AdministracionFabricaMundo.crearPersonasMundo();
		DtoPaciente paciente = new DtoPaciente();
		Personas persona =  mundoPersona.buscarPersonasCompleto(identificacionBuscar, acronimoTipoIdentificacion);
		
		
		UtilidadTransaccion.getTransaccion().begin();
		
		if(persona.getPacientes()!=null && persona.getPacientes().getConveniosIngresoPacientes() != null)
		{
			// Se iteran cada uno de los convenios asociados para mostrar su informacion
			for (Iterator iterator = persona.getPacientes().getConveniosIngresoPacientes().iterator(); iterator.hasNext();) 
			{
				// Convenios cargados asociados al paciente
				ConveniosIngresoPaciente conveniosIngresoPaciente = (ConveniosIngresoPaciente) iterator.next();
				
				/*
				 * 1. settear la informacion del Dto Seccion Convenio
				 */
				DtoSeccionConvenioPaciente 	dtoSeccionConvenioPaciente= PacienteMundoHelper.settearConveniosPaciente(conveniosIngresoPaciente);
				
				/*
				 * almacenamos el codigo tmp
				 */
				long conveniosIngresoPacientePk = conveniosIngresoPaciente.getCodigoPk();
				
				/*
				 * 2. Cargar validacion es bd conveio ing pac
				 */
				IValidacionesBdConvIngPacMundo mundoValidacionConvIngPacMundo  =FacturacionFabricaMundo.crearValidacionBdConvIngPacMundo();
				dtoSeccionConvenioPaciente.setValidacionesBdConvIngPac( mundoValidacionConvIngPacMundo.obtenerUltimaValidacionBd(conveniosIngresoPacientePk));
				
				/*
				 *3. Se carga la última Autorización que coincida con conveniosIngresoPaciente 
				 */
				IAutorizacionConvIngPac mundoAutorizacion=FacturacionFabricaMundo.crearAutorizacionMundo();
				AutorizacionConvIngPac autorizacionConvIngPac = mundoAutorizacion.obtenerUltimaAutorizacion(conveniosIngresoPacientePk); 
				
				/*
				 *4. Setteamos la autorizacion 
				 */
				dtoSeccionConvenioPaciente=PacienteMundoHelper.settearAutorizacinoCongIngPac(autorizacionConvIngPac, dtoSeccionConvenioPaciente);
				
				/*
				 * 5. Cargar los bonos
				 */
				dtoSeccionConvenioPaciente.setListaBonosConvIngPac(new ArrayList(conveniosIngresoPaciente.getBonosConvIngPacs()));
				 
				/*
				 * 6. Se carga la lista de convenios-contratos a la forma
				 */
				paciente.getListaDtoSeccionConvenioPaciente().add(dtoSeccionConvenioPaciente);
			}
		}
			
		UtilidadTransaccion.getTransaccion().commit();
		
		
		return paciente;
	}
	
	@Override
	public Pacientes findById(int id) {
		return pacientesDAO.findById(id);
	}

	
	
	@Override
	public void attachDirty(Pacientes pacienteOrm, int institucion)
	{
		pacientesDAO.attachDirty(pacienteOrm, institucion);
	}

	
	@Override
	public ArrayList<DtoUsuariosCapitados> buscarPacienteConvenio(DtoUsuariosCapitados parametrosBusqueda) {
		return pacientesDAO.buscarPacienteConvenio(parametrosBusqueda);
	}

	@Override
	public DTOPacienteCapitado buscarPacienteAutorizacionIngresoEstancia(
			long codigoIngresoEstancia) {
		return pacientesDAO.buscarPacienteAutorizacionIngresoEstancia(codigoIngresoEstancia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo#cargarAbonoDisponiblePorPaciente(int)
	 */
	@Override
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarAbonoDisponiblePorPaciente(
			int codPaciente) {
		return pacientesDAO.cargarAbonoDisponiblePorPaciente(codPaciente);
	}
}
