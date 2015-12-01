package com.servinte.axioma.bl.ordenes.interfaz;

import java.util.List;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionesSolicitudesDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Solicitudes;

/**
 * @author wilgomcr
 * @version 1.0
 * @created 29-jun-2012 11:45:01 a.m.
 */
public interface ISolicitudesMundo {

	
	
	/**
	 * Método que se encarga de asociar las solicitudes generadas con la autorización.
	 * @param autorizacionesEntidadesSub
	 * @param tipoOrden
	 * @param codigoServiArti
	 * @param codSolicitud
	 * @throws IPSException
	 */
	void asociarSolicitudesAutorizaciones(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int tipoOrden, int codigoServiArti, int codSolicitud, boolean requiereTransaccion)throws IPSException;

	/**
	 * Retorna las solicitudes de una autorizacion y que provienen de una orden ambulatoria
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Jeison Londono
	 */
	List<AutorizacionesSolicitudesDto> obtenerSolicitudesAutorizacionConOrdenAmbulatoria(
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionEntSubcontratadasCapitacion) throws IPSException;
	/**
	 * Metodo que se encarga de Anular la solicitud y actualizar en sus repectivas entidades
	 * 
	 * @param anulacionesSolicitud
	 * @throws IPSException
	 */
	void anularSolicitudes(AnulacionAutorizacionSolicitudDto anulacionesSolicitud)throws IPSException;
	
	/**
	 * Consultar una solicitud dado su ID
	 * 
	 * @param numeroSolicitud
	 * @return solicitud consultada
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	Solicitudes obtenerSolicitudPorId(int numeroSolicitud)throws IPSException;
	
	/**
	 * Metodo que se encarga de obtener los servicios por solicitud
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return List<ServicioAutorizacionOrdenDto>
	 * @throws IPSException
	 * @author Camilo Gómez
	 */
	List<ServicioAutorizacionOrdenDto> obtenerServiciosPorAutorizar(int codigoOrden,	int claseOrden, int tipoOrden)throws IPSException;
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos pendientes por autorizar para una orden medica
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @throws IPSException
	 * @author Camilo Gómez
	 */
	List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorAutorizar(int codigoOrden)throws IPSException;
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la orden médica
	 * 
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	ContratoDto obtenerConvenioContratoPorOrdenMedica(int codigoOrden, int claseOrden, int tipoOrden) throws IPSException;	

	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos con o sin autorización asociada
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @author DiaRuiPe
	 * @throws IPSException
	 */
	List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorSolicitud(int codigoOrden)throws IPSException;
	
	/**
	 * Retorna La entidad Ingreso por numero de solicitud 
	 * @param idSolicitud
	 * @return {@link com.servinte.axioma.orm.Ingresos}
	 * @throws IPSException
	 * @author javrammo
	 */
	 Ingresos  obtenerIngresoPorNumeroSolicitud(int idSolicitud) throws IPSException;	

	 /**
	 * Permite actualizar la cobertura de un articulo con respecto al convenio de una solicitud
	 * 
	 * @param solicitudesSubcuenta
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 5/02/2013
	 */
	void  actualizarCobertura(DtoSolicitudesSubCuenta solicitudesSubcuenta, boolean requiereTransaccion) throws IPSException;
}
