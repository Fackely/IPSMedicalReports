package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.ValoresPlanPacCampo;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ValoresPorDefecto;

public class DtoPlanTratamientoOdo implements Serializable , Cloneable{
	

	
	private BigDecimal codigoPk;
	private BigDecimal consecutivo;
	private int codigoPaciente;
	private int ingreso;
	private InfoDatosInt especialidad;
	private String fechaGrabacion;
	private String horaGrabacion;
	private String usuarioGrabacion;
	private BigDecimal odontogramaDiagnostico;
	private BigDecimal codigoCita;
	private String estado;
	private String nombreEstado;
	
	private BigDecimal odontogramaEvolucion;
	private String indicativo;
	
	private int institucion;
	private int centroAtencion;
	private int motivo;
	private DtoInfoFechaUsuario usuarioModifica;
    private String porConfirmar;
    private BigDecimal codigoEvolucion;
    private BigDecimal codigoValoracion;
    
    
    
    
   /**
    * ATRIBUTOS PARA MANEJAR HISTORICO
    */
    private BigDecimal codigoPlanHistorico;

    
	
	
    /**
     * Construtor
     */
	public DtoPlanTratamientoOdo(){
		reset();
	}
	
	
	/**
	 * Reset
	 */
	void reset(){
		  this.codigoPk  = new BigDecimal(0);
		  this.consecutivo= new BigDecimal(0);
		  this.codigoPaciente=0;
		  this.especialidad= new InfoDatosInt();
		  this.fechaGrabacion ="";
		  this.horaGrabacion="";
		  this.usuarioGrabacion="";
		  this.odontogramaDiagnostico= new BigDecimal(0);
		  this.estado="";
		  this.odontogramaEvolucion = new BigDecimal(0);
		  this.indicativo="";
		  this.institucion=0;
		  this.centroAtencion=0;
	      this.setUsuarioModifica(new DtoInfoFechaUsuario());
		  this.ingreso=0;
		  this.motivo=0;
		  this.porConfirmar="";
		  this.codigoCita = new BigDecimal(ConstantesBD.codigoNuncaValido);
		  this.codigoEvolucion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		  this.codigoValoracion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		  this.codigoPlanHistorico=BigDecimal.ZERO;
		  this.nombreEstado="";
		}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}
	public String getHoraGrabacion() {
		return horaGrabacion;
	}
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}
	public String getUsuarioGrabacion() {
		return usuarioGrabacion;
	}
	public void setUsuarioGrabacion(String usuarioGrabacion) {
		this.usuarioGrabacion = usuarioGrabacion;
	}
	public BigDecimal getOdontogramaDiagnostico() {
		return odontogramaDiagnostico;
	}
	public void setOdontogramaDiagnostico(BigDecimal odontogramaDiagnostico) {
		this.odontogramaDiagnostico = odontogramaDiagnostico;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public BigDecimal getOdontogramaEvolucion() {
		return odontogramaEvolucion;
	}
	public void setOdontogramaEvolucion(BigDecimal odontogramaEvolucion) {
		this.odontogramaEvolucion = odontogramaEvolucion;
	}
	public String getIndicativo() {
		return indicativo;
	}
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}
	
	public int getInstitucion() {
		return institucion;
	}
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	public int getCentroAtencion() {
		return centroAtencion;
	}
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	public int getIngreso() {
		return ingreso;
	}


	public void setMotivo(int motivo) {
		this.motivo = motivo;
	}


	public int getMotivo() {
		return motivo;
	}


	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}


	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}


	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}
	
	
	/***
	 * 
	 *  
	 */
	
	public Object clone()
	{
	      DtoPlanTratamientoOdo obj=null;
	        try{
	            obj= (DtoPlanTratamientoOdo)super.clone();
	            obj.setUsuarioModifica((DtoInfoFechaUsuario)this.usuarioModifica.clone());
	            obj.setEspecialidad((InfoDatosInt)this.especialidad.clone());
	       
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	  }


	public BigDecimal getCodigoCita() {
		return codigoCita;
	}


	public void setCodigoCita(BigDecimal codigoCita) {
		this.codigoCita = codigoCita;
	}


	public BigDecimal getCodigoEvolucion() {
		return codigoEvolucion;
	}


	public void setCodigoEvolucion(BigDecimal codigoEvolucion) {
		this.codigoEvolucion = codigoEvolucion;
	}


	public BigDecimal getCodigoValoracion() {
		return codigoValoracion;
	}


	public void setCodigoValoracion(BigDecimal codigoValoracion) {
		this.codigoValoracion = codigoValoracion;
	}


	public void setCodigoPlanHistorico(BigDecimal codigoPlanHistorico) {
		this.codigoPlanHistorico = codigoPlanHistorico;
	}


	public BigDecimal getCodigoPlanHistorico() {
		return codigoPlanHistorico;
	}


	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	/**
	 * Ayudante para Cargar el Nombre Estado
	 * @return
	 */
	public String getNombreEstado() {
		return ValoresPorDefecto.getIntegridadDominio(this.estado)+"";
	}
	

}
