package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;

public class ConsultaTarifasArticulosForm extends ValidatorForm 
{

	
	private String estado;
	
	private String codigoArticulo;
	
	private String descripcionArticulo;
	
	private String codigoInterfaz;
	
	private String clase;
	
	private String grupo;
	
	private String subgrupo;
	
	private String naturaleza;
	
	private HashMap<String, Object> mapaListadoArticulos;
	
	private HashMap<String, Object> mapaDetalleArticulos;
	
	private String articulo;
	
	private int indiceDetalle;
	
	private String patronOrdenar;
	
	private String ultimoPatron;
	
	/**
	 * Array list que contendrá la informacion de los esquemas tarifarios
	 */
	
	private ArrayList<HashMap<String, Object>> arrayEsquemaTarifario = new ArrayList<HashMap<String,Object>>(); 
	
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
	
    /**
     * esqTar almacena el esquema tarifario
     */
    private String esqTar="";
    /**
     * Aqui se almacena el tipo de codigo
     */
    private String tipoCod="";
    /**
     * Aqui se almacena el tipo de reporte
     */
    private String tipoReport="";
    
    private String tipoSalida="";
    	
    
    private boolean operacionTrue=false;
    private boolean existeArchivo=false;
	private String urlArchivo=""; 
	private String ruta="";


public void reset_archivo()
{
	this.existeArchivo=false;
	this.operacionTrue=false;
	this.urlArchivo="";
	this.ruta="";
}

public void resetcriterios ()
{
	this.setArrayEsquemaTarifario(new ArrayList<HashMap<String,Object>>());
}
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.maxPageItems = 20;
        this.linkSiguiente = "";
    	this.estado="";
		this.codigoArticulo="";
		this.descripcionArticulo="";
		this.codigoInterfaz="";
		this.clase="";
		this.grupo="";
		this.subgrupo="";
		this.naturaleza="";
		this.mapaListadoArticulos= new HashMap<String, Object>();
		this.mapaDetalleArticulos= new HashMap<String, Object>();
		this.articulo="";
		this.indiceDetalle=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.setEsqTar("");
		this.setTipoCod("");
		this.setTipoReport("");
		this.tipoSalida="";
		reset_archivo();
	}
	
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("buscar"))
		{
		
			if (UtilidadCadena.noEsVacio(this.tipoReport) &&  !this.tipoReport.equals(ConstantesBD.codigoNuncaValido+""))
			{
				if (this.tipoReport.equals(ConstantesIntegridadDominio.acronimoTipoReporteAnalisisCosto))
				{
				
					if (!UtilidadCadena.noEsVacio(this.esqTar) || this.esqTar.equals(ConstantesBD.codigoNuncaValido+""))
						errores.add("codigo", new ActionMessage("errors.required","El esquema tarifario "));
					
					if (!UtilidadCadena.noEsVacio(this.tipoCod) || this.tipoCod.equals(ConstantesBD.codigoNuncaValido+""))
						errores.add("codigo", new ActionMessage("errors.required","El tipo codigo "));
									
				}
				else
					if((!UtilidadCadena.noEsVacio(this.esqTar) || this.esqTar.equals(ConstantesBD.codigoNuncaValido+"")) && !UtilidadCadena.noEsVacio(this.codigoArticulo) && !UtilidadCadena.noEsVacio(this.descripcionArticulo) && !UtilidadCadena.noEsVacio(this.codigoInterfaz) && this.clase.equals("0") && this.grupo.equals("0")&&this.subgrupo.equals("0") && !UtilidadCadena.noEsVacio(this.naturaleza))
						errores.add("codigo", new ActionMessage("errors.required","Por lo menos uno de los campos "));
			}
			else
				errores.add("codigo", new ActionMessage("errors.required","El tipo de reporte"));
			
			
			if (!UtilidadCadena.noEsVacio(this.tipoSalida) || this.tipoSalida.equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("codigo", new ActionMessage("errors.required","El tipo de salida"));
		}
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
	public String getClase() {
		return clase;
	}
	
	/**
	 * 
	 * @param clase
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	
	/**
	 * 
	 * @param codigoArticulo
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}
	
	/**
	 * 
	 * @param codigoInterfaz
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	
	/**
	 * 
	 * @param descripcionArticulo
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGrupo() {
		return grupo;
	}
	
	/**
	 * 
	 * @param grupo
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNaturaleza() {
		return naturaleza;
	}
	
	/**
	 * 
	 * @param naturaleza
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSubgrupo() {
		return subgrupo;
	}
	
	/**
	 * 
	 * @param subgrupo
	 */
	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaListadoArticulos() {
		return mapaListadoArticulos;
	}

	/**
	 * 
	 * @param mapaListadoArticulos
	 */
	public void setMapaListadoArticulos(HashMap<String, Object> mapaListadoArticulos) {
		this.mapaListadoArticulos = mapaListadoArticulos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaListadoArticulos(String key) 
	{
		return mapaListadoArticulos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setMapaListadoArticulos(String key,Object value) 
	{
		this.mapaListadoArticulos.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * 
	 * @param articulo
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaDetalleArticulos() {
		return mapaDetalleArticulos;
	}

	/**
	 * 
	 * @param mapaDetalleArticulos
	 */
	public void setMapaDetalleArticulos(HashMap<String, Object> mapaDetalleArticulos) {
		this.mapaDetalleArticulos = mapaDetalleArticulos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleArticulos(String key) 
	{
		return mapaDetalleArticulos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setMapaDetalleArticulos(String key,Object value) 
	{
		this.mapaDetalleArticulos.put(key, value);
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
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}



	public void setArrayEsquemaTarifario(ArrayList<HashMap<String, Object>> arrayEsquemaTarifario) {
		this.arrayEsquemaTarifario = arrayEsquemaTarifario;
	}



	public ArrayList<HashMap<String, Object>> getArrayEsquemaTarifario() {
		return arrayEsquemaTarifario;
	}



	public void setEsqTar(String esqTar) {
		this.esqTar = esqTar;
	}


	public String getEsqTar() {
		return esqTar;
	}



	public void setTipoCod(String tipoCod) {
		this.tipoCod = tipoCod;
	}



	public String getTipoCod() {
		return tipoCod;
	}



	public void setTipoReport(String tipoReport) {
		this.tipoReport = tipoReport;
	}



	public String getTipoReport() {
		return tipoReport;
	}
	
	
  
    
	public String getTipoSalida() {
		return tipoSalida;
	}



	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	public boolean isOperacionTrue() {
		return operacionTrue;
	}



	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}



	public boolean isExisteArchivo() {
		return existeArchivo;
	}



	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}

	

	public String getUrlArchivo() {
		return urlArchivo;
	}



	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}



	public String getRuta() {
		return ruta;
	}



	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	//------------criterios de busqueda--------------------------

	

}