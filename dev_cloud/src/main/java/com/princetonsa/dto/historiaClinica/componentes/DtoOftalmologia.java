package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.Utilidades;

/**
 * Data Transfer Object: Usado para el manejo del componente oftalmologia
 * 
 * @author Sebastián Gómez R.
 *
 *Nota * Se contemplan las tablas
 *tipos_sintoma_oftal - tipo_sintoma_institucion
 *localizacion_oftal - localizacion_por_tipo
 *severidad_oftalmologica - severidad_por_tipo
 *sintoma_oftalmologia - sintoma_por_tipo
 *val_oftal_sintoma
 *otro_sintoma_oftal
 */
public class DtoOftalmologia implements Serializable
{
	//*************************ATRIBUTOS SECCION SINTOMAS MOTIVO CONSULTA*************************************************************
	public static final String[] indicesRegistroSintomas = {"codigo_","nombreTipoSintoma_","numeroSolicitud_","codigoSintoma_","nombreSintoma_","codigoSeveridad_","nombreSeveridad_","codigoLocalizacion_","nombreLocalizacion_"};
	public static final String[] indicesRegistroOtrosSintomas = {"numeroSolicitud_","nombreSintoma_","nombreSeveridad_","nombreLocalizacion_"};
	
	private String numeroSolicitud;
	
	/**
	 * Arreglo que almacena los tipos sintoma por institucion
	 * tipo_sintoma_institucion
	 */
	private ArrayList<InfoDatosInt> tiposSintoma;
	
	/**
	 * Arreglo que contiene arreglos (opciones) de cada tipo sintoma de la columna sintoma
	 * sintoma_por_tipo
	 */
	private ArrayList<ArrayList<HashMap<String, Object>>> opcionesSintoma;
	
	/**
	 * Arreglo que contiene arreglos (opciones) de cada tipo sintoma de la columna localizacion
	 * localizacion_por_tipo
	 */
	private ArrayList<ArrayList<HashMap<String, Object>>> opcionesLocalizacion;
	
	/**
	 * Arreglo que contiene arreglos (opciones) de cada tipo sintoma de la columna severidad
	 * severidad_por_tipo
	 */
	private ArrayList<ArrayList<HashMap<String, Object>>> opcionesSeveridad;
	
	
	/**
	 * Mapa usado para registrar los sintomas parametrizados
	 * val_oftal_sintoma
	 * 
	 * OJO * TENER EN CUENTA QUE LA ESTRUCTURA DEL MAPA CUANDO SE CAPTURA LA INFORMACION
	 * ES DIFERENTE CUANDO SE USA PARA MOSTRAR EL RESUMEN
	 */
	private HashMap<String, Object> registroSintomas;
	
	/**
	 * Mapa usado para registrar los otros sintomas
	 * otro_sintoma_oftal
	 */
	private HashMap<String, Object> registroOtrosSintomas;
	//**********************************************************************************************************************
	//****************ATRIBUTOS SECCION EXAMEN OFTALMOLOGICO*****************************************************************
	//Atributos subseccion Agudeza visual
	private ArrayList<HashMap<String, Object>> tiposAgudezaLejos;
	private ArrayList<HashMap<String, Object>> tiposAgudezaCerca;
	private InfoDatosInt ojoDerechoLejosSinCorrecion;
	private InfoDatosInt ojoDerechoLejosConCorrecion;
	private InfoDatosInt ojoDerechoCercaSinCorrecion;
	private InfoDatosInt ojoDerechoCercaConCorrecion;
	private InfoDatosInt ojoIzquierdoLejosSinCorrecion;
	private InfoDatosInt ojoIzquierdoLejosConCorrecion;
	private InfoDatosInt ojoIzquierdoCercaSinCorrecion;
	private InfoDatosInt ojoIzquierdoCercaConCorrecion;
	
	//Atributos subseccion refreaccion
	private String ojoDerechoSK;
	private String ojoDerechoSKCiclo;
	private String ojoDerechoSKSubj;
	private String ojoIzquierdoSK;
	private String ojoIzquierdoSKCiclo;
	private String ojoIzquierdoSKSubj;
	private String add;
	private String dip;
	
	//Atributos subseccion queratometria
	private String ojoDerechoQueratometria;
	private String ojoIzquierdoQueratometria;
	
	//Atributos subseccion tonometria
	private ArrayList<HashMap<String, Object>> equipos ;
	private String ojoDerechoTonometria;
	private String ojoIzquierdoTonometria;
	private InfoDatosInt equipoTonometria;
	//*************************************************************************************************************************
	
