/**
 * 
 */
package com.princetonsa.actionform.manejoPaciente;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;

/**
 * @author axioma
 *
 */
public class AmparosPorReclamarForm extends ValidatorForm 
{
	
	private static final String PROPERTIES_AMPAROS_POR_RECLAMAR="com.servinte.mensajes.manejoPaciente.AmparosPorReclamarForm";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String estado;

	/**
	 * 
	 */
	private int indiceIngresoSeleccionado;
	
	/**
	 * 
	 */
	private int indiceFacturaSeleccionado;
	
	/**
	 * Parametros para ordenar
	 */
	private String patronOrdenar;

	
	
	private String propiedadOrdenar;
	

	/**
	 * Parametros para ordenar
	 */
	private String esDescendente;
	
	/**
	 * 
	 */
	private ArrayList<DtoIngresos> ingresos;
	
	/**
	 * 
	 */
	private ArrayList<DtoFactura> facturas;
	
	
	/**
	 * 
	 */
	private DtoReclamacionesAccEveFact amparoXReclamar;
	
	/**
	 * 
	 */
	private boolean tienerReclamacionRadicadaPrevia;
	
	
	/**
	 * 
	 */
	private ArrayList<DtoReclamacionesAccEveFact> listadoReclamaciones;
	
	/**
	 * 
	 */
	private int indiceReclamacionSeleccionada=ConstantesBD.codigoNuncaValido;
	
	/**
	 * 
	 */
	private DtoFiltroBusquedaAvanzadaReclamaciones filtro;
	
	/**
	 * 
	 */
	private ArrayList conveniosAseguradora;
	
	/**
	 * Codigo que arroja la busqueda de diagnostico generico para el diagnostico de ingreso
	 */
	private String codigoDiagnosticoIngreso;
	
	/**
	 * Codigo que arroja la busqueda de diagnostico generico para el diagnostico de egreso
	 */
	private String codigoDiagnosticoEgreso;
	
	/**
	 * Campo necesario para llamar la busqueda generica de diagnosticos 
	 */
	private String idValorFicha;
	
	/**
	 * Indica si se encontraron diagnosticos de orden ambulatoria
	 */
	private boolean tieneDxOrdenAmbulatoria=false;
	/**
	 * 
	 */
	public void reset()
	{
		this.ingresos=new ArrayList<DtoIngresos>();
		this.facturas=new ArrayList<DtoFactura>();
		this.indiceIngresoSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceFacturaSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceReclamacionSeleccionada=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.propiedadOrdenar="";
		this.esDescendente = "";
		this.amparoXReclamar=new DtoReclamacionesAccEveFact();
		this.tienerReclamacionRadicadaPrevia=false;
		this.listadoReclamaciones=new ArrayList<DtoReclamacionesAccEveFact>();
		this.filtro=new DtoFiltroBusquedaAvanzadaReclamaciones();
		this.conveniosAseguradora=new ArrayList();
	}
	
