/*
 * Created on 6/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package util.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.UtilidadInventariosDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Clase que contiene las diferentes utilidades de el modulo de Inventarios.
 * @version 1.0, 6/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class UtilidadInventarios  
{
	private static Logger logger=Logger.getLogger(UtilidadInventarios.class);
	
    /**
	 * Metodo que retorna el DaoFactory de la funcionalidad
	 * @return
	 */
	private static UtilidadInventariosDao utilidadDao()  
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadInventariosDao();
	}
	 
	/**
	 * Metodo que retorna un mapa con el listado de almacenes Activos.
	 * @param institucion
	 * @param incluirAlmacenTodos 
	 * @return
	 */
	public static HashMap listadoAlmacensActivos(int institucion, boolean incluirAlmacenTodos)
	{
	    Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=utilidadDao().listadoAlmacensActivos(con,institucion,incluirAlmacenTodos);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone(); 
	}
	
     /**
     * metodo transaccional que actualiza las existencias articulos x almacen tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @param estadoTransaccion 
     * @return boolean 
     */
    public static boolean actualizarExistenciasArticuloAlmacenTransaccional(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion, String estadoTransaccion)throws SQLException
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS TRANSACCIONAL\n\n\n");
        boolean insertoBien=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertoBien=utilidadDao().actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
            if (!insertoBien)
            {
                myFactory.abortTransaction(con);
            }
            else
            	logger.info("\n\n...:::::... LA ACTUALIZACION DE LAS EXISTENCIAS DE ARTICULO POR ALMACEN ES EXITOSA ...:::::...\n");
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estadoTransaccion.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertoBien;
    }
    
    /**
     * metodo que actualiza las existencias articulos x almacen tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @return boolean 
     */
    public static boolean actualizarExistenciasArticuloAlmacen(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion)
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS NO TRANSACCIONAL\n\n\n");
        return utilidadDao().actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
    }
    

    /**
     * metodo transaccional que actualiza las existencias articulos x almacen x lote tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute
     * Actualiza tambien la existencias por almacen.
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @param estadoTransaccion
     * @param lote
     * @param fechaVencimiento (yyyy-mm-dd)
     * @return
     * @throws SQLException
     */
    public static boolean actualizarExistenciasArticuloAlmacenLoteTransaccional(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion, String estadoTransaccion,String lote,String fechaVencimiento)throws SQLException
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS POR LOTE TRANSACCIONAL\n\n\n");
        boolean insertoBien=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertoBien=utilidadDao().actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
            if (!insertoBien)
            {
                myFactory.abortTransaction(con);
            }
            else
            {
            	utilidadDao().actualizarExistenciasArticuloAlmacenLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,fechaVencimiento);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estadoTransaccion.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertoBien;
    }
    
    /**
     * metodo transaccional que actualiza las existencias articulos x almacen x lote tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute
     * Actualiza tambien la existencias por almacen.
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @param estadoTransaccion
     * @param lote
     * @param fechaVencimiento (yyyy-mm-dd)
     * @return
     * @throws SQLException
     */
    public static boolean actualizarArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion, String estadoTransaccion,String lote,String fechaVencimiento)throws SQLException
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS POR LOTE TRANSACCIONAL\n\n\n");
        boolean insertoBien=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertoBien=utilidadDao().actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
            if (!insertoBien)
            {
                myFactory.abortTransaction(con);
            }
            else
            {
            	utilidadDao().actualizarArticuloAlmacenXLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,fechaVencimiento);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estadoTransaccion.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertoBien;
    }
    
    /**
     * metodo transaccional que actualiza las existencias articulos x almacen x lote tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute.
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @param estadoTransaccion
     * @param lote
     * @param fechaVencimiento (yyyy-mm-dd)
     * @return
     * @throws SQLException
     */
    public static boolean actualizarExistenciasArticuloAlmacenSoloLoteTransaccional(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion, String estadoTransaccion,String lote,String fechaVencimiento)throws SQLException
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS POR LOTE TRANSACCIONAL\n\n\n");
        boolean insertoBien=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertoBien=utilidadDao().actualizarExistenciasArticuloAlmacenLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,fechaVencimiento);
            if (!insertoBien)
            {
                myFactory.abortTransaction(con);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estadoTransaccion.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertoBien;
    }
    
    /**
     * metodo que actualiza las existencias articulos x almacen x lote tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute
     * Actualiza tambien la existencias por almacen.
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @param estadoTransaccion
     * @param lote
     * @param fechaVencimiento (yyyy-mm-dd)
     * @return
     * @throws SQLException
     */

    public static boolean actualizarExistenciasArticuloAlmacenLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion,String lote,String fechaVencimiento)
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS POR LOTE NO TRANSACCIONAL\n\n\n");
        boolean insertoBien=false;
        insertoBien=utilidadDao().actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
        if (insertoBien)
        {
        	utilidadDao().actualizarExistenciasArticuloAlmacenLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,fechaVencimiento);
        }
        return insertoBien;
    }
    
    /**
	 * MT 2353:Método que devuelve el mapa con lote, fechaVencimiento y existencias para saber si se creo solo 
	 * para registrar una existencia en lote='',fechaVencimiento ='' y existencias=0; y si se debe reemplazar
	 * al crear una transaccion sobre ese mismo articulo/almacen 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @return mapa 
	 */
	public static HashMap obtenerArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen){
		return utilidadDao().obtenerArticuloAlmacenXLote(con, codArticulo, codAlmacen);
	}
    
    /**
     * metodo que evalua si un articulo ya esta insertado en articulos_almacen
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public static boolean existeArticuloEnExistenciasXAlmacen( int codArticulo, int codAlmacen, int codInstitucion )
    {
        boolean valor=false;
        Connection con=UtilidadBD.abrirConexion();
        valor= utilidadDao().existeArticuloEnExistenciasXAlmacen(con, codArticulo, codAlmacen, codInstitucion);
        UtilidadBD.closeConnection(con);
        return valor; 
    }
    
	/**
	 * Metodo que actualiza el costo promedio de un articulo.
	 * @param codArticulo
	 * @param nuevoCosto
	 * @return
	 */
	public static boolean actualizarCostoPromedioArticulo(int codArticulo,double nuevoCosto)
	{
	    boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor= utilidadDao().actualizarCostoPromedioArticulo(con,codArticulo,nuevoCosto);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}	
	
	/**
	 * Metodo que actualiza el costo promedio de un articulo.
	 * recibe la connection
	 * @param con Connection
	 * @param codArticulo
	 * @param nuevoCosto
	 * @return
	 */
	public static boolean actualizarCostoPromedioArticulo(Connection con,int codArticulo,double nuevoCosto)
	{
	    boolean valor=false;	    
	    valor= utilidadDao().actualizarCostoPromedioArticulo(con,codArticulo,nuevoCosto);	 
	    return valor;
	}
	
	/**
	 * Metodo que carga las transacciones validads para un determinado centro costo de una institucion.
	 * @param institucion
	 * @param centroCosto
	 * @param isIn
	 * @param rest
	 * @return
	 */
	public static HashMap transaccionesValidasCentroCosto(int institucion,int centroCosto, Vector rest, boolean isIn)
	{
	    Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=utilidadDao().transaccionesValidasCentroCosto(con,institucion,centroCosto,rest,isIn);
	    UtilidadBD.closeConnection(con);
        //transaccionesValidasCentroCostoAgrupados(institucion, centroCosto, rest, isIn);
	    return (HashMap) mapa.clone();
	}
    /**
     * metodo para generar el listado de las 
     * transacciones validas por centro de costo, 
     * filtrando las que no se encuentren definidas
     * en parametros generales, y agrupando por la 
     * descripción de la tipo transacciónes.
     * @param con Connection
     * @param institucion int
     * @param centroCosto int
     * @return HashMap
     * @author jarloc
     */
	public static HashMap transaccionesValidasCentroCostoAgrupados(int institucion,int centroCosto, Vector rest, boolean isIn)
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap mapa=utilidadDao().transaccionesValidasCentroCostoAgrupados(con, institucion, centroCosto, rest, isIn);
		HashMap mapaTemporal=new HashMap();
		mapa.put("esDuplicado_"+0, "false");//inicializar  el primer registro
		for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
		{
			if(mapaTemporal.containsKey(mapa.get("tipo_trans_inventario_"+k)+""))
			{
				mapa.put("esDuplicado_"+k, "true");
			}
			else
			{
				mapaTemporal.put(mapa.get("tipo_trans_inventario_"+k)+"", " ");
				mapa.put("esDuplicado_"+k, "false");
			}
			/*
			 * se cambia el metodo por el anterior, con el objetivo de optimizar el metodo.
			//if(mapa.containsKey("esDuplicado_"+k)/*&&mapa.get("esDuplicado_"+k)+"").equals("false"))
			{
				for(int j=k+1;j<Integer.parseInt(mapa.get("numRegistros")+"");j++)
				{
					if((mapa.get("tipo_trans_inventario_"+k)+"").equals(mapa.get("tipo_trans_inventario_"+j)+""))
						mapa.put("esDuplicado_"+j, "true");
					else
						mapa.put("esDuplicado_"+j, "false");
				}
			}
			*/
		}
		UtilidadBD.closeConnection(con);
		return (HashMap) mapa.clone();
	}
	/**
	 * Metodo que retorn todos los almacenes de una institucion que estan asociado a un usuario solo almacenes activos.
	 * @param institucion
	 * @param login
	 * @param filtroCentroAtencion
	 * @return
	 */
	public static HashMap listadoAlmacenesUsuarios(int institucion,String login,int filtroCentroAtencion)
	{
	    Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=utilidadDao().listadoAlmacenesUsuarios(con,institucion,login,filtroCentroAtencion);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
	}
	
	/**
     * 
     * @param con
     * @param institucion
     * @param centroCostoSolicitante
     * @param tipoTransaccion
     * @param paresClaseGrupo
     * @param planEspecial
     * @return
     */
    public static HashMap<Object, Object> listadoAlmacenes(	int institucion, 
    														int centroCostoSolicitante,  
    														String tipoTransaccion, 
    														String paresClaseGrupo, 
    														boolean planEspecial)
    {
    	Connection con=UtilidadBD.abrirConexion();
        HashMap<Object,Object> mapa=utilidadDao().listadoAlmacenes(con, institucion, centroCostoSolicitante, tipoTransaccion, paresClaseGrupo, planEspecial);
        UtilidadBD.closeConnection(con);
        return (HashMap) mapa.clone();
    }
	
    /**
     * Metodo para cargar los almacenes validos, validando que el usuario pertenezca al almacen y ademas
     *  filtrando por las transacciones validas por centro de costo. Tambien se carga el detalle de la info de
     * las clases inventario x centro costo
     * @param con
     * @param institucion
     * @param login
     * @param tipoTransaccion
     * @param pares clase-grupo (si va vacio no se valida)
     * @return
     */
    public static HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(int institucion, String login, String tipoTransaccion, String paresClaseGrupo, int filtroCentroAtencion, boolean planEspecial)
    {
        Connection con=UtilidadBD.abrirConexion();
        HashMap mapa=utilidadDao().listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(con, institucion, login, tipoTransaccion, paresClaseGrupo, filtroCentroAtencion, planEspecial);
        UtilidadBD.closeConnection(con);
        return (HashMap) mapa.clone();
    }
    
    /**
     * Metodo que carga la informacion de las clases inventario restringiendo por el centro de costo
     * de las transacciones validas
     * @param con
     * @param institucion
     * @param codigoAlmacen
     * @param paresClaseGrupo
     * @return
     */
    public static HashMap cargarInfoClasesInventario(int institucion, int codigoAlmacen,String paresClaseGrupo)
    {
        Connection con=UtilidadBD.abrirConexion();
        HashMap mapa=utilidadDao().cargarInfoClasesInventario(con, institucion, codigoAlmacen, paresClaseGrupo);
        UtilidadBD.closeConnection(con);
        return (HashMap) mapa.clone();
    }
    
	/**
	 * Metodo que retorna las existencias totales de una rticulo
	 * @param codArticulo
	 * @return
	 */
	public static int existenciasTotalesArticulo(int codArticulo,int institucion)
	{
	    int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenciasTotalesArticulo(con,codArticulo,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorna las existencias de una articulo en un determinado almacen de una institucion.
	 * @param codArticulo
	 * @param almacen
	 * @param institucion
	 * @return
	 */
	public static int existenciasArticuloAlmacen(int codArticulo,int almacen,int institucion)
	{
	    int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenciasArticuloAlmacen(con,codArticulo,almacen,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	

	/**
	 * Metodo que retorna las existencias de una articulo en un determinado almacen de una institucion, el lote y la fecha de vencimiento.
	 * @param codArticulo
	 * @param almacen
	 * @param lote
	 * @param fechaVencimiento (yyyy-mm-dd)
	 * @return
	 */
	public static int existenciasArticuloAlmacenLote(int codArticulo,int almacen,String lote,String fechaVencimiento)
	{
	    int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenciasArticuloAlmacenLote(con,codArticulo,almacen,lote,fechaVencimiento);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorn true or false dependiendo si un valor es valido para en nuevo costo promedio
	 * de un articulo, dependiendo del parametro porcentajeAlerta
	 * @param codArticulo
	 * @param nuevoValor
	 * @param porcentajeAlerta
	 * @return
	 */
	public static boolean esValorValidoCostoArticulo(int codArticulo,double nuevoValor,double porcentajeAlerta)
	{
	    boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().esValorValidoCostoArticulo(con,codArticulo,nuevoValor,porcentajeAlerta);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorna el costo actual de un articulo.
	 * @param codArticulo
	 * @return
	 */
	public static double costoActualArticulo(int codArticulo)
	{
	    double valor=ConstantesBD.codigoNuncaValidoDouble;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().costoActualArticulo(con,codArticulo);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorna el costo actual de un articulo.
	 * @param codArticulo
	 * @return
	 */
	public static double costoActualArticulo(Connection con,int codArticulo)
	{
	    return utilidadDao().costoActualArticulo(con,codArticulo);
	}
	/**
	 * Metodo que calcula el costo promedio de un articulo determinado.
	 * @param codArticulo
	 * @param numeroUnidades
	 * @param valorTotalTransacion. valor total de la transacion por articulos, es decir en numero de unidades en la transacion * el costo actura de ese articulo.
	 * @param tipoTransaccion. Entrada/Salida
	 * @param institucion
	 * @param codigoAlamcen
	 * @return
	 */
	public static double calcularCostoPromedioArticulo(int codArticulo,int numeroUnidades,double valorTotalTransacion,int tipoTransaccion,int institucion, int codigoAlmacen)
	{
	    double valor=ConstantesBD.codigoNuncaValidoDouble;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().calcularCostoPromedioArticulo(con,codArticulo,numeroUnidades,valorTotalTransacion,tipoTransaccion,institucion, codigoAlmacen);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	/**
	 * Metodo que calcula el costo promedio de un articulo determinado.
	 * @param codArticulo
	 * @param numeroUnidades
	 * @param valorTotalTransacion. valor total de la transacion por articulos, es decir en numero de unidades en la transacion * el costo actura de ese articulo.
	 * @param tipoTransaccion. Entrada/Salida
	 * @param institucion
	 * @param codigoAlmacen
	 * @return
	 */
	public static double calcularCostoPromedioArticulo(Connection con,int codArticulo,int numeroUnidades,double valorTotalTransacion,int tipoTransaccion,int institucion, int codigoAlmacen)
	{
	    double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().calcularCostoPromedioArticulo(con,codArticulo,numeroUnidades,valorTotalTransacion,tipoTransaccion,institucion,codigoAlmacen);
	    return valor; 
	}

	
	/**
	 * Metodo que retorna true si para una determinado fecha ya existe cierre de inventarios.
	 * @param fecha. debe estar en formato aplicacion dd/mm/yyyy
	 * @return
	 */
	public static boolean existeCierreInventarioParaFecha(String fecha,int institucion)
	{
	    boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existeCierreInventarioParaFecha(con,fecha,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorna true si para una determinado fecha ya existe cierre de inventarios.
	 * @param fecha. debe estar en formato aplicacion dd/mm/yyyy
	 * @return
	 */
	public static boolean existeCierreInventarioParaFecha(Connection con,String fecha,int institucion)
	{
	    boolean valor=utilidadDao().existeCierreInventarioParaFecha(con,fecha,institucion);
	    return valor;
	}
	
	/**
	 * Metodo que retorna La fecha del cierre inicial, en caso de no tener cierre inicial retorna -1.
	 * fecha en formato mm/yyyy, solo retorna el mes y el anio.
	 * @return
	 */
	public static String fechaCierreInicial(int institucion)
	{
	    String valor=""+ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().fechaCierreInicial(con,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorna true si las existencias de un articulo es mayor igual que el stock minimo.
	 * @param codArticulo
	 * @return
	 */
	public static boolean existenciasArticuloMayorIgualStockMinimo(int codArticulo,int institucion)
	{
	    boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenciasArticuloMayorIgualStockMinimo(con,codArticulo,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor;
	}
	
	/**
	 * Metodo que retorn true si las existencias de un articulo es menor igual que el stock maximo.
	 * @return
	 */
	public static boolean existenciasArticuloMenorIgualStockMaximo(int codArticulo,int institucion)
	{
	    boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenciasArticuloMenorIgualStockMaximo(con,codArticulo,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	/**
	 * Metodo que retorn true si las existencias de un articulo es mayor igual que el punto pedido
	 * @param codArticulo
	 * @param institucion
	 * @return
	 */
	public static boolean existenciasArticuloMayorIgualPuntoPedido(int codArticulo,int institucion)
	{
	    boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenciasArticuloMayorIgualPuntoPedido(con,codArticulo,institucion);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	//@todo pendiente la generacion de un metodo TransaccionesInventario cargarTransaccion(String codigoTransaccion)

    /**
     * Método que obtiene las parejas de clase-grupo para la 
     * interseccion centro costo y almacen
     * @param con
     * @param centroCosto
     * @param almacen
     * @return
     * El formato es String "'clase-grupo','clase-grupo',...."
     */
    public static String obtenerInterseccionClaseGrupo(int centroCosto,int almacen)
    {
    	String resultado = "";
    	Connection con=UtilidadBD.abrirConexion();
    	resultado = utilidadDao().obtenerInterseccionClaseGrupo(con,centroCosto,almacen);
    	UtilidadBD.closeConnection(con);
	    return resultado;
    }
    
    /**
     * Método que verifica 
     * @param con
     * @param usuario
     * @param centroCosto
     * @return
     */
    public static boolean esAlmacenUsuarioAutorizado(String usuario,int centroCosto, int codigoInstitucion)
    {
    	boolean resultado = false;
    	Connection con=UtilidadBD.abrirConexion();
    	resultado = utilidadDao().esAlmacenUsuarioAutorizado(con,usuario,centroCosto, codigoInstitucion);
    	UtilidadBD.closeConnection(con);
	    return resultado;
    }
    
    /**
     * Metodo que retorna el anio de ultimo cierre que se realizó.
     * Retorna -1 en caso de no existir un cierre
     * @param institucion
     * @return
     */
    public static int obtenerAnioUltimoCierre(int institucion)
    {
        int anio=ConstantesBD.codigoNuncaValido;
        Connection con=UtilidadBD.abrirConexion();
        anio = utilidadDao().obtenerAnioUltimoCierre(con,institucion);
    	UtilidadBD.closeConnection(con);
	    return anio;
    }
    
    /**
     * Metodo que retorna el mes de ultimo cierre de un determinado anio.
     * Retorna -1 en caso de no existir un cierre
     * @param institucion
     * @param anio
     * @return
     */
    public static int obtenerMesUltimoCierreAnio(int institucion,int anio)
    {
        int mes=ConstantesBD.codigoNuncaValido;
        Connection con=UtilidadBD.abrirConexion();
        mes = utilidadDao().obtenerMesUltimoCierreAnio(con,institucion,anio);
    	UtilidadBD.closeConnection(con);
	    return mes;
    }

    
    /**
     * Metodo que validad si existen cierres anterioaries a una fecha determinada.
     * Es util cuando se tinene que validar que no existan cierre anteriores a una fecha para
     * porder realizar una determinada accion.
     * fecha formato dd/mm/yyyy
     * @param i
     * @param string
     * @return
     */
    public static boolean existenCierresAnterioresAFecha(int institucion, String fecha)
    {
        boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existenCierresAnterioresAFecha(con,institucion,fecha);
	    UtilidadBD.closeConnection(con);
	    return valor; 
    }

    
    /**
     * Metodo que retona un boolean indicando si ya existe cierre anual para un determinado anio.
     * @param i
     * @param string
     * @return
     */
    public static boolean existeCierreFinalAnio(int institucion, String anio)
    {
        boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existeCierreFinalAnio(con,institucion,anio);
	    UtilidadBD.closeConnection(con);
	    return valor; 
    }

    
    /**
     * Metodo que consulta si existe cierre inicial en determinado año,
     * si existe retorna la fecha del cierre (mm/yyyy) si no exista retorna
     * ConstantesBD.codigoNuncaValido.
     * @param institucion
     * @param anio
     * @return
     */
    public static String existeCierreInicialFecha(int institucion, String anio)
    {
        String resultado = "";
    	Connection con=UtilidadBD.abrirConexion();
    	resultado = utilidadDao().existeCierreInicialFecha(con,institucion,anio);
    	UtilidadBD.closeConnection(con);
	    return resultado;
    }
    
    public static String existeCierreInicialFecha(Connection con,int institucion, String anio)
    {
        String resultado = "";
    	resultado = utilidadDao().existeCierreInicialFecha(con,institucion,anio);
	    return resultado;
    }

    
    /**
     * Metodo que retorna el numero de movimientos de inventarios que se realizaron en
     * un  rango de fechas.
     * @param institucion 
     * @param fechaInicial [dd/mm/yyyy]
     * @param fechaFinal [dd/mm/yyyy]
     * @return
     */
    public static int numeroMovimientosInventariosEntreFecha(int institucion, String fechaInicial, String fechaFinal)
    {
        int cant=ConstantesBD.codigoNuncaValido;
        Connection con=UtilidadBD.abrirConexion();
        cant = utilidadDao().numeroMovimientosInventariosEntreFecha(con,institucion,fechaInicial,fechaFinal);
    	UtilidadBD.closeConnection(con);
	    return cant;
    }

    
    /**
     * Metodo que retorna true or false si existen cierres finales posteriores a una fecha
     * @param institucion
     * @param anio
     * @return
     */
    public static boolean existeCierreFinalesPosterioresAnio(int institucion, String anio)
    {
        boolean valor=false;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().existeCierreFinalesPosterioresAnio(con,institucion,anio);
	    UtilidadBD.closeConnection(con);
	    return valor; 
    }

    /**
     * Mes que retorn el costo unitario final mes segun las validaciones de cierre de inventarios.
     * @param institucion
     * @param articulo
     * @param anio
     * @param mes
     * @return
     */
	public static String obtenerCostoUnitarioFinalMes(int institucion, int articulo, String anio, String mes)
	{
		String resultado = "";
    	Connection con=UtilidadBD.abrirConexion();
    	resultado = utilidadDao().obtenerCostoUnitarioFinalMes(con,institucion,articulo,anio,mes);
    	UtilidadBD.closeConnection(con);
	    return resultado;
	}
	
	public static String obtenerCostoUnitarioFinalMes(Connection con,int institucion, int articulo, String anio, String mes)
	{
		String resultado = "";
    	resultado = utilidadDao().obtenerCostoUnitarioFinalMes(con,institucion,articulo,anio,mes);
	    return resultado;
	}

	/**
	 * Metodo que retornar el valor de entradas para un mes cierre.
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public static double obtenerValorEntradaCierreMesAnio(int institucion, int almacen, int articulo, String anio, String mes)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerValorEntradaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	/**
	 * Metodo que retornar el valor de entradas para un mes cierre.
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public static double obtenerValorEntradaCierreMesAnioLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio, String mes,String lote,String fechaVencimiento)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorEntradaCierreMesAnioLoteFecha(con,institucion,almacen,articulo,anio,mes,lote,fechaVencimiento);
	    return valor; 
	}
	
	public static double obtenerValorEntradaCierreMesAnio(Connection con,int institucion, int almacen, int articulo, String anio, String mes)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorEntradaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    return valor; 
	}

	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public static double obtenerValorSalidaCierreMesAnio(int institucion, int almacen, int articulo, String anio, String mes)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerValorSalidaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	public static double obtenerValorSalidaCierreMesAnio(Connection con,int institucion, int almacen, int articulo, String anio, String mes)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorSalidaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    return valor; 
	}

	public static double obtenerValorSalidaCierreMesAnioLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio, String mes,String lote,String fechaVencimiento)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorSalidaCierreMesAnioLoteFecha(con,institucion,almacen,articulo,anio,mes,lote,fechaVencimiento);
	    return valor; 
	}

	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public static int obtenerCantidadEntradaCierreMesAnio(int institucion, int almacen, int articulo, String anio, String mes)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerCantidadEntradaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	public static int obtenerCantidadEntradaCierreMesAnio(Connection con,int institucion, int almacen, int articulo, String anio, String mes)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadEntradaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    return valor; 
	}
	
	public static int obtenerCantidadEntradaCierreMesAnioLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio, String mes,String lote,String fechaVencimiento)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadEntradaCierreMesAnioLoteFecha(con,institucion,almacen,articulo,anio,mes,lote,fechaVencimiento);
	    return valor; 
	}

	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public static int obtenerCantidadSalidaCierreMesAnio(int institucion, int almacen, int articulo, String anio, String mes)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerCantidadSalidaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	public static int obtenerCantidadSalidaCierreMesAnio(Connection con,int institucion, int almacen, int articulo, String anio, String mes)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadSalidaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	    return valor; 
	}
	
	public static int obtenerCantidadSalidaCierreMesAnioLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio, String mes,String lote,String fechaVencimiento)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadSalidaCierreMesAnioLoteFecha(con,institucion,almacen,articulo,anio,mes,lote,fechaVencimiento);
	    return valor; 
	}

	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public static double obtenerValorEntradaCierreMesAnioTotal(int institucion, int almacen, int articulo, String anio)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerValorEntradaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	public static double obtenerValorEntradaCierreMesAnioTotal(Connection con,int institucion, int almacen, int articulo, String anio)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorEntradaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    return valor; 
	}
	
	
	public static double obtenerValorEntradaCierreMesAnioTotalLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio,String lote,String fechaVencimiento)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorEntradaCierreMesAnioTotalLoteFecha(con,institucion,almacen,articulo,anio,lote,fechaVencimiento);
	    return valor; 
	}

	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public static double obtenerValorSalidaCierreMesAnioTotal(int institucion, int almacen, int articulo, String anio)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerValorSalidaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	public static double obtenerValorSalidaCierreMesAnioTotal(Connection con,int institucion, int almacen, int articulo, String anio)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorSalidaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    return valor; 
	}
	
	public static double obtenerValorSalidaCierreMesAnioTotalLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio,String lote,String fechaVencimiento)
	{
		double valor=ConstantesBD.codigoNuncaValidoDouble;
	    valor=utilidadDao().obtenerValorSalidaCierreMesAnioTotalLoteFecha(con,institucion,almacen,articulo,anio,lote,fechaVencimiento);
	    return valor; 
	}

	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public static int obtenerCantidadEntradaCierreMesAnioTotal(int institucion, int almacen, int articulo, String anio)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerCantidadEntradaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	public static int obtenerCantidadEntradaCierreMesAnioTotal(Connection con,int institucion, int almacen, int articulo, String anio)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadEntradaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    return valor; 
	}
	public static int obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio,String lote,String fechaVencimiento)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(con,institucion,almacen,articulo,anio,lote,fechaVencimiento);
	    return valor; 
	}
	/**
	 * 
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public static int obtenerCantidadSalidaCierreMesAnioTotal(int institucion, int almacen, int articulo, String anio)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerCantidadSalidaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	public static int obtenerCantidadSalidaCierreMesAnioTotal(Connection con,int institucion, int almacen, int articulo, String anio)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadSalidaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	    return valor; 
	}
	public static int obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(Connection con,int institucion, int almacen, int articulo, String anio,String lote,String fechaVencimiento)
	{
		int valor=ConstantesBD.codigoNuncaValido;
	    valor=utilidadDao().obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(con,institucion,almacen,articulo,anio,lote,fechaVencimiento);
	    return valor; 
	}
	/**
	 * 
	 * @param institucion
	 * @param mesCierre
	 * @param anioCierre
	 * @return
	 */
	public static String obtenerCodigoCierreInventario(int institucion, String mesCierre, String anioCierre)
	{
		String valor="";
	    Connection con=UtilidadBD.abrirConexion();
	    valor=utilidadDao().obtenerCodigoCierreInventario(con,institucion,mesCierre,anioCierre);
	    UtilidadBD.closeConnection(con);
	    return valor; 
	}
	
	/**
	 * metodo para consultar el costo del saldo,
	 * ele ultimo registro desde el dia anterior,
	 * toma el ultimo regisrro desde la fecha/hora	 
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String	
	 * @return  String
	 */
	public static String obtenerCostoUnitarioKardex(int institucion, int articulo, String fecha)
	{
		String resultado = "";
    	Connection con=UtilidadBD.abrirConexion();
    	resultado = utilidadDao().obtenerCostoUnitarioKardex(con,institucion,articulo,fecha);
    	UtilidadBD.closeConnection(con);
	    return resultado;
	}	
	/**
	 * metodo para consultar el costo del saldo,
	 * toma el ultimo regisrro desde la fecha/hora
	 * del movimiento que se esta relacionando.	
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String
	 * @param numeroTransaccion String
	 * @return  String
	 */
	public static String obtenerCostoUnitarioKardex(int institucion, int articulo, String fecha,String numeroTransaccion)
	{
	    String resultado = "";
    	Connection con=UtilidadBD.abrirConexion();
    	resultado = utilidadDao().obtenerCostoUnitarioKardex(con,institucion,articulo,fecha,numeroTransaccion);
    	UtilidadBD.closeConnection(con);
	    return resultado;
	}
	/**
	 * metodo para cargar el listado de clase-grupo, de 
	 * un usuario segun su centro de costo, y un tipo
	 * de transacción definido.
	 * @param con Connection
	 * @param institucion int
	 * @param centroCosto int
	 * @param transaccion int
	 * @return HashMap
	 */
	public static HashMap listadoClaseGrupoSegunCentroCostoUsuario(int institucion,int centroCosto,int transaccion)
	{
	    Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().listadoClaseGrupoSegunCentroCostoUsuario(con, institucion, centroCosto, transaccion);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
	}
    
     /**
     * metodo que devuelve el numero de existencias (- o +) de un articulo, en caso de que genere error
     * devuelve un null
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public static String getExistenciasXArticulo( int  codArticulo, int codAlmacen,int codInstitucion)
    {
        Connection con=UtilidadBD.abrirConexion();
        String resultado= utilidadDao().getExistenciasXArticulo(con, codArticulo, codAlmacen, codInstitucion);
        UtilidadBD.closeConnection(con);
        return resultado;
    }
    
    /**
     * metodo que devuelve el numero de existencias (- o +) de un articulo, en caso de que genere error
     * devuelve un null
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public static String getExistenciasXArticulo(Connection con, int  codArticulo, int codAlmacen,int codInstitucion)
    {
        return utilidadDao().getExistenciasXArticulo(con, codArticulo, codAlmacen, codInstitucion);
    }
    
    /**
     * Metodo para cargar los almacenes validos, validando 
     * que el usuario pertenezca al almacen y ademas filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
     * @param con Connection
     * @param institucion int     
     * @param tipoTransaccion String
     * @param pares clase-grupo (si va vacio no se valida) String
     * @param centroCosto int
     * @return HashMap
     */
    public static HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto)
    {
        Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(con, institucion,tipoTransaccion, paresClaseGrupo, centroCosto);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
    }
    
	/**
     * Metodo para cargar los almacenes validos, filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
	 * @param institucion int
	 * @param tipoTransaccion String
	 * @param centroCosto int
	 * @param login @todo
	 * @param con Connection
	 * @param login String
	 * @param pares clase-grupo (si va vacio no se valida) String
     * @return HashMap
     */
    public static HashMap listadoAlmacenesUsuariosXCentroAtencion(int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto, int centroAtencion, String login)
    {
        Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().listadoAlmacenesUsuariosXCentroAtencion(con, institucion,tipoTransaccion, paresClaseGrupo, centroCosto, centroAtencion, login);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
    }

    /**
     * Metodo que retorna las parejas clase-grupo que estan relacionado a una transaccion con determinado centro costo.	
     * @param con
     * @param institucion
     * @param centroCosto
     * @param codigoTransaccion
     * @return
     */
	public static String obtenerParejasClaseGrupoInventario(Connection con, int institucion, int centroCosto, String codigoTransaccion)
	{
		return utilidadDao().obtenerParejasClaseGrupoInventario(con,institucion,centroCosto,codigoTransaccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
    public static HashMap obtenerLotesDespachoNoAdministradosTotalmente(String numeroSolicitud, String codigoArticulo)
    {
    	Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().obtenerLotesDespachoNoAdministradosTotalmente(con, numeroSolicitud, codigoArticulo);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
    public static HashMap obtenerLotesDespachoNoAdministradosTotalmente(Connection con, String numeroSolicitud, String codigoArticulo)
    {
    	return utilidadDao().obtenerLotesDespachoNoAdministradosTotalmente(con, numeroSolicitud, codigoArticulo);
    } 
    
    /**
	 * Método que consulta el nombre de una via de administracion a partir
	 * de su consecutivo de la tabla vias_admin_institucion
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerNombreViaAdminInstitucion(Connection con,int consecutivo)
	{
		return utilidadDao().obtenerNombreViaAdminInstitucion(con, consecutivo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @return
	 */
	public static int obtenerTipoCierre(Connection con, String codigoCierre)
	{
		return utilidadDao().obtenerTipoCierre(con,codigoCierre);
	}

	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static String obtenerPrecioUltimaCompraArticulo(Connection con, int articulo) 
	{
		return utilidadDao().obtenerPrecioUltimaCompraArticulo(con,articulo);
	}

	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static String obtenerPrecioBaseVentaArticulo(Connection con, int articulo) 
	{
		return utilidadDao().obtenerPrecioBaseVentaArticulo(con,articulo);
	}

	/**
	 * 
	 * @param institucion 
	 * @return
	 */
	public static HashMap<String, Object> obtenerAlmacenesConsignacion(int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().obtenerAlmacenesConsignacion(con,institucion);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> obtenerConveniosProveedor(int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().obtenerConveniosProveedor(con,institucion,ConstantesBD.codigoNuncaValido);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> obtenerConveniosProveedor(int institucion,int articulo) 
	{
		Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().obtenerConveniosProveedor(con,institucion,articulo);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
	}


	public static HashMap<String, Object> obtenerProveedoresCatalogo(int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
	    HashMap mapa=new HashMap();
	    mapa=utilidadDao().obtenerProveedoresCatalogo(con,institucion);
	    UtilidadBD.closeConnection(con);
	    return (HashMap) mapa.clone();
	}

	/**
	 * 
	 * @param con
	 * @param codTransInterfaz
	 * @param institucion 
	 * @return
	 */
	public static int obtenerTipoTransaccionInterfaz(Connection con, int codTransInterfaz, int institucion) 
	{
		return utilidadDao().obtenerTipoTransaccionInterfaz(con,codTransInterfaz,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param tipoTransaccion
	 * @param fechaActual
	 * @param loginUsuario
	 * @param automatica 
	 * @param string
	 * @param string2
	 * @param codigoEstadoTransaccionInventarioPendiente
	 * @param codigoAlmacen
	 */
	public static int generarEncabezadoTransaccion(Connection con, int consecutivo, int tipoTransaccion, String fechaActual, String loginUsuario, int entidad, String obsevaciones, int estado, int almacen, boolean automatica) 
	{
		if(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroTransaccionesDao().insertarInformacionGeneralTransaccion(con, consecutivo, tipoTransaccion, fechaActual, loginUsuario, entidad, obsevaciones, estado, almacen,automatica))
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroTransaccionesDao().obtnerCodigoTransaccionInsertada(con);
		else
			return 0;
	}

	/**
	 * 
	 * @param con
	 * @param codTransaccion
	 * @param codigo
	 * @param cantidadDespacho
	 * @param valorUnitario
	 * @param proveedorCatalogo 
	 * @param proveedorCompra 
	 * @param string
	 * @param string2
	 */
	public static boolean insertarDetalleTransaccion(Connection con, int codTransaccion, int articulo, int cantidad, String valorUnitario, String lote, String fechaVencimiento, String proveedorCompra, String proveedorCatalogo)  
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroTransaccionesDao().insertarDetalleTransaccion(con, codTransaccion, articulo, cantidad, valorUnitario, lote, fechaVencimiento,proveedorCompra,proveedorCatalogo);
	}

	/**
	 * 
	 * @param con
	 * @param codTransaccion
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @return
	 */
	public static boolean generarRegistroCierreTransaccion(Connection con, String codTransaccion, String usuario, String fecha, String hora) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroTransaccionesDao().generarRegistroCierreTransaccion(con, codTransaccion, usuario, fecha, hora);
	}

	/**
	 * 
	 * @param con
	 * @param tipoTransaccion
	 * @param institucion
	 * @return
	 */
	public static String obtenerNombreTipoTransaccion(Connection con, int tipoTransaccion, int institucion) 
	{
		return utilidadDao().obtenerNombreTipoTransaccion(con,tipoTransaccion,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo) 
	{
		return utilidadDao().obtenerValorArticuloProveedorConveProveedor(con,proveedor,articulo);
	}

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo) 
	{
		return utilidadDao().obtenerValorArticuloProveedorCatalogoProveedor(con,proveedor,articulo);
	}

	/**
	 * 
	 * @param con 
	 * @param codigo
	 * @param valorUnitario
	 */
	public static boolean actualizarPrecioUltimaCompra(Connection con, int codigo, double valorUnitario) 
	{
		return utilidadDao().actualizarPrecioUltimaCompra(con,codigo,valorUnitario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazArticulo(Connection con, int codigoArticulo) 
	{
		return utilidadDao().obtenerCodigoInterfazArticulo(con,codigoArticulo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazArticulo(int codigoArticulo) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String r=utilidadDao().obtenerCodigoInterfazArticulo(con,codigoArticulo);
		UtilidadBD.closeConnection(con);
		return r;
	}
	
	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorIvaArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo) 
	{
		return utilidadDao().obtenerValorIvaArticuloProveedorConveProveedor(con,proveedor,articulo);
	}

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param codigo
	 * @return
	 */
	public static double obtenerValorIvaArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo) 
	{
		return utilidadDao().obtenerValorIvaArticuloProveedorCatalogoProveedor(con,proveedor,articulo);
	}
	
	/**
	 * 
	 * @param con
	 * @param pedido
	 * @return
	 */
	public static int obtenerNumeroPeticionPedidoQX(Connection con,int pedido)
	{
		return utilidadDao().obtenerNumeroPeticionPedidoQX(con,pedido);
	}
	
	/**
	 * Método que verifica si a una solicitud de cargos directos artículos le afectan los inventarios
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean afectaInventariosSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadDao().afectaInventariosSolicitud(con, numeroSolicitud);
	}
	

	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static HashMap obtenerClasesInventario(Connection con, int institucion) 
	{
		return utilidadDao().obtenerClasesInventario(con,institucion);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap obtenerGrupoInventario(Connection con, int institucion) 
	{
		return utilidadDao().obtenerGrupoInventario(con,institucion);
	}
	
	/**
	 * Metodo que valida el tiempo de tratamiento para la justificacion NOPOS
	 * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia
	 * @param paciente
	 * @return
	 */
	public static boolean validarTiempoTratamiento(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente)
	{
		return utilidadDao().obtenerValidarTiempoTratamiento(con, codigoArticulo, unidosis, dosis, tipoFrecuencia,frecuencia, paciente);
	}
	
	/**
	 * Metodo que retorna el codigo de la justificacion NOPOS para asociar
	 * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia
	 * @param paciente
	 * @return
	 */
	public static int obtenerUltimaJustificacion(Connection con, int codigoArticulo, String unidosis,String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente)
	{
		return utilidadDao().obtenerJustificacion(con, codigoArticulo, unidosis, dosis, tipoFrecuencia, frecuencia, paciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static double obtenerValorCompraMasAlta(Connection con, int articulo) 
	{
		return utilidadDao().obtenerValorCompraMasAlta(con, articulo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param valorUnitario
	 * @return
	 */
	public static boolean actualizarPrecioCompraMasAlta(Connection con, int codigo, double valorUnitario) 
	{
		return utilidadDao().actualizarPrecioCompraMasAlta(con,codigo,valorUnitario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static int existeAlmacenPlanEspecial(Connection con, int codigoCentroAtencion) 
	{
		return utilidadDao().existeAlmacenPlanEspecial(con, codigoCentroAtencion);
	}

	/***********************************************************************
	 * Metodo encargado de consultar las clases de inventario
	 * pudiendo filtrar por los siguiente criterios:
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion -->  Requerido
	 * -- codigo	--> Opcional
	 * -- cuentaInventario	--> Opcional
	 * -- centroCosto	--> Opcional
	 * @return ArrayList<HashMap>
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo,nombre,cuentaInventario,
	 * cuentaCosto
	 **************************************************************************/
	public static ArrayList<HashMap<String, Object>> obtenerClaseInventario (Connection connection,HashMap criterios)
	{
		return utilidadDao().obtenerClaseInventario(connection, criterios);
	}
	
	/**
	 * Método que retorna el código axioma de un artículo
	 * según el código interfaz
	 * @param con
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static String obtenerCodigoDadoCodigoInterfaz(Connection con, String codigoInterfaz) 
	{
		return utilidadDao().obtenerCodigoDadoCodigoInterfaz(con, codigoInterfaz);
	}
	
	/**
	 * Método para obtener las clases de inventario que aplican para un centro de costo
	 * según el tipo de transacción
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerClasesInventarioTransValidasCentroCosto(Connection con,int codigoCentroCosto,int codigoTipoTransaccion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroCosto", codigoCentroCosto);
		campos.put("codigoTipoTransaccion", codigoTipoTransaccion);
		return utilidadDao().obtenerClasesInventarioTransValidasCentroCosto(con, campos);
	}

	/**
	 * Método que devuelve la Naturaleza de un 
	 * artículo determinado
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static boolean esPosArticulo(Connection con, int articulo, int institucion)
	{
		return utilidadDao().esPosArticulo(con, articulo, institucion);
	}

	/**
	 * Metodo que devuelve el numero del consecutivo de una transaccion segun el codigo
	 * @param con
	 * @param string
	 * @return
	 */
	public static String obtenerConsecutivoTransaccion(Connection con, String codigoTransaccion) 
	{
		return utilidadDao().obtenerConsecutivoTransaccion(con, codigoTransaccion);
	}
	
	/**
	 * Método para obtener las farmacias x centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerFarmaciasXCentroCosto(Connection con,int codigoCentroCosto,int codigoCentroAtencion,int codigoInstitucion)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("codigoCentroCosto", codigoCentroCosto);
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("codigoInstitucion", codigoInstitucion);
		return utilidadDao().obtenerFarmaciasXCentroCosto(con, campos);
	}

	/**
	 * Método que indica si un articulo es o no medicamento
	 * @param con
	 * @param codigoArticulo
	 * @param codigoInstitucionInt
	 */
	public static boolean esMedicamento(Connection con, int codigoArticulo, int codigoInstitucion) {
		return utilidadDao().esMedicamento(con, codigoArticulo, codigoInstitucion);
	}

	/**
	 * @author Víctor Gómez
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerClasesInventarioArray(Connection con, int institucion)
	{
		return utilidadDao().obtenerClasesInventarioArray(con, institucion);
	}
	
	
	  /**
     * metodo transaccional que actualiza las existencias articulos x almacen x lote tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * para mantenerlo en un solo execute
     * Actualiza tambien la existencias por almacen.
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @param estadoTransaccion
     * @param lote
     * @param fechaVencimiento (yyyy-mm-dd)
     * @return
     * @throws SQLException
     */
    public static boolean actualizarExistenciasArticuloAlmacenLoteTransaccionalAnulacion(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar,
    		int cantidadArticulo, int institucion, String estadoTransaccion,String lote,
    		String fechaVencimiento,String numeroSolciitud,String  loteDetalle,HashMap<String,Object> detalleSolicitud,Integer index)throws SQLException
	{
    	logger.info("\n\n\nACTUALIZANDO EXISTENCIAS POR LOTE TRANSACCIONAL\n\n\n");
        boolean insertoBien=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertoBien=utilidadDao().actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
            if (!insertoBien)
            {
                myFactory.abortTransaction(con);
            }
            else
            {
            	utilidadDao().actualizarExistenciasArticuloAlmacenLoteAnulacion(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,
            			fechaVencimiento,numeroSolciitud,loteDetalle,detalleSolicitud, index);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estadoTransaccion.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertoBien;
    }
	
	
	
	
	
}