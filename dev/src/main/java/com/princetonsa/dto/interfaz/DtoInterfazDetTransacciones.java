package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoInterfazDetTransacciones implements Serializable{
	
	private String transaccion;
	
	private int articulo;
	
	private int cantidad;
	
	private double val_unitario;
	
	private String lote;
	
	private String fecha_vencimiento;
	
	public DtoInterfazDetTransacciones()
	{
		this.transaccion="";
		this.articulo=ConstantesBD.codigoNuncaValido;
		this.cantidad=ConstantesBD.codigoNuncaValido;
		this.val_unitario=ConstantesBD.codigoNuncaValido;
		this.lote=null;
		this.fecha_vencimiento=null;
	}
	
	public DtoInterfazDetTransacciones(String transaccion,int articulo,int cantidad,double val_unitario)
	{
		this.transaccion=transaccion;
		this.articulo=articulo;
		this.cantidad=cantidad;
		this.val_unitario=val_unitario;
		this.lote=null;
		this.fecha_vencimiento=null;
	}

	public int getArticulo() {
		return articulo;
	}

	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getFecha_vencimiento() {
		return fecha_vencimiento;
	}

	public void setFecha_vencimiento(String fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public double getVal_unitario() {
		return val_unitario;
	}

	public void setVal_unitario(double val_unitario) {
		this.val_unitario = val_unitario;
	}

}
