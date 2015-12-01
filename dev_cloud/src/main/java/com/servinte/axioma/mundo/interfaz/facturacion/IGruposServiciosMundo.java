package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.GruposServicios;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 8/09/2010
 */
public interface IGruposServiciosMundo {
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicioActivos();
	
	
	/**
	 * Este M�todo se encarga de obtener los distintos grupos de servicios
	 * que tienen asociado un servicio el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atenci�n para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<GruposServicios>
	 * 
	 */
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Este M�todo se encarga de obtener los distintos grupos de servicios
	 * que tienen asociado un servicio el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atenci�n para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<GruposServicios>
	 */
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses);

	/**
	 * Este M�todo se encarga de obtener el grupo de servicio 
	 * al cual est� asociado el servicio de la Orden. 
	 *  
	 * @author, Camilo G�mez
	 * 
	 * @param codServicio
	 * @return GruposServicios
	 */	
	public GruposServicios buscarGrupoServicioPorServicio(int codServicio);
	/**
	 * Este M�todo se encarga de obtener todos los grupos servicio
	 *  
	 * @author, Camilo G�mez
	 * 
	 * @param codServicio
	 * @return GruposServicios
	 */	
	public ArrayList<GruposServicios> buscarGruposServicio();
	
	/**
	 * Este Metodo se encarga de consultar los grupos de servicio
	 *  
	 * @author ginsotfu
	 * @param 
	 * @return List<GrupoServicioDto>
	 */		
	public List<GrupoServicioDto> consultarGruposServicio() throws IPSException;
}
