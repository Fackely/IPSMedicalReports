package com.princetonsa.actionform.tesoreria;

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

public class AprobacionAnulacionDevolucionesForm extends ValidatorForm 
{

	private String estado;
	
	private String devolucion;
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private String devolInicial;
	
	private String devolFinal;
	
	private String centroAtencion;
	
	private String motivoDevolucion;
	
	private String tipoId;
	
	private String numeroId;
	
	private String caja;
	
	private HashMap mapaResultadoDevoluciones;
	
	private HashMap detalleDevolucion;
	
	private String indiceDevolucion;
	
	private String motivoAnulacion;
	
	private int indiceDetalle;
	
	private String patronOrdenar;
	
	private String ultimoPatron;
	
	private String estadoDevolucion;
	
	private ResultadoBoolean mostrarMensaje;
	
	private String institucion;
	
	private String reciboCaja;
	
	private String valorConceptoIngTes;
	
	private Integer ingreso;
	
	private boolean validacionAnticiposConveOdontologia;
	
	/**
	 * atributo para determinar si el Recibo de Caja por concepto de Ingreso es de tipo Anticipos Convenios Odontologia
	 */
	private boolean existeReciboCajaXConceptAnticipConvenioOdonto;
	
	/**
	 * atibuto que contiene el numero del contrato asociado al convenio del recibo de caja
	 */
	private int numContratoOdontologia;
	
	private double valorAnticRecConv;
	
	private double valorTotalReciboCaja;
	
	/**Inc 2339
	  * Diana Ruiz  
	  * Almacena los datos de los pacientes    */
	
	 private HashMap mapaPacientes;
	
