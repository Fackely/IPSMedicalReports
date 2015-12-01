/**
 * 
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jorge ARmando Osorio Velasquez
 *
 */ 
public class RegistroDevolucionRecibosCajaForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String estado;
	
	/*--------ATRIBUTOS USADOS EN LA BUSQUEDA DE RECIBOS DE CAJA-------*/
	/**
	 * 
	 */
	private String numeroReciboCajaBusqueda;
	
	/**
	 * 
	 */
	private String fechaInicialBusqueda;
	
	/**
	 * 
	 */
	private String fechaFinalBusqueda;
	
	/**
	 * 
	 */
	private String tipoIDBeneficiarioBusqueda;
	
	/**
	 * 
	 */
	private String numeroIDBeneficiarioBusqueda;
	
	/**
	 * 
	 */
	private String conceptoBusqueda;
	
	/**
	 * 
	 */
	private String estadoReciboCajaBusqueda;
	
	/**
	 * 
	 */
	private String centroAtencionBusqueda;

	/**
	 * 
	 */
	private HashMap<String, Object> recibosCaja;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private boolean mostrarSeccionBusquedaAvanzada;
	
	
	/**
	 * 
	 */
	private int indiceReciboCaja;
	
	/*--------FIN ATRIBUTOS USADOS EN LA BUSQUEDA DE RECIBOS DE CAJA-------*/
	
	/**
	 * 
	 */
	private String reciboCaja;
	
	/**
	 * 
	 */
	private String consecutivoReciboCaja;
	
	/**
	 * 
	 */
	private String fechaHoraElaboracion;
	
	/**
	 * 
	 */
	private String nombreEstadoReciboCaja;
	
	/**
	 * 
	 */
	private String beneficiario;
	
	/**
	 * 
	 */
	private String concepto;
	
	/**
	 * 
	 */
	private String tipoConcepto;
	
	/**
	 * 
	 */
	private String valor;
	
	/**
	 * 
	 */
	private String motivoDevolucion;
	
	/**
	 * 
	 */
	private String valorDevolucion;
	
	/**
	 * 
	 */	
	private String formaPago;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private String documentoSoporte;
	
	/**
	 * 
	 */
	private String tipoIDBeneficiario;
	
	/**
	 * 
	 */
	private String numeroIDBeneficiario;
	/**
	 * 
	 */
	private String consecutivoDevolucion;
	
	/**
	 * 
	 */
	private boolean aprobarDevolucion;
	
	/**
	 * 
	 */
	private String paginaRetornaImpresion;
	
	/**
	 * valor del concepto de ingreos de Tesoreria
	 */
	private String valorConceptoIngTes;
	
	/**
	 * Ingreso en el cual se generó el recibo de caja
	 */
	private Integer ingreso;
	
	private double  valorTotalReciboCaja;
	
	private double  valorAnticRecConv;
	
	private int numContratoOdontologia;
	 
	private boolean validacionAnticiposConveOdontologia;
	
	private boolean existeReciboCajaXConceptAnticipConvenioOdonto;
	
	/**
	 * 
	 */
	private ResultadoBoolean mostrarMensaje=new ResultadoBoolean(false,"");
	
	
	
	/**
	 * 
	 */
	
	
	
	
	/**
	 * atributo que determian si se puede eliminar el recibo de caja
	 */
	private String eliminarRC;
	
	private String siguientePagina;
	
	private ArrayList<DtoFormaPago> listaFormasPago=new ArrayList<DtoFormaPago>();
	
	
	 /**Inc 2339
	  * Diana Ruiz  
	  * Almacena los datos de los pacientes    */
	
	 private HashMap mapaPacientes;
	
	public void reset()
	{
		this.numeroReciboCajaBusqueda="";
		this.fechaInicialBusqueda="";
		this.fechaFinalBusqueda="";
		this.tipoIDBeneficiarioBusqueda="";
		this.numeroIDBeneficiarioBusqueda="";
		this.conceptoBusqueda="";
		this.estadoReciboCajaBusqueda="";
		this.centroAtencionBusqueda="";
		this.mostrarSeccionBusquedaAvanzada=false;
		this.recibosCaja=new HashMap<String, Object>();
		this.recibosCaja.put("numRegistros", "0");
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.indiceReciboCaja=ConstantesBD.codigoNuncaValido;
		
		this.reciboCaja="";
		this.consecutivoReciboCaja="";
		this.fechaHoraElaboracion="";
		this.nombreEstadoReciboCaja="";
		this.beneficiario="";
		this.concepto="";
		this.valor="";
		this.documentoSoporte="";
		
		this.motivoDevolucion="";
		this.valorDevolucion="";
		this.formaPago="";
		this.observaciones="";
		//this.consecutivoDevolucion="";
		
		this.tipoConcepto="";
		this.tipoIDBeneficiario="";
		this.numeroIDBeneficiario="";
		
		this.aprobarDevolucion=false;
		
		this.valorConceptoIngTes = "";
		this.eliminarRC = ConstantesBD.acronimoNo;
		this.valorTotalReciboCaja= ConstantesBD.codigoNuncaValidoDouble;
		this.valorAnticRecConv=ConstantesBD.codigoNuncaValidoDouble;
		this.numContratoOdontologia= ConstantesBD.codigoNuncaValido;
		this.validacionAnticiposConveOdontologia= false;
		this.existeReciboCajaXConceptAnticipConvenioOdonto= false;
		
		this.siguientePagina="";
		
		this.setMapaPacientes(new HashMap ());
		
	}
	
	/**
	 * Método encargado de reset sin los parámetros de búsqueda
	 * Según tarea 2329
	 */
	public void resetSinBusqueda()
	{
		this.reciboCaja="";
		this.consecutivoReciboCaja="";
		this.fechaHoraElaboracion="";
		this.nombreEstadoReciboCaja="";
		this.beneficiario="";
		this.concepto="";
		this.valor="";
		this.documentoSoporte="";
		
		this.motivoDevolucion="";
		this.valorDevolucion="";
		this.formaPago="";
		this.observaciones="";
		//this.consecutivoDevolucion="";
		
		this.tipoConcepto="";
		this.tipoIDBeneficiario="";
		this.numeroIDBeneficiario="";
		
		this.aprobarDevolucion=false;
		
		this.valorConceptoIngTes = "";
		this.eliminarRC = ConstantesBD.acronimoNo;
		this.valorTotalReciboCaja= ConstantesBD.codigoNuncaValidoDouble;
		this.valorAnticRecConv=ConstantesBD.codigoNuncaValidoDouble;
		this.numContratoOdontologia= ConstantesBD.codigoNuncaValido;
		this.validacionAnticiposConveOdontologia= false;
		this.existeReciboCajaXConceptAnticipConvenioOdonto= false;
		
	}
	
	public void resetCamposBusqueda()
	{
		this.numeroReciboCajaBusqueda="";
		this.fechaInicialBusqueda="";
		this.fechaFinalBusqueda="";
		this.tipoIDBeneficiarioBusqueda="";
		this.numeroIDBeneficiarioBusqueda="";
		this.conceptoBusqueda="";
		this.estadoReciboCajaBusqueda="";
		this.centroAtencionBusqueda="";
		this.eliminarRC = ConstantesBD.acronimoNo;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		UsuarioBasico usuario=(UsuarioBasico)Utilidades.getUsuarioBasicoSesion(request.getSession());
		if(estado.equals("ejecutarBusqueda"))
		{
			if(!mostrarSeccionBusquedaAvanzada)
			{
				if(UtilidadTexto.isEmpty(this.numeroReciboCajaBusqueda))
				{
					errores.add("El Numero Recibo Caja", new ActionMessage("errors.required", "El Número Recibo Caja"));
				}
				else
				{
					try
					{
						Double.parseDouble(this.numeroReciboCajaBusqueda);
					}
					catch(Exception e)
					{
						errores.add("El Numero Recibo Caja", new ActionMessage("errors.integer", "El Número Recibo Caja"));
					}
				}
			}
			else
			{
				if(!UtilidadTexto.isEmpty(this.numeroReciboCajaBusqueda))
				{
					try
					{
						Double.parseDouble(this.numeroReciboCajaBusqueda);
					}
					catch(Exception e)
					{
						errores.add("El Numero Recibo Caja", new ActionMessage("errors.integer", "El Número Recibo Caja"));
					}
				}
				//si no tiene numero de recibo de caja hacer las otras validaciones.
				else
				{
					boolean errorFecha=false;
					
					if(UtilidadTexto.isEmpty(fechaInicialBusqueda))
					{
						errores.add("fecha Ini", new ActionMessage("errors.required", "La fecha Inicial"));
						errorFecha=true;
					}
					if(UtilidadTexto.isEmpty(fechaFinalBusqueda))
					{
						errores.add("fecha Fin", new ActionMessage("errors.required", "La fecha final"));
						errorFecha=true;
					}
					if(!errorFecha)
					{
						if(!UtilidadFecha.validarFecha(this.fechaInicialBusqueda))
						{
							errores.add("fecha de Inicial", new ActionMessage("errors.formatoFechaInvalido",this.fechaInicialBusqueda));
							errorFecha=true;
						}
						if(!UtilidadFecha.validarFecha(this.fechaFinalBusqueda))
						{
							errores.add("fecha de Fianl", new ActionMessage("errors.formatoFechaInvalido",this.fechaFinalBusqueda));
							errorFecha=true;
						}
						if(!errorFecha)
						{
							if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialBusqueda)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								errores.add("fecha de Inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Actual"));
							}
							if((UtilidadFecha.conversionFormatoFechaABD(this.fechaFinalBusqueda)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								errores.add("fecha de Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final", "Actual"));
							}
							if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialBusqueda)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFinalBusqueda))>0)
							{
								errores.add("fecha de Inicial-Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Final"));
							}				
						}
					}

					if(UtilidadTexto.isEmpty(this.numeroIDBeneficiarioBusqueda)&&!UtilidadTexto.isEmpty(this.tipoIDBeneficiarioBusqueda))
					{
						errores.add("num id", new ActionMessage("errors.required", "El Numero ID Beneficiario"));
					}
					if(!UtilidadTexto.isEmpty(this.numeroIDBeneficiarioBusqueda)&&UtilidadTexto.isEmpty(this.tipoIDBeneficiarioBusqueda))
					{
						errores.add("tipo id", new ActionMessage("errors.required", "El Tipo ID Beneficiario"));
					}
				}
			}
		}
	
		
		//else if(estado.equals("guardarDevolucion"))
		else  if(estado.equals("generacionAprobacion"))
		{
			//UtilidadBD.
			if(UtilidadTexto.isEmpty(this.motivoDevolucion))
			{
				errores.add("motivoDevolucion", new ActionMessage("errors.required", "El Motivo de la devolución"));
			}
			if(UtilidadTexto.isEmpty(this.valorDevolucion))
			{
				errores.add("valorDevolucion", new ActionMessage("errors.required", "El valor devolución"));
			}
			else
			{
				try
				{
					if(Double.parseDouble(this.valorDevolucion)<=0)
					{
						errores.add("valorDevolucion", new ActionMessage("errors.MayorIgualQue", "El valor devolución","0"));
					}
				}
				catch(Exception e)
				{
					errores.add("valorDevolucion", new ActionMessage("errors.integer", "El valor devolución"));
				}
			}
			
			if(Utilidades.convertirAEntero(tipoConcepto)==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)
			{
				if(Utilidades.convertirADouble(valorDevolucion)>Utilidades.convertirADouble(valor))
				{
					errores.add("valorDevolucion", new ActionMessage("errors.MenorIgualQue", "El valor devolución","Saldo Recibo Caja"));
				}
				else{
					
					boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(Utilidades.getUsuarioBasicoSesion(request.getSession()).getCodigoInstitucionInt()));					
					if(Utilidades.convertirADouble(valorDevolucion)>Utilidades.obtenerAbonosDisponiblesPaciente(Paciente.obtenerCodigoPersona( this.numeroIDBeneficiario, this.tipoIDBeneficiario), this.ingreso, usuario.getCodigoInstitucionInt()) && controlarAbonoPaciente)
					{
						errores.add("valorDevolucion", new ActionMessage("errors.MenorIgualQue", "El valor devolución","Saldo Abonos"));
					}
					/*
					 * Se valida con y sin ingreso (Anexo 893)
					 */
					else if(errores.isEmpty() && Utilidades.convertirADouble(valorDevolucion)>Utilidades.obtenerAbonosDisponiblesPaciente(Paciente.obtenerCodigoPersona( this.numeroIDBeneficiario, this.tipoIDBeneficiario), null, usuario.getCodigoInstitucionInt()))
					{
						errores.add("valorDevolucion", new ActionMessage("errors.MenorIgualQue", "El valor devolución","Saldo Abonos"));
					}
				}
			}
			
			if(observaciones.length()>200)
			{
				errores.add("observaciones", new ActionMessage("errors.maxlength", "Observaciones","200"));
			}
		}
		
		
		return errores;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getCentroAtencionBusqueda() {
		return centroAtencionBusqueda;
	}

	public void setCentroAtencionBusqueda(String centroAtencionBusqueda) {
		this.centroAtencionBusqueda = centroAtencionBusqueda;
	}

	public String getConceptoBusqueda() {
		return conceptoBusqueda;
	}

	public void setConceptoBusqueda(String conceptoBusqueda) {
		this.conceptoBusqueda = conceptoBusqueda;
	}

	public String getEstadoReciboCajaBusqueda() {
		return estadoReciboCajaBusqueda;
	}

	public void setEstadoReciboCajaBusqueda(String estadoReciboCajaBusqueda) {
		this.estadoReciboCajaBusqueda = estadoReciboCajaBusqueda;
	}

	public String getFechaFinalBusqueda() {
		return fechaFinalBusqueda;
	}

	public void setFechaFinalBusqueda(String fechaFinalBusqueda) {
		this.fechaFinalBusqueda = fechaFinalBusqueda;
	}

	public String getFechaInicialBusqueda() {
		return fechaInicialBusqueda;
	}

	public void setFechaInicialBusqueda(String fechaInicialBusqueda) {
		this.fechaInicialBusqueda = fechaInicialBusqueda;
	}

	public boolean isMostrarSeccionBusquedaAvanzada() {
		return mostrarSeccionBusquedaAvanzada;
	}

	public void setMostrarSeccionBusquedaAvanzada(
			boolean mostrarSeccionBusquedaAvanzada) {
		this.mostrarSeccionBusquedaAvanzada = mostrarSeccionBusquedaAvanzada;
	}

	public String getNumeroIDBeneficiarioBusqueda() {
		return numeroIDBeneficiarioBusqueda;
	}

	public void setNumeroIDBeneficiarioBusqueda(String numeroIDBeneficiarioBusqueda) {
		this.numeroIDBeneficiarioBusqueda = numeroIDBeneficiarioBusqueda;
	}

	public String getNumeroReciboCajaBusqueda() {
		return numeroReciboCajaBusqueda;
	}

	public void setNumeroReciboCajaBusqueda(String numeroReciboCajaBusqueda) {
		this.numeroReciboCajaBusqueda = numeroReciboCajaBusqueda;
	}

	public String getTipoIDBeneficiarioBusqueda() {
		return tipoIDBeneficiarioBusqueda;
	}

	public void setTipoIDBeneficiarioBusqueda(String tipoIDBeneficiarioBusqueda) {
		this.tipoIDBeneficiarioBusqueda = tipoIDBeneficiarioBusqueda;
	}

	public HashMap<String, Object> getRecibosCaja() {
		return recibosCaja;
	}

	public void setRecibosCaja(HashMap<String, Object> recibosCaja) {
		this.recibosCaja = recibosCaja;
	}
	
	public Object getRecibosCaja(String key) {
		return recibosCaja.get(key);
	}

	public void setRecibosCaja(String key,Object value) 
	{
		this.recibosCaja.put(key, value);
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public int getIndiceReciboCaja() {
		return indiceReciboCaja;
	}

	public void setIndiceReciboCaja(int indiceReciboCaja) {
		this.indiceReciboCaja = indiceReciboCaja;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getFechaHoraElaboracion() {
		return fechaHoraElaboracion;
	}

	public void setFechaHoraElaboracion(String fechaHoraElaboracion) {
		this.fechaHoraElaboracion = fechaHoraElaboracion;
	}

	public String getNombreEstadoReciboCaja() {
		return nombreEstadoReciboCaja;
	}

	public void setNombreEstadoReciboCaja(String nombreEstadoReciboCaja) {
		this.nombreEstadoReciboCaja = nombreEstadoReciboCaja;
	}

	public String getReciboCaja() {
		return reciboCaja;
	}

	public void setReciboCaja(String reciboCaja) {
		this.reciboCaja = reciboCaja;
	}

	public String getValor() {
		return valor;
	}
	public String getValorFormateado() {
		return UtilidadTexto.formatearValores(valor);
	}
	

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getMotivoDevolucion() {
		return motivoDevolucion;
	}

	public void setMotivoDevolucion(String motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getValorDevolucion() {
		return valorDevolucion;
	}

	public void setValorDevolucion(String valorDevolucion) {
		this.valorDevolucion = valorDevolucion;
	}

	public String getDocumentoSoporte() {
		return documentoSoporte;
	}

	public void setDocumentoSoporte(String documentoSoporte) {
		this.documentoSoporte = documentoSoporte;
	}

	public String getConsecutivoDevolucion() {
		return consecutivoDevolucion;
	}

	public void setConsecutivoDevolucion(String consecutivoDevolucion) {
		this.consecutivoDevolucion = consecutivoDevolucion;
	}

	public String getTipoConcepto() {
		return tipoConcepto;
	}

	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	public String getNumeroIDBeneficiario() {
		return numeroIDBeneficiario;
	}

	public void setNumeroIDBeneficiario(String numeroIDBeneficiario) {
		this.numeroIDBeneficiario = numeroIDBeneficiario;
	}

	public String getTipoIDBeneficiario() {
		return tipoIDBeneficiario;
	}

	public void setTipoIDBeneficiario(String tipoIDBeneficiario) {
		this.tipoIDBeneficiario = tipoIDBeneficiario;
	}

	public boolean isAprobarDevolucion() {
		return aprobarDevolucion;
	}

	public void setAprobarDevolucion(boolean aprobarDevolucion) {
		this.aprobarDevolucion = aprobarDevolucion;
	}

	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	public String getPaginaRetornaImpresion() {
		return paginaRetornaImpresion;
	}

	public void setPaginaRetornaImpresion(String paginaRetornaImpresion) {
		this.paginaRetornaImpresion = paginaRetornaImpresion;
	}

	/**
	 * @return the valorConceptoIngTes
	 */
	public String getValorConceptoIngTes() {
		return valorConceptoIngTes;
	}

	/**
	 * @param valorConceptoIngTes the valorConceptoIngTes to set
	 */
	public void setValorConceptoIngTes(String valorConceptoIngTes) {
		this.valorConceptoIngTes = valorConceptoIngTes;
	}

	public Integer getIngreso() {
		return ingreso;
	}

	public void setIngreso(Integer ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the eliminarRC
	 */
	public String getEliminarRC() {
		return eliminarRC;
	}

	/**
	 * @param eliminarRC the eliminarRC to set
	 */
	public void setEliminarRC(String eliminarRC) {
		this.eliminarRC = eliminarRC;
	}

	/**
	 * @return the valorTotalReciboCaja
	 */
	public double getValorTotalReciboCaja() {
		return valorTotalReciboCaja;
	}

	/**
	 * @param valorTotalReciboCaja the valorTotalReciboCaja to set
	 */
	public void setValorTotalReciboCaja(double valorTotalReciboCaja) {
		this.valorTotalReciboCaja = valorTotalReciboCaja;
	}

	/**
	 * @return the valorAnticRecConv
	 */
	public double getValorAnticRecConv() {
		return valorAnticRecConv;
	}

	/**
	 * @param valorAnticRecConv the valorAnticRecConv to set
	 */
	public void setValorAnticRecConv(double valorAnticRecConv) {
		this.valorAnticRecConv = valorAnticRecConv;
	}

	/**
	 * @return the numContratoOdontologia
	 */
	public int getNumContratoOdontologia() {
		return numContratoOdontologia;
	}

	/**
	 * @param numContratoOdontologia the numContratoOdontologia to set
	 */
	public void setNumContratoOdontologia(int numContratoOdontologia) {
		this.numContratoOdontologia = numContratoOdontologia;
	}

	/**
	 * @return the validacionAnticiposConveOdontologia
	 */
	public boolean isValidacionAnticiposConveOdontologia() {
		return validacionAnticiposConveOdontologia;
	}

	/**
	 * @param validacionAnticiposConveOdontologia the validacionAnticiposConveOdontologia to set
	 */
	public void setValidacionAnticiposConveOdontologia(
			boolean validacionAnticiposConveOdontologia) {
		this.validacionAnticiposConveOdontologia = validacionAnticiposConveOdontologia;
	}

	/**
	 * @return the existeReciboCajaXConceptAnticipConvenioOdonto
	 */
	public boolean isExisteReciboCajaXConceptAnticipConvenioOdonto() {
		return existeReciboCajaXConceptAnticipConvenioOdonto;
	}

	/**
	 * @param existeReciboCajaXConceptAnticipConvenioOdonto the existeReciboCajaXConceptAnticipConvenioOdonto to set
	 */
	public void setExisteReciboCajaXConceptAnticipConvenioOdonto(
			boolean existeReciboCajaXConceptAnticipConvenioOdonto) {
		this.existeReciboCajaXConceptAnticipConvenioOdonto = existeReciboCajaXConceptAnticipConvenioOdonto;
	}

	public String getSiguientePagina() {
		return siguientePagina;
	}

	public void setSiguientePagina(String siguientePagina) {
		this.siguientePagina = siguientePagina;
	}

	/**
	 * @return Retorna atributo listaFormasPago
	 */
	public ArrayList<DtoFormaPago> getListaFormasPago()
	{
		return listaFormasPago;
	}

	/**
	 * @param listaFormasPago Asigna atributo listaFormasPago
	 */
	public void setListaFormasPago(ArrayList<DtoFormaPago> listaFormasPago)
	{
		this.listaFormasPago = listaFormasPago;
	}

	public String getConsecutivoReciboCaja() {
		return consecutivoReciboCaja;
	}

	public void setConsecutivoReciboCaja(String consecutivoReciboCaja) {
		this.consecutivoReciboCaja = consecutivoReciboCaja;
	}

	public void setMapaPacientes(HashMap mapaPacientes) {
		this.mapaPacientes = mapaPacientes;
	}

	public HashMap getMapaPacientes() {
		return mapaPacientes;
	}
	
}
