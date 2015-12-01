package com.princetonsa.dao.odontologia;

import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import java.util.*;

/**
 * 
 * @author axioma
 *
 */
public interface MotivosDescuentosDao 
{
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoMotivoDescuento dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoMotivoDescuento> cargar(DtoMotivoDescuento dto);
	
	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public boolean modificar(DtoMotivoDescuento dtoNuevo, DtoMotivoDescuento dtoWhere);

	/**
	 * 
	 * @param objetoDescuento
	 * @return
	 */
	public boolean eliminar(DtoMotivoDescuento dto);
	
	
	/**
	 * M&eacute;todo para validar existencia de un motivo de atenci&oacute;n.
	 * en el Descuento Odontologico
	 * @param codigoPkMotivo
	 * @return
	 */
	public int validarExistenciaMotivos(double codigoPkMotivo);

}
