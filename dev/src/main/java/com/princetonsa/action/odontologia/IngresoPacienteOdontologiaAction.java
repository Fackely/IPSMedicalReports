package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.RespuestaInsercionPersona;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.clonacion.UtilidadClonacion;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.IngresoPacienteForm;
import com.princetonsa.actionform.odontologia.IngresoPacienteOdontologiaForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.facturacion.DtoAutorizacionConvIngPac;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.DtoBusquedaPacientesConvOdo;
import com.princetonsa.dto.odontologia.DtoRolesTipoDeUsuario;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.Persona;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.facturasVarias.Deudores;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.manejoPaciente.PerfilNED;
import com.princetonsa.mundo.odontologia.IngresoPacienteOdontologia;
import com.servinte.axioma.orm.AutorizacionConvIngPac;
import com.servinte.axioma.orm.BonosConvIngPac;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.CiudadesId;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosHome;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.Departamentos;
import com.servinte.axioma.orm.DepartamentosId;
import com.servinte.axioma.orm.DetAutorizacionConvIngPac;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.PersonasHome;
import com.servinte.axioma.orm.delegate.administracion.CiudadesDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.AutorizacionConvIngPacDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConvenioDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ValidacionesBdConvIngPacDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;
import com.sies.hibernate.delegate.PersonasDelegate;


@SuppressWarnings("unchecked")
public class IngresoPacienteOdontologiaAction extends Action{
	
