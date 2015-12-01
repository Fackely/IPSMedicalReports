package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoEnvioAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoRespAutorizacion;

public interface AutorizacionesDao {
	
	/**
	 * 
	 * @param con
	 * @param DtoAutorizacion
	 * @return el valor del codigo si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion) ;
	
	/**
	 * 
	 * @param con
	 * @param DtoDetAutorizacion
	 * @return el valor del codigo si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarDetalleAutorizacion(Connection con, DtoDetAutorizacion dtoDetAutorizacion) ;
	
	/**
	 * 
	 * @param con
	 * @param DtoEnvioAutorizacion
	 * @return el valor del codigo_pk si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarEnvioAutorizacion(Connection con, DtoEnvioAutorizacion dtoEnvAutorizacion) ;
	
	/**
	 * 
	 * @param con
	 * @param DtoDiagAutorizacion
	 * @return el valor del codigo si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarDiagnosticoAutorizacion(Connection con, DtoDiagAutorizacion dtoDiagAutorizacion) ;
	
	/**
	 * 
	 * @param con
	 * @param DtoAdjAutorizacion
	 * @return el valor del codigo si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarAdjuntoAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion) ;
	
	/**
	 * 
	 * @param con
	 * @param DtoRespAutorizacion
	 * @return el valor del det_autorizacion si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarRespuestaAutorizacion(Connection con, DtoRespAutorizacion dtoRespAutorizacion);
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return DtoDetAutorizacion
	 * @ 
	 */
	public DtoAutorizacion cargarAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return DtoDetAutorizacion
	 * @ 
	 */
	public ArrayList<DtoDetAutorizacion> cargarDetallesAutorizacion(Connection con, HashMap parametros) ;
		
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<HashMap<String,Object>>
	 * @ 
	 */
	public ArrayList<HashMap<String,Object>> getListadoPersonaSolicita(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int updateTipoSerSolAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int updateObservacionAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int updateAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int updateDiagPPAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int deleteDiagRAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int deleteArchivoAdjAutorizacion(Connection con, HashMap parametros) ;
	
	/**
	 * 
	 * @param connection
	 * @param HashMap datos
	 * @return int
	 * @ 
	 */
	public int actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos);
	
	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int updateEstadoDetAutorizacion(Connection con, HashMap parametros);
		
	/**
	 * Método para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 * @ 
	 */
	public int updateActivoDetAutorizacion(Connection con, HashMap parametros);
	
	/**
	 * Actualizar una Autorizacion
	 * @param Connection con
	 * @param DtoAutorizacion dtoAutorizacion
	 */
	public int actualizarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion);
	
	/**
	 * Actualizar Autorizacion Detalle de Autorizacion y detalle cargos
	 * @param con
	 * @param parametros
	 * @return
	 */
	public int actualizarAutorizacionyDetalle (Connection con, HashMap parametros);
	
	/**
	 * Método para obtener la persona que hace la solicitud 
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap<String, Object> obtenerPersonaSolicitad(Connection con,HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param DtoAdjAutorizacion
	 * @return el valor del codigo si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarAdjuntoRespAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion);
	
	/**
	 * Consulta informacion basica del detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap datosBasicosSolicitud(Connection con,HashMap parametros);
	
	/**
	 * Modificar el Atributo Cantidad del Detalle de la Autorizacion
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public int updateCantidadAutoDetAutorizacion(Connection con, HashMap parametros);
	
	/**
	 * Modificar el Atributo Cantidad Solicitada y Cantidad Autorizada de la Respuesta de la Autorizacion
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public int updateCantidadAutoRespAutorizacion(Connection con, HashMap parametros);
	
	/**
	 * Insertar Detalle Autorizacion Estancia
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public int insertarDetAutorizacionEstancia(Connection con, DtoDetAutorizacionEst dtoDetAutorizacionEst);
	
	/**
	 * consulta la informacion del convenio
	 * @param HashMap parametros
	 */
	public HashMap consultarInfoConvenio(Connection con, HashMap parametros);
	
	/**
	 * Modificar El Estado del Detalle de la Autorizacion
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public int updateAnulacionDetAutorizacion(Connection con, HashMap parametros);
	
	/**
	 * Busca Estancia y Valida
	 * @param con
	 * @param HashMap parametros
	 * @return HashMap
	 */
	public ArrayList<DtoDetAutorizacionEst> cargarInfoBasicaDetAutorizacionEstancia(Connection con, HashMap parametros);
	
	/**
	 * Cambiar el campo Activo del det_autorizaciones_estancia
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 */
	public int updateActivoDetAutorizacionEst(Connection con, HashMap parametros);
	
	/**
	 * Método para asociar una cuenta a autorizaciones sin cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public int asociarCuentaAAutorizacionesSinCuenta(Connection con,HashMap<String,Object> campos);
	
	/**
	 * Consulta la Cantidad que falta por Autorizar
	 * @param HashMap parametros
	 * @return int
	 */
	public int obtenerCantidadAutorizadaDetAuto(Connection con, HashMap parametros);
	
	
	/**
	 * Modificar la cobertura en salud de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public int updateCoberturaSaludSubCuenta(Connection con, HashMap parametros);
	
	/**
	 * Consulta Informe Tecnico de Solicitd de Autorizacion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public DtoAutorizacion caragarInformeTecnico(Connection con, HashMap parametros);
	
	/**
	 * Consulta listado de cuentas
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoCuentaAutorizacion> cargarListadoCuentas(Connection con, HashMap parametros);
	
	/**
	 * Consultar la autorizacion con informacion del encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public  DtoAutorizacion cargarAutorizacionXEncabezado(Connection con, HashMap parametros);
	
	
	/**
	 * Insertar Detalle Autorizacion Admision Estancias
	 * @param Connection con
	 * @param DtoDetAutorizacion dtoDetAutorizacion
	 */
	public int insertarDetalleAutorizacionAE(Connection con, DtoDetAutorizacion dtoDetAutorizacion);
	
	/**
	 * Captura la fecha de admision de la cuenta 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap getFechaAdmisionCuenta(Connection con,HashMap parametros);
	
	/**
	 * Consultar listado de solicitudes enviadas
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoAutorizacion> cargarListadoAutorizaciones(Connection con, HashMap parametros);
	
	
	
}