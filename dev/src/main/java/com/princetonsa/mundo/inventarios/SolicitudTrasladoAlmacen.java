
/*
 * Creado   11/01/2006
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

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.SolicitudTrasladoAlmacenDao;

/**
 * Clase que implementa los metodos
 * que son comunican con la capa Dao para
 * acceder a la fuente de datos.
 *
 * @version 1.0, 11/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SolicitudTrasladoAlmacen 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(SolicitudTrasladoAlmacen.class);
    /**
	 * DAO de este objeto, para trabajar con
	 * registro transacciones en la fuente de datos
	 */
    private static SolicitudTrasladoAlmacenDao solicitudDao;
    /**
     * codigo del almacen al cual se solicita
     */
    private int almacenSolicitado;
    /**
     * codigo del almacen solicitado
     */
    private int almacenSolicitante;
    /**
     * prioridad de la solicitud
     */
    private String esPrioritario;
    /**
     * fecha de elaboración de la solicitud
     */
    private String fechaElaboracion;
    /**
     * hora de elaboración de la solicitud
     */
    private String horaElaboracion;
    /**
     * código de la institución 
     */
    private int institucion;
    /**
     * almacena las observaciones de la solicitud
     */
    private String observaciones;  
    /**
     * almacena los logs
     */
    private String log;
    /**
     * login del usuario
     */
    private String usuario;
    /**
     * log para almacenar el detalle de
     * la solicitud
     */
    private String logDetalle;
    /**
     * log para almacenar el detalle de
     * la solicitud
     */
    private String logDetalle1;
    /**
     * log para almacenar el detalle de
     * la solicitud
     */
    private String logDetalle2;
    /**
     * para almacenar información de 
     * los articulos eliminados de una
     * solicitud
     */
    private String logEliminacion;
    /**
     * para almacenar información de 
     * los articulos eliminados de una
     * solicitud
     */
    private String logEliminacion1;
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */   
    public void reset ()
    { 
        this.almacenSolicitado=ConstantesBD.codigoNuncaValido;
        this.almacenSolicitante=ConstantesBD.codigoNuncaValido;
        this.esPrioritario="";
        this.fechaElaboracion="";
        this.horaElaboracion="";
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.observaciones="";
        this.log="";
        this.usuario="";
        this.logDetalle="";
        this.logDetalle1="";
        this.logDetalle2="";
        this.logEliminacion="";
        this.logEliminacion1="";
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( solicitudDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			solicitudDao= myFactory.getSolicitudTrasladoAlmacenDao();			
			if( solicitudDao!= null )
				return true;
		}
		return false;
	}
	/**
	 * constructor
	 */
	public SolicitudTrasladoAlmacen()
	{
	  this.reset();
	  this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * metodo para generar el listado de almacenes
	 * @param institucion int
	 * @param centroCosto int
	 * @param transaccion String
	 * @param login String 
	 * @return HashMap
	 */
	public HashMap genrarConsultaAlmacenes(int institucion,int centroCosto,String transaccion)
	{
	    HashMap mapaTemp=new HashMap();	 
	    HashMap mapa=new HashMap();
	    String parejas="";
	    boolean esPrimero=true;
	    mapaTemp=UtilidadInventarios.listadoClaseGrupoSegunCentroCostoUsuario(institucion,centroCosto,Integer.parseInt(transaccion));
	    if(!mapaTemp.isEmpty())
	    {
		    for(int k=0;k<Integer.parseInt(mapaTemp.get("numRegistros").toString());k++)
		    {
		        if(!esPrimero)
		            parejas+=",";
		        parejas+="'"+mapaTemp.get("pareja_"+k).toString()+"'";
		        esPrimero=false;
		    }
		    mapa=UtilidadInventarios.listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(institucion,transaccion,parejas,centroCosto);
	    }
	    return (HashMap) mapa.clone();
	}
	/**
	 * metodo para generar la 
	 * información general de la 
	 * solicitud
	 * @param con Connection
	 * @param numeroTraslado int
	 * @param usuario String
	 * @return boolean
	 */
	public boolean generarInformacionGeneralSolicitud(Connection con,int numeroTraslado,String usuario)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    boolean enTransaccion=false;
		try 
		{
			if (solicitudDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
		    enTransaccion=solicitudDao.insertarInformacionGeneralSolicitud(con,numeroTraslado,this.almacenSolicitado,this.almacenSolicitante,ConstantesBDInventarios.codigoEstadoTrasladoInventarioPendiente,UtilidadTexto.getBoolean(this.esPrioritario),UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracion),this.horaElaboracion,usuario,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual(),this.observaciones);
		}
		catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;
	}
	/**
     * metodo para insertar el detalle de la 
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param codArticulo int
     * @param cantidad int
     * @return booelan
     */
    public boolean insertarDetalleSolicitud(Connection con,HashMap mapa,int numReg,int numeroSolicitud,boolean enTransaccion)
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    try
	    {
		    if(enTransaccion)
		    {
		        for(int k=0;k<numReg;k++)
		        {
			        enTransaccion=solicitudDao.insertarDetalleSolicitud(con, numeroSolicitud,Integer.parseInt(mapa.get("codigoArticulo_"+k)+""),Integer.parseInt(mapa.get("cantidadArticulo_"+k)+""));
			        if(!enTransaccion)
			         {
			             myFactory.abortTransaction(con);
			             logger.warn("Transaction Aborted-No se inserto el detalle de la solicitud");
			             break;
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
     * metodo para actualizar el estado de la solicitud
     * @param con Connection 
     * @param estado int
     * @param numeroSolicitud int
     * @param institucion int
     * @return boolean
     */
    public boolean actualizarEstadoSolicitud(Connection con,int estado,int numeroSolicitud,int institucion,boolean enTransaccion)
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    try
	    {
		    if(enTransaccion)
		    {
		        enTransaccion=solicitudDao.actualizarEstadoSolicitud(con, estado, numeroSolicitud, institucion);
		        if(!enTransaccion)
		         {
		             myFactory.abortTransaction(con);
		             logger.warn("Transaction Aborted-No se actualizo el estado de la transacción");
		         }	        
		    }
	    }catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    }
	    return enTransaccion;
    }
    /**
     * metodo para realizar llamado a los metodos de
     * actualizar el estado de la solicitud e insertar
     * información del cierre
     * @param con Connection
     * @param estado  int
     * @param numeroSolicitud int
     * @param institucion int
     * @param usuario String
     * @param enTransaccion boolean
     * @return boolean
     */
    public boolean cerrarSolicitud(Connection con,int numeroSolicitud,int institucion,String usuario,boolean enTransaccion)
    {
        return this.insertarInformacionCierre(con, numeroSolicitud,usuario, enTransaccion);
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
			if (solicitudDao==null)
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
     * metodo para insertar la información del cierre dela
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param usuario Stirng
     * @return boolean
     */
    public boolean insertarInformacionCierre(Connection con,int numeroSolicitud,String usuario,boolean enTransaccion)
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    try
	    {
		    if(enTransaccion)
		    {
		        enTransaccion=solicitudDao.insertarInformacionCierre(con, numeroSolicitud, usuario);
		        if(!enTransaccion)
		         {
		             myFactory.abortTransaction(con);
		             logger.warn("Transaction Aborted-No se inserto información del cierre de la solicitud");
		         }
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
     * metodo para insertar la información de la 
     * anulación de la solicitud
     * @param con Connection
     * @param numeroSolicitud int 
     * @param usuario String
     * @param motivo String
     * @return boolean
     */
    public boolean insertarAnulacionSolicitud(Connection con,int numeroSolicitud,String usuario,String motivo)
    {
       return solicitudDao.insertarAnulacionSolicitud(con, numeroSolicitud, usuario, motivo); 
    }
    /**
     * metodo para realizar la anulación de 
     * la solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param usuario String
     * @param motivo String
     * @param estado int
     * @param institucion int 
     * @return boolean
     */
    public boolean anularSolicitudTrans(Connection con,int numeroSolicitud,String usuario,String motivo,int institucion)
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		try 
		{
			if (solicitudDao==null)
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
		        enTransaccion=this.insertarAnulacionSolicitud(con,numeroSolicitud,usuario,motivo);
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
     * metodo para generar la busqueda avanzada
     * de solicitudes
     * @param con Connection
     * @param vo HashMap
     * @return HashMap 
     */
    public HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
        HashMap mapa=new HashMap();
        mapa=solicitudDao.ejecutarBusquedaAvanzada(con, vo);
        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros").toString());k++)
        {
            mapa.put("fecha_elaboracion_"+k,UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion_"+k).toString()));
        }
        return mapa;
    }
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud)
    {
        return solicitudDao.consultaDetalleSolicitud(con, numeroSolicitud);
    }
    /**
     * metodo para actualizar la información 
     * general de la solicitud
     * @param con Connection
     * @param mapSoli HashMap, mapa con la información general de la solicitud
     * @param vo HashMap, listado de solicitudes 
     * @param regSeleccionado int, posición de la solicitud seleccionada
     * @return boolean
     */
    public boolean actualizarInformacionGeneralSolicitud(Connection con,HashMap mapSoli,HashMap vo,int regSeleccionado)
    {
        boolean hayModificacion=verificarModificacionesInfoGeneral(mapSoli,vo,regSeleccionado);
        if(hayModificacion)
        {
            boolean modifico=solicitudDao.actualizarInformacionGeneralSolicitud(con,mapSoli);
            if(modifico)
            {
                this.llenarLogInfoGeneral(vo,regSeleccionado);
                this.generarLogInfoGeneral(mapSoli);
            }
            return modifico;
        }
        return true;
    }
    /**
     * metodo para generar la modificación del
     * detalle de la solicitud
     * @param con Connection
     * @param vo HashMap, con los articulos
     * @param regEliminar ArrayList, codigos de los articulos a eliminar
     * @param numRegistros int, numero de articulos en el mapa
     * @param numSolicitud int, número de la solicitud a modificar
     * @return  boolean
     */
    public boolean modificarDetalleSolicitudTrans(Connection con,HashMap vo,ArrayList regEliminar,int numRegistros,int numSolicitud,boolean esCerrarSolicitud)
    {
        boolean existeModificadoDetalle=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		try 
		{
			if (solicitudDao==null)
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
			  if(!regEliminar.isEmpty())
		      {
		          for(int j=0;j<regEliminar.size();j++)
		          {
		              this.generarLogEliminacion(con,Integer.parseInt(regEliminar.get(j)+""),numSolicitud,false);
		              enTransaccion=solicitudDao.eliminarArticuloDetalleSolicitud(con,numSolicitud,Integer.parseInt(regEliminar.get(j)+""));  
		              if(!enTransaccion)
			          {
			             myFactory.abortTransaction(con);
			             logger.warn("Transaction Aborted-No se elimino el articulo de la solicitud para la modificación");
			             break;
			          }
		              else
		                  this.generarLogEliminacion(con,Integer.parseInt(regEliminar.get(j)+""),numSolicitud,true);   
		          }
		      	}
			    if(enTransaccion)
			    {
				  for(int k=0;k<numRegistros;k++)
				  {			      
				      if(enTransaccion)
				      {
					      if((vo.get("tipoRegistro_"+k)+"").equals("MEM"))
					      {
					          enTransaccion=solicitudDao.insertarDetalleSolicitud(con,numSolicitud,Integer.parseInt((vo.get("codigoArticulo_"+k)+"")),Integer.parseInt((vo.get("cantidadArticulo_"+k)+"")));
					          if(!enTransaccion)
					          {
					             myFactory.abortTransaction(con);
					             logger.warn("Transaction Aborted-No se inserto detalle de la solicitud para la modificación");
					             break;
					          }
					      }
				      }
				      if(enTransaccion)
				      {
					      if((vo.get("tipoRegistro_"+k)+"").equals("BD"))
					      {
					          if(verificarModificacionesDetalleSolicitud(con,Integer.parseInt((vo.get("cantidadArticulo_"+k)+"")),numSolicitud,Integer.parseInt((vo.get("codigoArticulo_"+k)+""))))
					          {
					              existeModificadoDetalle=true;
					              this.llenarLogDetalleSolicitud(con,numSolicitud,Integer.parseInt((vo.get("codigoArticulo_"+k)+"")),false);
					              enTransaccion=solicitudDao.actualizarDetalleSolicitud(con,numSolicitud,Integer.parseInt((vo.get("cantidadArticulo_"+k)+"")),Integer.parseInt((vo.get("codigoArticulo_"+k)+"")));
						          if(!enTransaccion)
						          {
						             myFactory.abortTransaction(con);
						             logger.warn("Transaction Aborted");
						             break;
						          }
						          else						          						              
						              this.generarLogDetalleSolicitud(vo,k,-1,false);						          						              
					          }
					      }
				      }
				  	}
				  	if(existeModificadoDetalle && enTransaccion)
				  	{
				  	    this.llenarLogDetalleSolicitud(null,numSolicitud,-1,true);
				  	    this.generarLogDetalleSolicitud(null,-1,numSolicitud,true);
				  	}
			    }
			    if(esCerrarSolicitud)
			    {
			        enTransaccion=this.cerrarSolicitud(con,numSolicitud,this.institucion,usuario,enTransaccion);
			        if(!enTransaccion)
			          {
			             myFactory.abortTransaction(con);
			             logger.warn("Transaction Aborted");
			          }
			    }
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
     * metodo para verificar si se realizaron 
     * modificaciones en la información 
     * general     
     * @param mapaSol HashMap, mapa con la información general de la solicitud
     * @return boolean
     */
    private boolean verificarModificacionesInfoGeneral(HashMap mapaSol,HashMap vo,int regSeleccionado)
    {                
        if(!(mapaSol.get("prioritario")+"").equals(vo.get("prioritario_"+regSeleccionado)+""))
        {
            return true;
        }
        else if(!(mapaSol.get("fecha_elaboracion")+"").equals(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_elaboracion_"+regSeleccionado)+"")))
        {
            return true;
        }
        else if(!(mapaSol.get("hora_elaboracion")+"").equals(vo.get("hora_elaboracion_"+regSeleccionado)+""))
        {
            return true;
        }
        else if(!(mapaSol.get("observaciones")+"").equals(vo.get("observaciones_"+regSeleccionado)+""))
        {
            return true;
        }
        else
            return false;
    }
    /**
     * metodo para verificar modificaciones de los articulos
     * pertenecientes al detalle de lasolicitud
     * @param con Connection
     * @param cantidad int
     * @param solicitud int
     * @param articulo int
     * @return boolean
     */
    private boolean verificarModificacionesDetalleSolicitud(Connection con,int cantidad,int solicitud,int articulo)
    {
        int cantidadBD=solicitudDao.consultarInfoArticuloSolicitud(con,solicitud,articulo);
        if(cantidadBD!=cantidad)
            return true;
        return false;
    }
    /**
     * llenar el log de modificaciones
     * @param mapa
     * @param k
     */
    private void llenarLogInfoGeneral(HashMap mapa,int pos)
    {     
	    this.log=	"\n			====INFORMACION ORIGINAL===== " +
		 			"\n*  							Solicitud Traslado al almacen ["+mapa.get("almacen_despacha_"+pos)+"] " +	   
	    			"\n*  Información General " +
	    			"\n*____________________________________________________________________________________________________________________________________" +
	    			"\n*  Almacén que Solicita["+mapa.get("nombre_almacen_solicita_"+pos)+"] Traslado No.["+mapa.get("numero_traslado_"+pos)+"] Estado["+mapa.get("nombre_estado_"+pos)+"] Prioritario["+mapa.get("prioritario_"+pos)+"]"+	   
	    			"\n*  Fecha/Hora Elaboración["+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion_"+pos)+"")+"] Usuario Elabora ["+mapa.get("hora_elaboracion_"+pos)+"]"+
	    			"\n*  Observaciones ["+mapa.get("observaciones_"+pos)+"] " +	    			   			
	    			"\n\n";
    }
    /**
	 * metodo para generar el log de
	 * la información general
	 * @param mapa HashMap
	 */
    private void generarLogInfoGeneral(HashMap mapa)
	{          
        this.log+=
		            "\n			====INFORMACION DESPUES DE LA MODIFICACION===== " +
		 			"\n*  							Solicitud Traslado al almacen ["+mapa.get("almacen_despacha")+"] " +	   
					"\n*  Información General " +
					"\n*____________________________________________________________________________________________________________________________________" +
					"\n*  Almacén que Solicita["+mapa.get("nombre_almacen_solicita")+"] Traslado No.["+mapa.get("numero_traslado")+"] Estado["+mapa.get("nombre_estado")+"] Prioritario["+mapa.get("prioritario")+"]"+	   
					"\n*  Fecha/Hora Elaboración["+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_elaboracion")+"")+"] Usuario Elabora ["+mapa.get("hora_elaboracion")+"]"+
					"\n*  Observaciones ["+mapa.get("observaciones")+"] " +	    			   			
					"\n\n";
		
		LogsAxioma.enviarLog(ConstantesBD.logSolicitudTrasladoAlmacenCodigo,this.log,ConstantesBD.tipoRegistroLogModificacion,this.usuario);       
	}
    /**
     * llenar el log de modificaciones del
     * detalle de la solicitud
     * @param mapa
     * @param k
     */
    private void generarLogDetalleSolicitud(HashMap mapa,int pos,int numSolicitud,boolean esGenerar)
    {     
	    if(esGenerar)
	    {
	        this.logDetalle1=	"\n			 ====INFORMACION DESPUES DE LA MODIFICACION===== " +    			
				    			"\n*  Detalle del Traslado No.["+numSolicitud+"]" +
				    			"\n*________________________________________________________________________________________" +
				    			"\n*  Código	|Descripción									|U.Medida		|Cantidad";
		    this.log+=this.logDetalle1+logDetalle2+"\n\n";
		    LogsAxioma.enviarLog(ConstantesBD.logSolicitudTrasladoAlmacenCodigo,this.log,ConstantesBD.tipoRegistroLogModificacion,this.usuario);
	    }
	    else
	        this.logDetalle2+=	"\n*  "+mapa.get("codigoArticulo_"+pos)+"	 |"+mapa.get("descripcionArticulo_"+pos)+"									|"+mapa.get("unidadMedidaArticulo_"+pos)+"		|"+mapa.get("cantidadArticulo_"+pos);
				    			
	    
    }
    /**
     * generar el log de modificaciones del
     * detalle de la solicitud
     * @param mapa
     * @param k
     */
    private void llenarLogDetalleSolicitud(Connection con,int solicitud,int articulo,boolean esGenerar)
    {     
        if(esGenerar)
        {
	        this.log=	"\n			====INFORMACION ORIGINAL===== " +    			
				    			"\n*  Detalle del Traslado No.["+solicitud+"]" +
				    			"\n*________________________________________________________________________________________" +
				    			"\n*  Código	|Descripción									|U.Medida		|Cantidad";
	        this.log+=this.logDetalle+"\n\n";	        
        }
		else
		{
		    HashMap mapa=new HashMap();
		    mapa=solicitudDao.consultarDetalleSolicitudUnArticulo(con, solicitud, articulo);
		    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
		        this.logDetalle+=	"\n*  "+mapa.get("articulo_"+k)+"	 |"+mapa.get("descripcion_"+k)+"									|"+mapa.get("unidadmedida_"+k)+"		|"+mapa.get("cantidad_"+k);
		}
				    			
	    
    }
    /**
     * metodo para generar el log de la 
     * eliminación de un articulo del detalle de 
     * una solicitud     
     * @param con
     * @param articulo
     */
    private void generarLogEliminacion(Connection con,int articulo,int numSolicitud,boolean esGenerar)
    {
        if(esGenerar)
        {
	        this.logEliminacion="\n			==== REGISTRO ELIMINADO ===== " +	        					
	        					"\n*	Detalle del Traslado No.["+numSolicitud+"]" +
	        					"\n*_________________________________________________________________" +
	        					"\n*  Código	|Descripción									|U.Medida		|Cantidad" ;
	        this.logEliminacion+=this.logEliminacion1+"\n\n";
	        LogsAxioma.enviarLog(ConstantesBD.logSolicitudTrasladoAlmacenCodigo,this.logEliminacion,ConstantesBD.tipoRegistroLogEliminacion,this.usuario);
        }
        else
        {
			HashMap mapa=new HashMap();
		    mapa=solicitudDao.consultarDetalleSolicitudUnArticulo(con,numSolicitud,articulo);
		    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
		        this.logEliminacion1+="\n*  "+mapa.get("articulo_"+k)+"	 |"+mapa.get("descripcion_"+k)+"									|"+mapa.get("unidadmedida_"+k)+"		|"+mapa.get("cantidad_"+k);
        }
    }
    /**
     * @param almacenSolicitado Asigna almacenSolicitado.
     */
    public void setAlmacenSolicitado(int almacenSolicitado) {
        this.almacenSolicitado = almacenSolicitado;
    }
    /**
     * @param almacenSolicitante Asigna almacenSolicitante.
     */
    public void setAlmacenSolicitante(int almacenSolicitante) {
        this.almacenSolicitante = almacenSolicitante;
    }
    /**
     * @param esPrioritario Asigna esPrioritario.
     */
    public void setEsPrioritario(String esPrioritario) {
        this.esPrioritario = esPrioritario;
    }
    /**
     * @return Retorna esPrioritario.
     */
    public String getEsPrioritario() {
        return esPrioritario;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @param fechaElaboracion Asigna fechaElaboracion.
     */
    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }
    /**
     * @param horaElaboracion Asigna horaElaboracion.
     */
    public void setHoraElaboracion(String horaElaboracion) {
        this.horaElaboracion = horaElaboracion;
    }
    /**
     * @param observaciones Asigna observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
