package com.servinte.axioma.dao.interfaz.facturacion;

import com.princetonsa.dto.cargos.DtoTercero;
import com.servinte.axioma.orm.Terceros;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Terceros
 * 
 * @author Diana Carolina G
 *
 */


public interface ITercerosDAO {
	
	/**
	 * M&eacute;todo que recibe un Id y retorna un objeto
	 * @param id
	 * @return Terceros
	 */
	
	public Terceros findById(int id);
	
	
	/**
	 * Obtener el tercero por el codigo pk de una entidad subcontratada 
	 * 
	 * @param codEntidadSubcontratada
	 * @return DtoTercero
	 * 
	 * @author Fabián Becerra
	 */
	public DtoTercero obtenerTerceroXEntidadSub (long codEntidadSubcontratada);

}
