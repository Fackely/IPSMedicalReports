package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntidadesSubDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link AutorizacionesEntidadesSubHibernateDAO}.
 * 
 * @author Cristhian Murillo
 * @see AutorizacionesEntidadesSubDelegate.
 */
public class AutorizacionesEntidadesSubHibernateDAO implements IAutorizacionesEntidadesSubDAO{

	
	private AutorizacionesEntidadesSubDelegate dutorizacionesEntidadesSubDelegate = new AutorizacionesEntidadesSubDelegate(); 

	
	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionesPorEntSub(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}


	@Override
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionesEntidadesSubPorId(id);
	}
	
	
	/**
	 * Obtener autorización por el consecutivo de autorizacion 
	 * 
	 * @param consecutivoAutorizacion
	 * @return
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionEntSubPorConsecutivoAutorizacion (String consecutivoAutorizacion){
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionEntSubPorConsecutivoAutorizacion(consecutivoAutorizacion);
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(AutorizacionesEntidadesSub autorizacion){
		return dutorizacionesEntidadesSubDelegate.guardarAutorizacionEntidadSubcontratada(autorizacion);
	}

	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase AutorizacionesEntidadesSubHome
	 * 
	 * @param AutorizacionesEntidadesSub autorizacion
	 * @return boolean
	 * 
	 * @author, Angela Maria Aguirre, Cristhian Murillo
	 *
	 */
	public boolean sincronizarAutorizacionEntidadSubcontratada(AutorizacionesEntidadesSub autorizacion) {
		return dutorizacionesEntidadesSubDelegate.sincronizarAutorizacionEntidadSubcontratada(autorizacion);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorización de capitación
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionEntidadSubCapitacionPorID(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorización de capitación, 
	 * estas autorizaciones son generadas en un ingreso estancia
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacion){
		return dutorizacionesEntidadesSubDelegate.actualizarAutorizacionEntidadSub(autorizacion);
	}
	
	
	/**
	 * 
	 * Este método se encarga de buscar las autorizaciones en estado autorizado 
	 * según un contrato dado
	 *  
	 * @Author Angela Aguirre
	 */
	public ArrayList<AutorizacionesEntidadesSub> obtenerAutorizacionesContratoID(DtoContrato contrato){
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionesContratoID(contrato);
	}
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada sin importar su vigencia.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubSinVigencia(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas)
	{
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionesPorEntSubSinVigencia(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}

	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones entidades subcontratadas y de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado.
	 * Proceso Autorizaciones anexo 1027 
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author Camilo Gómez
	 */	
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesEntSubServiArti(DtoProcesoPresupuestoCapitado dto){
		return dutorizacionesEntidadesSubDelegate.obtenerAutorizacionesEntSubServiArti(dto);
	}

	/** 
	 * Este Método se encarga de consultar las ordenes autorizadas a entidades subcontratadas  
	 * Anexo 925
	 * 
	 * @param DtoBusquedaTotalOrdenesAutorizadasEntSub dto
	 * @return ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> obtenerOrdenesAutorizadasEntSub(DtoBusquedaTotalOrdenesAutorizadasEntSub dto){
		return dutorizacionesEntidadesSubDelegate.obtenerOrdenesAutorizadasEntSub(dto);
	}
	
}
