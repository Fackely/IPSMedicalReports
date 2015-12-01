/*
 * @(#)SolicitudInterconsulta.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudInterconsultaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.parametrizacion.Servicio;

/**
 * Esta clase encapsula maneja los datos y funcionalidades particulares
 * de la solicitud de interconsulta.
 *
 * @version 1.0, Feb 10, 2004
 */

public class SolicitudInterconsulta extends Solicitud
{
		/**
		*  obtener el número de los documentos adjuntos
		 */
		private int numDocumentosAdjuntos=0;
		
		/**
		 * Vector donde quedan almacenados los nombres ORIGINALES de los documentos adjuntos
		 */
		private Vector nombresOriginalesDocumentosAdjuntos=new Vector();
		
		/**
		 * Vector donde quedan almacenados los nombres que fuenron generados de los documentos adjuntos
		 */
		private Vector nombresGeneradosDocumentosAdjuntos=new Vector();
			
		/**
		 * Objeto para contener los documentos adjuntos
		 */
		private DocumentosAdjuntos documentosAdjuntos = new DocumentosAdjuntos();
		   
		/**
		* Objeto para manejar los logs de esta clase
		*/
		private static Logger logger= Logger.getLogger(SolicitudInterconsulta.class);
		
		/**
		 * Para obtener número de la interconsulta parametrizada
		 */		
		private int codigoServicioSolicitado;
		
		/**
		 *  interconsulta no parametrizada 
		 */
		private String nombreOtros;
		
		/**
		 * String que contiene el nombre del código del Servicio Solicitado
		 */
		private String nombreCodigoServicioSolicitado;
		
		/**
		 * String que contiene el acronimo de dias tramite urgente o normal grupo del Servicio Solicitado
		 */
		private String acronimoDiasTramite; 
		
		/**
		 *  motivo solicitud interconsulta  
		 */
		private String motivoSolicitud;
		
		/**
		 * String que se utiliza en la modificación de solicitud de Interconsulta
		 * para añadir otro motivo de solicitud
		 */
		private String motivoSolicitudNueva;
		
		/**
		 * comentarios asociado a la interconsulta
		 */
		private String comentario;
		
		/**
		 * String que se utiliza en la modificación de solicitud de Interconsulta
		 * para añadir otro comentario
		 */
		private String comentarioNuevo;
		
		
		/**
		* resumen Historia clinica asociado a la interconsulta
		*/
		private String resumenHistoriaClinica;
		
		/**
		 * String que se utiliza en la modificación de solicitud de Interconsulta
		 * para añadir otro resumen de Historia Clínica.
		 */
		private String resumenHistoriaClinicaNueva;
		
		/**
		 * El DAO usado por el objeto <code>Solicitud</code> para acceder 
		 * a la fuente de datos.
		 */
		private static SolicitudInterconsultaDao solicitarInterconsultaDao;
	
		/**
		 *@param  
		 * Opciones de manejo interconsulta
		 *  1) se desea un concepto solamente
		 *  2) Se desea un conceto solamente
		 *  3) Manejo conjunto  
		 */
		private int codigoManejointerconsulta;
		
		/**
		 * String para contener la respuesta de una
		 * solicitud de interconsulta
		 */
		private String respuestaOtros;
		
		
		
		/**
		 * para obtener el código y el nombre de la selección de manejo
		 */
		private InfoDatosInt SeleccionManejo;
	
		/**
		 * codigo del paciente 
		 */
		private int codigoPaciente;
	
		/**
		* Constructor con todos los campos de entrada
		 */
		public SolicitudInterconsulta(int numeroSolicitud, int codigoServicioSolicitado, String nombreOtros, String motivoSolicitud, String comentario, String resumenHistoriaClinica, int codigoManejointerconsulta)
		{
				super.init(System.getProperty("TIPOBD"));
				init(System.getProperty("TIPOBD"));
				super.setNumeroSolicitud(numeroSolicitud);
				this.codigoServicioSolicitado=codigoServicioSolicitado;
				this.nombreOtros=nombreOtros;
				this.motivoSolicitud=motivoSolicitud;
				this.comentario=comentario;
				this.resumenHistoriaClinica=resumenHistoriaClinica;
				this.codigoManejointerconsulta=codigoManejointerconsulta;
		}
	
