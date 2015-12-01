
/*
 * Creado   8/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import util.UtilidadBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.RegistroTransaccionesDao;
import com.princetonsa.mundo.ConsecutivosDisponibles;

/**
 * 
 *
 * @version 1.0, 8/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RegistroTransacciones 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(RegistroTransacciones.class);
    /**
	 * DAO de este objeto, para trabajar con
	 * registro transacciones en la fuente de datos
	 */
    private static RegistroTransaccionesDao registroDao;    
    /**
     * código de la institución del usuario
     */
    private int institucion;
    /**
     * código del centro de costo de tipo subalmacen
     */
    private int codigoAlmacen;
    /**
     * código del tipo de transaccion
     */
    private int codigoTipoTransaccion;
    /**
     * nombre del tipo de transacción
     */
    private String nombreTransaccion;
    /**
     * login del usuario segun la institución
     */
    private String loginUsuario;
    /**
     * código de la transacción en BD,
     * para ser modificada
     */
    private int codigoTransModificar;
    /**
     * fecha de elaboración de la transacción
     */
    private String fechaElaboracion;
    /**
     * almacena datos que se necesitan conservar
     * para trabajar en la forma, y asi no declarar
     * atributos por cada uno
     */
    private HashMap mapaAtributos;
    /**
     * almacena los logs, por modificaciones
     */
    private String log;  
    /**
     * login del usuario
     */
    private String usuario;
    /**
     * almacena el código de la transacción,
     * el cual sirve de referencia a otras
     * tablas
     */
    private int codigoPKTransaccion;
    /**
     * código de la transacción valida por
     * centro de costo
     */
    private int codigoTransValidaXCC;
    /**
     * true si es cerrar transaccion
     */
    private String cerrarTransaccion;
    
    /**
     * 
     */
    private int codigoAlmacenTransaccion;
    
    /**
     * 
     */
    private Collection resultados;
    
    /**
     * 
     */
    private String codigoEntidad;
    
    /**
     * 
     */
    private String descripcionEntidad;
    
    /**
     * Campo que almacena el consecutivo disponible de la transaccion
     */
    private String consecutivoDisponible;
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */   
    public void reset ()
    {   
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.codigoAlmacen=ConstantesBD.codigoNuncaValido;
        this.codigoAlmacenTransaccion=ConstantesBD.codigoNuncaValido;
        this.mapaAtributos=new HashMap();
        this.codigoTipoTransaccion=ConstantesBD.codigoNuncaValido;
        this.loginUsuario="";
        this.codigoTransModificar=ConstantesBD.codigoNuncaValido;
        this.fechaElaboracion="";
        this.log="";
        this.nombreTransaccion="";
        this.usuario="";
        this.codigoPKTransaccion=ConstantesBD.codigoNuncaValido;
        this.codigoTransValidaXCC=ConstantesBD.codigoNuncaValido;
        this.cerrarTransaccion="false";
        this.consecutivoDisponible = "";
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( registroDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			registroDao= myFactory.getRegistroTransaccionesDao();			
			if( registroDao!= null )
				return true;
		}
		return false;
	}
	/**
	 * constructor
	 */
	public RegistroTransacciones()
	{
	  this.reset();
	  this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * metodo para generar el listado de almacenes
	 * filtrando los almacenes a los cuales pertenece
	 * el usuario.
	 * @param login String, login del usuario
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap cargarAlmacenesXUsuario(String login)
	{
	  HashMap map=new HashMap ();
	  map=UtilidadInventarios.listadoAlmacenesUsuarios(this.institucion,login,ConstantesBD.codigoNuncaValido);
	  return map;
	}
	/**
	 * metodo para gerar el listado de las
	 * trnasacciones validas por centro de costo.
	 * @param con
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap cargarListaTransaccionesValidasXCC(Connection con)
	{
	    Vector rest=new Vector();	            
        ValoresPorDefecto.cargarValoresIniciales(con);
        rest.add(ValoresPorDefecto.getCodigoTransSoliPacientes(institucion,true));
        rest.add(ValoresPorDefecto.getCodigoTransDevolPacientes(institucion,true));
        rest.add(ValoresPorDefecto.getCodigoTransaccionPedidos(institucion,true));
        rest.add(ValoresPorDefecto.getCodigoTransDevolucionPedidos(institucion,true));
        rest.add(ValoresPorDefecto.getCodigoTransTrasladoAlmacenes(institucion,true));
        rest.add(ValoresPorDefecto.getCodigoTransCompra(institucion,true));
        rest.add(ValoresPorDefecto.getCodigoTransDevolCompra(institucion,true));
	    HashMap map=new HashMap ();
	    map=UtilidadInventarios.transaccionesValidasCentroCostoAgrupados(this.institucion,this.codigoAlmacen,rest,false);
	    
	    /*
	     * Solución Tarea 50813
	     * campo de filtro cod_tipo_trans_inv 
	     */
	    int numRegistros = Integer.parseInt(map.get("numRegistros")+"");
	    String codTransaccion = "", tipoTransaccionNumero = "", tipoTransaccionString = "";
	    HashMap mapaTipoTransaccion = new HashMap();
	    
	    for(int i=0;i<numRegistros;i++)
	    {
	    	if((map.get("esDuplicado_"+i)+"").equals("false"))
	    	{
	    		codTransaccion = map.get("cod_tipo_trans_inv_"+i)+"";
	    		logger.info("===> codTransaccion = "+codTransaccion);
	    		
		    	mapaTipoTransaccion = this.consultaTipoTransaccion(con, codTransaccion);
		    	logger.info("===> mapaTipoTransaccion = "+mapaTipoTransaccion);
		    	
		    	tipoTransaccionNumero = mapaTipoTransaccion.get("tipos_conceptos_inv_0")+"";
		    	
		    	if(Integer.parseInt(tipoTransaccionNumero) == 1)
		    	{
		    		logger.info("===> Tipo Transacción Entrada");
		    		map.put(("tipoTransaccion_"+i)+"", "Entrada");
		    		map.get(("tipoTransaccion_"+i)+"");
		    	}
		    	else if(Integer.parseInt(tipoTransaccionNumero) == 2)
		    	{
		    		logger.info("===> Tipo Transacción Entrada");
		    		map.put(("tipoTransaccion_"+i)+"", "Salida");
		    		map.get(("tipoTransaccion_"+i)+"");
		    	}
	    	}
	    }
	    
	    return map;
	}
    
	/**
	 * metodo para generar el insert de la 
	 * información general de la transacción
	 * @param con Connection
	 * @param consecutivo int
	 * @param entidad int
	 * @param observaciones String
	 * @return boolean 
	 * @author jarloc
 	 */
	public boolean generarInformacionGeneralTrans(Connection con,int consecutivo,int entidad,String observaciones,int estado)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		try 
		{
			if (registroDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}
			else
			{
			     if(this.codigoTransModificar!=ConstantesBD.codigoNuncaValido)//si se cargo el codigo, existe una modificacion
			     {	
			    	 ArrayList filtro=new ArrayList();
			    	 filtro.add(this.codigoTransModificar+"");
			    	 UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoRegistroTransacciones,filtro);
			         HashMap mapa=new HashMap();
			         mapa=registroDao.existeRegistroTransaccionEnBD(con,this.codigoTransModificar);
			         if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")>0)
			         {
			             boolean existeMod=validarModificacionesInfoGeneral(mapa,observaciones,entidad);
			             if(existeMod)
			             {
			                 enTransaccion=registroDao.actualizarInformacionGeneral(con,this.codigoTransModificar,observaciones,entidad,UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracion));
				             if(!enTransaccion)
					         {
				                 myFactory.abortTransaction(con);
					             logger.warn("Transaction Aborted-No se Inserto"); 
				             }
			             }			             
			         }			                
			     }
			     else
			     {//de lo  contrario se trata de ingreso por primera vez
			         ConsecutivosDisponibles consec=new ConsecutivosDisponibles(); 
                     //05/04/2006 joan- cambio en la tabla para insertar la llave del tipo de transaccon, y no la de la transacción valida por centro de costo
				     //enTransaccion=registroDao.insertarInformacionGeneralTransaccion(con,consecutivo,this.codigoTransValidaXCC,UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracion),this.loginUsuario,entidad,observaciones,estado);
                     enTransaccion=registroDao.insertarInformacionGeneralTransaccion(con,consecutivo,this.codigoTipoTransaccion,UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracion),this.loginUsuario,entidad,observaciones,estado,this.codigoAlmacenTransaccion,false);
				     if(!enTransaccion)
			         {
			             myFactory.abortTransaction(con);
			             logger.warn("Transaction Aborted-No se Inserto informacion general de la transacción");
			         }
				     else
				     {			         
				         this.codigoPKTransaccion=obtnerCodigoTransaccionInsertada(con);
				         enTransaccion=consec.actualizarValorConsecutivoInventarios(con,this.codigoTipoTransaccion,this.codigoAlmacen,this.institucion);
				         if(!enTransaccion)
				         {
				             myFactory.abortTransaction(con);
				             logger.warn("Transaction Aborted-No se incremento consecutivo");
				         }
				     }
			     }
				 if(enTransaccion)
				 {			        
			       myFactory.endTransaction(con);
	               logger.warn("End Transaction !");
				 }
			}
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;
		
	}
	/**
	 * metodo para iniciar un atransacción,
	 * mediante una misma transacción
	 * @param con Connection
	 * @return boolean
	 */
	public boolean empezarTransaccional(Connection con)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
	    try 
		{
			if (registroDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;			
	}
	/**
	 * metodo para cerrar la transacción,
	 * abierta por una misma conexion
	 * @param con Connection
	 * @param enTransaccion boolean 
	 */
	public void cerrarTransaccion(Connection con,boolean enTransaccion)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    try
	    {
		    if(enTransaccion)
	        {
	            myFactory.endTransaction(con);
	            logger.warn("End Transaction !");
	        } 
	    }catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param lote
	 * @param fechaVencimiento
	 * @param vo
	 * @param enTransaccion
	 * @param artEliminados
	 * @return
	 */
	public boolean generarDetalleTransaccionTrans(Connection con,String codigoDetalle,String articulo,String cantidad,String valorUnitario,String lote,String codigoInterfaz,String fechaVencimiento,HashMap vo,boolean enTransaccion,ArrayList artEliminados, String nitProveedor)
	{		   
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    try 
		{
		    if(enTransaccion)
		    {
		        if(this.cerrarTransaccion.equals("true"))
			    {
			        enTransaccion=registroDao.actualizarEstadoTransaccion(con,ConstantesBDInventarios.codigoEstadoTransaccionInventarioCerrada,this.codigoPKTransaccion);
			        if(!enTransaccion)
			         {
			             myFactory.abortTransaction(con);
			             logger.warn("Transaction Aborted-No se Inserto");
			         }
			    }
		    }
		    if(enTransaccion)
		    {
                //ResultadoBoolean r=this.existeModificacionInfoGeneral(con,vo);//para el manejo de concurrencia, cuando varios usuarios entran a modificar una misma transacción
                //if(!r.isTrue())//solo permitir la modificación del detalle al usuario que modifico de ultimo la info. general de la transacción
                {
                	//BLOQUEAR EL REGISTRO.
			    	 ArrayList filtro=new ArrayList();
			    	 filtro.add(this.codigoTransModificar+"");
			    	 UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoRegistroTransacciones,filtro);
                     enTransaccion=true;
                     this.eliminarArticulosDetalle(con,this.codigoTransModificar,artEliminados);
                     HashMap mapa=new HashMap();
                     mapa=registroDao.existeDetalleTransaccionEnBD(con,codigoDetalle);
    		         if(Integer.parseInt(mapa.get("numRegistros")+"")!=0)
    		         {
    		             boolean existeMod=validarModificacionesDetalle(mapa,cantidad,valorUnitario,lote,fechaVencimiento);

    		            if(existeMod)
    		             {
    		                 enTransaccion=registroDao.actualizarDetalleTransaccion(con,Integer.parseInt(codigoDetalle),Integer.parseInt(cantidad),valorUnitario,lote,fechaVencimiento);
    			             if(!enTransaccion)
    				         {
    				             myFactory.abortTransaction(con);
    				             logger.warn("Transaction Aborted-No se modifico[problemas SQL]");
    				         }
    		             }                        
    		         }
    		         else
    		         {			  
    		            enTransaccion=insertarDetalleTransaccion(con,this.codigoPKTransaccion,Integer.parseInt(articulo),Integer.parseInt(cantidad),valorUnitario,lote,fechaVencimiento, nitProveedor);
    				    if(!enTransaccion)
    			         {
    			             myFactory.abortTransaction(con);
    			             logger.warn("Transaction Aborted-No se Inserto");
    			         }
    		         }	
                }
                /*else
                {
                     enTransaccion=false;
                     myFactory.abortTransaction(con);
                     logger.warn("Transaction Aborted-No se modifico[problemas concurrencia]");
                }*/
		    }	
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    }
	    return enTransaccion;
	}
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param lote
	 * @param fechaVencimiento
	 * @param vo
	 * @param artEliminados
	 * @return
	 */
	public boolean generarDetalleTransaccionTrans(Connection con,String codigoDetalle,String articulo,String cantidad,String valorUnitario,String lote,String codigoInterfaz,String fechaVencimiento,HashMap vo,ArrayList artEliminados, String nitProveedor)
	{	
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
	    try 
		{
			if (registroDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}		    
		    if(enTransaccion)
		    {
                //ResultadoBoolean r=this.existeModificacionInfoGeneral(con,vo);//para el manejo de concurrencia, cuando varios usuarios entran a modificar una misma transacción
                //if(!r.isTrue())//solo permitir la modificación del detalle al usuario que modifico de ultimo la info. general de la transacción
                {
                	//BLOQUEAR EL REGISTRO
			    	 ArrayList filtro=new ArrayList();
			    	 filtro.add(this.codigoTransModificar);
			    	 UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoRegistroTransacciones,filtro);
                    enTransaccion=true;
                    this.eliminarArticulosDetalle(con,this.codigoTransModificar,artEliminados);
                    HashMap mapa=new HashMap();
                    mapa=registroDao.existeDetalleTransaccionEnBD(con,codigoDetalle);
                    if(Integer.parseInt(mapa.get("numRegistros")+"")!=0)
    		         {                     
    		             boolean existeMod=validarModificacionesDetalle(mapa,cantidad,valorUnitario,lote,fechaVencimiento);
    		             if(existeMod)
    		             {
    		                 enTransaccion=registroDao.actualizarDetalleTransaccion(con,Integer.parseInt(mapa.get("codigo_0")+""),Integer.parseInt(cantidad),valorUnitario,lote,fechaVencimiento);
    			             if(!enTransaccion)
    				         {
    				             myFactory.abortTransaction(con);
    				             logger.warn("Transaction Aborted-No se modifico[problemas SQL]");
    				         }
                         }                     
    		         }
    		         else
    		         {			  
    		            enTransaccion=insertarDetalleTransaccion(con,this.codigoPKTransaccion,Integer.parseInt(articulo),Integer.parseInt(cantidad),valorUnitario,lote,fechaVencimiento, nitProveedor);
    				    if(!enTransaccion)
    			         {
    			             myFactory.abortTransaction(con);
    			             logger.warn("Transaction Aborted-No se Inserto[problemas SQL]");
    			         }
    		         }	
                }
                /*else
                {
                     enTransaccion=false;
                     myFactory.abortTransaction(con);
                     logger.warn("Transaction Aborted-No se modifico[problemas concurrencia]");
                }*/
		    }	
		    if(enTransaccion)
	        {
	            myFactory.endTransaction(con);
	            logger.warn("End Transaction !");
	        } 
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    }
	    return enTransaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param i
	 * @param artEliminados
	 */
	private void eliminarArticulosDetalle(Connection con, int codTrans, ArrayList artEliminados) 
	{
		for(int i=0;i<artEliminados.size();i++)
		{
			HashMap mapa=new HashMap();
			mapa=registroDao.existeDetalleTransaccionEnBD(con,artEliminados.get(i)+"");
	        if(Integer.parseInt(mapa.get("numRegistros")+"")!=0)
	        {
	        	registroDao.eliminarDetalleTransaccion(con,Integer.parseInt(mapa.get("codigo_0")+""));
	        }
		}
    }
	
	/**
	 * metodo para validar si existen modificaciones
	 * en el detalle
	 * @param mapa
	 * @param cantidad
	 * @param valorUnitario
	 * @return boolean
	 * @author jarloc
	 * @param fechaVencimiento 
	 * @param lote 
	 */
	private boolean validarModificacionesDetalle(HashMap mapa,String cantidad,String valorUnitario, String lote, String fechaVencimiento)
	{
	    boolean existemod=false;
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	        if(!(mapa.get("cantidad_"+k)+"").equals(cantidad))
	            existemod=true;
	        if(!(mapa.get("val_unitario_"+k)+"").equals(valorUnitario))
	            existemod=true;
	        if(!(mapa.get("lote_"+k)+"").equals(lote))
	        	existemod=true;
	        if(!(mapa.get("fecha_vencimiento_"+k)+"").equals(fechaVencimiento))
	        	existemod=true;
	    }
	    return existemod;
	}
	/**
	 * metodo para validar si existen modificaciones
	 * @param mapa
	 * @param observaciones
	 * @param entidad
	 * @return boolean
	 * @author jarloc
	 */
	private boolean validarModificacionesInfoGeneral(HashMap mapa,String observaciones,int entidad)
	{
	    boolean existemod=false;
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	        if(!(mapa.get("observaciones_"+k)+"").equals(observaciones))
	            existemod=true;
	        if(!(mapa.get("fecha_elaboracion_"+k)+"").equals(UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracion)))
	            existemod=true;
	        if(Integer.parseInt((mapa.get("entidad_"+k)+""))!=entidad)
	            existemod=true;
	    }
	    return existemod;
	}
	/**
     * llenar el log de modificaciones
     * @param k
     */
    private void llenarLog(HashMap mapa)
    {     
	    this.log=	"\n			====INFORMACION ORIGINAL===== " +
		 			"\n*  Transacción ["+this.nombreTransaccion+"] " +	   
	    			"\n*  Fecha Elaboración ["+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion_0")+"")+"] "+	   
	    			"\n*  Entidad ["+mapa.get("nombre_entidad_0")+"] "+
	    			"\n*  Observaciones ["+mapa.get("observaciones_0")+"] "+
	    			"\n\n";
    }
	/**
	 * metodo para generar el log
	 * @param k
	 */
    private void generarLog(String observaciones)
	{          
        this.log+=
					"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
					"\n*  Fecha Elaboración ["+this.fechaElaboracion+"] " +       
					"\n*  Entidad ["+this.mapaAtributos.get("nombreEntidad")+""+"] "+
					"\n*  Observaciones ["+observaciones+"] "+
					"\n========================================================\n\n\n " ;
		
		LogsAxioma.enviarLog(ConstantesBD.logRegistroTransaccionesInvCodigo,this.log,ConstantesBD.tipoRegistroLogModificacion,this.usuario);       
	}
    
    /**
     * metodo que carga los articulos de una transaccion.
     * @param con
     * @param listadoTrans
     * @return
     */
    public HashMap consultarDetalleTransaccion(Connection con, String codTransaccion)
    {
        HashMap map=new HashMap ();
	    map=registroDao.consultarDetalleTransaccion(con,codTransaccion);
	    return map;
    }
    
    /**
     * Metodo para consultar el detalle de
     * articulos de una transacción de inventarios
     * por almacen
     * @param con Connection
     * @param codTransaccion int
     * @param almacen int
     * @param institucion int 
     * @return HashMap
     * @author jarloc
     */
    public HashMap consultarDetalleTransaccion(Connection con, String codTransaccion,int almacen,int institucion)
    {
        return registroDao.consultarDetalleTransaccion(con,codTransaccion,almacen,institucion);
    }
    
    /**
     * metodo para verificar si un registro
     * posee entrda en la BD
     * @param con Connection
     * @param codigo int
     * @return boolean
     * @author jarloc
     */
    public boolean existeRegistroTransaccionEnBD(Connection con,int codigo)
    {
        HashMap mapa=new HashMap();
        mapa=registroDao.existeRegistroTransaccionEnBD(con, codigo);
        if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
            return true;
        else
            return false;
    }    
    /**
     * metodo para insertar el detalle de 
     * la transacción
     * @param con Connection
     * @param transaccion int
     * @param articulo int
     * @param cantidad int
     * @param valorUnitario String
     * @return boolean
     * @author jarloc
     * @param fechaVencimiento 
     * @param lote 
     */
    public boolean insertarDetalleTransaccion(Connection con,int transaccion,int articulo,int cantidad,String valorUnitario, String lote, String fechaVencimiento, String nitProveedor)
    {
    	//NO MANEJA PROVEEDOR DE COMPRA NI PROVEEDOR CATALOGO, SE ENVIA EN BLANCO
       return registroDao.insertarDetalleTransaccion(con, transaccion, articulo, cantidad, valorUnitario,lote,fechaVencimiento,nitProveedor,"");
    }

    
    /**
     * metodo para obtener el ultimo codigo
     * de la transacción insertada
     * @param con Connection
     * @return int
     * @author jarloc
     */
    public int obtnerCodigoTransaccionInsertada(Connection con)
    {
        return registroDao.obtnerCodigoTransaccionInsertada(con);
    }    
    /**
     * metodo para asignar el valor costo de
     * los articulos  
     * @param con Connection   
     * @param articulo int
     * @param valor double
     * @param enTransaccion boolean
     * @return boolean
     * @author jarloc
     */
    public boolean asignarValorCostoArticulosTrans(Connection con,int articulo,double valor,boolean enTransaccion)
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    try
	    {
		    if(enTransaccion)
		    {
		        UtilidadInventarios.actualizarCostoPromedioArticulo(con,articulo, valor);
		        if(!enTransaccion)
		         {
		             myFactory.abortTransaction(con);
		             logger.warn("Transaction Aborted-No se asigno el valor del costo del articulo");
		         }
		    }
	    }catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    }
	    return enTransaccion;
    }
    /**
     * metodo para generar el registro de ajustes,
     * al costo promedio
     * @param con Connection
     * @param codigo String
     * @param articulo int
     * @param almacen int
     * @param tipoTransaccion int
     * @param transaccion String
     * @param vlrCostoAntes double
     * @param vlrCostoDespues double
     * @param tipoCosto int
     * @param enTransaccion boolean
     * @return boolean
     * @author jarloc
     */
    public boolean generarRegistroAjusteTrans(Connection con,int articulo,int almacen,int tipoTransaccion,String transaccion,double vlrCostoAntes,double vlrCostoDespues,int tipoCosto,int institucion,boolean enTransaccion)
    {
    	DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));		
 		try 
 		{			
 		    String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAjusteCostoInv,institucion);
 		    this.consecutivoDisponible = consecutivo;
 		    //Se verifica que el consecutivo sea un consecutivo válido
 		    if(Utilidades.convertirADouble(consecutivo)>0)
 		    {
	 		    enTransaccion=registroDao.generarRegistroAjuste(con,consecutivo,articulo, almacen, tipoTransaccion, transaccion, vlrCostoAntes, vlrCostoDespues, tipoCosto);
	 		    if(!enTransaccion)
	 	         {
	 		    	UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAjusteCostoInv, institucion, consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
	 	             myFactory.abortTransaction(con);
	 	             logger.warn("Transaction Aborted-No se Inserto el registor del ajuste");
	 	         }
	 		    if(enTransaccion)
	 		    {
	 		    	UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAjusteCostoInv, institucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
	 			   if(!enTransaccion)
	 			   {
	 			       logger.warn("Transacción Abortada-No se incremento el consecutivo del ajuste");
	 			       myFactory.abortTransaction(con);
	 			   }
	 		    }
 		    }
 		    else
 		    {
 		    	enTransaccion = false;
 		    	logger.warn("Transacción Abortada-No se incremento el consecutivo del ajuste");
 		    }
 			
 		}catch (SQLException e) 
 		{	       
 	        e.printStackTrace();	        
 	    } 		
 	    return enTransaccion;        
    }
    /**
     * metodo para insertar o actualizar articulos
     * por almacen, segun el caso de la transacción
     * si es de entrada o de salida
     * @param con Connection
     * @param codArticulo int
     * @param codAlmacen int
     * @param esCantidadASumar boolean 
     * @param cantidadArticulo int
     * @param fechaVencimiento 
     * @param lote 
     * @param institucion int 
     * @param enTransaccion boolean 
     * @return boolean 
     */
    public boolean generarInsertUpdateArticuloXAlmacenTrans(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento, int institucion,boolean enTransaccion,boolean soloUpdate)
    {
    	DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));		
		try 
		{
			if(enTransaccion)
			{	
				if(!soloUpdate){
					enTransaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion, nombreTransaccion,lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));
				}else{
					enTransaccion=UtilidadInventarios.actualizarArticuloAlmacenXLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion, nombreTransaccion,lote,UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));
				}
				
				if(!enTransaccion)
		         {
		             myFactory.abortTransaction(con);
		             logger.warn("Transaction Aborted-No se Inserto o Actualizo Articulo X Almacen");
		         }
			}
		}
		catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;     
    }
    /**
     * metodo para actualizar el estado de una transaccón
     * a cerrada
     * @param con Connection
     * @param enTransaccion boolean
     * @return boolean
     */
    public boolean actualizarEstadoDeLaTransaccion(Connection con,boolean enTransaccion,int estado)
    {
    	DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
    	try 
		{
	    	if(enTransaccion)
		    {
		        if(this.cerrarTransaccion.equals("true"))
			    {
			        enTransaccion=registroDao.actualizarEstadoTransaccion(con,estado,this.codigoPKTransaccion);
			        if(!enTransaccion)
			         {
			             myFactory.abortTransaction(con);
			             logger.warn("Transaction Aborted-No se Inserto");
			         }
			    }
		    }
		}
    	catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion; 
    }
    /**
     * metodo para insertar el registro del 
     * cierre de la transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String
     * @param fecha String
     * @param hora String
     * @return boolean
     */
    public boolean generarRegistroCierreTransaccion(Connection con,String transaccion,String usuario,String fecha,String hora,boolean enTransaccion)
    {
    	DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
    	try 
		{
	    	if(enTransaccion)
		    {
	    		enTransaccion=registroDao.generarRegistroCierreTransaccion(con,transaccion,usuario,UtilidadFecha.conversionFormatoFechaABD(fecha),hora);
	    		if(!enTransaccion)
		         {
		             myFactory.abortTransaction(con);
		             logger.warn("Transaction Aborted-No se Inserto el registro del cierre");
		         }
		    }
		}
    	catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion; 
    }
    /**
     * metodo para generar la anulación de la
     * transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String 
     * @param motivo String
     * @return boolean
     */
    public boolean generarAnulacionTransaccion(Connection con,String usuario,String motivo)
    {
    	DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
	    try 
		{
			if (registroDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}		    
		    else
		    {
		    	enTransaccion=registroDao.generarAnulacionTransaccion(con,this.codigoPKTransaccion+"", usuario, motivo);
		    }
		    if(enTransaccion)
	        {
	            myFactory.endTransaction(con);
	            logger.warn("End Transaction !");
	        } 
		}
	    catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion; 
    }
    
    /**
     * obtiene los nombres para adicionarlos a la consulta de birt
     * @param con
     * @param mapaNombresYRestricciones
     * @return
     */
    public HashMap getNombresReporteBirt(Connection con, HashMap mapaNombresYRestricciones)
    {
    	return registroDao.getNombresReporteBirt(con, mapaNombresYRestricciones);
    }
    
    /**
     * Metodo para verificar si existe alguna
     * modificación en la información general
     * de una transacción
     * @param con Connection
     * @param vo HashMap, con los datos a verificar
     * @return ResultadoBoolean, true si existe modificación
     * @author jarloc
     */
    public ResultadoBoolean existeModificacionInfoGeneral(Connection con,HashMap vo)
    {
        return registroDao.existeModificacionInfoGeneral(con, vo);
    }
    
    public Collection buscarEntidad(Connection con, String codigoEntidad, String descripcionEntidad)
    {
    	return registroDao.buscarEntidad(con, codigoEntidad, descripcionEntidad);
    }
    
    
    /**
     * @param codigoTipoTransaccion Asigna codigoTipoTransaccion.
     */
    public void setCodigoTipoTransaccion(int codigoTipoTransaccion) {
        this.codigoTipoTransaccion = codigoTipoTransaccion;
    }
    /**
     * @param loginUsuario Asigna loginUsuario.
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @param codigoAlmacen Asigna codigoAlmacen.
     */
    public void setCodigoAlmacen(int codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }    
    /**
     * @param con
     * @param vo
     * @return
     */
    public HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
        return registroDao.ejecutarBusquedaAvanzada(con,vo);
    }    
    /**
     * @param codigoTransModificar Asigna codigoTransModificar.
     */
    public void setCodigoTransModificar(int codigoTransModificar) {
        this.codigoTransModificar = codigoTransModificar;
    }
    /**
     * @param fechaElaboracion Asigna fechaElaboracion.
     */
    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }
    /**
     * @param nombreTransaccion Asigna nombreTransaccion.
     */
    public void setNombreTransaccion(String nombreTransaccion) {
        this.nombreTransaccion = nombreTransaccion;
    }
    /**
     * @param mapaAtributos Asigna mapaAtributos.
     */
    public void setMapaAtributos(HashMap mapaAtributos) {
        this.mapaAtributos = mapaAtributos;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    /**
     * @return Retorna codigoPKTransaccion.
     */
    public int getCodigoPKTransaccion() {
        return codigoPKTransaccion;
    }
    /**
     * @param codigoPKTransaccion Asigna codigoPKTransaccion.
     */
    public void setCodigoPKTransaccion(int codigoPKTransaccion) {
        this.codigoPKTransaccion = codigoPKTransaccion;
    }
    /**
     * @param codigoTransValidaXCC Asigna codigoTransValidaXCC.
     */
    public void setCodigoTransValidaXCC(int codigoTransValidaXCC) {
        this.codigoTransValidaXCC = codigoTransValidaXCC;
    }
    /**
     * @param cerrarTransaccion Asigna cerrarTransaccion.
     */
    public void setCerrarTransaccion(String cerrarTransaccion) {
        this.cerrarTransaccion = cerrarTransaccion;
    }
	public int getCodigoAlmacenTransaccion() {
		return codigoAlmacenTransaccion;
	}
	public void setCodigoAlmacenTransaccion(int codigoAlmacenTransaccion) {
		this.codigoAlmacenTransaccion = codigoAlmacenTransaccion;
	}
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}
	public HashMap getMapaAtributos() {
		return mapaAtributos;
	}
	public Collection getResultados() {
		return resultados;
	}
	public void setResultados(Collection resultados) {
		this.resultados = resultados;
	}
	/**
	 * @return the consecutivoDisponible
	 */
	public String getConsecutivoDisponible() {
		return consecutivoDisponible;
	}
	/**
	 * @param consecutivoDisponible the consecutivoDisponible to set
	 */
	public void setConsecutivoDisponible(String consecutivoDisponible) {
		this.consecutivoDisponible = consecutivoDisponible;
	}
	
	/**
	 * Método encargado de consultar los tipos de transacción dado un condigo de transacción
	 * @param con
	 * @param codTransaccion
	 * @return HashMap
	 */
	public HashMap consultaTipoTransaccion(Connection con, String codTransaccion)
    {
    	return registroDao.consultaTipoTransaccion(con, codTransaccion);
    }
	/**
	 * 
	 * @param con
	 * @param fechaElaboracion
	 * @param entidad
	 * @param observaciones
	 */
	public boolean actualizarDatosBasicos(Connection con,String fechaElaboracion, int entidad, String observaciones) 
	{
		return registroDao.actualizarDatosBasicos(con, this.getCodigoPKTransaccion(),UtilidadFecha.conversionFormatoFechaABD(fechaElaboracion),entidad,observaciones);
	}
}