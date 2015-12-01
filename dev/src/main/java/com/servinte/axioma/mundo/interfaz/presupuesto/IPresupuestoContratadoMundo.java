package com.servinte.axioma.mundo.interfaz.presupuesto;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;

public interface IPresupuestoContratadoMundo {
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de obtener la informaci&oacute;n
	 * sobre los ingresos con valoraci&oacute;n inicial que tienen 
	 * presupuesto.
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos>  consolidarInfoIngresosValIniConPresupuesto (ArrayList<DtoIngresosOdontologicos> listaIngresosConPresupuesto);
	
	/**
	 * Este m&eacute;todo se encarga de listar los indicativos de
	 * contrato para los presupuestos en estado contratado.
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIntegridadDominio> listarIndicativoContrato();
	
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener la informaci&oacute;n
	 * de valores de presupuestos contratados 
	 * 
	 * @author Diana Carolina G&oacute;mez
	 */
	
	public ArrayList<DtoPresupuestosOdontologicosContratados>  consolidarConsultaPresupuestosContratados (ArrayList<DtoPresupuestosOdontologicosContratados> listaPresupuestosContratados);
	
	/**
	 * Este m&eacute;todo se encarga de obtener la informaci&oacute;n
	 * de valores de presupuestos contratados con promocion 
	 * 
	 * @author Javier Gonz&aacute;lez
	 */
	
	public ArrayList<DtoPresupuestosOdontologicosContratadosConPromocion>  consolidarConsultaPresupuestosContratadosConPromocion (ArrayList<DtoPresupuestosOdontologicosContratadosConPromocion> listaPresupuestosContratados);
	
}