		public SolicitudInterconsulta()
		{
			super.init(System.getProperty("TIPOBD"));
				init(System.getProperty("TIPOBD"));
				codigoServicioSolicitado=0;
				nombreOtros="";
				motivoSolicitud="";
				comentario="";
				resumenHistoriaClinica="";
				codigoManejointerconsulta= 0;
		}
		
		/**
		 * Método para limpiar este objeto
		 */
		public void clean()
		{
				super.clean();
				codigoServicioSolicitado=0;
				nombreOtros="";
				comentario="";
				resumenHistoriaClinica="";
				motivoSolicitud="";
				codigoManejointerconsulta= 0;
				nombreCodigoServicioSolicitado="";
				acronimoDiasTramite="";
				this.SeleccionManejo=new InfoDatosInt();
		}	

		/**
		 * Inicializa el acceso a bases de datos de un objeto.
		 * @param tipoBD el tipo de base de datos que va a usar el objeto
		 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
		 * son los nombres y constantes definidos en <code>DaoFactory</code>.
		 */
		public boolean init(String tipoBD)
		{
			if(solicitarInterconsultaDao== null)
			{
					DaoFactory myFactory= DaoFactory.getDaoFactory(tipoBD);
					solicitarInterconsultaDao= myFactory.getSolicitudInterconsultaDao();
					if(solicitarInterconsultaDao!=null)
						return true;
			}
			return false;
		}

		/**
		 * Inserta los datos del objeto en una fuente de datos, reutilizando una conexión existente,
		 * con la información presente en los atributos del objeto.
		 * 
		 * @param con una conexion abierta con una fuente de datos
		 * @param estado estado de la transaccion (empezar, continuar, finalizar)
		 * @return número de filas insertadas (1 o 0)
		 */
		public int Insertar_Interconsulta_Transaccional(Connection con, String estado, UsuarioBasico usuario, 
				PersonaBasica paciente, List<InfoResponsableCobertura>listaCoberturaServicio) throws SQLException 
		{
			int resp1=0, resp2=0;
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (estado==null)
			{
						myFactory.abortTransaction(con);
						throw new SQLException ("El estado de la transacción (Insertar Interconsulta) no esta especificado"); 
			}
			if (solicitarInterconsultaDao==null)
			{
	
						throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudInterconsultaDao - Insertar_Interconsulta_Transaccional )");
			}
			//Iniciamos la transacción, si el estado es empezar
			boolean inicioTrans;
		
			if (estado.equals("empezar"))
			{
						inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
						inicioTrans=true;
			}
	
			resp1=super.insertarSolicitudGeneralTransaccional(con, "continuar");
			
			
					
			
			
			//int duracioHora = SqlBaseServiciosDao.obtenerMinutosDuracionServicio(codigoServicioSolicitado, con);
	
	
			boolean flagInterpretacionInterconsulta=false; 
			if (this.codigoManejointerconsulta==ConstantesBD.codigoSeDeseaConceptoSolamente)
			{
				flagInterpretacionInterconsulta=true;
			}
						
