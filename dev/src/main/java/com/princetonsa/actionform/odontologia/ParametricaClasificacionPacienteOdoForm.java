package com.princetonsa.actionform.odontologia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.ClasOdoSecDiasPacientes;
import com.servinte.axioma.orm.ClasOdoSecEspe;
import com.servinte.axioma.orm.ClasOdoSecEstadosPres;
import com.servinte.axioma.orm.ClasOdoSecSalDisp;
import com.servinte.axioma.orm.ClasOdoSecSalDispHome;
import com.servinte.axioma.orm.ClasOdoSecTiposCita;
import com.servinte.axioma.orm.ClasificaPacientesOdo;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.FuncionalidadesRestriccion;

import com.servinte.axioma.orm.MotivosCita;

public class ParametricaClasificacionPacienteOdoForm extends ActionForm{
	
	transient Logger logger =Logger.getLogger( ParametricaClasificacionPacienteOdoForm.class);
	private String estado;
	
	/**
	 * Dto con el cual se comunica la JSP
	 */
	private ClasificaPacientesOdo clasificaPacientesOdo=null;
	
	
	
	//variables que traen datos de la BD para mostrar en la jsp
	private List<MotivosCita> listadoMotivosCita;
	private List<Especialidades> listaEspecialidades;
	private List<FuncionalidadesRestriccion> listaFuncionalidadesRestriccion;
	//********************************************************
	
	
	// checks para los campos ***********
	private List<String> motivosCheck;
	private List<String> presupuChecK;
	private List<String> presIndCheck;
	private List<String> tipoCCheck;
	private List<String> estadosCCheck;
	private String migraCheck;
	private String confCheck;
	private String ncConfCheck;
	private String migraCheckn;		
	private String manualConfCheck;
	private String automaticoCheck;
	private List<String> funcionalidadesCheck;
	
	//**********************************
	
	
	
	//checks para las secciones ******
	private String secSaldopacCheck;
	private String secEstadosPresCheck;
	private String secTiposCitaCheck;
	private String secEstadosCitaCheck;
	private String secIndConfCheck;
	private String secEspe;
	private String secServicioOdo;
	private String secRestriccionFuncionalidades;
	private String secNumeroDiasPac;
	//********************************
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//variables para mostrar los acronimos en la jsp *****
	private List<DtoIntegridadDominio> listaConstantes;
	private List<DtoIntegridadDominio> listaConstantesPres;
	private List<DtoIntegridadDominio> listaConstantesPresInd;
	private List<DtoIntegridadDominio> listaConstantesTipoC;
	private List<DtoIntegridadDominio> listaConstantesEstadosC;


	//****************************************************
	
	
	
	// variables para guardar en las tablas ************
	private ClasOdoSecSalDisp clasOdoSecSalDisp;
	private ClasOdoSecEstadosPres clasOdoSecEstadosPres;
	private ClasOdoSecTiposCita clasOdoSecTiposCitas;
	private ClasOdoSecDiasPacientes clasOdoSecDiasPacientes;
	/*
	 * 
	 */

	

	
	private ClasOdoSecEspe clasOdoSecEspe;
	//**************************************************
	
