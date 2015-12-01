/*
 * @(#)CentrosCostoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.CentrosCostoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.CentrosCosto;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase encargada del control de la funcionalidad de Centros de Costo

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 10 /May/ 2006
 */
public class CentrosCostoAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(CentrosCostoAction.class);
	boolean esNuevo=false;
	int maxPageItems = 0; 
	
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof CentrosCostoForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session = request.getSession();
				UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
				maxPageItems = Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));

				CentrosCosto mundo =new CentrosCosto();
				CentrosCostoForm forma=(CentrosCostoForm)form;

				String estado = forma.getEstado();
				logger.warn("[CentrosCostoAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de CentrosCostoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{

					forma.reset(usuario.getCodigoInstitucionInt());
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("resultadoBusqueda"))
				{

					return this.accionBusqueda(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("eliminarDelMapa"))
				{
					return this.accionEliminarDelMapa(forma, request, mapping, con, mundo, usuario);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma, request, mapping, con, mundo, usuario);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionAgregarNuevo(forma, con, response, request);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, response);
				}
				else if (estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");	
				}
				//***************ESTADOS FILTROS AJAX*******************************************
				else if (estado.equals("filtroUsoCentroCosto"))
				{
					return accionFiltroUsoCentroCosto(con,forma, response, usuario);
				}
				//***************************************************************************
			}
			else
			{
				logger.error("El form no es compatible con el form de Formato de Impresion de Factura");
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
	 * Método que realiza el filtro de uso de centro de costo
	 * @param con
	 * @param forma
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltroUsoCentroCosto(Connection con, CentrosCostoForm forma, HttpServletResponse response, UsuarioBasico usuario) 
	{
		String usado = ConstantesBD.acronimoNo;
		if(Utilidades.convertirAEntero(forma.getMapaCentrosCosto("codigo_"+forma.getPosicionMapa())+"")>0)
			usado = UtilidadValidacion.esCentroCostoUsado(con,forma.getMapaCentrosCosto("codigo_"+forma.getPosicionMapa()).toString(), usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
				"<id-hidden>esUsadoCentroCosto</id-hidden>" +
				"<contenido-hidden>"+usado+"</contenido-hidden>" +
			"</infoid>" +
			"</respuesta>" ;
		
		forma.setMapaCentrosCosto("esUsado_"+forma.getPosicionMapa(), usado);
		
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
			logger.error("Error al enviar respuesta AJAX en accionFiltroUsoCentroCosto: "+e);
		}
		return null;
	}


	/**
	 * Action para ordenar por cualquiera de las columnas del mapa
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, CentrosCostoForm forma, HttpServletResponse response) 
    {
        String[] indices={
				            "codigo_", 
				            "identificador_", 
				            "descripcion_", 
							"codigotipoarea_",
				            "nombretipoarea_",
				            "manejocamas_",
				            "reg_respx_tercer_",
							"unidadfuncional_",
							"codigo_interfaz_",
							"nombreunidadfuncional_",
							"activo_",
							"esUsado_",
							"existebd_",
							"tipoentidad_"
	            		};
        
        int tmp=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"");
        forma.setMapaCentrosCosto(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaCentrosCosto(),Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaCentrosCosto("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return this.redireccionColumna(con,forma, response,"ingresarModificarCentroCosto.jsp");
    }
	
	/**
	 * Método para que al ordenar se quede posicionado en la página del 
     * pager en la cual se encontraba
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
    public ActionForward redireccionColumna(Connection con, CentrosCostoForm forma, HttpServletResponse response, String enlace)
    {
            try 
            {
                UtilidadBD.closeConnection(con);
                response.sendRedirect(enlace+"?pager.offset="+forma.getOffset());
            }
            catch (IOException e)
			{
            	e.printStackTrace();
			}
            
         UtilidadBD.closeConnection(con);
         return null;
    }
	
	/**
	 * Action para realizar la busqueda de los centros de costo segun el centro de atencion
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusqueda(CentrosCostoForm forma, ActionMapping mapping,  Connection con, CentrosCosto mundo, UsuarioBasico usuario) throws SQLException
	{
		
		logger.info("PASO POR AQUI A");
		forma.setMapaCentrosCosto(mundo.consultarCentrosCostoEsUsado(con, forma.getCodCentroAtencion(), usuario.getCodigoInstitucionInt()));
		logger.info("PASO POR AQUI B");
		forma.setMapaCentrosCostoNoModificado(mundo.consultarCentrosCosto(con, forma.getCodCentroAtencion(), usuario.getCodigoInstitucionInt()));
		logger.info("PASO POR AQUI C");
		forma.setMapaCentrosCostoGeneral(mundo.consultarCentrosCosto(con, ConstantesBD.codigoNuncaValido, usuario.getCodigoInstitucionInt()));
		logger.info("PASO POR AQUI D");
		forma.resetMensaje();
		
		if(Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString()) <= 0)
		{
			forma.setMapaCentrosCosto("codigo_0", "0");
			forma.setMapaCentrosCosto("identificador_0", "");
			forma.setMapaCentrosCosto("descripcion_0", "");
			forma.setMapaCentrosCosto("codigotipoarea_0", "");
			forma.setMapaCentrosCosto("nombretipoarea_0", "");
			forma.setMapaCentrosCosto("manejocamas_0", "false");
			forma.setMapaCentrosCosto("unidadfuncional_0", "");
			forma.setMapaCentrosCosto("nombreunidadfuncional_0", "");
			forma.setMapaCentrosCosto("codigo_interfaz_0", "");
			forma.setMapaCentrosCosto("activo_0", "true");
			forma.setMapaCentrosCosto("reg_respx_tercer_0", ConstantesBD.acronimoNo);
			forma.setMapaCentrosCosto("existebd_0", "no");
			forma.setMapaCentrosCosto("numRegistros", "1");
			forma.setMapaCentrosCosto("esUsado_0", ConstantesBD.acronimoNo);
			forma.setMapaCentrosCosto("tipoentidadejecuta_0", forma.getTipoEntidad());
					
		}
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
		
	}
	
	/**
	 * Action para eliminar del mapa un registo de los centros de costo que se insertan en él
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarDelMapa(CentrosCostoForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, CentrosCosto mundo, UsuarioBasico usuario) throws SQLException
	{
	    int tamanioMapaAntesEliminar=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"");
	    ActionErrors errores=new ActionErrors();
	    int eliminacion= 0;
	    UtilidadBD.iniciarTransaccion(con);
	    int codigo = 0;
	    //String identificador = forma.getMapaCentrosCosto("identificador_"+forma.getPosicionMapa()).toString();
	    //int posG = 0;
	    
	    if(forma.getMapaCentrosCosto().containsKey("codigo_"+forma.getPosicionMapa()))
		{
    		if(!(forma.getMapaCentrosCosto("codigo_"+forma.getPosicionMapa())+"").equals(""))
    		{
    			codigo = Integer.parseInt(forma.getMapaCentrosCosto("codigo_"+forma.getPosicionMapa())+"");
    		}
		}
	    
	    if(codigo != 0)
	    {
	    	
	    	
		    //Evaluamos si el centro de costo existe en la base de datos
		    if((forma.getMapaCentrosCosto("existebd_"+forma.getPosicionMapa())+"").equals("si"))
		    {
		    	//Evaluamos si el Centro de Costo a borrar esta asociado a un usuario x almacen
		    	if(!Utilidades.centroCostoAsociadoUsuarioXAlmacen(con, codigo, usuario.getCodigoInstitucionInt()))
		    	{
		    			mundo.eliminarAlmacen(con, codigo);
		    			eliminacion = mundo.eliminarCentroCosto(con, codigo);
		    			if(eliminacion != 1)
		    			{
		    				forma.setMapaCentrosCosto("esUsado_"+forma.getPosicionMapa(), ConstantesBD.acronimoSi);
		    				errores.add("error.centrosCosto.centroCostoUsado", new ActionMessage("error.centrosCosto.centroCostoUsado",forma.getMapaCentrosCosto("descripcion_"+forma.getPosicionMapa()).toString()));
		    				UtilidadBD.abortarTransaccion(con);
		    			}
		    	}
		    	else
		    	{
		    		//El centro de costo esta asociado a un usuario por almacen en inventario
		    		errores.add("Centro Costo asociado usuarioXalmacen", new ActionMessage("error.centrosCosto.centroCostoAsociadoUsuarioXAlmacen", forma.getMapaCentrosCosto("descripcion_"+forma.getPosicionMapa()).toString()));
		    		UtilidadBD.abortarTransaccion(con);
		    	}
		    }
	    		
	    }
	    
	    logger.info("\n\nmapa 01->"+forma.getMapaCentrosCosto());
	    if(errores.size()>0)
		{
	    	UtilidadBD.abortarTransaccion(con);
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");
		}
	    
	    //Si pasa todas las validaciones entonces finalizamos la transaccion
	    UtilidadBD.finalizarTransaccion(con);
	    forma.getMapaCentrosCosto().remove("codigo_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("identificador_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("descripcion_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("codigotipoarea_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("nombretipoarea_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("manejocamas_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("unidadfuncional_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("nombreunidadfuncional_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("codigo_interfaz_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("activo_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("esUsado_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("existebd"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("tipoentidadejecuta_"+forma.getPosicionMapa());
	    forma.setMapaCentrosCosto("numRegistros",tamanioMapaAntesEliminar-1+"");
	    
	   //El siguiente cod, lo que verifica, es que si eliminan un elemento del mapa y atrás de el existen más datos entonces
	   //se debe correr el index del hash Map de los que estan después del eliminado una posición menos para que existe 
	   //concordancia entre el size real del mapa y el index de elementos
	   int indexEliminado= forma.getPosicionMapa();
	    if(tamanioMapaAntesEliminar>indexEliminado)
	    {
	        for(int k=indexEliminado; k<tamanioMapaAntesEliminar; k++)
	        {
	        	forma.setMapaCentrosCosto("codigo_"+k, forma.getMapaCentrosCosto("codigo_"+(k+1)));
	        	forma.setMapaCentrosCosto("identificador_"+k, forma.getMapaCentrosCosto("identificador_"+(k+1)));
	        	forma.setMapaCentrosCosto("descripcion_"+k, forma.getMapaCentrosCosto("descripcion_"+(k+1)));
	        	forma.setMapaCentrosCosto("codigotipoarea_"+k, forma.getMapaCentrosCosto("codigotipoarea_"+(k+1)));
	        	forma.setMapaCentrosCosto("nombretipoarea_"+k, forma.getMapaCentrosCosto("nombretipoarea_"+(k+1)));
	        	forma.setMapaCentrosCosto("manejocamas_"+k, forma.getMapaCentrosCosto("manejocamas_"+(k+1)));
	        	forma.setMapaCentrosCosto("unidadfuncional_"+k, forma.getMapaCentrosCosto("unidadfuncional_"+(k+1)));
	        	forma.setMapaCentrosCosto("nombreunidadfuncional_"+k, forma.getMapaCentrosCosto("nombreunidadfuncional_"+(k+1)));
	        	forma.setMapaCentrosCosto("codigo_interfaz_"+k, forma.getMapaCentrosCosto("codigo_interfaz_"+(k+1)));
	        	forma.setMapaCentrosCosto("activo_"+k, forma.getMapaCentrosCosto("activo_"+(k+1)));
	        	forma.setMapaCentrosCosto("esUsado_"+k, forma.getMapaCentrosCosto("esUsado_"+(k+1)));
	        	forma.setMapaCentrosCosto("existebd_"+k, forma.getMapaCentrosCosto("existebd_"+(k+1)));
	        	forma.setMapaCentrosCosto("tipoentidadejecuta_"+k, forma.getMapaCentrosCosto("tipoentidadejecuta_"+(k+1)));
	        	
	        }
	        //Removemos la ultima posicion del mapa que quedo vacias despues de mover los otros registros
	        forma.getMapaCentrosCosto().remove("codigo_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("identificador_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("descripcion_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("codigotipoarea_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("nombretipoarea_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("manejocamas_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("unidadfuncional_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("nombreunidadfuncional_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("codigo_interfaz_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("activo_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("esUsado_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("existebd_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("tipoentidadejecuta_"+(tamanioMapaAntesEliminar-1));
	    }
	    
	    //Vuelve y se carga el listado General de Centros de Costo
	    forma.setMapaCentrosCostoGeneral(mundo.consultarCentrosCosto(con, ConstantesBD.codigoNuncaValido, usuario.getCodigoInstitucionInt()));
	    
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Action para guardar los centros de costo
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(CentrosCostoForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, CentrosCosto mundo, UsuarioBasico usuario) throws SQLException
	{
		//Por si se presenta algun error de validacion en el momento de guardar
		ActionErrors errores = new ActionErrors();
		boolean activo = false;
		String tempActivo = "";
		boolean manejoCamas = false;
		String tempManejoCamas = "";
		int codigo = 0;
		
		//Iniciamos la transaccion
		UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getMapaCentrosCosto("numRegistros")+"");
		
		
	
		
		//Utilidades.imprimirMapa(forma.getMapaCentrosCosto());
		for(int i = 0 ; i < numReg ; i++)
		{
			
			manejoCamas = false;
			activo = false;
			
			//Buscamos como esta el valor de activo para hacer el cast a boolean
			//logger.info("\nEl activo->"+forma.getMapaCentrosCosto("activo_"+i).toString());
			tempActivo = forma.getMapaCentrosCosto("activo_"+i).toString();
			if(tempActivo.equals("true"))
			{
				activo = true;
			}
			//Buscamos como esta el valor del manejo de camas para hacer el cast a boolean
			//logger.info("\nEl manejo camas->"+forma.getMapaCentrosCosto("manejocamas_"+i).toString());
			tempManejoCamas = forma.getMapaCentrosCosto("manejocamas_"+i).toString();
			if(tempManejoCamas.equals("true")||tempManejoCamas.equals("1"))
			{
				manejoCamas = true;
			}
			//logger.info("El codigo->"+codigo);
			if(forma.getMapaCentrosCosto().containsKey("codigo_"+i))
			{
	    		if(!forma.getMapaCentrosCosto("codigo_"+i).toString().equals(""))
	    		{
	    			codigo = Integer.parseInt(forma.getMapaCentrosCosto("codigo_"+i).toString());
	    		}
			}
			
			//Validamos que si lo que se va a guardar por centro de costo es activo =  false debemos validar si esta relacionado a usuarios / usuariosXalmacen
			if(!activo&&forma.getMapaCentrosCosto("existebd_"+i).toString().equals("si"))
			{
				if(!Utilidades.centroCostoAsociadoUsuarioXAlmacen(con, codigo, usuario.getCodigoInstitucionInt()))
		    	{
		    		mundo.insertarCentrosCosto(con, codigo, forma.getMapaCentrosCosto("descripcion_"+i).toString(), Integer.parseInt(forma.getMapaCentrosCosto("codigotipoarea_"+i).toString()),forma.getMapaCentrosCosto("reg_respx_tercer_"+i).toString() ,usuario.getCodigoInstitucionInt(),activo,forma.getMapaCentrosCosto("identificador_"+i).toString(),manejoCamas, forma.getMapaCentrosCosto("unidadfuncional_"+i).toString(),forma.getMapaCentrosCosto("codigo_interfaz_"+i).toString(),forma.getCodCentroAtencion(),forma.getMapaCentrosCosto("tipoentidad_"+i).toString());
		    		generarLogModificacion(forma, usuario, i);
		    		//logger.info("\n\n\n -------------- 1");
		    	}
		    	else
		    	{
		    		//El centro de costo esta asociado a un usuario por almacen en inventario
		    		errores.add("Centro Costo asociado usuarioXalmacen", new ActionMessage("error.centrosCosto.centroCostoAsociadoUsuarioXAlmacenInactivar", forma.getMapaCentrosCosto("descripcion_"+i).toString()));
		    		//UtilidadBD.abortarTransaccion(con);
		    		//logger.info("\n\n\n -------------- 2");
		    	}
			}
			else
			{

				mundo.insertarCentrosCosto(
						con, 
						codigo,
						forma.getMapaCentrosCosto("descripcion_"+i).toString(),
						Integer.parseInt(forma.getMapaCentrosCosto("codigotipoarea_"+i).toString()),
						forma.getMapaCentrosCosto("reg_respx_tercer_"+i).toString(),
						usuario.getCodigoInstitucionInt(),
						activo,
						forma.getMapaCentrosCosto("identificador_"+i).toString()
						,manejoCamas,
						forma.getMapaCentrosCosto("unidadfuncional_"+i).toString(),
						forma.getMapaCentrosCosto("codigo_interfaz_"+i).toString(),
						forma.getCodCentroAtencion(),
						forma.getMapaCentrosCosto("tipoentidad_"+i).toString());
				
				
	    		generarLogModificacion(forma, usuario, i);
	    		//logger.info("\n\n\n -------------- 3");
			}
	    }
		
		//logger.info("Lo errores de la insercion->"+errores.size());
	    if(errores.size()>0)
		{
	    	UtilidadBD.abortarTransaccion(con);
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");
		}
	    else
	    {
	    	UtilidadBD.finalizarTransaccion(con);
	    	forma.setMensaje("Proceso realizado con Éxito");
	    	forma.reset(usuario.getCodigoInstitucionInt());
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");
	    }
		
	}
	
	/**
	 * Accion para agragr un nuevo centro de costo al mapa de los centros de costo
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAgregarNuevo(CentrosCostoForm forma, Connection con, HttpServletResponse response, HttpServletRequest request) throws SQLException
	{
	    int index=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString());
		forma.getMapaCentrosCosto().put("codigo_"+index, "0");
		forma.getMapaCentrosCosto().put("identificador_"+index, "");
		forma.getMapaCentrosCosto().put("descripcion_"+index, "");
		forma.getMapaCentrosCosto().put("codigotipoarea_"+index, "");
		forma.getMapaCentrosCosto().put("nombretipoarea_"+index, "");
		forma.getMapaCentrosCosto().put("manejocamas_"+index, "false");
		forma.getMapaCentrosCosto().put("unidadfuncional_"+index, "");
		forma.getMapaCentrosCosto().put("nombreunidadfuncional_"+index, "");
		forma.getMapaCentrosCosto().put("codigo_interfaz_"+index, "");
		forma.getMapaCentrosCosto().put("activo_"+index, "true");
		forma.getMapaCentrosCosto().put("existebd_"+index, "no");
		forma.getMapaCentrosCosto().put("reg_respx_tercer_"+index,ConstantesBD.acronimoNo);		
		forma.getMapaCentrosCosto().put("esUsado_"+index, ConstantesBD.acronimoNo);
		forma.getMapaCentrosCosto().put("tipoentidad_"+index,forma.getTipoEntidad());
	
	
		index=index+1;
		forma.setMapaCentrosCosto("numRegistros", index+"");
				
		UtilidadBD.closeConnection(con);
		return this.redireccionColumnaNuevo(con, forma, response, request, "ingresarModificarCentroCosto.do?estado=continuar");
	}
	
	
	/**
	 * Action para redireccionar y que me deje en la pagina del pager correspondiente
	 * @param con
	 * @param forma
	 * @param response
	 * @param request
	 * @param enlace
	 * @return
	 */
	public ActionForward redireccionColumnaNuevo(Connection con, CentrosCostoForm forma, HttpServletResponse response, HttpServletRequest request, String enlace)
	{
		int numRegistros = Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString());
		forma.setOffset(((int)((numRegistros-1)/maxPageItems))*maxPageItems);
		if(request.getParameter("ultimaPage")==null)
		{
			if(numRegistros > (forma.getOffset()+maxPageItems))
				forma.setOffset(((int)(numRegistros)/maxPageItems)*maxPageItems);
			
			try 
			{
				response.sendRedirect(enlace+"&pager.offset="+forma.getOffset());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{    
			String ultimaPagina=request.getParameter("ultimaPage");
			String tempOffset="offset=";
			int posOffSet=ultimaPagina.indexOf(tempOffset)+tempOffset.length();
			if(numRegistros>(forma.getOffset()+maxPageItems))
				forma.setOffset(forma.getOffset()+maxPageItems);
			try 
			{
				response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * Metodo para generar Log de Modificacion de los centros de costo
	 * @param forma
	 * @param usuario
	 * @param index
	 */
	private void generarLogModificacion(CentrosCostoForm forma,  UsuarioBasico usuario, int index)
	{
	    String log="\n            ==== INFORMACION ORIGINAL CENTROS DE COSTO ===== ";

	    	log+=
		    "\n*  Código [" + forma.getMapaCentrosCostoNoModificado("codigo_"+index) +"] "+
			"\n*  Identificador ["+forma.getMapaCentrosCostoNoModificado("identificador_"+index)+"] " +
			"\n*  Tipo Area ["+forma.getMapaCentrosCostoNoModificado("nombretipoarea_"+index)+"] " +
			"\n*  Manejo Camas ["+forma.getMapaCentrosCostoNoModificado("manejocamas_"+index)+"] " +
			"\n*  Unidad Funcional ["+forma.getMapaCentrosCostoNoModificado("nombreunidadfuncional_"+index)+"] " +
			"\n*  Unidad Funcional ["+forma.getMapaCentrosCostoNoModificado("codigo_interfaz_"+index)+"] " +
			"\n*  Tipo Entidad ["+forma.getMapaCentrosCostoNoModificado("tipoentidad_"+index)+"] " +
			"\n*  Activo ["+forma.getMapaCentrosCostoNoModificado("activo_"+index)+"] " ;
	    	
	    	
		log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION CENTROS DE COSTO===== " ;
		
			log+=
			"\n*  Código [" + forma.getMapaCentrosCosto("codigo_"+index) +"] "+
			"\n*  Identificador ["+forma.getMapaCentrosCosto("identificador_"+index)+"] " +
			"\n*  Tipo Area ["+forma.getMapaCentrosCosto("nombretipoarea_"+index)+"] " +
			"\n*  Manejo Camas ["+forma.getMapaCentrosCosto("manejocamas_"+index)+"] " +
			"\n*  Unidad Funcional ["+forma.getMapaCentrosCosto("nombreunidadfuncional_"+index)+"] " +
			"\n*  Unidad Funcional ["+forma.getMapaCentrosCostoNoModificado("codigo_interfaz_"+index)+"] " +
			"\n*  Tipo Entidad ["+forma.getMapaCentrosCostoNoModificado("tipoentidad_"+index)+"] " +
			"\n*  Activo ["+forma.getMapaCentrosCosto("activo_"+index)+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logCentrosCostoCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
	}
	
	
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
	
}