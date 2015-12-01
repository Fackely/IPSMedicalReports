package com.princetonsa.actionform.facturasVarias;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.dto.facturasVarias.DtoDeudor;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.dto.facturasVarias.DtoDeudor;

public class GenModFacturasVariasForm extends ValidatorForm {
	
	
	/**
	 * Codigo de la factura varia
	 */
	private String codigo;
	
	/**
	 * manejo de estados de ejecucion de acciones 
	 */
	private String estado;
	
	
	/**
	 * fecha de creacion de la factura
	 */
	private String fecha;
	
	
	/**
	 * codigo del centro de atencion seleccionado
	 */
	private String centroAtencion;
	
	
	/**
	 * Codigo del centro_costo seleccionado
	 *
	 */
	private String centroCosto;
	
	/**
	 * codigo de la tabla conceptos facturas varias
	 *
	 */
	private String concepto;
	
	/**
	 * DEscripcion del concepto factura varia
	 */
	private String nombreConcepto;
	
	/**
	 *tipo de deudor 
	 *
	 */
	private String tipoDeudor;
	
	
	/**
	 * Valor de la factura
	 */
	private String valorFactura;
	
	/**
	 * Deudor
	 *
	 */
	private String deudor;
	
	/**
	 * observaciones acerca de la factura creada
	 *
	 */	
	private String observaciones;
	
	
	/**
	 * Estado de la factura
	 */
	private String estadoFactura;
	
	/**
	 * Fecha de la aprobacion de la factura
	 */
	private String fechaAprobacion;
	
	/**
	 * Mapa de consulta de resultados de un deudor y su asociado
	 *
	 */
	HashMap<String, Object> mapaAsocioDeudor;
	
	
	/**
	 * Mapa de recepcion de resultados de la consulta o busqueda de facturas varias
	 */
	private HashMap<String, Object> mapaBusquedaFacVarias;
	
	private String posicionMapa;
	
	/**
	 * Atributos para la busqueda de facturas varias
	 *
	 */
	private String nroFactura;
	private String centroAte;
	private String tipDeudor;
	private String deudorSelect;
	private String fechaInicial;
	private String fechaFinal;
	
	private ArrayList<HashMap<String, Object>> deudores = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> deudoresConsul = new ArrayList<HashMap<String,Object>>();
	
	public ArrayList<HashMap<String, Object>> getDeudoresConsul() {
		return deudoresConsul;
	}

	public void setDeudoresConsul(ArrayList<HashMap<String, Object>> deudoresConsul) {
		this.deudoresConsul = deudoresConsul;
	}

	private HashMap mapaAsocioDeudorClone = new HashMap ();
	
	private HashMap mapaDeudorConsulta = new HashMap ();
	
	/**
	 * Bandera de utilizacion para guardado de modificaciones
	 *
	 */
	private int modificacionExitosa;
	
	
	//*************CAMBIOS ANEX 811**********************************
	private DtoDeudor datosDeudor;
	private DtoDeudor datosDeudorBusqueda;
	private ArrayList<DtoMultasCitas> multasEncontradas = new ArrayList<DtoMultasCitas>();
	private ArrayList<DtoMultasCitas> multasAsignadas = new ArrayList<DtoMultasCitas>();
	private boolean seleccionarMultas;
	private boolean tieneRolAprobacionFactura;
	private boolean confirmarAprobacion;
	

	//*****Pager*************
	/**
	 * Patron de Ordenamiento 
	 * */
	private String patronOrdenar;	
	
	/**
	 * Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;	
	
	
	//Agergado pro anexo 958
	private int codigoFacVar;
	
	
	private String linkSiguiente="";
	
	
	/**
	 *Atributo para almacenar el mensaje de caja cerrada 
	 */
	private String mensajeCajaCerrada;
	
	/**
	 * atributo para alamacenar si la caja esta en estado cerrada o abierta 
	 */
	private Boolean estadoCaja;
	