	private Logger logger = Logger.getLogger(IngresoPacienteOdontologiaAction.class);	
	IngresoPacienteOdontologia mundo;
	MessageResources fuenteMensaje=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.IngresoPacienteOdontologiaForm");
	
	
		public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			Connection con = null;
			try{
				if (form instanceof IngresoPacienteOdontologiaForm) 
				{			 
					con = UtilidadBD.abrirConexion();

					if(con == null)
					{
						request.setAttribute("CodigoDescripcionError","erros.problemasBd");
						return mapping.findForward("paginaError");
					}

					//Usuario cargado en session
					UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
					//paciente cargado en sesion 
					PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

					mundo = new IngresoPacienteOdontologia();

					IngresoPacienteOdontologiaForm forma = (IngresoPacienteOdontologiaForm)form;		
					String estado = forma.getEstado();

					Log4JManager.info("-------------------------------- Valor del Estado  >> "+forma.getEstado());			 


					if(estado.equals("empezar"))
					{ 
						forma.reset();

						// Se llena el arreglo con los roles a del usuario
						validarRolesUsuario(forma, usuario);

						if(request.getParameter("cerrarPopupNormal")!=null)
							forma.setCerrarPopupNormal(request.getParameter("cerrarPopupNormal"));

						forma.getPaciente().setTipoIdentificacion(forma.getTipoIdenPac());
						forma.getPaciente().setNumeroIdentificacion(forma.getNumeroIdenPac());

						forma.resetDtoConvenios();

						llenarDatosForma(forma, usuario);

						/* 
						 * Si el paciente NO existe se cargan los convenios parametrizados por defecto.
						 * Si el paciente SI existe se cargan los asociados a el.
						 */
						if(!forma.isExistePaciente()){
							mundo.cargarConveniosParametrizadosPorDefecto(forma); 
						}


						return accionEmpezarIngresoPacienteOdontologico(con, forma, usuario, request, mapping);

					}else if(estado.equals("guardarPaciente"))
					{
						ActionErrors errores = new ActionErrors(); 

						errores = accionGuardarPaciente(con, forma, paciente, usuario,request,mapping);

						//Se adiciona la validacion de campos parametrizados requeridos para la plantilla - Feb 20/2010
						if((errores!=null && !errores.isEmpty()) || (forma.getMensajesAlerta().size()>0 && !forma.isPreguntoContinuar()))
						{
							saveErrors(request, errores);	
							return mapping.findForward("empezarIngresarPacienteOdonto");

						}else{

							forma.setOperacionExito(ConstantesBD.acronimoSi);
							paciente.setCodigoPersona(forma.getPaciente().getCodigo());
							UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);	

							//Modificacion tarea 152635
							forma.setMensajesAlerta(new ArrayList<ElementoApResource>());

							if(paciente==null || paciente.getCodigoPersona()<=0)
							{				
								return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
							}
							UtilidadBD.closeConnection(con);			 

							if(!forma.getUrlRetorno().equals(""))
							{
								response.sendRedirect(forma.getUrlRetorno());
								return null;
							}

							forma.setExistePaciente(true);

							forma.resetDtoConvenios();
							llenarDatosForma(forma, usuario);



							return mapping.findForward("empezarIngresarPacienteOdonto");
						}
					}else if(estado.equals("comenzarEdicionBenf"))
					{					
						forma.resetMensajeBeneficiario();
						return mapping.findForward("detalleBeneficiario");					

					}else if(estado.equals("guardarBeneficiario"))
					{
						return accionGuardarDatosBeneficiario(con, forma,request,mapping);					
					}
					else if(estado.equals("nuevoBeneficiario"))
					{
						return accionNuevoBeneficiario(con, forma,usuario,request,mapping);					
					}
					else if(estado.equals("eliminarBeneficiario"))
					{
						UtilidadBD.closeConnection(con);
						return accionEliminarBeneficiario(forma,request,mapping);
					}
					else if(estado.equals("filtroCiudadesId")||estado.equals("filtroCiudadesNacimiento")||estado.equals("filtroCiudadesResidencia"))
					{
						return accionFiltroCiudades(con,forma,response);
					}



					/*
					 * Requerimiento 860 - Cambio 1.12
					 */

					else if(estado.equals("asignarPropiedad")){
						/*  * Se asigna una propiedad a la forma sin cambiar nada en la presentación  */
						return null;
					}

					else if(estado.equals("mostrarContratosConvenio"))
					{
						accionMostrarContratosConvenio(forma, usuario);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("nuevoConvenio"))
					{
						forma.resetDtoConvenios();
						forma.setMostrarNuevoConvenio(true);
						forma.setSeEstaModificando(false);
						forma.setEntroPorNuevoConvenio(true);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("validacionEnBDConvenio"))
					{
						forma.setMostrarNuevoConvenio(true);
						accionValidacionEnBDConvenio(forma, mapping, request);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("nuevoMedioAutorizacion"))
					{
						forma.setMostrarNuevoConvenio(true);
						accionNuevoMedioAutorizacion (forma, usuario,mapping, request);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("confirmarConvenio"))
					{
						forma.setMostrarNuevoConvenio(true);
						accionConfirmarConvenio(forma, mapping, request);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("cancelarConvenio"))
					{
						forma.setMostrarNuevoConvenio(false);

						// Se verifica que el convenio a cancelar sea uno que se estaba editando. De lo contrario
						// es decir si es nuevo convenio no se tiene en cuenta
						if(!forma.isEntroPorNuevoConvenio()){
							forma.getPaciente().getListaDtoSeccionConvenioPaciente().add(forma.getDtoSeccionConvenioPacienteTemp());
						}
						forma.setEntroPorNuevoConvenio(false);

						forma.setDtoSeccionConvenioPacienteTemp(null);
						forma.setSeEstaModificando(false);
						forma.resetDtoConvenios();
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("ingresarAutorizaciones"))
					{
						accionIngresarAutorizaciones(forma);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("eliminarAutorizacionesAsignadas"))
					{
						accionEliminarAutorizacionesAsignadas(forma, mapping, request);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("eliminarConveniosContratoPaciente"))
					{
						accionEliminarConveniosContratoPaciente(forma, mapping, request);
						return mapping.findForward("seccionConvenios");
					}
					else if(estado.equals("editarConveniosContratoPaciente"))
					{
						forma.setEntroPorNuevoConvenio(false);
						accionEditarConveniosContratoPaciente(forma, mapping, request, usuario);
						return mapping.findForward("seccionConvenios");
					}

					else if(estado.equals("ingresarBonos"))
					{
						accionIngresarBonos(forma, request);
						return mapping.findForward("seccionConvenios");
					}

					else if(estado.equals("nuevoBono"))
					{
						forma.setMostrarBonos(true);
						accionNuevoBono(forma, usuario, mapping, request);
						return mapping.findForward("seccionConvenios");
					}

					else if(estado.equals("eliminarBonosAsignados"))
					{
						accionEliminarBonosAsignados(forma, mapping, request);
						return mapping.findForward("seccionConvenios");
					}

					else if(estado.equals("obtenerValorSerialBono"))
					{
						accionObtenerValorSerialBono(forma, mapping, request, usuario);
						return mapping.findForward("seccionConvenios");
					}

				}
			}catch (Exception e) {
				Log4JManager.error(e);
			}
			finally{
				UtilidadBD.closeConnection(con);
			}
			return null;
		}









	/**
	 * Metodo que elimina un beneficiario de memoria
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarBeneficiario(IngresoPacienteOdontologiaForm forma,HttpServletRequest request, ActionMapping mapping) {
		forma.getArrayBeneficiarios().remove(forma.getPosBeneficiario());
		
		return mapping.findForward("empezarIngresarPacienteOdonto");
	}

	
	/**
	 * Método para Guardar la información del Pacinte Odonotologí
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionErrors accionGuardarPaciente(Connection con,IngresoPacienteOdontologiaForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request,
				ActionMapping mapping) {
			
		ActionErrors errores = new ActionErrors();
		int codPlantillaPaciente=0;
		errores= mundo.validacionDatosRequeridos(forma); 
        int insertoMotivo=1;
        int insertoBeneficiarios=1;
        int insertoBeneficiario=0;
        int modificoMotivo=1;
        int modificoBeneficiarios=1;
        int modificoBeneficiario=0;
        
        int codigoPacienteAsigndo = ConstantesBD.codigoNuncaValido;
        
        errores = Plantillas.validacionCamposPlantilla(forma.getPlantilla(), errores);
        
		if(!errores.isEmpty())
		{				
			UtilidadBD.closeConnection(con);
			return errores;
		}
		
		Paciente pacienteGeneral= new Paciente(); 
		pacienteGeneral = mundo.cargarMundoPaciente(forma,usuario.getLoginUsuario());
		
		//logger.info("motivo codigo: "+pacienteGeneral.getMotivoCitaPaciente().getCodMotivo());
		
		if(forma.isAvisoValidacionMismosNombres() &&!forma.isExistePaciente()&&!forma.isExisteComoUsuario())
		{
			forma.setPreguntoContinuar(true);
		}
		//**********VERIFICACION NOMBRES PACIENTE*****************************************
		//Se consultan pacientes que tienen el mismo nombre, solo se consulta si el indicador no estï¿½ activado
		if(!forma.isAvisoValidacionMismosNombres()&&!forma.isExistePaciente()&&!forma.isExisteComoUsuario())
		{
			forma.setPacientesMismosNombres(pacienteGeneral.validarPacienteIgualNombre(
				con, 
				forma.getPaciente().getPrimerNombre(), 
				forma.getPaciente().getSegundoNombre(), 
				forma.getPaciente().getPrimerApellido(), 
				forma.getPaciente().getSegundoApellido(),forma.getPaciente().getTipoId(),forma.getPaciente().getNumeroIdentificacion()));
			
			if(forma.getPacientesMismosNombres().size()>0)
			{
				//*************	SE AGREGA ADVERTENCIA SOBRE LOS PACIENTES CON MISMOS NOMBRES***********************
				ElementoApResource alerta = new ElementoApResource("errors.notEspecific");
				String contenido = "Ya existen pacientes con esos nombres y apellidos: ";
				for(HashMap elemento:forma.getPacientesMismosNombres())
				{
					contenido += elemento.get("tipoId") + ". " + elemento.get("numeroId") + ", ";
				}
				contenido += "Esta seguro que desea ingresar este paciente?";
				
				alerta.agregarAtributo(contenido);
				forma.getMensajesAlerta().add(alerta);
				//**************************************************************************************************
				//logger.info("Error paciente tiene los mismos nombres de uno existente ");					
				forma.setAvisoValidacionMismosNombres(true);
				UtilidadBD.closeConnection(con);
				return errores;
			}
		}
		
		if(!forma.isExistePaciente())
		{
			RespuestaValidacion respuesta = new RespuestaValidacion("Error en base datos al validar el ingreso del paciente",false);
			
	        try 
			{
	     	//Se valida si se puede ingresar el paciente
				respuesta = UtilidadValidacion.validacionIngresarPaciente(
				                con,
				                pacienteGeneral.getCodigoTipoIdentificacion(),
				                pacienteGeneral.getNumeroIdentificacion(),
				                ""
				        );
			} catch (SQLException e) 
			{
				//logger.error("Error al validar el ingreso del paciente: "+e);
			}
			//Se verifica si se puede continuar con la inserciï¿½n del paciente
			String consecutivoHC="";
			if(respuesta.puedoSeguir)
			{
				//Se inicia transaccion
				UtilidadBD.iniciarTransaccion(con);
				
				consecutivoHC=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt());
				String anioConsecutivoHC=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivoHC);
				pacienteGeneral.setNumeroHistoriaClinica(consecutivoHC); 
				pacienteGeneral.setAnioHistoriaClinica(anioConsecutivoHC);
				
				//por defecto se debe guardar false, en infoHistoSistemaAnte 153848
				pacienteGeneral.setInfoHistoSistemaAnt(false);
				
				//2) Se realiza la inserciï¿½n de paciente - persona ************************************************************************
				RespuestaInsercionPersona respInsercion = new RespuestaInsercionPersona(false,false,"",0);
				pacienteGeneral.setEstadoTransaccion(ConstantesBD.continuarTransaccion);
				try 
				{
					respInsercion = pacienteGeneral.insertarPaciente(con, "axioma@axioma.com.co", usuario.getCodigoInstitucion());
				} 
				catch (SQLException e) 
				{
					//logger.error("Error al insertar el paciente: "+e);
				}
				int codigoPaciente=respInsercion.getCodigoPersona();
				
				// Se captura el codigo de persona/paciente QUE SE ASIGNARA para guardar los registros. Anexo 860, Cambio 1.12
				codigoPacienteAsigndo = codigoPaciente;
	             
				if(respInsercion.isSalioBien())		         	 
				{
					//logger.info("Salio de InsertarPaciente1 ");
					forma.getPaciente().setCodigo(codigoPaciente);
					pacienteGeneral.setCodigoPersona(codigoPaciente); 
	         	 
					String nuevaIdentificacion=respInsercion.getNuevaIdentificacion();

					TipoNumeroId identificacion = new TipoNumeroId(pacienteGeneral.getCodigoTipoIdentificacion(), pacienteGeneral.getNumeroIdentificacion());
	                 
					//Si el paciente no tiene tipo de identificacion automï¿½tica entonces se carga en sesiï¿½n
					if (!Utilidades.esAutomaticoTipoId(con,pacienteGeneral.getCodigoTipoIdentificacion(),usuario.getCodigoInstitucionInt()) )
					{
						try 
						{
							paciente.cargar(con, identificacion);
							paciente.cargarPaciente2(con, identificacion, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
						} 
						catch (SQLException e) 
						{
							//logger.error("Error al cargar el paciente: "+e);
						}
						Observable observable = (Observable) request.getSession().getAttribute("observable");
						if (observable != null)
						{
							paciente.setObservable(observable);
							// Si ya lo habamos aadido, la siguiente lnea no hace nada
							observable.addObserver(paciente);
						}
					}
					else
					{
						pacienteGeneral.setNumeroIdentificacion(nuevaIdentificacion);
					}
					//3) Se realiza la insercion del paciente en las tabla pacientes_institucion y pacientes_institucion2*******************************
					RespuestaValidacion resp3 = new RespuestaValidacion("No se pudo asociar al paciente con la institucion",false);
					try 
					{
						resp3 = UtilidadValidacion.validacionPermisosInstitucionPaciente2
						(
								con,
								codigoPaciente,
								usuario.getCodigoInstitucion(),
								usuario.getCodigoInstitucion()
						);
					}
					catch (SQLException e) 
					{
						//logger.error("Error en el mï¿½todo validacionPermisosInstitucionPaciente2: "+e);
					}

					if(!resp3.puedoSeguir)
					{
						errores.add("Problemas ingresando el paciente", new ActionMessage("errors.noSeGraboInformacion","DE LA RELACION DEL PACIENTE CON LA INSTITUCION"));
					}else
					{
						if(!pacienteGeneral.getMotivoCitaPaciente().getCodMotivo().equals("")){
							pacienteGeneral.getMotivoCitaPaciente().setCodigoPaciente(codigoPaciente+"");
							insertoMotivo= Paciente.insertarMotivoCitaPacOdontologia(con, pacienteGeneral.getMotivoCitaPaciente());
						}  
						if(insertoMotivo<=0)
						{	         	 
							//logger.info("Se presento error Insertando Motivo Cita");
							errores.add("Problemas en la Insercion de Cita", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));								                		   
						}else
						{
							//logger.info("\n\n  Arreglo size "+ pacienteGeneral.getBeneficiariosPac().size());
							if(pacienteGeneral.getBeneficiariosPac().size()>0 && pacienteGeneral.getBeneficiariosPac().get(0).getPrimerNombre()!=null && !pacienteGeneral.getBeneficiariosPac().get(0).getPrimerNombre().equals("") )
							{	
								for(int i=0; i< pacienteGeneral.getBeneficiariosPac().size(); i++)
								{
									pacienteGeneral.getBeneficiariosPac().get(i).setCodigoPaciente(pacienteGeneral.getCodigoPersona()+"");
									insertoBeneficiario = Paciente.insertarBeneficiarioPaciente(con, pacienteGeneral.getBeneficiariosPac().get(i), usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
									if(insertoBeneficiario<=0)
									{
										i=pacienteGeneral.getBeneficiariosPac().size();
										insertoBeneficiarios=0;
									}		             		 		   
								}
							}
							if(insertoBeneficiarios>0)
							{
								//logger.info("MODIFICO CORRECTAMENTE LOS BENEFICIARIOS ");
								
								//logger.info("Plantilla >> "+ forma.getPlantilla()+" Tiene Informacion "+forma.getPlantilla().tieneInformacion());  
								if(forma.getPlantilla()!=null && forma.getPlantilla().tieneInformacion())
								{
									//guardamos la informacion de perfil ned
									if(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt())!=null && forma.getPlantilla().tieneInformacionPerfilNed(usuario.getCodigoInstitucionInt()))
									{	
										if(!mundo.insertarPerfilNed(con, forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt()), pacienteGeneral.getCodigoPersona(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()))
										{
											errores.add("", new ActionMessage("errors.notEspecific","NO INSERTA PERFIL NED"));
											//logger.info("ERROR EN INSERCION DE LOS DATOS PARAMETRIZABLES ");
										}
									}else
									{
										if(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt())!=null)
										{	
										 errores.add("Perfil Ned", new ActionMessage("errors.required","Perfil Ned "));
										}
									}
									
									ResultadoBoolean resultado=mundo.guardarCamposParametrizables(con, forma.getPlantilla(), pacienteGeneral.getCodigoPersona(), forma.getTipoFuncionalidad(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()); 
									
									if(!resultado.isTrue())
									{
										errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
										//logger.info("ERROR EN INSERCION DE LOS DATOS PARAMETRIZABLES ");
										
									}
									else
									{
										//logger.info("INSERTO CORRECTAMENTE LOS DATOS PARAMETRIZABLES ");
										
									}
								}
							}else
							{
								errores.add("Problemas en la Insercion de Beneficiarios", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));
								
							}
						}
					}
				}
				else
				{
					errores.add("Problemas ingresando el paciente", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));
				}
			}
			else
			{
				errores.add("Error al validar el ingreso del paciente", new ActionMessage("errors.notEspecific",respuesta.textoRespuesta));
			}
			
			//Se valida si hubo errores
			if(!errores.isEmpty())
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivoHC, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadBD.abortarTransaccion(con);	
				UtilidadBD.closeConnection(con);
				return errores;
			}
			else
			{
				
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivoHC, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				UtilidadBD.finalizarTransaccion(con);
				
				/* Anexo860, Cambio 1.12: Se debe capturar la iformacion de:
				 * Convenios del paciente
				 * Direccion
				 * Ciudad de residencia
				 * (cuando el paciente no existe)
				 */
				ingresarDatosAdicionalesPersona(forma, codigoPacienteAsigndo, usuario); 
				/* -----------------------------------------------------------*/

				return errores;
				
			}
		}
		//**************SI EL PACIENTE YA EXISTE SE REALIZA LA MODIFICACION********************************************************
		else
		{
			codPlantillaPaciente = mundo.verificarExistenciaPlantillaPaciente(pacienteGeneral.getCodigoPersona(), forma.getTipoFuncionalidad());
									
			int resp = 0;
			String ls_mensaje = "";
		
			//Se inicia transaccion
			UtilidadBD.iniciarTransaccion(con);
			try
			{
				//Se carga el paciente con los datos antiguos
				Paciente pacienteAnterior = new Paciente();
				pacienteAnterior.cargarPaciente(con, pacienteGeneral.getCodigoPersona());
				forma.getPaciente().setCodigo(pacienteGeneral.getCodigoPersona());
				//Se modifica el paciente
				pacienteGeneral.setEstadoTransaccion(ConstantesBD.continuarTransaccion);
				
				//por defecto se debe guardar false, en infoHistoSistemaAnte 153848
				pacienteGeneral.setInfoHistoSistemaAnt(false);
				
				resp = pacienteGeneral.modificarPaciente(con,usuario,pacienteAnterior);	
				
				// Se captura el codigo de persona/paciente CARGADO para actualizar los registros cargados. Anexo 860, Cambio 1.12
				codigoPacienteAsigndo = forma.getPaciente().getCodigo(); 
				
			}
			catch(Exception e)
			{
				resp = 0;
				//logger.error("Error realizando la modificaciï¿½n del paciente: "+e);
			}
			
			
			switch(resp)
			{
				case -1:
					ls_mensaje =
						"El documento de identificación " + pacienteGeneral.getCodigoTipoIdentificacion() +
						"-" + pacienteGeneral.getNumeroIdentificacion() + " ya esta registrado en el sistema. No se puede modificar el paciente";
					break;
				case -2:
					ls_mensaje =
						"La fecha de nacimiento debe ser anterior a la fecha actual. No se puede modificar el paciente";
					break;
				case -3:
					ls_mensaje =
						"Error al actualizar la historia clínica previa. No se puede modificar el paciente";
					break;
			}
			
			if(resp>0)
			{
				pacienteGeneral = mundo.cargarMundoPaciente(forma,usuario.getLoginUsuario()); 
				//logger.info("Motivo cita codigo "+pacienteGeneral.getMotivoCitaPaciente().getCodMotivo()); 
				if(!pacienteGeneral.getMotivoCitaPaciente().getCodMotivo().equals(""))
				{
					modificoMotivo=Paciente.modificarMotivoCitaPacOdontologia(con, pacienteGeneral.getMotivoCitaPaciente());
				}

				if(modificoMotivo>0)
				{
					//logger.info("\n Ya Modifico EL MOTIVO de LA CITA");

					//logger.info("\n Arreglo size "+ pacienteGeneral.getBeneficiariosPac().size());
					if(pacienteGeneral.getBeneficiariosPac().size()>0  && pacienteGeneral.getBeneficiariosPac().get(0).getPrimerNombre()!=null && !pacienteGeneral.getBeneficiariosPac().get(0).getPrimerNombre().equals("") )
					{	
						for(int i=0; i< pacienteGeneral.getBeneficiariosPac().size(); i++)
						{   
							if(!pacienteGeneral.getBeneficiariosPac().get(i).getCodigoTipoIdentificacion().equals("") && !pacienteGeneral.getBeneficiariosPac().get(i).getNumeroIdentificacion().equals("") )
							{
								//logger.info("\n Va A MOFICAR EL BENEFICIARIO  TipoID "+ pacienteGeneral.getBeneficiariosPac().get(i).getCodigoTipoIdentificacion()+"numId "+pacienteGeneral.getBeneficiariosPac().get(i).getNumeroIdentificacion());
								pacienteGeneral.getBeneficiariosPac().get(i).setCodigoPaciente(pacienteGeneral.getCodigoPersona()+"");
								modificoBeneficiario = Paciente.insertarBeneficiarioPaciente(con, pacienteGeneral.getBeneficiariosPac().get(i), usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
							}else
							{
								modificoBeneficiario=1;
							}
							if(modificoBeneficiario<=0)
							{
								i=forma.getArrayBeneficiarios().size();
								modificoBeneficiarios=0;
							}	
						}
					}	
					if(modificoBeneficiarios>0)
					{
						//logger.info("MODIFICO CORRECTAMENTE LOS BENEFICIARIOS ");
						resp=1;

						//logger.info("Codigo Plantilla Paciente : "+codPlantillaPaciente);
						if(codPlantillaPaciente <= 0)
						{
							//logger.info("------>verificando perfil ned.");
							//logger.info("PERFIL REQUERIDO?? -->"+forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt()).getRequerida());
							//guardamos la informacion de perfil ned
							if(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt())!=null && forma.getPlantilla().tieneInformacionPerfilNed(usuario.getCodigoInstitucionInt()))
							{	
								if(!mundo.insertarPerfilNed(con, forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt()), pacienteGeneral.getCodigoPersona(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()))
								{
									errores.add("", new ActionMessage("errors.notEspecific","NO INSERTA PERFIL NED"));
									//logger.info("ERROR EN INSERCION DE LOS DATOS PARAMETRIZABLES ");
								}
							}else
							{
								if(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt())!=null&&UtilidadTexto.getBoolean(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt()).getRequerida()))
								{	
									errores.add("Perfil Ned", new ActionMessage("errors.required","Perfil Ned "));
									//logger.info("ERROR DE PERFIL REQUERIDO");
								}
							}
							//logger.info("FIN ------>verificando perfil ned.");
							ResultadoBoolean resultado=mundo.guardarCamposParametrizables(con, forma.getPlantilla(), pacienteGeneral.getCodigoPersona(), forma.getTipoFuncionalidad(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()); 

							if(!resultado.isTrue())
							{
								errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
								//logger.info("ERROR EN INSERCION DE LOS DATOS PARAMETRIZABLES ");
								resp = 0;	
							}
							else
							{
								resp=1;
								//logger.info("INSERTO CORRECTAMENTE LOS DATOS PARAMETRIZABLES ");
							}

						}
						else
						{
							//guardamos la informacion de perfil ned
							if(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt())!=null && forma.getPlantilla().tieneInformacionPerfilNed(usuario.getCodigoInstitucionInt()))
							{	
								if(!mundo.insertarPerfilNed(con, forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt()), pacienteGeneral.getCodigoPersona(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()))
								{
									errores.add("", new ActionMessage("errors.notEspecific","NO INSERTA PERFIL NED"));
									//logger.info("ERROR EN INSERCION DE LOS DATOS PARAMETRIZABLES ");
								}
							}else
							{
								if(forma.getEscalaPerfilNed(usuario.getCodigoInstitucionInt())!=null)
								{	
								 errores.add("Perfil Ned", new ActionMessage("errors.required","Perfil Ned "));
								}
							}

							ResultadoBoolean resultado=mundo.modificarCamposParametrizablesPacOdontologia(con, forma.getPlantilla(), pacienteGeneral.getCodigoPersona(), codPlantillaPaciente, forma.getTipoFuncionalidad(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
							if(!resultado.isTrue())
							{
								errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));        		 			   
								//logger.info("ERROR EN MODIFICACION DE LOS DATOS PARAMETRIZABLES ");
								resp = 0;	
							}
							else
							{
								resp=1;
								//logger.info("MODIFICO CORRECTAMENTE DATOS PARAMETRIZABLES ");
							}

						}

					}else
					{
						errores.add("Problemas modificando Beneficiarios", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));
						//logger.info("Se presento error Cargando los Beneficiarios");
						resp=0;
					}
				}
			}
			else
			{
				resp=0;
				errores.add("Problemas modificando el paciente", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));
			}
			//Se realiza la validacion del proceso de modificacion
			if(resp<=0)
				errores.add("Error al realizar la modificación del paciente",new ActionMessage("errors.notEspecific",ls_mensaje));
			
			//Se valida si hubo errores
			if(!errores.isEmpty())
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return errores;	
			}
			else
			{	
				UtilidadBD.finalizarTransaccion(con);
				
				
				/* Anexo860, Cambio 1.12: Se debe capturar la iformacion de:
				 * Convenios del paciente
				 * Direccion
				 * Ciudad de residencia
				 * (cuando el paciente si existe)
				 */
				ingresarDatosAdicionalesPersona(forma, codigoPacienteAsigndo, usuario); 
				/* -----------------------------------------------------------*/
				
				
				return null;
			 	
			}
		}
		
			
	}

	
	/**
	 * Metodo que inicializa la funcionalidad del Ingreso Paciente Odontologia
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarIngresoPacienteOdontologico(Connection con, IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		ActionErrors errores = new ActionErrors();
		int codigoPkPlantilla=0;
				
		prepararInformacionPaciente(con,forma,usuario);
		codigoPkPlantilla= Plantillas.obtenerCodigoPlantillaXPacienteOdont(con,forma.getPaciente().getCodigo());
		forma.setPlantilla(Plantillas.cargarPlantillaIngresoPacienteOdontologico(con, usuario.getCodigoInstitucionInt(), usuario, forma.getPaciente().getCodigo(),forma.getTipoFuncionalidad(),codigoPkPlantilla ));
			
		//Se verifica que se hayan encontrado secciones fijas
		if(forma.getPlantilla().getSeccionesFijas().size()==0)
		{
			errores.add("",new ActionMessage("errors.noExiste2","parametrización de secciones fijas para el Ingreso del Paciente de Odontología. Por favor verifique"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
	
		cargarPerfilNed(forma, usuario, forma.getPaciente().getCodigo());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezarIngresarPacienteOdonto");
	}


	
	/**
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */
	private void cargarPerfilNed(IngresoPacienteOdontologiaForm forma,
			UsuarioBasico usuario, int codigoPersona) 
	{
		BigDecimal codigoEscala;
		forma.setPerfilNed(PerfilNED.cargarPerfilNEDXPaciente(codigoPersona, usuario.getCodigoInstitucionInt()));

		if(forma.getPerfilNed().getCodigoPk()>0)
		{
			//si existe debemos cargar el codigo de la escala perteneciente al paciente
			codigoEscala= new BigDecimal(forma.getPerfilNed().getEscala());
			forma.getPlantilla().setEscalaPerfilNed(Plantillas.cargarEscalaPerfilNed(codigoEscala, forma.getPerfilNed().getCodigoPk()), usuario.getCodigoInstitucionInt());
		}
		else
		{
			if( Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()))>0)
			{
				codigoEscala= new BigDecimal(Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()))); 
				forma.getPlantilla().setEscalaPerfilNed(Plantillas.cargarEscalaPerfilNed(codigoEscala, ConstantesBD.codigoNuncaValidoDoubleNegativo), usuario.getCodigoInstitucionInt());
			}	
		}
	}

	
	/**
	 * Metodo que carga en el form toda la informacion del paciente
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 */
	@SuppressWarnings("deprecation")
	private void prepararInformacionPaciente (Connection con,IngresoPacienteOdontologiaForm forma,UsuarioBasico usuario)
	{
		Paciente pacienteOdontologia = new Paciente();
		
		  
		//***************SE VERIFICA SI EL PACIENTE EXISTE ********************************************************************************************
		Log4JManager.info(forma.getPaciente().getNumeroIdentificacion()+": "+forma.getPaciente().getTipoIdentificacion());
		boolean existePaciente=false;
		boolean existeUsuario=false;
		try 
		{			
			existePaciente=UtilidadValidacion.existePaciente(con, forma.getPaciente().getTipoIdentificacion(), forma.getPaciente().getNumeroIdentificacion());		
		} catch (SQLException e) 
		{
			forma.setExistePaciente(false);
		}
		
		if(!existePaciente)
		{
			//se verifica si no existe como paciente entonces existe como usuario
			existeUsuario=new Paciente().cargarPersona(forma.getPaciente().getNumeroIdentificacion(), forma.getPaciente().getTipoIdentificacion());
		}

		forma.setExisteComoUsuario(existeUsuario);
		forma.setExistePaciente(existePaciente);
	 	//*****CARGAR ESTRUCTURAS***********************
		//Se deben mostrar los convenios odontologicos, adicional a filtrar los que tengan contratos vigentes -- Cambio Feb 20/2010
	    forma.setConvenios(Utilidades.obtenerConvenios (con, "", "", true, true, true));
	    
	    forma.setPaises(Utilidades.obtenerPaises(con));
		forma.setZonasDomicilio(Utilidades.consultarZonasDomicilio(con));
		forma.setOcupaciones(Utilidades.consultarOcupaciones(con));
		forma.setSexos(Utilidades.obtenerSexos(con));
		forma.setTiposSangre(Utilidades.consultarTiposSangre(con));
		forma.setEstadosCiviles(Utilidades.consultarEstadosCiviles(con));
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		forma.setEtnias(UtilidadesManejoPaciente.obtenerEtnias(con));
		forma.setReligiones(UtilidadesManejoPaciente.obtenerReligiones(con, usuario.getCodigoInstitucionInt()));
		forma.setEstudios(UtilidadesManejoPaciente.obtenerEstudios(con));
		forma.setParentezcos(Utilidades.consultarParentezcos(con));
		forma.setMotivosCita(Utilidades.consultarMotivosCita(con, usuario.getCodigoInstitucionInt(),ConstantesIntegridadDominio.acronimoMotivoLlamada));
		forma.setMediosConocimiento(Utilidades.consultarMediosConocimientoServ(con, usuario.getCodigoInstitucionInt()));	
		forma.setTiposIdentificacion(Utilidades.consultarTiposidentificacion(usuario.getCodigoInstitucionInt()));
		
		//********************************************
		
		forma.getPaciente().setIngresoEdad(UtilidadTexto.getBoolean(ValoresPorDefecto.getFechaNacimiento(usuario.getCodigoInstitucionInt())));
		if(forma.getPaciente().isIngresoEdad())
		{
			forma.getPaciente().setSeleccionEdad("anios");
		}
		
		// SI el Paciente Existe
		if(forma.isExistePaciente()||forma.isExisteComoUsuario())
		{
		
			forma.getPaciente().setCodigo(Persona.obtenerCodigoPersona(con, forma.getPaciente().getNumeroIdentificacion(), forma.getPaciente().getTipoIdentificacion()));
			
			if(forma.isExistePaciente()){
				try{
					pacienteOdontologia.cargarPaciente(con,forma.getPaciente().getCodigo());	
				}catch(Exception e){
					//logger.info("\n\n Ocurrio un error CARGANDO PACIENTEEE >>> "+e);
				}
			}
			else if(forma.isExisteComoUsuario())
			{
				pacienteOdontologia.cargarPersona(con, forma.getPaciente().getCodigo()); // Se cambia consulta por cargarPaciente... por tarea 163036
			}
			
			
			forma.getPaciente().setCodigo(pacienteOdontologia.getCodigoPersona());
			forma.getPaciente().setTipoPersona(pacienteOdontologia.getCodigoTipoPersona());
			forma.getPaciente().setPrimerNombre(pacienteOdontologia.getPrimerNombrePersona(false));
			forma.getPaciente().setSegundoNombre(pacienteOdontologia.getSegundoNombrePersona(false));
			forma.getPaciente().setPrimerApellido(pacienteOdontologia.getPrimerApellidoPersona(false));        
			forma.getPaciente().setSegundoApellido(pacienteOdontologia.getSegundoApellidoPersona(false));        
			forma.getPaciente().setTipoIdentificacion(pacienteOdontologia.getCodigoTipoIdentificacion());
			forma.getPaciente().setNombreTipoIdentificacion(pacienteOdontologia.getTipoIdentificacion(false));
			forma.getPaciente().setNumeroId(pacienteOdontologia.getNumeroIdentificacion());       
			forma.getPaciente().setNumeroHistoriaClinica(pacienteOdontologia.getNumeroHistoriaClinica());
			forma.getPaciente().setNroHijos(pacienteOdontologia.getNumeroHijos());
			forma.getPaciente().setTelefonoFijo(pacienteOdontologia.getTelefonoFijo());
			forma.getPaciente().setTelefonoCelular(pacienteOdontologia.getTelefonoCelular());
			forma.getPaciente().setGrupoPoblacional(pacienteOdontologia.getCodigoGrupoPoblacional());
			
			forma.getPaciente().getCiudadVivienda().setDescripcion(pacienteOdontologia.getCiudad());
			forma.setMotivoCitaPaciente(Paciente.consultarMotivoCitaPacOdontologia(con, forma.getPaciente().getCodigo()));
			forma.setArrayBeneficiarios(pacienteOdontologia.consultarBenficiariosPaciente(con,forma.getPaciente().getCodigo()));
			
			
			if(forma.getArrayBeneficiarios().size()==0)
			{
				DtoBeneficiarioPaciente  dto= new DtoBeneficiarioPaciente();
				//Consulta del parametro pais residencia
	        	String paisResidenciaBen = ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt());
				String ciudadResidenciaBen = ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt());
				if(UtilidadCadena.noEsVacio(paisResidenciaBen)&&!paisResidenciaBen.equals(" - "))
				{
					dto.setPaisResidencia(paisResidenciaBen.split("-")[0]);
					forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, paisResidenciaBen.split("-")[0]));
					
					if(UtilidadCadena.noEsVacio(ciudadResidenciaBen)&&!ciudadResidenciaBen.equals(" - - "))
					{							
						dto.setCiudadResidencia(ciudadResidenciaBen.split("-")[0]+ConstantesBD.separadorSplit+ciudadResidenciaBen.split("-")[1]);							
					}
				}
	     	
				forma.getArrayBeneficiarios().add(dto);
				
			}
			
			
			forma.getMotivoCitaPaciente().setFechaProgramacion(UtilidadFecha.conversionFormatoFechaAAp(pacienteOdontologia.getFechaMotivoCita()));
			forma.getMotivoCitaPaciente().setHoraProgramacion(pacienteOdontologia.getHoraMotivoCita());
			forma.getMotivoCitaPaciente().setObservaciones(pacienteOdontologia.getObservacionesMotivoCita());
			forma.getPaciente().setActivo(pacienteOdontologia.getActivo());
			forma.getPaciente().setConvenio(pacienteOdontologia.getConvenioReserva());
			
			HashMap datosPersona = Utilidades.obtenerDatosPersona(con,forma.getPaciente().getTipoIdentificacion(),forma.getPaciente().getNumeroIdentificacion());
			//Se llena la forma con los datos de la persona
			forma.getPaciente().setFechaNacimiento(datosPersona.get("fecha_nacimiento_0").toString());
			forma.getPaciente().setSexo(datosPersona.get("codigo_sexo_0").toString());
			forma.getPaciente().setTelefono(datosPersona.get("telefono_0").toString());
				
		}else // NO existe el Paciente
		{
		
			// Verifico si existe tercero
			DtoDeudor deudor=Deudores.cargar(con, forma.getPaciente().getTipoIdentificacion(), forma.getPaciente().getNumeroIdentificacion(), ConstantesIntegridadDominio.acronimoOtro);
			if(Utilidades.convertirAEntero(deudor.getCodigo())!=ConstantesBD.codigoNuncaValido)
			{
				forma.getPaciente().setPrimerNombre(deudor.getPrimerNombre());
				forma.getPaciente().setSegundoNombre(deudor.getSegundoNombre());
				forma.getPaciente().setPrimerApellido(deudor.getPrimerApellido());        
				forma.getPaciente().setSegundoApellido(deudor.getSegundoApellido());
				forma.getPaciente().setTelefono(deudor.getTelefono());
			}
			else
			{
				//**************SE POSTULA LA INFORMACION POR DEFECTO***********************************
				
				//logger.info("ENTRO...............a cargarPaciente NO EXISTE ");
				
				forma.getPaciente().setNombreTipoIdentificacion(ValoresPorDefecto.getIntegridadDominio(forma.getPaciente().getTipoIdentificacion())+"");
				forma.getPaciente().setTipoPersona(ConstantesBDManejoPaciente.codigoTipoPersonaNatural+"");
				
				//Consulta la parametrizacion de paises de la institucin
				ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
				mundoInstitucion.cargar(con, usuario.getCodigoInstitucionInt());
                
				
			}
			
			if(forma.getArrayBeneficiarios().size()==0)
			{
				DtoBeneficiarioPaciente  dto= new DtoBeneficiarioPaciente();				
				forma.getArrayBeneficiarios().add(dto);				
			}
			
		}		 		
		
		//logger.info("SALIO...............de cargarPaciente");
	}

	
	/**
	 * Metodo utilizado para agregar un nuevo Beneficiario en memoria
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	 private void agregarNuevoBeneficiario(Connection con, IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario)
	 {
		 
			//Beneficiarios
				   DtoBeneficiarioPaciente dtoBeneficiario= new DtoBeneficiarioPaciente();				
					
					//Consulta del parametro paï¿½s residencia
					String paisResidenciaBen = ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt());
					String ciudadResidenciaBen = ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt());
					if(UtilidadCadena.noEsVacio(paisResidenciaBen)&&!paisResidenciaBen.equals(" - "))
					{
						dtoBeneficiario.setPaisResidencia(paisResidenciaBen.split("-")[0]);
						forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, paisResidenciaBen.split("-")[0]));
						
						if(UtilidadCadena.noEsVacio(ciudadResidenciaBen)&&!ciudadResidenciaBen.equals(" - - "))
						{							
							dtoBeneficiario.setCiudadResidencia(ciudadResidenciaBen.split("-")[0]+ConstantesBD.separadorSplit+ciudadResidenciaBen.split("-")[1]);							
						}
					}
					
					forma.getArrayBeneficiarios().add(dtoBeneficiario);			
	 }
	 
	 
	 /**
	 * Meotodo para guardar los datos de un beneficiario en memoria 
	 * @param forma
	 * @return
	 */
		private ActionForward accionGuardarDatosBeneficiario(Connection con, IngresoPacienteOdontologiaForm forma,HttpServletRequest request,
				ActionMapping mapping) {
			
			ActionErrors errores = new ActionErrors();
			String tipoId,numId;
			 DtoBeneficiarioPaciente dtoBenef=new DtoBeneficiarioPaciente();
			 dtoBenef=forma.getArrayBeneficiarios().get(forma.getPosBeneficiario());
			
			tipoId=dtoBenef.getCodigoTipoIdentificacion();
			numId=dtoBenef.getNumeroIdentificacion();
			
			if(mundo.existeBeneficiario(forma.getArrayBeneficiarios(),forma.getPosBeneficiario(),tipoId,numId))
			{
				errores.add("",new ActionMessage("errors.notEspecific","Beneficiario ya Existe"));	
			}else
			{
				// Primer Apellido
				if(dtoBenef.getPrimerApellido().equals("")  )
				{
					errores.add("Datos Beneficiario", new ActionMessage("errors.required","Primer Apellido "));	
				}
				//Primer Nombre
				if(dtoBenef.getPrimerNombre().equals("")  )
				{
					errores.add("Datos Beneficiario", new ActionMessage("errors.required","Primer Nombre "));
				}
				//Sexo
				if(dtoBenef.getSexo().equals("")  )
				{
					errores.add("Datos Beneficiario", new ActionMessage("errors.required","Sexo "));
				}
				//Parentesco
				if(dtoBenef.getParentezco().equals("")  )
				{
					errores.add("Datos Beneficiario", new ActionMessage("errors.required","Parentesco "));
				}
			}
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);			
				
			}else
			{
				forma.setOperacionExitoBenef(ConstantesBD.acronimoSi);
				forma.setMensajeBenef("Proceso Exitoso");
			}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleBeneficiario");
		}

		/**
		 * Metodo para agregar en memoria un nuevo Beneficiario
		 * @param con
		 * @param forma
		 * @param usuario
		 * @param request
		 * @param mapping
		 * @return
		 */
		private ActionForward accionNuevoBeneficiario(Connection con,IngresoPacienteOdontologiaForm forma,UsuarioBasico usuario ,HttpServletRequest request, ActionMapping mapping) {
			
			agregarNuevoBeneficiario(con, forma, usuario);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("empezarIngresarPacienteOdonto");
		}

	
		
		
		@SuppressWarnings("unused")
		private ActionForward accionFiltroCiudades(Connection con, IngresoPacienteForm ingresoPacienteForm, HttpServletResponse response) 
		{
			String resultado = "<respuesta>";
			String codigoPais = "";
			ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
			
			//Se filtran las ciudades segun estado
			if(ingresoPacienteForm.getEstado().equals("filtroCiudadesId"))
			{
				codigoPais = ingresoPacienteForm.getCodigoPaisId();
				ingresoPacienteForm.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, codigoPais));
				arregloAux = ingresoPacienteForm.getCiudadesExp();
			}
			else if(ingresoPacienteForm.getEstado().equals("filtroCiudadesNacimiento"))
			{
				codigoPais = ingresoPacienteForm.getCodigoPaisNacimiento();
				ingresoPacienteForm.setCiudadesNac(Utilidades.obtenerCiudadesXPais(con, codigoPais));
				arregloAux = ingresoPacienteForm.getCiudadesNac();
			}
			else if(ingresoPacienteForm.getEstado().equals("filtroCiudadesResidencia"))
			{
				codigoPais = ingresoPacienteForm.getCodigoPaisResidencia();
				ingresoPacienteForm.setCiudades(Utilidades.obtenerCiudadesXPais(con, codigoPais));
				arregloAux = ingresoPacienteForm.getCiudades();
			}
			
			//logger.info("Nï¿½MERO DE ELEMENTO ARREGLO CIUDADES=> "+arregloAux.size());
			//logger.info("CODIGO PAIS=>*"+codigoPais+"*");
			
			//Revision de las ciudades segun pais seleccionado
			for(int i=0;i<arregloAux.size();i++)
			{
				HashMap elemento = (HashMap)arregloAux.get(i);
				if(elemento.get("codigoPais").toString().equals(codigoPais))
					resultado += "<ciudad>" +
						"<codigo-departamento>"+elemento.get("codigoDepartamento")+"</codigo-departamento>"+
						"<codigo-ciudad>"+elemento.get("codigoCiudad")+"</codigo-ciudad>"+
						"<nombre-departamento>"+elemento.get("nombreDepartamento")+"</nombre-departamento>"+
						"<nombre-ciudad>"+elemento.get("nombreCiudad")+"</nombre-ciudad>"+
					 "</ciudad>";
			}
			
			resultado += "</respuesta>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
		        response.getWriter().write(resultado);
			}
			catch(IOException e)
			{
				//logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
			}
			return null;
		}
		
		
		/**
		 * Metodo para  realizar el filtro de ciudades
		 * @param con
		 * @param form
		 * @param response
		 * @return
		 */
		private ActionForward accionFiltroCiudades(Connection con,IngresoPacienteOdontologiaForm form,
					HttpServletResponse response) {
				
			String resultado = "<respuesta>";
			String codigoPais = "";
			ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
			
			//Se filtran las ciudades segun estado
			if(form.getEstado().equals("filtroCiudadesId"))
			{
				codigoPais = form.getPaciente().getCodigoPaisResidencia();
				form.setCiudadesExp(Utilidades.obtenerCiudadesXPais(con, codigoPais));
				arregloAux = form.getCiudadesExp();
			}
			else if(form.getEstado().equals("filtroCiudadesNacimiento"))
			{
				codigoPais = form.getPaciente().getCodigoPaisNacimiento();
				form.setCiudadesNac(Utilidades.obtenerCiudadesXPais(con, codigoPais));
				arregloAux = form.getCiudadesNac();
			}
			else if(form.getEstado().equals("filtroCiudadesResidencia"))
			{
				codigoPais = form.getPaciente().getCodigoPaisResidencia();
				form.setCiudades(Utilidades.obtenerCiudadesXPais(con, codigoPais));
				arregloAux = form.getCiudades();
			}
			
			//logger.info("Nï¿½MERO DE ELEMENTO ARREGLO CIUDADES=> "+arregloAux.size());
			//logger.info("CODIGO PAIS=>*"+codigoPais+"*");
			
			//Revision de las ciudades segun pais seleccionado
			for(int i=0;i<arregloAux.size();i++)
			{
				HashMap elemento = (HashMap)arregloAux.get(i);
				if(elemento.get("codigoPais").toString().equals(codigoPais))
					resultado += "<ciudad>" +
						"<codigo-departamento>"+elemento.get("codigoDepartamento")+"</codigo-departamento>"+
						"<codigo-ciudad>"+elemento.get("codigoCiudad")+"</codigo-ciudad>"+
						"<nombre-departamento>"+elemento.get("nombreDepartamento")+"</nombre-departamento>"+
						"<nombre-ciudad>"+elemento.get("nombreCiudad")+"</nombre-ciudad>"+
					 "</ciudad>";
			}
			
			resultado += "</respuesta>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
		        response.getWriter().write(resultado);
			}
			catch(IOException e)
			{
				//logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
			}
			return null;
			}
		
		
		
		/**
		 * Metodo para validar que paciente este cargado en sesion	
		 * @param con
		 * @param forma
		 * @param paciente
		 * @param usuario
		 * @param request
		 * @param mapping
		 * @return
		 */
		@SuppressWarnings("unused")
		private ActionForward accionValidarPaciente(Connection con,IngresoPacienteOdontologiaForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
			
			if(paciente==null || paciente.getCodigoPersona()<=0)
			{				
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
			}
			
			return null;
		}
		
		
		
		
		
		/*
		#############################################################
		#	METODOS: Anexo 860, Cambio 1.12 							#			
		#############################################################
		*/
		
		/** 
		 * Anexo 860, Cambio 1.12 - Se debe capturar la iformacion de:
		 * Convenios del paciente (validaciones bd, autorizaciones y bonos)
		 * Direccion
		 * Ciudad de residencia
		 * 
		 * Este metodo luego de insertar el registro del paciente lo que ahce es actualizar ese registro asignandole
		 * los datos que no estaban siendo contemplados en el guardar inicial.
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param codigoPacienteAsigndo
		 * @param usuario
		 */
		private void ingresarDatosAdicionalesPersona(IngresoPacienteOdontologiaForm forma, int codigoPacienteAsigndo, UsuarioBasico usuario)
		{
			if(codigoPacienteAsigndo != ConstantesBD.codigoNuncaValido)
			{
				boolean direccionNula 	= false;
				boolean ciudadNula		= false;
				
				UtilidadTransaccion.getTransaccion().begin();
				
				Personas persona = new Personas();
				
				Ciudades ciudades = new Ciudades();
				ciudades.setId(new CiudadesId());
				
				Departamentos departamentos = new Departamentos();
				departamentos.setId(new DepartamentosId());
				ciudades.setDepartamentos(departamentos);
				
				Paises paises = new Paises();
				ciudades.setPaises(paises);
				
				PersonasHome personasHome	= new PersonasHome();
				persona = personasHome.findById(codigoPacienteAsigndo);
				
				try {
					persona.setCiudadesByFkPCiudadviv(ciudades);
				} catch (Exception e) {
					Log4JManager.error("problema asignando la ciudad del paciente",e);
				}
				
				
				if(!UtilidadTexto.isEmpty(forma.getPaciente().getDireccion()))
				{
					persona.setDireccion(forma.getPaciente().getDireccion());
				}
				else{
					direccionNula = true;
				}
				
				if(!UtilidadTexto.isEmpty(forma.getPaciente().getCiudadDeptoPais()))
				{
					String vec[]=forma.getPaciente().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
					forma.getPaciente().setCodigoCiudadResidencia(vec[0]);
					forma.getPaciente().setCodigoDepartamentoResidencia(vec[1]);
					forma.getPaciente().setCodigoPaisResidencia(vec[2]);
					
					ciudades = new CiudadesDelegate().buscarCiudad(forma.getPaciente().getCodigoCiudadResidencia(), 
											forma.getPaciente().getCodigoDepartamentoResidencia(), 
											forma.getPaciente().getCodigoPaisResidencia());
					
					persona.setCiudadesByFkPCiudadviv(ciudades);
				}
				else{
					ciudadNula	= true;
				}
				
				if((ciudadNula==false) && (direccionNula==false)){
					personasHome.merge(persona);
				}
				
				UtilidadTransaccion.getTransaccion().commit();
				
				// Se captura la información de la seccion del convenio para guardarla
				guardarSeccionConvenios(forma, codigoPacienteAsigndo, usuario);
				
			}
		}
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Llena la informacion necesaria para la presentacion de la forma
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param usuario
		 */
		private void llenarDatosForma(IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario)
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			// Llenar lista de ciudades */
			ILocalizacionServicio localizacionServicio = AdministracionFabricaServicio.crearLocalizacionServicio();
			forma.setListaCiudades(localizacionServicio.listarCiudades()); 
			
			//* llenar lista de convenios de la institucion
			forma.setListaConveniosInstitucion(new ConvenioDelegate().listaConveniosActivosPorInstitucionIngresoOdonto(usuario.getCodigoInstitucionInt()));
			
			// Se cargan los convenios parametrizados por defecto para la institucion para un paciente nuevo		
			if(!forma.isYaSeCargoPaciente() && forma.isExistePaciente())
			{
				// Llenar datos del paciente en caso de que este exista */
				Personas persona = new  PersonasDelegate().buscarPersona(forma.getNumeroIdenPac(),  forma.getTipoIdenPac());
				if(persona != null)
				{
					if(!forma.isSeEstaModificando()){
						cargarDatosPersona(forma, persona);
						forma.setYaSeCargoPaciente(true);
					}
				}
			}
			
			UtilidadTransaccion.getTransaccion().commit();

			//* Llenar lista estados medios de autorizacion */
			Connection con = UtilidadBD.abrirConexion(); 
			
			String[] listaConstantesEstadosStr = {   
					ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacExitoso,
					ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacNoExitoso	};
			List<DtoIntegridadDominio> listaConstantesEstados = Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesEstadosStr, false);
			forma.setListaEstadosTiposMedioAutorizacion(new ArrayList<DtoIntegridadDominio>(listaConstantesEstados));
			
			//* Llenar lista medios de autorizacion */
			String[] listaConstantesAutorizacionesStr = {   
					ConstantesIntegridadDominio.acronimoFax,
					ConstantesIntegridadDominio.acronimoInternet,
					ConstantesIntegridadDominio.acronimoOtro,
					ConstantesIntegridadDominio.acronimoTelefonica };
			List<DtoIntegridadDominio> listaConstantesAutorizaciones = Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesAutorizacionesStr, false);
			forma.setListaTiposMedioAutorizacion(new ArrayList<DtoIntegridadDominio>(listaConstantesAutorizaciones));
			
		}

		
		

		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Actualiza la lista de contratos segun el convenio seleccionado
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param usuario
		 */
		private void accionMostrarContratosConvenio(IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario) 
		{
			forma.resetAutorizaciones();
			
			forma.resetbonos();
			
			llenarDatosForma(forma, usuario);
			
			cargarConvenioSeleccionadoParaValidaciones(forma);
		}
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Carga el convenio seleccionado para hacer las validaciones de los parametros requeridos
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 */
		private void cargarConvenioSeleccionadoParaValidaciones(IngresoPacienteOdontologiaForm forma)
		{
			if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()))
			{
				int convenioSeleccionado = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio());
				forma.setListaContratoConvenio(new ContratosDelegate().listarContratosVigentesPorConvenio(convenioSeleccionado));
				habilitarParametrosRequeridos(forma, convenioSeleccionado);
				
				forma.getDtoSeccionConvenioPaciente().setListaDtoAutorizacionConvIngPac(new ArrayList<DtoAutorizacionConvIngPac>());
				
				if(forma.getListaContratoConvenio().size() == 1){
					forma.getDtoSeccionConvenioPaciente().setCodigoContrato(forma.getListaContratoConvenio().get(0)+"");
				}
			}
			else{
				forma.setMostrarValidacionesBd(false);
				forma.setMostrarAutorizaciones(false);
				forma.setListaContratoConvenio(new ArrayList<Contratos>());
			}
		}
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Habilita los parametros requeridos para el convenio enviado
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param codConvenio
		 */
		private void habilitarParametrosRequeridos(IngresoPacienteOdontologiaForm forma, int codConvenio)
		{
			Convenios convenios  = new Convenios();
			
			UtilidadTransaccion.getTransaccion().begin();
			
			convenios = new ConveniosHome().findById(codConvenio);
			
			if(convenios.getIngPacValBd() != null){
				if(!UtilidadTexto.isEmpty(convenios.getIngPacValBd().toString())){
					forma.setMostrarValidacionesBd(UtilidadTexto.getBoolean(convenios.getIngPacValBd()));
				}else{ forma.setMostrarValidacionesBd(false); }
			}
			
			if(convenios.getIngPacReqAut() != null){
				if(!UtilidadTexto.isEmpty(convenios.getIngPacReqAut().toString())){
					forma.setMostrarAutorizaciones(UtilidadTexto.getBoolean(convenios.getIngPacReqAut()));
				}else{ forma.setMostrarAutorizaciones(false); }
			}
			
			if(convenios.getManejaBonos() != null){
				if(!UtilidadTexto.isEmpty(convenios.getManejaBonos().toString())){
					forma.setMostrarBonos(UtilidadTexto.getBoolean(convenios.getManejaBonos()));
				}else{ forma.setMostrarBonos(false); }
			}
			
			
			UtilidadTransaccion.getTransaccion().commit();
		}
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Hace el llamado a las validaciones de la abse de datos
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 */
		private void accionValidacionEnBDConvenio(IngresoPacienteOdontologiaForm forma, ActionMapping mapping, HttpServletRequest request) 
		{
			boolean pasoValidaciones = false;
			if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()))
			{
				if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato()))
				{
					DtoBusquedaPacientesConvOdo dtoBusquedaPacientesConvOdo = new DtoBusquedaPacientesConvOdo();
					{
						dtoBusquedaPacientesConvOdo.setCodContrato(forma.getDtoSeccionConvenioPaciente().getCodigoContrato());
						dtoBusquedaPacientesConvOdo.setCodigoConvenio(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()));
						dtoBusquedaPacientesConvOdo.setFechaActual(UtilidadFecha.getFechaActualTipoBD());
						dtoBusquedaPacientesConvOdo.setNumeroIdPaciente(forma.getPaciente().getNumeroIdentificacion());
						dtoBusquedaPacientesConvOdo.setTipoIdPaciente(forma.getPaciente().getTipoIdentificacion());
					}
					pasoValidaciones = mundo.validacionEnBaseDatosIngresoPaciente(dtoBusquedaPacientesConvOdo);
					
					forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
					forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().setHoraModifica(UtilidadFecha.getHoraActual());
					
					if(pasoValidaciones){
						forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().setEstado(ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacExitoso);
					}
					else{
						forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().setEstado(ConstantesIntegridadDominio.acronimoEstadoValBDConvenioPacNoExitoso);
					}
				}
				else{
					// debe seleccionar contrato 
					MessageResources fuenteMensaje=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.IngresoPacienteOdontologiaForm");
					String mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.contrato");
					mostrarCampoRequerido(mapping, request, mensajeConcreto, forma);
				}
			}
			else{
				// debe seleccionar convenio (Aca no entra porque las validaciones en BD solo se muestran cuando se selecciona un convenio)
				MessageResources fuenteMensaje=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.IngresoPacienteOdontologiaForm");
				String mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.convenio");
				mostrarCampoRequerido(mapping, request, mensajeConcreto, forma);
			}
		}
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Muestra y valida un nuevo medio de autorizacion
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param usuario
		 * @param mapping
		 * @param request
		 */
		private void accionNuevoMedioAutorizacion(IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario,  ActionMapping mapping, HttpServletRequest request) 
		{
			String nuevoSeleccionado = forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getTipoMedioAuto();
			boolean sePuedeAsignar = true;
			
			if(UtilidadTexto.isEmpty(nuevoSeleccionado)){
				sePuedeAsignar = false;
			}
			
			if(sePuedeAsignar){
				
				ArrayList<DtoAutorizacionConvIngPac> listaDotAutorizacionConvIngPac = new ArrayList(forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac());
				for (DtoAutorizacionConvIngPac detAuto : listaDotAutorizacionConvIngPac)
				{
					if(nuevoSeleccionado.equals(detAuto.getTipoMedioAuto())){
						sePuedeAsignar = false;
					}
				}
			}
			
			if(sePuedeAsignar)
			{
				UtilidadTransaccion.getTransaccion().begin();
				forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac().add(mundo.construirAutorizacionConvIngPac(forma, usuario));
				UtilidadTransaccion.getTransaccion().commit();
				forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setObservacionesCambio("");
				forma.setTipoMedioAutoSeleccionado(nuevoSeleccionado);
			}
			else
			{
				forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setTipoMedioAuto(forma.getTipoMedioAutoSeleccionado());
				String mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.asignandoMismoMedioAuto");
				mostrarErrorEspecifico(mapping, request, mensajeConcreto, forma);
			}
				
		}
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * Realiza las validaciones al confirmar que se desea agregar un nuevo convenio-contrato a la lista del paciente
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 * 
		 * @param ActionForward
		 */
		private void accionConfirmarConvenio(IngresoPacienteOdontologiaForm forma, ActionMapping mapping,HttpServletRequest request) 
		{
			boolean pasoValidacionRequediros = validarCamposRequeridos(forma, mapping, request);
			
			if (pasoValidacionRequediros) {
				
				if(!mundo.existeMismoConvenioContrato(forma))
				{
					forma.getPaciente().getListaDtoSeccionConvenioPaciente().add(llenarInformacionSeccionConvenio(forma));
					forma.setSeEstaModificando(false);
				}
				else
				{
					forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setTipoMedioAuto(forma.getTipoMedioAutoSeleccionado());
					String mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.asignandoMismoConvenioContrato");
					mostrarErrorEspecifico(mapping, request, mensajeConcreto, forma);
				}
			}
		}
		
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * 
		 * @author Cristhian Murillo
		 * 
		 * complementa la informacion del convenio-contrato qué sera cargado a la lista
		 * @param forma
		 * @return
		 */
		private DtoSeccionConvenioPaciente llenarInformacionSeccionConvenio(IngresoPacienteOdontologiaForm forma)
		{
			// Los convenios-cotnratos qué vienen por defecto se mantienen sin poder eliminar
			if(forma.getDtoSeccionConvenioPaciente().getPorDefecto() != null){ 
				if(forma.getDtoSeccionConvenioPaciente().getPorDefecto().equals(ConstantesBD.acronimoSiChar)){ 
					forma.getDtoSeccionConvenioPaciente().setEsEliminable(false);	
				} 
			} 
			else
			{
				// Por ser un registro apenas ingresado y sin guardar en el sistema se puede eliminar y modificar a menos que sea por defecto
				forma.getDtoSeccionConvenioPaciente().setEsEliminable(true);	// Indicar que se puede eliminar antes de hacer el guardado total
				forma.getDtoSeccionConvenioPaciente().setEsModificable(true);	// Indicar que se puede modificar antes de hacer el guardado total
				forma.getDtoSeccionConvenioPaciente().setPorDefecto(ConstantesBD.acronimoNoChar); 	// Los convenios cargados desde la opcion nuevo se indican que no fueron cargados por defecto
			}
			
			
			
			
			mundo.llenarDetallesConvenioContratoParaMostrar(forma);
			
			boolean estadoConfirmacionConvenioContrato = mundo.obtenerEstadoConfirmacionConvenioContrato(forma);
			if(estadoConfirmacionConvenioContrato){
				forma.getDtoSeccionConvenioPaciente().setActivo(ConstantesBD.acronimoSi);
			}
			else{
				forma.getDtoSeccionConvenioPaciente().setActivo(ConstantesBD.acronimoNo);
			}
			
			forma.setMostrarNuevoConvenio(false);
			DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente;
			dtoSeccionConvenioPaciente = new DtoSeccionConvenioPaciente();
			dtoSeccionConvenioPaciente = forma.getDtoSeccionConvenioPaciente();
			
			dtoSeccionConvenioPaciente.setValidacionesBdConvIngPac(forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac());
			
			/*
				Al momento de confirmar el convenio, si se tienen autorizaciones se le coloca el estado 
				definitivo a cada una de las autorizaciones cargadas
			 */
			for (AutorizacionConvIngPac autorizacionConvIngPac : forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac()) 
			{
				autorizacionConvIngPac.setEstado(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getEstado());
			}
			
			
			dtoSeccionConvenioPaciente.setListaDtoAutorizacionConvIngPac(forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac());
			
			return dtoSeccionConvenioPaciente;
		}
		
		

		/**
		 * Anexo 860, Cambio 1.12 -
		 * Elimina la autorizacion seleccionada
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 * 
		 */
		private void accionEliminarAutorizacionesAsignadas(IngresoPacienteOdontologiaForm forma, ActionMapping mapping,	HttpServletRequest request)
		{
			forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac().remove(forma.getPosAutorizacionesAsignadas());
			forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setObservacionesCambio("");
		}
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * Elimina el bono seleccionada
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 */
		private void accionEliminarBonosAsignados(IngresoPacienteOdontologiaForm forma, ActionMapping mapping,	HttpServletRequest request)
		{
			// Si el convenio-contrato a eliminar es un registro rpeviamente asociado al apciente se carga en una lista para ser eliminado
			long codigoBonoConveniosIngresoPacienteEliminar = forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados()).getCodigoPk();
			if(codigoBonoConveniosIngresoPacienteEliminar > 0)
			{
				forma.getDtoSeccionConvenioPaciente().getListaBonosConveniosIngresoEliminar().add(codigoBonoConveniosIngresoPacienteEliminar);
			}
			
			forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().remove(forma.getPosBonosAsignados());
		}



		/**
		 * Anexo 860, Cambio 1.12 -
		 * Elimina el convenio-contrato seleccionado
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 */
		private void accionEliminarConveniosContratoPaciente(IngresoPacienteOdontologiaForm forma, ActionMapping mapping, HttpServletRequest request) 
		{
			// Si el convenio-contrato a eliminar es un registro previamente asociado al apciente se carga en una lista para ser eliminado
			long codigoConveniosIngresoPacienteEliminar = forma.getPaciente().getListaDtoSeccionConvenioPaciente().get(forma.getPosConveniosContratoPaciente()).getCodigoConveniosIngresoPaciente();
			if(codigoConveniosIngresoPacienteEliminar > 0)
			{
				forma.getDtoSeccionConvenioPaciente().getListaConveniosIngresoEliminar().add(codigoConveniosIngresoPacienteEliminar);
			}
			
			forma.getPaciente().getListaDtoSeccionConvenioPaciente().remove(forma.getPosConveniosContratoPaciente());
		}
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * 
		 * Habilida el registro seleccionado para ser editado
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 * @param usuario
		 */
		private void accionEditarConveniosContratoPaciente(IngresoPacienteOdontologiaForm forma, ActionMapping mapping,HttpServletRequest request, UsuarioBasico usuario) 
		{
			DtoSeccionConvenioPaciente dtoSeccionConvenioPacienteEditar;
			dtoSeccionConvenioPacienteEditar = new DtoSeccionConvenioPaciente();
			
			// El objeto a mostrar es clonado para mostrarlo en la forma
			dtoSeccionConvenioPacienteEditar = (DtoSeccionConvenioPaciente) UtilidadClonacion.clonar(forma.getPaciente().getListaDtoSeccionConvenioPaciente().get(forma.getPosConveniosContratoPaciente())) ;
			
			// el objetoa modificar es quitado de la lista y cargado para ser modificado y se limpia la posición
			forma.getPaciente().getListaDtoSeccionConvenioPaciente().remove(forma.getPosConveniosContratoPaciente()); 
			forma.setPosConveniosContratoPaciente(ConstantesBD.codigoNuncaValido);
			
			// Se habilita la seccion de nuevo/editar convenio-contrato
			forma.setMostrarNuevoConvenio(true);
			forma.setSeEstaModificando(true);
			
			// Se llenan las listas de la forma y se limpia la seccion editar/nuevo convenio-contrato
			llenarDatosForma(forma, usuario);
			forma.resetDtoConvenios();
			
			// Se almacena temporalmente el dto a modificar
			forma.setDtoSeccionConvenioPacienteTemp(dtoSeccionConvenioPacienteEditar);
			
			// Se llena la lista de contatos
			forma.setListaContratoConvenio(new ContratosDelegate().listarContratosVigentesPorConvenio(Integer.parseInt(dtoSeccionConvenioPacienteEditar.getCodigoConvenio()))); 
			
			// Se muestran las secciones del convenio segun sus parametros (validacion en bd, autorizacion, bonos)
			habilitarParametrosRequeridos(forma, Integer.parseInt(dtoSeccionConvenioPacienteEditar.getCodigoConvenio()));
			
			// Se carga la informacion a la forma
			forma.setDtoSeccionConvenioPaciente(dtoSeccionConvenioPacienteEditar);
			forma.getDtoSeccionConvenioPaciente().setListaDtoAutorizacionConvIngPac(dtoSeccionConvenioPacienteEditar.getListaDtoAutorizacionConvIngPac());
		}

		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * 
		 * Acciones realizadas al darle al boton de nueva autorizacion
		 * 	
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 */
		private void accionIngresarAutorizaciones(IngresoPacienteOdontologiaForm forma) 
		{
			forma.setMostrarIngresarAutorizaciones(true);
			
			/* * Si ya se tiene seleccionado un estado de la autorizacion se conserva y luego se vuelve a asignar (Esto se hace por una tarea de calidad) */
			String conservarEstado = "";
			if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getEstado())){
				conservarEstado = forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getEstado();
			}
			
			forma.getDtoSeccionConvenioPaciente().limpiarAutorizaciones();
			
			/* * Se asigna el estado conservado */
			if(!UtilidadTexto.isEmpty(conservarEstado)){
				forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setEstado(conservarEstado);
			}
			
			
			
			// En el momento que se selecciona nueva autorizacion se toman las fechas del sistema
			forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setHoraModifica(UtilidadFecha.getHoraActual());
			forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		}
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * Error generico que indica que el campo enviado es requerido
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param mapping
		 * @param request
		 * @param campoRequerido
		 * @param forma
		 * 
		 * @param ActionForward
		 */
		private ActionForward mostrarCampoRequerido(ActionMapping mapping, HttpServletRequest request, String campoRequerido, IngresoPacienteOdontologiaForm forma) 
		{
			ActionErrors errores = new ActionErrors();
			errores.add("error operacion_no_realizada", new ActionMessage("errors.required",campoRequerido));
			saveErrors(request, errores);
			forma.setErrorSeccionConvenio(true);
			return mapping.findForward("empezarIngresarPacienteOdonto");
		}
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * Muestra un error especifico del proceso. El mensaje a mostrar es recibido por parametros
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param mapping
		 * @param request
		 * @param mensajeConcreto
		 * @param forma
		 * 
		 * @param ActionForward
		 */
		private ActionForward mostrarErrorEspecifico(ActionMapping mapping, HttpServletRequest request, String mensajeConcreto, IngresoPacienteOdontologiaForm forma) 
		{
			ActionErrors errores = new ActionErrors();
			errores.add("error operacion_no_realizada", new ActionMessage("errors.notEspecific",mensajeConcreto));
			forma.setErrorSeccionConvenio(true);
			saveErrors(request, errores);
			return mapping.findForward("empezarIngresarPacienteOdonto");
		}
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * 
		 * Si existen registros de convenios cargados al paciente actual, cuando guarde la información del 
		 * paciente serán asociados a el.
		 * 
		 * @param forma
		 * @param codigoPacienteAsigndo
		 * @param usuario
		 */
		private void guardarSeccionConvenios(IngresoPacienteOdontologiaForm forma, int codigoPacienteAsigndo, UsuarioBasico usuario)
		{
			boolean guardoConvenios = false;
			if(!Utilidades.isEmpty(forma.getPaciente().getListaDtoSeccionConvenioPaciente()))
			{
				guardoConvenios = mundo.guardarSeccionConvenios(forma, codigoPacienteAsigndo, usuario);
			}
			
			if (!guardoConvenios) {
				Log4JManager.info("no se pudo guardar la seccion de convenios");
			}
		}
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Muestra y valida un nuevo medio de autorizacion
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param usuario
		 * @param mapping
		 * @param request
		 */
		private void accionNuevoBono(IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario,  ActionMapping mapping, HttpServletRequest request) 
		{
			String nuevoSeleccionado = forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getTipoMedioAuto();
			boolean sePuedeAsignar = true;
			
			if(UtilidadTexto.isEmpty(nuevoSeleccionado)){
				sePuedeAsignar = false;
			}
			
			if(sePuedeAsignar){
				for (DtoAutorizacionConvIngPac auto : forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac()) 
				{
					if(nuevoSeleccionado.equals(auto.getTipoMedioAuto())){
						sePuedeAsignar = false;
					}
				}
			}
			
			if(sePuedeAsignar)
			{
				UtilidadTransaccion.getTransaccion().begin();
				forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac().add(mundo.construirAutorizacionConvIngPac(forma, usuario));
				UtilidadTransaccion.getTransaccion().commit();
				forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setObservacionesCambio("");
				forma.setTipoMedioAutoSeleccionado(nuevoSeleccionado);
			}
			else
			{
				forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().setTipoMedioAuto(forma.getTipoMedioAutoSeleccionado());
				String mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.asignandoMismoMedioAuto");
				mostrarErrorEspecifico(mapping, request, mensajeConcreto, forma);
			}
		}
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Acciones realizadas al darle al boton de nueva autorizacion	
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param request
		 */
		private void accionIngresarBonos(IngresoPacienteOdontologiaForm forma, HttpServletRequest request) 
		{
			// Se verifica si se selecciono un contrato
			if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato()))
			{
				ActionErrors errores = new ActionErrors();
				String mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.contrato");
				errores.add("error operacion_no_realizada", new ActionMessage("errors.required",mensajeConcreto));
				forma.setErrorSeccionConvenio(true);
				saveErrors(request, errores);
			}
			else
			{
				forma.setMostrarIngresarBonos(true);
				BonosConvIngPac bono=new BonosConvIngPac();
				bono.setUtilizado(ConstantesBD.acronimoNoChar);
				forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().add(bono);
			}
		}
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Valida si el bono se puede utilizar y le asigna el valor 
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 * @param usuario
		 */
		private void accionObtenerValorSerialBono(IngresoPacienteOdontologiaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			String valorNumeroSerial = forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados()).getNumeroSerial()+"";
			
			if(!UtilidadTexto.isEmpty(valorNumeroSerial))
			{
				DtoBusquedaEmisionBonos dtoBusquedaEmisionBonos = new DtoBusquedaEmisionBonos();
				dtoBusquedaEmisionBonos.setNumeroSerial(Long.parseLong(valorNumeroSerial));
				dtoBusquedaEmisionBonos.setCodigoConvenios(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()));
				dtoBusquedaEmisionBonos.setCodInstituciones(Integer.parseInt(usuario.getCodigoInstitucion()));
				dtoBusquedaEmisionBonos.setCodigoContrato(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato()));
				
				DtoResultado dtoResultado = new DtoResultado(); 
				
				dtoResultado = mundo.validarNumeroSerialBono(dtoBusquedaEmisionBonos);
				
				if(!dtoResultado.getErrores().isEmpty())
				{				
					forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados()).setPorcentajeDescuentos(null);
					forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados()).setValorDescuento(null);
					forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados()).setUtilizado('\0');
					
					forma.setErrorSeccionConvenio(true);
					saveErrors(request, dtoResultado.getErrores());
				}
				else
				{
					if(dtoResultado.getTipoPk().equals(ConstantesIntegridadDominio.acronimoTipoValorBonoValor))
					{
						// Agrego el valor
						forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados())
							.setValorDescuento(new BigDecimal(dtoResultado.getPk()));
						// Limpio el porcentaje
						forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados())
							.setPorcentajeDescuentos(null);
					}
					else if(dtoResultado.getTipoPk().equals(ConstantesIntegridadDominio.acronimoTipoValorBonoDescuento))
					{
						// Agrego el porcentaje
						forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados())
							.setPorcentajeDescuentos(new BigDecimal(dtoResultado.getPk()));
						// Limpio el valor
						forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados())
							.setValorDescuento(null);
					}
					
					forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac().get(forma.getPosBonosAsignados())
						.setUtilizado(ConstantesBD.acronimoNoChar);
					
				}
			}
			else { /* no se ingreso serial para el bono*/ }
			
			UtilidadTransaccion.getTransaccion().commit();
		}
		
		
		
		/**
		 * Anexo 860, Cambio 1.12 -
		 * Valida los campos en el momento de confirmar
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param mapping
		 * @param request
		 * 
		 * @param boolean
		 */
		private boolean validarCamposRequeridos(IngresoPacienteOdontologiaForm forma, ActionMapping mapping,HttpServletRequest request) 
		{
			ActionErrors errores = new ActionErrors();
			MessageResources fuenteMensaje=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.IngresoPacienteOdontologiaForm");
			String mensajeConcreto = "";
			boolean pasovalidaciones = true; 
			
			// Se verifica si se selecciono un convenio
			if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()))
			{
				pasovalidaciones = false; 
				mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.convenio");
				errores.add("error operacion_no_realizada", new ActionMessage("errors.required",mensajeConcreto));
				forma.setErrorSeccionConvenio(true);
				saveErrors(request, errores);
			}
			
			// Se verifica si se selecciono un contrato
			if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato()))
			{
				pasovalidaciones = false; 
				mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.contrato");
				errores.add("error operacion_no_realizada", new ActionMessage("errors.required",mensajeConcreto));
				forma.setErrorSeccionConvenio(true);
				saveErrors(request, errores);
			}
			
			// se verifica si se requieren las validaciones de bd
			if(forma.isMostrarValidacionesBd()){
				
				// se valida que la validacion en bd tenga un estado definido
				if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getValidacionesBdConvIngPac().getEstado()))
				{
					pasovalidaciones = false; 
					mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.faltaEstadoValidacionBd");
					errores.add("error operacion_no_realizada", new ActionMessage("errors.notEspecific",mensajeConcreto));
					forma.setErrorSeccionConvenio(true);
					saveErrors(request, errores);
				}
			}
			
			// se verifica si se requieren las validaciones de autorizacion
			if(forma.isMostrarAutorizaciones()){
				
				// se valida que por lo menos exista una autorizacion ya que es requerida
				if(Utilidades.isEmpty(forma.getDtoSeccionConvenioPaciente().getListaDtoAutorizacionConvIngPac()))
				{
					pasovalidaciones = false; 
					mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.noHayAutorizacion");
					errores.add("error operacion_no_realizada", new ActionMessage("errors.notEspecific",mensajeConcreto));
					forma.setErrorSeccionConvenio(true);
					saveErrors(request, errores);
				}
				
				// se valida que la autorizacion tenga un estado definido
				else if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getDtoAutorizacionConvIngPac().getEstado()))
				{
					pasovalidaciones = false; 
					mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.faltaEstadoAutorizacion");
					errores.add("error operacion_no_realizada", new ActionMessage("errors.notEspecific",mensajeConcreto));
					forma.setErrorSeccionConvenio(true);
					saveErrors(request, errores);
				}
			}
			
			// se verifica si se requieren los bonos
			if(forma.isMostrarBonos()){
				
				// se valida que por lo menos exista una autorizacion ya qué es requerida
				if(!Utilidades.isEmpty(forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac()))
				{
					
					for (BonosConvIngPac bonosConvIngPac : forma.getDtoSeccionConvenioPaciente().getListaBonosConvIngPac()) 
					{
						if(	(bonosConvIngPac.getValorDescuento() == null)	&&
							(bonosConvIngPac.getPorcentajeDescuentos() == null) )
						{
							pasovalidaciones = false; 
							mensajeConcreto = fuenteMensaje.getMessage("IngresoPacienteOdontologiaForm.faltaInfoBono");
							errores.add("error operacion_no_realizada", new ActionMessage("errors.notEspecific",mensajeConcreto));
							forma.setErrorSeccionConvenio(true);
							saveErrors(request, errores);
						}
					}
				}
			}
			return pasovalidaciones;
		}
		
		

		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Carga los datos del paciente:
		 * Dirección y ciudad
		 * Seccion convenio
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param persona
		 */
		private void cargarDatosPersona(IngresoPacienteOdontologiaForm forma, Personas persona) 
		{
			// Se asigna la informacion qué no fue cargada anteriormente (dirección)
			if(persona.getDireccion() != null){
				forma.getPaciente().setDireccion(persona.getDireccion());
			}
			
			// Se asigna la informacion qué no fue cargada anteriormente (ciudad de vivienda)
			if(persona.getCiudadesByFkPCiudadviv() != null){
				forma.getPaciente().setCodigoCiudadResidencia(persona.getCiudadesByFkPCiudadviv().getId().getCodigoCiudad());
				forma.getPaciente().setCodigoDepartamentoResidencia(persona.getCiudadesByFkPCiudadviv().getId().getCodigoDepartamento());
				forma.getPaciente().setCodigoPaisResidencia(persona.getCiudadesByFkPCiudadviv().getId().getCodigoPais());
				forma.getPaciente().setCiudadVivienda(persona.getCiudadesByFkPCiudadviv());
			}
			
			
			// Se limpia la seccion de convenios para evitar que se dupliquen registros
			forma.getPaciente().setListaDtoSeccionConvenioPaciente(new ArrayList<DtoSeccionConvenioPaciente>());
			
			// Si la persona es paciente y tiene convenios asociados se cargan en la forma
			if(persona.getPacientes().getConveniosIngresoPacientes() != null)
			{
				// Se iteran cada uno de los convenios asociados para mostrar su informacion
				for (Iterator iterator = persona.getPacientes().getConveniosIngresoPacientes().iterator(); iterator.hasNext();) 
				{
					// Convenios cargados asociados al paciente
					ConveniosIngresoPaciente conveniosIngresoPaciente = (ConveniosIngresoPaciente) iterator.next();
					
					DtoSeccionConvenioPaciente 	dtoSeccionConvenioPaciente;
					dtoSeccionConvenioPaciente 	= new DtoSeccionConvenioPaciente();
					
					dtoSeccionConvenioPaciente.setCodigoConveniosIngresoPaciente(conveniosIngresoPaciente.getCodigoPk());
					
					//if(conveniosIngresoPaciente.getPorDefecto().equals(ConstantesBD.acronimoNoChar)){
						// Ningún convenio ya guardado se puede eliminar
						dtoSeccionConvenioPaciente.setEsEliminable(false);
						//dtoSeccionConvenioPaciente.setEsModificable(true);
					//}
					//else{
						//dtoSeccionConvenioPaciente.setEsEliminable(false);
						// Los convenios por defecto siempre se pueden modificar s29
						dtoSeccionConvenioPaciente.setEsModificable(true); 
					//}
					
					dtoSeccionConvenioPaciente.setPorDefecto(conveniosIngresoPaciente.getPorDefecto());
					dtoSeccionConvenioPaciente.setActivo(conveniosIngresoPaciente.getActivo()+"");
					dtoSeccionConvenioPaciente.setCodigoContrato(conveniosIngresoPaciente.getContratos().getCodigo()+"");
					dtoSeccionConvenioPaciente.setNumeroContrato(conveniosIngresoPaciente.getContratos().getNumeroContrato());
					dtoSeccionConvenioPaciente.setCodigoConvenio(conveniosIngresoPaciente.getContratos().getConvenios().getCodigo()+"");
					dtoSeccionConvenioPaciente.setDescripcionConvenio(conveniosIngresoPaciente.getContratos().getConvenios().getNombre());
					
					
					// Llave primaria del registro para consultar el resto de la informacion
					long conveniosIngresoPacientePk = conveniosIngresoPaciente.getCodigoPk();
					
					// Se carga la última Validación que coincida con conveniosIngresoPaciente
					dtoSeccionConvenioPaciente.setValidacionesBdConvIngPac(new ValidacionesBdConvIngPacDelegate().obtenerUltimaValidacionBd(conveniosIngresoPacientePk));
					
					// Se carga la última Autorización que coincida con conveniosIngresoPaciente
					AutorizacionConvIngPac autorizacionConvIngPac = new AutorizacionConvIngPacDelegate().obtenerUltimaAutorizacion(conveniosIngresoPacientePk);
					if(autorizacionConvIngPac != null)
					{
						dtoSeccionConvenioPaciente.limpiarAutorizaciones();
						
						dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().setEstado(autorizacionConvIngPac.getEstado());
						dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().setFechaModifica(autorizacionConvIngPac.getFechaModifica());
						dtoSeccionConvenioPaciente.getDtoAutorizacionConvIngPac().setHoraModifica(autorizacionConvIngPac.getHoraModifica());
						
						for (Iterator iterator2 = autorizacionConvIngPac.getDetAutorizacionConvIngPacs().iterator(); iterator2.hasNext();) 
						{
							DetAutorizacionConvIngPac detAutorizacionConvIngPac = (DetAutorizacionConvIngPac) iterator2.next();
							
							DtoAutorizacionConvIngPac dtoAutorizacionConvIngPac;
							dtoAutorizacionConvIngPac = new DtoAutorizacionConvIngPac();
							dtoAutorizacionConvIngPac.setTipoMedioAuto(detAutorizacionConvIngPac.getTipoMedioAuto());
							dtoAutorizacionConvIngPac.setObservacionesCambio(detAutorizacionConvIngPac.getObservacionesCambio());
							
							dtoSeccionConvenioPaciente.getListaDtoAutorizacionConvIngPac().add(dtoAutorizacionConvIngPac);
						}
					}
					
					// Se cargan todos los bonos
					dtoSeccionConvenioPaciente.setListaBonosConvIngPac(new ArrayList(conveniosIngresoPaciente.getBonosConvIngPacs()));
					
					// Se carga la lista de convenios-contratos a la forma
					forma.getPaciente().getListaDtoSeccionConvenioPaciente().add(dtoSeccionConvenioPaciente);
				}
			}
		}
		
		
		
		
		
		/** 
		 * Anexo 860, Cambio 1.12 -
		 * Valida los roles del usuario
		 * 
		 * @author Cristhian Murillo
		 * 
		 * @param forma
		 * @param usuario
		 */
		private void validarRolesUsuario(IngresoPacienteOdontologiaForm forma, UsuarioBasico usuario) 
		{
			DtoRolesTipoDeUsuario dtoRolesUsuario;
			
			// Rol Ingresar Modificar Convenios Paciente en Posición 0
			dtoRolesUsuario = new DtoRolesTipoDeUsuario();
			dtoRolesUsuario.setCodigoFuncionalidad(ConstantesBD.codigoFuncionalidadIngresarModificarConveiosPac);
			dtoRolesUsuario.setTieneRol(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), dtoRolesUsuario.getCodigoFuncionalidad()));
			forma.getListaDtoRolesUsuario().add(0,dtoRolesUsuario);
			
			// Rol Ingresar Modificar Bonos Paciente en Posición 1
			dtoRolesUsuario = new DtoRolesTipoDeUsuario();
			dtoRolesUsuario.setCodigoFuncionalidad(ConstantesBD.codigoFuncionalidadIngresarModificarBonosPac);
			dtoRolesUsuario.setTieneRol(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), dtoRolesUsuario.getCodigoFuncionalidad()));
			forma.getListaDtoRolesUsuario().add(1,dtoRolesUsuario);
		}
		
}

