/*
 * DetalleDevolucionPedidos.java 
 * Autor			:  mdiaz
 * Creado el	:  16-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.mundo.pedidos;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 16-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class DetalleDevolucionPedidos 
{
int	 codigo;
int	 devolucion;
int	 pedido;
int	 articulo;
int	 cantidad;
String lote;
String fechaVencimiento;
double costoUnitario;


public DetalleDevolucionPedidos(){
	this.codigo = -1;
	this.devolucion = -1;
	this.pedido = -1;
	this.articulo = -1;
	this.cantidad = -1;
	this.costoUnitario = 0;
	this.lote="";
	this.fechaVencimiento="";
}



public DetalleDevolucionPedidos(int	 codigo, int	 devolucion, int	 pedido, int	 articulo, int	 cantidad,String lote,String fechaV){
	this.codigo = codigo;
	this.devolucion = devolucion;
	this.pedido = pedido;
	this.articulo = articulo;
	this.cantidad = cantidad;
	this.lote=lote;
	this.fechaVencimiento=fechaV;
}


/**
 * @return Returns the articulo.
 */
public int getArticulo() {
	return articulo;
}
/**
 * @param articulo The articulo to set.
 */
public void setArticulo(int articulo) {
	this.articulo = articulo;
}
/**
 * @return Returns the cantidad.
 */
public int getCantidad() {
	return cantidad;
}
/**
 * @param cantidad The cantidad to set.
 */
public void setCantidad(int cantidad) {
	this.cantidad = cantidad;
}
/**
 * @return Returns the codigo.
 */
public int getCodigo() {
	return codigo;
}
/**
 * @param codigo The codigo to set.
 */
public void setCodigo(int codigo) {
	this.codigo = codigo;
}
/**
 * @return Returns the devolucion.
 */
public int getDevolucion() {
	return devolucion;
}
/**
 * @param devolucion The devolucion to set.
 */
public void setDevolucion(int devolucion) {
	this.devolucion = devolucion;
}
/**
 * @return Returns the pedido.
 */
public int getPedido() {
	return pedido;
}
/**
 * @param pedido The pedido to set.
 */
public void setPedido(int pedido) {
	this.pedido = pedido;
}
/**
 * @return Returns the costoUnitario.
 */
public double getCostoUnitario() {
	return costoUnitario;
}
/**
 * @param costoUnitario The costoUnitario to set.
 */
public void setCostoUnitario(double costoUnitario) {
	this.costoUnitario = costoUnitario;
}



public String getFechaVencimiento()
{
	return fechaVencimiento;
}



public void setFechaVencimiento(String fechaVencimiento)
{
	this.fechaVencimiento = fechaVencimiento;
}



public String getLote()
{
	return lote;
}



public void setLote(String lote)
{
	this.lote = lote;
}
}
