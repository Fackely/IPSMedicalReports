/*
 * @(#)SolicitudDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.servinte.axioma.fwk.exception.BDException;


/**
 * @version 2.0, Jan 14, 2003
 */
public interface SolicitudDao
{

	/**
	 * Método que permite insertar una solicitud de valoración
	 * inicial (Asume que la fecha / hora de solicitud es la misma
	 * que la fecha de inserción, la esp. solicitante es todos porque
	 * en general este dato lo llena per. administrativo, no médico,
	 * Siempre debe rastrearse- Consecutivos Diferentes para
	 * casos 'otros' y al ser la inicial siempre va a la epicrisis y
	 * es urgente)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroAutorizacion Número de autorización de esta
	 * Solicitud
	 * @param codigoCentroCostoSolicitante Código del centro de
	 * costo que hace esta solicitud
	 * @param codigoCentroCostoSolicitado Código del centro de
	 * costo que puede responder esta solicitud
	 * @param codigoCuenta Código de la cuenta a la que pertenece
	 * esta solicitud
	 * @param cobrable Define si esta solicitud se puede cobrar o no
	 * @return
	 * @throws SQLException
	 */
	// String numeroAutorizacion
	public int insertarSolicitudValoracionInicial (Connection con, int codigoCentroCostoSolicitante, int codigoCentroCostoSolicitado, int codigoCuenta, boolean cobrable, int codigoTipoSolicitud) throws SQLException;

	/**
	 * Método que permite insertar una Solicitud General en la
	 * fuente de datos
	 *
	 * @param con Conexión con la fuente de datos
	 * @param fechaSolicitud Fecha en la que el usuario asegura
	 * haber pedido esta solicitud
	 * @param horaSolicitud Hora en la que el usuario asegura
	 * haber pedido esta solicitud
	 * @param codigoTipoSolicitud Código del tipo de solicitud
	 * a insertar
	 * @param cobrable boolean que indica si esta solicitud
	 * es cobrable o no
	 * @param numeroAutorizacion Número que autoriza esta
	 * solicitud
	 * @param codigoEspecialidadSolicitante Código de la
	 * especialidad que solicita
	 * @param codigoOcupacionSolicitada Código de la ocupación
	 * a la que se solicita
	 * @param codigoCentroCostoSolicitante Código del centro de
	 * costo que solicita
	 * @param codigoCentroCostoSolicitado Código del centro de
	 * costo al que se le solicita
	 * @param codigoMedicoCreador Código del Médico creador de
	 * esta solicitud
	 * @param codigoCuenta Código de la cuenta a la que pertenece
	 * esta solicitud
	 * @param vaEpicrisis Boolean que indica si esta solicitud va a
	 * epicrisis
	 * @param urgente Boolean que indica si esta solicitud debe
	 * ejecutarse en la institución
	 * @param estadoClinico Estado Clínico con el que empieza esta
	 * solicitud
	 * @param datosMedico
	 * @param solPYP
	 * @param liquidar_asocio
	 * @return
	 * @throws SQLException
	 */
	// String numeroAutorizacion,
	public int insertarSolicitudGeneral (
			Connection con, 
			String fechaSolicitud, 
			String horaSolicitud, 
			int codigoTipoSolicitud, 
			boolean cobrable, 			 
			int codigoEspecialidadSolicitante, 
			int codigoOcupacionSolicitada, 
			int codigoCentroCostoSolicitante, 
			int  codigoCentroCostoSolicitado, 
			int codigoMedicoCreador, 
			int codigoCuenta, 
			boolean vaEpicrisis, 
			boolean urgente, 
			int estadoClinico, 
			String datosMedico, 
			boolean solPYP,
			boolean tieneCita,
			String liquidar_asocio,
			String justificacionSolicitud,
			int especialidadSolicitadaOrdAmbulatorias,
			int codigomedicoResponde,
			DtoDiagnostico dtoDiagnostico) throws SQLException;
 
	/**
	 * Método que inserta un tratante (No revisa si el tratante
	 * ya está, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los métodos de almacenamiento)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud con la que se
	 * solicita la inserción del tratante
	 * @param codigoMedico Código del médico que será tratante
	 * @param numeroIdentificacionMedico Número de identificación
	 * del médico que será tratante
	 * @return
	 * @throws SQLException
	 */
	public int insertarTratante (Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoTratante) throws SQLException;

