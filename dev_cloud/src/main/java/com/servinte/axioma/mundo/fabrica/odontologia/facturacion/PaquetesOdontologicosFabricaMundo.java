package com.servinte.axioma.mundo.fabrica.odontologia.facturacion;

import com.princetonsa.mundo.odontologia.PaquetesOdontologicosMundo;
import com.servinte.axioma.dao.impl.odontologia.facturacion.PaquetesOdontologicosHibernateDAO;
import com.servinte.axioma.dao.interfaz.odontologia.facturacion.IPaquetesOdontologicosDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.facturacion.IPaquetesOdontologicosMundo;

/**
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public abstract class PaquetesOdontologicosFabricaMundo {
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PaquetesOdontologicosFabricaMundo() {
		
	}
	
	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IPaquetesOdontologicosMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IPaquetesOdontologicosMundo}.
	 */
	public static IPaquetesOdontologicosMundo crearPaquetesOdontologicosMundo() {
		return new com.servinte.axioma.mundo.impl.odontologia.facturacion.PaquetesOdontologicosMundo();
	}
}
