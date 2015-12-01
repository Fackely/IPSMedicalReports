/*
 * @(#)ServiciosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Beans.BeanBusquedaServicio;

import com.princetonsa.actionform.ServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.Servicio;
import com.princetonsa.mundo.parametrizacion.Servicios;

/**
 * Clase encargada del control de la funcionalidad "Servicios"
 * (Manejo de tablas maestras de procedimientos y similares)
 *
 * @version 1.0 Nov 27, 2003
 */
public class ServiciosAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Servicios
	 */
	private Logger logger = Logger.getLogger(ServiciosAction.class);
	
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
		Connection con=null;
		try{
			if (form instanceof ServiciosForm)
			{
				if (response==null); //Para evitar que salga el warning
				if( logger.isDebugEnabled() )
				{
					logger.debug("Entro al Action de Servicios");
				}

				ServiciosForm serviciosForm=(ServiciosForm)form;
				String estado=serviciosForm.getEstado();

				logger.warn("ServiciosAction ------------- estado ["+estado+"]       \n\n");


				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					logger.error("Problemas abriendo la conexión en ServiciosAction");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.

				HttpSession session=request.getSession();		
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				//Primera Condición: El usuario debe existir
				if( medico == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else
					if (estado==null||estado.equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ServiciosAction)", "errors.estadoInvalido", true);
					}
				if (estado.equals("empezarInsertar"))
				{
					serviciosForm.resetOdontologia();
					return this.accionEmpezarInsertar(mapping, con, serviciosForm);
				}
				if (estado.equals("empezarInsertarOdontologia"))
				{
					serviciosForm.resetOdontologia();
					return this.accionEmpezarInsertarOdontologia(mapping, con, serviciosForm);
				}
				else if (estado.equals("empezarModificar"))
				{
					return this.accionEmpezarModificar(mapping, request, con, serviciosForm,medico);
				}
				else if (estado.equals("resumen"))
				{
					return this.accionResumen(mapping, request, con, serviciosForm,medico);
				}
				else if (estado.equals("cancelar"))
				{
					return this.accionEstadoCancelar(request, response, con, serviciosForm);
				}
				else if (estado.equals("navegar"))
				{
					return this.accionNavegar(mapping, con, serviciosForm);
				}
				else if (estado.equals("detalle"))
				{
					return this.accionDetalle(con,mapping, serviciosForm, medico);
				}
				else if (estado.equals("salir"))
				{
					logger.info("---Accion -->"+serviciosForm.getAccionAFinalizar());
					if (serviciosForm.getAccionAFinalizar()==null)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ServiciosAction)", "errors.estadoInvalido", true);
					}
					else if (serviciosForm.getAccionAFinalizar().equals("insertar"))
					{
						logger.info("insertar");
						return this.accionSalirInsertar(mapping, request, response, con, serviciosForm,medico);
					}
					else if (serviciosForm.getAccionAFinalizar().equals("modificar"))
					{
						logger.info("modificar");
						return this.accionSalirModificar(mapping, request, response, con, serviciosForm,medico);
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ServiciosAction)", "errors.estadoInvalido", true);
					}
				}
				//***********ESTADOS PARA EL MANEJO DE LOS FORMULARIOS*************************************
				else if (estado.equals("agregarFormularios"))
				{
					serviciosForm.setArregloFormularios(Servicio.obtenerArregloFormularios(con, medico.getCodigoInstitucionInt()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresarFormularios");
				}
				else if (estado.equals("mostrarFormularios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("mostrarFormularios");
				}
				else if (estado.equals("guardarFormularios"))
				{
					UtilidadBD.closeConnection(con);
					if(serviciosForm.getAccionAFinalizar().equals("insertar"))
						return mapping.findForward("principal");
					else
						return mapping.findForward("detalle");
				}
				//******************************************************************************************
				else
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (ServiciosAction)", "errors.estadoInvalido", true);
				}



			}
			else
			{
				//Todavía no existe conexión, por eso no se cierra
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Método implementado para ir al detalle del servicio
	 * @param con
	 * @param mapping
	 * @param serviciosForm
	 * @param medico 
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ActionMapping mapping, ServiciosForm serviciosForm, UsuarioBasico medico) 
	{
		int pos = serviciosForm.getPos();
		
		String portatilAsociado = "";
		if(serviciosForm.getValores().containsKey("servicioPortatil_"+pos))
		{
			if(!(serviciosForm.getValores("servicioPortatil_" + pos)+"").equals("null") && !(serviciosForm.getValores("servicioPortatil_" + pos)+"").equals(""))
				portatilAsociado = serviciosForm.getValores("servicioPortatil_" + pos).toString();
			else
				portatilAsociado = "No maneja Pórtatil Asociado";
		}
		
		//Se cargan los formularios del servicio
		serviciosForm.setFormulario(Servicio.cargarFormulariosServicio(con, Integer.parseInt(serviciosForm.getValores("codigo_" + pos).toString()), medico.getCodigoInstitucionInt()));
		/**
		 * AQUI VOY
		 */
		//Se edita el LOG de modificación
		String log="\n            ====INFORMACION ORIGINAL===== " +
		"\n*  Código Servicio ["+serviciosForm.getValores("codigo_" + pos)+"] "+
		"\n*  Especialidad Servicio ["+serviciosForm.getValores("nombreEspecialidad_"+pos)+"]"+
		"\n*  Es activo ["+(UtilidadTexto.getBoolean(serviciosForm.getValores("activo_" + pos)+"")?"Sí":"No")+"] "+
		"\n*  Tipo Servicio ["+serviciosForm.getValores("nombreTipoServicio_" + pos)+"] "+
		"\n*  Naturaleza Servicio ["+serviciosForm.getValores("nombreNaturalezaServicio_" + pos)+"] "+
		"\n*  Código Cups ["+serviciosForm.getValores("nombreCups_" + pos)+"] "+
		"\n*  Descripción Cups ["+serviciosForm.getValores("descripcionCups_" + pos)+"] "+
		"\n*  Codigo ISS ["+serviciosForm.getValores("nombreISS_" + pos)+"] "+
		"\n*  Descripción ISS ["+serviciosForm.getValores("descripcionISS_" + pos)+"] "+
		"\n*  Codigo SOAT ["+serviciosForm.getValores("nombreSoat_" + pos)+"] "+
		"\n*  Descripción SOAT ["+serviciosForm.getValores("descripcionSoat_" + pos)+"] "+
		"\n*  Unidades UVR ["+serviciosForm.getValores("unidadesUvr_" + pos)+"] "+
		"\n*  Unidades SOAT ["+serviciosForm.getValores("unidadesSoat_" + pos)+"] "+
		"\n*  Sexo ["+serviciosForm.getValores("nombreSexo_" + pos)+"] "+
		"\n*  Grupo Servicio ["+serviciosForm.getValores("nombreGrupoServicio_" + pos)+"] "+
		"\n*  Es pos ["+(UtilidadTexto.getBoolean(serviciosForm.getValores("esPos_" + pos)+"")?"Sí":"No")+"] "+
		"\n*  Es pos Subsidiado ["+(UtilidadTexto.getBoolean(serviciosForm.getValores("esPosSubsidiado_" + pos)+"")?"Sí":"No")+"] "+
		"\n*  Formulario:  ";
		for(int i=0;i<Integer.parseInt(serviciosForm.getFormulario("numRegistros")+"");i++)
			log += "\n      "+serviciosForm.getFormulario("nombrePlantilla_"+i)+(UtilidadTexto.isEmpty(serviciosForm.getFormulario("nombreDiagnostico_"+i)+"")?"":" - "+serviciosForm.getFormulario("nombreDiagnostico_"+i));
		log += "\n*  Nivel ["+serviciosForm.getValores("descNivel_" + pos)+"] "+
		"\n*  Se realiza en la Institución ["+serviciosForm.getValores("realizaInstitucion_" + pos)+"] "+
		"\n*  Costo ["+serviciosForm.getValores("costo_" + pos)+"] "+
		"\n*  Requiere Interpretación ["+serviciosForm.getValores("requiereInterpretacion_" + pos)+"] "+
		"\n*  Requiere Diagnóstico ["+ValoresPorDefecto.getIntegridadDominio(serviciosForm.getValores("requiereDiagnostico_" + pos).toString())+"] "+
		"\n*  Pórtatil Asociado ["+portatilAsociado+"] "+
		"\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+
		"";
		
		serviciosForm.setLogInfoOriginalServicio(log);
		
		serviciosForm.setCosto(serviciosForm.getValores("costo_" + pos)+"");
		serviciosForm.setRequiereInterpretacion(serviciosForm.getValores("requiereInterpretacion_" + pos)+"");
		serviciosForm.setRealizaInstitucion(serviciosForm.getValores("realizaInstitucion_" + pos)+"");
		serviciosForm.setRequiereDiagnostico(serviciosForm.getValores("requiereDiagnostico_" + pos)+"");
		
		serviciosForm.setTarifariosMap(Servicio.consultarTarifarios(con,Utilidades.convertirAEntero(serviciosForm.getValores("codigo_" + pos)+"")));
			
		
		this.cerrarConexion(con);
		return mapping.findForward("detalle");
	}

	/**
	 * Método que se encarga de llenar una forma de tipo ServiciosForm
	 * con todos los datos presentes en un objeto servicios, para estar
	 * visibles al usuario
	 * 
	 * @param serviciosForm 
	 * @param servicios
	 */
	private void llenarForm (ServiciosForm serviciosForm, Servicios servicios)
	{
		logger.info("\n\nDENTRO DEL llenarForm");
		Servicio servicio;
		int i=0;
		
		//Recorremos todos los servicios
		Iterator iterador=servicios.getIteradorServicios();
		while (iterador.hasNext())
		{
			servicio=(Servicio)iterador.next();

			serviciosForm.setValores("codigo_"+i, "" + servicio.getCodigo());
			serviciosForm.setValores("especialidad_"+i, "" + servicio.getEspecialidad().getCodigo());
			serviciosForm.setValores("nombreEspecialidad_"+i, servicio.getEspecialidad().getNombre());
			serviciosForm.setValores("sexo_"+i, "" + servicio.getRestriccionSexo().getCodigo());
			serviciosForm.setValores("nombreSexo_"+i, servicio.getRestriccionSexo().getNombre());
			serviciosForm.setValores("naturalezaServicio_"+i, servicio.getNaturalezaServicio().getAcronimo());
			serviciosForm.setValores("nombreNaturalezaServicio_"+i, servicio.getNaturalezaServicio().getValue());
			serviciosForm.setValores("tipoServicio_"+i, servicio.getTipoServicio().getAcronimo());
			serviciosForm.setValores("nombreTipoServicio_"+i, servicio.getTipoServicio().getValue());
			//serviciosForm.setValores("formulario_" + i, "" + servicio.getFormulario());
			serviciosForm.setValores("nivel_" + i, "" + servicio.getNivel().getId());
			serviciosForm.setValores("descNivel_" + i, servicio.getNivel().getDescripcion());
			
			serviciosForm.setValores("requiereInterpretacion_" + i, ""+servicio.getRequiereInterpretacion());
			serviciosForm.setValores("requiereDiagnostico_" + i, ""+servicio.getRequiereDiagnostico());
			serviciosForm.setValores("costo_" + i, ""+servicio.getCosto());
			serviciosForm.setValores("realizaInstitucion_"+i, ""+servicio.getRealizaInstitucion());
			
			serviciosForm.setValores("unidadesUvr_"+i, ""+servicio.getUnidadesUvr());
			serviciosForm.setValores("grupoServicio_"+i, ""+servicio.getGrupoServicio().getCodigo()+"");
			serviciosForm.setValores("nombreGrupoServicio_"+i, ""+servicio.getGrupoServicio().getNombre());
		
			serviciosForm.setValores("esPos_" + i, "" + servicio.isEsPos());
			serviciosForm.setValores("esPosSubsidiado_" + i, "" + servicio.getPosSubsidiado());
			serviciosForm.setValores("activo_" + i, "" + servicio.getActivo());
			serviciosForm.setValores("usado_" +i, "" + servicio.getUsado());
			if (servicio.tieneInformacionCups())
			{
				serviciosForm.setValores("tieneInformacionCups_" + i, "true");
				serviciosForm.setValores("nombreCups_" + i, servicio.getInformacionCups().getNombre());
				serviciosForm.setValores("descripcionCups_" + i, servicio.getInformacionCups().getDescripcion());
			}
			else
			{
				serviciosForm.setValores("tieneInformacionCups_" + i, "false");
				serviciosForm.setValores("nombreCups_" + i, "");
				serviciosForm.setValores("descripcionCups_" + i, "");
			}
			
			if (servicio.tieneInformacionISS())
			{
				serviciosForm.setValores("tieneInformacionISS_" + i, "true");
				serviciosForm.setValores("nombreISS_" + i, servicio.getInformacionISS().getNombre());
				serviciosForm.setValores("descripcionISS_" + i, servicio.getInformacionISS().getDescripcion());
			}
			else
			{
				serviciosForm.setValores("tieneInformacionISS_" + i, "false");
				serviciosForm.setValores("nombreISS_" + i, "");
				serviciosForm.setValores("descripcionISS_" + i, "");
			}
			
			if (servicio.tieneInformacionSonria())
			{
				serviciosForm.setValores("tieneInformacionSonria_" + i, "true");
				serviciosForm.setValores("nombreSonria_" + i, servicio.getInformacionSonria().getNombre());
				serviciosForm.setValores("descripcionSonria_" + i, servicio.getInformacionSonria().getDescripcion());
			}
			else
			{
				serviciosForm.setValores("tieneInformacionSonria_" + i, "false");
				serviciosForm.setValores("nombreSonria_" + i, "");
				serviciosForm.setValores("descripcionSonria_" + i, "");
			}
			
			if (servicio.tieneInformacionSoat())
			{
				serviciosForm.setValores("tieneInformacionSoat_" +i, "true");
				serviciosForm.setValores("nombreSoat_" + i, servicio.getInformacionSoat().getNombre());
				serviciosForm.setValores("unidadesSoat_" + i, servicio.getUnidadesSoatFloat());
				serviciosForm.setValores("descripcionSoat_" + i, servicio.getInformacionSoat().getDescripcion());
			}
			else
			{
				serviciosForm.setValores("tieneInformacionSoat_" +i, "false");
				serviciosForm.setValores("nombreSoat_" + i, "");
				serviciosForm.setValores("unidadesSoat_" + i, "");
				serviciosForm.setValores("descripcionSoat_" + i, "");
			}
			
			serviciosForm.setValores("servicioPortatil_" +i, servicio.getDesServicioPortatil());
			serviciosForm.setValores("codServicioPortatil_" +i, servicio.getCodigoServicioPortatil());
			serviciosForm.setValores("codigoEstadarTarifario_" +i, servicio.getCodigoCups());
			serviciosForm.setValores("descripcionEstadarTarifario_"+i,servicio.getDesServicioPortatil());
			
			//Cambios Anexo 868
			serviciosForm.setValores("atencionodontologica_"+i,servicio.getAtencionOdonlogica());
			serviciosForm.setValores("convencion_"+i,servicio.getConvencion());
			serviciosForm.setValores("minutosduracion_"+i,servicio.getMinutosDuracion());
			serviciosForm.setValores("archivo_"+i,servicio.getArchivo());
			
			i++;
			
		}
		
		
	}

	/**
	 * Método que se encarga de llenar una forma de tipo ServiciosForm
	 * con todos los datos presentes en un objeto servicios, para estar
	 * visibles al usuario
	 * 
	 * @param serviciosForm 
	 * @param servicios
	 */
	private void llenarFormServicioRecienInsertado (ServiciosForm serviciosForm, Servicio servicio)
	{
		int i=0;
		serviciosForm.setValores("codigo_"+i, "" + servicio.getCodigo());
		serviciosForm.setValores("especialidad_"+i, "" + servicio.getEspecialidad().getCodigo());
		serviciosForm.setValores("sexo_"+i, "" + servicio.getRestriccionSexo().getCodigo());
		serviciosForm.setValores("naturalezaServicio_"+i, servicio.getNaturalezaServicio().getAcronimo());
		serviciosForm.setValores("tipoServicio_"+i, servicio.getTipoServicio().getAcronimo());
		serviciosForm.setValores("formulario_" + i, "" + servicio.getFormulario());
		serviciosForm.setValores("nivel_"+i, servicio.getNivel().getId());
		serviciosForm.setValores("esPos_" + i, "" + servicio.isEsPos());
		serviciosForm.setValores("esPosSubsidiado_" + i, "" + servicio.getPosSubsidiado());
		serviciosForm.setValores("activo_" + i, "" + servicio.getActivo());
		serviciosForm.setValores("unidadesUvr_"+i, ""+servicio.getUnidadesUvr());
		serviciosForm.setValores("grupoServicio_"+i, ""+servicio.getGrupoServicio().getCodigo());
		serviciosForm.setValores("requiereInterpretacion_"+i, ""+servicio.getRequiereInterpretacion());
		serviciosForm.setValores("requiereDiagnostico_"+i, ""+servicio.getRequiereDiagnostico());
		serviciosForm.setValores("costo_"+i, ""+servicio.getCosto());
		serviciosForm.setValores("realizaInstitucion_"+i, ""+servicio.getRealizaInstitucion());
	
		if (servicio.tieneInformacionCups())
		{
			serviciosForm.setValores("tieneInformacionCups_" + i, "true");
			serviciosForm.setValores("nombreCups_" + i, servicio.getInformacionCups().getNombre());
			serviciosForm.setValores("descripcionCups_" + i, servicio.getInformacionCups().getDescripcion());
		}
		else
		{
			serviciosForm.setValores("tieneInformacionCups_" + i, "false");
		}
			
		if (servicio.tieneInformacionISS())
		{
			serviciosForm.setValores("tieneInformacionISS_" + i, "true");
			serviciosForm.setValores("nombreISS_" + i, servicio.getInformacionISS().getNombre());
			serviciosForm.setValores("descripcionISS_" + i, servicio.getInformacionISS().getDescripcion());
		}
		else
		{
			serviciosForm.setValores("tieneInformacionISS_" + i, "false");
		}
			
		if (servicio.tieneInformacionSoat())
		{
			serviciosForm.setValores("tieneInformacionSoat_" +i, "true");
			serviciosForm.setValores("nombreSoat_" + i, servicio.getInformacionSoat().getNombre());
			serviciosForm.setValores("unidadesSoat_" + i, servicio.getUnidadesSoat());
			serviciosForm.setValores("descripcionSoat_" + i, servicio.getInformacionSoat().getDescripcion());
		}
		else
		{
			serviciosForm.setValores("tieneInformacionSoat_" +i, "false");
		}
		
		serviciosForm.setValores("servicioPortatil_" +i, servicio.getDesServicioPortatil());
		serviciosForm.setValores("codServicioPortatil_" +i, servicio.getCodigoServicioPortatil());
			
		//Cambios Anexo 868
		serviciosForm.setValores("atencionodontologica_"+i,servicio.getAtencionOdonlogica());
		serviciosForm.setValores("convencion_"+i,servicio.getConvencion());
		serviciosForm.setValores("minutosduracion_"+i,servicio.getMinutosDuracion());
		serviciosForm.setValores("archivo_"+i,servicio.getArchivo());

		
		
		i++;
	}
	
	/**
	 * Método que se encarga de llenar el objeto "servicios" con cada
	 * servicio existente y modificado por parte del usuario
	 * 
	 * @param serviciosForm Forma de donde se toman los datos
	 * ingresados por el usuario y/o previos
	 * @param servicios Objeto donde se almacenarán los servicios
	 * @param usuario 
	 */
	private void llenarObjeto(ServiciosForm serviciosForm, Servicios servicios, UsuarioBasico usuario)
	{
		int i = serviciosForm.getPos();
		
		
		Servicio servicio;
		int codigo=Integer.parseInt((String)serviciosForm.getValores("codigo_" + i));
	 	int especialidad=Integer.parseInt((String)serviciosForm.getValores("especialidad_" + i));
		int sexo=Integer.parseInt((String)serviciosForm.getValores("sexo_" + i));
		
		String nivel=serviciosForm.getValores("nivel_"+i).toString();
		String unidadesUvr= ((String)serviciosForm.getValores("unidadesUvr_" + i));
		String requiereInterpretacion= serviciosForm.getValores("requiereInterpretacion_"+i).toString();
		String costo= serviciosForm.getValores("costo_"+i).toString();
		String realizaInstitucion= serviciosForm.getValores("realizaInstitucion_"+i).toString();
		String desPortatil = serviciosForm.getValores("servicioPortatil_"+i).toString();
		int codigoPortatil = Utilidades.convertirAEntero(serviciosForm.getValores("codServicioPortatil_"+i)+"");
		String esPosSubsidiado=serviciosForm.getValores("esPosSubsidiado_" + i)+"";
		String codigoCups = serviciosForm.getValores("codigoCups_" + i)+"";
		String atencionOdontologica= serviciosForm.getValores("atencionodontologica_" + i)+"";
		int convencion=Utilidades.convertirAEntero(serviciosForm.getValores("convencion_" + i)+"");
		int minutosDuracion=Utilidades.convertirAEntero(serviciosForm.getValores("minutosduracion_" + i)+"");
		
		
		boolean esPos;
		if (    ((String)serviciosForm.getValores("esPos_" + i)).equals("true")   )
		{
			esPos=true;
		}
		else
		{
			esPos=false;
		}

		boolean activo;
		if (    ((String)serviciosForm.getValores("activo_" + i)).equals("true")   )
		{
			activo=true;
		}
		else
		{
			activo=false;
		}
		servicio=new Servicio(codigo, new InfoDatosInt (especialidad, ""), new InfoDatosInt(sexo, "") ,new InfoDatos((String)serviciosForm.getValores("tipoServicio_" + i), "") , new InfoDatos((String)serviciosForm.getValores("naturalezaServicio_" + i), "") ,  serviciosForm.getFormulario(), esPos,esPosSubsidiado, activo, Double.parseDouble(unidadesUvr), new InfoDatos(nivel, "", ""), serviciosForm.getRealizaInstitucion(), serviciosForm.getCosto(), serviciosForm.getRequiereInterpretacion(),serviciosForm.getRequiereDiagnostico(), codigoPortatil, desPortatil, codigoCups,usuario.getCodigoInstitucionInt(), atencionOdontologica,convencion, minutosDuracion,"");
		String parametro=(String)serviciosForm.getValores("nombreCups_" + i);
		String parametro2=(String)serviciosForm.getValores("descripcionCups_" + i);
		if (      (parametro!=null&&!parametro.equals("")) ||   (parametro2!=null&&!parametro2.equals("")) )
		{
			InfoDatos aGuardar=new InfoDatos();
			aGuardar.setNombre(parametro);
			aGuardar.setDescripcion((String)  serviciosForm.getValores("descripcionCups_" + i));
			parametro=(String)serviciosForm.getValores("activoCups_" + i);
			if (parametro==null||parametro.equals("false"))
			{
				aGuardar.setActivo(false);
			}
			else
			{
				aGuardar.setActivo(true);
			}
			servicio.setInformacionCups(aGuardar);
		}

		//********SE VERIFICA SI AL SERVICIO SE LE INGRESÓ INFORMACIÓN ISS *********************************************
		parametro=(String)serviciosForm.getValores("nombreISS_" + i);
		parametro2=(String)serviciosForm.getValores("descripcionISS_" + i);
		
		if (      (parametro!=null&&!parametro.equals("")) ||   (parametro2!=null&&!parametro2.equals("")) || (((String)serviciosForm.getValores("tieneInformacionISS_" + i)).equals("true")))
		{
			InfoDatos aGuardar=new InfoDatos();
			aGuardar.setNombre(parametro);
			aGuardar.setDescripcion((String)  serviciosForm.getValores("descripcionISS_" + i));
			parametro=(String)serviciosForm.getValores("activoISS_" + i);
			if (parametro==null||parametro.equals("false"))
			{
				aGuardar.setActivo(false);
			}
			else
			{
				aGuardar.setActivo(true);
			}
			servicio.setInformacionISS(aGuardar);
			servicio.setCentinelaIss(true);
		}
		//*********************************************************************************************************
		//*******SE VERIFICA SI AL SERVICIO SE LE INGRESÓ INFORMACIÓN SOAT************************************************
		parametro=(String)serviciosForm.getValores("nombreSoat_" + i);
		parametro2=(String)serviciosForm.getValores("descripcionSoat_" + i);
		float parametro3 = -1;
		try
		{
			parametro3=Float.parseFloat(serviciosForm.getValores("unidadesSoat_" + i)+"");
		}
		catch(Exception e)
		{
			parametro3 = -1;
		}
		if (      (parametro!=null&&!parametro.equals("")) ||   (parametro2!=null&&!parametro2.equals("")) ||   
				(parametro3>0)|| (((String)serviciosForm.getValores("tieneInformacionSoat_" + i)).equals("true")))
		{
			InfoDatos aGuardar=new InfoDatos();
			aGuardar.setNombre(parametro);
			aGuardar.setDescripcion((String)  serviciosForm.getValores("descripcionSoat_" + i));
			parametro=(String)serviciosForm.getValores("activoSoat_" + i);
			if (parametro==null||parametro.equals("false"))
			{
				aGuardar.setActivo(false);
			}
			else
			{
				aGuardar.setActivo(true);
			}
			servicio.setInformacionSoat(aGuardar);
			parametro=serviciosForm.getValores("unidadesSoat_" + i)+"";
			servicio.setUnidadesSoat(Utilidades.convertirADouble(parametro));
			servicio.setCentinelaSoat(true);
		}
		//*******************************************************************************************************
		//se añade el grupo del servicio
		servicio.setGrupoServicio(new InfoDatosInt(
			Integer.parseInt(serviciosForm.getValores("grupoServicio_"+i)+""),
			serviciosForm.getValores("nombreGrupoServicio_"+i)+""));
		
		servicios.anadirServicio(servicio);
//		Intentamos hacer la inserción
		servicio.setTarifariosMap(serviciosForm.getTarifariosMap());
		
	}
	
	/**
	 * Este método se encarga de manejar la funcionalidad necesaria
	 * (Que es practicamente comun entre actions) para manejar el
	 * estado cancelar
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param response response de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEstadoCancelar(HttpServletRequest request, HttpServletResponse response, Connection con, ServiciosForm serviciosForm) throws Exception
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug("La acción especificada es cancelar");
		}

		String paginaSiguiente=request.getParameter("paginaSiguiente");
		serviciosForm.reset();
		UtilidadBD.cerrarConexion(con);
		response.sendRedirect(paginaSiguiente);

		return null;
	}


	/**
	 * Conjunto de pasos necesarios para terminar la funcionalidad
	 * de insertar (Se ejecuta cuando el estado es salir y la accion
	 * a finalizar es insertar)
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @param medico 
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionSalirInsertar (ActionMapping mapping,HttpServletRequest request, HttpServletResponse response, Connection con, ServiciosForm serviciosForm, UsuarioBasico medico) throws Exception
	{
		//Llenamos los datos básicos del servicio
		//en un objeto de tipo Servicio
		logger.info("EL ARCHIVO------>"+serviciosForm.getArchivoImagen());
		
		Servicio servicio= new Servicio (-1, new InfoDatosInt (serviciosForm.getEspecialidad(), "especialidad"), new InfoDatosInt (serviciosForm.getSexo(), "sexo") , new InfoDatos(serviciosForm.getTipoServicio(), "Tipo Servicio") , new InfoDatos(serviciosForm.getNaturalezaServicio(), "Naturaleza Servicio") ,  serviciosForm.getFormulario(), serviciosForm.isEsPos(),serviciosForm.getPosSubsidiado(), true, Double.parseDouble(serviciosForm.getUnidadesUvr()), new InfoDatos (serviciosForm.getNivel()), serviciosForm.getRealizaInstitucion(), serviciosForm.getCosto(), serviciosForm.getRequiereInterpretacion(), serviciosForm.getRequiereDiagnostico(), serviciosForm.getCodigoServicioPortatil(), serviciosForm.getDesServicioPortatil(), "",medico.getCodigoInstitucionInt(), serviciosForm.getAtencionOdontologica(),serviciosForm.getConvencion(), Utilidades.convertirAEntero(serviciosForm.getMinutosDuracion()),serviciosForm.getArchivoImagen());		
		//se asigna el valor del grupo de servicio
		servicio.getGrupoServicio().setCodigo(serviciosForm.getGrupoServicio());
		
		//Si el usuario llenó el código de cups, lo
		//agregamos al servicio
		if (  (serviciosForm.getCodigoCups().trim()!=null&&!serviciosForm.getCodigoCups().equals(""))   || (serviciosForm.getDescripcionCups()!=null&&!serviciosForm.getDescripcionCups().equals(""))  )
		{
			InfoDatos cups=new InfoDatos();
			cups.setNombre(serviciosForm.getCodigoCups());
			cups.setDescripcion(serviciosForm.getDescripcionCups());
			//Es activo porque o si no para lo está metiendo
			cups.setActivo(true);
			servicio.setInformacionCups(cups);
		}

		
		//Si el usuario llenó el código de ISS, lo
		//agregamos al servicio
		if (   (serviciosForm.getCodigoISS().trim()!=null&&!serviciosForm.getCodigoISS().equals("")) ||  (serviciosForm.getDescripcionISS()!=null&&!serviciosForm.getDescripcionISS().equals(""))  )
		{
			InfoDatos iss=new InfoDatos();
			iss.setNombre(serviciosForm.getCodigoISS());
			iss.setDescripcion(serviciosForm.getDescripcionISS());
			//Es activo porque o si no para lo está metiendo
			iss.setActivo(true);
			servicio.setInformacionISS(iss);
		}

		//Si el usuario llenó el código de Soat, lo
		//agregamos al servicio
		if (   (serviciosForm.getCodigoSoat().trim()!=null&&!serviciosForm.getCodigoSoat().equals(""))   ||  (serviciosForm.getDescripcionSoat()!=null&&!serviciosForm.getDescripcionSoat().equals(""))  || (serviciosForm.getUnidadesSoat()!=null&&!serviciosForm.getUnidadesSoat().equals("")))
		{
			InfoDatos soat=new InfoDatos();
			soat.setNombre(serviciosForm.getCodigoSoat());
			soat.setDescripcion(serviciosForm.getDescripcionSoat());
			//Es activo porque o si no para lo está metiendo
			soat.setActivo(true);
			servicio.setInformacionSoat(soat);
			servicio.setUnidadesSoat(Double.parseDouble(serviciosForm.getUnidadesSoat()));
		}
		
		Utilidades.imprimirMapa(serviciosForm.getTarifariosMap());
		
		
		int parametro=0;
		
		ActionErrors errores= new ActionErrors();
		
		if (Utilidades.convertirAEntero(serviciosForm.getMinutosDuracion()) > 0)
		{
			parametro = Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(medico.getCodigoInstitucionInt()));
			if((Utilidades.convertirAEntero(serviciosForm.getMinutosDuracion())%parametro) > 0)
				errores.add("prompt.generico", new ActionMessage("prompt.generico", "La duración del servicio debe ser multiplo del parametro Múltiplo en minutos para generación de citas ("+parametro+")"));
		}
			
		if(!errores.isEmpty())
		{			
			saveErrors(request, errores);
			return mapping.findForward("principal");
		}
		else
		{
		
			//Intentamos hacer la inserción
			servicio.setTarifariosMap(serviciosForm.getTarifariosMap());
			try
			{
				servicio.insertar(con);
			}
			catch (Exception e)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				logger.error("Error al insertar el Servicios (ServiciosAction) " + e.toString() );
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
					
			//Si llegamos a este punto la inserción fué exitosa
			serviciosForm.reset();
			serviciosForm.setEstado("empezar");
			//serviciosForm.setTarifariosMap(servicio.getTarifariosMap());
			String paginaSiguiente=request.getParameter("paginaSiguiente");
			
			if (paginaSiguiente==null||paginaSiguiente.equals(""))
			{
				this.llenarFormServicioRecienInsertado(serviciosForm, servicio);
				serviciosForm.setNumeroServicios(1);
	
				serviciosForm.setTarifariosMap(Servicio.consultarTarifarios(con, servicio.getCodigo()));
				serviciosForm.setFormulario(Servicio.cargarFormulariosServicio(con, servicio.getCodigo(), medico.getCodigoInstitucionInt()));
				
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else
			{
	
				serviciosForm.reset();
				response.sendRedirect(paginaSiguiente);
				UtilidadBD.cerrarConexion(con);
				return null;
			}
		}
	}
	
	/**
	 * Conjunto de pasos que se realizan cuando el usuario quiere 
	 * ver el resumen de servicios existentes en la aplicación
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @param medico
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionResumen (ActionMapping mapping,HttpServletRequest request, Connection con, ServiciosForm serviciosForm, UsuarioBasico medico) throws Exception
	{
		//Limpiamos la forma
		serviciosForm.reset();
		serviciosForm.setTarifariosMap(Servicio.consultarTarifarios(con, ConstantesBD.codigoNuncaValido));
		//Se crea el objeto servicios y se carga de la fuente de datos
		Servicios servicios = new Servicios();
		servicios.setCodigoInstitucion(medico.getCodigoInstitucionInt());
		
		try
		{
			//Si llegan restricciones por request
			if (request.getAttribute("restriccionesServicios")!=null)
				servicios.cargarRestringido(con, ((BeanBusquedaServicio) request.getAttribute("restriccionesServicios")));
			else
				servicios.cargar(con);
		}
		catch (Exception e)
		{
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			logger.error("Error al cargar los Servicios (ServiciosAction) " + e.toString() );
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		//Se llena el form con los datos de los servicios
		this.llenarForm(serviciosForm, servicios);
		  
		//Se guarda el número de servicios en la forma
		serviciosForm.setNumeroServicios(servicios.getNumServicios());
		  
		//Se asigna el número de registros permitidos por página
		serviciosForm.setNumeroElementosMostrar(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(medico.getCodigoInstitucionInt())));
		  
		serviciosForm.setIndiceNavegacion(0);
		if (serviciosForm.getNumeroElementosMostrar()>servicios.getNumServicios())
			serviciosForm.setSiguienteIndiceNavegacion(servicios.getNumServicios());
		else
			serviciosForm.setSiguienteIndiceNavegacion(serviciosForm.getNumeroElementosMostrar());
		  
		//Ponemos el atributo en request para poder
		//dibujarlo
		request.setAttribute("servicios", servicios);
		UtilidadBD.cerrarConexion(con);
		serviciosForm.setAccionAFinalizar("resumen");
		return mapping.findForward("resumen");
	}

	
	/**
	 * Conjunto de pasos que se realizan cuando el usuario entra por primera vez a
	 * insertar
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezarInsertar(ActionMapping mapping, Connection con, ServiciosForm serviciosForm) throws Exception
	{
		//Se limpian los datos anteriores
		serviciosForm.reset();
		serviciosForm.setTarifariosMap(Servicio.consultarTarifarios(con, ConstantesBD.codigoNuncaValido));

		
		UtilidadBD.cerrarConexion(con);
		serviciosForm.setEstado("salir");
		//Se crea el objeto servicios y se carga de la fuente de datos
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Conjunto de pasos que se realizan cuando el usuario entra por primera vez a
	 * insertar
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezarInsertarOdontologia(ActionMapping mapping, Connection con, ServiciosForm serviciosForm) throws Exception
	{
		//Se limpian los datos anteriores
		//serviciosForm.reset();
		serviciosForm.setTarifariosMap(Servicio.consultarTarifarios(con, ConstantesBD.codigoNuncaValido));

		
		UtilidadBD.cerrarConexion(con);
		serviciosForm.setEstado("salir");
		//Se crea el objeto servicios y se carga de la fuente de datos
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Conjunto de pasos que se realizan cuando el usuario entra por primera vez a
	 * modificar
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @param medico
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezarModificar(ActionMapping mapping,HttpServletRequest request, Connection con, ServiciosForm serviciosForm, UsuarioBasico medico) throws Exception
	{
		//Se limpian los datos anteriores
		serviciosForm.reset();
		serviciosForm.setTarifariosMap(Servicio.consultarTarifarios(con, ConstantesBD.codigoNuncaValido));

		//Se crea el objeto servicios y se carga de la fuente de datos
		Servicios servicios=new Servicios();
		servicios.setCodigoInstitucion(medico.getCodigoInstitucionInt());
		
		try
		{
			//Si llegan restricciones por request
			
			if (request.getAttribute("restriccionesServicios")!=null)
			{ 
				//Utilizamos el método de servicios No usados, para que valide
				//si el servicio ha sido usado o no
				servicios.cargarRestringidoServiciosNoUsados(con, ((BeanBusquedaServicio) request.getAttribute("restriccionesServicios")));
			}
			else
			{
				servicios.cargar(con);
			}
		}
		catch (Exception e)
		{
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			logger.error("Error al cargar los Servicios (ServiciosAction) " + e.toString() );
			e.printStackTrace();
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		
		//Se llena el form con los datos de los servicios
		this.llenarForm(serviciosForm, servicios);
		//Se guarda el número de servicios en la forma
		serviciosForm.setNumeroServicios(servicios.getNumServicios());
		//se asigna el número de registros en el listado
		serviciosForm.setNumeroElementosMostrar(
			Integer.parseInt(ValoresPorDefecto.getMaxPageItems(medico.getCodigoInstitucionInt())));

		serviciosForm.setIndiceNavegacion(0);
		if (serviciosForm.getNumeroElementosMostrar()>servicios.getNumServicios())
		{
			serviciosForm.setSiguienteIndiceNavegacion(servicios.getNumServicios());
		}
		else
		{
			serviciosForm.setSiguienteIndiceNavegacion(serviciosForm.getNumeroElementosMostrar());
		}
		
		UtilidadBD.cerrarConexion(con);
		serviciosForm.setEstado("salir");
		return mapping.findForward("principalModificar");
	}


	/**
	 * Conjunto de pasos que se realizan cuando el usuario 
	 * lleno los datos a modificar y oprimio el boton guardar 
	 * (o selecciono un link que lo lleva a otro sitio de la 
	 * aplicación y lleno el popup con la opción salir)
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionSalirModificar (ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, Connection con, ServiciosForm serviciosForm, UsuarioBasico usuario) throws Exception
	{
		Servicios servicios= new Servicios();
		servicios.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		//Llenamos el objeto con lo ingresado por el usuario
		this.llenarObjeto(serviciosForm, servicios,usuario);
		String log="";
		int band=0;
		int i = serviciosForm.getPos();
		String usr = usuario.getLoginUsuario();
		
		Utilidades.imprimirMapa(serviciosForm.getValores());
			
		int parametro=0;
		
		ActionErrors errores= new ActionErrors();
		
		if (Utilidades.convertirAEntero(serviciosForm.getValores("minutosduracion_"+i)+"") > 0)
		{
			parametro = Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
			if((Utilidades.convertirAEntero(serviciosForm.getValores("minutosduracion_"+i)+"")%parametro) > 0)
				errores.add("prompt.generico", new ActionMessage("prompt.generico", "La duración del servicio debe ser multiplo del parametro Múltiplo en minutos para generación de citas ("+parametro+")"));
		}
			
		if(!errores.isEmpty())
		{			
			saveErrors(request, errores);
			return mapping.findForward("detalle");
		}
		else
		{
			//Intentamos hacer la modificación
			try
			{
				band=servicios.modificar(con);
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger,"Error al modificar el Servicios (ServiciosAction) ", "errors.problemasBd", true);
			}
			if(band>0)
			{
				//se carga nuevamente el servicio
				String[] criterioBusqueda = {"codigoAxioma"};
				
				BeanBusquedaServicio bean = new BeanBusquedaServicio();
				bean.setCriteriosBusqueda(criterioBusqueda);
				bean.setCodigoAxioma(Integer.parseInt(serviciosForm.getValores("codigo_" + i)+""));
				
				servicios.cargarRestringido(con, bean);
				
				Iterator iterador=servicios.getIteradorServicios();
				while (iterador.hasNext())
				{
					Servicio serv=(Servicio)iterador.next();
					String portatilAsociado = "";
					
					if(serv.getCodigoServicioPortatil() != ConstantesBD.codigoNuncaValido)
						portatilAsociado = serv.getDesServicioPortatil();
					else
						portatilAsociado = "No maneja Portatil Asociado";
					
					serviciosForm.setFormulario(serv.getFormulario());
					
					log=log+
						"\n            ====INFORMACION MODIFICADA===== " +
						"\n*  Código Servicio ["+serv.getCodigo()+"] "+
						"\n*  Especialidad Servicio ["+serv.getEspecialidad().getNombre()+"]"+
						"\n*  Es activo ["+(serv.getActivo()?"Sí":"No")+"] "+
						"\n*  Tipo Servicio ["+serv.getTipoServicio().getValue()+"] "+
						"\n*  Naturaleza Servicio ["+serv.getNaturalezaServicio().getValue()+"] "+
						"\n*  Codigo Cups ["+serv.getInformacionCups().getNombre()+"] "+
						"\n*  Descripción Cups ["+serv.getInformacionCups().getDescripcion()+"] "+
						"\n*  Codigo ISS ["+serv.getInformacionISS().getNombre()+"] "+
						"\n*  Descripción ISS ["+serv.getInformacionISS().getDescripcion()+"] "+
						"\n*  Codigo SOAT ["+serv.getInformacionSoat().getNombre()+"] "+
						"\n*  Descripción SOAT ["+serv.getInformacionSoat().getDescripcion()+"] "+
						"\n*  Unidades UVR ["+serv.getUnidadesUvr()+"] "+
						"\n*  Unidades SOAT ["+serv.getUnidadesSoat()+"] "+
						"\n*  Sexo ["+serv.getRestriccionSexo().getNombre()+"] "+
						"\n*  Grupo Servicio ["+serv.getGrupoServicio().getNombre()+"] "+
						"\n*  Es pos ["+(serv.isEsPos()?"Sí":"No")+"] "+
						"\n*  Es pos Subsidiado ["+(serv.getPosSubsidiado())+"] ";
					if(Utilidades.convertirAEntero(serv.getFormulario().get("numRegistros")+"")>0)
					{
						log += "\n*  Formulario: ";
						for(int j=0;j<Utilidades.convertirAEntero(serv.getFormulario().get("numRegistros")+"");j++)
							log += "\n      "+serv.getFormulario().get("nombrePlantilla_"+j)+(UtilidadTexto.isEmpty(serv.getFormulario().get("nombreDiagnostico_"+j)+"")?"":" - "+serv.getFormulario().get("nombreDiagnostico_"+j));
					}
					log +=	"\n*  Nivel ["+serv.getNivel().getDescripcion()+"] "+
						"\n*  Requiere Interpretacion ["+serv.getRequiereInterpretacion()+"] "+
						"\n*  Costo ["+serv.getCosto()+"] "+
						"\n*  Se realiza en la Institucion ["+serv.getRealizaInstitucion()+"] "+
						"\n*  Requiere Diagnostico ["+ValoresPorDefecto.getIntegridadDominio(serv.getRequiereDiagnostico())+"] "+
						"\n*  Portatil Asociado ["+portatilAsociado+"] "+
						"\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+
						"";
				}
				log=serviciosForm.getLogInfoOriginalServicio()+log;
				LogsAxioma.enviarLog(ConstantesBD.logServicioCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usr);
				
			}
			
			
			UtilidadBD.cerrarConexion(con);
			serviciosForm.setEstado("empezar");
			
			String paginaSiguiente=request.getParameter("paginaSiguiente");
			if (paginaSiguiente==null||paginaSiguiente.equals(""))
			{
				return mapping.findForward("resumen");
			}
			else
			{
				serviciosForm.reset();
				response.sendRedirect(paginaSiguiente);
				return null;
			}
		}		
	}
	
	
	/* **************************warning***********************/

	/*private boolean indicesValidos (int indice1, int indice2, int numeroElementos)
	{
		if (indice1>=0&&indice2>=0&&indice1<=numeroElementos&&indice2<=numeroElementos)
		{
			return true;
		}
		else
		{
			return false;
		}
	}*/ 
	
	/**
	 * Este método me genera los indices que se utilizan al empezar a
	 * recorrer los servicios de derecha a izquierda 
	 * 
	 * @param numeroElementosMostrar Número de datos a mostrar
	 * en la página
	 * @param numeroElementos Número de elementos (Servicios)
	 * @return
	 */
	private int[] generarIndicesFinal (int numeroElementosMostrar, int numeroElementos)
	{
		int respuesta[]=new int[2];
		
		respuesta[1]=numeroElementos;
		if (numeroElementosMostrar<=numeroElementos)
		{
			respuesta[0]=numeroElementos-numeroElementosMostrar;
		}
		else
		{
			respuesta[0]=0;
		}
		
		return respuesta;
		
	}
	
	/**
	 * Este método me genera los indices que se utilizan al empezar a
	 * recorrer los servicios de izquierda a derecha
	 * 
	 * @param numeroElementosMostrar Número de datos a mostrar
	 * en la página
	 * @param numeroElementos Número de elementos (Servicios)
	 * @return
	 */
	private int[] generarIndicesPrincipio (int numeroElementosMostrar, int numeroElementos)
	{
		int respuesta[]=new int[2];
		
		respuesta[0]=0;
		if (numeroElementosMostrar<=numeroElementos)
		{
			respuesta[1]=numeroElementosMostrar;
		}
		else
		{
			respuesta[1]=numeroElementos;
		}
		
		return respuesta;
		
	}

	/**
	 * Conjunto de pasos que se realizan cuando el usuario está navegando 
	 * entre los servicios (sea modificando o solo mirandolos)
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param con Conexión con la fuente de datos
	 * @param serviciosForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionNavegar  (ActionMapping mapping,Connection con, ServiciosForm serviciosForm) throws Exception
	{

		int numeroElementosMostrar=serviciosForm.getNumeroElementosMostrar();
		int numeroElementos=serviciosForm.getNumeroServicios();

		int indiceActual=serviciosForm.getIndiceNavegacion();
		int siguienteIndiceActual=serviciosForm.getSiguienteIndiceNavegacion();
		
		//Hay dos posibles casos, si el usuario quiere ir a los registros
		//anteriores o si quiere ir a  los siguientes, aunque esta func.
		//trabaja con modificar y resumen, la única diferencia entre estos
		//es la página a la que nos dirigimos 
		if (serviciosForm.getAccionAFinalizar().equals("atrasModificar")||serviciosForm.getAccionAFinalizar().equals("atrasResumen"))
		{
			serviciosForm.setSiguienteIndiceNavegacion(serviciosForm.getIndiceNavegacion());
			serviciosForm.setIndiceNavegacion(serviciosForm.getSiguienteIndiceNavegacion() - numeroElementosMostrar);
					
			//El unico error que puede pasar, como todo estaba bien, es que me pasara
			//por detras en el indice actual (<0)
					
			if (serviciosForm.getIndiceNavegacion()<0)
			{
				serviciosForm.setIndiceNavegacion(0);
				serviciosForm.setSiguienteIndiceNavegacion(indiceActual);
			}
					
			//Si salio algo mal es porque nos pasamos por detras
			if (serviciosForm.getIndiceNavegacion()==serviciosForm.getSiguienteIndiceNavegacion())
			{
				int resp[]=this.generarIndicesFinal(numeroElementosMostrar, numeroElementos);
				serviciosForm.setIndiceNavegacion(resp[0]);
				serviciosForm.setSiguienteIndiceNavegacion(resp[1]);
			}
		}
		else
			if(serviciosForm.getAccionAFinalizar().equals("adelanteModificar")||serviciosForm.getAccionAFinalizar().equals("adelanteResumen"))
				{
					serviciosForm.setIndiceNavegacion(serviciosForm.getSiguienteIndiceNavegacion());
					serviciosForm.setSiguienteIndiceNavegacion(serviciosForm.getIndiceNavegacion() + numeroElementosMostrar);
							
					if (serviciosForm.getSiguienteIndiceNavegacion()>numeroElementos)
					{
						serviciosForm.setSiguienteIndiceNavegacion(numeroElementos);
						serviciosForm.setIndiceNavegacion(siguienteIndiceActual);
					}
							
					if (serviciosForm.getIndiceNavegacion()==serviciosForm.getSiguienteIndiceNavegacion())
					{
						int resp[]=this.generarIndicesPrincipio(numeroElementosMostrar, numeroElementos);
						serviciosForm.setIndiceNavegacion(resp[0]);
						serviciosForm.setSiguienteIndiceNavegacion(resp[1]);
					}
				}//if adelanteModificar o adelanteResumen
		else
			if (serviciosForm.getAccionAFinalizar().equals("inicioModificar")||serviciosForm.getAccionAFinalizar().equals("inicioResumen"))
			{
				serviciosForm.setSiguienteIndiceNavegacion(numeroElementosMostrar);
				serviciosForm.setIndiceNavegacion(0);
			}
		else
			if (serviciosForm.getAccionAFinalizar().equals("finalModificar")||serviciosForm.getAccionAFinalizar().equals("finalResumen"))
			{
				serviciosForm.setIndiceNavegacion(numeroElementos-numeroElementosMostrar);
				serviciosForm.setSiguienteIndiceNavegacion(numeroElementos);
			}
			
		UtilidadBD.cerrarConexion(con);
		if (serviciosForm.getAccionAFinalizar().equals("atrasModificar")||serviciosForm.getAccionAFinalizar().equals("adelanteModificar") 
			|| serviciosForm.getAccionAFinalizar().equals("inicioModificar") || serviciosForm.getAccionAFinalizar().equals("finalModificar")
			|| serviciosForm.getAccionAFinalizar().equals("modificar"))
		{
			return mapping.findForward("principalModificar");
		}
		else
		{
			return mapping.findForward("resumen");
		}

	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}

}
