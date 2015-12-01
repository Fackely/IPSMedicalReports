/*
* @(#)SolicitudProcedimiento.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.laboratorios.InterfazLaboratorios;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudProcedimientoDao;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.servinte.axioma.fwk.exception.IPSException;

/**
* Esta clase encapsula los atributos y la funcionalidad de una solicitud de procedimiento
*
* @version 1.0, Mar 10, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class SolicitudProcedimiento extends Solicitud
{
	/** Objeto para manejar los logs de esta clase */
	private transient Logger logger = Logger.getLogger(SolicitudProcedimiento.class);
	
	/**
	* Servicio solicitado para el procedimiento. Si esta propiedad tiene un código con valor mayor o
	* igual a 0 (cero), el procedimiento está parametrizado en el sistema. Por lo tanto la propiedad
	* is_otros debe ser igual a "" (cadena vaciá)
	*/
	InfoDatosInt iidi_servicioSolicitado;

	/**
	* El DAO usado por el objeto <code>SolicitudProcedimiento</code> para acceder a la fuente de
	* datos
	*/
	
	private static transient SolicitudProcedimientoDao ispd_dao = null;

	/** Comentario de la solicitud del procedimiento */
	private String is_comentario;

	/** Comentario adicional de la solicitud de procedimiento*/
	private String is_comentarioAdicional;

	private String esPos;
	
	private boolean esRespuestaMultiple;
	
	private boolean estaFinalizadaRespuestaMultiple;
	
	/**
	* Nombre del servicio solicitado para el procedimiento. Si esta propiedad tiene un valor
	* diferente a "" (cadena vacia), el procedimiento no está parametrizado en el sistema. Por lo
	* tanto el valor de la propiedad iidi_servicioSolicitado debe ser menor a 0 (cero)
	*/
	private String is_otros;
	
	private int numeroDocumento;
	
	private boolean multiple;
	
	private float frecuencia;
	
	private int tipoFrecuencia;
	
	private int finalidad;
	
	private String institucion;
	
	/**
	 * 
	 */
	private String nombreFinalidad;
	
	/**
	 * 
	 */
	private boolean respuestaMultiple=false;
	
	/**
	 * 
	 */
	private boolean finalizadaRespuesta=false;
	
	/**
	 * Codgio del tipo de servicio
	 */
	private String codigoTipoServicio;
	
	/** Nueva solicitud de procedimiento */
	
	/**
	 * Lista para guardar los id de la solicitudes generadas. 
	 * para realizar el proceso de generacion de autorizaciones.
	 */
	private List<Integer> listaNumerosSolicitud;
	
	public SolicitudProcedimiento()
	{
		/* Inicializar la solicitud general */
		super();

		/* Inicializar los datos particulares de la solicitud de procedimiento */
		clean();

		/* Inicializar el acceo a base de datos */
		init();
	}

	/** Carga un objeto de solicitud de procedimiento desde una fuente de datos */
	public boolean cargarSolicitudProcedimiento(Connection ac_con, int ai_numeroSolicitud)throws SQLException {
		boolean lb_resp;

		/* Inicializar las propiedades de la solicitud de procedimiento */
		clean();
		String tarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(this.institucion));
		/* Cargar la solicitud general */
		
		lb_resp = (super.cargar(ac_con, ai_numeroSolicitud) );
		if( lb_resp  )
		{			
			/* Cargar los datos paticulares de la solicitud de procedimientos */
			HashMap ldb_solicitud;
			lb_resp = ( (ldb_solicitud = ispd_dao.cargar(ac_con, ai_numeroSolicitud,tarifario) ) != null);
			if(lb_resp  )
			{
				Integer	li_aux;
				String	ls_aux;

				if( (li_aux = Utilidades.convertirAEntero(ldb_solicitud.get("codigoserviciosolicitado") +"")) != null)
					setCodigoServicioSolicitado(li_aux.intValue() );

				if( (ls_aux = (String)ldb_solicitud.get("comentario") ) != null)
					setComentario(ls_aux.trim() );

				if( (ls_aux = (String)ldb_solicitud.get("nombreotros") ) != null)
					setNombreOtros(ls_aux.trim() );

				if( (ls_aux = (String)ldb_solicitud.get("nombreserviciosolicitado") ) != null)
					setNombreServicioSolicitado(ls_aux);

				if( (li_aux = Utilidades.convertirAEntero(ldb_solicitud.get("numerodocumento")+"") ) != null)
					setNumeroDocumento(li_aux.intValue());
				
				li_aux = Utilidades.convertirAEntero(ldb_solicitud.get("codigoespecialidadsolicitada")+"");
				ls_aux = (String)ldb_solicitud.get("nombreespecialidadsolicitada");
				if(li_aux != null && ls_aux != null)
					setEspecialidadSolicitada(new InfoDatosInt(li_aux.intValue(), ls_aux.trim() ) );
				else
					setEspecialidadSolicitada(new InfoDatosInt(-1, "") );
				ls_aux = ldb_solicitud.get("solicitud_multiple") + "";

				if(ls_aux!=null && !ls_aux.equals(""))
				{
					setMultiple(UtilidadTexto.getBoolean(ls_aux));
				}
				ls_aux = ldb_solicitud.get("frecuencia") + "";
				if(ls_aux!=null && !ls_aux.equals("null") && !ls_aux.equals(""))
				{
					setFrecuencia(Float.parseFloat(ls_aux));
				}
				ls_aux = ldb_solicitud.get("tipo_frecuencia") + "";
				if(ls_aux!=null && !ls_aux.equals("null") && !ls_aux.equals(""))
				{
					setTipoFrecuencia(Integer.parseInt(ls_aux)); 
				}
				
				ls_aux = ldb_solicitud.get("finalidad") + "";
				if(ldb_solicitud.get("finalidad")!=null&&!ls_aux.equals("null")&&!ls_aux.equals(""))
					setFinalidad(Integer.parseInt(ls_aux));
				
				ls_aux = ldb_solicitud.get("nombrefinalidad") + "";
				if(ldb_solicitud.get("nombrefinalidad")!=null&&!ls_aux.equals("null")&&!ls_aux.equals(""))
					setNombreFinalidad(ls_aux);
				
				ls_aux = ldb_solicitud.get("espos") + "";
				if(ldb_solicitud.get("espos")!=null&&!ls_aux.equals("null"))
					setEsPos(ls_aux);
				
				if(Utilidades.tieneProcedimientoRespuestaMultiple(ac_con, ai_numeroSolicitud+""))
				{
					this.esRespuestaMultiple=true;
				}
				if(Utilidades.estaFinalizadaRespuestaMultiple(ac_con, ai_numeroSolicitud+""))
				{
					this.estaFinalizadaRespuestaMultiple=true;
				}
				
				///////////////////////////////////////////////////////////////////////////
				//portatail anexo 591
				ls_aux = ldb_solicitud.get("portatil") + "";
				if(ldb_solicitud.get("portatil")!=null&&!ls_aux.equals("null"))
					setPortatil(ls_aux);
				
				ls_aux = ldb_solicitud.get("motivoanulport") + "";
				if(ldb_solicitud.get("motivoanulport")!=null && !ls_aux.equals("null"))
					setMotivoAnulacionPort(ls_aux);				
				///////////////////////////////////////////////////////////////////////////							
			}
		}

		return lb_resp;
	}

	/** Inicialización de las propiedades de la solicitud de procedimientos */
	public void clean()
	{
		super.clean();
		iidi_servicioSolicitado = new InfoDatosInt();
		setCodigoServicioSolicitado(-1);
		setComentario("");
		setComentarioAdicional("");
		setNombreOtros("");
		this.multiple=false;
		this.frecuencia=-1;
		this.finalidad = -1;
		this.nombreFinalidad = "";
		this.esPos="";
		this.esRespuestaMultiple=false;
		this.estaFinalizadaRespuestaMultiple=false;
		this.codigoTipoServicio = "";
		this.listaNumerosSolicitud= new ArrayList<Integer>();
	}

	/**
	* Obtiene el código del servicio (parametrizado en el sistema) de la solicitud de procedimiento
	*/
	public int getCodigoServicioSolicitado()
	{
		return iidi_servicioSolicitado.getCodigo();
	}

	/** Obtiene el comentario de la solicitud de procedimientos*/
	public String getComentario()
	{
		return is_comentario;
	}

	/** Obtiene el comentario de la solicitud de procedimientos*/
	public String getComentarioAdicional()
	{
		return is_comentarioAdicional;
	}

	/**
	* Obtiene el nombre del servicio (no parametrizado en el sistema) de la solicitud de
	* procedimiento
	*/
	public String getNombreOtros()
	{
		return is_otros;
	}

	/**
	* Obtiene el nombre del servicio (parametrizado en el sistema) de la solicitud de procedimiento
	*/
	public String getNombreServicioSolicitado()
	{
		return iidi_servicioSolicitado.getNombre();
	}

	/** Inicializa el acceso a bases de datos de este objeto. */
	public void init()
	{
		String ls_tipoBD;

		/* Inicializar el acceso a base de datos de la solicitud general */
		super.init(ls_tipoBD = System.getProperty("TIPOBD") );

		/* Obtener el DAO que encapsula las operaciones de BD de este objeto */
		if(ispd_dao == null)
			ispd_dao = DaoFactory.getDaoFactory(ls_tipoBD).getSolicitudProcedimientoDao();
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param nombreOtros
	 * @param comentarios
	 * @param numeroDocumento
	 * @param esMultiple
	 * @param frecuencia
	 * @param tipoFrecuencia
	 * @param finalidad
	 * @param respuestaMultiple
	 * @param finalizadaRespuesta
	 * @param cuenta
	 * @param finalizar
	 * @return
	 */
	public static boolean insertarSolProc(	Connection con, 
											int numeroSolicitud, 
											int servicio, 
											String nombreOtros, 
											String comentarios, 
											int numeroDocumento, 
											boolean esMultiple,
											float frecuencia, 
											int tipoFrecuencia, 
											int finalidad, 
											boolean respuestaMultiple, 
											boolean finalizadaRespuesta, 
											int cuenta, 
											boolean finalizar,
											String portatil
											)
	{
		try 
		{
			if(	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().insertarTransaccional(	con,
																																ConstantesBD.continuarTransaccion,
																																numeroSolicitud,
																																servicio,
																																nombreOtros,
																																comentarios,
																																numeroDocumento,
																																esMultiple,
																																frecuencia,
																																tipoFrecuencia,
																																finalidad,
																																respuestaMultiple,
																																finalizadaRespuesta,
																																cuenta,
																																finalizar,
																																portatil
				)>0)
			{
				/*
				 * Se insertó correctamente la solicitud
				 */
				return true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de verificar si el servicio es un portatil
	 * @param con
	 * @param numSol
	 * @param codServ
	 * @return true/fasle
	 */
	public static boolean esPortatil(Connection con,String numSol,String codServ)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().esPortatil(con, numSol, codServ);
	}
	
	
	/** Inserta una solicitud de procedimiento en una fuente de datos
	 * 
	 * @param con
	 * @param estado
	 * @param numeroDocumento
	 * @param idCuenta
	 * @param finalizar Finalizar la solicitud multiple anterior
	 * @return numero de solicitud que se acabó de insertar
	 * @throws SQLException
	 */ 
	public int insertarTransaccional(Connection con, String estado, int numeroDocumento, int idCuenta, boolean finalizar,String portatil)throws SQLException
	{
		String ls_estadoSolicitud;
		String ls_estadoSolicitudProcedimiento;
		this.numeroDocumento = numeroDocumento;

		/* Iniciar los estados de transacción */
		if(estado != null)
		{
			ls_estadoSolicitud = ls_estadoSolicitudProcedimiento = "";

			/* Si el estado es empezar la inserción de la solicitud debe iniciar la transacción */
			if(estado.equals(ConstantesBD.inicioTransaccion) )
				ls_estadoSolicitud = estado;
			/*
				Si el estado es finalizar la inserción de la solicitud de procedimiento debe
				terminar la transacción
			*/
			if(estado.equals(ConstantesBD.finTransaccion) )
				ls_estadoSolicitudProcedimiento = estado;
		}
		else
		{
			/* El estado es inválido. Permitir que cada objeto maneje esta situación */
			ls_estadoSolicitud = ls_estadoSolicitudProcedimiento = null;
		}
 
		/* Insertar los datos generales de la solicitud */
		int resp=insertarSolicitudGeneralTransaccional(con, ls_estadoSolicitud);
		
		/*
         * Cambio 1.50 Anexo Solicitud de Procedimientos
         * Se adiciona las N solicitudes generadas
         * para entregarselas al proceso de 
         * autorización población capitada 
         * @author Diana Carolina G 
         */
		this.listaNumerosSolicitud.add(resp);

		setNumeroSolicitud(resp);
		/* Insertar los datos particulares de la solicitud de procedimiento */
		
		if(	ispd_dao.insertarTransaccional(
				con,
				ls_estadoSolicitudProcedimiento,
				getNumeroSolicitud(),
				getCodigoServicioSolicitado(),
				getNombreOtros(),
				getComentario(),
				numeroDocumento,
				this.getMultiple(),
				this.frecuencia,
				this.tipoFrecuencia,
				this.finalidad,
				this.respuestaMultiple,
				this.finalizadaRespuesta,
				idCuenta,
				finalizar,
				portatil
				
			)>0)
		{
			/*
			 * Se insertó correctamente la solicitud
			 */
			return resp;
		}
		else
		{
			return 0;
		}
	}

	/** Indica si la solicitud de procedimiento tiene un cometario para adicionar */
	public boolean isAdicionarComentario()
	{
		return is_comentarioAdicional != null && is_comentarioAdicional.length() > 0;
	}

	/** Indica si el procedimiento pertencece a un servicio no parametrizado */
	public boolean isOtroServicio()
	{
		return is_otros != null && is_otros.length() > 0;
	}

	/** Modifica los datos particulares de la solicitud de procedimiento */
	public boolean modificarSolicitudProcedimiento(Connection ac_con,  UsuarioBasico usuario)throws Exception
	{
		int li_modificados;

		
		/*log+="\n-========= COMENTARIO DESPUES DE MODIFICACIÓN ==============-\n"+
			" Comentario: \n"+is_comentarioAdicional;
		
		//LogsAxioma.enviarLog(ConstantesBD.logComentarioProcedimientoCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
		*/
		if( isAdicionarComentario() )
		{
			li_modificados =
				ispd_dao.modificar(ac_con, getNumeroSolicitud(), is_comentarioAdicional);

			setComentarioAdicional("");
		}
		else
			li_modificados = 1;

		return li_modificados == 1;
	}
	
	
	/** Metodo para Finalizar una Solicitud de procedimiento 
	 *
	 * */
	public int finalizarSolicitudMultiple(Connection con, int	numeroSolicitud)throws Exception
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")); 
		int  resp1=0;
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		resp1 = ispd_dao.finalizarSolicitudMultiple(con , getNumeroSolicitud());
	
		if ( !inicioTrans || resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}

	/** Modifica los datos particulares de la solicitud de procedimiento */
	/*public boolean modificarNumeroAutorizacion(Connection ac_con)throws Exception
	{
		boolean	lb_resp;
		String	ls_numeroAutorizacion;

		lb_resp					= true;
		ls_numeroAutorizacion	= getNumeroAutorizacion();

		if(ls_numeroAutorizacion != null && ls_numeroAutorizacion.length() > 0)
			lb_resp =
				ispd_dao.modificarNumeroAutorizacion(
					ac_con,
					getNumeroSolicitud(),
					ls_numeroAutorizacion
				) == 1;

		return lb_resp;
	}*/

	/**
	* Establece el código del servicio (parametrizado en el sistema) de la solicitud de
	* procedimiento
	*/
	public void setCodigoServicioSolicitado(int ai_servicioSolicitado)
	{
		iidi_servicioSolicitado.setCodigo(ai_servicioSolicitado < 0 ? -1 : ai_servicioSolicitado);

		if(iidi_servicioSolicitado.getCodigo() > -1)
			setNombreOtros("");
	}

	/** Establece el comentario de la solicitud de procedimiento */
	public void setComentario(String as_comentario)
	{
		if(as_comentario != null)
			is_comentario = as_comentario.trim();
	}

	/** Establece el comentario adicional de la solicitud de procedimiento */
	public void setComentarioAdicional(String as_comentarioAdicional)
	{
		if(as_comentarioAdicional != null)
			is_comentarioAdicional = as_comentarioAdicional.trim();
	}

	/**
	* Establece el nombre del servicio (no parametrizado en el sistema) de la solicitud de
	* procedimiento
	*/
	public void setNombreOtros(String as_otros)
	{
		if(as_otros != null)
			is_otros = as_otros.trim();

		if(is_otros.length() > 0)
			setCodigoServicioSolicitado(-1);
	}

	/**
	* Establece el nombre del servicio (parametrizado en el sistema) de la solicitud de
	* procedimiento
	*/
	public void setNombreServicioSolicitado(String as_servicioSolicitado)
	{
		iidi_servicioSolicitado.setNombre(
			as_servicioSolicitado != null ? as_servicioSolicitado.trim() : ""
		);
	}
	
	
	/**
	 * @return (Metodo GET) Returns the numeroDocumento.
	 */
	public int getNumeroDocumento()
	{
		return numeroDocumento;
	}
	/**
	 * @param numeroDocumento The numeroDocumento to set (Metodo SET).
	 */
	public void setNumeroDocumento(int numeroDocumento)
	{
		this.numeroDocumento= numeroDocumento;
	}
	/**
	 * Método para consultar el numero de documento siguiente
	 * para insertar en la solicitud de procedimientos
	 * @param con
	 * @return numero de documento
	 */
	public int numeroDocumentoSiguiente(Connection con)
	{
		return ispd_dao.numeroDocumentoSiguiente(con);
	}
	/**
	 * Metodo para cargar los procedimientos con su codgio, descripcion, especialidad, 
	 * cantidad y si es pos segun el numero de Documento que reciba
	 * @param con
	 * @param numeroDocumento
	 * @return col Collection para recorrerla en la impresion
	 */
	public  Collection solicitudesXDocumento(Connection con, int numeroDocumento)
    {
		return ispd_dao.solicitudesXDocumento(con, numeroDocumento);
    }
	
	/**
	 * Método que me carga la descripcion de una solictud de servicio externo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public  String consultarExterno(Connection con, int numeroSolicitud)
    {
		return ispd_dao.consultarExterno(con, numeroSolicitud);
    }

	/**
	 * @return Retorna multiple.
	 */
	public boolean getMultiple()
	{
		return multiple;
	}

	/**
	 * @param multiple Asigna multiple.
	 */
	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}

	/**
	 * @return Retorna frecuencia.
	 */
	public float getFrecuencia()
	{
		return frecuencia;
	}

	/**
	 * @param frecuencia Asigna frecuencia.
	 */
	public void setFrecuencia(float frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	/**
	 * @return Retorna tipoFrecuencia.
	 */
	public int getTipoFrecuencia()
	{
		return tipoFrecuencia;
	}

	/**
	 * @param tipoFrecuencia Asigna tipoFrecuencia.
	 */
	public void setTipoFrecuencia(int tipoFrecuencia)
	{
		this.tipoFrecuencia = tipoFrecuencia;
	}

	public int getFinalidad() {
		return finalidad;
	}

	public void setFinalidad(int finalidad) {
		this.finalidad = finalidad;
	}

	public String getNombreFinalidad() {
		return nombreFinalidad;
	}

	public void setNombreFinalidad(String nombreFinalidad) {
		this.nombreFinalidad = nombreFinalidad;
	}

	/**
	 * @return Returns the esPos.
	 */
	public String getEsPos() {
		return esPos;
	}

	/**
	 * @param esPos The esPos to set.
	 */
	public void setEsPos(String esPos) {
		this.esPos = esPos;
	}

	/**
	 * @return Returns the esRespuestaMultiple.
	 */
	public boolean getEsRespuestaMultiple() {
		return esRespuestaMultiple;
	}

	/**
	 * @param esRespuestaMultiple The esRespuestaMultiple to set.
	 */
	public void setEsRespuestaMultiple(boolean esRespuestaMultiple) {
		this.esRespuestaMultiple = esRespuestaMultiple;
	}

	/**
	 * @return Returns the estaFinalizadaRespuestaMultiple.
	 */
	public boolean getEstaFinalizadaRespuestaMultiple() {
		return estaFinalizadaRespuestaMultiple;
	}

	/**
	 * @param estaFinalizadaRespuestaMultiple The estaFinalizadaRespuestaMultiple to set.
	 */
	public void setEstaFinalizadaRespuestaMultiple(
			boolean estaFinalizadaRespuestaMultiple) {
		this.estaFinalizadaRespuestaMultiple = estaFinalizadaRespuestaMultiple;
	}

	public boolean isFinalizadaRespuesta()
	{
		return finalizadaRespuesta;
	}

	public void setFinalizadaRespuesta(boolean finalizadaRespuesta)
	{
		this.finalizadaRespuesta = finalizadaRespuesta;
	}

	public boolean isRespuestaMultiple()
	{
		return respuestaMultiple;
	}

	public void setRespuestaMultiple(boolean respuestaMultiple)
	{
		this.respuestaMultiple = respuestaMultiple;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param fechaSolicitud
	 * @param horaSolicitud
	 * @return
	 */
	public Collection solicitudesXCuentaFechaHora(Connection con, int codigoCuenta, String fechaSolicitud, String horaSolicitud, int institucion)
	{
		return ispd_dao.solicitudesXCuentaFechaHora(con, codigoCuenta,fechaSolicitud,horaSolicitud, institucion);
	}

	/**
	 * Metodo que devuelve el codigo del portatil asociado a la solicitud de proc, si no existe entonces -1
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerPortatilSolicitud(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().obtenerPortatilSolicitud(con, numeroSolicitud);
	}
	
	
	/**
	 * Metodo que devuelve el codigo del portatil asociado a un servicio, si no existe entonces -1
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerPortatilServicio(Connection con, int codServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().obtenerPortatilServicio(con, codServicio);
	}
	
	
	/**
	 * Metodo encargado de Anular o Aprobar un portatil
	 * @param con
	 * @param datos
	 * Jhony alexander Duque A.
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- portatil --> Opcional
	 * -- motAnuPort --> Opcional
	 * -- numeroSolicitud --> Requerido
	 * @return
	 */
	public static boolean anularAprobarPortatil(Connection con,HashMap datos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().anularAprobarPortatil(con, datos);
	}

	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * Método que devuelve todas las solicitudes para realizar el
	 * proceso de impresión de media página
	 * @param con
	 * @param numerosSolicitudes
	 * @return
	 */
	public String obtenerWhereFormatoMediaCarta(Connection con, HashMap numerosSolicitudes)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().obtenerWhereFormatoMediaCarta(con, numerosSolicitudes);
	}

	public String[] obtenerConsultaSolicitudProcedimientosReporte(Connection con, HashMap numerosSolicitudes)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().obtenerConsultaSolicitudProcedimientosReporte(con, numerosSolicitudes);
	}

	
	/**
	 * Método para registrar los servicios incluidos
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param infoInterfaz 
	 * @return
	 */
	public boolean registrarServiciosIncluidos(Connection con,UsuarioBasico usuario,PersonaBasica paciente) throws IPSException
	{
		HashMap<Object,Object> serviciosIncluidos = Servicios_ArticulosIncluidosEnOtrosProcedimientos.cargarServiciosIncluidosServicioPrincipal(con, this.getCodigoServicioSolicitado(), true /*activo*/, usuario.getCodigoInstitucionInt());
		HashMap<Object, Object> articulosIncluidos = Servicios_ArticulosIncluidosEnOtrosProcedimientos.cargarArticulosIncluidosServicioPrincipal(con, this.getCodigoServicioSolicitado(), true, usuario.getCodigoInstitucionInt());
		
		
		//PARTE DE SERVICIOS***********************************************************************************************************
		for(int w=0; w<Utilidades.convertirAEntero(serviciosIncluidos.get("numRegistros_"+this.getCodigoServicioSolicitado())+"");w++)
		{
			
			
			if(UtilidadTexto.getBoolean(serviciosIncluidos.get("confirmar_"+w+"_"+this.getCodigoServicioSolicitado())+""))
			{	
				for(int x=0; x<Utilidades.convertirAEntero(serviciosIncluidos.get("cantidad_"+w+"_"+this.getCodigoServicioSolicitado())+""); x++)
				{
					int numeroSolicitudServIncluida = ConstantesBD.codigoNuncaValido;
					
					DtoServicioIncluidoSolProc dto= new DtoServicioIncluidoSolProc();
					dto.setSolicitudPpal(this.getNumeroSolicitud());
					dto.setUsuarioModifica(usuario.getLoginUsuario());
					dto.setServicioPpal(this.getCodigoServicioSolicitado());
					dto.setServicioIncluido(Utilidades.convertirAEntero(serviciosIncluidos.get("cod_serv_incluido_"+w+"_"+this.getCodigoServicioSolicitado())+""));
					dto.setCentroCostoEjecuta(Utilidades.convertirAEntero(serviciosIncluidos.get("cod_centro_costo_ejecuta_"+w+"_"+this.getCodigoServicioSolicitado())+""));
					dto.setEsPos(UtilidadTexto.getBoolean(serviciosIncluidos.get("es_pos_serv_incluido_"+w+"_"+this.getCodigoServicioSolicitado())+""));
					
					dto.log();
					
					//primero evaluamos si es un servicio no cruento o procedimiento
					String tipoServicio= Utilidades.obtenerTipoServicio(con, dto.getServicioIncluido()+"");
					
					//*********************************************************************************************************
					//******************GENERACION SOLICITUD SERVICIO NO CRUENTOS***********************************************
					//*************************************************************************************************************
					if(tipoServicio.trim().equals(ConstantesBD.codigoServicioNoCruentos+""))
					{
						SolicitudProcedimiento solicitudProcedimientoNueva= llenarSolicitudProcIncluida(con, dto, ConstantesBD.codigoTipoSolicitudCirugia);
						 
						
						int numSolCx = ConstantesBD.codigoNuncaValido;
				        try
				        {
				            numSolCx=solicitudProcedimientoNueva.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
				            //logger.info("numeroSolicitud CX->"+numeroSolicitud);            
				            
				           numeroSolicitudServIncluida=numSolCx;
				        }
				        catch(SQLException sqle)
				        {
				            logger.warn("Error al generar la solicitud basica de cirugías: "+sqle);
				            return false;
				            
				        }
				        //************************************************************************************
				        
				        if(numeroSolicitudServIncluida>0)
				        {
							SolicitudesCx mundoSolCx= new SolicitudesCx();
					        
					        int codigoPeticion=ConstantesBD.codigoNuncaValido;
					        String manejoProgSalas="";
					        HashMap peticionEncabezadoMap= new HashMap();
					        Cuenta mundoCuenta= new Cuenta();
					        mundoCuenta.cargarCuenta(con, solicitudProcedimientoNueva.getCodigoCuenta()+"");
					        peticionEncabezadoMap.put("tipoPaciente",   mundoCuenta.getCodigoTipoPaciente());
					        peticionEncabezadoMap.put("fechaPeticion", solicitudProcedimientoNueva.getFechaSolicitud());
					        peticionEncabezadoMap.put("horaPeticion", solicitudProcedimientoNueva.getHoraSolicitud());
					        peticionEncabezadoMap.put("solicitante", usuario.getCodigoPersona()+"");
					        peticionEncabezadoMap.put("duracion", "");
					        manejoProgSalas=ValoresPorDefecto.getManejoProgramacionSalasSolicitudesDyt(usuario.getCodigoInstitucionInt());
					        if(manejoProgSalas.equals(ConstantesBD.acronimoNo))
					        	peticionEncabezadoMap.put("programable", ConstantesBD.acronimoNo);
					        else if(manejoProgSalas.equals(ConstantesBD.acronimoSi))
					        	peticionEncabezadoMap.put("programable", ConstantesBD.acronimoSi);
					        
					        //Datos del servicio
					        HashMap serviciosPeticionMap=new HashMap();
					        serviciosPeticionMap.put("numeroFilasMapaServicios", "1");
					        serviciosPeticionMap.put("fueEliminadoServicio_0", "false");
					        serviciosPeticionMap.put("codigoServicio_0", solicitudProcedimientoNueva.getCodigoServicioSolicitado()+"");
					        serviciosPeticionMap.put("codigoEspecialidad_0", ""); //va vaío
					        serviciosPeticionMap.put("codigoTipoCirugia_0", "-1"); //no se ingresa informacion
					        serviciosPeticionMap.put("numeroServicio_0", "1");
					        serviciosPeticionMap.put("observaciones_0", solicitudProcedimientoNueva.getComentario());
					        
					        HashMap articulosPeticionMap= new HashMap();
					        articulosPeticionMap.put("numeroMateriales", 0);
					        articulosPeticionMap.put("numeroOtrosMateriales", 0);
					        HashMap profesionalesMap= new HashMap();
					        profesionalesMap.put("numeroProfesionales", 0);
					        Peticion mundoPeticion= new Peticion();
					        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(con, peticionEncabezadoMap, serviciosPeticionMap, profesionalesMap, articulosPeticionMap, solicitudProcedimientoNueva.getCodigoPaciente(), ConstantesBD.codigoNuncaValido, usuario, true, false);
					        codigoPeticion=codigoPeticionYNumeroInserciones[1];
					        
					        if(codigoPeticion>0)
					        {
					        
						        double subCuenta=ConstantesBD.codigoNuncaValido;
						    	//evaluamos la cobertura
								InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
								int codigoViaIngreso= Integer.parseInt(mundoCuenta.getCodigoViaIngreso());
								infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, mundoCuenta.getCodigoIngreso()+"", codigoViaIngreso, mundoCuenta.getCodigoTipoPaciente(), solicitudProcedimientoNueva.getCodigoServicioSolicitado(), usuario.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);
								subCuenta=infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble();
								//LUEGO SE INSERTAN LOS SERVICIOS DE LA ORDEN
						        String temporalCodigoServicio="";
						        int temporalCodigoCirugia= ConstantesBD.codigoNuncaValido;
						        int temporalConsecutivo= 1;
						        String temporalObservaciones="";
						        temporalCodigoServicio= solicitudProcedimientoNueva.getCodigoServicioSolicitado()+"";
						        try {
						        	if(!mundoSolCx.insertarSolicitudCxGeneralTransaccional1(con, numeroSolicitudServIncluida+"", codigoPeticion+"", false, ConstantesBD.inicioTransaccion, subCuenta, ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
						        		return false;
						        	
									if(mundoSolCx.insertarSolicitudCxXServicioTransaccional(
						                	con, 
						                	numeroSolicitudServIncluida+"", 
						                	temporalCodigoServicio, 
						                	temporalCodigoCirugia, 
						                	temporalConsecutivo, 
						                	ConstantesBD.codigoNuncaValido, //esquema tarifario
						                	ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr
						                	usuario.getCodigoInstitucionInt(), 
						                	/*"", -- autorizacion*/
						                	solicitudProcedimientoNueva.getFinalidad(), //finalidad
						                	temporalObservaciones, 
						                	"", //via Cx 
						                	"", //indicativo bilateral
						                	"", //indicativo via de acceso
						                	ConstantesBD.codigoNuncaValido, //codigo de la especialidad
						                	"", //liquidar servicio
						                	ConstantesBD.continuarTransaccion, "",
						                	null)<=0);
										return false;
									
									
								} 
						        catch (SQLException e) 
								{
									e.printStackTrace();
								}
						        
						        logger.info("servicio no cruento por lo tanto inserta una cx!!!!!!!!!!!!");
								
								dto.setSolicitudIncluida(numeroSolicitudServIncluida);
								dto.log();
					        }
					        else //error insertando peticion qx
					        	return false;
								
								
								
							
				        }
				        else //error insertando solicitud base
				        	return false;
					}
					//***********************************************************************************************************************
					//****************************GENERACION SOLICITUD TIPO PROCEDIMIENTOS*****************************************************
					//**************************************************************************************************************************
					else
					{
						SolicitudProcedimiento solicitudProcedimientoNueva= llenarSolicitudProcIncluida(con, dto, ConstantesBD.codigoTipoSolicitudProcedimiento);
						logger.info("servicio procedimiento inserta proc!!!!!!!!!!!!!!!!!!");
						
						HashMap infoInterfaz = new HashMap();
						////*********************************************************************************************
						//****************EDICION ENCABEZADO INTERFAZ LABORATORIOS*************************************
						//**********************************************************************************************
						//Se llenan datos necesarios para interfaz laboratorios
						infoInterfaz.put("numeroDocumento",solicitudProcedimientoNueva.getNumeroDocumento()+"");
						infoInterfaz.put("fechaSolicitud",solicitudProcedimientoNueva.getFechaSolicitud());
						infoInterfaz.put("horaSolicitud",solicitudProcedimientoNueva.getHoraSolicitud());
						infoInterfaz.put("codigoMedico",solicitudProcedimientoNueva.getCodigoMedicoSolicitante()+"");
						infoInterfaz.put("nombreMedico",usuario.getNombreUsuario());
						infoInterfaz.put("institucion",usuario.getCodigoInstitucion());
						infoInterfaz.put("observaciones",solicitudProcedimientoNueva.getComentario());
						Cama cama = new Cama();
						try 
						{
							cama.cargarCama(con,paciente.getCodigoCama()+"");
							infoInterfaz.put("numeroCama",cama.getDescripcionCama());
						} 
						catch (SQLException e) 
						{
							logger.error("Error cargando la cama: "+e);
							infoInterfaz.put("numeroCama","");
						}
						
						if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
						{
							//infoInterfaz.put("comentarioDiagnostico", UtilidadesHistoriaClinica.obtenerDescripcionDxIngresoPaciente(con, paciente.getCodigoCuenta(), paciente.getCodigoUltimaViaIngreso()));
							infoInterfaz.put("horaSistema",UtilidadFecha.getHoraSegundosActual(con));
							infoInterfaz.put("nitEmpresa",UtilidadesFacturacion.obtenerNitEmpresaConvenio(con, paciente.getCodigoConvenio()));
							infoInterfaz.put("nroCarnet",UtilidadesManejoPaciente.obtenerNroCarnetIngresoPaciente(con, paciente.getCodigoIngreso()));
							infoInterfaz.put("codigoEspecialidadSolicitante",solicitudProcedimientoNueva.getEspecialidadSolicitante().getCodigo());
							//infoInterfaz.put("ciePrevio",Utilidades.consultarDiagnosticosPaciente(con, paciente.getCodigoCuenta()+"", paciente.getCodigoUltimaViaIngreso()));
							
						}
						//**********************************************************************************************
						///*********************************************************************************************
						
						boolean dejarPendiente=true;
						
						if(UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, paciente.getCodigoUltimaViaIngreso()+"", ""+solicitudProcedimientoNueva.getCodigoServicioSolicitado(), usuario.getCodigoInstitucion()))
						{
							dejarPendiente=false;
						}
						
						/* Genera ls solicitud */
						try 
						{
							solicitudProcedimientoNueva.insertarTransaccional(con, "", solicitudProcedimientoNueva.getNumeroDocumento(), paciente.getCodigoCuenta(), true,solicitudProcedimientoNueva.getPortatil());
							
							numeroSolicitudServIncluida=solicitudProcedimientoNueva.getNumeroSolicitud();
							
							
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
						
						if(numeroSolicitudServIncluida>0)
						{
							boolean inserto=true;
						
							 //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
						    Cargos cargos= new Cargos();
						    cargos.setPyp(false);
						    inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
						    																			usuario, 
						    																			paciente, 
						    																			dejarPendiente/*dejarPendiente*/, 
						    																			numeroSolicitudServIncluida, 
						    																			ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
						    																			paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
						    																			solicitudProcedimientoNueva.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
						    																			solicitudProcedimientoNueva.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
						    																			1 /*cantidadServicioOPCIONAL*/, 
						    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
						    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
						    																			/*"" -- numeroAutorizacionOPCIONAL*/
						    																			""/*esPortatil*/,false,solicitudProcedimientoNueva.getFechaSolicitud(),"");
						    
						    dto.setSolicitudIncluida(numeroSolicitudServIncluida);
							dto.log();
							if(!inserto)
							{
								return false;
							}
							else
							{
								///******************se completa informacion de la generación de archivo plano interfaz laboratorios*************************************
								infoInterfaz.put("numeroSolicitud_0",solicitudProcedimientoNueva.getNumeroSolicitud()+"");
								infoInterfaz.put("estado_0",ConstantesBD.codigoEstadoHCSolicitada+"");
								infoInterfaz.put("centroCosto_0",solicitudProcedimientoNueva.getCentroCostoSolicitado().getCodigo()+"");
								infoInterfaz.put("urgente_0",solicitudProcedimientoNueva.getUrgente()+"");
								infoInterfaz.put("codigoCUPS_0",Utilidades.obtenerCodigoPropietarioServicio(con,solicitudProcedimientoNueva.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
								infoInterfaz.put("numRegistros","1");
								ActionErrors errores = InterfazLaboratorios.generarRegistroArchivo(infoInterfaz,paciente,ValoresPorDefecto.getCliente(),new ActionErrors());
								if(!errores.isEmpty())
									return false;
								//****************************************************************************************************************************************
								
							}
						}
						else
							return false;
					    
					    
						
						
						
					}
					//********************************************************************************************************************
					//*********************************FIN GENERACION DE SOLICITUDES*********************************************************
					//*************************************************************************************************************************
					
					////////insertar las justificaciones pendientes
					insertarJustificacionesNoPosIncluidos(con, dto, usuario);
					
					///////////se inserta en tablas art_inclu_sol_proc y serv_inclu_sol_proc
					if(!insertarServiciosIncluidosConArticulosIncluidos(con, serviciosIncluidos, dto, w, usuario))
					{
						return false;
					}
					
					
					
					
					logger.info("****************************************************************************************************************************************");
					
				}
			}	
		}
		
		//PARTE DE ARTICULOS**************************************************************************************************************************
		for(int w=0; w<Utilidades.convertirAEntero(articulosIncluidos.get("numRegistros_"+this.getCodigoServicioSolicitado())+"");w++)
		{
			logger.info("*******************ARTICULO "+articulosIncluidos.get("cod_art_incluido_"+w+"_"+this.getCodigoServicioSolicitado())+" INCLUIDO CON CANTIDAD->"+articulosIncluidos.get("cantidad_"+w+"_"+this.getCodigoServicioSolicitado()));
			
			DtoArticuloIncluidoSolProc dtoArt= new DtoArticuloIncluidoSolProc();
			dtoArt.setArticuloIncluido(Utilidades.convertirAEntero(articulosIncluidos.get("cod_art_incluido_"+w+"_"+this.getCodigoServicioSolicitado())+""));
			dtoArt.setCantidad(Utilidades.convertirAEntero(articulosIncluidos.get("cantidad_"+w+"_"+this.getCodigoServicioSolicitado())+""));
			dtoArt.setCantidadMaxima(Utilidades.convertirAEntero(articulosIncluidos.get("cantidad_"+w+"_"+this.getCodigoServicioSolicitado())+""));
			dtoArt.setEsServicioIncluido(false);
			dtoArt.setFarmacia(Utilidades.convertirAEntero(articulosIncluidos.get("cod_farmacia_"+w+"_"+this.getCodigoServicioSolicitado())+""));
			dtoArt.setServicioPpal(this.getCodigoServicioSolicitado());
			dtoArt.setSolicitudIncluida(ConstantesBD.codigoNuncaValido); //esta se inserta en la respuesta
			dtoArt.setSolicitudPpal(this.getNumeroSolicitud());
			dtoArt.setUsuarioModifica(usuario.getLoginUsuario());
			
			logger.info("INSERCION DE LOS ARTICULOS INCLUIDOS DEL SERVICIO PPAL-->"+this.getCodigoServicioSolicitado());
			dtoArt.log();
			
			if(!Servicios_ArticulosIncluidosEnOtrosProcedimientos.insertarArticulosIncluidosSolicitudProcedimientos(con, dtoArt))
			{
				return false;
			}
			
		}
		
		return true;
	}
	
	private boolean insertarServiciosIncluidosConArticulosIncluidos(Connection con, HashMap<Object, Object> serviciosIncluidos,DtoServicioIncluidoSolProc dto, int indice, UsuarioBasico usuario) 
	{
		///insertamos el servicio incluido
		boolean inserto= Servicios_ArticulosIncluidosEnOtrosProcedimientos.insertarServiciosIncluidosSolicitudProcedimientos(con, dto);
		//insertamos los articulos incluidos del servicio incluido
		
		HashMap<Object, Object> articulosMap= (HashMap<Object, Object>)((HashMap<Object, Object>)serviciosIncluidos.get("ARTICULOS_INCLUIDOS_SERVICIO_INCLUIDO_"+indice+"_"+dto.getServicioPpal())).clone();
		
		for(int w=0; w<Utilidades.convertirAEntero(articulosMap.get("numRegistros_"+dto.getServicioIncluido())+""); w++)
		{
			DtoArticuloIncluidoSolProc dtoArt= new DtoArticuloIncluidoSolProc();
			dtoArt.setArticuloIncluido( Utilidades.convertirAEntero(articulosMap.get("cod_art_incluido_"+w)+""));
			dtoArt.setCantidad(Utilidades.convertirAEntero(articulosMap.get("cantidad_"+w)+""));
			dtoArt.setCantidadMaxima(Utilidades.convertirAEntero(articulosMap.get("cantidad_"+w)+""));
			dtoArt.setEsServicioIncluido(true);
			dtoArt.setFarmacia(Utilidades.convertirAEntero(articulosMap.get("cod_farmacia_"+w)+""));
			dtoArt.setServicioPpal(dto.getServicioIncluido()); // en este caso el servicio ppal es el servicio incluido
			dtoArt.setSolicitudIncluida(ConstantesBD.codigoNuncaValido); //nunca se genera solicitud de articulos incluidos
			dtoArt.setSolicitudPpal(dto.getSolicitudIncluida()); //en este caso la sol ppal del articulo es la solicitud del servicio incluido
			dtoArt.setUsuarioModifica(usuario.getLoginUsuario());
			
			logger.info("INSERCION DE LOS ARTICULOS INCLUIDOS DEL SERVICIO INCLUUIDO-->"+dto.getServicioIncluido());
			dtoArt.log();
			
			inserto=Servicios_ArticulosIncluidosEnOtrosProcedimientos.insertarArticulosIncluidosSolicitudProcedimientos(con, dtoArt);
			if(!inserto)
				w=Utilidades.convertirAEntero(articulosMap.get("numRegistros_"+dto.getServicioIncluido())+"");
		}
		
		return inserto;
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 */
	private void insertarJustificacionesNoPosIncluidos(Connection con,DtoServicioIncluidoSolProc dto, UsuarioBasico usuario) throws IPSException 
	{
		logger.info("\n********************JUSTIFICACION NO POS ->");
		logger.info("********************SOLICITUD INCLUIDA->"+dto.getSolicitudIncluida()+" servicio incluido->"+dto.getServicioIncluido());
		if(!dto.getEsPos())
		{
			logger.info("solicitud no pos!!!!");
			//verificamos la param del convenio que lo cubre
			ArrayList<DtoSubCuentas> dtoResponsable= UtilidadesHistoriaClinica.obtenerResponsablesSolServArt(con, dto.getSolicitudIncluida(), dto.getServicioIncluido(), true);
			if(dtoResponsable.size()==1)
			{
				logger.info("convenio responsable-->"+dtoResponsable.get(0).getConvenio().getCodigo()+" - "+dtoResponsable.get(0).getConvenio().getNombre());
				if (UtilidadesFacturacion.requiereJustificacioServ(con, dtoResponsable.get(0).getConvenio().getCodigo()))
				{	
					double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, dto.getServicioIncluido(), dto.getSolicitudIncluida(), dtoResponsable.get(0).getConvenio().getCodigo(), false);
					logger.info("REQUIERE JUSTIFICACION!!!!!!!!!!!");
					UtilidadJustificacionPendienteArtServ.insertarJusNP(con, dto.getSolicitudIncluida(), dto.getServicioIncluido(), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),"");
				}	
			}
		}
		logger.info("********************FINNNNNNNNN    JUSTIFICACION NO POS \n");
	}
	
	
	/**
	 * Método para llenar una nueva solicitud de procedimientos
	 * @param con
	 * @param dto
	 * @param tipoSolicitud
	 * @return
	 */
	private SolicitudProcedimiento llenarSolicitudProcIncluida(Connection con,DtoServicioIncluidoSolProc dto,int tipoSolicitud)
	{
		SolicitudProcedimiento solicitudProcedimientoNueva= new SolicitudProcedimiento();
		
		solicitudProcedimientoNueva.setCentroCostoSolicitado(new InfoDatosInt(dto.getCentroCostoEjecuta()));
		solicitudProcedimientoNueva.setCentroCostoSolicitante(this.getCentroCostoSolicitante());
		solicitudProcedimientoNueva.setCobrable(this.isCobrable());
		solicitudProcedimientoNueva.setCodigoCentroAtencionCuentaSol(this.getCodigoCentroAtencionCuentaSol());
		solicitudProcedimientoNueva.setCodigoCuenta(this.getCodigoCuenta());
		
		solicitudProcedimientoNueva.setCodigoPaciente(this.getCodigoPaciente());
		solicitudProcedimientoNueva.setCodigoServicioSolicitado(dto.getServicioIncluido());
		solicitudProcedimientoNueva.setComentario(this.getComentario());
		solicitudProcedimientoNueva.setComentarioAdicional("");
		
		//@todo verificar el consecutivo de orden medica insertado.........
		
		/////revisar el consecutivo de ordenes medicas
		solicitudProcedimientoNueva.setDatosMedico(this.getDatosMedico());
		solicitudProcedimientoNueva.setDatosMedicoResponde(this.getDatosMedicoResponde());
		solicitudProcedimientoNueva.setEspecialidadSolicitada(this.getEspecialidadSolicitada());
		solicitudProcedimientoNueva.setEspecialidadSolicitante(this.getEspecialidadSolicitante());
		solicitudProcedimientoNueva.setEsPos(UtilidadTexto.convertirSN(dto.getEsPos()+""));
		solicitudProcedimientoNueva.setEsRespuestaMultiple(false);
		solicitudProcedimientoNueva.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada,""));
		solicitudProcedimientoNueva.setEstaFinalizadaRespuestaMultiple(false); 
		solicitudProcedimientoNueva.setFechaAnulacion(this.getFechaAnulacion());
		solicitudProcedimientoNueva.setFechaGrabacion(this.getFechaGrabacion());
		solicitudProcedimientoNueva.setFechaInterpretacion(this.getFechaInterpretacion());
		solicitudProcedimientoNueva.setFechaRespuesta(this.getFechaRespuesta());
		solicitudProcedimientoNueva.setFechaSolicitud(this.getFechaSolicitud());
		solicitudProcedimientoNueva.setFinalidad(this.getFinalidad());
		solicitudProcedimientoNueva.setFinalizadaRespuesta(this.isFinalizadaRespuesta());
		solicitudProcedimientoNueva.setFrecuencia(this.getFrecuencia());
		solicitudProcedimientoNueva.setHoraAnulacion(this.getHoraAnulacion());
		solicitudProcedimientoNueva.setHoraGrabacion(this.getHoraGrabacion());
		solicitudProcedimientoNueva.setHoraInterpretacion(this.getHoraInterpretacion());
		solicitudProcedimientoNueva.setHoraRespuesta(this.getHoraRespuesta());
		solicitudProcedimientoNueva.setHoraSolicitud(this.getHoraSolicitud());
		solicitudProcedimientoNueva.setImpresion(this.getImpresion());
		solicitudProcedimientoNueva.setInterpretacion(this.getInterpretacion());
		
		//@todo revisar lo de las justificaciones
		
		solicitudProcedimientoNueva.setLiquidarAsocio(this.getLiquidarAsocio());
		solicitudProcedimientoNueva.setLoginMedico(this.getLoginMedico());
		solicitudProcedimientoNueva.setMotivoAnulacion(this.getMotivoAnulacion());
		solicitudProcedimientoNueva.setMotivoAnulacionPort(this.getMotivoAnulacionPort());
		solicitudProcedimientoNueva.setMultiple(false);
		solicitudProcedimientoNueva.setNombreCentroAtencionCuentaSol(this.getNombreCentroAtencionCuentaSol());
		solicitudProcedimientoNueva.setNombreCentroCostoSolicitado(this.getNombreCentroCostoSolicitado());
		solicitudProcedimientoNueva.setNombreFinalidad(this.getNombreFinalidad());
		solicitudProcedimientoNueva.setNombreMedico(this.getNombreMedico());
		solicitudProcedimientoNueva.setNombreMedicoResponde(this.getNombreMedicoResponde());
		solicitudProcedimientoNueva.setNombreOtros(this.getNombreOtros());
		solicitudProcedimientoNueva.setNombrePaciente(this.getNombrePaciente());
		solicitudProcedimientoNueva.setNombreServicioSolicitado("");
		//solicitudProcedimientoNueva.setNumeroAutorizacion(this.getNumeroAutorizacion());
		solicitudProcedimientoNueva.setNumeroDocumento(this.numeroDocumentoSiguiente(con));
		
		//@todo setear el numero de solicitud
		
		solicitudProcedimientoNueva.setOcupacionSolicitado(this.getOcupacionSolicitado());
		solicitudProcedimientoNueva.setOrdenAmbulatoria(this.getOrdenAmbulatoria());
		solicitudProcedimientoNueva.setPoolMedico(this.getPoolMedico());
		solicitudProcedimientoNueva.setPortatil(ConstantesBD.codigoNuncaValido+"");
		solicitudProcedimientoNueva.setRespuestaMultiple(Utilidades.esServicioRespuestaMultiple(con, dto.getServicioIncluido()+""));
		
		solicitudProcedimientoNueva.setSolPYP(this.isSolPYP());
		solicitudProcedimientoNueva.setTieneCita(false);
		solicitudProcedimientoNueva.setTipoFrecuencia(this.getTipoFrecuencia());
		solicitudProcedimientoNueva.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
		solicitudProcedimientoNueva.setUrgente(this.getUrgente());
		solicitudProcedimientoNueva.setVaAEpicrisis(this.isVaAEpicrisis()); 
		return solicitudProcedimientoNueva;
	}

	/**
	 * @return the codigoTipoServicio
	 */
	public String getCodigoTipoServicio() {
		return codigoTipoServicio;
	}

	/**
	 * @param codigoTipoServicio the codigoTipoServicio to set
	 */
	public void setCodigoTipoServicio(String codigoTipoServicio) {
		this.codigoTipoServicio = codigoTipoServicio;
	}
	private static SolicitudProcedimientoDao getSolicitudProcedimientoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao();
	}
	public HashMap valANulacion(Connection ac_con, int numeroSolicitud) {
		// TODO Auto-generated method stub
		return getSolicitudProcedimientoDao().valAnulacion(ac_con,numeroSolicitud);
		//return getMotivosAnulacionCondonacionDao().consultarMotivosAnulacionCondonacionMultas();
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 */
	public static boolean finalizarSolicitudMultiple(Connection con,String numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().finalizarSolicitudMultiple(con,numeroSolicitud);
	}
	
	/**
	 * M&eacute;todo encargado de consultar los servicios
	 * asociados a la solicitud de procedimientos
	 * @param Connection con, String numeroDocumento
	 * @return ArrayList<DtoServicios>
	 * @author Diana Carolina G
	 */
	public DtoProcedimiento buscarServiciosSolicitudProcedimientos(Connection con,
			int numeroSolicitud, int codigoTarifario){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().buscarServiciosSolicitudProcedimientos(con, numeroSolicitud, codigoTarifario);
	}
	/**
	 * M&eacute;todo encargado de consultar los servicios
	 * asociados a la solicitud de procedimientos
	 * @param Connection con, String numeroDocumento
	 * @return ArrayList<DtoServicios>
	 * @author Diana Carolina G
	 */
	public DtoProcedimiento buscarServiciosSolicitudProcedimientosC(Connection con,
			int numeroSolicitud, int codigoTarifario){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().buscarServiciosSolicitudProcedimientosC(con, numeroSolicitud, codigoTarifario);
	}


	public void setListaNumerosSolicitud(List<Integer> listaNumerosSolicitud) {
		this.listaNumerosSolicitud = listaNumerosSolicitud;
	}

	public List<Integer> getListaNumerosSolicitud() {
		return listaNumerosSolicitud;
	}
	
	/**
	 * @param conn
	 * @param codigoMedico
	 * @return datos del medico que anula
	 * @throws SQLException
	 */
	public  String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudProcedimientoDao().consultarDatosMedicoAnulacion(conn, codigoMedico);
	}
	
}