	/**
	 * Método que limpia los datos del DTO
	 */
	public void clean()
	{
		this.numeroSolicitud = "";
		
		//********Sintomas motivo de la consulta******************************************
		this.tiposSintoma = null;
		this.opcionesSintoma = null;
		this.opcionesLocalizacion = null;
		this.opcionesSeveridad = null;
		
		this.registroSintomas = null;
		this.registroOtrosSintomas = null;
		
		//************Examen oftalmologico************************************************
		//Atributos subseccion Agudeza visual
		this.tiposAgudezaLejos = null;
		this.tiposAgudezaCerca = null;
		this.ojoDerechoLejosSinCorrecion = new InfoDatosInt();
		this.ojoDerechoLejosConCorrecion = new InfoDatosInt();
		this.ojoDerechoCercaSinCorrecion = new InfoDatosInt();
		this.ojoDerechoCercaConCorrecion = new InfoDatosInt();
		this.ojoIzquierdoLejosSinCorrecion = new InfoDatosInt();
		this.ojoIzquierdoLejosConCorrecion = new InfoDatosInt();
		this.ojoIzquierdoCercaSinCorrecion = new InfoDatosInt();
		this.ojoIzquierdoCercaConCorrecion = new InfoDatosInt();
		
		//Atributos subseccion refreaccion
		this.ojoDerechoSK = "";
		this.ojoDerechoSKCiclo = "";
		this.ojoDerechoSKSubj = "";
		this.ojoIzquierdoSK = "";
		this.ojoIzquierdoSKCiclo = "";
		this.ojoIzquierdoSKSubj = "";
		this.add = "";
		this.dip = "";
		
		//Atributos subseccion queratometria
		this.ojoDerechoQueratometria = "";
		this.ojoIzquierdoQueratometria = "";
		
		//Atributos subseccion tonometria
		this.equipos = null;
		this.ojoDerechoTonometria = "";
		this.ojoIzquierdoTonometria = "";
		this.equipoTonometria =new InfoDatosInt();
		//********************************************************************************
	}
	
	/**
	 * Cosntructor
	 *
	 */
	public DtoOftalmologia()
	{
		clean();
	}

	/**
	 * @return the opcionesLocalizacion
	 */
	public ArrayList<ArrayList<HashMap<String, Object>>> getOpcionesLocalizacion() {
		if(this.opcionesLocalizacion == null)
		{
			this.opcionesLocalizacion = new ArrayList<ArrayList<HashMap<String,Object>>>();
		}
		return opcionesLocalizacion;
	}

	/**
	 * @param opcionesLocalizacion the opcionesLocalizacion to set
	 */
	public void setOpcionesLocalizacion(
			ArrayList<ArrayList<HashMap<String, Object>>> opcionesLocalizacion) {
		this.opcionesLocalizacion = opcionesLocalizacion;
	}

	/**
	 * @return the opcionesSeveridad
	 */
	public ArrayList<ArrayList<HashMap<String, Object>>> getOpcionesSeveridad() {
		if(this.opcionesSeveridad == null)
		{
			this.opcionesSeveridad = new ArrayList<ArrayList<HashMap<String,Object>>>();
		}
		return opcionesSeveridad;
	}

	/**
	 * @param opcionesSeveridad the opcionesSeveridad to set
	 */
	public void setOpcionesSeveridad(
			ArrayList<ArrayList<HashMap<String, Object>>> opcionesSeveridad) {
		this.opcionesSeveridad = opcionesSeveridad;
	}

	/**
	 * @return the opcionesSintoma
	 */
	public ArrayList<ArrayList<HashMap<String, Object>>> getOpcionesSintoma() {
		if(this.opcionesSintoma == null)
		{
			this.opcionesSintoma = new ArrayList<ArrayList<HashMap<String,Object>>>();
		}
		return opcionesSintoma;
	}

	/**
	 * @param opcionesSintoma the opcionesSintoma to set
	 */
	public void setOpcionesSintoma(
			ArrayList<ArrayList<HashMap<String, Object>>> opcionesSintoma) {
		this.opcionesSintoma = opcionesSintoma;
	}

	/**
	 * @return the registroOtrosSintomas
	 */
	public HashMap<String, Object> getRegistroOtrosSintomas() {
		if(registroOtrosSintomas == null)
		{
			registroOtrosSintomas = new HashMap<String, Object>();
		}
		return registroOtrosSintomas;
	}

	/**
	 * @param registroOtrosSintomas the registroOtrosSintomas to set
	 */
	public void setRegistroOtrosSintomas(
			HashMap<String, Object> registroOtrosSintomas) {
		this.registroOtrosSintomas = registroOtrosSintomas;
	}
	
	
	/**
	 * @return the registroOtrosSintomas
	 */
	public Object getRegistroOtrosSintomas(String key) {
		return getRegistroOtrosSintomas().get(key);
	}

	/**
	 * @param registroOtrosSintomas the registroOtrosSintomas to set
	 */
	public void setRegistroOtrosSintomas(String key,Object obj) {
		this.getRegistroOtrosSintomas().put(key,obj);
	}
	
	/**
	 * Método que retorna el número de otros sintomas
	 * @return
	 */
	public int getNumRegistroOtrosSintomas()
	{
		return Utilidades.convertirAEntero(this.getRegistroOtrosSintomas("numRegistros")+"", true);
	}
	
	/**
	 * Método que asigna tamaño al número de otros sintomas
	 * @param numRegistros
	 */
	public void setNumRegistroOtrosSintomas(int numRegistros)
	{
		this.setRegistroOtrosSintomas("numRegistros", numRegistros);
	}
	

