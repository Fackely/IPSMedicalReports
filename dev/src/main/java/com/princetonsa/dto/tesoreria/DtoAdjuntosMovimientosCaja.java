package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 *  
 * Dto que permite transferir los datos de los Archivos adjuntos en la 
 * Consulta de entregas a transportadora de valores
 * 
 * @author Diana Carolina G
 * @anexo 1042
 */

public class DtoAdjuntosMovimientosCaja implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long codigoPk;
	private String nombreGenerado;
	private String nombreOriginal;
	private Date fecha;
	private String hora;
	private String chequeado;
	
	
	
	public DtoAdjuntosMovimientosCaja(){
		
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.nombreGenerado ="";
		this.nombreOriginal="";
		this.fecha= new Date();
		this.hora="";
		
		
	}
	
	
	
	
	public long getCodigoPk() {
		return codigoPk;
	}
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getNombreGenerado() {
		return nombreGenerado;
	}
	public void setNombreGenerado(String nombreGenerado) {
		this.nombreGenerado = nombreGenerado;
	}
	public String getNombreOriginal() {
		return nombreOriginal;
	}
	public void setNombreOriginal(String nombreOriginal) {
		this.nombreOriginal = nombreOriginal;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}




	public void setChequeado(String chequeado) {
		this.chequeado = chequeado;
	}




	public String getChequeado() {
		return chequeado;
	}
	
	
	

}
