package com.servinte.axioma.servicio.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.RecibosCajaId;

public interface ICentroAtencionServicio
{
	/**
	 * Lista los centros de atenci&oacute;n activos
	 * @param activo indica si se deben filtrar solo los activos
	 * @return {@link ArrayList}<{@link DtoCentrosAtencion}>
	 */
	public ArrayList<DtoCentrosAtencion> listarCentrosAtencion (boolean activo);

	/**
	 * Lista los centros de atenci&oacute;n de acuerdo a los atributos asignados al Dto
	 * @param dtoCentrosAtencion {@link DtoCentrosAtencion} Par&aacute;metros de búsqueda
	 * @return {@link ArrayList}<{@link DtoCentrosAtencion}>
	 */
	public ArrayList<DtoCentrosAtencion> listarCentrosAtencion (DtoCentrosAtencion dtoCentrosAtencion);
	
	/**
	 * Este m&eacute;todo se encarga de retornar el c&oacute;digo de los centros 
	 * de atenci&oacute;n en los que se han dado ingresos.
	 * @param ingresos
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionIngresos (List<Integer> ingresos);
	
	
	/**
	 * Metodo que buscar un centro de atencion por el codigo pk
	 * @param codigos
	 * @return
	 */
	public CentroAtencion buscarPorCodigoPK(int codigo);
	
	/**
	 * Retorna los centros de atenci&oacute;n en los cuales
	 * se registraron los presupuestos odontol&oacute; contratados  
	 * @param presupuesto
	 * @return listaCentros
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionPresupuestos (List<Long> presupuesto);
	
	/**
	 * M&eacute;todo que retorna los centros de atenci&oacute;n en los 
	 * cuales quedan asociados los recibos de caja.
	 * @param numeroRC
	 * @return ArrayList<DtoCentrosAtencion>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionRecibosCaja (List<RecibosCajaId> numeroRC);

}
