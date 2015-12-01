/*
 * Ene 16, 2007
 */
package util.Busqueda;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * 
 * @author Sebastián Gómez Rivillas
 * 
 * Form que contiene todos los datos específicos para generar la
 * busqueda de diagnósticos genérica
 *
 */
public class BusquedaDiagnosticosGenericaForm extends ValidatorForm 
{
	/**
    * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(BusquedaDiagnosticosGenericaForm.class);
	
	/**
    * estado de la accion
    */
   private String estado;
   
   //*********ATRIBUTOS PARA LA BUSQUEDA**************************************
   
   /**
    * Criterio de busqueda
    */
   private String criterioBusqueda;
   
   /**
    * Indica si se busca por texto o por código
    */
   private boolean buscarTexto;
   
   /**
    * Indica si se debe verificar epidemiología
    */
   private boolean epidemiologia;
   
   /**
    * Indica si se deben buscar solo diagnosticos principales
    */
   private boolean esPrincipal;
   
   /**
    * Indica si se deben buscar solo diagnosticos de muerte
    */
   private boolean esMuerte;
   
   /**
    * Codigo Cie VIGENTE por el cual se realizará la busqueda
    */
   private String codigoCie;
   
   /**
    * Indica si se debe filtrar por sexo del paciente
    */
   private boolean filtrarSexo;
   
   /**
    * Valor del sexo pasado por parámetro que se desea filtrar
    */
   private String sexo;
   
   /**
    * Indica si se debe filtrar por Edad
    */
   private boolean filtrarEdad;
   
   /**
    * Valor de la edad pasado por parámetro que se desea filtrar
    */
   private String edad;
   
   /**
    * Variable que identifica un tipo de diagnostico
    */
   private int tipoDiagnostico;
   
   /**
    * Variable que guarda el codigo de filtro de la consulta de diagnosticos
    */
   private String codigoFiltro;
   
   /**
    * Atributo usado para la busqueda de diagnosticos en CONSULTA EXTERNA
    */
   private String numSolDx;
   
   /**
    * Objeto donde se almacena el listado de diagnosticos
    */
   private HashMap diagnosticos = new HashMap();
   
   /**
    * Numero de registros en el mapa de diagnosticos
    */
   private int numDiagnosticos;
   
   /**
    * Posicion del mapa del diagnostico seleccionado
    */
   private int pos;
   
   /**
    * Se almacena el valor del maxPageItems
    */
   private int maxPageItems;
   
   /**
    * Variable que se usa para paginar el listado de diagnosticos
    */
   private String linkSiguiente;
   
   /**
    * Variable para el manejo del offset del pager
    */
   private int offset;
   
   /**
    * Indice por el cual se desea ordenar el listado de diagnosticos
    */
   private String indice;
   
   /**
    * Variabel que lleva el registro del último índice ordenado
    */
   private String ultimoIndice;
   
   /**
    * Número de la solicitud a la cual se le grabará diagnostico
    */
   private String numeroSolicitud;
   
   /**
    * Valor del diagnostico seleccionado
    */
   private String valorDiagnostico;
   
   //********ATRIBUTOS PARA ASIGNAR EL RESULTADO EN LA FORMA - VIA HTML ************************
   /**
    * ID de la etiqueta HTML donde recide el diagnostico
    */
   private String idDiagnostico;
   
   /**
    * Nombre de la propiedad Diagnostico de la forma
    */
   private String propiedadDiagnostico;
   
   /**
    * ID de la etiqueta DIV HTML donde se postula el diagnostico
    * seleccionado
    */
   private String idDiv;
   /**
    * ID de la etiqueta CHECKBOX HTML que indica seleccion/deseleccion del diagnostico
    */
   private String idCheckBox;
   
   /**
    * ID de la etiqueta HIDDEN HTML que almacena el estado del CHECKBOX de seleccion/deseleccion del diagnostico
    */
   private String idHiddenCheckBox;
   
   /**
    * Nombre de la propiedad que almacena el estado del CHECKBOX desde la forma
    */
   private String propiedadHiddenCheckBox;
   
