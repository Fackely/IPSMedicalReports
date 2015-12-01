
/*
 * Creado   23/01/2006
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

import org.apache.log4j.Logger;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.DespachoTrasladoAlmacenDao;
import com.princetonsa.mundo.Articulo;

/**
 * Clase que implementa los metodos que
 * comunican el WorkFlow de la funcionalidad
 * con la fuente de datos
 *
 * @version 1.0, 23/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class DespachoTrasladoAlmacen 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(DespachoTrasladoAlmacen.class);
    /**
	 * DAO de este objeto, para trabajar con
	 * registro transacciones en la fuente de datos
	 */
    private static DespachoTrasladoAlmacenDao despachoDao;  
    /**
     * código de la institución
     */
    private int institucion;
    /**
     *codigo del almacen despacho
     */
    private int almacen;
    /**
     * login del usuario
     */
    private String usuario;
    
    /**
     * 
     */
    private String mensaje;
    
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */   
    public void reset ()
    { 
       this.institucion=ConstantesBD.codigoNuncaValido;
       this.almacen=ConstantesBD.codigoNuncaValido;
       this.usuario="";
       this.mensaje="";
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( despachoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			despachoDao= myFactory.getDespachoTrasladoAlmacenDao();			
			if( despachoDao!= null )
				return true;
		}
		return false;
	}
	/**
	 * constructor
	 */
	public DespachoTrasladoAlmacen()
	{
	  this.reset();
	  this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * metodo para generar el listado de solicitudes
	 * traslados almacenes
	 * @param con Connection
	 * @param estado int 
	 * @param almacenSolicitado int 
	 * @return HashMap
	 */
	public HashMap consultarListadoTrasladosAlmacenes(Connection con,int estado,int almacenSolicitado)
	{
	    HashMap mapa=new HashMap();
	    mapa=despachoDao.consultaSolicitudesTraslados(con, estado, almacenSolicitado);
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	        mapa.put("fecha_hora_elaboracion_"+k,UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion_"+k).toString())+"/"+mapa.get("hora_elaboracion_"+k));
	    }
	    return (HashMap) mapa.clone();
	}
	/**
	 * metodo para generar la consulta del detalle
	 * de la solicitud
	 * @param con Connection
	 * @param numeroSolicitud int 
	 * @return HashMap
	 */
	public HashMap consultarDetalleSolicitud(Connection con,int numeroSolicitud)
	{	    
	    HashMap mapa=new HashMap();	    
	    mapa=despachoDao.consultaDetalleSolicitud(con,numeroSolicitud);
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {	        
	    	if(Articulo.articuloManejaLote(con,Integer.parseInt(mapa.get("articulo_"+k)+""), institucion))
	    	{
	    		mapa.put("existenciaactual_"+k,UtilidadInventarios.existenciasArticuloAlmacenLote(Integer.parseInt(mapa.get("articulo_"+k)+""),this.almacen,mapa.get("lote_"+k)+"",UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+k)+""))+"");
	    	}
	    	else
	    	{
	    		mapa.put("existenciaactual_"+k,UtilidadInventarios.existenciasArticuloAlmacen(Integer.parseInt(mapa.get("articulo_"+k)+""),this.almacen,this.institucion)+"");
	    	}
	    }
	    return (HashMap) mapa.clone();
	}
	/**
	 * metodo para realizar el despacho del
	 * traslado almacen en una transacción
	 * @param con Connection
	 * @param articulos HashMap
	 * @param solicitud HashMap
	 * @return boolean
	 */
	public boolean generarDespachoTrans(Connection con,HashMap articulos,HashMap solicitud,boolean esDespachar)
	{
		logger.info("\n\n\n 	PASA POR (generarDespachoTrans 1 )!!!!");
		
	    boolean inicioTrans,enTransaccion=true;
		try 
		{
			if (despachoDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
	        if(!despachoDao.existeRegistroDespachoBD(con,Integer.parseInt(solicitud.get("numeroSolicitud")+"")))
	        {
	            enTransaccion=despachoDao.insertarDespacho(con,solicitud);
	        }
	        if(enTransaccion)
	        {
	        	
	        	logger.info("\n\n\n 	PASA POR (generarDespachoTrans 2 )!!!!");
	        	
	            HashMap mapa=new HashMap ();
                mapa.put("numeroSolicitud",solicitud.get("numeroSolicitud"));
	            for(int k=0;k<Integer.parseInt(articulos.get("numRegistros")+"");k++)
	            {	
	            	
	            	logger.info("\n\n\n 	PASA POR (generarDespachoTrans 3 )!!!!");
	            	
	            	String codigo=articulos.get("codigo_"+k)+"";
	            	if(UtilidadTexto.isEmpty(codigo))
	            		codigo="-1";
	            	mapa.put("codigo", codigo);
	                mapa.put("articulo",articulos.get("articulo_"+k));
	                mapa.put("cantidad",articulos.get("cantidaddespachada_"+k));
	                mapa.put("lote",articulos.get("lote_"+k));
	                mapa.put("fechavencimiento", articulos.get("fechavencimiento_"+k));
	                mapa.put("proveedorconsignacion", articulos.get("proveedorconsignacion_"+k));
	                mapa.put("valorUnitario",UtilidadInventarios.costoActualArticulo(Integer.parseInt(articulos.get("articulo_"+k)+""))+"");
	                
	                HashMap mapaOld=despachoDao.consultarCantidadRegistroDetalleDespacho(con,mapa);
	                if(Integer.parseInt(mapaOld.get("numRegistros")+"")==0)//se inserta
	                {
	                	logger.info("\n\n\n 	PASA POR (generarDespachoTrans 4 )!!!!");
	                	
	                    if(Integer.parseInt(articulos.get("cantidaddespachada_"+k)+"")>0)
	                    {
	                    	logger.info("\n\n\n 	PASA POR (generarDespachoTrans 5 )!!!!");
	                        enTransaccion=despachoDao.insertarDetalleDespacho(con,mapa);
	                    }
	                }
	                else
	                {
	                	logger.info("\n\n\n 	PASA POR (generarDespachoTrans 4 - 1)!!!!");
	                	
	                    if(verificarModificaciones(mapa,mapaOld))
	                    {
	                        enTransaccion=despachoDao.actualizarDetalleDespacho(con,mapa);
	                    }
	                }	
	            }
	            if(esDespachar)
	            {
	            	ArrayList filtro=new ArrayList();
	    			filtro.add(solicitud.get("numeroSolicitud")+"");
	    			UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearSolicitudTraslado, filtro);
	    			if(Utilidades.obtenerEstadoSolicitudTraslado(con,solicitud.get("numeroSolicitud")+"")!=ConstantesBDInventarios.codigoEstadoTrasladoInventarioCerrada)
	    			{
	    				this.mensaje="La solicitud fue modificada por otro usuario";
                        return false;
	    			}
	    			
                    enTransaccion=despachoDao.actualizarUsuarioDespacho(con,this.usuario,Integer.parseInt(solicitud.get("numeroSolicitud")+""));
                    if(enTransaccion)
                    {
                        SolicitudTrasladoAlmacen mundoSolicitud=new SolicitudTrasladoAlmacen();
		                enTransaccion=mundoSolicitud.actualizarEstadoSolicitud(con,ConstantesBDInventarios.codigoEstadoTrasladoInventarioDespachada,Integer.parseInt(solicitud.get("numeroSolicitud")+""),this.institucion,enTransaccion);
                    }
	            }		                
	        }	              
		
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;
	}
    
    /**
     * metodo para actualizar o insertar las existencias
     * de los articulos por almacén
     * @param con Connection
     * @param mapa HashMap
     * @param almacenSolicita int
     * @param almacenDespacha int
     * @param numSolicitud int
     * @return boolean
     */
    public boolean actualizarExistenciasAlmacenes(Connection con,HashMap mapa,int almacenSolicita,int almacenDespacha, int numSolicitud)
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        boolean inicioTrans,enTransaccion=true;
        try 
        {
            if (despachoDao==null)
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
               for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
               {                   
                   HashMap vo=new HashMap ();
                   String codigo=mapa.get("codigo_"+k)+"";
               	   if(UtilidadTexto.isEmpty(codigo))
               			codigo="-1";
               	   vo.put("codigo", codigo);
                   vo.put("numeroSolicitud",numSolicitud+"");
                   vo.put("articulo",mapa.get("articulo_"+k));
                   vo.put("lote",mapa.get("lote_"+k));
                   vo.put("fechavencimiento",mapa.get("fechavencimiento_"+k));
                   vo.put("cantidaddespachada",mapa.get("cantidaddespachada_"+k));                   
                   if(Integer.parseInt(mapa.get("cantidaddespachada_"+k)+"")>0)
                   {
                       /**actualizar las existencias del almacen que solicita*/
                	   if(Articulo.articuloManejaLote(con, Integer.parseInt(mapa.get("articulo_"+k)+""), 2))
                		{
                		   enTransaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,Integer.parseInt(mapa.get("articulo_"+k)+""),almacenSolicita,true,Integer.parseInt(mapa.get("cantidaddespachada_"+k)+""),this.institucion,ConstantesBD.continuarTransaccion,mapa.get("lote_"+k)+"",UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+k)+""));
                		}
                	   else
                	   {
                		   enTransaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,Integer.parseInt(mapa.get("articulo_"+k)+""),almacenSolicita,true,Integer.parseInt(mapa.get("cantidaddespachada_"+k)+""),this.institucion,ConstantesBD.continuarTransaccion);
                	   }
                       if(!enTransaccion)
                        {                        
                            logger.warn("Transaction Aborted");
                            break;
                        }
                       /**actualizar las existencias del almacén que despacha ó insertar existencia negativa si no poseia existencias*/
                       if(Articulo.articuloManejaLote(con, Integer.parseInt(mapa.get("articulo_"+k)+""), 2))
	               		{
	                    	   enTransaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,Integer.parseInt(mapa.get("articulo_"+k)+""),almacenDespacha,false,Integer.parseInt(mapa.get("cantidaddespachada_"+k)+""),this.institucion,ConstantesBD.continuarTransaccion,mapa.get("lote_"+k)+"",UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechavencimiento_"+k)+""));
	               		}
                       else
	               		{
                    	   enTransaccion=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,Integer.parseInt(mapa.get("articulo_"+k)+""),almacenDespacha,false,Integer.parseInt(mapa.get("cantidaddespachada_"+k)+""),this.institucion,ConstantesBD.continuarTransaccion);
	               		}
                       if(!enTransaccion)
                        {                        
                            logger.warn("Transaction Aborted");
                            break;
                        }
                   }
               }
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
	 * metodo para validar modificación	 
	 * @param mapa
	 * @param mapaOld
	 * @return
	 */
	private boolean verificarModificaciones(HashMap mapa,HashMap mapaOld)
	{
		logger.info("CANT 1 = "+mapa.get("cantidad"));
		logger.info("CANT 2 = "+mapaOld.get("cantidad"));
		
		if(!(mapa.get("cantidad")+"").equals(mapaOld.get("cantidad")+""))
			return true;
		if(!(mapa.get("lote")+"").equals(mapaOld.get("lote")+""))
			return true;
		if(!(mapa.get("fechavencimiento")+"").equals(mapaOld.get("fechavencimiento")+""))
			return true;
		if(!(mapa.get("proveedorconsignacion")+"").equals(mapaOld.get("proveedorconsignacion")+""))
			return true;
	 return false;   
	}
    /**
     * @param almacen Asigna almacen.
     */
    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
	public String getMensaje()
	{
		return mensaje;
	}
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}
}
