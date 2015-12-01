package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.orm.Paises;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface IPaisesDAO {

	/**
	 * Lista todos los Paises
	 * @return
	 */
	public ArrayList<Paises> listarPaises ();
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoPaises> listarPaisesDto();

	
}
