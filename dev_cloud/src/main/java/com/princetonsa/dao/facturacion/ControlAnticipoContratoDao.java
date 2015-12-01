package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.servinte.axioma.fwk.exception.BDException;

public interface ControlAnticipoContratoDao {

	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoControlAnticiposContrato dtoNuevo, DtoControlAnticiposContrato dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoControlAnticiposContrato> cargar(Connection con, DtoControlAnticiposContrato dtoWhere) throws BDException;
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoControlAnticiposContrato dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoControlAnticiposContrato dto, Connection con) ;
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param valorAnticipoPresupuesto
	 * @return
	 */
	public boolean modificarValorAnticipoReservadoPresupuesto(Connection con, int contrato, BigDecimal valorAnticipoPresupuesto);
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param valorAnticipo
	 * @return
	 */
	public boolean modificarValorAnticipoUtilizadoFactura(Connection con, int contrato, BigDecimal valorAnticipo);
}
