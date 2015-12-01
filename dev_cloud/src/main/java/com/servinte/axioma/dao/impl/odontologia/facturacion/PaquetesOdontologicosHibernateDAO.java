package com.servinte.axioma.dao.impl.odontologia.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.odontologia.facturacion.IPaquetesOdontologicosDAO;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.delegate.odontologia.PaquetesOdontologicosDelegate;

/**
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since  15/09/2010
 *
 */
public class PaquetesOdontologicosHibernateDAO implements IPaquetesOdontologicosDAO {
	
	private PaquetesOdontologicosDelegate delegate;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PaquetesOdontologicosHibernateDAO() {
		delegate = new PaquetesOdontologicosDelegate();
	}
	
	@Override
	public ArrayList<PaquetesOdontologicos> listarPaquetesOdontologicos(String codigoPaquete, String descripcionPaquete, int codigoEspecialidad) {
		return delegate.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
	}
}
