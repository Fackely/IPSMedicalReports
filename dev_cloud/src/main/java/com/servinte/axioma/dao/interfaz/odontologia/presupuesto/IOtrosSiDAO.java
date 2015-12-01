package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiInclusiones;


/**
 * Esta clase se encarga de definir los m�todos de negocio para el modelo
 *
 * @author Cristhian Murillo
 */
public interface IOtrosSiDAO {
	
	
	/**
	 * Retorna el �ltimo Otro Si (Inclusion/Esclusion) del oresupuesto enviado.
	 * EL resultado es organizado por fecha y hora de tal manera que el objeto
	 * de la posici�n cero (0) de la lista ser�a el �ltimo ingresado.
	 * 
	 * @autor Cristhian Murillo
	 * @param codPresoOdonto
	 * @return OtrosSi
	 */
	public ArrayList<OtrosSi> obtenerOtroSiOrdenadosMayorMenor(long codPresoOdonto);
	
	
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
	 * Implementacion del m�todo persist
	 */
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance);
	
	
	/**
	 * Implementacion del m�todo persist
	 */
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance);
	
	
	/**
	 * Consulta los n�meros de otro si para inclusiones o exclusiones de un presupuesto
	 * @param codPresoOdonto
	 * @return ArrayList<DtoOtroSi>
	 * 
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto);
		
}
