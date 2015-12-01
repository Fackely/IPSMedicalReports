package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;

import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.ValorizacionPresCapGen;

/**
 * Define la logica de negocio relacionada con la valorizaci�n 
 * de la parametrizaci�n del presupuesto de capitaci�n
 * @author diecorqu
 *
 */
public interface IValorizacionPresupuestoCapDAO {

	/**
	 * Este m�todo retorna la valorizacion para un mes y nivel de atenci�n dado
	 * por codigo
	 * 
	 * @param codigo de valorizacion de presupuesto
	 * @return {@link ValorizacionPresCapGen}
	 */
	public ValorizacionPresCapGen findById(long codValorizacion);
	
	/**
	 * Este m�todo retorna la valorizacion para el presupuesto capitado 
	 * dado
	 * 
	 * @param c�digo de la parametrizacion requerida
	 * @return ArrayList<{@link ValorizacionPresCapGen}>
	 */
	public ArrayList<ValorizacionPresCapGen> valoracionPresupuestoCap(long codigoParametrizacion);
	
	/**
	 * Este m�todo guarda la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param ArrayList<ValorizacionPresCapGen> lista con las valorizaciones para un presupuesto general
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarValorizacionPresupuestoCapitado(
			ArrayList<ValorizacionPresCapGen> valoracionPresupuesto);
	
	/**
	 * Este m�todo modifica la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param ValorizacionPresCapGen valorizacion
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public ValorizacionPresCapGen modificarValorizacionPresupuestoCapitado(
			ValorizacionPresCapGen parametrizacionPresupuesto);
	
	/**
	 * Este m�todo elimina la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param c�digo valorizacion a eliminar
	 * @return boolean con el resultado de la operaci�n
	 */
	public void eliminarValorizacionPresupuestoCapitado(ValorizacionPresCapGen unidadValorizcion);
	
	/**
	 * Obtiene la valorizaci�n general ingresada para un nivel de atenci�n, subgrupo y mes 
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
	 * nivel de atenci�n de un contrato para un mes determinado. 
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
	 * Verifica si existe una valorizaci�n general ingresada para una parametrizacion
	 * de presupuesto de capitaci�n, un nivel de atenci�n y subgrupo determinado
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
