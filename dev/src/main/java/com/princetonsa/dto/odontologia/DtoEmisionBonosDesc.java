package com.princetonsa.dto.odontologia;
import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoDatos;
import util.InfoDatosInt;
import util.InfoDatosDouble;

import util.UtilidadFecha;

public class DtoEmisionBonosDesc implements Serializable , Cloneable {

	
	
	private boolean estadoAsociado;
	/*
	 * 
	 */
	private double codigo;


	/**
	 * 
	 */
	private InfoDatosInt convenioPatrocinador;
	/**
	 * 
	 */
	private String id;
	/**
	 * 
	 */
	private int institucion;
	/**
	 * 
	 */
	private BigDecimal serialInicial;
	/**
	 * 
	 */
	private BigDecimal serialFinal;
	/**
	 * 
	 */
	private String fechaVigenciaInicial;
	/**
	 * 
	 */
	private String fechaVigenciaFinal;
	/**
	 * 
	 */
	private InfoDatosDouble  programa;
	/**
	 * 
	 */
	private double valorDescuento;
	/**
	 * 
	 */
	private double porcentajeDescuento;
	/**
	 * 
	 */
	private String fechaModifica;
	/**
	 * 
	 */
	private String horaModifica;
	/**
	 * 
	 */
	private String usuarioModifica;
	/**
	 * 
	 */
	private InfoDatosInt servicio;

	/**
	 * 
	 */
	public DtoEmisionBonosDesc() {
		this.clean();
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}

	/**
	 * 
	 */
	public void clean() {
		this.codigo = 0;
		this.setConvenioPatrocinador(new InfoDatosInt());
		
	
		this.id = "";
		this.institucion=0;
		this.serialInicial =BigDecimal.ZERO;
		this.serialFinal =BigDecimal.ZERO;
		this.fechaVigenciaInicial = "";
		this.fechaVigenciaFinal = "";
		this.setPrograma(new InfoDatosDouble());
		this.valorDescuento =0;
		this.porcentajeDescuento =0;
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = "";
		this.estadoAsociado=false;
		this.servicio = new InfoDatosInt();
		
	}

	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	        	obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	           
	        }
	        return obj;
	    }
	
	
	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

 public void logger(){
//	 System.out.println("Codigo=="+this.getCodigo()+"--"+" convenio= "+this.getConvenioPatrocinador().getCodigo()+" Id= "+this.getId()+"  Insti="+this.getInstitucion()+"  serialFinal= "+this.getSerialFinal()+" serialInic"+this.getSerialInicial());
	 
 }
	
	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the convenio_patrocinador
	 */
	public InfoDatosInt getConvenioPatrocinador() {
		return convenioPatrocinador;
	}

	/**
	 * @param convenio_patrocinador
	 *            the convenio_patrocinador to set
	 */
	public void setConvenioPatrocinador(InfoDatosInt convenioPatrocinador) {
		this.convenioPatrocinador = convenioPatrocinador;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion
	 *            the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}



	/**
	 * @return the fechaVigenciaInicial
	 */
	public String getFechaVigenciaInicial() {
		return this.fechaVigenciaInicial;	 
	}
	
	/**
	 * @return the fechaVigenciaInicial
	 */
	public String getFechaVigenciaInicialFormatoBD() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaVigenciaInicial);	 
	}
	
	

	/**
	 * @param fechaVigenciaInicial
	 *            the fechaVigenciaInicial to set
	 */
	public void setFechaVigenciaInicial(String fechaVigenciaInicial) {
			 this.fechaVigenciaInicial= fechaVigenciaInicial;
			 }

	/**
	 * @return the fechaVigenciaFinal
	 */
	public String getFechaVigenciaFinal() {
		return this.fechaVigenciaFinal;
	}

	/**
	 * @return the fechaVigenciaFinal
	 */
	public String getFechaVigenciaFinalFormatoBD() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaVigenciaFinal);
	}
	
	/**
	 * @param fechaVigenciaFinal
	 *            the fechaVigenciaFinal to set
	 */
	public void setFechaVigenciaFinal(String fechaVigenciaFinal) {
		this.fechaVigenciaFinal = fechaVigenciaFinal;
	}

	/**
	 * @return the programa
	 */
	public InfoDatosDouble getPrograma() {
		return programa;
	}

	/**
	 * @param programa
	 *            the programa to set
	 */
	public void setPrograma(InfoDatosDouble programa) {
		this.programa = programa;
	}

	/**
	 * @return the valorDescuento
	 */
	public double getValorDescuento() {
		return valorDescuento;
	}

	/**
	 * @param valorDescuento
	 *            the valorDescuento to set
	 */
	public void setValorDescuento(double valorDescuento) {
		this.valorDescuento = valorDescuento;
	}

	/**
	 * @return the porcentajeDescuento
	 */
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	/**
	 * @param porcentajeDescuento
	 *            the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";

	}

	/**
	 * @param fechaModifica
	 *            the fechaModifica to set
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
	 * @param horaModifica
	 *            the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica
	 *            the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

/**
 * 
 * @param estadoAsociado
 */
	public void setEstadoAsociado(boolean estadoAsociado) {
		this.estadoAsociado = estadoAsociado;
	}
/**
 * 
 * @return
 */
	public boolean isEstadoAsociado() {
		return estadoAsociado;
	}

	public BigDecimal getSerialInicial() {
		return serialInicial;
	}

	public void setSerialInicial(BigDecimal serialInicial) {
		this.serialInicial = serialInicial;
	}

	public BigDecimal getSerialFinal() {
		return serialFinal;
	}

	public void setSerialFinal(BigDecimal serialFinal) {
		this.serialFinal = serialFinal;
	}


}
