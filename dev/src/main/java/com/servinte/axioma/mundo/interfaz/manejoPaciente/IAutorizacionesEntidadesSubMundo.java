/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoTotalesOrdenesEntidadesSub;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;

/**
 * Esta clase se encarga de definir los m�todos de 
 * negocio de la entidad Autorizaciones Entidad Subcontratada
 * 
 * @author Angela Maria Aguirre
 * @since 09/01/2011
 */
public interface IAutorizacionesEntidadesSubMundo {
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorizaci�n de capitaci�n
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto);
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorizaci�n de capitaci�n, 
	 * estas autorizaciones son generadas en un ingreso estancia
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto);
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar el detalle de una autorizaci�n de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacion);
	
	
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id);
	
	/**
	 * Obtener autorizaci�n por el consecutivo de autorizacion 
	 * 
	 * @param consecutivoAutorizacion
	 * @return
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionEntSubPorConsecutivoAutorizacion (String consecutivoAutorizacion);
	
	
	/**
	 * 
	 * Este m�todo se encarga de buscar las autorizaciones en estado autorizado 
	 * seg�n un contrato dado
	 *  
	 * @Author Angela Aguirre
	 */
	public ArrayList<AutorizacionesEntidadesSub> obtenerAutorizacionesContratoID(DtoContrato contrato);
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada sin importar su vigencia.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubSinVigencia(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar las autorizaciones entidades subcontratadas y de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado.
	 * Proceso Autorizaciones anexo 1027 
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author Camilo G�mez
	 */	
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesEntSubServiArti(DtoProcesoPresupuestoCapitado dto);
	
	/** 
	 * Este M�todo se encarga de consultar las ordenes autorizadas a entidades subcontratadas  
	 * Anexo 925
	 * 
	 * @param DtoBusquedaTotalOrdenesAutorizadasEntSub dto
	 * @return ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> obtenerOrdenesAutorizadasEntSub(DtoBusquedaTotalOrdenesAutorizadasEntSub dto);

	/**
	 * Este M�todo se encarga de consultar ordenar y calcular los totales de las ordenes autorizadas de entidades subcontratadas
	 * Anexo 925
	 * @param DtoBusquedaTotalOrdenesAutorizadasEntSub dto
	 * @return ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>
	 * @author Camilo G�mez
	 */
	public DtoConsultaTotalOrdenesAutorizadasEntSub ordenarOrdenesAutorizadasEntSub(DtoBusquedaTotalOrdenesAutorizadasEntSub dto);
	
}
