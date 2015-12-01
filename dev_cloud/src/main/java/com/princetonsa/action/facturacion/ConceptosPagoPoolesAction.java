package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ConceptosPagoPoolesForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConceptosPagoPooles;


/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class ConceptosPagoPoolesAction extends Action
{
	
	/**
	 * Para manjar los logger de la clase ConceptosPagoPoolesAction
	 */
	Logger logger = Logger.getLogger(ConceptosPagoPoolesAction.class);
	
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		Connection connection = null;
		try{
			if (form instanceof ConceptosPagoPoolesForm) 
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
				ConceptosPagoPoolesForm forma = (ConceptosPagoPoolesForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE ConceptosPagoPoolesForm ES ====>> "+forma.getEstado());
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
					if (estado.equals("inicial")||estado.equals("operacionTrue"))
					{ 
						return this.accionEmpezar(connection, forma, mapping, usuario);
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
						return this.accionGuardar(connection, forma, mapping, usuario);

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
						forma.setConceptosPagoPoolesMap(this.accionOrdenarMapa(forma.getConceptosPagoPoolesMap(),forma));				 
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("principal");	

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
	public ActionForward accionEmpezar(Connection connection, ConceptosPagoPoolesForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{				
		forma.reset();
		HashMap parametros = new HashMap();
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		forma.setConceptosPagoPoolesMap(ConceptosPagoPooles.consultarConceptosPagosPooles(connection, parametros));
		
		
		
		for (int i=0;i<Integer.parseInt(forma.getConceptosPagoPoolesMap("numRegistros").toString());i++)
		{
			//Aqui se pregunta si el resgistro tiene permiso de ser borrado o modificado.
			if (Utilidades.esConceptosPagoPoolesUsado(forma.getConceptosPagoPoolesMap("codigoconcepto_"+i).toString(), forma.getConceptosPagoPoolesMap("institucion_"+i).toString()))
				forma.setConceptosPagoPoolesMap("permisomodificarcodigo_"+i, ValoresPorDefecto.getValorFalseParaConsultas());
			else
				forma.setConceptosPagoPoolesMap("permisomodificarcodigo_"+i, ValoresPorDefecto.getValorTrueParaConsultas());
				
		}
		UtilidadBD.closeConnection(connection);
		logger.info("mapa con los conceptos ==> "+forma.getConceptosPagoPoolesMap());
		return mapping.findForward("principal");		
	}
	
	/**
	 * metodo encargado de agregar campos a la forma.
	 * @param forma
	 */
	public ActionForward accionNuevo(Connection connection, ConceptosPagoPoolesForm forma, HttpServletRequest request, HttpServletResponse response, UsuarioBasico usuario)
	{
		UtilidadBD.closeConnection(connection);
		
		int pos = Integer.parseInt(forma.getConceptosPagoPoolesMap("numRegistros").toString());		
		
		forma.setConceptosPagoPoolesMap("codigoconcepto_"+pos, "");
		forma.setConceptosPagoPoolesMap("descripcionconcepto_"+pos, "");
		forma.setConceptosPagoPoolesMap("tipoconcepto_"+pos, "");
		forma.setConceptosPagoPoolesMap("institucion_"+pos, usuario.getCodigoInstitucionInt());
		forma.setConceptosPagoPoolesMap("usuario_"+pos, usuario.getLoginUsuario());
		forma.setConceptosPagoPoolesMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setConceptosPagoPoolesMap("permisomodificarcodigo_"+pos, ValoresPorDefecto.getValorTrueParaConsultas());
		forma.setConceptosPagoPoolesMap("numRegistros",(pos+1)+"");
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getConceptosPagoPoolesMap("numRegistros").toString()), response, request, "conceptosPagoPooles.jsp", true);
	}
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	private ActionForward accionGuardar(Connection connection, ConceptosPagoPoolesForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		String estadoEliminar="ninguno";
		ConceptosPagoPooles mundo = new ConceptosPagoPooles ();
		String [] indices = {"codigoconcepto_","descripcionconcepto_","tipoconcepto_","institucion_",""};
		//logger.info("valor del hasmap conceptosPagoPoolesMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesMap());
		//logger.info("valor del hasmap conceptosPagoPoolesEliminadosMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesEliminadosMap());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getConceptosPagoPoolesEliminadosMap("numRegistros")+"");i++)
		{
			//logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			//logger.info("Hashmap:::::::::::"+forma.getConsentimientoInfEliminadoMap());
			if(eliminarBD(connection, forma, i));			
			{				
				//se genera el log tipo harchivo
				Utilidades.generarLogGenerico(forma.getConceptosPagoPoolesEliminadosMap(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logConceptosPagoPoolesCodigo, indices);
				estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getConceptosPagoPoolesMap("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getConceptosPagoPoolesMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.existeModificacion(connection, forma, mundo, i, usuario))
				{	
												
					/*logger.info("HashMap:::::::::"+forma.getConsentimientoInfMap());
					logger.info("subindice:::::..."+i);*/
					//logger.info("ENTRO A MODIFICAR::::::::.."+forma.getConceptosPagoPoolesMap("codigoconcepto_"+i));
					HashMap tmp = new HashMap();
						
					tmp.put("institucion",forma.getConceptosPagoPoolesMap("institucion_"+i));
					tmp.put("codigoconcepto",forma.getConceptosPagoPoolesMap("codigoconceptoold_"+i));
								
					tmp = mundo.consultarConceptosPagosPooles(connection, tmp);
					
					//logger.info("EL VALOR DE TMP ES "+tmp);
					transacction = modificarBD(connection, forma, i,usuario);
					//se genera el log tipo harchivo
					logger.info("tmp i "+i+"  "+tmp);
					logger.info("forma.getconceptos "+forma.getConceptosPagoPoolesMap());
					Utilidades.generarLogGenerico( forma.getConceptosPagoPoolesMap(),tmp, usuario.getLoginUsuario(), false, i, ConstantesBD.logConceptosPagoPoolesCodigo, indices);
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar			
			else if(!forma.getConceptosPagoPoolesMap("numRegistros").toString().equals("0") && forma.getConceptosPagoPoolesMap("estabd_"+i).equals(ConstantesBD.acronimoNo))
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
			logger.info("----->INSERTO 100% CONCEPTOS PAGO POOLES");
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		if (estadoEliminar=="operacionTrue")
			forma.setEstado("operacionTrue");
		
		return this.accionEmpezar(connection, forma, mapping, usuario);		 		
	}
	
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	public boolean eliminarBD (Connection connection, ConceptosPagoPoolesForm forma,int i)
	{
		//logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		HashMap parametros = new HashMap ();
		ConceptosPagoPooles mundo = new ConceptosPagoPooles  ();
        
		//logger.info("el valor del hashmap es "+forma.getConceptosPagoPoolesEliminadosMap()+" y el valor de la i es "+i);
		
		//iniciamos los datos para la consulta
		parametros.put("institucion",forma.getConceptosPagoPoolesEliminadosMap("institucion_"+i));
		parametros.put("codigoconcepto",forma.getConceptosPagoPoolesEliminadosMap("codigoconcepto_"+i));
		
	
		
				
		return mundo.eliminarConceptosPagosPooles(connection, parametros);
	}
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection connection,ConceptosPagoPoolesForm forma,ConceptosPagoPooles mundo, int pos , UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();
		
		//iniciamos los datos para la consulta
		
			temp.put("codigoconcepto", forma.getConceptosPagoPoolesMap("codigoconceptoold_"+pos));
			temp.put("institucion", usuario.getCodigoInstitucionInt());
		temp = mundo.consultarConceptosPagosPooles(connection, temp);
		
		 //logger.info("HashMap de tmp existeModificacion ::::::"+temp);
		String[] indices = (String[])forma.getConceptosPagoPoolesMap("INDICES_MAPA");		
			
		for(int i=0;i<indices.length;i++)
		{		
			
			if(temp.containsKey(indices[i]+"0")&&forma.getConceptosPagoPoolesMap().containsKey(indices[i]+pos))
			{				
				
				if(!((temp.get(indices[i]+"0").toString().equals((forma.getConceptosPagoPoolesMap(indices[i]+pos)).toString()))))
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
	public boolean modificarBD (Connection connection,ConceptosPagoPoolesForm forma,int i,UsuarioBasico usuario )
	{
		//logger.info("valor de haspmap al entrar"+forma.getConceptosPagoPoolesMap());
		HashMap parametros = new HashMap ();
		ConceptosPagoPooles mundo = new ConceptosPagoPooles ();
		
		
		parametros.put("permisomodificarcodigo",forma.getConceptosPagoPoolesMap("permisomodificarcodigo_"+i));
		parametros.put("codigoconcepto",forma.getConceptosPagoPoolesMap("codigoconcepto_"+i));
		parametros.put("codigoconceptoold",forma.getConceptosPagoPoolesMap("codigoconceptoold_"+i));
		parametros.put("descripcionconcepto",forma.getConceptosPagoPoolesMap("descripcionconcepto_"+i));
		parametros.put("tipoconcepto",forma.getConceptosPagoPoolesMap("tipoconcepto_"+i));
		parametros.put("usuario",usuario.getLoginUsuario());
		parametros.put("institucion", forma.getConceptosPagoPoolesMap("institucion_"+i));
		
		return mundo.modificarConceptosPagosPooles(connection, parametros);
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
	public boolean insertarBD (Connection connection, ConceptosPagoPoolesForm forma,int i,UsuarioBasico usuario)
	{
		 HashMap parametros = new HashMap();
		 ConceptosPagoPooles mundo = new ConceptosPagoPooles ();
		//logger.info("\n\n entro a insertarDB "+forma.getConceptosPagoPoolesMap());	
		//logger.info("\n\n el valor de i  es "+i);	
		 //se inicializa el hashmap para que el sql lo pueda entender
		
			
		parametros.put("codigoconcepto",forma.getConceptosPagoPoolesMap("codigoconcepto_"+i));
		parametros.put("descripcionconcepto",forma.getConceptosPagoPoolesMap("descripcionconcepto_"+i));
		parametros.put("tipoconcepto",forma.getConceptosPagoPoolesMap("tipoconcepto_"+i));
		parametros.put("usuario",forma.getConceptosPagoPoolesMap("usuario_"+i));
		parametros.put("institucion", forma.getConceptosPagoPoolesMap("institucion_"+i));
			
			 //logger.info("\n\n saliendo de insertarDB el valor de temp"+temp);
		return mundo.insertarConceptosPagoPooles(connection, parametros);
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
	private ActionForward accionEliminarCampo(ConceptosPagoPoolesForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		//se almacena el tamaño del HashMap eliminados
		int numRegMapEliminados=Integer.parseInt(forma.getConceptosPagoPoolesEliminadosMap("numRegistros")+"");
		
		//se almacena la ultima posicion del HashMap
		int ultimaPosMapa=(Integer.parseInt(forma.getConceptosPagoPoolesMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getConceptosPagoPoolesMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma. getConceptosPagoPoolesMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setConceptosPagoPoolesEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getConceptosPagoPoolesMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getConceptosPagoPoolesMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setConceptosPagoPoolesEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setConceptosPagoPoolesMap(indices[j]+""+i,forma.getConceptosPagoPoolesMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getConceptosPagoPoolesMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setConceptosPagoPoolesMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getConceptosPagoPoolesMap("numRegistros").toString()), response, request, "conceptosPagoPooles.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	}
	
	
	
	/**
	 * Metodo Que ordena el mapa.
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public HashMap accionOrdenarMapa (HashMap mapaOrdenar, ConceptosPagoPoolesForm forma)
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
