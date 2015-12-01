package com.servinte.axioma.servicio.fabrica.administracion;

import com.servinte.axioma.servicio.impl.administracion.ConsecutivosSistemaServicio;
import com.servinte.axioma.servicio.impl.administracion.MedicosServicio;
import com.servinte.axioma.servicio.impl.administracion.OcupacionesMedicaServicio;
import com.servinte.axioma.servicio.impl.administracion.PersonasServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.HistObserGenerPacienteServicio;
import com.servinte.axioma.servicio.impl.manejoPaciente.PacienteServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IConsecutivosSistemaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IOcupacionesMedicasServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IHistObserGenerPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IPacienteServicio;

/**
 * Fabrica para crear los servicio que tiene la mundo persona
 * @author axioma
 *
 */
public abstract  class AdministracionFabricaServicio {

	
	/**
	 * 
	 */
	private  AdministracionFabricaServicio(){
	}
	
	/**
	 * Metodo que crea la instacion persona servicio
	 * @return
	 */
	public  static final IPersonasServicio crearPersonaServicio(){
		return new PersonasServicio();
	}
	

	/**
	 * Metodo para crear la instancia de los pacientes 
	 * @return
	 */
	public static final IPacienteServicio crearPacienteServicio(){
		return new PacienteServicio();
	}
	
	/**
	 * Retorna la implementacion la RolesFuncionalidades
	 */
	public static IMedicosServicio crearMedicosServicio()
	{
		return new MedicosServicio();
	}
	
	/**
	 * 
	 */
	private static  IHistObserGenerPacienteServicio historicoServicio;
	
	/**
	 *  Crear historico observaciones paciente servicio
	 * @return
	 */
	public static IHistObserGenerPacienteServicio crearHistObserGenerPaciente(){
		
		if(historicoServicio==null){
			return new HistObserGenerPacienteServicio();
		}
		return historicoServicio;
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la 
	 * entidad IOcupacionesMedicasServicio
	 * 
	 * @return IOcupacionesMedicasServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IOcupacionesMedicasServicio crearOcupacionesMedicasServicio() {
		return new OcupacionesMedicaServicio();
	}

	/**
	 * Este Método se encarga de crear una instancia concreta para la 
	 * entidad IConsecutivosDisponiblesServicio
	 * 
	 * @return IConsecutivosDisponiblesServicio
	 * @author Juan David Ramírez
	 * 
	 */
	public static IConsecutivosSistemaServicio crearConsecutivosDisponiblesServicio()
	{
		return new ConsecutivosSistemaServicio();
	}
	
	
	
}