	/**
	 * @return the registroSintomas
	 */
	public HashMap<String, Object> getRegistroSintomas() {
		if(registroSintomas == null)
		{
			registroSintomas = new HashMap<String, Object>();
		}
		return registroSintomas;
	}

	/**
	 * @param registroSintomas the registroSintomas to set
	 */
	public void setRegistroSintomas(HashMap<String, Object> registroSintomas) {
		this.registroSintomas = registroSintomas;
	}
	
	/**
	 * @return the registroSintomas
	 */
	public Object getRegistroSintomas(String key) {
		return getRegistroSintomas().get(key);
	}

	/**
	 * @param registroSintomas the registroSintomas to set
	 */
	public void setRegistroSintomas(String key,Object obj) {
		this.getRegistroSintomas().put(key, obj);
	}
	
	/**
	 * Método que retorna el número de sintomas
	 * @return
	 */
	public int getNumRegistroSintomas()
	{
		return Utilidades.convertirAEntero(this.getRegistroSintomas("numRegistros")+"", true);
	}
	
	/**
	 * Método que retorna el número de sintomas de un tipo especifico
	 * @return
	 */
	public int getNumRegistroSintomasTipo(int pos)
	{
		return Utilidades.convertirAEntero(this.getRegistroSintomas("numRegistros_"+pos)+"", true);
	}
	
	/**
	 * Método que asigna tamaño al número de  sintomas
	 * @param numRegistros
	 */
	public void setNumRegistroSintomas(int numRegistros)
	{
		this.setRegistroSintomas("numRegistros", numRegistros);
	}
	
	
	/**
	 * @return the tiposSintoma
	 */
	public ArrayList<InfoDatosInt> getTiposSintoma() {
		if(this.tiposSintoma == null)
		{
			this.tiposSintoma = new ArrayList<InfoDatosInt>();
		}
		return tiposSintoma;
	}

	/**
	 * @param tiposSintoma the tiposSintoma to set
	 */
	public void setTiposSintoma(ArrayList<InfoDatosInt> tiposSintoma) {
		this.tiposSintoma = tiposSintoma;
	}
	
	/**
	 * Método implementado para saber el número de tipos de síntomas
	 * @return
	 */
	public int getNumTiposSintoma()
	{
		return this.getTiposSintoma().size();
	}
	
	/**
	 * Método que verifica si se ingresó información del componente de oftalmologia
	 * @return
	 */
	public boolean ingresoInformacion()
	{
		boolean ingreso = false;
		//*************SECCION MOTIVO CONSULTA***********************
		//Se verifica si se ingresó sintoma parametrizado
		for(int i=0;i<this.getTiposSintoma().size();i++)
			for(int j=0;j<Utilidades.convertirAEntero(this.getRegistroSintomas("numRegistros_"+i).toString());j++)
				//El mero hecho de que haya un codigo de sintoma quiere decir que se diligenció
				if(Utilidades.convertirAEntero(this.getRegistroSintomas("codigoSintoma_"+i+"_"+j).toString())>0)
					ingreso = true;
		
		//SE verifica si hubo otros sintomas
		for(int i=0;i<this.getNumRegistroOtrosSintomas();i++)
			if(!this.getRegistroOtrosSintomas("nombreSintoma_"+i).toString().trim().equals(""))
				ingreso = true;
		
		//**********************************************************
		//*********SECCION EXAMEN OFTALMOLOGÍA*********************
		//Agudeza visual
		if(
			this.getCodigoOjoDerechoCercaSinCorrecion()>0||
			this.getCodigoOjoDerechoCercaConCorrecion()>0||
			this.getCodigoOjoDerechoLejosSinCorrecion()>0||
			this.getCodigoOjoDerechoLejosConCorrecion()>0||
			this.getCodigoOjoIzquierdoCercaSinCorrecion()>0||
			this.getCodigoOjoIzquierdoCercaConCorrecion()>0||
			this.getCodigoOjoIzquierdoLejosSinCorrecion()>0||
			this.getCodigoOjoIzquierdoLejosConCorrecion()>0
		)
			ingreso = true;
		//Refraccion Y Queratometría
		if(
			!this.ojoDerechoSK.trim().equals("")||
			!this.ojoDerechoSKCiclo.trim().equals("")||
			!this.ojoDerechoSKSubj.trim().equals("")||
			!this.ojoIzquierdoSK.trim().equals("")||
			!this.ojoIzquierdoSKCiclo.trim().equals("")||
			!this.ojoIzquierdoSKSubj.trim().equals("")||
			!this.add.trim().equals("")||
			!this.dip.trim().equals("")||
			!this.ojoDerechoQueratometria.trim().equals("")||
			!this.ojoIzquierdoQueratometria.equals("")
		)
			ingreso = true;
		//Tonometría
		if(
			!this.ojoDerechoTonometria.trim().equals("")||
			!this.ojoIzquierdoTonometria.trim().equals("")
		)
			ingreso = true;
			
		//*********************************************************
		
		return ingreso;
	}
	
