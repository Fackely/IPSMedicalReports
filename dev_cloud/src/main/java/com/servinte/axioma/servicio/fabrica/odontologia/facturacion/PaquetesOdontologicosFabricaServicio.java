package com.servinte.axioma.servicio.fabrica.odontologia.facturacion;

import com.servinte.axioma.servicio.impl.odontologia.facturacion.PaquetesOdontologicosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.facturacion.IPaquetesOdontologicosServicio;

public abstract class PaquetesOdontologicosFabricaServicio {
	
	public PaquetesOdontologicosFabricaServicio() {
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPaquetesOdontologicosServicio
	 * @return IPaquetesOdontologicosServicio
	 * 
	 * @author Yennifer Guerrero
	 *
	 */
	public static IPaquetesOdontologicosServicio crearIPaquetesOdontologicosServicio(){
		return new PaquetesOdontologicosServicio();
	}

}
