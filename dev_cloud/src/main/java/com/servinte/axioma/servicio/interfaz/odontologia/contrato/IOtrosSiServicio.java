package com.servinte.axioma.servicio.interfaz.odontologia.contrato;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.orm.OtrosSi;


/**
 * @author Cristhian Murillo
 */
public interface IOtrosSiServicio 
{
	
	/**
	 * Retorna el �ltimo Otro Si (Inclusion/Esclusion) del presupuesto enviado.
	 * EL resultado es organizado por fecha y hora de tal manera que el objeto
	 * de la posici�n cero (0) de la lista ser�a el �ltimo ingresado.
	 * 
	 * @autor Cristhian Murillo
	 * @param codPresoOdonto
	 * @return OtrosSi
	 */
	public ArrayList<OtrosSi> obtenerOtroSiOrdenadosMayorMenor(long codPresoOdonto);
	
	
	
	/**
	 * Realiza las validaciones para guardar el Otro Si del presupuesto enviado.
	 * Estas validaciones consisten en determinar y asignar el consecutivo 
	 * correspondiente al registro.
	 * 
	 * @autor Cristhian Murillo
	 * @param DtoOtroSi
	 * @return boolean
	 */
	public boolean guardarOtroSiPresupuesto(DtoOtroSi dtoOtroSi);
	
	
	/**
	 * Realiza las validaciones para guardar el Otro Si del presupuesto enviado.
	 * Estas validaciones consisten en determinar y asignar el consecutivo 
	 * correspondiente al registro.
	 * 
	 * @param DtoOtroSi
	 * @return long
	 */
	public OtrosSi guardarOtroSi(DtoOtroSi dtoOtroSi);
	
	
	/**
	 * Consulta los n�meros de otro si para inclusiones o exclusiones de un presupuesto
	 * @param codPresoOdonto
	 * @return ArrayList<DtoOtroSi>
	 * 
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto);
	
}
