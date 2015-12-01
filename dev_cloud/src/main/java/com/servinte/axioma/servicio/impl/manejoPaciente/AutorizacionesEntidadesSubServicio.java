/**
 * 
 */
package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntidadesSubMundo;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Autorizaciones Entidad Subcontratada
 * 
 * @author Angela Maria Aguirre
 * @since 09/01/2011
 */
public class AutorizacionesEntidadesSubServicio implements
		IAutorizacionesEntidadesSubServicio {
	
	
	IAutorizacionesEntidadesSubMundo mundo;
	
	public AutorizacionesEntidadesSubServicio(){
		mundo = ManejoPacienteFabricaMundo.crearAutorizacionEntidadesSubMundo();
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
	@Override
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto) {
		
		return mundo.obtenerAutorizacionEntidadSubCapitacionPorID(dto);
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
	@Override
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto) {
		
		return mundo.obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(dto);
	}
	
	/** Este Método se encarga de actualizar el detalle de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacion){
		return mundo.actualizarAutorizacionEntidadSub(autorizacion);
	}
	
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return mundo.obtenerAutorizacionesEntidadesSubPorId(id);
	}
	
	/**
	 * Obtener autorización por el consecutivo de autorizacion 
	 * 
	 * @param consecutivoAutorizacion
	 * @return
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionEntSubPorConsecutivoAutorizacion (String consecutivoAutorizacion){
		return mundo.obtenerAutorizacionEntSubPorConsecutivoAutorizacion(consecutivoAutorizacion);
	}
	
	
	/**
	 * 
	 * Este método se encarga de buscar las autorizaciones en estado autorizado 
	 * según un contrato dado
	 *  
	 * @Author Angela Aguirre
	 */
	public ArrayList<AutorizacionesEntidadesSub> obtenerAutorizacionesContratoID(DtoContrato contrato){
		return mundo.obtenerAutorizacionesContratoID(contrato);
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
		return mundo.obtenerAutorizacionesPorEntSubSinVigencia(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}

}
