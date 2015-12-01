/*
 * Sep 09 / 2005
 */
package com.princetonsa.actionform.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;

/**
 * @author Sebastián Gómez
 *
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Porcentajes Cirugías Múltiples
 */
public class PorcentajesCxMultiplesForm extends ValidatorForm {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(PorcentajesCxMultiplesForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
		
	/**
	 * Variable usada para almacenar el número de registros
	 * del mapa porcentajes
	 */
	private int numRegistros;
	
	/**
	 * Variable usada para almacenar el consecutivo del registro
	 * que se va a eliminar
	 */
	private int codigoRegistro;
	
	/**
	 * Cadena que almacena información de los registros utilizados en el
	 * sistema que no pueden ser modificados o eliminados
	 */
	private String registrosUsados;
	
	/**
	 * Offset del mapa procentajes
	 */
	private int offsetHash;
	
	/**
	 * Variable que apunta a la siguiente página del pager
	 */
	private String linkSiguiente;
	
	/**
	 * Variables usadas para la ordenacion
	 */
	private String indice;
	private String ultimoIndice;
	
	/**
	 * Objeto para almacenar los parámetros de búsqueda
	 */
	private HashMap mapaBusqueda=new HashMap();
	
	/**
	 * Variable usada para almacenar la posicion del mapa donde
	 * se encuentra ubicado el registro
	 */
	private int pos;
	
		
	//***********************************************************
	//Atributos del detalle	
	
	/**
	 * ArrayList Tipos Servicios
	 * */
	private ArrayList tiposServicioArray;
	
	/**
	 * ArrayList tipos Sala 
	 * */
	private ArrayList tiposSalaArray;
	
	/**
	 * Objeto donde se almacena el listado de Porcentajes
	 */
	private HashMap porcentajes=new HashMap();
	
	/**
	 * Objeto donde se almacena las vigencias
	 * */
	private HashMap vigenciasMap = new HashMap();
	
	/**
	 * Indice del mapa de vigencias
	 * */
	private String indiceVigencias;
	
	
	//***********************************************************
	//Atributos del encabezado
	
	/**
	 * Mapa de encabezado de porcentajes
	 * */
	private HashMap encabezadoMap;
	
	/**
	 * ArraList de Convenios
	 * */
	private ArrayList conveniosArray;
	
	/**
	 * ArrayList de Esquemas Tarifarios
	 * */
	private ArrayList esquemasTarifariosArray;
	
	/**
	 * Indicador de Ingreso
	 * */
	private String indicadorIngreso;
	
	/**
	 * String convenio
	 * */
	private HashMap convenioMap;
	
	/**
	 * Fecha Inicial 
	 * */
	private String fechaInicial;
	
	/**
	 * Fecha Final
	 * */
	private String fechaFinal;
	
	/**
	 * Esquema Tarifario
	 * */
	private HashMap esquemaTarifario;
	
	
	/**
	 * Seccion de Busqueda Avanzada
	 * */
	private boolean seccionBusquedaAvanzada;
	
	
	//**********************************************************
	//Fin Atributos Encabezado
	
	
	  
	
	
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
		
		if(this.estado.equals("eliminar"))
		{
			String [] tipoAsocio = {"","",""};			
			int numRegistros = Integer.parseInt(this.porcentajes.get("numRegistros").toString());
			
			for(int i = 0; i < numRegistros; i++)
			{		
				tipoAsocio = this.porcentajes.get("tipo_asocio_"+i).toString().split(ConstantesBD.separadorSplit);			
				this.porcentajes.put("tipo_asocio_"+i,tipoAsocio[0]);
			}
		}
		if(this.estado.equals("guardar"))
		{
			String [] tipoAsocio = {"","",""}; 
			int numRegistros = Integer.parseInt(this.porcentajes.get("numRegistros").toString());
			
			for(int i = 0; i < numRegistros; i++)
			{				
				tipoAsocio = this.porcentajes.get("tipo_asocio_"+i).toString().split(ConstantesBD.separadorSplit);
				//logger.info("valir de tipo asocio >> "+this.porcentajes.get("tipo_asocio_"+i).toString()+" >> "+tipoAsocio+" >> "+i+" >> "+tipoAsocio.length);
				this.porcentajes.put("tipo_asocio_"+i,tipoAsocio[0]);
					
				if(tipoAsocio[0].toString().equals(ConstantesBD.codigoNuncaValido+"") 
						|| tipoAsocio[0].toString().equals("0"))
				{
					errores.add("descripcion",new ActionMessage("errors.required","Tipo de Asocio para el Registro Nro. "+(i+1)));
				}
				else
				{
					//logger.info("valor del tipo servicio >> "+tipoAsocio[1]+" >> "+this.porcentajes.get("via_acceso_"+i));
					//para el asocio con el tipo de servicio honorario es requerido el tipo de especialista y la via
					if(tipoAsocio.length > 1 && 
							tipoAsocio[1].toString().equals(ConstantesBD.codigoServicioHonorariosCirugia+""))							
					{
						if(this.porcentajes.get("via_acceso_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") ||
								this.porcentajes.get("tipo_especialista_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Los Campos tipo de Especialista y Vias son requeridos para un tipo de Asocio Honorarios en el Registro Nro. "+(i+1)));
						}						
					}						
				}
				
				if(this.porcentajes.get("liquidacion_"+i).equals(""))
				{					
					errores.add("descripcion",new ActionMessage("errors.required","Liquidación para el Registro Nro. "+(i+1)));
				}
				else
				{
					if(Utilidades.convertirADouble(this.porcentajes.get("liquidacion_"+i).toString()) == ConstantesBD.codigoNuncaValido)
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Liquidación para el Registro Nro. "+(i+1)+" debe ser numerico "));						
				}
				
				if(this.porcentajes.get("adicional_"+i).equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","Adicional para el Registro Nro. "+(i+1)));
				}
				else
				{
					if(Utilidades.convertirADouble(this.porcentajes.get("adicional_"+i).toString()) == ConstantesBD.codigoNuncaValido)
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Adicional para el Registro Nro. "+(i+1)+" debe ser numerico "));						
				}
				
				if(this.porcentajes.get("politra_"+i).equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","Politraumatismo para el Registro Nro. "+(i+1)));
				}
				else
				{
					if(Utilidades.convertirADouble(this.porcentajes.get("politra_"+i).toString()) == ConstantesBD.codigoNuncaValido)
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Politraumatismo para el Registro Nro. "+(i+1)+" debe ser numerico "));						
				}
				
				for(int j = 0; j < numRegistros; j++)
				{
					if(j!=i)
					{
						if(this.porcentajes.get("tipo_servicio_"+i).equals(this.porcentajes.get("tipo_servicio_"+j)) 
								&& this.porcentajes.get("tipo_sala_"+i).equals(this.porcentajes.get("tipo_sala_"+j))
									&& this.porcentajes.get("tipo_cirugia_"+i).equals(this.porcentajes.get("tipo_cirugia_"+j))
										&& this.porcentajes.get("tipo_asocio_"+i).equals(this.porcentajes.get("tipo_asocio_"+j))
											&& this.porcentajes.get("via_acceso_"+i).equals(this.porcentajes.get("via_acceso_"+j))
												&& this.porcentajes.get("tipo_especialista_"+i).equals(this.porcentajes.get("tipo_especialista_"+j)))
						{
							errores.add("descripcion",new ActionMessage("errors.invalid","Campo Nro."+(i+1)+" Se encuentra repetido para el mismo Tipo Servicio, Tipo de Sala, Tipos de Cirugia, Tipo de Asocio, Vias de Acceso y Tipo Especialista"));
						}
							
					}
				}
				
			}					
						
		}		
		
