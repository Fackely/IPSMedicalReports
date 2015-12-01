package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TiposMovimientoCaja;

/**
 * Form que contiene los datos espec&iacute;ficos para realizar  
 * la consulta de los Arqueos y Cieres realizados (Anexo 234).
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * La informaci&oacute;n com&uacute;n a los procesos de arqueo (Arqueo Caja, Arqueo Entrega Parcial
 * y Cierre Turno de Caja) se encuentra contenida en MovimientosCajaForm.
 *
 * @author Jorge Armando Agudelo Quintero
 * @see MovimientosCajaForm
 *
 */
public class ConsultaCierreArqueoForm extends MovimientosCajaForm{
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Fecha inicial utilizada para conformar el rango de fechas en el
	 * cual se va a consultar los Arqueos y Cierres.
	 */
	private Date fechaInicial;
	

	/**
	 * Fecha final utilizada para conformar el rango de fechas en el
	 * cúal se va a consultar los Arqueos y Cierres.
	 */
	private Date fechaFinal;
	
	/**
	 * Listado con los registros encontrados según los parámetros de búsqueda
	 * indicados.
	 */
	private ArrayList<MovimientosCaja> registrosCierreArqueo;
	
	/**
	 * Atributo con el consecutivo del movimiento que se quiere consultar
	 */
	private long consecutivoMovimientoConsultar;
	
	/**
	 * Atributo con el codigo_pk del movimiento que se quiere consultar
	 */
	private long codigoPkMovimientoConsultar;
	
	/**
	 * Atributo con el tipo de forma de pago a imprimir en la presentación del arqueo parcial
	 */
	private int tipoFormaPago;
	
	/**
	 * Atributo que indica si se debe o no mostrar el cuadre de caja
	 * en los movimientos de caja de tipo Arqueo Caja.
	 */
	private boolean imprimeCuadreCaja;
	
	/**
	 * Atributo que indica el tipo de movimiento que se va a imprimir
	 */
	private String movimientoImprimir;
	
	
	/**
	 * Atributo que contiene toda la informaci&oacute;n asociada a un movimiento
	 * de Arqueo Entrega Parcial (anexo 905 - anexo 906)
	 */
	private DtoInformacionEntrega dtoInformacionEntrega;
	
	
	/**
	 * Atributo que contiene el movimiento de Cierre Turno de Caja
	 * consultado
	 */
	private MovimientosCaja cierreTurnoCaja;
	
	/**
	 * Atributo que contiene la información de la consulta realizada de la 
	 * Solicitud de Traslado a Caja de Recaudo realizada en el movimiento
	 * de cierre.
	 */
	private DtoConsultaTrasladosCajasRecaudo dtoConsultaTrasladosCajasRecaudo;
	
	/**
	 * Atributo que valida si existe documentos generados en el arqueo
	 */
	private boolean existenDocumentos;
	
	/*
	 * Constructor del form
	 */
	public ConsultaCierreArqueoForm() {
		
		this.setListadoCajas(new ArrayList<Cajas> ());
		this.setListadoCajeros(new ArrayList<DtoUsuarioPersona>());
		this.setListadoTiposArqueo(new ArrayList<TiposMovimientoCaja>());
		this.setFechaInicial(null);
		this.setFechaFinal(null);
		this.setConsecutivoMovimientoConsultar(ConstantesBD.codigoNuncaValidoLong);
		this.setCodigoPkMovimientoConsultar(ConstantesBD.codigoNuncaValidoLong);
		this.tipoFormaPago=ConstantesBD.codigoNuncaValido;
		this.setImprimeCuadreCaja(false);
		this.setMovimientoImprimir("");
		this.setDtoInformacionEntrega(new DtoInformacionEntrega());
		this.setCierreTurnoCaja(new MovimientosCaja());
		this.setDtoConsultaTrasladosCajasRecaudo(new DtoConsultaTrasladosCajasRecaudo());
		this.setExistenDocumentos(false);
		
	}
	
