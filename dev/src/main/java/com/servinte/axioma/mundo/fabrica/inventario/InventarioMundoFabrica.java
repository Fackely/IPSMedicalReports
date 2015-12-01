package com.servinte.axioma.mundo.fabrica.inventario;

import com.servinte.axioma.mundo.impl.inventario.ArticuloMundo;
import com.servinte.axioma.mundo.impl.inventario.ArticulosMundo;
import com.servinte.axioma.mundo.impl.inventario.ClaseInventarioMundo;
import com.servinte.axioma.mundo.impl.inventario.GrupoInventarioMundo;
import com.servinte.axioma.mundo.impl.inventario.NaturalezaArticuloMundo;
import com.servinte.axioma.mundo.impl.inventario.SubgrupoInventarioMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IArticuloMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IClaseInventarioMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IGrupoInventarioMundo;
import com.servinte.axioma.mundo.interfaz.inventario.INaturalezaArticuloMundo;
import com.servinte.axioma.mundo.interfaz.inventario.ISubgrupoInventarioMundo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public abstract class InventarioMundoFabrica {
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IArticuloMundo
	 * 
	 * @return IArticuloMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IArticuloMundo crearArticuloMundo(){
		return new ArticuloMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IClaseInventarioMundo
	 * 
	 * @return IClaseInventarioMundo
	 * @author, Angela Aguirre
	 *
	 */
	public static IClaseInventarioMundo crearClaseInventarioMundo(){
		return new ClaseInventarioMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IGrupoInventarioMundo
	 * 
	 * @return IGrupoInventarioMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IGrupoInventarioMundo crearGrupoInventarioMundo(){
		return new GrupoInventarioMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ISubgrupoInventarioMundo
	 * 
	 * @return ISubgrupoInventarioMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ISubgrupoInventarioMundo crearSubgrupoInventarioMundo(){
		return new SubgrupoInventarioMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad INaturalezaArticuloMundo
	 * 
	 * @return INaturalezaArticuloMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static INaturalezaArticuloMundo crearNaturalezaArticuloMundo(){
		return new NaturalezaArticuloMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IArticulosMundo
	 * 
	 * @return IArticulosMundo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IArticulosMundo crearArticulosMundo(){
		return new ArticulosMundo();
	}

}