	/**
	 *
	 */
	public void reset(String centroAtencion, String institucion)
	{
		this.devolucion="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.devolInicial="";
		this.devolFinal="";
		this.caja="";
		this.centroAtencion=centroAtencion;
		this.institucion=institucion;
		this.tipoId="";
		this.numeroId="";
		this.motivoDevolucion="";
		this.mapaResultadoDevoluciones=new HashMap();
		this.mapaResultadoDevoluciones.put("numRegistros","0");
		this.detalleDevolucion=new HashMap();
		this.detalleDevolucion.put("numRegistros","0");
		this.motivoAnulacion="";
		this.indiceDevolucion="";
		this.indiceDetalle=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.estadoDevolucion="";
		this.reciboCaja="";
		this.mostrarMensaje=new ResultadoBoolean(false,"");
		this.valorConceptoIngTes = "" ;
		this.ingreso=null;
		this.validacionAnticiposConveOdontologia=false;
		this.existeReciboCajaXConceptAnticipConvenioOdonto= false;
        this.numContratoOdontologia= ConstantesBD.codigoNuncaValido;
        this.valorTotalReciboCaja=ConstantesBD.codigoNuncaValidoDouble;
        this.valorAnticRecConv= ConstantesBD.codigoNuncaValidoDouble;
        this.setMapaPacientes(new HashMap());
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("buscar"))
		{
			if(this.fechaInicial.equals("")&&this.fechaFinal.equals("") && this.reciboCaja.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial y la Fecha Final "));
			}
			if(!this.fechaInicial.equals("")&&this.fechaFinal.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
			}
			if(!this.fechaFinal.equals("")&&this.fechaInicial.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
			}
			if(!this.tipoId.equals("")&&this.numeroId.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Número de Identificación "));
			}
			if(!this.numeroId.equals("")&&this.tipoId.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo de Identificación "));
			}
			if(!UtilidadTexto.isEmpty(this.fechaFinal) || !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
				}
				
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
					}
				}if(this.estado.equals("buscarCodigo"))
				{
					if(this.devolucion.equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Consecutivo de la Devolución "));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
				/*if(!centinelaErrorFechas)
				{
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal())>=6)
					{
						errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias", "para consultar Facturas Varias"));
					}
				}*/
					
			}
			if(!errores.isEmpty())
				this.estado="continuar";
		}
		
		/*if(this.estado.equals("guardar")&&this.estadoDevolucion.equals("ANU"))
		{
			if(this.motivoAnulacion.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El motivo de la anulacion "));
			}
			
		}*/
		
		return errores;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getDevolFinal() {
		return devolFinal;
	}

	/**
	 * 
	 * @param devolFinal
	 */
	public void setDevolFinal(String devolFinal) {
		this.devolFinal = devolFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getDevolInicial() {
		return devolInicial;
	}

	/**
	 * 
	 * @param devolInicial
	 */
	public void setDevolInicial(String devolInicial) {
		this.devolInicial = devolInicial;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * 
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * 
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * 
	 * @return
	 */
	public String getCaja() {
		return caja;
	}

	/**
	 * 
	 * @param caja
	 */
	public void setCaja(String caja) {
		this.caja = caja;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public String getMotivoDevolucion() {
		return motivoDevolucion;
	}

	/**
	 * 
	 * @param motivoDevolucion
	 */
	public void setMotivoDevolucion(String motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getNumeroId() {
		return numeroId;
	}

	/**
	 * 
	 * @param numeroId
	 */
	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoId() {
		return tipoId;
	}

	/**
	 * 
	 * @param tipoId
	 */
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaResultadoDevoluciones() {
		return mapaResultadoDevoluciones;
	}

	/**
	 * 
	 * @param mapaResultadoDevoluciones
	 */
	public void setMapaResultadoDevoluciones(HashMap mapaResultadoDevoluciones) {
		this.mapaResultadoDevoluciones = mapaResultadoDevoluciones;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getDetalleDevolucion() {
		return detalleDevolucion;
	}

	/**
	 * 
	 * @param detalleDevolucion
	 */
	public void setDetalleDevolucion(HashMap detalleDevolucion) {
		this.detalleDevolucion = detalleDevolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getDevolucion() {
		return devolucion;
	}

	/**
	 * 
	 * @param devolucion
	 */
	public void setDevolucion(String devolucion) {
		this.devolucion = devolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	/**
	 * 
	 * @param motivoAnulacion
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}


	/**
	 * 
	 * @return
	 */
	public int getIndiceDetalle() {
		return indiceDetalle;
	}

	/**
	 * 
	 * @param indiceDetalle
	 */
	public void setIndiceDetalle(int indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndiceDevolucion() {
		return indiceDevolucion;
	}

	/**
	 * 
	 * @param indiceDevolucion
	 */
	public void setIndiceDevolucion(String indiceDevolucion) {
		this.indiceDevolucion = indiceDevolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * 
	 * @return
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * 
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaResultadoDevoluciones(String key) 
	{
		return mapaResultadoDevoluciones.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setMapaResultadoDevoluciones(String key,Object value) 
	{
		this.mapaResultadoDevoluciones.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getEstadoDevolucion() {
		return estadoDevolucion;
	}

	/**
	 * 
	 * @param estadoDevolucion
	 */
	public void setEstadoDevolucion(String estadoDevolucion) {
		this.estadoDevolucion = estadoDevolucion;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}
	
	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * 
	 * @return
	 */
	public String getReciboCaja() {
		return reciboCaja;
	}
	
	/**
	 * 
	 * @param reciboCaja
	 */
	public void setReciboCaja(String reciboCaja) {
		this.reciboCaja = reciboCaja;
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
	 * @return Retorna atributo ingreso
	 */
	public Integer getIngreso()
	{
		return ingreso;
	}

	/**
	 * @param ingreso Asigna atributo ingreso
	 */
	public void setIngreso(Integer ingreso)
	{
		this.ingreso = ingreso;
	}

	public void setMapaPacientes(HashMap mapaPacientes) {
		this.mapaPacientes = mapaPacientes;
	}

	public HashMap getMapaPacientes() {
		return mapaPacientes;
	}

	
	
	
}
