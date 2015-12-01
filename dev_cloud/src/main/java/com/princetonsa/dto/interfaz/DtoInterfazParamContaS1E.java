package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;


public class DtoInterfazParamContaS1E implements Serializable
{
	private String consecutivoPk;
	
	private String pathArchivoInterfaz;
	
	private String pathArchivoInconsis;
	
	private String terceroGenFacPar;
	private String nitTerceroGenFacPar;
	private String nombreTerceroGenFacPar;
	
	private String terceroGenPagPac;
	private String nitTerceroGenPagPac;
	private String nombreTerceroGenPagPac;
	
	private String cuentaAbonoPac;
	private boolean huboInconsistenciaCuentaAbonoPac;
	
	private String codConFlEfeMovCxp;	 
	private boolean huboInconsistenciaCodConFlEfeMovCxp;
	
	private String codConFlEfeMovCon;
	private boolean huboInconsistenciaCodConFlEfeMovCon;
	
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
	
	private String unidMovCxcCap;
	
	private ArrayList<DtoTiposInterfazDocumentosParam1E> arrayListDtoTiposDocumentos;
	
	private ArrayList<DtoTiposInterfazDocumentosParam1E> inconsistenciasDtoTiposDocumentos;
	
	//**************************************
	// Anexo 823 Cambios Funcionalidad
	/**
	 * ArrayList Concepto Param 1E
	 */
	private ArrayList<DtoConceptosParam1E> arrayConceptoParam1E = new ArrayList<DtoConceptosParam1E>(); 
	//**************************************
	
	/**
	 * Registro incosnsistencias de los campos de la institucion
	 */
	private boolean huboInconsistenciaCompania;
	private boolean huboInconsistenciaIdentificacionInstitucion;
	
	/**
	 * Agosto 14 - Cambios por documentación
	 */
	
	/**
	 * Noviembre 17 Cambio realizado por documentación - Se solicita dejar un solo centro contable y no 3 como se propuso inicialmente
	 */
//	private String centroAtencionContableDRC;
//	private String codigoCentroAtencionDRC;
//	private String descCentroAtencionDRC;
//	private boolean huboInconsistenciaCentroAtencionDRC;
//	private String centroAtencionContableRG;
//	private String codigoCentroAtencionRG;
//	private String descCentroAtencionRG;
	private boolean huboInconsistenciaCentroAtencionRG;
	
	private ArrayList<DtoEventosParam1E> eventos = new ArrayList<DtoEventosParam1E>();
	private boolean huboInconsistenciaEventoFacturasPaciente;
	private boolean huboInconsistenciaEventoAuditoriaFacturas;
	private boolean huboInconsistenciaEventoCuentasCobroCartera;
	private boolean huboInconsistenciaEventoRadicacionCuentasCobro;
	private boolean huboInconsistenciaEventoInactivacionFacturasCuentaCobro;
	private boolean huboInconsistenciaEventoDevolucionCuentasCobro;
	private boolean huboInconsistenciaEventoRegistroGlosas;
	private boolean huboInconsistenciaEventoRegistroGlosasDevolucion;
	private boolean huboInconsistenciaEventoRegistroRespuestaGlosas;
	private boolean huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas;
	private boolean huboInconsistenciaEventoRadicacionRespuestaGlosas;
	private boolean huboInconsistenciaNEventoFacturasPaciente;
	private boolean huboInconsistenciaNEventoAuditoriaFacturas;
	private boolean huboInconsistenciaNEventoCuentasCobroCartera;
	private boolean huboInconsistenciaNEventoRadicacionCuentasCobro;
	private boolean huboInconsistenciaNEventoInactivacionFacturasCuentaCobro;
	private boolean huboInconsistenciaNEventoDevolucionCuentasCobro;
	private boolean huboInconsistenciaNEventoRegistroGlosas;
	private boolean huboInconsistenciaNEventoRegistroGlosasDevolucion;
	private boolean huboInconsistenciaNEventoRegistroRespuestaGlosas;
	private boolean huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas;
	private boolean huboInconsistenciaNEventoRadicacionRespuestaGlosas;
	private boolean huboInconsistenciaCentroCostoTesoreria;

	private String codigoCentroCostoTesoreria;
	private String codigoInterfazCentroCostoTesoreria;
	private String centroCostoTesoreria;
	
	
	public DtoInterfazParamContaS1E()
	{
		reset();
	}
	
