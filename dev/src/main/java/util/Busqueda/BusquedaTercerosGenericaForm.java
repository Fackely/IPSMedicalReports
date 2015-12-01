package util.Busqueda;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

public class BusquedaTercerosGenericaForm extends ValidatorForm{
	
	
	/**
	 * numero de identificacion de tercero
	 */
	private String nit;
	
	/**
	 * descripcion de tercero
	 */
	private String descripcionTercero;
	
	
	/**
	 * Estado referente a la accion realizable
	 */
	private String estado;
	
	
	/**
	 * Numero de resultados o de registros despues de una consulta
	 *
	 */
	private int numeroResultados;
	
	
	/**
	 * Mapa de recepcion de resultados
	 *
	 */
	private HashMap<String, Object> mapaTerceros;
	
	
	/**
	 * 
	 *atributo de filtrado por el estado del campo activo
	 */
	private String filtrarXEstadoActivo;
	
	
	/**
	 * atributo de filtrado de consulta por terceros no relacionados o relacionados en la tabla empresa
	 */
	private String filtrarXRelacionEmpresa;
	
	/**
	 * atributo de filtrado de consulta por terceros que no sean o que sean deudores
	 */
	private String filtrarXDeudor;
	
    /**
     * Atributo para filtrar por razon social de las empresas
     */
    private String  razonSocial ="";
    
    /**
     * Atributo para filtrar por el tipo de tercero.
     */
    private String  tipoTercero ="";
	
    /**
     * Este atributo indica si se muestran los demas campos de la busqueda generica.
     * su valos puede se "S" o "N"
     * 
     */
    private String mostrarTodosCampos ="";
	/**
	 * atributos de paginado
	 */
	private String indice;
	private String ultimoIndice;
	
	private String linkSiguiente;
	
	private String esEmpresa ="";
	private ArrayList<HashMap<String, Object>> listTipoTer = new ArrayList<HashMap<String,Object>>();
	
	//**********ATRIBUTOS PARA LLENAR HIDDENS DE LA BUSQUEDA GENERICA******************************
	private String idHiddenCodigo;
	private String idHiddenDescripcion;
	private String idDiv;
	private String funcionEliminar="";
	private boolean hacerSubmit;
	//**********************************************************************************************
	
