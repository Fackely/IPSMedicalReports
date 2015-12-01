package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import util.ConstantesBD;
import com.princetonsa.dto.facturacion.DtoConvenio;



/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class DTOVentaTarjetaEmpresarial implements Serializable
{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 *Codigo pk de las Venta 
	 */
	private long codigoPk;
	
	/**
	 * Atributo para almacenar el serial inicial
	 */
	private BigDecimal	serialInicial ;
	
	/**
	 * Atributo que sirve como ayudante para el serial final
	 */
	private  String ayudanteSerialInicial;
	
	/**
	 * Atributo para almacer el serial final 
	 */
	private BigDecimal 	serialFinal;
	
	/**
	 * Atributo  que sive como ayundate para el serial final 
	 */
	private  String ayudanteSerialFinal;
	
	/**
	 * Objeto que hace referencia a convenio, este convenio hace refenrencia a la venta empresarial
	 */
	private DtoConvenio dtoConvenio;
	
	
	
	/**
	 * DtoVentasTarjetaEmpresarial
	 */
	public DTOVentaTarjetaEmpresarial(){
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.serialInicial=BigDecimal.ZERO;
		this.serialFinal=BigDecimal.ZERO;
		
	}

	
	
	/**
	 * @return the codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @return the serialInicial
	 */
	public BigDecimal getSerialInicial() {
		return serialInicial;
	}

	/**
	 * @return the ayudanteSerialInicial
	 */
	public String getAyudanteSerialInicial() {
		return ayudanteSerialInicial;
	}

	/**
	 * @return the serialFinal
	 */
	public BigDecimal getSerialFinal() {
		return serialFinal;
	}

	/**
	 * @return the ayudanteSerialFinal
	 */
	public String getAyudanteSerialFinal() {
		return ayudanteSerialFinal;
	}

	/**
	 * @return the dtoConvenio
	 */
	public DtoConvenio getDtoConvenio() {
		return dtoConvenio;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @param serialInicial the serialInicial to set
	 */
	public void setSerialInicial(BigDecimal serialInicial) {
		this.serialInicial = serialInicial;
	}

	/**
	 * @param ayudanteSerialInicial the ayudanteSerialInicial to set
	 */
	public void setAyudanteSerialInicial(String ayudanteSerialInicial) {
		this.ayudanteSerialInicial = ayudanteSerialInicial;
	}

	/**
	 * @param serialFinal the serialFinal to set
	 */
	public void setSerialFinal(BigDecimal serialFinal) {
		this.serialFinal = serialFinal;
	}

	/**
	 * @param ayudanteSerialFinal the ayudanteSerialFinal to set
	 */
	public void setAyudanteSerialFinal(String ayudanteSerialFinal) {
		this.ayudanteSerialFinal = ayudanteSerialFinal;
	}

	/**
	 * @param dtoConvenio the dtoConvenio to set
	 */
	public void setDtoConvenio(DtoConvenio dtoConvenio) {
		this.dtoConvenio = dtoConvenio;
	}
	
	
	
	
		
	

	
}
