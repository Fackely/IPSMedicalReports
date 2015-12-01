/*
 * @(#)OracleSolicitudesCxDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.ValoresPorDefecto;

import com.princetonsa.dao.SolicitudesCxDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudesCxDao;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.Articulo;

/**
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para las solicitudes cx
 *
 * @version 1.0, Nov  09 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleSolicitudesCxDao implements SolicitudesCxDao
{
    
      /**
     * inserta las solicitudes Cx X Articulo general 
     */
     private static String insertarSolicitudCxXArticuloGeneralStr="INSERT INTO articulos_sol_cirugia(codigo, numero_solicitud, cantidad) VALUES (seq_articulos_sol_cx.nextval, ?, ?)";
    
     /**
      * Cadena para cargar la info de los articulos x solicitud cx
      */
     private static String cargarArticulosXSolicitudesCxStr="SELECT " +
                                                                                     "va.codigo AS codigoArticulo, " +
                                                                                     "coalesce(va.descripcion,'') ||'-CONC:'|| " +
                                                                                     "coalesce(va.concentracion,'') ||'-F.F:'|| " +
                                                                                     "coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'') ||'-'|| " +
                                                                                     "CASE WHEN va.es_pos= "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' " +
                                                                                     "ELSE 'NOPOS' END AS descripcionArticulo, " +
                                                                                     "getNomUnidadMedida(va.unidad_medida) AS unidadMedidaArticulo, " +
                                                                                     "ascx.cantidad AS cantidadDespachadaArticulo, " +
                                                                                     "'' AS autorizacionArticulo, " +
                                                                                     "'false' AS fueEliminadoArticulo, " +
                                                                                     "CASE WHEN va.es_pos= '" +ValoresPorDefecto.getValorTrueParaConsultas()+"' THEN 'POS' " +
                                                                                     "ELSE 'NOPOS' END AS tipoPosArticulo " +
                                                                                     "FROM " +
                                                                                     "view_articulos va, " +
                                                                                     "articulos_sol_cirugia ascx, " +
                                                                                     "det_articulo_sol_cx dasc " +
                                                                                     "WHERE " +
                                                                                     "ascx.codigo=dasc.articulos_sol_cirugia " +
                                                                                     "AND va.codigo=dasc.articulo " +
                                                                                     "AND va.estado='" +ValoresPorDefecto.getValorTrueParaConsultas()+"' "+
                                                                                     "AND ascx.numero_solicitud=?";
     
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
                                                                     )
    {
        return SqlBaseSolicitudesCxDao.insertarSolicitudCxGeneral1(con, numeroSolicitud, codigoPeticion, esCentroCostoSolicitadoExterno, subCuenta, indicativoQx, fechaInicialCx, horaInicialCx, fechaFinalCx, horaFinalCx, fechaIngresoSala, horaIngresoSala, fechaSalidaSala, horaSalidaSala, liquidarAnestesia, duracionCx);
    }
    
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
         )
    {
        return SqlBaseSolicitudesCxDao.insertarSolicitudCxXServicio(con, numeroSolicitud, codigoServicio, 
        		codigoTipoCirugia, consecutivo, codigoEsquemaTarifario, codigoGrupoUvr, codigoInstitucion, /*autorizacion,*/ 
        		finalidad, observaciones, viaCx, indBilateral, indViaAcceso, codigoEspecialidad, liquidarServicio, cubierto, codigoContrato);
    }
    
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
                                                                         )
    {
        return SqlBaseSolicitudesCxDao.insertarSolicitudCxXArticuloGeneral(con, numeroSolicitud, cantidad, insertarSolicitudCxXArticuloGeneralStr);
    }
    
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
                                                                  )
    {
        return SqlBaseSolicitudesCxDao.insertarDetalleXArticulo(con, codigoTableArticulosSolCx, codigoArticulo);
    }
    
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
                                                                           )
    {
        return SqlBaseSolicitudesCxDao.insertarDetalleXOtrosArticulos(con, codigoTableArticulosSolCx, nombreArticulo);
    }
    
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
                                                                               )
    {
        return SqlBaseSolicitudesCxDao.insertarOtrosProfesionalesSolCx(con, numeroSolicitud, codigoProfesional, codigoInstitucion, codigoTipoProfesional, codigoEspecialidad);
    }
    
    /**
     * consulta las peticiones que estan sin orden asociada
     * @param con
     * @param codigosServiciosSeparadosPorComas
     * @return
     */
    public Vector cargarPeticionesSinOrdenAsociada(Connection con, int codigoPaciente, String codigosServiciosSeparadosPorComas)
    {
        return SqlBaseSolicitudesCxDao.cargarPeticionesSinOrdenAsociada(con, codigoPaciente, codigosServiciosSeparadosPorComas);
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
        return SqlBaseSolicitudesCxDao.existeOrdenConEstadoMedicoPendienteYServicio(con, codigoPaciente, codigosServiciosSeparadosPorComas);
    }
    
    /**
     * Carga toda la información de los servicios de una solicitud de Cx
     * @param con
     * @param numeroSolicitud
     * @return
     */
     public HashMap cargarServiciosXSolicitudCx(Connection con, String numeroSolicitud,boolean liquidados)
     {
         return SqlBaseSolicitudesCxDao.cargarServiciosXSolicitudCx(con, numeroSolicitud,liquidados);
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
          return SqlBaseSolicitudesCxDao.cargarCodPeticionDadoNumSolCx(con, numeroSolicitud);
      }
      
      /**
       * Metodo que carga los materiales especiales PARAMETRIZADOS de una solicitud cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap cargarArticulosXSolicitudesCx(Connection con, String numeroSolicitud)
      {
          return SqlBaseSolicitudesCxDao.cargarArticulosXSolicitudesCx(con, numeroSolicitud, cargarArticulosXSolicitudesCxStr);
      }
      
      /**
       * Metodo para cargar los otros materiales de una solicitud cx especifica 
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public  HashMap cargarOtrosArticulosXSolicitudesCx(Connection con, String numeroSolicitud)
      {
          return SqlBaseSolicitudesCxDao.cargarOtrosArticulosXSolicitudesCx(con, numeroSolicitud);
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
          return SqlBaseSolicitudesCxDao.cargarProfesionalesXSolicitudesCx(con, numeroSolicitud);
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
          return SqlBaseSolicitudesCxDao.eliminarServiciosXSolicitudCx(con, numeroSolicitud);
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
          return SqlBaseSolicitudesCxDao.eliminarArticulosXSolicitudCx(con, numeroSolicitud);
      }
      
      /**
       * Borra los servicios de una solicitud Cx
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean  eliminarOtrosProfesionalesXSolicitudCx(  Connection con,
                                                                                            String numeroSolicitud   
                                                                                          ) 
      {
          return SqlBaseSolicitudesCxDao.eliminarOtrosProfesionalesXSolicitudCx(con, numeroSolicitud);
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
          return SqlBaseSolicitudesCxDao.insertarAnulacionSolCx(con, numeroSolicitud, codigoMotivoAnulacion, loginUsuario, comentariosAnulacion);
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
          return SqlBaseSolicitudesCxDao.insertarRespuestaSolCxCentroCostoExterno(con, numeroSolicitud, resultados);
      }
      
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
      		String liquidarServicio,String consecutivoSolCx,String estado)
      {
    	  return SqlBaseSolicitudesCxDao.modificarServiciosXSolicitudCx(con, servicio, codigoTipoCirugia, numeroServicio, codigoEsquemaTarifario, grupoUvr, /*autorizacion, */finalidad, observaciones, viaCx, indBilateral, indViaAcceso, codigoEspecialidad, liquidarServicio, consecutivoSolCx, estado);
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
          return SqlBaseSolicitudesCxDao.listadoSolicitudesCxPendientes(con, codigoPaciente);
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
      	return SqlBaseSolicitudesCxDao.listadoGeneralSolicitudesCx(con,cuenta);
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
      	return SqlBaseSolicitudesCxDao.cargarAnulacionSolCx(con,numeroSolicitud);
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
    	  return SqlBaseSolicitudesCxDao.cargarCodigoSolCxDadoNumeroSolicitudYServicio(con, numeroSolicitud, codigoServicio);
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
    	  return SqlBaseSolicitudesCxDao.cargarDetalleCxAsociadasFactura(con, codigoFactura, codigoServicio, codigoInstitucion);
      }
      
      /**
       * 
       * @param con
       * @param subCuenta
       * @param numeroSolicitud
       * @return
       */
      public boolean actualizarSubCuentaCx(Connection con, double subCuenta, String numeroSolicitud ) throws BDException
      {
    	  return SqlBaseSolicitudesCxDao.actualizarSubCuentaCx(con, subCuenta, numeroSolicitud);
      }
      
      /**
       * 
       * @param con
       * @param codigoServicio
       * @param numeroSolicitud
       * @return
       */
      public Vector<String> obtenerConsecutivoSolCx(Connection con, String numeroSolicitud)
      {
    	  return SqlBaseSolicitudesCxDao.obtenerConsecutivoSolCx(con, numeroSolicitud);
      }
      
      
      /**
       * 
       * @param con
       * @param codigoServicio
       * @param numeroSolicitud
       * @return
       */
      public int obtenerConsecutivoServicioCx(Connection con, int codigoServicio, String numeroSolicitud) throws BDException
      {
    	  return SqlBaseSolicitudesCxDao.obtenerConsecutivoServicioCx(con, codigoServicio, numeroSolicitud);
      }
      
      /**
       * Método que realiza la eliminación de una cirugia
       * @param con
       * @param codigoCirugia
       * @return
       */
      public int eliminarCirugiaSolicitud(Connection con,String codigoCirugia)
      {
    	  return SqlBaseSolicitudesCxDao.eliminarCirugiaSolicitud(con, codigoCirugia);
      }
      
      /**
       * Método implementado para actualizar el consecutivo de un cirugia
       * @param con
       * @param campos
       * @return
       */
      public int actualizarConsecutivoCirugia(Connection con,HashMap campos)
      {
    	  return SqlBaseSolicitudesCxDao.actualizarConsecutivoCirugia(con, campos);
      }
      
      /**
       * 
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public boolean existeServicioEnSolicitudCx(Connection con, int numeroSolicitud)
      {
    	  return SqlBaseSolicitudesCxDao.existeServicioEnSolicitudCx(con, numeroSolicitud);
      }
      
      /**
       * Método implementado para consultar el encabezado de a solicitud de cirugías
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public HashMap<String,Object> cargarEncabezadoSolicitudCx(Connection con,String numeroSolicitud)
      {
    	  return SqlBaseSolicitudesCxDao.cargarEncabezadoSolicitudCx(con, numeroSolicitud);
      }
      
      /**
       * Método implementado para realizar la actualización de los datos del encabezado de la solicitud
       * @param con
       * @param campos
       * @return
       */
      public int actualizarEncabezadoSolicitudCx(Connection con,HashMap campos)
      {
    	  return SqlBaseSolicitudesCxDao.actualizarEncabezadoSolicitudCx(con, campos);
      }
      
      /**
       * Método para obtener el indicativo de cargo de una solicitud de cirugía
       * @param con
       * @param numeroSolicitud
       * @return
       */
      public String obtenerIndicativoCargoSolicitud(Connection con,String numeroSolicitud)
      {
    	  return SqlBaseSolicitudesCxDao.obtenerIndicativoCargoSolicitud(con, numeroSolicitud);
      }
      
      /**
       * Método para actualizar la finalidad del servicio cx
       * @param con
       * @param campos
       * @return
       */
      public boolean actualizarFinalidadServicioCx(Connection con,HashMap campos)
      {
    	  return SqlBaseSolicitudesCxDao.actualizarFinalidadServicioCx(con, campos);
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
    	  return SqlBaseSolicitudesCxDao.buscarServiciosSolicitudQx(con, numeroSolicitud,codigoTarifario);
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
      public ArrayList<Articulo> buscarArticulosSolicitudQx(Connection con, String numeroSolicitud){
    	  return SqlBaseSolicitudesCxDao.buscarArticulosSolicitudQx(con, numeroSolicitud);
      }

}