	/**
	 * Método que verifica si un componente está cargado
	 * @return
	 */
	public boolean estaCargado()
	{
		boolean cargado = false;
		
		//****************SECCION MOTIVO CONSULTA***************************************
		if(this.getNumRegistroSintomas()>0)
			cargado = true;
		
		if(this.getNumRegistroOtrosSintomas()>0)
			cargado = true;
		//********************************************************************************
		//**********************************************************
		//*********SECCION EXAMEN OFTALMOLOGÍA*********************
		//Agudeza visual
		if(
			this.getCodigoOjoDerechoCercaSinCorrecion()>0||
			this.getCodigoOjoDerechoCercaConCorrecion()>0||
			this.getCodigoOjoDerechoLejosSinCorrecion()>0||
			this.getCodigoOjoDerechoLejosConCorrecion()>0||
			this.getCodigoOjoIzquierdoCercaSinCorrecion()>0||
			this.getCodigoOjoIzquierdoCercaConCorrecion()>0||
			this.getCodigoOjoIzquierdoLejosSinCorrecion()>0||
			this.getCodigoOjoIzquierdoLejosConCorrecion()>0
		)
			cargado = true;
		//Refraccion Y Queratometría
		if(
			!this.ojoDerechoSK.trim().equals("")||
			!this.ojoDerechoSKCiclo.trim().equals("")||
			!this.ojoDerechoSKSubj.trim().equals("")||
			!this.ojoIzquierdoSK.trim().equals("")||
			!this.ojoIzquierdoSKCiclo.trim().equals("")||
			!this.ojoIzquierdoSKSubj.trim().equals("")||
			!this.add.trim().equals("")||
			!this.dip.trim().equals("")||
			!this.ojoDerechoQueratometria.trim().equals("")||
			!this.ojoIzquierdoQueratometria.equals("")
		)
			cargado = true;
		//Tonometría
		if(
			!this.ojoDerechoTonometria.trim().equals("")||
			!this.ojoIzquierdoTonometria.trim().equals("")
		)
			cargado = true;
			
		//*********************************************************
		
		return cargado;
	}
	
