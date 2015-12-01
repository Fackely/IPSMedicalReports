/*
 * @(#)GruposEtareosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.capitacion;

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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.capitacion.GruposEtareosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.GruposEtareos;

/**
 * Clase encargada del control de la funcionalidad de Grupos Etareos

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 26 /May/ 2006
 */
public class GruposEtareosAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(GruposEtareosAction.class);
	
	int maxPageItems = 0;
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
		if(form instanceof GruposEtareosForm)
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
			GruposEtareos mundo =new GruposEtareos();
			GruposEtareosForm forma=(GruposEtareosForm)form;
			
			String estado = forma.getEstado();
			logger.warn("[GruposEtareosAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de GruposEtareosAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
			else if(estado.equals("empezar"))
			{
				forma.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
			}
			else if(estado.equals("resultadoBusqueda"))
			{
				return this.accionBusquedaAvanzada(forma, request, mapping, con, mundo, usuario);
			}
			else if(estado.equals("eliminarDelMapa"))
			{
				return this.accionEliminarDelMapa(forma, mapping, con, mundo, usuario);
			}
			else if(estado.equals("guardar"))
			{
				return this.accionGuardar(forma, mapping, con, mundo, usuario);
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("nuevo"))
			{
				return this.accionAgregarNuevo(forma, response, request, con, usuario);
			}
			else if (estado.equals("continuar"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaPrincipal");	
			}
	    }
		else
		{
			logger.error("El form no es compatible con el form de Grupos Etareos");
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
	 * Action que me resulta de la busuqeda de grupos etareos
	 * En caso de que la consulta no arroje ningun resultdo llenamos el mapa con un registro
	 * para poder ingresar nuevos datos
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(GruposEtareosForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, GruposEtareos mundo, UsuarioBasico usuario) throws SQLException
	{
		/**Validaciones para la búsqueda**/
		ActionErrors errores=new ActionErrors();
		if(forma.getCodigoConvenio()== -1 )
		{
			errores.add("errors.required", new ActionMessage("errors.required", "El Convenio"));
		}
		if(forma.getFechaInicial().trim().equals(""))
		{
			errores.add("errors.required", new ActionMessage("errors.required", "La Fecha Inicial"));
		}
		if(forma.getFechaFinal().trim().equals(""))
		{
			errores.add("errors.required", new ActionMessage("errors.required", "La Fecha Final"));
		}
		if((UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()))<0)
		{
			errores.add("Fecha Final menor a Fecha Incial", new ActionMessage("errors.fechaAnteriorIgualActual","Final", " Inicial"));				
		}
		if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
		{
			errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido",forma.getFechaInicial()));
		}
		if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
		{
			errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido",forma.getFechaFinal()));
		}
		
		if(errores.size()>0)
		{
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("inicioBusqueda");
		}
		else
		{
		
			forma.setMapaGruposEtareos(mundo.consultarGruposEtareos(con,forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
			forma.setMapaGruposEtareosNoModificado(mundo.consultarGruposEtareos(con,forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
			if(Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString()) <= 0)
			{
				forma.setMapaGruposEtareos("numRegistros", 1);
				forma.getMapaGruposEtareos().put("codigogrupoetareo_0", 0);
				forma.getMapaGruposEtareos().put("codigoconvenio_0", forma.getCodigoConvenio());
				forma.getMapaGruposEtareos().put("institucion_0", usuario.getCodigoInstitucionInt());
				forma.getMapaGruposEtareos().put("edadfinal_0", "");
				forma.getMapaGruposEtareos().put("edadinicial_0", "");
				forma.getMapaGruposEtareos().put("fechainicial_0", "");
				forma.getMapaGruposEtareos().put("fechafinal_0", "");
				forma.getMapaGruposEtareos().put("codigosexo_0", "-1");
				forma.getMapaGruposEtareos().put("nombresexo_0", "");
				forma.getMapaGruposEtareos().put("valor_0", "");
				forma.getMapaGruposEtareos().put("pyp_0", "");
				forma.getMapaGruposEtareos().put("existebd_0", "no");
			}
			if(Integer.parseInt(forma.getMapaGruposEtareosNoModificado("numRegistros").toString()) <= 0)
			{
				forma.setMapaGruposEtareosNoModificado("numRegistros", 1);
				forma.getMapaGruposEtareosNoModificado().put("codigogrupoetareo_0", 0);
				forma.getMapaGruposEtareosNoModificado().put("codigoconvenio_0", forma.getCodigoConvenio());
				forma.getMapaGruposEtareosNoModificado().put("institucion_0", usuario.getCodigoInstitucionInt());
				forma.getMapaGruposEtareosNoModificado().put("edadfinal_0", "");
				forma.getMapaGruposEtareosNoModificado().put("edadinicial_0", "");
				forma.getMapaGruposEtareosNoModificado().put("fechainicial_0", "");
				forma.getMapaGruposEtareosNoModificado().put("fechafinal_0", "");
				forma.getMapaGruposEtareosNoModificado().put("codigosexo_0", "-1");
				forma.getMapaGruposEtareosNoModificado().put("nombresexo_0", "");
				forma.getMapaGruposEtareosNoModificado().put("valor_0", "");
				forma.getMapaGruposEtareosNoModificado().put("pyp_0", "");
				forma.getMapaGruposEtareosNoModificado().put("existebd_0", "no");
			}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");		
		}
		
	}
	
	/**
	 * Accion para elimianr un elemento del mapa
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarDelMapa(GruposEtareosForm forma, ActionMapping mapping, Connection con, GruposEtareos mundo, UsuarioBasico usuario) throws SQLException
	{
		
	    int tamanioMapaAntesEliminar=Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"");
	    int eliminacion= 0;
	    String existeBD = forma.getMapaGruposEtareos("existebd_"+forma.getPosicionMapa()).toString();
	    UtilidadBD.iniciarTransaccion(con);
	    
	    if(existeBD.equals("si"))
	    {
				eliminacion = mundo.eliminarGrupoEtareo(con, Integer.parseInt(forma.getMapaGruposEtareos("codigogrupoetareo_"+forma.getPosicionMapa()).toString()));
				if(eliminacion != 1)
				{
					UtilidadBD.abortarTransaccion(con);
					forma.setMensaje("Problemas con la eliminación de los datos. Por favor verifique");
	    			UtilidadBD.closeConnection(con);
	    	        return mapping.findForward("paginaPrincipal");
				}
				else
				{
					//Si lo borra Bien entonces generamos el LOG
					generarLogEliminacion(con, forma, usuario, forma.getPosicionMapa());
				}
	    }
	    
	    //Si pasa todas las validaciones entonces finalizamos la transaccion
	    UtilidadBD.finalizarTransaccion(con);
	    forma.getMapaGruposEtareos().remove("codigogrupoetareo_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("codigoconvenio_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("institucion_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("edadfinal_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("edadinicial_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("fechainicial_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("fechafinal_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("codigosexo_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("nombresexo_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("valor_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("pyp_"+forma.getPosicionMapa());
	    forma.getMapaGruposEtareos().remove("existebd_"+forma.getPosicionMapa());
	    forma.setMapaGruposEtareos("numRegistros",tamanioMapaAntesEliminar-1+"");
	    
	   //El siguiente cod, lo que verifica, es que si eliminan un elemento del mapa y atrás de el existen más datos entonces
	   //se debe correr el index del hash Map de los que estan después del eliminado una posición menos para que existe 
	   //concordancia entre el size real del mapa y el index de elementos
	   int indexEliminado= forma.getPosicionMapa();
	    if(tamanioMapaAntesEliminar>indexEliminado)
	    {
	        for(int k=indexEliminado; k<tamanioMapaAntesEliminar; k++)
	        {
	        	forma.setMapaGruposEtareos("codigogrupoetareo_"+k, forma.getMapaGruposEtareos("codigogrupoetareo_"+(k+1)));
	        	forma.setMapaGruposEtareos("codigoconvenio_"+k, forma.getMapaGruposEtareos("codigoconvenio_"+(k+1)));
	        	forma.setMapaGruposEtareos("institucion_"+k, forma.getMapaGruposEtareos("institucion_"+(k+1)));
	        	forma.setMapaGruposEtareos("edadfinal_"+k, forma.getMapaGruposEtareos("edadfinal_"+(k+1)));
	        	forma.setMapaGruposEtareos("edadinicial_"+k, forma.getMapaGruposEtareos("edadinicial_"+(k+1)));
	        	forma.setMapaGruposEtareos("fechainicial_"+k, forma.getMapaGruposEtareos("fechainicial_"+(k+1)));
	        	forma.setMapaGruposEtareos("fechafinal_"+k, forma.getMapaGruposEtareos("fechafinal_"+(k+1)));
	        	forma.setMapaGruposEtareos("codigosexo_"+k, forma.getMapaGruposEtareos("codigosexo_"+(k+1)));
	        	forma.setMapaGruposEtareos("nombresexo_"+k, forma.getMapaGruposEtareos("nombresexo_"+(k+1)));
	        	forma.setMapaGruposEtareos("valor_"+k, forma.getMapaGruposEtareos("valor_"+(k+1)));
	        	forma.setMapaGruposEtareos("pyp_"+k, forma.getMapaGruposEtareos("pyp_"+(k+1)));
	        	forma.setMapaGruposEtareos("existebd_"+k, forma.getMapaGruposEtareos("existebd_"+(k+1)));
	        	
	        }
	        //Removemos la ultima posicion del mapa que quedo vacias despues de mover los otros registros
	        forma.getMapaGruposEtareos().remove("codigogrupoetareo_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("codigoconvenio_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("institucion_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("edadfinal_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("edadinicial_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("fechainicial_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("fechafinal_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("codigosexo_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("nombresexo_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("valor_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("pyp_"+(tamanioMapaAntesEliminar-1));
		    forma.getMapaGruposEtareos().remove("existebd_"+(tamanioMapaAntesEliminar-1));
	    }
	    
	    //Si borrramos el unico registro del mapa le adicionamos uno nuevo para poder seguir
	    //usando la funcionalidad
	    if(Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString()) <= 0)
		{
	    	int index=Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString());
			forma.getMapaGruposEtareos().put("codigogrupoetareo_0", 0);
			forma.getMapaGruposEtareos().put("codigoconvenio_0", forma.getCodigoConvenio());
			forma.getMapaGruposEtareos().put("institucion_0", usuario.getCodigoInstitucionInt());
			forma.getMapaGruposEtareos().put("edadfinal_0", "");
			forma.getMapaGruposEtareos().put("edadinicial_0", "");
			forma.getMapaGruposEtareos().put("fechainicial_0", "");
			forma.getMapaGruposEtareos().put("fechafinal_0", "");
			forma.getMapaGruposEtareos().put("codigosexo_0", "-1");
			forma.getMapaGruposEtareos().put("nombresexo_0", "");
			forma.getMapaGruposEtareos().put("valor_0", "");
			forma.getMapaGruposEtareos().put("pyp_0", "");
			forma.getMapaGruposEtareos().put("existebd_0", "no");
			index=index+1;
			forma.setMapaGruposEtareos("numRegistros", index+"");
		}
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Action para agregar un nuevo registro
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAgregarNuevo(GruposEtareosForm forma, HttpServletResponse response, HttpServletRequest request, Connection con, UsuarioBasico usuario) throws SQLException
	{
		forma.setMensaje("");
		int index=Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString());
		forma.getMapaGruposEtareos().put("codigogrupoetareo_"+index, 0);
		forma.getMapaGruposEtareos().put("codigoconvenio_"+index, forma.getCodigoConvenio());
		forma.getMapaGruposEtareos().put("institucion_"+index, usuario.getCodigoInstitucionInt());
		forma.getMapaGruposEtareos().put("edadfinal_"+index, "");
		forma.getMapaGruposEtareos().put("edadinicial_"+index, "");
		forma.getMapaGruposEtareos().put("fechainicial_"+index, "");
		forma.getMapaGruposEtareos().put("fechafinal_"+index, "");
		forma.getMapaGruposEtareos().put("codigosexo_"+index, "-1");
		forma.getMapaGruposEtareos().put("nombresexo_"+index, "");
		forma.getMapaGruposEtareos().put("valor_"+index, "");
		forma.getMapaGruposEtareos().put("pyp_"+index, "");
		forma.getMapaGruposEtareos().put("existebd_"+index, "no");
		index=index+1;
		forma.setMapaGruposEtareos("numRegistros", index+"");
		
				
		UtilidadBD.closeConnection(con);
		return this.redireccionColumnaNuevo(con, forma, response, request, "ingresarModificar.do?estado=continuar");
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
	public ActionForward redireccionColumnaNuevo(Connection con, GruposEtareosForm forma, HttpServletResponse response, HttpServletRequest request, String enlace)
	{
		int numRegistros = Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString());
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
	 * Action para guardar los nuevos cgrupos etareos
	 * En su defecto modificar los existentes
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(GruposEtareosForm forma, ActionMapping mapping, Connection con, GruposEtareos mundo, UsuarioBasico usuario) throws SQLException
	{
		int insercion = 0;
		int codigo = 0;
		//Iniciamos la transaccion
		UtilidadBD.iniciarTransaccion(con);
		for(int i = 0 ; i < Integer.parseInt(forma.getMapaGruposEtareos("numRegistros").toString()) ; i++)
		{
			if(!forma.getMapaGruposEtareos("codigogrupoetareo_"+i).toString().equals(""))
    		{
    			codigo = Integer.parseInt(forma.getMapaGruposEtareos("codigogrupoetareo_"+i).toString());
    		}
    		insercion = mundo.insertarGruposEtareos(con, codigo+"", forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt(), forma.getMapaGruposEtareos("edadinicial_"+i).toString(), forma.getMapaGruposEtareos("edadfinal_"+i).toString(), Integer.parseInt(forma.getMapaGruposEtareos("codigosexo_"+i)+""), forma.getMapaGruposEtareos("fechainicial_"+i).toString(), forma.getMapaGruposEtareos("fechafinal_"+i).toString(), forma.getMapaGruposEtareos("valor_"+i).toString(), forma.getMapaGruposEtareos("pyp_"+i).toString());
    		//Generamos el Log de Modificacion
    		generarLogModificacion(con, forma, usuario, i);
    		if(insercion != 1)
    		{
    			UtilidadBD.abortarTransaccion(con);
    			forma.setMensaje("Problemas con la inserción de los datos. Por favor verifique");
    			UtilidadBD.closeConnection(con);
    	        return mapping.findForward("paginaPrincipal");
    		}
	    }
	    
    	UtilidadBD.finalizarTransaccion(con);
    	forma.setMensaje("Proceso realizado con Éxito");
    	//forma.setMapaGruposEtareos(mundo.consultarGruposEtareos(con,forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
    	//forma.setMapaGruposEtareosNoModificado(mundo.consultarGruposEtareos(con,forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
        //return mapping.findForward("paginaPrincipal");
    	return this.accionResumen(forma, mapping, con, "empezar", usuario, mundo);
	}
	
	
	private ActionForward accionResumen (GruposEtareosForm forma, ActionMapping mapping, Connection con, String estado, UsuarioBasico usuario, GruposEtareos mundo) throws SQLException 
	{
		forma.setEstado(estado);
		forma.setMapaGruposEtareos(mundo.consultarGruposEtareos(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
    	forma.setMapaGruposEtareosNoModificado(mundo.consultarGruposEtareos(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
        return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Método para generar los LOGS de los Grupos Etareos
	 * @param forma
	 * @param usuario
	 * @param index
	**/ 
	private void generarLogEliminacion(Connection con, GruposEtareosForm forma,  UsuarioBasico usuario, int index)
	{
		String sexo = forma.getMapaGruposEtareos("nombresexo_"+index)+"";
		if(sexo.equals("null") || sexo.equals("-2"))
		{
			sexo = "Ambos";
		}
		else
		{
			sexo = forma.getMapaGruposEtareos("nombresexo_"+index)+"";
		}
	    String log="\n          =====INFORMACION GRUPO ETAREO ELIMINADO ===== " ;
		log+=
			"\n*  Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(forma.getMapaGruposEtareos("codigoconvenio_"+index).toString()))+"] " +
			"\n*  Edad Inicial  ["+forma.getMapaGruposEtareos("edadinicial_"+index).toString()+"] " +
			"\n*  Edad Final  ["+forma.getMapaGruposEtareos("edadfinal_"+index).toString()+"] " +
			"\n*  Sexo  ["+sexo+"] " +
	    	"\n*  Fecha Inicial  ["+forma.getMapaGruposEtareos("fechainicial_"+index).toString()+"] " +
	    	"\n*  Fecha Final  ["+forma.getMapaGruposEtareos("fechafinal_"+index).toString()+"] " +
	    	"\n*  Valor  ["+forma.getMapaGruposEtareos("valor_"+index).toString()+"] " +
	    	"\n*  %PyP  ["+forma.getMapaGruposEtareos("pyp_"+index).toString()+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logGruposEtareosCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
	}
	
	
	/**
	 * Metodo para generar Log de Modificacion de los Grupos Etareos
	 * @param forma
	 * @param usuario
	 * @param index
	 */
	private void generarLogModificacion(Connection con, GruposEtareosForm forma,  UsuarioBasico usuario, int index)
	{
		String sexo = forma.getMapaGruposEtareosNoModificado("nombresexo_"+index)+"";
		String convenio = forma.getMapaGruposEtareosNoModificado("codigoconvenio_"+index)+"";
		if(convenio.equals("") || convenio.equals("null"))
		{
			convenio = "0";
		}
		if(sexo.equals("null") || sexo.equals("-2"))
		{
			sexo = "Ambos";
		}
		else
		{
			sexo = forma.getMapaGruposEtareosNoModificado("nombresexo_"+index)+"";
		}
	    String log="\n            ==== INFORMACION ORIGINAL GRUPOS ETAREOS ===== ";

	    log+=
			"\n*  Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(convenio))+"] " +
			"\n*  Edad Inicial  ["+forma.getMapaGruposEtareosNoModificado("edadinicial_"+index)+""+"] " +
			"\n*  Edad Final  ["+forma.getMapaGruposEtareosNoModificado("edadfinal_"+index)+""+"] " +
			"\n*  Sexo  ["+sexo+"] " +
	    	"\n*  Fecha Inicial  ["+forma.getMapaGruposEtareosNoModificado("fechainicial_"+index)+""+"] " +
	    	"\n*  Fecha Final  ["+forma.getMapaGruposEtareosNoModificado("fechafinal_"+index)+""+"] " +
	    	"\n*  Valor  ["+forma.getMapaGruposEtareosNoModificado("valor_"+index)+""+"] " +
	    	"\n*  %PyP  ["+forma.getMapaGruposEtareosNoModificado("pyp_"+index)+""+"] " ;
		log+="\n========================================================\n\n\n " ;	
	    	
		log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION GRUPOS ESTAREOS===== " ;
		
		log+=
			"\n*  Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Integer.parseInt(forma.getMapaGruposEtareos("codigoconvenio_"+index).toString()))+"] " +
			"\n*  Edad Inicial  ["+forma.getMapaGruposEtareos("edadinicial_"+index)+""+"] " +
			"\n*  Edad Final  ["+forma.getMapaGruposEtareos("edadfinal_"+index)+""+"] " +
			"\n*  Sexo  ["+sexo+"] " +
	    	"\n*  Fecha Inicial  ["+forma.getMapaGruposEtareos("fechainicial_"+index).toString()+"] " +
	    	"\n*  Fecha Final  ["+forma.getMapaGruposEtareos("fechafinal_"+index)+""+"] " +
	    	"\n*  Valor  ["+forma.getMapaGruposEtareos("valor_"+index)+""+"] " +
	    	"\n*  %PyP  ["+forma.getMapaGruposEtareos("pyp_"+index)+""+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logGruposEtareosCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
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