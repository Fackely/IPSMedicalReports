/*
 * Creado el Jun 15, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;

import com.princetonsa.action.enfermeria.RegistroEnfermeriaAction;
import com.princetonsa.actionform.capitacion.ExcepcionNivelForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.ExcepcionNivel;

public class ExcepcionNivelAction extends Action {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(RegistroEnfermeriaAction.class);
	
	
	/**
	 * Funcion que maneja la navegacion por la funcionalidad.
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof ExcepcionNivelForm)
		{
			
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			} 
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			ExcepcionNivelForm forma = (ExcepcionNivelForm) form;
			String estado=forma.getEstado();
			logger.warn("[ExcepcionNivelAction] --> " + estado);
			
			if(estado.equals("ingresar"))
			{	
				forma.setSoloConsulta(false);
				return accionEmpezar(con, mapping, forma, usuario, false); //-El Falso es para que limpie el codigo y nombre del convenio
			}
			if(estado.equals("ingresarDeNuevo"))  
			{	
				forma.setSoloConsulta(false);
				return accionEmpezar(con, mapping, forma, usuario, true); //-El true es para que conserve el codigo y nombre del convenio
			}
			if(estado.equals("consultar"))
			{
				forma.setSoloConsulta(true);
				return accionEmpezar(con, mapping, forma, usuario, false);
			}
			if(estado.equals("consultarDeNuevo"))
			{
				forma.setSoloConsulta(true);
				return accionEmpezar(con, mapping, forma, usuario, true);
			}
			if(estado.equals("empezarContinuarServicio"))
			{
				return accionInsertarServicio(mapping, con, forma, usuario); 
			}
			if(estado.equals("ingresarConvenio"))   //-- Pasa a mostrar los contratos del convenio.( y los servicios si solo hay un contrato ). 
			{
				return buscarContratos(con, mapping, forma, usuario);
			}
			if(estado.equals("buscarServicios"))	
			{
				return buscarServicios(con, mapping, forma, usuario);
			}
			else if (estado.equals("guardar"))  
			{			    
				return accionGuardar(forma, con, mapping, usuario);
			}
			else if (estado.equals("eliminar"))  
			{			    
				return accionEliminar(forma, con, mapping, usuario);
			}
			else if (estado.equals("eliminarServicioMapa"))  
			{			    
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("servicio");
			}
			else if (estado.equals("ordenar"))  
			{			    
				return accionOrdenar(forma, con, mapping);
			}
			else if (estado.equals("redireccion"))  //--Estado para mantener los datos del pager
			{			    
			    UtilidadBD.cerrarConexion(con);
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
			}


		}//if
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * Metodo para ordenar la lista de servicios de un contrato especifico.
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionOrdenar(ExcepcionNivelForm forma, Connection con, ActionMapping mapping) throws SQLException
	{
		String[] indices = {"consecutivo_", "cod_contrato_", "cod_servicio_", "nom_servicio_", "nivel_", "vigencia_"};

		int num = 0;
		if ( UtilidadCadena.noEsVacio(forma.getMapaServicioReg().get("numRegistros")+"") )  
			{
				num = Integer.parseInt(forma.getMapaServicioReg().get("numRegistros")+"");
			}

		forma.setMapaServicioReg(Listado.ordenarMapa(indices, forma.getPatronOrdenar(),
										                      forma.getUltimoPatronOrdenar(),
										                      forma.getMapaServicioReg(),
										                      num ));
	    
	    forma.getMapaServicioReg().put("numRegistros", new Integer(num));
	    forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("servicio");
	}


	/**
	 * Metodo para eliminar un servicio de un contrato especifico 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(ExcepcionNivelForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException
	{
		ExcepcionNivel mundo = new ExcepcionNivel(); 
		llenarMundo(forma, mundo);
		
		mundo.eliminarServicioContrato(con, usuario.getLoginUsuario(), forma.getNroRegEliminar());
		return buscarServicios(con, mapping, forma, usuario); 
	}


	/**
	 * Metodo para guardar los servicios registrados para un convenio determinado
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(ExcepcionNivelForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) throws SQLException 
	{
		ExcepcionNivel mundo = new ExcepcionNivel(); 
		llenarMundo(forma, mundo);
		
		mundo.insertarServiciosConvenio(con, usuario.getCodigoInstitucionInt());
		return buscarContratos(con, mapping, forma, usuario);
	}

	/**
	 * Método para ingresar un servicio nuevo
	 * @param mapping
	 * @param con
	 * @param usuario 
	 * @param forma 
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionInsertarServicio(ActionMapping mapping, Connection con, ExcepcionNivelForm forma, UsuarioBasico usuario) throws SQLException
	{
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("servicio");  
	}

	/**
	 * Metodo Para Buscar Los Contratos de acuerdo a un Convenio Seleccionado
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward buscarContratos(Connection con, ActionMapping mapping, ExcepcionNivelForm forma, UsuarioBasico usuario) throws SQLException
	{
		ExcepcionNivel mundo = new ExcepcionNivel();
		
		llenarMundo(forma, mundo);

		//-- Para Conservar el codigo y el nombre del convenio.
		int nroConv = forma.getCodigoConvenio(); String nom = forma.getNombreConvenio(); int nroCont = forma.getCodigoContrato(); 
		forma.reset();
		forma.setCodigoConvenio(nroConv); forma.setNombreConvenio(nom); forma.setCodigoContrato(nroCont); 
		
		//-Buscar Los Contratos asociados al convenio seleccionado
		forma.setMapaContrato( mundo.cargarServiciosConvenio(con, 0, usuario.getCodigoInstitucionInt(),-1)); //-- El Cero es para que cargue los servicios de un contrato especifico. 

		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio(forma.getMapaContrato("numRegistros")+"") ) { nroReg = Integer.parseInt(forma.getMapaContrato("numRegistros")+""); }
		
		//-- Verificar si el convenio tiene un solo contrato si lo tiene -- > enviar a buscar los servicios asociados a ese contrato
		if ( (nroReg == 1) ||  (forma.getCodigoContrato()!=0) ) 
		{
			int nroContrato = 0;  
			
			if (forma.getCodigoContrato()!=0) //-Si se envio a guardar y se tiene un contrato seleccionado
			{ 
				nroContrato = forma.getCodigoContrato();
			}
			else 
			{
				nroContrato = Integer.parseInt(forma.getMapaContrato("codigo_contrato_0")+"");
			}
			//-Buscar Los Servicios Asociados al contrato Especifico.
			forma.setMapaServicioReg( mundo.cargarServiciosConvenio(con, 1, usuario.getCodigoInstitucionInt(), nroContrato)); //-- El Uno es para que cargue los servicios de un contrato especifico. 
		}
		else 
		{
			//-- Limpiar el mapa de servicios. 			
			forma.setMapaServicioReg( new HashMap() );
			forma.setCodigoContrato(0); //-- Para que seleccione el contrato. 
		}
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("servicio");  
	}

	/**
	 * Metodo Para Buscar Los Servicios de acuerdo a un Contrato y un Convenio Seleccionado
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward buscarServicios(Connection con, ActionMapping mapping, ExcepcionNivelForm forma, UsuarioBasico usuario) throws SQLException
	{
		ExcepcionNivel mundo = new ExcepcionNivel();
		
		//-Buscar Los Servicios Asociados al contrato Especifico.
		forma.setMapaServicioReg( mundo.cargarServiciosConvenio(con, 1, usuario.getCodigoInstitucionInt(), forma.getCodigoContrato() )); //-- El Uno es para que cargue los servicios de un contrato especifico.
			
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("servicio");  
	}
	
	
	/**
	 * Metodo para cargar Informacion al mundo para realizar operaciones de BD.
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(ExcepcionNivelForm forma, ExcepcionNivel mundo)
	{
		mundo.setMapa(forma.getMapa());
		mundo.setMapaServicio(forma.getMapaServicio());
		mundo.setMapaServicioReg(forma.getMapaServicioReg());
		mundo.setMapaContrato(forma.getMapaContrato());

		mundo.setCodigoConvenio(forma.getCodigoConvenio());
		mundo.setNombreConvenio(forma.getNombreConvenio());
		mundo.setCodigoContrato(forma.getCodigoContrato());
	}

	/**
	 * Medoto Para Iniciar En La Funcionalidad.
	 * @param con 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param limpiarConvenio 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, ExcepcionNivelForm forma, UsuarioBasico usuario, boolean limpiarConvenio) throws SQLException
	{
		ExcepcionNivel mundo = new ExcepcionNivel();
		 
		//-- Para Conservar el codigo y el nombre del convenio.
		if (limpiarConvenio)
		{
			int nroConv = forma.getCodigoConvenio(); String nom = forma.getNombreConvenio();
			forma.reset();
			forma.setCodigoConvenio(nroConv); forma.setNombreConvenio(nom);
		}
		else
		{
			forma.reset();
		}

		//-- Cargando los Convenios.
		forma.setMapa( mundo.cargarInformacion(con, usuario.getCodigoInstitucionInt()) );
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");  
	}	
}
