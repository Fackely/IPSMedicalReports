/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 4:52:46 PM
 */
public class DtoDetalleLogImpresionPresupuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3933763398903490432L;

	/**
	 * 
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 */
	private BigDecimal presupuestoLogImpresion;
	
	
	/**
	 * Enum con los mensajes de auditoria
	 */
	public static enum EAnexos 
	{
		IMP_PRE,
		IMP_CON,
		IMP_REC,
		IMP_OTR
	};
	
	/**
	 * 
	 */
	private EAnexos anexo;


	/**
	 * Constructor de la clase
	 * @param codigoPk
	 * @param presupuestoLogImpresion
	 * @param anexo
	 */
	public DtoDetalleLogImpresionPresupuesto() {
		this.codigoPk = BigDecimal.ZERO;
		this.presupuestoLogImpresion = BigDecimal.ZERO;
		this.anexo = null;
	}


	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the presupuestoLogImpresion
	 */
	public BigDecimal getPresupuestoLogImpresion() {
		return presupuestoLogImpresion;
	}


	/**
	 * @param presupuestoLogImpresion the presupuestoLogImpresion to set
	 */
	public void setPresupuestoLogImpresion(BigDecimal presupuestoLogImpresion) {
		this.presupuestoLogImpresion = presupuestoLogImpresion;
	}


	/**
	 * @return the anexo
	 */
	public EAnexos getAnexo() {
		return anexo;
	}


	/**
	 * @param anexo the anexo to set
	 */
	public void setAnexo(EAnexos anexo) {
		this.anexo = anexo;
	}
	
}