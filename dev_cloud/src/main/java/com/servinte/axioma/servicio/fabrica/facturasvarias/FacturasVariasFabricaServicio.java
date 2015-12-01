package com.servinte.axioma.servicio.fabrica.facturasvarias;

import com.servinte.axioma.servicio.impl.facturasvarias.FacturasVariasServicio;
import com.servinte.axioma.servicio.interfaz.facturasvarias.IFacturasVariasServicio;

public class FacturasVariasFabricaServicio {

	/**
	 * Creaci�n de la instancia del servicio FacturasVarias
	 * @author Juan David Ram�rez
	 * @since 12 Septiembre 2010
	 */
	public static IFacturasVariasServicio crearFacturasVariasServicio() {
		return new FacturasVariasServicio();
	}

}
