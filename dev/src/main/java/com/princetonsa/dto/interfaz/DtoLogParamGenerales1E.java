package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoLogParamGenerales1E implements Serializable
{
	private String consecutivoPk;
	
	private String paramGenerales;
	
private String pathArchivoInterfaz;
	
	private String pathArchivoInconsis;
	
	private String terceroGenFacPar;
	
	private String terceroGenPagPac;
	
	private String cuentaAbonoPac;
	
	private String codConFlEfeMovCxp;
	
	private String codConFlEfeMovCon;
	
	private String realizarCalRetenCxh;
	
	private String realizarCalRestenCxes;
	
	private String realizarCalAutoretFp;
	
	private String realizarCalAutoretFv;
	
	private String realizarCalRetenCxda;
	
	private String realizarCalAutoretCxCC;
	
	private String fechaControlDesmarcar;
	
	private String documentoCruceHi;
	
	private String centroAtencionContable;
	private String codigoCentroAtencionContable;
	private String descripcionCentroAtencionContable;
	private boolean huboInconsistenciaCentroAtencionContable;
	
	private String institucion;
	
	private String fechaModifica;
	
	private String horaModifica;
	
	private String usuarioModifica;
	
	private ArrayList<DtoTiposInterfazDocumentosParam1E> arrayListDtoTiposDocumentos;
	
	private ArrayList<DtoTiposInterfazDocumentosParam1E> inconsistenciasDtoTiposDocumentos;
	
	/**
	 * Agosto 14 - Cambios por documentación
	 */
	//Elementos removidos por documentación el 17 de Noviembre
//	private String centroAtencionContableDRC;
//	private String centroAtencionContableRG;

	
	
	public void reset()
	{
		this.consecutivoPk="";
		this.paramGenerales="";
		this.pathArchivoInconsis = "";
		this.pathArchivoInterfaz = "";
		this.terceroGenPagPac="";
		this.terceroGenFacPar="";
		this.cuentaAbonoPac=ConstantesBD.codigoNuncaValido+"";
		this.codConFlEfeMovCon="";
		this.codConFlEfeMovCxp="";
		this.realizarCalRetenCxh="";
		this.realizarCalRestenCxes="";
		this.realizarCalAutoretFp="";
		this.realizarCalAutoretFv="";
		this.realizarCalRetenCxda = "";
		this.realizarCalAutoretCxCC = "";
		this.fechaControlDesmarcar="";
		this.documentoCruceHi="";
		this.centroAtencionContable="";
		this.codigoCentroAtencionContable = "";
		this.descripcionCentroAtencionContable = "";
		this.huboInconsistenciaCentroAtencionContable = false;
		this.institucion="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.arrayListDtoTiposDocumentos= new ArrayList<DtoTiposInterfazDocumentosParam1E>();
		this.inconsistenciasDtoTiposDocumentos = new ArrayList<DtoTiposInterfazDocumentosParam1E>();
//		this.centroAtencionContableDRC="";
//		this.centroAtencionContableRG="";
	}

	public String getConsecutivoPk() {
		return consecutivoPk;
	}

	public void setConsecutivoPk(String consecutivoPk) {
		this.consecutivoPk = consecutivoPk;
	}

	public String getPathArchivoInterfaz() {
		return pathArchivoInterfaz;
	}

	public void setPathArchivoInterfaz(String pathArchivoInterfaz) {
		this.pathArchivoInterfaz = pathArchivoInterfaz;
	}

	public String getPathArchivoInconsis() {
		return pathArchivoInconsis;
	}

	public void setPathArchivoInconsis(String pathArchivoInconsis) {
		this.pathArchivoInconsis = pathArchivoInconsis;
	}

	public String getTerceroGenFacPar() {
		return terceroGenFacPar;
	}

	public void setTerceroGenFacPar(String terceroGenFacPar) {
		this.terceroGenFacPar = terceroGenFacPar;
	}

	public String getTerceroGenPagPac() {
		return terceroGenPagPac;
	}

	public void setTerceroGenPagPac(String terceroGenPagPac) {
		this.terceroGenPagPac = terceroGenPagPac;
	}

	public String getCuentaAbonoPac() {
		return cuentaAbonoPac;
	}

	public void setCuentaAbonoPac(String cuentaAbonoPac) {
		this.cuentaAbonoPac = cuentaAbonoPac;
	}

	public String getCodConFlEfeMovCxp() {
		return codConFlEfeMovCxp;
	}

	public void setCodConFlEfeMovCxp(String codConFlEfeMovCxp) {
		this.codConFlEfeMovCxp = codConFlEfeMovCxp;
	}

	public String getCodConFlEfeMovCon() {
		return codConFlEfeMovCon;
	}

	public void setCodConFlEfeMovCon(String codConFlEfeMovCon) {
		this.codConFlEfeMovCon = codConFlEfeMovCon;
	}

	public String getRealizarCalRetenCxh() {
		return realizarCalRetenCxh;
	}

	public void setRealizarCalRetenCxh(String realizarCalRetenCxh) {
		this.realizarCalRetenCxh = realizarCalRetenCxh;
	}

	public String getRealizarCalRestenCxes() {
		return realizarCalRestenCxes;
	}

	public void setRealizarCalRestenCxes(String realizarCalRestenCxes) {
		this.realizarCalRestenCxes = realizarCalRestenCxes;
	}

	public String getRealizarCalAutoretFp() {
		return realizarCalAutoretFp;
	}

	public void setRealizarCalAutoretFp(String realizarCalAutoretFp) {
		this.realizarCalAutoretFp = realizarCalAutoretFp;
	}

	public String getRealizarCalAutoretFv() {
		return realizarCalAutoretFv;
	}

	public void setRealizarCalAutoretFv(String realizarCalAutoretFv) {
		this.realizarCalAutoretFv = realizarCalAutoretFv;
	}

	public String getFechaControlDesmarcar() {
		return fechaControlDesmarcar;
	}

	public void setFechaControlDesmarcar(String fechaControlDesmarcar) {
		this.fechaControlDesmarcar = fechaControlDesmarcar;
	}

	public String getDocumentoCruceHi() {
		return documentoCruceHi;
	}

	public void setDocumentoCruceHi(String documentoCruceHi) {
		this.documentoCruceHi = documentoCruceHi;
	}

	public String getCentroAtencionContable() {
		return centroAtencionContable;
	}

	public void setCentroAtencionContable(String centroAtencionContable) {
		this.centroAtencionContable = centroAtencionContable;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public ArrayList<DtoTiposInterfazDocumentosParam1E> getArrayListDtoTiposDocumentos() {
		return arrayListDtoTiposDocumentos;
	}

	public void setArrayListDtoTiposDocumentos(
			ArrayList<DtoTiposInterfazDocumentosParam1E> arrayListDtoTiposDocumentos) {
		this.arrayListDtoTiposDocumentos = arrayListDtoTiposDocumentos;
	}

	/**
	 * @return the codigoCentroAtencionContable
	 */
	public String getCodigoCentroAtencionContable() {
		return codigoCentroAtencionContable;
	}

	/**
	 * @param codigoCentroAtencionContable the codigoCentroAtencionContable to set
	 */
	public void setCodigoCentroAtencionContable(String codigoCentroAtencionContable) {
		this.codigoCentroAtencionContable = codigoCentroAtencionContable;
	}

	/**
	 * @return the descripcionCentroAtencionContable
	 */
	public String getDescripcionCentroAtencionContable() {
		return descripcionCentroAtencionContable;
	}

	/**
	 * @param descripcionCentroAtencionContable the descripcionCentroAtencionContable to set
	 */
	public void setDescripcionCentroAtencionContable(
			String descripcionCentroAtencionContable) {
		this.descripcionCentroAtencionContable = descripcionCentroAtencionContable;
	}

	/**
	 * @return the huboInconsistenciaCentroAtencionContable
	 */
	public boolean isHuboInconsistenciaCentroAtencionContable() {
		return huboInconsistenciaCentroAtencionContable;
	}

	/**
	 * @param huboInconsistenciaCentroAtencionContable the huboInconsistenciaCentroAtencionContable to set
	 */
	public void setHuboInconsistenciaCentroAtencionContable(
			boolean huboInconsistenciaCentroAtencionContable) {
		this.huboInconsistenciaCentroAtencionContable = huboInconsistenciaCentroAtencionContable;
	}

	/**
	 * @return the inconsistenciasDtoTiposDocumentos
	 */
	public ArrayList<DtoTiposInterfazDocumentosParam1E> getInconsistenciasDtoTiposDocumentos() {
		return inconsistenciasDtoTiposDocumentos;
	}

	/**
	 * @param inconsistenciasDtoTiposDocumentos the inconsistenciasDtoTiposDocumentos to set
	 */
	public void setInconsistenciasDtoTiposDocumentos(
			ArrayList<DtoTiposInterfazDocumentosParam1E> inconsistenciasDtoTiposDocumentos) {
		this.inconsistenciasDtoTiposDocumentos = inconsistenciasDtoTiposDocumentos;
	}

	public String getParamGenerales() {
		return paramGenerales;
	}

	public void setParamGenerales(String paramGenerales) {
		this.paramGenerales = paramGenerales;
	}

	/**
	 * @return the realizarCalRetenCxda
	 */
	public String getRealizarCalRetenCxda() {
		return realizarCalRetenCxda;
	}

	/**
	 * @param realizarCalRetenCxda the realizarCalRetenCxda to set
	 */
	public void setRealizarCalRetenCxda(String realizarCalRetenCxda) {
		this.realizarCalRetenCxda = realizarCalRetenCxda;
	}

	/**
	 * @return the realizarCalAutoretCxCC
	 */
	public String getRealizarCalAutoretCxCC() {
		return realizarCalAutoretCxCC;
	}

	/**
	 * @param realizarCalAutoretCxCC the realizarCalAutoretCxCC to set
	 */
	public void setRealizarCalAutoretCxCC(String realizarCalAutoretCxCC) {
		this.realizarCalAutoretCxCC = realizarCalAutoretCxCC;
	}

//	public String getCentroAtencionContableDRC() {
//		return centroAtencionContableDRC;
//	}
//
//	public void setCentroAtencionContableDRC(String centroAtencionContableDRC) {
//		this.centroAtencionContableDRC = centroAtencionContableDRC;
//	}
//
//	public String getCentroAtencionContableRG() {
//		return centroAtencionContableRG;
//	}
//
//	public void setCentroAtencionContableRG(String centroAtencionContableRG) {
//		this.centroAtencionContableRG = centroAtencionContableRG;
//	}
}