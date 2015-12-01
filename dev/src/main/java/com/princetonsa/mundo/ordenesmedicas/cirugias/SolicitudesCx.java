/*
 * @(#)SolicitudesCx.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.ordenesmedicas.cirugias;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudesCxDao;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.Articulo;

/**
 * Clase para el manejo de las solicitudes Cx
 * @version 1.0, Nov 09, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SolicitudesCx 
{
    /**
     * DAO utilizado por el objeto parra acceder a la fuente de datos
     */
    private static SolicitudesCxDao cxDao = null;
    
    
    //Atributos del enabezado
    private String numeroSolicitud;
    private String codigoPeticion;
    private String indQx;
    private String fechaInicialCx;
    private String fechaFinalCx;
    private String horaInicialCx;
    private String horaFinalCx;
    private String fechaIngresoSala;
    private String horaIngresoSala;
    private String fechaSalidaSala;
    private String horaSalidaSala;
    private String codigoSalidaSalaPaciente;
    private String codigoSalidaPacienteCc;
    private String descripcionSalidaPacienteCc;
    /**
     * Campo usado en la liquidación de servicios para saber si se va a cobrar o no asocio de anestesia
     * cuando no se ha grabado información de la liquidación este campo se llena con el campo cobrar_anestesia de la hoja de anestesia
     */
    private String liquidarAnestesia;
    
    private String duracionCx;
    
    /**
     * Para hacer logs de debug / warn / error de esta funcionalidad.
     */
    //private Logger logger = Logger.getLogger(SolicitudesCxDao.class);

    /**
     * reset de los atributos
     *
     */
    public void reset()
    {
    	this.numeroSolicitud = "";
    	this.codigoPeticion = "";
    	this.indQx = "";
    	this.fechaInicialCx = "";
    	this.fechaFinalCx = "";
    	this.horaInicialCx = "";
    	this.horaFinalCx = "";
    	this.fechaIngresoSala = "";
    	this.horaIngresoSala = "";
    	this.fechaSalidaSala = "";
    	this.horaSalidaSala = "";
    	this.liquidarAnestesia = "";
    	this.duracionCx = "";
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
            cxDao = myFactory.getSolicitudesCxDao();
            wasInited = (cxDao != null);
        }
        return wasInited;
    }
    
    /**
     * Constructor vacio de la clase
     *
     */
    public SolicitudesCx()
    {
        this.reset();
        this.init (System.getProperty("TIPOBD"));
    }
    
   
    /**
     * Método transaccional que inserta la solicitud Cx General, en caso de que sea centro costo esterno entonces no se
     * hace relación a ninguna petición, (peticion=null)
     * @param con
     * @param numeroSolicitud
     * @param codigoPeticion
     * @param esCentroCostoSolicitadoExterno
     * @param estado
     * @return
     * @throws SQLException
     */
    public boolean insertarSolicitudCxGeneralTransaccional1 (    Connection con, 
                                                                                            String numeroSolicitud, 
                                                                                            String codigoPeticion, 
                                                                                            boolean esCentroCostoSolicitadoExterno,
                                                                                            String estado,
                                                                                            double subCuenta,
                                                                                            String indicativoQx) throws SQLException
    {
        boolean insertado=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estado.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {        
            insertado= cxDao.insertarSolicitudCxGeneral1(
            		con, 
            		numeroSolicitud,
            		codigoPeticion,
            		esCentroCostoSolicitadoExterno, 
            		subCuenta,
            		indicativoQx,
            		this.getFechaInicialCx(),
            		this.getHoraInicialCx(),
            		this.getFechaFinalCx(),
            		this.getHoraFinalCx(),
            		this.getFechaIngresoSala(),
            		this.getHoraIngresoSala(),
            		this.getFechaSalidaSala(),
            		this.getHoraSalidaSala(),
            		this.getLiquidarAnestesia(),
            		this.getDuracionCx());
            if (!insertado)
            {
                myFactory.abortTransaction(con);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estado.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertado;
    }
    
    /**
     * 
     * @param con
     * @param subCuenta
     * @param numeroSolicitud
     * @return
     */
    public static boolean actualizarSubCuentaCx(Connection con, double subCuenta, String numeroSolicitud ) throws IPSException
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().actualizarSubCuentaCx(con, subCuenta, numeroSolicitud);
    }

    /**
     * Método para actualizar la finalidad del servicio cx
     * @param con
     * @param campos
     * @return
     */
    public static boolean actualizarFinalidadServicioCx(Connection con,String codSolServ,int codigoFinalidad)
    {
    	HashMap campos = new HashMap();
    	campos.put("codigo",codSolServ);
    	campos.put("codigoFinalidad",codigoFinalidad);
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().actualizarFinalidadServicioCx(con, campos);
    }

    
    /**
     * Método Transaccional que Inserta las solicitud Cx X servicio, en caso de que 
     * (codigoViaAcceso, codigoMedico, codigoEsquemaTarifario,codigoGrupoUvr) == ConstantesBD.codigoNuncaValido
     * entonces se insertan como null.
     * @param con
     * @param numeroSolicitud
     * @param codigoServicio
     * @param codigoTipoCirugia
     * @param consecutivo
     * @param codigoEsquemaTarifario
     * @param codigoGrupoUvr
     * @param codigoInstitucion
     * @param finalidad
     * @param observaciones
     * @param viaCx
     * @param indBilateral
     * @param indViaAcceso
     * @param codigoEspecialidad
     * @param liquidarServicio
     * @param estado
     * @return
     * @throws SQLException
     */
    public int insertarSolicitudCxXServicioTransaccional (Connection con, 
                String numeroSolicitud,
                String codigoServicio,
                int codigoTipoCirugia,
                int consecutivo,
                int codigoEsquemaTarifario,
                double codigoGrupoUvr,
                int codigoInstitucion, 
				/*String autorizacion,*/
				int finalidad,
				String observaciones,
				String viaCx,
				String indBilateral,
				String indViaAcceso,
				int codigoEspecialidad,
				String liquidarServicio,
                String estado,
                String cubierto,
                Integer codigoContrato)
    {
        int insertado=0;
        
        
        
    	if (estado.equals(ConstantesBD.inicioTransaccion))
            if (!UtilidadBD.iniciarTransaccion(con))
                UtilidadBD.abortarTransaccion(con);
        
    	
        insertado= cxDao.insertarSolicitudCxXServicio(con, numeroSolicitud, codigoServicio, codigoTipoCirugia, consecutivo, 
        		codigoEsquemaTarifario, codigoGrupoUvr, codigoInstitucion, /*autorizacion,*/ finalidad, observaciones, viaCx, 
        		indBilateral, indViaAcceso, codigoEspecialidad, liquidarServicio, cubierto, codigoContrato);
        		
        if (insertado<=0)
        	if(estado.equals(ConstantesBD.finTransaccion))
        		UtilidadBD.abortarTransaccion(con);
        
        if (estado.equals(ConstantesBD.finTransaccion))
            UtilidadBD.finalizarTransaccion(con);
       
      
        
        return insertado;
    }
    
    /**
     * Método que inserta las solicitudes Cx X Articulo general y retorna el codigo Max insertado para
     * que lo utilicemos en el detalle
     * @param con
     * @param numeroSolicitud
     * @param cantidad
     * @param estado
     * @return
     * @throws SQLException
     */
    public int insertarSolicitudCxXArticuloGeneralTransaccional (      Connection con,
                                                                                                    String numeroSolicitud,
                                                                                                    int cantidad, 
                                                                                                    String estado) throws SQLException
    {
        int codigoInsertado=ConstantesBD.codigoNuncaValido;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estado.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            codigoInsertado= cxDao.insertarSolicitudCxXArticuloGeneral(con, numeroSolicitud, cantidad);
            if (codigoInsertado==ConstantesBD.codigoNuncaValido)
            {
                myFactory.abortTransaction(con);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estado.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return codigoInsertado;
    }
    
    
    /**
     * Método transaccional que inserta el detalle de los articulos (articulos_sol_cirugia) PARAMETRIZADOS para la solicitud de Cx
     * @param con
     * @param codigoTableArticulosSolCx
     * @param codigoArticulo
     * @param estado
     * @return
     * @throws SQLException
     */
    public boolean insertarDetalleXArticuloTransaccional(     Connection con,
                                                                                        int codigoTableArticulosSolCx,
                                                                                        int codigoArticulo,
                                                                                        String estado) throws SQLException
    {
        boolean insertado=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estado.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertado= cxDao.insertarDetalleXArticulo(con, codigoTableArticulosSolCx, codigoArticulo);
            if (!insertado)
            {
                myFactory.abortTransaction(con);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estado.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertado;
    }
    
    /**
     * Metodo transaccionalinserta el detalle de los articulos (articulos_sol_cirugia) NO parametrizados para la solicitud de Cx
     * @param con
     * @param codigoTableArticulosSolCx
     * @param nombreArticulo
     * @param estado
     * @return
     * @throws SQLException
     */
    public boolean insertarDetalleXOtrosArticulosTransaccional(     Connection con,
                                                                                                int codigoTableArticulosSolCx,
                                                                                                String nombreArticulo,
                                                                                                String estado) throws SQLException
    {
        boolean insertado=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estado.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertado= cxDao.insertarDetalleXOtrosArticulos(con, codigoTableArticulosSolCx, nombreArticulo);
            if (!insertado)
            {
                myFactory.abortTransaction(con);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estado.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertado;
    }
    
    
    /**
     * metodo transaccional que inserta los otros profesionales x sol cx
     * @param con
     * @param numeroSolicitud
     * @param codigoProfesional
     * @param codigoInstitucion
     * @param codigoTipoProfesional
     * @param codigoEspecialidad
     * @param estado
     * @return
     * @throws SQLException
     */
    public boolean insertarOtrosProfesionalesSolCxTransaccional(   Connection con,
                                                                                                    String numeroSolicitud,
                                                                                                    int codigoProfesional, 
                                                                                                    int codigoInstitucion,
                                                                                                    int codigoTipoProfesional,
                                                                                                    int codigoEspecialidad,
                                                                                                    String estado) throws SQLException
    {
        boolean insertado=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        if (estado.equals(ConstantesBD.inicioTransaccion))
        {
            if (!myFactory.beginTransaction(con))
            {
                myFactory.abortTransaction(con);
            }
        }
        try
        {
            insertado= cxDao.insertarOtrosProfesionalesSolCx(con, numeroSolicitud, codigoProfesional, codigoInstitucion, codigoTipoProfesional, codigoEspecialidad);
            if (!insertado)
            {
                myFactory.abortTransaction(con);
            }
        }
        catch (SQLException e)
        {
            myFactory.abortTransaction(con);
            throw e;
        }
      
        if (estado.equals(ConstantesBD.finTransaccion))
        {
            myFactory.endTransaction(con);
        }
        return insertado;
    }
    
    /**
     * metodo que inserta la anulacion de una solicitud cx 
     * @param con
     * @param numeroSolicitud
     * @param codigoMotivoAnulacion
     * @param loginUsuario
     * @param comentariosAnulacion
     * @return
     */
    public boolean  insertarAnulacionSolCx(  Connection con,
                                                                   String numeroSolicitud,
                                                                   int codigoMotivoAnulacion,
                                                                   String loginUsuario,
                                                                   String comentariosAnulacion
                                                                )
    {
        return cxDao.insertarAnulacionSolCx(con, numeroSolicitud, codigoMotivoAnulacion, loginUsuario, comentariosAnulacion);
    }
    
    /**
     * Metodo que inserta la respuesta a la solicitud cx a centro de costo externo
     * @param con
     * @param numeroSolicitud
     * @param resultados
     * @return
     */
    public boolean  insertarRespuestaSolCxCentroCostoExterno(  Connection con,
                                                                                                String numeroSolicitud,
                                                                                                String resultados
                                                                                             )
    {
        return cxDao.insertarRespuestaSolCxCentroCostoExterno(con, numeroSolicitud, resultados);
    }
    
    /**
     * consulta las peticiones que estan sin orden asociada
     * @param con
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public Vector cargarPeticionesSinOrdenAsociada(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas)
    {
        return cxDao.cargarPeticionesSinOrdenAsociada(con, codigoPaciente, codigosServiciosSeparadosPorComas);
    }
    
    /**
     * evalua si existe o no una orden de cx en estado medico solicitada y un(os) servicios 
     * @param con
     * @param codigoPaciente
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public Vector existeOrdenConEstadoMedicoPendienteYServicio(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas)
    {
        return cxDao.existeOrdenConEstadoMedicoPendienteYServicio(con, codigoPaciente, codigosServiciosSeparadosPorComas);
    }
    
    /**
     * Carga toda la información de los servicios de una solicitud de Cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
     public HashMap cargarServiciosXSolicitudCx(Connection con, String numeroSolicitud,boolean liquidados)
     {
         return cxDao.cargarServiciosXSolicitudCx(con, numeroSolicitud,liquidados);
     }
     
     /**
      * Metodo que carga el codigo de la peticion que esta asociada a una solicitud de cx, teniendo
      * en cuenta que:
      * 
      * Retorna 0= Es porque la solicitud cx se hizo a un centro costo externo entonces no existe peticion asociada
      * Retorna -1= Porque no encontro ese numero_solicitud en las solicitudes Cx
      * 
      * @param con
      * @param numeroSolicitud
      * @return
      * Retorna 0= Es porque la solicitud cx se hizo a un centro costo externo entonces no existe peticion asociada
      * Retorna -1= Porque no encontro ese numero_solicitud en las solicitudes Cx
      */
      public String cargarCodPeticionDadoNumSolCx(Connection con, String numeroSolicitud)
      {
          return cxDao.cargarCodPeticionDadoNumSolCx(con, numeroSolicitud);
      }
      
      /**
       * Metodo que carga los materiales especiales PARAMETRIZADOS de una solicitud cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarArticulosXSolicitudesCx(Connection con, String numeroSolicitud)
      {
          return cxDao.cargarArticulosXSolicitudesCx(con, numeroSolicitud);
      }
      
      /**
       * Metodo para cargar los otros materiales de una solicitud cx especifica 
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarOtrosArticulosXSolicitudesCx(Connection con, String numeroSolicitud)
      {
          return cxDao.cargarOtrosArticulosXSolicitudesCx(con, numeroSolicitud);
      }
      
      /**
       * Metodo para cargar los otros materiales de una solicitud cx especifica, este metodo será utilizado
       * para la consulta  
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarProfesionalesXSolicitudesCx(Connection con, String numeroSolicitud)
      {
          return cxDao.cargarProfesionalesXSolicitudesCx(con, numeroSolicitud);
      }
      
      /**
       * Borra los servicios de una solicitud Cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarServiciosXSolicitudCx(  Connection con,
                                                                              String numeroSolicitud   
                                                                            )
      {
          return cxDao.eliminarServiciosXSolicitudCx(con, numeroSolicitud);
      }
      
      /**
       * Borra la información general de los articulos x solicitud cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarArticulosXSolicitudCx(  Connection con,
                                                                             String numeroSolicitud   
                                                                           ) 
      {
          return cxDao.eliminarArticulosXSolicitudCx(con, numeroSolicitud);
      }
      
      /**
       * Borra los otros profesionales de una solicitud Cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarOtrosProfesionalesXSolicitudCx(  Connection con,
                                                                                            String numeroSolicitud   
                                                                                          )
      {
          return cxDao.eliminarOtrosProfesionalesXSolicitudCx(con, numeroSolicitud);
      }
      
      /**
       * Método que actualiza los datos de una cirugía de la orden
       * @param con
       * @param servicio
       * @param codigoTipoCirugia
       * @param numeroServicio
       * @param codigoEsquemaTarifario
       * @param grupoUvr
       * @param finalidad
       * @param observaciones
       * @param viaCx
       * @param indBilateral
       * @param indViaAcceso
       * @param codigoEspecialidad
       * @param liquidarServicio
       * @param consecutivoSolCx
       * @param estado
       * @return
       */
      public int modificarServiciosXSolicitudCx(
      		Connection con,int servicio,int codigoTipoCirugia,
      		int numeroServicio,int codigoEsquemaTarifario,double grupoUvr,
      		/*String autorizacion,*/int finalidad,String observaciones,String viaCx,
      		String indBilateral, String indViaAcceso, int codigoEspecialidad,
      		String liquidarServicio,String consecutivoSolCx,String estado)
      {
    	  return cxDao.modificarServiciosXSolicitudCx(con, servicio, codigoTipoCirugia, numeroServicio, codigoEsquemaTarifario, grupoUvr, /*autorizacion, */finalidad, observaciones, viaCx, indBilateral, indViaAcceso, codigoEspecialidad, liquidarServicio, consecutivoSolCx, estado);
      }
      
      /**
       * carga el listado de las solicitudes cx con estado HC Solicitada para un paciente particular, 
       * dentro de este mapa se encapsula otro mapa que contiene el detalle de los servicios (cx) 
       * pertenecientes a cada numero de solicitud consultado
       * @param con
       * @param codigoPaciente
       * @return
       */
      public HashMap listadoSolicitudesCxPendientes(Connection con, String codigoPaciente)
      {
          return cxDao.listadoSolicitudesCxPendientes(con, codigoPaciente);
      }
      
      /**
       * Método implementado para cargar el listado general de solicitudes Cx
       * Nota * Se está usando en Historia de Atenciones
       * @param con
       * @param cuenta
       * @return
       */
      public HashMap listadoGeneralSolicitudesCx(Connection con, int cuenta)
      {
      	return cxDao.listadoGeneralSolicitudesCx(con,cuenta);
      }
      
      /**
       * Método que carga los datos de anulación de una solicitud
       * anulada
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarAnulacionSolCx(Connection con,int numeroSolicitud)
      {
      	return cxDao.cargarAnulacionSolCx(con,numeroSolicitud);
      }
      
      /**
       * Metodo para cargar el codigo de la solicitud cx dado el numero de la solicitud y el servicio
       * los cuales son unique
       * @param con
       * @param numeroSolicitud
       * @param codigoServicio
       * @return
       */
      public String cargarCodigoSolCxDadoNumeroSolicitudYServicio(Connection con, String numeroSolicitud, String codigoServicio)
      {
    	  return cxDao.cargarCodigoSolCxDadoNumeroSolicitudYServicio(con, numeroSolicitud, codigoServicio);
      }
      
      /**
       * Carga el detalle de un acto quirurgico con sus correspondientes asocios los cuales ya han sido facturados
       * @param con
       * @param codigoFactura
       * @param codigoServicio
       * @param codigoInstitucion
       * @return
       */
      public HashMap cargarDetalleCxAsociadasFactura(Connection con, String codigoFactura, String codigoServicio, int codigoInstitucion)
      {
    	  return cxDao.cargarDetalleCxAsociadasFactura(con, codigoFactura, codigoServicio, codigoInstitucion);
      }
      
      /**
       * 
       * @param con
       * @param codigoServicio
       * @param numeroSolicitud
       * @return
       */
      public static Vector<String> obtenerConsecutivoSolCx(Connection con, String numeroSolicitud)
      {
    	  return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().obtenerConsecutivoSolCx(con, numeroSolicitud);
      }
      
      /**
       * 
       * @param con
       * @param codigoServicio
       * @param numeroSolicitud
       * @return
       */
      public static int obtenerConsecutivoServicioCx(Connection con, int codigoServicio, String numeroSolicitud) throws IPSException
      {
    	  return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().obtenerConsecutivoServicioCx(con, codigoServicio, numeroSolicitud);
      }
      
      /**
       * Método que realiza la eliminación de una cirugia
       * @param con
       * @param codigoCirugia
       * @return
       */
      public int eliminarCirugiaSolicitud(Connection con,String codigoCirugia)
      {
    	  return cxDao.eliminarCirugiaSolicitud(con, codigoCirugia);
      }
      
      /**
       * Método implementado para actualizar el consecutivo de un cirugia
       * @param con
       * @param codigoCirugia
       * @param consecutivo
       * @return
       */
      public int actualizarConsecutivoCirugia(Connection con,String codigoCirugia,int consecutivo)
      {
    	  HashMap campos = new HashMap();
    	  campos.put("codigoCirugia",codigoCirugia);
    	  campos.put("consecutivo",consecutivo);
    	  return cxDao.actualizarConsecutivoCirugia(con, campos);
      }
      
      /**
       * 
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public static boolean existeServicioEnSolicitudCx(String numeroSolicitud)
      {
    	  if(UtilidadTexto.isNumber(numeroSolicitud))
    	  {	  
	    	  Connection con=UtilidadBD.abrirConexion();
	    	  boolean b= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().existeServicioEnSolicitudCx(con, Integer.parseInt(numeroSolicitud));
	    	  UtilidadBD.closeConnection(con);
	    	  return b;
    	  }
    	  else
    		  return false;
      }
      
      
      /**
       * Método implementado para realizar la actualización de los datos del encabezado de la solicitud
       * @param con
       * @param campos
       * @return
       */
      public static int actualizarEncabezadoSolicitudCx(Connection con,String fechaInicialCx,String horaInicialCx,String fechaFinalCx,String horaFinalCx,String fechaIngresoSala,String horaIngresoSala,String fechaSalidaSala,String horaSalidaSala,String liquidarAnestesia,String numeroSolicitud)
      {
    	  HashMap campos = new HashMap();
    	  campos.put("fechaInicialCx", fechaInicialCx);
    	  campos.put("horaInicialCx", horaInicialCx);
    	  campos.put("fechaFinalCx", fechaFinalCx);
    	  campos.put("horaFinalCx", horaFinalCx);
    	  campos.put("fechaIngresoSala", fechaIngresoSala);
    	  campos.put("horaIngresoSala", horaIngresoSala);
    	  campos.put("fechaSalidaSala", fechaSalidaSala);
    	  campos.put("horaSalidaSala", horaSalidaSala);
    	  campos.put("liquidarAnestesia", liquidarAnestesia);
    	  campos.put("numeroSolicitud",numeroSolicitud);
    	  return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().actualizarEncabezadoSolicitudCx(con, campos);
      }
      
      /**
       * Método para obtener el indicativo de cargo de una solicitud de cirugía
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public static String obtenerIndicativoCargoSolicitud(Connection con,String numeroSolicitud)
      {
    	  return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudesCxDao().obtenerIndicativoCargoSolicitud(con, numeroSolicitud);
      }
     

	/**
	 * @return the codigoPeticion
	 */
	public String getCodigoPeticion() {
		return codigoPeticion;
	}

	/**
	 * @param codigoPeticion the codigoPeticion to set
	 */
	public void setCodigoPeticion(String codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}

	/**
	 * @return the fechaFinalCx
	 */
	public String getFechaFinalCx() {
		return fechaFinalCx;
	}

	/**
	 * @param fechaFinalCx the fechaFinalCx to set
	 */
	public void setFechaFinalCx(String fechaFinalCx) {
		this.fechaFinalCx = fechaFinalCx;
	}

	/**
	 * @return the fechaInicialCx
	 */
	public String getFechaInicialCx() {
		return fechaInicialCx;
	}

	/**
	 * @param fechaInicialCx the fechaInicialCx to set
	 */
	public void setFechaInicialCx(String fechaInicialCx) {
		this.fechaInicialCx = fechaInicialCx;
	}

	/**
	 * @return the horaFinalCx
	 */
	public String getHoraFinalCx() {
		return horaFinalCx;
	}

	/**
	 * @param horaFinalCx the horaFinalCx to set
	 */
	public void setHoraFinalCx(String horaFinalCx) {
		this.horaFinalCx = horaFinalCx;
	}

	/**
	 * @return the horaInicialCx
	 */
	public String getHoraInicialCx() {
		return horaInicialCx;
	}

	/**
	 * @param horaInicialCx the horaInicialCx to set
	 */
	public void setHoraInicialCx(String horaInicialCx) {
		this.horaInicialCx = horaInicialCx;
	}

	/**
	 * @return the indQx
	 */
	public String getIndQx() {
		return indQx;
	}

	/**
	 * @param indQx the indQx to set
	 */
	public void setIndQx(String indQx) {
		this.indQx = indQx;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
    

	/**
	 * Método para consultar los datos del encabezado de la solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 */
	  public void cargarEncabezadoSolicitudCx(Connection con,String numeroSolicitud)
	  {
		  HashMap<String, Object> resultados = cxDao.cargarEncabezadoSolicitudCx(con, numeroSolicitud);
		  
		  if(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)>0)
		  {
			  this.numeroSolicitud = numeroSolicitud;
			  this.codigoPeticion = resultados.get("codigoPeticion").toString();
			  this.indQx = resultados.get("indQx").toString();
			  this.fechaInicialCx = resultados.get("fechaInicialCx").toString();
			  this.fechaFinalCx = resultados.get("fechaFinalCx").toString();
			  this.horaInicialCx = resultados.get("horaInicialCx").toString();
			  this.horaFinalCx = resultados.get("horaFinalCx").toString();
			  this.fechaIngresoSala = resultados.get("fechaIngresoSala").toString();
			  this.fechaSalidaSala = resultados.get("fechaSalidaSala").toString();
			  this.horaIngresoSala = resultados.get("horaIngresoSala").toString();
			  this.horaSalidaSala = resultados.get("horaSalidaSala").toString();
			  this.liquidarAnestesia = resultados.get("liquidarAnestesia").toString();
			  this.codigoSalidaSalaPaciente = resultados.get("salidaSalaPaciente").toString();
			  this.codigoSalidaPacienteCc = resultados.get("salidaPacienteCc").toString();
			  this.descripcionSalidaPacienteCc = resultados.get("descripcionSalidaPacienteCc").toString();  
		  }
	  }

	  /**
	 * Método para consultar los datos del encabezado de la solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 */
	  public void cargarEncabezadoSolicitudCx(String numeroSolicitud)
	  {
		  Connection con=UtilidadBD.abrirConexion();
		  HashMap<String, Object> resultados = cxDao.cargarEncabezadoSolicitudCx(con, numeroSolicitud);
		  UtilidadBD.closeConnection(con);
		  
		  if(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)>0)
		  {
			  this.numeroSolicitud = numeroSolicitud;
			  this.codigoPeticion = resultados.get("codigoPeticion").toString();
			  this.indQx = resultados.get("indQx").toString();
			  this.fechaInicialCx = resultados.get("fechaInicialCx").toString();
			  this.fechaFinalCx = resultados.get("fechaFinalCx").toString();
			  this.horaInicialCx = resultados.get("horaInicialCx").toString();
			  this.horaFinalCx = resultados.get("horaFinalCx").toString();
			  this.fechaIngresoSala = resultados.get("fechaIngresoSala").toString();
			  this.fechaSalidaSala = resultados.get("fechaSalidaSala").toString();
			  this.horaIngresoSala = resultados.get("horaIngresoSala").toString();
			  this.horaSalidaSala = resultados.get("horaSalidaSala").toString();
			  this.liquidarAnestesia = resultados.get("liquidarAnestesia").toString();
		  }
	  }
		  
	/**
	 * @return the fechaIngresoSala
	 */
	public String getFechaIngresoSala() {
		return fechaIngresoSala;
	}

	/**
	 * @param fechaIngresoSala the fechaIngresoSala to set
	 */
	public void setFechaIngresoSala(String fechaIngresoSala) {
		this.fechaIngresoSala = fechaIngresoSala;
	}

	/**
	 * @return the fechaSalidaSala
	 */
	public String getFechaSalidaSala() {
		return fechaSalidaSala;
	}

	/**
	 * @param fechaSalidaSala the fechaSalidaSala to set
	 */
	public void setFechaSalidaSala(String fechaSalidaSala) {
		this.fechaSalidaSala = fechaSalidaSala;
	}

	/**
	 * @return the horaIngresoSala
	 */
	public String getHoraIngresoSala() {
		return horaIngresoSala;
	}

	/**
	 * @param horaIngresoSala the horaIngresoSala to set
	 */
	public void setHoraIngresoSala(String horaIngresoSala) {
		this.horaIngresoSala = horaIngresoSala;
	}

	/**
	 * @return the horaSalidaSala
	 */
	public String getHoraSalidaSala() {
		return horaSalidaSala;
	}

	/**
	 * @param horaSalidaSala the horaSalidaSala to set
	 */
	public void setHoraSalidaSala(String horaSalidaSala) {
		this.horaSalidaSala = horaSalidaSala;
	}

	/**
	 * @return the liquidarAnestesia
	 */
	public String getLiquidarAnestesia() {
		return liquidarAnestesia;
	}

	/**
	 * @param liquidarAnestesia the liquidarAnestesia to set
	 */
	public void setLiquidarAnestesia(String liquidarAnestesia) {
		this.liquidarAnestesia = liquidarAnestesia;
	}

	/**
	 * @return the duracionCx
	 */
	public String getDuracionCx() {
		return duracionCx;
	}

	/**
	 * @param duracionCx the duracionCx to set
	 */
	public void setDuracionCx(String duracionCx) {
		this.duracionCx = duracionCx;
	}

	/**
	 * @return the codigoSalidaPacienteCc
	 */
	public String getCodigoSalidaPacienteCc() {
		return codigoSalidaPacienteCc;
	}

	/**
	 * @param codigoSalidaPacienteCc the codigoSalidaPacienteCc to set
	 */
	public void setCodigoSalidaPacienteCc(String codigoSalidaPacienteCc) {
		this.codigoSalidaPacienteCc = codigoSalidaPacienteCc;
	}

	/**
	 * @return the codigoSalidaSalaPaciente
	 */
	public String getCodigoSalidaSalaPaciente() {
		return codigoSalidaSalaPaciente;
	}

	/**
	 * @param codigoSalidaSalaPaciente the codigoSalidaSalaPaciente to set
	 */
	public void setCodigoSalidaSalaPaciente(String codigoSalidaSalaPaciente) {
		this.codigoSalidaSalaPaciente = codigoSalidaSalaPaciente;
	}

	/**
	 * @return the descripcionSalidaPacienteCc
	 */
	public String getDescripcionSalidaPacienteCc() {
		return descripcionSalidaPacienteCc;
	}

	/**
	 * @param descripcionSalidaPacienteCc the descripcionSalidaPacienteCc to set
	 */
	public void setDescripcionSalidaPacienteCc(String descripcionSalidaPacienteCc) {
		this.descripcionSalidaPacienteCc = descripcionSalidaPacienteCc;
	}
	
	 /**
     * 
     * Este Método se encarga de consultar los códigos de los servicios 
     * asociados a una solicitud de cirugía
     * 
     * @param Connection con, String numeroSolicitud
     * @return ArrayList<Servicios>
     * @author, Angela Maria Aguirre
     *
     */
    public ArrayList<DtoServicios> buscarServiciosSolicitudQx(Connection con, String numeroSolicitud, int codigoTarifario){
  	   return DaoFactory.getDaoFactory(
  			   System.getProperty("TIPOBD")).getSolicitudesCxDao().buscarServiciosSolicitudQx(
  					   con, numeroSolicitud, codigoTarifario);
    }
    
    
    /**
     * 
     * Este Método se encarga de consultar los códigos de los artículos 
     * asociados a una solicitud de cirugía
     * 
     * @param Connection con, String numeroSolicitud
     * @return ArrayList<Servicios>
     * @author, Angela Maria Aguirre
     *
     */
    public static ArrayList<Articulo> buscarArticulosSolicitudQx(Connection con, String numeroSolicitud){
    	return DaoFactory.getDaoFactory(
   			   System.getProperty("TIPOBD")).getSolicitudesCxDao().buscarArticulosSolicitudQx(
   					   con, numeroSolicitud);
    }
}