package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionServ;

/**
 * Define la logica de negocio relacionada con la valorizaci�n 
 * de los grupos de servicio para una parametrizaci�n del presupuesto
 * @author diecorqu
 *
 */
public interface IDetalleValorizacionServicioDAO {

	/**
	 * Este m�todo retorna la valorizacion por el codigo del detalle
	 * 
	 * @param codigo de detalle de GrupoServicio
	 * @return ValorizacionPresupuestoCapitado
	 */
	public DetalleValorizacionServ findById(long codDetalle);
	
	/**
	 * Este m�todo verifica si existe una valorizaci�n para
	 * el grupo de servicio dado el c�digo de la parametrizaci�n
	 * y el nivel de atenci�n
	 * 
	 * @param codigo de la parametrazaci�n
	 * @param c�digo nivel de atenci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion);
	
	/**
	 * Este m�todo retorna la valorizacion para el detalle del grupo de servicio
	 * 
	 * @param c�digo de la valorizacion del grupo de servicio requerida
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionServ> detalleValorizacionServicio(
			long codigoParametrizacion, long nivelAtencion);
	
	/**
	 * Este m�todo guarda la valorizaci�n del detalle ingresado 
	 * 
	 * @param Valorizacion del detalle
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarValorizacionDetalleServicio(
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio);
	
	/**
	 * Este m�todo retorna una lista con la valorizacion de una parametrizaci�n
	 * detallada del nivel de atenci�n de grupo de servicios
	 * 
	 * @param codigo de la parametrazaci�n
	 * @param c�digo nivel de atenci�n
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionServ> obtenerValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion);
	
	/**
	 * Este m�todo modifica la valorizacion del detalle del grupo de servicio ingresado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public DetalleValorizacionServ modificarValorizacionDetalleServicio(
			DetalleValorizacionServ detallesValorizacionServicio);
	
	/**
	 * Este m�todo elimina la valorizacion del detalle del servicio ingresado
	 * 
	 * @param c�digo parametrizaci�n a eliminar
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean eliminarValorizacionDetalleServicio(int codigo);
	
	/**
	 * Este m�todo verifica si existe una parametrizacion detallada de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @author Ricardo Ruiz
	 * @param codigo del contrato
	 * @param mesAnio
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeValorizacionDetalleGrupoServicio(int codigoContrato, Calendar mesAnio);
	
	/**
	 * Obtiene los servicios presupuestados por
	 * nivel de atenci�n de un contrato para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param mesAnio
	 * @return ArrayList<Convenios>
	*/
	public List<DtoProductoServicioReporte> obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio);
}
