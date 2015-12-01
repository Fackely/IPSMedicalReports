package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
/**
 * 
 * @author axioma
 *
 */
public class DtoExclusionPresupuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1599313796869814697L;
	/**
	 * 
	 */
	private BigDecimal	codigoPk ;
	private DtoInfoFechaUsuario usuarioFechaModifica;
	private BigDecimal presupuesto;
	private int programaOservicio;
	private boolean utilizaProgramas;
	private BigDecimal valor;
	
	/**
	 * Código del encabezado de exclusión asociado
	 */
	private long codigoExcluPresuEncabezado;
	
	
	private ArrayList<DtoDetalleExclusionSuperficies> detalleSuperficies;
	
	/**
	 * 
	 */
	public DtoExclusionPresupuesto() {
		super();
		reset();
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
	 * @return the usuarioFechaModifica
	 */
	public DtoInfoFechaUsuario getUsuarioFechaModifica() {
		return usuarioFechaModifica;
	}

	/**
	 * @param usuarioFechaModifica the usuarioFechaModifica to set
	 */
	public void setUsuarioFechaModifica(DtoInfoFechaUsuario usuarioFechaModifica) {
		this.usuarioFechaModifica = usuarioFechaModifica;
	}

	/**
	 * @return the presupuesto
	 */
	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}

	/**
	 * 
	 */
	private void reset() {
		this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.usuarioFechaModifica = new DtoInfoFechaUsuario();
		this.presupuesto =  new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.programaOservicio= ConstantesBD.codigoNuncaValido;
		this.utilizaProgramas= false;
		this.valor=new BigDecimal(0);
		this.detalleSuperficies= new ArrayList<DtoDetalleExclusionSuperficies>();
		
		this.codigoExcluPresuEncabezado = ConstantesBD.codigoNuncaValidoLong;
	}

	/**
	 * @return the programaOservicio
	 */
	public int getProgramaOservicio()
	{
		return programaOservicio;
	}

	/**
	 * @param programaOservicio the programaOservicio to set
	 */
	public void setProgramaOservicio(int programaOservicio)
	{
		this.programaOservicio = programaOservicio;
	}

	/**
	 * @return the utilizaProgramas
	 */
	public boolean isUtilizaProgramas()
	{
		return utilizaProgramas;
	}

	/**
	 * @param utilizaProgramas the utilizaProgramas to set
	 */
	public void setUtilizaProgramas(boolean utilizaProgramas)
	{
		this.utilizaProgramas = utilizaProgramas;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor()
	{
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor)
	{
		this.valor = valor;
	}

	/**
	 * @return the detalleSuperficies
	 */
	public ArrayList<DtoDetalleExclusionSuperficies> getDetalleSuperficies() {
		return detalleSuperficies;
	}

	/**
	 * @param detalleSuperficies the detalleSuperficies to set
	 */
	public void setDetalleSuperficies(
			ArrayList<DtoDetalleExclusionSuperficies> detalleSuperficies) {
		this.detalleSuperficies = detalleSuperficies;
	}

	/**
	 * @param codigoExcluPresuEncabezado the codigoExcluPresuEncabezado to set
	 */
	public void setCodigoExcluPresuEncabezado(long codigoExcluPresuEncabezado) {
		this.codigoExcluPresuEncabezado = codigoExcluPresuEncabezado;
	}

	/**
	 * @return the codigoExcluPresuEncabezado
	 */
	public long getCodigoExcluPresuEncabezado() {
		return codigoExcluPresuEncabezado;
	}
	
	
	
	
	
}
