/*
 * @(#)SolicitudesCxDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.Articulo;


/**
 *  Interfaz para el acceder a la fuente de datos de las solicitudes cx
 *
 * @version 1.0, Nov 09 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface SolicitudesCxDao 
{
    /**
     * Metodo que inserta la solicitud Cx General, en caso de que sea centro costo esterno entonces no se
     * hace relación a ninguna petición, (peticion=null)
     * @param con
     * @param numeroSolicitud
     * @param codigoPeticion
     * @param esCentroCostoSolicitadoExterno
     * @return
     */
    public boolean  insertarSolicitudCxGeneral1(  Connection con,
	                                                String numeroSolicitud,
	                                                String codigoPeticion,
	                                                boolean esCentroCostoSolicitadoExterno,
	                                                double subCuenta,
	                                                String indicativoQx,
	                                                String fechaInicialCx,
	                                                String horaInicialCx,
	                                                String fechaFinalCx,
	                                                String horaFinalCx,
	                                                String fechaIngresoSala,
	                                                String horaIngresoSala,
	                                                String fechaSalidaSala,
	                                                String horaSalidaSala,
	                                                String liquidarAnestesia,
	                                                String duracionCx
                                                                     );
    
    /**
     * Inserta las solicitud Cx X servicio, 
     * entonces se insertan como null.
     * @param con
     * @param numeroSolicitud
     * @param codigoServicio
     * @param codigoTipoCirugia
     * @param consecutivo
     * @param codigoEsquemaTarifario
     * @param codigoGrupoUvr
     * @param codigoInstitucion
     * @param autorizacion
     * @param finalidad
     * @param observaciones
     * @param viaCx
     * @param indBilateral
     * @param indViaAcceso
     * @param codigoEspecialidad
     * @param liquidarServicio
     * @return
     */
    public int  insertarSolicitudCxXServicio(    Connection con,
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
             String cubierto,
             Integer codigoContrato
         );
    
    /**
     * Método que inserta las solicitudes Cx X Articulo general y retorna el codigo Max insertado para
     * que lo utilicemos en el detalle
     * @param con
     * @param numeroSolicitud
     * @param cantidad
     * @return
     */
    public int  insertarSolicitudCxXArticuloGeneral(  Connection con,
                                                                            String numeroSolicitud,
                                                                            int cantidad
                                                                         );
    
    /**
     * inserta el detalle de los articulos (articulos_sol_cirugia) PARAMETRIZADOS para la solicitud de Cx
     * @param con
     * @param codigoTableArticulosSolCx
     * @param codigoArticulo
     * @return
     */
    public boolean  insertarDetalleXArticulo(  Connection con,
                                                                    int codigoTableArticulosSolCx,
                                                                    int codigoArticulo
                                                                  );
    
    /**
     * inserta el detalle de los articulos (articulos_sol_cirugia) NO parametrizados para la solicitud de Cx
     * @param con
     * @param codigoTableArticulosSolCx
     * @param nombreArticulo
     * @return
     */
    public boolean  insertarDetalleXOtrosArticulos(    Connection con,
                                                                              int codigoTableArticulosSolCx,
                                                                              String nombreArticulo
                                                                           );
    
    /**
     * metodo que inserta los otros profesionales x sol cx
     * @param con
     * @param numeroSolicitud
     * @param codigoProfesional
     * @param codigoInstitucion
     * @param codigoTipoProfesional
     * @param codigoEspecialidad
     * @return
     */
    public boolean  insertarOtrosProfesionalesSolCx(   Connection con,
                                                                                  String numeroSolicitud,
                                                                                  int codigoProfesional, 
                                                                                  int codigoInstitucion,
                                                                                  int codigoTipoProfesional,
                                                                                  int codigoEspecialidad
                                                                               );

    /**
     * consulta las peticiones que estan sin orden asociada
     * @param con
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public Vector cargarPeticionesSinOrdenAsociada(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas);
    
    /**
     * evalua si existe o no una orden de cx en estado medico solicitada y un(os) servicios 
     * @param con
     * @param codigoPaciente
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public Vector existeOrdenConEstadoMedicoPendienteYServicio(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas);
    
    /**
     * Carga toda la información de los servicios de una solicitud de Cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
     public HashMap cargarServiciosXSolicitudCx(Connection con, String numeroSolicitud,boolean liquidados);
     
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
      public String cargarCodPeticionDadoNumSolCx(Connection con, String numeroSolicitud);
      
      /**
       * Metodo que carga los materiales especiales PARAMETRIZADOS de una solicitud cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarArticulosXSolicitudesCx(Connection con, String numeroSolicitud);
      
      /**
       * Metodo para cargar los otros materiales de una solicitud cx especifica 
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public  HashMap cargarOtrosArticulosXSolicitudesCx(Connection con, String numeroSolicitud);
      
      /**
       * Metodo para cargar los otros materiales de una solicitud cx especifica, este metodo será utilizado
       * para la consulta  
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarProfesionalesXSolicitudesCx(Connection con, String numeroSolicitud);
      
      /**
       * Borra los servicios de una solicitud Cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarServiciosXSolicitudCx(  Connection con,
                                                                              String numeroSolicitud   
                                                                            );
      
      /**
       * Borra la información general de los articulos x solicitud cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarArticulosXSolicitudCx(  Connection con,
                                                                             String numeroSolicitud   
                                                                           ) ;
      
      /**
       * Borra los servicios de una solicitud Cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarOtrosProfesionalesXSolicitudCx(  Connection con,
                                                                                            String numeroSolicitud   
                                                                                          );  
      
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
                                                                  ) ; 
      
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
                                                                                               );  
      
      /**
       * Método que actualiza los datos de una cirugía de la orden
       * @param con
       * @param servicio
       * @param codigoTipoCirugia
       * @param numeroServicio
       * @param codigoEsquemaTarifario
       * @param grupoUvr
       * @param autorizacion
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
      		String liquidarServicio,String consecutivoSolCx,String estado);
      
      /**
       * carga el listado de las solicitudes cx con estado HC Solicitada para un paciente particular, 
       * dentro de este mapa se encapsula otro mapa que contiene el detalle de los servicios (cx) 
       * pertenecientes a cada numero de solicitud consultado
       * @param con
       * @param codigoPaciente
       * @return
       */
      public HashMap listadoSolicitudesCxPendientes(Connection con, String codigoPaciente);
      
      /**
       * Método implementado para cargar el listado general de solicitudes Cx
       * Nota * Se está usando en Historia de Atenciones
       * @param con
       * @param cuenta
       * @return
       */
      public HashMap listadoGeneralSolicitudesCx(Connection con, int cuenta);
      
      /**
       * Método que carga los datos de anulación de una solicitud
       * anulada
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarAnulacionSolCx(Connection con,int numeroSolicitud);
 
      /**
       * Metodo para cargar el codigo de la solicitud cx dado el numero de la solicitud y el servicio
       * los cuales son unique
       * @param con
       * @param numeroSolicitud
       * @param codigoServicio
       * @return
       */
      public String cargarCodigoSolCxDadoNumeroSolicitudYServicio(Connection con, String numeroSolicitud, String codigoServicio);
      
      /**
       * Carga el detalle de un acto quirurgico con sus correspondientes asocios los cuales ya han sido facturados
       * @param con
       * @param codigoFactura
       * @param codigoServicio
       * @param codigoInstitucion
       * @return
       */
      public HashMap cargarDetalleCxAsociadasFactura(Connection con, String codigoFactura, String codigoServicio, int codigoInstitucion);
      
      /**
       * 
       * @param con
       * @param subCuenta
       * @param numeroSolicitud
       * @return
       */
      public boolean actualizarSubCuentaCx(Connection con, double subCuenta, String numeroSolicitud ) throws BDException;
      
      /**
       * 
       * @param con
       * @param codigoServicio
       * @param numeroSolicitud
       * @return
       */
      public Vector<String> obtenerConsecutivoSolCx(Connection con, String numeroSolicitud);
      
      /**
       * 
       * @param con
       * @param codigoServicio
       * @param numeroSolicitud
       * @return
       */
      public int obtenerConsecutivoServicioCx(Connection con, int codigoServicio, String numeroSolicitud) throws BDException;
      
      /**
       * Método que realiza la eliminación de una cirugia
       * @param con
       * @param codigoCirugia
       * @return
       */
      public int eliminarCirugiaSolicitud(Connection con,String codigoCirugia);
      
      /**
       * Método implementado para actualizar el consecutivo de un cirugia
       * @param con
       * @param campos
       * @return
       */
      public int actualizarConsecutivoCirugia(Connection con,HashMap campos);

      /**
       * 
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean existeServicioEnSolicitudCx(Connection con, int numeroSolicitud);
      
      /**
       * Método implementado para consultar el encabezado de a solicitud de cirugías
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap<String,Object> cargarEncabezadoSolicitudCx(Connection con,String numeroSolicitud);
      
      /**
       * Método implementado para realizar la actualización de los datos del encabezado de la solicitud
       * @param con
       * @param campos
       * @return
       */
      public int actualizarEncabezadoSolicitudCx(Connection con,HashMap campos);
      
      /**
       * Método para obtener el indicativo de cargo de una solicitud de cirugía
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public String obtenerIndicativoCargoSolicitud(Connection con,String numeroSolicitud);
      
      /**
       * Método para actualizar la finalidad del servicio cx
       * @param con
       * @param campos
       * @return
       */
      public boolean actualizarFinalidadServicioCx(Connection con,HashMap campos);
      
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
      public ArrayList<DtoServicios> buscarServiciosSolicitudQx(Connection con, String numeroSolicitud, int codigoTarifario);
      
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
      public ArrayList<Articulo> buscarArticulosSolicitudQx(Connection con, String numeroSolicitud);
}