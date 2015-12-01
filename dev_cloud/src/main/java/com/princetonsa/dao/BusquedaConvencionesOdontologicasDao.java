package com.princetonsa.dao;

import java.util.ArrayList;

import util.InfoDatosStr;

public interface BusquedaConvencionesOdontologicasDao {

	ArrayList<InfoDatosStr> consultarConvencionesOdontologicas(int codigoInstitucionInt,boolean porTipo, String tipo);

	ArrayList<InfoDatosStr> consultarImagenesConvencionesOdontologicas();

	ArrayList<InfoDatosStr> consultarTramasConvencionesOdontologicas();
	
	/**
	 * cadena que consulta las imagenes base de una institucion
	 * @param institucion
	 * @return
	 */
	public ArrayList<InfoDatosStr> consultarImagenesBase(int institucion);
}
