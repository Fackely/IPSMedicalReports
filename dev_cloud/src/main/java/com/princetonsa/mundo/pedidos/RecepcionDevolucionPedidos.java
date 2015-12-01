/*
 * Created on 24-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.mundo.pedidos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RecepcionDevolucionPedidosDao;

/**
 * @author armando
 *
 * Princeton 24-sep-2004
 * Clase del mundo para manejar la recpcion de devolucion de los pedidos.
 */
public class RecepcionDevolucionPedidos {
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RecepcionDevolucionPedidosDao recepcionDao;
	
	/**
	 * Numero de la devolucion
	 */
	private int numeroDevolucion;
	/**
	 * Area que realiza la devolucion
	 */
	private int area;
	/**
	 * Nombre Area que realiza la devolucion
	 */
	private String areaNombre;
	
	/**
	 * 
	 */
	private String centroAtencion;
	/**
	 * Area que realiza la devolucion
	 */
	private int farmacia;
	/**
	 * Nombre Area que realiza la devolucion
	 */
	private String nombreFarmacia;
	/**
	 * Variable que almacena el motivo de la devolucion
	 */
	private String motivo;
	/**
	 * Fecha de la recepcion
	 */
	private String fechaRecepcion;
	/**
	 * Hora de la recepcion
	 */
	private String horaRecepcion;
	/**
	 * Usuario que recibe.
	 */
	private String usuarioRecibe;
	
	//datos especificos del detalle de la devolucion
	/**
	 * Pedido del cual se hace la devolucion
	 */
	private int pedido;
	/**
	 * Articulo que se devuelve
	 */
	private int articulo;
	/**
	 * Cantidad que se debe devolver
	 */
	private int cantidadDevuelta;
	/**
	 * Cantidad recibida en la devolucion
	 */
	private int cantidadRecibida;
	/**
	 * Collecion para contener el detalle de una recepcion de devolucion
	 */
	private Collection coleccionDetalle;
	/**
	 * Variable que maneja la fecha de la devolucion
	 */
	private String fechaDevolucion="";
	/**
	 * Variable que maneja la hora de la devolucion
	 */
	private String horaDevolucion="";
	/**
	 * Variable que almacena el estado de la devolucion para la impresion
	 * el codigo esta en la coleccion
	 */
	private String estadoDevolucion="";
	/**
	 * Variable que almacen el usuario que realizo la devolucion.
	 */
	private String usuarioDevolucion="";
	
	/**
	 * Variable que almacena el codigo del detalle de la devolucion.
	 */
	private int codigo;
	
	/**
	 * Variable que almacena el costo unitario del artículo
	 */
	private double costo;
	
	/**
	 * Variable que almacena la descripcion del artículo
	 */
	private String descripcionArticulo;
	
	/**
	 * 
	 */
	private String lote;
	
	/**
	 * 
	 */
	private String fechaVencimiento;
	
	/**
	 * 
	 */
	private String tipoRecepcion;
	
	/**
	 * 
	 */
	private String almacenConsignacion;
	
	/**
	 * 
	 */
	private String proveedorCompra;
	
	/**
	 * 
	 */
	private String proveedorCatalogo;
	
