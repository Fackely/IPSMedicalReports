package com.princetonsa.action.manejoPaciente;

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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ProrrogarAnularAutorizacionesEntSubcontratadasForm;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ConsultarImprimirAutorizacionesEntSubcontratadas;
import com.princetonsa.mundo.manejoPaciente.ProrrogarAnularAutorizacionesEntSubcontratadas;

public class ProrrogarAnularAutorizacionesEntSubcontratadasAction  extends Action {

	Logger logger = Logger.getLogger(ProrrogarAnularAutorizacionesEntSubcontratadasAction.class);
	ProrrogarAnularAutorizacionesEntSubcontratadas  mundo;
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof ProrrogarAnularAutorizacionesEntSubcontratadasForm) 
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
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//ActionErrors
				ActionErrors errores = new ActionErrors();
				mundo = new ProrrogarAnularAutorizacionesEntSubcontratadas();

				ProrrogarAnularAutorizacionesEntSubcontratadasForm forma = (ProrrogarAnularAutorizacionesEntSubcontratadasForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset();	  
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("principal");
				}else
					if(estado.equals("consultaPaciente"))
					{  
						forma.reset();
						ActionForward forward = new ActionForward();
						forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
						if(forward != null)
							return forward;	
						UtilidadBD.closeConnection(con);
						forma.setOpcionListadoAutorizacion("paciente");
						return listadoAutorizaciones(forma, paciente, usuario, request, mapping);
					}
					else 
						if (estado.equals("consultaPeriodo"))
						{ 
							forma.reset();	  
							UtilidadBD.closeConnection(con); 
							forma.setOpcionListadoAutorizacion("periodo");
							return parametrosBusqueda(forma,usuario, mapping, request);

						}else
							if (estado.equals("buscarAutorizaciones"))
							{   	
								UtilidadBD.closeConnection(con); 
								return accionBusquedaAutorizacionesRango(forma, usuario,paciente,institucionBasica, mapping, request);											   
							}				
							else
								if (estado.equals("verDetalle"))
								{    
									UtilidadBD.closeConnection(con);
									forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
									return mapping.findForward("detalleAutorizacion");

								}
								else
									if (estado.equals("prorrogarAutorizacion"))
									{    
										UtilidadBD.closeConnection(con);
										forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
										return inicializarParametrosProrroga(forma, usuario, request, mapping);

									}else
										if (estado.equals("anularAutorizacion"))
										{    
											UtilidadBD.closeConnection(con);
											forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
											return inicializarParametrosAnulacion(forma, usuario , request, mapping);

										}
										else
											if (estado.equals("guardarProrroga"))
											{    
												forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
												return guardarDatosProrroga(con, forma, usuario, request, mapping);

											}
											else
												if (estado.equals("guardarAnulacion"))
												{    
													forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
													return guardarDatosAnulacion(con, forma, usuario, paciente, request, mapping);

												} else if (estado.equals("redireccion"))
												{
													UtilidadBD.closeConnection(con);
													response.sendRedirect(forma.getLinkSiguiente());
													return null;
												} else if (estado.equals("ordenar"))
												{    
													UtilidadBD.closeConnection(con); 
													return ordenarXColumna(forma, mapping);
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
	 * 	Metodo que realiza la COnsulta de las Autorizaciones a entidades Subcontratadas	
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
		
	private ActionForward listadoAutorizaciones(ProrrogarAnularAutorizacionesEntSubcontratadasForm forma, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{			
		    //forma.setAutorizacionesEntSubContratadas(mundo.listadoAutorizacionesEntSub(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));
		    @SuppressWarnings("static-access")
			ArrayList <DtoAutorizacionEntSubContratada> listado=mundo.listadoAutorizacionesEntSub(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
		 	ArrayList <DtoAutorizacionEntSubContratada> tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
		 	ArrayList<String> codigoSolicitud=new ArrayList<String>();
		 	
		 	//Agrupa las solicuitudes de Medicamentos e Insumos en una misma autorizacion
		 	for(int i=0;i<listado.size();i++)
		 	{
		 		tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
		 		if(!codigoSolicitud.contains(listado.get(i).getNumeroSolicitud()))
		 		{
		 			for(int j=i;j<listado.size();j++)
		 			{
		 				if(listado.get(i).getNumeroSolicitud().equals(listado.get(j).getNumeroSolicitud()))
		 				{
		 					tempListado.add(listado.get(j));	
		 				}									
		 			}	
		 			codigoSolicitud.add(listado.get(i).getNumeroSolicitud());
		 			listado.get(i).setAgrupaListadoEntSub(tempListado);
		 		}
		 	}
		 	
		 	//Borra los registros vacios
		 	ArrayList<DtoAutorizacionEntSubContratada> listaDefinitiva = new ArrayList<DtoAutorizacionEntSubContratada>();
		 	for (DtoAutorizacionEntSubContratada retorno : listado) 
		 	{
		 		if (!Utilidades.isEmpty(retorno.getAgrupaListadoEntSub()))
		 		{
		 			//MT6166/Sanbarga6166/ver 1.1.6 se elimina validación de consecutivo de autorización, ya que el DCU 785 indica el la opción Guardar
		 			// que NO debe generar el número consecutivo de autorización
		 		//	if(!UtilidadTexto.isEmpty(retorno.getConsecutivoAutorizacion())
		 			//		&& Utilidades.convertirAEntero(retorno.getConsecutivoAutorizacion())!=ConstantesBD.codigoNuncaValido)
		 			//{
		 				listaDefinitiva.add(retorno);
		 			//}			
		 		}
		 	}
		 			 	
		 	forma.setAutorizacionesEntSubContratadas(listaDefinitiva);
		 	return mapping.findForward("paciente");
		     
	}
	
	
	
	/**
	 * Metodo que realiza la busqueda segun los parametros ingresados
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param institucionBasica
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionBusquedaAutorizacionesRango(ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, PersonaBasica paciente,InstitucionBasica institucionBasica, ActionMapping mapping,	HttpServletRequest request) {
			
		    ActionErrors errores = new ActionErrors();
		    errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());	
		    if(errores.isEmpty())
		    {
				
		    	//forma.setAutorizacionesEntSubContratadas(mundo.obtenerAutorizacionesEntSubContrXRango(forma.getParametrosBusqueda(), usuario.getCodigoInstitucionInt()));	
		    	@SuppressWarnings("static-access")
				ArrayList <DtoAutorizacionEntSubContratada> listado=mundo.obtenerAutorizacionesEntSubContrXRango(forma.getParametrosBusqueda(), usuario.getCodigoInstitucionInt());
		    	ArrayList <DtoAutorizacionEntSubContratada> tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
		    	ArrayList<String> codigoSolicitud=new ArrayList<String>();
		    	
		    	//Agrupa las solicuitudes de Medicamentos e Insumos en una misma autorizacion
		    	for(int i=0;i<listado.size();i++)
		    	{
		    		tempListado=new ArrayList<DtoAutorizacionEntSubContratada>();
		    		if(!codigoSolicitud.contains(listado.get(i).getNumeroSolicitud()))
		    		{
		    			for(int j=i;j<listado.size();j++)
		    			{
		    				if(listado.get(i).getNumeroSolicitud().equals(listado.get(j).getNumeroSolicitud()))
		    				{
		    					tempListado.add(listado.get(j));	
		    				}									
		    			}	
		    			codigoSolicitud.add(listado.get(i).getNumeroSolicitud());
		    			listado.get(i).setAgrupaListadoEntSub(tempListado);
		    		}
		    	}
		    	
		    	//Borra los registros vacios
		    	ArrayList<DtoAutorizacionEntSubContratada> listaDefinitiva = new ArrayList<DtoAutorizacionEntSubContratada>();
		    	for (DtoAutorizacionEntSubContratada retorno : listado) 
		    	{
		    		if (!Utilidades.isEmpty(retorno.getAgrupaListadoEntSub())) 
		    		{
		    			if(!UtilidadTexto.isEmpty(retorno.getConsecutivoAutorizacion())
		    					&& Utilidades.convertirAEntero(retorno.getConsecutivoAutorizacion())!=ConstantesBD.codigoNuncaValido)
		    			{
		    				listaDefinitiva.add(retorno);
		    			}
		    		}
		    	}	    	
		    	forma.setAutorizacionesEntSubContratadas(listaDefinitiva);
				
				return mapping.findForward("resultadoPeriodo");				
			}
			else
			{		
				saveErrors(request, errores);	
				return mapping.findForward("periodo");
			} 
		
		}
	
	

	/**
	 * Metodo para Ordenar por columna
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward ordenarXColumna(ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,ActionMapping mapping) {
			
		   ArrayList<DtoAutorizacionEntSubContratada> arrayOrdenado = new ArrayList<DtoAutorizacionEntSubContratada>();
		   arrayOrdenado=mundo.ordenarColumna(forma.getAutorizacionesEntSubContratadas(),forma.getUltimaPropiedad(), forma.getPropiedadOrdenar());
		   forma.setUltimaPropiedad(forma.getPropiedadOrdenar());
		   forma.resetArrayAutorizacionesEntSub();
		   forma.setAutorizacionesEntSubContratadas(arrayOrdenado);
		   
		   if(forma.getOpcionListadoAutorizacion().equals("paciente"))
			  {
			  return mapping.findForward("paciente");
			  }
			  else{
				  if(forma.getOpcionListadoAutorizacion().equals("periodo"))
				  {
				  return mapping.findForward("resultadoPeriodo");
				  }
			  }
		  
			return null;
		}
	
	
	
	
	/**
	 * Metodo que inicializa los parametros de Prorroga
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
private ActionForward inicializarParametrosProrroga(ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
	   String loginUsuario = usuario.getLoginUsuario();
	   String fechaInicial=(((DtoAutorizacionEntSubContratada)forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion())).getFechaVencimiento());
	   String consecutivoAutorizacion= (((DtoAutorizacionEntSubContratada)forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion())).getCodigoPk());	   
	   forma.setParametrosProrroga(mundo.inicializarParametrosProrroga(loginUsuario, fechaInicial, consecutivoAutorizacion));
	  
	   return mapping.findForward("datosProrroga");
	}

/**
 * 
 * @param forma
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
private ActionForward inicializarParametrosAnulacion(ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
	
   String loginUsuario = usuario.getLoginUsuario();
   String consecutivoAutorizacion= (((DtoAutorizacionEntSubContratada)forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion())).getCodigoPk());	   
   forma.setUsuarioSesion(loginUsuario);
   forma.setParametrosAnulacion(mundo.inicializarParametrosAnulacion(loginUsuario, consecutivoAutorizacion));
  
   return mapping.findForward("datosAnulacion");
}


/**
 * Metodo para guardar Prorroga
 * @param forma
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
	private ActionForward guardarDatosProrroga(Connection con, ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, HttpServletRequest request,ActionMapping mapping) {
		
		HashMap resultado = new HashMap();
		UtilidadBD.iniciarTransaccion(con);
	
		 resultado= mundo.guardarProrroga(con, forma.getParametrosProrroga());
		 if(Utilidades.convertirAEntero(resultado.get("codigoProrroga").toString())>0)
		  {
			  forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
	      	  forma.setParametrosFiltros("mensaje","Prorroga de Autorizacion Generada con Exito");
	      	  forma.getAutorizacionesEntSubContratadas().get(forma.getPosAutorizacion()).setFechaVencimiento(forma.getParametrosProrroga().get("fechaProrroga").toString());
	      	  UtilidadBD.finalizarTransaccion(con);
	          UtilidadBD.closeConnection(con);
	          return inicializarParametrosProrroga(forma, usuario, request, mapping);
			}	 
	         else
			  {
		      UtilidadBD.abortarTransaccion(con);
		  
		       forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
		       ActionErrors errores = (ActionErrors)resultado.get("error");
		       saveErrors(request, errores);
		       UtilidadBD.closeConnection(con); 
		       return mapping.findForward("datosProrroga");
	          }
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward guardarDatosAnulacion(Connection con, ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request,ActionMapping mapping) {
		
		HashMap resultado = new HashMap();
		UtilidadBD.iniciarTransaccion(con);
	    Utilidades.imprimirMapa(forma.getParametrosAnulacion());
		 resultado= mundo.guardarAnulacion(con, forma.getParametrosAnulacion());
		 if(Utilidades.convertirAEntero(resultado.get("codigoAnulacion").toString())>0)
		  {
			  forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
	      	  forma.setParametrosFiltros("mensaje","Anulacion de Autorizacion Generada con Exito");
	      	  UtilidadBD.finalizarTransaccion(con);
	          UtilidadBD.closeConnection(con);
			  return mapping.findForward("datosAnulacion");
			}	 
	         else
			  {
		      UtilidadBD.abortarTransaccion(con);
		  
		       forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
		       forma.setParametrosFiltros("mensaje","No se pudo realizar la Anulacion de Autorizacion ");
		       ActionErrors errores = (ActionErrors)resultado.get("error");
		       saveErrors(request, errores);
		       UtilidadBD.closeConnection(con); 
		       return mapping.findForward("datosAnulacion");
	          }
		
	}
	
	
	/**
	 * 	Metodo para Validar que el paciente esté cargado en sesion	
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionValidarPaciente(Connection con,ProrrogarAnularAutorizacionesEntSubcontratadasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
		{
				if(paciente==null || paciente.getCodigoPersona()<=0)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
				}
		        
				return null;
	    }
		
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward parametrosBusqueda(ProrrogarAnularAutorizacionesEntSubcontratadasForm forma,UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request) {
 		
 	     forma.setParametrosBusqueda(mundo.inicializarParametrosBusqueda());
 	     forma.setEntidadesSubcontratadas(ConsultarImprimirAutorizacionesEntSubcontratadas.listaEntidadesSubcontratadas());
 	     return mapping.findForward("periodo");
 		
 	}
	
}
