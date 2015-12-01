package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionServ;

/**
 * Define la logica de negocio relacionada con la valorización 
 * de los grupos de servicio para una parametrización del presupuesto
 * @author diecorqu
 *
 */
public interface IDetalleValorizacionServicioDAO {

	/**
	 * Este método retorna la valorizacion por el codigo del detalle
	 * 
	 * @param codigo de detalle de GrupoServicio
	 * @return ValorizacionPresupuestoCapitado
	 */
	public DetalleValorizacionServ findById(long codDetalle);
	
	/**
	 * Este método verifica si existe una valorización para
	 * el grupo de servicio dado el código de la parametrización
	 * y el nivel de atención
	 * 
	 * @param codigo de la parametrazación
	 * @param código nivel de atención
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion);
	
	/**
	 * Este método retorna la valorizacion para el detalle del grupo de servicio
	 * 
	 * @param código de la valorizacion del grupo de servicio requerida
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionServ> detalleValorizacionServicio(
			long codigoParametrizacion, long nivelAtencion);
	
	/**
	 * Este método guarda la valorización del detalle ingresado 
	 * 
	 * @param Valorizacion del detalle
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarValorizacionDetalleServicio(
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio);
	
	/**
	 * Este método retorna una lista con la valorizacion de una parametrización
	 * detallada del nivel de atención de grupo de servicios
	 * 
	 * @param codigo de la parametrazación
	 * @param código nivel de atención
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionServ> obtenerValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion);
	
	/**
	 * Este método modifica la valorizacion del detalle del grupo de servicio ingresado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 */
	public DetalleValorizacionServ modificarValorizacionDetalleServicio(
			DetalleValorizacionServ detallesValorizacionServicio);
	
	/**
	 * Este método elimina la valorizacion del detalle del servicio ingresado
	 * 
	 * @param código parametrización a eliminar
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean eliminarValorizacionDetalleServicio(int codigo);
	
	/**
	 * Este método verifica si existe una parametrizacion detallada de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @author Ricardo Ruiz
	 * @param codigo del contrato
	 * @param mesAnio
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleGrupoServicio(int codigoContrato, Calendar mesAnio);
	
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
	public List<DtoProductoServicioReporte> obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio);
}
