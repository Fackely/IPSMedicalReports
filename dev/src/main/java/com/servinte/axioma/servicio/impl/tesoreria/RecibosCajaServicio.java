package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoFormaPagoReport;
import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.princetonsa.dto.tesoreria.DtoRecibosConceptoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link IRecibosCajaServicio}
 *  
 * @author Jorge Armando Agudelo Quintero
 * @see IRecibosCajaServicio
 */
public class RecibosCajaServicio implements IRecibosCajaServicio {

	
	private IRecibosCajaMundo recibosCajaMundo;
	
	public RecibosCajaServicio() {
		recibosCajaMundo =  TesoreriaFabricaMundo.crearRecibosCajaMundo();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#obtenerRecibosXMovimientoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCaja(MovimientosCaja movimientosCaja)  {

		return recibosCajaMundo.obtenerRecibosXMovimientoCaja(movimientosCaja);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#actualizarRecibosAsociadosCierre(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<RecibosCaja> actualizarRecibosAsociadosCierre(MovimientosCaja movimientosCaja) {
		
		return recibosCajaMundo.actualizarRecibosAsociadosCierre(movimientosCaja);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#obtenerFechaUltimoMovimientoRecibo(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoRecibo(TurnoDeCaja turnoDeCaja) {
		
		return recibosCajaMundo.obtenerFechaUltimoMovimientoRecibo(turnoDeCaja);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#obtenerRecibosNoAnuladosNoFormaPagoNinguna(int, com.servinte.axioma.orm.TurnoDeCaja, boolean)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerRecibosNoAnuladosNoFormaPagoNinguna(	int institucion, TurnoDeCaja turnoDeCaja, boolean directoBanco) {
		
		return recibosCajaMundo.obtenerRecibosNoAnuladosNoFormaPagoNinguna(institucion, turnoDeCaja, directoBanco);
	}

	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#obtenerTotalesRecibosNoAnulFormaPagoNinguno(int, com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesRecibosNoAnulFormaPagoNinguno(int institucion, TurnoDeCaja turnoDeCaja) {
		
		return recibosCajaMundo.obtenerTotalesRecibosNoAnulFormaPagoNinguno(institucion, turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#generarReciboCaja(com.servinte.axioma.orm.RecibosCaja)
	 */
	@Override
	public void generarReciboCaja(RecibosCaja dtoRecibo) {
		
		recibosCajaMundo.generarReciboCaja(dtoRecibo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#findById(com.servinte.axioma.orm.RecibosCajaId)
	 */
	@Override
	public RecibosCaja findById(RecibosCajaId id) {
		
		return recibosCajaMundo.findById(id);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio#imprimirReciboCaja(com.princetonsa.mundo.UsuarioBasico, com.princetonsa.mundo.InstitucionBasica, java.util.HashMap)
	 */
	@Override
	public String imprimirReciboCaja(UsuarioBasico usuario,
			InstitucionBasica institucion, HashMap<String, String> parametros) {
		
		return recibosCajaMundo.imprimirReciboCaja(usuario, institucion, parametros);
	}


	@Override
	public ArrayList<EstadosRecibosCaja> obtenerEstadosRC() {
		return recibosCajaMundo.obtenerEstadosRC();
	}


	@Override
	public ArrayList<DtoAnticiposRecibidosConvenio> obtenerCentrosAtencionRecibosCajaConceptoAnticipos(
			DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaMundo.obtenerCentrosAtencionRecibosCajaConceptoAnticipos(dto);
	}


	@Override
	public ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> obtenerRecibosCajaConceptoAnticiposConvenioOdont(
			int consCentroAtencion, DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaMundo.obtenerRecibosCajaConceptoAnticiposConvenioOdont(consCentroAtencion, dto);
	}


	@Override
	public ArrayList<DtoFormaPagoReport> cargarFormasPago(
			List<RecibosCajaId> numeroRC) {
		return recibosCajaMundo.cargarFormasPago(numeroRC);
	}


	@Override
	public ArrayList<DtoAnticiposRecibidosConvenio> consolidarConsultaAnticiposRecibidosConvenio(
			DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaMundo.consolidarConsultaAnticiposRecibidosConvenio(dto);
	}



}
