package com.princetonsa.dto.odontologia;


import java.io.Serializable;
import util.ConstantesBD;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class DtoRecomendaciones implements Serializable
{

	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private String codigo;
	private String descripcion;
	private boolean yaExiste;
	private long pkPrograma;
	private String codigoPrograma;
	private String descripcionPrograma;

	
	/**
	 * Constructor
	 */
	public DtoRecomendaciones() 
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.codigo="";
		this.descripcion="";
		this.setYaExiste(Boolean.FALSE);
		this.codigoPrograma="";
		this.descripcionPrograma="";
		this.pkPrograma = ConstantesBD.codigoNuncaValidoLong;
	}
	
	
	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	public void setYaExiste(boolean yaExiste) {
		this.yaExiste = yaExiste;
	}

	
	public boolean isYaExiste() {
		return yaExiste;
	}


	/**
	 * @return the descripcionPrograma
	 */
	public String getDescripcionPrograma() {
		return descripcionPrograma;
	}


	/**
	 * @param descripcionPrograma the descripcionPrograma to set
	 */
	public void setDescripcionPrograma(String descripcionPrograma) {
		this.descripcionPrograma = descripcionPrograma;
	}


	public String getCodigoPrograma() {
		return codigoPrograma;
	}


	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}


	public long getPkPrograma() {
		return pkPrograma;
	}


	public void setPkPrograma(long pkPrograma) {
		this.pkPrograma = pkPrograma;
	}




	
}
