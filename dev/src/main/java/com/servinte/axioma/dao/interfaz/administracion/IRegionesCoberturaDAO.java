package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.orm.RegionesCobertura;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface IRegionesCoberturaDAO {


	/**
	 * Lista todas las Regiones de Cobertura
	 * @return
	 */
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas();
	
}
