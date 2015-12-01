/*
 * @(#)DespachoPedidos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.pedidos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DespachoPedidosDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * Clase para el manejo de despacho de pedidos
 * @version 1.0, Septiembre 29, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class DespachoPedidos 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static DespachoPedidosDao despachoDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(DespachoPedidos.class);

	/**
	 * Número de pedido
	 */
	private int numeroPedido;
	
	/**
	 * Fecha - Hora del pedido
	 */
	private String fechaHoraPedido;

	/**
	 * Usuario Solicitante
	 */
	private String usuarioSolicitante;
	
	/**
	 * Observaciones generales
	 */
	private String observacionesGenerales;
	
	/**
	 * Fecha - Hora de Grabación del pedido
	 */
	private String fechaHoraGrabacion;
	
	/**
	 * Centro de costo que solicita
	 */
	private String centroCostoSolicitante;
	
	/**
	 * Centro de costo que solicita
	 */
	private String codCentroCostoSolicitante;
	
	/**
	 * Identificador de Prioridad
	 */
	private String identificadorPrioridad;
	
	/**
	 * Estado del pedido
	 */
	private String estadoPedido;
	
	/**
	 * Farmacia del pedido
	 */
	private String farmacia;
	
	/**
	 * Fecha - Hora de Despacho
	 */
	private String fechaHoraDespacho;
	
	/**
	 * usuario basico
	 */
	private UsuarioBasico usuario;
	
	/**
	 * fecha del despacho
	 */
	private String fechaDespacho;
	
	/**
	 * hora del despacho
	 */
	private String horaDespacho;
	
	/**
	 * aalmaecna las horas y fechas de los pedidos
	 */
	private HashMap<String, Object> fechaHoraPedidos;
	
	/**
	 * resetea los datos pertinentes
	 */
	public void reset()
	{
		this.numeroPedido=0;
		this.fechaHoraPedido="";
		this.usuarioSolicitante="";
		this.observacionesGenerales="";
		this.fechaHoraGrabacion="";
		this.centroCostoSolicitante="";
		this.identificadorPrioridad="";
		this.estadoPedido="";
		this.farmacia="";
		this.fechaHoraDespacho="";
		this.codCentroCostoSolicitante="";
		this.usuario = new UsuarioBasico();
	}
	
	/**
	 * Constructor 
	 *
	 */
	public DespachoPedidos()
	{
		this.init (System.getProperty("TIPOBD"));
		this.reset();
	}
	
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
				despachoDao = myFactory.getDespachoPedidosDao();
				wasInited = (despachoDao != null);
			}
			return wasInited;
	}
	
	/**
	 * Carga el Listado de los pedidos en estado TERMINADO filtrados deacuerdo a la farmacia
	 * solicitada en el pedido versus el centro de costo del usuario que ingresa a la opción  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param centroCostoUser, int, codigo del centro costo del user
	 * @return ResulSet list
	 */
	public Collection listadoPedidos(Connection con, int centroCostoUser)
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoPedidosDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.listadoPedidos(con,centroCostoUser));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho pedidos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * Carga el detalle de un pedido Part 1  (Collection con la info de articulos)
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @param almacen
	 * @param institucion
	 * @return Collection 
	 */
	public Collection detallePedidoPart1(Connection con, int numeroPedido,int almacen, int institucion)
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoPedidosDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.detallePedidoPart1(con,numeroPedido,almacen,institucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho pedidos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	
	/**
	 * Carga el detalle de un pedido Part 2  (encabezado y observaciones)
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @return boolean
	 */
	public boolean cargarDetallePedidoPart2(Connection con, int numeroPedido)
	{
		try
		{
			ResultSetDecorator rs=despachoDao.detallePedidoPart2(con,numeroPedido);
			
			if (rs.next())
			{
				this.fechaHoraPedido=rs.getString("fechaHoraPedido");
				this.numeroPedido=rs.getInt("numeroPedido");
				this.usuarioSolicitante=rs.getString("usuarioSolicitante");
				this.observacionesGenerales=rs.getString("observacionesGenerales");
				this.fechaHoraGrabacion=rs.getString("fechaHoraGrabacion");
				this.centroCostoSolicitante=rs.getString("centroCostoSolicitante");
				this.codCentroCostoSolicitante=rs.getString("codCentroCostoSolicitante");
				this.identificadorPrioridad=rs.getString("identificadorPrioridad");
				this.estadoPedido=rs.getString("estadoPedido");
				this.farmacia=rs.getString("farmacia");
				this.fechaHoraDespacho=rs.getString("fechaHoraDespacho");
				
				return true;
			}
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn("Error mundo pedidos (detalle parte 2) " +e.toString());
			return false;
		}
	}

	/**
	 * Método que Cambia el estado del pedido a DESPACHADO
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int cambiarEstadoPedido(Connection con) throws SQLException 
	{
			int resp=0;
			resp=despachoDao.cambiarEstadoPedido(con,this.numeroPedido);
			return resp;
	}
	
	/**
	 * Método Transaccional que inserta el despacho de los pedidos, cambiando el estado de despacho
	 * @param despachoPedido
	 * @param tamanioHashMap
	 * @param numeroPedido
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public int insertarDespacho(		Connection con, HashMap despachoPedidoMap, 
															int tamanioHashMap, int numeroPedido, 
															String user, String estado, String action) throws SQLException, IPSException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (despachoDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (DespachoPedidosDao - insertarDespacho )");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		if (estado.equals(ConstantesBD.inicioTransaccion))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		
		int validarInsertar= despachoDao.insertarDespachoBasico(con,numeroPedido,user);
		
		if(!inicioTrans||validarInsertar<1)
		{
		    myFactory.abortTransaction(con);
			return validarInsertar;
		}
		else
		{
			int resp=0;
			for(int w=0; w<tamanioHashMap; w++)
			{
				String tipoDes="",almCon="",provCompra="",provCatal="";
				if(despachoPedidoMap.containsKey("tipodespacho_"+w))
					tipoDes=despachoPedidoMap.get("tipodespacho_"+w)+"";
				if(despachoPedidoMap.containsKey("almacenConsignacion_"+w))
					almCon=despachoPedidoMap.get("almacenConsignacion_"+w)+"";
				if(despachoPedidoMap.containsKey("proveedorCompra_"+w))
					provCompra=despachoPedidoMap.get("proveedorCompra_"+w)+"";
				if(despachoPedidoMap.containsKey("proveedorCatalogo_"+w))
					provCatal=despachoPedidoMap.get("proveedorCatalogo_"+w)+"";
				
				
				resp = despachoDao.insertarDetalleDespachoPedido(con, 
						numeroPedido, 
						Integer.parseInt(despachoPedidoMap.get("codigosArt_"+w)+""),
						Integer.parseInt(despachoPedidoMap.get("cantidadADespachar_"+w)+""),
						Float.parseFloat(despachoPedidoMap.get("costo_"+w)+""),
						despachoPedidoMap.get("lote_"+w)+"",
						despachoPedidoMap.get("fechavencimiento_"+w)+"",tipoDes,almCon,provCompra,provCatal);
				if(resp<1)
				{
				    myFactory.abortTransaction(con);
					return resp;
				}
				else if (action.equals("DespachoPedidoQxAction"))
				{
					if(Utilidades.convertirAEntero(fechaHoraPedidos.get("codigoPedido_" + w)+"") == numeroPedido)
					{
						CargosEntidadesSubcontratadas cargosEntidadesSubcontratadas = new CargosEntidadesSubcontratadas();
						cargosEntidadesSubcontratadas
						.generarCargoArticulo(
								con,
								Utilidades.convertirAEntero(farmacia),
								Utilidades
										.convertirAEntero(despachoPedidoMap
												.get("codigosArt_" + w)
												+ ""),
								ConstantesBD.codigoNuncaValido,
								"",
								numeroPedido + "",
								fechaHoraPedidos.get("fechaPedido_" + w)
										+ "",
								UtilidadFecha
										.convertirHoraACincoCaracteres(fechaHoraPedidos
												.get("horaPedido_" + w)
												+ ""), false, usuario,"", "");
					}
				}
			}
			resp= despachoDao.cambiarEstadoPedido(con,numeroPedido);
			if(resp<1)
			{
			    myFactory.abortTransaction(con);
				return resp;
			}
		}
		return 1;
	}
	
	/**
	 * Carga el resumen del despacho del pedido
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @return Collection 
	 */
	public Collection resumen(Connection con, int numeroPedido)
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoPedidosDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.resumen(con,numeroPedido));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho pedidos (Resumen) " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * @return Returns the fechaHoraSolicitud.
	 */
	public String getFechaHoraPedido() {
		return fechaHoraPedido;
	}
	/**
	 * @param fechaHoraSolicitud The fechaHoraSolicitud to set.
	 */
	public void setFechaHoraPedido(String fechaHoraPedido) {
		this.fechaHoraPedido = fechaHoraPedido;
	}
	/**
	 * @return Returns the numeroPedido.
	 */
	public int getNumeroPedido() {
		return numeroPedido;
	}
	/**
	 * @param numeroPedido The numeroPedido to set.
	 */
	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	/**
	 * @return Returns the usuarioSolicitante.
	 */
	public String getUsuarioSolicitante() {
		return usuarioSolicitante;
	}
	/**
	 * @param usuarioSolicitante The usuarioSolicitante to set.
	 */
	public void setUsuarioSolicitante(String usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	
	/**
	 * @return Returns the fechaHoraGrabacion.
	 */
	public String getFechaHoraGrabacion() {
		return fechaHoraGrabacion;
	}
	/**
	 * @param fechaHoraGrabacion The fechaHoraGrabacion to set.
	 */
	public void setFechaHoraGrabacion(String fechaHoraGrabacion) {
		this.fechaHoraGrabacion = fechaHoraGrabacion;
	}
	
	/**
	 * @return Returns the centroCostoSolicitante.
	 */
	public String getCentroCostoSolicitante() {
		return centroCostoSolicitante;
	}
	/**
	 * @param centroCostoSolicitante The centroCostoSolicitante to set.
	 */
	public void setCentroCostoSolicitante(String centroCostoSolicitante) {
		this.centroCostoSolicitante = centroCostoSolicitante;
	}
	/**
	 * @return Returns the estadoPedido.
	 */
	public String getEstadoPedido() {
		return estadoPedido;
	}
	/**
	 * @param estadoPedido The estadoPedido to set.
	 */
	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	/**
	 * @return Returns the farmacia.
	 */
	public String getFarmacia() {
		return farmacia;
	}
	/**
	 * @param farmacia The farmacia to set.
	 */
	public void setFarmacia(String farmacia) {
		this.farmacia = farmacia;
	}
	/**
	 * @return Returns the identificadorPrioridad.
	 */
	public String getIdentificadorPrioridad() {
		return identificadorPrioridad;
	}
	/**
	 * @param identificadorPrioridad The identificadorPrioridad to set.
	 */
	public void setIdentificadorPrioridad(String identificadorPrioridad) {
		this.identificadorPrioridad = identificadorPrioridad;
	}
	/**
	 * @return Returns the fechaHoraDespacho.
	 */
	public String getFechaHoraDespacho() {
		return fechaHoraDespacho;
	}
	/**
	 * @param fechaHoraDespacho The fechaHoraDespacho to set.
	 */
	public void setFechaHoraDespacho(String fechaHoraDespacho) {
		this.fechaHoraDespacho = fechaHoraDespacho;
	}

	/**
	 * @return the codCentroCostoSolicitante
	 */
	public String getCodCentroCostoSolicitante() {
		return codCentroCostoSolicitante;
	}

	/**
	 * @param codCentroCostoSolicitante the codCentroCostoSolicitante to set
	 */
	public void setCodCentroCostoSolicitante(String codCentroCostoSolicitante) {
		this.codCentroCostoSolicitante = codCentroCostoSolicitante;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioBasico getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the fechaDespacho
	 */
	public String getFechaDespacho() {
		return fechaDespacho;
	}

	/**
	 * @param fechaDespacho the fechaDespacho to set
	 */
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}

	/**
	 * @return the horaDespacho
	 */
	public String getHoraDespacho() {
		return horaDespacho;
	}

	/**
	 * @param horaDespacho the horaDespacho to set
	 */
	public void setHoraDespacho(String horaDespacho) {
		this.horaDespacho = horaDespacho;
	}

	/**
	 * @return the fechaHoraPedidos
	 */
	public HashMap<String, Object> getFechaHoraPedidos() {
		return fechaHoraPedidos;
	}

	/**
	 * @param fechaHoraPedidos the fechaHoraPedidos to set
	 */
	public void setFechaHoraPedidos(HashMap<String, Object> fechaHoraPedidos) {
		this.fechaHoraPedidos = fechaHoraPedidos;
	}
}
