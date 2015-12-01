/*
 * @(#)FacturaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 
 *
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.facturacion.DtoFactura;

import util.ConstantesBD;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Forma para manejo de factura  
 *
 * @version 1.0 Jul 17, 2007
 */
public class FacturaForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * estado auxiliar para enviar la info de los frame
	 */
	private String estadoAux="";
	
	/**
	 * codigos de las cuentas a validar
	 */
	private Vector cuentas;
	
	/**
	 * 1- Paciente con atencion finalizada
	 * 2- Paciente continua en proceso de atencion
	 */
	private int tipoFacturacion;
	
	/**
	 * mapa con subCuentas
	 */
	private HashMap subCuentasMap;
	
	/**
	 * 
	 */
	private ArrayList<ElementoApResource> warningsFactura;
	
	/**
	 * 
	 */
	private ArrayList<ElementoApResource> erroresFactura;
	
	/**
	 * 
	 */
	private ArrayList<DtoFactura> facturas= new ArrayList<DtoFactura>();
	
	/**
	 * 
	 */
	private HashMap abonosYDescuentosMap= new HashMap();
	
	/**
	 * 
	 */
	private int index;
	
	/**
	 * 
	 */
	private String numeroReciboCajaGenerado;
	
	/**
	 * 
	 */
	private String consecutivoReciboCajaGenerado;
	
	/**
	 * Siguiente pagina cuando se cancela el proceso de facturación
	 */
	private String siguientePagina="";
	
	/**
	 * 
	 */
	private HashMap mapaPrefacturas;
	
	/**
	 * 
	 */
	private HashMap codigosFacturasImprimir;
	
	/**
	 * 
	 */
	private boolean permiteCorteFactura;
	
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * Tipos de formato para impresion de factura
	 */
	private String tipoImpresion;
	
	/**
	 * Anexo de Cuenta
	 */
	private String anexoC;
	
	/**
 	 * 
 	 */
 	private boolean imprimirAnexo;
 	/**
 	 * 
 	 */
 	private String tipoAnexo;
 	
 	/**
 	 * Formato de Impresion del Anexo 
 	 */
 	private String formatoConvenio;
 	
 	/**
 	 * Check Todos
 	 */
 	private String checkTodos;
	
	/**
 	 * Proceso exitoso generación factura
 	 */
 	private boolean procesoExitoso;
 	
 	/**
 	 * Almacena la institución del usuario en sesión
 	 */
 	private int institucion;
 	
 	
 	
 	/**
 	 * nombre del reporte generado 
 	 */
 	private String nombreArchivoGenerado;
	
	/**
 	 * nombre del reporte generado 
 	 */
 	private String nombreArchivoGeneradoCopia;
 	
 	
 	
 	
 	
 	//MT 6142 Se declara variables
 	
	/**
	 * almacena los datos de la consulta de una factura en especifico
	 */
	private HashMap mapaDetalleFactura;
	
	  private String numeroAutorizacion;
	  //Fin MT
 	
	/**
	 * Método que limpia este form
	 */
	public void reset()
	{
		this.cuentas= new Vector();
		this.tipoFacturacion= ConstantesBD.codigoNuncaValido;
		this.subCuentasMap= new HashMap();
		this.subCuentasMap.put("numRegistros", "0");
		this.warningsFactura= new ArrayList<ElementoApResource>();
		this.erroresFactura= new ArrayList<ElementoApResource>();
		this.facturas= new ArrayList<DtoFactura>();
		this.abonosYDescuentosMap= new HashMap();
		this.abonosYDescuentosMap.put("numRegistros", "0");
		this.estadoAux="";
		this.index=ConstantesBD.codigoNuncaValido;
		this.numeroReciboCajaGenerado="";
		this.siguientePagina="";
		this.mapaPrefacturas= new HashMap();
		this.mapaPrefacturas.put("numRegistros", "0");
		this.codigosFacturasImprimir= new HashMap();
		this.codigosFacturasImprimir.put("numRegistros", "0");
		this.permiteCorteFactura=false;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.tipoImpresion="";
		this.imprimirAnexo=false;
	 	this.tipoAnexo="";
	 	this.checkTodos="";
	 	this.anexoC="";
	 	this.formatoConvenio="";
	 	this.setConsecutivoReciboCajaGenerado("");
	 	this.setProcesoExitoso(false);
	 	this.institucion= ConstantesBD.codigoNuncaValido;
	 	this.nombreArchivoGenerado="";
	 	this.nombreArchivoGeneradoCopia="";
	}
	
	/**
	 * 
	 *
	 */
	public void resetAbonos()
	{
		this.abonosYDescuentosMap= new HashMap();
		this.abonosYDescuentosMap.put("numRegistros", "0");
		this.estadoAux="";
		this.erroresFactura= new ArrayList<ElementoApResource>();
		this.warningsFactura= new ArrayList<ElementoApResource>();
	}
	
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	@SuppressWarnings("rawtypes")
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		
		if (estado.equals("generarTotales"))
		{
			boolean seleccionoSubCuenta=false;
			for(int w=0; w<Integer.parseInt(this.subCuentasMap.get("numRegistros").toString());w++)
			{
				if(this.subCuentasMap.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
					seleccionoSubCuenta=true;
			}
			if(!seleccionoSubCuenta)
				errors.add("error.facturacion.sinSubcuentaAFacturar", new ActionMessage("error.facturacion.sinSubcuentaAFacturar"));
			
			if(!errors.isEmpty())
				this.setEstado("seleccionConvenios");
			
		}
		
		return errors;
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
	 * @return the tipoFacturacion
	 */
	public int getTipoFacturacion() {
		return tipoFacturacion;
	}

	/**
	 * @param tipoFacturacion the tipoFacturacion to set
	 */
	public void setTipoFacturacion(int tipoFacturacion) {
		this.tipoFacturacion = tipoFacturacion;
	}

	/**
	 * @return the cuentas
	 */
	public Vector getCuentas() {
		return cuentas;
	}

	/**
	 * @param cuentas the cuentas to set
	 */
	public void setCuentas(Vector cuentas) {
		this.cuentas = cuentas;
	}

	/**
	 * @return the SubCuentas
	 */
	public HashMap getSubCuentasMap() {
		return subCuentasMap;
	}

	/**
	 * @param SubCuentas the SubCuentas to set
	 */
	public void setSubCuentasMap(HashMap scMap) {
		this.subCuentasMap = scMap;
	}	
	
	/**
	 * @return the SubCuentas
	 */
	public Object getSubCuentasMap(Object key) {
		return subCuentasMap.get(key);
	}

	/**
	 * @param SubCuentas the SubCuentas to set
	 */
	public void setSubCuentasMap(Object key, Object value) {
		this.subCuentasMap.put(key, value);
	}	
	
	/**
	 * 
	 * @return
	 */
	public Vector getSubCuentasSeleccionadasVector ()
	{
		Vector subCuentas= new Vector();
		for(int w=0; w<Integer.parseInt(this.getSubCuentasMap("numRegistros").toString()); w++)
		{
			if(this.getSubCuentasMap("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
				subCuentas.add(this.getSubCuentasMap("subcuenta_"+w).toString());
		}	
		return subCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<InfoDatosInt> getConveniosSeleccionadosList ()
	{
		ArrayList<InfoDatosInt> resultado= new ArrayList<InfoDatosInt>();
		for(int w=0; w<Integer.parseInt(this.getSubCuentasMap("numRegistros").toString()); w++)
		{
			if(this.getSubCuentasMap("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
			{	
				resultado.add(new InfoDatosInt(Utilidades.convertirAEntero(this.getSubCuentasMap("codigoconvenio_"+w)+""), this.getSubCuentasMap("nombreresponsable_"+w)+""));
			}	
		}	
		return resultado;
	}

	
	
	/**
	 * 
	 * @return
	 */
	public Vector getEmpresasInstitucionSeleccionadasVector ()
	{
		Vector empresasInstitucion= new Vector();
		for(int w=0; w<Integer.parseInt(this.getSubCuentasMap("numRegistros").toString()); w++)
		{
			if(this.getSubCuentasMap("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
				empresasInstitucion.add(this.getSubCuentasMap("empresainstitucion_"+w).toString());
		}	
		return empresasInstitucion;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Vector getSubCuentasSeleccionadasYSinInfoAdicVenezuelaVector ()
	{
		Vector subCuentas= new Vector();
		for(int w=0; w<Integer.parseInt(this.getSubCuentasMap("numRegistros").toString()); w++)
		{
			if(this.getSubCuentasMap("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi) && !UtilidadTexto.getBoolean(this.getSubCuentasMap("validarinfovenezuela_"+w).toString()))
				subCuentas.add(this.getSubCuentasMap("subcuenta_"+w).toString());
		}	
		return subCuentas;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Vector getSubCuentasVector ()
	{
		Vector subCuentas= new Vector();
		for(int w=0; w<Integer.parseInt(this.getSubCuentasMap("numRegistros").toString()); w++)
		{
			subCuentas.add(this.getSubCuentasMap("subcuenta_"+w).toString());
		}	
		return subCuentas;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Vector getSubCuentasNOSeleccionadosVector ()
	{
		Vector subcuentas= new Vector();
		for(int w=0; w<Integer.parseInt(this.getSubCuentasMap("numRegistros").toString()); w++)
		{
			if(!this.getSubCuentasMap("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
				subcuentas.add(this.getSubCuentasMap("codigoconvenio_"+w).toString());
		}	
		return subcuentas;
	}
	
	
	/**
	 * @return the warningsFactura
	 */
	public ArrayList<ElementoApResource> getWarningsFactura() {
		return warningsFactura;
	}

	/**
	 * @param warningsFactura the warningsFactura to set
	 */
	public void setWarningsFactura(ArrayList<ElementoApResource> warningsFactura) {
		this.warningsFactura = warningsFactura;
	}

	/**
	 * @param warningsFactura the warningsFactura to set
	 */
	public void setWarningFactura(ElementoApResource warningFactura) {
		this.warningsFactura.add(warningFactura) ;
	}
	
	/**
	 * @return the facturas
	 */
	public ArrayList<DtoFactura> getFacturas() {
		return facturas;
	}

	/**
	 * @param facturas the facturas to set
	 */
	public void setFacturas(ArrayList<DtoFactura> facturas) {
		this.facturas = facturas;
	}

	/**
	 * @return the abonosYDescuentosMap
	 */
	public HashMap getAbonosYDescuentosMap() {
		return abonosYDescuentosMap;
	}

	/**
	 * @param abonosYDescuentosMap the abonosYDescuentosMap to set
	 */
	public void setAbonosYDescuentosMap(HashMap abonosYDescuentosMap) {
		this.abonosYDescuentosMap = abonosYDescuentosMap;
	}

	/**
	 * @return the abonosYDescuentosMap
	 */
	public Object getAbonosYDescuentosMap(Object key) {
		return abonosYDescuentosMap.get(key);
	}

	/**
	 * @param abonosYDescuentosMap the abonosYDescuentosMap to set
	 */
	public void setAbonosYDescuentosMap(Object key, Object value) {
		this.abonosYDescuentosMap.put(key, value);
	}

	/**
	 * @return the erroresFactura
	 */
	public ArrayList<ElementoApResource> getErroresFactura() {
		return erroresFactura;
	}

	/**
	 * @param erroresFactura the erroresFactura to set
	 */
	public void setErroresFactura(ArrayList<ElementoApResource> erroresFactura) {
		this.erroresFactura = erroresFactura;
	}

	/**
	 * @return the estadoAux
	 */
	public String getEstadoAux() {
		return estadoAux;
	}

	/**
	 * @param estadoAux the estadoAux to set
	 */
	public void setEstadoAux(String estadoAux) {
		this.estadoAux = estadoAux;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the numeroReciboCajaGenerado
	 */
	public String getNumeroReciboCajaGenerado() {
		return numeroReciboCajaGenerado;
	}

	/**
	 * @param numeroReciboCajaGenerado the numeroReciboCajaGenerado to set
	 */
	public void setNumeroReciboCajaGenerado(String numeroReciboCajaGenerado) {
		this.numeroReciboCajaGenerado = numeroReciboCajaGenerado;
	}

	/**
	 * @return the siguientePagina
	 */
	public String getSiguientePagina() {
		return siguientePagina;
	}

	/**
	 * @param siguientePagina the siguientePagina to set
	 */
	public void setSiguientePagina(String siguientePagina) {
		this.siguientePagina = siguientePagina;
	}

	/**
	 * @return the mapaPrefacturas
	 */
	public HashMap getMapaPrefacturas() {
		return mapaPrefacturas;
	}

	/**
	 * @param mapaPrefacturas the mapaPrefacturas to set
	 */
	public void setMapaPrefacturas(HashMap mapaPrefacturas) {
		this.mapaPrefacturas = mapaPrefacturas;
	}

	/**
	 * @return the mapaPrefacturas
	 */
	public Object getMapaPrefacturas(Object key) {
		return mapaPrefacturas.get(key);
	}

	/**
	 * @param mapaPrefacturas the mapaPrefacturas to set
	 */
	public void setMapaPrefacturas(Object key, Object value) {
		this.mapaPrefacturas.put(key, value);
	}

	/**
	 * @return the codigosFacturasImprimir
	 */
	public HashMap getCodigosFacturasImprimir() {
		return codigosFacturasImprimir;
	}

	/**
	 * @param codigosFacturasImprimir the codigosFacturasImprimir to set
	 */
	public void setCodigosFacturasImprimir(HashMap codigosFacturasImprimir) {
		this.codigosFacturasImprimir = codigosFacturasImprimir;
	}
	
	/**
	 * @return the codigosFacturasImprimir
	 */
	public Object getCodigosFacturasImprimir(Object key) {
		return codigosFacturasImprimir.get(key);
	}

	/**
	 * @param codigosFacturasImprimir the codigosFacturasImprimir to set
	 */
	public void setCodigosFacturasImprimir(Object key, Object value) {
		this.codigosFacturasImprimir.put(key, value);
	}

	/**
	 * @return the permiteCorteFactura
	 */
	public boolean getPermiteCorteFactura() {
		return permiteCorteFactura;
	}

	/**
	 * @param permiteCorteFactura the permiteCorteFactura to set
	 */
	public void setPermiteCorteFactura(boolean permiteCorteFactura) {
		this.permiteCorteFactura = permiteCorteFactura;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public String getTipoImpresion() {
		return tipoImpresion;
	}

	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
	}

	public boolean isImprimirAnexo() {
		return imprimirAnexo;
	}

	public void setImprimirAnexo(boolean imprimirAnexo) {
		this.imprimirAnexo = imprimirAnexo;
	}

	public String getTipoAnexo() {
		return tipoAnexo;
	}

	public void setTipoAnexo(String tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}

	public String getCheckTodos() {
		return checkTodos;
	}

	public void setCheckTodos(String checkTodos) {
		this.checkTodos = checkTodos;
	}

	public String getAnexoC() {
		return anexoC;
	}

	public void setAnexoC(String anexoC) {
		this.anexoC = anexoC;
	}

	public String getFormatoConvenio() {
		return formatoConvenio;
	}

	public void setFormatoConvenio(String formatoConvenio) {
		this.formatoConvenio = formatoConvenio;
	}

	/**
	 * @param procesoExitoso the procesoExitoso to set
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}

	/**
	 * @return the procesoExitoso
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}

	/**
	 * @param consecutivoReciboCajaGenerado the consecutivoReciboCajaGenerado to set
	 */
	public void setConsecutivoReciboCajaGenerado(
			String consecutivoReciboCajaGenerado) {
		this.consecutivoReciboCajaGenerado = consecutivoReciboCajaGenerado;
	}

	/**
	 * @return the consecutivoReciboCajaGenerado
	 */
	public String getConsecutivoReciboCajaGenerado() {
		return consecutivoReciboCajaGenerado;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * @return the nombreArchivoGeneradoCopia
	 */
	public String getNombreArchivoGeneradoCopia() {
		return nombreArchivoGeneradoCopia;
	}

	/**
	 * @param nombreArchivoGeneradoCopia the nombreArchivoGeneradoCopia to set
	 */
	public void setNombreArchivoGeneradoCopia(String nombreArchivoGeneradoCopia) {
		this.nombreArchivoGeneradoCopia = nombreArchivoGeneradoCopia;
	}

	//MT6142
	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	
	/**
	 *
	 * 
	 */
	public void setMapaDetalleFactura(HashMap mapaDetalleFactura)
	{
		this.mapaDetalleFactura= mapaDetalleFactura;
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleFactura(String key) 
	{
		return mapaDetalleFactura.get(key);
	}
	//MT fin
	
	
}
