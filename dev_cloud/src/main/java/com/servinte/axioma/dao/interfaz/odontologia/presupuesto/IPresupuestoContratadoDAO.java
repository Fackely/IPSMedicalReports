package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;

public interface IPresupuestoContratadoDAO {
	
	/**
	 * Este m&eacute;todo se encarga de obtener los n&uacute;meros de contratos
	 * para los presupuestos ingresados como par&aacute;metros.
	 * @param dto contiene los presupuestos contratados de los cuales se
	 * quere obtener el n&uacute;mero de contrato.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public List<DtoPresupuestoContratado> obtenerContratosPresupuestoOdonto(DtoPresupuestoContratado dto);

}
