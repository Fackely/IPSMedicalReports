package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;


/**
 * Data Transfer Object: PLANTILLA PARAMETRIZABLE POR SERVICIO y DIAGNOSTICO
 */
public class DtoPlantillaServDiag implements Serializable{
	
	/**
	 * 
	 * */
	private int codigpPk;
	
	/**
	 * Codigo pk de la plantilla
	 */
	private int codigoPkPlantilla;
	/**
	 * 
	 * */
	private int codigoPlantilla;
	
	/**
	 * 
	 * */
	private String descripcionPlantilla;
	
	/**
	 * 
	 * */
	private int codigoServicio;
	
	/**
	 * 
	 * */
	private String descripcionServicio;
	
	/**
	 * 
	 * */
	private int codigoEspecialidadServicio;
	
	/**
	 * 
	 * */
	private String codigoPropietarioServicio;
	
	/**
	 * 
	 * */
	private int codigoDiagnostico;
	
	/**
	 * 
	 * */
	private String descripcionDiagnostico;
	
	/**
	 * 
	 * */
	private String estaBD;
	
	
	/**
	 * 
	 * */
	private String esEliminado;
	
	
	/**
	 * 
	 * */
	private String activo;	
	
	
	/**
	 * Resetea los datos del DTO
	 *
	 */
	public void clean()
	{
		this.codigpPk = ConstantesBD.codigoNuncaValido;
		this.codigoPkPlantilla = ConstantesBD.codigoNuncaValido;
		this.codigoPlantilla = ConstantesBD.codigoNuncaValido;		
		this.codigoServicio = ConstantesBD.codigoNuncaValido;		
		this.codigoEspecialidadServicio = ConstantesBD.codigoNuncaValido;
		this.codigoPropietarioServicio = "";
		this.codigoDiagnostico = ConstantesBD.codigoNuncaValido;
		this.descripcionDiagnostico = "";
		this.descripcionServicio = "";
		this.descripcionDiagnostico = "";
		this.estaBD = ConstantesBD.acronimoSi;
		this.esEliminado = ConstantesBD.acronimoNo;
		this.activo = ConstantesBD.acronimoNo;
	}
	
	/**
	 * Constructor del DTO
	 *
	 */
	public DtoPlantillaServDiag()
	{
		this.clean();
	}	

	/**
	 * @return the descripcionPlantilla
	 */
	public String getDescripcionPlantilla() {
		return descripcionPlantilla;
	}

	/**
	 * @param descripcionPlantilla the descripcionPlantilla to set
	 */
	public void setDescripcionPlantilla(String descripcionPlantilla) {
		this.descripcionPlantilla = descripcionPlantilla;
	}

	/**
	 * @return the descripcionServicio
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * @param descripcionServicio the descripcionServicio to set
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * @return the descripcionDiagnostico
	 */
	public String getDescripcionDiagnostico() {
		return descripcionDiagnostico;
	}

	/**
	 * @param descripcionDiagnostico the descripcionDiagnostico to set
	 */
	public void setDescripcionDiagnostico(String descripcionDiagnostico) {
		this.descripcionDiagnostico = descripcionDiagnostico;
	}

	/**
	 * @return the codigoEspecialidadServicio
	 */
	public int getCodigoEspecialidadServicio() {
		return codigoEspecialidadServicio;
	}

	/**
	 * @param codigoEspecialidadServicio the codigoEspecialidadServicio to set
	 */
	public void setCodigoEspecialidadServicio(int codigoEspecialidadServicio) {
		this.codigoEspecialidadServicio = codigoEspecialidadServicio;
	}

	/**
	 * @param codigpPk the codigpPk to set
	 */
	public void setCodigpPk(int codigpPk) {
		this.codigpPk = codigpPk;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @param codigoPropietarioServicio the codigoPropietarioServicio to set
	 */
	public void setCodigoPropietarioServicio(String codigoPropietarioServicio) {
		this.codigoPropietarioServicio = codigoPropietarioServicio;
	}

	/**
	 * @param codigoDiagnostico the codigoDiagnostico to set
	 */
	public void setCodigoDiagnostico(int codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	/**
	 * @return the codigpPk
	 */
	public int getCodigpPk() {
		return codigpPk;
	}


	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @return the codigoPropietarioServicio
	 */
	public String getCodigoPropietarioServicio() {
		return codigoPropietarioServicio;
	}

	/**
	 * @return the codigoDiagnostico
	 */
	public int getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	/**
	 * @return the codigoPkPlantilla
	 */
	public int getCodigoPkPlantilla() {
		return codigoPkPlantilla;
	}

	/**
	 * @param codigoPkPlantilla the codigoPkPlantilla to set
	 */
	public void setCodigoPkPlantilla(int codigoPkPlantilla) {
		this.codigoPkPlantilla = codigoPkPlantilla;
	}

	/**
	 * @return the codigoPlantilla
	 */
	public int getCodigoPlantilla() {
		return codigoPlantilla;
	}

	/**
	 * @param codigoPlantilla the codigoPlantilla to set
	 */
	public void setCodigoPlantilla(int codigoPlantilla) {
		this.codigoPlantilla = codigoPlantilla;
	}

	/**
	 * @return the estaBD
	 */
	public String getEstaBD() {
		return estaBD;
	}

	/**
	 * @param estaBD the estaBD to set
	 */
	public void setEstaBD(String estaBD) {
		this.estaBD = estaBD;
	}

	/**
	 * @return the esEliminado
	 */
	public String getEsEliminado() {
		return esEliminado;
	}

	/**
	 * @param esEliminado the esEliminado to set
	 */
	public void setEsEliminado(String esEliminado) {
		this.esEliminado = esEliminado;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
}