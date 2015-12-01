package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.delegate.manejoPaciente.PacientesDelegate;

/**
 * Implementación de la interfaz {@link IPacientesDAO}.
 * 
 * @author Cristhian Murillo
 * @see PacientesDelegate.
 */
public class PacientesHibernateDAO implements IPacientesDAO{

	
	private PacientesDelegate delegate = new PacientesDelegate();
	
	@Override
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion) {
		return delegate.obtenerPaciente(identificacionBuscar, acronimoTipoIdentificacion);
	}

	@Override
	public boolean tienePacientePresupuestoEnEstados(int idPaciente,	String[] listaEstadosPresupuesto) {
		return delegate.tienePacientePresupuestoEnEstados(idPaciente, listaEstadosPresupuesto);
	}

	@Override
	public DtoPersonas obtenerDatosPaciente(int codPaciente) {
		return delegate.obtenerDatosPaciente(codPaciente);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void attachDirty(Pacientes pacienteOrm, int institucion)
	{
		try
		{
			delegate.attachDirty(pacienteOrm, institucion);
			Instituciones ins=new Instituciones();
			ins.setCodigo(institucion);
			pacienteOrm.getInstitucioneses().add(ins);
		} catch (Exception e)
		{
			Log4JManager.error("Error ingresando los datos del paciente", e);
		}
	}
	
	@Override
	public Pacientes findById(int id) {
		return delegate.findById(id);
	}

	
	@Override
	public ArrayList<DtoUsuariosCapitados> buscarPacienteConvenio(DtoUsuariosCapitados parametrosBusqueda) {
		return delegate.buscarPacienteConvenio(parametrosBusqueda);
	}

	@Override
	public DTOPacienteCapitado buscarPacienteAutorizacionIngresoEstancia(
			long codigoIngresoEstancia) {
		return delegate.buscarPacienteAutorizacionIngresoEstancia(codigoIngresoEstancia);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO#cargarAbonoDisponiblePorPaciente(int)
	 */
	@Override
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarAbonoDisponiblePorPaciente(
			int codPaciente) {
		return delegate.cargarAbonoDisponiblePorPaciente(codPaciente);
	}
	
}
