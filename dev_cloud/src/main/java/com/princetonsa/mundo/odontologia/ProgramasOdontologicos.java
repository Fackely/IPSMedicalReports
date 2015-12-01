package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseProgramasOdontologicosDao;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class ProgramasOdontologicos
{
	public static ArrayList<DtoPrograma> buscarProgramasXEspecialidad(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().buscarProgramasXEspecialidad(dto);
	}
	
	public static boolean insertarPrograma(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().insertarPrograma(dto);
	}
	
	public static ArrayList<DtoDetalleProgramas> cargarDetallePrograma(double programa, String codigoManualBusqueda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().cargarDetallePrograma(programa,codigoManualBusqueda);
	}
	
	public static ArrayList<DtoDetalleProgramas> cargarDetallePrograma(double programa )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().cargarDetallePrograma(programa,ConstantesBD.codigoTarifarioCups+"");
	}
	
	public static boolean insertarDetalle (DtoDetalleProgramas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().insertarDetalle(dto);
	}
	
	public static boolean insertarLogPrograma (DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().insertarLogPrograma(dto);
	}
	
	public static boolean eliminarPrograma(double codigoPrograma)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().eliminarPrograma(codigoPrograma);
	}
	
	public static boolean actualizarPrograma(DtoPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().actualizarPrograma(dto);
	}
	
	public static boolean insertarLogDetalle (DtoDetalleProgramas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().insertarLogDetalle(dto);
	}
	
	public static boolean eliminarDetallePrograma(double codigoDetalle)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().eliminarDetallePrograma(codigoDetalle);
	}
	
	public static boolean actualizarDetalle(String activo, double codigoDetalle)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().actualizarDetalle(activo,codigoDetalle);
	}

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public static String obtenerNombrePrograma(double codigoPk)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().obtenerNombrePrograma(codigoPk);
	}	
	
	public static boolean existeProgramaConNombre(String nombrePrograma)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasOdontologicosDao().existeProgramaConNombre(nombrePrograma);
	}
}