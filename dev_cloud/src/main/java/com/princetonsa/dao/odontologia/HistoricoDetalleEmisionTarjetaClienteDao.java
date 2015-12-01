package com.princetonsa.dao.odontologia;

import java.util.ArrayList;


import com.princetonsa.dto.odontologia.DtoHistoricoDetalleEmisionTarjetaCliente;

public interface HistoricoDetalleEmisionTarjetaClienteDao {

public boolean modificar(DtoHistoricoDetalleEmisionTarjetaCliente dtoNuevo, DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoHistoricoDetalleEmisionTarjetaCliente> cargar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoHistoricoDetalleEmisionTarjetaCliente dto) ;
	
}
