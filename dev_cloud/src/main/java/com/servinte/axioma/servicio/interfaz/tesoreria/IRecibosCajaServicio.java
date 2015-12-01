package com.servinte.axioma.servicio.interfaz.tesoreria;

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
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con los Recibos de Caja
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.RecibosCajaMundo
 */

public interface IRecibosCajaServicio {

	/**
	 * Recibos de caja filtrados solo por el turno de caja, cuando el movimiento de caja no ha sido registrado.
	 * Se involucra la fecha y la hora en la b&uacute;squeda, cuando el movimiento de caja est&aacute; registrado
	 * en el sistema.
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCaja(MovimientosCaja movimientosCaja);
	

	/**
	 * Retorna un listado con los detalles de los recibos de caja con formas de pago diferente a tipo "NINGUNO"
	 * asociados a un Turno de Caja y que no se han relacionado a una Anulaci&oacute;n. 
	 * 
	 * El par&aacute;metro directoBanco aplica solo para los recibos con detalles de pagos que involucren Tarjetas
	 * Financieras, con el se obtienen, dependiendo del valor (true), las tarjetas que tienen pago directo con una entidad
	 * financiera
	 * 
	 * @param institucion
	 * @param turnoDeCaja
	 * @param directoBanco
	 * @return Listado con los detalles de los recibos de caja de un Turno de Caja que no se han relacionado a una
	 * una Anulaci&oacute;n
	 */
	public List<DtoDetalleDocSopor> obtenerRecibosNoAnuladosNoFormaPagoNinguna (int institucion, TurnoDeCaja turnoDeCaja, boolean directoBanco);

	
	/**
	 * Actualiza los recibos de caja asociados a un movimiento de cierre espec&iacute;fico
	 * 
	 * @param movimientosCaja
	 * @return boolean indicando si se realiz&oacute; el proceso de actualizaci&oacute;n
	 */
	public List<RecibosCaja> actualizarRecibosAsociadosCierre(MovimientosCaja movimientosCaja);
	
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para un recibo
	 * de caja que est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoRecibo(TurnoDeCaja turnoDeCaja);
	
	/**
	 * M&eacute;todo que se encarga de totalizar los recibos de caja recaudados que no fueron
	 * anulados con detalles de formas de pago de tipo "NINGUNO"
	 * 
	 * @param institucion
	 * @param turnoDeCaja
	 * @return
	 */
	public List<DtoDetalleDocSopor> obtenerTotalesRecibosNoAnulFormaPagoNinguno(int institucion, TurnoDeCaja turnoDeCaja);

	/**
	 * Generar el recibo de caja
	 * @param dtoRecibo
	 */
	public void generarReciboCaja(RecibosCaja dtoRecibo);

	/**
	 * Buscar por id
	 * @param id
	 * @return
	 */
	public RecibosCaja findById(RecibosCajaId id);
	
	
	/**
	 * Método que se encarga de realizar la impresión del recibo de caja
	 * 
	 * @param usuario
	 * @param institucion
	 * @param parametros
	 * @return
	 */
	public String imprimirReciboCaja (UsuarioBasico usuario, InstitucionBasica  institucion, HashMap<String, String> parametros);
	
	/**
	 * M&eacute;todo que se encarga de listar
	 * los estados de los recibos de caja
	 * @return ArrayList<EstadosRecibosCaja>
	 * @author Diana Carolina G
	 */
	public ArrayList<EstadosRecibosCaja> obtenerEstadosRC();
	
	/**
	 * M&eacute;todo encargado de consultar
	 * el detalle de los recibos de caja por concepto 
	 * 'Anticipos Convenios Odontol&oacute;gicos', asociados
	 * un cada Centro de Atenci&oacute;n
	 * @param consCentroAtencion
	 * @param dto
	 * @return ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> obtenerRecibosCajaConceptoAnticiposConvenioOdont(int consCentroAtencion, DtoReporteAnticiposRecibidosConvenio dto);
	
	/**
	 * M&eacute;todo encargado de consultar
	 * los centros de atención a los cuales se encuentran asociados
	 * recibos de caja por tipo de concepto 
	 * 'Anticipos Convenios Odontol&oacute;gicos'
	 * @param dto
	 * @return ArrayList<DtoAnticiposRecibidosConvenio>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoAnticiposRecibidosConvenio> obtenerCentrosAtencionRecibosCajaConceptoAnticipos(DtoReporteAnticiposRecibidosConvenio dto);
	
	/**
	 * M&eacute;todo encargado de consultar el 
	 * detalle de las formas de pago con sus correspondientes
	 * valores por cada recibo de caja. 
	 * 
	 * @param numeroRC
	 * @return ArrayList<DtoFormaPagoReport>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoFormaPagoReport> cargarFormasPago(List<RecibosCajaId> numeroRC);
	
	
	/**
	 * M&eacute;todo encargado de consolidar la consulta
	 * de Anticipos recibidos del convenio
	 * 
	 * @param DtoReporteAnticiposRecibidosConvenio
	 * @return ArrayList<DtoAnticiposRecibidosConvenio>
	 * @author Diana Carolina G
	 */
	
	public ArrayList<DtoAnticiposRecibidosConvenio> consolidarConsultaAnticiposRecibidosConvenio (
			DtoReporteAnticiposRecibidosConvenio dto);
	
	
	
}
