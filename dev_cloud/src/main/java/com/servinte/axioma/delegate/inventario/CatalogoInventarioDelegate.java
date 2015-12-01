package com.servinte.axioma.delegate.inventario;

import java.util.HashMap;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;

/**
 * Clase que permite el acceso a datos para las parametricas y catalogos
 * del módulo de Inventario
 * 
 * @author ricruico
 * @version 1.0
 * @created 14-ago-2012 02:23:59 p.m.
 */
public class CatalogoInventarioDelegate {

	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo encargado de obtener La información de clase de inventario por subgrupo 
	 * 
	 * @param codigoSubGrupo
	 * @return
	 * @author ricruico
	 */
	public ClaseInventarioDto obtenerClaseInventarioPorSubGrupo(int codigoSubGrupo) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoSubGrupo", codigoSubGrupo);
			Object[] claseInv=(Object[])persistenciaSvc.createNamedQueryUniqueResult("catalogoInventario.obtenerClaseInventarioPorSubGrupo", params);
			return new ClaseInventarioDto((Integer)claseInv[0], (String) claseInv[1],(Integer)claseInv[2]);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
}
