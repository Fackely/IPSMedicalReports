package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

public class ConsultarImpFacAudiForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultarImpFacAudiForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Variable para consultar convenios
	 */
	private String codigoConvenio;
	
	/**
	 * Variable para consultar contratos
	 */	
	private int codigoContrato;
	
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Arreglo para almacenar los Contratos
	 */
	private ArrayList<HashMap<String, Object>> arregloContratos = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Variable para almacenar el convenio seleccionado
	 */
	private String convenio;

	/**
	 * Variable para el manejo de factura Inicial
	 */
	
	private String facturaInicial;
	
	/**
	 * Variable para el manejo de factura Final
	 */
	private String facturaFinal;
	
	/**
	 * Variable para el manejo de fecha Inicial de Auditoria
	 */
	private String fechaAuditoriaInicial;
	
	/**
	 * Variable para el manejo de fecha Final de Auditoria
	 */
	private String fechaAuditoriaFinal;
	
	/**
	 * Variable para el manejo del numero de glosa dado por el sistema
	 */
	private String numeroPreGlosa;
	
	/**
	 * Campo que verifica si se realizó una busqueda
	 */
	private boolean realizoBusqueda;
	
	/**
	 * Mapa que maneja el listado de las facturas
	 */
	private HashMap<String, Object> listadoFacturas = new HashMap<String, Object>();
	
	/**
	 * Variable que almacena el numero de mapas en el arraylist
	 */
	private int numMapas;

	/**
	 * Variable para manejo del maximo de registros por pagina
	 */
	private int maxPageItems; 
	
	//Atributos para el manejo de la ordenacion/paginacion
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
		
	
	//para la impresion de los reportes
	private String tipoSalida=""; 
	
	private String ruta="";
	private String urlArchivo="";
	private boolean existeArchivo=false;
	
	private boolean operacionTrue = false;
	
	private String codFactura;
	
	private HashMap consultaDetFacMap = new HashMap();
	
	private String tipoReporte;
	
	private String criteriosConsulta;	
	
	private TreeSet<String> agrupamientoFacturas;
	
	private String codigoGlosa;
	
	/**
	 * Metodo que resetea las variables de la Forma
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.convenio="";
		this.codigoConvenio = "";
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.arregloContratos = new ArrayList<HashMap<String,Object>>();
		this.fechaAuditoriaInicial="";
		this.fechaAuditoriaFinal="";
		this.facturaInicial="";
		this.facturaFinal="";
		this.codigoContrato=0;
		this.indice="";
		this.numeroPreGlosa="";
		this.listadoFacturas=new HashMap();
		this.listadoFacturas.put("numRegistros", "0");
		this.numMapas=0;
		this.tipoSalida="";
		this.ruta="";
		this.urlArchivo="";
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.codFactura="";
		this.consultaDetFacMap = new HashMap();
		this.consultaDetFacMap.put("numRegistros", "0");
		this.tipoReporte="";
		this.maxPageItems = 10;
		this.linkSiguiente="";
		this.indice = "";
		this.ultimoIndice = "";
		this.criteriosConsulta="";
		this.agrupamientoFacturas= new TreeSet<String>();
		this.codigoGlosa="";
	}
	
	
	
	/**
	 * @return the ruta
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	/**
	 * @return the urlArchivo
	 */
	public String getUrlArchivo() {
		return urlArchivo;
	}

	/**
	 * @param urlArchivo the urlArchivo to set
	 */
	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}

	/**
	 * @return the existeArchivo
	 */
	public boolean isExisteArchivo() {
		return existeArchivo;
	}

	/**
	 * @param existeArchivo the existeArchivo to set
	 */
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}

	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}

	public ArrayList<HashMap<String, Object>> getArregloContratos() {
		return arregloContratos;
	}

	public void setArregloContratos(
			ArrayList<HashMap<String, Object>> arregloContratos) {
		this.arregloContratos = arregloContratos;
	}

	public int getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public String getFacturaInicial() {
		return facturaInicial;
	}

	public void setFacturaInicial(String facturaInicial) {
		this.facturaInicial = facturaInicial;
	}

	public String getFacturaFinal() {
		return facturaFinal;
	}

	public void setFacturaFinal(String facturaFinal) {
		this.facturaFinal = facturaFinal;
	}

	public String getFechaAuditoriaInicial() {
		return fechaAuditoriaInicial;
	}

	public void setFechaAuditoriaInicial(String fechaAuditoriaInicial) {
		this.fechaAuditoriaInicial = fechaAuditoriaInicial;
	}

	public HashMap<String, Object> numReg() {
		return listadoFacturas;
	}

	public void setListadoFacturas(HashMap<String, Object> listadoFacturas) {
		this.listadoFacturas = listadoFacturas;
	}

	public Object getListadoFacturas(String key) {
		return listadoFacturas.get(key);
	}
	
	public void setListadoFacturas(String key, Object value) {
		this.listadoFacturas.put(key, value);
	}
	
	public boolean isRealizoBusqueda() {
		return realizoBusqueda;
	}

	public void setRealizoBusqueda(boolean realizoBusqueda) {
		this.realizoBusqueda = realizoBusqueda;
	}

	public String getFechaAuditoriaFinal() {
		return fechaAuditoriaFinal;
	}

	public void setFechaAuditoriaFinal(String fechaAuditoriaFinal) {
		this.fechaAuditoriaFinal = fechaAuditoriaFinal;
	}

	public String getNumeroPreGlosa() {
		return numeroPreGlosa;
	}

	public void setNumeroPreGlosa(String numeroPreGlosa) {
		this.numeroPreGlosa = numeroPreGlosa;
	}
	
	/**
	 * Método para obtener el numero de registros del listado de facturas
	 * @return
	 */
	public int getNumListadoFacturas()
	{
		return Utilidades.convertirAEntero(this.getListadoFacturas("numRegistros")+"", true);
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getUltimoIndice() {
		return ultimoIndice;
	}

	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public HashMap<String, Object> getListadoFacturas() {
		return listadoFacturas;
	}
	
	/**
	 * 
	 * @return
	 */
	public TreeSet<String> getAgrupamientoFacturas()
	{
		for (int i = 0; i < this.getNumListadoFacturas(); i++) 
		{
			this.agrupamientoFacturas.add(this.getListadoFacturas("nombreConvenio_"+i)+"");
		}
		return this.agrupamientoFacturas;
	}
	
	public int getNumMapas()
	{
		return arregloConvenios.size();
	}

	/**
	 * @return the codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the operacionTrue
	 */
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	/**
	 * @param operacionTrue the operacionTrue to set
	 */
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	
	/**
	 * @return the codFactura
	 */
	public String getCodFactura() {
		return codFactura;
	}

	/**
	 * @param codFactura the codFactura to set
	 */
	public void setCodFactura(String codFactura) {
		this.codFactura = codFactura;
	}

	/**
	 * @return the consultaDetFacMap
	 */
	public HashMap getConsultaDetFacMap() {
		return consultaDetFacMap;
	}
	
	/**
	 * @param consultaDetFacMap the consultaDetFacMap to set
	 */
	public void setConsultaDetFacMap(HashMap consultaDetFacMap) {
		this.consultaDetFacMap = consultaDetFacMap;
	}
	
	public Object getConsultaDetFacMap(String key) {
		return consultaDetFacMap.get(key);
	}
	
	public void setConsultaDetFacMap(String key, Object value) {
		this.consultaDetFacMap.put(key, value);
	}

	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @return the criteriosConsulta
	 */
	public String getCriteriosConsulta() {
		return criteriosConsulta;
	}

	/**
	 * @param criteriosConsulta the criteriosConsulta to set
	 */
	public void setCriteriosConsulta(String criteriosConsulta) {
		this.criteriosConsulta = criteriosConsulta;
	}



	/**
	 * @return the codigoGlosa
	 */
	public String getCodigoGlosa() {
		return codigoGlosa;
	}



	/**
	 * @param codigoGlosa the codigoGlosa to set
	 */
	public void setCodigoGlosa(String codigoGlosa) {
		this.codigoGlosa = codigoGlosa;
	}



	/**
	 * @param agrupamientoFacturas the agrupamientoFacturas to set
	 */
	public void setAgrupamientoFacturas(TreeSet<String> agrupamientoFacturas) {
		this.agrupamientoFacturas = agrupamientoFacturas;
	}



	/**
	 * ################################################
	 * METODO PARA LA VALIDACION CON EL VALIDATE
	 * ###############################################
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        String fecha=UtilidadFecha.getFechaActual();
        String fechaaux;
        
        if(this.estado.equals("buscar"))
        {
			if(!this.fechaAuditoriaFinal.equals("") && !this.fechaAuditoriaInicial.equals(""))
			{
				if(!UtilidadFecha.compararFechas(this.fechaAuditoriaFinal.toString(), "00:00", this.fechaAuditoriaInicial.toString(), "00:00").isTrue())
				{
					errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Auditoria Inicial "+this.fechaAuditoriaInicial.toString()+" mayor a la Fecha Auditoria Final "+this.fechaAuditoriaFinal.toString()+" "));
				}
				else
				{    					
					if(!UtilidadFecha.compararFechas(fecha, "00:00", this.fechaAuditoriaInicial.toString(), "00:00").isTrue())
					 	errores.add("descripcion",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Auditoria Inicial "+this.fechaAuditoriaInicial.toString(),fecha+" "));
					
					if(!UtilidadFecha.compararFechas(fecha, "00:00", this.fechaAuditoriaFinal.toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Auditoria Final "+this.fechaAuditoriaFinal.toString(),fecha+""));
					
					fechaaux=UtilidadFecha.incrementarDiasAFecha(this.fechaAuditoriaInicial.toString(), 180, false);
					
					if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.fechaAuditoriaFinal.toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha inicial y Fecha final supera los 180 días por lo tanto el rango elegido"));
				}
			}
        	if((this.facturaInicial.equals("") && !this.facturaFinal.equals("")) || (!this.facturaInicial.equals("") && this.facturaFinal.equals("")))
        		errores.add("descripcion",new ActionMessage("errors.required","La Factura Inicial y la Factura Final"));
        	if(Utilidades.convertirAEntero(this.facturaInicial) > Utilidades.convertirAEntero(this.facturaFinal))
        		errores.add("descripcion",new ActionMessage("prompt.generico","El número de Factura Final debe ser mayor o igual al número de Factura Inicial."));
        	if(!this.facturaInicial.equals(""))
        	{
        		if(Utilidades.convertirAEntero(this.facturaInicial) <= 0)
        			errores.add("descripcion",new ActionMessage("prompt.generico","El número de Factura Inicial debe ser mayor a cero."));
        	}
        	if(!this.facturaFinal.equals(""))
        	{
        		if(Utilidades.convertirAEntero(this.facturaFinal) <= 0)
        			errores.add("descripcion",new ActionMessage("prompt.generico","El número de Factura Final debe ser mayor a cero."));
        	}
        }
        
        return errores;
    }	
}