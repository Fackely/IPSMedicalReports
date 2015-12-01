package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

public class DtoProrrogaAutorizacionEntSub implements Serializable{

	
	private String  consecutivo;
	private String  autorizacion;
	private String fechaVencimientoInicial;
	private String fechaVencimientoNueva;
	private String horaVencimientoNueva;
	private String fechaProrroga;
	private String horaProrroga;
    private String usuarioProrroga;
	
    
    public  DtoProrrogaAutorizacionEntSub()
    {
      this.reset();	
    }
    
    public void reset()
    {
     this.consecutivo=new String("");
     this.autorizacion=new String("");
     this.fechaVencimientoInicial=new String("");
     this.fechaProrroga=new String("");
     this.horaProrroga=new String("");
     this.usuarioProrroga=new String("");
     this.fechaVencimientoNueva=new String("");
     this.horaVencimientoNueva=new String("");
    	
    }
    
    public String getConsecutivo() {
		return consecutivo;
	}
    
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getAutorizacion() {
		return autorizacion;
	}
	
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}
	
	public String getFechaVencimientoInicial() {
		return fechaVencimientoInicial;
	}
	
	public void setFechaVencimientoInicial(String fechaVencimientoInicial) {
		this.fechaVencimientoInicial = fechaVencimientoInicial;
	}
	
	public String getFechaProrroga() {
		return fechaProrroga;
	}
	
	public void setFechaProrroga(String fechaProrroga) {
		this.fechaProrroga = fechaProrroga;
	}
	
	public String getHoraProrroga() {
		return horaProrroga;
	}
	
	public void setHoraProrroga(String horaProrroga) {
		this.horaProrroga = horaProrroga;
	}
	
	public String getUsuarioProrroga() {
		return usuarioProrroga;
	}
	
	public void setUsuarioProrroga(String usuarioProrroga) {
		this.usuarioProrroga = usuarioProrroga;
	}

	public String getFechaVencimientoNueva() {
		return fechaVencimientoNueva;
	}

	public void setFechaVencimientoNueva(String fechaVencimientoNueva) {
		this.fechaVencimientoNueva = fechaVencimientoNueva;
	}

	public String getHoraVencimientoNueva() {
		return horaVencimientoNueva;
	}

	public void setHoraVencimientoNueva(String horaVencimientoNueva) {
		this.horaVencimientoNueva = horaVencimientoNueva;
	}
    
    
    
    
	
	
	
}
