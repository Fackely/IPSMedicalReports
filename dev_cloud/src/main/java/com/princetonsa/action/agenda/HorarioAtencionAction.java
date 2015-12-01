/*
* @(#)HorarioAtencionAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.action.agenda;

import java.lang.NumberFormatException;
import java.lang.String;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;
import org.axioma.util.xml.EscrituraXmlException;
import org.axioma.util.xml.UtilidadXMLJAXB;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.agenda.HorarioAtencionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.HorarioAtencion;
import com.servinte.axioma.jaxb.horariosAtencion.Contenido;
import com.servinte.axioma.jaxb.horariosAtencion.Convenciones;
import com.servinte.axioma.jaxb.horariosAtencion.HorariosAtencion;
import com.servinte.axioma.jaxb.horariosAtencion.Profesional;
import com.servinte.axioma.jaxb.horariosAtencion.Profesionales;
import com.servinte.axioma.jaxb.horariosAtencion.Unidad;
import com.servinte.axioma.jaxb.horariosAtencion.UnidadesAgenda;

/**
* Action, controla todas las opciones dentro de un horario de atención, incluyendo los posibles
* casos de error, y los casos de flujo.
*
* @version 1.0, Sep 4, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class HorarioAtencionAction extends Action
{
	/** Objeto que permite el manejo de archivos de registro */
	private transient Logger logger = Logger.getLogger(HorarioAtencionAction.class);

	@SuppressWarnings("unchecked")
	public ActionForward execute(
		ActionMapping		aam_mapping,
		ActionForm			aaf_form,
		HttpServletRequest 	ahsr_request,
		HttpServletResponse	ahsr_response
	)throws Exception
	{
		Connection lc_con= null;
		try {

		String ls_siguiente;

		/* Si la operación se pudo ejecutar de manera exitosa ls_siguiente sera reasignado */
		ls_siguiente = "paginaError";

		/* Validar que el tipo de formulario recibido sea el esperado */
		if(aaf_form instanceof HorarioAtencionForm)
		{

			HorarioAtencionForm	lhaf_form;
			String				ls_estado;
			String				ls_tipoBD;

			/* Obtener el tipo de fuente de datos */
			ls_tipoBD = System.getProperty("TIPOBD");

			/* Obtener el estado actual */
			lhaf_form = (HorarioAtencionForm)aaf_form;
			ls_estado = lhaf_form.getEstado();
			
			HttpSession session=ahsr_request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			logger.warn("Estado en HorarioAtencionAction-->"+ls_estado);

			try
			{
				/* Obtener una conexión a la fuente de datos */
				lc_con = DaoFactory.getDaoFactory(ls_tipoBD).getConnection();
			}
			catch(SQLException lse_e)
			{
				/* Error al intentar obtener una conexión a la fuente de datos */
				if(logger.isDebugEnabled() )
					logger.debug("No se pudo obtener conexión a la fuente de datos");

				lse_e.printStackTrace();
				ahsr_request.setAttribute("descripcionError", "errors.problemasBd");

				/* Direccionar a un estado dummie para evitar siguientes validaciones */
				lc_con		= null;
				ls_estado	= "-1";
			}
			
			if(util.ValoresPorDefecto.getMostrarNombreJSP()){
				lhaf_form.setMostrarRutaJsp(true);
			}

			if(ls_estado == null)
			{
				/* El estado actual es intederminado y por lo tanto no válido */
				if(logger.isDebugEnabled() )
					logger.debug("Estado inválido en el flujo de horarios de atención");

				ahsr_request.setAttribute("descripcionError", "errors.estadoInvalido");
			}
			else if(ls_estado.equals("-1") || ls_estado.equals("listarHorarioAtencion") )
			{
				/* No hacer nada en este estado. Solo evitar validaciones y procesamiento posterior */
			}
			
			
			else if(ls_estado.equals("detalleHorarioAtencion")||ls_estado.equals("modificarHorarioAtencion"))
			{
				/* Obtener los datos de un horario de atención para consulta o modificación */
				HorarioAtencion	lha_horario;
				int	li_codigo;				

				try
				{
					/* Obtener los datos del horario de atención especificado */
					lha_horario = new HorarioAtencion();

					li_codigo = Integer.parseInt(ahsr_request.getParameter("codigo") );

					lha_horario.cargarHorarioAtencion(lc_con, li_codigo, usuario.getCodigoInstitucionInt());
					
					inciarDiasSemana(lhaf_form,lc_con,lha_horario.getCodigoDiaSemana());
					
					lhaf_form.setEstadoTransaccion(ConstantesBD.acronimoNo);
					
					lhaf_form.setFunc("");
					/*
						Copiar los datos del horario de atención al formulario para despliegue de
						información
					*/
					PropertyUtils.copyProperties(lhaf_form, lha_horario);

					/* Se desea modificar el horario de atención */
					if(ls_estado.equals("modificarHorarioAtencion") )
						ls_estado = "guardarHorarioAtencionModificado";

					ls_siguiente = "horarioAtencion";
				}
				catch(NumberFormatException lnfe_e)
				{
					/* El código del horario de atención no es válido */
					if(logger.isDebugEnabled() )
						logger.debug("El código de horario de atención es inválido");

					lnfe_e.printStackTrace();
					ahsr_request.setAttribute(
						"descripcionError",
						"Código de horario de atención inválido"
					);
				}
				catch(SQLException lse_e)
				{
					/* Error al obtener datos del horario de atención desde la fuente de datos */
					if(logger.isDebugEnabled() )
						logger.debug("No se pudo obtener el horario de atencion");

					lse_e.printStackTrace();
					ahsr_request.setAttribute(
						"descripcionError",
						"Imposible obtener el horario de atención " + lse_e.getMessage()
					);
				}
				finally
				{
					/* Error al copiar información del horario de atención al formulario */
					if(logger.isDebugEnabled() )
						logger.debug("Error al copiar información al formulario");

					ahsr_request.setAttribute(
						"descripcionError",
						"Imposible obtener propiedades del horario de atención"
					);
				}
			}
			
			
			
			else if(ls_estado.equals("eliminarHorarioAtencion") )
			{
				/* Eliminar uno o mas horarios de atención */
				String[] lsa_codigos;
				lhaf_form.setEstadoEliminar(ConstantesBD.acronimoNo);
				/* Validar si los parámetros obtenidos son válidos */
				if(
					(lsa_codigos = ahsr_request.getParameterValues("codigo") ) != null &&
					lsa_codigos.length > 0
				)
				{
					boolean	lb_error;
					int[]	lia_codigos = new int[lsa_codigos.length];

					try
					{
						/*
							Obtener los códigos de los horarios de atención a eliminar en formato
							numérico
						*/
						for(int li_i = 0; li_i < lsa_codigos.length; li_i++)
							lia_codigos[li_i] = Integer.parseInt(lsa_codigos[li_i]);
						
						//Cargar un vector de horarios de atencion
						HorarioAtencion[] horarioAnt = this.cargarHorariosAtencion(lc_con,lia_codigos, usuario.getCodigoInstitucionInt()); 

						/* Eliminar los horarios de atención */
						lb_error = !HorarioAtencion.eliminarHorarioAtencion(lc_con, lia_codigos);
						
						
						
						
						
						/* Al menos un horario de atención no pudo ser eliminado */
						if(lb_error)
						{
							/* Error al eliminar uno de los horarios de atención */
							if(logger.isDebugEnabled() )
								logger.debug("Error al eliminar uno o más horarios de atención");

							ahsr_request.setAttribute(
								"descripcionError",
								"No se pudo eliminar al menos un horario de atención"
							);
						}
						else
						{
							lhaf_form.setEstadoEliminar(ConstantesBD.acronimoSi);
							ls_estado = "listarHorarioAtencion";
							///Si no hubo errores entonces genero log de eliminacion
							this.generarLog(null, null, ConstantesBD.tipoRegistroLogEliminacion, usuario, horarioAnt);
						}
					}
					catch(SQLException lse_e)
					{
						/* Error al eliminar uno de los horarios de atención */
						if(logger.isDebugEnabled() )
							logger.debug("Error al eliminar uno o más horarios de atención");

						lse_e.printStackTrace();
						ahsr_request.setAttribute(
							"descripcionError",
							"Imposible eliminar los horarios de atención"
						);
					}
					catch(NumberFormatException lnfe_e)
					{
						/* Error al obtener el código de un horario de atención */
						if(logger.isDebugEnabled() )
							logger.debug("El menos un código de horario de atención es inválido");

						lnfe_e.printStackTrace();
						ahsr_request.setAttribute(
							"descripcionError",
							"El menos un código de horario de atención es inválido"
						);
					}
				}
				else
					ls_estado = "listarHorarioAtencion";
				
			}
			else if(
				ls_estado.equals("guardarNuevoHorarioAtencion") ||
				ls_estado.equals("guardarHorarioAtencionModificado")
			)
			{
				/*
					Insertar un nuevo horario de atención en la fuente de datos o modificar los
					datos de un horario de atención
				*/
				HorarioAtencion	lha_horario;

				try
				{
					boolean lb_error;

					lb_error = false;

					/* Copiar la información de el formulario al horario de atención */
					lha_horario = new HorarioAtencion();
					PropertyUtils.copyProperties(lha_horario, lhaf_form);

					if(ls_estado.equals("guardarNuevoHorarioAtencion") )
					{
						/* Insertar un nuevo horario de atención en la fuente de datos */
						lb_error = lha_horario.interfazTipoInserccionHorario(lc_con, false, lhaf_form.getDiasSemanaMap(),lhaf_form.getTipoAtencion(), usuario.getCodigoInstitucionInt());
						logger.info("valor del lb_error >> "+lb_error);
					}
					else
					{
						HorarioAtencion horarioAnt = new HorarioAtencion();
						horarioAnt.cargarHorarioAtencion(lc_con, lhaf_form.getCodigo(), usuario.getCodigoInstitucionInt());
						
						lhaf_form.setTipoAtencion(lha_horario.getTipoAtencion());
						
						/* Modificar la información de un horario de atención */
						lb_error = lha_horario.interfazTipoInserccionHorario(lc_con, true, lhaf_form.getDiasSemanaMap(),lhaf_form.getTipoAtencion(), usuario.getCodigoInstitucionInt());
						
						//Se verifica éxito de la modificacion
						if(lb_error)
						{
							if(fueModificado(horarioAnt,lha_horario))
								this.generarLog(lha_horario,horarioAnt,ConstantesBD.tipoRegistroLogModificacion,usuario,new HorarioAtencion[0]);
						}
					}
					

					/*
						Copiar la información del horario de atención al formulario, (solo para
						despliegue de información)
					*/
					PropertyUtils.copyProperties(lhaf_form, lha_horario);
					
					

					if(!lb_error)
					{
						/* Error al insertar el horario de atenciÃ³n */
						if(ls_estado.equals("guardarNuevoHorarioAtencion") )
							ahsr_request.setAttribute("descripcionError","Imposible ingresar el horario de atención");
						/* Error al modificar el horario de atención */
						else
							ahsr_request.setAttribute("descripcionError","Imposible modificar el horario de atención");
					}
					else
					{
						lhaf_form.setEstadoTransaccion(ConstantesBD.acronimoSi);
						ls_estado		= "detalleHorarioAtencion";
						ls_siguiente	= "horarioAtencion";
					}
				}
				catch(SQLException lse_e)
				{
					ActionErrors lae_errores;

					/* Error al obtener datos del horario de atención desde la fuente de datos */
					if(logger.isDebugEnabled() )
						logger.debug("No se pudo obtener el horario de atencion");

					lae_errores		= new ActionErrors();
					ls_siguiente	= "horarioAtencion";
					
					String[] mensaje = lse_e.getMessage().split(ConstantesBD.separadorSplit); 
					
					if(mensaje[0].indexOf("día") > -1)
						lae_errores.add("diaSemana",new ActionMessage("error.horarioAtencion.diaReservado",(mensaje.length>1?"con las unidades de agenda: "+mensaje[1]:"")));
					else if(mensaje[0].indexOf("consultorio") > -1)
						lae_errores.add("codigoConsultorio",new ActionMessage("error.horarioAtencion.consultorioReservado",(mensaje.length>1?"con las unidades de agenda: "+mensaje[1]:"")));
					else if(mensaje[0].indexOf("profesional") > -1)
						lae_errores.add("codigoMedico",new ActionMessage("error.horarioAtencion.medicoReservado",(mensaje.length>1?"con las unidades de agenda: "+mensaje[1]:"")));
					else
					{
						lae_errores.add("descripcionError", new ActionMessage("errors.problemasBd") );
						ls_siguiente = "paginaError";
					}

					saveErrors(ahsr_request, lae_errores);
				}
			}
			
			else if(ls_estado.equals("nuevoHorarioAtencion"))
			{
				/* Ingresar la información de un nuevo horario de atención */
				int centroAtencionTempo=lhaf_form.getCentroAtencion();
				lhaf_form.resetAsignar();
				lhaf_form.setCodigoCentroAtencion(centroAtencionTempo);
				lhaf_form.setCentroAtencion(lhaf_form.getCodigoCentroAtencion());
				lhaf_form.setCodigoConsultorio(Utilidades.convertirAEntero(lhaf_form.getConsultorioAsignar()));
				lhaf_form.setHoraInicio(lhaf_form.getHoraIniAsignar());
				
				inciarDiasSemana(lhaf_form,lc_con,ConstantesBD.codigoNuncaValido);
				
				int n=Utilidades.convertirAEntero(lhaf_form.getDiasSemanaMap("numRegistros")+"");

				for(int i=0;i<n;i++)
				{
					if(Utilidades.convertirAEntero(lhaf_form.getDiasSemanaMap("codigo_"+i)+"") == Utilidades.convertirAEntero(lhaf_form.getDiaAsignar()))
						lhaf_form.setDiasSemanaMap("seleccionado_"+i, ConstantesBD.acronimoSi);
				}
				
				lhaf_form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(lc_con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion));
				//lhaf_form.setCentroAtencion(Utilidades.convertirAEntero(lhaf_form.getCentrosAtencionAutorizados("codigo_0")+""));
				lhaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(lc_con, usuario.getLoginUsuario(), lhaf_form.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion,""));
				ls_estado		= "guardarNuevoHorarioAtencion";
				ls_siguiente	= "horarioAtencion";				
			}
			else if(ls_estado.equals("ordenar"))
			{
				//se efectua la ordenacion del listado de horarios de atencion
				ArrayList lista  = new ArrayList(lhaf_form.getItems());
				lhaf_form.setItems(Listado.ordenarColumna(lista,lhaf_form.getUltimaPropiedad(),lhaf_form.getColumna()));
				lhaf_form.setUltimaPropiedad(lhaf_form.getColumna());
				ls_siguiente = "listarHorarioAtencion";
			}
			else if(ls_estado.equals("redireccion"))
			{
				lhaf_form.setEstadoTransaccion(ConstantesBD.acronimoNo);
				if(lc_con != null && !lc_con.isClosed() )
                    UtilidadBD.closeConnection(lc_con);
				ahsr_response.sendRedirect(lhaf_form.getLinkSiguiente());
				return null;
			}
			else if(ls_estado.equals("cambiarCentroAtencionXConsultorios"))
			{
				if(lhaf_form.getCentroAtencion() != ConstantesBD.codigoNuncaValido)
					lhaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(lc_con, usuario.getLoginUsuario(), lhaf_form.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion,""));
				if(lc_con != null && !lc_con.isClosed() )
                    UtilidadBD.closeConnection(lc_con);
				ls_estado		= "guardarNuevoHorarioAtencion";
				if(lhaf_form.getFunc().equals("consulta"))
					ls_siguiente	= "consultarHA";
				else if(lhaf_form.getFunc().equals("busquedaAvanzada"))
					ls_siguiente	= "busquedaAvanzadaHA";
				else
					ls_siguiente	= "horarioAtencion";
			}else if(ls_estado.equals("filtraUnidadAgenda"))
			{
				return accionFiltrarUnidadAgenda(lc_con, lhaf_form, usuario, ahsr_response, aam_mapping);
			} 
			else
			{
				/* El estado actual no corresponde a un estado válido */
				if(logger.isDebugEnabled() )
					logger.debug("Estado inválido en el flujo de horarios de atención");

				ahsr_request.setAttribute("descripcionError", "errors.estadoInvalido");
			}

			if(ls_estado.equals("listarHorarioAtencion") || ls_estado.equals("cambiarCentroAtencion"))
			{	
				/* Listar todos los horarios de atención existentes del centro de atención*/
				try
				{
					String estTransAux = ConstantesBD.acronimoNo;
					int cenAtenAux = ConstantesBD.codigoNuncaValido;
					try{
						logger.info("\n\nestado eliminar:: "+lhaf_form.getEstadoEliminar());
						if(lhaf_form.getEstadoEliminar().equals(ConstantesBD.acronimoNo)&&lhaf_form.getEstadoEliminar()!=null){
							estTransAux = ConstantesBD.acronimoSi;
							cenAtenAux = lhaf_form.getCentroAtencion();
						}
					}catch (Exception e) {}
					
					
					lhaf_form.reset();
					// Obtener centros de atención validos para el usuario
					lhaf_form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(lc_con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion));
					
					if(Integer.parseInt(lhaf_form.getCentrosAtencionAutorizados("numRegistros")+"")<=0)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("", new ActionMessage("errors.notEspecific","Usuario sin ningun centro de atención autorizado para ingresar a horarios de atención"));
						saveErrors(ahsr_request, errores);
						UtilidadBD.closeConnection(lc_con);
						return aam_mapping.findForward("paginaErroresActionErrors");
						
					}
					
					//-----Si es listar horario se asigna el centro de atención en sesión-------//
					if(ls_estado.equals("listarHorarioAtencion"))
						{
							//lhaf_form.setEstadoTransaccion(estTransAux);
							
							String[] caa = lhaf_form.getCentrosAtencionAutorizados("todos").toString().split(", ");
							logger.info("caa - "+caa);
							logger.info("caa long - "+caa.length);
							
							for(int i=0; i<caa.length; i++){
								logger.info("caa - "+caa[i]);
								if (Integer.parseInt(caa[i]) == usuario.getCodigoCentroAtencion()){
									//-----Se asigna el centro de atención del usuario por defecto cuando empieza ---------//
									lhaf_form.setCentroAtencion(usuario.getCodigoCentroAtencion());
									//cuando lo asigna debe salir del ciclo.
									i=caa.length;
								}else{
									lhaf_form.setCentroAtencion(ConstantesBD.codigoNuncaValido);
								}
							}
						}
					
					if(estTransAux.equals(ConstantesBD.acronimoSi))
						lhaf_form.setCentroAtencion(cenAtenAux);
					
					lhaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(lc_con, usuario.getLoginUsuario(), lhaf_form.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion,""));
					
					lhaf_form.setItems(HorarioAtencion.listarHorariosAtencion(lc_con, lhaf_form.getCentroAtencion(), lhaf_form.getUnidadesAgendaAutorizadas("todos").toString(), usuario.getCodigoInstitucionInt()));

					ls_siguiente = "listarHorarioAtencion";
				}
				catch(Exception lse_e)
				{
					lse_e.printStackTrace();
					ahsr_request.setAttribute(
						"descripcionError",
						"Imposible guardar el horario de atención " + lse_e.getMessage()
					);
				}
			}
			else if(ls_estado.equals("consultarHorarioAtencion"))
			{
				return accionConsultarHorarioAtencion(lc_con, lhaf_form, usuario, ahsr_response, aam_mapping,ahsr_request);
			}
			else if(ls_estado.equals("buscarHorariosAT"))
			{
				return accionBuscarHorarioAtencion(lc_con, lhaf_form, usuario, ahsr_response, aam_mapping,ahsr_request);
			}
			else if(ls_estado.equals("tipoRepDia"))
			{
				int numRegistros;	
				
				lhaf_form.setDiasSemanaBusqueda(Utilidades.obtenerDiasSemana(lc_con));		
				numRegistros = Integer.parseInt(lhaf_form.getDiasSemanaBusqueda("numRegistros").toString());
				
				for(int i = 0; i< numRegistros ; i++){
					lhaf_form.setDiasSemanaBusqueda("seleccionado_"+i,ConstantesBD.acronimoNo);
				}
				
				UtilidadBD.closeConnection(lc_con);		
				return aam_mapping.findForward("consultarHA");
			
			}else if(ls_estado.equals("cambiarConsultorioAsignado") || ls_estado.equals("cambiarProfesionalAsignado") ){
				UtilidadBD.closeConnection(lc_con);		
				
				if(lhaf_form.getFunc().equals("consulta"))
					return aam_mapping.findForward("consultarHA");
				else if(lhaf_form.getFunc().equals("busquedaAvanzada"))
					return aam_mapping.findForward("busquedaAvanzadaHA");
				
			}else if(ls_estado.equals("busquedaAvanzada")){
				return accionConsultarHorariosAtencionAvanzada(lhaf_form, usuario, aam_mapping, lc_con);
							
			}else if(ls_estado.equals("busquedaAvanzadaHA")){
				return accionBusquedaAvanzadaHorarioAtencion(lc_con, lhaf_form, usuario, ahsr_response, aam_mapping, ahsr_request);
			
			}else if (ls_estado.equals("volverDetalleHA")){
				/**
				 * MT 6661 -- al regresar al listado de horarios no muestra ninguno Horario de Atencion:
				 * La lista de items que se esta recorriendo en /src/main/webapp/horarioAtencion/listarHorarioAtencion.jsp 
				 * no esta llena al no volver al formulario.
				 * Se creo un metodo para volver a cosultar los horarios en la base de datos.4
				 */
				setItemsHorarioAtencion(lc_con, lhaf_form, ahsr_request, usuario);
				UtilidadBD.closeConnection(lc_con);		
				lhaf_form.setEstado("listarHorarioAtencion");
			    return aam_mapping.findForward("listarHorarioAtencion");
			    /**
			     * Fin  MT 6661 
			     */
				
			}
			
			/* Cerrar la conexión a la fuente de datos si es necesario */
			if(lc_con != null && !lc_con.isClosed() )
                UtilidadBD.closeConnection(lc_con);

			lhaf_form.setEstado(ls_estado);
		}
		else
		{
			/* El formulario reciibdo no corresponde a un formulario de horario de atención */
			if(logger.isDebugEnabled() )
				logger.debug(
					"El formulario actual no corresponde con el formulario esperado por Horario " +
					"de Atención"
				);

			ahsr_request.setAttribute("descripcionError", "errors.formaTipoInvalido");
		}

		return aam_mapping.findForward(ls_siguiente);

		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(lc_con);
		}
		return null;

	}
	
	/**
	 * Metodo que consulta todos los horarios de atencion para un centro de atencion.
	 * Este metodo se crea para solucion de la intecidencia 
	 * MT 6661 -- al regresar al listado de horarios no muestra ninguno Horario de Atencion:
	 * @param lc_con
	 * @param lhaf_form
	 * @param ahsr_request
	 * @param usuario
	 */
	private void setItemsHorarioAtencion(Connection lc_con,HorarioAtencionForm	lhaf_form ,	HttpServletRequest 	ahsr_request,UsuarioBasico usuario ){
		/* Listar todos los horarios de atención existentes del centro de atención*/
		Collection collection = new Vector();
		try
		{
			String estTransAux = ConstantesBD.acronimoNo;
			int cenAtenAux = ConstantesBD.codigoNuncaValido;
			try{
				logger.info("\n\nestado eliminar:: "+lhaf_form.getEstadoEliminar());
				if(lhaf_form.getEstadoEliminar().equals(ConstantesBD.acronimoNo)&&lhaf_form.getEstadoEliminar()!=null){
					estTransAux = ConstantesBD.acronimoSi;
					cenAtenAux = lhaf_form.getCentroAtencion();
				}
			}catch (Exception e) {
				
			}
			
			
			lhaf_form.reset();
			// Obtener centros de atención validos para el usuario
			lhaf_form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(lc_con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion));
			
			if(Integer.parseInt(lhaf_form.getCentrosAtencionAutorizados("numRegistros")+"")<=0)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.notEspecific","Usuario sin ningun centro de atención autorizado para ingresar a horarios de atención"));
				saveErrors(ahsr_request, errores);
				UtilidadBD.closeConnection(lc_con);
			
				//return aam_mapping.findForward("paginaErroresActionErrors");
				
			}
			
			//-----Si es listar horario se asigna el centro de atención en sesión-------//
		
					String[] caa = lhaf_form.getCentrosAtencionAutorizados("todos").toString().split(", ");
					logger.info("caa - "+caa);
					logger.info("caa long - "+caa.length);
					
					for(int i=0; i<caa.length; i++){
						logger.info("caa - "+caa[i]);
						if (Integer.parseInt(caa[i]) == usuario.getCodigoCentroAtencion()){
							//-----Se asigna el centro de atención del usuario por defecto cuando empieza ---------//
							lhaf_form.setCentroAtencion(usuario.getCodigoCentroAtencion());
							//cuando lo asigna debe salir del ciclo.
							i=caa.length;
						}else{
							lhaf_form.setCentroAtencion(ConstantesBD.codigoNuncaValido);
						}
					}
				
			
			if(estTransAux.equals(ConstantesBD.acronimoSi))
				lhaf_form.setCentroAtencion(cenAtenAux);
			
			lhaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(lc_con, usuario.getLoginUsuario(), lhaf_form.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion,""));
			
			
			
			lhaf_form.setItems(HorarioAtencion.listarHorariosAtencion(lc_con, lhaf_form.getCentroAtencion(), lhaf_form.getUnidadesAgendaAutorizadas("todos").toString(), usuario.getCodigoInstitucionInt()));
			
			
		
			 
		}
		catch(Exception lse_e)
		{
			lse_e.printStackTrace();
			ahsr_request.setAttribute(
				"descripcionError",
				"Imposible guardar el horario de atención " + lse_e.getMessage()
			);
		}
				
			
			
			
	}
	/**
	 * Método implementado para realizar el filtro de unidades de agenda
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionBuscarHorarioAtencion(Connection con, HorarioAtencionForm forma, UsuarioBasico usuario, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{	
		ActionErrors errores= new ActionErrors();
	
		if(forma.getCentroAtencion() == ConstantesBD.codigoNuncaValido)
			errores.add("descripcion",new ActionMessage("errors.required","El Centro de Atencion "));
		
		if(forma.getTipoReporte() == 0)
		{
			if(forma.getDiaSemanaSel() == ConstantesBD.codigoNuncaValido)
				errores.add("descripcion",new ActionMessage("errors.required","El Dia de la Semana "));
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
		}
		else{	

			
			ArrayList<Integer> diasSeleccionados = new ArrayList<Integer>();
			diasSeleccionados.add(forma.getDiaSemanaSel());

			forma.setListaHorarios(HorarioAtencion.consultarHA(forma.getCentroAtencion(),forma.getUnidadAgenda(), forma.getConsultorioAsignado(), forma.getCodConsultorio(), forma.getProfesionalAsignado(), forma.getCodigoMedico(), diasSeleccionados, forma.getTipoReporte()));

			
			String XMLHorarios=generarXMLHorarios(forma);
			logger.info(XMLHorarios);
			
			
			forma.setXmlHorarioAtencion(XMLHorarios/*HorarioAtencion.generacionXMLHorarios(forma, false)*/);
		}
		
		
		forma.setCodigoCentroAtencion(forma.getCentroAtencion());
		forma.setDiaSemana(forma.getDiaSemanaSel()+"");
		forma.setConsultorio(forma.getCodConsultorio()+"");
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("consultarHA");
	}
	

	/**
	 * Método para henerar el XML de los horarios de atención para ser mostrados en la consulta gráfica
	 * @param forma
	 * @return String con el XML
	 */
	private String generarXMLHorarios(HorarioAtencionForm forma) {
		HashMap<String, Integer> profesionalesExistentes;

		Contenido contenido=new Contenido();
		
		// Se generan las convenciones
		Convenciones convenciones=new Convenciones();

		// Se generan los profesionales
		profesionalesExistentes=new HashMap<String, Integer>();
		int contMedicos=1;
		Profesionales profesionales=new Profesionales();
		for(DtoHorarioAtencion elem: forma.getListaHorarios())
		{			
			if(!profesionalesExistentes.containsKey(elem.getConvencion()))
			{
				Profesional profesional=new Profesional();
//				profesional.setCodigo(contMedicos);
//				profesional.setCodigo(elem.getCodigoMedico());
				profesional.setCodigo(elem.getConvencion());
				profesional.setNombre(elem.getNombreMedico());
				profesionales.getProfesional().add(profesional);
				profesionalesExistentes.put(elem.getCodigoMedico()+"", elem.getConvencion());
				contMedicos++;
			}
			convenciones.setProfesionales(profesionales);
		}
		contenido.setConvenciones(convenciones);
			
		//Se generan las unidades de consulta
		
		ArrayList<Integer> unidadesExistentes=new ArrayList<Integer>();
		UnidadesAgenda unidadesAgenda=new UnidadesAgenda();
		for(DtoHorarioAtencion elem: forma.getListaHorarios())
		{			
			if(!unidadesExistentes.contains(elem.getUnidadConsulta()))
			{
				Unidad unidad=new Unidad();
				unidad.setColor(elem.getColorUniAgenda());
				unidad.setNombre(elem.getNombreUniAgenda());
				unidadesAgenda.getUnidad().add(unidad);
				
			}
			convenciones.setUnidadesAgenda(unidadesAgenda);
		}
		
		String horaInicio=null;
		String horaFin=null;
		// Se generan los horarios
		HorariosAtencion horariosAtencion=new HorariosAtencion();
		for(DtoHorarioAtencion elem: forma.getListaHorarios())
		{
			com.servinte.axioma.jaxb.horariosAtencion.HorarioAtencion horarioAtencion=new com.servinte.axioma.jaxb.horariosAtencion.HorarioAtencion(); 
			horarioAtencion.setCodigoHorario(elem.getCodigo());
			horarioAtencion.setHoraInicio(elem.getHoraInicio());
			horarioAtencion.setHoraFin(elem.getHoraFin());
			horarioAtencion.setColorUnidadAgenda(elem.getColorUniAgenda());
			horarioAtencion.setDiaSemana(elem.getNombreDia());
			if(forma.getTipoReporte()==0)
			{
				horarioAtencion.setConsultorio(elem.getConsultorio()+"");
			}
			else
			{
				horarioAtencion.setConsultorio(elem.getDia()+"");
			}
			horarioAtencion.setProfesional(profesionalesExistentes.get(elem.getCodigoMedico()+""));
			//horarioAtencion.setProfesional(elem.getCodigoMedico());
			horarioAtencion.setProfesional(elem.getConvencion());
			horariosAtencion.getHorarioAtencion().add(horarioAtencion);
			
			if(forma.getTipoReporte()==0)
			{
				horarioAtencion.setNombreConsultorio(elem.getNombreConsultorio());
			}
			else
			{
				horarioAtencion.setDiaSemana(obtenerDiaSemana(forma.getListDiaSemana(), elem.getDia()));
			}
			
			if(horaInicio==null || horaInicio.compareTo(elem.getHoraInicio())>0)
			{
				horaInicio=elem.getHoraInicio();
			}
			if(horaFin==null || horaFin.compareTo(elem.getHoraFin())<0)
			{
				horaFin=elem.getHoraFin();
			}
		}
		contenido.setHorariosAtencion(horariosAtencion);
		
		// Se asignan los atributos del contenido
		if(forma.getTipoReporte()==0)
		{
			contenido.setTipo("dia");
		}
		else
		{
			contenido.setTipo("consultorio");
		}		

		contenido.setHoraInicio(horaInicio);
		contenido.setHoraFin(horaFin);
		contenido.setIntervalo(forma.getMultiploMinGenCita());
		contenido.setDia(obtenerDiaSemana(forma.getListDiaSemana(), forma.getDiaSemanaSel()).trim());
		
		String XML=null;
		try {
			XML = UtilidadXMLJAXB.marshallerXml("com.servinte.axioma.jaxb.horariosAtencion", contenido);
		} catch (EscrituraXmlException e) {
			logger.error("Error generando el XML",e);
		}
		return XML;
	}

	/**
	 * Método que devuelve el día de la semana basandose en el <code>List</code> pasado por parámetros
	 * @param listDiaSemana
	 * @param diaSemana
	 * @return <code>String</code> con el día de la semana
	 */
	@SuppressWarnings("unchecked")
	private String obtenerDiaSemana(ArrayList<HashMap> listDiaSemana, int diaSemana)
	{
		String diaSemanaStr="";
		for(HashMap<String, Object> diaSemanaMap:listDiaSemana)
		{
			if(diaSemana==(Integer)diaSemanaMap.get("codigo"))
			{
				diaSemanaStr=(String)diaSemanaMap.get("dia");
				break;
			}
		}
		return diaSemanaStr;
	}

	/**
	 * Método implementado para realizar el filtro de unidades de agenda
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionConsultarHorarioAtencion(Connection con, HorarioAtencionForm forma, UsuarioBasico usuario, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		forma.reset();
		forma.setFunc("consulta");
		forma.setMultiploMinGenCita(Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt())));
		ActionErrors errores= new ActionErrors();
		
		forma.setParametroMultiplo(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
		
		if(forma.getParametroMultiplo().equals(""))
			errores.add("descripcion", new ActionMessage("prompt.generico","No se tiene definido el parametro general Multiplo en minutos para generacion de citas, por favor verifique."));
	
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
		}
		else
		{			
			//Obtener centros de atención validos para el usuario
			forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion));
			
			//Obtener unidades de agenda autorizadas
			forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), forma.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion,ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));

			forma.setListDiaSemana(Utilidades.obtenerDiasSemanaArray(con));
			
		}

		UtilidadBD.closeConnection(con);		
		return mapping.findForward("consultarHA");
	}
	
	
	
	/**
	 * Método que verifica si un horario de atención fue modificado
	 * @param horarioAnt
	 * @param horarioNue
	 * @return
	 */
	private boolean fueModificado(HorarioAtencion horarioAnt, HorarioAtencion horarioNue) 
	{
		boolean modificado = false;
		
		if(horarioAnt.getCodigoUnidadConsulta()!=horarioNue.getCodigoUnidadConsulta())
			modificado = true;
		if(horarioAnt.getCodigoConsultorio()!=horarioNue.getCodigoConsultorio())
			modificado = true;
		if(horarioAnt.getCodigoDiaSemana()!=horarioNue.getCodigoDiaSemana())
			modificado = true;
		if(horarioAnt.getCodigoMedico()!=horarioNue.getCodigoMedico())
			modificado = true;
		if(!horarioAnt.getHoraInicio().equals(horarioNue.getHoraInicio()))
			modificado = true;
		if(!horarioAnt.getHoraFin().equals(horarioNue.getHoraFin()))
			modificado = true;
		if(horarioAnt.getDuracionConsulta()!=horarioNue.getDuracionConsulta())
			modificado = true;
		if(horarioAnt.getPacientesSesion()!=horarioNue.getPacientesSesion())
			modificado = true;
		
		return modificado;
	}


	/**
	 * Método que carga los horarios de atención que se van a eliminar
	 * @param con
	 * @param codigosEliminados
	 * @return
	 */
	private HorarioAtencion[] cargarHorariosAtencion(Connection con, int[] codigoEliminados, int institucion) 
	{
		HorarioAtencion[] horarios = new HorarioAtencion[codigoEliminados.length];
		
		for(int i=0;i<codigoEliminados.length;i++)
		{
			horarios[i] =  new HorarioAtencion();
			try 
			{
				horarios[i].cargarHorarioAtencion(con, codigoEliminados[i], institucion);
			} 
			catch (Exception e) 
			{
				logger.error("Error cargando un horario de atencion: "+e);
			}
		}
		
		return horarios;
	}


	/**
	 * Método implementado para generar el log tipo Archivo para los horarios de atención
	 * @param con 
	 * @param horarioNue
	 * @param horarioAnt
	 * @param tipo
	 * @param usuario
	 * @param codigosEliminados 
	 */
	private void generarLog(HorarioAtencion horarioNue, HorarioAtencion horarioAnt, int tipo, UsuarioBasico usuario, HorarioAtencion[] horariosEli) 
	{
		String log = "";
	
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
			//se edita el log*********************************************************************
			log = 		 "\n   ============INFORMACIÓN ORIGINAL HORARIO DE ATENCIÓN=========== " +
			 "\n*  Código Axioma ["+horarioAnt.getCodigo()+"] " +
			 "\n*  Unidad Agenda ["+horarioAnt.getUnidadConsulta()+"] " +
			 "\n*  Consultorio ["+horarioAnt.getConsultorio()+"] " +
			 "\n*  Día ["+horarioAnt.getDiaSemana()+"] "+
			 "\n*  Médico ["+horarioAnt.getMedico()+"] " +
			 "\n*  Hora Inicio ["+UtilidadFecha.convertirHoraACincoCaracteres(horarioAnt.getHoraInicio())+"] "+
			 "\n*  Hora Fin ["+UtilidadFecha.convertirHoraACincoCaracteres(horarioAnt.getHoraFin())+"] "+
			 "\n*  Tiempo Sesión ["+horarioAnt.getDuracionConsulta()+"] "+
			 "\n*  Pacientes Sesión ["+horarioAnt.getPacientesSesion()+"] "+
			 "\n*  Centro Atención ["+horarioAnt.getNombreCentroAtencion()+"] ";
			
			 log+="\n   ===========INFORMACIÓN MODIFICADA HORARIO DE ATENCIÓN========== 	" +
			 "\n*  Código Axioma ["+horarioNue.getCodigo()+"] " +
			 "\n*  Unidad Agenda ["+horarioNue.getUnidadConsulta()+"] " +
			 "\n*  Consultorio ["+horarioNue.getConsultorio()+"] " +
			 "\n*  Día ["+horarioNue.getDiaSemana()+"] "+
			 "\n*  Médico ["+horarioNue.getMedico()+"] " +
			 "\n*  Hora Inicio ["+UtilidadFecha.convertirHoraACincoCaracteres(horarioNue.getHoraInicio())+"] "+
			 "\n*  Hora Fin ["+UtilidadFecha.convertirHoraACincoCaracteres(horarioNue.getHoraFin())+"] "+
			 "\n*  Tiempo Sesión ["+horarioNue.getDuracionConsulta()+"] "+
			 "\n*  Pacientes Sesión ["+horarioNue.getPacientesSesion()+"] "+
			 "\n*  Centro Atención ["+horarioNue.getNombreCentroAtencion()+"] ";
			 
			 log+="\n========================================================\n\n\n ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			
			for(int i=0;i<horariosEli.length;i++)
			{
				log += 		 "\n   ============REGISTRO ELIMINADO=========== " +
				"\n*  Código Axioma ["+horariosEli[i].getCodigo()+"] " +
				 "\n*  Unidad Agenda ["+horariosEli[i].getUnidadConsulta()+"] " +
				 "\n*  Consultorio ["+horariosEli[i].getConsultorio()+"] " +
				 "\n*  Día ["+horariosEli[i].getDiaSemana()+"] "+
				 "\n*  Médico ["+horariosEli[i].getMedico()+"] " +
				 "\n*  Hora Inicio ["+UtilidadFecha.convertirHoraACincoCaracteres(horariosEli[i].getHoraInicio())+"] "+
				 "\n*  Hora Fin ["+UtilidadFecha.convertirHoraACincoCaracteres(horariosEli[i].getHoraFin())+"] "+
				 "\n*  Tiempo Sesión ["+horariosEli[i].getDuracionConsulta()+"] "+
				 "\n*  Pacientes Sesión ["+horariosEli[i].getPacientesSesion()+"] "+
				 "\n*  Centro Atención ["+horariosEli[i].getNombreCentroAtencion()+"] ";
				 
				 log+="\n========================================================\n\n\n ";
				
			}
			
			 
			 
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logHorarioAtencionCodigo,log,tipo,usuario.getLoginUsuario());
	}
	
	
	/**
	 * Carga los dias de la semana 
	 * @param HorarioAtencionForm forma
	 * @param Connection con
	 * */
	private void inciarDiasSemana(HorarioAtencionForm forma, 
								  Connection con,
								  int diaSemana)
	{	
		int numRegistros;		
		forma.setDiasSemanaMap(Utilidades.obtenerDiasSemana(con));		
		numRegistros = Integer.parseInt(forma.getDiasSemanaMap("numRegistros").toString());
		
		for(int i = 0; i< numRegistros ; i++)
		{
			if(diaSemana == Integer.parseInt(forma.getDiasSemanaMap("codigo_"+i).toString()))
				forma.setDiasSemanaMap("seleccionado_"+i,ConstantesBD.acronimoSi);
			else
				forma.setDiasSemanaMap("seleccionado_"+i,ConstantesBD.acronimoNo);
		}						
	}
	
	/**
	 * Método implementado para realizar el filtro de unidades de agenda
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarUnidadAgenda(Connection con, HorarioAtencionForm	forma, UsuarioBasico usuario, HttpServletResponse response, ActionMapping mapping) 
	{
		//Se consultan los Profesionales de la Salud
		ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>(); 
		HorarioAtencion horarioAten = new HorarioAtencion();
		forma.getProfesionaleSaludUniAgen().clear();
		forma.setIndexCodUniAgen(forma.getUnidadAgenda()+"");
		if(Utilidades.convertirAEntero(forma.getIndexCodUniAgen())!=ConstantesBD.codigoNuncaValido)
		{
			forma.setDatosUnidadAgenda(horarioAten.getEspecialidad(con, forma.getIndexCodUniAgen()));
			forma.setTipoAtencion(forma.getDatosUnidadAgenda().get("tipoatencion")+"");
			
			if(!(forma.getDatosUnidadAgenda().get("profesionales")+"").isEmpty())
			{
			
				if(Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()) != ConstantesBD.codigoNuncaValido
						&& forma.getDatosUnidadAgenda().containsKey("especialidad")
						)
				{				
					if(forma.getDatosUnidadAgenda().get("profesionales").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
					{
						
						array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, forma.getIndexCodUniAgen());
						forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
								usuario.getCodigoInstitucionInt(), 
								Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()), 
								false, true, array));
					}else{
						if(forma.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioProcedimiento)
						{
							array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, forma.getIndexCodUniAgen());
							forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
									usuario.getCodigoInstitucionInt(), 
									ConstantesBD.codigoNuncaValido, 
									false, true, array));
						}else{
							if(forma.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioInterconsulta)
							{
								forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, 
										usuario.getCodigoInstitucionInt(), 
										Utilidades.convertirAEntero(forma.getDatosUnidadAgenda().get("especialidad").toString()),
										false, true, array));
							}
						}
					}
				}
			}
		}
		
		if(forma.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
			forma.setMultiploMinGenCita(Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt())));
			
		forma.setEstado(forma.getEstadoAnt());
		
		UtilidadBD.closeConnection(con);		
		if(forma.getFunc().equals("consulta"))
			return mapping.findForward("consultarHA");
		else if(forma.getFunc().equals("busquedaAvanzada"))
			return mapping.findForward("busquedaAvanzadaHA");
		else
			return mapping.findForward("horarioAtencion");
	}
	
	/**
	 * Metodo para retornar la pagina de consulta avanzada de horarios de atención
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionConsultarHorariosAtencionAvanzada(HorarioAtencionForm forma,  UsuarioBasico usuario, ActionMapping mapping, Connection con)
	{
		forma.reset();
		forma.setFunc("busquedaAvanzada");
		
		// Obtener centros de atención validos para el usuario
		forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion));
		//Obtener unidades de agenda autorizadas
		forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), forma.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaHorarioAtencion,ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
		//Obtener profesionales 
		forma.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido,false, true,null));
		
		int numRegistros;	
		
		forma.setDiasSemanaBusqueda(Utilidades.obtenerDiasSemana(con));		
		numRegistros = Integer.parseInt(forma.getDiasSemanaBusqueda("numRegistros").toString());
		
		for(int i = 0; i< numRegistros ; i++){
			forma.setDiasSemanaBusqueda("seleccionado_"+i,ConstantesBD.acronimoNo);
		}
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("busquedaAvanzadaHA");
		
	}
	
	/**
	 * Método realiza la busqueda avanzade de Horarios de atencion
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionBusquedaAvanzadaHorarioAtencion(Connection con, HorarioAtencionForm forma, UsuarioBasico usuario, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) {	
		ArrayList<Integer> diasSeleccionados = new ArrayList<Integer>();
			
		for(int i=0 ; i<Integer.parseInt(forma.getDiasSemanaBusqueda("numRegistros")+"") ; i++){
			if((forma.getDiasSemanaBusqueda("seleccionado_"+i)+"").equals(ConstantesBD.acronimoSi)){
				diasSeleccionados.add(Integer.parseInt(forma.getDiasSemanaBusqueda("codigo_"+i)+""));
			}
		}

		forma.setItems(HorarioAtencion.listarHorariosAtencion(con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), forma.getCentroAtencion(),forma.getUnidadAgenda(), forma.getConsultorioAsignado(), forma.getCodConsultorio(), forma.getProfesionalAsignado(), forma.getCodigoMedico(), diasSeleccionados));
		forma.setCentroAtencion(ConstantesBD.codigoNuncaValido);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("listarHorarioAtencion");
	}
	
}