	public void reset()
	{
		this.consecutivoPk="";
		this.pathArchivoInconsis = "";
		this.pathArchivoInterfaz = "";
		this.terceroGenPagPac="";
		this.nitTerceroGenPagPac = "";
		this.terceroGenFacPar="";
		this.nitTerceroGenFacPar = "";
		this.cuentaAbonoPac=ConstantesBD.codigoNuncaValido+"";
		this.huboInconsistenciaCuentaAbonoPac = false;
		this.codConFlEfeMovCon="";
		this.huboInconsistenciaCodConFlEfeMovCon = false;
		this.codConFlEfeMovCxp="";
		this.huboInconsistenciaCodConFlEfeMovCxp = false;
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
		this.unidMovCxcCap="";
		this.huboInconsistenciaCentroAtencionContable = false;
		this.institucion="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.arrayListDtoTiposDocumentos= new ArrayList<DtoTiposInterfazDocumentosParam1E>();
		this.inconsistenciasDtoTiposDocumentos = new ArrayList<DtoTiposInterfazDocumentosParam1E>();
		this.nombreTerceroGenPagPac="";
		this.nombreTerceroGenFacPar="";
		this.huboInconsistenciaCompania = false;
		this.huboInconsistenciaIdentificacionInstitucion = false;
		this.arrayConceptoParam1E = new ArrayList<DtoConceptosParam1E>();
//		this.centroAtencionContableDRC="";
//		this.codigoCentroAtencionDRC="";
//		this.descCentroAtencionDRC="";
//		this.huboInconsistenciaCentroAtencionDRC = false;
//		this.centroAtencionContableRG="";
//		this.codigoCentroAtencionRG="";
//		this.descCentroAtencionRG="";
		this.huboInconsistenciaCentroAtencionRG = false;
		this.eventos = new ArrayList<DtoEventosParam1E>();
		this.huboInconsistenciaEventoFacturasPaciente = false;
		this.huboInconsistenciaEventoAuditoriaFacturas = false;
		this.huboInconsistenciaEventoCuentasCobroCartera = false;
		this.huboInconsistenciaEventoRadicacionCuentasCobro = false;
		this.huboInconsistenciaEventoInactivacionFacturasCuentaCobro = false;
		this.huboInconsistenciaEventoDevolucionCuentasCobro = false;
		this.huboInconsistenciaEventoRegistroGlosas = false;
		this.huboInconsistenciaEventoRegistroGlosasDevolucion = false;
		this.huboInconsistenciaEventoRegistroRespuestaGlosas = false;
		this.huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas = false;
		this.huboInconsistenciaEventoRadicacionRespuestaGlosas = false;
		this.huboInconsistenciaNEventoFacturasPaciente=false;
		this.huboInconsistenciaNEventoAuditoriaFacturas=false;
		this.huboInconsistenciaNEventoCuentasCobroCartera=false;
		this.huboInconsistenciaNEventoRadicacionCuentasCobro=false;
		this.huboInconsistenciaNEventoInactivacionFacturasCuentaCobro=false;
		this.huboInconsistenciaNEventoDevolucionCuentasCobro=false;
		this.huboInconsistenciaNEventoRegistroGlosas=false;
		this.huboInconsistenciaNEventoRegistroGlosasDevolucion=false;
		this.huboInconsistenciaNEventoRegistroRespuestaGlosas=false;
		this.huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas=false;
		this.huboInconsistenciaNEventoRadicacionRespuestaGlosas=false;
		this.setHuboInconsistenciaCentroCostoTesoreria(false);
		this.setCodigoCentroCostoTesoreria(ConstantesBD.codigoNuncaValido+"");
		this.setCodigoInterfazCentroCostoTesoreria("");
		this.setCentroCostoTesoreria("");
		
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

	/**huboInconsistenciaCuentaAbonoPac
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
	
	
	/**
	 * Método para obtener el indicativo del tipo de documento
	 * @param tipoDocumento
	 * @return
	 */
	public boolean isHuboInconsistenciaIndTipoDocumento(String tipoDocumento,String acronimoTipoMovimiento)
	{
		boolean exito = false;
		
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(
				tipo.isHuboInconsistenciaIndTipoDocumento()&&
				tipo.getTipoDocumento().equals(tipoDocumento)&&
				(acronimoTipoMovimiento.equals("")||tipo.getTipoMovimiento().equals(acronimoTipoMovimiento))
			)
			{
				exito = true;
			}
		}
		return exito;
	}
	
