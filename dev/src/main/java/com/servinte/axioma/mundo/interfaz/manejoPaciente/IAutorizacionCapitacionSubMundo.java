/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;

/**
 * Esta clase se encarga de definir los métodos de
 * negocio de la entidad AutorizacionCapitacionSub
 * 
 * @author Angela Maria Aguirre
 * @since 03/01/2011
 */
public interface IAutorizacionCapitacionSubMundo {
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal de un paciente determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorPaciente(DTOAdministracionAutorizacion dto);
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal en un rango determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto);
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de una autorización de capitación
	 * 
	 * @param AutorizacionesCapitacionSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionCapitacion(AutorizacionesCapitacionSub autorizacion);
	
	
	/**
	 * 
	 * Este Método se encarga de consultar una  autorización de capitación 
	 * subcontratada por su id
	 * 
	 * @param long id
	 * @return AutorizacionesCapitacionSub
	 * @author, Angela Maria Aguirresw
	 *
	 */
	public AutorizacionesCapitacionSub findById(long id);
	
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitaServiArtiInconsistentes(DtoProcesoPresupuestoCapitado dto);

	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicios(DtoProcesoPresupuestoCapitado dto);


	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 *MT6715
	 **/
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServiciosEstado(DtoProcesoPresupuestoCapitado dto);
	

	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -ARTICULOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticulos(DtoProcesoPresupuestoCapitado dto);
	
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para el -SERVICIO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicioAnulada(DtoProcesoPresupuestoCapitado dto);

	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para cada -ARTICULO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticuloAnulada(DtoProcesoPresupuestoCapitado dto);
}
