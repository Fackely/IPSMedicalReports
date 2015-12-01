package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoFormaPagoReport;
import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.princetonsa.dto.tesoreria.DtoRecibosConceptoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.RecibosCajaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IRecibosCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see RecibosCajaDelegate
 */

public class RecibosCajaHibernateDAO implements IRecibosCajaDAO{

	private RecibosCajaDelegate recibosCajaDaoImp;

	public RecibosCajaHibernateDAO() {
		recibosCajaDaoImp  = new RecibosCajaDelegate();
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO#obtenerRecibosXfechaXusuXcaja(java.util.Date, com.princetonsa.mundo.UsuarioBasico, com.servinte.axioma.orm.Cajas)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCaja(MovimientosCaja movimientosCaja)  {
		
		return recibosCajaDaoImp.obtenerRecibosXMovimientoCaja(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO#obtenerRecibosXMovimientoCajaFecha(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCajaFecha(MovimientosCaja movimientosCaja)  {
		
		return recibosCajaDaoImp.obtenerRecibosXMovimientoCajaFecha(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO#actualizarRecibosAsociadosCierre(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<RecibosCaja> actualizarRecibosAsociadosCierre(MovimientosCaja movimientosCaja) {
		
		return recibosCajaDaoImp.actualizarRecibosAsociadosCierre(movimientosCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO#obtenerFechaUltimoMovimientoRecibo(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoRecibo(TurnoDeCaja turnoDeCaja) {
		
		return recibosCajaDaoImp.obtenerFechaUltimoMovimientoRecibo(turnoDeCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO#obtenerRecibosNoAnuladosNoFormaPagoNinguna(int, com.servinte.axioma.orm.TurnoDeCaja, boolean)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerRecibosNoAnuladosNoFormaPagoNinguna(int institucion, TurnoDeCaja turnoDeCaja, boolean directoBanco) {
		
		return recibosCajaDaoImp.obtenerRecibosNoAnuladosNoFormaPagoNinguna(institucion, turnoDeCaja, directoBanco);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO#obtenerTotalRecibosNoAnulFormaPagoNinguno(int, com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesRecibosNoAnulFormaPagoNinguno(int institucion, TurnoDeCaja turnoDeCaja) {
		
		return recibosCajaDaoImp.obtenerTotalesRecibosNoAnulFormaPagoNinguno(institucion, turnoDeCaja);
	}


	@Override
	public void generarReciboCaja(RecibosCaja dtoRecibo) {
		recibosCajaDaoImp.attachDirty(dtoRecibo);
		
	}


	@Override
	public RecibosCaja findById(RecibosCajaId id) {
		return recibosCajaDaoImp.findById(id);
	}


	@Override
	public ArrayList<EstadosRecibosCaja> obtenerEstadosRC() {
		return recibosCajaDaoImp.obtenerEstadosRC();
	}


	@Override
	public ArrayList<DtoAnticiposRecibidosConvenio> obtenerCentrosAtencionRecibosCajaConceptoAnticipos(
			DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaDaoImp.obtenerCentrosAtencionRecibosCajaConceptoAnticipos(dto);
	}


	@Override
	public ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> obtenerRecibosCajaConceptoAnticiposConvenioOdont(
			int consCentroAtencion, DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaDaoImp.obtenerRecibosCajaConceptoAnticiposConvenioOdont(consCentroAtencion, dto);
	}


	@Override
	public ArrayList<DtoFormaPagoReport> cargarFormasPago(
			List<RecibosCajaId> numeroRC) {
		return recibosCajaDaoImp.cargarFormasPago(numeroRC);
	}




}
