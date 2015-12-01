package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.historiaClinica.EventosAdversosAction;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * Clase para el manejo de el formato de justificación de servicios No POS
 * Date: 2008-03-25
 * @author garias@princetonsa.com
 */
public class FormatoJustServNoposForm extends ValidatorForm 
{
	/**
	 * logger 
	 * */
	private static Logger logger = Logger.getLogger(FormatoJustServNoposForm.class);
	
	/**
	 * HashMap Mapa donde se almacenan los mapas de cada una de las secciones del formulario. 
	 */
	private HashMap formatoJustNoposMap;
	
	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * Variable para saber si la opcion es imprimir
	 */
	private String imprimir="";
	
	/**
	 * nombre de la funcionalidad desde la cual es llamado el formato
	 */
	private String funcionalidad="";
	
	/**
	 * Numero de la solicitud de servicio
	 */
	private String solicitud="";
	
	/**
	 * 
	 */
	private String subcuentasAux="";
	
	/**
	 * 
	 */
	private String solicitudAux="";
	
	/**
	 * 
	 */
	private String nombreFormaAnterior="";
	
	/**
	 * Contiene las Subcuentas asociadas a una solicitud
	 */
	private HashMap subCuentasMap = new HashMap();
	
	/**
	 * Contiene la Sub Cuenta Seleccionada
	 */
	private String subCuentas;
	
	/**
	 * 
	 */
	private String subcuenta="";
	
	/**
	 * Codigo del servicio
	 */
	private String servicio;
	
	/**
	 * indica si se debe recordar una justificacion ya ingresada
	 */
	private String recordar="false";
	
	/**
	 * Cantidad del servicio
	 */
	private int cantServicio = 1;
	
	/**
	 * Convenio responsable de la cobertura
	 */
	private String convenio ="";
	
	/**
	 * Aqui se captura el diagnostico de ingreso
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoIngreso_1="";

	/**
	 * Aqui se captura el diagnostico de complicación
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoComplicacion_1="";
	
	/**
	 * Aqui se captura el diagnostico de muerte
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoMuerte_1="";
	
	/**
	 * Para manejar los diagnosticos presuntivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosPresuntivos = new HashMap();
	
	/**
	 * Define si  los diagnosticos presuntivos de 
	 * la evolución del paciente van o no a la 
	 * epicrisis
	 */
	private boolean diagnosticosPresuntivosBoolean=false;
	 
	/**
	 * Entero para saber cuantos diagnosticos presuntivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosPresuntivos = 0;

	/**
	 * Entero para saber cuantos diagnosticos presuntivos
	 * existían en el objeto (Restricción de adjunto no
	 * puede modificar antiguos)
	 */
	private int numDiagnosticosPresuntivosOriginal = 0;
	
	/**
	 * Fecha de solicitud
	 */
	private String fecha_solicitud;
	
	/**
	 * 
	 */
	String numJus;
	
	/**
	 * posicion registro
	 * */
	private String posicionRegistro;
	/**
	 * posicion campo
	 * */
	private String posicionCampo;
	/**
	 * identificador campo
	 * */
	private String identificadorCampo;
	/**
	 * numero de la seccion del campo
	 * */
	private String numeroSeccion;
	
	/**
	 * Indicador que dice si la justificacion se hizo desde una orden ambulatoria
	 * */
	
	private boolean provieneOrdenAmbulatoria=false;
	/**
	 * Codigo de la orden ambulatoria
	 * */
	private int codigoOrden = ConstantesBD.codigoNuncaValido;
	/**
	 * 
	 */
	public void reset( int codigoInstitucion, int centroAtencion)
	{
		diagnosticoComplicacion_1="";
		diagnosticosDefinitivos = new HashMap();
		numJus="";
		
		this.justificacionHistorica = new HashMap<String, Object>();
		this.justificacionHistorica.put("numRegistros", 0);
	}
	
	/**
	 * 
	 */
	public void resetDiag()
	{
		diagnosticoComplicacion_1="";
		diagnosticosDefinitivos = new HashMap();
	}
	
	public void resetSubCuentas(){
		this.subCuentasMap=new HashMap();
		subCuentasMap.put("numRegistros", 0);
		this.subCuentas="";
	}
	
