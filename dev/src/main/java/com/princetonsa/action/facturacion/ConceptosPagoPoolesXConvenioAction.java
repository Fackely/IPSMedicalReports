package com.princetonsa.action.facturacion;

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

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ConceptosPagoPoolesXConvenioForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConceptosPagoPoolesXConvenio;





/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class ConceptosPagoPoolesXConvenioAction extends Action
{
	
	/**
	 * Para manjar los logger de la clase CensoCamasAction
	 */
	Logger logger = Logger.getLogger(ConceptosPagoPoolesXConvenioAction.class);
	
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		Connection connection = null;
		try{
		if (form instanceof ConceptosPagoPoolesXConvenioForm) 
		 {
			
			
			connection = UtilidadBD.abrirConexion();
			
			//se verifica si la conexion esta nula
			if (connection == null)
			{
				// de ser asi se envia a una pagina de error. 
				request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			//se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//optenemos el valor de la forma.
			ConceptosPagoPoolesXConvenioForm forma = (ConceptosPagoPoolesXConvenioForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	           EL ESTADO DE ConceptosPagoPoolesXConvenioForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			
			if (estado == null)
			{
				
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  INICIAL
			 ---------------------------------------------*/
			else
				if (estado.equals("inicial"))
				{ 
					return this.accionEmpezar(connection, forma, mapping, usuario);
				}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  BUSCAR
			 ---------------------------------------------*/
			else
				if (estado.equals("buscar")||estado.equals("operacionTrue"))
				{ 
					return this.accionBuscar(connection, forma, mapping, usuario, response, request);
				}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NUEVO
			 ---------------------------------------------*/
				else if (estado.equals("nuevo"))
				{
				
					return this.accionNuevo(connection, forma, request, response, usuario);
				}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  GUARDAR
			 ---------------------------------------------*/
				else if (estado.equals("guardar"))
				{
					return this.accionGuardar(connection, forma, mapping, usuario, response, request);
					
				}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  ELIMINARCAMPO
			 ---------------------------------------------*/
				else if (estado.equals("eliminarCampo"))
				{
					return this.accionEliminarCampo(forma, request, response, mapping, usuario.getCodigoInstitucionInt());
				}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  ORDENAR
			 ---------------------------------------------*/
			else if (estado.equals("ordenar"))//estado encargado de ordenar el HashMap del censo.
				{
					forma.setConceptosPagoPoolesXConvenioMap(this.accionOrdenarMapa(forma.getConceptosPagoPoolesXConvenioMap(),forma));				 
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("principal");	
			
				}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  CONSULTAR
			 ---------------------------------------------*/
			else if (estado.equals("consultar"))//estado encargado de ordenar el HashMap del censo.
				{
					forma.resetpager();
					forma.reset();
					forma.init(connection, usuario);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("consulta");	
			
				}
			else/*----------------------------------------------
				 * ESTADO =================>>>>>  CONSULTACONCEPTO
				 ---------------------------------------------*/
				if (estado.equals("consultarconcepto")||estado.equals("operacionTrue"))
							return this.accionConsultar(connection, forma, mapping, usuario, request);
		
				else /*----------------------------------------------
					 * ESTADO =================>>>>>  ORDENARCONSULTA
					 ---------------------------------------------*/
					if (estado.equals("ordenarconsulta"))//estado encargado de ordenar el HashMap del censo.
				{
					forma.setConceptosPagoPoolesXConvenioMap(this.accionOrdenarMapa(forma.getConceptosPagoPoolesXConvenioMap(),forma));				 
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("consulta");	
			
				}
				else /*----------------------------------------------
					 * ESTADO =================>>>>>  REDIRECCION
					 ---------------------------------------------*/
					if (estado.equals("redireccion"))
					{
						UtilidadBD.closeConnection(connection);
						response.sendRedirect(forma.getLinkSiguiente());
						return null;
					}
			
		
			
		 }
		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
	
	/**
	 * Metodo encargado de Buscar los Conceptos
	 * de pagos de pooles
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param estado
	 * @return
	 */
	public ActionForward accionEmpezar(Connection connection, ConceptosPagoPoolesXConvenioForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{				
		forma.resetpager();
		forma.reset();
		forma.init(connection, usuario);
		
		UtilidadBD.closeConnection(connection);
		//logger.info("mapa con los conceptos ==> "+forma.getConceptosPagoPoolesMap());
		return mapping.findForward("principal");		
	}
	
	
	public ActionForward accionConsultar(Connection connection, ConceptosPagoPoolesXConvenioForm forma, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request)
	{
		
		ActionErrors errores = new ActionErrors();
		
		//validacion
	
			if(forma.getCriteriosBusuqeda("codigopool").toString().trim().equals(""))
			{
				errores.add("seleccion",new ActionMessage("errors.required","El pool"));
				forma.setEstado("consultar");
			}
		//pregunta so errores esta vacio
		if (!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(connection);
			return mapping.findForward("consulta") ;
		}
		
		
		HashMap parametros = new HashMap ();
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		parametros.put("codigopool", forma.getCriteriosBusuqeda("codigopool"));
		if (forma.getCriteriosBusuqeda().containsKey("codigoconvenio"))		
			parametros.put("codigoconvenio", forma.getCriteriosBusuqeda("codigoconvenio"));
		
		forma.setConceptosPagoPoolesXConvenioMap(ConceptosPagoPoolesXConvenio.consultarConceptosPagosPoolesXConvenio(connection, parametros));
	//	logger.info("el hashmap al buscar es "+forma.getConceptosPagoPoolesXConvenioMap());
		
		UtilidadBD.closeConnection(connection);
		
			return mapping.findForward("consulta");	
		
	}
	
	
	
	public ActionForward accionBuscar (Connection connection, ConceptosPagoPoolesXConvenioForm forma, ActionMapping mapping,UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request)
	{
		forma.resetpager();
	
		HashMap parametros = new HashMap ();
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		parametros.put("codigopool", forma.getCriteriosBusuqeda("codigopool"));
		parametros.put("codigoconvenio", forma.getCriteriosBusuqeda("codigoconvenio"));
		
		forma.setConceptosPagoPoolesXConvenioMap(ConceptosPagoPoolesXConvenio.consultarConceptosPagosPoolesXConvenio(connection, parametros));
	//	logger.info("el hashmap al buscar es "+forma.getConceptosPagoPoolesXConvenioMap());
		forma.setConceptosPagoPoolesXConvenioEliminadosMap("numRegistros", 0);
		UtilidadBD.closeConnection(connection);
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getConceptosPagoPoolesXConvenioMap("numRegistros").toString()), response, request, "../conceptosPagoPoolesXConvenio/conceptosPagoPoolesXConvenio.jsp", false);
		
		
	}
	
	
	/**
	 * metodo encargado de agregar campos a la forma.
	 * @param forma
	 */
	public ActionForward accionNuevo(Connection connection, ConceptosPagoPoolesXConvenioForm forma, HttpServletRequest request, HttpServletResponse response, UsuarioBasico usuario)
	{
		UtilidadBD.closeConnection(connection);
		
		int pos = Integer.parseInt(forma.getConceptosPagoPoolesXConvenioMap("numRegistros").toString());		
		
		forma.setConceptosPagoPoolesXConvenioMap("codigopool_"+pos, forma.getCriteriosBusuqeda("codigopool"));
		forma.setConceptosPagoPoolesXConvenioMap("codigoconvenio_"+pos, forma.getCriteriosBusuqeda("codigoconvenio"));
		forma.setConceptosPagoPoolesXConvenioMap("codigoconcepto_"+pos, "");
		forma.setConceptosPagoPoolesXConvenioMap("tipoconcepto_"+pos, "");
		forma.setConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+pos, "");
		forma.setConceptosPagoPoolesXConvenioMap("porcentaje_"+pos, "0");
		forma.setConceptosPagoPoolesXConvenioMap("codigocuentacontable_"+pos, "");
		forma.setConceptosPagoPoolesXConvenioMap("institucion_"+pos, usuario.getCodigoInstitucionInt());
		forma.setConceptosPagoPoolesXConvenioMap("usuario_"+pos, usuario.getLoginUsuario());
		forma.setConceptosPagoPoolesXConvenioMap("estabd_"+pos,ConstantesBD.acronimoNo);
		
		forma.setConceptosPagoPoolesXConvenioMap("numRegistros",(pos+1)+"");
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getConceptosPagoPoolesXConvenioMap("numRegistros").toString()), response, request, "conceptosPagoPoolesXConvenio.jsp", true);
	}
	
	
	
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @param response 
	 * @return ActionForward
	 * */
	private ActionForward accionGuardar(Connection connection, ConceptosPagoPoolesXConvenioForm forma, ActionMapping mapping,UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		String estadoEliminar="ninguno";
		ConceptosPagoPoolesXConvenio mundo = new ConceptosPagoPoolesXConvenio ();
		
		//logger.info("valor del hasmap conceptosPagoPoolesMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesMap());
		//logger.info("valor del hasmap conceptosPagoPoolesEliminadosMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesEliminadosMap());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getConceptosPagoPoolesXConvenioEliminadosMap("numRegistros")+"");i++)
		{
			//logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			//logger.info("Hashmap:::::::::::"+forma.getConsentimientoInfEliminadoMap());
			String []concepto = forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigoconcepto_"+i).toString().split(ConstantesBD.separadorSplit);
			forma.setConceptosPagoPoolesXConvenioEliminadosMap("codigoconcepto_"+i, concepto[0]);
			
			if(eliminarBD(connection, forma, i));			
			{				
				//se genera el log tipo harchivo
				HashMap nombreslog = new HashMap();
				
				//logger.info("el valor de los eliminado es "+forma.getConceptosPagoPoolesXConvenioEliminadosMap());
				//se cargan los valores para los cuales se va a buscar los nombres
				nombreslog.put("codigopool", forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigopool_"+i));
				nombreslog.put("codigoconvenio", forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigoconvenio_"+i));
				nombreslog.put("codigoconcepto", forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigoconcepto_"+i));
				nombreslog.put("codigotiposervicio", forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigotiposervicio_"+i));
				nombreslog.put("codigocuentacontable", forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigocuentacontable_"+i));
				nombreslog.put("institucion", forma.getConceptosPagoPoolesXConvenioEliminadosMap("institucion_"+i));
				
				//se realiza la consulta de nombres para el log.
				nombreslog=mundo.consultarInfoLog(connection, nombreslog);
				
				//se le ingresan los vlaores de codigo y de porcentaje para que sean mostrados dentro del log.
				nombreslog.put("codigoconceptoxconvenio_0", forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigoconceptoxconvenio_"+i));
				nombreslog.put("porcentaje_0", forma.getConceptosPagoPoolesXConvenioEliminadosMap("porcentaje_"+i));
				
				//se envia la informacion al generador de logs.
				Utilidades.generarLogGenerico(nombreslog, null, usuario.getLoginUsuario(), true, 0, ConstantesBD.logConceptosPagoPoolesXConvenioCodigo, (String[])nombreslog.get("INDICES_MAPA"));
				estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getConceptosPagoPoolesXConvenioMap("numRegistros")+"");i++)
		{			
			
			String []concepto = forma.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i).toString().split(ConstantesBD.separadorSplit);
			forma.setConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i, concepto[0]);
			
			//modificar			
				
			if((forma.getConceptosPagoPoolesXConvenioMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.existeModificacion(connection, forma, mundo, i, usuario))
				{	
												
					/*logger.info("HashMap:::::::::"+forma.getConsentimientoInfMap());
					logger.info("subindice:::::..."+i);*/
					//logger.info("ENTRO A MODIFICAR::::::::.."+forma.getConceptosPagoPoolesMap("codigoconcepto_"+i));
					HashMap tmp = new HashMap();
					HashMap nombrelogOri = new HashMap();
					HashMap nombreLogMod = new HashMap();
						
					//se cargan los datos modificados para buscar sus nombres.
					/*logger.info("\n\n******************************************************************");
					logger.info("hashmap de conceptos "+forma.getConceptosPagoPoolesXConvenioMap());
					logger.info("\n\n******************************************************************");*/
					nombreLogMod.put("codigopool",  forma.getConceptosPagoPoolesXConvenioMap("codigopool_"+i));
					nombreLogMod.put("codigoconvenio",  forma.getConceptosPagoPoolesXConvenioMap("codigoconvenio_"+i));
					nombreLogMod.put("codigoconcepto",  forma.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i));
					nombreLogMod.put("codigotiposervicio",  forma.getConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+i));
					nombreLogMod.put("codigocuentacontable", forma.getConceptosPagoPoolesXConvenioMap("codigocuentacontable_"+i));
					nombreLogMod.put("institucion",  forma.getConceptosPagoPoolesXConvenioMap("institucion_"+i));
					
//					se realiza la consulta de nombres para el log.
					nombreLogMod=mundo.consultarInfoLog(connection, nombreLogMod);					
					
//					se le ingresan los vlaores de codigo y de porcentaje para que sean mostrados dentro del log.
					nombreLogMod.put("codigoconceptoxconvenio_0", forma.getConceptosPagoPoolesXConvenioMap("codigoconceptoxconvenio_"+i));
					nombreLogMod.put("porcentaje_0", forma.getConceptosPagoPoolesXConvenioMap("porcentaje_"+i));
					/*
					logger.info("\n\n******************************************************************");
					logger.info("hashmap de nombreLogMod "+nombreLogMod);
					logger.info("\n\n******************************************************************");
					*/
					tmp.put("institucion",forma.getConceptosPagoPoolesXConvenioMap("institucion_"+i));
					
					tmp = mundo.consultarConceptosPagosPoolesXConvenio(connection, tmp);
				/*
					logger.info("\n\n******************************************************************");
					logger.info("hashmap de original  "+tmp);
					logger.info("\n\n******************************************************************");
					*/
					for(int k=0;k<Integer.parseInt(tmp.get("numRegistros")+"");k++)
					{	
						String []conceptotmp = tmp.get("codigoconcepto_"+k).toString().split(ConstantesBD.separadorSplit);
						tmp.put("codigoconcepto_"+k, conceptotmp[0]);
					}
					
//					se cargan los datos modificados para buscar sus nombres.
					nombrelogOri.put("codigopool", tmp.get("codigopool_"+i));
					nombrelogOri.put("codigoconvenio",  tmp.get("codigoconvenio_"+i));
					nombrelogOri.put("codigoconcepto",  tmp.get("codigoconcepto_"+i));
					nombrelogOri.put("codigotiposervicio", tmp.get("codigotiposervicio_"+i));
					nombrelogOri.put("codigocuentacontable",  tmp.get("codigocuentacontable_"+i));
					nombrelogOri.put("institucion",  tmp.get("institucion_"+i));
					
					//se realiza la consulta de nombres para el log.
					nombrelogOri=mundo.consultarInfoLog(connection, nombrelogOri);		
					
//					se le ingresan los vlaores de codigo y de porcentaje para que sean mostrados dentro del log.
					nombrelogOri.put("codigoconceptoxconvenio_0", tmp.get("codigoconceptoxconvenio_"+i));
					nombrelogOri.put("porcentaje_0", tmp.get("porcentaje_"+i));
					
					/*logger.info("\n\n******************************************************************");
					logger.info("hashmap de nombres originales  "+nombrelogOri);
					logger.info("\n\n******************************************************************");
					*/
					//logger.info("EL VALOR DE TMP ES "+tmp);
					transacction = modificarBD(connection, forma, i,usuario);
					//se genera el log tipo harchivo
					Utilidades.generarLogGenerico(nombreLogMod, nombrelogOri, usuario.getLoginUsuario(), false, 0, ConstantesBD.logConceptosPagoPoolesXConvenioCodigo, (String[])nombrelogOri.get("INDICES_MAPA"));
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar			
			else if(!forma.getConceptosPagoPoolesXConvenioMap("numRegistros").toString().equals("0") && forma.getConceptosPagoPoolesXConvenioMap("estabd_"+i).equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar consentimiento informado "+forma.getConceptosPagoPoolesMap()); 
				 transacction = insertarBD(connection, forma, i, usuario);
				 
				 estadoEliminar="operacionTrue";
			}
			
			// logger.info("\n\n valor i >> "+i);
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONCEPTOS PAGO POOLES X CONVENIOS");
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		if (estadoEliminar=="operacionTrue")
			forma.setEstado("operacionTrue");
		
		return this.accionBuscar(connection, forma, mapping, usuario, response, request);		
	}
	
	
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	public boolean eliminarBD (Connection connection, ConceptosPagoPoolesXConvenioForm forma,int i)
	{
		//logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		HashMap parametros = new HashMap ();
		ConceptosPagoPoolesXConvenio mundo = new ConceptosPagoPoolesXConvenio();
        
		//logger.info("el valor del hashmap es "+forma.getConceptosPagoPoolesEliminadosMap()+" y el valor de la i es "+i);
		
		//iniciamos los datos para la consulta
		parametros.put("institucion",forma.getConceptosPagoPoolesXConvenioEliminadosMap("institucion_"+i));
		parametros.put("codigoconceptoxconvenio",forma.getConceptosPagoPoolesXConvenioEliminadosMap("codigoconceptoxconvenio_"+i));
		
		return mundo.eliminarConceptosPagosPoolesXConvenio(connection, parametros);
	}
	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection connection,ConceptosPagoPoolesXConvenioForm forma,ConceptosPagoPoolesXConvenio mundo, int pos , UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();
		
		//iniciamos los datos para la consulta
		
			temp.put("codigoconceptoxconvenio", forma.getConceptosPagoPoolesXConvenioMap("codigoconceptoxconvenio_"+pos));
			temp.put("institucion", usuario.getCodigoInstitucionInt());
		temp = mundo.consultarConceptosPagosPoolesXConvenio(connection, temp);
		
		for(int k=0;k<Integer.parseInt(temp.get("numRegistros")+"");k++)
		{	
			String []conceptotmp = temp.get("codigoconcepto_"+k).toString().split(ConstantesBD.separadorSplit);
			temp.put("codigoconcepto_"+k, conceptotmp[0]);
		}
		 //logger.info("HashMap de tmp existeModificacion ::::::"+temp);
		String[] indices = (String[])forma.getConceptosPagoPoolesXConvenioMap("INDICES_MAPA");		
			
		for(int i=0;i<indices.length;i++)
		{		
			
			if(temp.containsKey(indices[i]+"0")&&forma.getConceptosPagoPoolesXConvenioMap().containsKey(indices[i]+pos))
			{				
				
				if(!((temp.get(indices[i]+"0").toString().equals((forma.getConceptosPagoPoolesXConvenioMap(indices[i]+pos)).toString()))))
				{
					//this.generarlog(forma, temp, usuario, false, pos);
					
					return true;
					
				}				
			}
		}	
		
		return false;		
	}
	
	
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @param i 
	 * @return
	 */
	public boolean modificarBD (Connection connection,ConceptosPagoPoolesXConvenioForm forma,int i,UsuarioBasico usuario )
	{
		//logger.info("valor de haspmap al entrar"+forma.getConceptosPagoPoolesMap());
		HashMap parametros = new HashMap ();
		ConceptosPagoPoolesXConvenio mundo = new ConceptosPagoPoolesXConvenio();
		
		
		parametros.put("codigopool",forma.getCriteriosBusuqeda("codigopool"));
		parametros.put("codigoconvenio",forma.getCriteriosBusuqeda("codigoconvenio"));
		parametros.put("codigoconcepto",forma.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i));
		parametros.put("codigotiposervicio",forma.getConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+i));
		parametros.put("porcentaje",forma.getConceptosPagoPoolesXConvenioMap("porcentaje_"+i));
		parametros.put("usuario", usuario.getLoginUsuario());
		parametros.put("codigoconceptoxconvenio", forma.getConceptosPagoPoolesXConvenioMap("codigoconceptoxconvenio_"+i));
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		parametros.put("codigocuentacontable",forma.getConceptosPagoPoolesXConvenioMap("codigocuentacontable_"+i));
		
		
		
		return mundo.modificarConceptosPagosPoolesXConvenio(connection, parametros);
	}
	
	
	
	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; pordentro el crea un HashMap
	 * con los nuevos datos que se van a ingresar a la BD
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 */
	public boolean insertarBD (Connection connection, ConceptosPagoPoolesXConvenioForm forma,int i,UsuarioBasico usuario)
	{
		 HashMap parametros = new HashMap();
		 ConceptosPagoPoolesXConvenio mundo = new ConceptosPagoPoolesXConvenio();
		//logger.info("\n\n entro a insertarDB "+forma.getConceptosPagoPoolesMap());	
		//logger.info("\n\n el valor de i  es "+i);	
		 //se inicializa el hashmap para que el sql lo pueda entender
		
			
		parametros.put("codigopool",forma.getCriteriosBusuqeda("codigopool"));
		parametros.put("codigoconvenio",forma.getCriteriosBusuqeda("codigoconvenio"));
		parametros.put("codigoconcepto",forma.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i));
		parametros.put("codigotiposervicio",forma.getConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+i));
		parametros.put("porcentaje",forma.getConceptosPagoPoolesXConvenioMap("porcentaje_"+i));
		parametros.put("codigocuentacontable",forma.getConceptosPagoPoolesXConvenioMap("codigocuentacontable_"+i));
		parametros.put("usuario",forma.getConceptosPagoPoolesXConvenioMap("usuario_"+i));
		parametros.put("institucion",forma.getConceptosPagoPoolesXConvenioMap("institucion_"+i));
		
			
			 //logger.info("\n\n saliendo de insertarDB el valor de temp"+temp);
		return mundo.insertarConceptosPagoPoolesXConvenio(connection, parametros);
	}
	
	
	/**
	 * Elimina un Registro del HashMap, 
	 * Adicciona el Registro Eliminado en
	 * el HashMap Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private ActionForward accionEliminarCampo(ConceptosPagoPoolesXConvenioForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		//se almacena el tamaño del HashMap eliminados
		int numRegMapEliminados=Integer.parseInt(forma.getConceptosPagoPoolesXConvenioEliminadosMap("numRegistros")+"");
		
		//se almacena la ultima posicion del HashMap
		int ultimaPosMapa=(Integer.parseInt(forma.getConceptosPagoPoolesXConvenioMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getConceptosPagoPoolesXConvenioMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma. getConceptosPagoPoolesXConvenioMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setConceptosPagoPoolesXConvenioEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getConceptosPagoPoolesXConvenioMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getConceptosPagoPoolesXConvenioMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setConceptosPagoPoolesXConvenioEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setConceptosPagoPoolesXConvenioMap(indices[j]+""+i,forma.getConceptosPagoPoolesXConvenioMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getConceptosPagoPoolesXConvenioMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setConceptosPagoPoolesXConvenioMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getConceptosPagoPoolesXConvenioMap("numRegistros").toString()), response, request, "conceptosPagoPoolesXConvenio.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	}
	
	
	/**
	 * Metodo Que ordena el mapa.
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public HashMap accionOrdenarMapa (HashMap mapaOrdenar, ConceptosPagoPoolesXConvenioForm forma)
	{
		HashMap tmp = new HashMap ();
		tmp=mapaOrdenar;
		
		
		String [] indices = ((String[])mapaOrdenar.get("INDICES_MAPA"));
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));	
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);	
		logger.info("sale de ordenar ");
	
		
		
		
		return mapaOrdenar;
	}
	
	
}