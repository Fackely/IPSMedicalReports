package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
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

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.TextosRespuestaProcedimientosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.TextosRespuestaProcedimientos;

/**
 * Anexo 714
 * Creado el 17 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class TextosRespuestaProcedimientosAction extends Action
{
	/**
	 * Se define una bandera para saber como es el flujo dentro de textos respuesta procedimientos
	 * @return bandera
	 */
	public int getBandera() {
		return bandera;
	}

	/**
	 * Se define una bandera para saber como es el flujo dentro de textos respuesta procedimientos
	 * @param bandera
	 */
	public void setBandera(int bandera) {
		this.bandera = bandera;
	}

	/**
	 * Loggers de la clase ReporteReferenciaExternaAction
	 * 	 */
	Logger logger = Logger.getLogger(TextosRespuestaProcedimientosAction.class);
	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	private int bandera = 0;
	
	/**
	 * Método execute action
	 */
	public ActionForward execute (ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception
			{
		Connection connection = null;
		try {
			if(form instanceof TextosRespuestaProcedimientosForm)
			{

				connection = UtilidadBD.abrirConexion();
				/*
				 * Utilizamos una bandera para saber en que parte del flujo tenemos que utilizar los codigos de los servicios
				 */

				//Verifica si la conexion esta nula
				if (connection == null)
				{
					// De ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//Se obtiene el paciente cargado en sesion.
				//PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//Obtenemos el valor de la forma.
				TextosRespuestaProcedimientosForm forma = (TextosRespuestaProcedimientosForm) form;

				//Obtenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se obtiene la institucion
				//InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				TextosRespuestaProcedimientos mundo = new TextosRespuestaProcedimientos();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	 EL ESTADO DE TextosRespuestaProcedimientosForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");
				/*-------------------------------------------------------------------
				 * 		ESTADOS PARA TEXTOS RESPUESTA PROCEDIMIENTOS HISTORIA CLINICA
			 -------------------------------------------------------------------*/

				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/

				if (estado == null)
				{
					//inicializamos los valores de los atributos del pager.
					forma.resetpager();
					//se saca un log con el siguiente texto.
					logger.warn(":( PAILAS :( Estado no Valido dentro del Flujo de Textos Respuesta Procedimientos (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);
					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  EMPEZAR
			 ---------------------------------------------*/
				else if (estado.equals("empezar"))
				{ 
					mundo.empezar(connection, forma, usuario);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ORDENAR
			 ---------------------------------------------*/
				else if (estado.equals("ordenar"))
				{
					mundo.ordenar(connection, forma, usuario);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("principal");	
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ORDENAR
			 ---------------------------------------------*/
				else if (estado.equals("ordenarTextos"))
				{
					mundo.ordenarTextos(forma);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("modificacion");	
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  REDIRECCION
			 ---------------------------------------------*/
				else if (estado.equals("redireccion"))
				{
					forma.resetMensaje();
					UtilidadBD.closeConnection(connection);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  NUEVO - CARGARSERVICIO
			 ---------------------------------------------*/
				else if (estado.equals("nuevo") || estado.equals("cargarServicio"))
				{
					if(estado.equals("nuevo"))
					{
						forma.resetMensaje();
					}
					if(estado.equals("cargarServicio"))
					{
						forma.setMensaje(new ResultadoBoolean(true,"SERVICIO CARGADO CON ÉXITO !!!"));
					}
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("nuevo");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  NUEVO TEXTO PROCEDIMIENTO
			 ---------------------------------------------*/
				else if (estado.equals("nuevoTextoProcedimientos"))
				{ 
					forma.resetNuevo();
					forma.resetMensaje();
					UtilidadBD.cerrarConexion(connection);
					this.setBandera(1);
					logger.info("===> Secuencia bandera 1 = nuevoTextoProcedimientos > guardarInsertar; \n ===> bandera ="+this.getBandera());
					return mapping.findForward("nuevo");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  GUARDAR
			 ---------------------------------------------*/
				else if (estado.equals("guardarInsertar"))
				{ 
					ActionErrors errores = new ActionErrors();
					int ban=this.getBandera();
					errores = validarTextosRespuesta(connection, forma, errores, ban); 
					logger.info("===> Aquí está el valor de errores"+errores);
					if(!errores.isEmpty())
					{
						logger.info("===> Entré al if de guardarInsertar, Si hay errores");
						saveErrors(request, errores);
						return mapping.findForward("nuevo");
					}
					else
					{
						logger.info("===> Entré al else de guardarInsertar");
						logger.info("===> La bandera viene en... "+ban);
						mundo.guardarInsertar(connection, forma, usuario, ban);
						//Llenamos el HashMap con los procedimientos de respuesta del servicio seleccionado
						logger.info("===>Código del Servicio: "+forma.getCodigoServicio());
						logger.info("===>Código del Servicio: "+forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex()));
						forma.setTextosProcedimientosServicio(mundo.cargarTextosRespuestaProcedimientos(connection, 
								Utilidades.convertirAEntero(forma.getCodigoServicio()), usuario.getCodigoInstitucionInt()));
						//Utilidades.imprimirMapa(forma.getTextosProcedimientosServicio());
						forma.setEstado("cargarServicio");
					}

					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("nuevo");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  MODIFICAR TEXTO
			 ---------------------------------------------*/
				else if (estado.equals("modificarTexto"))
				{ 
					forma.resetMensaje();
					forma.resetpager();
					mundo.cargar(connection, forma, usuario.getCodigoInstitucionInt());
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("modificacion");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  CARGAR TEXTO RESPUESTA
			 ---------------------------------------------*/
				else if (estado.equals("cargarTextoRespuesta"))
				{
					forma.resetMensaje();
					UtilidadBD.cerrarConexion(connection);
					this.setBandera(3);
					logger.info("===> Secuencia bandera 3 = cargarTextoRespuesta > guardarModificar; \n ===> bandera ="+this.getBandera());
					return mapping.findForward("modificacion");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  GUARDAR MODIFICAR
			 ---------------------------------------------*/
				else if (estado.equals("guardarModificar"))
				{ 
					ActionErrors errores = new ActionErrors();
					int ban=this.getBandera();
					errores = validarTextosRespuesta(connection, forma, errores, ban); 
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						return mapping.findForward("modificacion");
					}
					else
					{
						mundo.guardarModificar(connection, forma, usuario);
						forma.setEstado("modificarTexto");
						forma.resetpager();
					}
					mundo.cargar(connection, forma, usuario.getCodigoInstitucionInt());
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("modificacion");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  NUEVO TEXTO PROCEDIMIENTO
			 ---------------------------------------------*/
				else if (estado.equals("nuevoModificacionTextoProcedimientos"))
				{ 
					forma.resetNuevo();
					forma.resetMensaje();
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("modificacion");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  GUARDAR INSERTAR MODIFICACION
			 ---------------------------------------------*/
				else if (estado.equals("guardarInsertarModificacion"))
				{
					logger.info("===> Entré a guardarInsertarModificacion");
					ActionErrors errores = new ActionErrors();
					this.setBandera(2);
					int ban = this.getBandera();
					errores = validarTextosRespuesta(connection, forma ,errores, ban); 
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						return mapping.findForward("modificacion");
					}
					else
					{
						logger.info("===> Secuencia bandera 2 = guardarInsertarModificacion > guardarInsertar; \n ===> bandera ="+ban);
						mundo.guardarInsertar(connection, forma, usuario, ban);
						forma.setEstado("modificarTexto");
					}
					forma.resetpager();
					mundo.cargar(connection, forma, usuario.getCodigoInstitucionInt());
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("modificacion");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ELIMINAR TEXTO RESPUESTA SERVICIO
			 ---------------------------------------------*/
				else if (estado.equals("eliminarTextoRespuestaServicio"))
				{
					logger.info("===> Voy a Eliminar el registro seleccionado");
					ActionErrors errores = new ActionErrors();
					errores = mundo.eliminar(connection, forma, usuario);
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}
					forma.resetpager();
					forma.reset();
					forma.setTextosRespuestaProcedimientos(mundo.consultaTextosRespuestaProcedimientos(connection, 
							usuario.getCodigoInstitucionInt()));

					mundo.empezar(connection, forma, usuario);

					forma.setMensaje(new ResultadoBoolean(true,"SERVICIO ELIMINADO CON ÉXITO !!!"));

					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ELIMINAR TEXTO RESPUESTA
			 ---------------------------------------------*/
				else if (estado.equals("eliminarTextoRespuesta"))
				{ 
					ActionErrors errores = new ActionErrors();
					errores = mundo.eliminarTexto(connection, forma, usuario);
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}
					forma.setEstado("modificarTexto");
					forma.resetpager();
					mundo.cargar(connection, forma, usuario.getCodigoInstitucionInt());
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("modificacion");
				}

			}
			this.setBandera(0);
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
	}
	
	/**
	 * Método para validar los textos respuesta
	 * @param con Connection
	 * @param forma TextosRespuestaProcedimientosForm
	 * @param errores ActionErrors
	 * @param bandera int
	 * @return errores
	 */
	private ActionErrors validarTextosRespuesta(Connection con, TextosRespuestaProcedimientosForm forma ,ActionErrors errores, int bandera)
	{
		
		int numRegistros = Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+"");
		boolean existeEnBaseDatos = false;
		int ban=bandera;
		
		/*
		 * Validaciones al momento de guardar de un registro nuevo
		 */
		if(forma.getEstado().equals("guardarInsertar"))
		{
			logger.info("===> Entré a guardarInsertar: estado="+forma.getEstado());
			
			/*
			 * Validación para que los textos predeterminandos no sobrepasen los 4000 caracteres
			 * Registros Nuevos
			 */
			HashMap<String, Object> criterios = new HashMap<String, Object>();
			criterios.put("textoPredeterminado", forma.getTextoPredeterminado());
			String textoPred=criterios.get("textoPredeterminado")+"";
			int longitudTexto = textoPred.length();
			logger.info("===> Guardar Insertar: Longitud Texto: "+longitudTexto);
			if(longitudTexto<4000)
			{
				logger.info("===> Guardar Insertar: La longitud del Texto es MENOR que 4000");
				forma.setTextoMenorA4000(true);
			}
			else
			{
				logger.info("===> Guardar Insertar: La longitud del Texto es MAYOR que 4000");
				forma.setTextoMenorA4000(false);
				errores.add("Texto Predeterminado", new ActionMessage(
						"errors.required", "El Texto Predeterminado No Puede Sobrepasar los 4000 Caracteres, "));
			}
			
			if(forma.getDescripcionTexto().trim().equals("") || forma.getDescripcionTexto().trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "La Descripción de Texto "));
			
			if(forma.getTextoPredeterminado().trim().equals("") || forma.getTextoPredeterminado().trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Texto Predeterminado "));
			
			if(bandera == 1)
			{
				logger.info("===> Método ValidarTextosRespuesta La bandera viene en 1 !!!\n el flujo de la secuencia debe de ser... " +
						"nuevoTextoProcedimientos > guardarInsertar");
				logger.info("===> Método ValidarTextosRespuestaCodigo Servicio: "+forma.getCodigoServicio());
				logger.info("===> Aquí está la descripción de textos "+forma.getDescripcionTexto());
				existeEnBaseDatos = Utilidades.existeDescripcionTextoProcedimiento(con, forma.getDescripcionTexto(), 
						forma.getCodigoServicio());
				logger.info("===> Este es como viene existeEnBaseDatos= "+existeEnBaseDatos);
				
				if(existeEnBaseDatos==true)
				{
					logger.info("===> Entré a validar existeEnBaseDatos !!! = "+existeEnBaseDatos);
					errores.add("codigoConcepto", new ActionMessage("error.historiaClinica.duply", "No se puede duplicar "));
				}
			}
			
			if(bandera == 2)
			{
				logger.info("===> Método ValidarTextosRespuesta La bandera viene en 2 !!!\n el flujo de la secuencia debe de ser... " +
						"guardarInsertarModificacion > guardarInsertar");
				logger.info("===> Método ValidarTextosRespuesta Codigo Servicio: "+forma.getTextosRespuestaProcedimientos("servicio_"+
						forma.getIndex()));
			}
		}
		
		else if(forma.getEstado().equals("guardarInsertarModificacion"))
		{
			logger.info("===> Entré a guardarInsertarModificacion: estado="+forma.getEstado());
			logger.info("===> El Código del Servicio es: "+forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex()));
			if(forma.getDescripcionTexto().trim().equals("") || forma.getDescripcionTexto().trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "La Descripción de Texto "));
			if(forma.getTextoPredeterminado().trim().equals("") || forma.getTextoPredeterminado().trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Texto Predeterminado "));
			
			for(int i=0; i<Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+""); i++)
			{
				logger.info("===> Entré al for del estado guardarInsertar o guardarInsertarModificacion \n estado ="+forma.getEstado());
				if((forma.getTextosProcedimientosServicio("descripciontexto_"+i)+"").equals(forma.getDescripcionTexto().trim()))
				{
					logger.info("Entré aquí en el for "+i+" veces");
					errores.add("codigoConcepto", new ActionMessage("error.historiaClinica.duply", "No se puede duplicar "));
					i = Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+"");
				}
			}
			logger.info("===> guardarInsertarModificacion: Texto Predeterminado: "+(forma.getTextoPredeterminado().trim()));
			int longitudTexto = (forma.getTextoPredeterminado().trim()).length();
			logger.info("===> guardarInsertarModificacion: La longitud del Texto Predeterminado Es: "+longitudTexto);
			
			/*
			 * Validación para que los textos predeterminandos no sobrepasen los 4000 caracteres
			 * Registros Nuevos
			 */
			if(longitudTexto<4000)
			{
				logger.info("===> guardarInsertarModificacion: La longitud del Texto es MENOR que 4000");
				forma.setTextoMenorA4000(true);
			}
			else
			{
				logger.info("===> guardarInsertarModificacion: La longitud del Texto es MAYOR que 4000");
				forma.setTextoMenorA4000(false);
				errores.add("Texto Predeterminado", new ActionMessage(
						"errors.required", "El Texto Predeterminado No Puede Sobrepasar los 4000 Caracteres, "));
			}
		}
		
		/*
		 * Validaciones al momento de guardar de un registro modificado
		 */
		else if(forma.getEstado().equals("guardarModificar"))
		{
			logger.info("===> Entré a guardarModificar: estado="+forma.getEstado());
			logger.info("===> El código del servicio es: "+forma.getTextosProcedimientosServicio("servicio_"+forma.getPosicion()));
			if((forma.getTextosProcedimientosServicio("descripciontexto_"+forma.getPosicion())+"").trim().equals("") || 
				(forma.getTextosProcedimientosServicio("descripciontexto_"+forma.getPosicion())+"").trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "La Descripción de Texto "));
			if((forma.getTextosProcedimientosServicio("textopredeterminado_"+forma.getPosicion())+"").trim().equals("") || 
				(forma.getTextosProcedimientosServicio("textopredeterminado_"+forma.getPosicion())+"").trim().equals("null"))
				errores.add("codigoConcepto", new ActionMessage("errors.required", "El Texto Predeterminado "));
			
			/*
			 * Validación para que los textos predeterminandos no sobrepasen los 4000 caracteres
			 * Registros Nuevos
			 */
			int longitudTexto = (forma.getTextosProcedimientosServicio("textopredeterminado_"+forma.getPosicion())+"").length();
			logger.info("===> Guardar Modificar La longitud del Texto Predeterminado Es: "+longitudTexto);
			
			if(longitudTexto<4000)
			{
				logger.info("===> Guardar Modificar La longitud del Texto es MENOR que 4000");
				forma.setTextoMenorA4000(true);
			}
			else
			{
				logger.info("===> Guardar Modificar La longitud del Texto es MAYOR que 4000");
				forma.setTextoMenorA4000(false);
				errores.add("Texto Predeterminado", new ActionMessage(
						"errors.required", "El Texto Predeterminado No Puede Sobrepasar los 4000 Caracteres, "));
			}
			
			int temp = Utilidades.convertirAEntero(forma.getPosicion());
			int i=0, j = 0;
			int numeroRegistros = Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+"");
			logger.info("===> El valor de temp es... "+temp);
			logger.info("===> El número de registros es... "+temp);
			
			for(i=0; i<numeroRegistros; i++)
			{
				if((forma.getTextosProcedimientosServicio("descripciontexto_"+i)+"").equals
						(forma.getTextosProcedimientosServicio("descripciontexto_"+forma.getPosicion())))
				{
					logger.info("===> Mira Aqui Pipe !!! Entré aquí en el for "+i+" veces");
					
					if(bandera==3)
					{
						logger.info("===> Bandera = 3 Método ValidarTextosRespuestaLa bandera viene en 3 !!!" +
								"\n el flujo de la secuencia debe de ser... cargarTextoRespuesta > guardarModificar");
						logger.info("===> Bandera = 3 Método ValidarTextosRespuesta Codigo Servicio: "+
								forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex()));
						
						if(i != temp)
						{
							logger.info("===> i es diferente de temp");
							logger.info("===> valor de i: "+i);
							logger.info("===> valor de temp: "+temp);
							errores.add("codigoConcepto", new ActionMessage("error.historiaClinica.duply", 
									"IMPOSIBLE !!! No se puede duplicar "));
							this.setBandera(4);
							ban=this.getBandera();
						}
					}
					else
					{
						if(ban == 4)
						{
							logger.info("===> La bandera viene en 4, no se muestra error !!! :P");
						}
						else
						{
							errores.add("codigoConcepto", new ActionMessage("error.historiaClinica.duply", "No se puede duplicar "));
							i = Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+"");
						}
					}
				}
			}
		}
		return errores;
	}
	
	/**
	 * Método que verifica si la descripción existe
	 * @param forma
	 * @return false/true
	 */
	public boolean estaDescripcion (TextosRespuestaProcedimientosForm forma)
	{
		for (int i=0;i<Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+"");i++)
		{
			if ((forma.getTextosProcedimientosServicio("descripciontexto_"+i)+"").equals(forma.getDescripcionTexto()))
				return true;
		}
		
		return false;
	}
}