   /**
    * Variable que almacena la cuenta de diagnosticos seleccionados
    * (aplica para Dx. Relacionados)
    */
   private int numero;
   
   /**
    * ID de la etiqueta HTML donde recide la cuenta de diagnosticos seleccionados
    * (aplica para Dx. Relacionados)
    */
   private String idNumero;
   
   /**
    * Cadena que lleva el registro de los diagnosticos seleccionados para evitar que
    * se seleccione el mismo diagnostico
    */
   private String diagnosticosSeleccionados;
   
   /**
    * ID de la etiqueta HTML donde recide el registro de los diagnosticos seleccionados
    */
   private String idDiagSeleccionados;
   
   /**
    * ID de la etiqueta HTML donde recide el resultado del proceso de ficha epidemiologica
    * relacionada con el diagnostico
    */
   private String idValorFicha;
   
   /**
    * Variable que determina el siguiente paso a seguir (en epidemiologia) cuando se selecciona un diagnostico.
    * Determina si se abre una nueva ficha o si se muestran las fichas anteriores para el usuario.
    */
   private int estadoEpidemiologia;
   
   /**
    * url para abrir una nueva ficha o para mostrar los resultados de la busqueda de fichas anteriores.
    */
   private String urlEpidemiologia;
   
   /**
    * Variable que indica si se inserto una ficha nueva o si la ficha ya estaba reportada y se decidio
    * no abrir una ficha nueva.  Si su valor es un numero, este indica el codigo de la ficha que se inserto exitosamente;
    * si su valor es la palabra "LOG", quiere decir que no se abrio una ficha nueva y si su valor es un caracter vacio,
    * quiere decir que hubo un error en la insercion.
    */
   private String resultadoInsercionFicha;
   
   /**
    * Por estandarizacion todos los nuevos desarrollos deben ir a busquedaDiagnosticosGenerica/asignarDiagnosticosGenerica.jsp,
    * pero se deja el abierta la asignacion en otro path
    */
   private String pathJavaScriptAsignacion;
   
   /**
    * Campo que activa la fecha de muerte cuando el diagnóstico es de tipo muerte
    */
   private String idFechaMuerte; 
   
   /**
    * Campo que activa la hora de muerte cuando el diagnóstico es de tipo muerte
    */
   private String idHoraMuerte;
   