	public void reset()
	{
		this.fecha = "";
		this.estado = "";
		this.codigo = "";
		this.deudor = "";		
		this.concepto = "";
		this.nombreConcepto = "";
		this.centroAte = "";
		this.tipDeudor = "";
		this.fechaFinal = ""; //UtilidadFecha.getFechaActual();
		this.nroFactura = "";
		this.tipoDeudor = "";		
		this.centroCosto = "";
		this.deudorSelect = "";
		this.fechaInicial = ""; //UtilidadFecha.getFechaActual();
		this.posicionMapa = "";		
		this.valorFactura = "0";
		this.estadoFactura = "";
		this.fechaAprobacion = "";
		this.observaciones = "";		
		this.centroAtencion = "";
		this.modificacionExitosa = 0;
		this.mapaAsocioDeudor = new HashMap<String, Object>();
		this.mapaAsocioDeudorClone = new HashMap ();
		this.mapaBusquedaFacVarias = new HashMap<String, Object>();
		this.deudores = new ArrayList<HashMap<String,Object>>();
		this.deudoresConsul= new ArrayList<HashMap<String,Object>>();
		this.mapaDeudorConsulta = new  HashMap ();
		
		this.datosDeudor = new DtoDeudor();
		this.datosDeudorBusqueda = new DtoDeudor();
		this.multasAsignadas = new ArrayList<DtoMultasCitas>();
		this.multasEncontradas = new ArrayList<DtoMultasCitas>();
		this.seleccionarMultas = false;
		this.tieneRolAprobacionFactura = false;
		this.confirmarAprobacion = false;
		
		this.linkSiguiente="";

		this.patronOrdenar = "";
		this.ultimoPatron = "";
		
		//Anexo 958
		this.codigoFacVar=ConstantesBD.codigoNuncaValido;
		
		this.estadoCaja=true;
		this.mensajeCajaCerrada="";
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public HashMap getMapaDeudorConsulta() {
		return mapaDeudorConsulta;
	}

	public void setMapaDeudorConsulta(HashMap mapaDeudorConsulta) {
		this.mapaDeudorConsulta = mapaDeudorConsulta;
	}

	public ArrayList<HashMap<String, Object>> getDeudores() {
		return deudores;
	}

	public void setDeudores(ArrayList<HashMap<String, Object>> deudores) {
		this.deudores = deudores;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * @return the deudor
	 */
	public String getDeudor() {
		return deudor;
	}

	/**
	 * @param deudor the deudor to set
	 */
	public void setDeudor(String deudor) {
		this.deudor = deudor;
	}

	/**
	 * @return the valorFactura
	 */
	public String getValorFactura() {
		return valorFactura;
	}

	/**
	 * @param valorFactura the valorFactura to set
	 */
	public void setValorFactura(String valorFactura) {
		this.valorFactura = valorFactura;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the mapaAsocioDeudor
	 */
	public HashMap<String, Object> getMapaAsocioDeudor() {
		return mapaAsocioDeudor;
	}

	/**
	 * @param mapaAsocioDeudor the mapaAsocioDeudor to set
	 */
	public void setMapaAsocioDeudor(HashMap<String, Object> mapaAsocioDeudor) {
		this.mapaAsocioDeudor = mapaAsocioDeudor;
	}
	
	/**
	 * @return the key mapaAsocioDeudor
	 */
	public Object getMapaAsocioDeudor(String key) {
		return mapaAsocioDeudor.get(key);
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request) 
	{
		 ActionErrors errores = new ActionErrors();
		 
		 if(this.estado.equals("guardar"))
	     {
			 //*********************************** VALIDACIONES DE CAMPOS IMPRESINDIBLES PARA LA CREACION DE LA FACTURA *****************//
						 
			 // campo fecha	
			 if(this.fecha.equals(""))
				 errores.add("",new ActionMessage("errors.required","La Fecha "));			 		
			 else
				 //se valida si el formato de la fecha es el adecuado
				 if(!UtilidadFecha.validarFecha(this.fecha))
					 errores.add("",new ActionMessage("errors.formatoFechaInvalido",this.fecha));
			
			 
			 

			 // campo de centro de atencion
			 if(this.centroAtencion.equals("") || this.centroAtencion.equals("-1"))
				 errores.add("",new ActionMessage("errors.required","El Centro de Atención "));
			 
			// campo de centro de costo
			 if(Utilidades.convertirAEntero(this.getCentroCosto())<=0)
				 errores.add("",new ActionMessage("errors.required","El Centro de Costo "));
			 
			 /// campo de deudor
			 if (this.datosDeudor.getCodigo().equals(""))
				 errores.add("",new ActionMessage("errors.required","Seleccione el Deudor,"));
			 
			 

			 // campo de concepto
			 if(this.concepto.equals("") || this.concepto.equals("-1"))
				 errores.add("",new ActionMessage("errors.required","El Concepto "));
			 else
			 {
				 String[] datosConcepto = this.concepto.split(ConstantesBD.separadorSplit);
				 //Validación para el concepto de tipo multa
				 if(datosConcepto[1].equals(ConstantesIntegridadDominio.acronimoMultas)&&this.getNumDefinitivoMultasAsignadas()==0)
				 {
					 errores.add("", new ActionMessage("errors.minimoCampos","la selección de una multa","factura"));
				 }
 			 }

			 // campo de valor de la factura
			 if(this.valorFactura.equals("0") || this.valorFactura.equals(""))
				 errores.add("",new ActionMessage("errors.required","El Valor de la Factura "));
			 
			 
			 if(!errores.isEmpty())
				 this.estado = "";
			 
	     }
		 if(this.estado.equals("guardarModificado"))
		 {
			 String posMapa = this.getPosicionMapa();
			  // campo de centro de atencion
			 if(Utilidades.convertirAEntero(this.getMapaBusquedaFacVarias("centroAtencion_"+posMapa)+"")<=0)
				 errores.add("",new ActionMessage("errors.required","El Centro de Atención "));
			 
			 // campo de centro de costo
			 if(Utilidades.convertirAEntero(this.getMapaBusquedaFacVarias("centroCosto_"+posMapa)+"")<=0)
				 errores.add("",new ActionMessage("errors.required","El Centro de Costo "));
			 
			 /// campo de deudor
			 if (this.datosDeudor.getCodigo().equals(""))
				 errores.add("",new ActionMessage("errors.required","Seleccione el Deudor,"));
			 
			 

			 // campo de concepto
			 if(this.getMapaBusquedaFacVarias("concepto_"+posMapa).toString().equals("") || this.getMapaBusquedaFacVarias("concepto_"+posMapa).toString().equals(ConstantesBD.codigoNuncaValido+""))
			 {
				 errores.add("",new ActionMessage("errors.required","El Concepto "));
			 }
			 else
			 {
				 String[] datosConcepto = this.getMapaBusquedaFacVarias("concepto_"+posMapa).toString().split(ConstantesBD.separadorSplit);
				 //Validación para el concepto de tipo multa
				 if(datosConcepto[1].equals(ConstantesIntegridadDominio.acronimoMultas)&&this.getNumDefinitivoMultasAsignadas()==0)
				 {
					 errores.add("", new ActionMessage("errors.minimoCampos","la selección de una multa","factura"));
				 }
 			 }

			 // campo de valor de la factura
			 if(this.getMapaBusquedaFacVarias("valorFactura_"+posMapa).toString().equals("0") || this.getMapaBusquedaFacVarias("valorFactura_"+posMapa).toString().equals(""))
				 errores.add("",new ActionMessage("errors.required","El Valor de la Factura "));
			 
			 if(!errores.isEmpty())
			 {
				 this.setEstado("facturaSelecCargPlantilla");
			 }
		 }
		 
		 
		 if(this.estado.equals("buscar"))
	     {
			 if(!UtilidadTexto.isEmpty(this.fechaInicial.toString()) || !UtilidadTexto.isEmpty(this.fechaFinal.toString()))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
						centinelaErrorFechas=true;
					}
					if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal.toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
						centinelaErrorFechas=true;
					}
					
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(UtilidadFecha.numeroDiasEntreFechas(this.getFechaInicial(), this.getFechaFinal())>31)
						{
							errores.add("", new ActionMessage("error.inventarios.articulosConsumidosPacientes", "para consultar Facturas Varias"));
						}
					}
				}
			 
			 if(!errores.isEmpty())
				 this.estado = "";
	     }
		 
		
		
		 
		 return errores;
	}

	public HashMap getMapaAsocioDeudorClone() {
		return mapaAsocioDeudorClone;
	}

	public void setMapaAsocioDeudorClone(HashMap mapaAsocioDeudorClone) {
		this.mapaAsocioDeudorClone = mapaAsocioDeudorClone;
	}

	public Object getMapaAsocioDeudorClone(String key) {
		return mapaAsocioDeudorClone.get(key);
	}

	public void setMapaAsocioDeudorClone(String key,Object value) {
		this.mapaAsocioDeudorClone.put(key, value);
	}
	
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the mapaBusquedaFacVarias
	 */
	public HashMap<String, Object> getMapaBusquedaFacVarias() {
		return mapaBusquedaFacVarias;
	}
	
	/**
	 * @return the key from mapaBusquedaFacVarias
	 */
	public Object getMapaBusquedaFacVarias(String key) {
		return mapaBusquedaFacVarias.get(key);
	}

	/**
	 * @param mapaBusquedaFacVarias the mapaBusquedaFacVarias to set
	 */
	public void setMapaBusquedaFacVarias(
			HashMap<String, Object> mapaBusquedaFacVarias) {
		this.mapaBusquedaFacVarias = mapaBusquedaFacVarias;
	}

	public void setMapaBusquedaFacVarias(String key,Object value) {
		this.mapaBusquedaFacVarias.put(key, value);
	}
	/**
	 * @return the posicionMapa
	 */
	public String getPosicionMapa() {
		return posicionMapa;
	}

	/**
	 * @param posicionMapa the posicionMapa to set
	 */
	public void setPosicionMapa(String posicionMapa) {
		this.posicionMapa = posicionMapa;
	}

	/**
	 * @return the centroAte
	 */
	public String getCentroAte() {
		return centroAte;
	}

	/**
	 * @param centroAte the centroAte to set
	 */
	public void setCentroAte(String centroAte) {
		this.centroAte = centroAte;
	}

	/**
	 * @return the deudorSelect
	 */
	public String getDeudorSelect() {
		return deudorSelect;
	}

	/**
	 * @param deudorSelect the deudorSelect to set
	 */
	public void setDeudorSelect(String deudorSelect) {
		this.deudorSelect = deudorSelect;
	}

	/**
	 * @return the nroFactura
	 */
	public String getNroFactura() {
		return nroFactura;
	}

	/**
	 * @param nroFactura the nroFactura to set
	 */
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	/**
	 * @return the tipDeudor
	 */
	public String getTipDeudor() {
		return tipDeudor;
	}

	/**
	 * @param tipDeudor the tipDeudor to set
	 */
	public void setTipDeudor(String tipDeudor) {
		this.tipDeudor = tipDeudor;
	}

	/**
	 * @return the modificacionExitosa
	 */
	public int getModificacionExitosa() {
		return modificacionExitosa;
	}

	/**
	 * @param modificacionExitosa the modificacionExitosa to set
	 */
	public void setModificacionExitosa(int modificacionExitosa) {
		this.modificacionExitosa = modificacionExitosa;
	}

	/**
	 * @return the estadoFactura
	 */
	public String getEstadoFactura() {
		return estadoFactura;
	}

	/**
	 * @param estadoFactura the estadoFactura to set
	 */
	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
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

	/**
	 * @return the datosDeudor
	 */
	public DtoDeudor getDatosDeudor() {
		return datosDeudor;
	}

	/**
	 * @param datosDeudor the datosDeudor to set
	 */
	public void setDatosDeudor(DtoDeudor datosDeudor) {
		this.datosDeudor = datosDeudor;
	}

	/**
	 * @return the multasEncontradas
	 */
	public ArrayList<DtoMultasCitas> getMultasEncontradas() {
		return multasEncontradas;
	}

	/**
	 * @param multasEncontradas the multasEncontradas to set
	 */
	public void setMultasEncontradas(ArrayList<DtoMultasCitas> multasEncontradas) {
		this.multasEncontradas = multasEncontradas;
	}

	/**
	 * @return the multasAsignadas
	 */
	public ArrayList<DtoMultasCitas> getMultasAsignadas() {
		return multasAsignadas;
	}

	/**
	 * @param multasAsignadas the multasAsignadas to set
	 */
	public void setMultasAsignadas(ArrayList<DtoMultasCitas> multasAsignadas) {
		this.multasAsignadas = multasAsignadas;
	}

	/**
	 * @return the seleccionarMultas
	 */
	public boolean isSeleccionarMultas() {
		return seleccionarMultas;
	}

	/**
	 * @param seleccionarMultas the seleccionarMultas to set
	 */
	public void setSeleccionarMultas(boolean seleccionarMultas) {
		this.seleccionarMultas = seleccionarMultas;
	}
	
	/**
	 * Número de multas encontradas
	 * @return
	 */
	public int getNumMultasEncontradas()
	{
		return this.multasEncontradas.size();
	}
	
	/**
	 * Número de multas asignadas
	 * @return
	 */
	public int getNumMultasAsignadas()
	{
		return this.multasAsignadas.size();
	}
	
	
	/**
	 * Método para obtener el número definitivo de multas asignadas
	 * @return
	 */
	public int getNumDefinitivoMultasAsignadas()
	{
		int numRegistros = 0;
		for(DtoMultasCitas multa:this.multasAsignadas)
		{
			if(multa.isSeleccionado())
			{
				numRegistros++;
			}
		}
		return numRegistros;
	}

	/**
	 * @return the tieneRolAprobacionFactura
	 */
	public boolean isTieneRolAprobacionFactura() {
		return tieneRolAprobacionFactura;
	}

	/**
	 * @param tieneRolAprobacionFactura the tieneRolAprobacionFactura to set
	 */
	public void setTieneRolAprobacionFactura(boolean tieneRolAprobacionFactura) {
		this.tieneRolAprobacionFactura = tieneRolAprobacionFactura;
	}

	/**
	 * @return the confirmarAprobacion
	 */
	public boolean isConfirmarAprobacion() {
		return confirmarAprobacion;
	}

	/**
	 * @param confirmarAprobacion the confirmarAprobacion to set
	 */
	public void setConfirmarAprobacion(boolean confirmarAprobacion) {
		this.confirmarAprobacion = confirmarAprobacion;
	}

	/**
	 * @return the datosDeudorBusqueda
	 */
	public DtoDeudor getDatosDeudorBusqueda() {
		return datosDeudorBusqueda;
	}

	/**
	 * @param datosDeudorBusqueda the datosDeudorBusqueda to set
	 */
	public void setDatosDeudorBusqueda(DtoDeudor datosDeudorBusqueda) {
		this.datosDeudorBusqueda = datosDeudorBusqueda;
	}

	/**
	 * @return the nombreConcepto
	 */
	public String getNombreConcepto() {
		return nombreConcepto;
	}

	/**
	 * @param nombreConcepto the nombreConcepto to set
	 */
	public void setNombreConcepto(String nombreConcepto) {
		this.nombreConcepto = nombreConcepto;
	}

	/**
	 * @return the fechaAprobacion
	 */
	public String getFechaAprobacion() {
		return fechaAprobacion;
	}

	/**
	 * @param fechaAprobacion the fechaAprobacion to set
	 */
	public void setFechaAprobacion(String fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public int getCodigoFacVar() {
		return codigoFacVar;
	}

	public void setCodigoFacVar(int codigoFacVar) {
		this.codigoFacVar = codigoFacVar;
	}

	/**
	 * @return the mensajeCajaCerrada
	 */
	public String getMensajeCajaCerrada() {
		return mensajeCajaCerrada;
	}

	/**
	 * @param mensajeCajaCerrada the mensajeCajaCerrada to set
	 */
	public void setMensajeCajaCerrada(String mensajeCajaCerrada) {
		this.mensajeCajaCerrada = mensajeCajaCerrada;
	}

	/**
	 * @return the estadoCaja
	 */
	public Boolean getEstadoCaja() {
		return estadoCaja;
	}

	/**
	 * @param estadoCaja the estadoCaja to set
	 */
	public void setEstadoCaja(Boolean estadoCaja) {
		this.estadoCaja = estadoCaja;
	}
	
}
