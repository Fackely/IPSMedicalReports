package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleServicioHonorarioDao;
import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;

public class DetalleAgrupacionHonorario 
{


	private static Logger logger = Logger.getLogger(DetalleAgrupacionHonorario.class);
	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public static boolean modificar(DtoDetalleAgrupacionHonorarios dtoNuevo, DtoDetalleAgrupacionHonorarios dtoWhere, boolean siVacioUpdateNull)
	{
		logger.info(dtoNuevo.getTipoServicio().getCodigo() +"==" + dtoWhere.getTipoServicio().getCodigo());
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleAgrupacionHonorarioDao().modificar(dtoNuevo, dtoWhere, siVacioUpdateNull); 
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleAgrupacionHonorarios> cargar(	DtoDetalleAgrupacionHonorarios dtoWhere)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleAgrupacionHonorarioDao().cargar(dtoWhere);
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDetalleAgrupacionHonorarios dtoWhere)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleAgrupacionHonorarioDao().eliminar(dtoWhere);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoDetalleAgrupacionHonorarios dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleAgrupacionHonorarioDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param codigoHonorario
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @return
	 */
	public static boolean existeDetalleAgrupacion(double codigoHonorario, int grupoServicio, String tipoServicio, int especialidad,  double codigoNotIn)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleAgrupacionHonorarioDao().existeDetalleAgrupacion(codigoHonorario, grupoServicio, tipoServicio, especialidad, codigoNotIn);
	}

}