   /**
    * resetea los valores de la forma
    */
   public void reset()
   {
	   this.estado = "";
	   this.criterioBusqueda = "";
	   this.buscarTexto = false;
	   this.epidemiologia = false;
	   this.esPrincipal = false;
	   this.esMuerte = true;
	   this.codigoCie = "";
	   this.filtrarSexo = false;
	   this.sexo = "";
	   this.filtrarEdad = false;
	   this.edad = "";
	   this.tipoDiagnostico = 0;
	   this.codigoFiltro = "";
	   this.numeroSolicitud = "";
	   this.valorDiagnostico = "";
	   this.diagnosticos = new HashMap();
	   this.numDiagnosticos = 0;
	   this.pos = 0;
	   this.maxPageItems = 0;
	   this.linkSiguiente = "";
	   this.offset = 0;
	   this.indice = "";
	   this.ultimoIndice = "";
	   this.numSolDx = "";
	   
	   this.idDiagnostico = "";
	   this.propiedadDiagnostico = "";
	   this.idDiv = "";
	   this.idCheckBox = "";
	   this.idHiddenCheckBox = "";
	   this.propiedadHiddenCheckBox = "";
	   this.numero = 0;
	   this.idNumero = "";
	   this.diagnosticosSeleccionados = "";
	   this.idDiagSeleccionados = "";
	   this.idValorFicha = "";
	   
	   this.estadoEpidemiologia = 0;
	   this.urlEpidemiologia = "";
	   this.resultadoInsercionFicha = "";
	   this.pathJavaScriptAsignacion="";
	   
	   this.idFechaMuerte = "";
	   this.idHoraMuerte = "";
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
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
   {
       ActionErrors errores= new ActionErrors();
       return errores;
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
	 * @return the epidemiologia
	 */
	public boolean isEpidemiologia() {
		return epidemiologia;
	}

	/**
	 * @param epidemiologia the epidemiologia to set
	 */
	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}

	/**
	 * @return the esMuerte
	 */
	public boolean isEsMuerte() {
		return esMuerte;
	}

	/**
	 * @param esMuerte the esMuerte to set
	 */
	public void setEsMuerte(boolean esMuerte) {
		this.esMuerte = esMuerte;
	}

	/**
	 * @return the esPrincipal
	 */
	public boolean isEsPrincipal() {
		return esPrincipal;
	}

	/**
	 * @param esPrincipal the esPrincipal to set
	 */
	public void setEsPrincipal(boolean esPrincipal) {
		this.esPrincipal = esPrincipal;
	}

	/**
	 * @return the idCheckBox
	 */
	public String getIdCheckBox() {
		return idCheckBox;
	}

	/**
	 * @param idCheckBox the idCheckBox to set
	 */
	public void setIdCheckBox(String idCheckBox) {
		this.idCheckBox = idCheckBox;
	}

	/**
	 * @return the idDiagnostico
	 */
	public String getIdDiagnostico() {
		return idDiagnostico;
	}

	/**
	 * @param idDiagnostico the idDiagnostico to set
	 */
	public void setIdDiagnostico(String idDiagnostico) {
		this.idDiagnostico = idDiagnostico;
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
	 * @return the idHiddenCheckBox
	 */
	public String getIdHiddenCheckBox() {
		return idHiddenCheckBox;
	}

	/**
	 * @param idHiddenCheckBox the idHiddenCheckBox to set
	 */
	public void setIdHiddenCheckBox(String idHiddenCheckBox) {
		this.idHiddenCheckBox = idHiddenCheckBox;
	}

	/**
	 * @return the propiedadDiagnostico
	 */
	public String getPropiedadDiagnostico() {
		return propiedadDiagnostico;
	}

	/**
	 * @param propiedadDiagnostico the propiedadDiagnostico to set
	 */
	public void setPropiedadDiagnostico(String propiedadDiagnostico) {
		this.propiedadDiagnostico = propiedadDiagnostico;
	}

	/**
	 * @return the propiedadHiddenCheckBox
	 */
	public String getPropiedadHiddenCheckBox() {
		return propiedadHiddenCheckBox;
	}

	/**
	 * @param propiedadHiddenCheckBox the propiedadHiddenCheckBox to set
	 */
	public void setPropiedadHiddenCheckBox(String propiedadHiddenCheckBox) {
		this.propiedadHiddenCheckBox = propiedadHiddenCheckBox;
	}

	/**
	 * @return the buscarTexto
	 */
	public boolean isBuscarTexto() {
		return buscarTexto;
	}

	/**
	 * @param buscarTexto the buscarTexto to set
	 */
	public void setBuscarTexto(boolean buscarTexto) {
		this.buscarTexto = buscarTexto;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the codigoCie
	 */
	public String getCodigoCie() {
		return codigoCie;
	}

	/**
	 * @param codigoCie the codigoCie to set
	 */
	public void setCodigoCie(String codigoCie) {
		this.codigoCie = codigoCie;
	}

	/**
	 * @return the filtrarEdad
	 */
	public boolean isFiltrarEdad() {
		return filtrarEdad;
	}

	/**
	 * @param filtrarEdad the filtrarEdad to set
	 */
	public void setFiltrarEdad(boolean filtrarEdad) {
		this.filtrarEdad = filtrarEdad;
	}

	/**
	 * @return the filtrarSexo
	 */
	public boolean isFiltrarSexo() {
		return filtrarSexo;
	}

	/**
	 * @param filtrarSexo the filtrarSexo to set
	 */
	public void setFiltrarSexo(boolean filtrarSexo) {
		this.filtrarSexo = filtrarSexo;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the diagnosticos
	 */
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	
	/**
	 * @return REtorna elemento del mapa diagnosticos
	 */
	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa diagnosticos 
	 */
	public void setDiagnosticos(String key,Object obj) {
		this.diagnosticos.put(key,obj);
	}

	/**
	 * @return the numDiagnosticos
	 */
	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}

	/**
	 * @param numDiagnosticos the numDiagnosticos to set
	 */
	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
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
	 * @return the tipoDiagnostico
	 */
	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return the diagnosticosSeleccionados
	 */
	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}

	/**
	 * @param diagnosticosSeleccionados the diagnosticosSeleccionados to set
	 */
	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}

	/**
	 * @return the idNumero
	 */
	public String getIdNumero() {
		return idNumero;
	}

	/**
	 * @param idNumero the idNumero to set
	 */
	public void setIdNumero(String idNumero) {
		this.idNumero = idNumero;
	}

	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the idDiagSelecccionados
	 */
	public String getIdDiagSeleccionados() {
		return idDiagSeleccionados;
	}

	/**
	 * @param idDiagSelecccionados the idDiagSelecccionados to set
	 */
	public void setIdDiagSeleccionados(String idDiagSelecccionados) {
		this.idDiagSeleccionados = idDiagSelecccionados;
	}

	public int getEstadoEpidemiologia() {
		return estadoEpidemiologia;
	}

	public void setEstadoEpidemiologia(int estadoEpidemiologia) {
		this.estadoEpidemiologia = estadoEpidemiologia;
	}

	public String getUrlEpidemiologia() {
		return urlEpidemiologia;
	}

	public void setUrlEpidemiologia(String urlEpidemiologia) {
		this.urlEpidemiologia = urlEpidemiologia;
	}

	/**
	 * @return the numSolDx
	 */
	public String getNumSolDx() {
		return numSolDx;
	}

	/**
	 * @param numSolDx the numSolDx to set
	 */
	public void setNumSolDx(String numSolDx) {
		this.numSolDx = numSolDx;
	}

	/**
	 * @return the idValorFicha
	 */
	public String getIdValorFicha() {
		return idValorFicha;
	}

	/**
	 * @param idValorFicha the idValorFicha to set
	 */
	public void setIdValorFicha(String idValorFicha) {
		this.idValorFicha = idValorFicha;
	}
	
	public String getResultadoInsercionFicha() {
		return resultadoInsercionFicha;
	}

	public void setResultadoInsercionFicha(String resultadoInsercionFicha) {
		this.resultadoInsercionFicha = resultadoInsercionFicha;
	}

	/**
	 * @return the codigoFiltro
	 */
	public String getCodigoFiltro() {
		return codigoFiltro;
	}

	/**
	 * @param codigoFiltro the codigoFiltro to set
	 */
	public void setCodigoFiltro(String codigoFiltro) {
		this.codigoFiltro = codigoFiltro;
	}

	/**
	 * @return the valorDiagnostico
	 */
	public String getValorDiagnostico() {
		return valorDiagnostico;
	}

	/**
	 * @param valorDiagnostico the valorDiagnostico to set
	 */
	public void setValorDiagnostico(String valorDiagnostico) {
		this.valorDiagnostico = valorDiagnostico;
	}

	/**
	 * @return Returns the pathJavaScriptAsignacion.
	 */
	public String getPathJavaScriptAsignacion()
	{
		return pathJavaScriptAsignacion;
	}

	/**
	 * @param pathJavaScriptAsignacion The pathJavaScriptAsignacion to set.
	 */
	public void setPathJavaScriptAsignacion(String pathJavaScriptAsignacion)
	{
		this.pathJavaScriptAsignacion = pathJavaScriptAsignacion;
	}

	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}

	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the idFechaMuerte
	 */
	public String getIdFechaMuerte() {
		return idFechaMuerte;
	}

	/**
	 * @param idFechaMuerte the idFechaMuerte to set
	 */
	public void setIdFechaMuerte(String idFechaMuerte) {
		this.idFechaMuerte = idFechaMuerte;
	}

	/**
	 * @return the idHoraMuerte
	 */
	public String getIdHoraMuerte() {
		return idHoraMuerte;
	}

	/**
	 * @param idHoraMuerte the idHoraMuerte to set
	 */
	public void setIdHoraMuerte(String idHoraMuerte) {
		this.idHoraMuerte = idHoraMuerte;
	}
}
