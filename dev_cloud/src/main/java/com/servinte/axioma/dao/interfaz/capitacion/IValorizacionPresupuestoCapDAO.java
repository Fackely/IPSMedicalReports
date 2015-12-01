package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;

import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.ValorizacionPresCapGen;

/**
 * Define la logica de negocio relacionada con la valorización 
 * de la parametrización del presupuesto de capitación
 * @author diecorqu
 *
 */
public interface IValorizacionPresupuestoCapDAO {

	/**
	 * Este método retorna la valorizacion para un mes y nivel de atención dado
	 * por codigo
	 * 
	 * @param codigo de valorizacion de presupuesto
	 * @return {@link ValorizacionPresCapGen}
	 */
	public ValorizacionPresCapGen findById(long codValorizacion);
	
	/**
	 * Este método retorna la valorizacion para el presupuesto capitado 
	 * dado
	 * 
	 * @param código de la parametrizacion requerida
	 * @return ArrayList<{@link ValorizacionPresCapGen}>
	 */
	public ArrayList<ValorizacionPresCapGen> valoracionPresupuestoCap(long codigoParametrizacion);
	
	/**
	 * Este método guarda la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param ArrayList<ValorizacionPresCapGen> lista con las valorizaciones para un presupuesto general
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarValorizacionPresupuestoCapitado(
			ArrayList<ValorizacionPresCapGen> valoracionPresupuesto);
	
	/**
	 * Este método modifica la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param ValorizacionPresCapGen valorizacion
	 * @return boolean con el resultado de la operación de guardado
	 */
	public ValorizacionPresCapGen modificarValorizacionPresupuestoCapitado(
			ValorizacionPresCapGen parametrizacionPresupuesto);
	
	/**
	 * Este método elimina la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param código valorizacion a eliminar
	 * @return boolean con el resultado de la operación
	 */
	public void eliminarValorizacionPresupuestoCapitado(ValorizacionPresCapGen unidadValorizcion);
	
	/**
	 * Obtiene la valorización general ingresada para un nivel de atención, subgrupo y mes 
	 * 
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param mesAnio
	 * @param grupoClase
	 * @return ArrayList<Convenios>
	*/
	public ValorizacionPresCapGen obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(long codigoParametrizacionGeneral, 
			long consecutivoNivel, int mes, String grupoClase);
	
	/**
	 * Obtiene el total presupuestado de Articulos o Servicios por
	 * nivel de atención de un contrato para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param mesAnio
	 * @param grupoClase
	 * @return ArrayList<Convenios>
	*/
	public DtoTotalProceso obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio, String grupoClase);
	
	/**
	 * Verifica si existe una valorización general ingresada para una parametrizacion
	 * de presupuesto de capitación, un nivel de atención y subgrupo determinado
	 * 
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param grupoClase
	 * @return boolean
	 * @author diecorqu
	*/
	public boolean existeValorizacionGeneralxNivelAtencionSubSeccion(long codigoParametrizacionGeneral, 
			long consecutivoNivel, String grupoClase);
}
