package com.servinte.axioma.delegate.pyp;

import java.util.HashMap;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ActProgPypPac;

/**
 * Clase que permite el acceso a datos de las actividades y 
 * programas del módulo PYP 
 * 
 * @author Camilo Gomez
 * @version 1.0
 * @created 08-oct-2012 02:47:25 p.m.
 */
public class ActividadesProgramasPypDelegate {

	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	/**
	 * Servicio que consulta la actividad y programa pyp por el 
	 * codigo de orden ambulatoria 
	 * 
	 * @return ActProgPypPac
	 * @throws BDException
	 * @author Camilo Gomez
	 */
	public ActProgPypPac obtenerActiProgPYPPacPorOrdenAmbulatoria(long codigoOrden) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoOrden", codigoOrden);
			return (ActProgPypPac)persistenciaSvc.
					createNamedQueryUniqueResult("actividadesProgramasPyp.obtenerActiProgPYPPacPorOrdenAmbulatoria",parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
}
