package com.servinte.axioma.bl.inventario.facade;

import java.util.List;

import com.servinte.axioma.bl.inventario.impl.CatalogoInventarioMundo;
import com.servinte.axioma.bl.inventario.interfaz.ICatalogoInventarioMundo;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * Clase Fachada que provee todos los servicios de l�gica de negocio del m�dulo de
 * Inventario a todos los Action de la Capa Web
 * 
 * @author ricruico
 * @version 1.0
 * @created 14-ago-2012 02:24:00 p.m.
 */
public class InventarioFacade {
	
	/**
	 * Metodo encargado de obtener La informaci�n de clase de inventario por subgrupo 
	 * 
	 * @param codigoSubGrupo
	 * @return
	 * @author ricruico
	 */
	public ClaseInventarioDto obtenerClaseInventarioPorSubGrupo(int codigoSubGrupo) throws IPSException{
		ICatalogoInventarioMundo catalogoInventarioMundo = new CatalogoInventarioMundo();
		return catalogoInventarioMundo.obtenerClaseInventarioPorSubGrupo(codigoSubGrupo);
	}
	 /**
  	 * Metodo encargado de consultar las clases de inventario
  	 * 
  	 * @param 
  	 * @return List<ClaseInventarioDto>
  	 * @throws IPSException
  	 * @author ginsotfu
  	 */
	public List<ClaseInventarioDto> consultarClaseInventario() throws IPSException{
		ICatalogoInventarioMundo catalogoInventarioMundo = new CatalogoInventarioMundo();
		return catalogoInventarioMundo.consultarClaseInventario();
	}
	
	

}