	/**
	 * 
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources(
				PROPERTIES_AMPAROS_POR_RECLAMAR);
		if(estado.equals("guardar"))
		{
			if(this.isTienerReclamacionRadicadaPrevia())
			{
				if(UtilidadTexto.isEmpty(this.amparoXReclamar.getRespuestaGlosa()))
				{
					errores.add("REQUERIDO", new ActionMessage("errors.required","Respuesta a Glosa "));  
				}
			}
			if(UtilidadTexto.isEmpty(this.amparoXReclamar.getTipoReclamacion()))
			{
				errores.add("REQUERIDO", new ActionMessage("errors.required","Tipo Reclamación "));  
			}
			if(this.getAmparoXReclamar().getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURIPS)) {
				
				double totalFacAmpGastMedQX = 0;
				double totalFacAmpGastTransMov = 0;
				double totalRecAmpGastMedQX = 0;
				double totalRecAmpGastTransMov = 0;
				
				if (this.getAmparoXReclamar().getAmparoXReclamar().getTotalFacAmpGastMedQX() != null) {
					totalFacAmpGastMedQX = this.getAmparoXReclamar().getAmparoXReclamar().getTotalFacAmpGastMedQX().doubleValue();
				} else {
					this.getAmparoXReclamar().getAmparoXReclamar().setTotalFacAmpGastMedQX(new BigDecimal(0));
				}
				if (this.getAmparoXReclamar().getAmparoXReclamar().getTotalFacAmpGastTransMov() != null) {
					totalFacAmpGastTransMov = this.getAmparoXReclamar().getAmparoXReclamar().getTotalFacAmpGastTransMov().doubleValue();
				} else {
					this.getAmparoXReclamar().getAmparoXReclamar().setTotalFacAmpGastTransMov(new BigDecimal(0));
				}
				if (this.getAmparoXReclamar().getAmparoXReclamar().getTotalRecAmpGastMedQX() != null) {
					totalRecAmpGastMedQX = this.getAmparoXReclamar().getAmparoXReclamar().getTotalRecAmpGastMedQX().doubleValue();
				} else {
					this.getAmparoXReclamar().getAmparoXReclamar().setTotalRecAmpGastMedQX(new BigDecimal(0));
				}
				if (this.getAmparoXReclamar().getAmparoXReclamar().getTotalRecAmpGastTransMov() != null) {
					totalRecAmpGastTransMov = this.getAmparoXReclamar().getAmparoXReclamar().getTotalRecAmpGastTransMov().doubleValue();
				} else {
					this.getAmparoXReclamar().getAmparoXReclamar().setTotalRecAmpGastTransMov(new BigDecimal(0));
				}
				
				if(totalFacAmpGastMedQX <= 0 && totalRecAmpGastMedQX < 0&&
						totalFacAmpGastTransMov <= 0 && totalRecAmpGastTransMov < 0) {
					errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco","Es requerido ingresar información de la reclamación."));
				}
				if(totalFacAmpGastMedQX > this.getFacturas().get(this.getIndiceFacturaSeleccionado()).getValorConvenio())
				{
					errores.add("errors.floatMenorQue", new ActionMessage("errors.floatMenorQue","Total facturado amparo gastos médico quirúrgicos","Valor del Convenio de la Factura Seleccionada"));  
				}
				if(totalRecAmpGastMedQX > totalFacAmpGastMedQX)
				{
					errores.add("errors.floatMenorQue", new ActionMessage("errors.floatMenorQue","Total reclamado amparo gastos médico quirúrgicos al Fosyga","Total facturado amparo gastos médico quirúrgicos"));
				}
				
				double totalFacturadoReclamacion = totalFacAmpGastMedQX + totalFacAmpGastTransMov;
				double totalConvenio = this.getFacturas().get(this.getIndiceFacturaSeleccionado()).getValorConvenio();
				if(totalFacturadoReclamacion != totalConvenio) { 
					errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco","La suma de los valores de los campos Total facturado amparo gastos médico quirúrgicos y Total facturado amparo gastos de transporte y movilización de la víctima debe ser igual al valor total del convenio de la factura."));
				}
				if(totalRecAmpGastTransMov > totalFacAmpGastTransMov) {
					errores.add("errors.floatMenorQue", new ActionMessage("errors.floatMenorQue","Total reclamado amparo gastos de transporte y movilización de la víctima al Fosyga","Total facturado amparo gastos de transporte y movilización de la víctima"));
				}
				
				/**MT 5424, guardar diagnosticos de ingreso y/o egreso*/
				if(this.getAmparoXReclamar().getCertAtenMedicaFurips().getViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios){
					if(UtilidadTexto.isEmpty(this.getCodigoDiagnosticoIngreso())){
						errores.add("",new ActionMessage("errors.required",mensajes.getMessage("codigoDxIngreso")));
					}else{
						String[] codigoDx=this.getCodigoDiagnosticoIngreso().split(ConstantesBD.separadorSplit);
						if(codigoDx.length>1){
							this.getAmparoXReclamar().getCertAtenMedicaFurips().setAcronimoDxIngreso(codigoDx[0]);
							this.getAmparoXReclamar().getCertAtenMedicaFurips().setTipoCieDxIngreso(Integer.parseInt(codigoDx[1]));
						}
			}
					if(UtilidadTexto.isEmpty(this.getCodigoDiagnosticoEgreso())){
						errores.add("",new ActionMessage("errors.required",mensajes.getMessage("codigoDxEgreso")));
					}else{
						String[] codigoDx=this.getCodigoDiagnosticoEgreso().split(ConstantesBD.separadorSplit);
						if(codigoDx.length>1){
							this.getAmparoXReclamar().getCertAtenMedicaFurips().setAcronimoDxEgreso(codigoDx[0]);
							this.getAmparoXReclamar().getCertAtenMedicaFurips().setTipoCieDxEgreso(Integer.parseInt(codigoDx[1]));
						}
					}
				}
				/****/
			}
			else if(this.getAmparoXReclamar().getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURPRO)) {
				double valorProtesis = 0;
				double valorAdaptacionProtesis = 0;
				double valorRehabilitacion = 0;
				if (this.getAmparoXReclamar().getServiciosReclamados().getValorProtesis() != null) {
					valorProtesis = this.getAmparoXReclamar().getServiciosReclamados().getValorProtesis().doubleValue();
				} else {
					 this.getAmparoXReclamar().getServiciosReclamados().setValorProtesis(new BigDecimal(0));
				}
				if (this.getAmparoXReclamar().getServiciosReclamados().getValorAdaptacionProtesis() != null) {
					valorAdaptacionProtesis = this.getAmparoXReclamar().getServiciosReclamados().getValorAdaptacionProtesis().doubleValue();
				} else {
					 this.getAmparoXReclamar().getServiciosReclamados().setValorAdaptacionProtesis(new BigDecimal(0));
				}
				if (this.getAmparoXReclamar().getServiciosReclamados().getValorRehabilitacion() != null) {
					valorRehabilitacion = this.getAmparoXReclamar().getServiciosReclamados().getValorRehabilitacion().doubleValue();
				} else {
					 this.getAmparoXReclamar().getServiciosReclamados().setValorRehabilitacion(new BigDecimal(0));
				}
				
				if((valorProtesis +	valorAdaptacionProtesis + valorRehabilitacion) > 
						this.getFacturaSeleccionado().getValorConvenio()) {
					errores.add("errors.floatMenorQue", new ActionMessage("errors.floatMenorQue","Total de los servicios reclamados","Valor del Convenio de la Factura Seleccionada"));  
				}
			}
		}
		else if(estado.equals("radicar"))
		{
			if(UtilidadTexto.isEmpty(this.amparoXReclamar.getNroRadicado()))
			{
				errores.add("REQUERIDO", new ActionMessage("errors.required","Número Radicado "));  
			}
			if(UtilidadTexto.isEmpty(this.amparoXReclamar.getFechaRadicacion()))
			{
				errores.add("REQUERIDO", new ActionMessage("errors.required","Fecha "));  
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.amparoXReclamar.getFechaRadicacion()))
				{
					errores.add("fecha de radicación", new ActionMessage("errors.formatoFechaInvalido",this.amparoXReclamar.getFechaRadicacion()));
				}
				else
				{
					// se verifica que la fecha de radicación no sea mayor a la actual
					if((UtilidadFecha.conversionFormatoFechaABD(this.amparoXReclamar.getFechaRadicacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha de radicación", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de radicación", "actual"));
					}
					// se verifica que la fecha de radicación no sea menor a la fecha de elaboracion
					if((UtilidadFecha.conversionFormatoFechaABD(this.amparoXReclamar.getFechaReclamacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.amparoXReclamar.getFechaRadicacion()))>0)
					{
						errores.add("fecha de radicación", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de radicación", "de elaboración de cuenta de cobro"));
					}
				}
			}
			if(UtilidadTexto.isEmpty(this.amparoXReclamar.getHoraRadicacion()))
			{
				errores.add("REQUERIDO", new ActionMessage("errors.required","Hora "));  
			}
		}
		else if(estado.equals("anular"))
		{
			if(UtilidadTexto.isEmpty(this.amparoXReclamar.getMotivoAnulacion()))
			{
				errores.add("REQUERIDO", new ActionMessage("errors.required","Motivo Anulación "));  
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

	
	public ArrayList<DtoIngresos> getIngresos() {
		return ingresos;
	}

	public void setIngresos(ArrayList<DtoIngresos> ingresos) {
		this.ingresos = ingresos;
	}

	public int getIndiceIngresoSeleccionado() {
		return indiceIngresoSeleccionado;
	}

	public void setIndiceIngresoSeleccionado(int indiceIngresoSeleccionado) {
		this.indiceIngresoSeleccionado = indiceIngresoSeleccionado;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getPropiedadOrdenar() {
		return propiedadOrdenar;
	}

	public void setPropiedadOrdenar(String propiedadOrdenar) {
		this.propiedadOrdenar = propiedadOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public ArrayList<DtoFactura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<DtoFactura> facturas) {
		this.facturas = facturas;
	}

	public int getIndiceFacturaSeleccionado() {
		return indiceFacturaSeleccionado;
	}

	public void setIndiceFacturaSeleccionado(int indiceFacturaSeleccionado) {
		this.indiceFacturaSeleccionado = indiceFacturaSeleccionado;
	}

	public DtoReclamacionesAccEveFact getAmparoXReclamar() {
		return amparoXReclamar;
	}

	public void setAmparoXReclamar(DtoReclamacionesAccEveFact amparoXReclamar) {
		this.amparoXReclamar = amparoXReclamar;
	}

	public boolean isTienerReclamacionRadicadaPrevia() {
		return tienerReclamacionRadicadaPrevia;
	}

	public void setTienerReclamacionRadicadaPrevia(
			boolean tienerReclamacionRadicadaPrevia) {
		this.tienerReclamacionRadicadaPrevia = tienerReclamacionRadicadaPrevia;
	}

	public ArrayList<DtoReclamacionesAccEveFact> getListadoReclamaciones() {
		return listadoReclamaciones;
	}

	public void setListadoReclamaciones(
			ArrayList<DtoReclamacionesAccEveFact> listadoReclamaciones) {
		this.listadoReclamaciones = listadoReclamaciones;
	}
	
	public DtoIngresos getIngresoSeleccionado()
	{
		return this.ingresos.get(this.getIndiceIngresoSeleccionado());
	}
	
	public DtoFactura getFacturaSeleccionado()
	{
		return this.getFacturas().get(this.getIndiceFacturaSeleccionado());
	}

	public int getIndiceReclamacionSeleccionada() {
		return indiceReclamacionSeleccionada;
	}

	public void setIndiceReclamacionSeleccionada(int indiceReclamacionSeleccionada) {
		this.indiceReclamacionSeleccionada = indiceReclamacionSeleccionada;
	}

	public DtoFiltroBusquedaAvanzadaReclamaciones getFiltro() {
		return filtro;
	}

	public void setFiltro(DtoFiltroBusquedaAvanzadaReclamaciones filtro) {
		this.filtro = filtro;
	}

	public ArrayList getConveniosAseguradora() {
		return conveniosAseguradora;
	}

	public void setConveniosAseguradora(ArrayList conveniosAseguradora) {
		this.conveniosAseguradora = conveniosAseguradora;
	}

	public String getIdValorFicha() {
		return idValorFicha;
	}

	public void setIdValorFicha(String idValorFicha) {
		this.idValorFicha = idValorFicha;
	}

	public String getCodigoDiagnosticoIngreso() {
		return codigoDiagnosticoIngreso;
	}

	public void setCodigoDiagnosticoIngreso(String codigoDiagnosticoIngreso) {
		this.codigoDiagnosticoIngreso = codigoDiagnosticoIngreso;
	}

	public String getCodigoDiagnosticoEgreso() {
		return codigoDiagnosticoEgreso;
	}

	public void setCodigoDiagnosticoEgreso(String codigoDiagnosticoEgreso) {
		this.codigoDiagnosticoEgreso = codigoDiagnosticoEgreso;
	}

	public boolean isTieneDxOrdenAmbulatoria() {
		return tieneDxOrdenAmbulatoria;
	}

	public void setTieneDxOrdenAmbulatoria(boolean tieneDxOrdenAmbulatoria) {
		this.tieneDxOrdenAmbulatoria = tieneDxOrdenAmbulatoria;
	}

}