	/**
	 * Método que inserta un tratante (No revisa si el tratante
	 * ya está, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los métodos de almacenamiento)
	 * A diferencia de insertarTratante, este método inserta el
	 * tratante de urgencias, que a diferencia del resto NO maneja
	 * médico
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud con la que se
	 * solicita la inserción del tratante
	 * @return
	 * @throws SQLException
	 */
	public int insertarTratanteUrgencias (Connection con, int numeroSolicitud,int area) throws SQLException;

	/**
	 * Método que cierra una entrada existente de tratante
	 * (Con el propósito de dejar una única válida)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta a la que
	 * se le van a cerrar las antiguas entradas de tratante
	 * @return
	 * @throws SQLException
	 */
	public int cerrarAntiguaEntradaTratante (Connection con, int idCuenta ) throws SQLException;

	/**
	 * Método que cierra una entrada existente de tratante
	 * (Con el propósito de dejar una única válida)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * para cuya cuenta vamos a eliminar los tratantes
	 * @return
	 * @throws SQLException
	 */
	public int cerrarAntiguaEntradaTratanteDadaSolicitud (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	* Método que interpreta una solicitud
	* @param ac_con				Conexión abierta a una fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud a ser interpretada
	* @param ai_codigoMedico	Código del médico que realiza la interpretación de la consulta
	* @param as_interpretacion	Texto de la interpretación de la solicitud
	*@return Número de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos PostgreSQL
	*/
	public int interpretarSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		int			ai_codigoMedico,
		String		as_interpretacion
		
	)throws SQLException;
	
	/**
	 * Método que inserta la interpretacion de una solicitud de procedimientos,
	 * se diferencia del otro método de que la fecha/hora interpretacion se mandan como parámetros
	 * @param con
	 * @param campos
	 * @return
	 */
	public int interpretarSolicitud(Connection con,HashMap campos);

