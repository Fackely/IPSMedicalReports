package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.dto.administracion.DtoCiudad;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.orm.Ciudades;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface ICiudadesDAO {
	
	
	
	/**
	 * Lista todas las Ciudades
	 * @return
	 */
	public ArrayList<Ciudades> listarCiudades ();
	
	
	
	
	/**
	 * Lista todas las Ciudades de un país
	 * @return
	 */
	public ArrayList<Ciudades> listarCiudadesPorPais (String codigoPais);




	/**
	 * 
	 * @param codigoPais
	 * @return
	 */
	public ArrayList<DtoCiudades> listarCiudadesDtoPorPais(String codigoPais);
	

	/**
	 * Lista todas las Ciudades parametrizadas en el sistema
	 * @return La lista de todas las ciudades en la estructura DtoCiudad
	 * @autor Ricardo Ruiz
	 */
	public List<DtoCiudad> listarAllCiudades();
	
}
