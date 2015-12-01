package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.servinte.axioma.dao.interfaz.tesoreria.IHistoCambioResponsableDAO;
import com.servinte.axioma.orm.HistoCambioResponsable;
import com.servinte.axioma.orm.delegate.tesoreria.HistoCambioResponsableDelegate;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos necesarios
 * para el proceso de negocio de la entidad HistorialCambioResponsable
 * 
 * @author Angela Maria Aguirre
 * @since 19/07/2010
 */
public class HistoCambioResponsableHibernateDAO implements IHistoCambioResponsableDAO {

	/**
	 * Instancia delegate de HistoCambioResponsableDelegate
	 */
	private HistoCambioResponsableDelegate delegate;
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public HistoCambioResponsableHibernateDAO(){
		delegate = new HistoCambioResponsableDelegate();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de registrar un historial de 
	 * cambio de responsable para un registro faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return Boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public Boolean registrarHistorialFaltanteSobrante(HistoCambioResponsable dto) {
		return delegate.registrarHistorialFaltanteSobrante(dto);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar el historial del  cambio de responsable
	 * del registro de faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return ArrayList<DTOHistoCambioResponsable> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override	
	public ArrayList<DTOHistoCambioResponsable> consultarHistorialPorDetalleFaltanteSobranteID(DTOHistoCambioResponsable dto){
		return delegate.consultarHistorialPorDetalleFaltanteSobranteID(dto);
	}

}
