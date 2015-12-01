package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.servinte.axioma.orm.TrasladosAbonos;


/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface ITrasladosAbonosDAO 
{
	
	
	/**
	 * Guarda el traslado
	 * @param transientInstance
	 * @return
	 */
	public boolean guardarTraslado(TrasladosAbonos transientInstance);
	
	
	
	/**
	 * Retorna los detalles  de un traslado de abonos enviando como parametro el codigo del traslado
	 * @param dtoConsulta
	 */
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(DtoConsultaTrasladoAbonoPAciente dtoConsulta);
	
}