	/**
	 * Indica si un campo es quirurgico
	 */
	private String esQuirurgico;
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
			if (myFactory != null)
			{
				recepcionDao = myFactory.getRecepcionDevolucionPedidosDao();
				wasInited = (recepcionDao != null);
			}
			return wasInited;
	}
    
    public void reset()
    {
    	this.numeroDevolucion=0;
    	this.area=0;
    	this.fechaRecepcion="";
    	this.horaRecepcion="";
    	this.usuarioRecibe="";
    	this.pedido=0;
    	this.articulo=0;
    	this.cantidadDevuelta=0;
    	this.cantidadRecibida=0;
    	this.fechaDevolucion="";
    	this.horaDevolucion="";
    	this.estadoDevolucion="";
    	this.usuarioDevolucion="";
    	this.farmacia=-1;
    	this.nombreFarmacia="";
    	this.costo = 0;
    	this.descripcionArticulo = "";
    	this.centroAtencion="";
    	this.lote="";
    	this.fechaVencimiento="";
    	this.tipoRecepcion="";
    	this.almacenConsignacion="";
    	this.proveedorCatalogo="";
    	this.proveedorCompra="";
    	this.esQuirurgico = "";
    }
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros.
	 */

	public RecepcionDevolucionPedidos() 
    {
		    reset();
    		this.init (System.getProperty("TIPOBD"));
    }
	/**
     * Metodo que realiza la consulta de de las devoluciones que se realizaron con el 
     * centro de costo del usario logueado.
     * @param con,Conexion
     * @param ccentroCostoSolicitado,<b>int</b>, Centro de costo solicitado de quien realiza la consulta
     * @return Collection con los datos
     */
    public Collection consultarDevoluciones(Connection con,int centroCostoSolicitado,int institucion)
    {
        return  recepcionDao.consultarDevoluciones(con,centroCostoSolicitado,institucion);
    }
    
    /**
     * Metodo para realizar la consulta de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return ResultSet, resultset con los datos.
     */
    public Collection consultarUnaDevolucion(Connection con,int devolucion)
    {
    	return recepcionDao.consultarUnaDevolucion(con,devolucion);
    }
    
    /**
     * Metodo para realizar la consulta de una devolucion Qx
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return ResultSet, resultset con los datos.
     */
    public Collection consultarUnaDevolucionQx(Connection con,int devolucion)
    {
    	return recepcionDao.consultarUnaDevolucionQx(con,devolucion);
    }
    
	/**
     * Metodo para realizar la consulta de una recepcion de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return Resultset,Resultado de la consulta
     */
    public ResultSetDecorator consultarMaestroRecepcion(Connection con)
    {
    	return recepcionDao.consultarMaestroRecepcion(con,this.numeroDevolucion);
    }
    
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    
    public Collection consultarDetalleRecepcion(Connection con)
    {
    	return recepcionDao.consultarDetalleRecepcion(con,this.numeroDevolucion);
    }
    
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion Qx
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    
    public Collection consultarDetalleRecepcionQx(Connection con)
    {
    	return recepcionDao.consultarDetalleRecepcionQx(con,this.numeroDevolucion);
    }
    
    /**
     * Metodo que realiza la insercion de una recepcion
     * @param con
     * @return int
     * 
     */
    public int insertarRecepcionMaestro(Connection con)
	{
    	return recepcionDao.insertarRecepcionMaestro(con,this.numeroDevolucion,this.fechaRecepcion,this.horaRecepcion,this.usuarioRecibe);
	}
	/**
	 * Metodo para realizar la insercion del detalle de una recepcion devolucion pedido
	 * @param con
	 * @throws SQLException, Se manaeja en el action para terminar la transaccion.
	 */
    public int insertarRecepcionDetalle(Connection con)
	{
    	return recepcionDao.insertarRecepcionDetalle(con,this.codigo,this.numeroDevolucion,this.cantidadRecibida,this.lote,this.fechaVencimiento,this.articulo,this.tipoRecepcion,this.almacenConsignacion,this.proveedorCompra,this.proveedorCatalogo);
	}
    
    /**
     * Metodo que actualiza el estado de una devolucion
	 * @param con
	 * @return
	 */
	public int actualizarEstadoDevolucion(Connection con,int estadoDevolucion) 
	{
		return recepcionDao.actualizarEstadoDevolucion(con,this.numeroDevolucion,estadoDevolucion);
	}
	
	/**
	 * Método implementado para obtener el costo unitario del despacho del pedido
	 */
	public double obtenerCostoUnitarioDespacho(Connection con)
	{
		return recepcionDao.obtenerCostoUnitarioDespacho(con,this.pedido,this.articulo);
	}
	
	/**
	 * Método implementado para actualizar el costo unitario de un articulo
	 * de una devolucion
	 * @param con
	 * @return
	 */
	public int actualizarCostoUnitarioDevolucion(Connection con)
	{
		return recepcionDao.actualizarCostoUnitarioDevolucion(
				con,
				this.numeroDevolucion,
				this.pedido,
				this.articulo,
				this.costo);
	}
	
	/**
	 * Método implementado para actualizar 
	 * la cantidad de recepcion en la devolucion
	 * @param con
	 * @return
	 */
	public int actualizarDevolucion(Connection con)
	{
		return recepcionDao.actualizarDevolucion(
				con,
				this.numeroDevolucion,
				this.pedido,
				this.articulo,
				this.cantidadRecibida);
	}
	
	/**
	 * @return Retorna el fechaRecepcion.
	 */
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}
	/**
	 * @param fechaRecepcion Asigna el fechaRecepcion.
	 */
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	/**
	 * @return Retorna el horaRecepcion.
	 */
	public String getHoraRecepcion() {
		return horaRecepcion;
	}
	/**
	 * @param horaRecepcion Asigna el horaRecepcion.
	 */
	public void setHoraRecepcion(String horaRecepcion) {
		this.horaRecepcion = horaRecepcion;
	}
	/**
	 * @return Retorna el numeroDevolucion.
	 */
	public int getNumeroDevolucion() {
		return numeroDevolucion;
	}
	/**
	 * @param numeroDevolucion Asigna el numeroDevolucion.
	 */
	public void setNumeroDevolucion(int numeroDevolucion) {
		this.numeroDevolucion = numeroDevolucion;
	}
	/**
	 * @return Retorna el usuarioRecibe.
	 */
	public String getUsuarioRecibe() {
		return usuarioRecibe;
	}
	/**
	 * @param usuarioRecibe Asigna el usuarioRecibe.
	 */
	public void setUsuarioRecibe(String usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}
	/**
	 * @return Retorna el area.
	 */
	public int getArea() {
		return area;
	}
	/**
	 * @param area Asigna el area.
	 */
	public void setArea(int area) {
		this.area = area;
	}
	/**
	 * @return Retorna el articulo.
	 */
	public int getArticulo() {
		return articulo;
	}
	/**
	 * @param articulo Asigna el articulo.
	 */
	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}
	/**
	 * @return Retorna el cantidadDevuelta.
	 */
	public int getCantidadDevuelta() {
		return cantidadDevuelta;
	}
	/**
	 * @param cantidadDevuelta Asigna el cantidadDevuelta.
	 */
	public void setCantidadDevuelta(int cantidadDevuelta) {
		this.cantidadDevuelta = cantidadDevuelta;
	}
	/**
	 * @return Retorna el cantidadRecibida.
	 */
	public int getCantidadRecibida() {
		return cantidadRecibida;
	}
	/**
	 * @param cantidadRecibida Asigna el cantidadRecibida.
	 */
	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}
	/**
	 * @return Retorna el pedido.
	 */
	public int getPedido() {
		return pedido;
	}
	/**
	 * @param pedido Asigna el pedido.
	 */
	public void setPedido(int pedido) {
		this.pedido = pedido;
	}

	
	/**
	 * @return Retorna el coleccionDetalle.
	 */
	public Collection getColeccionDetalle() {
		return coleccionDetalle;
	}
	/**
	 * @param coleccionDetalle Asigna el coleccionDetalle.
	 */
	public void setColeccionDetalle(Collection coleccionDetalle) {
		this.coleccionDetalle = coleccionDetalle;
	}
	/**
	 * @return Retorna el areaNombre.
	 */
	public String getAreaNombre() {
		return areaNombre;
	}
	/**
	 * @param areaNombre Asigna el areaNombre.
	 */
	public void setAreaNombre(String areaNombre) {
		this.areaNombre = areaNombre;
	}
	/**
	 * @return Retorna el motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo Asigna el motivo.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	/**
	 * @return Retorna el estadoDevolucion.
	 */
	public String getEstadoDevolucion() {
		return estadoDevolucion;
	}
	/**
	 * @param estadoDevolucion Asigna el estadoDevolucion.
	 */
	public void setEstadoDevolucion(String estadoDevolucion) {
		this.estadoDevolucion = estadoDevolucion;
	}
	/**
	 * @return Retorna el fechaDevolucion.
	 */
	public String getFechaDevolucion() {
		return fechaDevolucion;
	}
	/**
	 * @param fechaDevolucion Asigna el fechaDevolucion.
	 */
	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	/**
	 * @return Retorna el horaDevolucion.
	 */
	public String getHoraDevolucion() {
		return horaDevolucion;
	}
	/**
	 * @param horaDevolucion Asigna el horaDevolucion.
	 */
	public void setHoraDevolucion(String horaDevolucion) {
		this.horaDevolucion = horaDevolucion;
	}
	/**
	 * @return Retorna el usuarioDevolucion.
	 */
	public String getUsuarioDevolucion() {
		return usuarioDevolucion;
	}
	/**
	 * @param usuarioDevolucion Asigna el usuarioDevolucion.
	 */
	public void setUsuarioDevolucion(String usuarioDevolucion) {
		this.usuarioDevolucion = usuarioDevolucion;
	}
	/**
	 * @return Retorna el farmacia.
	 */
	public int getFarmacia() {
		return farmacia;
	}
	/**
	 * @param farmacia Asigna el farmacia.
	 */
	public void setFarmacia(int farmacia) {
		this.farmacia = farmacia;
	}
	/**
	 * @return Retorna el nombreFarmacia.
	 */
	public String getNombreFarmacia() {
		return nombreFarmacia;
	}
	/**
	 * @param nombreFarmacia Asigna el nombreFarmacia.
	 */
	public void setNombreFarmacia(String nombreFarmacia) {
		this.nombreFarmacia = nombreFarmacia;
	}
	/**
	 * @return Retorna el codigo.
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo Asigna el codigo.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the costo.
	 */
	public double getCosto() {
		return costo;
	}
	/**
	 * @param costo The costo to set.
	 */
	public void setCosto(double costo) {
		this.costo = costo;
	}
	/**
	 * @return Returns the descripcionArticulo.
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	/**
	 * @param descripcionArticulo The descripcionArticulo to set.
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
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

	/**
	 * @return the almacenConsignacion
	 */
	public String getAlmacenConsignacion() {
		return almacenConsignacion;
	}

	/**
	 * @param almacenConsignacion the almacenConsignacion to set
	 */
	public void setAlmacenConsignacion(String almacenConsignacion) {
		this.almacenConsignacion = almacenConsignacion;
	}

	/**
	 * @return the proveedorCatalogo
	 */
	public String getProveedorCatalogo() {
		return proveedorCatalogo;
	}

	/**
	 * @param proveedorCatalogo the proveedorCatalogo to set
	 */
	public void setProveedorCatalogo(String proveedorCatalogo) {
		this.proveedorCatalogo = proveedorCatalogo;
	}

	/**
	 * @return the proveedorCompra
	 */
	public String getProveedorCompra() {
		return proveedorCompra;
	}

	/**
	 * @param proveedorCompra the proveedorCompra to set
	 */
	public void setProveedorCompra(String proveedorCompra) {
		this.proveedorCompra = proveedorCompra;
	}

	/**
	 * @return the tipoRecepcion
	 */
	public String getTipoRecepcion() {
		return tipoRecepcion;
	}

	/**
	 * @param tipoRecepcion the tipoRecepcion to set
	 */
	public void setTipoRecepcion(String tipoRecepcion) {
		this.tipoRecepcion = tipoRecepcion;
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
		
}