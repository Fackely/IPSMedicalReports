package com.servinte.axioma.servicio.fabrica.odontologia;

import com.servinte.axioma.servicio.impl.odontologia.PromocionesOdontologicasServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.IPromocionesOdontologicasServicio;

public abstract class PromocionesOdontologicasFabricaServicio {
	
	public PromocionesOdontologicasFabricaServicio() {
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPromocionesOdontologicasServicio
	 * @return IPromocionesOdontologicasServicio
	 * 
	 * @author Javier Gonzalez
	 *
	 */
	public static IPromocionesOdontologicasServicio crearIPromocionesOdontologicasServicio(){
		return new PromocionesOdontologicasServicio();
	}

}
