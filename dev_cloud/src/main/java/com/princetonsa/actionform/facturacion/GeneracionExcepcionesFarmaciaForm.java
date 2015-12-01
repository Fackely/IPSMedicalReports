
/*
 * Creado   7/12/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.facturacion;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadValidacion;


/**
 * Clase para manejar
 *
 * @version 1.0, 7/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class GeneracionExcepcionesFarmaciaForm extends ActionForm
{
	/**
	 * id de la cuenta
	 */
	private int idCuenta;
	
	/**
	 * # de solicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * Cod del área
	 */
	private int codigoArea;
	
	/**
	 * Nombre del área
	 */
	private String nombreArea;
	
	/**
	 * Cod del articulo
	 */
	private int codigoArticulo;
	
	/**
	 * Desc del articulo
	 */
	private String descripcionArticulo;
	
	/**
	 * % No cubierto
	 */
	private String porcentajeNoCubiertoString;
	
	/**
	 * Boolean que indica si la búsqueda avanzada fue realizada
	 * por el cod o por la descripcion
	 */
	private boolean esBusquedaPorCodigo;
	
	/**
	* Estado en el que se encuentra el proceso.	   
	*/
	private String estado;
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaGeneracion;
	
	/**
	 * almacena los datos de la consulta y no los modifica
	 */
	private HashMap mapaGeneracionNoModificado;
	
	/**
	 * almacena el numero de filas en el HashMap mapaGeneracion
	 */
	private int numeroElementos;
	
	/**
	 * Almacena los datos de la consulta
	 */
	private Collection colGeneracion; 
	
	/**
	 * Contiene el link al cual se hace referencia 
	 * en el pageUrl del pager con el fin de mantener 
	 * los datos al hacer submit
	 */
	private String linkSiguiente;
	
	/**
	 * almacena el patron por el cul se va ordenar
	 */
	private String patronOrdenar;
	
	/**
	 * almacena un tipo de estado accion
	 */
	private String accion;
	
	/**
	 * Ultimo patro por el que se ordeno.
	 */
	private String ultimoPatron;
	
	/*
	 * columna por la cual se quiere ordenar
	 /
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 
	private String ultimaPropiedad;*/
	
	
	/**
	 * limpiar e inicializar atributos de la clase
	 *
	 */	
   public void reset ()
   {
	   this.mapaGeneracion = new HashMap ();
	   this.mapaGeneracionNoModificado= new HashMap();
	   this.numeroElementos = 0;
	   this.linkSiguiente = "";
	   this.idCuenta=0;
	   this.colGeneracion = null;
	   this.patronOrdenar = "";
	   this.ultimoPatron="";
	   
	   this.numeroSolicitud=0;
	   this.codigoArea=0;
	   this.nombreArea="";
	   this.codigoArticulo=0;
	   this.descripcionArticulo="";
	   this.porcentajeNoCubiertoString="";
	   this.esBusquedaPorCodigo=false;
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
		if(estado.equals("salirGuardar"))
		{
			//errores=super.validate(mapping, request);


			for(int i=0; i<this.getNumeroElementos(); i++)
			{
				/*if(this.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+i).equals("0"))
				{
					if(this.getMapaGeneracion(""))
				}*/
				String tempoNomArt=" [número solicitud: "+this.getMapaGeneracion("orden_"+i)+" y artículo: "+this.getMapaGeneracion("descripcionArticulo_"+i)+"] ";


				try
				{

					if(  (this.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+i)+"").equals("")  || (this.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+i)+"").equals("null"))
					{	
						errores.add("% no generado es requerido", new ActionMessage("errors.required","El % no generado "));
					}	
					else
					{
						double tempDouble= Double.parseDouble(this.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+i)+"");
						
						if(tempDouble<0 || tempDouble>100)
						{
							errores.add("% no generado ", new ActionMessage("errors.range", "El % no generado "+tempDouble+tempoNomArt+" es entero o decimal y", "cero (0)", "cien (100)"));
						}
						else
						{
							boolean esMayorAdosDecimales=UtilidadValidacion.esMayorNdecimales(tempDouble,2);
							if(esMayorAdosDecimales)
							{
								errores.add("% mayor a dos decimales", new ActionMessage("errors.numDecimales", "El % no generado "+tempDouble+tempoNomArt, "dos (2)"));
							}
						}
					}	
				}catch(Exception e)
				{
					errores.add("% no generado", new ActionMessage("errors.range", "El % no generado "+tempoNomArt+" es entero o decimal y", "cero (0)", " cien (100)"));
				}
		   }
		}
		return errores;
	}   

	/**
	 * @return Returns the esBusquedaPorCodigo.
	 */
	public boolean getEsBusquedaPorCodigo() {
		return esBusquedaPorCodigo;
	}
	/**
	 * @param esBusquedaPorCodigo The esBusquedaPorCodigo to set.
	 */
	public void setEsBusquedaPorCodigo(boolean esBusquedaPorCodigo) {
		this.esBusquedaPorCodigo = esBusquedaPorCodigo;
	}
	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna mapaGeneracion.
	 */
	public HashMap getMapaGeneracion() {
		return mapaGeneracion;
	}
	/**
	 * @param mapaGeneracion Asigna mapaGeneracion.
	 */
	public void setMapaGeneracion(HashMap mapaGeneracion) {
		this.mapaGeneracion = mapaGeneracion;
	}
	
	/**
	 * Set del medicos por pool
	 * @param key
	 * @param value
	 */
	public void setMapaGeneracion(String key, Object value) 
	{
		mapaGeneracion.put(key, value);
	}

	/**
	 * Get del mapa de medicos por pool
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaGeneracion(String key) 
	{
		return mapaGeneracion.get(key);
	}
	/**
	 * @return Retorna numeroElementos.
	 */
	public int getNumeroElementos() {
		return numeroElementos;
	}
	/**
	 * @param numeroElementos Asigna numeroElementos.
	 */
	public void setNumeroElementos(int numeroElementos) {
		this.numeroElementos = numeroElementos;
	}
	/**
	 * @return Retorna linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente Asigna linkSiguiente.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Retorna coleccion.
	 */
	public Collection getColGeneracion() {
		return colGeneracion;
	}
	/**
	 * @param coleccion Asigna coleccion.
	 */
	public void setColGeneracion(Collection coleccion) {
		this.colGeneracion = coleccion;
	}
	/*
	 * Returns the columna.
	 * @return String
	 /
	public String getColumna()	{
		return columna;
	}
	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 /
	public String getUltimaPropiedad(){
		return ultimaPropiedad;
	}
	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 /
	public void setColumna(String columna)	{
		this.columna = columna;
	}
	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 /
	public void setUltimaPropiedad(String ultimaPropiedad)	{
		this.ultimaPropiedad = ultimaPropiedad;
	}*/
	/**
	 * @return Returns the codigoArea.
	 */
	public int getCodigoArea() {
		return codigoArea;
	}
	/**
	 * @param codigoArea The codigoArea to set.
	 */
	public void setCodigoArea(int codigoArea) {
		this.codigoArea = codigoArea;
	}
	/**
	 * @return Returns the codigoArticulo.
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo The codigoArticulo to set.
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return Returns the descripcionArticulo.
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	/**
	 * @param descripcionArticulo The descripcionArticulo to set.
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	/**
	 * @return Returns the nombreArea.
	 */
	public String getNombreArea() {
		return nombreArea;
	}
	/**
	 * @param nombreArea The nombreArea to set.
	 */
	public void setNombreArea(String nombreArea) {
		this.nombreArea = nombreArea;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the porcentajeNoCubiertoString.
	 */
	public String getPorcentajeNoCubiertoString() {
		return porcentajeNoCubiertoString;
	}
	/**
	 * @param porcentajeNoCubiertoString The porcentajeNoCubiertoString to set.
	 */
	public void setPorcentajeNoCubiertoString(String porcentajeNoCubiertoString) {
		this.porcentajeNoCubiertoString = porcentajeNoCubiertoString;
	}
	/**
	 * Set del gen excepciones farmacia No modificado
	 * @param key
	 * @param value
	 */
	public void setMapaGeneracionNoModificado(String key, Object value) 
	{
		mapaGeneracionNoModificado.put(key, value);
	}
	/**
	 * Get del mapa de gen excepciones farmacia No modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaGeneracionNoModificado(String key) 
	{
		return mapaGeneracionNoModificado.get(key);
	}
	/**
	 * @return Returns the mapaGeneracionNoModificado.
	 */
	public HashMap getMapaGeneracionNoModificado() {
		return mapaGeneracionNoModificado;
	}
	/**
	 * @param mapaGeneracionNoModificado The mapaGeneracionNoModificado to set.
	 */
	public void setMapaGeneracionNoModificado(HashMap mapaGeneracionNoModificado) {
		this.mapaGeneracionNoModificado = mapaGeneracionNoModificado;
	}
	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	/**
	 * @param patronOrdenar Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Retorna accion.
	 */
	public String getAccion() {
		return accion;
	}
	/**
	 * @param accion Asigna accion.
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}
	/**
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
}
