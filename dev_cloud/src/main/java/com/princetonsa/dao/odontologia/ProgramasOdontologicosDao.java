package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoPrograma;

public interface ProgramasOdontologicosDao
{
	public ArrayList<DtoPrograma> buscarProgramasXEspecialidad(DtoPrograma dto); 
	
	public boolean insertarPrograma(DtoPrograma dto);
	
	public ArrayList<DtoDetalleProgramas> cargarDetallePrograma(double programa, String codigoManualBusqueda);
	
	public boolean insertarDetalle(DtoDetalleProgramas dto);
	
	public boolean insertarLogPrograma(DtoPrograma dto);
	
	public boolean eliminarPrograma(double codigoPrograma);
	
	public boolean actualizarPrograma(DtoPrograma dto);
	
	public boolean insertarLogDetalle(DtoDetalleProgramas dto);
	
	public boolean eliminarDetallePrograma (double codigoPrograma);
	
	public boolean actualizarDetalle(String activo, double codigoPrograma);
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public String obtenerNombrePrograma(double codigoPk);
	
	public boolean existeProgramaConNombre(String nombrePrograma);
	
}