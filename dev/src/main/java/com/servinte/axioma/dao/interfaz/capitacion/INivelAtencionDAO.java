package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoNivelAtencion;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.orm.NivelAtencion;

public interface INivelAtencionDAO 
{

	
	/**
	 * Este método se encarga de consultar los niveles de atención activos en el sistema.
	 *  
	 * @return ArrayList<NivelAtencion>
	 * 
	 * @author Cristhian Murillo
	 */
	public  ArrayList<NivelAtencion> obtenerNivelesAtencion();
	
	
	
	/**
	 * Implementacion del findById.
	 * 
	 * @param id
	 * @return NivelAtencion
	 * 
	 * @author Cristhian Murillo
	 */
	public NivelAtencion findById(long id);
	
	
	/**
	 * Este método se encarga de consultar los niveles de atencion activos 
	 * en el sistema 
	 * @param 
	 * @return ArrayList<NivelAtencion>
	 */
	public  ArrayList<NivelAtencion> obtenerNivelesAtencionActivos();
	
	/**
	 * Lista los niveles de atención asociados al contrato
	 * 
	 * @param convenio
	 * @return ArrayList<NivelAtencion>
	 */
	public ArrayList<NivelAtencion> listarNivelesAtencionContrato(int codContrato);
	
	/**
	 * Lista los niveles de atención asociados a la parametrización
	 * 
	 * @param convenio
	 * @return ArrayList<NivelAtencion>
	 */
	public ArrayList<NivelAtencion> listarNivelesAtencionParametrizacionPresupuesto(long codParametrizacion);
	
	/**
	 * Lista los niveles de atención de un contrato y que tengan una
	 * parametrización de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param mesAnio
	 * @return ArrayList<DtoNivelReporte>
	*/
	public List<DtoNivelReporte> listarNivelesAtencionConParametrizacionPresupuestoPorContrato(int codigoContrato, Calendar mesAnio);
	
	/**
	 * Lista los niveles de atención de un contrato y que tengan una
	 * parametrización detallada de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param mesAnio
	 * @return ArrayList<DtoNivelAtencion>
	*/
	public List<DtoNivelAtencion> listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(int codigoContrato, Calendar mesAnio);
	
	/**
	 * Lista los distintos niveles de atención relacionados a servicios involucrados en un cierre
	 * para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorConvenio(int codigoConvenio, String proceso, List<Calendar> meses);
	
	/**
	 * Lista los distintos niveles de atención relacionados a servicios involucrados en un cierre
	 * para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorContrato(int codigoContrato, String proceso, List<Calendar> meses);
	
	/**
	 * Lista los distintos niveles de atención relacionados a articulos involucrados en un cierre
	 * para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorConvenio(int codigoConvenio, String proceso, List<Calendar> meses);
	
	/**
	 * Lista los distintos niveles de atención relacionados a articulos involucrados en un cierre
	 * para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorContrato(int codigoContrato, String proceso, List<Calendar> meses);
	
	
}