	/**
	 * Método para obtener el indicativo de la Unidad Funcional Estandar
	 * @param tipoDocumento
	 * @return
	 */
	public boolean isHuboInconsistenciaUnidadFuncionalEstandar(String tipoDocumento)
	{
		boolean exito = false;
		
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.isHuboInconsistenciaUnidadFuncionalEstandar()&&tipo.getTipoDocumento().equals(tipoDocumento))
			{
				exito = true;
			}
		}
		return exito;
	}
	
	/**
	 * Método para obtener el indicativo de la Unidad Funcional Estandar
	 * @param tipoDocumento
	 * @return
	 */
	public boolean isHuboInconsistenciaTipoDocCruce(String tipoDocumento)
	{
		boolean exito = false;
		
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.isHuboInconsistenciaTipoDocCruce()&&tipo.getTipoDocumento().equals(tipoDocumento))
			{
				exito = true;
			}
		}
		return exito;
	}
	
	/**
	 * Método para marcar la inconsisntecia de ind tipo documneto para un tipo de documento específico
	 * @param tipoDocumento
	 */
	public void setHuboInconsistenciaIndTipoDocumento(String tipoDocumento,String acronimoTipoMovimiento)
	{
		boolean existeTipoDocumento = false;
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.getTipoDocumento().equals(tipoDocumento)&&(acronimoTipoMovimiento.equals("")||tipo.getTipoMovimiento().equals(acronimoTipoMovimiento)))
			{
				tipo.setHuboInconsistenciaIndTipoDocumento(true);
				existeTipoDocumento = true;
			}
		}
		
		if(!existeTipoDocumento)
		{
			DtoTiposInterfazDocumentosParam1E tipo = new DtoTiposInterfazDocumentosParam1E();
			tipo.setTipoDocumento(tipoDocumento);
			tipo.setTipoMovimiento(acronimoTipoMovimiento);
			tipo.setHuboInconsistenciaIndTipoDocumento(true);
			this.inconsistenciasDtoTiposDocumentos.add(tipo);
		}
	}
	
	/**
	 * Método para marcar la inconsisntecia de ind tipo documneto para un tipo de documento específico
	 * @param tipoDocumento
	 */
	public void eliminarHuboInconsistenciaIndTipoDocumento(String tipoDocumento,String acronimoTipoMovimiento)
	{
		boolean existeTipoDocumento = false;
		int i = 0, posicion = 0;
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.getTipoDocumento().equals(tipoDocumento)&&(acronimoTipoMovimiento.equals("")||tipo.getTipoMovimiento().equals(acronimoTipoMovimiento)))
			{
				tipo.setHuboInconsistenciaIndTipoDocumento(true);
				existeTipoDocumento = true;
				posicion = i;
			}
			i++;
		}
		
		if(existeTipoDocumento)
		{
			
			this.inconsistenciasDtoTiposDocumentos.remove(posicion);
		}
	}
	
	/**
	 * Método para marcar la inconsisntecia de Unidad Funcional Estandar para un tipo de documento específico
	 * @param tipoDocumento
	 */
	public void setHuboInconsistenciaUnidadFuncionalEstandar(String tipoDocumento)
	{
		boolean existeTipoDocumento = false;
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.getTipoDocumento().equals(tipoDocumento))
			{
				tipo.setHuboInconsistenciaUnidadFuncionalEstandar(true);
				existeTipoDocumento = true;
			}
		}
		
		if(!existeTipoDocumento)
		{
			DtoTiposInterfazDocumentosParam1E tipo = new DtoTiposInterfazDocumentosParam1E();
			tipo.setTipoDocumento(tipoDocumento);
			tipo.setHuboInconsistenciaUnidadFuncionalEstandar(true);
			this.inconsistenciasDtoTiposDocumentos.add(tipo);
		}
	}
	
	/**
	 * Método para marcar la inconsisntecia de Unidad Funcional Estandar para un tipo de documento específico
	 * @param tipoDocumento
	 */
	public void setHuboInconsistenciaTipoDocCruce(String tipoDocumento)
	{
		boolean existeTipoDocumento = false;
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.getTipoDocumento().equals(tipoDocumento))
			{
				tipo.setHuboInconsistenciaTipoDocCruce(true);
				existeTipoDocumento = true;
			}
		}
		
		if(!existeTipoDocumento)
		{
			DtoTiposInterfazDocumentosParam1E tipo = new DtoTiposInterfazDocumentosParam1E();
			tipo.setTipoDocumento(tipoDocumento);
			tipo.setHuboInconsistenciaUnidadFuncionalEstandar(true);
			this.inconsistenciasDtoTiposDocumentos.add(tipo);
		}
	}
	
	/**
	 * Método para obtener el indicativo del tipo de documento
	 * @param tipoDocumento
	 * @param acronimoTipoMovimiento 
	 * @return
	 */
	public boolean isHuboInconsistenciaTipoConsecutivo(String tipoDocumento, String acronimoTipoMovimiento)
	{
		boolean exito = false;
		
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(
				tipo.isHuboInconsistenciaCodigoTipoConsecutivo()&&
				tipo.getTipoDocumento().equals(tipoDocumento)&&
				(acronimoTipoMovimiento.equals("")||tipo.getTipoMovimiento().equals(acronimoTipoMovimiento))
			)
			{
				exito = true;
			}
		}
		return exito;
	}
	
	/**
	 * Método para marcar la inconsisntecia de ind tipo documneto para un tipo de documento específico
	 * @param tipoDocumento
	 * @param acronimoTipoMovimiento 
	 */
	public void setHuboInconsistenciaTipoConsecutivo(String tipoDocumento, String acronimoTipoMovimiento)
	{
		boolean existeTipoDocumento = false;
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.getTipoDocumento().equals(tipoDocumento)&&(acronimoTipoMovimiento.equals("")||tipo.getTipoMovimiento().equals(acronimoTipoMovimiento)))
			{
				tipo.setHuboInconsistenciaCodigoTipoConsecutivo(true);
				existeTipoDocumento = true;
			}
		}
		
		if(!existeTipoDocumento)
		{
			DtoTiposInterfazDocumentosParam1E tipo = new DtoTiposInterfazDocumentosParam1E();
			tipo.setTipoDocumento(tipoDocumento);
			tipo.setTipoMovimiento(acronimoTipoMovimiento);
			tipo.setHuboInconsistenciaCodigoTipoConsecutivo(true);
			this.inconsistenciasDtoTiposDocumentos.add(tipo);
		}
	}
	
	/**
	 * Método para marcar la inconsisntecia de ind tipo documneto para un tipo de documento específico
	 * @param tipoDocumento
	 * @param acronimoTipoMovimiento 
	 */
	public void eliminarHuboInconsistenciaTipoConsecutivo(String tipoDocumento, String acronimoTipoMovimiento)
	{
		boolean existeTipoDocumento = false;
		int i=0, posicion = 0;
		for(DtoTiposInterfazDocumentosParam1E tipo:this.inconsistenciasDtoTiposDocumentos)
		{
			if(tipo.getTipoDocumento().equals(tipoDocumento)&&(acronimoTipoMovimiento.equals("")||tipo.getTipoMovimiento().equals(acronimoTipoMovimiento)))
			{
				tipo.setHuboInconsistenciaCodigoTipoConsecutivo(true);
				existeTipoDocumento = true;
				posicion = i;
			}
			i++;
		}
		
		if(existeTipoDocumento)
		{
			
			this.inconsistenciasDtoTiposDocumentos.remove(posicion);
		}
	}

	/**
	 * @return the nitTerceroGenFacPar
	 */
	public String getNitTerceroGenFacPar() {
		return nitTerceroGenFacPar;
	}

	/**
	 * @param nitTerceroGenFacPar the nitTerceroGenFacPar to set
	 */
	public void setNitTerceroGenFacPar(String nitTerceroGenFacPar) {
		this.nitTerceroGenFacPar = nitTerceroGenFacPar;
	}

	/**
	 * @return the nitTerceroGenPagPac
	 */
	public String getNitTerceroGenPagPac() {
		return nitTerceroGenPagPac;
	}

	/**
	 * @param nitTerceroGenPagPac the nitTerceroGenPagPac to set
	 */
	public void setNitTerceroGenPagPac(String nitTerceroGenPagPac) {
		this.nitTerceroGenPagPac = nitTerceroGenPagPac;
	}

	public boolean isHuboInconsistenciaCuentaAbonoPac() {
		return huboInconsistenciaCuentaAbonoPac;
	}

	public void setHuboInconsistenciaCuentaAbonoPac(
			boolean huboInconsistenciaCuentaAbonoPac) {
		this.huboInconsistenciaCuentaAbonoPac = huboInconsistenciaCuentaAbonoPac;
	}

	public boolean isHuboInconsistenciaCodConFlEfeMovCon() {
		return huboInconsistenciaCodConFlEfeMovCon;
	}

	public void setHuboInconsistenciaCodConFlEfeMovCon(
			boolean huboInconsistenciaCodConFlEfeMovCon) {
		this.huboInconsistenciaCodConFlEfeMovCon = huboInconsistenciaCodConFlEfeMovCon;
	}

	public String getNombreTerceroGenFacPar() {
		return nombreTerceroGenFacPar;
	}

	public void setNombreTerceroGenFacPar(String nombreTerceroGenFacPar) {
		this.nombreTerceroGenFacPar = nombreTerceroGenFacPar;
	}

	public String getNombreTerceroGenPagPac() {
		return nombreTerceroGenPagPac;
	}

	public void setNombreTerceroGenPagPac(String nombreTerceroGenPagPac) {
		this.nombreTerceroGenPagPac = nombreTerceroGenPagPac;
	}
	public boolean isHuboInconsistenciaCompania() {
		return huboInconsistenciaCompania;
	}

	public void setHuboInconsistenciaCompania(boolean huboInconsistenciaCompania) {
		this.huboInconsistenciaCompania = huboInconsistenciaCompania;
	}

	/**
	 * @return the huboInconsistenciaIdentificacionInstitucion
	 */
	public boolean isHuboInconsistenciaIdentificacionInstitucion() {
		return huboInconsistenciaIdentificacionInstitucion;
	}

	/**
	 * @param huboInconsistenciaIdentificacionInstitucion the huboInconsistenciaIdentificacionInstitucion to set
	 */
	public void setHuboInconsistenciaIdentificacionInstitucion(
			boolean huboInconsistenciaIdentificacionInstitucion) {
		this.huboInconsistenciaIdentificacionInstitucion = huboInconsistenciaIdentificacionInstitucion;
	}

	/**
	 * @return the huboInconsistenciaCodConFlEfeMovCxp
	 */
	public boolean isHuboInconsistenciaCodConFlEfeMovCxp() {
		return huboInconsistenciaCodConFlEfeMovCxp;
	}

	/**
	 * @param huboInconsistenciaCodConFlEfeMovCxp the huboInconsistenciaCodConFlEfeMovCxp to set
	 */
	public void setHuboInconsistenciaCodConFlEfeMovCxp(
			boolean huboInconsistenciaCodConFlEfeMovCxp) {
		this.huboInconsistenciaCodConFlEfeMovCxp = huboInconsistenciaCodConFlEfeMovCxp;
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

	/**
	 * @return the arrayConceptoParam1E
	 */
	public ArrayList<DtoConceptosParam1E> getArrayConceptoParam1E() {
		return arrayConceptoParam1E;
	}

	/**
	 * @param arrayConceptoParam1E the arrayConceptoParam1E to set
	 */
	public void setArrayConceptoParam1E(
			ArrayList<DtoConceptosParam1E> arrayConceptoParam1E) {
		this.arrayConceptoParam1E = arrayConceptoParam1E;
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
//
//	public String getCodigoCentroAtencionDRC() {
//		return codigoCentroAtencionDRC;
//	}
//
//	public void setCodigoCentroAtencionDRC(String codigoCentroAtencionDRC) {
//		this.codigoCentroAtencionDRC = codigoCentroAtencionDRC;
//	}
//
//	public String getDescCentroAtencionDRC() {
//		return descCentroAtencionDRC;
//	}
//
//	public void setDescCentroAtencionDRC(String descCentroAtencionDRC) {
//		this.descCentroAtencionDRC = descCentroAtencionDRC;
//	}
//
//	public String getCodigoCentroAtencionRG() {
//		return codigoCentroAtencionRG;
//	}
//
//	public void setCodigoCentroAtencionRG(String codigoCentroAtencionRG) {
//		this.codigoCentroAtencionRG = codigoCentroAtencionRG;
//	}
//
//	public String getDescCentroAtencionRG() {
//		return descCentroAtencionRG;
//	}
//
//	public void setDescCentroAtencionRG(String descCentroAtencionRG) {
//		this.descCentroAtencionRG = descCentroAtencionRG;
//	}
//
//	/**
//	 * @return the huboInconsistenciaCentroAtencionDRC
//	 */
//	public boolean isHuboInconsistenciaCentroAtencionDRC() {
//		return huboInconsistenciaCentroAtencionDRC;
//	}
//
//	/**
//	 * @param huboInconsistenciaCentroAtencionDRC the huboInconsistenciaCentroAtencionDRC to set
//	 */
//	public void setHuboInconsistenciaCentroAtencionDRC(
//			boolean huboInconsistenciaCentroAtencionDRC) {
//		this.huboInconsistenciaCentroAtencionDRC = huboInconsistenciaCentroAtencionDRC;
//	}

	/**
	 * @return the huboInconsistenciaCentroAtencionRG
	 */
	public boolean isHuboInconsistenciaCentroAtencionRG() {
		return huboInconsistenciaCentroAtencionRG;
	}

	/**
	 * @param huboInconsistenciaCentroAtencionRG the huboInconsistenciaCentroAtencionRG to set
	 */
	public void setHuboInconsistenciaCentroAtencionRG(
			boolean huboInconsistenciaCentroAtencionRG) {
		this.huboInconsistenciaCentroAtencionRG = huboInconsistenciaCentroAtencionRG;
	}

	/**
	 * @return the eventos
	 */
	public ArrayList<DtoEventosParam1E> getEventos() {
		return eventos;
	}

	/**
	 * @param eventos the eventos to set
	 */
	public void setEventos(ArrayList<DtoEventosParam1E> eventos) {
		this.eventos = eventos;
	}

	/**
	 * @return the huboInconsistenciaEventoFacturasPaciente
	 */
	public boolean isHuboInconsistenciaEventoFacturasPaciente() {
		return huboInconsistenciaEventoFacturasPaciente;
	}

	/**
	 * @param huboInconsistenciaEventoFacturasPaciente the huboInconsistenciaEventoFacturasPaciente to set
	 */
	public void setHuboInconsistenciaEventoFacturasPaciente(
			boolean huboInconsistenciaEventoFacturasPaciente) {
		this.huboInconsistenciaEventoFacturasPaciente = huboInconsistenciaEventoFacturasPaciente;
	}

	/**
	 * @return the huboInconsistenciaEventoAuditoriaFacturas
	 */
	public boolean isHuboInconsistenciaEventoAuditoriaFacturas() {
		return huboInconsistenciaEventoAuditoriaFacturas;
	}

	/**
	 * @param huboInconsistenciaEventoAuditoriaFacturas the huboInconsistenciaEventoAuditoriaFacturas to set
	 */
	public void setHuboInconsistenciaEventoAuditoriaFacturas(
			boolean huboInconsistenciaEventoAuditoriaFacturas) {
		this.huboInconsistenciaEventoAuditoriaFacturas = huboInconsistenciaEventoAuditoriaFacturas;
	}

	/**
	 * @return the huboInconsistenciaEventoCuentasCobroCartera
	 */
	public boolean isHuboInconsistenciaEventoCuentasCobroCartera() {
		return huboInconsistenciaEventoCuentasCobroCartera;
	}

	/**
	 * @param huboInconsistenciaEventoCuentasCobroCartera the huboInconsistenciaEventoCuentasCobroCartera to set
	 */
	public void setHuboInconsistenciaEventoCuentasCobroCartera(
			boolean huboInconsistenciaEventoCuentasCobroCartera) {
		this.huboInconsistenciaEventoCuentasCobroCartera = huboInconsistenciaEventoCuentasCobroCartera;
	}

	/**
	 * @return the huboInconsistenciaEventoRadicacionCuentasCobro
	 */
	public boolean isHuboInconsistenciaEventoRadicacionCuentasCobro() {
		return huboInconsistenciaEventoRadicacionCuentasCobro;
	}

	/**
	 * @param huboInconsistenciaEventoRadicacionCuentasCobro the huboInconsistenciaEventoRadicacionCuentasCobro to set
	 */
	public void setHuboInconsistenciaEventoRadicacionCuentasCobro(
			boolean huboInconsistenciaEventoRadicacionCuentasCobro) {
		this.huboInconsistenciaEventoRadicacionCuentasCobro = huboInconsistenciaEventoRadicacionCuentasCobro;
	}

	/**
	 * @return the huboInconsistenciaEventoInactivacionFacturasCuentaCobro
	 */
	public boolean isHuboInconsistenciaEventoInactivacionFacturasCuentaCobro() {
		return huboInconsistenciaEventoInactivacionFacturasCuentaCobro;
	}

	/**
	 * @param huboInconsistenciaEventoInactivacionFacturasCuentaCobro the huboInconsistenciaEventoInactivacionFacturasCuentaCobro to set
	 */
	public void setHuboInconsistenciaEventoInactivacionFacturasCuentaCobro(
			boolean huboInconsistenciaEventoInactivacionFacturasCuentaCobro) {
		this.huboInconsistenciaEventoInactivacionFacturasCuentaCobro = huboInconsistenciaEventoInactivacionFacturasCuentaCobro;
	}

	/**
	 * @return the huboInconsistenciaEventoDevolucionCuentasCobro
	 */
	public boolean isHuboInconsistenciaEventoDevolucionCuentasCobro() {
		return huboInconsistenciaEventoDevolucionCuentasCobro;
	}

	/**
	 * @param huboInconsistenciaEventoDevolucionCuentasCobro the huboInconsistenciaEventoDevolucionCuentasCobro to set
	 */
	public void setHuboInconsistenciaEventoDevolucionCuentasCobro(
			boolean huboInconsistenciaEventoDevolucionCuentasCobro) {
		this.huboInconsistenciaEventoDevolucionCuentasCobro = huboInconsistenciaEventoDevolucionCuentasCobro;
	}

	/**
	 * @return the huboInconsistenciaEventoRegistroGlosas
	 */
	public boolean isHuboInconsistenciaEventoRegistroGlosas() {
		return huboInconsistenciaEventoRegistroGlosas;
	}

	/**
	 * @param huboInconsistenciaEventoRegistroGlosas the huboInconsistenciaEventoRegistroGlosas to set
	 */
	public void setHuboInconsistenciaEventoRegistroGlosas(
			boolean huboInconsistenciaEventoRegistroGlosas) {
		this.huboInconsistenciaEventoRegistroGlosas = huboInconsistenciaEventoRegistroGlosas;
	}

	/**
	 * @return the huboInconsistenciaEventoRegistroGlosasDevolucion
	 */
	public boolean isHuboInconsistenciaEventoRegistroGlosasDevolucion() {
		return huboInconsistenciaEventoRegistroGlosasDevolucion;
	}

	/**
	 * @param huboInconsistenciaEventoRegistroGlosasDevolucion the huboInconsistenciaEventoRegistroGlosasDevolucion to set
	 */
	public void setHuboInconsistenciaEventoRegistroGlosasDevolucion(
			boolean huboInconsistenciaEventoRegistroGlosasDevolucion) {
		this.huboInconsistenciaEventoRegistroGlosasDevolucion = huboInconsistenciaEventoRegistroGlosasDevolucion;
	}

	/**
	 * @return the huboInconsistenciaEventoRegistroRespuestaGlosas
	 */
	public boolean isHuboInconsistenciaEventoRegistroRespuestaGlosas() {
		return huboInconsistenciaEventoRegistroRespuestaGlosas;
	}

	/**
	 * @param huboInconsistenciaEventoRegistroRespuestaGlosas the huboInconsistenciaEventoRegistroRespuestaGlosas to set
	 */
	public void setHuboInconsistenciaEventoRegistroRespuestaGlosas(
			boolean huboInconsistenciaEventoRegistroRespuestaGlosas) {
		this.huboInconsistenciaEventoRegistroRespuestaGlosas = huboInconsistenciaEventoRegistroRespuestaGlosas;
	}

	/**
	 * @return the huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas
	 */
	public boolean isHuboInconsistenciaEventoRegistroRespuestaGlosasConciliadas() {
		return huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas;
	}

	/**
	 * @param huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas the huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas to set
	 */
	public void setHuboInconsistenciaEventoRegistroRespuestaGlosasConciliadas(
			boolean huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas) {
		this.huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas = huboInconsistenciaEventoRegistroRespuestaGlosasConciliadas;
	}

	/**
	 * @return the huboInconsistenciaEventoRadicacionRespuestaGlosas
	 */
	public boolean isHuboInconsistenciaEventoRadicacionRespuestaGlosas() {
		return huboInconsistenciaEventoRadicacionRespuestaGlosas;
	}

	/**
	 * @param huboInconsistenciaEventoRadicacionRespuestaGlosas the huboInconsistenciaEventoRadicacionRespuestaGlosas to set
	 */
	public void setHuboInconsistenciaEventoRadicacionRespuestaGlosas(
			boolean huboInconsistenciaEventoRadicacionRespuestaGlosas) {
		this.huboInconsistenciaEventoRadicacionRespuestaGlosas = huboInconsistenciaEventoRadicacionRespuestaGlosas;
	}

	/**
	 * @return the huboInconsistenciaNEventoFacturasPaciente
	 */
	public boolean isHuboInconsistenciaNEventoFacturasPaciente() {
		return huboInconsistenciaNEventoFacturasPaciente;
	}

	/**
	 * @param huboInconsistenciaNEventoFacturasPaciente the huboInconsistenciaNEventoFacturasPaciente to set
	 */
	public void setHuboInconsistenciaNEventoFacturasPaciente(
			boolean huboInconsistenciaNEventoFacturasPaciente) {
		this.huboInconsistenciaNEventoFacturasPaciente = huboInconsistenciaNEventoFacturasPaciente;
	}

	/**
	 * @return the huboInconsistenciaNEventoAuditoriaFacturas
	 */
	public boolean isHuboInconsistenciaNEventoAuditoriaFacturas() {
		return huboInconsistenciaNEventoAuditoriaFacturas;
	}

	/**
	 * @param huboInconsistenciaNEventoAuditoriaFacturas the huboInconsistenciaNEventoAuditoriaFacturas to set
	 */
	public void setHuboInconsistenciaNEventoAuditoriaFacturas(
			boolean huboInconsistenciaNEventoAuditoriaFacturas) {
		this.huboInconsistenciaNEventoAuditoriaFacturas = huboInconsistenciaNEventoAuditoriaFacturas;
	}

	/**
	 * @return the huboInconsistenciaNEventoCuentasCobroCartera
	 */
	public boolean isHuboInconsistenciaNEventoCuentasCobroCartera() {
		return huboInconsistenciaNEventoCuentasCobroCartera;
	}

	/**
	 * @param huboInconsistenciaNEventoCuentasCobroCartera the huboInconsistenciaNEventoCuentasCobroCartera to set
	 */
	public void setHuboInconsistenciaNEventoCuentasCobroCartera(
			boolean huboInconsistenciaNEventoCuentasCobroCartera) {
		this.huboInconsistenciaNEventoCuentasCobroCartera = huboInconsistenciaNEventoCuentasCobroCartera;
	}

	/**
	 * @return the huboInconsistenciaNEventoRadicacionCuentasCobro
	 */
	public boolean isHuboInconsistenciaNEventoRadicacionCuentasCobro() {
		return huboInconsistenciaNEventoRadicacionCuentasCobro;
	}

	/**
	 * @param huboInconsistenciaNEventoRadicacionCuentasCobro the huboInconsistenciaNEventoRadicacionCuentasCobro to set
	 */
	public void setHuboInconsistenciaNEventoRadicacionCuentasCobro(
			boolean huboInconsistenciaNEventoRadicacionCuentasCobro) {
		this.huboInconsistenciaNEventoRadicacionCuentasCobro = huboInconsistenciaNEventoRadicacionCuentasCobro;
	}

	/**
	 * @return the huboInconsistenciaNEventoInactivacionFacturasCuentaCobro
	 */
	public boolean isHuboInconsistenciaNEventoInactivacionFacturasCuentaCobro() {
		return huboInconsistenciaNEventoInactivacionFacturasCuentaCobro;
	}

	/**
	 * @param huboInconsistenciaNEventoInactivacionFacturasCuentaCobro the huboInconsistenciaNEventoInactivacionFacturasCuentaCobro to set
	 */
	public void setHuboInconsistenciaNEventoInactivacionFacturasCuentaCobro(
			boolean huboInconsistenciaNEventoInactivacionFacturasCuentaCobro) {
		this.huboInconsistenciaNEventoInactivacionFacturasCuentaCobro = huboInconsistenciaNEventoInactivacionFacturasCuentaCobro;
	}

	/**
	 * @return the huboInconsistenciaNEventoDevolucionCuentasCobro
	 */
	public boolean isHuboInconsistenciaNEventoDevolucionCuentasCobro() {
		return huboInconsistenciaNEventoDevolucionCuentasCobro;
	}

	/**
	 * @param huboInconsistenciaNEventoDevolucionCuentasCobro the huboInconsistenciaNEventoDevolucionCuentasCobro to set
	 */
	public void setHuboInconsistenciaNEventoDevolucionCuentasCobro(
			boolean huboInconsistenciaNEventoDevolucionCuentasCobro) {
		this.huboInconsistenciaNEventoDevolucionCuentasCobro = huboInconsistenciaNEventoDevolucionCuentasCobro;
	}

	/**
	 * @return the huboInconsistenciaNEventoRegistroGlosas
	 */
	public boolean isHuboInconsistenciaNEventoRegistroGlosas() {
		return huboInconsistenciaNEventoRegistroGlosas;
	}

	/**
	 * @param huboInconsistenciaNEventoRegistroGlosas the huboInconsistenciaNEventoRegistroGlosas to set
	 */
	public void setHuboInconsistenciaNEventoRegistroGlosas(
			boolean huboInconsistenciaNEventoRegistroGlosas) {
		this.huboInconsistenciaNEventoRegistroGlosas = huboInconsistenciaNEventoRegistroGlosas;
	}

	/**
	 * @return the huboInconsistenciaNEventoRegistroGlosasDevolucion
	 */
	public boolean isHuboInconsistenciaNEventoRegistroGlosasDevolucion() {
		return huboInconsistenciaNEventoRegistroGlosasDevolucion;
	}

	/**
	 * @param huboInconsistenciaNEventoRegistroGlosasDevolucion the huboInconsistenciaNEventoRegistroGlosasDevolucion to set
	 */
	public void setHuboInconsistenciaNEventoRegistroGlosasDevolucion(
			boolean huboInconsistenciaNEventoRegistroGlosasDevolucion) {
		this.huboInconsistenciaNEventoRegistroGlosasDevolucion = huboInconsistenciaNEventoRegistroGlosasDevolucion;
	}

	/**
	 * @return the huboInconsistenciaNEventoRegistroRespuestaGlosas
	 */
	public boolean isHuboInconsistenciaNEventoRegistroRespuestaGlosas() {
		return huboInconsistenciaNEventoRegistroRespuestaGlosas;
	}

	/**
	 * @param huboInconsistenciaNEventoRegistroRespuestaGlosas the huboInconsistenciaNEventoRegistroRespuestaGlosas to set
	 */
	public void setHuboInconsistenciaNEventoRegistroRespuestaGlosas(
			boolean huboInconsistenciaNEventoRegistroRespuestaGlosas) {
		this.huboInconsistenciaNEventoRegistroRespuestaGlosas = huboInconsistenciaNEventoRegistroRespuestaGlosas;
	}

	/**
	 * @return the huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas
	 */
	public boolean isHuboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas() {
		return huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas;
	}

	/**
	 * @param huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas the huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas to set
	 */
	public void setHuboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas(
			boolean huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas) {
		this.huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas = huboInconsistenciaNEventoRegistroRespuestaGlosasConciliadas;
	}

	/**
	 * @return the huboInconsistenciaNEventoRadicacionRespuestaGlosas
	 */
	public boolean isHuboInconsistenciaNEventoRadicacionRespuestaGlosas() {
		return huboInconsistenciaNEventoRadicacionRespuestaGlosas;
	}

	/**
	 * @param huboInconsistenciaNEventoRadicacionRespuestaGlosas the huboInconsistenciaNEventoRadicacionRespuestaGlosas to set
	 */
	public void setHuboInconsistenciaNEventoRadicacionRespuestaGlosas(
			boolean huboInconsistenciaNEventoRadicacionRespuestaGlosas) {
		this.huboInconsistenciaNEventoRadicacionRespuestaGlosas = huboInconsistenciaNEventoRadicacionRespuestaGlosas;
	}

	public String getUnidMovCxcCap() {
		return unidMovCxcCap;
	}

	public void setUnidMovCxcCap(String unidMovCxcCap) {
		this.unidMovCxcCap = unidMovCxcCap;
	}

	/**
	 * @param centroCostoTesoreria the centroCostoTesoreria to set
	 */
	public void setCentroCostoTesoreria(String centroCostoTesoreria) {
		this.centroCostoTesoreria = centroCostoTesoreria;
	}

	/**
	 * @return the centroCostoTesoreria
	 */
	public String getCentroCostoTesoreria() {
		return centroCostoTesoreria;
	}

	/**
	 * @param codigoCentroCostoTesoreria the codigoCentroCostoTesoreria to set
	 */
	public void setCodigoCentroCostoTesoreria(String codigoCentroCostoTesoreria) {
		this.codigoCentroCostoTesoreria = codigoCentroCostoTesoreria;
	}

	/**
	 * @return the codigoCentroCostoTesoreria
	 */
	public String getCodigoCentroCostoTesoreria() {
		return codigoCentroCostoTesoreria;
	}

	/**
	 * @param huboInconsistenciaCentroCostoTesoreria the huboInconsistenciaCentroCostoTesoreria to set
	 */
	public void setHuboInconsistenciaCentroCostoTesoreria(
			boolean huboInconsistenciaCentroCostoTesoreria) {
		this.huboInconsistenciaCentroCostoTesoreria = huboInconsistenciaCentroCostoTesoreria;
	}

	/**
	 * @return the huboInconsistenciaCentroCostoTesoreria
	 */
	public boolean isHuboInconsistenciaCentroCostoTesoreria() {
		return huboInconsistenciaCentroCostoTesoreria;
	}

	/**
	 * @param codigoInterfazCentroCostoTesoreria the codigoInterfazCentroCostoTesoreria to set
	 */
	public void setCodigoInterfazCentroCostoTesoreria(
			String codigoInterfazCentroCostoTesoreria) {
		this.codigoInterfazCentroCostoTesoreria = codigoInterfazCentroCostoTesoreria;
	}

	/**
	 * @return the codigoInterfazCentroCostoTesoreria
	 */
	public String getCodigoInterfazCentroCostoTesoreria() {
		return codigoInterfazCentroCostoTesoreria;
	}
	
	
}