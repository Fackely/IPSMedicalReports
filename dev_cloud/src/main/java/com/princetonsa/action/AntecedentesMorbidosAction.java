/*
 * @(#)AntecedentesMorbidosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesMorbidosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedenteMorbidoMedico;
import com.princetonsa.mundo.antecedentes.AntecedenteMorbidoQuirurgico;
import com.princetonsa.mundo.antecedentes.AntecedentesMorbidos;

/**
 * Action, controla todo el modulo de antecedentes morbidos
 * @version 1.0, Agosto 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesMorbidosAction  extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AntecedentesMorbidosAction.class);
	
	/**
	 * Usuario que esta trabajando actualmente en el sistema.
	 */
	private UsuarioBasico usuario;
	
	
	/**
	 * Metodo encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
	{
		Connection con = null;
			
		try {
		if( form instanceof AntecedentesMorbidosForm )
		{
			AntecedentesMorbidosForm morbidosForm = (AntecedentesMorbidosForm)form;
			String estado = morbidosForm.getEstado();
			logger.warn("\n\n\t\t En AntecedentesMorbidosAction El Estado Es [" + estado + "] \n\n");	

			String tipoBD;
			try
			{
				tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				morbidosForm.reset();
				
				logger.warn("Problemas con la base de datos "+e);
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
	
			HttpSession session=request.getSession();			
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			if( usuario == null )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug("Usuario no valido (null)");
				}
				
				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
					
				morbidosForm.reset();					
				
				request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
				return mapping.findForward("paginaError");				
			}
			else
			if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug("El paciente no es valido (null)");
				}
				
				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
					
				morbidosForm.reset();					

				request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
				return mapping.findForward("paginaError");				
			}
			else	
			if( estado == null )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug("estado no valido dentro del flujo de valoración (null) ");
				}
				
				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
					
				morbidosForm.reset();

				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("descripcionError");				
			}
			else  
			if( !UtilidadValidacion.esProfesionalSalud(usuario) )
			{
				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);

				morbidosForm.reset();
						
				logger.warn("El usuario no tiene permisos de acceder a esta funcionalidad "+usuario.getLoginUsuario());				
				request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
				return mapping.findForward("paginaError");								
				
				
			}
			else
			if( estado.equals("modificarQuirurgico") )
			{
				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);

				return mapping.findForward("morbidosPpal");					
			}
			else			
			if( estado.equals("adicionarQuirurgico") )
			{
				morbidosForm.setNumAntMorbidosQuirurgicos(morbidosForm.getNumAntMorbidosQuirurgicos()+1);

				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);

				return mapping.findForward("morbidosPpal");
			}
			else
			if( estado.equals("adicionarMedico") )
			{
				morbidosForm.setNumAntMorbidosMedicosOtros(morbidosForm.getNumAntMorbidosMedicosOtros()+1);
				morbidosForm.setAntecedenteMedicoOtro("checkbox_"+morbidosForm.getNumAntMorbidosMedicosOtros(), "true");

				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);

				return mapping.findForward("morbidosPpal");
			}
			else
			if( estado.equals("cargar") )
			{
				morbidosForm.reset();
				AntecedentesMorbidos morbidos = new AntecedentesMorbidos();
				morbidos.setPaciente(paciente);
				
				morbidos.cargar(con);
				
				cargarForm(morbidos, morbidosForm);

				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
								
				return mapping.findForward("morbidosPpal");
			}
			else
			if( estado.equals("resumen") )
			{
				morbidosForm.reset();
				AntecedentesMorbidos morbidos = new AntecedentesMorbidos();
				morbidos.setPaciente(paciente);
				
				morbidos.cargar(con);
				
				cargarForm(morbidos, morbidosForm);

				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
								
				return mapping.findForward("resumenMorbidos");				
			}
			else
			if( estado.equals("finalizar") )
			{
				AntecedentesMorbidos morbidos = new AntecedentesMorbidos();
				morbidos.setPaciente(paciente);
				
				cargarObjeto(morbidos, morbidosForm);
				
				ResultadoBoolean resultado = morbidos.updateTransaccional(con);
				
				if( resultado.isTrue() )
				{
					morbidosForm.reset();
					
					morbidos.reset();					
					
					morbidos.cargar(con);
					cargarForm(morbidos, morbidosForm);

					if( con != null && !con.isClosed() )
	                    UtilidadBD.closeConnection(con);
					
					String paginaSiguiente=request.getParameter("paginaSiguiente");
						
					if( paginaSiguiente != null && !paginaSiguiente.equals("") )
					{
	                    UtilidadBD.closeConnection(con);
						response.sendRedirect(paginaSiguiente);
					}
								
					return mapping.findForward("morbidosPpal");					
				}
				else
				{
					morbidosForm.reset();
					
					if( con != null && !con.isClosed() )
	                    UtilidadBD.closeConnection(con);

					logger.warn("No se pudo hacer la inserción de los antecedentes se presenta el siguiente error : "+resultado.getDescripcion());				
					request.setAttribute("descripcionError", "No se pudo hacer la inserción de los antecedentes se presenta el siguiente error : "+resultado.getDescripcion());
					return mapping.findForward("paginaError");													
				}				
			}
			else
			if( estado.equals("cancelar") )
			{
				String paginaSiguiente=request.getParameter("paginaSiguiente");
				morbidosForm.reset();
				
				if( con != null && !con.isClosed() )
				{
                    UtilidadBD.closeConnection(con);
				}
				response.sendRedirect(paginaSiguiente);
			}
			else
			{
				logger.error("Se intento acceder con un estado invalido "+usuario.getLoginUsuario()+" Estado --> "+estado);
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				
				if( con != null && !con.isClosed() )
                    UtilidadBD.closeConnection(con);
				
				return mapping.findForward("paginaError");																	
			}
			return null;
		}
		else
		{
			logger.error("El form no coincide con el esperado ");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");													
		}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Carga  el form (bean) con la información pertinente contenida en el
	 * objeto
	 * @param 	AntecedentesMorbidos, morbidos
	 * @param		AntecedentesMorbidosForm, form
	 */
	@SuppressWarnings("rawtypes")
	private void cargarForm(	AntecedentesMorbidos morbidos, 
												AntecedentesMorbidosForm form)
	{		
		if( morbidos.getObservaciones() != null )
		{
			if( form.estado.equals("resumen") )
			{
				String observaciones = morbidos.getObservaciones();
				observaciones = observaciones.replaceAll("\n", "<br>");
				form.setObservacionesAnteriores(observaciones);
			}
			else
			{
				String observaciones = morbidos.getObservaciones().replaceAll("<br>", "\n");
				form.setObservacionesAnteriores(observaciones);
			}
		}
		
		//		MORBIDOS MEDICOS PREDEFINIDOS		
		ArrayList antecedentes = morbidos.getAntecedentesMorbidosMedicosPredefinidos();
		int tam = antecedentes.size();
		
		form.setNumAntMorbidosMedicos(tam);
				
		for(int i=0; i<tam; i++)
		{
			AntecedenteMorbidoMedico antMorbidoMedPredef = (AntecedenteMorbidoMedico) antecedentes.get(i);
            //logger.info("Cargando antecedente:"+i+" "+antMorbidoMedPredef.getNombre());
			cargarMapMorbidosMedicosPredef(antMorbidoMedPredef, form, i+1);  
		}
		
		//		MORBIDOS MEDICOS OTROS
		antecedentes = morbidos.getAntecedentesMorbidosMedicosAdicionales();
		tam = antecedentes.size();
		form.setNumAntMorbidosMedicosOtros(tam);
		
		for(int i=0; i<tam; i++)
		{
			AntecedenteMorbidoMedico antMorbidoMedOtro = (AntecedenteMorbidoMedico) antecedentes.get(i);
			cargarMapMorbidosMedicosOtros(antMorbidoMedOtro, form, i+1);  
		}
		
		//		MORBIDOS MEDICOS QUIRURGICOS
		antecedentes = morbidos.getAntecedentesMorbidosQuirurgicos();
		tam = antecedentes.size();
		form.setNumAntMorbidosQuirurgicosBD(tam);
		form.setNumAntMorbidosQuirurgicos(tam);
		
	
		for(int i=0; i<tam; i++)
		{
			AntecedenteMorbidoQuirurgico antMorbidoQuirurgico = (AntecedenteMorbidoQuirurgico) antecedentes.get(i);
			cargarMapMorbidosQuirurgicos(antMorbidoQuirurgico, form, i+1);
		}
	}
	
	/**
	 * Carga en el objeto la información pertinente captada en el form
	 * @param morbidos
	 * @param form
	 */
	private void cargarObjeto(AntecedentesMorbidos morbidos, 
												AntecedentesMorbidosForm form)
	{
		morbidos.setObservaciones(cargarObservaciones(form.getObservacionesNuevas(), form.getObservacionesAnteriores()));
		
		// Antecedentes mórbidos módicos predefinidos
		int tam = form.getNumAntMorbidosMedicos();
		
		//if( logger.isDebugEnabled() )
			logger.debug("El número de antecedentes morbidos módicos predefinidos es : "+tam+" (desde el form)");
		
		for( int i=1; i<=tam; i++ )
		{
			String codigoStr = (String)form.getAntecedenteMedico("codigo_"+i);
			int codigo = 0;
			
			if( noVacio(codigoStr) )
				codigo = Integer.parseInt(codigoStr);
				
			String checkBox = (String)form.getAntecedenteMedico("checkbox_"+codigo);
			String grabadoBDCheck = (String)form.getAntecedenteMedico("cb_grabadoBD_"+codigo);
			
			String nombre = (String)form.getAntecedenteMedico("nombre_"+codigo);
			
			String fechaInicio = (String)form.getAntecedenteMedico("desde_"+codigo); 
			String grabadoBDFechaInic = (String)form.getAntecedenteMedico("dc_grabadoBD_"+codigo);
			
			String tratamiento = (String)form.getAntecedenteMedico("tratamiento_"+codigo);
			String grabadoBDTrat = (String)form.getAntecedenteMedico("t_grabadoBD_"+codigo);
			
			String restriccionDietaria = (String)form.getAntecedenteMedico("dieta_"+codigo);
			String grabadoBDRestDiet = (String)form.getAntecedenteMedico("rd_grabadoBD_"+codigo);
			
			String observaciones = (String)form.getAntecedenteMedico("observaciones_"+codigo);
			String observacionesAnt = (String)form.getAntecedenteMedico("observacionesAnteriores_"+codigo);
			
			if( noVacio(checkBox) || noVacio(fechaInicio) || noVacio(tratamiento) || noVacio(restriccionDietaria) || noVacio(observaciones) )
			{
				morbidos.setAntecedenteMorbidoMedicoPredefinido(cargarObjetoMorbidoMedico(	codigo, 
																																						nombre, 
																																						fechaInicio, 
																																						tratamiento, 
																																						restriccionDietaria, 
																																						observaciones, 
																																						observacionesAnt, 
																																						grabadoBDFechaInic, 
																																						grabadoBDTrat, 
																																						grabadoBDRestDiet, 
																																						grabadoBDCheck));
			}
		}
		
		// Antecedentes mórbidos módicos adicionales
		tam = form.getNumAntMorbidosMedicosOtros();
		
		
		for( int i=1; i<=tam; i++ )
		{
			String chequeado = (String)form.getAntecedenteMedicoOtro("chequeado_"+i);
			String checkBox = (String)form.getAntecedenteMedicoOtro("checkbox_"+i);
			String grabadoBDCheck = (String)form.getAntecedenteMedicoOtro("cb_grabadoBD_"+i);
			
			String nombre = (String)form.getAntecedenteMedicoOtro("nombre_"+i);
			
			String grabadoBDFechaInic = (String)form.getAntecedenteMedicoOtro("dc_grabadoBD_"+i);
			String fechaInicio = (String)form.getAntecedenteMedicoOtro("desde_"+i); 
			
			String grabadoBDTrat = (String)form.getAntecedenteMedicoOtro("t_grabadoBD_"+i);
			String tratamiento = (String)form.getAntecedenteMedicoOtro("tratamiento_"+i);
			
			String grabadoBDRestDiet = (String)form.getAntecedenteMedicoOtro("rd_grabadoBD_"+i);
			String restriccionDietaria = (String)form.getAntecedenteMedicoOtro("dieta_"+i);
			
			String observaciones = (String)form.getAntecedenteMedicoOtro("observaciones_"+i);
			String observacionesAnt = (String)form.getAntecedenteMedicoOtro("observacionesAnteriores_"+i);
			
			logger.info("\n\n  chequeado.  [" + chequeado + "]  \n\n");
//			if( !chequeado.equals("false") )
			if( UtilidadTexto.getBoolean(chequeado) )
			{
				if( noVacio(nombre) || noVacio(checkBox) || noVacio(fechaInicio) || noVacio(tratamiento) || noVacio(restriccionDietaria) || noVacio(observaciones) )
				{
					morbidos.setAntecedenteMorbidoMedicoAdicional(cargarObjetoMorbidoMedico(	i, 
																																						nombre, 
																																						fechaInicio, 
																																						tratamiento, 
																																						restriccionDietaria, 
																																						observaciones,
																																						observacionesAnt, 
																																						grabadoBDFechaInic, 
																																						grabadoBDTrat, 
																																						grabadoBDRestDiet, 
																																						grabadoBDCheck));
				}
			}
		}
		
		// Antecedentes mórbidos quirurgicos
		tam = form.getNumAntMorbidosQuirurgicos();
		
		
		for( int i=1; i<=tam; i++ )
		{
			//String codigo = (String)form.getAntecedenteQuirurgico("codigo_"+i);
			String nombre = (String)form.getAntecedenteQuirurgico("nombre_"+i);
			String fecha = (String)form.getAntecedenteQuirurgico("fecha_"+i);
			String causa = (String)form.getAntecedenteQuirurgico("causa_"+i);
			String complicaciones = (String)form.getAntecedenteQuirurgico("complicaciones_"+i);
			String recomendaciones = (String)form.getAntecedenteQuirurgico("recomendaciones_"+i);
			String observaciones = (String)form.getAntecedenteQuirurgico("observaciones_"+i);
			String grabadoBDNombre = (String)form.getAntecedenteQuirurgico("n_grabadoBD_"+i);
			String grabadoBDFecha = (String)form.getAntecedenteQuirurgico("f_grabadoBD_"+i);
			String grabadoBDCausa = (String)form.getAntecedenteQuirurgico("ca_grabadoBD_"+i);
			String grabadoBDComplicaciones = (String)form.getAntecedenteQuirurgico("co_grabadoBD_"+i);
			String grabadoBDRecomendaciones = (String)form.getAntecedenteQuirurgico("r_grabadoBD_"+i);
			String observacionesAnt = (String)form.getAntecedenteQuirurgico("observacionesAnteriores_"+i);
			
			if( noVacio(nombre) || noVacio(fecha) || noVacio(causa) || noVacio(complicaciones) || noVacio(recomendaciones) || noVacio(observaciones) )
			{
				morbidos.setAntecedenteMorbidoQuirurgico(cargarObjetoMorbidoQuirurgico(	i, 
						nombre, 
						fecha, 
						causa, 
						complicaciones, 
						recomendaciones, 
						observaciones, 
						observacionesAnt, 
						grabadoBDNombre, 
						grabadoBDFecha, 
						grabadoBDCausa, 
						grabadoBDComplicaciones, 
						grabadoBDRecomendaciones));
			}
		}		
	}
	
	/**
	 * Carga en un "Map", los antecedentes módicos predefinidos en la base de
	 * datos, con sus respectivos valores ingresados y sus correspondientes
	 * llaves.
	 * @param 	AntecedenteMorbidoMedico, antMedico
	 * @param 	AntecedentesMorbidosForm, form
	 * @param 	int, indice. Indice (orden) del antecedente. Para generar las
	 * llaves.
	 */
	private void cargarMapMorbidosMedicosPredef(AntecedenteMorbidoMedico antMedico, 
																					AntecedentesMorbidosForm form, 
																					int indice)
	{		
		form.setAntecedenteMedico("codigo_"+indice, ""+antMedico.getCodigo());
		
		indice = antMedico.getCodigo();
		
		form.setAntecedenteMedico("cb_grabadoBD_"+indice, "true");
		form.setAntecedenteMedico("checkbox_"+indice, "true");	
		
		form.setAntecedenteMedico("nombre_"+indice, antMedico.getNombre());
		
		form.setAntecedenteMedico("desde_"+indice, antMedico.getFechaInicio());
		if( antMedico.getFechaInicio() != null && !antMedico.getFechaInicio().equals("") )
			form.setAntecedenteMedico("dc_grabadoBD_"+indice, "true");
		
		form.setAntecedenteMedico("tratamiento_"+indice, antMedico.getTratamiento());
		if( antMedico.getTratamiento() != null && !antMedico.getTratamiento().equals("") )
			form.setAntecedenteMedico("t_grabadoBD_"+indice, "true");
		
		form.setAntecedenteMedico("dieta_"+indice, antMedico.getRestriccionDietaria());
		if( antMedico.getRestriccionDietaria() != null && !antMedico.getRestriccionDietaria().equals("") )
			form.setAntecedenteMedico("rd_grabadoBD_"+indice, "true");
		
		if( noVacio(antMedico.getObservaciones()) )
		{
			String observaciones = antMedico.getObservaciones().replaceAll("\n", "<br>");
			form.setAntecedenteMedico("observacionesAnteriores_"+indice, observaciones);
		}
	}

	/**
	 * Carga en un "Map", los antecedentes módicos adicionales, no predefinidos
	 * en la base de datos, con sus respectivos valores ingresados y sus
	 * correspondientes llaves.
	 * @param 	AntecedenteMorbidoMedico, antMedico
	 * @param 	AntecedentesMorbidosForm, form
	 * @param 	int, indice. Indice (orden) del antecedente. Para generar las
	 * llaves.
	 */	
	private void cargarMapMorbidosMedicosOtros(	AntecedenteMorbidoMedico antMedico, 
																					AntecedentesMorbidosForm form, 
																					int indice)
	{		
		form.setAntecedenteMedicoOtro("cb_grabadoBD_"+indice, "true");
		form.setAntecedenteMedicoOtro("checkbox_"+indice, "true");	
		
		form.setAntecedenteMedicoOtro("nombre_"+indice, antMedico.getNombre());
		
		form.setAntecedenteMedicoOtro("desde_"+indice, antMedico.getFechaInicio());
		if( antMedico.getFechaInicio() != null && !antMedico.getFechaInicio().equals("") )
			form.setAntecedenteMedicoOtro("dc_grabadoBD_"+indice, "true");
		
		form.setAntecedenteMedicoOtro("tratamiento_"+indice, antMedico.getTratamiento());
		if( antMedico.getTratamiento() != null && !antMedico.getTratamiento().equals("") )
			form.setAntecedenteMedicoOtro("t_grabadoBD_"+indice, "true");
		
		form.setAntecedenteMedicoOtro("dieta_"+indice, antMedico.getRestriccionDietaria());
		if( antMedico.getRestriccionDietaria() != null && !antMedico.getRestriccionDietaria().equals("") )
			form.setAntecedenteMedicoOtro("rd_grabadoBD_"+indice, "true");
		
		if( noVacio(antMedico.getObservaciones()) )
		{
			String observaciones = antMedico.getObservaciones().replaceAll("\n", "<br>");
			form.setAntecedenteMedicoOtro("observacionesAnteriores_"+indice, observaciones);
		}
	}
	
	/**
	 * Carga en un "Map", los antecedentes mórbidos quirurgicos, con sus
	 * respectivos valores ingresados y sus correspondientes llaves.
	 * @param 	AntecedenteMorbidoQuirurgico, antMedico
	 * @param 	AntecedentesMorbidosForm, form
	 * @param 	int, indice. Indice (orden) del antecedente. Para generar las
	 * llaves.
	 */	
	private void cargarMapMorbidosQuirurgicos(	AntecedenteMorbidoQuirurgico antQuirurgico, 
																				AntecedentesMorbidosForm form, 
																				int indice)
	{
		form.setAntecedenteQuirurgico("codigo_"+indice, ""+antQuirurgico.getCodigo());
		form.setAntecedenteQuirurgico("nombre_"+indice, antQuirurgico.getNombre());
		form.setAntecedenteQuirurgico("n_grabadoBD_"+indice, "true");
		
		form.setAntecedenteQuirurgico("fecha_"+indice, antQuirurgico.getFecha());
		if( noVacio(antQuirurgico.getFecha()) )
			form.setAntecedenteQuirurgico("f_grabadoBD_"+indice, "true");
			
		form.setAntecedenteQuirurgico("causa_"+indice, antQuirurgico.getCausa());
		if( noVacio(antQuirurgico.getCausa()) )		
			form.setAntecedenteQuirurgico("ca_grabadoBD_"+indice, "true");
			
		form.setAntecedenteQuirurgico("complicaciones_"+indice, antQuirurgico.getComplicaciones());
		if( noVacio(antQuirurgico.getComplicaciones()) )		
			form.setAntecedenteQuirurgico("co_grabadoBD_"+indice, "true");
			
		form.setAntecedenteQuirurgico("recomendaciones_"+indice, antQuirurgico.getRecomendaciones());
		if( noVacio(antQuirurgico.getRecomendaciones()) )	
			form.setAntecedenteQuirurgico("r_grabadoBD_"+indice, "true");
		
		if( noVacio(antQuirurgico.getObservaciones()) )
		{
			String observaciones = antQuirurgico.getObservaciones().replaceAll("\n", "<br>");
			form.setAntecedenteQuirurgico("observacionesAnteriores_"+indice, observaciones);
		}
	}
	
	/**
	 * Retorna true su la cadena no es vacia, es decir, no es null, ni vacia,
	 * false de lo contrario.
	 * @param valor
	 * @return boolean
	 */	
	private boolean noVacio(String valor)
	{
		if( valor != null && !valor.equals("") )
			return true;
		else
			return false; 
	}
	
	/**
	 * Carga la representación del mundo de un antecedente mórbido módico con la
	 * información ingresada.
	 * @param 	int, codigo. Código del antecedente
	 * @param 	String, nombre. Nombre del antecedente
	 * @param 	String, fechaInicio. Fecha de inicio del antecedente.
	 * @param 	String, tratamiento. Tratamiento del antecedente.
	 * @param 	String, restriccionDietaria. Restricción dietaria del
	 * antecedente.
	 * @param 	String, observaciones. Observaciones nuevas.
	 * @param 	String, observacionesAnt. Observaciones previamente ingresadas.
	 * @param 	String, grabadoBDFecha. Si la fecha ha sido grabado previamente.
	 * @param 	String, grabadoBDTratamiento. Si el tratamiento dietaria ha sido
	 * grabado previamente.
	 * @param 	String, grabadoBDRestDiet. Si la restricción dietaria ha sido
	 * grabado previamente.
	 * @param 	String, grabadoBDAnt. Si el antecedente ha sido grabado
	 * previamente.
	 * @return AntecedenteMorbidoMedico
	 */
	private AntecedenteMorbidoMedico cargarObjetoMorbidoMedico(int codigo, 
																												String nombre, 
																												String fechaInicio, 
																												String tratamiento, 
																												String restriccionDietaria, 
																												String observaciones, 
																												String observacionesAnt, 
																												String grabadoBDFecha, 
																												String grabadoBDTratamiento, 
																												String grabadoBDRestDiet, 
																												String grabadoBDAnt)
	{
		AntecedenteMorbidoMedico antecedente = new AntecedenteMorbidoMedico();
		
		antecedente.setCodigo(codigo);
		antecedente.setNombre(nombre);
		antecedente.setFechaInicio(fechaInicio);
		antecedente.setTratamiento(tratamiento);
		antecedente.setRestriccionDietaria(restriccionDietaria);
		antecedente.setObservaciones(cargarObservacionesMorbidosMedicosObjeto(	observaciones, 
																																			observacionesAnt, 
																																			nombre, 
																																			fechaInicio, 
																																			tratamiento, 
																																			restriccionDietaria, 
																																			grabadoBDFecha, 
																																			grabadoBDTratamiento, 
																																			grabadoBDRestDiet, 
																																			grabadoBDAnt));
		
		return antecedente;
	}
	
	/**
	 * Carga la representación del mundo de un antecedente mórbido quirurgico
	 * con la información ingresada.
	 * @param 	int, codigo. Código del antecedente
	 * @param 	String, nombre. Nombre del antecedente
	 * @param 	String, fecha. Fecha del antecedente.
	 * @param 	String, causa. Causa del antecedente.
	 * @param 	String, complicaciones. Complicaciones del antecedente.
	 * @param 	String, recomendaciones. Recomendaciones del antecedente.
	 * @param 	String, observaciones. Observaciones nuevas.
	 * @param 	String, observacionesAnt. Observaciones previamente ingresadas.
	 * @param 	String, grabadoBDNombre. Si el nombre ha sido grabado
	 * previamente.
	 * @param	 	String, grabadoBDFecha. Si la fecha ha sido grabado
	 * previamente.
	 * @param 	String, grabadoBDCausa. Si la causa, ha sido grabado
	 * previamente.
	 * @param 	String, grabadoBDComplicaciones. Si las complicaciones han sido
	 * grabado previamente.
	 * @param 	String, grabadoBDRecomendaciones. Si las recomendaciones han
	 * sido grabado previamente.
	 * @return AntecedenteMorbidoQuirurgico
	 */
	private AntecedenteMorbidoQuirurgico cargarObjetoMorbidoQuirurgico(	int codigo, 
																															String nombre, 
																															String fecha, 
																															String causa, 
																															String complicaciones, 
																															String recomendaciones, 
																															String observaciones, 
																															String observacionesAnt, 
																															String grabadoBDNombre, 
																															String grabadoBDFecha, 
																															String grabadoBDCausa, 
																															String grabadoBDComplicaciones, 
																															String grabadoBDRecomendaciones)
	{
		AntecedenteMorbidoQuirurgico antecedente = new AntecedenteMorbidoQuirurgico();
		
		antecedente.setCodigo(codigo);
		antecedente.setNombre(nombre);
		antecedente.setFecha(fecha);
		antecedente.setCausa(causa);
		antecedente.setComplicaciones(complicaciones);
		antecedente.setRecomendaciones(recomendaciones);
		antecedente.setObservaciones(this.cargarObservacionesMorbidosQuirurgicos(	codigo, 
																																			nombre, 
																																			fecha, 
																																			causa, 
																																			complicaciones,
																																			recomendaciones,
																																			observaciones,
																																			observacionesAnt,
																																			grabadoBDNombre,
																																			grabadoBDFecha,
																																			grabadoBDCausa,
																																			grabadoBDComplicaciones,
																																			grabadoBDRecomendaciones));
		
		return antecedente;
	}
	
	/**
	 * Las observaciones de cada antecedente mórbido a medicamento tienen un
	 * formato y una información especial, este mótodo es el encargado de dados
	 * unos datos de entrada, producir de forma adecuada las observaciones para
	 * el antecedente módico.
	 * @param 	String, nuevas. Observaciones nuevas
	 * @param		String, anteriores. Observaciones previamente ingresadas.
	 * @param 	String, nombre. Nombre del antecedente
	 * @param 	String, fechaInicio. Fecha de inicio del antecedente.
	 * @param 	String, tratamiento del antecedente.
	 * @param 	String, restriccionDietaria. Restricción dietaria del
	 * antecedente.
	 * @param 	String, grabadoBDFecha, Si la fecha ha sido grabado previamente.
	 * @param 	String, grabadoBDTratamiento. Si el tratamiento ha sido grabado
	 * previamente.
	 * @param 	Stringm grabadoBDRestDiet. Si las restricciones dietarias han
	 * sido grabadas previamente.
	 * @param grabadoBDAnt
	 * @return String
	 */
	private String cargarObservacionesMorbidosMedicosObjeto(String nuevas, 
																										String anteriores, 
																										String nombre, 
																										String fechaInicio, 
																										String tratamiento, 
																										String restriccionDietaria, 
																										String grabadoBDFecha, 
																										String grabadoBDTratamiento, 
																										String grabadoBDRestDiet, 
																										String grabadoBDAnt)
	{
		String observaciones="";
		if(!noVacio(anteriores))
			observaciones = nombre;
		else
		    observaciones = anteriores;
	
		if( noVacio(nuevas) )
		{
			nuevas = "<b>"+nuevas+"</b>";
		}
		
		if( !noVacio(grabadoBDAnt) )
		    nuevas += "\nSe grabó el antecedente.";			

		if( noVacio(fechaInicio) && !noVacio(grabadoBDFecha) )
		    nuevas += "\nSe grabó información en el campo \"Desde cuando\" del antecedente";
		if( noVacio(tratamiento) && !noVacio(grabadoBDTratamiento) )
		    nuevas += "\nSe grabó información en el campo \"Tratamiento\" del antecedente";
		if( noVacio(restriccionDietaria) && !noVacio(grabadoBDRestDiet) )
		    nuevas += "\nSe grabó información en el campo \"Restricción Dietaria\" del antecedente";
		
		if( noVacio(nuevas) )
			observaciones=UtilidadTexto.agregarTextoAObservacion(observaciones, nuevas, usuario, true);
		
		return observaciones;	
	}
	
	/**
	 * Las observaciones de cada antecedente mórbido quirurgico tienen un
	 * formato y una información especial, este mótodo es el encargado de dados
	 * unos datos de entrada, producir de forma adecuada las observaciones para
	 * el antecedente módico.
	 * @param 	int, codigo. Código del antecedente.
	 * @param 	String, nombre. Nombre del antecedente.
	 * @param 	Strng, fecha. Fecha del antecedente.
	 * @param 	String, causa. Causa del antecedente.
	 * @param 	String, complicaciones. Complicaciones del antecedente.
	 * @param 	String, recomendaciones. Recomendaciones del antecedente.
	 * @param 	String, nuevas. Observaciones nuevas.
	 * @param 	String, anteriores. Observaciones previamente ingresadas.
	 * @param 	String, grabadoBDNombre. Si el nombre ha sido grabado
	 * previamente.
	 * @param 	String, grabadoBDFecha. Si la fecha ha sido grabado previamente.
	 * @param 	String, grabadoBDCausa. Si la causa ha sido grabado previamente.
	 * @param 	String, grabadoBDComplicaciones. Si las complicaciones han sido
	 * grabados previamente.
	 * @param 	String, grabadoBDRecomendaciones. Si las recomendaciones han
	 * sido grabados previamente.
	 * @return String
	 */
	private String cargarObservacionesMorbidosQuirurgicos(	int codigo, 
																									String nombre, 
																									String fecha, 
																									String causa, 
																									String complicaciones, 
																									String recomendaciones, 
																									String nuevas, 
																									String anteriores, 
																									String grabadoBDNombre, 
																									String grabadoBDFecha, 
																									String grabadoBDCausa, 
																									String grabadoBDComplicaciones, 
																									String grabadoBDRecomendaciones)
	{
		String observaciones="";
		if(!noVacio(anteriores))
			observaciones =nombre +" "+ codigo;
		else
			observaciones = anteriores;

		if( noVacio(nuevas))
		    nuevas="<b>"+nuevas+"</b>";
	
		if( !noVacio(grabadoBDNombre) )
		    nuevas += "\nSe grabó el antecedente.";			
	
		if( noVacio(fecha) && !noVacio(grabadoBDFecha) )
		    nuevas += "\nSe grabó información en el campo \"Fecha\" del antecedente";
		if( noVacio(causa) && !noVacio(grabadoBDCausa) )
		    nuevas += "\nSe grabó información en el campo \"Causa\" del antecedente";
		if( noVacio(complicaciones) && !noVacio(grabadoBDComplicaciones) )
		    nuevas += "\nSe grabó información en el campo \"Complicaciones\" del antecedente";
		if( noVacio(recomendaciones) && !noVacio(grabadoBDRecomendaciones) )
		    nuevas += "\nSe grabó información en el campo \"Recomendaciones\" del antecedente";

		if( noVacio(nuevas) )
			observaciones=UtilidadTexto.agregarTextoAObservacion(observaciones, nuevas, usuario, true);

		return observaciones;
	}
	
	/**
	 * Se le da formato a las observaciones generales. Es decir, se pegan a las
	 * anteriores, en caso que existan, se adiciona la fecha y hora de
	 * grabación, la cadena con las observaciones escritas y el medico que las
	 * ingresa con toda su información.
	 * @param 	String, nuevas. Nuevas observaciones.
	 * @param 	String, anteriores. Observaciones previamente ingresadas.
	 * @return 		String, cadena con las observaciones a guardar.
	 */
	public String cargarObservaciones(String nuevas, String anteriores)
	{
		//si no se editaron nuevas observaciones no se hace nada
		if(nuevas.equals(""))
			return anteriores;
		else
			return UtilidadTexto.agregarTextoAObservacion(anteriores, nuevas, usuario, true);
	}

	/**
	 * Retorna las especialidades del usuario que esta en la aplicación.
	 * @param usuario
	 * @return String
	 */	
	public String getEspecialidadesUsuario(UsuarioBasico usuario)
	{
		return UsuarioBasico.getEspecialidadesMedico(usuario);
	}
	
	/**
	 * Retorna la fecha actual en formato dd/mm/aaaa
	 * @return String
	 */
	public String getFechaActual()
	{	
		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));

		int anioAct = calendar.get(Calendar.YEAR);
		int mesAct  = calendar.get(Calendar.MONTH)+1;
		String mesAct2 = (new Integer(mesAct)).toString();	
		mesAct2 = (mesAct2.length() < 2) ? "0"+mesAct2 : mesAct2;	
		int diaAct  = calendar.get(Calendar.DAY_OF_MONTH);
		String diaAct2 = (new Integer(diaAct)).toString();
		diaAct2 = (diaAct2.length() < 2) ? "0"+diaAct2 : diaAct2;	

		return diaAct2+"/"+mesAct2+"/"+anioAct;
	}
	
	/**
	 * Retorna la hora actual, en formato hh:MM
	 * @return String
	 */	
	public String getHoraActual()
	{
		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));
		
		String minute = calendar.get(Calendar.MINUTE) + "";
		minute = (minute.length() < 2) ? "0"+minute : minute;
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		hour = (hour.length() < 2) ? "0"+hour : hour;
		String horaAct = hour + ":" + minute;
		
		return horaAct;
	}
	
}
