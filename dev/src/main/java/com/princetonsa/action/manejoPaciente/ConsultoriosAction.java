/*
 * @(#)ConsultoriosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.action.manejoPaciente;

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
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.ConsultoriosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.Consultorios;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class ConsultoriosAction extends Action  
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ConsultoriosAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try{
    		if (response==null); 
    		if(form instanceof ConsultoriosForm)
    		{
    			MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.ConsultoriosForm");

    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    			ConsultoriosForm forma =(ConsultoriosForm)form;
    			String estado=forma.getEstado();
    			logger.warn("\n\n\nEl estado en Consultorios es------->"+estado+"\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Consultorios (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			else if(estado.equals("inicio"))
    			{
    				return this.accionInicio(forma, mapping, con, usuario);
    			}

    			else if(estado.equals("empezar"))
    			{
    				return this.accionEmpezar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("detalle"))
    			{
    				return this.accionDetalle(forma, mapping, con);
    			}
    			else if (estado.equals("redireccion"))
    			{
    				UtilidadBD.closeConnection(con);
    				response.sendRedirect(forma.getLinkSiguiente());
    				return null;
    			}
    			else if(estado.equals("nuevo"))
    			{
    				this.accionNuevo(forma,request, response, mapping);
    				UtilidadBD.closeConnection(con);
    				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getConsultoriosMap("numRegistros").toString()), response, request, "consultorios.jsp",true);
    			}
    			else if(estado.equals("eliminar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return this.accionEliminarRegistro(forma,request,response,mapping, usuario.getCodigoInstitucionInt());
    			}
    			else if(estado.equals("ordenar"))
    			{
    				this.accionOrdenarMapa(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}
    			else if(estado.equals("guardar"))
    			{
    				if(forma.isErrorGuardando()){
    					String mensajeConcreto = fuenteMensaje.getMessage("ConsultoriosForm.errorInsertarConsultorios");
    					this.mostrarErrorEnviado(forma, request, mensajeConcreto);
    					return mapping.findForward("principal");
    				}
    				else{
    					//guardamos en BD.
    					return this.accionGuardarRegistros(con,forma,usuario, mapping);
    				}

    			}
    			else if(estado.equals("resumen"))
    			{
    				return this.accionResumen(con, mapping, forma, usuario);
    			}
    			else if(estado.equals("empezarConsulta"))
    			{
    				return this.accionEmpezarConsulta(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("consultar"))
    			{
    				return this.accionConsultar(forma,  con,usuario, response, request);
    			}
    			else if(estado.equals("ordenarConsultar"))
    			{
    				this.accionOrdenarMapa(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("consultar");
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Consultorios -> "+estado);
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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

    
	private ActionForward accionInicio(ConsultoriosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("inicio");
	}
    
    
	private void mostrarErrorEnviado(ConsultoriosForm forma, HttpServletRequest request, String mensajeConcreto) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
	}
	
 	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(ConsultoriosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalle(ConsultoriosForm forma, ActionMapping mapping, Connection con) 
	{
		forma.setConsultoriosMap(Consultorios.consultoriosXCentroAtencionTipo(con, forma.getCentroAtencion(), ""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevo(ConsultoriosForm forma,HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) 
	{
		int pos=Integer.parseInt(forma.getConsultoriosMap("numRegistros")+"");
		forma.setConsultoriosMap("codigo_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setConsultoriosMap("codigoconsultorio_"+pos,"");
		forma.setConsultoriosMap("descripcion_"+pos,"");
		forma.setConsultoriosMap("activo_"+pos, ConstantesBD.acronimoSi);
		forma.setConsultoriosMap("acronimotipo_"+pos,"");
		forma.setConsultoriosMap("tipo_"+pos,"");
		forma.setConsultoriosMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setConsultoriosMap("numRegistros", (pos+1)+"");		
	}
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarRegistro(ConsultoriosForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		int numRegMapEliminados=Integer.parseInt(forma.getConsultoriosEliminadosMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getConsultoriosMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getConsultoriosMap("INDICES_MAPA");
		for(int i=0;i<indices.length;i++)
		{
			//solo pasar al mapa los registros que son de BD
			if((forma.getConsultoriosMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setConsultoriosEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getConsultoriosMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}
		if((forma.getConsultoriosMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{
			forma.setConsultoriosEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setConsultoriosMap(indices[j]+""+i,forma.getConsultoriosMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getConsultoriosMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setConsultoriosMap("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getConsultoriosMap("numRegistros").toString()), response, request, "consultorios.jsp",forma.getIndexEliminado()==ultimaPosMapa);
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(ConsultoriosForm forma) 
	{
		String[] indices= (String[])forma.getConsultoriosMap("INDICES_MAPA");
		int numReg=Integer.parseInt(forma.getConsultoriosMap("numRegistros")+"");
		forma.setConsultoriosMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getConsultoriosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConsultoriosMap("numRegistros",numReg+"");	
		forma.setConsultoriosMap("INDICES_MAPA", indices);
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarRegistros(Connection con, ConsultoriosForm forma, UsuarioBasico usuario, ActionMapping mapping) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		Consultorios mundo= new Consultorios();
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getConsultoriosEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con, Integer.parseInt(forma.getConsultoriosEliminadosMap("codigo_"+i)+"")))
			{
				this.generarLog(forma, new HashMap(), usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getConsultoriosMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getConsultoriosMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)
				&&this.existeModificacion(con,forma,mundo, Integer.parseInt(forma.getConsultoriosMap("codigo_"+i)+""),i,usuario))
			{
				transaccion=mundo.modificar(con, llenarCrearMundo(forma, i));
			}
			//insertar
			else if((forma.getConsultoriosMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transaccion=mundo.insertar(con, llenarCrearMundo(forma, i));
			}
		}
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("--->INSERTO 100% CONSULTORIOS");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			
		}
		return this.accionResumen(con, mapping, forma, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param indice
	 */
	private Consultorios llenarCrearMundo(ConsultoriosForm forma, int indice)
	{
		Consultorios mundo= new Consultorios();
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setActivo(forma.getConsultoriosMap("activo_"+indice).toString());
		mundo.setCodigo(Integer.parseInt(forma.getConsultoriosMap("codigo_"+indice).toString()));
		mundo.setCodigoConsultorio(Integer.parseInt(forma.getConsultoriosMap("codigoconsultorio_"+indice).toString()));
		mundo.setDescripcion(forma.getConsultoriosMap("descripcion_"+indice).toString());
		return mundo;
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLog(ConsultoriosForm forma, HashMap mapaConsultoriosTemp, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])forma.getConsultoriosMap("INDICES_MAPA");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getConsultoriosEliminadosMap(indices[i]+""+pos)+""):forma.getConsultoriosEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaConsultoriosTemp.get(indices[i]+"0")+""):mapaConsultoriosTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getConsultoriosMap(indices[i]+""+pos)+""):forma.getConsultoriosMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logConsultoriosCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @return
	 */
	private boolean existeModificacion(Connection con, ConsultoriosForm forma, Consultorios mundo,int codigo,int pos, UsuarioBasico usuario)
	{
		HashMap temp=mundo.consultarConsultorio(con, codigo);
		String[] indices= (String[])forma.getConsultoriosMap("INDICES_MAPA");
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getConsultoriosMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getConsultoriosMap(indices[i]+""+pos)+"")))
				{
					this.generarLog(forma, temp, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
	private ActionForward accionResumen(Connection con, ActionMapping mapping, ConsultoriosForm forma, UsuarioBasico usuario) 
	{
		int centroAtencionTemp= forma.getCentroAtencion();
		//limpiamos el form
		forma.reset();
		forma.setCentroAtencion(centroAtencionTemp);
		forma.setEstado("resumen");
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		forma.setConsultoriosMap(Consultorios.consultoriosXCentroAtencionTipo(con, forma.getCentroAtencion(), ""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarConsulta(ConsultoriosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultar");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario 
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionConsultar(ConsultoriosForm forma, Connection con, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
	{
		forma.setConsultoriosMap(Consultorios.consultoriosXCentroAtencionTipo(con, forma.getCentroAtencion(), forma.getAcronimoTipo()));
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar("", ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getConsultoriosMap("numRegistros").toString()), response, request, "../consultorios/consultaConsultorios.jsp", false);
		//return mapping.findForward("consultar");
	}
	
}