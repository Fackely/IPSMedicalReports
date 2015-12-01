/*
 * DevolucionPedidos.java 
 * Autor			:  mdiaz
 * Creado el	:  16-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.mundo.pedidos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DevolucionPedidosDao;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 16-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class DevolucionPedidos {

private DevolucionPedidosDao objectDao;
	
// datos de la cabecera de la devolucion del pedido	
private int  codigo;
private String  motivo;
private String  fechaDevolucion;
private String	horaDevolucion;
private String fechaGrabacion;
private String	horaGrabacion;
private String usuario;
private int estado; 
private String observaciones;
private int institucion;
private String esQuirurgico;
// conjunto de detalles de la devolucion del pedido
private ArrayList detalleDevolucionPedidos;

/**  * Lleva el numero de la secuencia a asignar en caso de que se queira ingresar manualmente */
private String secuenciaAsignar;




public boolean init(String tipoBD)
{
	if ( this.objectDao == null ) 
	{
		// Obtengo el DAO que encapsula las operaciones de BD de este objeto
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		this.objectDao = myFactory.getDevolucionPedidosDao();
		if( this.objectDao != null )
			return true;
	}

	return false;
}


public void clean(){
	codigo = -1;
	motivo = "";
	fechaDevolucion = "";
	horaDevolucion = "";
	fechaGrabacion = "";
	horaGrabacion = "";
	usuario = "";
	estado = -1; 
	observaciones = "";
	institucion = -1;
	this.esQuirurgico = "";
	detalleDevolucionPedidos = new ArrayList();
	this.secuenciaAsignar = "";
}


/** constructor por defecto */
public DevolucionPedidos(){
	clean();
	init(System.getProperty("TIPOBD"));
}


public DevolucionPedidos(String  motivo, String  fechaDevolucion, String	horaDevolucion,  String usuario,String observaciones,int institucion){ 
	clean();

	this.motivo = motivo;
	this.fechaDevolucion = fechaDevolucion;
	this.horaDevolucion = horaDevolucion;
	this.usuario = usuario;
	this.observaciones = observaciones;
	this.institucion = institucion;
	init(System.getProperty("TIPOBD"));
	this.secuenciaAsignar = "";
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
 * @return Returns the estado.
 */
public int getEstado() {
	return estado;
}
/**
 * @param estado The estado to set.
 */
public void setEstado(int estado) {
	this.estado = estado;
}
/**
 * @return Returns the fechaDevolucion.
 */
public String getFechaDevolucion() {
	return fechaDevolucion;
}
/**
 * @param fechaDevolucion The fechaDevolucion to set.
 */
public void setFechaDevolucion(String fechaDevolucion) {
	this.fechaDevolucion = fechaDevolucion;
}
/**
 * @return Returns the fechaGrabacion.
 */
public String getFechaGrabacion() {
	return fechaGrabacion;
}
/**
 * @param fechaGrabacion The fechaGrabacion to set.
 */
public void setFechaGrabacion(String fechaGrabacion) {
	this.fechaGrabacion = fechaGrabacion;
}
/**
 * @return Returns the horaDevolucion.
 */
public String getHoraDevolucion() {
	return horaDevolucion;
}
/**
 * @param horaDevolucion The horaDevolucion to set.
 */
public void setHoraDevolucion(String horaDevolucion) {
	this.horaDevolucion = horaDevolucion;
}
/**
 * @return Returns the horaGrabacion.
 */
public String getHoraGrabacion() {
	return horaGrabacion;
}
/**
 * @param horaGrabacion The horaGrabacion to set.
 */
public void setHoraGrabacion(String horaGrabacion) {
	this.horaGrabacion = horaGrabacion;
}
/**
 * @return Returns the motivo.
 */
public String getMotivo() {
	return motivo;
}
/**
 * @param motivo The motivo to set.
 */
public void setMotivo(String motivo) {
	this.motivo = motivo;
}
/**
 * @return Returns the usuario.
 */
public String getUsuario() {
	return usuario;
}
/**
 * @param usuario The usuario to set.
 */
public void setUsuario(String usuario) {
	this.usuario = usuario;
}

/**
 * @return Returns the detalleDevolucionPedidos.
 */
public ArrayList getDetalleDevolucionPedidos() {
	if(detalleDevolucionPedidos == null)
		return new ArrayList();
	return detalleDevolucionPedidos;
}
/**
 * @param detalleDevolucionPedidos The detalleDevolucionPedidos to set.
 */
public void setDetalleDevolucionPedidos(ArrayList detalleDevolucionPedidos) {
	this.detalleDevolucionPedidos = detalleDevolucionPedidos;
}


public int getCodigoDevolucionDisponible(Connection con){
	return objectDao.getCodigoDevolucionDisponible(con);
}


public int getSiguienteCodigoDevolucionDisponible(Connection con){
	return objectDao.getSiguienteCodigoDevolucionDisponible(con);
}



public int insertar(Connection con){
	return objectDao.insertar(con, this);
}
		

public ResultSetDecorator buscar(Connection con, String[] selectedColumns, String from, String where, String orderBy){
	return objectDao.buscar( con, selectedColumns, from, where, orderBy);
}


public String getNombreCentroCosto(Connection con, int codCentroCosto){
	return objectDao.getNombreCentroCosto(con, codCentroCosto);
}

/**
 * Método que consulta la fecha y hora de un pedido,
 * fecha + ConstantesBD.separadorSplit + hora
 * @param con
 * @param codigoPedido
 * @return
 */
public String consultarFechaHoraDespacho(Connection con,String codigoPedido)
{
	return objectDao.consultarFechaHoraDespacho(con,codigoPedido);
}

/**
 * Método implementado para realizar la busqueda avanzada de despachos
 * @param con
 * @param campos
 * @return
 */
public static HashMap consultarDespachos(Connection con,HashMap campos)
{
	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDevolucionPedidosDao().consultarDespachos(con,campos);
}


/**
 * @return Returns the observaciones.
 */
public String getObservaciones() {
	return observaciones;
}
/**
 * @param observaciones The observaciones to set.
 */
public void setObservaciones(String observaciones) {
	this.observaciones = observaciones;
}
/**
 * @return Returns the institucion.
 */
public int getInstitucion() {
	return institucion;
}
/**
 * @param institucion The institucion to set.
 */
public void setInstitucion(int institucion) {
	this.institucion = institucion;
}


/**
 * @return the esQuirurgico
 */
public String getEsQuirurgico() {
	return esQuirurgico;
}


/**
 * @param esQuirurgico the esQuirurgico to set
 */
public void setEsQuirurgico(String esQuirurgico) {
	this.esQuirurgico = esQuirurgico;
}


public String getSecuenciaAsignar() {
	return secuenciaAsignar;
}


public void setSecuenciaAsignar(String secuenciaAsignar) {
	this.secuenciaAsignar = secuenciaAsignar;
}
}
