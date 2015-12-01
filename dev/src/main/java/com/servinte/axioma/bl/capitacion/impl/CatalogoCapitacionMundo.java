package com.servinte.axioma.bl.capitacion.impl;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.capitacion.interfaz.ICatalogoCapitacionMundo;
import com.servinte.axioma.delegate.capitacion.CatalogoCapitacionDelegate;
import com.servinte.axioma.dto.capitacion.NivelAtencionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Capitación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class CatalogoCapitacionMundo implements ICatalogoCapitacionMundo{

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.ICatalogoCapitacionMundo#consultarNivelesAtencion()
	 * @author ricruico
	 */
	@Override
	public List<NivelAtencionDto> consultarNivelesAtencion()  throws IPSException{
		List<NivelAtencionDto> listaNiveles=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoCapitacionDelegate delegate = new CatalogoCapitacionDelegate();
			listaNiveles=delegate.consultarNivelesAtencion();
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaNiveles;
	}

}