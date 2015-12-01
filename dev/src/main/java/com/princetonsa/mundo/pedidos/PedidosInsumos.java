/*
 * Creado  15/09/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.pedidos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PedidosInsumosDao;
import com.princetonsa.dao.SolicitudMedicamentosDao;
import com.princetonsa.dao.sqlbase.SqlBasePedidosInsumosDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;


/**
 * Clase para manejar todos los atributos, metodos correspondientes
 * a los pedidos de Insumos.
 *
 * @version 1.0, 15/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
@SuppressWarnings({"unchecked","unused"})
public class PedidosInsumos
{
/********************************------Atributos--------*********************************************/
    /**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
   private static PedidosInsumosDao pedidosDao; 
   /**
    * Para hacer logs de debug / warn / error de esta funcionalidad.
    */
   private Logger logger = Logger.getLogger(PedidosInsumos.class);   
   /**
   * Codigo del pedido, consecutivo asignado por el sistema.
   */
    private int numeroPedido;
  /**
   * Fecha del pedido, la postula el sistema, se puede cambiar.
   */
    private String fechaPedido;
  /**
   * Hora del pedido, la postula el sistema, se puede cambiar.
   */
    private String horaPedido;
    /**
     * Fecha de grabación del pedido
     */
    private String fechaGrabacion;
    /**
     * Hora de grabacion del pedido
     */
    private String horaGrabacion;
  /**
   * Centro de costo que requiere el pedido.
   */
    private String centroCosto;
  /**
   * Centro de costo que despacha el pedido.
   */
    private String farmacia;
  /**
   * Prioridad de la solicitud Si/No.
   */
    private String urgente;
    /**
     * Usuario que grabó el pedido
     */
    private String usuario;
    /**
     * Detalle de los artículos del pedido
     */
	private HashMap articulos = new HashMap();
    /**
     * Número de registros del detalle del pedido
     */
    private int numArticulos;
    
    private String entidadSubcontratada;
   
	private SolicitudMedicamentosDao solicitudMedicamentosDao;

  
/************************************-------Metodos-----*******************************************/ 
    
/**
 * Metodo para inicializar atributos de la clase.
 */
  public void reset ()
  {
      this.centroCosto="";
      this.farmacia="";
      this.fechaPedido="";
      this.horaPedido="";
      this.fechaGrabacion = "";
      this.horaGrabacion = "";
      this.numeroPedido=0;
      this.urgente="";
      this.articulos = new HashMap();
      this.numArticulos = 0;
      this.usuario = "";
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
				pedidosDao = myFactory.getPedidosInsumosDao();
				wasInited = (pedidosDao != null);
			}
			return wasInited;
	}
  
//constructor de la clase
//@todo insertar atributos del constructor
	public PedidosInsumos ()
	{
	    reset ();
		this.init (System.getProperty("TIPOBD"));
	}	
	
	/**
	 * Método usado para cargar la información de un pedido
	 * @param con
	 * @return
	 */
	public int cargarPedido(Connection con)
	{
		HashMap general = pedidosDao.cargarDatosGeneralesPedido(con,this.numeroPedido);
		int tamanio = Integer.parseInt(general.get("numRegistros") + "");
		if(tamanio > 0)
		{
			this.fechaPedido = general.get("fecha_0") +"";
			this.horaPedido = UtilidadFecha.convertirHoraACincoCaracteres(general.get("hora_0") + "");
			this.fechaGrabacion = general.get("fecha_grabacion_0") + "";
			this.horaGrabacion = UtilidadFecha.convertirHoraACincoCaracteres(general.get("hora_grabacion_0") + "");
			this.centroCosto = general.get("codigo_centro_costo_0") + ConstantesBD.separadorSplit + general.get("nombre_centro_costo_0");
			this.farmacia = general.get("codigo_farmacia_0") + ConstantesBD.separadorSplit + general.get("nombre_farmacia_0");
			this.urgente = general.get("urgente_0") + "";
			this.usuario = general.get("usuario_0") + "";
			
			this.articulos = pedidosDao.cargarDetallePedido(con,this.numeroPedido);
			this.numArticulos = Integer.parseInt(articulos.get("numRegistros")+"");
		}
		
		return tamanio;
	
	}
	
