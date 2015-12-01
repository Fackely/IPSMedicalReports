package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;



/**
 * DTO PARA MANEJAR CONTRATOS A NIVEL DEL PRESUPUETO
 * @author Edgar Carvajal
 *
 */
public class DtoFirmasContratoOtrosiInst implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * CODIGO PK 
	 */
	private long  codigoPk; 
	/**
	 * CODIGO PK DEL LA TABLA PRESU CONTRATO ODON IMP
	 */
	private long contratoOdo;
	
	/**
	 * 
	 */
	private int numero;
	/**
	 * 
	 */
	private String 	labelDebajoFirma;
	/**
	 * 
	 */
	private String	firmaDigital;
	/**
	 * 
	 */
	private String	adjuntoFirma;
	/**
	 * 
	 */
	private String 	usuarioModifica;
	/**
	 * 
	 */
	private String	fechaModifica ;
	/**
	 * 
	 */
	private String	horaModifica ;
	
	/**
	 * ATRIBUTO PARA SABER SI ES MULTIEMPRESA
	 */
	private String empresaInstitucion;

	
	
	
	/**
	*	CONTRUTOR
	*/
	public DtoFirmasContratoOtrosiInst()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.contratoOdo=ConstantesBD.codigoNuncaValidoLong;
		this.labelDebajoFirma ="";
		this.firmaDigital="";
		this.adjuntoFirma="";
		this.usuarioModifica="";
		this.horaModifica="";
		this.numero=ConstantesBD.codigoNuncaValido;
		this.setEmpresaInstitucion("");
	}

	/**
	 * @return the codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the contratoOdo
	 */
	public long getContratoOdo() {
		return contratoOdo;
	}

	/**
	 * @param contratoOdo the contratoOdo to set
	 */
	public void setContratoOdo(long contratoOdo) {
		this.contratoOdo = contratoOdo;
	}

	/**
	 * @return the labelDebajoFirma
	 */
	public String getLabelDebajoFirma() {
		return labelDebajoFirma;
	}

	/**
	 * @param labelDebajoFirma the labelDebajoFirma to set
	 */
	public void setLabelDebajoFirma(String labelDebajoFirma) {
		this.labelDebajoFirma = labelDebajoFirma;
	}

	/**
	 * @return the firmaDigital
	 */
	public String getFirmaDigital() {
		return firmaDigital;
	}

	/**
	 * @param firmaDigital the firmaDigital to set
	 */
	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	/**
	 * @return the adjuntoFirma
	 */
	public String getAdjuntoFirma() {
		return adjuntoFirma;
	}

	/**
	 * @param adjuntoFirma the adjuntoFirma to set
	 */
	public void setAdjuntoFirma(String adjuntoFirma) {
		this.adjuntoFirma = adjuntoFirma;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}
	
	
	
	

}
