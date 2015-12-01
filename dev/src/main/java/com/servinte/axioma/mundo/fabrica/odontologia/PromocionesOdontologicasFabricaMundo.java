package com.servinte.axioma.mundo.fabrica.odontologia;

import com.servinte.axioma.mundo.interfaz.odontologia.IPromocionesOdontologicasMundo;


public abstract class PromocionesOdontologicasFabricaMundo {

	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public PromocionesOdontologicasFabricaMundo() {
		
	}
	
	/**
	 * Crear y retorna un objeto que es implementaci&oacute;n de
	 * {@link IPromocionesOdontologicasMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IPromocionesOdontologicasMundo}.
	 */
	public static IPromocionesOdontologicasMundo crearPromocionesOdontologicasMundo() {
		return new com.servinte.axioma.mundo.impl.odontologia.PromocionesOdontologicasMundo();
	}
	
}