	/**
	 * Método que realiza las validaciones de los campos del componente de oftalmología
	 * @param errores
	 * @return
	 */
	public ActionErrors validate(ActionErrors errores)
	{
		String sintoma = "", localizacion = "", severidad = "";
		
		//***********SECCION MOTIVO CONSULTA************************************************
		
		//Se verifica sintomas parametrizados
		int i=0;
		for(InfoDatosInt tipoSintoma : this.getTiposSintoma())
		{
			HashMap repetidos = new HashMap();
			
			for(int j=0;j<Utilidades.convertirAEntero(this.getRegistroSintomas("numRegistros_"+i).toString());j++)
			{
				sintoma = this.getRegistroSintomas("codigoSintoma_"+i+"_"+j).toString();
				localizacion = this.getRegistroSintomas("codigoLocalizacion_"+i+"_"+j).toString();
				severidad = this.getRegistroSintomas("codigoSeveridad_"+i+"_"+j).toString();
				
				//Se verifica si se ingresó un síntoma
				if(!sintoma.equals(""))
				{
					//Si el mapa que almacena los repetidos contiene la combinacion de sintoma, localizacion y severidad se muestra error
					if(repetidos.containsKey(sintoma+"_"+localizacion+"_"+severidad))
						repetidos.put(sintoma+"_"+localizacion+"_"+severidad, ConstantesBD.acronimoSi);
					else
						repetidos.put(sintoma+"_"+localizacion+"_"+severidad, ConstantesBD.acronimoNo);
				}
				//Si no se ingresó sintoma pero se ingresó alguna localizacion o severidad se debe pedir requerido
				else if(!localizacion.equals("") || !severidad.equals(""))
					errores.add("", new ActionMessage("error.salasCirugia.sinDefinirGeneral","síntomas de "+tipoSintoma.getNombre()+" (Oftalmología)"));
				
				
			}
			
			//Si en el mapa repetidos hay algun valor en S quiere decir que hubo sintomas repetidos
			if(repetidos.containsValue(ConstantesBD.acronimoSi))
				errores.add("", new ActionMessage("error.salasCirugia.igualesGeneral","síntomas de "+tipoSintoma.getNombre(),"(Oftalmología)"));
			
			i++;
		}
		
		//Se verifica otros síntomas
		HashMap repetidos = new HashMap();
		for(i=0;i<this.getNumRegistroOtrosSintomas();i++)
		{
			sintoma = this.getRegistroOtrosSintomas("nombreSintoma_"+i).toString();
			localizacion = this.getRegistroOtrosSintomas("nombreLocalizacion_"+i).toString();
			severidad = this.getRegistroOtrosSintomas("nombreSeveridad_"+i).toString();
			
			if(!sintoma.equals(""))
			{
				//Si el mapa que almacena los repetidos contiene la combinacion de sintoma, localizacion y severidad se muestra error
				if(repetidos.containsKey(sintoma+"_"+localizacion))
					repetidos.put(sintoma+"_"+localizacion, ConstantesBD.acronimoSi);
				else
					repetidos.put(sintoma+"_"+localizacion+"_"+severidad, ConstantesBD.acronimoNo);
				
				//Si al otro síntoma le llega a faltar la localilzación se debe generar mensaje de error
				if(localizacion.equals(""))
					errores.add("", new ActionMessage("errors.required","La localización del síntoma "+sintoma+" (Oftalmología)"));
			}
		}
		//Si en el mapa repetidos hay algun valor en S quiere decir que hubo sintomas repetidos
		if(repetidos.containsValue(ConstantesBD.acronimoSi))
			errores.add("", new ActionMessage("error.salasCirugia.igualesGeneral","otros síntomas","(Oftalmología)"));
		//**********************************************************************************
		//************	SECCION EXAMEN OFTALMOLÓGICO***************************************
		//Si se ingresó información de tonometría entonces es requerido el equipo
		if((!this.ojoDerechoTonometria.trim().equals("")||!this.ojoIzquierdoTonometria.trim().equals(""))&&
			this.getCodigoEquipoTonometria()<=0)
			errores.add("", new ActionMessage("errors.required","El equipo de tonometría (Oftalmología)"));
			
		//********************************************************************************
		
		return errores;
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
	 * @return the add
	 */
	public String getAdd() {
		return add;
	}

	/**
	 * @param add the add to set
	 */
	public void setAdd(String add) {
		this.add = add;
	}

	/**
	 * @return the dip
	 */
	public String getDip() {
		return dip;
	}

	/**
	 * @param dip the dip to set
	 */
	public void setDip(String dip) {
		this.dip = dip;
	}

	/**
	 * @return the equipos
	 */
	public ArrayList<HashMap<String, Object>> getEquipos() {
		if(equipos == null)
		{
			equipos = new ArrayList<HashMap<String,Object>>();
		}
		return equipos;
	}

	/**
	 * @param equipos the equipos to set
	 */
	public void setEquipos(ArrayList<HashMap<String, Object>> equipos) {
		this.equipos = equipos;
	}

	/**
	 * @return the equipoTonometria
	 */
	public int getCodigoEquipoTonometria() {
		return equipoTonometria.getCodigo();
	}

	/**
	 * @param equipoTonometria the equipoTonometria to set
	 */
	public void setCodigoEquipoTonometria(int equipoTonometria) {
		this.equipoTonometria.setCodigo(equipoTonometria);
	}
	
	
	/**
	 * @return the equipoTonometria
	 */
	public String getNombreEquipoTonometria() {
		return equipoTonometria.getNombre();
	}

	/**
	 * @param equipoTonometria the equipoTonometria to set
	 */
	public void setNombreEquipoTonometria(String equipoTonometria) {
		this.equipoTonometria.setNombre(equipoTonometria);
	}
	

	/**
	 * @return the ojoDerechoCercaConCorrecion
	 */
	public int getCodigoOjoDerechoCercaConCorrecion() {
		return ojoDerechoCercaConCorrecion.getCodigo();
	}

	/**
	 * @param ojoDerechoCercaConCorrecion the ojoDerechoCercaConCorrecion to set
	 */
	public void setCodigoOjoDerechoCercaConCorrecion(
			int ojoDerechoCercaConCorrecion) {
		this.ojoDerechoCercaConCorrecion.setCodigo(ojoDerechoCercaConCorrecion);
	}
	
	/**
	 * @return the ojoDerechoCercaConCorrecion
	 */
	public String getNombreOjoDerechoCercaConCorrecion() {
		return ojoDerechoCercaConCorrecion.getNombre();
	}

	/**
	 * @param ojoDerechoCercaConCorrecion the ojoDerechoCercaConCorrecion to set
	 */
	public void setNombreOjoDerechoCercaConCorrecion(
			String ojoDerechoCercaConCorrecion) {
		this.ojoDerechoCercaConCorrecion.setNombre(ojoDerechoCercaConCorrecion);
	}
	
	

	/**
	 * @return the ojoDerechoCercaSinCorrecion
	 */
	public int getCodigoOjoDerechoCercaSinCorrecion() {
		return ojoDerechoCercaSinCorrecion.getCodigo();
	}

	/**
	 * @param ojoDerechoCercaSinCorrecion the ojoDerechoCercaSinCorrecion to set
	 */
	public void setCodigoOjoDerechoCercaSinCorrecion(
			int ojoDerechoCercaSinCorrecion) {
		this.ojoDerechoCercaSinCorrecion.setCodigo(ojoDerechoCercaSinCorrecion);
	}
	
	
	
	/**
	 * @return the ojoDerechoCercaSinCorrecion
	 */
	public String getNombreOjoDerechoCercaSinCorrecion() {
		return ojoDerechoCercaSinCorrecion.getNombre();
	}

	/**
	 * @param ojoDerechoCercaSinCorrecion the ojoDerechoCercaSinCorrecion to set
	 */
	public void setNombreOjoDerechoCercaSinCorrecion(
			String ojoDerechoCercaSinCorrecion) {
		this.ojoDerechoCercaSinCorrecion.setNombre(ojoDerechoCercaSinCorrecion);
	}
	
	

	/**
	 * @return the ojoDerechoLejosConCorrecion
	 */
	public int getCodigoOjoDerechoLejosConCorrecion() {
		return ojoDerechoLejosConCorrecion.getCodigo();
	}

	/**
	 * @param ojoDerechoLejosConCorrecion the ojoDerechoLejosConCorrecion to set
	 */
	public void setCodigoOjoDerechoLejosConCorrecion(
			int ojoDerechoLejosConCorrecion) {
		this.ojoDerechoLejosConCorrecion.setCodigo(ojoDerechoLejosConCorrecion);
	}
	
	
	
	/**
	 * @return the ojoDerechoLejosConCorrecion
	 */
	public String  getNombreOjoDerechoLejosConCorrecion() {
		return ojoDerechoLejosConCorrecion.getNombre();
	}

	/**
	 * @param ojoDerechoLejosConCorrecion the ojoDerechoLejosConCorrecion to set
	 */
	public void setNombreOjoDerechoLejosConCorrecion(
			String ojoDerechoLejosConCorrecion) {
		this.ojoDerechoLejosConCorrecion.setNombre(ojoDerechoLejosConCorrecion);
	}
	
	

	/**
	 * @return the ojoDerechoLejosSinCorrecion
	 */
	public int getCodigoOjoDerechoLejosSinCorrecion() {
		return ojoDerechoLejosSinCorrecion.getCodigo();
	}

	/**
	 * @param ojoDerechoLejosSinCorrecion the ojoDerechoLejosSinCorrecion to set
	 */
	public void setCodigoOjoDerechoLejosSinCorrecion(
			int ojoDerechoLejosSinCorrecion) {
		this.ojoDerechoLejosSinCorrecion.setCodigo(ojoDerechoLejosSinCorrecion);
	}
	
	
	/**
	 * @return the ojoDerechoLejosSinCorrecion
	 */
	public String getNombreOjoDerechoLejosSinCorrecion() {
		return ojoDerechoLejosSinCorrecion.getNombre();
	}

	/**
	 * @param ojoDerechoLejosSinCorrecion the ojoDerechoLejosSinCorrecion to set
	 */
	public void setNombreOjoDerechoLejosSinCorrecion(
			String ojoDerechoLejosSinCorrecion) {
		this.ojoDerechoLejosSinCorrecion.setNombre(ojoDerechoLejosSinCorrecion);
	}
	

	/**
	 * @return the ojoDerechoQueratometria
	 */
	public String getOjoDerechoQueratometria() {
		return ojoDerechoQueratometria;
	}

	/**
	 * @param ojoDerechoQueratometria the ojoDerechoQueratometria to set
	 */
	public void setOjoDerechoQueratometria(String ojoDerechoQueratometria) {
		this.ojoDerechoQueratometria = ojoDerechoQueratometria;
	}

	/**
	 * @return the ojoDerechoSK
	 */
	public String getOjoDerechoSK() {
		return ojoDerechoSK;
	}

	/**
	 * @param ojoDerechoSK the ojoDerechoSK to set
	 */
	public void setOjoDerechoSK(String ojoDerechoSK) {
		this.ojoDerechoSK = ojoDerechoSK;
	}

	/**
	 * @return the ojoDerechoSKCiclo
	 */
	public String getOjoDerechoSKCiclo() {
		return ojoDerechoSKCiclo;
	}

	/**
	 * @param ojoDerechoSKCiclo the ojoDerechoSKCiclo to set
	 */
	public void setOjoDerechoSKCiclo(String ojoDerechoSKCiclo) {
		this.ojoDerechoSKCiclo = ojoDerechoSKCiclo;
	}

	/**
	 * @return the ojoDerechoSKSubj
	 */
	public String getOjoDerechoSKSubj() {
		return ojoDerechoSKSubj;
	}

	/**
	 * @param ojoDerechoSKSubj the ojoDerechoSKSubj to set
	 */
	public void setOjoDerechoSKSubj(String ojoDerechoSKSubj) {
		this.ojoDerechoSKSubj = ojoDerechoSKSubj;
	}

	/**
	 * @return the ojoDerechoTonometria
	 */
	public String getOjoDerechoTonometria() {
		return ojoDerechoTonometria;
	}

	/**
	 * @param ojoDerechoTonometria the ojoDerechoTonometria to set
	 */
	public void setOjoDerechoTonometria(String ojoDerechoTonometria) {
		this.ojoDerechoTonometria = ojoDerechoTonometria;
	}

	/**
	 * @return the ojoIzquierdoCercaConCorrecion
	 */
	public int getCodigoOjoIzquierdoCercaConCorrecion() {
		return ojoIzquierdoCercaConCorrecion.getCodigo();
	}

	/**
	 * @param ojoIzquierdoCercaConCorrecion the ojoIzquierdoCercaConCorrecion to set
	 */
	public void setCodigoOjoIzquierdoCercaConCorrecion(
			int ojoIzquierdoCercaConCorrecion) {
		this.ojoIzquierdoCercaConCorrecion.setCodigo(ojoIzquierdoCercaConCorrecion);
	}
	
	/**
	 * @return the ojoIzquierdoCercaConCorrecion
	 */
	public String getNombreOjoIzquierdoCercaConCorrecion() {
		return ojoIzquierdoCercaConCorrecion.getNombre();
	}

	/**
	 * @param ojoIzquierdoCercaConCorrecion the ojoIzquierdoCercaConCorrecion to set
	 */
	public void setNombreOjoIzquierdoCercaConCorrecion(
			String ojoIzquierdoCercaConCorrecion) {
		this.ojoIzquierdoCercaConCorrecion.setNombre(ojoIzquierdoCercaConCorrecion);
	}
	

	/**
	 * @return the ojoIzquierdoCercaSinCorrecion
	 */
	public int getCodigoOjoIzquierdoCercaSinCorrecion() {
		return ojoIzquierdoCercaSinCorrecion.getCodigo();
	}

	/**
	 * @param ojoIzquierdoCercaSinCorrecion the ojoIzquierdoCercaSinCorrecion to set
	 */
	public void setCodigoOjoIzquierdoCercaSinCorrecion(
			int ojoIzquierdoCercaSinCorrecion) {
		this.ojoIzquierdoCercaSinCorrecion.setCodigo(ojoIzquierdoCercaSinCorrecion);
	}
	
	/**
	 * @return the ojoIzquierdoCercaSinCorrecion
	 */
	public String getNombreOjoIzquierdoCercaSinCorrecion() {
		return ojoIzquierdoCercaSinCorrecion.getNombre();
	}

	/**
	 * @param ojoIzquierdoCercaSinCorrecion the ojoIzquierdoCercaSinCorrecion to set
	 */
	public void setNombreOjoIzquierdoCercaSinCorrecion(
			String  ojoIzquierdoCercaSinCorrecion) {
		this.ojoIzquierdoCercaSinCorrecion.setNombre(ojoIzquierdoCercaSinCorrecion);
	}
	

	/**
	 * @return the ojoIzquierdoLejosConCorrecion
	 */
	public int getCodigoOjoIzquierdoLejosConCorrecion() {
		return ojoIzquierdoLejosConCorrecion.getCodigo();
	}

	/**
	 * @param ojoIzquierdoLejosConCorrecion the ojoIzquierdoLejosConCorrecion to set
	 */
	public void setCodigoOjoIzquierdoLejosConCorrecion(
			int ojoIzquierdoLejosConCorrecion) {
		this.ojoIzquierdoLejosConCorrecion.setCodigo(ojoIzquierdoLejosConCorrecion);
	}
	
	/**
	 * @return the ojoIzquierdoLejosConCorrecion
	 */
	public String getNombreOjoIzquierdoLejosConCorrecion() {
		return ojoIzquierdoLejosConCorrecion.getNombre();
	}

	/**
	 * @param ojoIzquierdoLejosConCorrecion the ojoIzquierdoLejosConCorrecion to set
	 */
	public void setNombreOjoIzquierdoLejosConCorrecion(
			String ojoIzquierdoLejosConCorrecion) {
		this.ojoIzquierdoLejosConCorrecion.setNombre(ojoIzquierdoLejosConCorrecion);
	}
	
	
	
	

	/**
	 * @return the ojoIzquierdoLejosSinCorrecion
	 */
	public int getCodigoOjoIzquierdoLejosSinCorrecion() {
		return ojoIzquierdoLejosSinCorrecion.getCodigo();
	}

	/**
	 * @param ojoIzquierdoLejosSinCorrecion the ojoIzquierdoLejosSinCorrecion to set
	 */
	public void setCodigoOjoIzquierdoLejosSinCorrecion(
			int ojoIzquierdoLejosSinCorrecion) {
		this.ojoIzquierdoLejosSinCorrecion.setCodigo(ojoIzquierdoLejosSinCorrecion);
	}
	
	/**
	 * @return the ojoIzquierdoLejosSinCorrecion
	 */
	public String getNombreOjoIzquierdoLejosSinCorrecion() {
		return ojoIzquierdoLejosSinCorrecion.getNombre();
	}

	/**
	 * @param ojoIzquierdoLejosSinCorrecion the ojoIzquierdoLejosSinCorrecion to set
	 */
	public void setNombreOjoIzquierdoLejosSinCorrecion(
			String ojoIzquierdoLejosSinCorrecion) {
		this.ojoIzquierdoLejosSinCorrecion.setNombre(ojoIzquierdoLejosSinCorrecion);
	}
	
	

	/**
	 * @return the ojoIzquierdoQueratometria
	 */
	public String getOjoIzquierdoQueratometria() {
		return ojoIzquierdoQueratometria;
	}

	/**
	 * @param ojoIzquierdoQueratometria the ojoIzquierdoQueratometria to set
	 */
	public void setOjoIzquierdoQueratometria(String ojoIzquierdoQueratometria) {
		this.ojoIzquierdoQueratometria = ojoIzquierdoQueratometria;
	}

	/**
	 * @return the ojoIzquierdoSK
	 */
	public String getOjoIzquierdoSK() {
		return ojoIzquierdoSK;
	}

	/**
	 * @param ojoIzquierdoSK the ojoIzquierdoSK to set
	 */
	public void setOjoIzquierdoSK(String ojoIzquierdoSK) {
		this.ojoIzquierdoSK = ojoIzquierdoSK;
	}

	/**
	 * @return the ojoIzquierdoSKCiclo
	 */
	public String getOjoIzquierdoSKCiclo() {
		return ojoIzquierdoSKCiclo;
	}

	/**
	 * @param ojoIzquierdoSKCiclo the ojoIzquierdoSKCiclo to set
	 */
	public void setOjoIzquierdoSKCiclo(String ojoIzquierdoSKCiclo) {
		this.ojoIzquierdoSKCiclo = ojoIzquierdoSKCiclo;
	}

	/**
	 * @return the ojoIzquierdoSKSubj
	 */
	public String getOjoIzquierdoSKSubj() {
		return ojoIzquierdoSKSubj;
	}

	/**
	 * @param ojoIzquierdoSKSubj the ojoIzquierdoSKSubj to set
	 */
	public void setOjoIzquierdoSKSubj(String ojoIzquierdoSKSubj) {
		this.ojoIzquierdoSKSubj = ojoIzquierdoSKSubj;
	}

	/**
	 * @return the ojoIzquierdoTonometria
	 */
	public String getOjoIzquierdoTonometria() {
		return ojoIzquierdoTonometria;
	}

	/**
	 * @param ojoIzquierdoTonometria the ojoIzquierdoTonometria to set
	 */
	public void setOjoIzquierdoTonometria(String ojoIzquierdoTonometria) {
		this.ojoIzquierdoTonometria = ojoIzquierdoTonometria;
	}

	/**
	 * @return the tiposAgudezaCerca
	 */
	public ArrayList<HashMap<String, Object>> getTiposAgudezaCerca() {
		if(tiposAgudezaCerca == null)
		{
			tiposAgudezaCerca = new ArrayList<HashMap<String,Object>>();
		}
		return tiposAgudezaCerca;
	}

	/**
	 * @param tiposAgudezaCerca the tiposAgudezaCerca to set
	 */
	public void setTiposAgudezaCerca(
			ArrayList<HashMap<String, Object>> tiposAgudezaCerca) {
		this.tiposAgudezaCerca = tiposAgudezaCerca;
	}

	/**
	 * @return the tiposAgudezaLejos
	 */
	public ArrayList<HashMap<String, Object>> getTiposAgudezaLejos() {
		if(tiposAgudezaLejos == null)
		{
			tiposAgudezaLejos = new ArrayList<HashMap<String,Object>>();
		}
		return tiposAgudezaLejos;
	}

	/**
	 * @param tiposAgudezaLejos the tiposAgudezaLejos to set
	 */
	public void setTiposAgudezaLejos(ArrayList<HashMap<String, Object>> tiposAgudezaLejos) {
		this.tiposAgudezaLejos = tiposAgudezaLejos;
	}
	
	/**
	 * Método que verifica si existe agudeza visual
	 * @return
	 */
	public boolean isExisteAgudezaVisual()
	{
		boolean existe = false;
		
		if(!this.getNombreOjoDerechoLejosSinCorrecion().trim().equals("")||
			!this.getNombreOjoDerechoLejosConCorrecion().trim().equals("")||
			!this.getNombreOjoDerechoCercaSinCorrecion().trim().equals("")||
			!this.getNombreOjoDerechoCercaConCorrecion().trim().equals("")||
			!this.getNombreOjoIzquierdoLejosSinCorrecion().trim().equals("")||
			!this.getNombreOjoIzquierdoLejosConCorrecion().trim().equals("")||
			!this.getNombreOjoIzquierdoCercaSinCorrecion().trim().equals("")||
			!this.getNombreOjoIzquierdoCercaConCorrecion().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe refracción
	 * @return
	 */
	public boolean isExisteRefraccion()
	{
		boolean existe = false;
		
		if(!this.getOjoDerechoSK().trim().equals("")||
			!this.getOjoDerechoSKCiclo().trim().equals("")||
			!this.getOjoDerechoSKSubj().trim().equals("")||
			!this.getOjoIzquierdoSK().trim().equals("")||
			!this.getOjoIzquierdoSKCiclo().trim().equals("")||
			!this.getOjoIzquierdoSKSubj().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe ADd o DIP
	 * @return
	 */
	public boolean isExisteAddDip()
	{
		boolean existe = false;
		
		if(!this.getAdd().trim().equals("")||!this.getDip().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe queratometría
	 * @return
	 */
	public boolean isExisteQueratometria()
	{
		boolean existe = false;
		
		if(!this.getOjoDerechoQueratometria().trim().equals("")||!this.getOjoIzquierdoQueratometria().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si esiste tonometría
	 * @return
	 */
	public boolean isExisteTonometria()
	{
		boolean existe = false;
		
		if(!this.getOjoDerechoTonometria().trim().equals("")||!this.getOjoIzquierdoTonometria().trim().equals("")||!this.getNombreEquipoTonometria().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	
	/**
	 * Método que verifica si existe examen oftalmologico
	 * @return
	 */
	public boolean isExisteExamenOftalmologico()
	{
		boolean existe = false;
		
		if(this.isExisteAgudezaVisual()||this.isExisteRefraccion()||this.isExisteAddDip()||this.isExisteQueratometria()||this.isExisteTonometria())
			existe = true;
		return existe;
	}
	
}
