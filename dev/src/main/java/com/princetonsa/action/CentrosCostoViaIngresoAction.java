/*
 * @(#)CentrosCostoViaIngresoAction.java
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.CentrosCostoViaIngresoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.CentrosCostoViaIngreso;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase encargada del control de la funcionalidad de Centros de Costo X Via de Ingreso

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 15 /May/ 2006
 */
public class CentrosCostoViaIngresoAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(CentrosCostoViaIngresoAction.class);
	
	int maxPageItems = 0;
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof CentrosCostoViaIngresoForm)
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

				//maxPageItems = Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
				maxPageItems = 15;
				CentrosCostoViaIngreso mundo =new CentrosCostoViaIngreso();
				CentrosCostoViaIngresoForm forma=(CentrosCostoViaIngresoForm)form;

				String estado = forma.getEstado();
				logger.info("[CentrosCostoViaIngresoAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de CentrosCostoViaIngresoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					forma.setMensajeExitoso(false);
					return this.accionEmpezar(forma, mapping, con, mundo, usuario);
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
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, response);
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionAgregarNuevo(forma, response, request, con);
				}
				else if (estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");	
				}
				else if (estado.equals("cargarTipoPaciente"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");	
				}



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
	 * Accion para empezar cargado todos los centros de costo relacionados a su via de ingreso.
	 * Si no existen resgistros le adicionamos uno para que sea el primero a ingresar 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(CentrosCostoViaIngresoForm forma, ActionMapping mapping,  Connection con, CentrosCostoViaIngreso mundo, UsuarioBasico usuario) throws SQLException
	{
		if(!forma.isMensajeExitoso()){
			forma.resetMensaje();
		}
		forma.reset();
		forma.setMapaCentrosCosto(mundo.consultarCentrosCostoViaIngreso(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));
		forma.setMapaCentrosCostoNoModificado(mundo.consultarCentrosCostoViaIngreso(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));
		if(Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString()) <= 0)
		{
			forma.setMapaCentrosCosto("numRegistros", 1);
			forma.getMapaCentrosCosto().put("codigo_0", "");
			forma.getMapaCentrosCosto().put("codigocentrocosto_0", "-1");
			forma.getMapaCentrosCosto().put("nombrecentrocosto_0", "");
			forma.getMapaCentrosCosto().put("codigoviaingreso_0", "-1");
			forma.getMapaCentrosCosto().put("nombreviaingreso_0", "");
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
		
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
	private ActionForward accionEliminarDelMapa(CentrosCostoViaIngresoForm forma, ActionMapping mapping, Connection con, CentrosCostoViaIngreso mundo, UsuarioBasico usuario) throws SQLException
	{
		forma.resetMensaje();
	    int tamanioMapaAntesEliminar=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"");
	    int eliminacion= 0;
	    String existeBD = forma.getMapaCentrosCosto("existebd_"+forma.getPosicionMapa()).toString();
	    UtilidadBD.iniciarTransaccion(con);
	    
	    if(existeBD.equals("si"))
	    {
		    //Evaluamos si el centro de costo - via ingreso existe en la base de datos
		    if(UtilidadValidacion.existeCentroCostoXViaIngreso(con, Integer.parseInt(forma.getMapaCentrosCosto("codigocentrocosto_"+forma.getPosicionMapa()).toString()), Integer.parseInt(forma.getMapaCentrosCosto("codigoviaingreso_"+forma.getPosicionMapa()).toString()), usuario.getCodigoInstitucionInt()))
		    {
				eliminacion = mundo.eliminarCentroCostoViaIngreso(con, Integer.parseInt(forma.getMapaCentrosCosto("codigo_"+forma.getPosicionMapa()).toString()));
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
	    }
	    
	    //Si pasa todas las validaciones entonces finalizamos la transaccion
	    UtilidadBD.finalizarTransaccion(con);
	    forma.getMapaCentrosCosto().remove("codigo_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("codigocentrocosto_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("nombrecentrocosto_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("codigoviaingreso_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("nombreviaingreso_"+forma.getPosicionMapa());
	    forma.getMapaCentrosCosto().remove("existebd_"+forma.getPosicionMapa());
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
	        	forma.setMapaCentrosCosto("codigocentrocosto_"+k, forma.getMapaCentrosCosto("codigocentrocosto_"+(k+1)));
	        	forma.setMapaCentrosCosto("nombrecentrocosto_"+k, forma.getMapaCentrosCosto("nombrecentrocosto_"+(k+1)));
	        	forma.setMapaCentrosCosto("codigoviaingreso_"+k, forma.getMapaCentrosCosto("codigoviaingreso_"+(k+1)));
	        	forma.setMapaCentrosCosto("nombreviaingreso_"+k, forma.getMapaCentrosCosto("nombreviaingreso_"+(k+1)));
	        	forma.setMapaCentrosCosto("existebd_"+k, forma.getMapaCentrosCosto("existebd_"+(k+1)));
	        	
	        }
	        //Removemos la ultima posicion del mapa que quedo vacias despues de mover los otros registros
	        forma.getMapaCentrosCosto().remove("codigo_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("codigocentrocosto_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("nombrecentrocosto_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("codigoviaingreso_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("nombreviaingreso_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaCentrosCosto().remove("existebd_"+(tamanioMapaAntesEliminar-1));
	    }
	    
	    //Si borrramos el unico registro del mapa le adicionamos uno nuevo para poder seguir
	    //usando la funcionalidad
	    if(Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString())<= 0)
	    {
	    	int index=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString());
			forma.getMapaCentrosCosto().put("codigo_"+index, "0");
			forma.getMapaCentrosCosto().put("codigocentrocosto_"+index, "-1");
			forma.getMapaCentrosCosto().put("nombrecentrocosto_"+index, "");
			forma.getMapaCentrosCosto().put("codigoviaingreso_"+index, "-1");
			forma.getMapaCentrosCosto().put("nombreviaingreso_"+index, "");
			forma.getMapaCentrosCosto().put("existebd_"+index, "no");
			index=index+1;
			forma.setMapaCentrosCosto("numRegistros", index+"");
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
	private ActionForward accionAgregarNuevo(CentrosCostoViaIngresoForm forma, HttpServletResponse response, HttpServletRequest request, Connection con) throws SQLException
	{
		forma.resetMensaje();
	    int index=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString());
		forma.getMapaCentrosCosto().put("codigo_"+index, ""+ConstantesBD.codigoNuncaValido);
		forma.getMapaCentrosCosto().put("codigocentrocosto_"+index, ""+ConstantesBD.codigoNuncaValido);
		forma.getMapaCentrosCosto().put("nombrecentrocosto_"+index, "");
		forma.getMapaCentrosCosto().put("codigoviaingreso_"+index, ""+ConstantesBD.codigoNuncaValido);
		forma.getMapaCentrosCosto().put("nombreviaingreso_"+index, "");
		forma.getMapaCentrosCosto().put("tipopaciente_"+index, "");
		forma.getMapaCentrosCosto().put("existebd_"+index, "no");
		index=index+1;
		forma.setMapaCentrosCosto("numRegistros", index+"");
		
				
		UtilidadBD.closeConnection(con);
		return this.redireccion(con,forma,response,request,request.getContextPath()+"/ingresarModificarCCxViaIngreso/ingresarModificarCCxViaIngreso.jsp");  
		//return this.redireccionColumnaNuevo(con, forma, response, request, "ingresarModificar.do?estado=continuar");
		//return mapping.findForward("paginaPrincipal");
	}
	
	/**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param poolesForm ConceptosCarteraForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param String enlace
     * @return null
     */
    public ActionForward redireccion (	Connection con,CentrosCostoViaIngresoForm forma,
						            					HttpServletResponse response,
						            					HttpServletRequest request, String enlace)
    {
    	int numRegistros = Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString());
		forma.setOffset(((int)((numRegistros-1)/maxPageItems))*maxPageItems);
        if(request.getParameter("ultimaPage")==null)
        {
           if(numRegistros > (forma.getOffset()+maxPageItems))
               forma.setOffset(((int)(numRegistros/maxPageItems))*maxPageItems);
            try 
            {
                response.sendRedirect(enlace+"?pager.offset="+forma.getOffset());
            } catch (IOException e) 
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
       try {
		UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			// @todo Auto-generated catch block
			e.printStackTrace();
		}
       return null;
    }
	
	/**
	 * Action para guardar los nuevos centros de cotos x via de ingreso
	 * En su defecto modificar los existentes
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(CentrosCostoViaIngresoForm forma, ActionMapping mapping, Connection con, CentrosCostoViaIngreso mundo, UsuarioBasico usuario) throws SQLException
	{
		forma.setMensajeExitoso(false);
		int insercion = 0;
		int codigo = 0;
		//Iniciamos la transaccion
		UtilidadBD.iniciarTransaccion(con);
		for(int i = 0 ; i < Integer.parseInt(forma.getMapaCentrosCosto("numRegistros").toString()) ; i++)
		{
			if(!forma.getMapaCentrosCosto("codigo_"+i).toString().equals(""))
    		{
    			codigo = Integer.parseInt(forma.getMapaCentrosCosto("codigo_"+i).toString());
    		}
    		insercion = mundo.insertarCentrosCostoViaIngreso(con, codigo, Integer.parseInt(forma.getMapaCentrosCosto("codigocentrocosto_"+i).toString()), Integer.parseInt(forma.getMapaCentrosCosto("codigoviaingreso_"+i).toString()), usuario.getCodigoInstitucionInt(), forma.getMapaCentrosCosto("tipopaciente_"+i).toString());
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
    	forma.setMensajeExitoso(true);
        //return mapping.findForward("paginaPrincipal");
        return this.accionEmpezar(forma, mapping, con, mundo, usuario);
	}
	
	
	/**
	 * Método para generar los LOGS de los Centros de Costo por Via de Ingreso
	 * @param forma
	 * @param usuario
	 * @param index
	 */
	private void generarLogEliminacion(Connection con, CentrosCostoViaIngresoForm forma,  UsuarioBasico usuario, int index)
	{
	    String log="\n            ==== INFORMACION ORIGINAL CENTROS DE COSTO POR VIA INGRESO ===== ";
	    	log+=
			"\n*  Centro Costo ["+Utilidades.obtenerNombreCentroCosto(con, Integer.parseInt(forma.getMapaCentrosCostoNoModificado("codigocentrocosto_"+index).toString()),usuario.getCodigoInstitucionInt())+"] " +
			"\n*  Vía Ingreso  ["+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getMapaCentrosCostoNoModificado("codigoviaingreso_"+index).toString()))+"] " ;
	    	
		log+="\n          =====INFORMACION DESPUES DE LA ELIMINACION CENTROS DE COSTO POR VIA INGRESO ===== " ;
			log+=
			"\n*  Centro Costo [" + Utilidades.obtenerNombreCentroCosto(con, Integer.parseInt(forma.getMapaCentrosCosto("codigocentrocosto_"+index).toString()),usuario.getCodigoInstitucionInt())+"] " +
			"\n*  Vía Ingreso  [" + Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(forma.getMapaCentrosCosto("codigoviaingreso_"+index).toString()))+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logCentrosCostoXViaIngresoCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
	}
	
	
	/**
	 * Action que permite organizar por cada una de las columnas de la forma
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, CentrosCostoViaIngresoForm forma, HttpServletResponse response) 
    {
		forma.resetMensaje();
        String[] indices={
				            "codigo_", 
				            "codigocentrocosto_", 
				            "nombrecentrocosto_", 
							"codigoviaingreso_",
				            "nombreviaingreso_",
				            "existebd_",
				            "tipopaciente_"
	            		};
        
        int tmp=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"");
        forma.setMapaCentrosCosto(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaCentrosCosto(),Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaCentrosCosto("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return this.redireccionColumna(con, forma, response,"ingresarModificar.do?estado=continuar");
    }
	
	/**
	 * Para quedar en la pagina del pager en la que me encontraba ubicado
	 * en el momento de realizar el ordenamiento
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
	public ActionForward redireccionColumna(Connection con, CentrosCostoViaIngresoForm forma, HttpServletResponse response, String enlace)
    {
            try 
            {
                UtilidadBD.closeConnection(con);
                response.sendRedirect(enlace+"&pager.offset="+forma.getOffset());
            }
            catch (IOException e)
			{
            	e.printStackTrace();
			}
            
         UtilidadBD.closeConnection(con);
         return null;
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