package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ProgramasOdontologicosDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseProgramasOdontologicosDao;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class OracleProgramasOdontologicosDao implements ProgramasOdontologicosDao
{
	public ArrayList<DtoPrograma> buscarProgramasXEspecialidad(DtoPrograma dto)
	{
		return SqlBaseProgramasOdontologicosDao.buscarProgramasXEspecialidad(dto);
	}
	
	public boolean insertarPrograma(DtoPrograma dto)
	{
		return SqlBaseProgramasOdontologicosDao.insertarPrograma(dto);
	}
	
	public ArrayList<DtoDetalleProgramas> cargarDetallePrograma(double programa, String codigoManualBusqueda)
	{
		return SqlBaseProgramasOdontologicosDao.cargarDetallePrograma(programa,codigoManualBusqueda);
	}
	
	public boolean insertarDetalle (DtoDetalleProgramas dto)
	{
		return SqlBaseProgramasOdontologicosDao.insertarDetalle(dto);
	}
	
	public boolean insertarLogPrograma(DtoPrograma dto)
	{
		return SqlBaseProgramasOdontologicosDao.insertarLogPrograma(dto);
	}
	
	public boolean eliminarPrograma(double codigoPrograma)
	{
		return SqlBaseProgramasOdontologicosDao.eliminarPrograma(codigoPrograma);
	}
	
	public boolean actualizarPrograma(DtoPrograma dto)
	{
		return SqlBaseProgramasOdontologicosDao.actualizarPrograma(dto);
	}
	
	public boolean insertarLogDetalle(DtoDetalleProgramas dto)
	{
		return SqlBaseProgramasOdontologicosDao.insertarLogDetalle(dto);
	}
	
	public boolean eliminarDetallePrograma(double codigoDetalle)
	{
		return SqlBaseProgramasOdontologicosDao.eliminarDetallePrograma(codigoDetalle);
	}
	
	public boolean actualizarDetalle(String activo, double codigoDetalle)
	{
		return SqlBaseProgramasOdontologicosDao.actualizarDetalle(activo, codigoDetalle);
	}

	@Override
	public String obtenerNombrePrograma(double codigoPk) {
		
		return SqlBaseProgramasOdontologicosDao.obtenerNombrePrograma(codigoPk);
	}
	
	public boolean existeProgramaConNombre(String nombrePrograma)
	{
		return SqlBaseProgramasOdontologicosDao.existeProgramaConNombre(nombrePrograma);
	}
}