	public BusquedaTercerosGenericaForm()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.descripcionTercero = "";
		this.nit = "";
		this.estado = "";
		this.numeroResultados = 0;
		this.mapaTerceros = new HashMap<String, Object>();
		this.filtrarXEstadoActivo = "";
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		//this.filtrarXRelacionEmpresa = "";
		//this.filtrarXDeudor = "";
	     this.razonSocial="";
         this.mostrarTodosCampos=ConstantesBD.acronimoNo;
         this.listTipoTer = new ArrayList<HashMap<String,Object>>();
         this.esEmpresa="";
   }

	/**
	 * @return the descripcionTercero
	 */
	public String getDescripcionTercero() {
		return descripcionTercero;
	}

	/**
	 * @param descripcionTercero the descripcionTercero to set
	 */
	public void setDescripcionTercero(String descripcionTercero) {
		this.descripcionTercero = descripcionTercero;
	}

	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
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
	 * @return the numeroResultados
	 */
	public int getNumeroResultados() {
		return numeroResultados;
	}

	/**
	 * @param numeroResultados the numeroResultados to set
	 */
	public void setNumeroResultados(int numeroResultados) {
		this.numeroResultados = numeroResultados;
	}

	/**
	 * @return the mapaTerceros
	 */
	public HashMap<String, Object> getMapaTerceros() {
		return mapaTerceros;
	}

	/**
	 * @param mapaTerceros the mapaTerceros to set
	 */
	public void setMapaTerceros(HashMap<String, Object> mapaTerceros) {
		this.mapaTerceros = mapaTerceros;
	}
	
	/**
	 * @param mapaTerceros the mapaTerceros to set
	 */
	public void setMapaTerceros(String key, Object obj) {
		this.mapaTerceros.put(key, obj);
	}
	
	
	
	/**
	 * @return key from mapaTerceros
	 */
	public Object getMapaTerceros(String key) {
		return mapaTerceros.get(key);
	}

	/**
	 * @return the filtrarXEstadoActivo
	 */
	public String getFiltrarXEstadoActivo() {
		return filtrarXEstadoActivo;
	}

	/**
	 * @param filtrarXEstadoActivo the filtrarXEstadoActivo to set
	 */
	public void setFiltrarXEstadoActivo(String filtrarXEstadoActivo) {
		this.filtrarXEstadoActivo = filtrarXEstadoActivo;
	}
	
	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
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
	 * @return the filtrarXRelacionEmpresa
	 */
	public String getFiltrarXRelacionEmpresa() {
		if(UtilidadTexto.isEmpty(filtrarXRelacionEmpresa))
			return "t";
		return filtrarXRelacionEmpresa;
	}

	/**
	 * @param filtrarXRelacionEmpresa the filtrarXRelacionEmpresa to set
	 */
	public void setFiltrarXRelacionEmpresa(String filtrarXRelacionEmpresa) {
		this.filtrarXRelacionEmpresa = filtrarXRelacionEmpresa;
	}

	/**
	 * @return the filtrarXDeudor
	 */
	public String getFiltrarXDeudor() {
		if(UtilidadTexto.isEmpty(filtrarXDeudor))
			return "t";
		return filtrarXDeudor;
	}

	/**
	 * @param filtrarXDeudor the filtrarXDeudor to set
	 */
	public void setFiltrarXDeudor(String filtrarXDeudor) {
		this.filtrarXDeudor = filtrarXDeudor;
	}

	
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTipoTercero() {
		return tipoTercero;
	}

	public void setTipoTercero(String tipoTercero) {
		this.tipoTercero = tipoTercero;
	}

	public String getMostrarTodosCampos() {
		return mostrarTodosCampos;
	}

	public void setMostrarTodosCampos(String mostrarTodosCampos) {
		this.mostrarTodosCampos = mostrarTodosCampos;
	}

	public ArrayList<HashMap<String, Object>> getListTipoTer() {
		return listTipoTer;
	}

	public void setListTipoTer(ArrayList<HashMap<String, Object>> listTipoTer) {
		this.listTipoTer = listTipoTer;
	}

	public String getEsEmpresa() {
		return esEmpresa;
	}

	public void setEsEmpresa(String esEmpresa) {
		this.esEmpresa = esEmpresa;
	}

	/**
	 * @return the idHiddenCodigo
	 */
	public String getIdHiddenCodigo() {
		return idHiddenCodigo;
	}

	/**
	 * @param idHiddenCodigo the idHiddenCodigo to set
	 */
	public void setIdHiddenCodigo(String idHiddenCodigo) {
		this.idHiddenCodigo = idHiddenCodigo;
	}

	/**
	 * @return the idHiddenDescripcion
	 */
	public String getIdHiddenDescripcion() {
		return idHiddenDescripcion;
	}

	/**
	 * @param idHiddenDescripcion the idHiddenDescripcion to set
	 */
	public void setIdHiddenDescripcion(String idHiddenDescripcion) {
		this.idHiddenDescripcion = idHiddenDescripcion;
	}

	/**
	 * @return the idDiv
	 */
	public String getIdDiv() {
		return idDiv;
	}

	/**
	 * @param idDiv the idDiv to set
	 */
	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}

	/**
	 * @return the funcionEliminar
	 */
	public String getFuncionEliminar() {
		return funcionEliminar;
	}

	/**
	 * @param funcionEliminar the funcionEliminar to set
	 */
	public void setFuncionEliminar(String funcionEliminar) {
		this.funcionEliminar = funcionEliminar;
	}

	/**
	 * @return the hacerSubmit
	 */
	public boolean isHacerSubmit() {
		return hacerSubmit;
	}

	/**
	 * @param hacerSubmit the hacerSubmit to set
	 */
	public void setHacerSubmit(boolean hacerSubmit) {
		this.hacerSubmit = hacerSubmit;
	}
}
