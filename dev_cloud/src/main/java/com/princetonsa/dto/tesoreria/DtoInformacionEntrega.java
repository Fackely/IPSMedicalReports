package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TransportadoraValores;

/**
 * consolidado de la informaci&oacute;n disponible por forma de pago para
 * realizar un proceso de arqueo (Entregas a caja Mayor / Principal, Solicitudes
 * de Traslado, Entregas a Transportadora de valores)
 * 
 * @author Jorge Armando Agudelo Quintero - Cristhian Murillo
 * 
 */
public class DtoInformacionEntrega extends DtoConsolidadoMovimiento implements
		Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Listado con las formas de pago y los detalles de los documentos de
	 * soporte (informaci&oacute;n) asociados a estos
	 */
	private ArrayList<DtoFormaPagoDocSoporte> listadoDtoFormaPagoDocSoportes;

	/**
	 * Caja Mayor / principal a la cual se le va a asociar la entrega
	 */
	private Cajas cajaMayorPrincipal;

	/**
	 * Observaciones de la Entrega a Caja Mayor / Principal
	 */
	private String observaciones;

	/**
	 * Observaciones para el movimiento de Aceptacion y para el movimiento
	 * Cierre Turno
	 */
	private String observacionesAceptacion;

	/**
	 * Transportadora de valores donde se va a registrar parte de la
	 * informaci&oacute;n correspondiente a la entrega.
	 */
	private TransportadoraValores transportadoraValores;

	/**
	 * Testigo de la aceptacion
	 */
	private String loginTestigo;

	/**
	 * Entidad Financiera con la informaci&oacute;n necesaria para registrar la
	 * entrega a Transportadora de Valores
	 */
	private DtoEntidadesFinancieras entidadFinanciera;

	/**
	 * Llave primaria del movimiento que esta relacionado con esta informacion
	 */
	private long idMovimientoCaja;

	/**
	 * Determina en algunos de los detalles de de la entrega existe una
	 * diferencia (Faltante / Sobrante)
	 */
	private boolean existeDiferencia;

	/**
	 * 
	 * Atributo que contiene la informaci&oacute;n del responsable de:
	 * -El Proceso de Cierre de Turno de Caja
	 * -Testigo de la aceptaci&oacute;n.
	 */
	private DtoUsuarioPersona responsable;

	/**
	 * Caja de recaudo para las solicitudes de traslado
	 */
	private Cajas cajaRecaudo;

	/**
	 * Determina si para el movimiento de Cierre de Turno de Caja se va a
	 * registrar un movimiento de Solicitud de Traslado a Caja
	 */
	private boolean registroSolicitudTrasladoCaja;

	/**
	 * institucion en la que se esta haciendo el proceso
	 */
	private int institucionActual;

	/**
	 * Valor total Entregado a la Transportadora de Valores
	 */
	private double totalEntregadoTransportadora;

	/**
	 * Atributo que almacena el valor total del traslado por cada una de las
	 * formas de pago asociadas.
	 */
	private BigDecimal valorEntregado;

	/**
	 * Atributo que almacena el valor total trasladado por la forma de pago en
	 * efectivo.
	 */
	private BigDecimal valorEntregadoEfectivo;

	/**
	 * Atributo que almacena el valor total trasladado por la forma de pago
	 * Cheque.
	 */
	private BigDecimal valorEntregadoCheque;

	/**
	 * Atributo que almacena el valor total trasladado por la forma de pago
	 * Tarjeta de Cr&eacute;dito.
	 */
	private BigDecimal valorEntregadoTarjeta;

	/**
	 * Atributo que contiene el nombre completo de la transportadora
	 * (Ordenamiento)
	 */
	private String nombreTransportadora;
	
	/**
	 * Atributo que contiene el nombre completo de la caja que realiza el traslado
	 * (Ordenamiento)
	 */
	private String nombreCajaTraslado;
	
	/**
	 * Atributo que contiene la fecha y la hora en la que se realizó el traslado
	 * (Ordenamiento)
	 */
	private String fechaHora;
	
	/**
	 * Almacena el consecutivo del movimiento de caja asociado.
	 */
	private long consecutivoMovimientoCaja;
	
	
	/**
	 * Usuario que genera cierre
	 */
	private String nombreUsuarioGeneraCierre;
	
	
	/**
	 *Movimiento de caja asignado  
	 */
	private MovimientosCaja movimientoCajaAsignado;
	/**
	 * Inc 2067
	 * Atributo para contener el valor de la base
	 * @author DiaRuiPe
	 */
	private double valorBase;
	
	/**
	 * Constuctor de la clase
	 */
	public DtoInformacionEntrega() {

		setListadoDtoFormaPagoDocSoportes(new ArrayList<DtoFormaPagoDocSoporte>());
		this.cajaMayorPrincipal = null;
		this.transportadoraValores = null;
		this.observaciones = "";
		this.observacionesAceptacion = "";
		this.loginTestigo = "";
		this.idMovimientoCaja = ConstantesBD.codigoNuncaValidoLong;
		this.existeDiferencia = false;
		this.responsable = new DtoUsuarioPersona();
		this.cajaRecaudo = null;
		this.setRegistroSolicitudTrasladoCaja(false);
		this.institucionActual = ConstantesBD.codigoNuncaValido;
		this.setTotalEntregadoTransportadora(0);
		this.setNombreCajaTraslado("");
		this.setNombreTransportadora("");
		this.setFechaHora("");
		this.consecutivoMovimientoCaja = ConstantesBD.codigoNuncaValidoLong;
		this.valorBase = ConstantesBD.codigoNuncaValidoDouble;
		this.nombreUsuarioGeneraCierre="";
		this.movimientoCajaAsignado= new MovimientosCaja();
	}

	
	
	/**
	 * Constructor de la clase. Recibiendo un {@link DtoConsolidadoMovimiento} 
	 * para inicializar los listados con la informaci&oacute;n respectiva.
	 */
	public DtoInformacionEntrega(DtoConsolidadoMovimiento consolidadoMovimientoDTO) {
	
		this.setReciboCajaDTOs(consolidadoMovimientoDTO.getReciboCajaDTOs());
		
		this.setDevolucionReciboCajaDTOs(consolidadoMovimientoDTO.getDevolucionReciboCajaDTOs());
		
		this.setTrasladoCajaDTOs(consolidadoMovimientoDTO.getTrasladoCajaDTOs());
		
		this.setTrasladoCajaRecaudoEnCierre(consolidadoMovimientoDTO.getTrasladoCajaRecaudoEnCierre());
		
		this.setTrasladoCajaMayorEnCierre(consolidadoMovimientoDTO.getTrasladoCajaMayorEnCierre());
		
		this.setEntregaCajaMayorPrincipalDTOs(consolidadoMovimientoDTO.getEntregaCajaMayorPrincipalDTOs());
		
		this.setEntregaTransportadoraValoresDTOs(consolidadoMovimientoDTO.getEntregaTransportadoraValoresDTOs());
		
		this.setTotalesDocumentoDTOs(consolidadoMovimientoDTO.getTotalesDocumentoDTOs());
	
		this.setMovimientosCaja(consolidadoMovimientoDTO.getMovimientosCaja());
		
		this.setNumeroTotalDocumentos(consolidadoMovimientoDTO.getNumeroTotalDocumentos());
		
		this.setCuadreCajaDTOs(consolidadoMovimientoDTO.getCuadreCajaDTOs());
		
		this.setTotalesParcialesTrasladosDTOs(consolidadoMovimientoDTO.getTotalesParcialesTrasladosDTOs());
		this.setTotalesParcialesEntrTransDTOs(consolidadoMovimientoDTO.getTotalesParcialesEntrTransDTOs());
		this.setTotalesParcialesEntrCajaDTOs(consolidadoMovimientoDTO.getTotalesParcialesEntrCajaDTOs());
		
		this.setTotalRecibosCaja(consolidadoMovimientoDTO.getTotalRecibosCaja());
		this.setTotalDevolRecibosCaja(consolidadoMovimientoDTO.getTotalDevolRecibosCaja());
		this.setTotalTrasladosCajaRecibido(consolidadoMovimientoDTO.getTotalTrasladosCajaRecibido());
		this.setTotalTrasladosCajaTrasladado(consolidadoMovimientoDTO.getTotalTrasladosCajaTrasladado());
		this.setTotalTrasladosCajaFaltante(consolidadoMovimientoDTO.getTotalTrasladosCajaFaltante());
		this.setTotalEntregaTransportadoraCajaSistema(consolidadoMovimientoDTO.getTotalEntregaTransportadoraCajaSistema());
		this.setTotalEntregaTransportadoraCajaEntregado(consolidadoMovimientoDTO.getTotalEntregaTransportadoraCajaEntregado());
		this.setTotalEntregaTransportadoraCajaFaltante(consolidadoMovimientoDTO.getTotalEntregaTransportadoraCajaFaltante());
		this.setTotalEntregaCajaMayor(consolidadoMovimientoDTO.getTotalEntregaCajaMayor());
		
		this.setTotalTrasladoCajaRecaudoEnCierre(consolidadoMovimientoDTO.getTotalTrasladoCajaRecaudoEnCierre());
		this.setTotalTrasladoCajaMayorEnCierre(consolidadoMovimientoDTO.getTotalTrasladoCajaMayorEnCierre());
		this.setValorBase(consolidadoMovimientoDTO.getValorBase());
	}
	
	
	/**
	 * @return the listadoDtoFormaPagoDocSoportes
	 */
	public ArrayList<DtoFormaPagoDocSoporte> getListadoDtoFormaPagoDocSoportes() {
		return listadoDtoFormaPagoDocSoportes;
	}

	/**
	 * @param listadoDtoFormaPagoDocSoportes
	 *            the listadoDtoFormaPagoDocSoportes to set
	 */
	public void setListadoDtoFormaPagoDocSoportes(
			ArrayList<DtoFormaPagoDocSoporte> listadoDtoFormaPagoDocSoportes) {
		this.listadoDtoFormaPagoDocSoportes = listadoDtoFormaPagoDocSoportes;
	}

	/**
	 * @return the cajaMayorPrincipal
	 */
	public Cajas getCajaMayorPrincipal() {
		return cajaMayorPrincipal;
	}

	/**
	 * @param cajaMayorPrincipal
	 *            the cajaMayorPrincipal to set
	 */
	public void setCajaMayorPrincipal(Cajas cajaMayorPrincipal) {
		this.cajaMayorPrincipal = cajaMayorPrincipal;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones
	 *            the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the observacionesAceptacion
	 */
	public String getObservacionesAceptacion() {
		return observacionesAceptacion;
	}

	/**
	 * @param observacionesAceptacion
	 *            the observacionesAceptacion to set
	 */
	public void setObservacionesAceptacion(String observacionesAceptacion) {
		this.observacionesAceptacion = observacionesAceptacion;
	}

	/**
	 * @return the transportadoraValores
	 */
	public TransportadoraValores getTransportadoraValores() {
		return transportadoraValores;
	}

	/**
	 * @param transportadoraValores
	 *            the transportadoraValores to set
	 */
	public void setTransportadoraValores(
			TransportadoraValores transportadoraValores) {
		this.transportadoraValores = transportadoraValores;
	}

	/**
	 * @return the loginTestigo
	 */
	public String getLoginTestigo() {
		return loginTestigo;
	}

	/**
	 * @param loginTestigo
	 *            the loginTestigo to set
	 */
	public void setLoginTestigo(String loginTestigo) {
		this.loginTestigo = loginTestigo;
	}

	/**
	 * @return the entidadFinanciera
	 */
	public DtoEntidadesFinancieras getEntidadFinanciera() {
		return entidadFinanciera;
	}

	/**
	 * @param entidadFinanciera
	 *            the entidadFinanciera to set
	 */
	public void setEntidadFinanciera(DtoEntidadesFinancieras entidadFinanciera) {
		this.entidadFinanciera = entidadFinanciera;
	}

	/**
	 * @return the idMovimientoCaja
	 */
	public long getIdMovimientoCaja() {
		return idMovimientoCaja;
	}

	/**
	 * @param idMovimientoCaja
	 *            the idMovimientoCaja to set
	 */
	public void setIdMovimientoCaja(long idMovimientoCaja) {
		this.idMovimientoCaja = idMovimientoCaja;
	}

	/**
	 * @return the existeDiferencia
	 */
	public boolean isExisteDiferencia() {
		return existeDiferencia;
	}

	/**
	 * @param existeDiferencia
	 *            the existeDiferencia to set
	 */
	public void setExisteDiferencia(boolean existeDiferencia) {
		this.existeDiferencia = existeDiferencia;
	}

	/**
	 * @return the cajaRecaudo
	 */
	public Cajas getCajaRecaudo() {
		return cajaRecaudo;
	}

	/**
	 * @param cajaRecaudo
	 *            the cajaRecaudo to set
	 */
	public void setCajaRecaudo(Cajas cajaRecaudo) {
		this.cajaRecaudo = cajaRecaudo;
	}

	/**
	 * @return the registroSolicitudTrasladoCaja
	 */
	public boolean isRegistroSolicitudTrasladoCaja() {
		return registroSolicitudTrasladoCaja;
	}

	/**
	 * @param registroSolicitudTrasladoCaja
	 *            the registroSolicitudTrasladoCaja to set
	 */
	public void setRegistroSolicitudTrasladoCaja(
			boolean registroSolicitudTrasladoCaja) {
		this.registroSolicitudTrasladoCaja = registroSolicitudTrasladoCaja;
	}

	/**
	 * @return the institucionActual
	 */
	public int getInstitucionActual() {
		return institucionActual;
	}

	/**
	 * @param institucionActual
	 *            the institucionActual to set
	 */
	public void setInstitucionActual(int institucionActual) {
		this.institucionActual = institucionActual;
	}

	/**
	 * @return the totalEntregadoTransportadora
	 */
	public double getTotalEntregadoTransportadora() {
		return totalEntregadoTransportadora;
	}

	/**
	 * @param totalEntregadoTransportadora
	 *            the totalEntregadoTransportadora to set
	 */
	public void setTotalEntregadoTransportadora(
			double totalEntregadoTransportadora) {
		this.totalEntregadoTransportadora = totalEntregadoTransportadora;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del atributo
	 * valorEntregado
	 * 
	 * @return Retorna la variable valorEntregado
	 */
	public BigDecimal getValorEntregado() {
		return valorEntregado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del atributo
	 * valorEntregadoEfectivo
	 * 
	 * @return Retorna la variable valorEntregadoEfectivo
	 */
	public BigDecimal getValorEntregadoEfectivo() {
		return valorEntregadoEfectivo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del atributo
	 * valorEntregadoCheque
	 * 
	 * @return Retorna la variable valorEntregadoCheque
	 */
	public BigDecimal getValorEntregadoCheque() {
		return valorEntregadoCheque;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del atributo
	 * valorEntregadoTarjeta
	 * 
	 * @return Retorna la variable valorEntregadoTarjeta
	 */
	public BigDecimal getValorEntregadoTarjeta() {
		return valorEntregadoTarjeta;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor del atributo
	 * valorEntregado
	 * 
	 * @param valor
	 *            para el atributo valorEntregado
	 */
	public void setValorEntregado(BigDecimal valorEntregado) {
		this.valorEntregado = valorEntregado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor del atributo
	 * valorEntregadoEfectivo
	 * 
	 * @param valor
	 *            para el atributo valorEntregadoEfectivo
	 */
	public void setValorEntregadoEfectivo(BigDecimal valorEntregadoEfectivo) {
		this.valorEntregadoEfectivo = valorEntregadoEfectivo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor del atributo
	 * valorEntregadoCheque
	 * 
	 * @param valor
	 *            para el atributo valorEntregadoCheque
	 */
	public void setValorEntregadoCheque(BigDecimal valorEntregadoCheque) {
		this.valorEntregadoCheque = valorEntregadoCheque;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor del atributo
	 * valorEntregadoTarjeta
	 * 
	 * @param valor
	 *            para el atributo valorEntregadoTarjeta
	 */
	public void setValorEntregadoTarjeta(BigDecimal valorEntregadoTarjeta) {
		this.valorEntregadoTarjeta = valorEntregadoTarjeta;
	}


	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(DtoUsuarioPersona responsable) {
		this.responsable = responsable;
	}


	/**
	 * @return the responsable
	 */
	public DtoUsuarioPersona getResponsable() {
		return responsable;
	}


	/**
	 * @param nombreTransportadora the nombreTransportadora to set
	 */
	public void setNombreTransportadora(String nombreTransportadora) {
		this.nombreTransportadora = nombreTransportadora;
	}


	/**
	 * @return the nombreTransportadora
	 */
	public String getNombreTransportadora() {
		
		TransportadoraValores transportadora = getTransportadoraValores();
		
		if(transportadora!=null && transportadora.getTerceros()!=null){
			
			nombreTransportadora = transportadora.getTerceros().getDescripcion();
			
		}else{
			
			nombreTransportadora  = "";
		}
		
		return nombreTransportadora;
	}


	/**
	 * @param nombreCajaTraslado the nombreCajaTraslado to set
	 */
	public void setNombreCajaTraslado(String nombreCajaTraslado) {
		this.nombreCajaTraslado = nombreCajaTraslado;
	}

	
	/**
	 * @return the nombreCajaTraslado
	 */
	public String getNombreCajaTraslado() {
		
		Cajas caja = getMovimientosCaja().getTurnoDeCaja().getCajas();
		
		if(caja!=null){
			
			nombreCajaTraslado = "("+caja.getCodigo()+") " + caja.getDescripcion();
			
		}else{
			
			nombreCajaTraslado ="";
		}
		
		return nombreCajaTraslado;
	}

	/**
	 * @param fechaHora the fechaHora to set
	 */
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}


	/**
	 * @return the fechaHora
	 */
	public String getFechaHora() {
		
		fechaHora = UtilidadFecha.conversionFormatoFechaAAp(getMovimientosCaja().getFecha());
		
		return fechaHora;
	}



	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  consecutivoMovimientoCaja
	 *
	 * @return retorna la variable consecutivoMovimientoCaja
	 */
	public long getConsecutivoMovimientoCaja() {
		return consecutivoMovimientoCaja;
	}



	/**
	 * Método que se encarga de establecer el valor
	 * del atributo consecutivoMovimientoCaja
	 * @param consecutivoMovimientoCaja es el valor para el atributo consecutivoMovimientoCaja 
	 */
	public void setConsecutivoMovimientoCaja(long consecutivoMovimientoCaja) {
		this.consecutivoMovimientoCaja = consecutivoMovimientoCaja;
	}
	
	
	public void setValorBase(double valorBase) {
		this.valorBase = valorBase;
	}


	public double getValorBase() {
		return valorBase;
	}




	/**
	 * @return the nombreUsuarioGeneraCierre
	 */
	public String getNombreUsuarioGeneraCierre() {
		return nombreUsuarioGeneraCierre;
	}



	/**
	 * @param nombreUsuarioGeneraCierre the nombreUsuarioGeneraCierre to set
	 */
	public void setNombreUsuarioGeneraCierre(String nombreUsuarioGeneraCierre) {
		this.nombreUsuarioGeneraCierre = nombreUsuarioGeneraCierre;
	}



	/**
	 * @return the movimientoCajaAsignado
	 */
	public MovimientosCaja getMovimientoCajaAsignado() {
		return movimientoCajaAsignado;
	}



	/**
	 * @param movimientoCajaAsignado the movimientoCajaAsignado to set
	 */
	public void setMovimientoCajaAsignado(MovimientosCaja movimientoCajaAsignado) {
		this.movimientoCajaAsignado = movimientoCajaAsignado;
	}
	
	



	
}