			resp2=solicitarInterconsultaDao.insertarSolicitudInterconsulta(con,super.getNumeroSolicitud(),this.codigoServicioSolicitado,this.nombreOtros,this.motivoSolicitud,this.resumenHistoriaClinica,this.comentario,this.codigoManejointerconsulta, flagInterpretacionInterconsulta);
			
		
			
			

			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt())))
			{
				
				if(UtilidadesFacturacion.esServicioOdontologico(con,this.codigoServicioSolicitado))
				{
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getSolicitudCitaInterconsultaOdontoCitaProgramada(usuario.getCodigoInstitucionInt())))
					{
						int minutosDuracion= Servicio.obtenerMinutosDuracionServicio(this.codigoServicioSolicitado , con);
						
						
						DtoCitaOdontologica dto = new DtoCitaOdontologica();
						dto.setEstado(ConstantesIntegridadDominio.acronimoProgramado);
						dto.setUsuarioModifica(usuario.getLoginUsuario());
						dto.setDuracion(minutosDuracion);
						dto.setCodigoPaciente(paciente.getCodigoPersona());
						dto.setFechaProgramacion(this.getFechaSolicitud());
						dto.setTipo(ConstantesIntegridadDominio.acronimoRemisionInterconsulta);
						dto.setCodigoCentroCosto(this.getCentroCostoSolicitante().getCodigo());
						/*
						 *Insertar Cita Odotologica 
						 */
						int codigoCita= CitaOdontologica.insertarCitaOdontologica(con, dto);
						
						
						/*
						 * Abortar transaccion si no se guardo la cita
						*/
						if(codigoCita<=0)
						{
							myFactory.abortTransaction(con);
						}
						
						DtoServicioCitaOdontologica dtoServicio= new DtoServicioCitaOdontologica();
						
						dtoServicio.setCitaOdontologica(codigoCita);
						dtoServicio.setActivo(ConstantesBD.acronimoSi);
						dtoServicio.setServicio(this.codigoServicioSolicitado);
						dtoServicio.setUsuarioModifica(usuario.getLoginUsuario());
						dtoServicio.setDuracion(minutosDuracion);
						dtoServicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
						dtoServicio.setEstadoCita(ConstantesIntegridadDominio.acronimoProgramado);
									
						dtoServicio.setFechaCita(this.getFechaSolicitud());
						
						dtoServicio.setAplicaAbono(ConstantesBD.acronimoNo);
						dtoServicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
						dtoServicio.setNumeroSolicitud(super.getNumeroSolicitud());
						/*
						 * Insert servicios cita odontologica 
						 */
						
						
						int codigoServicio= CitaOdontologica.insertarServiciosCitaOdontologica(con, dtoServicio);
						
						/*
						 * Abortar transaction 
						 */
						if(codigoServicio<=0)
						{
							myFactory.abortTransaction(con);
						}
					}
				}
				
			}
			
			
			
			
			if (resp2==-1)
				resp1 = resp2;
			
			documentosAdjuntos.insertarEliminarDocumentosAdjuntos(con,super.getNumeroSolicitud());
		
			//*************************************************************************************************************************************
			//GENERACION DEL CARGO PENDIENTE Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    boolean insertoCargo= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
					    																			usuario, 
					    																			paciente, 
					    																			true/*dejarPendiente*/, 
					    																			super.getNumeroSolicitud(), 
					    																			super.getTipoSolicitud().getCodigo() /*codigoTipoSolicitudOPCIONAL*/, 
					    																			super.getCodigoCuenta(), 
					    																			super.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
					    																			this.codigoServicioSolicitado/*codigoServicioOPCIONAL*/, 
					    																			1/*cantidadServicioOPCIONAL*/, 
					    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
					    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
					    																			/*"" -- numeroAutorizacionOPCIONAL*/
					    																			""/*esPortatil*/,false,super.getFechaSolicitud(),"" /*subCuentaCoberturaOPCIONAL*/);
		   	
		    //*************************************************************************************************************************************
		    /**FIXME Se adiciona a cada Servicio la informacion correspondiente de la cobertura para 
    	     * evaluacion posterior en la autorizacion de Capitacion sub*/
    	    DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta=new DtoSolicitudesSubCuenta();
    	    dtoSolicitudesSubCuenta.getServicio().setCodigo(this.codigoServicioSolicitado+"");
    	    dtoSolicitudesSubCuenta.setNumeroSolicitud(super.getNumeroSolicitud()+"");
    	    dtoSolicitudesSubCuenta.setConsecutivoSolicitud(super.getConsecutivoOrdenesMedicas()+"");
    	    dtoSolicitudesSubCuenta.setUrgenteSolicitud(super.getUrgente());
    	    cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
    	    listaCoberturaServicio.add(cargos.getInfoResponsableCoberturaGeneral());
			
			
			if (!inicioTrans||resp1<1||resp2<1 || !insertoCargo)
			{
					myFactory.abortTransaction(con);
					return -1;
			}
			else
			{
					if (estado.equals("finalizar"))
					{
								myFactory.endTransaction(con);
					}
			}
			return resp1;
		}

		/**
		 * Método para insertar una interconsulta 
		 * @param con una conexion abierta con una fuente de datos
		 * @return número de filas insertadas (1 o 0)
		 * @throws SQLException
		 */
		public int Insertar_Interconsulta(Connection con) throws SQLException 
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			int resp1=0, resp2=0;
			if (solicitarInterconsultaDao==null)
			{

						throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudInterconsultaDao - Insertar_Interconsulta_Transaccional )");
			}
			//Iniciamos la transacción, si el estado es empezar
			boolean inicioTrans;
	
			inicioTrans=myFactory.beginTransaction(con);

			resp1=super.insertarSolicitudGeneralTransaccional(con, "continuar");

			//Para manejar el flag de interconsulta (el que hace que 
			//se muestre la interconsulta en listado de interpretación) 
			//sabemos que debemos poner el flag en falso en los 
			//casos manejo conjunto y solicitud cambio tratante y
			//únicamente en true para el caso de solo concepto
			
			boolean flagInterpretacionInterconsulta=false; 
			if (this.codigoManejointerconsulta==ConstantesBD.codigoSeDeseaConceptoSolamente)
			{
				flagInterpretacionInterconsulta=true;
			}
			
			resp2=solicitarInterconsultaDao.insertarSolicitudInterconsulta(con,super.getNumeroSolicitud(),this.codigoServicioSolicitado,this.nombreOtros,this.motivoSolicitud,this.resumenHistoriaClinica,this.comentario,this.codigoManejointerconsulta, flagInterpretacionInterconsulta);
		
			ResultadoBoolean resDocsAdjuntos = documentosAdjuntos.insertarEliminarDocumentosAdjuntos(con,super.getNumeroSolicitud());
								
			if (!inicioTrans||resp1<1||resp2<1 || (!resDocsAdjuntos.isTrue() && resDocsAdjuntos.getDescripcion() == null ) )
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

		/**
		 * Método utilizado en la funcionalidad Modificar solicitud de interconsulta 
		 * que tiene como finalidad añadir texto en el motivo de solicitud, resumen Historia clínica,
		 * comentario, e ingreso de un nuevo número de autorización
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		public int Modificar_Interconsulta(Connection con) throws SQLException 
		{
			int resp=0;
			resp=solicitarInterconsultaDao.cambiarSolicitudInterconsulta(con, this.getNumeroSolicitud(), this.getMotivoSolicitudNueva(), this.getResumenHistoriaClinicaNueva(), this.getComentarioNuevo()/*, ""*/);
			return resp;
		}
				
		/**
		 * Actualiza los datos del objeto en una fuente de datos, reutilizando una conexión existente,
		 * con la información presente en los atributos del objeto.
		 * 
		 * @param con una conexion abierta con una fuente de datos
		 * @param estado estado de la transaccion (empezar, continuar, finalizar)
		 * @return número de filas insertadas (1 o 0)
		 */
		public int ModificarInterconsultaTransaccional (Connection con, String estado) throws SQLException
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			int resp1=0;
			if (estado==null)
			{
						myFactory.abortTransaction(con);
						throw new SQLException ("El estado de la transacción (Solicitud - insertarTratanteInicialTransaccional ) no esta especificado");
			}
			if (solicitarInterconsultaDao==null)
			{
						throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (solitarInterconsultaDao - insertarTratanteInicialTransaccional )");
			}
	
			//Iniciamos la transacción, si el estado es empezar
	
			boolean inicioTrans;
	
			if (estado.equals("empezar"))
			{
						inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
						inicioTrans=true;
			}
			
			resp1=solicitarInterconsultaDao.cambiarSolicitudInterconsulta(con, this.getNumeroSolicitud(), this.getMotivoSolicitudNueva(), this.getResumenHistoriaClinicaNueva(), this.getComentarioNuevo()/*, ""*/);
	
			if (!inicioTrans||resp1<1)
			{
						myFactory.abortTransaction(con);
			}
			else
			{
						if (estado.equals("finalizar"))
						{
									myFactory.endTransaction(con);
						}
			}
			return resp1;
		}
				
		/**
		 * Método para cargar una solicitud dado su
		 * código
		 *
		 * @param con Conexión con la fuente de datos
		 * @param numeroSolicitud Código de la solicitud a
		 * cargar
		 * @return
		 * @throws SQLException
		 */
		public boolean cargar(Connection con, int numeroSolicitud) throws SQLException
		{
			//this.numeroSolicitud=numeroSolicitud;
			super.cargar(con,numeroSolicitud);
			ResultSetDecorator rs=solicitarInterconsultaDao.cargarSolicitudInterconsulta(con, numeroSolicitud);
		
			if (rs.next())
			{
					this.codigoServicioSolicitado=rs.getInt("codigoServicioSolicitado");
					this.nombreOtros=rs.getString("nombreOtros");
					this.motivoSolicitud=rs.getString("motivoSolicitud");
					this.resumenHistoriaClinica=rs.getString("resumenHistoriaClinica");
					this.comentario=rs.getString("comentario");					
					this.SeleccionManejo.setNombre(rs.getString("nombreManejoInterconsulta"));
					this.SeleccionManejo.setCodigo(rs.getInt("codigoManejoInterconsulta"));
					this.respuestaOtros=rs.getString("respuestaOtros");
					this.setCodigoManejointerconsulta(rs.getInt("codigoManejoInterconsulta"));

					return true;
			}
			else
			{
					return false;
			}
		}

		/**
		 * Método para cargar el nombre del código del servicio solicitado
		 * 
		 * @param con Conexión con la fuente de datos
		 * @param numeroSolicitud Código de la solicitud a cargar
		 * @return
		 * @throws SQLException
		 */
		public boolean cargarCodigoServicioSolicitadoInterconsulta(Connection con, int numeroSolicitud)throws SQLException
		{
			ResultSetDecorator rs=solicitarInterconsultaDao.cargarCodigoServicioSolicitado(con,numeroSolicitud);
			if(rs.next())
			{
					this.nombreCodigoServicioSolicitado=rs.getString("description");
					return true;	
			}
			else
			{
					return false;	
			}
		}
		
		/**
		 * Método para cargar el acronimo días urgente o normal del grupo del servicio solicitado
		 * dependiendo del campo urgente que se selecciono
		 * 
		 * @param con Conexión con la fuente de datos
		 * @param codigoServicio
		 * @param urgente
		 * @return 
		 * @throws SQLException
		 */
		public void cargarDiasTramiteServicioSolicitado(Connection con, int codigoServicio, boolean urgente)throws SQLException
		{
			this.acronimoDiasTramite=solicitarInterconsultaDao.cargarDiasTramiteServicioSolicitado(con,codigoServicio,urgente);
		}
			
		/**
		 * Método para cargar el motivo de una anulación de interconsulta
		 * 
		 * @param con Conexión con la fuente de datos
		 * @param numeroSolicitud Código de la solicitud a cargar
		 * @return
		 * @throws SQLException
		 */
		public boolean cargarMotivoAnulacionInterconsulta(Connection con, int numeroSolicitud)throws SQLException
		{
					ResultSetDecorator rs=solicitarInterconsultaDao.cargarMotivoAnulacionSolicitudInterconsulta(con,numeroSolicitud);
					
					if(rs.next())
					{
							super.setMotivoAnulacion(rs.getString("motivo"));
							return true;	
					}
					else
					{
							return false;	
					}
		}
			
	
		/**Retorna el código del servicio solicitado en formato entero
		 * @return
		 */
		public int getCodigoServicioSolicitado() {
			return codigoServicioSolicitado;
		}
		
		/**Retorna el comentario asociado a la solicitud de interconsulta
		 * @return
		 */
		public String getComentario() {
			return comentario;
		}
	
		/**Retorna el motivo de la solicitud de interconsulta
		 * @return
		 */
		public String getMotivoSolicitud() {
			return motivoSolicitud;
		}
	
		/**Retorna el nombre del procedimiento no parametrizado
		 * @return
		 */
		public String getNombreOtros() {
			return nombreOtros;
		}
			
		/**Retorna el resumen de la Historia Clínica asociada a la solicitud de interconsulta
		 * @return
		 */
		public String getResumenHistoriaClinica() {
			return resumenHistoriaClinica;
		}
	
		/**Asigna el código (int) de manejo de la interconsulta
		 * @param codigoManejointerconsulta 
		 */
		public void setcodigoManejointerconsulta(int int1) {
			codigoManejointerconsulta = int1;
		}
	
		/**Asigna el código (int) del servicio solicitado asociado a la interconsulta 
		 * @param codigoServicioSolicitado
		 */
		public void setCodigoServicioSolicitado(int i) {
			codigoServicioSolicitado = i;
		}
	
		/**Asigna el comentario asociado a la solicitud de interconsulta
		 * @param comentario
		 */
		public void setComentario(String string) {
			comentario = string;
		}
	
		/**Asigna el motivo de la solicitud asociado a la solicitud de interconsulta 
		 * @param motivoSolicitud
		 */
		public void setMotivoSolicitud(String string) {
			motivoSolicitud = string;
		}
	
		/**Asigna un nombre de procedimiento cuando el centro de costo es externo
		 * @param nombreOtros
		 */
		public void setNombreOtros(String string) {
			nombreOtros = string;
		}
	
		/**Asigna el resumen de la historia clínica asociada a la solicitud de interconsulta
		 * @param resumenHistoriaClinica
		 */
		public void setResumenHistoriaClinica(String string) {
			resumenHistoriaClinica = string;
		}
	
		/**Retorna el código de manejo de la solicitud de interconsulta
		 * @return
		 */
		public int getCodigoManejointerconsulta() {
			return codigoManejointerconsulta;
		}
	
		/**Asigna  el código de manejo de la solicitud de interconsulta
		 * @param codigoManejointerconsulta
		 */
		public void setCodigoManejointerconsulta(int i) {
			codigoManejointerconsulta = i;
		}
			
		/**
		 * Método para cargar los documentos adjuntos
		 * @param con Conexión establecida
		 * @return ResultadoBoolean
		 */
		public ResultadoBoolean cargarDocumentosAdjuntos(Connection con)
		{
			//Seccion cargar documentos adjuntos
			return documentosAdjuntos.cargarDocumentosAdjuntos(con, this.getNumeroSolicitud(), true, "");
		}
		
		/**Retorna un InfoDatosInt de la seleccion de manejo asociada a la solicitud
		 * @return
		 */
		public InfoDatosInt getSeleccionManejo() {
			return SeleccionManejo;
		}
	
		/**Asigna un InfoDatosInt de la seleccion de manejo asociada a la solicitud
		 * @param SeleccionManejo
		 */
		public void setSeleccionManejo(InfoDatosInt int1) {
			SeleccionManejo = int1;
		}

		/**Retorna una cadena para añadirla al motivo de la solicitud previamente ingresada
		 * @return
		 */
		public String getMotivoSolicitudNueva() {
			return motivoSolicitudNueva;
		}

		/**Asigna una cadena para añadirla al motivo de la solicitud previamente ingresada
		 * @param motivoSolicitudNueva
		 */
		public void setMotivoSolicitudNueva(String string) {
			motivoSolicitudNueva = string;
		}

		/**Retorna una cadena para añadirla al comentario de la solicitud previamente ingresada
		 * @return
		 */
		public String getComentarioNuevo() {
			return comentarioNuevo;
		}

		/**Retorna una cadena para añadirla al motivo de la solicitud previamente ingresada
		 * @return
		 */
		public String getResumenHistoriaClinicaNueva() {
			return resumenHistoriaClinicaNueva;
		}

		/**Asigna una cadena para añadirla al comentario previamente ingresado
		 * @param comentarioNuevo
		 */
		public void setComentarioNuevo(String string) {
			comentarioNuevo = string;
		}

		/**Asigna una cadena para añadirla a la Historia clínica previamente ingresada
		 * @param resumenHistoriaClinicaNueva
		 */
		public void setResumenHistoriaClinicaNueva(String string) {
			resumenHistoriaClinicaNueva = string;
		}

		
		/**Retorna el nombre del código de servicio solicitado en formato cadena
		 * @return
		 */
		public String getNombreCodigoServicoSolicitado() {
			return nombreCodigoServicioSolicitado;
		}

		/**Asigna el nombre del código de servicio solicitado en formato cadena
		 * @param nombreCodigoServicioSolicitado 
		 */
		public void setNombreCodigoServicoSolicitado(String string) {
			nombreCodigoServicioSolicitado = string;
		}
		
		/**
		 * @return the acronimoDiasTramite
		 */
		public String getAcronimoDiasTramite() {
			return acronimoDiasTramite;
		}

		/**
		 * @param acronimoDiasTramite the acronimoDiasTramite to set
		 */
		public void setAcronimoDiasTramite(String acronimoDiasTramite) {
			this.acronimoDiasTramite = acronimoDiasTramite;
		}

		/**Retorna objeto para manejo de logs  
		 * @return
		 */
		public static Logger getLogger() {
			return logger;
		}
			
		/**Asigna objeto para manejo de logs
		 * @param logger
		 */
		public static void setLogger(Logger logger) {
			SolicitudInterconsulta.logger = logger;
		}

		/**Adiciona el nombre generado por el doc adjunto en el objeto (Vector) 
		 * @param nombre
		 */
		public void addNombreGeneradoDocumentoAdjunto(String nombre) {
			nombresGeneradosDocumentosAdjuntos.add(nombre);
		}

		/**Adiciona el nombre original al objeto (Vector)
		 * @param vector
		 */
		public void addNombreOriginalDocumentoAdjunto(String nombre) {
			nombresOriginalesDocumentosAdjuntos.add(nombre);
		}
		
		/**Retorna el objeto (Vector) que contiene los nombres generados por los documentos adjuntos
		 * @return
		 */
		public Vector getNombresGeneradosDocumentosAdjuntos() {
			return nombresGeneradosDocumentosAdjuntos;
		}
	
		/**Retorna el Vector donde quedan almacenados los nombres ORIGINALES de los documentos adjuntos
		 * @return
		 */
		public Vector getNombresOriginalesDocumentosAdjuntos() {
			return nombresOriginalesDocumentosAdjuntos;
		}
	
		/**Retorna el número de documentos adjuntos asociado a la solicitud
		 * @return
		 */
		public int getNumDocumentosAdjuntos() {
			return numDocumentosAdjuntos;
		}
	
		/**Asigna los nombres generados por los documentos adjuntos
		 * @param vector
		 */
		public void setNombresGeneradosDocumentosAdjuntos(Vector vector) {
			nombresGeneradosDocumentosAdjuntos = vector;
		}
	
		/**Asigna el Vector donde quedan almacenados los nombres ORIGINALES de los documentos adjuntos
		 * @param vector
		 */
		public void setNombresOriginalesDocumentosAdjuntos(Vector vector) {
			nombresOriginalesDocumentosAdjuntos = vector;
		}
	
		/**Asigna el número de documentos adjuntos asociado a la solicitud
		 * @param numDocumentosAdjuntos
		 */
		public void setNumDocumentosAdjuntos(int i) {
			numDocumentosAdjuntos = i;
		}
		
		/**Retorna el nombre compuesto del Documento Adjunto en el formato (nombre-generado @ nombre-original)
		 * @param indice
		 * @return
		 */
		public String getNombreCompuestoDocumentoAdjunto(int indice)
		{
			return this.nombresGeneradosDocumentosAdjuntos.get(indice)+"@"+this.nombresOriginalesDocumentosAdjuntos.get(indice);
		}
		

		/**Retorna un objeto que contiene todos los documentos adjuntos asociados a esta solicitud
		 * @return
		 */
		public DocumentosAdjuntos getDocumentosAdjuntos() {
			return documentosAdjuntos;
		}
			
		/**Asigna un objeto que contiene todos los documentos adjuntos asociados a esta solicitud
		 * @param adjuntos 
		 */
		public void setDocumentosAdjuntos(DocumentosAdjuntos adjuntos) {
			documentosAdjuntos = adjuntos;
		}
					
		/**
		 * Método para adicionar documentos al objeto
		 * @param adjunto
		 */
		public void addDocumentoAdjunto(DocumentoAdjunto adjunto) {
			documentosAdjuntos.addDocumentoAdjunto(adjunto);
		}
		
		/**Retorna  la respuesta de la solicitud de interconsulta
		 * @return
		 */
		public String getRespuestaOtros() {
			return respuestaOtros;
		}

		/**Asigna la respuesta de la solicitud de interconsulta
		 * @param string
		 */
		public void setRespuestaOtros(String string) {
			respuestaOtros = string;
		}

		
		/**
		 * settea el codigo paciente
		 */
		public void setCodigoPaciente(int codigoPaciente){
			this.codigoPaciente=codigoPaciente;
		}
		
		/**
		 * Retorna el codigo del paciente
		 */
		public int getCodigoPaciente(){
			return this.codigoPaciente;
		}
		
		
		/**
		 * Método que permite actualizar el flag de mostrar interconsulta
		 * en interpretación
		 * 
		 * @param con Conexión con la fuente de datos
		 * @param numeroSolicitud Número de la solicitud de interconsulta
		 * a actualizar
		 * @param nuevoEstadoFlag El nuevo valor del flag 
		 * @return
		 * @throws SQLException
		 */
		public static int actualizarFlagMostrarInterconsulta (Connection con, int numeroSolicitud, boolean nuevoEstadoFlag) 
		{
			try
			{
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudInterconsultaDao().actualizarFlagMostrarInterconsulta (con, numeroSolicitud, nuevoEstadoFlag) ;
			}
			catch(Exception e)
			{
				return 0;
			}

		}
		
		/**
		 * M&eacute;todo encargado de consultar el c&oacute;digo del servicio
		 * asociado a una solicitud de interconsulta 
		 * @param Connection con, 
	    		String numeroSolicitud, int codigoTarifario
		 * @return DtoServicios
		 * @author Diana Carolina G
		 */
		public DtoServicios buscarServiciosSolicitudInterconsulta(Connection con, 
	    		int numeroSolicitud, int codigoTarifario){
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudInterconsultaDao().buscarServiciosSolicitudInterconsulta(
					con, numeroSolicitud, codigoTarifario);
		}
		
		
}
