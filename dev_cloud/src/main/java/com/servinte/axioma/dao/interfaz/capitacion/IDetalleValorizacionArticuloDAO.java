package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionArt;

/**
 * Define la logica de negocio relacionada con la valorizaci�n 
 * de las clases de inventario para una parametrizaci�n del presupuesto
 * @author diecorqu
 *
 */
public interface IDetalleValorizacionArticuloDAO {

	/**
	 * Este m�todo retorna la valorizacion por el codigo del detalle
	 * 
	 * @param codigo detalle de la Clase de inventario
	 * @return DetalleValorizacionArt
	 */
	public DetalleValorizacionArt findById(long codDetalle);
	
	/**
	 * Este m�todo retorna la valorizacion para el detalle de la clase de inventario
	 * 
	 * @param c�digo de la parametrizacion requerida
	 * @return ArrayList<ValorizacionPresupuesto>
	 */
	public ArrayList<DetalleValorizacionArt> detalleValorizacionArticulo(
			long codigoParametrizacion, long nivelAtencion);
	
	/**
	 * Este m�todo guarda la valorizaci�n del detalle ingresado 
	 * 
	 * @param Valorizacion del detalle
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarValorizacionDetalleArticulo(
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulo);
	
	/**
	 * Este m�todo verifica si existe una valorizaci�n para
	 * la clase de inventario dado el c�digo de la parametrizaci�n
	 * y el nivel de atenci�n
	 * 
	 * @param codigo de la parametrazaci�n
	 * @param c�digo nivel de atenci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeValorizacionDetalleClaseInventario(long codParametrizacion, 
			long consecutivoNivelAtencion);
	
	/**
	 * Este m�todo retorna una lista con la valorizacion de una parametrizaci�n
	 * detallada del nivel de atenci�n de Clase de Inventario
	 * 
	 * @param c�digo del contrato
	 * @param c�digo nivel de atenci�n
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionArt> obtenerValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion);
	
	/**
	 * Este m�todo modifica la valorizaci�n del detalle de la 
	 * clase de inventario ingresado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public DetalleValorizacionArt modificarValorizacionDetalleClaseInventario(
			DetalleValorizacionArt detallesValorizacionClaseInventario);
	
	/**
	 * Este m�todo elimina la valorizaci�n del detalle del art�culo ingresado
	 * 
	 * @param c�digo valorizaci�n a eliminar
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean eliminarValorizacionDetalleArticulo(int codigo);
	
	/**
	 * Este m�todo verifica si existe una parametrizacion detallada de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @author Ricardo Ruiz
	 * @param codigo del contrato
	 * @param mesAnio
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeValorizacionDetalleClaseInventario(int codigoContrato, Calendar mesAnio);
	
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
	public List<DtoProductoServicioReporte> obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio);
}