	/**
	 * Para manejar los diagnosticos definitivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosDefinitivos = new HashMap();
	
	/**
	 * Entero para saber cuantos diagnosticos definitivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosDefinitivos = 0;
	
	/**
	 * Numero del servio seleccionado
	 */
	private String numServicio="";
	
	/**
	 * Tipo de diagnostico
	 */
	private int tipoDiagnosticoPrincipal;
	
	/**
	 * Descripción de las complicaciones de la 
	 * enfermedad del paciente.
	 */
	private String descripcionComplicacion="";
	
	/**
	 * Justificacion Historica
	 */
	private HashMap<String, Object> justificacionHistorica;
	
	
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
        
        if (this.estado.equals("guardar"))
        {
        	if (this.getFormatoJustNoposMap("descjustificacion").equals("")){
        		errores.add("Validación observaciones", new ActionMessage("errors.required","Observaciones "));
        		this.estado = "empezar";
        	}
        	if(this.numJus.equals(ConstantesBD.codigoNuncaValido+"")){
        		errores.add("Validación consecutivo", new ActionMessage("errors.notEspecific","Se debe parametrizar el consecutivo para la justificación de servicios "));
        		this.estado = "empezar";
        	}
        	
        	if(UtilidadTexto.isEmpty(this.diagnosticosDefinitivos.get("principal")+"")&&UtilidadTexto.isEmpty(this.diagnosticosDefinitivos.get("valorFichaDxPrincipal")+"")){
        		errores.add("", new ActionMessage("errors.required", "El Diagnóstico Principal", "0"));
				this.estado="empezar";
			}
        	
        	/*
        	 * Solución Tarea 45671
        	 * Módulo: Historia Clínica Sub Módulo: Justificación No POS Opción: Servicios
        	 * Al ingresar una justificación No POS, según el anexo 586 se debe parametrizar como requerida la selección de una de las opciones 
        	 * (Mortalidad - Morbilidad - Falta efectividad - No Alternativos) del campo Riesgo inminente del paciente.
        	 * En este momento este campo está como no requerido y permite guardar la justificación sin haber seleccionado al menos una opción. 
        	 * Por favor revisar y corregir.
        	 * Muchas gracias
        	 * Luz Adriana López G  
        	 * Acceptor: 	Giovanny Arias Galeano
        	 * Created: 	2008-10-24
        	 */
        	String valorPropiedad = "";
        	boolean mostrarError = true;
        	/*if (formatoJustNoposMap.get("tipo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo).toString().equals("CHEC"))
        	{
				for (int h=0; h<Integer.parseInt(formatoJustNoposMap.get("numHijos_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo).toString()); h++)
				{
					logger.info("Dato: "+formatoJustNoposMap.get("etiquetacampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
					String idCh="ch_valorcampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h;
					String idHd="hd_valorcampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h;
					String propiedad="formatoJustNoposMap(valorcampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h+")";
					logger.info("idCh = "+idCh);
					logger.info("idHd = "+idHd);
					logger.info("propiedad = "+propiedad);
					valorPropiedad = formatoJustNoposMap.get("valorcampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h)+"";
					//logger.info("Valor Propiedad: "+formatoJustNoposMap.get("valorcampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
					logger.info("Valor Propiedad: "+valorPropiedad);
					if(valorPropiedad.equals("true"))
					{
						logger.info("NO se va a mostrar el Error");
						mostrarError = false;
						return errores;
					}
					
				}
				if(mostrarError == true)
				{
					logger.info("Voy a mostrar la validación de Riesgo Inminente del Paciente");
					errores.add("Validación Riesgo Inminente del Paciente", new ActionMessage(
							"errors.required",(formatoJustNoposMap.get("etiquetacampo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo))+"" ));
	        		this.estado = "empezar";
				}
			}*/
        	HashMap mapaSecciones=(HashMap) this.getFormatoJustNoposMap("mapasecciones");
			int numRegSecciones=Integer.parseInt((String)(mapaSecciones.get("numRegistros")));
			
			for(int i=0;i<numRegSecciones;i++){
				int numRegistrosCampos=Integer.parseInt((String)this.formatoJustNoposMap.get("numRegistrosXSec_"+mapaSecciones.get("codigo_"+i)));
				String idSeccion=mapaSecciones.get("codigo_"+i).toString();
				for(int t=0; t< numRegistrosCampos;t++)
				{
					if(UtilidadTexto.getBoolean(this.formatoJustNoposMap.get("mostrar_"+idSeccion+"_"+t)+"")&&
							UtilidadTexto.getBoolean(this.formatoJustNoposMap.get("requerido_"+idSeccion+"_"+t)+"")){
						if(this.formatoJustNoposMap.get("tipo_"+idSeccion+"_"+t).toString().equals("CHEC"))
						{
							boolean seleccionado= false;
							if(this.formatoJustNoposMap.get("numHijos_"+idSeccion+"_"+t)!=null){
								for(int x=0; x< Integer.parseInt(this.formatoJustNoposMap.get("numHijos_"+idSeccion+"_"+t).toString());x++)
								{
									if(this.formatoJustNoposMap.get("campopadre_"+idSeccion+"_"+t+"_"+x).toString().equals(this.formatoJustNoposMap.get("codigocampo_"+
													idSeccion+"_"+t).toString()))	
									{
										
										if(UtilidadTexto.getBoolean(this.formatoJustNoposMap.get("valorcampohijo_"+idSeccion+"_"+t+"_"+x)+""))
										{
											seleccionado=true;
										}
									}
								}
							}
				
							if(!seleccionado)
							{
								errores.add("", new ActionMessage("errors.required", this.formatoJustNoposMap.get("etiquetacampo_"+idSeccion+"_"+t)));
								this.estado="empezar";
							}
						}else{
							if(!this.formatoJustNoposMap.get("tipo_"+idSeccion+"_"+t).toString().equals("CHEC"))
							{
								/*if(this.formatoJustNoposMap.get("numHijos_"+idSeccion+"_"+t)!=null){*/
									if(this.formatoJustNoposMap.get("valorcampo_"+idSeccion+"_"+t) == null||this.formatoJustNoposMap.get("valorcampo_"+idSeccion+"_"+t).toString().trim().equals("")){
										errores.add("", new ActionMessage("errors.required", this.formatoJustNoposMap.get("etiquetacampo_"+idSeccion+"_"+t)));
										this.estado="empezar";
									}
								//}
							}
						}
					}
				}
			}
        }	
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
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the solicitud
	 */
	public String getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the formatoJustNoposMap
	 */
	public HashMap getFormatoJustNoposMap() {
		return formatoJustNoposMap;
	}