	private String codigoServicio;
	private String nombreServicio;
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{
		
		logger.info("el estado llega hasta aqui"+estado);
		ActionErrors errores=new ActionErrors();
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		
		if (estado.equals("guardar"))
		{
			
			/////////////////VALIDACIONES DEL ENCABEZADO
			if (clasificaPacientesOdo.getNombre().equals(""))
			{
				errores.add("nombre", new ActionMessage ("errors.required","El Campo Nombre de la Clasificación"));
			}
			if (clasificaPacientesOdo.getNomenclatura().equals(""))
			{
				errores.add("nomenclatura", new ActionMessage("errors.required","El campo Nomenclatura"));
			}
			if (clasificaPacientesOdo.getPrioridad()<=0)
			{
				errores.add("prioridad", new ActionMessage("errors.required","El campo Prioridad"));
			}
			//////////FIN VALIDACIONES DE ENCABEZADO
			
			//////////////// VALIDACIONES DE SALDO PACIENTE
			if(UtilidadTexto.getBoolean(this.getSecSaldopacCheck()))
			{
				if(UtilidadTexto.isEmpty(this.clasOdoSecSalDisp.getValorPaciente()))
				{
					errores.add("valPac", new ActionMessage("errors.required","El campo Valor paciente en la Seccion Saldo Paciente."));
				}
				else
				{
					if(this.clasOdoSecSalDisp.getValor()<0)
					{
						errores.add("valor", new ActionMessage("errors.integerMayorIgualQue","El campo Valor en la Seccion Saldo Paciente","0"));
					}
					if(this.clasOdoSecSalDisp.getValorPaciente().equals(ConstantesIntegridadDominio.acronimoEntre))
					{
						if(this.clasOdoSecSalDisp.getValorMax()<0)
						{
							errores.add("valor", new ActionMessage("errors.integerMayorIgualQue","El campo Valor Maximo en la Seccion Saldo Paciente","0"));
						}
						if(this.clasOdoSecSalDisp.getValor()<this.clasOdoSecSalDisp.getValorMax())
						{
							errores.add("valor", new ActionMessage("errors.integerMayorIgualQue","El campo Valor Maximo"," el campo valor Minimo en la Seccion Saldo Paciente"));
						}
					}
				}
			}////////////FIN VALIDACIONES DE SALDO PACIENTE 
			
			
			
			/// VALIDACIONES SECCION  PRESUPUESTO
			if(UtilidadTexto.getBoolean(getSecEstadosPresCheck()))
			{
				
				if(UtilidadTexto.isEmpty(this.clasOdoSecEstadosPres.getEstado()))
					{
						errores.add("estadoPres", new ActionMessage("errors.required","El campo Estado de presupuesto  En la Seccion Estados Presupuesto"));
					}
				
			
				
			}/// FIN VALIDACIONES SECCION PRESUPUESTO
			
			
			
			/// VALIDACIONES SECCION TIPOS CITA 			
			if(UtilidadTexto.getBoolean(getSecTiposCitaCheck()))
			{
				
				logger.info("**************campo tipo Cita"+this.clasOdoSecTiposCitas.getTipoCita());
				
				
								
			}// FIN VALIDACIONES SECCION TIPOS CITA
			
			
			
			
	
		
			
			
			
			
		
		
		
		
		
		
		
		}
		
		
		
		
		
		
		return errores;
	}
	

