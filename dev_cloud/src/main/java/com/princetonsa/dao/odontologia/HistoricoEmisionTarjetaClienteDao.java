package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoHistoricoEmisionTarjetaCliente;

public interface HistoricoEmisionTarjetaClienteDao {

public boolean modificar(DtoHistoricoEmisionTarjetaCliente dtoNuevo, DtoHistoricoEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoHistoricoEmisionTarjetaCliente> cargar(DtoHistoricoEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoHistoricoEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoHistoricoEmisionTarjetaCliente dto) ;
	
	
}
