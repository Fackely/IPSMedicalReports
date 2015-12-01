package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaInsercionPersona;
import util.RespuestaValidacion;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.PacientesEntidadesSubConForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.SubirPaciente;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.PacientesEntidadesSubCon;
import com.princetonsa.pdf.PacientesEntidadesSubConPdf;
import com.servinte.axioma.dto.capitacion.DtoUsuariosXConvenio;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Jose Eduardo Arias Doncel
 * Enero 2008 
 */
public class PacientesEntidadesSubConAction extends Action
{ 

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
    private Logger logger = Logger.getLogger(PacientesEntidadesSubConAction.class);

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
		{
		Connection con = null;
		try{
			if (form instanceof PacientesEntidadesSubConForm)
			{
				if (response==null); //Para evitar que salga el warning



				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				PacientesEntidadesSubConForm forma = (PacientesEntidadesSubConForm)form;			 			 
				String estado = forma.getEstado();


				ActionErrors errores = new ActionErrors();			 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado >> "+forma.getEstado());
				logger.info("-------------------------------------");

				if(estado == null)
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo de Pacientes Entidades SubContratadas (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//***************************EVALUACION DE ESTADO DE LA FORMA****************************
				//Estado inicial de la funcionalidad
				else if(estado.equals("empezar"))
				{
					//valida los requisitos para ingresar a la funcionalidad
					errores = validacionesIniciarFuncionalidad(con,errores,usuario.getLoginUsuario(),679,usuario.getCodigoInstitucionInt());

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}
					accionIniciarEstructuras(con,forma,usuario.getCodigoInstitucionInt(),false);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("identificacion");
				}			 
				//Estado de verificacion del paciente en base a su numero de identificacion y tipo de identificacion
				else if(estado.equals("verificarPaciente"))
				{
					errores = validacionesPacienteIngresar(con,forma,errores,usuario,paciente);				 

					if(!errores.isEmpty())
					{
						UtilidadBD.closeConnection(con);
						//Reagrupa la informacion del tipo de identificacion 
						forma.setDatosPacienteMap("tipoIdentificacion",forma.getDatosPacienteMap("tipoIdentificacion").toString()+ConstantesBD.separadorSplit+forma.getDatosPacienteMap("nombreTipoIdentificacion"));					 
						saveErrors(request,errores);
					}
					else
					{
						//***************SE VERIFICA SI SE DEBE VALIDAR LA VIGENCIA EN LA COMPROBACIÓN DE DERECHOS CAPITADOS *******************************				
						if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
						{
							forma.setAutorizacionIngEvento(UtilidadValidacion.esPacienteCapitadoVigente(con,UtilidadFecha.getFechaActual(),forma.getDatosPacienteMap("numeroIdentificacion").toString(),forma.getDatosPacienteMap("tipoIdentificacion").toString()));

							//Valida que exista el paciente dentro de la vigencia capitada
							if(!forma.getAutorizacionIngEvento().isActivo())
							{							
								UtilidadBD.closeConnection(con);
								forma.getAutorizacionIngEvento().setCodigo("");
								return mapping.findForward("autoIngEvento");
							}
						}
						//************************************************************************************************************************************
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("identificacion");
				}
				//Estado de la validacion del numero de autorizacion 
				else if(estado.equals("validarAutoIngEvento"))
				{
					return mapping.findForward("principal");
				}				 
				//Estado para guardar la informacion del paciente en BD
				else if(estado.equals("guardarPaciente"))
				{
					errores = validacionesGuardarPaciente(con,forma,usuario,errores);

					if(!errores.isEmpty())				 
						saveErrors(request,errores);
					else
					{
						errores = accionGuardarPaciente(con,forma,usuario,errores);

						//Valida que no exista errores y muestra pagina de resumen 
						if(!errores.isEmpty())				 
							saveErrors(request,errores);			
						else
						{						
							UtilidadBD.closeConnection(con);
							return mapping.findForward("resumenIngreso");
						}						 
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//Estado para ingresar un nuevo servicio
				else if(estado.equals("nuevoServicio"))
				{
					accionNuevoServicio(forma,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("servicios");				 
				}
				else if(estado.equals("eliminarServicio"))
				{
					accionEliminarServicio(forma,request,response,mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("servicios");
				}
				else if(estado.equals("imprimirRegistro")||estado.equals("imprimirTodo")||estado.equals("imprimirServicio"))
				{
					return accionImprimir(con,forma,mapping,usuario,request, paciente);
				}

				//*********************************************************************************
				//ESTADOS DE NAVEGABILIDAD ********************************************************

				//Regresa el flujo a la pagina inicial de captura del tipo de identificacion y numero de identificacion
				else if(estado.equals("volverCrearPaciente"))
				{				 
					accionIniciarEstructuras(con, forma, usuario.getCodigoInstitucionInt(),false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("identificacion");
				}
				//Regresa el Flujo a la pagina de captura de datos del paciente
				else if(estado.equals("volverGuardarPaciente"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//Recarga el Paciente desde el Resumen
				else if(estado.equals("recargarPaciente"))
				{				
					accionIniciarEstructuras(con, forma, usuario.getCodigoInstitucionInt(),true);
					validacionesPacienteIngresar(con,forma,errores,usuario,paciente);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				 				 
				}
				else if(estado.equals("irServiciosAutorizados"))
				{					
					UtilidadBD.closeConnection(con);
					return mapping.findForward("servicios");				 
				}			 
				else if(estado.equals("volverPacValidar"))
				{
					errores = validacionesServiciosAutorizados(forma,errores);
					UtilidadBD.closeConnection(con);

					if(!errores.isEmpty())
					{					 
						saveErrors(request, errores);
						return mapping.findForward("servicios");
					}
					else				 					 
						return mapping.findForward("principal");				 
				}			
				//vuelve al listado por paciente
				else if(estado.equals("irPorPaciente"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paciente");
				}
				//vuelve al listado por periodo
				else if(estado.equals("irPorPeriodo"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("periodo");
				}				 
				//*********************************************************************************
				//ESTADOS DE LA CONSULTA **********************************************************

				//Verifica el ingreso a la funcionalidad
				else if(estado.equals("empezarConsulta"))
				{
					//valida los requisitos para ingresar a la funcionalidad de Consulta de Pacientes Entidades
					errores = validacionesIniciarFuncionalidad(con,errores,usuario.getLoginUsuario(),694,usuario.getCodigoInstitucionInt());

					if(!errores.isEmpty())
						saveErrors(request,errores);				 

					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}			 
				//Flujo opcion de busqueda por paciente
				else if(estado.equals("porPaciente"))
				{
					//valida los requisitos para ingresar a la funcionalidad de Consulta de Pacientes Entidades
					errores = validacionesCarguePaciente(con,forma,paciente,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("opciones");
					}
					else
					{					 
						forma.setIndicadorFlujo("porPaciente");
						accionCargarInfoPacEntidadesSub(con,forma,paciente,usuario,false);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paciente");					 
					}
				}
				//Flujo opcion de busqueda por periodo
				else if(estado.equals("porPeriodo"))
				{				
					accionCargarEstructurasPorPeriodo(con,forma,usuario);				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("periodo");					 				 
				}
				//Muestra el detalle del registro
				else if(estado.equals("verDetalle"))
				{				 
					accionCargarInfoServAutorizados(con,forma,usuario,paciente,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleReg");
				}
				//Busqueda Avanzada con filtros
				else if(estado.equals("buscarFiltros"))
				{
					errores = validacionesBusquedaFiltros(con,forma,errores);

					if(!errores.isEmpty())				 
						saveErrors(request,errores);
					else				 
						accionCargarInfoPacEntidadesSub(con,forma,paciente,usuario,true);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("periodo");
				}

				//*********************************************************************************
				//ESTADOS PARA EL AJAX ************************************************************

				//Estado Cambio de Ubicacion Geografica
				else if(estado.equals("cambiarUbicacion"))
				{
					accionAjaxCambiarUbicacion(con,forma,response);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("verificarPaciente");				 
				}
				//Estado de Cambio de Contrato        
				else if(estado.equals("cambiarContrato"))
				{
					accionAjaxFiltroContratos(con, forma, usuario, response);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("verificarPaciente");				 
				}
				//**************************************************************************************
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	
	


	//*********************************METODOS**************************************

	//*************************************************************************************************
	//*********************************METODOS BASE MODULO PACIENTES***********************************

	
	/**
	 * Método implementado para realizar la impresión del registro de pacientes entidades subcontratadas
	 * @param request 
	 * @param usuario 
	 */
	private ActionForward accionImprimir(Connection con, PacientesEntidadesSubConForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) throws IPSException 
	{
		
		
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		
		PacientesEntidadesSubConPdf mundoPdf = new PacientesEntidadesSubConPdf();
		mundoPdf.imprimir(con,ValoresPorDefecto.getFilePath()+ nombreArchivo, forma, usuario, paciente);
		
		
		UtilidadBD.closeConnection(con);	
		request.setAttribute("nombreArchivo",nombreArchivo);
		request.setAttribute("nombreVentana", "Registro Paciente Entidad Subcontratada");
		UtilidadBD.closeConnection(con);	
		return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Guarda la informacion del paciente en BD
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionGuardarPaciente(
												Connection con,
												PacientesEntidadesSubConForm forma,
												UsuarioBasico usuario,
												ActionErrors errores)
	{
		//Se instancia el mundo del Paciente
		Paciente mundoPaciente = new Paciente();
		
		//carga la informacion de la forma en el mundo de Paciente		
		mundoPaciente = accionCargarMundoPaciente(mundoPaciente,forma);
		
		
		//Indica si la Operacion fue realizada con exito
		forma.setValidacionesMap("esOperacionTrue",ConstantesBD.acronimoNo);
		
		//**********************************************************************************************************************************************
		//***************FLUJO A PARTIR DE LA EXISTENCIA DEL PACIENTE***********************************************************************************
		//**********************************************************************************************************************************************
		if(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoSi))
		{	
			
			//Se inicia transaccion
			UtilidadBD.iniciarTransaccion(con);
			
			//Evalua si se eliminar el registro no es necesario modifica la informacion del paciente
			if(forma.getValidacionesMap("anularRegistro").toString().equals(ConstantesBD.acronimoNo))
			{
			
				RespuestaValidacion respuesta = new RespuestaValidacion("Error al validar la modificacion del paciente",false);
				
				try
				{
					respuesta = UtilidadValidacion.validacionModificarPaciente(con, System.getProperty("TIPOBD"),  mundoPaciente.getCodigoBarrio() );
				}
				catch(SQLException e)
				{
					logger.error("Error al validar la modificacion del paciente: "+e);
				}
				
				int resp = 0;
				String ls_mensaje = "";
				
				if(respuesta.puedoSeguir)
				{
					
					try
					{
						//Se carga el paciente con los datos antiguos
						Paciente pacienteAnterior = new Paciente();
						pacienteAnterior.cargarPaciente(con, mundoPaciente.getCodigoPersona());
						//Se modifica el paciente
						resp = mundoPaciente.modificarPaciente(con,usuario,pacienteAnterior);
						
						
			                
					}
					catch(Exception e)
					{
						resp = 0;
						logger.error("Error realizando la modificación del paciente: "+e);
					}
					
					
					switch(resp)
					{
						case -1:
							ls_mensaje =
								"El documento de identificación " + mundoPaciente.getCodigoTipoIdentificacion() +
								"-" + mundoPaciente.getNumeroIdentificacion() + " ya esta registrado en el sistema. No se puede modificar el paciente";
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
			
					//Se realiza la validacion del proceso de modificacion
					if(resp<=0)
						errores.add("Error al realizar la modificación del paciente",new ActionMessage("errors.notEspecific",ls_mensaje));
					
				}
				else
					errores.add("Error al validar el la modificacion del paciente", new ActionMessage("errors.notEspecific",respuesta.textoRespuesta));					
			}
			
			//*******************************************************************************				
			//Guarda La Informacion del Registro Paciente en Entidades SubContratadas********
			
			if(errores.isEmpty())
				errores = accionGuardarPacienteEntSubContratadas(con, forma, usuario, errores);
			if(errores.isEmpty())
				errores = accionGuardarServiciosAutorizados(con, forma, usuario, errores);
			
			//*******************************************************************************
			//*******************************************************************************			
			
			//Se valida si hubo errores
			if(!errores.isEmpty())
			{
				UtilidadBD.abortarTransaccion(con);				
				return errores;
			}
			else
			{
				guardarLogAutorizacion(con, usuario, forma, mundoPaciente);
				UtilidadBD.finalizarTransaccion(con);
			}
		}
		//**********************************************************************************************************************************************
		//***************FLUJO A PARTIR DE LA NO EXISTENCIA DEL PACIENTE********************************************************************************
		//**********************************************************************************************************************************************
		else if(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoNo))
		{
			RespuestaValidacion respuesta = new RespuestaValidacion("Error en base datos al validar el ingreso del paciente",false);
			
			try 
			{
	        	//Se valida si se puede ingresar el paciente
				respuesta = UtilidadValidacion.validacionIngresarPaciente(
															               con,
															               mundoPaciente.getCodigoTipoIdentificacion(),
															               mundoPaciente.getNumeroIdentificacion(),
															               mundoPaciente.getCodigoBarrio()
															        	);
				String consecutivo="";
				//Se verifica si se puede continuar con la inserción del paciente
				if(respuesta.puedoSeguir)
				{							
					//Se inicia transaccion
					UtilidadBD.iniciarTransaccion(con);
					
					//1) SE obtiene de nuevo el consecutivo disponible (con bloqueo de registro)***************************************
					 consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoHistoriaClinica,usuario.getCodigoInstitucionInt());
					 String anioConsecutivo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoHistoriaClinica,usuario.getCodigoInstitucionInt(), consecutivo);
					 mundoPaciente.setNumeroHistoriaClinica(consecutivo); //se le suma 1 al consecutivo
					 mundoPaciente.setAnioHistoriaClinica(anioConsecutivo);
					 
					 //2) Se realiza la inserción de paciente - persona ************************************************************************
					 RespuestaInsercionPersona respInsercion = new RespuestaInsercionPersona(false,false,"",0);
					try 
					{
						respInsercion = mundoPaciente.insertarPaciente(con, "axioma@axioma.com.co",usuario.getCodigoInstitucion());
					} 
					catch (SQLException e) 
					{
						logger.error("Error al insertar el paciente: "+e);
					}
					
		            int codigoPaciente=respInsercion.getCodigoPersona();	
		            
		            //almacena la informacion del codigo del paciente en el HashMap
		            forma.setRegistroEntidadesSubMap("codigoPaciente",codigoPaciente);            
		            
		             
		            if(respInsercion.isSalioBien())
		            {
		                String nuevaIdentificacion=respInsercion.getNuevaIdentificacion();
		                mundoPaciente.setCodigoPersona(codigoPaciente);
	
		                TipoNumeroId identificacion = new TipoNumeroId(mundoPaciente.getCodigoTipoIdentificacion(), mundoPaciente.getNumeroIdentificacion());
		                
		                //  Si el paciente no tiene tipo de identificacion automática entonces se carga en sesión
		                if(Utilidades.esAutomaticoTipoId(con,mundoPaciente.getCodigoTipoIdentificacion(),usuario.getCodigoInstitucionInt()))
		                	mundoPaciente.setNumeroIdentificacion(nuevaIdentificacion);
		                
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
							logger.error("Error en el método validacionPermisosInstitucionPaciente2: "+e);
						}
	
		                 if(!resp3.puedoSeguir)
		                	 errores.add("Problemas ingresando el paciente", new ActionMessage("errors.noSeGraboInformacion","DE LA RELACION DEL PACIENTE CON LA INSTITUCION"));
		                 
		                 
		                 //4) Aplicacion del proceso de capitacion**************************************************************
		                 if(forma.getValidacionesMap("esPacienteCapitado").toString().equals(ConstantesBD.acronimoSi))
		                 {
		                 	int rs0=0,rs1=0;
		                 	SubirPaciente subirPac = new SubirPaciente();
		                 	
		                 	//4.1) Guardar registro usuario_x_convenio
		                 	for(int i=0;i<Utilidades.convertirAEntero(forma.getDatosCapitacionMap("numRegistros")+"", true);i++)
		                 	{
		                 		subirPac.setFechaInicial(UtilidadFecha.conversionFormatoFechaABD(forma.getDatosCapitacionMap("fecha_inicial_"+i).toString()));
			                 	subirPac.setFechaFinal(UtilidadFecha.conversionFormatoFechaABD(forma.getDatosCapitacionMap("fecha_final_"+i).toString()));
			                 	subirPac.setTipoCargue(Integer.parseInt(forma.getDatosCapitacionMap("tipo_cargue_"+i).toString()));
			                 	subirPac.setUsuario(usuario.getLoginUsuario());
			                 	
			                 	//rs0=subirPac.ingresarUsuarioXConvenio(con,Integer.parseInt(forma.getDatosCapitacionMap("codigo_contrato_"+i).toString()),codigoPaciente,Integer.parseInt(forma.getDatosCapitacionMap("clasificacion_"+i)+""),forma.getDatosCapitacionMap("tipoafiliado_"+i)+"");
		                 		rs0=subirPac.ingresarUsuarioXConvenio(con,cargarDtoDatosCapitacion(forma.getDatosCapitacionMap(),i,codigoPaciente,usuario));

			                 	
			                 	if(rs0<=0)
			                 		i = Utilidades.convertirAEntero(forma.getDatosCapitacionMap("numRegistros")+"", true);
		                 	}
		                 		
		                 	//4.2) eliminar registro usuarios_capitados y conv_usuarios_capitados
		                 	HashMap campos = new HashMap();
		                 	campos.put("estado",ConstantesBD.continuarTransaccion);
		                 	campos.put("codigo",forma.getDatosPacienteMap("codigoUsuarioCapitado").toString());
		                 	rs1 = subirPac.eliminarUsuarioCapitado(con,campos);
		                 		
	                 		if(rs0<=0||rs1<=0)
	                 			errores.add("Problemas ingresando el paciente para Triage", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE PARA CAPITACION"));
		                 }
		                 //*******************************************************************************	                
		            }			 
		            else
		            	errores.add("Problemas ingresando el paciente", new ActionMessage("errors.noSeGraboInformacion","DEL PACIENTE"));	                 
				}
				else
					errores.add("Error al validar el ingreso del paciente", new ActionMessage("errors.notEspecific",respuesta.textoRespuesta));
						
				
				
				//*******************************************************************************				
				//Guarda La Informacion del Registro Paciente en Entidades SubContratadas********
				
				if(errores.isEmpty())
					errores = accionGuardarPacienteEntSubContratadas(con, forma, usuario, errores);
				if(errores.isEmpty())
					errores = accionGuardarServiciosAutorizados(con, forma, usuario, errores);
				
				//*******************************************************************************
				//*******************************************************************************
				
				
				if(!errores.isEmpty())		
				{
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,  ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					UtilidadBD.abortarTransaccion(con);
					//Indica si la Operacion fue realizada con exito
					forma.setValidacionesMap("esOperacionTrue",ConstantesBD.acronimoNo);
					return errores;
				}
				else
				{
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,  ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);					
					guardarLogAutorizacion(con, usuario, forma, mundoPaciente);
					UtilidadBD.finalizarTransaccion(con);
				}
				
			}
			catch (SQLException e){
				logger.error("Error al validar el ingreso del paciente: "+e);
			}						
		}
		
		return errores;
	}
	
	


	/**
	 * 
	 * @param datosCapitacion
	 * @param indice
	 * @param usuarioActual 
	 * @return
	 */
	private DtoUsuariosXConvenio cargarDtoDatosCapitacion(HashMap datosCapitacion,int indice,int codigoPaciente, UsuarioBasico usuarioActual)
	{
		DtoUsuariosXConvenio dto=new DtoUsuariosXConvenio();
		dto.setContrato(Utilidades.convertirAEntero(datosCapitacion.get("codigo_contrato_"+indice).toString()));
		dto.setCodigoPersona(codigoPaciente);
		dto.setClasificacionSE(Utilidades.convertirAEntero(datosCapitacion.get("clasificacion_"+indice)+""));
		dto.setTipoAfiliado(datosCapitacion.get("tipoafiliado_"+indice)+"");
		dto.setFechaInicial(UtilidadFecha.conversionFormatoFechaABD(datosCapitacion.get("fecha_inicial_"+indice).toString()));
		dto.setFechaFinal(UtilidadFecha.conversionFormatoFechaABD(datosCapitacion.get("fecha_final_"+indice).toString()));
		dto.setTipoCargue(datosCapitacion.get("tipo_cargue_"+indice)+"");
		dto.setUsuario(usuarioActual.getLoginUsuario());
		dto.setActivo(ConstantesBD.acronimoSi);
		dto.setNaturalezaPaciente(Utilidades.convertirAEntero(datosCapitacion.get("naturaleza_"+indice)+""));
		dto.setCentroAtencion(Utilidades.convertirAEntero(datosCapitacion.get("centroatencion_"+indice)+""));
		dto.setTipoIdEmpleador(datosCapitacion.get("tipoidempleador_"+indice)+"");
		dto.setNumeroIdEmpleador(datosCapitacion.get("numeroidempleador_"+indice)+"");
		dto.setRazonSocialEmpleador(datosCapitacion.get("razonsociempleador_"+indice)+"");
		dto.setTipoIdCotizante(datosCapitacion.get("tipoidcotizante_"+indice)+"");
		dto.setNumeroIdCotizante(datosCapitacion.get("numeroidcotizante_"+indice)+"");
		dto.setNombresCotizante(datosCapitacion.get("nombrescotizante_"+indice)+"");
		dto.setApellidosCotizante(datosCapitacion.get("apellidoscotizante_"+indice)+"");
		dto.setParentesco(Utilidades.convertirAEntero(datosCapitacion.get("parentesco_"+indice)+""));
		dto.setNumeroFicha(datosCapitacion.get("numeroficha_"+indice)+"");
		return dto;
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Guarda la informacion del paciente en la estructura de Entidades Pacientes Entidades Sub Contratadas
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionGuardarPacienteEntSubContratadas(
															Connection con,
															PacientesEntidadesSubConForm forma,
															UsuarioBasico usuario,
															ActionErrors errores)
	{		
		
		//**********************************************************************************************************************************************
		//***************FLUJO A PARTIR DE LA EXISTENCIA DEL REGISTRO***********************************************************************************
		//**********************************************************************************************************************************************
		if(forma.getValidacionesMap("existePacEntidades").toString().equals(ConstantesBD.acronimoSi))
		{
			HashMap temporal = new HashMap();
			
			//Se verifica si se anula el registro
			if(forma.getValidacionesMap("anularRegistro").toString().equals(ConstantesBD.acronimoSi))
			{
				//Se actualiza el estado Anulado
				forma.setRegistroEntidadesSubMap("estado",ConstantesIntegridadDominio.acronimoEstadoAnulado);
				
				if(!PacientesEntidadesSubCon.actualizarEstadoPacientesEntidadesSubcontratadas(con,
									ConstantesIntegridadDominio.acronimoEstadoAnulado,
									forma.getRegistroEntidadesSubMap("consecutivo").toString(),
									forma.getRegistroEntidadesSubMap("anioConsecutivo").toString(),
									forma.getRegistroEntidadesSubMap("obserAnulacion").toString(),
									usuario.getLoginUsuario()))
							
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Registro a Estado Anulado"));
				else
					forma.setValidacionesMap("esOperacionTrue",ConstantesBD.acronimoSi);				
			}
			else
			{				
				//Se carga la informacion antigua del Registro del Paciente								
				temporal = PacientesEntidadesSubCon.consultarPacientesEntidadesSubcontratadas(
																							con,
																							usuario.getCodigoInstitucionInt(),
																							forma.getRegistroEntidadesSubMap("consecutivo").toString(),
																							forma.getRegistroEntidadesSubMap("anioConsecutivo").toString(),
																							forma.getRegistroEntidadesSubMap("codigoPaciente").toString(),
																							ConstantesIntegridadDominio.acronimoEstadoAbierto,
																							"","","","","","",
																							false);
				
				//Valida si se genero cambios en la informacion 
				if(validacionesCambiosInfoPacEntidades(temporal,forma.getRegistroEntidadesSubMap()))
				{
					//Realiza la conversion de la fecha
					forma.setRegistroEntidadesSubMap("fechaAutorizacionBD",UtilidadFecha.conversionFormatoFechaABD(forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString()));
					
					if(!PacientesEntidadesSubCon.actualizarPacientesEntidadesSubcontratadas(con,forma.getRegistroEntidadesSubMap()))
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Registro Paciente Entidades Sub"));
					else
						forma.setValidacionesMap("esOperacionTrue",ConstantesBD.acronimoSi);					
				}				
			}
		}		
		//**********************************************************************************************************************************************
		//***************FLUJO A PARTIR DE LA NO EXISTENCIA DEL REGISTRO********************************************************************************
		//**********************************************************************************************************************************************
		else if(forma.getValidacionesMap("existePacEntidades").toString().equals(ConstantesBD.acronimoNo))
		{			
//			Solo se captura el numero del Consecutivo
			String consecutivoPacEntidades = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRegistrosPacientesEntidadesSub,usuario.getCodigoInstitucionInt());
			
			//Almacena la informacion del consecutivo			
			forma.setRegistroEntidadesSubMap("consecutivo",consecutivoPacEntidades);
			forma.setRegistroEntidadesSubMap("anioConsecutivo","");
			forma.setRegistroEntidadesSubMap("fechaAutorizacionBD",UtilidadFecha.conversionFormatoFechaABD(forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString()));
						
			//Realiza la insercion de los datos			
			if(!PacientesEntidadesSubCon.insertarPacientesEntidadesSubcontratadas(con,forma.getRegistroEntidadesSubMap()))
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoRegistrosPacientesEntidadesSub, usuario.getCodigoInstitucionInt(), consecutivoPacEntidades, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Guardar la Informacion en Entidades SubContratadas"));
			}
			else
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoRegistrosPacientesEntidadesSub, usuario.getCodigoInstitucionInt(), consecutivoPacEntidades, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
		}

		return errores;		
	}
	
	//*************************************************************************************************
	
	/**
	 * Guarda la informacion de los Servicios Autorizados
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionGuardarServiciosAutorizados(
															Connection con,
															PacientesEntidadesSubConForm forma,
															UsuarioBasico usuario,
															ActionErrors errores)
	{		
		int numRegistros = Integer.parseInt(forma.getServiciosAutorizadosMap("numRegistros").toString());		
		int numRegistrosEliminados = Integer.parseInt(forma.getServiciosAutorizadosEliminadosMap("numRegistros").toString());
	
		//Evalua si Existen Servicios Autorizados para eliminar
		if(numRegistrosEliminados > 0)
		{
			for(int i = 0 ; i < numRegistrosEliminados; i++)
			{
				if(!PacientesEntidadesSubCon.eliminarServiciosAutorizados(con,forma.getServiciosAutorizadosEliminadosMap("consecutivo_"+i).toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Eliminar los Servicios Autorizados"));
					return errores;
				}				
			}
		}
		
		//Evalua si Existen Servicios Autorizados para insertar o modificar
		if(numRegistros > 0)
		{
			for(int i = 0; i < numRegistros; i++)
			{
				if(forma.getServiciosAutorizadosMap("estabd_"+i).toString().equals(ConstantesBD.acronimoSi))
				{
					//Realiza las validaciones de modificacion del registro
					if(validacionesCambiosServiciosAutorizados(con, 
							forma,
							forma.getServiciosAutorizadosMap("consecutivo_"+i).toString(),
							"",
							"", 
							usuario.getCodigoInstitucionInt(),
							i))
					{						
						if(!PacientesEntidadesSubCon.actualizarServiciosAutorizados(con,accionLlenarMapaServiciosAutorizados(forma, i, usuario)))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar los Servicios Autorizados"));
							return errores;
						}	
					}
				}
				else
				{
					if(!PacientesEntidadesSubCon.insertarServiciosAutorizados(con,accionLlenarMapaServiciosAutorizados(forma,i, usuario)))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Insertar los Servicios Autorizados"));
						return errores;
					}	
				}				
			}
		}
			 
				
		return errores;
		
	}
	
	//*************************************************************************************************
	
	/**
	 * Insertar nuevo registro en el HashMap de Servicios
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionNuevoServicio(PacientesEntidadesSubConForm forma, UsuarioBasico usuario)
	{
		int numRegistros = Integer.parseInt(forma.getServiciosAutorizadosMap("numRegistros").toString());
		
		forma.setServiciosAutorizadosMap("consecutivo_"+numRegistros,"");
		forma.setServiciosAutorizadosMap("servicio_"+numRegistros,"");
		forma.setServiciosAutorizadosMap("nombreServicio_"+numRegistros,"");
		forma.setServiciosAutorizadosMap("cantidad_"+numRegistros,"1");
		forma.setServiciosAutorizadosMap("autorizacionServicio_"+numRegistros,"");
		forma.setServiciosAutorizadosMap("fechaAutorizacion_"+numRegistros,UtilidadFecha.getFechaActual());
		forma.setServiciosAutorizadosMap("responsable_"+numRegistros,usuario.getLoginUsuario());
		forma.setServiciosAutorizadosMap("nombreResponsable_"+numRegistros,usuario.getNombreUsuario());
		forma.setServiciosAutorizadosMap("observaciones_"+numRegistros,"");
		forma.setServiciosAutorizadosMap("estabd_"+numRegistros,ConstantesBD.acronimoNo);		
		forma.setServiciosAutorizadosMap("numRegistros",numRegistros+1);
		
		if(!forma.getServiciosAutorizadosMap().containsKey("INDICES_MAPA"))
			forma.setServiciosAutorizadosMap("INDICES_MAPA",PacientesEntidadesSubCon.getIndicesServiciosMap());
	}
	
	//*************************************************************************************************
	
	/**
	 * Elimina un Registro del HashMap de Servicios Autorizados y Adicciona el Registro Eliminado en el HashMap de Servicios Autorizados Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private void accionEliminarServicio(
										PacientesEntidadesSubConForm forma, 
										HttpServletRequest request, 
										HttpServletResponse response, 
										ActionMapping mapping) 
	{		
		int numRegMapEliminados=Integer.parseInt(forma.getServiciosAutorizadosEliminadosMap("numRegistros")+"");
		
		int ultimaPosMapa=(Integer.parseInt(forma.getServiciosAutorizadosMap("numRegistros")+"")-1);		
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getServiciosAutorizadosMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getServiciosAutorizadosMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setServiciosAutorizadosEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getServiciosAutorizadosMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getServiciosAutorizadosMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setServiciosAutorizadosEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{				
				forma.setServiciosAutorizadosMap(indices[j]+""+i,forma.getServiciosAutorizadosMap(indices[j]+""+(i+1)));
			}
		}		
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getServiciosAutorizadosMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setServiciosAutorizadosMap("numRegistros",ultimaPosMapa);		
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Carga la informacion de Servicios Autorizados
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param HttpServletRequest request
	 * */
	private void accionCargarInfoServAutorizados(
			Connection con, 
			PacientesEntidadesSubConForm forma, 
			UsuarioBasico usuario,
			PersonaBasica paciente,
			HttpServletRequest request)
	{		
		forma.setServiciosAutorizadosMap(PacientesEntidadesSubCon.consultarServiciosAutorizados(con, 
				usuario.getCodigoInstitucionInt(), 
				"", 
				forma.getRegistroEntidadesSubMap("consecutivo_"+forma.getIndicadorComponenteBusqueda()).toString(), 
				forma.getRegistroEntidadesSubMap("anioConsecutivo_"+forma.getIndicadorComponenteBusqueda()).toString()));
				
		if(forma.getIndicadorFlujo().equals("porPeriodo"))
		{
			paciente.setCodigoPersona(Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoPaciente_"+forma.getIndicadorComponenteBusqueda()).toString()));
			UtilidadesManejoPaciente.cargarPaciente(con,usuario, paciente, request);
		}
	}
	
	//*************************************************************************************************
	//*************************************************************************************************
	
	
	//*************************************************************************************************
	//*********************************METODOS DE VALIDACIONES*****************************************
	
	/**
	 * Validaciones del paciente a partir del numero y tipo de identificacion
	 * @param Connection con
	 * @param PacientesEntidadesSubonForm forma
	 * @paran ActionErrors errores
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica persona 
	 * @return ActionErrors errores
	 * */
	private ActionErrors validacionesPacienteIngresar(
														Connection con, 
														PacientesEntidadesSubConForm forma,
														ActionErrors errores, 
														UsuarioBasico usuario,
														PersonaBasica paciente)
	{	

		logger.info("valor del convenio >> "+forma.getRegistroEntidadesSubMap("codigoConvenio"));
		//Captura el codigo del convenio dado en la busqueda generica
		String codigoConvenio = forma.getCodigoConvenio();
		
		//Separa la informacion del Nombre de Tipo Identificacion del Paciente del Acronimo del Tipo de Identificacion del paciente
		String aux [] = forma.getDatosPacienteMap("tipoIdentificacion").toString().split(ConstantesBD.separadorSplit);
		forma.setDatosPacienteMap("tipoIdentificacion",aux[0]);
		forma.setDatosPacienteMap("nombreTipoIdentificacion",aux[1]);
		
		//Marcador indica si se debe consultar el consecutivo 
		forma.setValidacionesMap("esConsecutivoTipoPaciente",Utilidades.esAutomaticoTipoId(con,forma.getDatosPacienteMap("tipoIdentificacion").toString(),usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);		
		
		//***************SE VERIFICA SI EL PACIENTE EXISTE ********************************************************************************************
		try 
		{	
			forma.setValidacionesMap("existePaciente",UtilidadValidacion.existePaciente(con,
					forma.getDatosPacienteMap("tipoIdentificacion").toString(),
					forma.getDatosPacienteMap("numeroIdentificacion").toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);			
			
		} catch (SQLException e) 
		{
			logger.error("Error al verificar si el paciente existe: "+e);
			forma.setDatosPacienteMap("existePaciente",ConstantesBD.acronimoNo);
		}
		
		
		//**********************************************************************************************************************************************
		//***************FLUJO A PARTIR DE LA EXISTENCIA DEL PACIENTE***********************************************************************************
		//**********************************************************************************************************************************************
		
		if(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoSi))
		{					
			//Se verifica si al paciente se le puede crear un nuevo ingreso
			RespuestaValidacion respPrevia = UtilidadValidacion.validacionPreviaIngresoPaciente(con,
																								forma.getDatosPacienteMap("tipoIdentificacion").toString(),
																								forma.getDatosPacienteMap("numeroIdentificacion").toString(),
																								usuario.getCodigoInstitucionInt()+"");
			
			//El Paciente no Posee un Ingreso Abierto
			if(respPrevia.puedoSeguir)
			{
				//inicializa las estructuras de datos necesarias para el paciente
				accionIniciarEstructurasPaciente(con,forma,usuario);
				
				//Se carga el paciente en PersonaBasica
				TipoNumeroId identificacion = new TipoNumeroId(forma.getDatosPacienteMap("tipoIdentificacion").toString(),
															   forma.getDatosPacienteMap("numeroIdentificacion").toString());
				
				try 
				{
					paciente.cargar(con, identificacion);
					paciente.cargarPaciente2(con,identificacion,usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
				} 
				catch (SQLException e) 
				{
					logger.error("Error al tratar de cargar un paciente: "+e);
				}
				
				//Se verifica si el paciente está muerto
				if(UtilidadValidacion.esPacienteMuerto(con,paciente.getCodigoPersona()))
				{					
					String datoPaciente = paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona()+" "+paciente.getNombrePersona();
					errores.add("PACIENTE MUERTO",new ActionMessage("errors.paciente.estaMuerto",datoPaciente));
					return errores;
				}				
				
				//Se carga la información del paciente
				this.accionLlenarFormaPaciente(con,forma,paciente,"paciente",null);				
				
				//Verificacion del Convenio Capitado del paciente
				this.validacionesConvenioCapitadoPaciente(con,forma,paciente.getCodigoPersona());			
				
				//*****************************************************************************************************************************
				//Se carga la informacion del REGISTRO del paciente en entidades subcontratadas si posee uno y se encuentra en estado ABIERTO								
				forma.setRegistroEntidadesSubMap(PacientesEntidadesSubCon.consultarPacientesEntidadesSubcontratadas(
																													con,
																													usuario.getCodigoInstitucionInt(),
																													"",
																													"",
																													paciente.getCodigoPersona()+"",
																													ConstantesIntegridadDominio.acronimoEstadoAbierto,
																													"","","","","","",
																													false));
								
				
				//Marcador que indica si existe registros de informacion en Pacientes Entidades Subcontratadas
				if(forma.getRegistroEntidadesSubMap("numRegistros").toString().equals("0"))				
					forma.setValidacionesMap("existePacEntidades",ConstantesBD.acronimoNo);
				else				
					forma.setValidacionesMap("existePacEntidades",ConstantesBD.acronimoSi);
						
				//Llena los datos Requeridos de la forma de Paciente Entidades SubContratadas
				accionIniciarInfoPacEntiSub(con,forma, usuario);				
				
				if(!UtilidadTexto.isEmpty(codigoConvenio) && forma.getValidacionesMap("existePacEntidades").toString().equals(ConstantesBD.acronimoNo))
					forma.setCodigoConvenio(codigoConvenio);
					
				//Verifica si existe registro en una entidad subcontratada para cargar los servicios
				if(!forma.getRegistroEntidadesSubMap("numRegistros").equals("0"))
				{
					//Se carga la informacion de los Servicios Autorizados para la atencion en una empresa subcontratada					
					forma.setServiciosAutorizadosMap(PacientesEntidadesSubCon.consultarServiciosAutorizados(
																											con,
																											usuario.getCodigoInstitucionInt(),
																											"",
																											forma.getRegistroEntidadesSubMap("consecutivo").toString(),
																											forma.getRegistroEntidadesSubMap("anioConsecutivo").toString()));
										
					
					//Marcador que indica si existe registros de Servicios Autorizados del Paciente
					if(forma.getServiciosAutorizadosMap("numRegistros").toString().equals("0"))
						forma.setValidacionesMap("existeServAutorizados",ConstantesBD.acronimoNo);
					else
						forma.setValidacionesMap("existeServAutorizados",ConstantesBD.acronimoSi);
				}
				//*****************************************************************************************************************************
				
				return errores;
			}
			//	El Paciente Posee un Ingreso Abierto
			else if(!respPrevia.puedoSeguir)
			{				
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Paciente Posee un Ingreso Abierto " + respPrevia.textoRespuesta));				
				return errores;
			}
		}
		
		
		//**********************************************************************************************************************************************
		//***************FLUJO A PARTIR DE LA NO EXISTENCIA DEL PACIENTE********************************************************************************
		//**********************************************************************************************************************************************
		if(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoNo))
		{
			
			//inicializa las estructuras de datos necesarias para el paciente
			accionIniciarEstructurasPaciente(con,forma,usuario);
			
			//Marcador que indica si existe registros de informacion en Pacientes Entidades Subcontratadas
			forma.setValidacionesMap("existePacEntidades",ConstantesBD.acronimoNo);
			
			//Marcador que indica si existe registros de Servicios Autorizados del Paciente
			forma.setValidacionesMap("existeServAutorizados",ConstantesBD.acronimoNo);
			
			//Marcador que indica que el paciente es capitado 
			forma.setValidacionesMap("esPacienteCapitado",ConstantesBD.acronimoNo);	
			
			//Si el paciente no existe se verifica si tiene tipo id automático para asignarle el numero de la secuencia
			if(forma.getValidacionesMap("esConsecutivoTipoPaciente").toString().equals(ConstantesBD.acronimoSi))
			{
				try 
				{
					forma.setValidacionesMap("numeroIdentificacion",((DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_personas_sin_id")+1)+""));					
					
					//Se tiene que volver a verificar que no exista un paciente con el mismo tipo y numero de identificacion
					forma.setValidacionesMap("existePaciente",UtilidadValidacion.existePaciente(con,
																								forma.getDatosPacienteMap("tipoIdentificacion").toString(),
																								forma.getDatosPacienteMap("numeroIdentificacion").toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					
					//Si ya existe un paciente con ese consecutivo, se debe continuar con la siguiente secuencia, hasta que haya una disponible
					while(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoSi))
					{
						DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con, "seq_personas_sin_id");
						forma.setValidacionesMap("numeroIdentificacion",((DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_personas_sin_id")+1)+""));						
						
						forma.setValidacionesMap("existePaciente",UtilidadValidacion.existePaciente(con,
								forma.getDatosPacienteMap("tipoIdentificacion").toString(),
								forma.getDatosPacienteMap("numeroIdentificacion").toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					}
					
				} 
				catch (SQLException e) 
				{
					logger.error("ERROR AL OBTENER AL REALIZAR LA VALIDACION DEL NUMERO DE IDENTIFICACION AUTOMATICA: "+e);
				}
			}						

			//Se Obtiene el ultimo Numero de Historia Clinica
			errores = validacionesConsecutivoHistoriaClinica(con,forma,usuario,errores);
			
			//validación de usuario/medico existente
			accionLlenarFormaPaciente(con, forma, null,"persona",null);
			
			//Validacion de usuarios capitados
			errores = this.validacionesUsuariosCapitados(con,forma,errores);	
			
			//Llena los datos Requeridos de la forma de Paciente Entidades SubContratadas
			accionIniciarInfoPacEntiSub(con,forma, usuario);			
		}		
		return errores;
	}	
	
	//*************************************************************************************************
	
	/**
	 * Valida si el usuario se encuentre en la capita 
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param ActionErrors errores
	 * */
	private ActionErrors validacionesUsuariosCapitados(Connection con,PacientesEntidadesSubConForm forma, ActionErrors errores)
	{
		//Se verifica si el paciente existe en la capita
		if(SubirPaciente.consultarCodigoUsuarioCapitado(
				con, 
				forma.getDatosPacienteMap("tipoIdentificacion").toString(),
				forma.getDatosPacienteMap("numeroIdentificacion").toString())>0)
		{		
			HashMap campos = new HashMap();
			campos.put("tipoIdentificacion",forma.getDatosPacienteMap("tipoIdentificacion"));
			campos.put("numeroIdentificacion",forma.getDatosPacienteMap("numeroIdentificacion"));
			campos.put("validarVigencia",ConstantesBD.acronimoNo);			
	
			HashMap mapaDatosPaciente = SubirPaciente.consultarUsuarioCapitado(con,campos);			
			
			//Utilidades.imprimirMapa(mapaDatosPaciente);
			
			if(Integer.parseInt(mapaDatosPaciente.get("numRegistros").toString())>0)
			{					
				forma.setValidacionesMap("esPacienteCapitado",ConstantesBD.acronimoSi);
				forma.setValidacionesMap("esCapitacionVigente",ConstantesBD.acronimoSi);			
				
				mapaDatosPaciente = SubirPaciente.removerComillasDatosUsuarioCapitado(mapaDatosPaciente);
				
				//Accion llena la informacion del paciente 
				accionLlenarFormaPaciente(con, forma,null,"capitado",mapaDatosPaciente);
				
				//Se Almacena informacion del paciente capitado
				forma.setDatosPacienteMap("codigoUsuarioCapitado",mapaDatosPaciente.get("codigo_0").toString());
				forma.setDatosPacienteMap("nroCarnet",mapaDatosPaciente.get("numeroficha_0").toString());
				forma.setDatosCapitacionMap(mapaDatosPaciente);
				
				//*****se verifica si el usuario tiene mas de 1 contrato vigente****************
				if(Integer.parseInt(mapaDatosPaciente.get("numRegistros").toString())>1)
				{
					int codigoEmpresa = 0;
					boolean diferentes = false;
					//Se verifica que los contratos pertenezcan a la misma empresa
					for(int i=0;i<Integer.parseInt(mapaDatosPaciente.get("numRegistros").toString());i++)
					{
						if(codigoEmpresa==0)
							codigoEmpresa = Utilidades.convertirAEntero(mapaDatosPaciente.get("codigo_empresa_"+i).toString(), true);
						
						if(codigoEmpresa!=Utilidades.convertirAEntero(mapaDatosPaciente.get("codigo_empresa_"+i).toString(), true))
							diferentes = true;
					}
					if(diferentes)
						errores.add("",new ActionMessage("errores.paciente.contratoCapitacion",forma.getDatosPacienteMap("primerApellido")+" "+forma.getDatosPacienteMap("segundoApellido")+" "+forma.getDatosPacienteMap("primerNombre")+" "+forma.getDatosPacienteMap("segundoNombre")));
				}
			}
			else
			{
				forma.setValidacionesMap("esPacienteCapitado",ConstantesBD.acronimoNo);
				forma.setValidacionesMap("esCapitacionVigente",ConstantesBD.acronimoNo);
			}
		}
		else
		{
			forma.setValidacionesMap("esPacienteCapitado",ConstantesBD.acronimoNo);
			forma.setValidacionesMap("esCapitacionVigente",ConstantesBD.acronimoNo);
		}
			
		return errores;
	}
	
	
	//*************************************************************************************************	
	
	/**
	 * Valida los requisitos necesarios para el ingreso a la funcionalidad
	 * @param institucion 
	 * @param Connection con
	 * @param ActionErrors errores
	 * @param String loginUsuario
	 * */
	private ActionErrors validacionesIniciarFuncionalidad(Connection con,ActionErrors errores,String loginUsuario, int nroFuncionalidad, int institucion)
	{
		String nombreFuncionalidad = "";
		
		//679 Codigo de la funcionalidad Pacientes Entidades Subcontratadas en la tabla Funcionalidades
		if(nroFuncionalidad == 679)
			nombreFuncionalidad = "Pacientes Entidades Subcontratadas";
			
		//694 Codigo de la funcionalidad Consulta de Pacientes Entidades Subcontratadas en la tabla Funcionalidades
		if(nroFuncionalidad == 694)
			nombreFuncionalidad = "Consulta Pacientes Entidades Subcontratadas";
		
		if(!Utilidades.tieneRolFuncionalidad(loginUsuario,nroFuncionalidad))
				errores.add("descripcion",new ActionMessage("errors.usuarioSinRolFuncionalidad",loginUsuario,nombreFuncionalidad));
		
		//Se verifica el consecutivo disponible
		if(nroFuncionalidad==679&&!Utilidades.isDefinidoConsecutivo(con,institucion,ConstantesBD.nombreConsecutivoRegistrosPacientesEntidadesSub))
			errores.add("Falta definir consecutivo", new ActionMessage("error.faltaDefinirConsecutivo","EL REGISTRO DE ENTIDADES SUBCONTRATADAS"));
							
		return errores;		
	}
	
	
	//*************************************************************************************************	
	
	
	/**
	 * Método que realiza la verificación del Convenio Capitado del paciente
	 * @param con
	 * @param PacientesEntidadesSubConForm
	 * @param paciente
	 * @param mapping
	 * @param usuarioActual 
	 * @return
	 */
	private void validacionesConvenioCapitadoPaciente(Connection con, 
													  PacientesEntidadesSubConForm forma, 
													  int codigoPaciente) 
	{			
		//Se verifica si el paciente tiene convenio capitado
		SubirPaciente subirPac = new SubirPaciente();
		HashMap usuarioConvenio = subirPac.consultarUsuarioXConvenio(con,codigoPaciente+"");
		int numCapitacion = Integer.parseInt(usuarioConvenio.get("numRegistros").toString()); 
		
		//Marcador que indica que el paciente es capitado 
		forma.setValidacionesMap("esPacienteCapitado",ConstantesBD.acronimoNo);

		if(numCapitacion>0)
		{
			//Guarda la informacion del convenio del paciente capitado
			forma.setDatosPacienteMap("codigoConvenio",usuarioConvenio.get("codigo_convenio_0").toString());
			forma.setDatosPacienteMap("nombreConvenio",usuarioConvenio.get("nombre_convenio_0").toString());
			
			
			
			//Carga los contratos del convenio
			forma.setContratoList(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoPaciente").toString()),Integer.parseInt(forma.getDatosPacienteMap("codigoConvenio").toString())));
			
			//Marcador que indica que el paciente es capitado 
			forma.setValidacionesMap("esPacienteCapitado",ConstantesBD.acronimoSi);		
		}
	}
	
	//*****************************************************************************************************
	
	/**
	 * Método implementado para efectuar las validaciones del consecutivo disponible de historia clinica
	 * @param con
	 * @param PacientesEntidadesSubConForm
	 * @param usuarioActual
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionesConsecutivoHistoriaClinica(Connection con, 
																PacientesEntidadesSubConForm forma, 
																UsuarioBasico usuarioActual, 
																ActionErrors errores) 
	{
		
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt());		
		
		if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","la historia clínica"));
		else
		{
			//se asignan los nuevos valores
			try
			{
				//se asigna el proximo consecutivo
				forma.setDatosPacienteMap("numeroHistoriaClinica",consecutivo);
			}
			catch(Exception e)
			{
				logger.error("Error en validacionConsecutivoDisponible:  "+e);
				errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la historia clínica"));
			}
		}
		
		return errores;
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Realiza las validaciones necesarias para permitir almacenar la informacion del paciente Entidades Subcontratadas
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionErrors errores
	 * */
	private ActionErrors validacionesGuardarPaciente(
													Connection con, 
													PacientesEntidadesSubConForm forma, 
													UsuarioBasico usuario, 
													ActionErrors errores) throws IPSException
	{
	
		
		//Verificacion del Pais de Expedicion 
		if(forma.getDatosPacienteMap("paisExpedicion").toString().equals(""))		
			errores.add("descripcion",new ActionMessage("errors.required","El Pais de Expedicion "));
		
		//Verificacion de la Ciudad de Expedicion
		if(forma.getDatosPacienteMap("ciudadExpedicion").toString().equals(""))		
			errores.add("descripcion",new ActionMessage("errors.required","La Ciudad de Expedicion "));
		
		//Verificacion del primer apellido 
		if(forma.getDatosPacienteMap("primerApellido").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Primer Apellido "));
		
		//Verificacion del Primer Nombre 
		if(forma.getDatosPacienteMap("primerNombre").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Primer Nombre "));
		
		//Verificacion del Pais de Nacimiento 
		if(forma.getDatosPacienteMap("paisNacimiento").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Pais de Nacimiento "));
		
		//Verificacion de la Ciudad de Nacimiento
		if(forma.getDatosPacienteMap("ciudadNacimiento").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Ciudad de Nacimiento "));
		
		//Verificacion de la Fecha de Nacimiento 
		if(forma.getDatosPacienteMap("fechaNacimiento").toString().equals(""))		
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Nacimiento "));
		else
		{
			if(!UtilidadFecha.validarFecha(forma.getDatosPacienteMap("fechaNacimiento").toString()))		
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getDatosPacienteMap("fechaNacimiento").toString()));
			else
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getDatosPacienteMap("fechaNacimiento").toString(),UtilidadFecha.getFechaActual()))
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getDatosPacienteMap("fechaNacimiento").toString(),UtilidadFecha.getFechaActual()));
		}
		
		//Verificacion del Sexo
		if(forma.getDatosPacienteMap("sexo").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Sexo "));		
		
		//Verificacion del Pais de Residencia 
		if(forma.getDatosPacienteMap("paisResidencia").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Pais de Residencia "));
		
		//Verificacion de la Ciudad de Residencia
		if(forma.getDatosPacienteMap("ciudadResidencia").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Ciudad de Residencia "));
		
		//Verificacion del codigo del Barrio
		if(forma.getDatosPacienteMap("codigoBarrio").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Barrio de Residencia "));		
		
		//Verificacion de la Direccion 
		if(forma.getDatosPacienteMap("direccion").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Direccion "));
		
		//Verificacion de la Zona de Domicilio 
		if(forma.getDatosPacienteMap("zonaDomicilio").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La zona de Domicilio "));
		
		//Verificacion de la Ocupacion 
		if(forma.getDatosPacienteMap("ocupacion").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Ocupación   "));	
		
		//Verificacion del Grupo Poblacional 
		if(forma.getDatosPacienteMap("grupoPoblacional").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Grupo Poblacional  "));
		
		//Verificacion de la entidad SubContratada
		if(forma.getRegistroEntidadesSubMap("entidadSubcontratada").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Entidad Sub Contratada "));		
		
		//Verificacion del Convenio
		if(forma.getRegistroEntidadesSubMap("codigoConvenio").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Convenio "));
		else
		{
			//Verifica si se requiere el numero de nroCarnet
			Convenio mundoConvenio = new Convenio();
			mundoConvenio.cargarResumen(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoConvenio").toString()));			
			forma.setValidacionesMap("esRequeridoCarnet",UtilidadTexto.getBoolean(mundoConvenio.getRequiereNumeroCarnet())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			if(forma.getValidacionesMap("esRequeridoCarnet").toString().equals(ConstantesBD.acronimoSi))
			{
				if(forma.getRegistroEntidadesSubMap("nroCarnet").toString().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Numero del carnet "));
			}			
			
			//Verificacion de la Autorizacion de Ingreso
			forma.setValidacionesMap("esRequeridoAutoIngreso",UtilidadTexto.getBoolean(UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con,
					ConstantesBD.tipoInformacionIngresoCuenta,					
					ConstantesIntegridadDominio.acronimoValidarRequeridoNumeroAutorizacionIngreso, 
					usuario.getCodigoInstitucionInt(),
					Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoConvenio").toString()),
					ConstantesBD.codigoNuncaValido,
					usuario.getCodigoCentroAtencion()))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						
			if(forma.getValidacionesMap("esRequeridoAutoIngreso").toString().equals(ConstantesBD.acronimoSi))
			{
				if(forma.getRegistroEntidadesSubMap("autorizacionIngreso").toString().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Autorizacion de Ingreso "));				
			}

		}
		
		//Verificacion del Contrato
		if(forma.getRegistroEntidadesSubMap("contrato").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Contrato "));
		
		//Verificacion de la Fecha de Autorizacion 
		if(forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString().equals(""))		
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Autorizacion "));
		else
		{
			if(!UtilidadFecha.validarFecha(forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString()))		
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString()));
			else
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString(),UtilidadFecha.getFechaActual()))
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getRegistroEntidadesSubMap("fechaAutorizacion").toString(),UtilidadFecha.getFechaActual()));
		}	
		
		//Verifica la hora de Autorizacion
		if(forma.getRegistroEntidadesSubMap("horaAutorizacion").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Hora de Autorizacion "));
		else
		{
			if(!UtilidadFecha.validacionHora(forma.getRegistroEntidadesSubMap("horaAutorizacion").toString()).puedoSeguir)
				errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",forma.getRegistroEntidadesSubMap("horaAutorizacion").toString()));				
		}			
		
		//Verifica que el campo Observaciones en la anulacion sea requerido
		if(forma.getValidacionesMap("anularRegistro").toString().equals(ConstantesBD.acronimoSi))
		{
			if(forma.getRegistroEntidadesSubMap("obserAnulacion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Observacion de la Anulacion "));
		}
		
		return errores;				
	}	
	
	
	//*************************************************************************************************
	
	/**
	 * Valida si la informacion de Pacientes Entidades SubContratadas fue modificada
	 * @param HashMap infoOriginalMap
	 * @param HashMap infoModificadaMap
	 * */
	private boolean validacionesCambiosInfoPacEntidades(HashMap infoOriginalMap,HashMap infoModificadaMap)
	{
		//Valida Campo por campo si se genero cambios en la informacion		
		if(infoOriginalMap.get("centroAtencion").toString().equals(infoModificadaMap.get("centroAtencion").toString()) && 
				infoOriginalMap.get("entidadSubcontratada").toString().equals(infoModificadaMap.get("entidadSubcontratada").toString()) && 
					infoOriginalMap.get("codigoConvenio").toString().equals(infoModificadaMap.get("codigoConvenio").toString()) &&
						infoOriginalMap.get("contrato").toString().equals(infoModificadaMap.get("contrato").toString()) && 
							infoOriginalMap.get("nroCarnet").toString().equals(infoModificadaMap.get("nroCarnet").toString()) && 
								infoOriginalMap.get("autorizacionIngreso").toString().equals(infoModificadaMap.get("autorizacionIngreso").toString()) && 
									infoOriginalMap.get("fechaAutorizacion").toString().equals(infoModificadaMap.get("fechaAutorizacion").toString()) && 
										infoOriginalMap.get("observaciones").toString().equals(infoModificadaMap.get("observaciones").toString()) && 
											infoOriginalMap.get("horaAutorizacion").toString().equals(infoModificadaMap.get("horaAutorizacion").toString()))
			return false;
							
		
		return true;
	}
	
//*************************************************************************************************	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param PacientesEntidadesSubConForm forma	
	 * @param String consecutivoServicio
	 * @param String consecutivoPacEntidadesSub
	 * @param String anioConsecPacEntidades
	 * @param int institucion
	 * @param int pos
	 * */
	private boolean validacionesCambiosServiciosAutorizados(
			Connection con,
			PacientesEntidadesSubConForm forma,
			String consecutivoServicio,			
			String consecutivoPacEntidadesSub,
			String anioConsecPacEntidades,
			int institucion, 
			int pos)
	{
		HashMap temp = PacientesEntidadesSubCon.consultarServiciosAutorizados(con,institucion,consecutivoServicio,consecutivoPacEntidadesSub,anioConsecPacEntidades);		
		String[] indices = (String[])forma.getServiciosAutorizadosMap("INDICES_MAPA");		
				
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getServiciosAutorizadosMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getServiciosAutorizadosMap(indices[i]+pos)).toString().trim())))
				{					
					return true;
				}				
			}
		}		
		return false;		
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Validaciones de los Servicios Autorizados Ingresados
	 * @param PacientesEntidadesSubConForm forma
	 * @param ActionErrors errores
	 * */	
	private ActionErrors validacionesServiciosAutorizados(PacientesEntidadesSubConForm forma, ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getServiciosAutorizadosMap("numRegistros").toString());
				
		
		for(int i = 0; i < numRegistros ; i++)
		{
			if(!forma.getServiciosAutorizadosMap("servicio_"+i).toString().equals(""))
			{
				if(!forma.getServiciosAutorizadosMap("autorizacionServicio_"+i).toString().equals(""))
				{
					for(int j = (i+1); j < numRegistros; j++)
					{
						if(forma.getServiciosAutorizadosMap("servicio_"+i).toString().equals(forma.getServiciosAutorizadosMap("servicio_"+j).toString()) 
								&& forma.getServiciosAutorizadosMap("autorizacionServicio_"+i).toString().equals(forma.getServiciosAutorizadosMap("autorizacionServicio_"+j).toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro Nro "+(i+1)+" Posee el mismo Servicio y Autorizacion que el Registro Nro."+(j+1)));
						}							
					}
				}								
			}
			else{
				errores.add("descripcion",new ActionMessage("errors.required","El Servicio del Registro Nro. "+(i+1)+" "));				
			}
			
			//Verificacion de la cantidad
			if(forma.getServiciosAutorizadosMap("cantidad_"+i).toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Cantidad del Registro Nro. "+(i+1)+" "));
			
			//Verificacion de la autorizacion del servicio
			if(forma.getServiciosAutorizadosMap("autorizacionServicio_"+i).toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Autorización del Registro Nro. "+(i+1)+" "));
			
			//Verificacion de la Fecha de Autorizacion 
			if(forma.getServiciosAutorizadosMap("fechaAutorizacion_"+i).toString().equals(""))		
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Autorización "));
			else
			{
				if(!UtilidadFecha.validarFecha(forma.getServiciosAutorizadosMap("fechaAutorizacion_"+i).toString()))		
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getServiciosAutorizadosMap("fechaAutorizacion_"+i).toString()));
				else
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getServiciosAutorizadosMap("fechaAutorizacion_"+i).toString(),UtilidadFecha.getFechaActual()))
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getServiciosAutorizadosMap("fechaAutorizacion_"+i).toString(),UtilidadFecha.getFechaActual()));
			}		
		}
		
		return errores;
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Valida que el paciente se encuentre cargado
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param PersonaBasica paciente
	 * @param ActionErrors errores
	 * */
	private ActionErrors validacionesCarguePaciente(Connection con, 
			PacientesEntidadesSubConForm forma, 
			PersonaBasica paciente, 
			ActionErrors errores)
	{
		forma.reset();
		
		if(paciente.getCodigoPersona()<1)
			errores.add("descripcion",new ActionMessage("errors.paciente.noCargado"));			
		
		return errores;
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Validaciones para la busqueda por filtros
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param ActionErrors errores
	 * */	
	private ActionErrors validacionesBusquedaFiltros(Connection con,PacientesEntidadesSubConForm forma, ActionErrors errores)
	{
		//Verificacion de los Rangos de Fechas		
		if(forma.getValidacionesMap("fechaInicialBusqueda").toString().equals("") 
				|| forma.getValidacionesMap("fechaFinalBusqueda").toString().equals(""))		
			errores.add("descripcion",new ActionMessage("errors.required","Las Fecha Inicial y Final del Registro "));
		else
		{
			if(!UtilidadFecha.validarFecha(forma.getValidacionesMap("fechaInicialBusqueda").toString()))		
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial "+ forma.getValidacionesMap("fechaInicialBusqueda").toString()));
			
			if(!UtilidadFecha.validarFecha(forma.getValidacionesMap("fechaFinalBusqueda").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final "+forma.getValidacionesMap("fechaFinalBusqueda").toString()));
			
			if(errores.isEmpty())
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getValidacionesMap("fechaInicialBusqueda").toString(),UtilidadFecha.getFechaActual()))
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial "+forma.getValidacionesMap("fechaInicialBusqueda").toString(),UtilidadFecha.getFechaActual()));
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getValidacionesMap("fechaFinalBusqueda").toString(),UtilidadFecha.getFechaActual()))
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Final "+forma.getValidacionesMap("fechaFinalBusqueda").toString(),UtilidadFecha.getFechaActual()));
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getValidacionesMap("fechaInicialBusqueda").toString(),forma.getValidacionesMap("fechaFinalBusqueda").toString()))
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial "+forma.getValidacionesMap("fechaInicialBusqueda").toString()," Final "+forma.getValidacionesMap("fechaFinalBusqueda").toString()));
			}				
		}
		
		return errores;
	}
	
	//*************************************************************************************************
	//*************************************************************************************************
	
	
	//*************************************************************************************************
	///********************************METODOS DE LLENADO DE ESTRUCTURAS DE DATOS**********************
	/**
	 * Inicializa las estructuras de datos necesarias para la funcionalidad
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param int Institucion
	 * @param boolean recargarPaciente
	 * */
	private void accionIniciarEstructuras(Connection con, PacientesEntidadesSubConForm forma, int institucion,boolean recargarPaciente)
	{
		String temporal = "";	
		
		//rescata la informacion antes de reiniciar las estructuras
		if(recargarPaciente)
			temporal =	forma.getDatosPacienteMap("tipoIdentificacion").toString()+ConstantesBD.separadorSplit+
						forma.getDatosPacienteMap("nombreTipoIdentificacion").toString()+ConstantesBD.separadorSplit+
						forma.getDatosPacienteMap("numeroIdentificacion").toString();
		
		forma.reset();		
		forma.setTiposIdentificacionList(Utilidades.obtenerTiposIdentificacion(con,"",institucion));
		forma.setDatosPacienteMap("tipoIdentificacion",ValoresPorDefecto.getTipoId(institucion)+ConstantesBD.separadorSplit+Utilidades.getDescripcionTipoIdentificacion(con,ValoresPorDefecto.getTipoId(institucion)));
		
		//Inicializa los valores de resgistros de los mapas necesarios para el registro del paciente
		forma.setRegistroEntidadesSubMap("numRegistros","0");
		forma.setServiciosAutorizadosMap("numRegistros","0");		
		forma.setServiciosAutorizadosEliminadosMap("numRegistros","0");
		
		
		if(recargarPaciente)		
		{
			String array [] = temporal.split(ConstantesBD.separadorSplit);
			forma.setDatosPacienteMap("tipoIdentificacion",array[0]+ConstantesBD.separadorSplit+array[1]);			
			forma.setDatosPacienteMap("numeroIdentificacion",array[2]);
		}
	}
	
	//*************************************************************************************************
	
	/**
	 * Inicializa las estructuras de datos para el ingreso del paciente    
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionIniciarEstructurasPaciente(Connection con, PacientesEntidadesSubConForm forma, UsuarioBasico usuario)
	{		
		forma.setPaisesList(Utilidades.obtenerPaises(con));
		forma.setZonasDomiciliosMap(Utilidades.consultarZonasDomicilio(con));
		forma.setOcupacionesMap(Utilidades.consultarOcupaciones(con));
		forma.setSexosList(Utilidades.obtenerSexos(con));
		forma.setEntidadesSubContratadasList(UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		forma.setConveniosList(Utilidades.obtenerConvenios(con, "","", true, "", true));	
		
		//Marcador que indica si el registro se debe anular
		forma.setValidacionesMap("anularRegistro",ConstantesBD.acronimoNo);		
		
		//Se verifica si se debe ingresar edad en el formulario de ingreso del paciente
		forma.setValidacionesMap("ingresoEdad",UtilidadTexto.getBoolean(ValoresPorDefecto.getFechaNacimiento(usuario.getCodigoInstitucionInt()))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						
		//Consulta la parametrizacion de paises de la institucion
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con,usuario.getCodigoInstitucionInt());
		
		if(!mundoInstitucion.getPais().getCodigo().equals(""))
		{
			forma.setDatosPacienteMap("paisExpedicion",mundoInstitucion.getPais().getCodigo());
			forma.setDatosPacienteMap("paisNacimiento",mundoInstitucion.getPais().getCodigo());
			forma.setDatosPacienteMap("paisResidencia",mundoInstitucion.getPais().getCodigo()); 
			
			forma.setCiudadExpedicionList(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			forma.setCiudadNacimientoList(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
			forma.setCiudadResidenciaList(Utilidades.obtenerCiudadesXPais(con, mundoInstitucion.getPais().getCodigo()));
		}	
		
		//Consulta del parametro país nacimiento
		String paisNacimiento = ValoresPorDefecto.getPaisNacimiento(usuario.getCodigoInstitucionInt());
		String ciudadNacimiento = ValoresPorDefecto.getCiudadNacimiento(usuario.getCodigoInstitucionInt());
		
		if(UtilidadCadena.noEsVacio(paisNacimiento)&&!paisNacimiento.equals(" - "))
		{
			forma.setDatosPacienteMap("paisNacimiento",paisNacimiento.split("-")[0]);
			forma.setCiudadNacimientoList(Utilidades.obtenerCiudadesXPais(con, paisNacimiento.split("-")[0]));
						
			if(UtilidadCadena.noEsVacio(ciudadNacimiento)&&!ciudadNacimiento.equals(" - - "))
				forma.setDatosPacienteMap("ciudadNacimiento",ciudadNacimiento.split("-")[0]+ConstantesBD.separadorSplit+ciudadNacimiento.split("-")[1]);
		}
		
		//Consulta del parametro país residencia
		String paisResidencia = ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt());
		String ciudadResidencia = ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(paisResidencia)&&!paisResidencia.equals(" - "))
		{
			forma.setDatosPacienteMap("paisResidencia",paisResidencia.split("-")[0]);
			forma.setCiudadResidenciaList(Utilidades.obtenerCiudadesXPais(con, paisResidencia.split("-")[0]));

			if(UtilidadCadena.noEsVacio(ciudadResidencia)&&!ciudadResidencia.equals(" - - "))				
				forma.setDatosPacienteMap("ciudadResidencia",ciudadResidencia.split("-")[0]+ConstantesBD.separadorSplit+ciudadResidencia.split("-")[1]);
		}
		
		//Consulta del barrio
		String barrio = ValoresPorDefecto.getBarrioResidencia(usuario.getCodigoInstitucionInt());
		if(UtilidadCadena.noEsVacio(barrio))
		{
			forma.setDatosPacienteMap("codigoBarrio",barrio.split("-")[2]);
			forma.setDatosPacienteMap("nombreBarrio",barrio.split("-")[0]+"-"+barrio.split("-")[1]);
		}
		
		//Parametros no requeridos por la forma, pero necesarios para la insercion del paciente
		forma.setDatosPacienteMap("codigoTipoPersona","1");
		forma.setDatosPacienteMap("estadoCivil",ConstantesBD.acronimoEstadoCivilDesconocido);
		forma.setDatosPacienteMap("tipoSangre","9");
		
		
		
		//Se toma los valores de parametros generales
		forma.setDatosPacienteMap("direccion",ValoresPorDefecto.getDireccionPaciente(usuario.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getDireccionPaciente(usuario.getCodigoInstitucionInt()));	
		forma.setDatosPacienteMap("ocupacion",ValoresPorDefecto.getOcupacion(usuario.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getOcupacion(usuario.getCodigoInstitucionInt()));
	}
	
	//*************************************************************************************************	
	
	/**
	 * Ingresa datos en el mapa del paciente
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param PersonaBasica paciente
	 * @param String indFuente
	 * @param HashMap datosPaciente
	 * */
	private void accionLlenarFormaPaciente(
											Connection con,
											PacientesEntidadesSubConForm forma, 
											PersonaBasica paciente,
											String indFuente, 
											HashMap datosPersona)
	{
		//********************************************************************************************
		//********************************************************************************************
		//Inicializa el codigoPaciente
		forma.setDatosPacienteMap("codigoPaciente","");
		
		//Se carga informacion del paciente de la estructura Paciente		
		if(indFuente.equals("paciente"))
		{
			//Se instancia mundo de paciente
			Paciente mundoPaciente = new Paciente();
			
			//Se carga paciente
			try 
			{
				mundoPaciente.cargarPaciente(con, paciente.getCodigoPersona());
			} 
			catch (SQLException e) 
			{
				logger.error("Error cargando paciente en llenarFormaPaciente: "+e);
			}
			
			forma.setDatosPacienteMap("codigoPaciente",mundoPaciente.getCodigoPersona());
			forma.setRegistroEntidadesSubMap("codigoPaciente",mundoPaciente.getCodigoPersona());
			forma.setDatosPacienteMap("numeroHistoriaClinica",mundoPaciente.getNumeroHistoriaClinica());		
			forma.setDatosPacienteMap("anioHistoriaclinica",mundoPaciente.getAnioHistoriaClinica());
			forma.setDatosPacienteMap("tipoIdentificacion",mundoPaciente.getCodigoTipoIdentificacion());
			forma.setDatosPacienteMap("numeroIdentificacion",mundoPaciente.getNumeroIdentificacion());	
			
			forma.setDatosPacienteMap("primerApellido",mundoPaciente.getPrimerApellidoPersona(false));
			forma.setDatosPacienteMap("segundoApellido",mundoPaciente.getSegundoApellidoPersona(false));
			forma.setDatosPacienteMap("primerNombre",mundoPaciente.getPrimerNombrePersona(false));
			forma.setDatosPacienteMap("segundoNombre",mundoPaciente.getSegundoNombrePersona(false));
			forma.setDatosPacienteMap("fechaNacimiento",mundoPaciente.getDiaNacimiento()+"/"+mundoPaciente.getMesNacimiento()+"/"+mundoPaciente.getAnioNacimiento());
			
			forma.setDatosPacienteMap("codigoBarrio",mundoPaciente.getCodigoBarrio());
			forma.setDatosPacienteMap("nombreBarrio",mundoPaciente.getBarrio(false));
			forma.setDatosPacienteMap("direccion",mundoPaciente.getDireccion());
			forma.setDatosPacienteMap("sexo",mundoPaciente.getCodigoSexo());
			forma.setDatosPacienteMap("ocupacion",mundoPaciente.getCodigoOcupacion());
			forma.setDatosPacienteMap("zonaDomicilio",mundoPaciente.getCodigoZonaDomicilio());
			forma.setDatosPacienteMap("grupoPoblacional",mundoPaciente.getCodigoGrupoPoblacional());
			
			//Parametros no requeridos por la forma, pero necesarios para la insercion del paciente
			forma.setDatosPacienteMap("codigoTipoPersona",mundoPaciente.getCodigoTipoPersona());
			forma.setDatosPacienteMap("estadoCivil",mundoPaciente.getCodigoEstadoCivil());
			forma.setDatosPacienteMap("tipoSangre",mundoPaciente.getCodigoTipoSangre());
			
			//Valida la existencia de la informacion del pais y ciudad de expedicion
			if(!mundoPaciente.getCodigoPais().equals(""))
			{
				forma.setDatosPacienteMap("paisExpedicion",mundoPaciente.getCodigoPaisId());
				forma.setCiudadExpedicionList(Utilidades.obtenerCiudadesXPais(con,mundoPaciente.getCodigoPaisId()));
			}
			if(!mundoPaciente.getCodigoCiudadId().equals("")&&!mundoPaciente.getCodigoDepartamentoId().equals(""))		
				forma.setDatosPacienteMap("ciudadExpedicion",mundoPaciente.getCodigoDepartamentoId()+ConstantesBD.separadorSplit+mundoPaciente.getCodigoCiudadId());
			
			//Valida la existencia de la informacion del pais y ciudad de identificacion 
			if(!mundoPaciente.getCodigoPaisIdentificacion().equals(""))
			{
				forma.setDatosPacienteMap("paisNacimiento",mundoPaciente.getCodigoPaisIdentificacion());
				forma.setCiudadNacimientoList(Utilidades.obtenerCiudadesXPais(con,mundoPaciente.getCodigoPaisIdentificacion()));
			}
			if(!mundoPaciente.getCodigoDepartamentoIdentificacion().equals("")&&!mundoPaciente.getCodigoCiudadIdentificacion().equals(""))
				forma.setDatosPacienteMap("ciudadNacimiento",mundoPaciente.getCodigoDepartamentoIdentificacion()+ConstantesBD.separadorSplit+mundoPaciente.getCodigoCiudadIdentificacion());
			
			//Valida la existencia de la informacion del pais y ciudad de residencia
			if(!mundoPaciente.getCodigoPais().equals(""))
			{
				forma.setDatosPacienteMap("paisResidencia",mundoPaciente.getCodigoPais());
				forma.setCiudadResidenciaList(Utilidades.obtenerCiudadesXPais(con,mundoPaciente.getCodigoPais()));
			}
			if(!mundoPaciente.getCodigoCiudad().equals("")&&!mundoPaciente.getCodigoDepartamento().equals(""))
				forma.setDatosPacienteMap("ciudadResidencia",mundoPaciente.getCodigoDepartamento()+ConstantesBD.separadorSplit+mundoPaciente.getCodigoCiudad());
		}
		//********************************************************************************************
		//********************************************************************************************
		//Se carga informacion de la estructura de persona
		else if(indFuente.equals("persona"))
		{
			String tipoBD = System.getProperty("TIPOBD");
			boolean existeUsuario = false;
			try 
			{
				existeUsuario = UtilidadValidacion.existeUsuario(con,tipoBD,forma.getDatosPacienteMap("tipoIdentificacion").toString(),forma.getDatosPacienteMap("numeroIdentificacion").toString());
			} catch (SQLException e) 
			{
				logger.error("Error al verificar si existe usuario para ese paciente: "+e);
			}
			
			if(existeUsuario)
			{
				//se consultan datos de la persona
				datosPersona = Utilidades.obtenerDatosPersona(
								con,
								forma.getDatosPacienteMap("tipoIdentificacion").toString(),
								forma.getDatosPacienteMap("numeroIdentificacion").toString()
								);
				
				//Se llena la forma con los datos de la persona
				forma.setDatosPacienteMap("codigoPaciente",datosPersona.get("codigo_0").toString());			
				forma.setRegistroEntidadesSubMap("codigoPaciente",datosPersona.get("codigo_0").toString());
				
				forma.setDatosPacienteMap("primerApellido",datosPersona.get("primer_apellido_0").toString());
				forma.setDatosPacienteMap("segundoApellido",datosPersona.get("segundo_apellido_0").toString());
				forma.setDatosPacienteMap("primerNombre",datosPersona.get("primer_nombre_0").toString());
				forma.setDatosPacienteMap("segundoNombre",datosPersona.get("segundo_nombre_0").toString());
				forma.setDatosPacienteMap("fechaNacimiento",datosPersona.get("fecha_nacimiento_0").toString());
				
				forma.setDatosPacienteMap("codigoBarrio",datosPersona.get("codigo_barrio_0").toString());
				forma.setDatosPacienteMap("nombreBarrio",datosPersona.get("barrio_0").toString());
				forma.setDatosPacienteMap("direccion",datosPersona.get("direccion_0").toString());
				forma.setDatosPacienteMap("sexo",datosPersona.get("codigo_sexo_0").toString());
				forma.setDatosPacienteMap("ocupacion","");
				forma.setDatosPacienteMap("zonaDomicilio","");
				forma.setDatosPacienteMap("grupoPoblacional","");				

				forma.setDatosPacienteMap("paisExpedicion",datosPersona.get("codigo_pais_id_0").toString());
				
				if(!datosPersona.get("codigo_pais_id_0").toString().equals(""))
					forma.setCiudadExpedicionList(Utilidades.obtenerCiudadesXPais(con,datosPersona.get("codigo_pais_id_0").toString()));
				
				forma.setDatosPacienteMap("ciudadExpedicion",datosPersona.get("codigo_depto_id_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_id_0").toString());
 
				forma.setDatosPacienteMap("paisNacimiento",datosPersona.get("codigo_pais_nacimiento_0").toString());
				
				if(!datosPersona.get("codigo_pais_nacimiento_0").toString().equals(""))
					forma.setCiudadNacimientoList(Utilidades.obtenerCiudadesXPais(con,datosPersona.get("codigo_pais_nacimiento_0").toString()));
				
				forma.setDatosPacienteMap("ciudadNacimiento",datosPersona.get("codigo_depto_nacimiento_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_nacimiento_0").toString());
				
				forma.setDatosPacienteMap("paisResidencia",datosPersona.get("codigo_pais_vivienda_0").toString());
				
				if(!datosPersona.get("codigo_pais_vivienda_0").toString().equals(""))
					forma.setCiudadResidenciaList(Utilidades.obtenerCiudadesXPais(con,datosPersona.get("codigo_pais_vivienda_0").toString()));
				
				forma.setDatosPacienteMap("ciudadResidencia",datosPersona.get("codigo_depto_vivienda_0").toString()+ConstantesBD.separadorSplit+datosPersona.get("codigo_ciudad_vivienda_0").toString());

				//Parametros no requeridos por la forma, pero necesarios para la insercion del paciente
				forma.setDatosPacienteMap("codigoTipoPersona","1");
				forma.setDatosPacienteMap("estadoCivil",datosPersona.get("codigo_estado_civil_0").toString());
				forma.setDatosPacienteMap("tipoSangre","9");
				
				//Marcador que indica que el usuario es paciente
				forma.setValidacionesMap("esUsuarioPaciente",ConstantesBD.acronimoSi);				
								
				//Se consulta el login del usuario
				Usuario mundoUsuario = new Usuario();
				mundoUsuario.init(tipoBD);
				forma.setDatosPacienteMap("loginUsuarioPaciente",mundoUsuario.buscarLogin(con,forma.getDatosPacienteMap("tipoIdentificacion").toString(),forma.getDatosPacienteMap("numeroIdentificacion").toString(),""));
			}
			else
				//Marcador que indica que el usuario es paciente
				forma.setValidacionesMap("esUsuarioPaciente",ConstantesBD.acronimoNo);			
		}
		//********************************************************************************************
		//********************************************************************************************
		//se carga la informacion del paciente desde la estructura de capitados
		else if(indFuente.equals("capitado"))
		{
			forma.setDatosPacienteMap("primerApellido",datosPersona.get("primer_apellido_0").toString());
			forma.setDatosPacienteMap("segundoApellido",datosPersona.get("segundo_apellido_0").toString());
			forma.setDatosPacienteMap("primerNombre",datosPersona.get("primer_nombre_0").toString());
			forma.setDatosPacienteMap("segundoNombre",datosPersona.get("segundo_nombre_0").toString());
			forma.setDatosPacienteMap("fechaNacimiento",datosPersona.get("fecha_nacimiento_0").toString());			
			
			forma.setDatosPacienteMap("codigoBarrio","");
			forma.setDatosPacienteMap("nombreBarrio","");
			forma.setDatosPacienteMap("direccion",datosPersona.get("direccion_0").toString());
			forma.setDatosPacienteMap("sexo",datosPersona.get("sexo_0").toString());
			forma.setDatosPacienteMap("ocupacion","");
			forma.setDatosPacienteMap("zonaDomicilio","");
			forma.setDatosPacienteMap("grupoPoblacional","");
			
			forma.setDatosPacienteMap("estadoCivil",ConstantesBD.acronimoEstadoCivilDesconocido);			
			forma.setDatosPacienteMap("codigoTipoPersona","1");		
			forma.setDatosPacienteMap("tipoSangre","9");
		}
	}
	
	//*************************************************************************************************
	
	
	/**
	 * Llena la informacion del paciente en entidades Subcontratadas
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionIniciarInfoPacEntiSub(Connection con,PacientesEntidadesSubConForm forma, UsuarioBasico usuario)
	{				
		if(forma.getValidacionesMap("existePacEntidades").toString().equals(ConstantesBD.acronimoSi))
		{	
			
			//Consulta los contratos Vigentes
			if(UtilidadValidacion.esConvenioCapitado(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoConvenio").toString())))
				forma.setContratoList(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoPaciente").toString()), Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoConvenio").toString())));
			else
				forma.setContratoList(Utilidades.obtenerContratos(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoConvenio").toString()), false, true));			
		}
		else if(forma.getValidacionesMap("existePacEntidades").toString().equals(ConstantesBD.acronimoNo))
		{
			//Verifica si existe informacion del paciente
			if(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoSi))
				forma.setRegistroEntidadesSubMap("codigoPaciente",forma.getDatosPacienteMap("codigoPaciente"));
			
			forma.setRegistroEntidadesSubMap("institucion",usuario.getCodigoInstitucionInt());
			forma.setRegistroEntidadesSubMap("centroAtencion",usuario.getCodigoCentroAtencion());
			forma.setRegistroEntidadesSubMap("entidadSubcontratada","");		
			forma.setRegistroEntidadesSubMap("ingreso","");
			forma.setRegistroEntidadesSubMap("codigoConvenio","");
			forma.setRegistroEntidadesSubMap("contrato","");
			forma.setRegistroEntidadesSubMap("nroCarnet","");
			forma.setRegistroEntidadesSubMap("autorizacionIngreso","");
			forma.setRegistroEntidadesSubMap("fechaAutorizacion",UtilidadFecha.getFechaActual());
			forma.setRegistroEntidadesSubMap("horaAutorizacion",UtilidadFecha.getHoraActual());			
			forma.setRegistroEntidadesSubMap("observaciones","");
			forma.setRegistroEntidadesSubMap("obserAnulacion","");
			forma.setRegistroEntidadesSubMap("estado",ConstantesIntegridadDominio.acronimoEstadoAbierto);		
		}	
		
		forma.setRegistroEntidadesSubMap("usuarioModifica",usuario.getLoginUsuario());
		forma.setRegistroEntidadesSubMap("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.setRegistroEntidadesSubMap("horaModifica",UtilidadFecha.getHoraActual());
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Carga informacion en la estructura de paciente
	 * @param Paciente paciente
	 * @param PacientesEntidadesSubConForm forma
	 * */
	private Paciente accionCargarMundoPaciente(Paciente mundoPaciente, PacientesEntidadesSubConForm forma)
	{	
		
		//Se llena el mundo del paciente con los datos del form		
		if(!forma.getDatosPacienteMap("codigoPaciente").toString().equals(""))
			mundoPaciente.setCodigoPersona(Integer.parseInt(forma.getDatosPacienteMap("codigoPaciente").toString()));		
			
		mundoPaciente.setCodigoTipoIdentificacion(forma.getDatosPacienteMap("tipoIdentificacion").toString());		
		mundoPaciente.setNumeroIdentificacion(forma.getDatosPacienteMap("numeroIdentificacion").toString());			
		mundoPaciente.setCodigoDepartamentoId(forma.getDatosPacienteMap("ciudadExpedicion").toString().split(ConstantesBD.separadorSplit)[0]);		
		mundoPaciente.setCodigoCiudadId(forma.getDatosPacienteMap("ciudadExpedicion").toString().split(ConstantesBD.separadorSplit)[1]);		
		mundoPaciente.setCodigoPaisId(forma.getDatosPacienteMap("paisExpedicion").toString());		
		mundoPaciente.setPrimerApellidoPersona(forma.getDatosPacienteMap("primerApellido").toString());		
		mundoPaciente.setSegundoApellidoPersona(forma.getDatosPacienteMap("segundoApellido").toString());		
		mundoPaciente.setPrimerNombrePersona(forma.getDatosPacienteMap("primerNombre").toString());		
		mundoPaciente.setSegundoNombrePersona(forma.getDatosPacienteMap("segundoNombre").toString());		
		mundoPaciente.setCodigoPaisIdentificacion(forma.getDatosPacienteMap("paisNacimiento").toString());		
		mundoPaciente.setCodigoDepartamentoIdentificacion(forma.getDatosPacienteMap("ciudadNacimiento").toString().split(ConstantesBD.separadorSplit)[0]);		
		mundoPaciente.setCodigoCiudadIdentificacion(forma.getDatosPacienteMap("ciudadNacimiento").toString().split(ConstantesBD.separadorSplit)[1]);		
		
		String[] fechaNac = forma.getDatosPacienteMap("fechaNacimiento").toString().split("/");		
		mundoPaciente.setAnioNacimiento(fechaNac[2]);
		mundoPaciente.setMesNacimiento(fechaNac[1]);
		mundoPaciente.setDiaNacimiento(fechaNac[0]);
		
		mundoPaciente.setCodigoPais(forma.getDatosPacienteMap("paisResidencia").toString());		
		mundoPaciente.setCodigoDepartamento(forma.getDatosPacienteMap("ciudadResidencia").toString().split(ConstantesBD.separadorSplit)[0]);		
		mundoPaciente.setCodigoCiudad(forma.getDatosPacienteMap("ciudadResidencia").toString().split(ConstantesBD.separadorSplit)[1]);		
		mundoPaciente.setCodigoBarrio(forma.getDatosPacienteMap("codigoBarrio").toString());		
		mundoPaciente.setDireccion(forma.getDatosPacienteMap("direccion").toString());		
		mundoPaciente.setCodigoZonaDomicilio(forma.getDatosPacienteMap("zonaDomicilio").toString());		
		mundoPaciente.setCodigoOcupacion(forma.getDatosPacienteMap("ocupacion").toString());	
		mundoPaciente.setCodigoSexo(forma.getDatosPacienteMap("sexo").toString());				
		mundoPaciente.setCodigoGrupoPoblacional(forma.getDatosPacienteMap("grupoPoblacional").toString());	
		
		//Parametros no requeridos por la forma, pero necesarios para la insercion del paciente
		mundoPaciente.setCodigoTipoPersona("1");
		mundoPaciente.setCodigoEstadoCivil(forma.getDatosPacienteMap("estadoCivil").toString());
		mundoPaciente.setCodigoTipoSangre(forma.getDatosPacienteMap("tipoSangre").toString());
		
		return mundoPaciente;		
	}
	
	//*************************************************************************************************	
	
	/**
	 * Llena el HashMap de Base de Datos de  Servicios Autorizados
	 * @param PacientesEntidadesSubConForm forma
	 * @param int pos
	 * @param UsuarioBasico usuario
	 * */
	private HashMap accionLlenarMapaServiciosAutorizados(PacientesEntidadesSubConForm forma, int pos, UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		
		mapa.put("consecutivo",forma.getServiciosAutorizadosMap("consecutivo_"+pos).toString());
		mapa.put("consecutivoPacEntidadesSub",forma.getRegistroEntidadesSubMap("consecutivo").toString());
		mapa.put("anioConsecutivoPacEntidades",forma.getRegistroEntidadesSubMap("anioConsecutivo").toString());
		mapa.put("institucion",usuario.getCodigoInstitucionInt());
		mapa.put("servicio",forma.getServiciosAutorizadosMap("servicio_"+pos).toString());
		mapa.put("cantidad",forma.getServiciosAutorizadosMap("cantidad_"+pos).toString());
		mapa.put("autorizacionServicio",forma.getServiciosAutorizadosMap("autorizacionServicio_"+pos).toString());
		mapa.put("fechaAutorizacion",UtilidadFecha.conversionFormatoFechaABD(forma.getServiciosAutorizadosMap("fechaAutorizacion_"+pos).toString()));		
		
		mapa.put("responsable",usuario.getLoginUsuario());		
		forma.setServiciosAutorizadosMap("responsable_"+pos,usuario.getLoginUsuario());
		forma.setServiciosAutorizadosMap("nombreResponsable_"+pos,usuario.getNombreUsuario());
		
		mapa.put("observaciones",forma.getServiciosAutorizadosMap("observaciones_"+pos).toString());		
		
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));		
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());				
		
		return mapa;		
	}
	
	//*************************************************************************************************
	
	/**
	 * Carga las estrucuras de datos necesarias para el flujo de busqueda por Periodo
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionCargarEstructurasPorPeriodo(Connection con, PacientesEntidadesSubConForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		
		forma.setEntidadesSubContratadasList(UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(
				con, 
				usuario.getCodigoInstitucionInt(), 
				UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		
		forma.setConveniosList(Utilidades.obtenerConvenios(con, "","", true, "", true));
		
		forma.setUsuariosList(UtilidadesManejoPaciente.obtenerUsuariosEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));		
		
		forma.setRegistroEntidadesSubMap("numRegistros","0");
		
		forma.setIndicadorFlujo("porPeriodo");
	}	
	
	//*************************************************************************************************
	
	/**
	 * Carga la informacion de Pacientes Entidades SubContratadas
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * @param tomarFiltros
	 * */
	private void accionCargarInfoPacEntidadesSub(Connection con,
			PacientesEntidadesSubConForm forma, 
			PersonaBasica paciente, 
			UsuarioBasico usuario,
			boolean tomarFiltros)
	{
		if(!tomarFiltros)
		{
			//Carga la informacion de Pacientes Entidades SubContratadas en base al paciente cargado en session
			forma.setRegistroEntidadesSubMap(PacientesEntidadesSubCon.consultarPacientesEntidadesSubcontratadas(con, 
					usuario.getCodigoInstitucionInt(), 
					"", 
					"", 
					paciente.getCodigoPersona()+"", 
					"",
					"","","","","","",
					true));
		}
		else
		{
			//Carga la informacion de Pacientes Entidades SubContratadas de acuerdo a los filtros
			forma.setRegistroEntidadesSubMap(PacientesEntidadesSubCon.consultarPacientesEntidadesSubcontratadas(con, 
					usuario.getCodigoInstitucionInt(), 
					"", 
					"", 
					"", 
					"",
					forma.getValidacionesMap("codigoEntidadSub").toString(),
					forma.getValidacionesMap("codigoConvenio").toString(),
					forma.getValidacionesMap("fechaInicialBusqueda").toString(),
					forma.getValidacionesMap("fechaFinalBusqueda").toString(),
					forma.getValidacionesMap("acronimoEstado").toString(),
					forma.getValidacionesMap("loginUsuario").toString(),			
					true));			
		}		
	}
	
	//*************************************************************************************************
	//*************************************************************************************************
	
	
	//*************************************************************************************************
	///********************************METODOS DE MANEJO AJAX BASICO***********************************	
	
	/**
	 * Recarga las Ciudad dependiendo del pais
	 * @param Connection con
	 * @param PacientesEntidadesSubConForm forma
	 * @param HttpServletResponse response
	 * */
	public ActionForward accionAjaxCambiarUbicacion(Connection con,PacientesEntidadesSubConForm forma,HttpServletResponse response)
	{
		String resultado = "<respuesta>";
		String codigoPais = "";
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
				
		codigoPais = forma.getCodigoPais();
		
		if(forma.getIndicadorComponenteBusqueda().equals("ciudadExpedicion"))
		{
			forma.setCiudadExpedicionList(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = forma.getCiudadExpedicionList();
		}
		else if(forma.getIndicadorComponenteBusqueda().equals("ciudadNacimiento"))
		{
			forma.setCiudadNacimientoList(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = forma.getCiudadNacimientoList();
		}
		else if(forma.getIndicadorComponenteBusqueda().equals("codigoCiudadReside"))
		{
			forma.setCiudadResidenciaList(Utilidades.obtenerCiudadesXPais(con, codigoPais));
			arregloAux = forma.getCiudadResidenciaList();						
		}
		
		
		logger.info("NÚMERO DE ELEMENTO ARREGLO CIUDADES=> "+arregloAux.size());
		logger.info("CODIGO PAIS=>*"+codigoPais+"*");
		logger.info("INDICADOR DE CIUDAD =>*"+forma.getIndicadorComponenteBusqueda()+"*");
		
				
		resultado+= "<infoid>" +
						"<sufijo>ajaxBusquedaCiudades</sufijo>" +
						"<id-select>"+forma.getIndicadorComponenteBusqueda()+"</id-select>" +
					"</infoid>"; 
		
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
			response.reset();
			response.resetBuffer();
			
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().flush();			
	        response.getWriter().write(resultado);
	        response.getWriter().close();
	        
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroCiudades: "+e);
		}
		return null;
	}
	
	//*************************************************************************************************	
	
	/**
	 * Método implementado para realizar el filtro de Contratos por Convenio Seleccionado
	 * @param con
	 * @param PacientesEntidadesSubConForm forma
	 * @param usuarioActual
	 * @param response
	 * @return
	 */
	private ActionForward accionAjaxFiltroContratos(
													Connection con, 
													PacientesEntidadesSubConForm forma, 
													UsuarioBasico usuario, 
													HttpServletResponse response) 
	{		
		String aux = "";		
		HashMap mapaAux = new HashMap();		
				
		//Consulta los contratos Vigentes
		if(UtilidadValidacion.esConvenioCapitado(con, Integer.parseInt(forma.getIndicadorConvenioBusqueda().toString())))
		{
			
			forma.setContratoList(new ArrayList());
			
			if(forma.getValidacionesMap("existePaciente").toString().equals(ConstantesBD.acronimoSi))
				forma.setContratoList(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, Integer.parseInt(forma.getRegistroEntidadesSubMap("codigoPaciente").toString()), Integer.parseInt(forma.getIndicadorConvenioBusqueda().toString())));
			else
			{
				
				//Si el paciente no existe primero se verifica si el convenio capitado elegido está dentro de su cargue
				for(int i=0;i<Utilidades.convertirAEntero(forma.getDatosCapitacionMap("numRegistros")+"", true);i++)
				{
					//Si tiene el mismo convenio y la fecha inicial y final del cargue es vigente se añade
					
					if(Integer.parseInt(forma.getDatosCapitacionMap("codigo_convenio_"+i).toString())==Integer.parseInt(forma.getIndicadorConvenioBusqueda().toString())&&
						!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(con), UtilidadFecha.conversionFormatoFechaAAp(forma.getDatosCapitacionMap("fecha_inicial_"+i).toString()))&&
						UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(con),UtilidadFecha.conversionFormatoFechaAAp(forma.getDatosCapitacionMap("fecha_final_"+i).toString())))
					{
						HashMap elemento = new HashMap();
						elemento.put("codigo", forma.getDatosCapitacionMap("codigo_contrato_"+i));
						elemento.put("numerocontrato", forma.getDatosCapitacionMap("numero_contrato_"+i));
						forma.getContratoList().add(elemento);
					}
						
				}
			}
			
			//Si no se encontró nada entonces se muestran los contratos vigentes que no sean del cargue
			if(forma.getContratoList().size()==0)
				forma.setContratoList(Utilidades.obtenerContratos(con, Integer.parseInt(forma.getIndicadorConvenioBusqueda().toString()), false, true));
		}
		else
			forma.setContratoList(Utilidades.obtenerContratos(con, Integer.parseInt(forma.getIndicadorConvenioBusqueda().toString()), false, true));
				
		aux = "<respuesta>"+
			  	"<infoid>" +
			  		"<sufijo>ajaxBusquedaContratos</sufijo>" +
			  		"<id-select>"+forma.getIndicadorComponenteBusqueda()+"</id-select>" +
			  	"</infoid>";
		
		for(int i=0;i<forma.getContratoList().size();i++)
		{
			mapaAux = (HashMap)forma.getContratoList().get(i);
			aux += "" +
				"<contrato>" +
					"<codigo>"+mapaAux.get("codigo")+"</codigo>" +
					"<numero-contrato>"+mapaAux.get("numerocontrato")+"</numero-contrato>" +
				"</contrato>";
		}
		aux += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.reset();
			response.resetBuffer();			
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().flush();			
	        response.getWriter().write(aux);
	        response.getWriter().close();	        
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroContratos: "+e);
		}
		return null;		
	}
	
	//**********************************************************************************************
	
	/**
	 * Guarda el log de codigo de autorizacion
	 * @param Connectio con 
	 * @param UsuarioBasico usuario
	 * @param PacientesEntidadesSubConForm forma
	 * @param Paciente mundoPaciente
	 * */
	public boolean guardarLogAutorizacion(Connection con, UsuarioBasico usuario, PacientesEntidadesSubConForm forma, Paciente mundoPaciente)
	{	
     	//Inserta el log autorizacion en caso de que se hubiere pedido
     	if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)
     			&& !forma.getAutorizacionIngEvento().getCodigo().equals("") 
     				&& !forma.getAutorizacionIngEvento().isActivo())
     	{
     		if(UtilidadesManejoPaciente.insertarLogAutorizacionIngresoEvento(
     				con,
     				forma.getAutorizacionIngEvento().getCodigo()+"",
     				mundoPaciente.getCodigoPersona()+"",     				
     				mundoPaciente.getNumeroIdentificacion()+"", 
     				mundoPaciente.getCodigoTipoIdentificacion(),
     				usuario,
     				678+""))
     		{
     			logger.info("\n\nINSERTO LOG DE AUTORIZACION INGRESO EVENTO");
     			return true;
     		}
     		else
     		{
     			logger.info("\n\n NO INSERTO LOG DE AUTORIZACION INGRESO EVENTO");
     			return true;
     		}
     	}
     	
     	return true;
	}
}	