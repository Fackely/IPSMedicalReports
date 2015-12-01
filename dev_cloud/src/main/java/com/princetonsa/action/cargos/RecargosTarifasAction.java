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

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.cargos.ListadoRecargosTarifasForm;
import com.princetonsa.actionform.cargos.RecargoTarifaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.RecargoTarifa;
import com.princetonsa.mundo.cargos.RecargosTarifas;

/**
 * Clase para el manejo de los recargos de las tarifas , ingreso, modificación, eliminación 
 * y consulta 
 * 
 * @version 1.0, 05-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class RecargosTarifasAction extends Action
{
	/* ******************** WARNING ***********************/
	//private String accion;
	/**
	 * guarda el tipo de modificacion solicitado el usuario
	 */
	private String tipoModificacion="";
	/**
	 * guarda el estado del form
	 */
	private String estado="";
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RecargosTarifasAction.class);
	
	UsuarioBasico usuario;

	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response)throws Exception 
	{
		Connection con=null;
		String tipoBD;
		
		try{
		
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
		usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			
		if( usuario == null )
		{
			this.closeConnection(con);				
	
			logger.warn("El usuario no esta cargado (null)");
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");				
		}		

		if( form instanceof RecargoTarifaForm )
		{
			RecargoTarifaForm recargoTarifaForm = (RecargoTarifaForm)form;
			this.estado=recargoTarifaForm.getEstado();
			//this.accion=recargoTarifaForm.getAccion();
			if(this.estado.equals("ingresar")){
			
				return accionIngresarRecargo(mapping,con,recargoTarifaForm);
			}else if(this.estado.equals("guardar")){
				return accionGuardarRecargo(mapping,request,con,recargoTarifaForm);
			}else if(this.estado.equals("modificar")){
				return accionModificarRecargo(mapping,request,con,recargoTarifaForm);
			}else if(this.estado.equals("eliminar")){
				return accionEliminarRecargo(mapping,con,recargoTarifaForm);

			}else if(this.estado.equals("menu")){
				recargoTarifaForm.reset();
				this.closeConnection(con);
				return mapping.findForward("menu");
			}else if(this.estado.equals("consultar")){
				return accionConsultarRecargo(mapping,con,recargoTarifaForm);
			}
		}
		else
		if( form instanceof ListadoRecargosTarifasForm )
		{
			
			ListadoRecargosTarifasForm listadoRecargosTarifasForm = (ListadoRecargosTarifasForm)form;
			this.tipoModificacion=listadoRecargosTarifasForm.getTipoModificacion();
			
			this.estado=listadoRecargosTarifasForm.getEstado();
			if (estado.equals("iniciarBusqueda"))
			{		return this.accionEmpezarBusqueda(mapping, con, listadoRecargosTarifasForm);
			}
			else if (estado.equals("buscar"))
			{
				
					return this.accionBuscar(mapping, request,con, listadoRecargosTarifasForm);
			}
			if(this.estado.equals("ordenar")){
				return accionOrdenarRecargo(mapping,con,listadoRecargosTarifasForm);
			}else if(this.estado.equals("menu")){
				listadoRecargosTarifasForm.reset();
				this.closeConnection(con);
				return mapping.findForward("menu");
			}
			else 
			if(this.estado.equals("imprimir"))
			{
				this.closeConnection(con);
				return mapping.findForward("imprimir");
			}	
		}
		else
		{
			this.closeConnection(con);
			logger.error("El form no es compatible con el form de RecargosTarifaForm o ListadoRecargosTarifasForm");
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
	 * Method accionBuscar.
	 * @param mapping
	 * @param con
	 * @param listadoRecargosTarifasForm
	 * @return ActionForward
	 */
	private ActionForward accionBuscar(
		ActionMapping mapping,
		HttpServletRequest request,
		Connection con,
		ListadoRecargosTarifasForm listadoRecargosTarifasForm) throws SQLException, IllegalAccessException, InvocationTargetException
	{
		
		Collection listado;
		boolean parametros[]=new boolean[8];
		for(int j=0;j<parametros.length;j++){
			parametros[j]=false;
		}
		String criteriosBusqueda[]=listadoRecargosTarifasForm.getCriteriosBusqueda();
		if(criteriosBusqueda!=null){
			for(int i=0;i<criteriosBusqueda.length;i++){
				if(criteriosBusqueda[i].equals("codigo")){
					parametros[0]=true;
				}
	
				if(criteriosBusqueda[i].equals("codigoViaIngreso") && listadoRecargosTarifasForm.getCodigoViaIngreso()!=0){
					parametros[1]=true;
				}
				if(criteriosBusqueda[i].equals("codigoTipoRecargo")){
					parametros[2]=true;
				}
				if(criteriosBusqueda[i].equals("codigoEspecialidad") && listadoRecargosTarifasForm.getCodigoEspecialidad()!=0){
					parametros[3]=true;
				}
				if(criteriosBusqueda[i].equals("codigoServicio") && listadoRecargosTarifasForm.getCodigoServicio()!=0){
					parametros[4]=true;
				}
				if(criteriosBusqueda[i].equals("porcentaje")){
					parametros[5]=true;
				}
				if(criteriosBusqueda[i].equals("valor")){
					parametros[6]=true;
				}
				if(criteriosBusqueda[i].equals("codigoContrato")){
					parametros[7]=true;
				}
				
			}
		}
		if(this.tipoModificacion.equals("consulta")){
			parametros[7]=true;
		}
		RecargosTarifas rt=new RecargosTarifas();
		
		rt.consultar(con,
			listadoRecargosTarifasForm.getCodigo(),parametros[0],
			listadoRecargosTarifasForm.getPorcentaje(),parametros[5],
			listadoRecargosTarifasForm.getValor(),parametros[6],
			listadoRecargosTarifasForm.getCodigoTipoRecargo(),parametros[2],
			listadoRecargosTarifasForm.getCodigoServicio(),parametros[4],
			listadoRecargosTarifasForm.getCodigoEspecialidad(),parametros[3],
			listadoRecargosTarifasForm.getCodigoViaIngreso(),parametros[1],
			listadoRecargosTarifasForm.getCodigoContrato(),parametros[7],
			listadoRecargosTarifasForm.getConvenio());
		listado = rt.getRecargosTarifas();
		listadoRecargosTarifasForm.setNombreConvenio(rt.getNombreConvenio());
		listado=validacionListado(listado);
				//añadirlo al form
		listadoRecargosTarifasForm.setRecargosTarifas(new ArrayList(listado));
		
		request.getSession().setAttribute("ultimaBusquedaRecargos","listadoRecargosTarifas.do?estado="+this.estado);
		
		//por motivos de modelado se realiza esta consulta, y todo lo anterior
		//se conserva para evitar desaquilibrar el workFlow.
		listadoRecargosTarifasForm.setCol(rt.listarRecargosAvanzada(con,
																		listadoRecargosTarifasForm.getCodigo(),parametros[0],
																		listadoRecargosTarifasForm.getPorcentaje(),parametros[5],
																		listadoRecargosTarifasForm.getValor(),parametros[6],
																		listadoRecargosTarifasForm.getCodigoTipoRecargo(),parametros[2],
																		listadoRecargosTarifasForm.getCodigoServicio(),parametros[4],
																		listadoRecargosTarifasForm.getCodigoEspecialidad(),parametros[3],
																		listadoRecargosTarifasForm.getCodigoViaIngreso(),parametros[1],
																		listadoRecargosTarifasForm.getCodigoContrato(),parametros[7],
																		listadoRecargosTarifasForm.getConvenio()));
		
		
		this.closeConnection(con);
		
		return mapping.findForward("listado");
		
		
	}
	/**
	 * Method accionEmpezarBusqueda.
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listadoRecargosTarifasForm
	 * @return ActionForward
	 */
	private ActionForward accionEmpezarBusqueda(
		ActionMapping mapping,
		Connection con,
		ListadoRecargosTarifasForm listadoRecargosTarifasForm) throws SQLException
	{
		this.closeConnection(con);
		
		listadoRecargosTarifasForm.reset();
		listadoRecargosTarifasForm.setTipoModificacion(this.tipoModificacion);
		return mapping.findForward("busquedaAvanzada");
		

	}
	
	
	
	
	private ActionForward accionIngresarRecargo(ActionMapping mapping,Connection con,RecargoTarifaForm recargoTarifasForm)throws Exception{ 
			recargoTarifasForm.reset();
			this.closeConnection(con);
			return mapping.findForward("paginaPrincipal");	
	}
	private ActionForward accionEliminarRecargo(ActionMapping mapping,Connection con,RecargoTarifaForm recargoTarifasForm)throws Exception{ 
			RecargoTarifa rt=new RecargoTarifa();
			BeanUtils.copyProperties(rt,recargoTarifasForm);
			rt.eliminar(con);
			this.closeConnection(con);
			return mapping.findForward("paginaResumen");	
				
	}
	private ActionForward accionConsultarRecargo(ActionMapping mapping,Connection con,RecargoTarifaForm recargoTarifasForm)throws Exception{ 
			RecargoTarifa rt=new RecargoTarifa();
			BeanUtils.copyProperties(rt,recargoTarifasForm);
			rt.consultar(con,true);
			BeanUtils.copyProperties(recargoTarifasForm,rt);
			this.closeConnection(con);
			return mapping.findForward("paginaResumen");	
	}

	private ActionForward accionModificarRecargo(ActionMapping mapping,HttpServletRequest request,Connection con,RecargoTarifaForm recargoTarifasForm)throws Exception{ 
			RecargoTarifa rt=new RecargoTarifa();
			BeanUtils.copyProperties(rt,recargoTarifasForm);
			rt.consultar(con,true);
			recargoTarifasForm.reset();
			BeanUtils.copyProperties(recargoTarifasForm,rt);
			request.getSession().setAttribute("recargoOriginal", rt);
			recargoTarifasForm.adecuarValores();
			this.closeConnection(con);
			return mapping.findForward("paginaModificar");	
	}
	
	private ActionForward accionOrdenarRecargo(ActionMapping mapping, Connection con,ListadoRecargosTarifasForm listadoRecargosTarifasForm)throws Exception{
		
		
		listadoRecargosTarifasForm.setRecargosTarifas(Listado.ordenarColumna(listadoRecargosTarifasForm.getRecargosTarifas(),listadoRecargosTarifasForm.getUltimaPropiedad(),listadoRecargosTarifasForm.getColumna()));
		listadoRecargosTarifasForm.setUltimaPropiedad(listadoRecargosTarifasForm.getColumna());
		closeConnection(con);
		return  mapping.findForward("listado");
	}
	private ActionForward accionGuardarRecargo(ActionMapping mapping,HttpServletRequest request,Connection con,RecargoTarifaForm recargoTarifasForm)throws Exception{ 
			
			RecargoTarifa rt=new RecargoTarifa();
			ResultadoBoolean resultado=new ResultadoBoolean(true);
			BeanUtils.copyProperties(rt,recargoTarifasForm);


			if(recargoTarifasForm.getAccion().equals("insertar")){
				//varificar que no existe un recargo igual
				if(!rt.existeRecargo(con)){
					rt.insertar(con);
				}else{
						
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("error.recargo.definido"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaPrincipal");

				}
			}
			else 
			if(recargoTarifasForm.getAccion().equals("modificar"))
			{
				resultado =rt.modificar(con,(RecargoTarifa)request.getSession().getAttribute("recargoOriginal"));				
				
				if( resultado.isTrue() )
				{
					this.generarLogRecargoTarifa((RecargoTarifa)request.getSession().getAttribute("recargoOriginal"), rt, usuario, true);
				}
				else
				{
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("error.recargo.definido"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaModificar");
				}
			}
			else 
			if(recargoTarifasForm.getAccion().equals("eliminar"))
			{
				rt.eliminar(con);
				resultado=new ResultadoBoolean(true);
				if( resultado.isTrue() )
				{
					this.generarLogRecargoTarifa(null, rt, usuario, false);
					request.getSession().setAttribute("recargoOriginal", null);
					this.closeConnection(con);
					return mapping.findForward("mensaje");	
				}
			}
			if(!resultado.isTrue()){
				this.closeConnection(con);
				request.setAttribute("codigoDescripcionError", resultado.getDescripcion());				
				return mapping.findForward("paginaError");																	
			}
			if(recargoTarifasForm.getAccion().equals("modificar")  ){
				
				//rt.consultar(con,true);
				recargoTarifasForm.setColeccionRecargo(rt.consultaUnRecargoTarifa(con,rt.getCodigo()));
				BeanUtils.copyProperties(recargoTarifasForm,rt);
				
			}
			if(recargoTarifasForm.getAccion().equals("insertar"))
				{
					//rt.consultar(con,false);
			    	recargoTarifasForm.setColeccionRecargo(rt.consultaUnRecargoTarifa(con,rt.recargoActual(con)));
					BeanUtils.copyProperties(recargoTarifasForm,rt);
					if(request.getSession().getAttribute("ultimaBusquedaRecargos")!=null){
						request.getSession().setAttribute("ultimaBusquedaRecargos",null);
				}
			}
			
			request.getSession().setAttribute("recargoOriginal", null);
			
			this.closeConnection(con);
			return mapping.findForward("paginaResumen");	
			
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
	 * @param listado de solicitudes resultado de la busqueda
	 * @return lista validada con las reglas de solicitudes y modificada para mostrar con el tag display:*
	 */
	private Collection validacionListado(Collection listado) throws SQLException, IllegalAccessException, InvocationTargetException
	{
		//añadir a listado definitivo
		ArrayList listadoTemporal= new ArrayList(listado);
		Collection c=new ArrayList();
		
		//	recorrer el listado
		for(int i=0;i<listadoTemporal.size();i++)
		{
			
		//validar condiciones
			RecargoTarifa temp = (RecargoTarifa)listadoTemporal.get(i);
			RecargoTarifaForm cf=new RecargoTarifaForm();
			BeanUtils.copyProperties(cf,temp);
			if(this.tipoModificacion.equals("modificar")){
				cf.setEnlace("../modificarRecargoTarifa/modificarRecargoTarifa.do?estado=modificar&tipo=recargo&accion=modificar&codigo="+temp.getCodigo());
			}else if(this.tipoModificacion.equals("consulta")){
				cf.setEnlace("../listadoRecargosTarifas/listadoRecargosTarifas1.do?estado=consultar&codigo="+temp.getCodigo());
			}
			c.add(cf);
		}
		return c;
	}

	/**
	 * Genera un log en el archivo de logs de recargos de tarifas
	 * @param recargoTarifaOriginal, null en el caso de eliminar, la recargo de la tarifa original en el caso de modificar
	 * @param recargoTarifaNueva
	 * @param usuario
	 * @param esModificar
	 */
	private void generarLogRecargoTarifa( RecargoTarifa recargoTarifaOriginal, RecargoTarifa recargoTarifaNueva, UsuarioBasico usuario, boolean esModificar )
	{	
		String logCambiosTarifa = new String();
		logCambiosTarifa = "\nRECARGO TARIFA";
		int tipoRegistro = 0;
		
		/*
		 * Si es modificar se deben meter tanto los datos originales como los nuevos
		 */		
		if( esModificar )
		{
			tipoRegistro = ConstantesBD.tipoRegistroLogModificacion;
			
			logCambiosTarifa += "\nDATOS ORIGINALES\n";			
			logCambiosTarifa += recargoTarifaOriginal.getCadenaLogRecargo();
		}
		else
		{
			tipoRegistro = ConstantesBD.tipoRegistroLogEliminacion;			
		}
		
		logCambiosTarifa += "\nDATOS RECARGO TARIFA\n";
		logCambiosTarifa += recargoTarifaNueva.getCadenaLogRecargo();
		logCambiosTarifa += "\n\n";		
		
		LogsAxioma.enviarLog(ConstantesBD.logRecargosTarifasCodigo, logCambiosTarifa, tipoRegistro, usuario.getInformacionGeneralPersonalSalud());
	}	
	
}
