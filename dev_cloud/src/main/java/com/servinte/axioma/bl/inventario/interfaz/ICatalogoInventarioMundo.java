package com.servinte.axioma.bl.inventario.interfaz;

import java.util.List;

import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que provee los servicios de Negocio correspondientes a los
 * catalogos o par�metricas del modulo de Inventario
 * 
 * @author ricruico
 * @version 1.0
 * @created 14-ago-2012 02:24:00 p.m.
 */
public interface ICatalogoInventarioMundo {

	
	/**
	 * Metodo encargado de obtener La informaci�n de clase de inventario por subgrupo 
	 * 
	 * @param codigoSubGrupo
	 * @return
	 * @author ricruico
	 */
	ClaseInventarioDto obtenerClaseInventarioPorSubGrupo(int codigoSubGrupo) throws IPSException;
	
	/**
	 * Metodo encargado de consultar las clases de inventario
	 * 
	 * @param 
	 * @return List<ClaseInventarioDto>
	 * @author ginsotfu
	 */
	List<ClaseInventarioDto> consultarClaseInventario() throws IPSException;
}
