package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.orm.CentrosCosto;


/**
 * @author Edgar Carvajal 
 */
public interface ICentroCostoMundo  
{
	
	/**
	 * CARGA EL CENTRO DE COSTOS POR INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public List<DtoCentroCostosVista> busquedaCentroCostos(DtoCentroCostosVista dto);
	
	

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
	 * Carga el centro de costo por su llave primaria
	 * @param id
	 * @return CentrosCosto
	 */
	public CentrosCosto findById(int id);
	
	
	/**
	 *  obtenerCentrosCostoPorViaIngreso
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorViaIngreso(int consecutivoViaIngreso );
	
	
	
	/**
	 * Lista los centros de costo activos por institución que cumplan con el listados de tipos de entidad que ejecuta enviado
	 * Se envia un arreglo de los tipos de entidad que ejecuta que se quieren filtrar
	 * 
	 * @author Cristhian Murillo
	 * @param institucion
	 * @param tipoentidadesEjecuta
	 * @return  List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorInstitucionTipoEntidad(int institucion, String[] tipoentidadesEjecuta);
	
	
	
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
	
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por grupo de servicio
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 23/06/2011
	 * 
	 */	
	public ArrayList<DtoCentroCosto> listaCentroCostoGrupoServicio(int grupoServicio, int codCentroAtencion);
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por unidad de agenda
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 24/06/2011
	 * 
	 */		
	
	public ArrayList<DtoCentroCosto> listaCentroCostoUnidadConsulta(DtoCentroCosto centroCosto);	
	
	
	/**
	 * Retorna los Centros de costo por el Grupo de Servicio enviado
	 * @param gruposervicio
	 * @return ArrayList<DtoCentroCosto>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoCentroCosto> obtenerCentrosCostoPorGrupoServicio(Integer grupoServicio);
	
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta del centro de Atencion de acuerdo al codigo del centro de costo 
	 *  
	 * @param codigoCentroAtencion
	 * @return ArrayList
	 * @author Camilo Gómez
	 */		
	public DtoCentroCosto obtenerCentroAtencionXCentroCosto(DtoCentroCosto centroCosto);
	
}
