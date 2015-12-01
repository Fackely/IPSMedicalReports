package com.princetonsa.actionform.facturacion;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

public class ConsultaTarifasForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private boolean mostrarInfo;
	
	
	/**
	 * 
	 */
	private HashMap consultaTarifasMap;
	
	/**
	 * 
	 */
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	
	 private int offset;
	 
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	    
	 private int posEliminar;
	 
	 /**
	  * 
	  */
	 private String tiposTarifarios;
	 
	 /**
	  * 
	  */
	 private String esquemasTarifarios;
	 
	 /**
	  * 
	  */
	 private HashMap<String,Object> esquemasVigencias; 
	 
	 
	 //******** Busqueda Servicios
	 private String codigo;
	 private String descripcion;
	 private String especialidad;
	 private String tipoServicio;
	 private String grupo;
	 private HashMap resultados;
	 
	 //************** Busqueda Articulos
	private String codigoArticulo;
	private String descripcionArticulo;
	private String clase;
	private String grupoArticulo;
	private String subgrupo;
	private String naturaleza;
	private HashMap articulos;
	
	//************** Consulta Detalle Servicios
	
	private Collection consultaServicio;
	
	private String fechaAsignacion;
	private String tipoLiquidacion;
	private String valorTarifa;
	private String usuarioAsigna;
	private String tipoCambios;
	private String esquemaTarifario;
	private String tipoRedondeo;
	private String servicio;
	private String liquidarAsocios;
	
	private HashMap consultaServiciosMap;
	
	private int indexSeleccionado;
	
	
	//************* Consulta Detalle Articulo
	
	private Collection consultaArticulo;
	
	private String fecha;
	private String tipoTarifa;
	private String porcentaje;
	private String valor;
	private String usuario;
	private String cambio;
	private String esquema;
	private String articulo;
	private String redondeo;
	
	private HashMap consultaArticulosMap;
	
	private int codigoServicio;
	
	private int codigoArti;
	
	private String nombreEsquemaTarifario;
	
	private int tarifarioOficial;
	
	/**
	 * 
	 */
	private int contrato;
	
	
	/**
	 * 
	 */
	private int codigoConvenio;
	
	
	/**
	  * 
	  */
	 private ArrayList convenios;
	 
	 /**
	  * 
	  */
	 private ArrayList contratos;
	 
	 /**
	  * 
	  */
	 private String consultaPorServicio;
	 
	 /**
	  * 
	  */
	 private String fechaVigencia;
	 
	
	 /**
	  *  Resetea los Atributos del Form.
	  *
	  */
	 public void reset()
	 {
		 consultaTarifasMap= new HashMap();
		 consultaTarifasMap.put("numRegistros", "0");
		 
		 consultaServiciosMap=new HashMap();
		 consultaServiciosMap.put("numRegistros", "0");
		 
		 consultaArticulosMap=new HashMap();
		 consultaArticulosMap.put("numRegistros", "0");
		 
		 resultados = new HashMap();
		 resultados.put("numRegistros", "0");
		 
		 articulos = new HashMap();
		 articulos.put("numRegistros", "0");
		 
		 this.tiposTarifarios=ConstantesBD.codigoNuncaValido+"";
		 this.esquemasTarifarios=ConstantesBD.codigoNuncaValido+"";
		 this.esquemasVigencias=new HashMap<String, Object>();
		 this.esquemasVigencias.put("numRegistros","0");
		 linkSiguiente="";this.maxPageItems=10;
		 this.patronOrdenar="";
		 this.ultimoPatron="";
		 this.offset=0;
		 this.posEliminar=ConstantesBD.codigoNuncaValido;
		 
		 this.codigo="";
		 this.descripcion="";
		 this.especialidad="";
		 this.tipoServicio="";
		 this.grupo="";
		 
		 this.codigoArticulo="";
		 this.descripcionArticulo="";
		 this.clase="";
		 this.grupoArticulo="";
		 this.subgrupo="";
		 this.naturaleza="";
		 
		 this.codigoServicio=ConstantesBD.codigoNuncaValido;
		 this.codigoArti=ConstantesBD.codigoNuncaValido;
		 
		 this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
		 this.nombreEsquemaTarifario="";
		 this.tarifarioOficial=ConstantesBD.codigoNuncaValido;
		 
		 this.liquidarAsocios = "";
		 
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.convenios=new ArrayList();
    	this.contratos=new ArrayList();
    	this.mostrarInfo=false;
    	
    	this.consultaPorServicio="";
    	this.fechaVigencia="";
	 }

	/**
	* 
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        if(this.estado.equals("cargarInfo"))
        {
        	if(codigoConvenio<=0)
        	{
        		errores.add("convenio",new ActionMessage("errors.required","El Convenio "));
        	}
        	if(contrato<=0)
        	{
        		errores.add("contrato",new ActionMessage("errors.required","El Contrato "));
        	}
        	if(UtilidadTexto.getBoolean(consultaPorServicio))
        	{
        		if(Utilidades.convertirAEntero(esquemasTarifarios)<0)
        		{
        			errores.add("esquemaTarifario",new ActionMessage("errors.required","El esquema tarifario "));
        		}
        	}
        	this.mostrarInfo=errores.isEmpty();
        	if(!mostrarInfo)
        	{
        		this.consultaPorServicio="";
        	}
        }
        return errores;
    }
	 

	/**
	 * 
	 * @return
	 */ 
	public HashMap getConsultaTarifasMap() {
		return consultaTarifasMap;
	}


	/**
	 * 
	 * @param consultaTarifasMap
	 */
	public void setConsultaTarifasMap(HashMap consultaTarifasMap) {
		this.consultaTarifasMap = consultaTarifasMap;
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
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	/**
	 * 
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}


	/**
	 * 
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	/**
	 * 
	 * @return
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
	public int getPosEliminar() {
		return posEliminar;
	}


	/**
	 * 
	 * @param posEliminar
	 */
	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
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
	 * 
	 * @return
	 */
	public String getTiposTarifarios() {
		return tiposTarifarios;
	}

	/**
	 * 
	 * @param tiposTarifarios
	 */
	public void setTiposTarifarios(String tiposTarifarios) {
		this.tiposTarifarios = tiposTarifarios;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquemasTarifarios() {
		return esquemasTarifarios;
	}

	/**
	 * 
	 * @param esquemasTarifarios
	 */
	public void setEsquemasTarifarios(String esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
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
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * 
	 * @param especialidad
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
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
	public String getGrupoArticulo() {
		return grupoArticulo;
	}

	/**
	 * 
	 * @param grupoArticulo
	 */
	public void setGrupoArticulo(String grupoArticulo) {
		this.grupoArticulo = grupoArticulo;
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
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * 
	 * @param tipoServicio
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * 
	 * @param codigo
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	public String getCambio() {
		return cambio;
	}

	/**
	 * 
	 * @param cambio
	 */
	public void setCambio(String cambio) {
		this.cambio = cambio;
	}

	/**
	 * 
	 * @return
	 */
	public Collection getConsultaArticulo() {
		return consultaArticulo;
	}

	/**
	 * 
	 * @param consultaArticulo
	 */
	public void setConsultaArticulo(Collection consultaArticulo) {
		this.consultaArticulo = consultaArticulo;
	}

	/**
	 * 
	 * @return
	 */
	public Collection getConsultaServicio() {
		return consultaServicio;
	}

	/**
	 * 
	 * @param consultaServicio
	 */
	public void setConsultaServicio(Collection consultaServicio) {
		this.consultaServicio = consultaServicio;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquema() {
		return esquema;
	}

	/**
	 * 
	 * @param esquema
	 */
	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * 
	 * @param esquemaTarifario
	 */
	public void setEsquemaTarifario(String esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * 
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * 
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaAsignacion() {
		return fechaAsignacion;
	}

	/**
	 * 
	 * @param fechaAsignacion
	 */
	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getPorcentaje() {
		return porcentaje;
	}

	/**
	 * 
	 * @param porcentaje
	 */
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * 
	 * @return
	 */
	public String getRedondeo() {
		return redondeo;
	}

	/**
	 * 
	 * @param redondeo
	 */
	public void setRedondeo(String redondeo) {
		this.redondeo = redondeo;
	}

	/**
	 * 
	 * @return
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * 
	 * @param servicio
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoCambios() {
		return tipoCambios;
	}

	/**
	 * 
	 * @param tipoCambios
	 */
	public void setTipoCambios(String tipoCambios) {
		this.tipoCambios = tipoCambios;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoLiquidacion() {
		return tipoLiquidacion;
	}

	/**
	 * 
	 * @param tipoLiquidacion
	 */
	public void setTipoLiquidacion(String tipoLiquidacion) {
		this.tipoLiquidacion = tipoLiquidacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoRedondeo() {
		return tipoRedondeo;
	}

	/**
	 * 
	 */
	public void setTipoRedondeo(String tipoRedondeo) {
		this.tipoRedondeo = tipoRedondeo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}

	/**
	 * 
	 * @param tipoTarifa
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuarioAsigna() {
		return usuarioAsigna;
	}

	/**
	 * 
	 * @param usuarioAsigna
	 */
	public void setUsuarioAsigna(String usuarioAsigna) {
		this.usuarioAsigna = usuarioAsigna;
	}

	/**
	 * 
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * 
	 * @return
	 */
	public String getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * 
	 * @param valorTarifa
	 */
	public void setValorTarifa(String valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getConsultaArticulosMap() {
		return consultaArticulosMap;
	}

	/**
	 * 
	 * @param consultaArticulosMap
	 */
	public void setConsultaArticulosMap(HashMap consultaArticulosMap) {
		this.consultaArticulosMap = consultaArticulosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getConsultaArticulosMap(String key) {
		return consultaArticulosMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param Value
	 */
	public void setConsultaArticulosMap(String key, Object Value) {
		this.consultaArticulosMap.put(key,Value);
	}
	

	/**
	 * 
	 * @return
	 */
	public HashMap getConsultaServiciosMap() {
		return consultaServiciosMap;
	}

	/**
	 * 
	 * @param consultaServiciosMap
	 */
	public void setConsultaServiciosMap(HashMap consultaServiciosMap) {
		this.consultaServiciosMap = consultaServiciosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getConsultaServiciosMap(String key) {
		return consultaServiciosMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param Value
	 */
	public void setConsultaServiciosMap(String key, Object Value) {
		this.consultaServiciosMap.put(key,Value);
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoArti() {
		return codigoArti;
	}

	/**
	 * 
	 * @param codigoArti
	 */
	public void setCodigoArti(int codigoArti) {
		this.codigoArti = codigoArti;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * 
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}

	/**
	 * 
	 * @param indexSeleccionado
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getResultados() {
		return resultados;
	}

	/**
	 * 
	 * @param resultados
	 */
	public void setResultados(HashMap resultados) {
		this.resultados = resultados;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getResultados(String key) {
		return resultados.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param Value
	 */
	public void setResultados(String key, Object Value) {
		this.resultados.put(key,Value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getArticulos() {
		return articulos;
	}

	/**
	 * 
	 * @param articulos
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getArticulos(String key) {
		return articulos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param Value
	 */
	public void setArticulos(String key, Object Value) {
		this.articulos.put(key,Value);
	}


	/**
	 * @return the nombreEsquemaTarifario
	 */
	public String getNombreEsquemaTarifario() {
		return nombreEsquemaTarifario;
	}


	/**
	 * @param nombreEsquemaTarifario the nombreEsquemaTarifario to set
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}


	/**
	 * @return the tarifarioOficial
	 */
	public int getTarifarioOficial() {
		return tarifarioOficial;
	}


	/**
	 * @param tarifarioOficial the tarifarioOficial to set
	 */
	public void setTarifarioOficial(int tarifarioOficial) {
		this.tarifarioOficial = tarifarioOficial;
	}


	/**
	 * @return the liquidarAsocios
	 */
	public String getLiquidarAsocios() {
		return liquidarAsocios;
	}


	/**
	 * @param liquidarAsocios the liquidarAsocios to set
	 */
	public void setLiquidarAsocios(String liquidarAsocios) {
		this.liquidarAsocios = liquidarAsocios;
	}
		
	

	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio()
	{
		return codigoConvenio;
	}


	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio)
	{
		this.codigoConvenio = codigoConvenio;
	}


	/**
	 * @return the contrato
	 */
	public int getContrato()
	{
		return contrato;
	}


	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato)
	{
		this.contrato = contrato;
	}


	/**
	 * @return the contratos
	 */
	public ArrayList getContratos()
	{
		return contratos;
	}


	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList contratos)
	{
		this.contratos = contratos;
	}


	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios()
	{
		return convenios;
	}


	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios)
	{
		this.convenios = convenios;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMostrarInfo() {
		return mostrarInfo;
	}

	/**
	 * 
	 * @param mostrarInfo
	 */
	public void setMostrarInfo(boolean mostrarInfo) {
		this.mostrarInfo = mostrarInfo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String,Object> getEsquemasVigencias() {
		return esquemasVigencias;
	}

	/**
	 * 
	 * @param esquemasVigencias
	 */
	public void setEsquemasVigencias(HashMap<String,Object> esquemasVigencias) {
		this.esquemasVigencias = esquemasVigencias;
	}

	/**
	 * 
	 * @return
	 */
	public String getConsultaPorServicio() {
		return consultaPorServicio;
	}

	/**
	 * 
	 * @param consultaPorServicio
	 */
	public void setConsultaPorServicio(String consultaPorServicio) {
		this.consultaPorServicio = consultaPorServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * 
	 * @param fechaVigencia
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

		
	/*public Object getConsultaServiciosMap(String key) 
	{
		return consultaServiciosMap.get(key);
	}

	
		
	public void setConsultaServiciosMap(String key,Object value) 
	{
		this.consultaServiciosMap.put(key, value);
	}*/

	 

}
