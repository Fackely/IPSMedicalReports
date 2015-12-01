package com.servinte.axioma.mundo.interfaz.facturacion;

import com.princetonsa.dto.cargos.DtoTercero;
import com.servinte.axioma.orm.Terceros;

/**
 * Define la l&oacute;gica de negocio relacionada con los Terceros
 * @author Diana Carolina G
 *
 */


public interface ITercerosMundo {
	
	
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
