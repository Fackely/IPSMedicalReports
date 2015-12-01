package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoFirmasValoresPorDefecto  implements Serializable
{
	private double codigoPk;
	String valorPorDefecto;
	String usuario;
	String cargo;
	int institucion;
	String tipo;
	String firmaDigital;
	String usuarioModifica;
	String fechaModifica;
	String horaModifica;
	
	void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoDouble;
		this.valorPorDefecto="";
		this.usuario="";
		this.cargo="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.tipo="";
		this.firmaDigital="";
		this.usuarioModifica="";
		this.horaModifica="";
		this.fechaModifica="";
	}
	
	public void resetConValorXDefecto()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoDouble;
		this.usuario="";
		this.cargo="";
		this.firmaDigital="";
		this.usuarioModifica="";
		this.horaModifica="";
		this.fechaModifica="";
	}
	
	public DtoFirmasValoresPorDefecto ()
	{
		this.reset();
	}

	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getValorPorDefecto() {
		return valorPorDefecto;
	}

	public void setValorPorDefecto(String valorPorDefecto) {
		this.valorPorDefecto = valorPorDefecto;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFirmaDigital() {
		return firmaDigital;
	}

	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}
	
}