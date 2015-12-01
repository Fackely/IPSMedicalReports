/*
 * Nov 05, 2009
 */
package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.UtilidadesFacturacion;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.AtencionCitasOdontologiaDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoFacturaAutomaticaOdontologica;
import com.princetonsa.dto.facturacion.DtoParametroFacturaAutomatica;
import com.princetonsa.dto.facturacion.DtoServiIncluidoServiPpal;
import com.princetonsa.dto.facturacion.DtoServiPpalIncluido;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;
import com.princetonsa.dto.odontologia.DtoInfoAtencionCitaOdo;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.FacturaOdontologicaAutomatica;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * Clase de capa mundo para el manejo de la lógica del negocio
 * de la atención de citas de odontología
 * @author Sebastián Gómez R.
 *
 */
public class AtencionCitasOdontologia 
{
	/**
	 * logger
	 */
	static Logger logger = Logger.getLogger(AtencionCitasOdontologia.class);
	
	/**
	 * Interfaz para e uso del DAO de atencion citas odontologia
	 */
	private AtencionCitasOdontologiaDao atencionCitasDao;
	
	/**
	 * Clase para el manejo de los errores STRUTS
	 */
	private ActionErrors errores;
	
	/**
	 * Facturas automaticas posiblemente generadas
	 */
	private DtoFacturaAutomaticaOdontologica facturasAutomaticas;
	
	/**
	 * Usuario en sesiï¿½n 
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Atributo para saber si el profesional es odontologo
	 */
	private boolean odontologo;
	
	private ArrayList<HashMap<String, Object>> arregloProfesionales;
	
	/**
	 * Objeto que almacen la informacion de la atencion de citas
	 */
	private DtoInfoAtencionCitaOdo infoAtencionCitaOdo;
	
	//*******************ATRIBUTOS USADOS PARA LA IMPRESION DE CITA**********************************
	private ArrayList<DtoPlantilla> plantillas;
	private DtoPaciente paciente;
	private DtoPlantilla plantillaSeleccionadaHistorico;
	//**************************************************************************************************
	
	/**
	 * Cosntructor
	 */
	public AtencionCitasOdontologia()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Mï¿½todo para limpiar los datos
	 */
	public void reset()
	{
		this.errores = new ActionErrors();
		this.facturasAutomaticas= new DtoFacturaAutomaticaOdontologica();
		this.usuario = new UsuarioBasico();
		this.odontologo = false;
		this.arregloProfesionales = new ArrayList<HashMap<String,Object>>();
		this.infoAtencionCitaOdo = new DtoInfoAtencionCitaOdo();
		
		//Atributos usados para la improesion de citas
		this.plantillas = new ArrayList<DtoPlantilla>();
		this.paciente = new DtoPaciente();
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (this.atencionCitasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			atencionCitasDao = myFactory.getAtencionCitasOdontologiaDao();
		}
		
			
	}
	
