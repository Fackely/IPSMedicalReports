package com.servinte.axioma.servicio.fabrica.facturasvarias;

import com.servinte.axioma.servicio.impl.facturasvarias.FacturasVariasServicio;
import com.servinte.axioma.servicio.interfaz.facturasvarias.IFacturasVariasServicio;

public class FacturasVariasFabricaServicio {

	/**
	 * Creación de la instancia del servicio FacturasVarias
	 * @author Juan David Ramírez
	 * @since 12 Septiembre 2010
	 */
	public static IFacturasVariasServicio crearFacturasVariasServicio() {
		return new FacturasVariasServicio();
	}

}
