package com.servinte.axioma.servicio.fabrica.inventario;

import com.servinte.axioma.servicio.impl.inventario.ArticuloServicio;
import com.servinte.axioma.servicio.impl.inventario.ArticulosServicio;
import com.servinte.axioma.servicio.impl.inventario.ClaseInventarioServicio;
import com.servinte.axioma.servicio.impl.inventario.GrupoInventarioServicio;
import com.servinte.axioma.servicio.impl.inventario.NaturalezaArticuloServicio;
import com.servinte.axioma.servicio.impl.inventario.SubgrupoInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IArticuloServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IArticulosServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IClaseInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IGrupoInventarioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.INaturalezaArticuloServicio;
import com.servinte.axioma.servicio.interfaz.inventario.ISubgrupoInventarioServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public abstract class InventarioServicioFabrica {
	

	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IArticuloServicio
	 * 
	 * @return IArticuloServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IArticuloServicio crearArticuloServicio(){
		return new ArticuloServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IClaseInventarioServicio
	 * 
	 * @return IClaseInventarioServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IClaseInventarioServicio crearClaseInventarioServicio(){
		return new ClaseInventarioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IGrupoInventarioServicio
	 * 
	 * @return IGrupoInventarioServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IGrupoInventarioServicio crearGrupoInventarioServicio(){
		return new GrupoInventarioServicio();
	}

	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ISubgrupoInventarioServicio
	 * 
	 * @return ISubgrupoInventarioServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ISubgrupoInventarioServicio crearSubgrupoInventarioServicio(){
		return new SubgrupoInventarioServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INaturalezaArticuloServicio
	 * 
	 * @return INaturalezaArticuloServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INaturalezaArticuloServicio crearNaturalezaArticuloServicio(){
		return new NaturalezaArticuloServicio();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IArticulosServicio
	 * 
	 * @return IArticulosServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IArticulosServicio crearArticulosServicio(){
		return new ArticulosServicio();
	}
	

}