	/**
	 * Instancia DAO
	 * */
	public static AtencionCitasOdontologiaDao getAtencionCitasOdontologiaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAtencionCitasOdontologiaDao();		
	}
	
	//**************************************METODOS LOGICA NEGOCIO***********************************************************
	
	/**
	 * Mï¿½todo que realiza las validaciones de ingreso
	 */
	public void validacionesIngreso()
	{
		//1) Se verifica que sea un profesional de la salud
		if(UtilidadValidacion.esProfesionalSalud(this.usuario))
		{
			logger.info("Ocupacion AuxiliaR ODONTOLOGO: "+ValoresPorDefecto.getOcupacionAuxiliarOdontologo(this.usuario.getCodigoInstitucionInt()));
			logger.info("Ocupacion ODONTOLOGO: "+ValoresPorDefecto.getOcupacionOdontologo(this.usuario.getCodigoInstitucionInt()));
			logger.info("Ocupacion medica del usuario: "+this.usuario.getCodigoOcupacionMedica());
			

			logger.info("\n\n AuxiliarOdontologo "+ValoresPorDefecto.getOcupacionAuxiliarOdontologo(this.usuario.getCodigoInstitucionInt()));
			logger.info("\n\n Odontologo "+ValoresPorDefecto.getOcupacionOdontologo(this.usuario.getCodigoInstitucionInt()));
			//2) Se verifica que se haya definido ocupacion odontologo y/o auxiliar de enfermeria
			if(Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionAuxiliarOdontologo(this.usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoNuncaValido
				||
				Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionOdontologo(this.usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoNuncaValido
				)
			{
				logger.info("Entro 1");
				errores.add("", new ActionMessage("errors.noOcupacionOdontologo"));
			}
			//3) Se verifica que su ocupaciï¿½n sea Auxiliar Odontologia u Odontologia
			else if(Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionAuxiliarOdontologo(this.usuario.getCodigoInstitucionInt()))!=this.usuario.getCodigoOcupacionMedica()
				&&  
				Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionOdontologo(this.usuario.getCodigoInstitucionInt()))!=this.usuario.getCodigoOcupacionMedica()
				)
			{  
				logger.info("Entro 2");
				errores.add("", new ActionMessage("errors.noEsOdontologo"));
			}
		}
		else
		{
			errores.add("",new ActionMessage("errors.noProfesionalSalud"));
		}
		
		if(this.errores.isEmpty())
		{
		
			//Se verifica si el profesional es un odontologo
			if(Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionOdontologo(this.usuario.getCodigoInstitucionInt()))==this.usuario.getCodigoOcupacionMedica())
			{
				this.odontologo = true;
				
			}
			else
			{
				this.odontologo = false;
				
				Connection con = UtilidadBD.abrirConexion();
				this.arregloProfesionales = UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, false, true,Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionOdontologo(this.usuario.getCodigoInstitucionInt())));
				UtilidadBD.closeConnection(con);
				
				
			}
			
			//Se aï¿½ade el usuario
			HashMap<String, Object> elemento = new HashMap<String, Object>();
			elemento.put("codigo", usuario.getCodigoPersona());
			elemento.put("nombre", usuario.getNombreUsuario());
			this.arregloProfesionales.add(elemento);
		}
	}
	
	/**
	 * M&eacute;todo para obtener las citas odontol&oacute;gicas para atender
	 * @param parametros
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitas(String fecha,UsuarioBasico usuario)
	{
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fecha",fecha);
		parametros.put("usuario",usuario);
		return atencionCitasDao.consultarCitas(parametros);
	}
	
	/**
	 * Mï¿½todo para obtener las citas odontologicas para atender
	 * @param codigoCentroAtencion 
	 * @param parametros
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitas(String fecha,int codigoPersona, int codigoCentroAtencion)
	{
		UsuarioBasico usuario = new UsuarioBasico();
		Connection con = UtilidadBD.abrirConexion();
		logger.info("codigo progfesional agendado: "+codigoPersona);
		try 
		{
			usuario.cargarUsuarioBasico(con, codigoPersona);
			//Se asigna el mismo centro de atencion del usuario cargado en sesiï¿½n
			usuario.setCodigoCentroAtencion(codigoCentroAtencion);
		} catch (SQLException e) 
		{
			logger.error("Error al tratar de cargar el profesional al consultar las citas: ",e);
			
		}
		UtilidadBD.closeConnection(con);
		logger.info("el login del usuario es: "+usuario.getLoginUsuario());
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fecha",fecha);
		parametros.put("usuario",usuario);
		return atencionCitasDao.consultarCitas(parametros);
	}
	
	/**
	 * Realizar la atención de citas
	 * @param cita
	 * @param usuario
	 */
	public void atenderCita(DtoCitaOdontologica cita,UsuarioBasico usuario) throws IPSException
	{
		//*******************NO ASISTIO - NO ATENCION *****************************************************************
		if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAsistio)||cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAtencion))
		{
			//1) Se verifica si se ingresó el motivo de no atención
			if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAtencion))
			{
				if(cita.getMotivoNoAtencion().trim().equals(""))
				{
					errores.add("", new ActionMessage("errors.required","El motivo de no atención"));
				}
			}
			if(
					(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAsistio) || cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAtencion))
					&&
					errores.isEmpty()
				)
			{
				Connection conAbonos=UtilidadBD.abrirConexion();
				ArrayList<DtoServicioCitaOdontologica> serviciosViejosCitaOdonAbono=CitaOdontologica.consultarServiciosCitaOdontologica(conAbonos, cita.getCodigoPk(), usuario.getCodigoInstitucionInt(), cita.getCodigoPaciente()); 

				double valorAbonos=CitaOdontologica.obtenerValorCargadoCita(conAbonos, serviciosViejosCitaOdonAbono);
				
				CitaOdontologica.devolverReservaAbonosCita(conAbonos, valorAbonos, usuario, paciente.getCodigo(), cita.getCodigoPk(), paciente.getIdIngreso().intValue());

				UtilidadBD.closeConnection(conAbonos);
			}
			
			if(errores.isEmpty())
			{
				//1) Se asigna el usuario modifica-------------------------------------------------------
				cita.setUsuarioModifica(usuario.getLoginUsuario());
				for(DtoServicioCitaOdontologica servicio:cita.getServiciosCitaOdon())
				{
					servicio.setUsuarioModifica(usuario.getLoginUsuario());
				}
				//-------------------------------------------------------------------------------
				Connection con = UtilidadBD.abrirConexion();
				UtilidadBD.iniciarTransaccion(con);
				
				//2) Se anulan las solicitudes de la cita -----------------------------------------
				for(DtoServicioCitaOdontologica servicio:cita.getServiciosCitaOdon())
				{
					//Se anulan las solicitudes que estaban en estado HC solicitada
					if(servicio.getEstadoHistoriaClinicaSolicitud().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada)
					{
						ResultadoBoolean resultado = Solicitud.cambiarEstadosSolicitudStatico(con, servicio.getNumeroSolicitud(), ConstantesBD.codigoEstadoFAnulada, ConstantesBD.codigoEstadoHCAnulada);
						if(!resultado.isTrue())
						{
							errores.add("", new ActionMessage("errors.notEspecific","Problemas tratando de anular la solicitud con consecutivo interno Nï¿½"+servicio.getNumeroSolicitud()+": "+resultado.getDescripcion()));
						}
						
							
					}
				}
				
				//3) Se actualiza el estado de la cita
				if(errores.isEmpty()&&!CitaOdontologica.cambiarSerOdontologicos(con, cita))
				{
					errores.add("",new ActionMessage("errors.notEspecific","Problemas tratando de actualizar el estado de la cita"));
				}
				
				if(errores.isEmpty())
				{
					UtilidadBD.finalizarTransaccion(con);
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
				}
				
				UtilidadBD.closeConnection(con);
			}
		}
		//***************************************************************************************
		else
		{
			cargarInfoAtencionCitaOdo(cita, usuario, false);
		}
		//*********************************************************************************************
	}
	
	
	/**
	 * Carga la informaci&oacute;n de la atenci&oacute;n de citas odontol&oacute;gicas
	 * @param cita
	 * @param usuario
	 * @param cargarCita
	 */
	public void cargarInfoAtencionCitaOdo(DtoCitaOdontologica cita,UsuarioBasico usuario,boolean cargarCita)
	{
		if(cargarCita)
		{
			atencionCitasDao.cargarDatosCita(cita, usuario, false);
		}
		
		this.infoAtencionCitaOdo.setUsuarioRegistra(cita.getUsuarioRegistra());
		this.infoAtencionCitaOdo.setFechaRegistra(cita.getFechaRegistra());
		this.infoAtencionCitaOdo.setHoraRegistra(cita.getHoraRegistra());
		this.infoAtencionCitaOdo.setPlantillas(atencionCitasDao.cargarPlantillasAtencion(cita, usuario));
	}
	
	/**
	 * 
	 * @param cita
	 * @param usuario
	 */
	public void cargarPlantillasAtencion(DtoCitaOdontologica cita,UsuarioBasico usuario)
	{
		this.infoAtencionCitaOdo.setPlantillas(atencionCitasDao.cargarPlantillasAtencion(cita, usuario));
	}
	
	/**
	 * Este método organiza la información de la consulta de plantillas en el historico,
	 * concatenando las especialidades del profesional que modificó la plantilla
	 * @param codigoPaciente
	 * @param consecutivoIngreso
	 */
	public ArrayList<DtoPlantilla> consultarPlantillasPorIngresos(int codigoPaciente, String consecutivoIngreso)
	{
		ArrayList<DtoPlantilla> dtoPlant=new ArrayList<DtoPlantilla>();
		ArrayList<DtoPlantilla> dtoPlantillasDefinitivas=new ArrayList<DtoPlantilla>();
		dtoPlant=atencionCitasDao.consultarPlantillasPorIngresos(codigoPaciente, consecutivoIngreso);
		ArrayList<String> codigosValEvo=new ArrayList<String>();
		StringBuilder tmp=null;
		for(int i=0;i<dtoPlant.size();i++){
			if(!codigosValEvo.contains(dtoPlant.get(i).getCodigo())){
				tmp=new StringBuilder();
				for(int j=i;j<dtoPlant.size();j++){
					if(dtoPlant.get(i).getCodigo().equals(dtoPlant.get(j).getCodigo())){
						tmp.append(dtoPlant.get(j).getNombreEspecialidad());
						tmp.append("<br>");
					}
				}
				dtoPlant.get(i).setNombreEspecialidad(tmp.toString());
				dtoPlantillasDefinitivas.add(dtoPlant.get(i));
				codigosValEvo.add(dtoPlant.get(i).getCodigo());
			}
		}
		return dtoPlantillasDefinitivas;
	}
	
	
	/**
	 * 
	 * @param cita
	 * @param usuario
	 * @return
	 */
	public static DtoCitaOdontologica cargarDatosCita(BigDecimal cita, UsuarioBasico usuario)
	{
		DtoCitaOdontologica dto= new DtoCitaOdontologica();
		dto.setCodigoPk(cita.intValue());
		getAtencionCitasOdontologiaDao().cargarDatosCita(dto, usuario, true);
		return dto;
	}
	
	
	/**
	 * Mï¿½todo que realiza las validaciones de la confirmacion de la cita
	 * @param cita
	 * @param usuario
	 */
	public static void validarUsuarioInicioConfirmacion(DtoCitaOdontologica cita,UsuarioBasico usuario,DtoInfoAtencionCitaOdo infoAtencionCitaOdo)
	{
		if(cita.getAgendaOdon().getUsuarioMedico().getLoginUsuario().equals(usuario.getLoginUsuario()))
		{
			infoAtencionCitaOdo.setMismoProfesional(false);
			infoAtencionCitaOdo.getUsuarioConfirma().setLoginUsuario(usuario.getLoginUsuario());
			infoAtencionCitaOdo.getUsuarioConfirma().setPasswordUsuario("");
		}
		else
		{
			infoAtencionCitaOdo.setMismoProfesional(false);
			infoAtencionCitaOdo.getUsuarioConfirma().setLoginUsuario("");
			infoAtencionCitaOdo.getUsuarioConfirma().setPasswordUsuario("");
		}
	}
	
	/**
	 * Método implementado para verificar el usuario de la confirmación de la atención de citas
	 * @param cita
	 * @param usuario
	 * @param infoAtencionCitaOdo
	 */
	public void validarUsuarioFinConfirmacion(DtoCitaOdontologica cita,UsuarioBasico usuario,DtoInfoAtencionCitaOdo infoAtencionCitaOdo)
	{
		Connection con = UtilidadBD.abrirConexion();
		//Se verifica si existe el usuario
		
		if(!infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario().equals(usuario.getLoginUsuario()))
		{
			errores.add("", new ActionMessage("errors.usuarioContrasenaInvalido"));
			return;
		}
		ResultadoBoolean resultado = UtilidadesAdministracion.existeUsuario(con, infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario(), infoAtencionCitaOdo.getUsuarioConfirma().getPasswordUsuario(),infoAtencionCitaOdo.isMismoProfesional());
		
		//Si el usuario existe se continua validación
		if(resultado.isTrue())
		{
			//Se verifica si el usuario esta activo
			try 
			{
				if(UtilidadValidacion.esUsuarioActivo(con, infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario(), usuario.getCodigoInstitucionInt()))
				{
					
					infoAtencionCitaOdo.getUsuarioConfirma().cargarUsuarioBasico(con, infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario());
					
					//Si el usuario estï¿½ activo se verifica que sea profesional de la salud
					if(UtilidadValidacion.esProfesionalSalud(infoAtencionCitaOdo.getUsuarioConfirma()))
					{
						if(!UtilidadValidacion.estaMedicoInactivo(con, infoAtencionCitaOdo.getUsuarioConfirma().getCodigoPersona(), usuario.getCodigoInstitucionInt()))
						{
							if(cita.getAgendaOdon().getCodigoMedico()>0)
							{
								//*************VALIDACION AGENDA CON PROFESIONAL**********************++
								if(cita.getAgendaOdon().getCodigoMedico()!=infoAtencionCitaOdo.getUsuarioConfirma().getCodigoPersona())
								{
									errores.add("", new ActionMessage("errors.notEspecific","el profesional "+cita.getAgendaOdon().getUsuarioMedico().getNombreUsuario()+" que está confirmando la atención, no corresponde al profesional de la salud para el que se agenda la cita. Por favor verifique el usuario y password digitados"));
								}
								//***********************************************************************
							}
							else
							{
								
								//***********VALIDACION AGENDA SIN PROFESIONAL**************************
								boolean profesionalValido = false;
								
								Log4JManager.info("cita.tieneServiciosConsulta() "+cita.tieneServiciosConsulta());
								
								if(cita.tieneServiciosConsulta())
								{
									for(InfoDatosInt especialidad:infoAtencionCitaOdo.getUsuarioConfirma().getEspecialidades())
									{
										if(especialidad.isActivo()&&especialidad.getCodigo()==cita.getAgendaOdon().getEspecialidadUniAgen())
										{
											profesionalValido = true;
										}
									}
								}
								if(cita.tieneServiciosProcedimiento()&&!profesionalValido)
								{
									if(UtilidadesConsultaExterna.validarCentrosCostoUsuarioEnUnidadAgenda(con, infoAtencionCitaOdo.getUsuarioConfirma(), cita.getAgendaOdon().getUnidadAgenda()))
									{
										profesionalValido = true;
									}
								}
								logger.info("es profesional valido? "+profesionalValido);
								
								if(!profesionalValido)
								{
									errores.add("", new ActionMessage("errors.opcionDisponible","un profesional que tenga la misma especialidad o algún centro de costo de la unidad de agenda de la cita"));
								}
								else if(infoAtencionCitaOdo.getUsuarioConfirma().getCodigoOcupacionMedica()!=Utilidades.convertirAEntero(ValoresPorDefecto.getOcupacionOdontologo(usuario.getCodigoInstitucionInt())))
								{
									errores.add("", new ActionMessage("errors.noEsOdontologo2"));
								}
								//************************************************************************
							}
						}
						else
						{
							errores.add("", new ActionMessage("errors.profesionalSaludInactivo"));
						}
					}
					else
					{
						errores.add("", new ActionMessage("errors.noProfesionalSalud"));
					}
					
				}
				else
				{
					errores.add("", new ActionMessage("error.salasCirugia.noActivoUsuario"));
				}
			}
			catch (SQLException e) 
			{
				logger.error("Problemas validando si el usuario está activo: ",e);
				errores.add("", new ActionMessage("errors.problemasGenericos","validando si el usuario está activo"));
			}
		}
		else
		{
			errores.add("", new ActionMessage("errors.usuarioContrasenaInvalido"));
		}
		
		UtilidadBD.closeConnection(con);
		
	}
	

	/**
	 * Realiza la confirmaci&oacute;n de la cita
	 * @param cita {@link DtoCitaOdontologica} Dto de la cita
	 * @param usuario {@link UsuarioBasico} Usuario que realiza la conirmación
	 * @param infoAtencionCitaOdo
	 * @param paciente
	 * @param verificarDiligenciamiento
	 * @param cargarPlantillas
	 * @param institucion 
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public void confirmarCita(DtoCitaOdontologica cita, UsuarioBasico usuario, DtoInfoAtencionCitaOdo infoAtencionCitaOdo,PersonaBasica paciente,boolean verificarDiligenciamiento,boolean cargarPlantillas, String idSession , int codigoValoracion, InstitucionBasica institucion) throws BDException
	{
		
		if(cargarPlantillas)
		{
			cargarInfoAtencionCitaOdo(cita, usuario, false);
			infoAtencionCitaOdo.setPlantillas(this.infoAtencionCitaOdo.getPlantillas());
		}
		
		this.infoAtencionCitaOdo = infoAtencionCitaOdo;
		
		
		Connection con = UtilidadBD.abrirConexion();
		
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		int numPlantillasEnProceso = 0; //contador de numero de plantillas en proceso
		
		//Se toman de nuevo los servicios de la cita cuyas solicitudes estén en estado historia clinica = solicitada
		DtoCitaOdontologica citaTemp = new DtoCitaOdontologica();
		citaTemp.setCodigoPk(cita.getCodigoPk());
		citaTemp.setCodigoCentroCosto(cita.getCodigoCentroCosto());
		atencionCitasDao.cargarDatosCita(citaTemp, usuario, false);
		atencionCitasDao.cargarServiciosCita(con, citaTemp, usuario);
		
		BigDecimal ingreso = UtilidadOdontologia.consultarIngresoCitaOdontologica(con, new BigDecimal(citaTemp.getCodigoPk()));
		
		
		logger.info("************************SECCION VALIDACIÓN CONFIRMACION PLANTILLAS****************************************************");
		//********************VALIDACION DE LOS DATOS REQUERIDOS DE LAS PLANTILLAS*******************************+
		for(DtoPlantilla plantilla:infoAtencionCitaOdo.getPlantillas())
		{
			//logger.info("nombre de plantilla a confirmar: "+plantilla.getNombre());
			//logger.info("¿Está en proceso la plantilla? : "+plantilla.isPlantillaEnProceso());
			if(plantilla.isPlantillaEnProceso())
			{
				numPlantillasEnProceso++;
				if(plantilla.getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia)
				{
					//1) cargar la valoracion y validacion de los campos requeridos
					DtoValoracionesOdonto dtoValoracion = ValoracionOdontologica.consultarValoracionPaciente(paciente.getCodigoPersona(), Double.parseDouble(cita.getCodigoPk()+""), Integer.parseInt(plantilla.getCodigoPK()));
					
					ArrayList<DtoValDiagnosticosOdo> diagnosticosVal = ValoracionOdontologica.consultaDiagnosticosValOdo(dtoValoracion.getCodigoPk()); 
					ArrayList<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
					if(diagnosticosVal.size()>0)
					{
						Diagnostico diagPrincipal = new Diagnostico();
						diagPrincipal.setNombre(diagnosticosVal.get(0).getNombreDiagnostico());
						diagPrincipal.setTipoCIE(diagnosticosVal.get(0).getTipoCleDiagnostico());
						diagPrincipal.setAcronimo(diagnosticosVal.get(0).getAcronimoDiagnostico());
						diagnosticos.add(diagPrincipal);
					}
					this.errores = dtoValoracion.validate(diagnosticos, dtoValoracion.getTipoDiagnostico(), this.errores, plantilla.getNombre(),plantilla);
					
					//2) cargar plantilla de la valoracion y validación de los campos requeridos
//					plantilla.setSeccionesFijas(Plantillas.cargarPlantillaValoracionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", Integer.parseInt(plantilla.getCodigoPK()),ingreso.intValue(),dtoValoracion.getCodigoPk()).getSeccionesFijas());
					plantilla.setSeccionesFijas(Plantillas.cargarPlantillaValoracionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", Integer.parseInt(plantilla.getCodigoPK()),ingreso.intValue(),ConstantesBD.codigoNuncaValidoDouble).getSeccionesFijas());
					this.errores = Plantillas.validacionCamposPlantilla(plantilla, this.errores);
					logger.info("************* llegan errores jorge *******"+this.errores);
					plantilla.setPlantillaEnProceso(true);
					plantilla.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia);
					plantilla.setCodigoValoracionOdontologia(new BigDecimal(dtoValoracion.getCodigoPk()));
					
				}
				else if(plantilla.getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica)
				{
					//1) cargar la plantilla de evolucion (codigocita, codigoplantilla)
					DtoEvolucionOdontologica dtoEvolucion = new DtoEvolucionOdontologica();
					dtoEvolucion.setCita(Double.parseDouble(cita.getCodigoPk()+""));
					dtoEvolucion.setCodigoPaciente(paciente.getCodigoPersona());
					dtoEvolucion.setPlantilla(Integer.parseInt(plantilla.getCodigoPK()));
					
					ArrayList<DtoEvolucionOdontologica> arregloEvolucionOdo = EvolucionOdontologica.consultarEvolucion(dtoEvolucion);
					plantilla.setSeccionesFijas(Plantillas.cargarPlantillaEvolucionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", Integer.parseInt(plantilla.getCodigoPK()),ingreso.intValue(), arregloEvolucionOdo.get(0).getCodigoPk()).getSeccionesFijas());
					this.errores = Plantillas.validacionCamposPlantilla(plantilla, this.errores);
					plantilla.setPlantillaEnProceso(true);
					plantilla.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica);
					plantilla.setCodigoEvolucionOdontologia(new BigDecimal(arregloEvolucionOdo.get(0).getCodigoPk()));
					
				}
				
			}
			//logger.info("¿EXISTE COMPONENTE DE ODONTOGRAMA DE DIAGNOSTICO? : "+plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag));
		}
		if(numPlantillasEnProceso==0&&verificarDiligenciamiento)
		{
			this.errores.add("", new ActionMessage("errors.minimoCampos","diligenciar una plantilla","confirmación de la cita"));
		}
		logger.info("************************FIN SECCION VALIDACIÓN CONFIRMACION PLANTILLAS****************************************************");
		//**********************************************************************************************************
		
		UtilidadBD.iniciarTransaccion(con);
		//Si hasta el momento no hay errores de datos requeridos se prosigue con la confirmacion
		logger.info("************* llegan errores 0 *******"+this.errores);
		if(this.errores.isEmpty())
		{

			//Se verifica su hay plan de tratamiento para el ingreso del paciente
			ComponenteOdontograma mundoOdontograma = new ComponenteOdontograma();
			InfoOdontograma infoOdontograma = new InfoOdontograma();
			infoOdontograma.setInstitucion(usuario.getCodigoInstitucionInt());
			infoOdontograma.setEvaluarEstadosPlanTratamiento(false);
			infoOdontograma.setIdIngresoPaciente(paciente.getCodigoIngreso());
			
			//TODO VERIFICACION SI ESTE CONFIRMA APLICA PARA 
			mundoOdontograma.accionCargarOdontogramasSinEstadosPlan(infoOdontograma, new BigDecimal(codigoValoracion)); 
			
			
			//Se consulta el codigo del presupuesto odontologico del plan de tratamiento (si lo tiene)
			// 
			if(infoOdontograma.getInfoPlanTrata().getCodigoPk().intValue()>0)
			{
				infoOdontograma.getInfoPlanTrata().setCodigoPresupuesto(PresupuestoOdontologico.consultarCodigoPresupuestoXPlanTratamiento(infoOdontograma.getInfoPlanTrata().getCodigoPk(), ConstantesIntegridadDominio.acronimoContratadoContratado));
			}
				
			logger.info("¿SE OBTUVO CODIGO DEL PLAN DE TRATAMIENTO????????????????"+infoOdontograma.getInfoPlanTrata().getCodigoPk().intValue());
			
			//***************************CONFIRMACION DE LAS PLANTILLAS / COMPONENTES ************************************
			for(DtoPlantilla plantilla:infoAtencionCitaOdo.getPlantillas())
			{
				/*logger.info("*NOMBRE PLANTILLA EN PROCESO TRANSACCIONAL: "+plantilla.getNombre());
				logger.info("*¿esta en proceso plantilla?: "+plantilla.isPlantillaEnProceso());
				logger.info("*CODIGO FUNCIONALIDAD: "+plantilla.getCodigoFuncionalidad());
				logger.info("*EXISTE COMPONENTE ODONTOGRAMA DIAGNOSTICO: "+plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag));*/
				if(plantilla.isPlantillaEnProceso())
				{
					logger.info("*NOMBRE PLANTILLA EN PROCESO TRANSACCIONAL: "+plantilla.getNombre());
					logger.info("*¿esta en proceso plantilla?: "+plantilla.isPlantillaEnProceso());
					logger.info("*CODIGO FUNCIONALIDAD: "+plantilla.getCodigoFuncionalidad());
					logger.info("*EXISTE COMPONENTE ODONTOGRAMA DIAGNOSTICO: "+plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag));
					
					if(plantilla.getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia)
					{
						//************CONFIRMACION COMPONENTE DE INDICE DE PLACA**********************************************
						if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
						{
							ResultadoBoolean resultado = UtilidadOdontologia.confirmarIndicePlaca(con, infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario(), plantilla.getConsecutivoHistorico(), "");
							if(!resultado.isTrue())
							{
								this.errores.add("",new ActionMessage("errors.notEspecific",resultado.getDescripcion()+" (plantilla "+plantilla.getNombre()+")"));
							}
						}
						//*************CONFIRMACION COMPONENTE ANTECEDENTES ODONTOLOGICOS**************************************
						if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
						{
							ResultadoBoolean resultado = UtilidadOdontologia.confirmarAntecedenteOdontologico(con, infoAtencionCitaOdo.getUsuarioConfirma().getCodigoPersona(), infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario(), plantilla.getConsecutivoHistorico(), "");
							if(!resultado.isTrue())
							{
								this.errores.add("",new ActionMessage("errors.notEspecific",resultado.getDescripcion()+" (plantilla "+plantilla.getNombre()+")"));
							}
						}
						//*****************************************************************************************************
						//*************CONFIRMACION COMPONENTE ODONTOGRAMA DIAGNÓSTICO**************************************
						if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag))
						{	
							if(infoOdontograma.getInfoPlanTrata().getCodigoPk().doubleValue()>0)
							{
								if(!PlanTratamiento.confirmarOdontograma(con, infoOdontograma.getInfoPlanTrata(), plantilla.getCodigoValoracionOdontologia().intValue(), ConstantesBD.codigoNuncaValido, cita.getCodigoPk(), infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario()))
								{
									this.errores.add("",new ActionMessage("errors.problemasGenericos","al confirmar el odontograma de diagnostico (plantilla "+plantilla.getNombre()+")"));
								}
							}
							
							
						}
						//*****************************************************************************************************
					}
					else if(plantilla.getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica)
					{
						logger.info("errores vacios Nro1? "+this.errores.isEmpty());
						//************CONFIRMACION COMPONENTE DE INDICE DE PLACA**********************************************
						if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
						{
							
							
							ResultadoBoolean resultado = UtilidadOdontologia.confirmarIndicePlaca(con, infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario(), "",plantilla.getConsecutivoHistorico());
							
						
							if(!resultado.isTrue())
							{
								this.errores.add("",new ActionMessage("errors.notEspecific",resultado.getDescripcion()+" (plantilla "+plantilla.getNombre()+")"));
								logger.info("errores vacios Nro1.1? "+this.errores.isEmpty());
							}
							
						}
						//*********CONFIRMACION COMPONENTE ANTECEDENTES ODONTOLOGICOS**************************************************
						if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
						{
							ResultadoBoolean resultado = UtilidadOdontologia.confirmarAntecedenteOdontologico(con, infoAtencionCitaOdo.getUsuarioConfirma().getCodigoPersona(), infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario(), "", plantilla.getConsecutivoHistorico());
							if(!resultado.isTrue())
							{
								this.errores.add("",new ActionMessage("errors.notEspecific",resultado.getDescripcion()+" (plantilla "+plantilla.getNombre()+")"));
								logger.info("errores vacios Nro1.2? "+this.errores.isEmpty());
							}
						}
						//*****************************************************************************************************
						//*************CONFIRMACION COMPONENTE ODONTOGRAMA EVOLUCION**************************************
						if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol))
						{
							if(infoOdontograma.getInfoPlanTrata().getCodigoPk().doubleValue()>0)
							{
								
								//  TODO DEBE TRAER OJO ESTA 
								
								
								
								
								
								
								if(!PlanTratamiento.confirmarOdontograma(con, infoOdontograma.getInfoPlanTrata(),  ConstantesBD.codigoNuncaValido,plantilla.getCodigoEvolucionOdontologia().intValue(), cita.getCodigoPk(), infoAtencionCitaOdo.getUsuarioConfirma().getLoginUsuario()))
								{
									this.errores.add("",new ActionMessage("errors.problemasGenericos","al confirmar el odontograma de evolución (plantilla "+plantilla.getNombre()+")"));
									logger.info("errores vacios Nro1.3? "+this.errores.isEmpty());
								}
								logger.info("errores vacios Nro2? "+this.errores.isEmpty());
								procesoConfirmacionOdontogramaEvo(con,plantilla,infoOdontograma,citaTemp,usuario,paciente);
								logger.info("errores vacios Nro3? "+this.errores.isEmpty());
							}
							
							
						}
						//*****************************************************************************************************
					}
					
					
				}
			}
			
			//**************************************************************************************************************
		}
		logger.info("************* llegan errores 1 *******"+this.errores);
		if(this.errores.isEmpty())
		{
			//************************CONFIRMACION DE LA CITA*****************************************************
			/*
			DtoCitaOdontologica citaLog = CitaOdontologica.obtenerCitaOdontologica(con, cita.getCodigoPk(), usuario.getCodigoInstitucionInt());
			citaLog.setUsuarioModifica(usuario.getLoginUsuario());
			if(CitaOdontologica.insertLogCitaOdontologica(con, citaLog)>0)*/
			{
			
				ResultadoBoolean resultado = atencionCitasDao.confirmarDatosCita(con, cita, infoAtencionCitaOdo.getUsuarioConfirma());
				
				if(resultado.isTrue())
				{
					for(DtoServicioCitaOdontologica servicio:citaTemp.getServiciosCitaOdon())
					{
						logger.info("Estado HC del servicio encontrado: "+servicio.getEstadoHistoriaClinicaSolicitud().getCodigo());
						if(servicio.getEstadoHistoriaClinicaSolicitud().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada)
						{
							logger.info("1");
							logger.info("\n\n\n >>>>> Estado Cita para CONFIRMAR  Cargo >> " +cita.getTipo());
							//Se actualiza el estado de la solicitud
							if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta))
						 	 {
								resultado = Solicitud.cambiarEstadosSolicitudStatico(con, servicio.getNumeroSolicitud(), ConstantesBD.codigoEstadoFCargada, ConstantesBD.codigoEstadoHCInterpretada);
						 	 }else
						 	 {
						 		resultado = Solicitud.cambiarEstadosSolicitudStatico(con, servicio.getNumeroSolicitud(), 0, ConstantesBD.codigoEstadoHCInterpretada);
						 	 }
							if(resultado.isTrue())
							{
								Solicitud sol = new Solicitud();
								//Se actualiza el médico que responde
								try 
								{
									if(sol.actualizarMedicoRespondeTransaccional(con, servicio.getNumeroSolicitud(), infoAtencionCitaOdo.getUsuarioConfirma(), ConstantesBD.continuarTransaccion)<=0)
										errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional que responde del servicio "+servicio.getNombreServicio()));
								} 
								catch (Exception e) 
								{
									logger.error("Error en actualizarOrden: "+e);
									errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional que responde del servicio "+servicio.getNombreServicio()));
								} 
								
								//Se actualiza la especialidad que responde
								if(Solicitud.actualizarEspecialidadProfResponde(con, servicio.getNumeroSolicitud(), cita.getAgendaOdon().getEspecialidadUniAgen())<=0)
								{
									errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la especialidad que responde del servicio "+servicio.getNombreServicio()));
								}
								
								
								//Se actualiza el pool del médico si solo tiene 1 vigente
								ArrayList array=Utilidades.obtenerPoolesMedico(con,fechaSistema,infoAtencionCitaOdo.getUsuarioConfirma().getCodigoPersona());
								//En el caso de que el médico tenga solo 
								if(array.size()==1)
								{
									int codigoPool = Integer.parseInt(array.get(0)+"");
									if(sol.actualizarPoolSolicitud(con,servicio.getNumeroSolicitud(),codigoPool)>0)
									{
										if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(usuario.getCodigoInstitucionInt())))
										{
											if(UtilidadesFacturacion.esSolicitudFacturada(con, new BigDecimal(servicio.getNumeroSolicitud())))
											{
												//Se hace el calculo del pool y se actualiza la factura
												resultado = UtilidadesFacturacion.actualizarPoolFacturaSolicitud(con, servicio.getNumeroSolicitud()+"", codigoPool, infoAtencionCitaOdo.getUsuarioConfirma().getCodigoPersona());
												
												if(!resultado.isTrue())
												{
													this.errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
												}
											}
										}	
											
									}
									else
									{
										this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el pool médico para el servicio "+servicio.getNombreServicio()));
									}
								}
							}
							else
							{
								this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado historia clínica para el servicio "+servicio.getNombreServicio()));
							}
						}
						else if(servicio.getEstadoHistoriaClinicaSolicitud().getCodigo()==ConstantesBD.codigoEstadoHCAnulada)
						{
							logger.info("2");

							if(UtilidadesFacturacion.esSolicitudFacturada(con, new BigDecimal(servicio.getNumeroSolicitud())))
							{
								if(!Solicitud.cambiarEstadosSolicitudStatico(con, servicio.getNumeroSolicitud(), 0, ConstantesBD.codigoEstadoHCAnulada).isTrue())
								{
									this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado historia clínica (Anulacion) para el servicio "+servicio.getNombreServicio()));
								}
							}
							else
							{
								if(!Solicitud.cambiarEstadosSolicitudStatico(con, servicio.getNumeroSolicitud(), ConstantesBD.codigoEstadoFAnulada, ConstantesBD.codigoEstadoHCAnulada).isTrue())
								{
									this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado historia clínica y facturacion (Anulacion) para el servicio "+servicio.getNombreServicio()));
								}
							}
						}
					}
				}
				else
				{
					this.errores.add("",new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				}
			}
		/*	else
			{
				this.errores.add("", new ActionMessage("errors.problemasGenericos","registrando el log de modificaciï¿½n de la cita odontolï¿½gica"));
			}*/
			//*****************************************************************************************************
		}
		logger.info("************* llegan errores *******"+this.errores);
		if(this.errores.isEmpty())
		{
			UtilidadBD.finalizarTransaccion(con);
			
			///segun el documento esto no debe quedar dentro de la transaccion, si no es exitosa solamente se muestra el warning
			//pero no debe afectar lo demás
			generarFacturaAutomatica(usuario, paciente, idSession, citaTemp, institucion);
			//UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	
		UtilidadBD.closeConnection(con);
	}

	/**
	 * Metodo para Generar la(s) factura(s) Automatica(s)
	 * @param usuario
	 * @param paciente
	 * @param idSession
	 * @param citaTemp
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param institucion 
	 * @throws BDException 
	 * @see      
	 */
	private void generarFacturaAutomatica(	UsuarioBasico usuario,
											PersonaBasica paciente, 
											String idSession, 
											DtoCitaOdontologica citaTemp, InstitucionBasica institucion) throws BDException 
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionManejaFacturacionAutomatica(usuario.getCodigoInstitucionInt())))
		{	
			/*SI SALE BIEN EL PROCESO ENTONCES GENERAMOS LA FACTURA*/
			ArrayList<BigDecimal> listaSolicitudes = new ArrayList<BigDecimal>();
			for(DtoServicioCitaOdontologica servicio:citaTemp.getServiciosCitaOdon())
			{
				listaSolicitudes.add(new BigDecimal(servicio.getNumeroSolicitud()));
			}
			
			Connection con= UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(con);
			DtoParametroFacturaAutomatica dtoParametros = new DtoParametroFacturaAutomatica(con, Cargos.obtenerCodigosPkCargosDadoSolicitudes(con, listaSolicitudes), usuario, paciente, idSession);
			dtoParametros.setCodigoInstitucion(Utilidades.convertirAEntero(institucion.getCodigo()));
			facturasAutomaticas= FacturaOdontologicaAutomatica.insertar(dtoParametros, paciente.getCodigoIngreso()); 
			//HibernateUtil.endTransaction();
			UtilidadBD.finalizarTransaccion(con);
			facturasAutomaticas.logger();
		}	
	}
	
	/**
	 * Método implementado confirmar el odontograma de evolución respecto 
	 * @param con
	 * @param plantilla
	 * @param infoOdontograma
	 * @param citaTemp
	 * @param usuario 
	 * @param paciente 
	 */
	private void procesoConfirmacionOdontogramaEvo(Connection con,
			DtoPlantilla plantilla, InfoOdontograma infoOdontograma,
			DtoCitaOdontologica citaTemp, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		
		//************SE RECORRE LA INFORMACION DEL PLAN DE TRTAAMIENTO*************************************
		//Se filtra la informacion por la valoracion o evolución que se está tratando de confirmar
		//1) SECCION DETALLE PLAN DE TRATAMIENTO
		for(InfoDetallePlanTramiento detalle:infoOdontograma.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{	
			for(InfoHallazgoSuperficie hallazgo:detalle.getDetalleSuperficie())
			{
			
				procesoConfirmacionOdontogramaEvoDetallado(con,plantilla,infoOdontograma,citaTemp,usuario,paciente,hallazgo);
			}
		}
		//2) SECCION OTROS HALLAZGOS PLAN DE TRATAMIENTO
		for(InfoDetallePlanTramiento detalle:infoOdontograma.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			for(InfoHallazgoSuperficie hallazgo:detalle.getDetalleSuperficie())
			{
				procesoConfirmacionOdontogramaEvoDetallado(con,plantilla,infoOdontograma,citaTemp,usuario,paciente,hallazgo);
			}
		}
		//3) SECCION HALAZGOS BOCA PLAN DE TRATAMIENTO
		for(InfoHallazgoSuperficie hallazgo:infoOdontograma.getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			procesoConfirmacionOdontogramaEvoDetallado(con,plantilla,infoOdontograma,citaTemp,usuario,paciente,hallazgo);
		}
		
		
		logger.info("\n >>>>>>><<Servicios codigoEstadoHC >> \n");
		//Los servicios que no estaban marcados como evoluconados se anulan
		for(DtoServicioCitaOdontologica servicio:citaTemp.getServiciosCitaOdon())
		{
			logger.info("Servicio codigoEstadoHC >> "+servicio.getEstadoHistoriaClinicaSolicitud().getCodigo());
			if(!servicio.isEvolucionado() && servicio.getEstadoHistoriaClinicaSolicitud().getCodigo()!=ConstantesBD.codigoEstadoHCSolicitada)
			{
				servicio.getEstadoHistoriaClinicaSolicitud().setCodigo(ConstantesBD.codigoEstadoHCAnulada);
			}
		}
		
	}
	
	/**
	 * Método implementado para realizar el proceso de confirmacion del odontograma de evolucion
	 * a nivel de detalle de cada pieza dental
	 * @param con
	 * @param plantilla
	 * @param infoOdontograma
	 * @param citaTemp
	 * @param usuario
	 * @param paciente
	 * @param hallazgo
	 */
	private void procesoConfirmacionOdontogramaEvoDetallado(Connection con,
			DtoPlantilla plantilla, InfoOdontograma infoOdontograma,
			DtoCitaOdontologica citaTemp, UsuarioBasico usuario,
			PersonaBasica paciente, InfoHallazgoSuperficie hallazgo) 
	{
		
			
			
		for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
		{
			if(programa.tieneMismaEvolucionPrograma(plantilla.getCodigoEvolucionOdontologia()))
			{
				//Se verifica si un programa
			}
			
			for(InfoServicios servicio:programa.getListaServicios())
			{
				servicio.setProgramaAsociado(programa);
				//Se verifica si hace parte de la misma evolución
				if(servicio.getCodigoEvolucion().intValue()==plantilla.getCodigoEvolucionOdontologia().intValue())
				{
					//VALIDACION REALIZADO EXTERNO*********************************************
					if(servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoExterno))
					{
						//Se verifica si el servicio  hace parte de la cita para saber si se debe anular
						anularServicioPlanTratamiento(con, servicio, citaTemp, ConstantesIntegridadDominio.acronimoRealizadoExterno);
					}
					//***************************************************************************
					//VALIDACION REALIZADO INTERNO********************************************************
					if(servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)&&!UtilidadTexto.getBoolean(servicio.getGarantia()))
					{
						citaTemp.marcarEvolucionadoServicioXProgramaHallazgoPieza(UtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con, servicio.getCodigoPkProgServ().intValue()));
						//validacionesEvolucionServicioPlanTratamiento(con,servicio,citaTemp,ConstantesIntegridadDominio.acronimoRealizadoInterno,usuario,paciente,infoOdontograma.getInfoPlanTrata());
					}
					//**************************************************************************************
				}
			}
		}
	}

	/**
	 * Método implementado para realizar las validaciones de la evolución de un servicio del plan de tratamiento
	 * @param con
	 * @param servicio
	 * @param citaTemp
	 * @param acronimoRealizadoInterno
	 * @param usuario
	 * @param paciente
	 * @param infoPlanTratamiento
	 */
	private void validacionesEvolucionServicioPlanTratamiento(Connection con,InfoServicios servicio, DtoCitaOdontologica citaTemp,String acronimoRealizadoInterno, UsuarioBasico usuario, PersonaBasica paciente, InfoPlanTratamiento infoPlanTratamiento) throws IPSException 
	{
		int codOcupacionMedica = Utilidades.obtenerOcupacionMedica(con, citaTemp.getAgendaOdon().getCodigoMedico());
		
		logger.info("Entra a la validación de realizado interno");
		
		//1) Se verifica si la cita contiene el servicio del plan de tratamiento***********************************************************
		if(!citaTemp.tieneCitaServicioXProgramaHallazgoPieza(UtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con, servicio.getCodigoPkProgServ().intValue())))
		{
			//Como no lo contiene, se verifica si el servicio se encuentra en otra cita
			DtoCitaOdontologica citaExterna = CitaOdontologica.obtenerCitaOdontologicaXProgServPlanT(con, servicio.getCodigoPkProgServ(), usuario.getCodigoInstitucionInt());
			DtoCitaOdontologica citaValidarEstado=CitaOdontologica.obtenerCitaOdontologica(con, citaExterna.getCodigoPk(), usuario.getCodigoInstitucionInt());
			
			/*
			 * Esto no se estaba usando
			 */
			//int tmpCodigoCita =servicio.getCodigoCita().intValue();//Cambios carvajal
			
			if(citaExterna.getCodigoPk()>0 && citaValidarEstado.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
			{
				//2) Se encontró cita entonces se asocia el servicio a la cita**************************************************************************************
				DtoServicioCitaOdontologica servicioExterno = citaExterna.obtenerServicioXProgramaHallazgoPieza(UtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con, servicio.getCodigoPkProgServ().intValue()));
				
				//Se inserta log del servicio antes de anular
/*				if(CitaOdontologica.insertLogServiciosCitaOdon(con, servicioExterno)>0)
				{*/
					//Se inactiva el servicio
					if(CitaOdontologica.inactivarServicioCitaOdontologica(con, usuario.getLoginUsuario(), /*tmpCodigoCita*/ servicioExterno.getCodigoPk() ))
					{
						//Se le cambia los datos a la cita a asociar
						servicioExterno.setCitaOdontologica(citaTemp.getCodigoPk());
						servicioExterno.setCodigoAgenda(citaTemp.getAgenda());
						servicioExterno.setHoraInicio(citaTemp.getHoraInicio());
						servicioExterno.setHoraFinal(citaTemp.getHoraFinal());
						servicioExterno.setFechaCita(citaTemp.getAgendaOdon().getFecha());
						servicioExterno.setUsuarioModifica(usuario.getLoginUsuario());
						//servicioExterno.setServicio(servicio.getServicio().getCodigo()); //CAMBIO
					
						
						Log4JManager.info(" ESTADO CITA "+citaValidarEstado.getEstado());
					
						//servicioExterno.setActivo(ConstantesBD.acronimoSi);//Ojo donde saco esto??
						//servicioExterno.setEstadoCita("CUM"); // cambio carvajal 
						
						
						
						int numeroSolicitud=ConstantesBD.codigoNuncaValido;
						servicioExterno.setNumeroSolicitud(numeroSolicitud);
						
						if(CitaOdontologica.insertarServiciosCitaOdontologica(con, servicioExterno)>0)
						{
							//Se verifica si la cita externa solo tenía ese servicio para saber si se debe cancelar
							//Nota * Se cancela cuando solo tenía ese servicio
							if(citaExterna.getServiciosCitaOdon().size()==1)
							{
								citaExterna.setEstado(ConstantesIntegridadDominio.acronimoEstadoCancelado);
								citaExterna.setUsuarioModifica(usuario.getLoginUsuario());
								if(!CitaOdontologica.cambiarSerOdontologicos(con, citaExterna))
								{
									this.errores.add("", new ActionMessage("errors.problemasGenericos","cancelando la cita "+citaExterna.getCodigoPk()+" por haber evolucionado el servicio "+servicio.getServicio().getNombre()));
								}
							}
						}
						else
						{
							this.errores.add("",new ActionMessage("errors.problemasGenericos","asociando el servicio "+servicio.getServicio().getNombre()+" a la cita, debido a que estaba agendado en la cita "+citaExterna.getCodigoPk()));
						}
					}
					else
					{
						this.errores.add("",new ActionMessage("errors.problemasGenericos","inactivando log para el servicio "+servicio.getServicio().getNombre()+" , que está asociado a la cita "+citaExterna.getCodigoPk()));
					}
			}
			else
			{
				
				//3) El servicio no estaba agendado**********************************************************************************
				DtoServicioCitaOdontologica nuevoServicio = new DtoServicioCitaOdontologica();
				nuevoServicio.setActivo(ConstantesBD.acronimoSi);
				nuevoServicio.setCitaOdontologica(citaTemp.getCodigoPk());
				nuevoServicio.setCodigoAgenda(citaTemp.getAgenda());
				nuevoServicio.setCodigoTipoServicio(Utilidades.obtenerTipoServicio(con, servicio.getServicio().getCodigo()+""));
				nuevoServicio.setDuracion(0);
				nuevoServicio.setEstadoCita(citaTemp.getEstado());
				nuevoServicio.setFechaCita(citaTemp.getAgendaOdon().getFecha());
				nuevoServicio.setHoraInicio(citaTemp.getHoraInicio());
				nuevoServicio.setHoraFinal(citaTemp.getHoraFinal());
				nuevoServicio.setNuevoServicio(ConstantesBD.acronimoSi);
				nuevoServicio.setNombreServicio(servicio.getServicio().getNombre());
				nuevoServicio.setCodigoProgramaHallazgoPieza(UtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con,servicio.getCodigoPkProgServ().intValue()));
				nuevoServicio.getEstadoHistoriaClinicaSolicitud().setCodigo(ConstantesBD.codigoEstadoHCSolicitada);
				nuevoServicio.setServicio(servicio.getServicio().getCodigo());
				nuevoServicio.setUnidadAgenda(citaTemp.getAgendaOdon().getUnidadAgenda());
				nuevoServicio.setUsuarioModifica(usuario.getLoginUsuario());
				nuevoServicio.setAplicaAbono(ConstantesBD.acronimoNo);
				nuevoServicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
				boolean manejaProgramas=UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
				//El servicio debe estar ammarado a un presupuesto, por lo tanto se busca el codigo_pk de la tabla presupuesto_odo_prog_ser
				BigDecimal presupuesto=PresupuestoOdontologico.consultarCodigoPresupuestoXPlanTratamiento(infoPlanTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoContratadoContratado);
				if(presupuesto!=null && presupuesto.intValue()>0)
				{
					nuevoServicio.setCodigoPresuOdoProgSer(CargosOdon.obtenerPresupuestoOdoProgSer(con, presupuesto.intValue(), new DtoProgramaServicio(servicio.getProgramaAsociado().getCodigoPkProgramaServicio(), manejaProgramas)));
				}
				
				//Se inserta el nuevo servicio en la tabla servicios_cita_odontologica
				if(CitaOdontologica.insertarServiciosCitaOdontologica(con, nuevoServicio)>0)
				{
					ArrayList<DtoServicioCitaOdontologica> serviciosControl = new ArrayList<DtoServicioCitaOdontologica>();
					serviciosControl.add(nuevoServicio);
					
					//Se realiza validacion de control de saldos y se calcula la tarifa
					CargosOdon.validacionControlAbonos(con, 
							serviciosControl, 
							new BigDecimal(paciente.getCodigoCuenta()), 
							true, 
							this.errores, usuario, citaTemp.getCodigoPk(), infoPlanTratamiento.getCodigoPk().intValue());
					
					
					/*
					 * EL DOCUMENTO DICE QUE NO SE DEBE VALIDAR QUE LA TARIFA SEA VÁLIDA
					 * 
					if(nuevoServicio.getInfoTarifa().getError().equals(""))
					{*/
						
						//Se genera la solicitud
					/*
						BigDecimal nuevaSolicitud = CitaOdontologica.generarSolicitud(
							con, 
							nuevoServicio, 
							paciente, 
							usuario, 
							citaTemp.getCodigoCentroCosto(), 
							citaTemp.getAgendaOdon().getEspecialidadUniAgen(), 
							citaTemp.getAgendaOdon().getCodigoMedico(), 
							codOcupacionMedica, 
							errores,false);
						
						
						if(nuevaSolicitud.intValue()>0)
						{
							//Se actualizan los datos del servicio de la cita. servicios_cita_odontologica
							if(!CitaOdontologica.actualizarServicioCitaOdontologica(con, nuevoServicio))
							{
								this.errores.add("", new ActionMessage("errors.problemasGenericos","actualizando los datos del nuevo servicio no agendado: "+servicio.getServicio().getNombre()));
							}
							else
							{
								//Se marca el servicio como evolucionado y se agrega a la cita
								nuevoServicio.setEvolucionado(true);
								citaTemp.getServiciosCitaOdon().add(nuevoServicio);
							}
						}
					/*}*/
				}
				else
				{
					this.errores.add("", new ActionMessage("errors.problemasGenericos","insertando el nuevo servicio "+servicio.getServicio().getNombre()+"  no agendado "));
				}
				//*******************************************************************************************************************
			}
			
		}
		else
		{
			//Se marca el servicio como evolucionado dentro de la cita
			citaTemp.marcarEvolucionadoServicioXProgramaHallazgoPieza(UtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con, servicio.getCodigoPkProgServ().intValue()));
		}
		
		this.setUsuario(usuario);
		generarSolicitudesServiciosIncluidos(con, servicio, citaTemp, codOcupacionMedica, paciente);
		
		
		//Iteracion de los articulos incluidos del servicio principal
		/**
		 * TODO pendiente por desarrollar
		 */
		
	}

	private void generarSolicitudesServiciosIncluidos(Connection con, InfoServicios servicio, DtoCitaOdontologica citaTemp, int codOcupacionMedica, PersonaBasica paciente) throws IPSException
	{
		String fechaActual = UtilidadFecha.getFechaActual(con);
		String horaActual = UtilidadFecha.getHoraActual(con);

		//2) Generación de las solicitudes para los servicios incluidos
		DtoServiPpalIncluido servicioPrincipal = new DtoServiPpalIncluido();
		servicioPrincipal.getServicio().setCodigo(servicio.getServicio().getCodigo());
		servicioPrincipal.setInstitucion(usuario.getCodigoInstitucionInt());
		servicioPrincipal.setAtencionOdontologica(true);
		
		UtilidadesFacturacion.cargarServiciosArticulosIncluidos(con, servicioPrincipal);
		
		
		logger.info("ya cargó los incluidos "+servicioPrincipal.getCodigo());
		//Iteracion de los servicios incluidos de un servicio principal
		for(DtoServiIncluidoServiPpal servicioIncluido:servicioPrincipal.getServiciosIncluidos())
		{
			logger.info("Servicios incluidos "+servicioIncluido.getCodigo());
			for(int i=servicioIncluido.getCantidad();i>=1;i--)
			{
				DtoServicioCitaOdontologica servicioCita = new DtoServicioCitaOdontologica();
				servicioCita.setServicio(servicioIncluido.getServicio().getCodigo());
				servicioCita.setNombreServicio(servicioIncluido.getServicio().getNombre()+" (servicio incluido)");
				servicioCita.setCodigoTipoServicio(ConstantesBD.codigoServicioProcedimiento+"");
				servicioCita.getInfoTarifa().setEstadoFacturacion(ConstantesBD.codigoEstadoFExento);
				
				
				//BigDecimal numeroSolicitud = CitaOdontologica.generarSolicitud(con, servicioCita, paciente, usuario, servicioIncluido.getCentroCosto().getCodigo(), citaTemp.getAgendaOdon().getEspecialidadUniAgen(), citaTemp.getAgendaOdon().getCodigoMedico(), codOcupacionMedica, errores, false);
				BigDecimal numeroSolicitud = new BigDecimal(0);
				
				if(numeroSolicitud.doubleValue()>0)
				{
					//***************INSERCION DE LA JUSTIFICACION***************************************************
					if(UtilidadJustificacionPendienteArtServ.validarNOPOS(
							con,
							numeroSolicitud.intValue(), 
							servicioIncluido.getServicio().getCodigo(), 
							false, 
							false, 
							ConstantesBD.codigoNuncaValido /*codigoConvenio , para esta parte no es necesario*/))
					{
						double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, servicioIncluido.getServicio().getCodigo(), numeroSolicitud.intValue(), ConstantesBD.codigoNuncaValido, false);
						
						if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud.intValue(), servicioIncluido.getServicio().getCodigo(), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),""))
						{
							this.errores.add("", new ActionMessage("errors.problemasGenericos","registrando justificacion No POS pendiente para el servicio incluido "+servicioIncluido.getServicio().getNombre()+", del servicio "+servicioPrincipal.getServicio().getNombre()));
						}
					}
					//********************************************************************************
					
					//*******************ASOCIACIÓN DEL SERVICIO INCLUIDO CON EL SERVICIO DEL PLAN DE TRATAMIENTO**************************++
					DtoServArtIncCitaOdo servicioPlanT = new DtoServArtIncCitaOdo();
					servicioPlanT.getServicio().setCodigo(servicioIncluido.getServicio().getCodigo());
					servicioPlanT.setCantidad(1);
					servicioPlanT.getSolicitud().setCodigo(numeroSolicitud.intValue());
					
					//Se usa este metodo para obtener el codigo servicio cita Tarea 154554
					//servicioPlanT.setServicioCitaOdo(servicio.getCodigoPkProgServ());
					servicioPlanT.setServicioCitaOdo(PlanTratamiento.obtenerCodigoPkUltimaCitaProgServPlanT(servicio.getCodigoPkProgServ(),"",new ArrayList<String>()));
					
					
					DtoInfoFechaUsuario infoFechaUsuario = new DtoInfoFechaUsuario();
					infoFechaUsuario.setFechaModifica(fechaActual);
					infoFechaUsuario.setHoraModifica(horaActual);
					infoFechaUsuario.setUsuarioModifica(usuario.getLoginUsuario());
					
					if(!PlanTratamiento.guardarServArtIncPlant(con,servicioPlanT))
					{
						this.errores.add("", new ActionMessage("errors.problemasGenericos","registrando la relación entre el servicio incluido "+servicioIncluido.getServicio().getNombre()+" y el servicio "+servicioPrincipal.getServicio().getNombre()+" del plan de tratamiento"));
					}
					
					
					//****************************************************************************************************************************
				}
				
			}
		}
	}

	/**
	 * Método implementado para anular un servicio del plan de tratamiento
	 * @param con
	 * @param servicio
	 */
	private void anularServicioPlanTratamiento(Connection con,InfoServicios servicio,DtoCitaOdontologica citaTemp,String estado)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
	
		BigDecimal numeroSolicitud = atencionCitasDao.consultarNumeroSolicitudServicioCitaXPlanTratamiento(con, servicio.getCodigoPkProgServ());
		
		//Anulación de la solicitud
		if(numeroSolicitud.longValue()>0)
		{
			
			//Se verifica si la solicitud ya fue facturada para saber si se anula el estado de facturacion o no
			if(UtilidadesFacturacion.esSolicitudFacturada(con, numeroSolicitud))
			{
				resultado = Solicitud.cambiarEstadosSolicitudStatico(con, numeroSolicitud.intValue(), 0, ConstantesBD.codigoEstadoHCAnulada);
			}
			else
			{
				resultado = Solicitud.cambiarEstadosSolicitudStatico(con, numeroSolicitud.intValue(), ConstantesBD.codigoEstadoFAnulada, ConstantesBD.codigoEstadoHCAnulada);
			}
			if(!resultado.isTrue())
			{
				this.errores.add("", new ActionMessage("errors.problemasGenericos","anulando el servicio "+servicio.getServicio().getNombre()+" marcado como "+ValoresPorDefecto.getIntegridadDominio(estado)+" (odontograma evolución). Proceso cancelado "));
			}
			else
			{
				citaTemp.marcarAnulacionSolicitudCita(numeroSolicitud);
			}
			
			//Reversión abonos de abonos
			BigDecimal codigoServicioCitaOdo = atencionCitasDao.consultarCodigoServicioCitaOdoXPlanTratamiento(con, servicio.getCodigoPkProgServ());
			if(codigoServicioCitaOdo.intValue()>0)
			{
				//@TODO REVISAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAARRRRRRRRRRRRRRRRR OOOOOOOOOOJOOOOOOOOOOOOOOOOOOO
				logger.warn("REVISAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAARRRRRRRRRRRRRRRRR OOOOOOOOOOJOOOOOOOOOOOOOOOOOOO");
//				resultado = AbonosYDescuentos.eliminarAbonos(con, codigoServicioCitaOdo, ConstantesBD.tipoMovimientoAbonoAnu);
				if(!resultado.isTrue())
				{
					errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				}
			}
			else
			{
				errores.add("", new ActionMessage("errors.notEspecific","No se encontró registro del servicio de la cita asociado al servicio del plan de tratamiento para reversar el abono"));
			}
			
		}
	}
	
	/**
	 * Método implementado para cargar las plantillas de la cita
	 * @param con
	 * @param codigoCita
	 * @param usuario
	 */
	public void capturarDatosImpresion(Connection con,BigDecimal codigoCita,UsuarioBasico usuario)
	{
		BigDecimal ingreso = UtilidadOdontologia.consultarIngresoCitaOdontologica(con, codigoCita);
		logger.info("\n\n\n Codigo INgreso >> "+ingreso);
		
		//Se cargan cada una de las plantillas que fueron registradas en la cita
		this.plantillas = atencionCitasDao.cargarPlantillasRegistradas(con, codigoCita);
		
		logger.info("cuantas plantillas encontré? "+this.plantillas.size());
		
		for(DtoPlantilla plantilla:this.plantillas)
		{
			if(plantilla.getCodigoValoracionOdontologia().intValue()>0)
			{
				double codigoValoracionOdo = plantilla.getCodigoValoracionOdontologia().doubleValue();
				
				logger.info("codigo pk plantilla: "+plantilla.getCodigoPK());
				logger.info("codigo ingreso: "+ingreso.intValue());
				logger.info("codigo Valoración Odo: "+codigoValoracionOdo);
				
				//Se carga la información parametrizable de la valoracion
				plantilla.setSeccionesFijas(Plantillas.cargarPlantillaValoracionOdon(
					con, 
					usuario.getCodigoInstitucionInt(), 
					usuario, 
					ConstantesBD.codigoNuncaValido, //codigo paciente 
					"", // tipo funcionalidad 
					Integer.parseInt(plantilla.getCodigoPK()),
					ingreso.intValue(),
					plantilla.getCodigoValoracionOdontologia().doubleValue()).getSeccionesFijas());
				
				//Se carga la información fija de la valoracion
				plantilla.getDtoValoracionOdo().setCodigoPk(codigoValoracionOdo);
				ValoracionOdontologica.consultar(con, plantilla.getDtoValoracionOdo());
				
			}
			else if(plantilla.getCodigoEvolucionOdontologia().intValue()>0)
			{
				double codigoEvolucionOdo = plantilla.getCodigoEvolucionOdontologia().doubleValue();
				
				
				
				//Se carga lña informacion parametrizable de la evolucion
				plantilla.setSeccionesFijas(Plantillas.cargarPlantillaEvolucionOdon(
					con, 
					usuario.getCodigoInstitucionInt(), 
					usuario, 
					ConstantesBD.codigoNuncaValido, //codigo paciente 
					"",  //tipo funcionalidad
					Integer.parseInt(plantilla.getCodigoPK()),
					ingreso.intValue(),
					plantilla.getCodigoEvolucionOdontologia().doubleValue()).getSeccionesFijas());
				
				///Se carga la informacion fija de la evoluion
				plantilla.getDtoEvolucionOdo().setCodigoPk(codigoEvolucionOdo);
				EvolucionOdontologica.consultar(con, plantilla.getDtoEvolucionOdo());
			}
			
			//*************************	PARTE DE LOS COMPONENTES*************************************************************
			//Componente de antecedentes odontologicos
			if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
			{
				if(plantilla.getCodigoValoracionOdontologia().intValue()>0)
				{
					plantilla.getDtoValoracionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().setValoracion(plantilla.getCodigoValoracionOdontologia().intValue());
					UtilidadOdontologia.obtenerAntecedenteOdontologicoHistorico(con, plantilla.getDtoValoracionOdo().getInfoAntecedentesOdo().getAntecedenteOdon());
				}
				else if(plantilla.getCodigoEvolucionOdontologia().intValue()>0)
				{
					plantilla.getDtoEvolucionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().setEvolucion(plantilla.getCodigoEvolucionOdontologia().intValue());
					UtilidadOdontologia.obtenerAntecedenteOdontologicoHistorico(con, plantilla.getDtoEvolucionOdo().getInfoAntecedentesOdo().getAntecedenteOdon());
				}
			}
			//Componente de indice de placa
			if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
			{
				if(plantilla.getCodigoValoracionOdontologia().intValue()>0)
				{
					plantilla.getDtoValoracionOdo().getDtoIndicePlaca().setPlantillaIngreso(Integer.parseInt(plantilla.getConsecutivoHistorico()));
					ComponenteIndicePlaca.accionesEstados(plantilla.getDtoValoracionOdo().getDtoIndicePlaca(), "imprimir");
					
				}
				else if(plantilla.getCodigoEvolucionOdontologia().intValue()>0)
				{
					plantilla.getDtoEvolucionOdo().getDtoIndicePlaca().setPlantillaEvolucionOdo(Integer.parseInt(plantilla.getConsecutivoHistorico()));
					ComponenteIndicePlaca.accionesEstados(plantilla.getDtoEvolucionOdo().getDtoIndicePlaca(), "imprimir");
				}
			}
			//Componente de odontograma de diagnóstico
		
					
			if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag)) // modificacion tipoComponenteOdontogramaDiag->Valoreacion 
			{
				logger.info("COMPONENTEEEEEEEE ODONTOGRAMA ");
				InfoOdontograma info = new InfoOdontograma();
				info.getInfoPlanTrata().setValoracion(plantilla.getCodigoValoracionOdontologia());
				info.getInfoPlanTrata().setEvolucion(plantilla.getCodigoEvolucionOdontologia());
				ComponenteOdontograma compOdontograma =  new ComponenteOdontograma();
				compOdontograma.accionCargarOdontogramaHisConf(con, info, usuario.getCodigoInstitucionInt(),  new BigDecimal(plantilla.getCodigoValoracionOdontologia().doubleValue()));
				
				if(plantilla.getCodigoValoracionOdontologia().intValue()>0)
				{
					plantilla.getDtoValoracionOdo().setInfoOdontograma(info);
					
				}
				else if(plantilla.getCodigoEvolucionOdontologia().intValue()>0)
				{
					plantilla.getDtoEvolucionOdo().setInfoOdontograma(info);
				}
				
			}
			
			// SI ES COMPENENTE TIPO ODONTOGRAMA DE DIAGNOSTICOS (EVOLUCION )
			
			if(plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol)) // modificacion tipoComponenteOdontogramaDiag->Valoreacion 
			{
				logger.info("COMPONENTEEEEEEEE ODONTOGRAMA EVO");
				InfoOdontograma info = new InfoOdontograma();
				info.getInfoPlanTrata().setValoracion(plantilla.getCodigoValoracionOdontologia());
				info.getInfoPlanTrata().setEvolucion(plantilla.getCodigoEvolucionOdontologia());
				info.setCodigoCita(codigoCita.intValue());
				ComponenteOdontogramaEvo compOdontogramaEvo =  new ComponenteOdontogramaEvo();
				compOdontogramaEvo.accionCargarOdontogramaHisConf(con, info, usuario.getCodigoInstitucionInt(),  new BigDecimal(plantilla.getCodigoEvolucionOdontologia().intValue()));
				
				logger.info("Codigo Valoracion >> "+plantilla.getCodigoValoracionOdontologia().intValue());
				logger.info("Codigo Evolucion  >> "+plantilla.getCodigoEvolucionOdontologia().intValue());
				logger.info("Codigo Cita   >> "+info.getCodigoCita());
				
				if(plantilla.getCodigoValoracionOdontologia().intValue()>0)
				{
					plantilla.getDtoValoracionOdo().setInfoOdontograma(info);
					
				}
				else if(plantilla.getCodigoEvolucionOdontologia().intValue()>0)
				{
					logger.info("Seccion progSerCita >> "+info.getInfoPlanTrata().getSeccionProgServCita().size());
					logger.info("Seccion OtrosHallazgos >> "+info.getInfoPlanTrata().getSeccionOtrosHallazgos().size());
					logger.info("Seccion HallazgosDetalle >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().size());
					logger.info("Seccion HallazgosBoca >> "+info.getInfoPlanTrata().getSeccionHallazgosBoca().size());
					
					plantilla.getDtoEvolucionOdo().setInfoOdontograma(info);
					
					logger.info("Seccion progSerCita >> "+plantilla.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionProgServCita().size());
					logger.info("Seccion OtrosHallazgos >> "+plantilla.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionOtrosHallazgos().size());
					logger.info("Seccion HallazgosDetalle >> "+plantilla.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionHallazgosDetalle().size());
					logger.info("Seccion HallazgosBoca >> "+plantilla.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionHallazgosBoca().size());
				}
				
				
				
			}
			//*****************************************************************************************************
		
		
		}// FIN CARGO PLANTILLAS
		logger.info("cuantas plantillas encontré? después de iterar: "+this.plantillas.size());
		//Se consulta la informacion del encabezado para el paciente
		this.paciente.setIdIngreso(UtilidadOdontologia.consultarIngresoCitaOdontologica(con, codigoCita));
		atencionCitasDao.cargarDatosPaciente(con, this.paciente);
	
		this.paciente.setNombreCentroAtencion(usuario.getCentroAtencion());
	}
	
	
	
	/**
	 * Método implementado para cargar la informacion de la plantilla seleccionada en el historico
	 * @param con
	 * @param codigoCita
	 * @param usuario
	 * @param codigoPkPlantilla
	 */
	public void capturarDatosImpresionXPlantillaSeleccionada(Connection con,BigDecimal codigoCita,UsuarioBasico usuario, BigDecimal codigoPkPlantilla)
	{
		BigDecimal ingreso = UtilidadOdontologia.consultarIngresoCitaOdontologica(con, codigoCita);
		logger.info("\n\n\n Codigo INgreso >> "+ingreso);
		
		this.plantillaSeleccionadaHistorico = atencionCitasDao.cargarPlantillaSeleccionadaDeHistorico(con, codigoCita, codigoPkPlantilla);
		
		logger.info("codigoPkPlantillaEncontrada? "+this.plantillaSeleccionadaHistorico.getCodigoPK());
		
		if(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue()>0)
			{
				double codigoValoracionOdo = this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().doubleValue();
				
				logger.info("codigo pk plantilla: "+this.plantillaSeleccionadaHistorico.getCodigoPK());
				logger.info("codigo ingreso: "+ingreso.intValue());
				logger.info("codigo Valoración Odo: "+codigoValoracionOdo);
				
				//Se carga la información parametrizable de la valoracion
				this.plantillaSeleccionadaHistorico.setSeccionesFijas(Plantillas.cargarPlantillaValoracionOdon(
					con, 
					usuario.getCodigoInstitucionInt(), 
					usuario, 
					ConstantesBD.codigoNuncaValido, //codigo paciente 
					"", // tipo funcionalidad 
					Integer.parseInt(this.plantillaSeleccionadaHistorico.getCodigoPK()),
					ingreso.intValue(),
					this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().doubleValue()).getSeccionesFijas());
				
				//Se carga la información fija de la valoracion
				this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().setCodigoPk(codigoValoracionOdo);
				ValoracionOdontologica.consultar(con, this.plantillaSeleccionadaHistorico.getDtoValoracionOdo());
				
			}
			else if(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue()>0)
			{
				double codigoEvolucionOdo = this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().doubleValue();
				
				
				
				//Se carga lña informacion parametrizable de la evolucion
				this.plantillaSeleccionadaHistorico.setSeccionesFijas(Plantillas.cargarPlantillaEvolucionOdon(
					con, 
					usuario.getCodigoInstitucionInt(), 
					usuario, 
					ConstantesBD.codigoNuncaValido, //codigo paciente 
					"",  //tipo funcionalidad
					Integer.parseInt(this.plantillaSeleccionadaHistorico.getCodigoPK()),
					ingreso.intValue(),
					this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().doubleValue()).getSeccionesFijas());
				
				///Se carga la informacion fija de la evoluion
				this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().setCodigoPk(codigoEvolucionOdo);
				EvolucionOdontologica.consultar(con, this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo());
			}
			
			//*************************	PARTE DE LOS COMPONENTES*************************************************************
			//Componente de antecedentes odontologicos
			if(this.plantillaSeleccionadaHistorico.existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
			{
				if(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue()>0)
				{
					this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().setValoracion(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue());
					UtilidadOdontologia.obtenerAntecedenteOdontologicoHistorico(con, this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().getInfoAntecedentesOdo().getAntecedenteOdon());
				}
				else if(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue()>0)
				{
					this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().setEvolucion(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue());
					UtilidadOdontologia.obtenerAntecedenteOdontologicoHistorico(con, this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getInfoAntecedentesOdo().getAntecedenteOdon());
				}
			}
			//Componente de indice de placa
			if(this.plantillaSeleccionadaHistorico.existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
			{
				if(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue()>0)
				{
					this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().getDtoIndicePlaca().setPlantillaIngreso(Integer.parseInt(this.plantillaSeleccionadaHistorico.getConsecutivoHistorico()));
					ComponenteIndicePlaca.accionesEstados(this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().getDtoIndicePlaca(), "imprimir");
					
				}
				else if(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue()>0)
				{
					this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getDtoIndicePlaca().setPlantillaEvolucionOdo(Integer.parseInt(this.plantillaSeleccionadaHistorico.getConsecutivoHistorico()));
					ComponenteIndicePlaca.accionesEstados(this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getDtoIndicePlaca(), "imprimir");
				}
			}
			//Componente de odontograma de diagnóstico
		
					
			if(this.plantillaSeleccionadaHistorico.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag)) // modificacion tipoComponenteOdontogramaDiag->Valoreacion 
			{
				logger.info("COMPONENTEEEEEEEE ODONTOGRAMA ");
				InfoOdontograma info = new InfoOdontograma();
				info.getInfoPlanTrata().setValoracion(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia());
				info.getInfoPlanTrata().setEvolucion(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia());
				ComponenteOdontograma compOdontograma =  new ComponenteOdontograma();
				compOdontograma.accionCargarOdontogramaHisConf(con, info, usuario.getCodigoInstitucionInt(),  new BigDecimal(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().doubleValue()));
				
				if(info.getInfoPlanTrata().getCodigoPk().intValue()==ConstantesBD.codigoNuncaValido){
					for(DtoSeccionFija dtoSeccion: plantillaSeleccionadaHistorico.getSeccionesFijas())
					{
						if(!Utilidades.isEmpty(dtoSeccion.getElementos())){
							if(dtoSeccion.getElementos().get(0).getDescripcion().equals("Odontograma de Diagnóstico")){
								dtoSeccion.setVisible(false);
								dtoSeccion.getElementos().get(0).setVisible(false);
							}
						}
					}
				}else{
				
					if(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue()>0)
					{
						this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().setInfoOdontograma(info);
						
					}
					else if(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue()>0)
					{
						this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().setInfoOdontograma(info);
					}
				}
				
			}
			
			// SI ES COMPENENTE TIPO ODONTOGRAMA DE DIAGNOSTICOS (EVOLUCION )
			
			if(this.plantillaSeleccionadaHistorico.existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol)) // modificacion tipoComponenteOdontogramaDiag->Valoreacion 
			{
				logger.info("COMPONENTEEEEEEEE ODONTOGRAMA EVO");
				InfoOdontograma info = new InfoOdontograma();
				info.getInfoPlanTrata().setValoracion(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia());
				info.getInfoPlanTrata().setEvolucion(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia());
				info.setCodigoCita(codigoCita.intValue());
				ComponenteOdontogramaEvo compOdontogramaEvo =  new ComponenteOdontogramaEvo();
				compOdontogramaEvo.accionCargarOdontogramaHisConf(con, info, usuario.getCodigoInstitucionInt(),  new BigDecimal(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue()));
				
				logger.info("Codigo Valoracion >> "+this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue());
				logger.info("Codigo Evolucion  >> "+this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue());
				logger.info("Codigo Cita   >> "+info.getCodigoCita());
				logger.info("Codigo Cita   >> "+info.getInfoPlanTrata().getCodigoPk());
				
				if(info.getInfoPlanTrata().getCodigoPk().intValue()==ConstantesBD.codigoNuncaValido){
					for(DtoSeccionFija dtoSeccion: plantillaSeleccionadaHistorico.getSeccionesFijas())
					{
						if(!Utilidades.isEmpty(dtoSeccion.getElementos())){
							if(dtoSeccion.getElementos().get(0).getDescripcion().equals("Odontograma de Evolución")){
								dtoSeccion.setVisible(false);
								dtoSeccion.getElementos().get(0).setVisible(false);
							}
						}
					}
				}else{
				
				
					if(this.plantillaSeleccionadaHistorico.getCodigoValoracionOdontologia().intValue()>0)
					{
						this.plantillaSeleccionadaHistorico.getDtoValoracionOdo().setInfoOdontograma(info);
						
					}
					else if(this.plantillaSeleccionadaHistorico.getCodigoEvolucionOdontologia().intValue()>0)
					{
						logger.info("Seccion progSerCita >> "+info.getInfoPlanTrata().getSeccionProgServCita().size());
						logger.info("Seccion OtrosHallazgos >> "+info.getInfoPlanTrata().getSeccionOtrosHallazgos().size());
						logger.info("Seccion HallazgosDetalle >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().size());
						logger.info("Seccion HallazgosBoca >> "+info.getInfoPlanTrata().getSeccionHallazgosBoca().size());
						
						this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().setInfoOdontograma(info);
						
						logger.info("Seccion progSerCita >> "+this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionProgServCita().size());
						logger.info("Seccion OtrosHallazgos >> "+this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionOtrosHallazgos().size());
						logger.info("Seccion HallazgosDetalle >> "+this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionHallazgosDetalle().size());
						logger.info("Seccion HallazgosBoca >> "+this.plantillaSeleccionadaHistorico.getDtoEvolucionOdo().getInfoOdontograma().getInfoPlanTrata().getSeccionHallazgosBoca().size());
					}
				}
				
				
			}
		//Se consulta la informacion del encabezado para el paciente
		this.paciente.setIdIngreso(UtilidadOdontologia.consultarIngresoCitaOdontologica(con, codigoCita));
		atencionCitasDao.cargarDatosPaciente(con, this.paciente);
	
		this.paciente.setNombreCentroAtencion(usuario.getCentroAtencion());
	}

	//************************GETTERS 6 SETTERS****************************************************************************

	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioBasico getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the odontologo
	 */
	public boolean isOdontologo() {
		return odontologo;
	}

	/**
	 * @param odontologo the odontologo to set
	 */
	public void setOdontologo(boolean odontologo) {
		this.odontologo = odontologo;
	}

	/**
	 * @return the arregloProfesionales
	 */
	public ArrayList<HashMap<String, Object>> getArregloProfesionales() {
		return arregloProfesionales;
	}

	/**
	 * @param arregloProfesionales the arregloProfesionales to set
	 */
	public void setArregloProfesionales(
			ArrayList<HashMap<String, Object>> arregloProfesionales) {
		this.arregloProfesionales = arregloProfesionales;
	}

	/**
	 * @return the infoAtencionCitaOdo
	 */
	public DtoInfoAtencionCitaOdo getInfoAtencionCitaOdo() {
		return infoAtencionCitaOdo;
	}

	/**
	 * @param infoAtencionCitaOdo the infoAtencionCitaOdo to set
	 */
	public void setInfoAtencionCitaOdo(DtoInfoAtencionCitaOdo infoAtencionCitaOdo) {
		this.infoAtencionCitaOdo = infoAtencionCitaOdo;
	}

	/**
	 * @return the plantillas
	 */
	public ArrayList<DtoPlantilla> getPlantillas() {
		return plantillas;
	}

	/**
	 * @param plantillas the plantillas to set
	 */
	public void setPlantillas(ArrayList<DtoPlantilla> plantillas) {
		this.plantillas = plantillas;
	}

	/**
	 * @return the paciente
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the facturasAutomaticas
	 */
	public DtoFacturaAutomaticaOdontologica getFacturasAutomaticas() {
		return facturasAutomaticas;
	}

	/**
	 * @param facturasAutomaticas the facturasAutomaticas to set
	 */
	public void setFacturasAutomaticas(
			DtoFacturaAutomaticaOdontologica facturasAutomaticas) {
		this.facturasAutomaticas = facturasAutomaticas;
	}

	public void setPlantillaSeleccionadaHistorico(
			DtoPlantilla plantillaSeleccionadaHistorico) {
		this.plantillaSeleccionadaHistorico = plantillaSeleccionadaHistorico;
	}

	public DtoPlantilla getPlantillaSeleccionadaHistorico() {
		return plantillaSeleccionadaHistorico;
	}
}