/**
 * para obtener el codigo del pedido actual.
 * @see SqlBasePedidosInsumosDao.
 * @param con Connection con la fuente de datos
 * @return int (codigo pedido)
 */  
	public int siguientePedido (Connection con)
	{
	    int codigo=0;
	    codigo=pedidosDao.cargarUltimoCodigoSequence(con);
	    //codigo=codigo +1 ;
	    return codigo;
	}
	 
	
	/**
	 * metodo de busqueda generico
	 * @param con   conexion con la fuente de datos
	 * @param columns   columnas seleccionadas de la busqueda en un array de strings
	 * @param from   clausula from de la consulta
	 * @param where  clausula where de la consulta
	 * @param orderBy   columna por la cual se ordenara la consulta
	 *  @param orderDirection  direccion del ordenamiento
	 * @return  un resultset con el conjunto de datos resultantes de la consulta
	 */
	public ResultSetDecorator buscar(Connection con, String[] columns, String from, String where, String orderBy, String orderDirection){
		 return SqlBasePedidosInsumosDao.buscar(con, columns, from, where, orderBy, orderDirection);
	}

	
	/**
	 *  metodo que me permite saber si puedo mostrar determinada funcionalidad dado su codigo y el login del usuario actual
	 * @param con   una conexion con la fuente de datos
	 * @param codigoFuncionalidad   el codigo de la funcionalidad
	 * @param loginUsuario  el login del usuario actual en el sistema
	 * @return  un array de maximo dos String el primero apunta al nombre de la funcionalidad; el segundo a la ruta de la funcionalidad. 
	 *               De no poder ingresar a esta funcionalidad se retornaran Strings vacios 
	 */
	public String[] puedoMostrarFuncionalidad(Connection con, int codigoFuncionalidad, String loginUsuario, boolean onlyPath){
		return SqlBasePedidosInsumosDao.puedoMostrarFuncionalidad(con, codigoFuncionalidad, loginUsuario, onlyPath);
	}
	
	/**
	 * obtiene le nombre del centro de costo dado su codigo
	 * @param con  una conexion abierta con la fuente de datos
	 * @param codCentroCosto   codigo del centro de costo 
	 * @return  el nombre del centro de costo asociado con este codigo
	 */
	public String getNombreCentroCosto(Connection con, int codCentroCosto){
		return SqlBasePedidosInsumosDao.getNombreCentroCosto(con, codCentroCosto);
	}
	
	/**
	 * Metodo para insertar Un pedido y su respectivo detalle, por medio de transacciones.
	 * @param string 
	 * @param con, Connection con la fuente de datos.
	 * @param centroCostoSolicitante, centro de costo que solicita.
	 * @param centroCostoSolicitado, centro de costo que despacha.
	 * @param urgente, estado del pedido Urgente Si / No.
	 * @param obsevacionesGenerales, observaciones del pedido.
	 * @param usuario, usuario que ingresa el pedido.
	 * @param terminarPedido, boolean true terminar pedido.
	 * @param codigoPedido, codigo del pedido para el detalle.
	 * @param codigoArticulo, codigo del articulo para el detalle.
	 * @param cantidad, cantidad a despachar de un articulo.
	 * @param estado, estado del pedido solicitado ó terminado.
	 * @return, in 1 efectivo 0 de lo contrario.
	 * @throws SQLException
	 */
	public int insertarPedidoYDetalle(Connection con,
												        int centroCostoSolicitante,
												        int centroCostoSolicitado,
												        boolean urgente,
												        String obsevacionesGenerales,
												        UsuarioBasico usuario,
												        boolean terminarPedido,
												        ArrayList arrayListCodigos,
												        ArrayList arrayListCantidad,
												        String estado,
												        String fechaPedido,
												        String horaPedido, 
												        String esQuirurgico,
												        String autoPorSub) throws SQLException
	
	{
	    int resp=0;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

	    if (pedidosDao == null) 
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (pedidosInsumosDao - insertarPedidoYDetalle )");
		}
	    
