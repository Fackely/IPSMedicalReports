/*
 * 
 * Clase para el manejo del flujo de CajasCajeros
 * 
 */
package com.princetonsa.action.tesoreria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.CajasCajerosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.CajasCajeros;

/**
 * 
 * @author artotor
 *
 */
public class CajasCajerosAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CajasCajerosAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {

		Connection con = null;
		try{

			if(form instanceof CajasCajerosForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CajasCajerosForm forma = (CajasCajerosForm)form;
				CajasCajeros mundo= new CajasCajeros();
				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				forma.setMensaje(new ResultadoBoolean(false));
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				logger.warn("estado [CajasCajerosAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					ValoresPorDefecto.cargarValoresIniciales(con);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					//-----Se asigna el centro de atención del usuario por defecto cuando empieza ---------//
					forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
					this.accionCargarMapaBaseDatos(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), 
							forma.getMaxPageItems(), 
							forma.getNumRegistros(), 
							response, 
							request, 
							"cajasCajeros.jsp", 
							false);
					//return mapping.findForward("paginaPrincipal");
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					return this.ingresarNuevo(con,forma,usuario,request,response);
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(con,forma,mapping,request,response);
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("guardar"))
				{
					//----Se guarda el centro de atención temporalmente para después de guardar poder consultar -----//
					int centroAtencionTemp=forma.getCentroAtencion();
					this.accionGuardar(con,forma,mundo,usuario);
					//repetir la consulata y le que se hace en estado empezar
					forma.reset();
					mundo.reset();
					forma.setCentroAtencion(centroAtencionTemp);
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), 
							forma.getMaxPageItems(), 
							forma.getNumRegistros(), 
							response, 
							request, 
							"cajasCajeros.jsp", 
							false);
					//return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cargarCajasCajeroXCentroAtencion"))
				{
					forma.resetPager(); 
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);				 
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), 
							forma.getMaxPageItems(), 
							forma.getNumRegistros(), 
							response, 
							request, 
							"cajasCajeros.jsp", 
							false);
					//return mapping.findForward("paginaPrincipal");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConceptosCarteraForm");
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
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, CajasCajerosForm forma, CajasCajeros mundo, UsuarioBasico usuario)
	{
		int numReg=forma.getNumRegistros();
		int regEliminados=Integer.parseInt(forma.getMapaCajasCajerosEliminado("registroseliminados")==null?"0":forma.getMapaCajasCajerosEliminado("registroseliminados")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,Integer.parseInt(forma.getMapaCajasCajerosEliminado("consecutivocajaEliminado_"+el)+""),forma.getMapaCajasCajerosEliminado("logincajeroEliminado_"+el)+"");
					this.generarLog(forma,usuario,true, el);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DE LA CAJA CON CONSECUTIVO "+forma.getMapaCajasCajerosEliminado("consecutivocajaEliminado_"+el)+" usuario ---> "+forma.getMapaCajasCajerosEliminado("logincajeroEliminado_"+el));
					el=regEliminados;
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getMapaCajasCajeros("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		if(registroModificado(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistro(con,Integer.parseInt(forma.getMapaCajasCajeros("consecutivocaja_"+k)+""),forma.getMapaCajasCajeros("logincajero_"+k)+"",UtilidadTexto.getBoolean(forma.getMapaCajasCajeros("activo_"+k)+""));
		        			this.generarLog(forma,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getMapaCajasCajeros("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistro(con,Integer.parseInt(forma.getMapaCajasCajeros("consecutivocaja_"+k)+""),forma.getMapaCajasCajeros("logincajero_"+k)+"",UtilidadTexto.getBoolean(forma.getMapaCajasCajeros("activo_"+k)+""),usuario.getCodigoInstitucionInt());
		        	}
	        	}
	        	else
	        	{
	        		logger.error("error modificando - insertando el registro ---> "+forma.getMapaCajasCajeros("tiporegistro_"+k)+" caja-->"+forma.getMapaCajasCajeros("caja_"+k));
	        		k=numReg;
	        	}
	        }			
		}
		if(numReg > 0)
		{
			if(enTransaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true, "OPERACIÓN REALIZADA CON ÉXITO"));
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true, "OPERACIÓN NO EXITOSA. POR FAVOR VERIFICAR"));
				UtilidadBD.abortarTransaccion(con);
			}
		}
		else
			UtilidadBD.finalizarTransaccion(con);
	}
	
	
	/**
	 * @param forma
	 * @param usuario
	 * @param b
	 * @param el
	 */
	private void generarLog(CajasCajerosForm forma, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Caja [" +forma.getMapaCajasCajerosEliminado("cajaEliminado_"+pos)+""+"] "+
			 "\n*  Cajero ["+forma.getMapaCajasCajerosEliminado("logincajeroEliminado_"+pos)+""+"] " +
			 "\n*  Activo ["+forma.getMapaCajasCajerosEliminado("activoEliminado_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Caja [" +forma.getMapaCajasCajeros("caja_"+pos)+""+"] "+
			 "\n*  Cajero ["+forma.getMapaCajasCajeros("logincajero_"+pos)+""+"] " +
			 "\n*  Activo ["+(!UtilidadTexto.getBoolean(forma.getMapaCajasCajeros("activo_"+pos)+""))+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Caja [" +forma.getMapaCajasCajeros("caja_"+pos)+""+"] "+
			 "\n*  Cajero ["+forma.getMapaCajasCajeros("logincajero_"+pos)+""+"] " +
			 "\n*  Activo ["+forma.getMapaCajasCajeros("activo_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logCajasCajerosCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param k
	 * @return
	 */
	private boolean registroModificado(Connection con, CajasCajerosForm forma, CajasCajeros mundo, int pos) 
	{
		return mundo.existeModificacion(con,Integer.parseInt(forma.getMapaCajasCajeros("consecutivocaja_"+pos)+""),forma.getMapaCajasCajeros("logincajero_"+pos)+"",UtilidadTexto.getBoolean(forma.getMapaCajasCajeros("activo_"+pos)+""));
	}

	/**
	 * @param forma
	 */
	private void accionOrdenar(CajasCajerosForm forma) 
	{
		int numReg=forma.getNumRegistros();
		String[] indices={"consecutivocaja_","codigocaja_","caja_","logincajero_","cajero_","activo_","institucion_","existerelacion_","tiporegistro_"};
		forma.setMapaCajasCajeros(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaCajasCajeros(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

	/**
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, CajasCajerosForm forma, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) 
	{
		int pos=forma.getRegEliminar();
		int numReg=forma.getNumRegistros();
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getMapaCajasCajerosEliminado("registroseliminados")==null?"0":forma.getMapaCajasCajerosEliminado("registroseliminados")+"");
		if((forma.getMapaCajasCajeros("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setMapaCajasCajerosEliminado("consecutivocajaEliminado_"+regEliminados,forma.getMapaCajasCajeros("consecutivocaja_"+pos));
			forma.setMapaCajasCajerosEliminado("codigocajaEliminado_"+regEliminados,forma.getMapaCajasCajeros("codigocaja_"+pos));
			forma.setMapaCajasCajerosEliminado("cajaEliminado_"+regEliminados,forma.getMapaCajasCajeros("caja_"+pos));
			forma.setMapaCajasCajerosEliminado("logincajeroEliminado_"+regEliminados,forma.getMapaCajasCajeros("logincajero_"+pos));
			forma.setMapaCajasCajerosEliminado("cajeroEliminado_"+regEliminados,forma.getMapaCajasCajeros("cajero_"+pos));
			forma.setMapaCajasCajerosEliminado("activoEliminado_"+regEliminados,forma.getMapaCajasCajeros("activo_"+pos));
			forma.setMapaCajasCajerosEliminado("institucionEliminado_"+regEliminados,forma.getMapaCajasCajeros("institucion_"+pos));
			forma.setMapaCajasCajerosEliminado("existerelacionEliminado_"+regEliminados,forma.getMapaCajasCajeros("existerelacion_"+pos));
			forma.setMapaCajasCajerosEliminado("tiporegistroEliminado_"+regEliminados,forma.getMapaCajasCajeros("tiporegistro_"+pos));
			forma.setMapaCajasCajerosEliminado("registroseliminados",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setMapaCajasCajeros("consecutivocaja_"+i,forma.getMapaCajasCajeros("consecutivocaja_"+(i+1)));
			forma.setMapaCajasCajeros("codigocaja_"+i,forma.getMapaCajasCajeros("codigocaja_"+(i+1)));
			forma.setMapaCajasCajeros("caja_"+i,forma.getMapaCajasCajeros("caja_"+(i+1)));
			forma.setMapaCajasCajeros("logincajero_"+i,forma.getMapaCajasCajeros("logincajero_"+(i+1)));
			forma.setMapaCajasCajeros("cajero_"+i,forma.getMapaCajasCajeros("cajero_"+(i+1)));
			forma.setMapaCajasCajeros("activo_"+i,forma.getMapaCajasCajeros("activo_"+(i+1)));
			forma.setMapaCajasCajeros("institucion_"+i,forma.getMapaCajasCajeros("institucion_"+(i+1)));
			forma.setMapaCajasCajeros("existerelacion_"+i,forma.getMapaCajasCajeros("existerelacion_"+(i+1)));
			forma.setMapaCajasCajeros("tiporegistro_"+i,forma.getMapaCajasCajeros("tiporegistro_"+(i+1)));
		}
		forma.getMapaCajasCajeros().remove("consecutivocaja_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("codigocaja_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("caja_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("logincajero_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("cajero_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("activo_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("institucion_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("existerelacion_"+ultimoRegistro);
		forma.getMapaCajasCajeros().remove("tiporegistro_"+ultimoRegistro);
		//actualizando numero de registros.
		forma.setNumRegistros(ultimoRegistro);
		if(forma.getRegEliminar()==ultimoRegistro)
		{
			return this.redireccion(con,forma,response,request,"cajasCajeros.jsp");  
		}
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			// @todo Auto-generated catch block
			e.printStackTrace();
		}
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param response
	 */
	private ActionForward ingresarNuevo(Connection con, CajasCajerosForm forma, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		forma.setMapaCajasCajeros("consecutivocaja_"+forma.getNumRegistros(),"");
		forma.setMapaCajasCajeros("codigocaja_"+forma.getNumRegistros(),"");
		forma.setMapaCajasCajeros("caja_"+forma.getNumRegistros(),"Seleccione");
		forma.setMapaCajasCajeros("logincajero_"+forma.getNumRegistros(),"");
		forma.setMapaCajasCajeros("cajero_"+forma.getNumRegistros(),"Seleccione");
		forma.setMapaCajasCajeros("activo_"+forma.getNumRegistros(),"true");
		forma.setMapaCajasCajeros("institucion_"+forma.getNumRegistros(),usuario.getCodigoInstitucionInt()+"");
		forma.setMapaCajasCajeros("existerelacion_"+forma.getNumRegistros(),false+"");
		forma.setMapaCajasCajeros("tiporegistro_"+forma.getNumRegistros(),"MEM");
		forma.setNumRegistros(forma.getNumRegistros()+1);
		return this.redireccion(con,forma,response,request,"cajasCajeros.jsp");  
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
    public ActionForward redireccion (	Connection con,
            											CajasCajerosForm forma,
						            					HttpServletResponse response,
						            					HttpServletRequest request, String enlace)
    {
    	forma.setOffset(((int)((forma.getNumRegistros()-1)/forma.getMaxPageItems()))*forma.getMaxPageItems());
        if(request.getParameter("ultimaPage")==null)
        {
           if(forma.getNumRegistros() > (forma.getOffset()+forma.getMaxPageItems()))
               forma.setOffset(((int)(forma.getNumRegistros()/forma.getMaxPageItems()))*forma.getMaxPageItems());
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
            if(forma.getNumRegistros()>(forma.getOffset()+forma.getMaxPageItems()))
                forma.setOffset(forma.getOffset()+forma.getMaxPageItems());
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
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, CajasCajerosForm forma, CajasCajeros mundo, UsuarioBasico usuario) 
	{
		logger.info("===>Centro Atencion: "+forma.getCentroAtencion());
		logger.info("===>OFFSET: "+forma.getOffset());
		logger.info("===>LINK SIGUIENTE: "+forma.getLinkSiguiente());
		//--------Se pasa del form al mundo el valor del centro de atención ------------//
		if(forma.getCentroAtencion() != ConstantesBD.codigoNuncaValido)
		{
			mundo.setCentroAtencion(forma.getCentroAtencion());
			forma.setNumRegistros(mundo.cargarInformacion(con,usuario.getCodigoInstitucionInt()));
			forma.setMapaCajasCajeros((HashMap)mundo.getMapaCajasCajeros().clone());
			logger.info("===>NUM REGISTROS: "+forma.getNumRegistros());
		}
		else
		{
			mundo.setCentroAtencion(forma.getCentroAtencion());
			forma.setNumRegistros(0);
			logger.info("===>NUM REGISTROS: "+forma.getNumRegistros());
		}
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
