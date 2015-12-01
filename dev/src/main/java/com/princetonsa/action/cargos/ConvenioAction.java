package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
import util.ConstantesIntegridadDominio;
import util.Errores;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cargos.ConvenioForm;
import com.princetonsa.dao.ConvenioDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.TiposContrato;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposContratoServicio;


public class ConvenioAction extends Action
{
	// Prueba de merge entre branches
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConvenioAction.class);
	
	//indices 
	private static final String [] indicesConceptos = {"codigo_", "usuario_", "tipoUsuario_", "activo_", "nombreUsuario_"};
	
	private static final String[] indicesCorreoElect={"codigo_","mail_","convenio_","fecha_","hora_","usuario_","eliminar_"};
	
	private static final String TIPO_CONTRATO_CAPITADO = "Capitado";
	/**
	 * Metodo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
	{
		
		Connection con=null;
		try{
		
		if (response==null); //Para evitar que salga el warning
		if(form instanceof ConvenioForm)
		{
			con=UtilidadBD.abrirConexion();

			ConvenioForm convenioForm =(ConvenioForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			String estado=convenioForm.getEstado();
			
			boolean esConsulta=convenioForm.getEsConsulta();
			logger.warn("[ConvenioAction] estado [" + estado + "]\n\n");
			if(estado.equals("listar"))
			{
				//Si el estado es listar, quiere decir que el proximo evento a realizar es una consulta
				convenioForm.setEsConsulta(true);
				return mapping.findForward("principalMenu");
			}
			if(estado.equals("listarModificar"))
			{
				//Si el estado es salir, quiere decir que acaba de realizar una insercion, por tanto no es una consulta
				convenioForm.setEsConsulta(false);
				return mapping.findForward("principalMenu");
			}
			logger.info("===> esConsulta: "+esConsulta);

			Convenio mundo = new Convenio();
			
			if(estado == null)
			{
				convenioForm.reset(usuario.getCodigoInstitucionInt());	
				logger.warn("Estado no valido dentro del flujo de registro convenio (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				return this.accionEmpezar(convenioForm,usuario,mapping, con);
			}
			
			else if (estado.equals("empezarOdontologia"))
			{
				return this.accionEmpezarOdontologia(convenioForm,usuario,mapping, con);
			}
			else if (estado.equals("empezarGeneral"))
			{
				
				return accionEmpezarGeneral(convenioForm,usuario,mapping, con);
			}
			
			//guardar insertar
			else if(estado.equals("salir"))
			{
				return this.accionSalir(convenioForm,usuario,mapping,con, request);
			}
			else if(estado.equals("resumen"))
			{
				 
				return this.accionResumen(convenioForm,usuario,mapping,request, con);
			}
			else if(estado.equals("resumenModificar"))
			{
				return this.accionResumenModificar(convenioForm,usuario,mapping,request, con);
			}
			else if(estado.equals("modificar"))
			{
				 
				return this.accionModificar(convenioForm,usuario,request,mapping,con);
			}
			else if(estado.equals("guardarModificacion"))
			{ 
				return this.accionGuardarModificacion(convenioForm,request,mapping,con);
			}
			else if(estado.equals("listar")||estado.equals("listarModificar"))
			{
				return this.accionListarConvenios(convenioForm,mapping,con,estado, usuario);
			}
			
			else if(estado.equals("listarOdontologia")||estado.equals("listarModificarOdontologia"))
			{
				return this.accionListarConveniosOdontologia(convenioForm,mapping,con,estado, usuario);
			}
			
			else if(estado.equals("busquedaAvanzada"))
			{
				return this.accionBusquedaAvanzada(convenioForm,usuario,mapping,con);
			}
			else if(estado.equals("resultadoBusquedaAvanzada"))
			{
				return this.accionResultadoBusquedaAvanzada(convenioForm,mapping,con, false, usuario);
			}
			else if(estado.equals("resultadoBusquedaAvanzadaModificar"))
			{
				return this.accionResultadoBusquedaAvanzada(convenioForm,mapping,con, true, usuario);
			}
			//**************Cambio Anexo 753 Decreto 4747 Andres Ortiz***********************
			else if(estado.equals("agregarMail"))
			{
				return this.accionAdicionarCorreo(con,mapping,convenioForm, usuario,indicesCorreoElect);	
			}
			
			else if (estado.equals("eliminarEmail"))
			{	
				return this.accionEliminarCorreo(mapping,convenioForm);
			}
			//******************************************************************************
			else if(estado.equals("imprimir"))
			{
				if(!convenioForm.getEstadoTemp().trim().equals(""))
					convenioForm.setEstado(convenioForm.getEstadoTemp());
				this.cerrarConexion(con);
				return mapping.findForward("imprimir");	
			}
			else if(estado.equals("adicionarUsuarios"))
			{				
				return this.accionAdicionarUsuarios(convenioForm, usuario, mapping, con, mundo);
			}
			else if (estado.equals("cargarUsuarios"))
			{
				//mundo.cargarInstitucionesSirc(connection, forma, usuario);
				//UtilidadBD.cerrarConexion(con);
				this.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			else if (estado.equals("nuevoUsuario"))
			{
				//mundo.cargarInstitucionesSirc(connection, forma, usuario);
				//UtilidadBD.cerrarConexion(con);
				//this.cerrarConexion(con);
				return this.accionNuevoAdicionarUsuarios(convenioForm, usuario, mapping, con);
			}
			else if (estado.equals("adicionar"))
			{
				
				//this.cerrarConexion(con);
				this.adicionar(con, convenioForm, usuario);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("adicionarNuevosUsuarios");
			}
			else if(estado.equals("modificarUsuario"))
			{
				convenioForm.resetMensaje();
				logger.info("===> Entro a modificarUsuario !!!");
				this.cargarUsuarios(con, convenioForm, usuario);
				this.cerrarConexion(con);
				return mapping.findForward("adicionarUsuarios");
			}
			else if(estado.equals("guardarModificacionUsuario"))
			{
				convenioForm.resetMensaje();
				logger.info("===> Entro a guardarModificacion !!!");
				this.modificar(con, convenioForm, usuario);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("adicionarUsuarios");
			}
			else if (estado.equals("ordenar"))
			{
				this.ordenar(convenioForm);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("adicionarUsuarios");	
			}
			else if (estado.equals("redireccion"))
			{
				convenioForm.resetMensaje();
				UtilidadBD.closeConnection(con);
				response.sendRedirect(convenioForm.getLinkSiguiente());
				return null;
			}else if(estado.equals("mostrarOpcionesCapitacion")){
				return mostrarOpcionesCapitacion(mapping,convenioForm,usuario,con);
			}
			else
			{
				convenioForm.reset(usuario.getCodigoInstitucionInt());
				logger.warn("Estado no valido dentro del flujo de registro convenio (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				this.cerrarConexion(con);
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

	
	//*************************Cambios Anexo 753 Secreto 7474 Andres Ortiz*********************************************************************
	/**
	 * 
	 */
	private  ActionForward accionAdicionarCorreo(Connection con,ActionMapping mapping,
			                                     ConvenioForm forma,
			                                     UsuarioBasico usuario,
			                                     String[] indices)throws SQLException
	{
		Utilidades.nuevoRegistroMapaGenerico(forma.getCorreosElectronicos(),indices,"numRegistros","tiporegistro_","MEM");
		forma.getCorreosElectronicos().put("mail_"+(Utilidades.convertirAEntero(forma.getCorreosElectronicos().get("numRegistros")+"")-1), "");
		forma.getCorreosElectronicos().put("eliminar_"+(Utilidades.convertirAEntero(forma.getCorreosElectronicos().get("numRegistros")+"")-1), ConstantesBD.acronimoNo);
		logger.info("Numero de registros >>"+forma.getCorreosElectronicos().get("numRegistros")+"");
		
		forma.setEstado(forma.getEstadoSecundario());
		logger.info("Estado secundario en el Action: "+forma.getEstadoSecundario());
		if(forma.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
			return mapping.findForward("principal");
		}
		else 
			if(forma.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
				return mapping.findForward("principalOdontologia");
		}
		return mapping.findForward("principal");
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
    private ActionForward accionEliminarCorreo(ActionMapping mapping,
             									ConvenioForm forma)
    {
    	 logger.info("Posicion a eliminar: " +forma.getPosicionMail()); 
    	 forma.getCorreosElectronicos().put("eliminar_"+forma.getPosicionMail(),ConstantesBD.acronimoSi);
    	 logger.info("IMPRESION MAPA EMAIL >>>");
    	 Utilidades.imprimirMapa(forma.getCorreosElectronicos());
    	 
    	 forma.setEstado(forma.getEstadoSecundario());
    	 logger.info("Estado secundario en el Action: "+forma.getEstadoSecundario());
    	 if(forma.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
 			return mapping.findForward("principal");
 		}
 		else 
 			if(forma.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
 				return mapping.findForward("principalOdontologia");
 		}
 		return mapping.findForward("principal");
 		
    }
	//********************************************************************************************************************************
	
	/**
	 * Se encarga del ordenamiento almacenando los criteros
	 * de busqueda.
	 * @param forma
	 */
	private void ordenar(ConvenioForm forma)
	{
		logger.info("===> Vamos a ordenar !!!");
		int numReg = Integer.parseInt(forma.getAdicionarUsuariosConvenio("numRegistros")+"");
		logger.info("===> numReg: "+numReg);
		forma.setAdicionarUsuariosConvenio(Listado.ordenarMapa(indicesConceptos, forma.getPatronOrdenar(), forma.getUltimoPatron(), 
				forma.getAdicionarUsuariosConvenio(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setAdicionarUsuariosConvenio("numRegistros", numReg);
		logger.info("===> Mapa ordenado: "+forma.getAdicionarUsuariosConvenio());
	}
	
	/**
	 * Metodo encargado de realizar una modificacion a un usuario seleccionado
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void modificar (Connection con, ConvenioForm forma, UsuarioBasico usuario)
	{
		logger.info("===> El mapa es: "+forma.getAdicionarUsuariosConvenio());
		logger.info("===> El usuario es: "+forma.getAdicionarUsuariosConvenio("usuario_"+forma.getPosicion()+""));
		logger.info("===> La posicion es: "+forma.getPosicion());
		
		if((forma.getAdicionarUsuariosConvenio("tipoUsuario_"+forma.getPosicion()+"").equals("Auditor"))){
			forma.setAdicionarUsuariosConvenio("tipoUsuario_"+forma.getPosicion(),ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor);
		}
		else if((forma.getAdicionarUsuariosConvenio("tipoUsuario_"+forma.getPosicion()+"").equals("Glosa"))){
			forma.setAdicionarUsuariosConvenio("tipoUsuario_"+forma.getPosicion(),ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa);
		}
		
		if(forma.getAdicionarUsuariosConvenio("usuario_"+forma.getPosicion()+"").equals(""))
		{
			logger.info("===> El usuario vino NULL: ");
			forma.setMensaje(new ResultadoBoolean(true,
				"ES NECESARIO SELECCIONAR UN USUARIO PARA REALIZAR LA MODIFICACIoN"));
		}
		else
		{
			logger.info("===> Hola Pipe !!! :P");
			HashMap <String, Object> criterios = new HashMap <String, Object>();
			logger.info("===> El tipo convenio es: "+forma.getTipoConvenio());
			forma.setAdicionarUsuariosConvenio("convenio", Integer.parseInt(forma.getTipoConvenio()));
			logger.info("===> forma.getposicion trae: "+forma.getPosicion());
			criterios.put("codigo", forma.getAdicionarUsuariosConvenio("codigo_"+forma.getPosicion()+""));
			criterios.put("usuario", forma.getAdicionarUsuariosConvenio("usuario_"+forma.getPosicion()+""));
			criterios.put("tipousuario", forma.getAdicionarUsuariosConvenio("tipoUsuario_"+forma.getPosicion()+""));
			criterios.put("activo", forma.getAdicionarUsuariosConvenio("activo_"+forma.getPosicion()+""));
			criterios.put("convenio", forma.getCodigo());
			criterios.put("usuariomodifica", usuario.getLoginUsuario());
			logger.info("===> El mapa criterios es: "+criterios);
			logger.info("===> El num registros de criterios es: "+criterios.get("numRegistros"));
			logger.info("===> Los AdicionarUsuariosConvenio Son: "+forma.getAdicionarUsuariosConvenio());
			logger.info("===> Los numRegistros de AdicionarUsuariosConvenio Son: "+forma.getAdicionarUsuariosConvenio("numRegistros"));
			if(forma.getAdicionarUsuariosConvenio("usuario_"+forma.getPosicion()+"").equals(""))
			{
				logger.info("===> El usuario viene vacio, hay que seleccionar un usuario");
			}
			else
			{
				logger.info("===> El usuario es: "+criterios.get("usuarios"));
			}
			
			UtilidadBD.iniciarTransaccion(con);
			boolean transaccion = modificarUsuariosGlosasConvenio(con, criterios);
			if(transaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON EXITO !!!"));
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,
						"SE PRESENTARON INCONVENIENTES EN EL ALMACENAMIENTO DE USUARIOS GLOSAS CONVENIO"));
				UtilidadBD.abortarTransaccion(con);
			}
			forma.setAdicionarUsuariosConvenio(consultaUsuariosGlosasPorConvenio(con, forma.getCodigo()+""));
		}
	}
	
	/**
	 * Instancia del Metodo modificarUsuariosGlosasConvenio
	 * @param con
	 * @param datos
	 * @return
	 */
	private boolean modificarUsuariosGlosasConvenio (Connection con, HashMap datos)
	{
		return ConvenioDao().modificarUsuariosGlosasConvenio(con, datos);
	}

	/**
	 * Metodo encargado de adicionar los usuarios con sus campos
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void adicionar (Connection con, ConvenioForm forma, UsuarioBasico usuario)
	{
		logger.info("===> El usuario es: "+forma.getAdicionarUsuariosConvenio("usuarios"));
		//La bandera 0 signifnica que el usuario va a validarse si se puede insertar
		int bandera= 0, flag=0;
		
		if((forma.getAdicionarUsuariosConvenio("tipousuario").equals("Auditor"))){
			forma.setAdicionarUsuariosConvenio("tipousuario",ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor);
		}
		else if((forma.getAdicionarUsuariosConvenio("tipousuario").equals("Glosa"))){
			forma.setAdicionarUsuariosConvenio("tipousuario",ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa);
		}
		
		if(forma.getAdicionarUsuariosConvenio("usuarios").equals(""))
		{
			logger.info("===> El usuario es vino NULL: ");
			forma.setMensaje(new ResultadoBoolean(true,
				"ES NECESARIO SELECCIONAR UN USUARIO PARA REALIZAR LA INSERCIoN"));
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			if(forma.getAdicionarUsuariosConvenio("activo").equals(""))
			{
				logger.info("===> EL activo vino null, eso quiere decir que activo es true ");
				forma.setAdicionarUsuariosConvenio("activo", "S");
			}
			HashMap <String, Object> criterios = new HashMap <String, Object>();
			String usuarioRepetido = forma.getAdicionarUsuariosConvenio("usuarios")+"";
			String usuarioBuscar = "";
			logger.info("===> El tipo convenio es: "+forma.getTipoConvenio());
			forma.setAdicionarUsuariosConvenio("convenio", Integer.parseInt(forma.getTipoConvenio()));
			logger.info("===> forma.getposicion trae: "+forma.getPosicion());
			criterios.put("usuario", forma.getAdicionarUsuariosConvenio("usuarios"));
			criterios.put("tipousuario", forma.getAdicionarUsuariosConvenio("tipousuario"));
			criterios.put("activo", forma.getAdicionarUsuariosConvenio("activo"));
			criterios.put("convenio", forma.getCodigo());
			criterios.put("usuariomodifica", usuario.getLoginUsuario());
			criterios.put("tipoliquidacionpool", forma.getTipoLiquidacionPool());
			logger.info("===> El mapa criterios es: "+criterios);
			logger.info("===> El num registros de criterios es: "+criterios.get("numRegistros"));
			logger.info("===> Los AdicionarUsuariosConvenio Son: "+forma.getAdicionarUsuariosConvenio());
			logger.info("===> Los numRegistros de AdicionarUsuariosConvenio Son: "+forma.getAdicionarUsuariosConvenio("numRegistros"));
			if(forma.getAdicionarUsuariosConvenio("usuarios").equals(""))
			{
				logger.info("===> El usuario viene vacio, hay que seleccionar un usuario");
			}
			else
			{
				logger.info("===> El usuario es: "+criterios.get("usuario"));
				
			}
			
			for(int i=0; i<Utilidades.convertirAEntero(forma.getAdicionarUsuariosConvenio("numRegistros")+""); i++)
			{
				logger.info("===> Entro al for !!! "+i+" veces");
				logger.info("===> usuarioRepetido es: "+usuarioRepetido);
				usuarioBuscar = forma.getAdicionarUsuariosConvenio("usuario_"+i)+"";
				logger.info("===> el usuario a buscar es: "+usuarioBuscar);
				if(usuarioRepetido.equals(usuarioBuscar))
				{
					logger.info("===> Pipe !!! hay un usuario repetido, hay que sacar error");
					// La bandera en valor 1 signnifica que el usuario ya existe en la tabla, por tanto no se puede insertar
					bandera=1;
					// Se utiliza flag para validar de que el usuario por lo menos fue encontrado 1 vez en la tabla
					flag=1;
				}
				else
				{
					//La bandera en valor 2 significa que hay un usuario que se puede agregar normalmente
					logger.info("===> Pipe !!! Vamos a insertar el Usuario !!!");
					bandera=2;
				}
			}
			if(bandera==1 || flag==1)
			{
				logger.info("===>bandera = "+bandera);
				logger.info("===>flag = "+flag);
				forma.setMensaje(new ResultadoBoolean(true,
						"EL USUARIO SELECCIONADO YA EXISTE EN LA LISTA\n POR FAVOR VUELVA Y SELECCIONE UN USUARIO"));
				UtilidadBD.abortarTransaccion(con);
			}
			else if (bandera==2 || bandera == 0 )
			{
				logger.info("===> La bandera viene en: "+bandera);
				logger.info("===> The flag comes in: "+flag);
				UtilidadBD.iniciarTransaccion(con);
				boolean transaccion = insertarUsuariosGlosasConvenio(con, criterios);
				if(transaccion)
				{
					forma.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON EXITO !!!"));
					UtilidadBD.finalizarTransaccion(con);
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,
							"SE PRESENTARoN INCONVENIENTES EN EL ALMACENAMIENTO DE USUARIOS GLOSAS CONVENIO"));
					UtilidadBD.abortarTransaccion(con);
				}
				
				forma.setAdicionarUsuariosConvenio(consultaUsuariosGlosasPorConvenio(con, forma.getCodigo()+""));
			}
		}
	}
		
	/**
	 * Instancia de insertarUsuariosGlosasConvenio
	 * @param con
	 * @param datos
	 * @return
	 */
	private boolean insertarUsuariosGlosasConvenio (Connection con, HashMap datos)
	{
		return ConvenioDao().insertarUsuariosGlosasConvenio(con, datos);
	}
	
	/**
	 * Metodo encargado de carlar los usuarios
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void cargarUsuarios (Connection con, ConvenioForm forma, UsuarioBasico usuario)
	{
		forma.setAdicionarUsuariosConvenio("usuario", ConstantesBD.codigoNuncaValido);
		forma.setAdicionarUsuariosConvenio("tipousuario", ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor);
		forma.setAdicionarUsuariosConvenio("activo", ConstantesBD.acronimoSi);
		
		forma.setAdicionarUsuariosConvenio(consultaUsuariosGlosasPorConvenio(con, forma.getCodigo()+""));
		logger.info("===> Los AdicionarUsuariosConvenio Son: "+forma.getAdicionarUsuariosConvenio());
		forma.setUsuarios(Utilidades.obtenerUsuarios(con, usuario.getCodigoInstitucionInt(),false));
	}
	
	
	/**
	 * Instancia del consultaUsuariosGlosasConvenio
	 * @param con
	 * @return
	 */
	
	public static HashMap consultaUsuariosGlosasConvenio (Connection con)
	{
		return ConvenioDao().consultaUsuariosGlosasConvenio(con);
	}
	
	/**
	 * Instancia del consultaUsuariosGlosasPorConvenio
	 * @param con
	 * @return
	 */
	
	public static HashMap consultaUsuariosGlosasPorConvenio (Connection con, String convenio)
	{
		return ConvenioDao().consultaUsuariosGlosasPorConvenio(con, convenio);
	}
	
	/**
	 * Se inicializa el Dao
	 */
		private static ConvenioDao ConvenioDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao();
	}

	/**
	 * Metodo encargado de adicionar un nuevo usuario a la lista
	 * @param convenioForm
	 * @param usuario
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	
	private ActionForward accionNuevoAdicionarUsuarios (
			ConvenioForm convenioForm, 
			UsuarioBasico usuario, 
			ActionMapping mapping,
			Connection con) throws SQLException
	{
	logger.info("===> Entro a accionNuevoAdicionarUsuario !!!");
	this.cargarUsuarios(con, convenioForm, usuario);
	this.cerrarConexion(con);
	return mapping.findForward("adicionarNuevosUsuarios");
	}
	
	
	/**
	 * Metodo encargado de adicionar N usuarios para 
	 * Auditoroa de facturas y registro de glosas
	 * 
	 * @param convenioForm
	 * @param usuario
	 * @param mapping Mapping para manejar la nevagacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward a la pagina principal "convenio.jsp"
	 * @throws SQLException 
	 */
	
	private ActionForward accionAdicionarUsuarios (	ConvenioForm convenioForm, 
													UsuarioBasico usuario, 
													ActionMapping mapping,
													Connection con,
													Convenio mundo) throws SQLException
	{
		convenioForm.resetMensaje();
		convenioForm.setAdicionarUsuariosConvenio(consultaUsuariosGlosasPorConvenio(con, convenioForm.getCodigo()+""));
		
		convenioForm.setPosicion("0");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("adicionarUsuarios");
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param convenioForm ConvenioForm
	 * 				para pre-llenar datos si es necesario
	 * @param usuario 
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward a la pagina principal "convenio.jsp"
	 * @throws SQLException
	 */
	
	private ActionForward accionEmpezarGeneral(ConvenioForm convenioForm, 
			 UsuarioBasico usuario, ActionMapping mapping, 
			 Connection con) throws SQLException
	{
			//Limpiamos lo que venga del form
		convenioForm.reset();
		convenioForm.reset(usuario.getCodigoInstitucionInt());
		
		// Capturamos la Informacion de la tabla Centros Costo para el Plan Especial
		Convenio mundoConvenio=new Convenio ();
		convenioForm.setPlanEspecialList(mundoConvenio.cargarPlanEspecial(con,usuario.getCodigoInstitucion()));
		convenioForm.setCcContableList(mundoConvenio.consultarCcContable(con, usuario.getCodigoInstitucion()));
		try {
			convenioForm.setCenAtencContableList(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));
		}
		catch (Errores e)
		{
			logger.info("Errores consultar centros de atencion ",e);
		}
		convenioForm.setEstado("empezarGeneral");
		this.cerrarConexion(con);
		
		convenioForm.setTipoAtencion(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param convenioForm
	 * @param usuario
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarOdontologia(ConvenioForm convenioForm, 
			 UsuarioBasico usuario, ActionMapping mapping, 
			 Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		convenioForm.resetOdontologia(usuario.getCodigoInstitucionInt());
		
		// Capturamos la Informacion de la tabla Centros Costo para el Plan Especial
		Convenio mundoConvenio=new Convenio ();
		convenioForm.setPlanEspecialList(mundoConvenio.cargarPlanEspecial(con,usuario.getCodigoInstitucion()));
		convenioForm.setCcContableList(mundoConvenio.consultarCcContable(con, usuario.getCodigoInstitucion()));
		convenioForm.setEstado("empezarOdontologia");
		this.cerrarConexion(con);
		
		convenioForm.setTipoAtencion(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico);
		convenioForm.setCcContableList(mundoConvenio.consultarCcContable(con, usuario.getCodigoInstitucion()));
		return mapping.findForward("principalOdontologia");
	}
	
	
	private ActionForward accionEmpezar( ConvenioForm convenioForm, 
										 UsuarioBasico usuario, ActionMapping mapping, 
										 Connection con) throws SQLException
	{
		
		
			
		
		convenioForm.setOdontologiaActivo(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt())));
		
		if(convenioForm.isOdontologiaActivo())
		{

				
				
			return mapping.findForward("principalMenu");
			
			
				
		}
		else 
		{

			
			 return accionEmpezarGeneral(convenioForm,usuario,mapping, con);
			
		}
			
	 }	
				

				

	

				
	

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto convenio
	 * en el objeto mundo
	 * 
	 * @param convenioForm ConvenioForm
	 * @param usuario 
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * 
	 * @return ActionForward "convenio.do?estado=resumen"
	 * @throws SQLException 
	 * @throws SQLException
	*/
	private ActionForward accionSalir(	 ConvenioForm convenioForm,
										 UsuarioBasico usuario, ActionMapping mapping,
										 Connection con,
										 HttpServletRequest request) throws SQLException 
	{
		Convenio mundoConvenio=new Convenio ();
		
		llenarMundo(convenioForm, mundoConvenio,usuario);
		
		UtilidadBD.iniciarTransaccion(con);
		convenioForm.setCodigo(mundoConvenio.insertarConvenio(con, usuario));
		
		//Genero el log
	    generarLog(mundoConvenio, convenioForm, usuario);
		
		if(convenioForm.getCodigo()<=0)
		{	
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping,request,con,logger,"","error.facturacion.errorInsertConvenio",true);
		}
		llenarMediosAutorizacionForma(convenioForm, usuario);
		
		if(!Convenio.insertarMediosAutorizacion(convenioForm.getMediosAutorizacion(), con))
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping,request,con,logger,"","error.facturacion.errorMediosAutoConvenio",true);
		}
		if(Convenio.insertarMediosEnvio(con,usuario,convenioForm.getCodigo(), convenioForm.getMediosEnvio())<=0)
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping,request,con,logger,"","error.facturacion.errorMediosEnvioConvenio",true);
		}
		if(Convenio.insertarCorreosElectronicos(con, usuario, convenioForm.getCodigo(),convenioForm.getCorreosElectronicos())<=0)
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping,request,con,logger,"","error.facturacion.errorEmailConvenio",true);
		}
		logger.info("!!!!!!!!!!!!!!!!inserto 100%!!!!!!!!!!!!!!");
		UtilidadBD.finalizarTransaccion(con);
		this.cerrarConexion(con);
		return mapping.findForward("funcionalidadResumenConvenio");
	}

	/**
	 * 
	 * @param convenioForm
	 * @param usuario
	 */
	private void llenarMediosAutorizacionForma(ConvenioForm convenioForm,
			UsuarioBasico usuario) 
	{
		for(int w=0; w<4; w++)
		{	
			convenioForm.getMediosAutorizacion().get(w).setFechaModifica(UtilidadFecha.getFechaActual());
			convenioForm.getMediosAutorizacion().get(w).setHoraModifica(UtilidadFecha.getHoraActual());
			convenioForm.getMediosAutorizacion().get(w).setUsuarioModifica(usuario.getLoginUsuario());
			convenioForm.getMediosAutorizacion().get(w).setConvenio(convenioForm.getCodigo());
			
			logger.info("************ el medio aca es :"+ UtilidadLog.obtenerString(convenioForm.getMediosAutorizacion().get(w), true) );
		}	
		
		
		
	}
	
	private void llenarMediosAutorizacionMundo(Convenio Mundo,
			UsuarioBasico usuario) 
	{
		
		
		for(int w=0; w<4; w++)
		{	
			Mundo.getMediosAutorizacion().get(w).setFechaModifica(UtilidadFecha.getFechaActual());
			Mundo.getMediosAutorizacion().get(w).setHoraModifica(UtilidadFecha.getHoraActual());
			Mundo.getMediosAutorizacion().get(w).setUsuarioModifica(usuario.getLoginUsuario());
			Mundo.getMediosAutorizacion().get(w).setConvenio(Mundo.getCodigo());
			
			logger.info("************ el medio aca es :"+ UtilidadLog.obtenerString(Mundo.getMediosAutorizacion().get(w), true) );
		}	
		
		
		
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * resumen
	 * @param convenioForm ConvenioForm
	 * @param usuario 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward a la pagina de resumen "resumenConvenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	ConvenioForm convenioForm, 
											UsuarioBasico usuario, ActionMapping mapping, 
											HttpServletRequest request, 
											Connection con) throws SQLException, IPSException
	{
		Convenio mundoConvenio=new Convenio();
		mundoConvenio.setCodigo(convenioForm.getCodigo());
		logger.info("---------------------------------------------------------------------------------------HACIENDO LAS CONSULTAS ------------------------------------------------>"+mundoConvenio.existeConvenioOdontologico(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico,usuario.getCodigoInstitucionInt()));
		if(mundoConvenio.getCodigo()>0)
		{ 
			
			boolean validarCargar=mundoConvenio.cargarResumen(con,mundoConvenio.getCodigo());
			if(validarCargar)
			{
				llenarForm(convenioForm,mundoConvenio);
				if(convenioForm.getCenAtencContableList()!=null)
				{
					Iterator listaCentrosAtencion=convenioForm.getCenAtencContableList().iterator();
					boolean encontro=false;
					while(listaCentrosAtencion.hasNext())
					{
						HashMap<String, String> convenio=(HashMap<String, String>)listaCentrosAtencion.next();
						if(Utilidades.convertirAEntero(convenio.get("codigo"))==convenioForm.getCenAtencContable())
						{
							convenioForm.setNombreCentroAtencionContable(convenio.get("descripcion"));
							encontro=true;
							break;
						}
					}
					
					if(!encontro)
					{
						convenioForm.setNombreCentroAtencionContable("");
					}
				}
				//Consulto el Nombre del Centro Costo Plan Especial para el Convenio.
				logger.info("\n\n\nVALOR RESUMEN PLAN ESPECIAL---->"+convenioForm.getPlanEspecial());
				if(convenioForm.getPlanEspecial().equals("0"))
				{
					convenioForm.setNombreCentroCostoPlanEspecial("");
					logger.info("NOMBRE PLAN ESPECIAL sin"+convenioForm.getNombreCentroCostoPlanEspecial());
				}
				else
				{
					convenioForm.setNombreCentroCostoPlanEspecial(mundoConvenio.cargarNombreCentroCostoPlanEspecial(con, convenioForm.getPlanEspecial()));
					logger.info("NOMBRE PLAN ESPECIAL con"+convenioForm.getNombreCentroCostoPlanEspecial());
				}
				
				//Consulto el Nombre del Centro Costo Contable para el Convenio.
				logger.info("\n\n\nVALOR RESUMEN CONTABLE---->"+convenioForm.getCcContable());
				if(convenioForm.getCcContable().equals("0") || convenioForm.getCcContable().equals(""))
				{
					convenioForm.setNombreCentroCostoContable("");
					logger.info("NOMBRE CONTABLE sin"+convenioForm.getNombreCentroCostoContable());
				}
				else
				{
					convenioForm.setNombreCentroCostoContable(mundoConvenio.cargarNombreCentroCostoPlanEspecial(con, convenioForm.getCcContable()));
					logger.info("NOMBRE CONTABLE con"+convenioForm.getNombreCentroCostoContable());
				}
				this.cerrarConexion(con);		
				
				if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
				return mapping.findForward("paginaResumenConvenio");
				}else{
					return mapping.findForward("paginaResumenConvenioOdontologia");
				}
			}
			else
			{
				logger.warn("Codigo convenio invalido "+convenioForm.getCodigo());
				this.cerrarConexion(con);
				convenioForm.reset(usuario.getCodigoInstitucionInt());
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Codigo convenio");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
			}
		}
		else
		{
			logger.warn("(Cargar ultima insercion)Codigo convenio invalido "+convenioForm.getCodigo());
			this.cerrarConexion(con);
			convenioForm.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Codigo convenio");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * modificar convenio
	 * @param convenioForm ConvenioForm
	 * @param usuario 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward  a la pagina "modificarConvenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(	ConvenioForm convenioForm, 
											UsuarioBasico usuario, HttpServletRequest request, 
											ActionMapping mapping, 
											Connection con) throws SQLException, IPSException
	{
		Convenio mundoConvenio=new Convenio();
		mundoConvenio.reset();
		
		
		//Consulto el Nombre del Centro Costo Plan Especial para el Convenio.
		//convenioForm.setNombreCentroCostoPlanEspecial(mundoConvenio.cargarNombreCentroCostoPlanEspecial(con, convenioForm.getPlanEspecial()));
		mundoConvenio.cargarResumen(con,convenioForm.getCodigo());
		logger.info("**************** TIPO ATENCION:!"+mundoConvenio.getTipoAtencion());
		logger.info("**************** MEDIOS       :!"+(mundoConvenio.getMediosAutorizacion().size())+ (mundoConvenio.getMediosAutorizacion().get(0).getActivo())+ (mundoConvenio.getMediosAutorizacion().get(1).getActivo())+ (mundoConvenio.getMediosAutorizacion().get(2).getActivo())+ (mundoConvenio.getMediosAutorizacion().get(3).getActivo()));
		llenarForm(convenioForm,mundoConvenio);
		try {
			convenioForm.setCenAtencContableList(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));
		}
		catch (Errores e)
		{
			logger.error("Errores consultar centros de atencion ",e);
		}
		
		convenioForm.setTipoContratoCapitado(false);
		if(ConstantesBD.codigoTipoContratoCapitado==convenioForm.getTipoContrato()){
			convenioForm.setTipoContratoCapitado(true);
			ArrayList<DtoCheckBox> listaCapitacionSubcontratada = new ArrayList<DtoCheckBox>();
			DtoCheckBox dto = new DtoCheckBox();						
			
			String valorSi = (String)ValoresPorDefecto.getIntegridadDominio(ConstantesBD.acronimoSi);
			String valorNo = (String)ValoresPorDefecto.getIntegridadDominio(ConstantesBD.acronimoNo);
			dto.setNombre(valorSi);
			dto.setCodigo(ConstantesBD.acronimoSi);
			listaCapitacionSubcontratada.add(dto);
			
			dto = new DtoCheckBox();
			dto.setNombre(valorNo);
			dto.setCodigo(ConstantesBD.acronimoNo);
			listaCapitacionSubcontratada.add(dto);		
			
			convenioForm.setListaCapitacionSubcontratada(listaCapitacionSubcontratada);
		}
		
		convenioForm.setEstado("modificar");
		if(convenioForm.getCodigo()<0)
		{
			logger.warn("No se pudo cargar el Convenio: "+convenioForm.getCodigo());
			this.cerrarConexion(con);
			convenioForm.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add("El codigo del Convenio involido ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
			
			//********** LOG GENERAL
			String logGeneral="\n            ====INFORMACION ORIGINAL===== " +
				"\n*  Codigo Convenio [" +mundoConvenio.getCodigo() +"] "+
				"\n*  Codigo Empresa ["+mundoConvenio.getEmpresa()+"] " +
				"\n* 	Tipo Regimen ["+mundoConvenio.getTipoRegimen()+"] "+
				"\n*  Nombre Convenio ["+mundoConvenio.getNombre()+"] " +
				"\n*  Observaciones ["+mundoConvenio.getObservaciones()+"] "+
				"\n*  Plan Beneficios ["+mundoConvenio.getPlanBeneficios()+"] "+
				"\n*  Codigo MinSalud ["+mundoConvenio.getCodigoMinSalud()+"] " +
				"\n*  Formato Factura ["+mundoConvenio.getFormatoFactura()+"] " +
				"\n*  Tipo Contrato ["+mundoConvenio.getTipoContrato()+"] " +
				"\n*  Numero Dias Vencimiento ["+mundoConvenio.getNumeroDiasVencimiento()+"] "+
				"\n*  Requiere Justificacion Servicios ["+mundoConvenio.getRequiereJustificacionServicios()+"] "+
				"\n*  Requiere Justificacion Articulos ["+mundoConvenio.getRequiereJustificacionArticulos()+"] "+
				"\n*  Maneja Complejidad ["+mundoConvenio.getManejaComplejidad()+"] " +
				"\n*  Semanas Minimas Cotizacion ["+mundoConvenio.getSemanasMinimasCotizacion()+"] " +
				"\n*  Requiere Ingreso Numero Carnte ["+mundoConvenio.getRequiereNumeroCarnet()+"] " +
				"\n*  Metodo de Ajuste para Calculo de Tarifa de Servicios ["+mundoConvenio.getAjusteServicios()+"] "+
				"\n*  Metodo de Ajuste para Calculo de Tarifa de Articulos ["+mundoConvenio.getAjusteArticulos()+"] "+
				"\n*  Interfaz ["+mundoConvenio.getInterfaz()+"] "+
				"\n*  Tipo Convenio ["+mundoConvenio.getTipoConvenio()+"] " +
				"\n*  Tipo Convenio ["+mundoConvenio.getTipoCodigo()+"] " +
				"\n*  Centro Costo Plan Especial ["+mundoConvenio.getPlanEspecial()+"] " +
				"\n*  Excento Deudor ["+mundoConvenio.getExcentoDeudor()+"] " +
				"\n*  Excento Documento Garantia ["+mundoConvenio.getExcentoDocumentoGarantia()+"] " +
				"\n*  VIP ["+mundoConvenio.getVip()+"] " +
				"\n*  Radicar Cuentas Negativas ["+mundoConvenio.getRadicarCuentasNegativas()+"] " +
				"\n*  Asignar en la Factura Como Valor del Paciente el Valor de Abonos ["+mundoConvenio.getAsignarFactValorPacValorAbono()+"]" ;
			
				if(!UtilidadTexto.isEmpty(mundoConvenio.getCantidadMaxCirugia()))
					logGeneral+="\n*  Cantidad Maxima de Cirugias Adicionales de Paga ["+mundoConvenio.getCantidadMaxCirugia()+"] ";
				else
					logGeneral+="\n*  Cantidad Maxima de Cirugias Adicionales de Paga [] ";
				
				if(!UtilidadTexto.isEmpty(mundoConvenio.getCantidadMaxAyudpag()))
					logGeneral+="\n*  Cantidad Maxima de Ayudantes que Paga ["+mundoConvenio.getCantidadMaxAyudpag()+"] ";
				else
					logGeneral+="\n*  Cantidad Maxima de Ayudantes que Paga [] ";
				
				logGeneral+=
				"\n*  Tipo de Liquidacion Salas Cirugia ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionScx())+"] " +
				"\n*  Tipo de Liquidacion Salas No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionDyt())+"] " +
				"\n*  Tipo de Liquidacion General Cirugias ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionGcx())+"] " +
				"\n*  Tipo de Liquidacion General No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionGdyt())+"] " +
				"\n*  Tipo de Tarifa para Liquidacion Materiales Cirugia ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoTarifaLiqMateCx())+"]" +
				"\n*  Tipo de Tarifa para Liquidacion Materiales No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoTarifaLiqMateDyt())+"]" +
				"\n*  Tipo de Fecha para Liquidacion Tiempos Cirugia: ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoFechaLiqTiemPcx())+"]" +
				"\n*  Tipo de Fecha para Liquidacion Tiempos No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoFechaLiqTiempDyt())+"]";
		
				logGeneral+=
				"\n*  Cantidad Maxima de Cirugias Adicionales de Paga ["+mundoConvenio.getCantidadMaxCirugia()+"] " +
				"\n*  Cantidad Maxima de Ayudantes que Paga ["+mundoConvenio.getCantidadMaxAyudpag()+"] "+ 
				"\n*  Tipo de Liquidacion Salas Cirugia ["+mundoConvenio.getTipoLiquidacionScx()+"] " +
				"\n*  Tipo de Liquidacion Salas No Cruentos ["+mundoConvenio.getTipoLiquidacionDyt()+"] " +
				"\n*  Tipo de Liquidacion General Cirugias ["+mundoConvenio.getTipoLiquidacionGcx()+"] " +
				"\n*  Tipo de Liquidacion General No Cruentos ["+mundoConvenio.getTipoLiquidacionGdyt()+"] " +
				"\n*  Tipo de Tarifa para Liquidacion Materiales Cirugia ["+mundoConvenio.getTipoTarifaLiqMateCx()+"]" +
				"\n*  Tipo de Tarifa para Liquidacion Materiales No Cruentos ["+mundoConvenio.getTipoTarifaLiqMateDyt()+"]" +
				"\n*  Tipo de Fecha para Liquidacion Tiempos Cirugia: ["+mundoConvenio.getTipoFechaLiqTiemPcx()+"]" +
				"\n*  Tipo de Fecha para Liquidacion Tiempos No Cruentos ["+mundoConvenio.getTipoFechaLiqTiempDyt()+"]" +
				"";
				
			if(mundoConvenio.getPyp().equals("true"))		
				logGeneral+="\n*  PyP [SI]	 " ;
			else
				logGeneral+="\n*  PyP [NO]	 " ;
			if(mundoConvenio.getActiva())
				logGeneral += "\n*  Activa [ SI ]";
			else
				logGeneral += "\n*  Activa [ NO ]";
			
			logGeneral+="\n*  Encabezado Factura ["+mundoConvenio.getEncabezadoFactura()+"]";
			logGeneral+="\n*  Pie Factura ["+mundoConvenio.getPieFactura()+"]";
			
			if(mundoConvenio.getAseguradora().equals(ConstantesBD.acronimoSi))
			{
				logGeneral += "\n*  Aseguradora [ SI ]";
				logGeneral += "\n*  Codigo Aseguradora [ "+mundoConvenio.getCodigoAseguradora()+" ]";
			}
			else
				logGeneral += "\n*  Aseguradora [ NO ]";
			
			if(mundoConvenio.getValorLetrasFactura().equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaConvenio))
				logGeneral += "\n*  Valor Letras Factura [CONVENIO]";
			
			if(mundoConvenio.getValorLetrasFactura().equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaCargo))
				logGeneral += "\n*  Valor Letras Factura [TOTAL CARGOS]";
			
			
			
			//********** LOG ODONTOLOGIA
			
			
			
			String logOdontologia ="\n            ====INFORMACION ORIGINAL===== " +
				"\n*	Codigo Convenio [" +mundoConvenio.getCodigo() +"]" +
				"\n*	Codigo Empresa [" +mundoConvenio.getEmpresa() +"]" +
				"\n*	Nombre Convenio [" +mundoConvenio.getNombre() +"]" +
				"\n*	Codigo MinSalud [" +mundoConvenio.getCodigoMinSalud() +"]" +
				"\n*	Tipo Regimen [" +mundoConvenio.getTipoRegimen() +"]" +
				"\n*	Tipo Contrato [" +mundoConvenio.getTipoContrato() +"]" +
				"\n*	Tipo Codigo [" +mundoConvenio.getTipoCodigo() +"]" +
				"\n*	Tipo Convenio [" +mundoConvenio.getTipoConvenio() +"]" +
				"\n*	Aseguradora [" +mundoConvenio.getAseguradora() +"]" +
				"\n* 	Codigo Aseguradora [" +mundoConvenio.getCodigoAseguradora() +"]" +
				"\n*	Maneja Bonos [" +mundoConvenio.getManejaBonos() +"]" +
				"\n*	Requerido Bono [" +mundoConvenio.getRequiereBono() +"]" +
				"\n*	Maneja Promocines [" +mundoConvenio.getRequiereBono() +"]" +
				"\n*	Convenio Tarjeta Cliente [" +mundoConvenio.getEsTargetaCliene() +"]" +
				"\n*	Plan de Beneficios [" +mundoConvenio.getPlanBeneficios() +"]" +
				"\n*	Codigo Interfaz [" +mundoConvenio.getInterfaz() +"]" +
				"\n*	E-Mail [" +mundoConvenio.getCorreosElectronicos() +"]" +
				"\n*	Observaciones [" +mundoConvenio.getObservaciones() +"]" +
				"\n*	Activa [" +mundoConvenio.getActiva() +"]" +
				"\n*	Requiere Ingreso Numero de Carnet [" +mundoConvenio.getRequiereNumeroCarnet() +"]" +
				"\n*	Ingresar Paciente Realizar Validacion en Base de Dato? [" +mundoConvenio.getIngresoBdValido() +"]" +
				"\n*	Ingresar Paciente Requiere Autorizacion [" +mundoConvenio.getIngresoPacienteReqAutorizacion() +"]" ;
					for(int i=0;i<mundoConvenio.getMediosAutorizacion().size();i++){
			     
			         if(mundoConvenio.getMediosAutorizacion().get(i).isActivo()){
			    	 logOdontologia+="\n*	Medio de Autorizacion:  " +mundoConvenio.getMediosAutorizacion().get(i).getTipo().getNombre() +"]" ;
			         }
			     }
			     logOdontologia+="\n*	Requiere Ingresar Valor Autorizado [" +mundoConvenio.getReqIngresoValido() +"]" +
			
				"\n*	Metodo de Ajuste para Calculo de Tarifa de Servicios [" +mundoConvenio.getAjusteServicios() +"]" +
				"\n*	Metodo de Ajuste para Calculo de Tarifa de Articulos [" +mundoConvenio.getAjusteArticulos() +"]" +
				"\n*	Numero dias Vencimiento [" +mundoConvenio.getNumeroDiasVencimiento() +"]" +
				"\n*	Formato Factura [" +mundoConvenio.getFormatoFactura() +"]" +
				"\n*	Encabezado Factura [" +mundoConvenio.getEncabezadoFactura() +"]" +
				"\n*	Pie Factura [" +mundoConvenio.getPieFactura() +"]" +
				UtilidadLog.obtenerString(mundoConvenio.getMediosAutorizacion(), true);
			
			
			
			
			
			
			
			
			 if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
				 convenioForm.setLogInfoOriginalConvenio(logGeneral);
		 		}
		 		else 
		 			if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
		 				convenioForm.setLogInfoOriginalConvenio(logOdontologia);
		 		}
		 		
		 
			
				
			
			convenioForm.setCodigo(mundoConvenio.getCodigo());
			
			/**
			 * Busco los contratos activos del convenio - Tarea 3653
			 */
			//Busco si el convenio posee contratos
			logger.info("\n\n********CONVENIO A MODIFICAR------->"+mundoConvenio.getCodigo());
			ArrayList<HashMap> contratosConvenio=new ArrayList<HashMap>();
			contratosConvenio=Utilidades.obtenerContratos(con, mundoConvenio.getCodigo(), true, true);
			//Reviso si al menos un contrato se encuentra activo (la fecha actual se encuentra entre la fecha ini y fecha fin parametrizadas para el contrato)
			for(int i=0;i<contratosConvenio.size();i++)
			{
				logger.info("ENTRE EN EL CICLO PARA EL CONTRATO--->"+i);
				if((!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()),UtilidadFecha.conversionFormatoFechaAAp(contratosConvenio.get(0).get("fechainicialbd")+"")))
					&&
					!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(contratosConvenio.get(0).get("fechafinalbd")+""),UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
				{
					convenioForm.setConvenioPoseeContratoActivo("disabled");
				}
			}
			if (contratosConvenio.size()==0)
				convenioForm.setConvenioPoseeContratoActivo("");
			
			logger.info("\n\n\n******CONVENIO POSEE CONTS------->"+convenioForm.getConvenioPoseeContratoActivo());
			
			
			this.cerrarConexion(con);
			
			 if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
		 			return mapping.findForward("principal");
		 		}
		 		else 
		 			if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
		 				return mapping.findForward("principalOdontologia");
		 		}
		 		return mapping.findForward("principal");
			
		}
	}	

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param convenioForm ConvenioForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward "convenio.do?estado=modificar"
	 * @throws SQLException
	 * @throws IPSException 
	 */
	private ActionForward accionGuardarModificacion(ConvenioForm convenioForm,
													HttpServletRequest request, 
													ActionMapping mapping, 
													Connection con)	
													throws SQLException, IPSException
	{
		
		
		    Convenio mundoConvenio=new Convenio ();  
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			llenarMundo(convenioForm, mundoConvenio,usuario);
            llenarMediosAutorizacionMundo(mundoConvenio, usuario);
		    mundoConvenio.modificarConvenio(con,usuario);
		    Convenio.modificarMediosAutorizacion(mundoConvenio.getCodigo(),mundoConvenio.getMediosAutorizacion(), con);
		
			//Genero el log
		    generarLog(mundoConvenio, convenioForm, usuario);
			

			this.cerrarConexion(con);
			
			if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
				return mapping.findForward("funcionalidadResumenConvenio");
				}else{
					return mapping.findForward("paginaResumenConvenioOdontologia");
				}
			
	}
	
	
	/**
	 * 
	 */
	
	private void generarLog(Convenio mundoConvenio, ConvenioForm convenioForm, UsuarioBasico usuario)
	{
				
		logger.info("\n\n\n********\n SE GENERO EL LOG EN-------->"+"  /web/logs/axioma/facturacion/MantenimientoTablas/");
		
		
				String logGeneral=convenioForm.getLogInfoOriginalConvenio()+
				"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
				"\n*  Codigo Convenio [" +mundoConvenio.getCodigo() +"] "+
				"\n*  Codigo Empresa ["+mundoConvenio.getEmpresa()+"] " +
				"\n*  Tipo Regimen ["+mundoConvenio.getTipoRegimen()+"] "+
				"\n*  Nombre Convenio ["+mundoConvenio.getNombre()+"] " +
				"\n*  Observaciones ["+mundoConvenio.getObservaciones()+"] "+
				"\n*  Plan Beneficios ["+mundoConvenio.getPlanBeneficios()+"] "+
				"\n*  Codigo MinSalud ["+mundoConvenio.getCodigoMinSalud()+"] " +
				"\n*  Formato Factura ["+mundoConvenio.getFormatoFactura()+"]"+ 
				"\n*  Tipo Contrato ["+mundoConvenio.getTipoContrato()+"]" +
				"\n*  Numero Dias Vencimiento ["+mundoConvenio.getNumeroDiasVencimiento()+"] "+
				"\n*  Requiere Justificacion Servicios ["+mundoConvenio.getRequiereJustificacionServicios()+"] "+
				"\n*  Requiere Justificacion Articulos ["+mundoConvenio.getRequiereJustificacionArticulos()+"] "+
				"\n*  Requiere Justificacion de Articulos No POS Diferentes a los Medicamentos ["+mundoConvenio.getRequiereJustArtNoposDifMed()+"] "+
				"\n*  Maneja Complejidad ["+mundoConvenio.getManejaComplejidad()+"] " +
				"\n*  Semanas Minimas Cotizacion ["+mundoConvenio.getSemanasMinimasCotizacion()+"] " +
				"\n*  Requiere Ingreso Numero Carnte ["+convenioForm.getRequiereNumeroCarnet()+"] " +
				"\n*  Metodo de Ajuste para Calculo de Tarifa de Servicios ["+mundoConvenio.getAjusteServicios()+"] "+
				"\n*  Metodo de Ajuste para Calculo de Tarifa de Articulos ["+mundoConvenio.getAjusteArticulos()+"] "+
				"\n*  Interfaz ["+mundoConvenio.getInterfaz()+"] "+
				"\n*  Tipo Convenio ["+mundoConvenio.getTipoConvenio()+"] " +
				"\n*  Tipo Codigo Servicio ["+mundoConvenio.getTipoCodigo()+"] " +
				"\n*  Tipo Codigo Articulo ["+mundoConvenio.getTipoCodigoArt()+"] " +
				"\n*  Centro Costo Plan Especial ["+mundoConvenio.getPlanEspecial()+"] " +
				"\n*  Excento Deudor ["+mundoConvenio.getExcentoDeudor()+"] " +
				"\n*  Excento Documento Garantia ["+mundoConvenio.getExcentoDocumentoGarantia()+"] " +
				"\n*  VIP ["+mundoConvenio.getVip()+"] " +
				"\n*  Radicar Cuentas Negativas ["+mundoConvenio.getRadicarCuentasNegativas()+"] " +
				"\n*  Asignar en la Factura Como Valor del Paciente el Valor de Abonos ["+mundoConvenio.getAsignarFactValorPacValorAbono()+"] "+ 				
				"\n*  Dias Control Post Operatorio ["+mundoConvenio.getDiasCPO()+"] "+
				"\n*  Cantidad Maxima Citas Control Post Operatorio ["+mundoConvenio.getCantCPO()+"] " +
				"\n*  Tipo Liquidacion Pool  ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionPool()) +"] ";
		
				
		
		
				if(!UtilidadTexto.isEmpty(mundoConvenio.getCantidadMaxCirugia()))
					logGeneral+="\n*  Cantidad Maxima de Cirugias Adicionales de Paga ["+mundoConvenio.getCantidadMaxCirugia()+"] ";
				else
					logGeneral+="\n*  Cantidad Maxima de Cirugias Adicionales de Paga [] ";
				
				if(!UtilidadTexto.isEmpty(mundoConvenio.getCantidadMaxAyudpag()))
					logGeneral+="\n*  Cantidad Maxima de Ayudantes que Paga ["+mundoConvenio.getCantidadMaxAyudpag()+"] ";
				else
					logGeneral+="\n*  Cantidad Maxima de Ayudantes que Paga [] ";
				
				logGeneral+=
				"\n*  Tipo de Liquidacion Salas Cirugia ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionScx())+"] " +
				"\n*  Tipo de Liquidacion Salas No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionDyt())+"] " +
				"\n*  Tipo de Liquidacion General Cirugias ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionGcx())+"] " +
				"\n*  Tipo de Liquidacion General No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoLiquidacionGdyt())+"] " +
				"\n*  Tipo de Tarifa para Liquidacion Materiales Cirugia ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoTarifaLiqMateCx())+"]" +
				"\n*  Tipo de Tarifa para Liquidacion Materiales No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoTarifaLiqMateDyt())+"]" +
				"\n*  Tipo de Fecha para Liquidacion Tiempos Cirugia: ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoFechaLiqTiemPcx())+"]" +
				"\n*  Tipo de Fecha para Liquidacion Tiempos No Cruentos ["+ValoresPorDefecto.getIntegridadDominio(mundoConvenio.getTipoFechaLiqTiempDyt())+"]";
		
		if(mundoConvenio.getActiva())
			logGeneral += "\n*  Activa [ SI ]";
		else
			logGeneral += "\n*  Activa [ NO ]";
		if(mundoConvenio.getPyp().equals("true"))		
			logGeneral+="\n*  PyP [SI]	 " ;
		else
			logGeneral+="\n*  PyP [NO]	 " ;
		
		if(mundoConvenio.getUnificarPyp().equals("true"))		
			logGeneral+="\n* Unificar PyP [SI]	 " ;
		else
			logGeneral+="\n* Unificar PyP [NO]	 " ;
		
		logGeneral+="\n*  Encabezado Factura ["+mundoConvenio.getEncabezadoFactura()+"]";
		logGeneral+="\n*  Pie Factura ["+mundoConvenio.getPieFactura()+"]";
		
		if(mundoConvenio.getValorLetrasFactura().equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaCargo))
			logGeneral += "\n*  Valor Letras Factura [TOTAL CARGOS] ";
		
		if(mundoConvenio.getValorLetrasFactura().equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaConvenio))
			logGeneral += "\n*  Valor Letras Factura [CONVENIO] ";
		
		logGeneral+=	"\n*	Usuario Modifica: [" +usuario.getLoginUsuario() +"]"+
						"\n*	Fecha Modifica: [" +UtilidadFecha.getFechaActual() +"]"+
						"\n*	Hora Modifica: [" +UtilidadFecha.getHoraActual() +"]";
		
		logGeneral+="\n========================================================\n\n\n " ;		
		
		
		//********** LOG ODONTOLOGIA
		
		
		
		String logOdontologia = convenioForm.getLogInfoOriginalConvenio()+ 
		"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
			"\n*	Codigo Convenio [" +mundoConvenio.getCodigo() +"]" +
			"\n*	Codigo Empresa [" +mundoConvenio.getEmpresa() +"]" +
			"\n*	Nombre Convenio [" +mundoConvenio.getNombre() +"]" +
			"\n*	Codigo MinSalud [" +mundoConvenio.getCodigoMinSalud() +"]" +
			"\n*	Tipo Regimen [" +mundoConvenio.getTipoRegimen() +"]" +
			"\n*	Tipo Contrato [" +mundoConvenio.getTipoContrato() +"]" +
			"\n*	Tipo Codigo [" +mundoConvenio.getTipoCodigo() +"]" +
			"\n*	Tipo Convenio [" +mundoConvenio.getTipoConvenio() +"]" +
			"\n*	Aseguradora [" +mundoConvenio.getAseguradora() +"]" +
			"\n* 	Codigo Aseguradora [" +mundoConvenio.getCodigoAseguradora() +"]" +
			"\n*	Maneja Bonos [" +mundoConvenio.getManejaBonos() +"]" +
			"\n*	Requerido Bono [" +mundoConvenio.getRequiereBono() +"]" +
			"\n*	Maneja Promocines [" +mundoConvenio.getRequiereBono() +"]" +
			"\n*	Convenio Tarjeta Cliente [" +mundoConvenio.getEsTargetaCliene() +"]" +
			"\n*	Plan de Beneficios [" +mundoConvenio.getPlanBeneficios() +"]" +
			"\n*	Codigo Interfaz [" +mundoConvenio.getInterfaz() +"]" +
			"\n*	E-Mail [" +mundoConvenio.getCorreosElectronicos() +"]" +
			"\n*	Observaciones [" +mundoConvenio.getObservaciones() +"]" +
			"\n*	Activa [" +mundoConvenio.getActiva() +"]" +
			"\n*	Tipo Liquidacion Pool [" +mundoConvenio.getTipoLiquidacionPool() +"]" +
			"\n*	Requiere Ingreso Numero de Carnet [" +mundoConvenio.getRequiereNumeroCarnet() +"]" +
			"\n*	Ingresar Paciente Realizar Validacion en Base de Dato? [" +mundoConvenio.getIngresoBdValido() +"]" +
			"\n*	Ingresar Paciente Requiere Autorizacion [" +mundoConvenio.getIngresoPacienteReqAutorizacion() +"]" ;
		     for(int i=0;i<mundoConvenio.getMediosAutorizacion().size();i++){
		     
		         if(mundoConvenio.getMediosAutorizacion().get(i).isActivo()){
		    	 logOdontologia+="\n*	Medio de Autorizacion [" +mundoConvenio.getMediosAutorizacion().get(i).getTipo().getNombre() +"]" ;
		         }
		     }
		     logOdontologia+="\n*	Requiere Ingresar Valor Autorizado [" +mundoConvenio.getReqIngresoValido() +"]" +
		   
			"\n*	Metodo de Ajuste para Calculo de Tarifa de Servicios [" +mundoConvenio.getAjusteServicios() +"]" +
			"\n*	Metodo de Ajuste para Calculo de Tarifa de Articulos [" +mundoConvenio.getAjusteArticulos() +"]" +
			"\n*	Numero dias Vencimiento [" +mundoConvenio.getNumeroDiasVencimiento() +"]" +
			"\n*	Formato Factura [" +mundoConvenio.getFormatoFactura() +"]" +
			"\n*	Encabezado Factura [" +mundoConvenio.getEncabezadoFactura() +"]" +
			"\n*	Pie Factura [" +mundoConvenio.getPieFactura() +"]"+
			"\n*	Usuario Modifica: [" +usuario.getLoginUsuario() +"]"+
			"\n*	Fecha Modifica: [" +UtilidadFecha.getFechaActual() +"]"+
			"\n*	Hora Modifica: [" +UtilidadFecha.getHoraActual() +"]"+
			"\n*	Tipo Atencion: [" +convenioForm.getTipoAtencion()+"]";
		     
			
		
		
		if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
			 LogsAxioma.enviarLog(ConstantesBD.logConvenioCodigo, logGeneral, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				}
		else 
			if(convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
				LogsAxioma.enviarLog(ConstantesBD.logConvenioCodigo, logOdontologia, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
		}
	}
	
	

	
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * resumenModificar
	 * @param convenioForm ConvenioForm
	 * @param usuario 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward a la pagina de resumen "resumenConvenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumenModificar(		ConvenioForm convenioForm, 
													 	UsuarioBasico usuario, ActionMapping mapping, 
														HttpServletRequest request, 
													 	Connection con) throws SQLException, IPSException
	{
		Convenio mundoConvenio=new Convenio();  	
		boolean validarCargar=mundoConvenio.cargarResumen(con,convenioForm.getCodigo());
		if(validarCargar)
		{
			llenarForm(convenioForm,mundoConvenio);
			Iterator listaCentrosAtencion=convenioForm.getCenAtencContableList().iterator();
			boolean encontro=false;
			while(listaCentrosAtencion.hasNext())
			{
				HashMap<String, String> convenio=(HashMap<String, String>)listaCentrosAtencion.next();
				if(Utilidades.convertirAEntero(convenio.get("codigo"))==convenioForm.getCenAtencContable())
				{
					convenioForm.setNombreCentroAtencionContable(convenio.get("descripcion"));
					encontro=true;
					break;
				}
			}
			
			if(!encontro)
			{
				convenioForm.setNombreCentroAtencionContable("");
			}
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenConvenio");
		}
		else
		{
			logger.warn("Codigo convenio invalido "+convenioForm.getCodigo());
			this.cerrarConexion(con);
			convenioForm.reset(usuario.getCodigoInstitucionInt());
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Codigo convenio");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * listar convenios
	 * @param convenioForm ConvenioForm
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward  a la pagina "listadoConvenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarConvenios(	ConvenioForm convenioForm, 
													ActionMapping mapping,
													Connection con,String estado,
													UsuarioBasico usuario) throws SQLException 
	{
		
      	
		
		if(convenioForm.isOdontologiaActivo())
		{

				
			
			return mapping.findForward("principalMenu");
			
			
				
		}
		else 
		{

			Convenio mundoConvenio= new Convenio();
			
			mundoConvenio.setAmbos(false);
			mundoConvenio.setTipoAtencion(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral);
			convenioForm.setTipoAtencion(mundoConvenio.getTipoAtencion());
		    
			
			
			convenioForm.setEstado(estado);
			
			convenioForm.setCol(mundoConvenio.listadoConvenio(con, Integer.parseInt(usuario.getCodigoInstitucion())));
			
			this.cerrarConexion(con);
			return mapping.findForward("paginaListar");
			
		}
			
	}	
	
	private ActionForward accionListarConveniosOdontologia(	ConvenioForm convenioForm, 
			ActionMapping mapping,
			Connection con,String estado,
			UsuarioBasico usuario) throws SQLException 
			
{
	Convenio mundoConvenio= new Convenio();
	
	
	logger.info("En la forma ---> "+convenioForm.isAmbos()+ "    "+convenioForm.getTipoAtencion());
	mundoConvenio.setAmbos(convenioForm.isAmbos());
	mundoConvenio.setTipoAtencion(convenioForm.getTipoAtencion());
	
	
	convenioForm.setEstado(estado);
	
	convenioForm.setCol(mundoConvenio.listadoConvenio(con, Integer.parseInt(usuario.getCodigoInstitucion())));
	
	this.cerrarConexion(con);
	
	convenioForm.setOdontologiaActivo(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt())));
	
	
	
	
	
	return mapping.findForward("paginaListar");
}
	
	
	
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param convenioForm ConvenioForm
	 * 				para pre-llenar datos si es necesario
	 * @param usuario 
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward a la pagina funcionalidadBuscarConvenio "busquedaConvenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	ConvenioForm convenioForm, 
													UsuarioBasico usuario, ActionMapping mapping, 
													Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		convenioForm.reset(usuario.getCodigoInstitucionInt());
		convenioForm.setEstado("busquedaAvanzada");
		convenioForm.setPlanEspecial("");
		convenioForm.setRadicarCuentasNegativas("");
		convenioForm.setAsignarFactValorPacValorAbono("");
		this.cerrarConexion(con);
		
		logger.info("CARGANDO ACCION BUSQUEDA "+ convenioForm.isAmbos()+ " "+ convenioForm.getTipoAtencion() );
		
		if(convenioForm.isAmbos() || convenioForm.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
			return mapping.findForward("paginaBusquedaOdontologia");
		}
		return mapping.findForward("paginaBusqueda");		
	}
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param convenioForm ConvenioForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegacion
	 * @param con Conexion con la fuente de datos
	 * @return ActionForward a la pagina funcionalidadBuscarConvenio "busquedaConvenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(	ConvenioForm convenioForm, 
															ActionMapping mapping, 
															Connection con,
															boolean accionModificar,
															UsuarioBasico usuario) throws SQLException
	{
		Convenio mundoConvenio= new Convenio();
		mundoConvenio.reset();
		mundoConvenio.setRadicarCuentasNegativas("");
		mundoConvenio.setAsignarFactValorPacValorAbono("");
		enviarItemsSeleccionadosBusqueda(convenioForm, mundoConvenio);
		convenioForm.resetCriteriosBusqueda();
		convenioForm.setCol(mundoConvenio.resultadoBusquedaAvanzada(con, Integer.parseInt(usuario.getCodigoInstitucion())));
		
		if(accionModificar)
		    convenioForm.setEstado("listarModificar");
		
		this.cerrarConexion(con);
		//llenarMundo(empresaForm,mundoEmpresa);
		return mapping.findForward("paginaListar");
	}
	
	/**
	 * 
	 * @param convenioForm
	 * @param mundoConvenio
	 */
	private void enviarItemsSeleccionadosBusqueda(ConvenioForm convenioForm, Convenio mundoConvenio)
	{
		String bb[]= convenioForm.getCriteriosBusqueda();
		for(int i=0; i<bb.length; i++)
		{
			
			if(bb[i].equals("codigoStr"))
		    {
		        try
		        {
		            mundoConvenio.setCodigo(Integer.parseInt(convenioForm.getCodigoStr()));
		        }
		        catch(NumberFormatException e)
		        {
		            logger.warn("Error en enviarItemsSeleccionados "+e);
		            int codigoBusquedaSinExcepcionBD= -8;
		            mundoConvenio.setCodigo(codigoBusquedaSinExcepcionBD);
			    }
		    }    
		    try
			{		    
		    	
				if(bb[i].equals("razonSocial"))
					mundoConvenio.setRazonSocial(convenioForm.getRazonSocial());
				if(bb[i].equals("nombreTipoRegimen"))
					mundoConvenio.setNombreTipoRegimen(convenioForm.getNombreTipoRegimen());
				if(bb[i].equals("nombre"))
					mundoConvenio.setNombre(convenioForm.getNombre());
				if(bb[i].equals("observaciones"))
					mundoConvenio.setObservaciones(convenioForm.getObservaciones());
				if(bb[i].equals("planBeneficios"))
					mundoConvenio.setPlanBeneficios(convenioForm.getPlanBeneficios());
				if(bb[i].equals("codigoMinSalud"))
					mundoConvenio.setCodigoMinSalud(convenioForm.getCodigoMinSalud());
				if(bb[i].equals("nombreFormatoFactura"))
					mundoConvenio.setNombreFormatoFactura(convenioForm.getNombreFormatoFactura());
				if(bb[i].equals("activaAux"))
					mundoConvenio.setActivaAux(convenioForm.getActivaAux());		
				if(bb[i].equals("nombreTipoContrato"))
					mundoConvenio.setNombreTipoContrato(convenioForm.getNombreTipoContrato());
				if(bb[i].equals("pypAux"))
					mundoConvenio.setPypAux(convenioForm.getPypAux());
				if(bb[i].equals("unificarPyp"))
					mundoConvenio.setUnificarPyp(convenioForm.getUnificarPyp());
				if(bb[i].equals("interfaz"))
					mundoConvenio.setInterfaz(convenioForm.getInterfaz());
				if(bb[i].equals("empresaInstitucion"))
				{
					mundoConvenio.setEmpresaInstitucion(convenioForm.getEmpresaInstitucion());
				}
				
				if(bb[i].equals("cantidadMaxCirugia") && !UtilidadTexto.isEmpty(convenioForm.getCantidadMaxCirugia()))
					mundoConvenio.setCantidadMaxCirugia(convenioForm.getCantidadMaxCirugia());
				
				if(bb[i].equals("cantidadMaxAyudpag") && !UtilidadTexto.isEmpty(convenioForm.getCantidadMaxAyudpag()))
					mundoConvenio.setCantidadMaxAyudpag(convenioForm.getCantidadMaxAyudpag());
				
				if(bb[i].equals("tipoLiquidacionScx"))
					mundoConvenio.setTipoLiquidacionScx(convenioForm.getTipoLiquidacionScx());
				
				if(bb[i].equals("tipoLiquidacionDyt"))
					mundoConvenio.setTipoLiquidacionDyt(convenioForm.getTipoLiquidacionDyt());
				
				if(bb[i].equals("tipoLiquidacionGcx"))
					mundoConvenio.setTipoLiquidacionGcx(convenioForm.getTipoLiquidacionGcx());
				
				if(bb[i].equals("tipoLiquidacionGdyt"))
					mundoConvenio.setTipoLiquidacionGdyt(convenioForm.getTipoLiquidacionGdyt());
				
				if(bb[i].equals("tipoTarifaLiqMateCx"))
					mundoConvenio.setTipoTarifaLiqMateCx(convenioForm.getTipoTarifaLiqMateCx());
				
				if(bb[i].equals("tipoTarifaLiqMateDyt"))
					mundoConvenio.setTipoTarifaLiqMateDyt(convenioForm.getTipoTarifaLiqMateDyt());
				
				if(bb[i].equals("tipoFechaLiqTiemPcx"))
					mundoConvenio.setTipoFechaLiqTiemPcx(convenioForm.getTipoFechaLiqTiemPcx());
				
				if(bb[i].equals("tipoFechaLiqTiempDyt"))
					mundoConvenio.setTipoFechaLiqTiempDyt(convenioForm.getTipoFechaLiqTiempDyt());
				
				if(bb[i].equals("liquidacionTmpFracAdd"))
					mundoConvenio.setLiquidacionTmpFracAdd(convenioForm.getLiquidacionTmpFracAdd());				
				
				if(bb[i].equals("planEspecial"))
					mundoConvenio.setPlanEspecial(Utilidades.convertirAEntero(convenioForm.getPlanEspecial()));
				
				if(bb[i].equals("radicarCuentasNegativas"))
					mundoConvenio.setRadicarCuentasNegativas(convenioForm.getRadicarCuentasNegativas());
				
				if(bb[i].equals("asignarFactValorPacValorAbono"))
					mundoConvenio.setAsignarFactValorPacValorAbono(convenioForm.getAsignarFactValorPacValorAbono());
				
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}
	}
	

	/**
	 * Metodo que carga los datos pertinentes desde el 
	 * form ConvenioForm para el mundo de Convenio
	 * @param convenioForm ConvenioForm (forma)
	 * @param mundoConvenio Convenio (mundo)
	 * @param usuario 
	 */
	protected void llenarMundo(ConvenioForm convenioForm, Convenio mundoConvenio, UsuarioBasico usuario)
	{
		
		mundoConvenio.setCodigo(convenioForm.getCodigo());
		mundoConvenio.setEmpresa(convenioForm.getEmpresa());
		mundoConvenio.setEmpresaNuevo(convenioForm.getEmpresa());

		mundoConvenio.setTipoRegimen(convenioForm.getTipoRegimen());
		mundoConvenio.setTipoRegimenNuevo(convenioForm.getTipoRegimen());

		mundoConvenio.setNombre(convenioForm.getNombre());
		mundoConvenio.setObservaciones(convenioForm.getObservaciones());

		mundoConvenio.setCheckInfoAdicCuenta(convenioForm.getCheckInfoAdicCuenta());

		mundoConvenio.setPlanBeneficios(convenioForm.getPlanBeneficios());
		mundoConvenio.setCodigoMinSalud(convenioForm.getCodigoMinSalud());

		mundoConvenio.setFormatoFactura(convenioForm.getFormatoFactura());
		mundoConvenio.setFormatoFacturaNuevo(convenioForm.getFormatoFactura());

		mundoConvenio.setActiva(convenioForm.getActiva());
		
		if(convenioForm.getTipoAtencion().compareTo(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)==0){
			convenioForm.setConvenioManejaMontoCobro1("false");
		}
		
		if(convenioForm.getConvenioManejaMontoCobro1().compareTo(ConstantesBD.acronimoNo)==0 || convenioForm.getConvenioManejaMontoCobro1().compareTo("Seleccione")==0){
			mundoConvenio.setConvenioManejaMontoCobro(false);
		}else{
			if(convenioForm.getConvenioManejaMontoCobro1().compareTo(ConstantesBD.acronimoSi)==0){
			mundoConvenio.setConvenioManejaMontoCobro(true);
			}
			else{
				mundoConvenio.setConvenioManejaMontoCobro(convenioForm.isConvenioManejaMontoCobro());
			}
		}
		//mundoConvenio.setConvenioManejaMontoCobro(convenioForm.isConvenioManejaMontoCobro());
		
		mundoConvenio.setTipoContrato(convenioForm.getTipoContrato());
		mundoConvenio.setPyp(convenioForm.getPyp());

		mundoConvenio.setUnificarPyp(convenioForm.getUnificarPyp());

		mundoConvenio.setNumeroDiasVencimiento(convenioForm.getNumeroDiasVencimiento());
		mundoConvenio.setRequiereJustificacionServicios(convenioForm.getRequiereJustificacionServicios());
		mundoConvenio.setRequiereJustificacionArticulos(convenioForm.getRequiereJustificacionArticulos());
		mundoConvenio.setRequiereJustArtNoposDifMed(convenioForm.getRequiereJustArtNoposDifMed());
		mundoConvenio.setManejaComplejidad(convenioForm.getManejaComplejidad());
		mundoConvenio.setSemanasMinimasCotizacion(convenioForm.getSemanasMinimasCotizacion());

		mundoConvenio.setAjusteServicios(convenioForm.getAjusteServicios());
		mundoConvenio.setAjusteArticulos(convenioForm.getAjusteArticulos());
		mundoConvenio.setInterfaz(convenioForm.getInterfaz());

		mundoConvenio.setTipoConvenio(convenioForm.getTipoConvenio());			
		mundoConvenio.setTipoCodigo(convenioForm.getTipoCodigo());	
		mundoConvenio.setTipoCodigoArt(convenioForm.getTipoCodigoArt());	
		mundoConvenio.setInstitucion(usuario.getCodigoInstitucionInt());		

		mundoConvenio.setCantidadMaxCirugia(convenioForm.getCantidadMaxCirugia());			
		mundoConvenio.setCantidadMaxAyudpag(convenioForm.getCantidadMaxAyudpag());			
		mundoConvenio.setTipoLiquidacionScx(convenioForm.getTipoLiquidacionScx());			
		mundoConvenio.setTipoLiquidacionDyt(convenioForm.getTipoLiquidacionDyt());			
		mundoConvenio.setTipoLiquidacionGcx(convenioForm.getTipoLiquidacionGcx());			
		mundoConvenio.setTipoLiquidacionGdyt(convenioForm.getTipoLiquidacionGdyt());			
		mundoConvenio.setTipoTarifaLiqMateCx(convenioForm.getTipoTarifaLiqMateCx());			
		mundoConvenio.setTipoTarifaLiqMateDyt(convenioForm.getTipoTarifaLiqMateDyt());			
		mundoConvenio.setTipoFechaLiqTiemPcx(convenioForm.getTipoFechaLiqTiemPcx());			
		mundoConvenio.setTipoFechaLiqTiempDyt(convenioForm.getTipoFechaLiqTiempDyt());			
		mundoConvenio.setLiquidacionTmpFracAdd(convenioForm.getLiquidacionTmpFracAdd());		
		
		mundoConvenio.setEncabezadoFactura(convenioForm.getEncabezadoFactura());
		mundoConvenio.setPieFactura(convenioForm.getPieFactura());
		mundoConvenio.setEmpresaInstitucion(convenioForm.getEmpresaInstitucion());
		logger.info("\n\n\n valor en la forma >> "+convenioForm.getPlanEspecial());
		mundoConvenio.setPlanEspecial(Integer.parseInt(convenioForm.getPlanEspecial()));
		mundoConvenio.setExcentoDeudor(convenioForm.getExcentoDeudor());
		mundoConvenio.setExcentoDocumentoGarantia(convenioForm.getExcentoDocumentoGarantia());
		mundoConvenio.setVip(convenioForm.getVip());
		mundoConvenio.setRadicarCuentasNegativas(convenioForm.getRadicarCuentasNegativas());
		mundoConvenio.setAsignarFactValorPacValorAbono(convenioForm.getAsignarFactValorPacValorAbono());
		mundoConvenio.setCantCPO(convenioForm.getCantCPO());
		mundoConvenio.setDiasCPO(convenioForm.getDiasCPO());
		mundoConvenio.setAseguradora(convenioForm.getAseguradora());
		mundoConvenio.setCodigoAseguradora(convenioForm.getCodigoAseguradora());
		
		mundoConvenio.setRequiereNumeroCarnet(convenioForm.getRequiereNumeroCarnet());
		mundoConvenio.setValorLetrasFactura(convenioForm.getValorLetrasFactura());
		
     //****************Cambios Anexo 753 Decreto 4747*************************************************
        mundoConvenio.setReporte_incons_bd(convenioForm.getReporte_incons_bd());
        mundoConvenio.setReporte_atencion_ini_urg(convenioForm.getReporte_atencion_ini_urg());
        mundoConvenio.setGeneracion_autom_val_urg(convenioForm.getGeneracion_autom_val_urg());
		mundoConvenio.setMediosEnvio(convenioForm.getMediosEnvio());
		mundoConvenio.setCorreosElectronicos(convenioForm.getCorreosElectronicos());
		mundoConvenio.setRequiere_autorizacion_servicio(convenioForm.getRequiere_autoriz_servicios());
		mundoConvenio.setFormato_autorizacion(convenioForm.getFormato_autorizacion());
		
	//****************************************************************************************************
		
	//****************Cambios Anexo 791*******************************************************************
        mundoConvenio.setManejaMultasPorIncumplimiento(convenioForm.getManejaMultasPorIncumplimiento());
        mundoConvenio.setValorMultaPorIncumplimientoCitas(convenioForm.getValorMultaPorIncumplimientoCitas());
		
	//****************************************************************************************************

//        ******** Cambios Anexo 809 *********
        mundoConvenio.setCcContable(Utilidades.convertirAEntero(convenioForm.getCcContable()));
        mundoConvenio.setCcContableList(convenioForm.getCcContableList());
        
        mundoConvenio.setCenAtencContable(convenioForm.getCenAtencContable());
//        ************************************
        
        mundoConvenio.setManejaBonos(convenioForm.getManejaBonos());
        mundoConvenio.setRequiereBono(convenioForm.getRequiereBono());
        mundoConvenio.setManejaPromociones(convenioForm.getManejaPromociones());
        mundoConvenio.setEsTargetaCliene(convenioForm.getEsTargetaCliene());
        mundoConvenio.setIngresoBdValido(convenioForm.getIngresoBdValido());
        mundoConvenio.setIngresoPacienteReqAutorizacion(convenioForm.getIngresoPacienteReqAutorizacion());
        mundoConvenio.setReqIngresoValido(convenioForm.getReqIngresoValido());
        mundoConvenio.setTipoAtencion(convenioForm.getTipoAtencion());
        mundoConvenio.setMediosAutorizacion(convenioForm.getMediosAutorizacion());
        mundoConvenio.setTipoLiquidacionPool(convenioForm.getTipoLiquidacionPool()); // anexo 961
        mundoConvenio.setManejaPresupCapitacion(convenioForm.getManejaPresupuestoCapitacion());
        mundoConvenio.setCapitacionSubcontratada(convenioForm.getConvenioManejaAutorizacionCapitada());
     }

	/**
	 * Este metodo carga los datos pertinentes a la forma 
	 * @param convenioForm (form)
	 * @param mundoConvenio (mundo)
	 */
	protected void llenarForm(ConvenioForm convenioForm, Convenio mundoConvenio)
	{		
		convenioForm.setCodigo(mundoConvenio.getCodigo());
		convenioForm.setEmpresa(mundoConvenio.getEmpresa());
		convenioForm.setEmpresaNuevo(mundoConvenio.getEmpresa());
		
		convenioForm.setTipoRegimen(mundoConvenio.getTipoRegimen());
		convenioForm.setTipoRegimenNuevo(mundoConvenio.getTipoRegimen());
		
		convenioForm.setNombre(mundoConvenio.getNombre());
		convenioForm.setObservaciones(mundoConvenio.getObservaciones());
		
		convenioForm.setCheckInfoAdicCuenta(mundoConvenio.getCheckInfoAdicCuenta());
		
		convenioForm.setPlanBeneficios(mundoConvenio.getPlanBeneficios());
		convenioForm.setCodigoMinSalud(mundoConvenio.getCodigoMinSalud());
		
		convenioForm.setFormatoFactura(mundoConvenio.getFormatoFactura());
		convenioForm.setFormatoFacturaNuevo(mundoConvenio.getFormatoFactura());
		
		convenioForm.setActiva(mundoConvenio.getActiva());
		convenioForm.setConvenioManejaMontoCobro(mundoConvenio.isConvenioManejaMontoCobro());
		
		convenioForm.setTipoContrato(mundoConvenio.getTipoContrato());
		convenioForm.setPyp(mundoConvenio.getPyp());
		convenioForm.setUnificarPyp(mundoConvenio.getUnificarPyp());
		
		convenioForm.setNumeroDiasVencimiento(mundoConvenio.getNumeroDiasVencimiento());
		convenioForm.setRequiereJustificacionServicios(mundoConvenio.getRequiereJustificacionServicios());
		convenioForm.setRequiereJustificacionArticulos(mundoConvenio.getRequiereJustificacionArticulos());
		convenioForm.setRequiereJustArtNoposDifMed(mundoConvenio.getRequiereJustArtNoposDifMed());
		convenioForm.setManejaComplejidad(mundoConvenio.getManejaComplejidad());
		convenioForm.setSemanasMinimasCotizacion(mundoConvenio.getSemanasMinimasCotizacion());
		convenioForm.setAjusteServicios(mundoConvenio.getAjusteServicios());
		convenioForm.setAjusteArticulos(mundoConvenio.getAjusteArticulos());
		convenioForm.setInterfaz(mundoConvenio.getInterfaz());
		convenioForm.setTipoConvenio(mundoConvenio.getTipoConvenio());
		convenioForm.setTipoCodigo(mundoConvenio.getTipoCodigo());
		convenioForm.setTipoCodigoArt(mundoConvenio.getTipoCodigoArt());
		
		convenioForm.setCantidadMaxCirugia(mundoConvenio.getCantidadMaxCirugia());
		convenioForm.setCantidadMaxAyudpag(mundoConvenio.getCantidadMaxAyudpag());
		
		convenioForm.setTipoLiquidacionScx(mundoConvenio.getTipoLiquidacionScx());
		convenioForm.setTipoLiquidacionDyt(mundoConvenio.getTipoLiquidacionDyt());
		convenioForm.setTipoLiquidacionGcx(mundoConvenio.getTipoLiquidacionGcx());
		convenioForm.setTipoLiquidacionGdyt(mundoConvenio.getTipoLiquidacionGdyt());
		
		convenioForm.setTipoTarifaLiqMateCx(mundoConvenio.getTipoTarifaLiqMateCx());
		convenioForm.setTipoTarifaLiqMateDyt(mundoConvenio.getTipoTarifaLiqMateDyt());
		
		convenioForm.setTipoFechaLiqTiemPcx(mundoConvenio.getTipoFechaLiqTiemPcx());
		convenioForm.setTipoFechaLiqTiempDyt(mundoConvenio.getTipoFechaLiqTiempDyt());
		convenioForm.setLiquidacionTmpFracAdd(mundoConvenio.getLiquidacionTmpFracAdd());
		
		convenioForm.setEncabezadoFactura(mundoConvenio.getEncabezadoFactura());
		convenioForm.setPieFactura(mundoConvenio.getPieFactura());
		convenioForm.setEmpresaInstitucion(mundoConvenio.getEmpresaInstitucion());
		
		convenioForm.setPlanEspecial(mundoConvenio.getPlanEspecial()+"");
		convenioForm.setPlanEspecialList(mundoConvenio.getPlanEspecialList());
		convenioForm.setExcentoDeudor(mundoConvenio.getExcentoDeudor());
		convenioForm.setExcentoDocumentoGarantia(mundoConvenio.getExcentoDocumentoGarantia());
		convenioForm.setVip(mundoConvenio.getVip());
		convenioForm.setRadicarCuentasNegativas(mundoConvenio.getRadicarCuentasNegativas());
		convenioForm.setAsignarFactValorPacValorAbono(mundoConvenio.getAsignarFactValorPacValorAbono());
		convenioForm.setCantCPO(mundoConvenio.getCantCPO());
		convenioForm.setDiasCPO(mundoConvenio.getDiasCPO());
		convenioForm.setAseguradora(mundoConvenio.getAseguradora());
		convenioForm.setCodigoAseguradora(mundoConvenio.getCodigoAseguradora());
		
		convenioForm.setRequiereNumeroCarnet(mundoConvenio.getRequiereNumeroCarnet());
		convenioForm.setValorLetrasFactura(mundoConvenio.getValorLetrasFactura());
		
	//*************	Cambios Anexo 753 Decreto 4747*************************************************
		
		convenioForm.setReporte_incons_bd(mundoConvenio.getReporte_incons_bd());
		convenioForm.setReporte_atencion_ini_urg(mundoConvenio.getReporte_atencion_ini_urg());
		convenioForm.setGeneracion_autom_val_urg(mundoConvenio.getGeneracion_autom_val_urg());
		convenioForm.setMediosEnvio(mundoConvenio.getMediosEnvio());
		convenioForm.setCorreosElectronicos(mundoConvenio.getCorreosElectronicos());
		convenioForm.setRequiere_autoriz_servicios(mundoConvenio.getRequiere_autorizacion_servicio());
		convenioForm.setFormato_autorizacion(mundoConvenio.getFormato_autorizacion());
		
	//********************************************************************************************
		
//		******** anexo 791 ********
		convenioForm.setManejaMultasPorIncumplimiento(mundoConvenio.getManejaMultasPorIncumplimiento());
		convenioForm.setValorMultaPorIncumplimientoCitas(mundoConvenio.getValorMultaPorIncumplimientoCitas());
//	    ***************************
		
//		************* cambios anexo 809 *************
		convenioForm.setCcContable(mundoConvenio.getCcContable()+"");
		convenioForm.setCcContableList(mundoConvenio.getCcContableList());
//		*********************************************
		
		
		convenioForm.setManejaBonos(mundoConvenio.getManejaBonos());
		convenioForm.setRequiereBono(mundoConvenio.getRequiereBono());
		convenioForm.setManejaPromociones(mundoConvenio.getManejaPromociones());
		convenioForm.setEsTargetaCliene(mundoConvenio.getEsTargetaCliene());
		convenioForm.setIngresoBdValido(mundoConvenio.getIngresoBdValido());
		convenioForm.setIngresoPacienteReqAutorizacion(mundoConvenio.getIngresoPacienteReqAutorizacion());
		convenioForm.setReqIngresoValido(mundoConvenio.getReqIngresoValido());
		convenioForm.setTipoAtencion(mundoConvenio.getTipoAtencion());
		convenioForm.setMediosAutorizacion(mundoConvenio.getMediosAutorizacion());
		convenioForm.setTipoLiquidacionPool(mundoConvenio.getTipoLiquidacionPool()); // anexo 961
		
		convenioForm.setConvenioManejaMontoCobro1((mundoConvenio.isConvenioManejaMontoCobro())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		convenioForm.setManejaPresupuestoCapitacion(mundoConvenio.getManejaPresupCapitacion());
		convenioForm.setConvenioManejaAutorizacionCapitada(mundoConvenio.getCapitacionSubcontratada());
	}
	
	/**
	 * 
	 * Este M�todo se encarga de verificar si el tipo de contrato
	 * seleccionado es tipo capitado, si es asi se mostrar�n los
	 * campos de maneja presupuesto capitado y capitacion subcontratada
	 * 
	 * @param ActionMapping mapping,ConvenioForm convenioForm
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward mostrarOpcionesCapitacion(ActionMapping mapping,ConvenioForm forma,
			UsuarioBasico usuario,Connection con)throws SQLException{
		try{
			HibernateUtil.beginTransaction();
			int codigoTipoContrato =forma.getTipoContrato();
			ITiposContratoServicio servicio = FacturacionServicioFabrica.crearTiposContratoServicio();
			forma.setTipoContratoCapitado(false);		
			
			ArrayList<TiposContrato> listaTiposContrato = servicio.consultarTiposContrato();
			if(listaTiposContrato!=null && listaTiposContrato.size()>0){
				for(TiposContrato registro :listaTiposContrato){
					if(codigoTipoContrato==registro.getCodigo()){
						if(registro.getNombre().equals(TIPO_CONTRATO_CAPITADO)){
							forma.setTipoContratoCapitado(true);
							ArrayList<DtoCheckBox> listaCapitacionSubcontratada = new ArrayList<DtoCheckBox>();
							DtoCheckBox dto = new DtoCheckBox();						
							
							String valorSi = (String)ValoresPorDefecto.getIntegridadDominio(ConstantesBD.acronimoSi);
							String valorNo = (String)ValoresPorDefecto.getIntegridadDominio(ConstantesBD.acronimoNo);
							dto.setNombre(valorSi);
							dto.setCodigo(ConstantesBD.acronimoSi);
							listaCapitacionSubcontratada.add(dto);
							
							dto = new DtoCheckBox();
							dto.setNombre(valorNo);
							dto.setCodigo(ConstantesBD.acronimoNo);
							listaCapitacionSubcontratada.add(dto);		
							
							forma.setListaCapitacionSubcontratada(listaCapitacionSubcontratada);
						}
						break;
					}
				}
			}
			forma.setEstado("empezarGeneral");
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR mostrarOpcionesCapitacion",e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("principal");
	}


	/**
	 * Metodo en que se cierra la conexion (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexion con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
			if (con!=null&&!con.isClosed())
			{
                UtilidadBD.closeConnection(con);

			}
	}	
	
}
