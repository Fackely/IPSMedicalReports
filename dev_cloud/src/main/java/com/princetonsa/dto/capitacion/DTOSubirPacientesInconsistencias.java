package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.mundo.PersonaBasica;

/**
 * @author CAmilo Gómez
 */
public class DTOSubirPacientesInconsistencias implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	/*************para Mostrar en Pantalla********/
	private ArrayList<DtoPersonas> listaPersonasBD;	
	private DtoPersonas dtoPersonaArchivoPlano;
	/********************************************/
	
	/**************para guardar en BD*******************************************/
	private ArrayList<DtoInconsistenciasArchivoPlano>listaInconsistenciasCampos;
	private ArrayList<DtoInconsistenciasArchivoPlano>listaInconsistenciasPersona;
	private ArrayList<Integer>lineasConInconsistencias;
	private ArrayList<PersonaBasica> listaPersonaArchivoPlano;
	/***************************************************************************/
	private int contrato;
	private int convenio;	
	private int totalLineasLeidas;
	private int totallineasGrabadas;
		
	
	//*//////
	private String campoInconsistente;///quitar ahora se encuantran en DtoInconsistenciasArchivoPlano
	private String observaciones;///quitar ahora se encuantran en DtoInconsistenciasArchivoPlano
	private boolean personaInconsistente;
	
		
	public void reset()
	{			
		this.listaPersonasBD			=new ArrayList<DtoPersonas>();
		this.personaInconsistente		=false;
		this.setDtoPersonaArchivoPlano(new DtoPersonas());
		this.listaInconsistenciasCampos =new ArrayList<DtoInconsistenciasArchivoPlano>();
		this.listaInconsistenciasPersona=new ArrayList<DtoInconsistenciasArchivoPlano>();
		this.observaciones				="";
		this.campoInconsistente			="";
		this.lineasConInconsistencias	=new ArrayList<Integer>();
		this.listaPersonaArchivoPlano	=new ArrayList<PersonaBasica>();
		this.totalLineasLeidas			=ConstantesBD.codigoNuncaValido;
		this.totallineasGrabadas		=ConstantesBD.codigoNuncaValido;
		this.contrato					=ConstantesBD.codigoNuncaValido;
		this.convenio					=ConstantesBD.codigoNuncaValido;
	}
	
	
	public void setPersonaInconsistente(boolean personaInconsistente) {
		this.personaInconsistente = personaInconsistente;
	}

	public boolean isPersonaInconsistente() {
		return personaInconsistente;
	}	

	public void setListaPersonasBD(ArrayList<DtoPersonas> listaPersonasBD) {
		this.listaPersonasBD = listaPersonasBD;
	}

	public ArrayList<DtoPersonas> getListaPersonasBD() {
		return listaPersonasBD;
	}


	public void setDtoPersonaArchivoPlano(DtoPersonas dtoPersonaArchivoPlano) {
		this.dtoPersonaArchivoPlano = dtoPersonaArchivoPlano;
	}


	public DtoPersonas getDtoPersonaArchivoPlano() {
		return dtoPersonaArchivoPlano;
	}


	public void setCampoInconsistente(String campoInconsistente) {
		this.campoInconsistente = campoInconsistente;
	}


	public String getCampoInconsistente() {
		return campoInconsistente;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setListaInconsistenciasPersona(
			ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciasPersona) {
		this.listaInconsistenciasPersona = listaInconsistenciasPersona;
	}


	public ArrayList<DtoInconsistenciasArchivoPlano> getListaInconsistenciasPersona() {
		return listaInconsistenciasPersona;
	}


	public void setContrato(int contrato) {
		this.contrato = contrato;
	}


	public int getContrato() {
		return contrato;
	}


	public void setLineasConInconsistencias(ArrayList<Integer> lineasConInconsistencias) {
		this.lineasConInconsistencias = lineasConInconsistencias;
	}


	public ArrayList<Integer> getLineasConInconsistencias() {
		return lineasConInconsistencias;
	}


	public void setListaPersonaArchivoPlano(ArrayList<PersonaBasica> listaPersonaArchivoPlano) {
		this.listaPersonaArchivoPlano = listaPersonaArchivoPlano;
	}


	public ArrayList<PersonaBasica> getListaPersonaArchivoPlano() {
		return listaPersonaArchivoPlano;
	}


	public void setListaInconsistenciasCampos(
			ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciasCampos) {
		this.listaInconsistenciasCampos = listaInconsistenciasCampos;
	}


	public ArrayList<DtoInconsistenciasArchivoPlano> getListaInconsistenciasCampos() {
		return listaInconsistenciasCampos;
	}


	public void setTotalLineasLeidas(int totalLineasLeidas) {
		this.totalLineasLeidas = totalLineasLeidas;
	}


	public int getTotalLineasLeidas() {
		return totalLineasLeidas;
	}


	public void setTotallineasGrabadas(int totallineasGrabadas) {
		this.totallineasGrabadas = totallineasGrabadas;
	}


	public int getTotallineasGrabadas() {
		return totallineasGrabadas;
	}


	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}


	public int getConvenio() {
		return convenio;
	}
		
	
}
