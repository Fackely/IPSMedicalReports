package com.princetonsa.action.consultaExterna;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.UtilidadBD;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.consultaExterna.AnulacionCondonacionMultasForm;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.AnulacionCondonacionMultas;


public class AnulacionCondonacionMultasAction extends Action{

	Logger logger = Logger.getLogger(AnulacionCondonacionMultasAction.class);
	AnulacionCondonacionMultas mundo;    
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {		

		Connection con = null;
		try {		
			if(response == null);

			if (form instanceof AnulacionCondonacionMultasForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{ 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd"); 
					logger.info("NO PUDO EMPEZAR PAGINA ANULAR CONDONAR MULTA........>>>>  ");
					return mapping.findForward("paginaError");

				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");


				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo= new AnulacionCondonacionMultas();
				AnulacionCondonacionMultasForm forma = (AnulacionCondonacionMultasForm)form;		
				String estado = forma.getEstado(); 
				logger.info("estado-->"+forma.getEstado());
				if(estado.equals("empezar"))
				{
					forma.reset();
					ActionForward forward = new ActionForward();
					forward=accionValidarPermisosUsuario(con, forma, paciente, usuario, request, mapping);
					if(forward != null)
						return forward;	
					UtilidadBD.closeConnection(con);		
					return mapping.findForward("principal");		
				}  
				else
					if (estado.equals("consultaPaciente"))
					{    
						forma.reset();
						ActionForward forward = new ActionForward();
						forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
						if(forward != null)
							return forward;	

						forma.setParametrosFiltros("operacionExitosa",ConstantesBD.codigoNuncaValido);
						return citasconMultasAsociadasalPaciente(con,forma, paciente, usuario, request, mapping);
					}
					else
						if (estado.equals("ordenar"))
						{
							UtilidadBD.closeConnection(con);

							return ordenarXColumna(forma, mapping);
						}
						else
							if (estado.equals("ordenarRango"))
							{     
								UtilidadBD.closeConnection(con); 
								return ordenarXColumnaRango(forma, mapping);
							} 
							else
								if (estado.equals("anularcondonarmulta"))
								{    
									forma.setErrorPermisos(ConstantesBD.acronimoSi);
									forma.setMotivosAnulacionCondonacion(UtilidadesConsultaExterna.consultaMotivosAnulacionCondonacionMulta(con, usuario.getCodigoInstitucionInt()));
									UtilidadBD.cerrarConexion(con);	
									DtoMultasCitas dtoMultas = (DtoMultasCitas)forma.getArrayCitasconMulta().get(forma.getPosMulta());
									if(!dtoMultas.isAnular() && !dtoMultas.isCondonar())
									{  
										errores.add("descripcion",new ActionMessage("errors.notEspecific","Usuario no tiene Permisos para Modificar Estados de Multas."));
										saveErrors(request,errores);
										forma.setErrorPermisos(ConstantesBD.acronimoNo);					   
										return mapping.findForward("anularCondonar");  

									}else
									{
										return mapping.findForward("anularCondonar");  
									}
								}
								else
									if (estado.equals("guardar"))
									{
										forma.setParametrosFiltros("operacionExitosa",ConstantesBD.codigoNuncaValido); 
										errores = accionValidarDatosRequeridos(forma);

										if(!errores.isEmpty())
										{
											saveErrors(request,errores);
											UtilidadBD.closeConnection(con);		
											return mapping.findForward("anularCondonar");						   
										}
										else
										{						  
											return guardarMulta(con, forma, usuario, request, mapping);
										}				
									}  else
										if(estado.equals("consultarRango"))
										{
											forma.reset();
											forma.setParametrosFiltros("operacionExitosa",ConstantesBD.codigoNuncaValido);  
											return citasconMultasRango(con,forma, paciente, usuario, request, mapping);

										}
										else
											if(estado.equals("buscarMultasCita"))
											{
												UtilidadBD.closeConnection(con);
												forma.setParametrosFiltros("operacionExitosa",ConstantesBD.codigoNuncaValido); 
												return busquedaCitasconMultaRango(forma, paciente, usuario, request, mapping);

											}		
											else
												if(estado.equals("filtrarUnidadesAgenda"))
												{
													accionFiltrarUnidadAgenda(con, forma,response);
													UtilidadBD.closeConnection(con);	
													return mapping.findForward("rango");	

												}	 
												else if (estado.equals("redireccion"))
												{
													UtilidadBD.closeConnection(con);
													response.sendRedirect(forma.getLinkSiguiente());
													return null;
												}

			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	


/**
 * 
 * @param con
 * @param forma
 * @param request
 * @param mapping
 */
private ActionForward guardarMulta(Connection con,AnulacionCondonacionMultasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) 
{
	 HashMap resultado = new HashMap();
	 int multa = Utilidades.convertirAEntero(forma.getMultaFiltro());
	 String estadoMulta=forma.getEstadoMulta();
	 logger.info("estadoMulta "+estadoMulta);
	 int motivo=Utilidades.convertirAEntero(forma.getCodMotivoAnulacionCondonacion());
	 String observaciones= forma.getObservaciones();
	 logger.info("observaciones-->"+forma.getObservaciones());
	 String loginUsuario= usuario.getLoginUsuario();
	 
	 UtilidadBD.iniciarTransaccion(con);
	 resultado=AnulacionCondonacionMultas.guardarMultaCita(con, multa, estadoMulta, motivo, observaciones, loginUsuario);
	 
	 
	 if(Utilidades.convertirAEntero(resultado.get("consecutivomulta").toString())>0)
	  {
   	    forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
   	    forma.setParametrosFiltros("mensaje","Proceso Exitoso ");
   	    UtilidadBD.finalizarTransaccion(con);
	  }	 
      else
		{
        UtilidadBD.abortarTransaccion(con);

        forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
        forma.setParametrosFiltros("mensaje","Proceso No Exitoso");
        ActionErrors errores = (ActionErrors)resultado.get("error");
        saveErrors(request, errores);	
		
		}
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("anularCondonar");
 }



/**
 * Metodo que realiza la validacion del Paciente .......debe estar cargado en sesion y tener habilitados los permisos para Anular y Condonar Multas	
 * @param con
 * @param forma
 * @param paciente
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
 private ActionForward accionValidarPaciente(Connection con,AnulacionCondonacionMultasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}
		UtilidadBD.closeConnection(con);
		return null;
		
	}
 
 /**
  * 
  * @param con
  * @param forma
  * @param paciente
  * @param usuario
  * @param request
  * @param mapping
  * @return
  */
 private ActionForward accionValidarPermisosUsuario(Connection con,AnulacionCondonacionMultasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
 {
	 if(mundo.validarPermisos(con, usuario.getLoginUsuario()).equals(ConstantesBD.acronimoNo))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Usuario no tiene Permisos para Modificar Estados de Multas", false);
		}
	 UtilidadBD.closeConnection(con);
	 return null;
 }
 
 
 private ActionErrors accionValidarDatosRequeridos(AnulacionCondonacionMultasForm forma)
 {

	ActionErrors errores = new ActionErrors(); 
	
	 if(forma.getCodMotivoAnulacionCondonacion().equals(""))
	 {
			errores.add("Campo Motivo", new ActionMessage("errors.required","El campo Motivo de Anulacion/Condonacion "));
	 }
	 if(forma.getConfirmarCondonar().equals(ConstantesBD.acronimoNo) && forma.getConfirmarAnular().equals(""))
	  {
		 errores.add("Seleccion Estado Condonar", new ActionMessage("errors.required","Seleccionar el estado Condonar  "));
	  }
      if( forma.getConfirmarAnular().equals(ConstantesBD.acronimoNo) && forma.getConfirmarCondonar().equals(""))
	  {
    	  errores.add("Seleccion Estado Anular", new ActionMessage("errors.required","Seleccionar el estado Anular "));
	  }
	  
      if(forma.getConfirmarAnular().equals(ConstantesBD.acronimoNo) && forma.getConfirmarCondonar().equals(ConstantesBD.acronimoNo) || forma.getConfirmarAnular().equals("") && forma.getConfirmarCondonar().equals(""))
	  {
    	  errores.add("Seleccion Estado", new ActionMessage("errors.required","Seleccionar el estado "));
	  }
      
      if(errores.isEmpty())
    	  logger.info("errores vacio ");
      
	     return errores;
	 
 }
 
 /**
  * Metodo para cargar las citas con multas Asociadas al paciente en estado Generada
  * @param con
  * @param forma
  * @param paciente
  * @param usuario
  * @param request
  * @param mapping
  * @return
  */
 private ActionForward citasconMultasAsociadasalPaciente(Connection con,AnulacionCondonacionMultasForm forma,PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
 {    
	 ArrayList actividades=new ArrayList();
	 actividades.add(ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas);
	 actividades.add(ConstantesBD.codigoActividadAutorizadaAnularMultasCitas);
	 forma.setUnidadesAgendaStr(mundo.unidadesAgendaStr(usuario.getLoginUsuario(), actividades));
	 logger.info("\n\n Unidades de Agenda  >>>>"+forma.getUnidadesAgendaStr());	 
	 forma.setArrayCitasconMulta(mundo.obtenerCitasconMultas(paciente.getCodigoPersona(),usuario.getLoginUsuario(),forma.getUnidadesAgendaStr())); 
	     if(forma.getArrayCitasconMulta().size()==0)
	     {
	    	 forma.setArrayCitasconMulta(mundo.obtenerCitasMultasSinPermisos(paciente.getCodigoPersona(),usuario.getLoginUsuario()));
	    	 if(forma.getArrayCitasconMulta().size()>0)
	    	 {   
	    		 UtilidadBD.closeConnection(con);
	    		 return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "Usuario no tiene Permisos para Modificar Estados de Multas", false);
	    	 }
	     }  
	     
	  UtilidadBD.closeConnection(con);
 	  return mapping.findForward("paciente");
 }
	
 
 /**
  * Metodo para ordenar por columna la busqueda por paciente
  * @param con
  * @param forma
  * @param mapping
  * @return
  */
 private ActionForward ordenarXColumna(AnulacionCondonacionMultasForm forma,ActionMapping mapping )
 {
	  ArrayList<DtoMultasCitas> array= new ArrayList<DtoMultasCitas>();
	  logger.info ("\n\n Tamaño Lista MULTAS  >> "+forma.getArrayCitasconMulta().size());
	  array=mundo.ordenarColumna(forma.getArrayCitasconMulta(),forma.getUltimaPropiedad(), forma.getPropiedadOrdenar());
	  forma.setUltimaPropiedad(forma.getPropiedadOrdenar());
	  forma.resetArrayMultas();
	  forma.setArrayCitasconMulta(array);	
	  return mapping.findForward("paciente");
 }
 
 /**
  * Meotodo para ordenar por columna..la busqueda por rango
  * @param forma
  * @param mapping
  * @return
  */
 private ActionForward ordenarXColumnaRango(AnulacionCondonacionMultasForm forma,ActionMapping mapping )
 {
	  ArrayList<DtoMultasCitas> array= new ArrayList<DtoMultasCitas>();
	  logger.info ("\n\n Tamaño Lista MULTAS  >> "+forma.getArrayCitasconMulta().size());
	  array=mundo.ordenarColumna(forma.getArrayCitasconMulta(),forma.getUltimaPropiedad(), forma.getPropiedadOrdenar());
	  forma.setUltimaPropiedad(forma.getPropiedadOrdenar());
	  forma.resetArrayMultas();
	  forma.setArrayCitasconMulta(array);	
	  return mapping.findForward("resultadoRango");
 }
 
 /**
  * 
  * @param con
  * @param forma
  * @param paciente
  * @param usuario
  * @param request
  * @param mapping
  * @return
  */
 private ActionForward citasconMultasRango(Connection con,AnulacionCondonacionMultasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
	      forma.setParametrosBusqueda(mundo.inicializarParametrosBusquedaRango(usuario.getCodigoCentroAtencion()));	
	      forma.setListadoCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
	      forma.setListadoUnidadesAgenda(mundo.unidadesAgendaCentrosAtencion(forma.getListadoCentroAtencion()));
	      forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, false, true,ConstantesBD.codigoNuncaValido));	      
	      Utilidades.imprimirMapa(forma.getListadoCentroAtencion());
	      forma.setListadoConvenio(Utilidades.obtenerConvenios(con, "", "", false, "", false)); 
	      
	      UtilidadBD.closeConnection(con);	
		  return mapping.findForward("rango");
	}

 
/**
 * 
 * @param con
 * @param forma
 * @param paciente
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
 private ActionForward busquedaCitasconMultaRango(AnulacionCondonacionMultasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
 		
 		
	    ActionErrors errores = new ActionErrors();
		errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());		
		
		if(errores.isEmpty())
		{
	    HashMap parametros=new HashMap();
 		parametros.put("fechaInicialGeneracion",forma.getParametrosBusqueda("fechaInicialGeneracion"));
 		parametros.put("fechaFinalGeneracion",forma.getParametrosBusqueda("fechaFinalGeneracion"));
 		parametros.put("centroatencion",forma.getParametrosBusqueda("centroatencion"));
 		parametros.put("unidadagenda",forma.getParametrosBusqueda("unidadagenda"));
 		parametros.put("convenio",forma.getParametrosBusqueda("convenio"));
 		parametros.put("profesional",forma.getParametrosBusqueda("profesional"));
 		parametros.put("estadocita",forma.getParametrosBusqueda("estadocita"));
 		parametros.put("mapacentrosatencion",(HashMap)forma.getListadoCentroAtencion());
 		parametros.put("mapaunidadesagenda",(HashMap)forma.getListadoUnidadesAgenda());
 		
 		forma.setArrayCitasconMulta(mundo.busquedaPorRangoMultasCita(usuario.getLoginUsuario(), parametros));
 		return mapping.findForward("resultadoRango");
		}
		else
		{		
			saveErrors(request, errores);	
			return mapping.findForward("rango");
		}
		
 	
 	}
 
 
 
 
 private ActionForward accionFiltrarUnidadAgenda(Connection con,AnulacionCondonacionMultasForm form,HttpServletResponse response) 
	{
		logger.info(" >>>  ENTRO A FILTRAR UNIDADES AGENDA    ");
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>unidadagenda</id-select>" +
				"<id-arreglo>unidades</id-arreglo>" +
			"</infoid>" ;
		
		if(!form.getCentroAtencionFiltro().equals(""))
		{
			if(form.getCentroAtencionFiltro().equals("*"))
			{
			     form.setListadoUnidadesAgenda(mundo.unidadesAgendaCentrosAtencion(form.getListadoCentroAtencion()));
			}else
			{
				form.setListadoUnidadesAgenda(mundo.unidadesAgendaXCentroAtencion(Utilidades.convertirAEntero(form.getCentroAtencionFiltro().toString())));
			}
	    }
		resultado += "<unidades>";
		resultado += "<codigo>*</codigo>";
		resultado += "<descripcion>Todos</descripcion>";
		resultado += "</unidades>";	
		
		for(int i=0; i< Utilidades.convertirAEntero(form.getListadoUnidadesAgenda().get("numRegistros")+"");i++)
		{
				resultado += "<unidades>";
				resultado += "<codigo>"+form.getListadoUnidadesAgenda().get("codigo_"+i)+"</codigo>";
				resultado += "<descripcion>"+form.getListadoUnidadesAgenda().get("nombre_"+i)+"</descripcion>";
			    resultado += "</unidades>";	
			
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarUnidadesAgenda: "+e);
		}
		return null;
	}

 
	
}