//**********Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;

		if (estado.equals(ConstantesBD.inicioTransaccion)) 
		{
			inicioTrans = myFactory.beginTransaction(con);
		} 
		else 
		{
			inicioTrans = true;
		}
		
		if (!inicioTrans ) 
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else 
		{
		   int codigoArticulo=0,cantidad=0;
		   
		   ArrayList<DtoEntidadSubcontratada> entidades =null;
			

			String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, centroCostoSolicitado);
						
			if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				entidades = CargosEntidadesSubcontratadas.obtenerEntidadesSubcontratadasCentroCosto(con, centroCostoSolicitado, usuario.getCodigoInstitucionInt());
				
				for(int i=0;i<entidades.size();i++)
				{
					entidadSubcontratada=entidades.get(i).getConsecutivo();
				}
			}  
					   
		   resp=pedidosDao.insertarAdmin(con,
		           UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
		           UtilidadFecha.getHoraActual(),
		           centroCostoSolicitante, 
		           centroCostoSolicitado,
		           urgente,
		           obsevacionesGenerales,
		           usuario.getLoginUsuario(),
		           terminarPedido,
		           UtilidadFecha.conversionFormatoFechaABD(fechaPedido),
		           horaPedido,
		           entidadSubcontratada,
		           esQuirurgico,
		           autoPorSub);
		   if (resp < 1) 
			{
				logger.warn("Error insertando detalle de pedidos (codigo pedido)->"+pedidosDao.cargarUltimoCodigoSequence(con));
				myFactory.abortTransaction(con);
				return -1;
			}
		   
		   int codigoPedido=pedidosDao.cargarUltimoCodigoSequence(con);
		   for (int k=0; k <  arrayListCodigos.size(); k++)
		   {
			   //por alguna razon en suba estan llegando datos nulos
			   if(arrayListCodigos.get(k)!=null)
			   {
					   
			       codigoArticulo= Integer.parseInt(arrayListCodigos.get(k) + "");
			       cantidad=Integer.parseInt(arrayListCantidad.get(k) + "");
			       
			       resp=pedidosDao.insertarDetallePedido(con,codigoPedido,codigoArticulo,cantidad);
			       if (resp < 1) 
					{
						logger.warn("Error insertando detalle de pedidos (codigo articulo)->"+arrayListCodigos.get(k) + "");
						myFactory.abortTransaction(con);
						return -1;
					}
			   }
		   }
			
		}
	    
		if (estado.equals(ConstantesBD.inicioTransaccion)) 
		{
			myFactory.endTransaction(con);
		}
		return resp;
	}
	

	/**
	 * Método implementado para modificar un pedido con su detalle
	 * @param con
	 * @param codigoPedido
	 * @param centroCostoSolicitante
	 * @param centroCostoSolicitado
	 * @param urgente
	 * @param observacionesGenerales
	 * @param estado
	 * @param motivoAnulacion
	 * @param usuario
	 * @param detallePedidos
	 * @param numeroIngresos
	 * @param detalleEliminacion
	 * @param estadoTransaccion 
	 * @return
	 * @throws SQLException
	 */
	public int modificarPedidoYDetalle(Connection con, int codigoPedido, int centroCostoSolicitante, 
			int centroCostoSolicitado, boolean urgente, String observacionesGenerales, int estado, String motivoAnulacion,  
			String usuario, HashMap detallePedidos, int numeroIngresos, HashMap detalleEliminacion, String estadoTransaccion ) throws SQLException 
	{
		//variables auxiliares a usar
		int resp = 0;
		boolean inicioTrans = false;
		String accion;
		int codigoArticulo = 0, cantidad = 0, numRegEliminacion = 0;
	
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (pedidosDao == null)
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (PedidoInsumos - modificarPedidoYDetalle )");
		}
	
		if(estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
			inicioTrans = myFactory.beginTransaction(con);
		else
			inicioTrans = true;
	
	
		if (!inicioTrans)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		
			resp = pedidosDao.modificarPedido(con, codigoPedido, centroCostoSolicitante, centroCostoSolicitado, urgente, observacionesGenerales, estado, motivoAnulacion, usuario);
			if (resp < 1)
			{
					logger.warn("Error modificando el pedido (PedidoInsumos - modificarPedidoYDetalle )");
					myFactory.abortTransaction(con);
					return 0;
			}
			
			//se verifica que el pedido no se haya anulado
			if(estado!=ConstantesBD.codigoEstadoPedidoAnulado)
			{
				try
				{
					numRegEliminacion = Integer.parseInt(detalleEliminacion.get("numRegistros")+"");
				}
				catch(Exception e)
				{
					numRegEliminacion = 0;
				}
				// realizamos primero las eliminaciones de registros de detalles
				for (int k=0; k <  numRegEliminacion; k++)
				{
					codigoArticulo = Integer.parseInt(detalleEliminacion.get("codigoArticulo_"+k) + "");
					resp = pedidosDao.eliminarDetallePedido(con, codigoPedido, codigoArticulo);
					if (resp < 1){
						logger.warn("Error eliminando el detalle de pedidos   (PedidoInsumos - modificarPedidoYDetalle )");
						myFactory.abortTransaction(con);
						return 0;
					}
				}
				
				// realizamos segundo las modificaciones de registros de detalles
				for (int k=0; k <  numeroIngresos; k++)
				{ 
					accion = detallePedidos.get("accion_"+k) + "";
					codigoArticulo = Integer.parseInt(detallePedidos.get("codigoArticulo_"+k) + "");
					cantidad = Integer.parseInt(detallePedidos.get("cantidadDespacho_"+k) + "");
					
					//se verifica si es un registro de modificacion
					if(accion.equals("modify"))
					{
						resp = pedidosDao.modificarDetallePedido(con, codigoPedido, codigoArticulo, cantidad);
						if (resp < 1)
						{
							logger.warn("Error modificando el detalle de pedidos  (PedidoInsumos - modificarPedidoYDetalle )");
							myFactory.abortTransaction(con);
							return 0;
						}
					}
				}
			
				// realizamos por ultimo las adiciones de registros de detalles
				for (int k=0; k <  numeroIngresos; k++)
				{
						
					accion = detallePedidos.get("accion_"+k) + ""; 
					codigoArticulo = Integer.parseInt(detallePedidos.get("codigoArticulo_"+k) + "");
					cantidad = Integer.parseInt(detallePedidos.get("cantidadDespacho_"+k) + "");
		
					if(accion.equals("add"))
					{
						resp = pedidosDao.insertarDetallePedido(con, codigoPedido, codigoArticulo, cantidad);
						if (resp < 1)
						{
							logger.warn("Error adicionando el detalle de pedidos  (PedidoInsumos - modificarPedidoYDetalle )");
							myFactory.abortTransaction(con);
							return 0;
						}
					}
				}
			}
		
		}
		
		if(estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
			myFactory.endTransaction(con);
		return resp;
	}

	
	
	/**
	 * Metodo para listar un pedido de insumo especifico.
	 * @param con, Connection con la funete de datos.
	 * @param codigoPedido, int Codigo del pedido a listar.
	 * @return, Collection
	 */
	public Collection listarPedidos(Connection con, int codigoPedido)
	{
	    pedidosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPedidosInsumosDao();
	    Collection coleccion = null;
	    
	    try 
	    {
			coleccion = UtilidadBD.resultSet2Collection(pedidosDao.listarPedidoInsumo(con, codigoPedido));
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo pedidos Insumos con el pedido->"+codigoPedido+" "+e.toString());
			coleccion = null;
		}
	    
		return coleccion;
	}
	
	/**
	 * Método implementado para cargar los datos de anulación de un pedido
	 * @param con
	 * @return
	 */
	public HashMap cargarDatosAnulacion(Connection con)
	{
		return pedidosDao.cargarDatosAnulacion(con,this.numeroPedido);
	}
	
	/**
	 * Método que consulta los datos adicionales de la peticion de un pedido
	 * @param con
	 * @return
	 */
	public HashMap cargarDatosPeticionPedido(Connection con)
	{
		return pedidosDao.cargarDatosPeticionPedido(con, this.numeroPedido+"");
	}
	
