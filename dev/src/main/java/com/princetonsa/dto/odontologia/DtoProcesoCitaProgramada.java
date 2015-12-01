package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.odontologia.InfoPlanTratamiento;

/**
 * 
 * Clase que contiene la información necesaria para realizar el proceso
 * de programación de una cita para un paciente desde la funcionalidad
 * 'Contratar presupuesto' y 'Atención de la Cita'
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class DtoProcesoCitaProgramada implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Listado con los programas / Servicios a contratar relacionados en el presupuesto,
	 * disponibles para que sean seleccionados en el proceso de próxima cita.
	 */
	private ArrayList<DtoPresuOdoProgServ> programasServiciosPresupuesto;

	/**
	 * Código del Plan de Tratamiento
	 */
	private BigDecimal codigoPlanTratamiento;
	
	/**
	 * Código del Presupuesto contratado
	 */
	private BigDecimal codigoPresupuesto;
	
	/**
	 * Código de la persona asociada al registro del paciente
	 */
	private int codigoPersona;

	/**
	 * Atributo que contiene toda la información registrada en la atención de la cita.
	 */
	private InfoPlanTratamiento infoPlanTratamiento;
	
	/**
	 * Código de la evolución registrada
	 */
	private int codigoEvolucion;
	
	
	/**
	 * Contructor
	 */
	
	public DtoProcesoCitaProgramada(){
		
		programasServiciosPresupuesto = new ArrayList<DtoPresuOdoProgServ>();
		codigoPlanTratamiento =  new BigDecimal(ConstantesBD.codigoNuncaValido);
		codigoPresupuesto =  new BigDecimal(ConstantesBD.codigoNuncaValido);
		codigoPersona = ConstantesBD.codigoNuncaValido;
		infoPlanTratamiento = new InfoPlanTratamiento();
		codigoEvolucion = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * @return the programasServiciosPresupuesto
	 */
	public ArrayList<DtoPresuOdoProgServ> getProgramasServiciosPresupuesto() {
		return programasServiciosPresupuesto;
	}

	/**
	 * @param programasServiciosPresupuesto the programasServiciosPresupuesto to set
	 */
	public void setProgramasServiciosPresupuesto(
			ArrayList<DtoPresuOdoProgServ> programasServiciosPresupuesto) {
		this.programasServiciosPresupuesto = programasServiciosPresupuesto;
	}

	/**
	 * @return the codigoPlanTratamiento
	 */
	public BigDecimal getCodigoPlanTratamiento() {
		return codigoPlanTratamiento;
	}

	/**
	 * @param codigoPlanTratamiento the codigoPlanTratamiento to set
	 */
	public void setCodigoPlanTratamiento(BigDecimal codigoPlanTratamiento) {
		this.codigoPlanTratamiento = codigoPlanTratamiento;
	}

	/**
	 * @return the codigoPresupuesto
	 */
	public BigDecimal getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	/**
	 * @param codigoPresupuesto the codigoPresupuesto to set
	 */
	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}

	/**
	 * @param codigoPersona the codigoPersona to set
	 */
	public void setCodigoPersona(int codigoPersona) {
		this.codigoPersona = codigoPersona;
	}

	/**
	 * @return the codigoPersona
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}

	/**
	 * @param infoPlanTratamiento the infoPlanTratamiento to set
	 */
	public void setInfoPlanTratamiento(InfoPlanTratamiento infoPlanTratamiento) {
		this.infoPlanTratamiento = infoPlanTratamiento;
	}

	/**
	 * @return the infoPlanTratamiento
	 */
	public InfoPlanTratamiento getInfoPlanTratamiento() {
		return infoPlanTratamiento;
	}

	/**
	 * @param codigoEvolucion the codigoEvolucion to set
	 */
	public void setCodigoEvolucion(int codigoEvolucion) {
		this.codigoEvolucion = codigoEvolucion;
	}

	/**
	 * @return the codigoEvolucion
	 */
	public int getCodigoEvolucion() {
		return codigoEvolucion;
	}
}