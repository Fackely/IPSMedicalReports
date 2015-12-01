/*
 * @(#)RecargosTarifasAction.java
 * 
 * Created on 05-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.action.cargos;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import util.UtilidadTexto;

import com.princetonsa.actionform.cargos.SalarioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.SalarioMinimo;

/**
 * Clase para el manejo de los recargos de las tarifas , ingreso, modificación, eliminación 
 * y consulta 
 * 
 * @version 1.0, 05-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class SalariosAction extends Action
{

	/*/**
	 * guarda el tipo de modificacion solicitado el usuario
	 */
	//private String tipoModificacion="";
	/**
	 * guarda el estado del form
	 */
	private String estado="";
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(SalariosAction.class);
	

	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response)throws Exception 
														{
		Connection con=null;
		try{
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

				logger.warn("Problemas con la base de datos "+e);
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			HttpSession session=request.getSession();			
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

			if( usuario == null )
			{
				this.closeConnection(con);				

				logger.warn("El usuario no esta cargado (null)");
				request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
				return mapping.findForward("paginaError");				
			}		

			if( form instanceof SalarioForm )
			{
				SalarioForm salarioForm = (SalarioForm)form;
				this.estado=salarioForm.getEstado();
				logger.warn("[action]estado->"+this.estado);
				if(this.estado.equals("ingresar"))
				{
					return accionIngresarSalario(mapping,con,salarioForm);
				}
				else if(this.estado.equals("guardar"))
				{
					return accionGuardarSalario(mapping,request,con,salarioForm,session);
				}
				else if(this.estado.equals("modificar"))
				{
					return accionModificarSalario(mapping,con,salarioForm);
				}
				else if(this.estado.equals("consultar"))
				{
					return accionConsultarSalario(mapping,con,salarioForm);
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					closeConnection(con);
					response.sendRedirect(salarioForm.getLinkSiguiente());
					return null;
				}
			}
			else
			{
				this.closeConnection(con);
				logger.error("El form no es compatible con el form de SalarioForm");
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
	
	private ActionForward accionIngresarSalario(ActionMapping mapping,Connection con,SalarioForm salarioForm)throws Exception{ 
			salarioForm.reset();
			this.closeConnection(con);
			return mapping.findForward("paginaPrincipal");	
	}
	
	private ActionForward accionConsultarSalario(ActionMapping mapping,Connection con,SalarioForm salarioForm)throws Exception{ 
			SalarioMinimo rt=new SalarioMinimo();
			//BeanUtils.copyProperties(rt,salarioForm);
			salarioForm.setColeccionSalario(rt.consultarSalarios(con));
			//BeanUtils.copyProperties(salarioForm,rt);
			this.closeConnection(con);
			return mapping.findForward("paginaResumen");	
	}
	
	private ActionForward accionModificarSalario(ActionMapping mapping,Connection con,SalarioForm salarioForm)throws Exception{ 
			SalarioMinimo rt=new SalarioMinimo();
			salarioForm.reset();
			salarioForm.setColeccionSalario(rt.consultarSalarios(con));		
			this.closeConnection(con);
			return mapping.findForward("paginaModificar");	
	}
	

	private ActionForward accionGuardarSalario(ActionMapping mapping,
																        HttpServletRequest request,
																        Connection con,
																        SalarioForm salarioForm,
																        HttpSession session)throws Exception{ 
			
			
		
		try{
		SalarioMinimo rt=new SalarioMinimo();
			BeanUtils.copyProperties(rt,salarioForm);
		
			if(salarioForm.getAccion().equals("insertar")){
				
				if(!rt.existeCruzeSalario(con)){
					rt.insertar(con);
				}else{
						
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("error.salario.definido"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaPrincipal");

				}
			}
			else if(salarioForm.getAccion().equals("modificar"))
			{
			    boolean modificado = verificarModificados(salarioForm,salarioForm.getCodigo());
			    if(modificado)
			    {
				  llenarloghistorial(salarioForm, salarioForm.getCodigo());
				  rt.modificar(con);			  
				  generarLog(salarioForm,session);
				  salarioForm.setColeccionSalario(rt.consultarSalarios(con));
			    }
			}
			
			this.closeConnection(con);
			if(salarioForm.getAccion().equals("insertar"))
			{
				return mapping.findForward("paginaResumen");	
			}
			
			if(salarioForm.getAccion().equals("modificar"))
			{
			    return mapping.findForward("paginaModificar");	 
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
			return null;
			
	}
	
	private boolean verificarModificados (SalarioForm salarioForm,String codigo)
	{
	    boolean modificado = false;
	    
	    Iterator iterador=salarioForm.getColeccionSalario().iterator();
	     
	     while(iterador.hasNext() )
	     {
	    	 HashMap encabezado=(HashMap)iterador.next();
	         
	         if(codigo.equals(encabezado.get("codigo")+""))
	         {
	             if(!(UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechainicial")+"")).equals(salarioForm.getFechaInicial()))
	                 modificado=true;
	             else if(!(UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechafinal")+"")).equals(salarioForm.getFechaFinal()))
	                 modificado=true;
	             //else if(Double.parseDouble(encabezado.get("salario")+"")!=salarioForm.getSalario())
	             else if(!(encabezado.get("salario")+"").equals(salarioForm.getSalario()))
	                 modificado=true;
	         }
	     }
	     
	    return modificado;
	}
	/**
	 * Cierra la conexión en el caso que esté abierta
	 * @param con
	 * @throws SQLException
	 */
	private void closeConnection(Connection con) throws SQLException
	{
		if( con != null && !con.isClosed() )
			UtilidadBD.closeConnection(con);
	}

	/**
	  * Metodo para cargar el Log con los datos actuales, antes de la 
	  * modificación.
	  * @param salarioForm, Instancia del Form SalarioForm.
	  */
	 private void llenarloghistorial(SalarioForm salarioForm,String codigo)
	 {
	     Iterator iterador=salarioForm.getColeccionSalario().iterator();
	     
	     while(iterador.hasNext() )
	     {
	    	 HashMap encabezado=(HashMap)iterador.next();
	         
	         if(codigo.equals(encabezado.get("codigo")+""))
	         {
	             salarioForm.setLog(	"\n            ====INFORMACION ORIGINAL===== " +
	                    			"\n*  Fecha Inicial [" +UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechainicial")+"")+"] "+
	                    			"\n*  Fecha Final ["+UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fechafinal")+"")+"] " +
	                    			"\n*  Monto ["+encabezado.get("salario")+""+"] ");
	         }
	          	        
	     }   
	        
	 }
	 
	 
	 
	 /**
	  * Metodo para generar el Log, y añadir los cambios realizados.
	  * @param salarioForm, Instancia del Form SalarioForm.
	  * @param session, Session para obtener el usuario.
	  */
	 private void generarLog(SalarioForm salarioForm, HttpSession session)
	    {
	        UsuarioBasico usuario;
	        
	        String log=salarioForm.getLog() +
												"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
												"\n*  Fecha Inicial [" +salarioForm.getFechaInicial()+"] "+
												"\n*  Fecha Final ["+salarioForm.getFechaFinal()+"] " +
												"\n*  Monto ["+UtilidadTexto.formatearValores(salarioForm.getSalario()+"",2,false,false)+"] "+												
												"\n========================================================\n\n\n " ;
	        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
	        LogsAxioma.enviarLog(ConstantesBD.logSalarioMinimoCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
	        
	    }
	
}
