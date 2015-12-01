package com.servinte.axioma.servicio.interfaz.administracion;

import java.util.List;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.orm.CentrosCosto;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public interface ICentroCostosServicio {
	
	
	/**
	 * CARGA EL CENTRO DE COSTOS POR INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public List<DtoCentroCostosVista> busquedaCentroCostos(DtoCentroCostosVista dto);
	
	
	
	/**
	 * Carga el centro de costo por su llave primaria
	 * @param id
	 * @return CentrosCosto
	 */
	public CentrosCosto findById(int id);
	
	/**
	 * 
	 * @param consecutivoViaIngreso
	 * @return
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorViaIngreso(int consecutivoViaIngreso );
	

	/**
	 * Lista los centros de costo por Centro de Atención.
	 * Si se le envia el tipoArea realiza el filtro para el tipo de area del centro de costo
	 * 
	 * @author Cristhian Murillo
	 * @param consecutivoCentroAttencion
	 * @param tipoArea
	 * @return  List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(int consecutivoCentroAttencion, int tipoArea);
	
	
	

	/**
	 * Lista los centros de costo activos por institución que cumplan con el listados de tipos de entidad que ejecuta enviado
	 * Se envia un arreglo de los tipos de entidad que ejecuta que se quieren filtrar
	 * 
	 * @author Cristhian Murillo
	 * @param institucion
	 * @param tipoentidadesEjecuta
	 * @return  List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorInstitucionTipoEntidad(int institucion, String[] tipoentidadesEjecuta);;
	
	
	
	
	/**
	 * Retorna los centro de costo activos para las EntidadesSubcontratadas
	 * 
	 * @param codentidadSubcontratada
	 * @return  List<DtoCentroCostosVista>
	 * 
	 * @author Cristhian Murillo
	*/
	public List<DtoCentroCostosVista> listaCentroCostoActivoXrEntidadesSub (long codentidadSubcontratada);
	
	
	
	/**
	 * Realiza la búsqueda segun la parametrica de centro de costo en donde se selecciona la vía de 
	 *  ingreso y la entidad subcontratada por cada centro de costo.
	 * 
	 * @param parametros
	 * @return CentrosCosto
	 * 
	 * @author Cristhian Murillo
	 */
	public CentrosCosto obtenerCentrosCostoPorViaIngresoEntSub(DTOEstanciaViaIngCentroCosto parametros);
}
