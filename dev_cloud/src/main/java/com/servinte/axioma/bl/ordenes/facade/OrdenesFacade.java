package com.servinte.axioma.bl.ordenes.facade;

import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.servinte.axioma.bl.manejoPaciente.impl.AutorizacionCapitacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo;
import com.servinte.axioma.bl.ordenes.impl.OrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.impl.SolicitudesMundo;
import com.servinte.axioma.bl.ordenes.interfaz.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo;
import com.servinte.axioma.bl.salasCirugia.impl.PeticionesMundo;
import com.servinte.axioma.bl.salasCirugia.interfaz.IPeticionesMundo;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */
public class OrdenesFacade {


	/**
	 * Metodo que se encarga de obtener los servicios por solicitud
	 * @author Camilo Gomez
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return List<ServicioAutorizacionOrdenDto>
	 * @throws IPSException
	 */
	public List<ServicioAutorizacionOrdenDto> obtenerServiciosPorAutorizar(int codigoOrden,	int claseOrden, int tipoOrden) throws IPSException{
		ISolicitudesMundo solicitudesMundo = new SolicitudesMundo();
		return solicitudesMundo.obtenerServiciosPorAutorizar(codigoOrden, claseOrden, tipoOrden);
	}
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos pendientes por autorizar para una orden medica
	 * @author Camilo Gomez
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @throws IPSException
	 */
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorAutorizar(int codigoOrden)throws IPSException{
		ISolicitudesMundo solicitudesMundo = new SolicitudesMundo();
		return solicitudesMundo.obtenerMedicamentosInsumosPorAutorizar(codigoOrden);
	}
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la orden ambulatoria
	 * 
	 * @param codigoOrden
	 * @param tipoOrden
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	public ContratoDto obtenerConvenioContratoPorOrdenAmbulatoria(Long codigoOrden, int tipoOrden) throws IPSException{
		IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo=new OrdenesAmbulatoriasMundo();
		return ordenesAmbulatoriasMundo.obtenerConvenioContratoPorOrdenAmbulatoria(codigoOrden, tipoOrden);
	}
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la Petición
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	public ContratoDto obtenerConvenioContratoPorPeticion(int codigoOrden) throws IPSException{
		IPeticionesMundo peticionesMundo=new PeticionesMundo();
		return peticionesMundo.obtenerConvenioContratoPorPeticion(codigoOrden);
	}
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la orden médica
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	public ContratoDto obtenerConvenioContratoPorOrdenMedica(int codigoOrden, int claseOrden, int tipoOrden) throws IPSException{
		ISolicitudesMundo solicitudesMundo=new SolicitudesMundo();
		return solicitudesMundo.obtenerConvenioContratoPorOrdenMedica(codigoOrden, claseOrden, tipoOrden);
	}
	
	
	/**
	 * Servicio que permite obtener las autorizaciones de capitación y entidad subcontratada asociadas a las 
	 * ordenes medicas, ordenes ambulatorias y peticiones. 
	 * 
	 * @param claseOrden
	 * @param tipoOrden
	 * @param codOrden
	 * @param Estado
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	
	public List<AutorizacionPorOrdenDto> obtenerAutorizacionCapitacion(int claseOrden, int tipoOrden, Long codigoOrden, List<String> estados) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.obtenerAutorizacionCapitacion(claseOrden, tipoOrden, codigoOrden, estados);
	}
	
	/**
	 * Servicio que permite realizar la anulación  y recalcular el cierre temporal
	 * para las ordenes medicas, ordenes ambulatorias y peticiones que se esten modificando, 
	 * las cuales tengan una autorización asociada. 
	 * 
	 * @param claseOrden
	 * @param tipoOrden
	 * @param autorizacionesPorOrdenDto
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	public boolean procesoAnulacionAutorizacion (int claseOrden, int tipoOrden,List<AutorizacionPorOrdenDto> autorizacionesPorOrdenDto,
	AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto, int codInstitucion) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.procesoAnulacionAutorizacion(claseOrden, tipoOrden, autorizacionesPorOrdenDto,
				anulacionAutorizacionDto, codInstitucion);
	}
	
	/**
	 * Sevicio que permite identificar si una orden ambulatoria se encuentra asociada a una solicitud.
	 * 
	 * @param codOrden
	 * @return
	 * @throws IPSException
	 * @author DiaRuiPe
	 */
	public String existeOrdenAmbAsociadaSolicitud (int numeroSolicitud) throws IPSException{
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
		return autorizacionCapitacionMundo.existeOrdenAmbAsociadaSolicitud(numeroSolicitud);
	}
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos con o sin autorización asociada
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @author DiaRuiPe
	 * @throws IPSException
	 */
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorSolicitud(int codigoOrden)throws IPSException{
		ISolicitudesMundo solicitudesMundo = new SolicitudesMundo();
		return solicitudesMundo.obtenerMedicamentosInsumosPorSolicitud(codigoOrden);
	}
	
	/**
	 * Permite actualizar la cobertura de un articulo con respecto al convenio de una solicitud
	 * 
	 * @param solicitudesSubcuenta
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 5/02/2013
	 */
	public  void  actualizarCobertura(DtoSolicitudesSubCuenta solicitudesSubcuenta, boolean requiereTransaccion) throws IPSException{
		ISolicitudesMundo solicitudesMundo = new SolicitudesMundo();
		solicitudesMundo.actualizarCobertura(solicitudesSubcuenta, requiereTransaccion);
	}
	
}