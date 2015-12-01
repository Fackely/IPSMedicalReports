package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;




/**
 * ANEXO 961
 * 
 */
public class DtoHonorarioPoolServicio  implements Serializable, Cloneable{
	
	
	private BigDecimal codigoPk;
	private BigDecimal honorarioPool;
	private InfoDatosInt servicio;
	private Double porcentajeParticipacion;
	private BigDecimal valorParticipacion ;
	private InfoDatosDouble cuentaContableIns ;
	private InfoDatosDouble cueContInstVigAnterior ;
	private InfoDatosDouble cuentaContableHonorario;
	private DtoInfoFechaUsuario usuarioModifica;
	
	
	
	
	public DtoHonorarioPoolServicio() {
		this.reset();
	
	}

	public void reset(){
		this.codigoPk = BigDecimal.ZERO;
		this.honorarioPool =BigDecimal.ZERO;
		this.servicio = new InfoDatosInt();
		this.porcentajeParticipacion = ConstantesBD.codigoNuncaValidoDouble;
		this.valorParticipacion = BigDecimal.ZERO;
		this.cuentaContableIns =  new InfoDatosDouble();
		this.cueContInstVigAnterior = new InfoDatosDouble();
		this.cuentaContableHonorario = new InfoDatosDouble();
		this.usuarioModifica = new DtoInfoFechaUsuario();
	}

	
	
	/**
	 * 
	 */
	public DtoHonorarioPoolServicio clone(){
		DtoHonorarioPoolServicio obj=null;
        
		try
        {
        
        	obj=(DtoHonorarioPoolServicio)super.clone();
            obj.cueContInstVigAnterior=(InfoDatosDouble)this.cueContInstVigAnterior;
            obj.cuentaContableHonorario=(InfoDatosDouble)this.cuentaContableHonorario;
            obj.cuentaContableIns=(InfoDatosDouble)this.cuentaContableIns;
            obj.usuarioModifica= (DtoInfoFechaUsuario)this.usuarioModifica;
            obj.servicio= (InfoDatosInt)this.servicio;
        }
        
		catch(CloneNotSupportedException ex)
        {
           
        }
        return (DtoHonorarioPoolServicio)obj;
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



	public InfoDatosInt getServicio() {
		return servicio;
	}



	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}



	public Double getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(Double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	public BigDecimal getValorParticipacion() {
		return valorParticipacion;
	}



	public void setValorParticipacion(BigDecimal valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
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



	public InfoDatosDouble getCuentaContableHonorario() {
		return cuentaContableHonorario;
	}



	public void setCuentaContableHonorario(InfoDatosDouble cuentaContableHonorario) {
		this.cuentaContableHonorario = cuentaContableHonorario;
	}



	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}



	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	
	
	

}
