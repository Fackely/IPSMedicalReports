package com.servinte.axioma.servicio.interfaz.inventario;

import java.util.ArrayList;

import com.servinte.axioma.orm.Articulo;



/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public interface IArticulosServicio {
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return Articulo
	 */
	public Articulo obtenerArticuloPorId(int id);
	
	/**
	 * 
	 * Este Método se encarga de obtener los codigos de los articulos por su tipo de codigo
	 * de medicamentos e insumos 
	 * @param tipoCodigoMed tipo de codigo Axioma o Interfaz 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<String> consultarCodigosArticulosPorTipoCodigo(String tipoCodigoMed);

}
