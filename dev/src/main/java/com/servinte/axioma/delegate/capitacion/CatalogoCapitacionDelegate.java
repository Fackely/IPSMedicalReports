package com.servinte.axioma.delegate.capitacion;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dto.capitacion.NivelAtencionDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;

/**
 * Clase que permite el acceso a datos para las parametricas y catalogos
 * del módulo de Capitación
 * 
 * @author ricruico
 * @version 1.0
 * @created 05-jul-2012 02:23:59 p.m.
 */
public class CatalogoCapitacionDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	/**
	 * Servicio encargado de obtener los niveles de atención parametrizados
	 * en el sistema
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<NivelAtencionDto> consultarNivelesAtencion() throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			return (List<NivelAtencionDto>)persistenciaSvc.createNamedQuery("catalogoCapitacion.consultarNivelesAtencion");
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

}
