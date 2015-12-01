package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosCajaDAO;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.TrasladoCajaPrincipalMayor;
import com.servinte.axioma.orm.delegate.tesoreria.MovimientosCajaDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TrasladoCajaPrincipalMayorDelegate;

/**
 * Implementación de la interfaz {@link IMovimientosCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia - Cristhian Murillo
 */

public class MovimientosCajaHibernateDAO implements IMovimientosCajaDAO{
	
	private MovimientosCajaDelegate delegate = new MovimientosCajaDelegate();
	private TrasladoCajaPrincipalMayorDelegate trasladoCajaPrincipalMayorDelegate = new TrasladoCajaPrincipalMayorDelegate();

	@Override
	public MovimientosCaja obtenerUltimoTurnoCierre(Cajas caja, MovimientosCaja movimientoCaja) {
		return delegate.obtenerUltimoTurnoCierre(caja,movimientoCaja);
	}

	@Override
	public MovimientosCaja findById(long pk) {
		return delegate.findById(pk);
	}

	@Override
	public boolean guardarMovimientoCaja(MovimientosCaja movimientosCaja) {
		return	delegate.guardarMovimientoCaja(movimientosCaja);
	}
	
	@Override
	public SolicitudTrasladoCaja obtenerSolicitudTraslado(long idMovimiento){
		return delegate.obtenerSolicitudTraslado(idMovimiento);
	}

	@Override
	public List<MovimientosCaja> consultaEntregaTransportadora(
			DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora) {
		
		return delegate.ConsultaEntregaTransportadora(dtoConsultaEntregaTransportadora);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosCajaDAO#consultarCierreArqueo(com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo)
	 */
	@Override
	public List<MovimientosCaja> consultarCierreArqueo(	DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo) {
		
		return delegate.consultarCierreArqueo(dtoBusquedaCierreArqueo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosCajaDAO#consultarEntregaAsociadaArqueoParcial(long)
	 */
	@Override
	public MovimientosCaja consultarEntregaAsociadaArqueoParcial(long codigoMovimientoArqueo) {
	
		return delegate.consultarEntregaAsociadaArqueoParcial(codigoMovimientoArqueo);
	}
	
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(Date fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formasPago){ 
		return delegate.consultarconsolidadoCierre(fecha,centroAtencion,empresaInstitucion,formasPago);
	}
	
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(Date fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formasPago){
		return delegate.consultarconsolidadoCierreCajaCajero(fecha,centroAtencion,empresaInstitucion,formasPago);
	}

	@Override
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoCajaMayorPrincipal(DtoConsolidadoCierreReporte parametros) 
	{
		return delegate.consultaTrasladoCajaMayorPrincipal(parametros);
	}
	
	@Override
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoHaciaCajaMayor(DtoConsolidadoCierreReporte parametros) 
	{
		return delegate.consultaTrasladoHaciaCajaMayor(parametros);
	}

	@Override
	public boolean persistTrasladoCajaPrincipalMayor(TrasladoCajaPrincipalMayor transientInstance) {
		return trasladoCajaPrincipalMayorDelegate.persistTrasladoCajaPrincipalMayor(transientInstance);
	}
	
}
