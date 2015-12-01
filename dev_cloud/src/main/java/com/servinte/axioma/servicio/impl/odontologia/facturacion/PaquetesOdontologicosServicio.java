package com.servinte.axioma.servicio.impl.odontologia.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.odontologia.facturacion.PaquetesOdontologicosFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.facturacion.IPaquetesOdontologicosMundo;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.servicio.interfaz.odontologia.facturacion.IPaquetesOdontologicosServicio;

public class PaquetesOdontologicosServicio implements IPaquetesOdontologicosServicio {
	
	IPaquetesOdontologicosMundo mundo;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PaquetesOdontologicosServicio() {
		mundo = PaquetesOdontologicosFabricaMundo.crearPaquetesOdontologicosMundo();
	}
	
	@Override
	public ArrayList<PaquetesOdontologicos> listarPaquetesOdontologicos(String codigoPaquete, String descripcionPaquete, int codigoEspecialidad) {
		return mundo.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
	}

}
