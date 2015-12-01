package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.actionform.salasCirugia.DevolucionPedidoQxForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.DevolucionPedidoQxDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class DevolucionPedidoQx
{

	/**
     * Constructor de la Clase
     */
    public DevolucionPedidoQx()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static DevolucionPedidoQxDao aplicacionDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( aplicacionDao == null ) 
		{ 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getDevolucionPedidoQxDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que consulta las solicitudes de cirugia
	 * por paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap listadoSolicitudes(Connection con, int codigoCuenta)
	{
		return aplicacionDao.listadoSolicitudes(con, codigoCuenta);
	}
	
	/**
	 * Metodo que valida si la peticion de cirugia
	 * seleccionada tiene devoluciones de pedido en
	 * esta pendiente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String validarPeticionDevolucionesPendientes(Connection con, int codigoPeticion)
	{
		return aplicacionDao.validarPeticionDevolucionesPendientes(con, codigoPeticion);
	}
	
	/**
	 * Metodo que consulta el detalle de los servicios
	 * de la peticion de cirugia seleccionada
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap<String,Object> detallePeticion(Connection con, int codigoPeticion)
	{
		return aplicacionDao.detallePeticion(con, codigoPeticion);
	}
	
	/**
	 * Metodo que consulta el detalle de los articulos
	 * de cada una de las cirugias de la solicitud seleccionada
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap detalleSolicitudArticulos(Connection con, int codigoCuenta)
	{
		return aplicacionDao.detalleSolicitudArticulos(con, codigoCuenta, false);
	}
	
	/**
	 * Metodo que consulta el detalle de los articulos
	 * de cada una de las cirugias de la solicitud seleccionada
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	public static HashMap detalleSolicitudArticulosStatic(Connection con, int codigoSolicitud, boolean validacionFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDevolucionPedidoQxDao().detalleSolicitudArticulos(con, codigoSolicitud, validacionFactura);
	}
	
	/**
	 * Metodo que consulta las solicitudes de cirugia
	 * por rango
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap<String,Object> listadoPeticionesPorRangos(Connection con, DevolucionPedidoQxForm forma, int centroAtencion)
	{
		HashMap vo = new HashMap();
		vo.put("centroCostoDevuelve", forma.getCodigoCentroCostoDevuelve());
		vo.put("centroCostoEjecuta", forma.getCodigoCentroCostoEjecuta());
		vo.put("fechaInicial", forma.getFechaInicial());
		vo.put("fechaFinal", forma.getFechaFinal()); 
		vo.put("centroAtencion", centroAtencion);
		return aplicacionDao.listadoPeticionesPorRangos(con, vo);
	}
	
	/**
	 * Metodo para generar la devolucion
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public int devolucionPedidoQx(Connection con, DevolucionPedidoQxForm forma, UsuarioBasico usuario)
	{
		HashMap vo = new HashMap();
		vo.put("motivo", forma.getMotivoDevolucionSeleccionado());
		vo.put("usuario", usuario.getLoginUsuario());
		vo.put("institucion", usuario.getCodigoInstitucion());
		vo.put("observaciones", forma.getObservaciones());
		return aplicacionDao.devolucionPedidoQx(con, vo);
	}
	
	/**
	 * Metodo que devuelve el pedido de cirugia
	 * seleccionado
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param cantidadADevolver 
	 * @param farmacia 
	 * @param codigoArticulo 
	 * @param fechavencimiento 
	 * @param lote 
	 * @return
	 */
	public boolean devolverDetallePedidoQx(Connection con, DevolucionPedidoQxForm forma, UsuarioBasico usuario, String codigoArticulo, String pedido, String cantidadADevolver, String lote, String fechaVencimiento, int codigoDevolucion)
	{
		HashMap vo = new HashMap();
		vo.put("pedido", pedido);
		vo.put("articulo", codigoArticulo);
		vo.put("cantidad", cantidadADevolver);
		vo.put("lote", lote);
		vo.put("fechaVencimiento", fechaVencimiento);
		return aplicacionDao.devolverDetallePedidoQx(con, vo, codigoDevolucion);
	}
	
	/**
	 * Metodo que consulta la informacion de los almacenes
	 * del articulo seleccionado del consumo
	 * @param con
	 * @param codigoPeticion
	 * @param codigoArticulo 
	 * @return
	 */
	public HashMap<String,Object> consultarAlmacenes(Connection con, int codigoPeticion, int codigoArticulo)
	{
		return aplicacionDao.consultarAlmacenes(con, codigoPeticion, codigoArticulo);
	}
	
	/**
	 * Metodo que consulta el estado
	 * de la devolucion
	 * @param con
	 * @param codigoDevolucion 
	 * @return
	 */
	public String consultarEstadoDevolucion(Connection con, String codigoDevolucion)
	{
		return aplicacionDao.consultarEstadoDevolucion(con, codigoDevolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static Vector<String> obtenerPedidosPeticionesQx(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDevolucionPedidoQxDao().obtenerPedidosPeticionesQx(con, numeroSolicitud);
	}
	
	/**
	 * Metodo que consulta las peticiones de cirugia
	 * por paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public HashMap<String, Object> listadoPeticiones(Connection con, int codigoIngreso, int codigoInstitucion){
		return aplicacionDao.listadoPeticiones(con, codigoIngreso, codigoInstitucion);
	}
	
	/**
	 * Metodo que consulta el detalle de los articulos
	 * de cada una de las cirugias de la peticion seleccionada
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public HashMap<String,Object> detallePeticionArticulos(Connection con, int codigoPeticion)
	{
		return aplicacionDao.detallePeticionArticulos(con, codigoPeticion);
	}
}