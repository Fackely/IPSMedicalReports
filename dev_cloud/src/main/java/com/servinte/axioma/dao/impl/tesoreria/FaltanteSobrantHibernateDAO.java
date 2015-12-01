package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.dao.interfaz.tesoreria.IFaltanteSobranteDAO;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.delegate.tesoreria.FaltanteSobranteDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IFaltanteSobranteDAO}.
 * 
 * @author Cristhian Murillo
 */

public class FaltanteSobrantHibernateDAO implements IFaltanteSobranteDAO
{
	
	private FaltanteSobranteDelegate delegate = new FaltanteSobranteDelegate();

	
	@Override
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(long idMovimiento) 
	{
		return delegate.obtenerFaltantesSobrantesPorMovimiento(idMovimiento);
	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de consular los datos del registro faltante
	 * sobrante.
	 * 
	 * @param FaltanteSobrante
	 * @return FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public FaltanteSobrante consultarRegistroFaltanteSobrantePorID(FaltanteSobrante registro){
		return delegate.consultarRegistroFaltanteSobrantePorID(registro);
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el responsable de un registro
	 * faltante sobrante y guardar su hist&oacute;rico.
	 * 
	 * @param FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarFaltanteSobrante(FaltanteSobrante dto){
		return delegate.actualizarFaltanteSobrante(dto);
	}
	

}
