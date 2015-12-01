package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.servinte.axioma.fwk.exception.IPSException;

public interface DetalleServicioHonorariosDao {

	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public boolean modificar(DtoDetalleServicioHonorarios dtoNuevo, DtoDetalleServicioHonorarios dtoWhere , boolean siVacioUpdateNull);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDetalleServicioHonorarios> cargar(	DtoDetalleServicioHonorarios dtoWhere) throws IPSException;
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoDetalleServicioHonorarios dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoDetalleServicioHonorarios dto) ;
	
	/**
	 * 
	 * 
	 */
	public  boolean existeDetalleServicio(double codigoHonorario, int Servicio , int especialidad, double codigoNotIn);
}