/**************************----------Getters and Setters-------************************************************/
     
/**
 * @return Retorna  centroCosto.
 */
public String getCentroCosto()
{
    return centroCosto;
}
/**
 * @param centroCosto asigna centroCosto.
 */
public void setCentroCosto(String centroCosto)
{
    this.centroCosto = centroCosto;
}
/**
 * @return Retorna  farmacia.
 */
public String getFarmacia()
{
    return farmacia;
}
/**
 * @param farmacia asigna farmacia.
 */
public void setFarmacia(String farmacia)
{
    this.farmacia = farmacia;
}
/**
 * @return Retorna  fechaPedido.
 */
public String getFechaPedido()
{
    return fechaPedido;
}
/**
 * @param fechaPedido asigna fechaPedido.
 */
public void setFechaPedido(String fechaPedido)
{
    this.fechaPedido = fechaPedido;
}
/**
 * @return Retorna  horaPedido.
 */
public String getHoraPedido()
{
    return horaPedido;
}
/**
 * @param horaPedido asigna horaPedido.
 */
public void setHoraPedido(String horaPedido)
{
    this.horaPedido = horaPedido;
}
/**
 * @return Retorna  numeroPedido.
 */
public int getNumeroPedido()
{
    return numeroPedido;
}
/**
 * @param numeroPedido asigna numeroPedido.
 */
public void setNumeroPedido(int numeroPedido)
{
    this.numeroPedido = numeroPedido;
}
/**
 * @return Retorna  urgente.
 */
public String getUrgente()
{
    return urgente;
}
/**
 * @param urgente asigna urgente.
 */
public void setUrgente(String urgente)
{
    this.urgente = urgente;
}
	/**
	 * @return Returns the articulos.
	 */
	public HashMap getArticulos() {
		return articulos;
	}
	/**
	 * @param articulos The articulos to set.
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
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
	 * @return Returns the numArticulos.
	 */
	public int getNumArticulos() {
		return numArticulos;
	}
	
	public String getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	public void setEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}

	/**
	 * @param numArticulos The numArticulos to set.
	 */
	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
	}
}
