package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dao.manejoPaciente.AutorizacionesDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAutorizacionesDao;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoEnvioAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoRespAutorizacion;

public class PostgresqlAutorizacionesDao implements AutorizacionesDao {
	
	/**
	 * Insertar Autorizacion 
	 */
	public int insertarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion)  {
		return SqlBaseAutorizacionesDao.insertarAutorizacion(con, dtoAutorizacion);
	}
	
	/**
	 * Insertar Detalle de Autorizacion 
	 */
	public int insertarDetalleAutorizacion(Connection con, DtoDetAutorizacion dtoDetAutorizacion)  {
		return SqlBaseAutorizacionesDao.insertarDetalleAutorizacion(con, dtoDetAutorizacion);
	}
	
	/**
	 * Insertar Envio de Autorizacion 
	 */
	public int insertarEnvioAutorizacion(Connection con, DtoEnvioAutorizacion dtoEnvAutorizacion)  {
		return SqlBaseAutorizacionesDao.insertarEnvioAutorizacion(con, dtoEnvAutorizacion);
	}
	
	/**
	 * Insertar Diagnostico de Autorizacion 
	 */
	public int insertarDiagnosticoAutorizacion(Connection con, DtoDiagAutorizacion dtoDiagAutorizacion)  {
		return SqlBaseAutorizacionesDao.insertarDiagnosticoAutorizacion(con, dtoDiagAutorizacion);
	}
	
	/**
	 * Insertar Archivo Adjunto de Autorizacion 
	 */
	public int insertarAdjuntoAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion)  {
		return SqlBaseAutorizacionesDao.insertarAdjuntoAutorizacion(con, dtoAdjAutorizacion);
	}
	
	/**
	 * Insertar Respuesta de Solicitud de Autorizacion 
	 */
	public int insertarRespuestaAutorizacion(Connection con, DtoRespAutorizacion dtoRespAutorizacion) {
		return SqlBaseAutorizacionesDao.insertarRespuestaAutorizacion(con, dtoRespAutorizacion);
	}
	
	/**
	 * Cargar Solicitud de Autorizacion 
	 */
	public DtoAutorizacion cargarAutorizacion(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.cargarAutorizacion(con, parametros);
	}
	
	/**
	 * Cargar los Detalles de Autorizacion 
	 */
	public ArrayList<DtoDetAutorizacion> cargarDetallesAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.cargarDetallesAutorizacion(con, parametros);
	}	
	
	/**
	 * Listar las Personas que Solicitan una Orden Amblatoria o de Servicio/Articulo 
	 */
	public ArrayList<HashMap<String,Object>> getListadoPersonaSolicita(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.getListadoPersonaSolicita(con, parametros);
	}
	
	/**
	 * Modificar el Tipo se Servicio Solicitado 
	 */
	public int updateTipoSerSolAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.updateTipoSerSolAutorizacion(con, parametros);
	}
	
	/**
	 * Modificar el Observacion de Solicitud de Autorizacion 
	 */
	public int updateObservacionAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.updateObservacionAutorizacion(con, parametros);
	}
	
	/**
	 * Modifica la Autorizacion Asocida al detalle de Autorizacion
	 */
	public int updateAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.updateTipoSerSolAutorizacion(con, parametros);
	}
	
	/**
	 * Modifica el Diagnostico Principal de la Solicitud de Autorizacion 
	 */
	public int updateDiagPPAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.updateDiagPPAutorizacion(con, parametros);
	}
	
	/**
	 * Elimina el Diagnostico Relacionado de la Solicitud de Autorizacion 
	 */
	public int deleteDiagRAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.deleteDiagRAutorizacion(con, parametros);
	}
	
	/**
	 * Elimina el Archivo Adjunto de la Solicitud de Autorizacion 
	 */
	public int deleteArchivoAdjAutorizacion(Connection con, HashMap parametros)  {
		return SqlBaseAutorizacionesDao.deleteArchivoAdjAutorizacion(con, parametros);
	}
	
	/**
	 * Actualizar el Numero de Autorizacion en "det_cargo2 e Insertar log de Actualizacion de Autorizaciones
	 */
	public int actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos) {
		return SqlBaseAutorizacionesDao.actualizarAutorizacionServicioArticulo(connection, datos);
	}

	/**
	 * Modifica el Estado del Detalle de Autorizacion 
	 */
	public int updateEstadoDetAutorizacion(Connection con, HashMap parametros) {
		return SqlBaseAutorizacionesDao.updateEstadoDetAutorizacion(con, parametros);
	}
	
	/**
	 * Modifica el Atributo Activo del Detalle de Autorizacion 
	 */
	public int updateActivoDetAutorizacion(Connection con, HashMap parametros) {
		return SqlBaseAutorizacionesDao.updateActivoDetAutorizacion(con, parametros);
	}
	
	/**
	 * Método para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,HashMap parametros){
		return SqlBaseAutorizacionesDao.obtenerOrigenAtencionXCuenta(con, parametros); 
	}
	
	/**
	 * Actualizar una Autorizacion
	 * @param Connection con
	 * @param DtoAutorizacion dtoAutorizacion
	 */
	public int actualizarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion)
	{
		return SqlBaseAutorizacionesDao.actualizarAutorizacion(con, dtoAutorizacion);
	}
	
	/**
	 * Actualizar Autorizacion Detalle de Autorizacion y detalle cargos
	 */
	public int actualizarAutorizacionyDetalle (Connection con, HashMap parametros)
	{
		return SqlBaseAutorizacionesDao.actualizarAutorizacionyDetalle(con, parametros);
	}
	
	/**
	 * Método para obtener la persona que hace la solicitud 
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap<String, Object> obtenerPersonaSolicitad(Connection con,HashMap parametros){
		return SqlBaseAutorizacionesDao.obtenerPersonaSolicitad(con, parametros);
	}

	/**
	 * 
	 * @param con
	 * @param DtoAdjAutorizacion
	 * @return el valor del codigo si la insercion ha sido correcta de lo contrario cero 0
	 * @ 
	 */
	public int insertarAdjuntoRespAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion){
		return SqlBaseAutorizacionesDao.insertarAdjuntoRespAutorizacion(con, dtoAdjAutorizacion);
	}
	
	
	/**
	 * Consulta informacion basica del detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap datosBasicosSolicitud(Connection con,HashMap parametros){
		return SqlBaseAutorizacionesDao.datosBasicosSolicitud(con, parametros);
	}	
	
	/**
	 * Modificar el Atributo Cantidad del Detalle de la Autorizacion
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public int updateCantidadAutoDetAutorizacion(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.updateCantidadAutoDetAutorizacion(con, parametros);
	}
	
	/**
	 * Modificar el Atributo Cantidad Autorizada de la Respuesta de la Autorizacion
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public int updateCantidadAutoRespAutorizacion(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.updateCantidadAutoRespAutorizacion(con, parametros);
	}
	
	/**
	 * Insertar Detalle Autorizacion Estancia
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public int insertarDetAutorizacionEstancia(Connection con, DtoDetAutorizacionEst dtoDetAutorizacionEst){
		return SqlBaseAutorizacionesDao.insertarDetAutorizacionEstancia(con, dtoDetAutorizacionEst);
	}
	
	/**
	 * consulta la informacion del convenio
	 * @param HashMap parametros
	 */
	public HashMap consultarInfoConvenio(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.consultarInfoConvenio(con, parametros);
	}
	
	/**
	 * Modificar El Estado del Detalle de la Autorizacion
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public int updateAnulacionDetAutorizacion(Connection con, HashMap parametros)
	{
		return SqlBaseAutorizacionesDao.updateAnulacionDetAutorizacion(con, parametros);
	}
	
	/**
	 * Busca Estancia y Valida
	 * @param con
	 * @param HashMap parametros
	 * @return HashMap
	 */
	public ArrayList<DtoDetAutorizacionEst> cargarInfoBasicaDetAutorizacionEstancia(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.cargarInfoBasicaDetAutorizacionEstancia(con, parametros);
	}
	
	/**
	 * Cambiar el campo Activo del det_autorizaciones_estancia
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 */
	public int updateActivoDetAutorizacionEst(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.updateActivoDetAutorizacionEst(con, parametros);
	}
	
	/**
	 * Consulta la Cantidad que falta por Autorizar
	 * @param HashMap parametros
	 * @return int
	 */
	public int obtenerCantidadAutorizadaDetAuto(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.obtenerCantidadAutorizadaDetAuto(con, parametros);
	}
	
	/**
	 * Método para asociar una cuenta a autorizaciones sin cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public int asociarCuentaAAutorizacionesSinCuenta(Connection con,HashMap<String,Object> campos)
	{
		return SqlBaseAutorizacionesDao.asociarCuentaAAutorizacionesSinCuenta(con, campos);
	}
	
	/**
	 * Modificar la cobertura en salud de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public int updateCoberturaSaludSubCuenta(Connection con, HashMap parametros)
	{
		return SqlBaseAutorizacionesDao.updateCoberturaSaludSubCuenta(con, parametros);
	}
	
	/**
	 * Consulta Informe Tecnico de Solicitd de Autorizacion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public DtoAutorizacion caragarInformeTecnico(Connection con, HashMap parametros){
		return SqlBaseAutorizacionesDao.caragarInformeTecnico(con, parametros);
	}
	
	/**
	 * Consulta listado de cuentas
	 * @param Connection con
	 * @param HashMap parametros
	 */ 
	public ArrayList<DtoCuentaAutorizacion> cargarListadoCuentas(Connection con, HashMap parametros)
	{
		return SqlBaseAutorizacionesDao.cargarListadoCuentas(con, parametros);
	}
	
	/**
	 * Consultar la autorizacion con informacion del encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public  DtoAutorizacion cargarAutorizacionXEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseAutorizacionesDao.cargarAutorizacionXEncabezado(con, parametros);
	}
	
	/**
	 * Insertar Detalle Autorizacion Admision Estancias
	 * @param Connection con
	 * @param DtoDetAutorizacion dtoDetAutorizacion
	 */
	public int insertarDetalleAutorizacionAE(Connection con, DtoDetAutorizacion dtoDetAutorizacion)
	{
		return SqlBaseAutorizacionesDao.insertarDetalleAutorizacionAE(con, dtoDetAutorizacion);
	}
	
	/**
	 * Captura la fecha de admision de la cuenta 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap getFechaAdmisionCuenta(Connection con,HashMap parametros)
	{
		return SqlBaseAutorizacionesDao.getFechaAdmisionCuenta(con, parametros);
	}
	
	/**
	 * Consultar listado de solicitudes enviadas
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoAutorizacion> cargarListadoAutorizaciones(Connection con, HashMap parametros)	
	{
		return SqlBaseAutorizacionesDao.cargarListadoAutorizaciones(con, parametros);
	}
}