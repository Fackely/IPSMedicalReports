/*
 * Sep 19, 2008
 */
package com.princetonsa.dto.ordenesmedicas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * Data Transfer Object, Para el manejo de la información
 * de la prescripcion de dialisis
 * @author Sebastián Gómez R.
 *
 */
public class DtoPrescripcionDialisis  implements Serializable
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	//Constantes de tipos de consulta
	public static final int tipoConsultaFiltro = 1;
	public static final int tipoConsultaFlujoBomba = 2;
	public static final int tipoConsultaFlujoDializado = 3;
	public static final int tipoConsultaAccesoVascular = 4;
	public static final int tipoConsultaRecambio = 5;
	public static final int tipoConsultaVolumen = 6;
	public static final int tipoConsultaTiposMembrana = 7;
	
	private String codigoHistoEnca;
	private String modalidadTerapia;
	private String fechaFinalDialisis;
	private String horaFinalDialisis;
	private UsuarioBasico profesionalFinaliza;
	private UsuarioBasico profesional;
	private String fechaOrden;
	private String horaOrden;
	//Atributos para el manejod e fecha/hora inicio - fin dialisis
	private boolean finalizado;
	private boolean manejoFinalizado; //atributo que maneja la finalizacion desde la página
	
	//*******ARREGLO DEL REGISTRO DE LAS FECHAS/HORAS INICIALES*****************
	private ArrayList<DtoPrescripDialFechaHora> fechasHorasIniciales = new ArrayList<DtoPrescripDialFechaHora>();
	//*************************************************************************
	
	//*********ATRIBUTOS PARA LA HEMODIALISIS***********************************
	private ArrayList<DtoHemodialisis> hemodialisis = new ArrayList<DtoHemodialisis>();
	//***************************************************************************
	
	//Arreglos
	private ArrayList<HashMap<String,Object>> tiposMembrana = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String,Object>> filtros = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String,Object>> flujosBomba = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String,Object>> flujosDializado = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String,Object>> accesosVasculares = new ArrayList<HashMap<String,Object>>();
	//*************************************************
	//*******ATRIBUTOS PARA EL CAPD O EL APD***********
	private HashMap<String, Object> mapa = new HashMap<String, Object>();
	//Arreglos
	private ArrayList<HashMap<String,Object>> recambios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String,Object>> volumenes = new ArrayList<HashMap<String,Object>>();
	//**************************************************
	
	/**
	 * Constructor 
	 */
	public DtoPrescripcionDialisis()
	{
		this.clean();
	}
	
	/**
	 * Método que realiza el reset de los campos del DTO
	 */
	public void clean()
	{
		this.codigoHistoEnca = "";
		this.modalidadTerapia = "";
		this.fechaFinalDialisis = "";
		this.horaFinalDialisis = "";
		this.profesionalFinaliza = new UsuarioBasico();
		this.profesional = new UsuarioBasico();
		this.fechasHorasIniciales = new ArrayList<DtoPrescripDialFechaHora>();
		
		this.fechaOrden = "";
		this.horaOrden = "";
		this.finalizado = false;
		this.manejoFinalizado = false;
		
		
		//Atributos para la hemodialisis
		this.hemodialisis = new ArrayList<DtoHemodialisis>();
		
		
		this.tiposMembrana = new ArrayList<HashMap<String,Object>>();
		this.filtros = new ArrayList<HashMap<String,Object>>();
		this.flujosBomba = new ArrayList<HashMap<String,Object>>();
		this.flujosDializado = new ArrayList<HashMap<String,Object>>();
		this.accesosVasculares = new ArrayList<HashMap<String,Object>>();
		
		//Atributos para el CAPD o el APD
		this.mapa = new HashMap<String, Object>();
		this.recambios = new ArrayList<HashMap<String,Object>>();
		this.volumenes = new ArrayList<HashMap<String,Object>>();
	}

	/**
	 * @return the fechasHorasIniciales
	 */
	public ArrayList<DtoPrescripDialFechaHora> getFechasHorasIniciales() {
		return fechasHorasIniciales;
	}

	/**
	 * @param fechasHorasIniciales the fechasHorasIniciales to set
	 */
	public void setFechasHorasIniciales(
			ArrayList<DtoPrescripDialFechaHora> fechasHorasIniciales) {
		this.fechasHorasIniciales = fechasHorasIniciales;
	}

	/**
	 * @return the fechaOrden
	 */
	public String getFechaOrden() {
		return fechaOrden;
	}

	/**
	 * @param fechaOrden the fechaOrden to set
	 */
	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	/**
	 * @return the horaOrden
	 */
	public String getHoraOrden() {
		return horaOrden;
	}

	/**
	 * @param horaOrden the horaOrden to set
	 */
	public void setHoraOrden(String horaOrden) {
		this.horaOrden = horaOrden;
	}

	/**
	 * @return the codigoHistoEnca
	 */
	public String getCodigoHistoEnca() {
		return codigoHistoEnca;
	}

	/**
	 * @param codigoHistoEnca the codigoHistoEnca to set
	 */
	public void setCodigoHistoEnca(String codigoHistoEnca) {
		this.codigoHistoEnca = codigoHistoEnca;
	}

	/**
	 * @return the modalidadTerapia
	 */
	public String getModalidadTerapia() {
		return modalidadTerapia;
	}

	/**
	 * @param modalidadTerapia the modalidadTerapia to set
	 */
	public void setModalidadTerapia(String modalidadTerapia) {
		this.modalidadTerapia = modalidadTerapia;
	}

	

	/**
	 * @return the fechaFinalDialisis
	 */
	public String getFechaFinalDialisis() {
		return fechaFinalDialisis;
	}

	/**
	 * @param fechaFinalDialisis the fechaFinalDialisis to set
	 */
	public void setFechaFinalDialisis(String fechaFinalDialisis) {
		this.fechaFinalDialisis = fechaFinalDialisis;
	}

	/**
	 * @return the horaFinalDialisis
	 */
	public String getHoraFinalDialisis() {
		return horaFinalDialisis;
	}

	/**
	 * @param horaFinalDialisis the horaFinalDialisis to set
	 */
	public void setHoraFinalDialisis(String horaFinalDialisis) {
		this.horaFinalDialisis = horaFinalDialisis;
	}

	

	/**
	 * @return the filtros
	 */
	public ArrayList<HashMap<String, Object>> getFiltros() {
		return filtros;
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(ArrayList<HashMap<String, Object>> filtros) {
		this.filtros = filtros;
	}

	/**
	 * @return the flujosBomba
	 */
	public ArrayList<HashMap<String, Object>> getFlujosBomba() {
		return flujosBomba;
	}

	/**
	 * @param flujosBomba the flujosBomba to set
	 */
	public void setFlujosBomba(ArrayList<HashMap<String, Object>> flujosBomba) {
		this.flujosBomba = flujosBomba;
	}

	/**
	 * @return the flujosDializado
	 */
	public ArrayList<HashMap<String, Object>> getFlujosDializado() {
		return flujosDializado;
	}

	/**
	 * @param flujosDializado the flujosDializado to set
	 */
	public void setFlujosDializado(
			ArrayList<HashMap<String, Object>> flujosDializado) {
		this.flujosDializado = flujosDializado;
	}

	/**
	 * @return the accesosVasculares
	 */
	public ArrayList<HashMap<String, Object>> getAccesosVasculares() {
		return accesosVasculares;
	}

	/**
	 * @param accesosVasculares the accesosVasculares to set
	 */
	public void setAccesosVasculares(
			ArrayList<HashMap<String, Object>> accesosVasculares) {
		this.accesosVasculares = accesosVasculares;
	}

	/**
	 * @return the mapa
	 */
	public HashMap<String, Object> getMapa() {
		return mapa;
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(HashMap<String, Object> mapa) {
		this.mapa = mapa;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getMapa(String key) {
		return mapa.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(String key, Object obj) {
		this.mapa.put(key,obj);
	}

	/**
	 * @return the recambios
	 */
	public ArrayList<HashMap<String, Object>> getRecambios() {
		return recambios;
	}

	/**
	 * @param recambios the recambios to set
	 */
	public void setRecambios(ArrayList<HashMap<String, Object>> recambios) {
		this.recambios = recambios;
	}

	/**
	 * @return the volumenes
	 */
	public ArrayList<HashMap<String, Object>> getVolumenes() {
		return volumenes;
	}

	/**
	 * @param volumenes the volumenes to set
	 */
	public void setVolumenes(ArrayList<HashMap<String, Object>> volumenes) {
		this.volumenes = volumenes;
	}

	/**
	 * @return the profesional
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the hemodialisis
	 */
	public ArrayList<DtoHemodialisis> getHemodialisis() {
		return hemodialisis;
	}

	/**
	 * @param hemodialisis the hemodialisis to set
	 */
	public void setHemodialisis(ArrayList<DtoHemodialisis> hemodialisis) {
		this.hemodialisis = hemodialisis;
	}
	
	/**
	 * Método para obtener el número de volúmenes
	 * @return
	 */
	public int getNumVolumenes()
	{
		return this.volumenes.size();
	}
	
	/**
	 * Método para realizar las validaciones de la pescripcion dialisis
	 * @param errores
	 * @return
	 */
	public ActionErrors validate(ActionErrors errores)
	{
		if(this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoHemodialisis))
		{
			//CAMPO TIEMPO **********************************************
			if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.hemodialisis.get(0).getTiempo()))
				errores.add("",new ActionMessage("errors.caracteresInvalidos","El campo Tiempo (Prescripción Diálisis)"));
			
			//CAMPO PESO SECO*******************************************************
			if(!this.hemodialisis.get(0).getPesoSeco().equals(""))
			{
				if(Utilidades.convertirADouble(this.hemodialisis.get(0).getPesoSeco())>=0)
					errores = validacionDigitosDecimales(this.hemodialisis.get(0).getPesoSeco(), errores, "El campo Peso seco (Prescripción Diálisis)");
				else
					errores.add("", new ActionMessage("errors.float","El campo Peso seco (Prescripción Diálisis)"));
			}
			
			//CAMPO UP *********************************************************
			/**Cambio por tarea 92979 xplanner2008
			 * if(!this.hemodialisis.get(0).getUp().equals("")&&Utilidades.convertirAEntero(this.hemodialisis.get(0).getUp())==ConstantesBD.codigoNuncaValido)
				errores.add("", new ActionMessage("errors.integer","El campo UP (Prescripción Diálisis)"));*/
			
			//CAMPO ANTICUAGULACION ***************************************************
			if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.hemodialisis.get(0).getAnticoagulacion()))
				errores.add("",new ActionMessage("errors.caracteresInvalidos","El campo Anticoagulación (Prescripción Diálisis)"));
			
			//CAMPO PESO PRE*******************************************************************
			if(!this.hemodialisis.get(0).getPesoPre().equals(""))
			{
				if(Utilidades.convertirADouble(this.hemodialisis.get(0).getPesoPre())>=0)
					errores = validacionDigitosDecimales(this.hemodialisis.get(0).getPesoPre(), errores, "El campo Peso pre (Prescripción Diálisis)");
				else
					errores.add("", new ActionMessage("errors.float","El campo Peso pre (Prescripción Diálisis)"));
			}
			//CAMPO PESO POS**********************************************************************
			if(!this.hemodialisis.get(0).getPesoPos().equals(""))
			{
				if(Utilidades.convertirADouble(this.hemodialisis.get(0).getPesoPos())>=0)
					errores = validacionDigitosDecimales(this.hemodialisis.get(0).getPesoPos(), errores, "El campo Peso pos (Prescripción Diálisis)");
				else
					errores.add("", new ActionMessage("errors.float","El campo Peso pos (Prescripción Diálisis)"));
			}
			
			
		}
		else if(this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoCAPD)||
				this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoAPD))
		{
			for(HashMap elementoR:this.recambios)
				for(HashMap elementoV:this.volumenes)
				{
					String valor = this.mapa.get(elementoR.get("codigo")+"_"+elementoV.get("codigo")).toString();
					if(!valor.equals("")&&Utilidades.convertirAEntero(valor)==ConstantesBD.codigoNuncaValido)
						errores.add("", new ActionMessage("errors.integer","El valor en Recambio:"+elementoR.get("nombre")+" y Volumen:"+elementoV.get("nombre")+" (Prescripción Diálisis)"));
				}
		}
		
		return errores;
	}
	
	/**
	 * Método para verificar el número de digitos y decimales de un numero float
	 * @param valor
	 * @param errores
	 * @param mensaje
	 * @return
	 */
	public static ActionErrors validacionDigitosDecimales(String valor,ActionErrors errores,String mensaje)
	{
		//logger.info("VALOR: "+valor);
		//logger.info("INDICE [.] DEL CAMPO VALOR: "+valor.indexOf("[.]"));
		//logger.info("INDICE . DEL CAMPO VALOR: "+valor.indexOf("."));
		if(valor.indexOf(".")>=0)
		{
			String[] vector = valor.split("[.]");
			//logger.info("NUMERO DE ELEMENTOS SPLIT: "+vector.length);
			if(vector[0].length()>3)
				errores.add("",new ActionMessage("errors.numDigitos",mensaje,"3"));
			if(vector[1].length()>2)
				errores.add("",new ActionMessage("errors.numDecimales",mensaje,"2"));
				
		}
		else if(valor.length()>3)
			errores.add("",new ActionMessage("errors.numDigitos",mensaje,"3"));
		
		return errores;
	}
	
	/**
	 * Método que verifica si la hemodialisis tiene registros de enfermería
	 * @return
	 */
	public boolean existeHemodialisisEnfermeria()
	{
		boolean existe = false;
		
		for(DtoHemodialisis hemo:this.hemodialisis)
			if(!hemo.getConsecutivoHemodialisisPadre().equals(""))
				existe =true;
		
		return existe;
	}

	

	/**
	 * @return the finalizado
	 */
	public boolean isFinalizado() {
		return finalizado;
	}

	/**
	 * @param finalizado the finalizado to set
	 */
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	
	/**
	 * Se verifica si la prescripcion de diálisis feu modificada
	 * en el registro de enfermeria
	 * @return
	 */
	public boolean fueModificadoEnEnfermeria()
	{
		boolean modificado = false;
		
		for(DtoPrescripDialFechaHora fechaHora:this.fechasHorasIniciales)
			if(fechaHora.getConsecutivo().equals("")&&!fechaHora.getFechaInicialDialisis().equals("")&&!fechaHora.getHoraInicialDialisis().equals(""))
				modificado = true;
		
		if(!this.finalizado&& this.manejoFinalizado)
			modificado = true;
		
		if(this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoHemodialisis)&&this.isIngresadoFechaHoraInicio())
		{
			if(!this.hemodialisis.get(0).getPesoSeco().equals(this.hemodialisis.get(0).getPesoSecoAnterior())&&
				!this.hemodialisis.get(0).getPesoSeco().equals(""))
				modificado = true;
			
			
			if(this.hemodialisis.get(0).getCodigoFiltro()!=this.hemodialisis.get(0).getCodigoFiltroAnterior()&&
					this.hemodialisis.get(0).getCodigoFiltro()>0)
				modificado = true;
			

			if(!this.hemodialisis.get(0).getUp().equals(this.hemodialisis.get(0).getUpAnterior())&&
					!this.hemodialisis.get(0).getUp().equals(""))
					modificado = true;
			

			if(!this.hemodialisis.get(0).getPesoPre().equals(this.hemodialisis.get(0).getPesoPreAnterior())&&
					!this.hemodialisis.get(0).getPesoPre().equals(""))
					modificado = true;
			

			if(!this.hemodialisis.get(0).getPesoPos().equals(this.hemodialisis.get(0).getPesoPosAnterior())&&
					!this.hemodialisis.get(0).getPesoPos().equals(""))
					modificado = true;
		}
			
		return modificado;
	}
	
	/**
	 * Método que verifica si se ingresó información de la prescripcion
	 * diálisis dependiendo de la  modalidad
	 * @return
	 */
	public boolean ingresoInformacion()
	{
		boolean ingreso = false;
		
		if(this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoHemodialisis))
		{
			if(!this.getHemodialisis().get(0).getTiempo().equals("")||
				!this.getHemodialisis().get(0).getPesoSeco().equals("")||
				this.getHemodialisis().get(0).getCodigoFiltro()!=ConstantesBD.codigoNuncaValido||
				this.getHemodialisis().get(0).getCodigoFlujoBomba()!=ConstantesBD.codigoNuncaValido||
				this.getHemodialisis().get(0).getCodigoFlujoDializado()!=ConstantesBD.codigoNuncaValido||
				!this.getHemodialisis().get(0).getUp().equals("")||
				this.getHemodialisis().get(0).getCodigoAccesoVascular()!=ConstantesBD.codigoNuncaValido||
				!this.getHemodialisis().get(0).getAnticoagulacion().equals("")||
				!this.getHemodialisis().get(0).getPesoPre().equals("")||
				!this.getHemodialisis().get(0).getPesoPos().equals(""))
				ingreso = true;
			
			
		}
		else if(this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoCAPD)||this.modalidadTerapia.equals(ConstantesIntegridadDominio.acronimoAPD))
		{
			for(HashMap elementoR:this.recambios)
				for(HashMap elementoV:this.volumenes)
				{
					String valor = this.mapa.get(elementoR.get("codigo")+"_"+elementoV.get("codigo")).toString();
					if(!valor.equals(""))
						ingreso = true;
				}
		}
		
		return ingreso;
	}

	/**
	 * @return the profesionalFinaliza
	 */
	public UsuarioBasico getProfesionalFinaliza() {
		return profesionalFinaliza;
	}

	/**
	 * @param profesionalFinaliza the profesionalFinaliza to set
	 */
	public void setProfesionalFinaliza(UsuarioBasico profesionalFinaliza) {
		this.profesionalFinaliza = profesionalFinaliza;
	}

	/**
	 * @return the tiposMembrana
	 */
	public ArrayList<HashMap<String, Object>> getTiposMembrana() {
		return tiposMembrana;
	}

	/**
	 * @param tiposMembrana the tiposMembrana to set
	 */
	public void setTiposMembrana(ArrayList<HashMap<String, Object>> tiposMembrana) {
		this.tiposMembrana = tiposMembrana;
	}

	/**
	 * @return the manejoFinalizado
	 */
	public boolean isManejoFinalizado() {
		return manejoFinalizado;
	}

	/**
	 * @param manejoFinalizado the manejoFinalizado to set
	 */
	public void setManejoFinalizado(boolean manejoFinalizado) {
		this.manejoFinalizado = manejoFinalizado;
	}
	
	
	/**
	 * Métood para verificar si sí se ingresó una fecha/hora inicio y habilitar la captura de los datos
	 * @return
	 */
	public boolean isIngresadoFechaHoraInicio()
	{
		boolean ingresado = false;
		
		for(DtoPrescripDialFechaHora fechaHora:this.fechasHorasIniciales)
			if(!fechaHora.getFechaInicialDialisis().equals("")||!fechaHora.getHoraInicialDialisis().equals(""))
				ingresado = true;
		
		return ingresado;
	}

	
}