	/**
	 * 
	 */
	public void reset()
	{
		this.migraCheck="";
		this.confCheck="";
		this.ncConfCheck="";
		this.migraCheckn="";
		this.manualConfCheck="";
		this.automaticoCheck="";
		
	
		
		
		
		
		this.secSaldopacCheck="";
		this.secEstadosPresCheck="";
		this.secTiposCitaCheck="";
		this.secEstadosCitaCheck="";
		this.secIndConfCheck="";
		this.secServicioOdo="";
		this.secRestriccionFuncionalidades="";
		this.secNumeroDiasPac="";
		this.secEspe="";
		
		
		
		
		
		this.listaConstantes=new ArrayList<DtoIntegridadDominio>();
		this.listaConstantesPres= new ArrayList<DtoIntegridadDominio>();
		this.listaConstantesPresInd= new ArrayList <DtoIntegridadDominio>(); 
		this.listaConstantesTipoC= new ArrayList <DtoIntegridadDominio>();
		this.listaConstantesEstadosC =new ArrayList <DtoIntegridadDominio>();
	
		
		
		///inicializo las variables que guardan en las tablas
		this.clasificaPacientesOdo=new ClasificaPacientesOdo();
		this.clasOdoSecSalDisp=new ClasOdoSecSalDisp();
		this.clasOdoSecEstadosPres=new ClasOdoSecEstadosPres();
		this.clasOdoSecTiposCitas=new ClasOdoSecTiposCita();
		this.clasOdoSecDiasPacientes= new ClasOdoSecDiasPacientes();
		this.clasOdoSecEspe= new ClasOdoSecEspe();
		//************** *fin varialbes guardan en las tablas
		
		this.clasificaPacientesOdo.setActivo(ConstantesBD.acronimoSi);
		
		
		
		
		
		

		
		
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
	 * @return the clasificaPacientesOdo
	 */
	public ClasificaPacientesOdo getClasificaPacientesOdo() {
		return clasificaPacientesOdo;
	}


	/**
	 * @param clasificaPacientesOdo the clasificaPacientesOdo to set
	 */
	public void setClasificaPacientesOdo(ClasificaPacientesOdo clasificaPacientesOdo) {
		this.clasificaPacientesOdo = clasificaPacientesOdo;
	}


	/**
	 * @return the listadoMotivosCita
	 */
	public List<MotivosCita> getListadoMotivosCita() {
		return listadoMotivosCita;
	}


	/**
	 * @param listadoMotivosCita the listadoMotivosCita to set
	 */
	public void setListadoMotivosCita(List<MotivosCita> motivosCita) {
		this.listadoMotivosCita = motivosCita;
	}


	/**
	 * @return the motivosCheck
	 */
	public List<String> getMotivosCheck() {
		return motivosCheck;
	}


	/**
	 * @param motivosCheck the motivosCheck to set
	 */
	public void setMotivosCheck(List<String> motivosCheck) {
		this.motivosCheck = motivosCheck;
	}


	/**
	 * @return the listaConstantes
	 */
	public List<DtoIntegridadDominio> getListaConstantes() {
		return listaConstantes;
	}


	/**
	 * @param listaConstantes the listaConstantes to set
	 */
	public void setListaConstantes(List<DtoIntegridadDominio> listaConstantes) {
		this.listaConstantes = listaConstantes;
	}






	/**
	 * @return the clasOdoSecSalDisp
	 */
	public ClasOdoSecSalDisp getClasOdoSecSalDisp() {
		return clasOdoSecSalDisp;
	}


	/**
	 * @param clasOdoSecSalDisp the clasOdoSecSalDisp to set
	 */
	public void setClasOdoSecSalDisp(ClasOdoSecSalDisp clasOdoSecSalDisp) {
		this.clasOdoSecSalDisp = clasOdoSecSalDisp;
	}


	/**
	 * @return the listaConstantesPres
	 */
	public List<DtoIntegridadDominio> getListaConstantesPres() {
		return listaConstantesPres;
	}


	/**
	 * @param listaConstantesPres the listaConstantesPres to set
	 */
	public void setListaConstantesPres(
			List<DtoIntegridadDominio> listaConstantesPres) {
		this.listaConstantesPres = listaConstantesPres;
	}


	/**
	 * @return the clasOdoSecEstadosPres
	 */
	public ClasOdoSecEstadosPres getClasOdoSecEstadosPres() {
		return clasOdoSecEstadosPres;
	}


	/**
	 * @param clasOdoSecEstadosPres the clasOdoSecEstadosPres to set
	 */
	public void setClasOdoSecEstadosPres(ClasOdoSecEstadosPres clasOdoSecEstadosPres) {
		this.clasOdoSecEstadosPres = clasOdoSecEstadosPres;
	}


	/**
	 * @return the presupuChecK
	 */
	public List<String> getPresupuChecK() {
		return presupuChecK;
	}


	/**
	 * @param presupuChecK the presupuChecK to set
	 */
	public void setPresupuChecK(List<String> presupuChecK) {
		this.presupuChecK = presupuChecK;
	}


	/**
	 * @return the listaConstantesPresInd
	 */
	public List<DtoIntegridadDominio> getListaConstantesPresInd() {
		return listaConstantesPresInd;
	}


	/**
	 * @param listaConstantesPresInd the listaConstantesPresInd to set
	 */
	public void setListaConstantesPresInd(
			List<DtoIntegridadDominio> listaConstantesPresInd) {
		this.listaConstantesPresInd = listaConstantesPresInd;
	}


	/**
	 * @return the presIndCheck
	 */
	public List<String> getPresIndCheck() {
		return presIndCheck;
	}


	/**
	 * @param presIndCheck the presIndCheck to set
	 */
	public void setPresIndCheck(List<String> presIndCheck) {
		this.presIndCheck = presIndCheck;
	}


	/**
	 * @return the listaConstantesTipoC
	 */
	public List<DtoIntegridadDominio> getListaConstantesTipoC() {
		return listaConstantesTipoC;
	}


	/**
	 * @param listaConstantesTipoC the listaConstantesTipoC to set
	 */
	public void setListaConstantesTipoC(
			List<DtoIntegridadDominio> listaConstantesTipoC) {
		this.listaConstantesTipoC = listaConstantesTipoC;
	}


	/**
	 * @return the tipoCCheck
	 */
	public List<String> getTipoCCheck() {
		return tipoCCheck;
	}


	/**
	 * @param tipoCCheck the tipoCCheck to set
	 */
	public void setTipoCCheck(List<String> tipoCCheck) {
		this.tipoCCheck = tipoCCheck;
	}


	/**
	 * @return the listaConstantesEstadosC
	 */
	public List<DtoIntegridadDominio> getListaConstantesEstadosC() {
		return listaConstantesEstadosC;
	}


	/**
	 * @param listaConstantesEstadosC the listaConstantesEstadosC to set
	 */
	public void setListaConstantesEstadosC(
			List<DtoIntegridadDominio> listaConstantesEstadosC) {
		this.listaConstantesEstadosC = listaConstantesEstadosC;
	}


	/**
	 * @return the estadosCCheck
	 */
	public List<String> getEstadosCCheck() {
		return estadosCCheck;
	}


	/**
	 * @param estadosCCheck the estadosCCheck to set
	 */
	public void setEstadosCCheck(List<String> estadosCCheck) {
		this.estadosCCheck = estadosCCheck;
	}


	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}


	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}


	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}


	/**
	 * @return the migraCheck
	 */
	public String getMigraCheck() {
		return migraCheck;
	}


	/**
	 * @param migraCheck the migraCheck to set
	 */
	public void setMigraCheck(String migraCheck) {
		this.migraCheck = migraCheck;
	}


	/**
	 * @return the confCheck
	 */
	public String getConfCheck() {
		return confCheck;
	}


	/**
	 * @param confCheck the confCheck to set
	 */
	public void setConfCheck(String confCheck) {
		this.confCheck = confCheck;
	}


	/**
	 * @return the ncConfCheck
	 */
	public String getNcConfCheck() {
		return ncConfCheck;
	}


	/**
	 * @param ncConfCheck the ncConfCheck to set
	 */
	public void setNcConfCheck(String ncConfCheck) {
		this.ncConfCheck = ncConfCheck;
	}


	/**
	 * @return the migraCheckn
	 */
	public String getMigraCheckn() {
		return migraCheckn;
	}


	/**
	 * @param migraCheckn the migraCheckn to set
	 */
	public void setMigraCheckn(String migraCheckn) {
		this.migraCheckn = migraCheckn;
	}


	/**
	 * @return the clasOdoSecDiasPacientes
	 */
	public ClasOdoSecDiasPacientes getClasOdoSecDiasPacientes() {
		return clasOdoSecDiasPacientes;
	}


	/**
	 * @param clasOdoSecDiasPacientes the clasOdoSecDiasPacientes to set
	 */
	public void setClasOdoSecDiasPacientes(
			ClasOdoSecDiasPacientes clasOdoSecDiasPacientes) {
		this.clasOdoSecDiasPacientes = clasOdoSecDiasPacientes;
	}


	/**
	 * @return the manualConfCheck
	 */
	public String getManualConfCheck() {
		return manualConfCheck;
	}


	/**
	 * @param manualConfCheck the manualConfCheck to set
	 */
	public void setManualConfCheck(String manualConfCheck) {
		this.manualConfCheck = manualConfCheck;
	}


	/**
	 * @return the automaticoCheck
	 */
	public String getAutomaticoCheck() {
		return automaticoCheck;
	}


	/**
	 * @param automaticoCheck the automaticoCheck to set
	 */
	public void setAutomaticoCheck(String automaticoCheck) {
		this.automaticoCheck = automaticoCheck;
	}


	/**
	 * @return the listaEspecialidades
	 */
	public List<Especialidades> getListaEspecialidades() {
		return listaEspecialidades;
	}


	/**
	 * @param listaEspecialidades the listaEspecialidades to set
	 */
	public void setListaEspecialidades(List<Especialidades> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}


	/**
	 * @return the clasOdoSecEspe
	 */
	public ClasOdoSecEspe getClasOdoSecEspe() {
		return clasOdoSecEspe;
	}


	/**
	 * @param clasOdoSecEspe the clasOdoSecEspe to set
	 */
	public void setClasOdoSecEspe(ClasOdoSecEspe clasOdoSecEspe) {
		this.clasOdoSecEspe = clasOdoSecEspe;
	}


	
	/**
	 * @return the funcionalidadesCheck
	 */
	public List<String> getFuncionalidadesCheck() {
		return funcionalidadesCheck;
	}


	/**
	 * @param funcionalidadesCheck the funcionalidadesCheck to set
	 */
	public void setFuncionalidadesCheck(List<String> funcionalidadesCheck) {
		this.funcionalidadesCheck = funcionalidadesCheck;
	}


	/**
	 * @return the listaFuncionalidadesRestriccion
	 */
	public List<FuncionalidadesRestriccion> getListaFuncionalidadesRestriccion() {
		return listaFuncionalidadesRestriccion;
	}


	/**
	 * @param listaFuncionalidadesRestriccion the listaFuncionalidadesRestriccion to set
	 */
	public void setListaFuncionalidadesRestriccion(
			List<FuncionalidadesRestriccion> listaFuncionalidadesRestriccion) {
		this.listaFuncionalidadesRestriccion = listaFuncionalidadesRestriccion;
	}


	/**
	 * @return the secSaldopacCheck
	 */
	public String getSecSaldopacCheck() {
		return secSaldopacCheck;
	}


	/**
	 * @param secSaldopacCheck the secSaldopacCheck to set
	 */
	public void setSecSaldopacCheck(String secSaldopacCheck) {
		this.secSaldopacCheck = secSaldopacCheck;
	}


	/**
	 * @return the secEstadosPresCheck
	 */
	public String getSecEstadosPresCheck() {
		return secEstadosPresCheck;
	}


	/**
	 * @param secEstadosPresCheck the secEstadosPresCheck to set
	 */
	public void setSecEstadosPresCheck(String secEstadosPresCheck) {
		this.secEstadosPresCheck = secEstadosPresCheck;
	}


	/**
	 * @return the secTiposCitaCheck
	 */
	public String getSecTiposCitaCheck() {
		return secTiposCitaCheck;
	}


	/**
	 * @param secTiposCitaCheck the secTiposCitaCheck to set
	 */
	public void setSecTiposCitaCheck(String secTiposCitaCheck) {
		this.secTiposCitaCheck = secTiposCitaCheck;
	}


	/**
	 * @return the secEstadosCitaCheck
	 */
	public String getSecEstadosCitaCheck() {
		return secEstadosCitaCheck;
	}


	/**
	 * @param secEstadosCitaCheck the secEstadosCitaCheck to set
	 */
	public void setSecEstadosCitaCheck(String secEstadosCitaCheck) {
		this.secEstadosCitaCheck = secEstadosCitaCheck;
	}


	/**
	 * @return the secIndConfCheck
	 */
	public String getSecIndConfCheck() {
		return secIndConfCheck;
	}


	/**
	 * @param secIndConfCheck the secIndConfCheck to set
	 */
	public void setSecIndConfCheck(String secIndConfCheck) {
		this.secIndConfCheck = secIndConfCheck;
	}


	/**
	 * @return the secServicioOdo
	 */
	public String getSecServicioOdo() {
		return secServicioOdo;
	}


	/**
	 * @param secServicioOdo the secServicioOdo to set
	 */
	public void setSecServicioOdo(String secServicioOdo) {
		this.secServicioOdo = secServicioOdo;
	}


	/**
	 * @return the secRestriccionFuncionalidades
	 */
	public String getSecRestriccionFuncionalidades() {
		return secRestriccionFuncionalidades;
	}


	/**
	 * @param secRestriccionFuncionalidades the secRestriccionFuncionalidades to set
	 */
	public void setSecRestriccionFuncionalidades(
			String secRestriccionFuncionalidades) {
		this.secRestriccionFuncionalidades = secRestriccionFuncionalidades;
	}




	/**
	 * @return the secEspe
	 */
	public String getSecEspe() {
		return secEspe;
	}


	/**
	 * @param secEspe the secEspe to set
	 */
	public void setSecEspe(String secEspe) {
		this.secEspe = secEspe;
	}


	/**
	 * @return the secNumeroDiasPac
	 */
	public String getSecNumeroDiasPac() {
		return secNumeroDiasPac;
	}


	/**
	 * @param secNumeroDiasPac the secNumeroDiasPac to set
	 */
	public void setSecNumeroDiasPac(String secNumeroDiasPac) {
		this.secNumeroDiasPac = secNumeroDiasPac;
	}


	/**
	 * @return the clasOdoSecTiposCitas
	 */
	public ClasOdoSecTiposCita getClasOdoSecTiposCitas() {
		return clasOdoSecTiposCitas;
	}


	/**
	 * @param clasOdoSecTiposCitas the clasOdoSecTiposCitas to set
	 */
	public void setClasOdoSecTiposCitas(ClasOdoSecTiposCita clasOdoSecTiposCitas) {
		this.clasOdoSecTiposCitas = clasOdoSecTiposCitas;
	}




	
	
	
}
