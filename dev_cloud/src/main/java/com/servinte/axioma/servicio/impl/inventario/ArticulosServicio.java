package com.servinte.axioma.servicio.impl.inventario;


import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.servicio.interfaz.inventario.IArticulosServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class ArticulosServicio implements IArticulosServicio {
	
	IArticulosMundo mundo;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public ArticulosServicio(){
		mundo=InventarioMundoFabrica.crearArticulosMundo();
	}
	
	
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return Articulo
	 */
	public Articulo obtenerArticuloPorId(int id){
		return mundo.obtenerArticuloPorId(id);
	}

	/**
	 * 
	 * Este Método se encarga de obtener los codigos de los articulos por su tipo de codigo
	 * de medicamentos e insumos 
	 * @param tipoCodigoMed tipo de codigo Axioma o Interfaz 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<String> consultarCodigosArticulosPorTipoCodigo(String tipoCodigoMed){
		return mundo.consultarCodigosArticulosPorTipoCodigo(tipoCodigoMed);
	}
}
