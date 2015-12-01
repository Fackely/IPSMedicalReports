package com.servinte.axioma.mundo.interfaz.odontologia.contrato;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiInclusiones;


/**
 * @author Cristhian Murillo
 */
public interface IOtrosSiMundo 
{
	
	/**
	 * Retorna el último Otro Si (Inclusion/Esclusion) del oresupuesto enviado.
	 * EL resultado es organizado por fecha y hora de tal manera que el objeto
	 * de la posición cero (0) de la lista sería el último ingresado.
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
	 * Guarda la entidad del orm
	 * @param transientInstance
	 */
	public void persist(OtrosSi transientInstance);
	
	
	
	/**
	 * Sincroniza la entidad
	 */
	public void attachDirty(OtrosSi instance);
	
	
	/**
	 * Implementacion del método persist
	 */
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance);
	
	
	/**
	 * Implementacion del método persist
	 */
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance);
	
	
	/**
	 * Realiza las validaciones para guardar el Otro Si del presupuesto enviado.
	 * Estas validaciones consisten en determinar y asignar el consecutivo 
	 * correspondiente al registro.
	 * 
	 * @param DtoOtroSi
	 * @return {@link OtrosSi}
	 */
	public OtrosSi guardarOtroSi(DtoOtroSi dtoOtroSi);
	
	
	/**
	 * Consulta los números de otro si para inclusiones o exclusiones de un presupuesto
	 * @param codPresoOdonto
	 * @return ArrayList<DtoOtroSi>
	 * 
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto);
	
	
}