	/**
	 * Constructor del form. Recibe un objeto de tipo MovimientosCajaForm, del cual herada.
	 * @param forma
	 */
	public ConsultaCierreArqueoForm(MovimientosCajaForm forma) {
		
		this.setListadoCajas(forma.getListadoCajas());
		this.setListadoCajeros(forma.getListadoCajeros());
		this.setListadoTiposArqueo(forma.getListadoTiposArqueo());
		
	}

	
	/**
	 * M&eacute;todo para limpiar los atributos del formulario
	 */
	public void reset() {
		
		super.reset();
		super.resetListados();
		this.setFechaInicial(null);
		this.setFechaFinal(null);
		this.setRegistrosCierreArqueo(new  ArrayList<MovimientosCaja> ());
		this.setConsecutivoMovimientoConsultar(ConstantesBD.codigoNuncaValidoLong);
		this.setCodigoPkMovimientoConsultar(ConstantesBD.codigoNuncaValidoLong);
		this.setImprimeCuadreCaja(false);
		this.setMovimientoImprimir("");
		this.setDtoInformacionEntrega(new DtoInformacionEntrega());
		this.setCierreTurnoCaja(new MovimientosCaja());
		this.setExistenDocumentos(false);
	}
	
	
	/**
	 * M&eacute;todo encargado de procesar los errores presentados
	 * @param forma
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();

		String estado = getEstado();
		
		if (UtilidadTexto.isEmpty(estado)) {
			errores.add("estado invalido", new ActionMessage(
					"errors.estadoInvalido"));
			
		} else if (estado.equals("consultar")) {
			
			MessageResources mensages=MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultaCierreArqueoForm");
			
			long fechaSistema = UtilidadFecha.getFechaActualTipoBD().getTime();
			String mensaje = "";
			
			if (getFechaInicial() == null) {

				errores.add("valor requerido", new ActionMessage(
						"errors.required", "La Fecha Inicial"));
			}else{
				
				if(!(getFechaInicial().getTime() <= fechaSistema)){
					
					mensaje = mensages.getMessage("ConsultaCierreArqueoForm.informar.menorIgualFechaSistema", "Inicial " + UtilidadFecha.conversionFormatoFechaAAp(getFechaInicial()), 
							 UtilidadFecha.conversionFormatoFechaAAp(new Date(fechaSistema)));
				
					errores.add("Mayor Igual Fecha Sistema", new ActionMessage("errors.notEspecific", mensaje));
				}
			}
			
			if (getFechaFinal()== null) {

				errores.add("valor requerido", new ActionMessage(
						"errors.required", "La Fecha Final"));
			}else{
				
				if(!(getFechaFinal().getTime() <= fechaSistema)){
					
					mensaje = mensages.getMessage("ConsultaCierreArqueoForm.informar.menorIgualFechaSistema", "final " + UtilidadFecha.conversionFormatoFechaAAp(getFechaFinal()), 
							 UtilidadFecha.conversionFormatoFechaAAp(new Date(fechaSistema)));
				
					errores.add("Mayor Igual Fecha Sistema", new ActionMessage("errors.notEspecific", mensaje));
				}
			}
			
			if(getFechaInicial()!=null && getFechaFinal()!=null){
				
				if(!(getFechaFinal().getTime() >= getFechaInicial().getTime())){
					
					mensaje = mensages.getMessage("ConsultaCierreArqueoForm.informar.mayorIgualFechaInicial", "final " + UtilidadFecha.conversionFormatoFechaAAp(getFechaFinal()), 
							 UtilidadFecha.conversionFormatoFechaAAp(getFechaInicial()));
				
					errores.add("Mayor Igual Fecha Sistema", new ActionMessage("errors.notEspecific", mensaje));
				}
			}
		}
		
		if(!errores.isEmpty()){
			
			setEstado("");
			
		}
		
		return errores;
	}
	

	/**
	 * @return the fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @param registrosCierreArqueo the registrosCierreArqueo to set
	 */
	public void setRegistrosCierreArqueo(ArrayList<MovimientosCaja> registrosCierreArqueo) {
		this.registrosCierreArqueo = registrosCierreArqueo;
	}

