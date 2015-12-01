package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionArt;

/**
 * Define la logica de negocio relacionada con la valorización 
 * de las clases de inventario para una parametrización del presupuesto
 * @author diecorqu
 *
 */
public interface IDetalleValorizacionArticuloDAO {

	/**
	 * Este método retorna la valorizacion por el codigo del detalle
	 * 
	 * @param codigo detalle de la Clase de inventario
	 * @return DetalleValorizacionArt
	 */
	public DetalleValorizacionArt findById(long codDetalle);
	
	/**
	 * Este método retorna la valorizacion para el detalle de la clase de inventario
	 * 
	 * @param código de la parametrizacion requerida
	 * @return ArrayList<ValorizacionPresupuesto>
	 */
	public ArrayList<DetalleValorizacionArt> detalleValorizacionArticulo(
			long codigoParametrizacion, long nivelAtencion);
	
	/**
	 * Este método guarda la valorización del detalle ingresado 
	 * 
	 * @param Valorizacion del detalle
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarValorizacionDetalleArticulo(
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulo);
	
	/**
	 * Este método verifica si existe una valorización para
	 * la clase de inventario dado el código de la parametrización
	 * y el nivel de atención
	 * 
	 * @param codigo de la parametrazación
	 * @param código nivel de atención
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleClaseInventario(long codParametrizacion, 
			long consecutivoNivelAtencion);
	
	/**
	 * Este método retorna una lista con la valorizacion de una parametrización
	 * detallada del nivel de atención de Clase de Inventario
	 * 
	 * @param código del contrato
	 * @param código nivel de atención
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionArt> obtenerValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion);
	
	/**
	 * Este método modifica la valorización del detalle de la 
	 * clase de inventario ingresado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 */
	public DetalleValorizacionArt modificarValorizacionDetalleClaseInventario(
			DetalleValorizacionArt detallesValorizacionClaseInventario);
	
	/**
	 * Este método elimina la valorización del detalle del artículo ingresado
	 * 
	 * @param código valorización a eliminar
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean eliminarValorizacionDetalleArticulo(int codigo);
	
	/**
	 * Este método verifica si existe una parametrizacion detallada de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @author Ricardo Ruiz
	 * @param codigo del contrato
	 * @param mesAnio
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleClaseInventario(int codigoContrato, Calendar mesAnio);
	
	/**
	 * Obtiene los servicios presupuestados por
	 * nivel de atención de un contrato para un mes determinado. 
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