	/**
	* Método que modifica la interpretación de una solicitud
	* @param ac_con				Conexión abierta a una fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud a ser interpretada
	* @param as_interpretacion	Texto de la interpretación de la solicitud
	* @param ab_vaEpicrisis		Indicador de si la solicitud va o no a la epicrisis
	* @return Número de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos PostgreSQL
	*/
	public int modificarInterpretacionSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_interpretacion
	)throws SQLException;

	/**
	 * Método que dados un centro de costo y una
	 * solicitud deja como tratante de la cuenta al
	 * centro de costo (solicitado) de la solicitud dada
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @param codigoMedicoSolicitante Código del médico
	 * que creo la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int crearAdjuntoCuenta (Connection con, int numeroSolicitud, int codigoMedicoSolicitante, int codigoCentroCostoSolicitante) throws SQLException;
	
	/**
	 * Método que cambia la solicitud de tratante para una cuenta 
	 * en particular.
	 * Se debe encargar de cancelar la anterior entradada (debe quedar
	 * inactiva) y manejar los dos casos especificados en Anexo35. 
	 * Solicitud de Interconsulta (Se haya respondido la solicitud que 
	 * genero la petición de nuevo tratante o no). Debe soportar los
	 * métodos transaccionales.
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud que
	 * crea este tratante 
	 * @param idCuenta Cuenta a la que se le piensa cambiar
	 * el tratante
	 * @param codigoMedico Código del médico que realiza este
	 * proceso
	 * @param estado Estado de la transacción (empezar, continuar
	 * o finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarSolicitudMedicoTratante (Connection con, int numeroSolicitud, int idCuenta, int codigoMedico, String estado) throws SQLException;
	
	/**
	 * Método que permite cargar esta solicitud
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a 
	 * cargar
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarSolicitud (Connection con, int numeroSolicitud) throws SQLException;
	
	
	/**
	 * Metodo que carga todas las solicitudes de una cuenta, y retorna una colleccion de solicitudes
	 * @armando
	 * @param con, conexion
	 * @param cuenta, cuenta que se desea consultar
	 * @return  collection, datos de la consulta.
	 */
	public Collection cargarSolicitudesInternas(Connection con,int cuenta);
	/**
	 * Inactiva la solicitud de cambio de tratante dada
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean inactivarSolicitudCambioTratante(Connection con, int numeroSolicitud);
	
	/**
	 * Método que dice si hay una solicitud de cambio de tratante
	 * activa en el sistema para una cuenta 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Cuenta en la que se quiere revisar
	 * si hay una solicitud de cambio de tratante activa
	 * @return
	 * @throws SQLException
	 */
	public boolean haySolicitudCambioTratantePrevia (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Método que realiza todo el proceso de anular una solicitud
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a anular
	 * @param motivoAnulacion Motivo de la anulación
	 * @param codigoMedicoAnulacion Código del médico que
	 * realizó la anulación
	 * @return
	 * @throws SQLException
	 */
	public int anularSolicitud (Connection con, int numeroSolicitud, String motivoAnulacion, int  codigoMedicoAnulacion) throws SQLException;	
	
	/**
	 * Método que finaliza la atención conjunta de un centro 
	 * de costo adjunto
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta a la
	 * que se va a quitar el centro de costo adjunto
	 * @param codigoCentroCostoAdjunto Código
	 * del centro de costo que hasta ahora era adjunto
	 * @return
	 * @throws SQLException
	 */
	public int finalizarAtencionConjunta (Connection con, int idCuenta, int codigoCentroCostoAdjunto, String notaFinalizacion) throws SQLException;
	
	/**
	 * Cambia los estados de facturación o de historica clinica, o ambos,
	 * dependiendo de los datos enviados
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEstadoFacturacion
	 * @param codigoEstadoHistoriaClinica
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean cambiarEstadosSolicitud (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica);
	
	/**
	 * Cambia dentro de una transaccion los estados de facturación o de historica clinica, o ambos,
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEstadoFacturacion
	 * @param codigoEstadoHistoriaClinica
	 * @param estado
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean cambiarEstadosSolicitudTransaccional (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica, String estado);
	
	/**
	 * Actualiza el número de autorización
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroAutorizacion
	 * @return
	 */
	//public ResultadoBoolean actualizarNumeroAutorizacionTransaccional(Connection con, int numeroSolicitud, String numeroAutorizacion, String estado);

	/**
	 * Método que mueve / copia todo lo relacionado con solicitudes 
	 * (solicitudes adjuntos y tratantes) cuando ocurre un asocio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param cuentaOrigen Cuenta de la que se sacan los datos
	 * (cuenta de urgencias)
	 * @param cuentaDestino Cuenta en la que se ponen los datos
	 * (cuenta de hospitalización)
	 * @param institución del médico tratante
	 * @return
	 * @throws SQLException
	 */
	public int moverPorAsocio (Connection con, int cuentaOrigen, int cuentaDestino, int codigoMedicoTratante, int codigoCentroCostoTratante, int institucion) throws SQLException;
		
	/**
	 * Retorna el número de autorización
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return int numeroAutorizacion
	 */
	//public ResultadoCollectionDB cargarNumeroAutorizacion(Connection con, int numeroSolicitud);
	
	/**
	 * Método que realiza los cambios necesarios en caso de aceptar
	 * ser tratante en caso de asocio. En el futuro, si es necesario que
	 * la cuenta no se mueva, se puede crear una solicitud anulada
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @param idCuentaDestino
	 * @return
	 * @throws SQLException
	 */
	public boolean cambiosRequeridosAceptacionTratanteCasoAsocio (Connection con, int numeroSolicitud, int idCuentaDestino) throws SQLException;
	
	/**
	 * Método que permite cargar el motivo de la anulación cuando una solicitud está anulada
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud a cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	public int modificarEpicrisis(Connection con ,int numeroSolicitud, boolean estado)throws SQLException;
	
	public ResultadoBoolean cambiarCentroCostoSolicitado(Connection con, int numeroSolicitud, int centroCostoSolicitado);
	
	
	public ResultadoBoolean cambiarCentroCostoSolicitante(Connection con, int numeroSolicitud, int centroCostoSolicitante);
	
	/**
	 * Método para actualizar la fecha y la hora de la solicitud
	 * @param con Conección con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param fecha Fecha actualizada
	 * @param hora Hora actualizada
	 * @param estado Estado de la transacción
	 * @return entero mayor que 1 si se realizó correctamente la modificación 
	 */
	public int actualizarFechaHoraTransaccional(Connection con, int numeroSolicitud, String fecha, String hora, String estado);

	/**
	 * Método para actualizar la fecha y la hora de la solicitud
	 * 
	 * @param con Conexión con la base de datos
	 * @param numeroSolicitud Solicitud que se quiere modificar
	 * @param urgente Prioridad (true --> urgente)
	 * @param estado Estado de la transacción
	 * @return entero mayor que 1 si se realizó correctamente la modificación
	 */
	public int actualizarPrioridadUrgenteTransaccional(Connection con, int numeroSolicitud, boolean urgente, String estado);
	
	/**
	 * Método que actualiza el médico que responde 
	 * 
	 * @param con Conexión con la base de datos
	 * @param numeroSolicitud Número de Solicitud
	 * @param idMedico Código del médico
	 * @param detalleMedico Detalle con especialidades
	 * al momento de responder la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int actualizarMedicoResponde (Connection con, int numeroSolicitud, int idMedico, String detalleMedico) throws SQLException;
	
	/**
	 * Método para ingresar la justificación del procedimiento
	 * este método solo inserta un atributo, así que se llama
	 * cada vez que se quiere insertar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param atributo Parámetro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacción
	 * @return int mayor que 0 si se insertó correctamente el atributo específico.
	 */
	public int ingresarAtributoTransaccional(Connection con, int numeroSolicitud, int servicio, int atributo, String descripcion, String estado);

	/**
	 * Método que actualiza el tipo de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudValoracionGinecoObstetrica
	 * @return Numero de registros modificados
	 */
	public int actualizarTipoSolicitud(Connection con, int numeroSolicitud, int tipoSolicitud);

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @param pool
	 * @return
	 */
	public int actualizarPoolSolicitud(Connection con, int numeroSolicitud, int pool);

	/**
	 * Obtiene el numero de solicitud a inasertar para evitar problemas
	 * de concurrencia
	 * @param con
	 * @param consulta
	 * @return
	 */
	public int getNumeroSolicitudAinsertar(Connection con);
	
	/**
	 * Método que actualiza el indicativo pyp de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param pyp
	 * @return
	 */
	public int actualizarIndicativoPYP(Connection con,String numeroSolicitud,boolean pyp);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int getCodigoTipoSolicitud(Connection con, String numeroSolicitud) throws BDException;

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dto) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoCentroCostoSolicitado(Connection con, String numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param dtoSolicitudesSubCuenta
	 * @return
	 */
	public boolean eliminarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoCentroCostoSolicitante(Connection con, String numeroSolicitud) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap obteneInfoSolicitudInterfaz(Connection con, String numeroSolicitud);
	
	
	/**
	 * Actualiza el valor de liquidar Asocios de la tabla Solicitudes
	 * @param Connection con
	 * @param int NumeroSolicitud
	 * @param String liqudarAsocios
	 * */
	public boolean cambiarLiquidacionAsociosSolicitud(Connection con, int numeroSolicitud, String liquidarAsocios);
	
	/**
	 * Metodo para consultar el Valor del Parametro General de Validar Registro Evoluciones
	 * @param con
	 * @return
	 */
	public String consultarParametroEvoluciones(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public String obtenerFirmaDigitalMedico(Connection con, int numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerInfoMedicoSolicita(Connection con, int numeroSolicitud);
	
	/**
	 * metodo que actualiza el numero de orden de medicamentos con diferente dosificacion
	 * @param con
	 * @param vectorNumeroSolicitudes
	 * @return
	 */
	public boolean actualizarNumeroOrdenMedDiferenteDosificacion(Connection con, Vector<String> vectorNumeroSolicitudes);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public String obtenerLoginMedicoSolicita(Connection con, int numeroSolicitud, int institucion);
	
	/**
	 * Método para actualizar los centros de costo de medicamentos x despachar
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarCentrosCostoMedicamentosXDespachar(Connection con,HashMap<String, Object> campos);
	
	/**
	 * Actualiza la informacion de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarInfoSolicitud(Connection con, HashMap parametros);

	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(
			Connection con, int codigo, int codigoInstitucionInt);


	// Cambios Segun Anexo 809
	/**
	  * Obtener la especilidad solicitada de un solicitud
	  * @param Connection con
	  * @param HashMap parametros
	  * @return HashMap<String, Object>
	  */
	public HashMap<String, Object> obtenerEspecilidadSolicitada(Connection con, HashMap parametros);
	// Fin Cambios Segun Anexo 809

	/**
	 * Actulizar la especialidad del Profesiona que responde
	 */
	public int actualizarEspecialidadProfResponde(Connection con,int numeroSolicitud, int codEspecialidad) throws SQLException;
	
	/**
	 * M&eacute;todo encargado de buscar los articulos asociados a 
	 * una solicitud de medicamentos generada desde un cargo directo de 
	 * articulos
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoArticulos> buscarArticulosCargosDirectosArticulos (Connection conn,
			int numeroSolicitud);

	/**
	 * 
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerInterpretacionSolicitud(int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerCodigoCita(Connection con, int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerEspecilidadSolicitadaCita(Connection con,int numeroSolicitud);
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return si es tipo de solicitud de valoracion consulta externa
	 * @throws Exception
	 */
	public  Boolean esConsultaExterna(Connection con,int numeroSolicitud) throws Exception;
	
	
	
}