	/**
	 * @return the registrosCierreArqueo
	 */
	public ArrayList<MovimientosCaja> getRegistrosCierreArqueo() {
		return registrosCierreArqueo;
	}

	/**
	 * @param consecutivoMovimientoConsultar the consecutivoMovimientoConsultar to set
	 */
	public void setConsecutivoMovimientoConsultar(
			long consecutivoMovimientoConsultar) {
		this.consecutivoMovimientoConsultar = consecutivoMovimientoConsultar;
	}

	/**
	 * @return the consecutivoMovimientoConsultar
	 */
	public long getConsecutivoMovimientoConsultar() {
		return consecutivoMovimientoConsultar;
	}

	/**
	 * @param imprimeCuadreCaja the imprimeCuadreCaja to set
	 */
	public void setImprimeCuadreCaja(boolean imprimeCuadreCaja) {
		this.imprimeCuadreCaja = imprimeCuadreCaja;
	}

	/**
	 * @return the imprimeCuadreCaja
	 */
	public boolean isImprimeCuadreCaja() {
		return imprimeCuadreCaja;
	}

	/**
	 * @param movimientoImprimir the movimientoImprimir to set
	 */
	public void setMovimientoImprimir(String movimientoImprimir) {
		this.movimientoImprimir = movimientoImprimir;
	}

	/**
	 * @return the movimientoImprimir
	 */
	public String getMovimientoImprimir() {
		return movimientoImprimir;
	}

	/**
	 * @param dtoInformacionEntrega the dtoInformacionEntrega to set
	 */
	public void setDtoInformacionEntrega(DtoInformacionEntrega dtoInformacionEntrega) {
		this.dtoInformacionEntrega = dtoInformacionEntrega;
	}

	/**
	 * @return the dtoInformacionEntrega
	 */
	public DtoInformacionEntrega getDtoInformacionEntrega() {
		return dtoInformacionEntrega;
	}

	/**
	 * @param cierreTurnoCaja the cierreTurnoCaja to set
	 */
	public void setCierreTurnoCaja(MovimientosCaja cierreTurnoCaja) {
		this.cierreTurnoCaja = cierreTurnoCaja;
	}

	/**
	 * @return the cierreTurnoCaja
	 */
	public MovimientosCaja getCierreTurnoCaja() {
		return cierreTurnoCaja;
	}

	public void setCodigoPkMovimientoConsultar(long codigoPkMovimientoConsultar) {
		this.codigoPkMovimientoConsultar = codigoPkMovimientoConsultar;
	}

	public long getCodigoPkMovimientoConsultar() {
		return codigoPkMovimientoConsultar;
	}

	public void setDtoConsultaTrasladosCajasRecaudo(
			DtoConsultaTrasladosCajasRecaudo dtoConsultaTrasladosCajasRecaudo) {
		this.dtoConsultaTrasladosCajasRecaudo = dtoConsultaTrasladosCajasRecaudo;
	}

	public DtoConsultaTrasladosCajasRecaudo getDtoConsultaTrasladosCajasRecaudo() {
		return dtoConsultaTrasladosCajasRecaudo;
	}

	/**
	 * @return the tipoFormaPago
	 */
	public int getTipoFormaPago() {
		return tipoFormaPago;
	}

	/**
	 * @param tipoFormaPago the tipoFormaPago to set
	 */
	public void setTipoFormaPago(int tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}

	/**
	 * @param existenDocumentos the existenDocumentos to set
	 */
	public void setExistenDocumentos(boolean existenDocumentos) {
		this.existenDocumentos = existenDocumentos;
	}

	/**
	 * @return the existenDocumentos
	 */
	public boolean isExistenDocumentos() {
		return existenDocumentos;
	}


}