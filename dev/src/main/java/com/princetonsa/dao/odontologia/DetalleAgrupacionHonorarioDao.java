/**
 * 
 */
package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;

/**
 * @author axioma
 *
 */
public interface DetalleAgrupacionHonorarioDao {

	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public boolean modificar(DtoDetalleAgrupacionHonorarios dtoNuevo, DtoDetalleAgrupacionHonorarios dtoWhere, boolean siVacioUpdateNull);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDetalleAgrupacionHonorarios> cargar(	DtoDetalleAgrupacionHonorarios dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoDetalleAgrupacionHonorarios dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoDetalleAgrupacionHonorarios dto) ;
	
	/**
	 * 
	 * @param codigoHonorario
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @return
	 */
	public boolean existeDetalleAgrupacion(double codigoHonorario, int grupoServicio, String tipoServicio, int especialidad,  double codigoNotIn);
	
}
