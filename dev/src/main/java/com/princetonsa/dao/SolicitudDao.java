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
	 * M�todo que permite insertar una solicitud de valoraci�n
	 * inicial (Asume que la fecha / hora de solicitud es la misma
	 * que la fecha de inserci�n, la esp. solicitante es todos porque
	 * en general este dato lo llena per. administrativo, no m�dico,
	 * Siempre debe rastrearse- Consecutivos Diferentes para
	 * casos 'otros' y al ser la inicial siempre va a la epicrisis y
	 * es urgente)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroAutorizacion N�mero de autorizaci�n de esta
	 * Solicitud
	 * @param codigoCentroCostoSolicitante C�digo del centro de
	 * costo que hace esta solicitud
	 * @param codigoCentroCostoSolicitado C�digo del centro de
	 * costo que puede responder esta solicitud
	 * @param codigoCuenta C�digo de la cuenta a la que pertenece
	 * esta solicitud
	 * @param cobrable Define si esta solicitud se puede cobrar o no
	 * @return
	 * @throws SQLException
	 */
	// String numeroAutorizacion
	public int insertarSolicitudValoracionInicial (Connection con, int codigoCentroCostoSolicitante, int codigoCentroCostoSolicitado, int codigoCuenta, boolean cobrable, int codigoTipoSolicitud) throws SQLException;

	/**
	 * M�todo que permite insertar una Solicitud General en la
	 * fuente de datos
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param fechaSolicitud Fecha en la que el usuario asegura
	 * haber pedido esta solicitud
	 * @param horaSolicitud Hora en la que el usuario asegura
	 * haber pedido esta solicitud
	 * @param codigoTipoSolicitud C�digo del tipo de solicitud
	 * a insertar
	 * @param cobrable boolean que indica si esta solicitud
	 * es cobrable o no
	 * @param numeroAutorizacion N�mero que autoriza esta
	 * solicitud
	 * @param codigoEspecialidadSolicitante C�digo de la
	 * especialidad que solicita
	 * @param codigoOcupacionSolicitada C�digo de la ocupaci�n
	 * a la que se solicita
	 * @param codigoCentroCostoSolicitante C�digo del centro de
	 * costo que solicita
	 * @param codigoCentroCostoSolicitado C�digo del centro de
	 * costo al que se le solicita
	 * @param codigoMedicoCreador C�digo del M�dico creador de
	 * esta solicitud
	 * @param codigoCuenta C�digo de la cuenta a la que pertenece
	 * esta solicitud
	 * @param vaEpicrisis Boolean que indica si esta solicitud va a
	 * epicrisis
	 * @param urgente Boolean que indica si esta solicitud debe
	 * ejecutarse en la instituci�n
	 * @param estadoClinico Estado Cl�nico con el que empieza esta
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
	 * M�todo que inserta un tratante (No revisa si el tratante
	 * ya est�, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los m�todos de almacenamiento)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud con la que se
	 * solicita la inserci�n del tratante
	 * @param codigoMedico C�digo del m�dico que ser� tratante
	 * @param numeroIdentificacionMedico N�mero de identificaci�n
	 * del m�dico que ser� tratante
	 * @return
	 * @throws SQLException
	 */
	public int insertarTratante (Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoTratante) throws SQLException;

	/**
	 * M�todo que inserta un tratante (No revisa si el tratante
	 * ya est�, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los m�todos de almacenamiento)
	 * A diferencia de insertarTratante, este m�todo inserta el
	 * tratante de urgencias, que a diferencia del resto NO maneja
	 * m�dico
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud con la que se
	 * solicita la inserci�n del tratante
	 * @return
	 * @throws SQLException
	 */
	public int insertarTratanteUrgencias (Connection con, int numeroSolicitud,int area) throws SQLException;

	/**
	 * M�todo que cierra una entrada existente de tratante
	 * (Con el prop�sito de dejar una �nica v�lida)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta a la que
	 * se le van a cerrar las antiguas entradas de tratante
	 * @return
	 * @throws SQLException
	 */
	public int cerrarAntiguaEntradaTratante (Connection con, int idCuenta ) throws SQLException;

	/**
	 * M�todo que cierra una entrada existente de tratante
	 * (Con el prop�sito de dejar una �nica v�lida)
	 *
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud
	 * para cuya cuenta vamos a eliminar los tratantes
	 * @return
	 * @throws SQLException
	 */
	public int cerrarAntiguaEntradaTratanteDadaSolicitud (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	* M�todo que interpreta una solicitud
	* @param ac_con				Conexi�n abierta a una fuente de datos
	* @param ai_numeroSolicitud	N�mero de la solicitud a ser interpretada
	* @param ai_codigoMedico	C�digo del m�dico que realiza la interpretaci�n de la consulta
	* @param as_interpretacion	Texto de la interpretaci�n de la solicitud
	*@return N�mero de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos PostgreSQL
	*/
	public int interpretarSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		int			ai_codigoMedico,
		String		as_interpretacion
		
	)throws SQLException;
	
	/**
	 * M�todo que inserta la interpretacion de una solicitud de procedimientos,
	 * se diferencia del otro m�todo de que la fecha/hora interpretacion se mandan como par�metros
	 * @param con
	 * @param campos
	 * @return
	 */
	public int interpretarSolicitud(Connection con,HashMap campos);

	/**
	* M�todo que modifica la interpretaci�n de una solicitud
	* @param ac_con				Conexi�n abierta a una fuente de datos
	* @param ai_numeroSolicitud	N�mero de la solicitud a ser interpretada
	* @param as_interpretacion	Texto de la interpretaci�n de la solicitud
	* @param ab_vaEpicrisis		Indicador de si la solicitud va o no a la epicrisis
	* @return N�mero de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos PostgreSQL
	*/
	public int modificarInterpretacionSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_interpretacion
	)throws SQLException;

	/**
	 * M�todo que dados un centro de costo y una
	 * solicitud deja como tratante de la cuenta al
	 * centro de costo (solicitado) de la solicitud dada
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud
	 * @param codigoMedicoSolicitante C�digo del m�dico
	 * que creo la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int crearAdjuntoCuenta (Connection con, int numeroSolicitud, int codigoMedicoSolicitante, int codigoCentroCostoSolicitante) throws SQLException;
	
	/**
	 * M�todo que cambia la solicitud de tratante para una cuenta 
	 * en particular.
	 * Se debe encargar de cancelar la anterior entradada (debe quedar
	 * inactiva) y manejar los dos casos especificados en Anexo35. 
	 * Solicitud de Interconsulta (Se haya respondido la solicitud que 
	 * genero la petici�n de nuevo tratante o no). Debe soportar los
	 * m�todos transaccionales.
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud que
	 * crea este tratante 
	 * @param idCuenta Cuenta a la que se le piensa cambiar
	 * el tratante
	 * @param codigoMedico C�digo del m�dico que realiza este
	 * proceso
	 * @param estado Estado de la transacci�n (empezar, continuar
	 * o finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarSolicitudMedicoTratante (Connection con, int numeroSolicitud, int idCuenta, int codigoMedico, String estado) throws SQLException;
	
	/**
	 * M�todo que permite cargar esta solicitud
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a 
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
	 * M�todo que dice si hay una solicitud de cambio de tratante
	 * activa en el sistema para una cuenta 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta Cuenta en la que se quiere revisar
	 * si hay una solicitud de cambio de tratante activa
	 * @return
	 * @throws SQLException
	 */
	public boolean haySolicitudCambioTratantePrevia (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que realiza todo el proceso de anular una solicitud
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a anular
	 * @param motivoAnulacion Motivo de la anulaci�n
	 * @param codigoMedicoAnulacion C�digo del m�dico que
	 * realiz� la anulaci�n
	 * @return
	 * @throws SQLException
	 */
	public int anularSolicitud (Connection con, int numeroSolicitud, String motivoAnulacion, int  codigoMedicoAnulacion) throws SQLException;	
	
	/**
	 * M�todo que finaliza la atenci�n conjunta de un centro 
	 * de costo adjunto
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idCuenta C�digo de la cuenta a la
	 * que se va a quitar el centro de costo adjunto
	 * @param codigoCentroCostoAdjunto C�digo
	 * del centro de costo que hasta ahora era adjunto
	 * @return
	 * @throws SQLException
	 */
	public int finalizarAtencionConjunta (Connection con, int idCuenta, int codigoCentroCostoAdjunto, String notaFinalizacion) throws SQLException;
	
	/**
	 * Cambia los estados de facturaci�n o de historica clinica, o ambos,
	 * dependiendo de los datos enviados
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEstadoFacturacion
	 * @param codigoEstadoHistoriaClinica
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean cambiarEstadosSolicitud (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica);
	
	/**
	 * Cambia dentro de una transaccion los estados de facturaci�n o de historica clinica, o ambos,
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEstadoFacturacion
	 * @param codigoEstadoHistoriaClinica
	 * @param estado
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean cambiarEstadosSolicitudTransaccional (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica, String estado);
	
	/**
	 * Actualiza el n�mero de autorizaci�n
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroAutorizacion
	 * @return
	 */
	//public ResultadoBoolean actualizarNumeroAutorizacionTransaccional(Connection con, int numeroSolicitud, String numeroAutorizacion, String estado);

	/**
	 * M�todo que mueve / copia todo lo relacionado con solicitudes 
	 * (solicitudes adjuntos y tratantes) cuando ocurre un asocio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param cuentaOrigen Cuenta de la que se sacan los datos
	 * (cuenta de urgencias)
	 * @param cuentaDestino Cuenta en la que se ponen los datos
	 * (cuenta de hospitalizaci�n)
	 * @param instituci�n del m�dico tratante
	 * @return
	 * @throws SQLException
	 */
	public int moverPorAsocio (Connection con, int cuentaOrigen, int cuentaDestino, int codigoMedicoTratante, int codigoCentroCostoTratante, int institucion) throws SQLException;
		
	/**
	 * Retorna el n�mero de autorizaci�n
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return int numeroAutorizacion
	 */
	//public ResultadoCollectionDB cargarNumeroAutorizacion(Connection con, int numeroSolicitud);
	
	/**
	 * M�todo que realiza los cambios necesarios en caso de aceptar
	 * ser tratante en caso de asocio. En el futuro, si es necesario que
	 * la cuenta no se mueva, se puede crear una solicitud anulada
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud
	 * @param idCuentaDestino
	 * @return
	 * @throws SQLException
	 */
	public boolean cambiosRequeridosAceptacionTratanteCasoAsocio (Connection con, int numeroSolicitud, int idCuentaDestino) throws SQLException;
	
	/**
	 * M�todo que permite cargar el motivo de la anulaci�n cuando una solicitud est� anulada
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroSolicitud N�mero de la solicitud a cargar
	 * @return ResultSetDecorator resultado de la consulta
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException;
	
	public int modificarEpicrisis(Connection con ,int numeroSolicitud, boolean estado)throws SQLException;
	
	public ResultadoBoolean cambiarCentroCostoSolicitado(Connection con, int numeroSolicitud, int centroCostoSolicitado);
	
	
	public ResultadoBoolean cambiarCentroCostoSolicitante(Connection con, int numeroSolicitud, int centroCostoSolicitante);
	
	/**
	 * M�todo para actualizar la fecha y la hora de la solicitud
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param fecha Fecha actualizada
	 * @param hora Hora actualizada
	 * @param estado Estado de la transacci�n
	 * @return entero mayor que 1 si se realiz� correctamente la modificaci�n 
	 */
	public int actualizarFechaHoraTransaccional(Connection con, int numeroSolicitud, String fecha, String hora, String estado);

	/**
	 * M�todo para actualizar la fecha y la hora de la solicitud
	 * 
	 * @param con Conexi�n con la base de datos
	 * @param numeroSolicitud Solicitud que se quiere modificar
	 * @param urgente Prioridad (true --> urgente)
	 * @param estado Estado de la transacci�n
	 * @return entero mayor que 1 si se realiz� correctamente la modificaci�n
	 */
	public int actualizarPrioridadUrgenteTransaccional(Connection con, int numeroSolicitud, boolean urgente, String estado);
	
	/**
	 * M�todo que actualiza el m�dico que responde 
	 * 
	 * @param con Conexi�n con la base de datos
	 * @param numeroSolicitud N�mero de Solicitud
	 * @param idMedico C�digo del m�dico
	 * @param detalleMedico Detalle con especialidades
	 * al momento de responder la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int actualizarMedicoResponde (Connection con, int numeroSolicitud, int idMedico, String detalleMedico) throws SQLException;
	
	/**
	 * M�todo para ingresar la justificaci�n del procedimiento
	 * este m�todo solo inserta un atributo, as� que se llama
	 * cada vez que se quiere insertar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param atributo Par�metro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacci�n
	 * @return int mayor que 0 si se insert� correctamente el atributo espec�fico.
	 */
	public int ingresarAtributoTransaccional(Connection con, int numeroSolicitud, int servicio, int atributo, String descripcion, String estado);

	/**
	 * M�todo que actualiza el tipo de la solicitud
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
	 * M�todo que actualiza el indicativo pyp de la solicitud
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
	 * M�todo para actualizar los centros de costo de medicamentos x despachar
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