/*
 * @(#)EmpresaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.cargos.EmpresaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Empresa;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   empresas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Mayo 3, 2004
 * @author wrios
 */
public class EmpresaAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(EmpresaAction.class);
		
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response ) throws Exception
	{
		if (response==null); //Para evitar que salga el warning
		
		if(form instanceof EmpresaForm)
		{
				Connection con=null;
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
						
				EmpresaForm empresaForm =(EmpresaForm)form;
				String estado=empresaForm.getEstado();
				
				logger.info("Estado -->"+estado);
				
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				
				if(estado == null)
				{
						empresaForm.reset();	
						logger.warn("Estado no valido dentro del flujo de Registro empresa (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				else if(estado.equals("resumen"))
				{
						return this.accionResumen(empresaForm,mapping,request, con);
				}
	
				else if (estado.equals("empezar"))
				{
					 empresaForm.setEmpezarConsulta(false);
					return this.accionEmpezar(empresaForm, usuario, mapping, con);
				}
				else if(estado.equals("salir"))
				{
					 empresaForm.setEmpezarConsulta(false);	
					return this.accionSalir(empresaForm,mapping,con);
				}
				else if(estado.equals("modificar"))
				{
					 empresaForm.setEmpezarConsulta(false);	
					return this.accionModificar(empresaForm,request,mapping,con);
				}
				else if(estado.equals("guardarModificacion"))
				{
					 empresaForm.setEmpezarConsulta(false); 	
					return this.accionGuardarModificacion(empresaForm,request,mapping,con);
				}
				else if(estado.equals("listar")||estado.equals("listarModificar"))
				{
					     empresaForm.setEmpezarConsulta(true);
                         return this.accionListarEmpresas(empresaForm,mapping,con,estado, usuario);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
						return this.accionBusquedaAvanzada(empresaForm,mapping,con);
				}
				else if(estado.equals("resultadoBusquedaAvanzada"))
				{
						return this.accionResultadoBusquedaAvanzada(empresaForm,mapping,con, false, usuario);
				}
				else if(estado.equals("resultadoBusquedaAvanzadaModificar"))
				{
						return this.accionResultadoBusquedaAvanzada(empresaForm,mapping,con, true, usuario);
				}
				else if(estado.equals("consultaCiudadPrincipal"))
				{
					empresaForm.setEstado("empezar");
					empresaForm.setCiudadPrincipal("");
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultaCiudadCuentas"))
				{
					empresaForm.setEstado("empezar");
					empresaForm.setCiudadCuentas("");
					empresaForm.setEmpezarConsulta(false);
					return mapping.findForward("principal");
				}
				else if(estado.equals("imprimir"))
				{
					this.cerrarConexion(con);
					return mapping.findForward("imprimir");	
				}
				
				//Anexo 958
				else if (estado.equals("buscarInfoTercero"))
				{
					return this.accionBuscarInfoTercero(empresaForm,mapping,con);
				}
				else
				{
					empresaForm.reset();
					logger.warn("Estado no valido dentro del flujo de registro empresa (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
		}			
		return null;
	}	
	
	/**
	 * Anexo 958
	 * @throws SQLException 
	 */
	private ActionForward accionBuscarInfoTercero(EmpresaForm empresaForm,ActionMapping mapping,Connection con) throws SQLException
	{
		empresaForm.setDatosTercero(Utilidades.obtenerDatosTercero(con, empresaForm.getTercero()));
		empresaForm.setDireccion(empresaForm.getDatosTercero().get("direccion_0")+"");
		empresaForm.setTelefono(empresaForm.getDatosTercero().get("telefono_0")+"");
		
		empresaForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param empresaForm EmpresaForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "empresa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(EmpresaForm empresaForm, UsuarioBasico usuario,
																	 ActionMapping mapping, 
																	 Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		empresaForm.reset();
		empresaForm.setEstado("empezar");
		if(con != null) {
			if(con.isClosed()) {
				con=UtilidadBD.abrirConexion();
			}
			this.postularPais(con,empresaForm,usuario.getCodigoInstitucionInt());
			Empresa mundoEmpresa = new Empresa();
			
			Boolean resultado = mundoEmpresa.consultarTercerosRelacionados(con, usuario.getCodigoInstitucionInt());
			if(resultado != null && resultado)
				empresaForm.setValidarTerceros(true);
			else
				empresaForm.setValidarTerceros(false);
			this.cerrarConexion(con);
		}
		return mapping.findForward("principal");		
	}

	/**
		* Este método especifica las acciones a realizar en el estado
		* salir.
		* Se copian las propiedades del objeto empresa
		* en el objeto mundo
		* 
		* @param empresaForm EmpresaForm
		* @param request HttpServletRequest para obtener 
		* 					datos de la session 
		* @param mapping Mapping para manejar la navegación
		* @param con Conexión con la fuente de datos
		* 
		* @return ActionForward "empresa.do?estado=resumen"
		* @throws SQLException
	*/
	private ActionForward accionSalir(EmpresaForm empresaForm,
															 ActionMapping mapping,
															 Connection con) throws SQLException
	{
		Empresa mundoEmpresa=new Empresa ();  
		llenarMundo(empresaForm, mundoEmpresa);
		empresaForm.setCodigo(mundoEmpresa.insertarEmpresa(con));
		this.cerrarConexion(con);									
		return mapping.findForward("funcionalidadResumenEmpresa");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar empresa
	 * @param empresaForm EmpresaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "modificarEmpresa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(	EmpresaForm empresaForm, 
											HttpServletRequest request, 
											ActionMapping mapping, 
											Connection con) throws SQLException
	{
		
		try{
	    Empresa mundoEmpresa=new Empresa();
		mundoEmpresa.reset();
		mundoEmpresa.cargar(con,empresaForm.getCodigo());
		
		llenarForm(empresaForm,mundoEmpresa);
		empresaForm.setEstado("modificar");
		
		if(empresaForm.getTercero()<0)
		{
			logger.warn("No se pudo cargar la empresa (nit): "+empresaForm.getTercero());
			this.cerrarConexion(con);
			empresaForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("La empresa ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{	
			String log="\n            ====INFORMACION ORIGINAL===== " +
			"\n*  Código Empresa [" +mundoEmpresa.getCodigo() +"] "+
			"\n*  Tercero (Nit) ["+mundoEmpresa.getTercero()+"] " +
			"\n*  Razón Social ["+mundoEmpresa.getRazonSocial()+"] "+
			"\n*  Nombre Representante ["+mundoEmpresa.getRepresentante() +"] "+

			"\n*  E-mail ["+mundoEmpresa.getCorreo()+"] " +
			"\n*  Observaciones ["+mundoEmpresa.getObservaciones()+"] " +
		    "\n* Dirección Territorial ["+mundoEmpresa.getDireccionTerritorial()+"]  " +
		    "\n* Número de Afiliados	["+mundoEmpresa.getNumeroAfiliados()+"]  " +
		    "\n* Nivel de Ingreso	[ Codigo: "+mundoEmpresa.getNivelIngreso().getCodigo()+" Nombre: "+mundoEmpresa.getNivelIngreso().getNombre() +"]  " +
		    "\n* Forma de pago Facturación	[Codigo: "+mundoEmpresa.getFormaPago().getCodigo()+" Nombre:"+mundoEmpresa.getNombreContacto()+"] " +
		    
		    "\n* País Radicación Cuentas	["+mundoEmpresa.getPaisCuentas()  +"]  " +
		    "\n* Ciudad Radicación Cuentas	["+mundoEmpresa.getCiudadCuentas()  +"]  " +
		    "\n* Direccion Radicación Cuentas	["+mundoEmpresa.getDireccionCuentas()  +"]  " +
		    
		    "\n* País Sede Principal ["+mundoEmpresa.getPaisPrincipal()  +"]	" +
		    "\n* Ciudad Sede Principal   ["+mundoEmpresa.getCiudadPrincipal()+"]  " +
		    "\n* Dirección Sede Principal     ["+mundoEmpresa.getDireccion()+"]  " +
		    "\n* Teléfono Sede Principal     ["+mundoEmpresa.getTelefono() +"]  " +
			"\n* Fax Sede Principal   ["+mundoEmpresa.getFaxSedePrincipal() +"]  " +
		    
			"\n* Dirección Sucursal Local   ["+mundoEmpresa.getDireccionSucursal() +"]  " +
		    "\n* Teléfono Sucursal Local     ["+mundoEmpresa.getTelefonoSucursal() +"]  " +
		    "\n* Fax Sucursal Local:	["+mundoEmpresa.getFaxSucursalLocal() +"]  " ;
		
		if(mundoEmpresa.getActiva())
					log += "\n*  Activa [ SI ]";
				else
					log += "\n*  Activa [ NO ]";
				
			empresaForm.setLogInfoOriginalEmpresa(log);
			this.cerrarConexion(con);
			return mapping.findForward("principal");
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
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param empresaForm EmpresaForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "empresa.do?estado=modificar"
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion(	EmpresaForm empresaForm,
														HttpServletRequest request, 
														ActionMapping mapping, 
														Connection con)	
														throws SQLException
	{
	    /*boolean existioModificacion= existeModificacion(empresaForm, con);
		if(existioModificacion)
		{*/
			Empresa mundoEmpresa=new Empresa (); 
			logger.info("CODIGO EMPRESA A MODIFICAR---------------------------------------->: "+empresaForm.getCodigo());
			logger.info("Codigo del Nit---------------------------------------------------> "+mundoEmpresa.getNit());
			llenarMundo(empresaForm, mundoEmpresa);
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			mundoEmpresa.modificarEmpresa(con);
			String log=empresaForm.getLogInfoOriginalEmpresa()+
				"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
				"\n*  Código Empresa [" +mundoEmpresa.getCodigo() +"] "+
				"\n*  Tercero (Nit) ["+mundoEmpresa.getTercero()+"] " +
				"\n*  Razón Social ["+mundoEmpresa.getRazonSocial()+"] "+
				"\n*  Nombre Representante ["+mundoEmpresa.getRepresentante() +"] "+
				"\n*  Teléfono ["+mundoEmpresa.getTelefono()+"] "+
				"\n*  E-mail ["+mundoEmpresa.getCorreo()+"] " +
				"\n*  Observaciones ["+mundoEmpresa.getObservaciones()+"] " +
			    "\n* Dirección Territorial ["+mundoEmpresa.getDireccionTerritorial()+"]  " +
			    "\n* Número de Afiliados	["+mundoEmpresa.getNumeroAfiliados()+"]  " +
			    "\n* Nivel de Ingreso	[ Codigo: "+mundoEmpresa.getNivelIngreso().getCodigo()+" Nombre: "+mundoEmpresa.getNivelIngreso().getNombre() +"]  " +
			    "\n* Forma de pago Facturación	[Codigo: "+mundoEmpresa.getFormaPago().getCodigo()+" Nombre:"+mundoEmpresa.getNombreContacto()+"] " +
			    
			    "\n* País Radicación Cuentas	["+mundoEmpresa.getPaisCuentas()  +"]  " +
			    "\n* Ciudad Radicación Cuentas	["+mundoEmpresa.getCiudadCuentas()  +"]  " +
			    "\n* Direccion Radicación Cuentas	["+mundoEmpresa.getDireccionCuentas()  +"]  " +
			    
			    "\n* País Sede Principal ["+mundoEmpresa.getPaisPrincipal()  +"]	" +
			    "\n* Ciudad Sede Principal   ["+mundoEmpresa.getCiudadPrincipal()+"]  " +
			    "\n* Dirección Sede Principal     ["+mundoEmpresa.getDireccion()+"]  " +
			    "\n* Teléfono Sede Principal     ["+mundoEmpresa.getTelefono() +"]  " +
				"\n* Fax Sede Principal   ["+mundoEmpresa.getFaxSedePrincipal() +"]  " +
			    
				"\n* Dirección Sucursal Local   ["+mundoEmpresa.getDireccionSucursal() +"]  " +
			    "\n* Teléfono Sucursal Local     ["+mundoEmpresa.getTelefonoSucursal() +"]  " +
			    "\n* Fax Sucursal Local:	["+mundoEmpresa.getFaxSucursalLocal() +"]  " ;
			
			if(mundoEmpresa.getActiva())
					log += "\n*  Activa [ SI ]";
				else
					log += "\n*  Activa [ NO ]";
					
			log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logEmpresaCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());	
			this.cerrarConexion(con);									
			return mapping.findForward("funcionalidadResumenEmpresa");
		/*}
		else
		{
		    //NO EXISTIO MODIFICACION ENTONCES SE DEJA EN LA MISMA PAGINA
			this.cerrarConexion(con);
			empresaForm.setEstado("modificar");
			return mapping.findForward("principal");
		}*/		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listar empresa
	 * @param empresaForm EmpresaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoEmpresa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarEmpresas(	EmpresaForm empresaForm, 
												ActionMapping mapping,
												Connection con,String estado,
												UsuarioBasico usuario) throws SQLException 
	{
		Empresa mundoEmpresa= new Empresa();
		empresaForm.setEstado(estado);
		empresaForm.setCol(mundoEmpresa.listadoEmpresa(con, Integer.parseInt(usuario.getCodigoInstitucion())));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar")	;	
	}	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzadaempezar.
	 * 
	 * @param empresaForm EmpresaForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarEmpresa "busquedaEmpresa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	EmpresaForm empresaForm, 
																	 					ActionMapping mapping, 
																	 					Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		empresaForm.reset();
		empresaForm.setEstado("busquedaAvanzada");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzadaempezar.
	 * 
	 * @param empresaForm EmpresaForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarEmpresa "busquedaEmpresa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(EmpresaForm empresaForm, 
															ActionMapping mapping, 
															Connection con,
															boolean accionModificar,
															UsuarioBasico usuario) throws SQLException
	{
		Empresa mundoEmpresa= new Empresa();
		mundoEmpresa.reset();
		enviarItemsSeleccionadosBusqueda(empresaForm, mundoEmpresa);
		empresaForm.resetCriteriosBusqueda();
		empresaForm.setCol(mundoEmpresa.resultadoBusquedaAvanzada(con, Integer.parseInt(usuario.getCodigoInstitucion())));
		
		if(accionModificar)
		    empresaForm.setEstado("listarModificar");
		
		this.cerrarConexion(con);
		//llenarMundo(empresaForm,mundoEmpresa);
		return mapping.findForward("paginaListar");
	}
	
	
	private void enviarItemsSeleccionadosBusqueda(EmpresaForm empresaForm, Empresa mundoEmpresa)
	{
		String bb[]= empresaForm.getCriteriosBusqueda();
		for(int i=0; i<bb.length; i++)
		{
			try
			{
				if(bb[i].equals("nit"))
					mundoEmpresa.setNit(empresaForm.getNit());
				if(bb[i].equals("descripcionTercero"))
					mundoEmpresa.setDescripcionTercero(empresaForm.getDescripcionTercero());	
				if(bb[i].equals("tercero"))
					mundoEmpresa.setTercero(empresaForm.getTercero());
				if(bb[i].equals("razonSocial"))
					mundoEmpresa.setRazonSocial(empresaForm.getRazonSocial());
				if(bb[i].equals("nombreContacto"))
					mundoEmpresa.setNombreContacto(empresaForm.getNombreContacto());
				if(bb[i].equals("telefono"))
					mundoEmpresa.setTelefono(empresaForm.getTelefono());
				if(bb[i].equals("direccion"))
					mundoEmpresa.setDireccion(empresaForm.getDireccion());
				if(bb[i].equals("correo"))
					mundoEmpresa.setCorreo(empresaForm.getCorreo());
				if(bb[i].equals("activaAux"))
					mundoEmpresa.setActivaAux(empresaForm.getActivaAux());
				if(bb[i].equals("ciudadPrincipal"))
					mundoEmpresa.setCiudadPrincipal(empresaForm.getCiudadPrincipal());
				if(bb[i].equals("numeroAfiliados"))
					mundoEmpresa.setNumeroAfiliados(empresaForm.getNumeroAfiliados());
				if(bb[i].equals("nivelIngreso"))
					mundoEmpresa.setNivelIngreso(empresaForm.getNivelIngreso());
				if(bb[i].equals("formaPago"))
					mundoEmpresa.setFormaPago(empresaForm.getFormaPago());
				// mundoEmpresa 
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}		
	}
	
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form EmpresaForm para el mundo de Empresa
	 * @param empresaForm EmpresaForm (forma)
	 * @param mundoEmpresa Empresa (mundo)
	 */
	protected void llenarMundo(EmpresaForm empresaForm, Empresa mundoEmpresa)
	{
			mundoEmpresa.setCodigo(empresaForm.getCodigo());
			mundoEmpresa.setTercero(empresaForm.getTercero());
			mundoEmpresa.setTerceroNuevo(empresaForm.getTercero());
			mundoEmpresa.setRazonSocial(empresaForm.getRazonSocial());
			mundoEmpresa.setNombreContacto(empresaForm.getNombreContacto());
			mundoEmpresa.setTelefono(empresaForm.getTelefono());
			mundoEmpresa.setDireccion(empresaForm.getDireccion());
			mundoEmpresa.setCorreo(empresaForm.getCorreo());
			mundoEmpresa.setActiva(empresaForm.getActiva());
			if(empresaForm.getPaisPrincipal().equals(""))
				mundoEmpresa.setPaisPrincipal(empresaForm.getCodigoPaisPrincipal());
			else
				mundoEmpresa.setPaisPrincipal(empresaForm.getPaisPrincipal());
			if(empresaForm.getCiudadPrincipal().equals(""))
				mundoEmpresa.setCiudadPrincipal(empresaForm.getCodigoCiudadPrincipal());
			else
				mundoEmpresa.setCiudadPrincipal(empresaForm.getCiudadPrincipal());
			mundoEmpresa.setPaisCuentas(empresaForm.getPaisCuentas());
			mundoEmpresa.setCiudadCuentas(empresaForm.getCiudadCuentas());
			mundoEmpresa.setDireccionCuentas(empresaForm.getDireccionCuentas());
			mundoEmpresa.setTelefonoSucursal(empresaForm.getTelefonoSucursal());
			mundoEmpresa.setRepresentante(empresaForm.getRepresentante());
			mundoEmpresa.setObservaciones(empresaForm.getObservaciones());
			mundoEmpresa.setDireccionSucursal(empresaForm.getDireccionSucursal());
			mundoEmpresa.setDeptoPrincipal(empresaForm.getDeptoPrincipal());
			mundoEmpresa.setDeptoCuentas(empresaForm.getDeptoCuentas());
			mundoEmpresa.setCodigoPaisPrincipal(empresaForm.getCodigoPaisPrincipal());
			mundoEmpresa.setCodigoCiudadPrincipal(empresaForm.getCodigoCiudadPrincipal());
			mundoEmpresa.setCodigoPaisCuentas(empresaForm.getCodigoPaisCuentas());
			mundoEmpresa.setCodigoCiudadCuentas(empresaForm.getCodigoCiudadCuentas());
			
			mundoEmpresa.setFaxSedePrincipal(empresaForm.getFaxSedePrincipal());
			mundoEmpresa.setFaxSucursalLocal(empresaForm.getFaxSucursalLocal());
			mundoEmpresa.setDireccionTerritorial(empresaForm.getDireccionTerritorial());
			mundoEmpresa.setNumeroAfiliados(empresaForm.getNumeroAfiliados());
			mundoEmpresa.setNivelIngreso(empresaForm.getNivelIngreso());
			mundoEmpresa.setFormaPago(empresaForm.getFormaPago());
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param empresaForm EmpresaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenEmpresa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(EmpresaForm empresaForm, 
																	 ActionMapping mapping, 
																	HttpServletRequest request, 
																	 Connection con) throws SQLException
	{
		Empresa mundoEmpresa=new Empresa();  
		
		logger.info("codigo empresA--------->"+empresaForm.getCodigo());
		
		boolean validarCargar=mundoEmpresa.cargar(con,empresaForm.getCodigo());
		if(validarCargar)
		{
			llenarForm(empresaForm,mundoEmpresa);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenEmpresa");
		}
		else
		{
			logger.warn("Nit (tercero) inválido "+empresaForm.getTercero());
			this.cerrarConexion(con);
			empresaForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El nit (terceros)");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param empresaForm (form)
	 * @param mundoInterconsulta (mundo)
	 */
	protected void llenarForm(EmpresaForm empresaForm, Empresa mundoEmpresa)
	{		
		empresaForm.setCodigo(mundoEmpresa.getCodigo());
		empresaForm.setTercero(mundoEmpresa.getTercero());
		empresaForm.setTerceroNuevo(mundoEmpresa.getTercero());
		empresaForm.setRazonSocial(mundoEmpresa.getRazonSocial());
		empresaForm.setNombreContacto(mundoEmpresa.getNombreContacto());
		empresaForm.setTelefono(mundoEmpresa.getTelefono());
		empresaForm.setDireccion(mundoEmpresa.getDireccion());
		empresaForm.setCorreo(mundoEmpresa.getCorreo());
		empresaForm.setActiva(mundoEmpresa.getActiva());
		empresaForm.setPaisPrincipal(mundoEmpresa.getPaisPrincipal());
		empresaForm.setCiudadPrincipal(mundoEmpresa.getCiudadPrincipal());
		empresaForm.setPaisCuentas(mundoEmpresa.getPaisCuentas());
		empresaForm.setCiudadCuentas(mundoEmpresa.getCiudadCuentas());
		empresaForm.setDireccionCuentas(mundoEmpresa.getDireccionCuentas());
		empresaForm.setTelefonoSucursal(mundoEmpresa.getTelefonoSucursal());
		empresaForm.setRepresentante(mundoEmpresa.getRepresentante());
		empresaForm.setObservaciones(mundoEmpresa.getObservaciones());
		empresaForm.setDireccionSucursal(mundoEmpresa.getDireccionSucursal());
		empresaForm.setDeptoPrincipal(mundoEmpresa.getDeptoPrincipal());
		empresaForm.setDeptoCuentas(mundoEmpresa.getDeptoCuentas());
		empresaForm.setCodigoPaisPrincipal(mundoEmpresa.getCodigoPaisPrincipal());
		empresaForm.setCodigoCiudadPrincipal(mundoEmpresa.getDeptoPrincipal()+ConstantesBD.separadorSplit+mundoEmpresa.getCodigoCiudadPrincipal());
		empresaForm.setCodigoPaisCuentas(mundoEmpresa.getCodigoPaisCuentas());
		empresaForm.setCodigoCiudadCuentas(mundoEmpresa.getDeptoCuentas()+ConstantesBD.separadorSplit+mundoEmpresa.getCodigoCiudadCuentas());
		
		empresaForm.setFaxSedePrincipal(mundoEmpresa.getFaxSedePrincipal());
		empresaForm.setFaxSucursalLocal(mundoEmpresa.getFaxSucursalLocal());
		empresaForm.setDireccionTerritorial(mundoEmpresa.getDireccionTerritorial());
		
		empresaForm.setNumeroAfiliados(mundoEmpresa.getNumeroAfiliados());
		empresaForm.setNivelIngreso(mundoEmpresa.getNivelIngreso());
		empresaForm.setFormaPago(mundoEmpresa.getFormaPago());
	}					


	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
		if (con!=null&&!con.isClosed())
		{
            UtilidadBD.closeConnection(con);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucionInt
	 */
	private void postularPais(Connection con, EmpresaForm  forma, int codigoInstitucionInt) 
	{
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, codigoInstitucionInt);
		if(UtilidadTexto.isEmpty(forma.getPaisPrincipal()))
			forma.setPaisPrincipal(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisCuentas()))
			forma.setPaisCuentas(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getCiudadPrincipal()))
			forma.setCiudadPrincipal(mundoInstitucion.getCiudad().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getCiudadCuentas()))
			forma.setCiudadCuentas(mundoInstitucion.getCiudad().getCodigo());
		
	}
	
	
}
