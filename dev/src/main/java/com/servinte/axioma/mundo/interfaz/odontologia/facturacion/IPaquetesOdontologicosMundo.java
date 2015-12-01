package com.servinte.axioma.mundo.interfaz.odontologia.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.PaquetesOdontologicos;

public interface IPaquetesOdontologicosMundo {
	
	/**
	 * 
	 * @param codigoEspecialidad 
	 * @param descripcionPaquete 
	 * @param codigoPaquete 
	 * @return
	 */
	public ArrayList<PaquetesOdontologicos> listarPaquetesOdontologicos(String codigoPaquete, String descripcionPaquete, int codigoEspecialidad);

}
