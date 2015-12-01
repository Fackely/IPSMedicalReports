package com.servinte.axioma.dao.fabrica.odontologia;

import com.servinte.axioma.dao.impl.odontologia.PromocionesOdontologicasHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.facturacion.PaquetesOdontologicosHibernateDAO;
import com.servinte.axioma.dao.interfaz.odontologia.IPromocionesOdontologicasDAO;
import com.servinte.axioma.dao.interfaz.odontologia.facturacion.IPaquetesOdontologicosDAO;

public class PromocionesOdontologicasFabricaDAO {
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public PromocionesOdontologicasFabricaDAO() {
		
	}
	
	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IPromocionesOdontologicasDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IPaquetesOdontologicosDAO}.
	 */
	public static IPromocionesOdontologicasDAO crearPromocionesOdontologicasDAO() {
		return new PromocionesOdontologicasHibernateDAO();
	}

}
