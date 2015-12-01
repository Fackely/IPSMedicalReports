package com.servinte.axioma.dao.impl.manejoPaciente;

import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEstanciaCapitaDAO;
import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEstanciaCapitaDelegate;

/**
 * Esta clase se encarga de ejecutar la l�gica de negocio
 * para la entidad AutorizacionesEstanciaCapita
 * 
 * @author Angela Maria Aguirre
 * @since 15/12/2010
 */
public class AutorizacionesEstanciaCapitaHibernateDAO implements
		IAutorizacionesEstanciaCapitaDAO {
	
	AutorizacionesEstanciaCapitaDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public AutorizacionesEstanciaCapitaHibernateDAO(){
		delegate = new AutorizacionesEstanciaCapitaDelegate();
	}

	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * el registro que asocia la autorizaci�n de ingreso estancia con la
	 * autorizaci�n de capitaci�n subcontratada
	 * 
	 * @param AutorizacionesEstanciaCapita autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean guardarAutorizacionEntidadSubcontratada(
			AutorizacionesEstanciaCapita autorizacion) {
		return delegate.guardarAutorizacionEntidadSubcontratada(autorizacion);
	}

}