	/**
	 * @param formatoJustNoposMap the formatoJustNoposMap to set
	 */
	public void setFormatoJustNoposMap(HashMap formatoJustNoposMap) {
		this.formatoJustNoposMap = formatoJustNoposMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getFormatoJustNoposMap(String key){
		return formatoJustNoposMap.get(key);
	}
	 
	/**
	* 
	*/
	public void setFormatoJustNoposMap(String key,Object obj) {
		this.formatoJustNoposMap.put(key, obj);
	}

	/**
	 * @return the cantServicio
	 */
	public int getCantServicio() {
		return cantServicio;
	}

	/**
	 * @param cantServicio the cantServicio to set
	 */
	public void setCantServicio(int cantServicio) {
		this.cantServicio = cantServicio;
	}

	/**
	 * @return the diagnosticoComplicacion_1
	 */
	public String getDiagnosticoComplicacion_1() {
		return diagnosticoComplicacion_1;
	}

	/**
	 * @param diagnosticoComplicacion_1 the diagnosticoComplicacion_1 to set
	 */
	public void setDiagnosticoComplicacion_1(String diagnosticoComplicacion_1) {
		this.diagnosticoComplicacion_1 = diagnosticoComplicacion_1;
	}

	/**
	 * @return the diagnosticoIngreso_1
	 */
	public String getDiagnosticoIngreso_1() {
		return diagnosticoIngreso_1;
	}

	/**
	 * @param diagnosticoIngreso_1 the diagnosticoIngreso_1 to set
	 */
	public void setDiagnosticoIngreso_1(String diagnosticoIngreso_1) {
		this.diagnosticoIngreso_1 = diagnosticoIngreso_1;
	}

	/**
	 * @return the diagnosticoMuerte_1
	 */
	public String getDiagnosticoMuerte_1() {
		return diagnosticoMuerte_1;
	}

	/**
	 * @param diagnosticoMuerte_1 the diagnosticoMuerte_1 to set
	 */
	public void setDiagnosticoMuerte_1(String diagnosticoMuerte_1) {
		this.diagnosticoMuerte_1 = diagnosticoMuerte_1;
	}

	/**
	 * @return the diagnosticosDefinitivos
	 */
	public Map getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(Map diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * @return the diagnosticosPresuntivos
	 */
	public Map getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
	 */
	public void setDiagnosticosPresuntivos(Map diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * @return the diagnosticosPresuntivosBoolean
	 */
	public boolean isDiagnosticosPresuntivosBoolean() {
		return diagnosticosPresuntivosBoolean;
	}

	/**
	 * @param diagnosticosPresuntivosBoolean the diagnosticosPresuntivosBoolean to set
	 */
	public void setDiagnosticosPresuntivosBoolean(
			boolean diagnosticosPresuntivosBoolean) {
		this.diagnosticosPresuntivosBoolean = diagnosticosPresuntivosBoolean;
	}

	/**
	 * @return the numDiagnosticosDefinitivos
	 */
	public int getNumDiagnosticosDefinitivos() {
		return numDiagnosticosDefinitivos;
	}

	/**
	 * @param numDiagnosticosDefinitivos the numDiagnosticosDefinitivos to set
	 */
	public void setNumDiagnosticosDefinitivos(int numDiagnosticosDefinitivos) {
		this.numDiagnosticosDefinitivos = numDiagnosticosDefinitivos;
	}

	/**
	 * @return the numDiagnosticosPresuntivos
	 */
	public int getNumDiagnosticosPresuntivos() {
		return numDiagnosticosPresuntivos;
	}

	/**
	 * @param numDiagnosticosPresuntivos the numDiagnosticosPresuntivos to set
	 */
	public void setNumDiagnosticosPresuntivos(int numDiagnosticosPresuntivos) {
		this.numDiagnosticosPresuntivos = numDiagnosticosPresuntivos;
	}

	/**
	 * @return the numDiagnosticosPresuntivosOriginal
	 */
	public int getNumDiagnosticosPresuntivosOriginal() {
		return numDiagnosticosPresuntivosOriginal;
	}

	/**
	 * @param numDiagnosticosPresuntivosOriginal the numDiagnosticosPresuntivosOriginal to set
	 */
	public void setNumDiagnosticosPresuntivosOriginal(
			int numDiagnosticosPresuntivosOriginal) {
		this.numDiagnosticosPresuntivosOriginal = numDiagnosticosPresuntivosOriginal;
	}

	/**
	 * @return the tipoDiagnosticoPrincipal
	 */
	public int getTipoDiagnosticoPrincipal() {
		return tipoDiagnosticoPrincipal;
	}

	/**
	 * @param tipoDiagnosticoPrincipal the tipoDiagnosticoPrincipal to set
	 */
	public void setTipoDiagnosticoPrincipal(int tipoDiagnosticoPrincipal) {
		this.tipoDiagnosticoPrincipal = tipoDiagnosticoPrincipal;
	} 
	
	/**
	 * * Asigna un diagnostico definitivo (ppal o relacionado)
	 */
	public void setDiagnosticoDefinitivo(String key, Object value) 
	{
		diagnosticosDefinitivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico definitivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoDefinitivo(String key) 
	{
		return diagnosticosDefinitivos.get(key);
	}	
	
	/**
	 * Asigna un diagnostico presuntivo (ppal o relacionado)
	 */
	public void setDiagnosticoPresuntivo(String key, Object value) 
	{
		diagnosticosPresuntivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico presuntivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoPresuntivo(String key) 
	{
		return diagnosticosPresuntivos.get(key);
	}

	/**
	 * @return the descripcionComplicacion
	 */
	public String getDescripcionComplicacion() {
		return descripcionComplicacion;
	}

	/**
	 * @param descripcionComplicacion the descripcionComplicacion to set
	 */
	public void setDescripcionComplicacion(String descripcionComplicacion) {
		this.descripcionComplicacion = descripcionComplicacion;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the numServicio
	 */
	public String getNumServicio() {
		return numServicio;
	}

	/**
	 * @param numServicio the numServicio to set
	 */
	public void setNumServicio(String numServicio) {
		this.numServicio = numServicio;
	}

	/**
	 * @return the nombreFormaAnterior
	 */
	public String getNombreFormaAnterior() {
		return nombreFormaAnterior;
	}

	/**
	 * @param nombreFormaAnterior the nombreFormaAnterior to set
	 */
	public void setNombreFormaAnterior(String nombreFormaAnterior) {
		this.nombreFormaAnterior = nombreFormaAnterior;
	}

	/**
	 * @return the recordar
	 */
	public String getRecordar() {
		return recordar;
	}

	/**
	 * @param recordar the recordar to set
	 */
	public void setRecordar(String recordar) {
		this.recordar = recordar;
	}

	/**
	 * @return the subcuenta
	 */
	public String getSubcuenta() {
		return subcuenta;
	}

	/**
	 * @param subcuenta the subcuenta to set
	 */
	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}

	/**
	 * @return the funcionalidad
	 */
	public String getFuncionalidad() {
		return funcionalidad;
	}

	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}

	public String getSubCuentas() {
		return subCuentas;
	}

	public void setSubCuentas(String subCuentas) {
		this.subCuentas = subCuentas;
	}

	public HashMap getSubCuentasMap() {
		return subCuentasMap;
	}

	public void setSubCuentasMap(HashMap subCuentasMap) {
		this.subCuentasMap = subCuentasMap;
	}
	
	public Object getSubCuentasMap(String key) {
		return subCuentasMap.get(key);
	}


	public void setSubCuentasMap(String key, Object value) {
		this.subCuentasMap.put(key, value);
	}

	/**
	 * @return the subcuentasAux
	 */
	public String getSubcuentasAux() {
		return subcuentasAux;
	}

	/**
	 * @param subcuentasAux the subcuentasAux to set
	 */
	public void setSubcuentasAux(String subcuentasAux) {
		this.subcuentasAux = subcuentasAux;
	}

	/**
	 * @return the solicitudAux
	 */
	public String getSolicitudAux() {
		return solicitudAux;
	}

	/**
	 * @param solicitudAux the solicitudAux to set
	 */
	public void setSolicitudAux(String solicitudAux) {
		this.solicitudAux = solicitudAux;
	}

	/**
	 * @return the imprimir
	 */
	public String getImprimir() {
		return imprimir;
	}

	/**
	 * @param imprimir the imprimir to set
	 */
	public void setImprimir(String imprimir) {
		this.imprimir = imprimir;
	}

	/**
	 * @return the fecha_solicitud
	 */
	public String getFecha_solicitud() {
		return fecha_solicitud;
	}

	/**
	 * @param fecha_solicitud the fecha_solicitud to set
	 */
	public void setFecha_solicitud(String fecha_solicitud) {
		this.fecha_solicitud = fecha_solicitud;
	}

	/**
	 * @return the numJus
	 */
	public String getNumJus() {
		return numJus;
	}

	/**
	 * @param numJus the numJus to set
	 */
	public void setNumJus(String numJus) {
		this.numJus = numJus;
	}

	/**
	 * @return the justificacionHistorica
	 */
	public HashMap<String, Object> getJustificacionHistorica() {
		return justificacionHistorica;
	}

	/**
	 * @param justificacionHistorica the justificacionHistorica to set
	 */
	public void setJustificacionHistorica(
			HashMap<String, Object> justificacionHistorica) {
		this.justificacionHistorica = justificacionHistorica;
	}

	public String getPosicionRegistro() {
		return posicionRegistro;
	}

	public void setPosicionRegistro(String posicionRegistro) {
		this.posicionRegistro = posicionRegistro;
	}

	public String getPosicionCampo() {
		return posicionCampo;
	}

	public void setPosicionCampo(String posicionCampo) {
		this.posicionCampo = posicionCampo;
	}

	public String getIdentificadorCampo() {
		return identificadorCampo;
	}

	public void setIdentificadorCampo(String identificadorCampo) {
		this.identificadorCampo = identificadorCampo;
	}

	public String getNumeroSeccion() {
		return numeroSeccion;
	}

	public void setNumeroSeccion(String numeroSeccion) {
		this.numeroSeccion = numeroSeccion;
	}

	public boolean isProvieneOrdenAmbulatoria() {
		return provieneOrdenAmbulatoria;
	}

	public void setProvieneOrdenAmbulatoria(boolean provieneOrdenAmbulatoria) {
		this.provieneOrdenAmbulatoria = provieneOrdenAmbulatoria;
	}

	public int getCodigoOrden() {
		return codigoOrden;
	}
	
	public void setCodigoOrden(int codigoOrden) {
		this.codigoOrden = codigoOrden;
	}



}