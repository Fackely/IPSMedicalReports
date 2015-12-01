package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.orm.Instituciones;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface IInstitucionesDAO {

	
	/**
	 * Lista todas las Instituciones
	 * @return
	 */
	public ArrayList<Instituciones> listarInstituciones ();
	
	
	/**
	 * Busca por su llave primaria
	 * @param id
	 * @return
	 */
	public Instituciones findById(int id);
	
}
