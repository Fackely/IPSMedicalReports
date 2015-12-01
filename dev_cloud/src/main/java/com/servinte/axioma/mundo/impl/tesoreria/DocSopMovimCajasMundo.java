package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.MovimientosCajaHome;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * documentos de soporte de los movimientos de caja
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IDocSopMovimCajasMundo
 */
public class DocSopMovimCajasMundo implements IDocSopMovimCajasMundo {

	private IDocSopMovimCajasDAO docSopMovimCajasDAO;
	
	public DocSopMovimCajasMundo() {
		inicializar();
	}

	private void inicializar() {
		docSopMovimCajasDAO = TesoreriaFabricaDAO.crearDocSopMovimCajasDAO();
	}
	
	@Override
	public DtoInformacionEntrega consolidarInformacionAceptacion(long idMovimiento, int codigoInstitucion)
	{
		
		IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo = TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
		
		ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop = new ArrayList<DtoDetalleDocSopor>();
		
		listadoDefinitivoDocSop = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasDAO.obtenerDetallesDocumentosParaAceptacion(idMovimiento);

		MovimientosCaja movimientosCaja = new MovimientosCajaHome().findById(idMovimiento);
		
		
		/*
		 * Se obtiene el listado de aceptaciones con efectivo, se evaluan que esten disponibles para realizar una posible entrega
		 * y se calcula el total disponible a entregar.
		 */
		ArrayList<DtoDetalleDocSopor> listadoAceptacionesEfectivo = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasDAO.obtenerDetallesDocumentosParaAceptacionEfectivo(idMovimiento);

		Log4JManager.info("------------------------------>" + listadoAceptacionesEfectivo.size());
		
		listadoDefinitivoDocSop.addAll(listadoAceptacionesEfectivo);
		
		for(DtoDetalleDocSopor dtoDetDocumento : listadoDefinitivoDocSop)
		{
			dtoDetDocumento = docSopMovimCajasDAO.obtenerDetallesDocumentosParaAceptacionEntidad(dtoDetDocumento, idMovimiento);
		}
		
		DtoInformacionEntrega dtoInformacionEntrega = consultaArqueoCierreCajaMundo.obtenerDtoInformacionEntregaImpresion(listadoDefinitivoDocSop, codigoInstitucion);
		dtoInformacionEntrega.setMovimientosCaja(movimientosCaja);
		
		
		/*
		 * Se obtiene el listado de aceptaciones con efectivo, se evaluan que esten disponibles para realizar una posible entrega
		 * y se calcula el total disponible a entregar.
		 */
		//ArrayList<DtoDetalleDocSopor> listadoAceptacionesEfectivo = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasDAO.obtenerDetallesDocumentosParaAceptacionEfectivo(idMovimiento);

		//Log4JManager.info("------------------------------>" + listadoAceptacionesEfectivo.size());
		
//		if(listadoAceptacionesEfectivo.size()>0){
//			
//			ArrayList<Integer> codigosTipoMovimiento = new ArrayList<Integer>();
//			codigosTipoMovimiento.add(ConstantesBD.codigoTipoMovimientoSolicitudTraslado);
//			//dtoInformacionEntrega = movimientosCajaMundo.agregarValorEfectivoAceptacion(dtoInformacionEntrega, listadoAceptacionesEfectivo, codigosTipoMovimiento, ConstantesBD.codigoNuncaValidoLong);
//		}

//		for(DtoFormaPagoDocSoporte detalle:dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes())
//		{
//			Log4JManager.info("---------> "+detalle.getFormaPago());
//		}
		
		return dtoInformacionEntrega;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo#obtenerDocSoportePorMovimiento(long)
	 */
	@Override
	public List<DocSopMovimCajas> obtenerDocSoportePorMovimiento(long codigoMovimientosCaja) {
	
		return docSopMovimCajasDAO.obtenerDocSoportePorMovimiento(codigoMovimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo#obtenerDetallesDocumentosParaAceptacion(long)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacion(long idMovimiento) {
		
		return docSopMovimCajasDAO.obtenerDetallesDocumentosParaAceptacion(idMovimiento);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo#obtenerDetallesDocumentosParaAceptacionEfectivo(long)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacionEfectivo(long idMovimiento) {
		
		return docSopMovimCajasDAO.obtenerDetallesDocumentosParaAceptacionEfectivo(idMovimiento);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo#obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(MovimientosCaja movimientosCaja) {
		
		return docSopMovimCajasDAO.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(movimientosCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo#obtenerAceptacionesCajaNoFormaPagoNinguno(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerAceptacionesCajaNoFormaPagoNinguno(MovimientosCaja movimientosCaja) {
		
		return docSopMovimCajasDAO.obtenerAceptacionesCajaNoFormaPagoNinguno(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo#obtenerTotalesEntregasFormaPagoNinguno(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesEntregasFormaPagoNinguno(	MovimientosCaja movimientosCaja) {
	
		return docSopMovimCajasDAO.obtenerTotalesEntregasFormaPagoNinguno(movimientosCaja);
	}
	
}
