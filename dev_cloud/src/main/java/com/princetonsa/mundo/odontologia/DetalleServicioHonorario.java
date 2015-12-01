package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.servinte.axioma.fwk.exception.IPSException;

public class DetalleServicioHonorario {
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 * @throws IPSException 
	 */
	
	
	
	public static ArrayList<DtoDetalleServicioHonorarios> cargar(	DtoDetalleServicioHonorarios dtoWhere) throws IPSException 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleServicioHonorariosDao().cargar(dtoWhere);
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDetalleServicioHonorarios dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleServicioHonorariosDao().eliminar(dtoWhere);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoDetalleServicioHonorarios dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleServicioHonorariosDao().guardar(dto);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoDetalleServicioHonorarios dtoNuevo,DtoDetalleServicioHonorarios dtoWhere , boolean siVacioUpdateNull) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleServicioHonorariosDao().modificar(dtoNuevo, dtoWhere , siVacioUpdateNull);
	}
	
	
	/***
	 * 
	 * 
	 * 
	 */

	public static boolean existeDetalleServicio(double codigoHonorario, int Servicio,
			int especialidad, double codigoNotIn) {
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleServicioHonorariosDao().existeDetalleServicio(codigoHonorario, Servicio, especialidad, codigoNotIn);
	}

}
