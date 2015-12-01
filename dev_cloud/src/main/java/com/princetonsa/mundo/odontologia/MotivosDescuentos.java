package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;

/**
 * 
 * @author axioma
 *
 */
public class MotivosDescuentos 
{
	/**
	 * 
	 * @param objetoDescuento
	 * @return
	 */
	public static double guardar(	DtoMotivoDescuento dto )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDescuentosDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param objetoDescuento
	 * @return
	 */
	public static ArrayList<DtoMotivoDescuento> cargar(DtoMotivoDescuento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDescuentosDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param objetoDescuentoNuevo
	 * @param objetoDtoWhere
	 * @return
	 */
	public static boolean modificar(DtoMotivoDescuento dtoNuevo, DtoMotivoDescuento dtoWhere)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDescuentosDao().modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * @param objetoDescuento
	 * @return
	 */
	public static boolean eliminar(DtoMotivoDescuento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDescuentosDao().eliminar(dto);
	}
	

	
	/**
	 * M&eacute;todo para validar existencia de un motivo de atenci&oacute;n.
	 * en el Descuento Odontologico
	 * Retorna la cantidad de registros encontrados
	 * @param codigoPkMotivo
	 * @return int
	 */
	public static int validarExistenciaMotivos(double codigoPkMotivo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDescuentosDao().validarExistenciaMotivos(codigoPkMotivo);
	}

	

}
