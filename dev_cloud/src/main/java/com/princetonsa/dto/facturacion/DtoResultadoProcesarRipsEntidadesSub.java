package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;

public class DtoResultadoProcesarRipsEntidadesSub implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> dtoInconsistenciasArchivos;
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> dtoInconsistenciasCamposReg;
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> dtoValoresCampos;
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> listaAutorizacionesValidas;
	private ActionErrors errores=new ActionErrors();
	private ArrayList<String> nombresArchivoCargue=new ArrayList<String>();
	
	private long registrosArchivoCT=0;
	private long registrosArchivoAF=0;
	private long registrosArchivoAD=0;
	/*private long registrosArchivoAH=0;
	private long registrosArchivoAU=0;*/
	private long registrosArchivoAC=0;
	private long registrosArchivoAP=0;
	private long registrosArchivoAM=0;
	private long registrosArchivoAT=0;
	private long registrosArchivoUS=0;
	
	private long registrosProcesadosArchivoCT=0;
	private long registrosProcesadosArchivoAF=0;
	private long registrosProcesadosArchivoAD=0;
	/*private long registrosProcesadosArchivoAH=0;
	private long registrosProcesadosArchivoAU=0;*/
	private long registrosProcesadosArchivoAC=0;
	private long registrosProcesadosArchivoAP=0;
	private long registrosProcesadosArchivoAM=0;
	private long registrosProcesadosArchivoAT=0;
	private long registrosProcesadosArchivoUS=0;
	
	


	

	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}


	public ActionErrors getErrores() {
		return errores;
	}


	public ArrayList<DtoInconsistenciasRipsEntidadesSub> getDtoInconsistenciasArchivos() {
		return dtoInconsistenciasArchivos;
	}


	public void setDtoInconsistenciasArchivos(
			ArrayList<DtoInconsistenciasRipsEntidadesSub> dtoInconsistenciasArchivos) {
		this.dtoInconsistenciasArchivos = dtoInconsistenciasArchivos;
	}


	public ArrayList<DtoInconsistenciasRipsEntidadesSub> getDtoInconsistenciasCamposReg() {
		return dtoInconsistenciasCamposReg;
	}


	public void setDtoInconsistenciasCamposReg(
			ArrayList<DtoInconsistenciasRipsEntidadesSub> dtoInconsistenciasCamposReg) {
		this.dtoInconsistenciasCamposReg = dtoInconsistenciasCamposReg;
	}


	public long getRegistrosArchivoAF() {
		return registrosArchivoAF;
	}


	public void setRegistrosArchivoAF(long registrosArchivoAF) {
		this.registrosArchivoAF = registrosArchivoAF;
	}


	public long getRegistrosArchivoAD() {
		return registrosArchivoAD;
	}


	public void setRegistrosArchivoAD(long registrosArchivoAD) {
		this.registrosArchivoAD = registrosArchivoAD;
	}


	/*public long getRegistrosArchivoAH() {
		return registrosArchivoAH;
	}


	public void setRegistrosArchivoAH(long registrosArchivoAH) {
		this.registrosArchivoAH = registrosArchivoAH;
	}


	public long getRegistrosArchivoAU() {
		return registrosArchivoAU;
	}


	public void setRegistrosArchivoAU(long registrosArchivoAU) {
		this.registrosArchivoAU = registrosArchivoAU;
	}*/


	public long getRegistrosArchivoAC() {
		return registrosArchivoAC;
	}


	public void setRegistrosArchivoAC(long registrosArchivoAC) {
		this.registrosArchivoAC = registrosArchivoAC;
	}


	public long getRegistrosArchivoAP() {
		return registrosArchivoAP;
	}


	public void setRegistrosArchivoAP(long registrosArchivoAP) {
		this.registrosArchivoAP = registrosArchivoAP;
	}


	public long getRegistrosArchivoAM() {
		return registrosArchivoAM;
	}


	public void setRegistrosArchivoAM(long registrosArchivoAM) {
		this.registrosArchivoAM = registrosArchivoAM;
	}


	public long getRegistrosArchivoAT() {
		return registrosArchivoAT;
	}


	public void setRegistrosArchivoAT(long registrosArchivoAT) {
		this.registrosArchivoAT = registrosArchivoAT;
	}


	public long getRegistrosArchivoUS() {
		return registrosArchivoUS;
	}


	public void setRegistrosArchivoUS(long registrosArchivoUS) {
		this.registrosArchivoUS = registrosArchivoUS;
	}


	public void setRegistrosArchivoCT(long registrosArchivoCT) {
		this.registrosArchivoCT = registrosArchivoCT;
	}


	public long getRegistrosArchivoCT() {
		return registrosArchivoCT;
	}


	public long getRegistrosProcesadosArchivoCT() {
		return registrosProcesadosArchivoCT;
	}


	public void setRegistrosProcesadosArchivoCT(long registrosProcesadosArchivoCT) {
		this.registrosProcesadosArchivoCT = registrosProcesadosArchivoCT;
	}


	public long getRegistrosProcesadosArchivoAF() {
		return registrosProcesadosArchivoAF;
	}


	public void setRegistrosProcesadosArchivoAF(long registrosProcesadosArchivoAF) {
		this.registrosProcesadosArchivoAF = registrosProcesadosArchivoAF;
	}


	public long getRegistrosProcesadosArchivoAD() {
		return registrosProcesadosArchivoAD;
	}


	public void setRegistrosProcesadosArchivoAD(long registrosProcesadosArchivoAD) {
		this.registrosProcesadosArchivoAD = registrosProcesadosArchivoAD;
	}


	/*public long getRegistrosProcesadosArchivoAH() {
		return registrosProcesadosArchivoAH;
	}


	public void setRegistrosProcesadosArchivoAH(long registrosProcesadosArchivoAH) {
		this.registrosProcesadosArchivoAH = registrosProcesadosArchivoAH;
	}


	public long getRegistrosProcesadosArchivoAU() {
		return registrosProcesadosArchivoAU;
	}


	public void setRegistrosProcesadosArchivoAU(long registrosProcesadosArchivoAU) {
		this.registrosProcesadosArchivoAU = registrosProcesadosArchivoAU;
	}*/


	public long getRegistrosProcesadosArchivoAC() {
		return registrosProcesadosArchivoAC;
	}


	public void setRegistrosProcesadosArchivoAC(long registrosProcesadosArchivoAC) {
		this.registrosProcesadosArchivoAC = registrosProcesadosArchivoAC;
	}


	public long getRegistrosProcesadosArchivoAP() {
		return registrosProcesadosArchivoAP;
	}


	public void setRegistrosProcesadosArchivoAP(long registrosProcesadosArchivoAP) {
		this.registrosProcesadosArchivoAP = registrosProcesadosArchivoAP;
	}


	public long getRegistrosProcesadosArchivoAM() {
		return registrosProcesadosArchivoAM;
	}


	public void setRegistrosProcesadosArchivoAM(long registrosProcesadosArchivoAM) {
		this.registrosProcesadosArchivoAM = registrosProcesadosArchivoAM;
	}


	public long getRegistrosProcesadosArchivoAT() {
		return registrosProcesadosArchivoAT;
	}


	public void setRegistrosProcesadosArchivoAT(long registrosProcesadosArchivoAT) {
		this.registrosProcesadosArchivoAT = registrosProcesadosArchivoAT;
	}


	public long getRegistrosProcesadosArchivoUS() {
		return registrosProcesadosArchivoUS;
	}


	public void setRegistrosProcesadosArchivoUS(long registrosProcesadosArchivoUS) {
		this.registrosProcesadosArchivoUS = registrosProcesadosArchivoUS;
	}


	public void setNombresArchivoCargue(ArrayList<String> nombresArchivoCargue) {
		this.nombresArchivoCargue = nombresArchivoCargue;
	}


	public ArrayList<String> getNombresArchivoCargue() {
		return nombresArchivoCargue;
	}


	public void setDtoValoresCampos(ArrayList<DtoInconsistenciasRipsEntidadesSub> dtoValoresCampos) {
		this.dtoValoresCampos = dtoValoresCampos;
	}


	public ArrayList<DtoInconsistenciasRipsEntidadesSub> getDtoValoresCampos() {
		return dtoValoresCampos;
	}


	public void setListaAutorizacionesValidas(
			ArrayList<DtoInconsistenciasRipsEntidadesSub> listaAutorizacionesValidas) {
		this.listaAutorizacionesValidas = listaAutorizacionesValidas;
	}


	public ArrayList<DtoInconsistenciasRipsEntidadesSub> getListaAutorizacionesValidas() {
		return listaAutorizacionesValidas;
	}


	


	
	

}
