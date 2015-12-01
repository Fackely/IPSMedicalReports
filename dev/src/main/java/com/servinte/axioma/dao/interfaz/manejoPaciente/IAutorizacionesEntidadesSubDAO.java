package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;


/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface IAutorizacionesEntidadesSubDAO {
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada enviada.
	 * Filtrando por su estado
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas);
	
	
	
	/**
	 * Implementacion del m�todo findById
	 * @param id
	 * @return AutorizacionesEntidadesSub
	 */
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
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de autorizaci�n de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(AutorizacionesEntidadesSub autorizacion);
	
	

	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase AutorizacionesEntidadesSubHome
	 * 
	 * @param AutorizacionesEntidadesSub autorizacion
	 * @return boolean
	 * 
	 * @author, Angela Maria Aguirre, Cristhian Murillo
	 *
	 */
	public boolean sincronizarAutorizacionEntidadSubcontratada(AutorizacionesEntidadesSub autorizacion);
	
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
	
}