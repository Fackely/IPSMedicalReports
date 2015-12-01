package com.servinte.axioma.dao.fabrica.odontologia.facturacion;

import com.servinte.axioma.dao.impl.odontologia.facturacion.PaquetesOdontologicosHibernateDAO;
import com.servinte.axioma.dao.interfaz.odontologia.facturacion.IPaquetesOdontologicosDAO;

public abstract class PaquetesOdontologicosFabricaDAO {
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PaquetesOdontologicosFabricaDAO() {
		
	}
	
	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IPaquetesOdontologicosDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IPaquetesOdontologicosDAO}.
	 */
	public static IPaquetesOdontologicosDAO crearPaquetesOdontologicosDAO() {
		return new PaquetesOdontologicosHibernateDAO();
	}
}