		return errores;
	}
	
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.porcentajes=new HashMap();
		this.numRegistros=0;
		this.codigoRegistro=0;
		this.registrosUsados="";
		this.offsetHash=0;
		this.linkSiguiente="";
		this.indice="";
		this.ultimoIndice="";
		this.mapaBusqueda=new HashMap();
		this.pos=-1;
		
		this.tiposServicioArray = new ArrayList();		
		this.tiposSalaArray = new ArrayList();
		this.conveniosArray = new ArrayList();
				
		this.convenioMap = new HashMap();
		this.encabezadoMap = new HashMap();
		this.esquemaTarifario = new HashMap();
		this.fechaFinal = "";
		this.fechaInicial= "";
		this.indicadorIngreso = "";	
		this.indiceVigencias = "";
		this.seccionBusquedaAvanzada = false;
	}
	
	/**
	 * @return Returns the codigoRegistro.
	 */
	public int getCodigoRegistro() {
		return codigoRegistro;
	}
	/**
	 * @param codigoRegistro The codigoRegistro to set.
	 */
	public void setCodigoRegistro(int codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the porcentajes.
	 */
	public HashMap getPorcentajes() {
		return porcentajes;
	}
	/**
	 * @param porcentajes The porcentajes to set.
	 */
	public void setPorcentajes(HashMap porcentajes) {
		this.porcentajes = porcentajes;
	}
	/**
	 * @return Retorna un elemento del mapa porcentajes.
	 */
	public Object getPorcentajes(String key) {
		return porcentajes.get(key);
	}
	/**
	 * @param asigna un elemento al mapa porcentajes
	 */
	public void setPorcentajes(String key,Object obj) {
		this.porcentajes.put(key,obj);
	}
	/**
	 * @return Returns the registrosUsados.
	 */
	public String getRegistrosUsados() {
		return registrosUsados;
	}
	/**
	 * @param registrosUsados The registrosUsados to set.
	 */
	public void setRegistrosUsados(String registrosUsados) {
		this.registrosUsados = registrosUsados;
	}
	/**
	 * @return Returns the offsetHash.
	 */
	public int getOffsetHash() {
		return offsetHash;
	}
	/**
	 * @param offsetHash The offsetHash to set.
	 */
	public void setOffsetHash(int offsetHash) {
		this.offsetHash = offsetHash;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the mapaBusqueda.
	 */
	public HashMap getMapaBusqueda() {
		return mapaBusqueda;
	}
	/**
	 * @param mapaBusqueda The mapaBusqueda to set.
	 */
	public void setMapaBusqueda(HashMap mapaBusqueda) {
		this.mapaBusqueda = mapaBusqueda;
	}
	/**
	 * @return Retorna un elemento del mapaBusqueda
	 */
	public Object getMapaBusqueda(String key) {
		return mapaBusqueda.get(key);
	}
	/**
	 * @param asigna un elemento a mapaBusqueda
	 */
	public void setMapaBusqueda(String key,Object obj) {
		this.mapaBusqueda.put(key,obj);
	}
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}


	/**
	 * @return the tiposServicio
	 */
	public ArrayList getTiposServicioArray() {
		return tiposServicioArray;
	}


	/**
	 * @param tiposServicio the tiposServicio to set
	 */
	public void setTiposServicioArray(ArrayList tiposServicio) {
		this.tiposServicioArray = tiposServicio;
	}


	

	/**
	 * @return the conveniosArray
	 */
	public ArrayList getConveniosArray() {
		return conveniosArray;
	}


	/**
	 * @param conveniosArray the conveniosArray to set
	 */
	public void setConveniosArray(ArrayList conveniosArray) {
		this.conveniosArray = conveniosArray;
	}


	/**
	 * @return the esquemasTarifariosArray
	 */
	public ArrayList getEsquemasTarifariosArray() {
		return esquemasTarifariosArray;
	}


	/**
	 * @param esquemasTarifariosArray the esquemasTarifariosArray to set
	 */
	public void setEsquemasTarifariosArray(ArrayList esquemasTarifariosArray) {
		this.esquemasTarifariosArray = esquemasTarifariosArray;
	}


	/**
	 * @return the tiposSalaArray
	 */
	public ArrayList getTiposSalaArray() {
		return tiposSalaArray;
	}


	/**
	 * @param tiposSalaArray the tiposSalaArray to set
	 */
	public void setTiposSalaArray(ArrayList tiposSalaArray) {
		this.tiposSalaArray = tiposSalaArray;
	}


	/**
	 * @return the indicadorIngreso
	 */
	public String getIndicadorIngreso() {
		return indicadorIngreso;
	}


	/**
	 * @param indicadorIngreso the indicadorIngreso to set
	 */
	public void setIndicadorIngreso(String indicadorIngreso) {
		this.indicadorIngreso = indicadorIngreso;
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
	 * @return the esquemaTarifario
	 */
	public HashMap getEsquemaTarifario() {
		return esquemaTarifario;
	}


	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(HashMap esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}
	
	/**
	 * @return the esquemaTarifario
	 */
	public Object getEsquemaTarifario(String key) {
		return esquemaTarifario.get(key);
	}


	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(String key, Object value) {
		this.esquemaTarifario.put(key, value);
	}


	/**
	 * @return the vigencias
	 */
	public HashMap getVigenciasMap() {
		return vigenciasMap;
	}


	/**
	 * @param vigencias the vigencias to set
	 */
	public void setVigenciasMap(HashMap vigenciasMap) {
		this.vigenciasMap = vigenciasMap;
	}
	
	/**
	 * @return the vigencias
	 */
	public Object getVigenciasMap(String key) {
		return vigenciasMap.get(key);
	}


	/**
	 * @param vigencias the vigencias to set
	 */
	public void setVigenciasMap(String key, Object value) {
		this.vigenciasMap.put(key, value);
	}


	/**
	 * @return the indiceVigencias
	 */
	public String getIndiceVigencias() {
		return indiceVigencias;
	}


	/**
	 * @param indiceVigencias the indiceVigencias to set
	 */
	public void setIndiceVigencias(String indiceVigencias) {
		this.indiceVigencias = indiceVigencias;
	}


	/**
	 * @return the convenioMap
	 */
	public HashMap getConvenioMap() {
		return convenioMap;
	}


	/**
	 * @param convenioMap the convenioMap to set
	 */
	public void setConvenioMap(HashMap convenioMap) {
		this.convenioMap = convenioMap;
	}
	
	
	/**
	 * @return the convenioMap
	 */
	public Object getConvenioMap(String key) {
		return convenioMap.get(key);
	}


	/**
	 * @param convenioMap the convenioMap to set
	 */
	public void setConvenioMap(String key, Object value) {
		this.convenioMap.put(key, value);
	}


	/**
	 * @return the encabezadoMap
	 */
	public HashMap getEncabezadoMap() {
		return encabezadoMap;
	}


	/**
	 * @param encabezadoMap the encabezadoMap to set
	 */
	public void setEncabezadoMap(HashMap encabezadoMap) {
		this.encabezadoMap = encabezadoMap;
	}
	
	/**
	 * @return the encabezadoMap
	 */
	public Object getEncabezadoMap(String key) {
		return encabezadoMap.get(key);
	}


	/**
	 * @param encabezadoMap the encabezadoMap to set
	 */
	public void setEncabezadoMap(String key,Object value) {
		this.encabezadoMap.put(key, value);
	}


	/**
	 * @return the seccionBusquedaAvanzada
	 */
	public boolean isSeccionBusquedaAvanzada() {
		return seccionBusquedaAvanzada;
	}


	/**
	 * @param seccionBusquedaAvanzada the seccionBusquedaAvanzada to set
	 */
	public void setSeccionBusquedaAvanzada(boolean seccionBusquedaAvanzada) {
		this.seccionBusquedaAvanzada = seccionBusquedaAvanzada;
	}
}