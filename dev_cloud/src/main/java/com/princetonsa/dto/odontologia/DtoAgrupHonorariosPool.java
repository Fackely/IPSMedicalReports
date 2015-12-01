package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;



/**
 * Anexo 961
 * @author axioma
 *
 */

public class DtoAgrupHonorariosPool implements Serializable , Cloneable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6665510253859429567L;
	private BigDecimal codigoPk ;
	private BigDecimal honorarioPool;  
	private InfoDatosInt grupoServicio ;
	private InfoDatosStr tipoServicio ;
	private Double porcentajeParticipacion ;
	private BigDecimal valorParticipacion; 
    private InfoDatosDouble cuentaContableHonorario; 
	private InfoDatosDouble cuentaContableIns ;
	private InfoDatosDouble cueContInstVigAnterior ;
	private DtoInfoFechaUsuario usuarioModifica;

	

	public DtoAgrupHonorariosPool() {
		this.codigoPk = BigDecimal.ZERO;
		this.honorarioPool = BigDecimal.ZERO;
		this.grupoServicio =  new InfoDatosInt();
		this.tipoServicio =  new InfoDatosStr();
		this.porcentajeParticipacion = ConstantesBD.codigoNuncaValidoDouble;
		this.valorParticipacion = BigDecimal.ZERO;
		this.cuentaContableHonorario = new InfoDatosDouble();
		this.cuentaContableIns = new InfoDatosDouble();
		this.cueContInstVigAnterior = new InfoDatosDouble();
		this.usuarioModifica = new DtoInfoFechaUsuario();
	}


	/**
	 * 
	 */
	public DtoAgrupHonorariosPool clone(){
				
				DtoAgrupHonorariosPool obj=null;
				try{
					
					obj=(DtoAgrupHonorariosPool) super.clone();
					obj.cueContInstVigAnterior= (InfoDatosDouble)this.cueContInstVigAnterior.clone();
					obj.cuentaContableIns= (InfoDatosDouble)this.cuentaContableIns.clone();
					obj.cuentaContableHonorario= (InfoDatosDouble)this.cuentaContableHonorario.clone();
					obj.grupoServicio=(InfoDatosInt)this.grupoServicio.clone();
					obj.tipoServicio= (InfoDatosStr)this.tipoServicio.clone();
					obj.usuarioModifica= (DtoInfoFechaUsuario)this.usuarioModifica.clone();
				}
				catch(CloneNotSupportedException ex)
				{
					
				}
			return obj;
    }

	public InfoDatosDouble getCuentaContableHonorario() {
		return cuentaContableHonorario;
	}



	public void setCuentaContableHonorario(InfoDatosDouble cuentaContableHonorario) {
		this.cuentaContableHonorario = cuentaContableHonorario;
	}



	public InfoDatosDouble getCuentaContableIns() {
		return cuentaContableIns;
	}



	public void setCuentaContableIns(InfoDatosDouble cuentaContableIns) {
		this.cuentaContableIns = cuentaContableIns;
	}



	public InfoDatosDouble getCueContInstVigAnterior() {
		return cueContInstVigAnterior;
	}



	public void setCueContInstVigAnterior(InfoDatosDouble cueContInstVigAnterior) {
		this.cueContInstVigAnterior = cueContInstVigAnterior;
	}



	public BigDecimal getCodigoPk() {
		return codigoPk;
	}



	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}



	public BigDecimal getHonorarioPool() {
		return honorarioPool;
	}



	public void setHonorarioPool(BigDecimal honorarioPool) {
		this.honorarioPool = honorarioPool;
	}



	public InfoDatosInt getGrupoServicio() {
		return grupoServicio;
	}



	public void setGrupoServicio(InfoDatosInt grupoServicio) {
		this.grupoServicio = grupoServicio;
	}



	public InfoDatosStr getTipoServicio() {
		return tipoServicio;
	}



	public void setTipoServicio(InfoDatosStr tipoServicio) {
		this.tipoServicio = tipoServicio;
	}










	public BigDecimal getValorParticipacion() {
		return valorParticipacion;
	}



	public void setValorParticipacion(BigDecimal valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}





	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}



	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}



	public Double getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}



	public void setPorcentajeParticipacion(Double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}
	
	
}
