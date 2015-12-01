/*
 * @(#)ContratoAction.java
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
import java.util.HashMap;

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
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.cargos.ContratoForm;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoLogControlAnticipoContrato;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.DistribucionCuenta;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   contratos, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Mayo 3, 2004
 */
public class ContratoAction  extends Action
{
	/** 
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ContratoAction.class);
	
	/**
	 * 
	 */
	private static final String[] indicesEsquemasInventario={"codigo_","contrato_","claseinventario_","nombreclaseinventario_","esquematarifario_","nombreesquematarifario_","fechavigencia_","fechavigenciaoriginal_","codcentroatencion_", "nombrecentroatencion_"};
		
	/**
	 * 
	 */
	private static final String[] indicesEsquemasProcedimientos={"codigo_","contrato_","gruposervicio_","nombregruposervicio_","esquematarifario_","nombreesquematarifario_","fechavigencia_","fechavigenciaoriginal_","codcentroatencion_", "nombrecentroatencion_"};

	/**
	 * metodo encargado de el flujo y control de la funcionalidad
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
			if(form instanceof ContratoForm)
			{
				con= UtilidadBD.abrirConexion();	

				ContratoForm contratoForm =(ContratoForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=contratoForm.getEstado(); 
				logger.info("\n\n--->ESTADO CONTRATOS-->>"+estado);

				if(!contratoForm.getCodigoTipoContrato().equals(ConstantesBD.codigoTipoContratoCapitado+""))
				{
					if(estado.trim().equals("continuar"))
					{	
						contratoForm.resetCapitacion();
					}	
				}

				if(estado == null)
				{
					contratoForm.reset();	
					logger.warn("Estado no valido dentro del flujo de registro contrato (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//validacion de las acciones
				if(contratoForm.getAccion().equals("nuevoEsquemaInventarios"))
				{
					Utilidades.nuevoRegistroMapaGenerico(contratoForm.getEsquemasInventario(), indicesEsquemasInventario, "numRegistros","tiporegistro_","MEM");
					contratoForm.setAccion("");
					this.cerrarConexion(con);
					return mapping.findForward("principal");	
				}
				else if(contratoForm.getAccion().equals("nuevoEsquemaProcedimientos"))
				{
					Utilidades.nuevoRegistroMapaGenerico(contratoForm.getEsquemasProcedimientos(), indicesEsquemasProcedimientos, "numRegistros","tiporegistro_","MEM");
					contratoForm.setAccion("");
					this.cerrarConexion(con);
					return mapping.findForward("principal");	
				}
				else if(contratoForm.getAccion().equals("eliminarEsquemaInventarios"))
				{
					Utilidades.eliminarRegistroMapaGenerico(contratoForm.getEsquemasInventario(),contratoForm.getEsquemasInventarioEliminados(),contratoForm.getPosEliminar(),indicesEsquemasInventario,"numRegistros","tiporegistro_","BD",false);
					contratoForm.setAccion("");
					this.cerrarConexion(con);
					return mapping.findForward("principal");	
				}
				else if(contratoForm.getAccion().equals("eliminarEsquemaProcedimientos"))
				{
					Utilidades.eliminarRegistroMapaGenerico(contratoForm.getEsquemasProcedimientos(),contratoForm.getEsquemasProcedimientosEliminados(),contratoForm.getPosEliminar(),indicesEsquemasProcedimientos,"numRegistros","tiporegistro_","BD",false);
					contratoForm.setAccion("");
					this.cerrarConexion(con);
					return mapping.findForward("principal");	
				}
				if (estado.equals("empezar"))
				{
					return this.accionEmpezar(contratoForm,mapping, con);
				}
				else if(estado.equals("refrescarEsquemas"))
				{
					contratoForm.setEstado("empezar");
					this.cerrarConexion(con);
					return mapping.findForward("principal"); 
				}
				else if(estado.equals("refrescarEsquemasModificacion"))
				{
					contratoForm.setEstado("modificar");

					//eliminamos los que existian
					int cantidadRegistros= Utilidades.convertirAEntero(contratoForm.getEsquemasInventario("numRegistros")+"");
					for(int w=0; w<cantidadRegistros;w++)
					{
						Utilidades.eliminarRegistroMapaGenerico(contratoForm.getEsquemasInventario(),contratoForm.getEsquemasInventarioEliminados(),0,indicesEsquemasInventario,"numRegistros","tiporegistro_","BD",false);
					}

					cantidadRegistros= Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("numRegistros")+"");
					for(int w=0; w<cantidadRegistros;w++)
					{
						Utilidades.eliminarRegistroMapaGenerico(contratoForm.getEsquemasProcedimientos(),contratoForm.getEsquemasInventarioEliminados(),0,indicesEsquemasInventario,"numRegistros","tiporegistro_","BD",false);
					}

					this.cerrarConexion(con);
					return mapping.findForward("principal"); 
				}
				else if(estado.equals("salir"))
				{
					return this.accionSalir(contratoForm,request,mapping,con,usuario);
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumen(contratoForm,mapping,request, con);
				}
				else if(estado.equals("modificar"))
				{
					return this.accionModificar(contratoForm,request,mapping,con);
				}
				else if(estado.equals("logContrato"))
				{
					Contrato mundoContrato=new Contrato ();
					DtoLogControlAnticipoContrato dtoWhere = new DtoLogControlAnticipoContrato();
					dtoWhere.setControlAnticipoContrato(contratoForm.getDtoControlAnticipo().getCodigo());
					contratoForm.setListaLogControl(mundoContrato.obtenerListalog(dtoWhere));
					return mapping.findForward("paginaLogContrato");
				}
				else if(estado.equals("guardarModificacion"))
				{
					logger.info("\n\n--->empezar la modificacion");
					return this.accionGuardarModificacion(contratoForm,request,mapping,con);
				}
				else if(estado.equals("listar")||estado.equals("listarModificar"))
				{
					return this.accionListarContratos(contratoForm,mapping,con,estado, usuario);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(contratoForm,mapping,con);
				}
				else if(estado.equals("resultadoBusquedaAvanzada"))
				{
					return this.accionResultadoBusquedaAvanzada(contratoForm,mapping,con,false, usuario);
				}
				else if(estado.equals("resultadoBusquedaAvanzadaModificar"))
				{
					return this.accionResultadoBusquedaAvanzada(contratoForm,mapping,con,true, usuario);
				}
				else if(estado.equals("imprimir"))
				{
					this.cerrarConexion(con);
					return mapping.findForward("imprimir");
				}
				else if (estado.equals("continuar"))
				{
					return accionContinuar(mapping, con, contratoForm);
				}
				else if(estado.equals("adicionarNivelAtencionMapa"))
				{
					contratoForm.setEstado(contratoForm.getEstadoTemp());
					return this.accionAdicionarNivelAtencionMapa(contratoForm, mapping, con);
				}
				else if(estado.equals("eliminarNivelAtencion"))
				{
					contratoForm.setEstado(contratoForm.getEstadoTemp());
					return this.accionEliminarNivelAtencionMapa(contratoForm,request, mapping, con);
				}
				else
				{
					contratoForm.reset();
					logger.warn("Estado no valido dentro del flujo de registro contrato (null) ");
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

	/**
	 * @param mapping
	 * @param con
	 * @param contratoForm
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionContinuar(ActionMapping mapping,
			Connection con, ContratoForm contratoForm) throws SQLException {
		contratoForm.setEstado(contratoForm.getEstadoTemp());

		//Agrego esta validacion por tarea 3370, ya que previamente solo aplicaba para contratos capitados
		logger.info("****************************************************************************\n\n\n\n\n\n");
		logger.info(" CODIGO CONVENIO---------->"+contratoForm.getCodigoConvenio());
		logger.info("\n\n\n\n\n\n****************************************************************************");
		if(contratoForm.getCodigoConvenio()>0)
		{	
			DtoConvenio dto = new DtoConvenio();

			dto.setCodigo(contratoForm.getCodigoConvenio());
			ArrayList<DtoConvenio> listaTmp=  Convenio.cargarConveniosArrayList(dto);
			if(listaTmp.size()>0)
			{
				logger.info("*****************************************************************************");
				if (listaTmp.get(0).getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
				{
					logger.info("\n\n\n\n EXISTEN CONVENIO ******************************************** \n\n\n\n");
					contratoForm.setEsConvenioOdontologicio(Boolean.TRUE);
				}
				else
					contratoForm.setEsConvenioOdontologicio(Boolean.FALSE);

				logger.info("es convenio maneja montos------>"+listaTmp.get(0).getConvenioManejaMontos());
				contratoForm.setConvenioManejaMontos(listaTmp.get(0).getConvenioManejaMontos());
				logger.info("es convenio odon------>"+contratoForm.isEsConvenioOdontologicio());
			}
		}
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}	
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param contratoForm ContratoForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward a la pagina principal "contrato.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	ContratoForm contratoForm, 
										 	ActionMapping mapping, 
										 	Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		contratoForm.reset();
		contratoForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");		
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto contrato
	 * en el objeto mundo
	 * 
	 * @param contratoForm ContratoForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @param usuario 
	 * 
	 * @return ActionForward "contrato.do?estado=resumen"
	 * @throws SQLException
	*/
	@SuppressWarnings("unchecked")
	private ActionForward accionSalir(ContratoForm contratoForm,
															 HttpServletRequest request, 
															 ActionMapping mapping,
															 Connection con, UsuarioBasico usuario) throws SQLException
															 {
		//TODO guardar esquemas tarifarios.
		Contrato mundoContrato=new Contrato ();  

		boolean puedoInsertarContrato1 =mundoContrato.puedoInsertarContrato(	con,
				contratoForm.getCodigoConvenio(),
				contratoForm.getFechaInicial(),
				contratoForm.getFechaFinal(), 
				"insertar", "", "");	
		logger.info("puedo insertar --->"+puedoInsertarContrato1);

		contratoForm.getDtoControlAnticipo().setUsuarioModifica(usuario.getLoginUsuario());
		contratoForm.getDtoControlAnticipo().setFechaModifica(UtilidadFecha.getFechaActual());
		contratoForm.getDtoControlAnticipo().setHoraModifica(UtilidadFecha.getHoraActual());
		HashMap validacionNumeroContratoMap= new HashMap();
		validacionNumeroContratoMap=mundoContrato.validacionNumeroContrato(con, contratoForm.getCodigoConvenio()+"", contratoForm.getNumeroContrato(), contratoForm.getFechaInicial(), contratoForm.getFechaFinal(), "");
		if(validacionNumeroContratoMap.containsKey("numRegistros"))
		{
			if(Integer.parseInt(validacionNumeroContratoMap.get("numRegistros").toString()) >0)
			{
				ActionErrors errores = new ActionErrors();

				for(int w=0; w<Integer.parseInt(validacionNumeroContratoMap.get("numRegistros").toString()); w++)
				{	
					errores.add("Fechas contrato", new ActionMessage("error.contrato.numeroContratoExistente", 
							contratoForm.getNumeroContrato(),
							validacionNumeroContratoMap.get("nombre_convenio_"+w).toString(),
							validacionNumeroContratoMap.get("codigo_"+w).toString(),
							validacionNumeroContratoMap.get("fecha_inicial_"+w).toString() +" "+
							validacionNumeroContratoMap.get("fecha_final_"+w).toString()));
				}	
				logger.warn("entra al error de numero de contrato");
				contratoForm.setEstado("empezar");
				saveErrors(request, errores);	
				this.cerrarConexion(con);									
				return mapping.findForward("principal");
			}
		}	

		if(puedoInsertarContrato1)
		{
			//contratoForm.setPuedoInsertar(puedoInsertarContrato1);
			llenarMundo(contratoForm, mundoContrato);
			contratoForm.setCodigo(mundoContrato.insertarContrato(con));

			if(contratoForm.getCodigo()<=0)
			{
				logger.warn("No se pudo insertar el contrato: "+contratoForm.getNumeroContrato());
				this.cerrarConexion(con);
				contratoForm.reset();
				ArrayList atributosError = new ArrayList();
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");				
				request.setAttribute("atributosError", atributosError);

				//manejo del mensaje
				contratoForm.setVieneDeIngresarModificar(true);

				return mapping.findForward("paginaError");	
			}
			else
			{
				//insertar los esquemas tarifario
				this.guardarEsquemasTarifarios(con,contratoForm,mundoContrato,usuario);

				//manejo del mensaje
				contratoForm.setVieneDeIngresarModificar(true);

				this.cerrarConexion(con);									
				return mapping.findForward("funcionalidadResumenContrato");
			}

		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Fechas contrato", new ActionMessage("error.cargo.rangoFechas"));
			logger.warn("entra al error de rangos de fechas");
			saveErrors(request, errores);	
			contratoForm.setEstado("empezar");
			this.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
															 }
	
	/**
	 * 
	 * @param con
	 * @param contratoForm
	 * @param mundoContrato
	 * @param usuario
	 */
	@SuppressWarnings("unchecked")
	private void guardarEsquemasTarifarios(Connection con, ContratoForm contratoForm, Contrato mundoContrato, UsuarioBasico usuario) 
	{
		///ESQUEMAS TARIFARIOS DE INVENTARIO
		//proceso para esquemas tarifarios de inventario.
		//eliminar
		logger.info("-------->"+contratoForm.getEsquemasInventarioEliminados().get("numRegistros")+"");
		for(int i=0;i<Utilidades.convertirAEntero(contratoForm.getEsquemasInventarioEliminados().get("numRegistros")+"");i++)
		{
			if(mundoContrato.eliminarEsquema(con,contratoForm.getEsquemasInventarioEliminados().get("codigo_"+i)+"",true))
			{
				Utilidades.generarLogGenerico(contratoForm.getEsquemasInventarioEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logContratoCodigo,indicesEsquemasInventario);
			}
		}
		logger.info("===>Paso el primer FOR");
		for(int i=0;i<Utilidades.convertirAEntero(contratoForm.getEsquemasInventario("numRegistros")+"");i++)
		{
			//modificar
			if((contratoForm.getEsquemasInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,contratoForm.getEsquemasInventario(),mundoContrato.consultarEsquemaInventarioLLave(con, contratoForm.getEsquemasInventario("codigo_"+i)+""),i,usuario,indicesEsquemasInventario,true))
			{
				logger.info("===>Entro a modificar");
				HashMap vo=new HashMap();
				vo.put("codigo", contratoForm.getEsquemasInventario("codigo_"+i)+"");
				vo.put("contrato", contratoForm.getCodigo());
				vo.put("clase", contratoForm.getEsquemasInventario("claseinventario_"+i));
				vo.put("esquematarifario", contratoForm.getEsquemasInventario("esquematarifario_"+i));
				vo.put("fechavigencia", contratoForm.getEsquemasInventario("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("codcentroatencion", contratoForm.isManejaTarifasXCA()?contratoForm.getEsquemasInventario("codcentroatencion_"+i):ConstantesBD.codigoNuncaValido+"");
				mundoContrato.modificarEsquemasInventario(con, vo);
				
			}
			//insertar
			else if((contratoForm.getEsquemasInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				logger.info("===>Entro a insertar");
				HashMap vo=new HashMap();
				vo.put("contrato", contratoForm.getCodigo());
				vo.put("clase", contratoForm.getEsquemasInventario("claseinventario_"+i));
				vo.put("esquematarifario", contratoForm.getEsquemasInventario("esquematarifario_"+i));
				vo.put("fechavigencia", contratoForm.getEsquemasInventario("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("codcentroatencion", contratoForm.isManejaTarifasXCA()?contratoForm.getEsquemasInventario("codcentroatencion_"+i):ConstantesBD.codigoNuncaValido+"");
				mundoContrato.insertarEsquemasInventario(con, vo);
			}
			
		}
		
		
//		proceso para esquemas tarifarios de procedimientos.
		//eliminar
		for(int i=0;i<Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientosEliminados().get("numRegistros")+"");i++)
		{
			if(mundoContrato.eliminarEsquema(con,contratoForm.getEsquemasProcedimientosEliminados().get("codigo_"+i)+"",false))
			{
				Utilidades.generarLogGenerico(contratoForm.getEsquemasProcedimientosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logContratoCodigo,indicesEsquemasProcedimientos);
			}
		}
		for(int i=0;i<Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("numRegistros")+"");i++)
		{
			//modificar
			if((contratoForm.getEsquemasProcedimientos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,contratoForm.getEsquemasProcedimientos(),mundoContrato.consultarEsquemaProcedimientoLLave(con, contratoForm.getEsquemasProcedimientos("codigo_"+i)+""),i,usuario,indicesEsquemasProcedimientos,true))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", contratoForm.getEsquemasProcedimientos("codigo_"+i)+"");
				vo.put("contrato", contratoForm.getCodigo());
				vo.put("grupo", contratoForm.getEsquemasProcedimientos("gruposervicio_"+i));
				vo.put("esquematarifario", contratoForm.getEsquemasProcedimientos("esquematarifario_"+i));
				vo.put("fechavigencia", contratoForm.getEsquemasProcedimientos("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("codcentroatencion", contratoForm.isManejaTarifasXCA()?contratoForm.getEsquemasProcedimientos("codcentroatencion_"+i):ConstantesBD.codigoNuncaValido+"");
				mundoContrato.modificarEsquemasProcedimientos(con, vo);
				
			}
			//insertar
			else if((contratoForm.getEsquemasProcedimientos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("contrato", contratoForm.getCodigo());
				vo.put("grupo", contratoForm.getEsquemasProcedimientos("gruposervicio_"+i));
				vo.put("esquematarifario", contratoForm.getEsquemasProcedimientos("esquematarifario_"+i));
				vo.put("fechavigencia", contratoForm.getEsquemasProcedimientos("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("codcentroatencion", contratoForm.isManejaTarifasXCA()?contratoForm.getEsquemasProcedimientos("codcentroatencion_"+i):ConstantesBD.codigoNuncaValido+"");
				mundoContrato.insertarEsquemasProcedimientos(con, vo);
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param esquemasInventario
	 * @param map
	 * @param i
	 * @param usuario
	 * @param indices
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices,boolean generarLog) 
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					if(generarLog)
						Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logContratoCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * resumen
	 * @param contratoForm ContratoForm
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward a la pagina de resumen "resumenContrato.jsp"
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionResumen(	ContratoForm contratoForm, 
											ActionMapping mapping, 
											HttpServletRequest request, 
											Connection con) throws SQLException, IPSException
	{
		Contrato mundoContrato=new Contrato();  	
		boolean validarCargar=mundoContrato.cargar(con,contratoForm.getCodigo()+"");
		
		if(!contratoForm.isVieneDeIngresarModificar())
		{	
			contratoForm.setMensaje("");
		}	
		else
		{
			contratoForm.setMensaje("Proceso Realizado Exitosamente!");
			contratoForm.setVieneDeIngresarModificar(false);
		}
		
		if(UtilidadTexto.isEmpty(mundoContrato.getCodigoTipoPago()))
		{	
			mundoContrato.setCodigoTipoPago("");
		}	
		if(validarCargar)
		{
			llenarForm(con,contratoForm,mundoContrato);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenContrato");
		}
		else
		{
			//logger.warn(" convenio de contrato modificado por otro usuario "+contratoForm.getCodigoConvenio());
			this.cerrarConexion(con);
			contratoForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El Contrato");
			request.setAttribute("codigoDescripcionError", "errors.excepcionSQL.registroYaActualizado");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * modificar contrato
	 * @param contratoForm ContratoForm
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward  a la pagina "modificarContrato.jsp"
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionModificar(ContratoForm contratoForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con) throws SQLException, IPSException
	{
		Contrato mundoContrato=new Contrato();
		mundoContrato.reset();
		mundoContrato.cargar(con,contratoForm.getCodigo()+"");
		
		if (mundoContrato.getTipoConvenio().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
			contratoForm.setEsConvenioOdontologicio(true);
		else
			contratoForm.setEsConvenioOdontologicio(false);
		
		llenarForm(con,contratoForm,mundoContrato);
		contratoForm.setEstado("modificar");
		contratoForm.setEliminar(false);
		contratoForm.setFechaFinalAntigua(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaFinal()));
		contratoForm.setFechaInicialAntigua(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaInicial()));
		contratoForm.setFechaFirmaContrato(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaFirmaContrato()));
		
		//si es menor que la fecha del sistema
		
		if(UtilidadValidacion.existeCuentasAsociadasAContrato(con, mundoContrato.getCodigo()))
		{
			contratoForm.setPuedoEliminar(false);
			contratoForm.setPuedoModificar(false);
		}
		else
		{
			contratoForm.setPuedoEliminar(true);
			contratoForm.setPuedoModificar(true);
		}
		if(contratoForm.getCodigo()<0)
		{
			logger.warn("No se pudo cargar el Codigo Contrato: "+contratoForm.getCodigo());
			this.cerrarConexion(con);
			contratoForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El Contrato ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
			
			contratoForm.getDtoLogControlAnticipo().setValorAnticipoContratadoConvenioAnterior(mundoContrato.getDtoControlAnticipo().getValorAnticipoContratadoConvenio());
			String log="\n            ====INFORMACION ORIGINAL===== " +
			"\n*  Codigo Contrato [" +mundoContrato.getCodigo() +"] "+
			"\n*  Codigo Convenio ["+mundoContrato.getCodigoConvenio()+"] " +
			"\n*  Sin Contrato ["+mundoContrato.getSinContrato()+"] "+
			"\n*  Observaciones ["+mundoContrato.getFrmObservaciones()+"] "+
			"\n*  No Contrato ["+mundoContrato.getNumeroContrato()+"] "+
			"\n*  Vr.Contrato ["+mundoContrato.getValorContrato()+"] "+
			"\n*  Vr Acumulado ["+mundoContrato.getValorAcumulado()+"] "+
			"\n*  Fecha Inicial ["+mundoContrato.getFechaInicial()+"] "+
			"\n*  Fecha Final ["+mundoContrato.getFechaFinal()+"] "+
			"\n*  Fecha Firma Contrato ["+mundoContrato.getFechaFirmaContrato()+"] "+
			"\n*  Dia Limite Radicacion ["+mundoContrato.getDiaLimiteRadicacion()+"] "+
			"\n*  Dias de Radicacion ["+mundoContrato.getDiasRadicacion()+"] "+
			"\n*  Controla Anticipos ["+mundoContrato.getControlaAnticipos()+"] "+
			"\n*  Maneja Cobertura ["+mundoContrato.getManejaCobertura()+"] " +
			"\n*  Requiere Auto. ["+mundoContrato.getRequiereAutorizacionNoCobertura()+"] ";
			//"\n*  Tipo Contrato ["+mundoContrato.getTipoContrato()+"]\n " ;
			
			//parte de capitacion
			if(mundoContrato.getCodigoTipoContrato().equals(ConstantesBD.codigoTipoContratoCapitado+""))
			{
				log+="\n*  Codigo Tipo Pago ["+mundoContrato.getCodigoTipoPago()+"] " +
				"\n*  %UPC ["+mundoContrato.getPorcentajeUpc()+"] "+		
				"\n*  UPC Contrato ["+mundoContrato.getUpc()+"] "+ 
				"\n*  %PYP ["+mundoContrato.getPyp()+"] " +
				"\n*  Base ["+mundoContrato.getBase()+" - "+mundoContrato.getNombreBase()+"] " +		
				"\n*  Contrato Secretaria ["+mundoContrato.getContratoSecretaria()+"] ";
			}
			String consecutivosNivelesTemp="";
			for(int w=0; w<mundoContrato.getNumeroRegistrosNivelAtencionMap(); w++)
			{
				consecutivosNivelesTemp+=mundoContrato.getNivelAtencionMap("consecutivo_"+w)+",";
			}
			log+="\n*  Codigo Niveles ["+consecutivosNivelesTemp+"]";
			contratoForm.setLogInfoOriginalContrato(log);
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}	

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param contratoForm ContratoForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward "contrato.do?estado=modificar"
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionGuardarModificacion(ContratoForm contratoForm,
																						HttpServletRequest request, 
																						ActionMapping mapping, 
																						Connection con)	
																						throws SQLException, IPSException
	{
		
		//TODO guardar modificar.
		logger.info("\n\nMODIFICANDO");
	    boolean existioModificacion= existeModificacion(contratoForm, con)||existeModificacionEsquemas(con,contratoForm);
		if(existioModificacion)
		{
			Contrato mundoContrato=new Contrato ();
			
			HashMap validacionNumeroContratoMap= new HashMap();
			validacionNumeroContratoMap=mundoContrato.validacionNumeroContrato(con, contratoForm.getCodigoConvenio()+"", contratoForm.getNumeroContrato(), contratoForm.getFechaInicial(), contratoForm.getFechaFinal(), contratoForm.getCodigo()+"");
			if(validacionNumeroContratoMap.containsKey("numRegistros"))
			{
				if(Integer.parseInt(validacionNumeroContratoMap.get("numRegistros").toString()) >0)
				{
					ActionErrors errores = new ActionErrors();
					
					for(int w=0; w<Integer.parseInt(validacionNumeroContratoMap.get("numRegistros").toString()); w++)
					{	
						errores.add("Fechas contrato", new ActionMessage("error.contrato.numeroContratoExistente", 
																		contratoForm.getNumeroContrato(),
																		validacionNumeroContratoMap.get("nombre_convenio_"+w).toString(),
																		validacionNumeroContratoMap.get("codigo_"+w).toString(),
																		validacionNumeroContratoMap.get("fecha_inicial_"+w).toString() +" "+
																		validacionNumeroContratoMap.get("fecha_final_"+w).toString()));
					}	
					logger.warn("entra al error de numero de contrato");
					contratoForm.setEstado("modificar");
					saveErrors(request, errores);	
					this.cerrarConexion(con);									
					return mapping.findForward("principal");
				}
			}	
		
			boolean puedoInsertarContrato1 =mundoContrato.puedoInsertarContrato(	con,
																					contratoForm.getCodigoConvenio(),
																					contratoForm.getFechaInicial(),
																					contratoForm.getFechaFinal(),
																					"modificar",
																					contratoForm.getFechaInicialAntigua(),
																					contratoForm.getFechaFinalAntigua()
																				);
			logger.info("puedo modificar --->"+puedoInsertarContrato1);

			
			if(puedoInsertarContrato1)
			{
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				llenarMundo(contratoForm, mundoContrato);
				mundoContrato.modificarContrato(con); 
				
				//modificar los esquemas tarifarios.
				this.guardarEsquemasTarifarios(con,contratoForm,mundoContrato,usuario);
				
				if(!contratoForm.getEliminar())
				{
					
					String log=contratoForm.getLogInfoOriginalContrato()+
							"\n   =====INFORMACI�N DESPUES DE LA MODIFICACI�N===== " +
							"\n*  Codigo Contrato [" +mundoContrato.getCodigo() +"] "+
							"\n*  Codigo Convenio ["+mundoContrato.getCodigoConvenio()+"] " +
							"\n*  Sin Contrato ["+mundoContrato.getSinContrato()+"] "+
							"\n*  No.Contrato ["+mundoContrato.getNumeroContrato()+"] "+
							"\n*  Vr.Contrato ["+mundoContrato.getValorContrato()+"] "+
							"\n*  Vr Acumulado ["+mundoContrato.getValorAcumulado()+"] "+
							"\n*  Fecha Inicial ["+mundoContrato.getFechaInicial()+"] "+
							"\n*  Fecha Final ["+mundoContrato.getFechaFinal()+"] "+
							"\n*  Fecha Firma Contrato ["+mundoContrato.getFechaFirmaContrato()+"] "+
							"\n*  D�a Limite Radicacion ["+mundoContrato.getDiaLimiteRadicacion()+"] "+
							"\n*  D�as de Radicacion ["+mundoContrato.getDiasRadicacion()+"] "+
							"\n*  Controla Anticipos ["+mundoContrato.getControlaAnticipos()+"] "+
							"\n*  Maneja Cobertura ["+mundoContrato.getManejaCobertura()+"] " +
							"\n*  Requiere Auto. ["+mundoContrato.getRequiereAutorizacionNoCobertura()+"] ";
							//parte de capitacion
							if(contratoForm.getCodigoTipoContrato().equals(ConstantesBD.codigoTipoContratoCapitado+""))
							{
								log+="\n*  Codigo Tipo Pago ["+mundoContrato.getCodigoTipoPago()+"] " +
								"\n*  %UPC ["+mundoContrato.getPorcentajeUpc()+"] "+
								"\n*  UPC ["+mundoContrato.getUpc()+"] "+ 
								"\n*  %PYP ["+mundoContrato.getPyp()+"] "+
								"\n*  Base ["+mundoContrato.getBase()+" - "+mundoContrato.getNombreBase()+"] " +
								"\n*  Contrato Secretaria ["+mundoContrato.getContratoSecretaria()+"] ";
							}
							String consecutivosNivelesTemp="";
							for(int w=0; w<mundoContrato.getNumeroRegistrosNivelAtencionMap(); w++)
							{
								consecutivosNivelesTemp+=mundoContrato.getNivelAtencionMap("consecutivo_"+w)+",";
							}
							log+="\n*  Codigo Niveles ["+consecutivosNivelesTemp+"]";
							//"\n*  Tipo Contrato ["+mundoContrato.getTipoContrato()+"]\n " +
							log+="========================================================\n\n\n " ;
					
						LogsAxioma.enviarLog(ConstantesBD.logContratoCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
						
				}				
				
				if(contratoForm.getEliminar())
				{
					mundoContrato.eliminarContrato(con);
					String log=	contratoForm.getLogInfoOriginalContrato()+"\n 	====>>>> FUE ELIMINADO\n\n\n";
					LogsAxioma.enviarLog(ConstantesBD.logContratoCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
					
					contratoForm.setEstado("modificar");
					
					this.cerrarConexion(con);
					return mapping.findForward("paginaMensajeBorrado");
				}

				contratoForm.setEstado("modificar");

				//manejo del mensaje
				contratoForm.setVieneDeIngresarModificar(true);

				this.cerrarConexion(con);									
				return mapping.findForward("funcionalidadResumenContrato");
			}	
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("Fechas contrato", new ActionMessage("error.cargo.rangoFechas"));
				logger.warn("entra al error de rango Fechas");
				saveErrors(request, errores);	
				contratoForm.setEstado("modificar");
				this.cerrarConexion(con);									
				return mapping.findForward("principal");
			}
		}
		else
		{
		    //NO EXISTIO MODIFICACION ENTONCES SE DEJA EN LA MISMA PAGINA
			this.cerrarConexion(con);
			contratoForm.setEstado("modificar");
			return mapping.findForward("principal");
		}	
	}
	
	/**
	 * 
	 * @param contratoForm
	 * @return
	 */
	private boolean existeModificacionEsquemas(Connection con,ContratoForm contratoForm) 
	{
		Contrato mundoContrato=new Contrato ();
		
		for(int i=0;i<Utilidades.convertirAEntero(contratoForm.getEsquemasInventario("numRegistros")+"");i++)
		{
			//modificar
			if(this.existeModificacion(con,contratoForm.getEsquemasInventario(),mundoContrato.consultarEsquemaInventarioLLave(con, contratoForm.getEsquemasInventario("codigo_"+i)+""),i,new UsuarioBasico(),indicesEsquemasInventario,false))
			{
				return true;
			}
		}
		
		for(int i=0;i<Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("numRegistros")+"");i++)
		{
			//modificar
			if(this.existeModificacion(con,contratoForm.getEsquemasProcedimientos(),mundoContrato.consultarEsquemaProcedimientoLLave(con, contratoForm.getEsquemasProcedimientos("codigo_"+i)+""),i,new UsuarioBasico(),indicesEsquemasProcedimientos,false))
			{
				return true;
			}
		}

		return false;
		
	}

	/**
	 * Evalua si existio modificacion
	 * @param contratoForm
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private boolean existeModificacion (ContratoForm contratoForm, Connection con)throws SQLException, IPSException
	{
	    if(!contratoForm.getEliminar())
		{
		    Contrato mundoContrato=new Contrato();
			mundoContrato.reset();
			mundoContrato.cargar(con,contratoForm.getCodigo()+"");
			
		    if(mundoContrato.getCodigoConvenio()== contratoForm.getCodigoConvenio())
		    {
		        if((mundoContrato.getNumeroContrato()).equals(contratoForm.getNumeroContrato()))
			    {
			        if((mundoContrato.getValorContrato()+"").equals(contratoForm.getValorContratoStr()))
			        {
			            if((mundoContrato.getValorAcumulado()+"").equals(contratoForm.getValorAcumuladoStr()))
			            {
	                        if(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaInicial()).equals(contratoForm.getFechaInicial()))
	                        {
	                            if(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaFinal()).equals(contratoForm.getFechaFinal()))
		                        {
	                            	if(contratoForm.getNivelAtencionMap().equals(mundoContrato.getNivelAtencionMap()))
            						{
		                            	//si es capitado entonces debe validar otros campos
		                            	if(!contratoForm.getCodigoTipoContrato().equals(ConstantesBD.codigoTipoContratoCapitado))
		                            	{	
		                            		return false;
		                            	}
		                            	else
		                            	{
		                            		if(contratoForm.getCodigoTipoPago().equals(mundoContrato.getCodigoTipoContrato()))
		                            		{
		                            			if(contratoForm.getPorcentajeUpc().equals(mundoContrato.getPorcentajeUpc()))
		                            			{
		                            				if(contratoForm.getUpc().equals(mundoContrato.getUpc()))
			                            			{
			                            				if(contratoForm.getPyp().equals(mundoContrato.getPyp()))
			                            				{
			                            					if(contratoForm.getBase().equals(mundoContrato.getBase()))
			                            					{	
				                            					if(contratoForm.getContratoSecretaria().equals(mundoContrato.getContratoSecretaria()))
				                            					{
				                            						return false;						                            					}
			                            					}	
			                            				}
			                            			}
		                            			}	
		                            		}
		                            	}
            						}	
		                        }    
			                }
			            }
			        }
			    }
			}
		}
	    return true;
	}
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * listar contratos
	 * @param contratoForm ContratoForm
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward  a la pagina "listadoContrato.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarContratos	(	ContratoForm contratoForm, 
																					ActionMapping mapping,
																					Connection con,String estado,
																					UsuarioBasico usuario) throws SQLException 
	{
		Contrato mundoContrato= new Contrato();
		contratoForm.setEstado(estado);
		contratoForm.setCol(mundoContrato.listadoConvenio(con, Integer.parseInt(usuario.getCodigoInstitucion())));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar")	;	
	}	

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param contratoForm ContratoForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward a la pagina funcionalidadBuscarContrato "busquedaContrato.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	ContratoForm contratoForm, 
																						ActionMapping mapping, 
																						Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		contratoForm.reset();
		contratoForm.setEstado("busquedaAvanzada");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");		
	}
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param contratoForm ContratoForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegacion
	 * @param con conexion con la fuente de datos
	 * @return ActionForward a la pagina funcionalidadBuscarContrato "busquedaContrato.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(	ContratoForm contratoForm, 
															ActionMapping mapping, 
															Connection con,
															boolean accionModificar,
															UsuarioBasico usuario) throws SQLException
	{
		Contrato mundoContrato= new Contrato();
		mundoContrato.reset();
		enviarItemsSeleccionadosBusqueda(contratoForm, mundoContrato);
		contratoForm.resetCriteriosBusqueda();
		contratoForm.setCol(mundoContrato.resultadoBusquedaAvanzada(con, Integer.parseInt(usuario.getCodigoInstitucion()), ""));
		
		if(accionModificar)
		{
		    contratoForm.setEstado("listarModificar");
		}
		this.cerrarConexion(con);
		//llenarMundo(empresaForm,mundoEmpresa);
		return mapping.findForward("paginaListar");
	}
	
	/**
	 * accion que adiciona un nivel de atencion al mapa
	 * @param contratoForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAdicionarNivelAtencionMapa(	ContratoForm contratoForm, 
															ActionMapping mapping, 
															Connection con)throws SQLException
	{
		int index=contratoForm.getNumeroRegistrosNivelAtencionMap();
		contratoForm.setNivelAtencionMap("consecutivo_"+index, contratoForm.getConsecutivoNivelAtencion());
		contratoForm.setNivelAtencionMap("estaBD_"+index, "false");
		contratoForm.setNivelAtencionMap("fueEliminado_"+index, "false");
		index++;
		contratoForm.setNivelAtencionMap("numRegistros", index+"");
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * accion que adiciona un nivel de atencion al mapa
	 * @param contratoForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarNivelAtencionMapa(	ContratoForm contratoForm,
															HttpServletRequest request,
															ActionMapping mapping, 
															Connection con)throws SQLException
	{
		ActionErrors errores = new ActionErrors();
		UtilidadTransaccion.getTransaccion().begin(); 
		
		try{
			
			IParametrizacionPresupuestoCapitacionMundo parametrizacionPresupuestoCapitacionMundo=CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			
			Log4JManager.info("consecutivo---------------------->"+Utilidades.convertirALong(contratoForm.getNivelAtencionMap("consecutivo_"+contratoForm.getIndexToDelete())));
			boolean existeNivel=parametrizacionPresupuestoCapitacionMundo.existeNivelAtencionPresupuestoCapitacion(Utilidades.convertirALong(contratoForm.getNivelAtencionMap("consecutivo_"+contratoForm.getIndexToDelete())));
		
			if(existeNivel)
			{
				contratoForm.setNivelAtencionMap("fueEliminado_"+contratoForm.getIndexToDelete(), "false");
				errores.add("Eliminar Nivel", new ActionMessage("error.contrato.nivelAtencion"));		
				saveErrors(request, errores);
					
			}else
			{
				contratoForm.setNivelAtencionMap("fueEliminado_"+contratoForm.getIndexToDelete(), "true");
				this.cerrarConexion(con);
			}
			UtilidadTransaccion.getTransaccion().commit();
		}catch(SQLException se){
			UtilidadTransaccion.getTransaccion().rollback();
			errores.add("Problema Eliminar Nivel", new ActionMessage("errors.transaccion"));
			saveErrors(request, errores);
		}
		
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param contratoForm
	 * @param mundoContrato
	 */
	private void enviarItemsSeleccionadosBusqueda(ContratoForm contratoForm, Contrato mundoContrato)
	{
		String bb[]= contratoForm.getCriteriosBusqueda();
		
		logger.info("Criterio de Busqueda 1--->"+contratoForm.getCriteriosBusqueda());
		
		for(int i=0; i<bb.length; i++)
		{
			logger.info("Criterio de Busqueda BB--->"+bb[i]);
			try
			{
				if(bb[i].equals("nombreConvenio"))
					mundoContrato.setNombreConvenio(contratoForm.getNombreConvenio());
				if(bb[i].equals("numeroContrato"))
					mundoContrato.setNumeroContrato(contratoForm.getNumeroContrato());
				if(bb[i].equals("valorContratoStr"))
				{   
				    try
				    {
				        mundoContrato.setValorContrato(Double.parseDouble(contratoForm.getValorContratoStr()));
				    }
				    catch (NumberFormatException e)
				    {
				        mundoContrato.setValorContrato(ConstantesBD.codigoNuncaValido);
				    }
				}
				if(bb[i].equals("valorAcumuladoStr"))
				{   
				    try
				    {
				        mundoContrato.setValorAcumulado(Double.parseDouble(contratoForm.getValorAcumuladoStr()));
				    }
				    catch (NumberFormatException e)
				    {
				        mundoContrato.setValorAcumulado(ConstantesBD.codigoNuncaValido);
				    }
				}	
				if(bb[i].equals("numeroContrato"))
					mundoContrato.setNumeroContrato(contratoForm.getNumeroContrato());

				if(bb[i].equals("requiereautonocobertura"))
					mundoContrato.setRequiereAutorizacionNoCobertura(contratoForm.getRequiereAutorizacionNoCobertura());
				
				//PARTE DE CAPITACION
				if(bb[i].equals("nombreTipoPago"))
				{
					mundoContrato.setNombreTipoPago(contratoForm.getNombreTipoPago());
				}	
				if(bb[i].equals("upc"))
				{	
					mundoContrato.setUpc(contratoForm.getUpc());
				}	
				if(bb[i].equals("contratoSecretaria"))
					mundoContrato.setContratoSecretaria(contratoForm.getContratoSecretaria());
				if(bb[i].equals("pyp"))
					mundoContrato.setPyp(contratoForm.getPyp());
				if(bb[i].equals("busquedaNivelAtencion"))
					mundoContrato.setBusquedaNivelAtencion(contratoForm.getBusquedaNivelAtencion());
				//fin capitacion
				
				if(bb[i].equals("rangoFechas"))
				{
					mundoContrato.setEleccionFechaBusqueda(contratoForm.getEleccionFechaBusqueda());
					mundoContrato.setFechaInicial(contratoForm.getFechaInicial());
					mundoContrato.setFechaFinal(contratoForm.getFechaFinal());
				}
				if(bb[i].equals("porcentajeUpc"))
					mundoContrato.setPorcentajeUpc(contratoForm.getPorcentajeUpc());
				if(bb[i].equals("base"))
					mundoContrato.setBase(contratoForm.getBase());
				
				
				if(bb[i].equals("esquemaTarProcedimiento"))
					mundoContrato.setEsquemaTarProcedimiento(contratoForm.getEsquemaTarProcedimiento());
				
				if(bb[i].equals("esquemaTarInventario"))
					mundoContrato.setEsquemaTarInventario(contratoForm.getEsquemaTarInventario());
				
				if(bb[i].equals("sinContrato"))
					mundoContrato.setSinContrato(contratoForm.getSinContrato());
				
				
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}		
	}


	/**
	 * Este metodo carga los datos pertinentes a la forma 
	 * @param convenioForm (form)
	 * @param mundoConvenio (mundo)
	 */
	@SuppressWarnings("unchecked")
	protected void llenarForm(Connection con,ContratoForm contratoForm, Contrato mundoContrato)
	{	
		contratoForm.setCodigo(mundoContrato.getCodigo());
		contratoForm.setCadenaCodigoConvenio(mundoContrato.getCodigoConvenio()+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico);
		contratoForm.setFechaInicial(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaInicial()));
		contratoForm.setFechaFinal(UtilidadFecha.conversionFormatoFechaAAp(mundoContrato.getFechaFinal()));
		contratoForm.setNumeroContrato(mundoContrato.getNumeroContrato());
		contratoForm.setFechaFirmaContrato(mundoContrato.getFechaFirmaContrato());
		contratoForm.setDiaLimiteRadicacion(mundoContrato.getDiaLimiteRadicacion());
		contratoForm.setDiasRadicacion(mundoContrato.getDiasRadicacion());
		contratoForm.setControlaAnticipos(mundoContrato.getControlaAnticipos());
		contratoForm.setManejaCobertura(mundoContrato.getManejaCobertura());
		contratoForm.setSinContrato(mundoContrato.getSinContrato());
		contratoForm.setFrmObservaciones(mundoContrato.getFrmObservaciones());	//tarea 66618
		
		//contratoForm.setTipoContrato(mundoContrato.getTipoContrato());
		
		//parte de capitacion
		contratoForm.setCodigoTipoContrato(mundoContrato.getCodigoTipoContrato());
		contratoForm.setCodigoTipoPago(mundoContrato.getCodigoTipoPago());
		contratoForm.setUpc(mundoContrato.getUpc());
		contratoForm.setPyp(mundoContrato.getPyp());
		contratoForm.setContratoSecretaria(mundoContrato.getContratoSecretaria());
		contratoForm.setNivelAtencionMap((HashMap)mundoContrato.getNivelAtencionMap().clone());
		
		if(contratoForm.getEstado().equals("modificar") )
		{
		    contratoForm.setValorContratoStr(UtilidadTexto.formatearExponenciales(Double.parseDouble(mundoContrato.getValorContrato()+"")));
		    contratoForm.setValorAcumuladoStr(UtilidadTexto.formatearExponenciales(Double.parseDouble(mundoContrato.getValorAcumulado()+"")));
		}
		
		contratoForm.setPorcentajeUpc(mundoContrato.getPorcentajeUpc());
		contratoForm.setBase(mundoContrato.getBase());
		contratoForm.setNombreBase(mundoContrato.getNombreBase());
		
		contratoForm.setRequiereAutorizacionNoCobertura(mundoContrato.getRequiereAutorizacionNoCobertura());
		contratoForm.setPacientePagaAtencion(mundoContrato.getPacientePagaAtencion());
		contratoForm.setValidarAbonoAtencionOdo(mundoContrato.getValidarAbonoAtencionOdo());
		
		contratoForm.setEsquemasInventario(mundoContrato.getEsquemasInventario());
		contratoForm.setEsquemasProcedimientos(mundoContrato.getEsquemasProcedimientos());
		Utilidades.imprimirMapa(contratoForm.getEsquemasProcedimientos());
		logger.info("--->"+(Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("numRegistros")+"")>0));
		if(Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("numRegistros")+"")>0)
		{
			logger.info("--->entre");
			HashMap tmp= new HashMap ();
			
			DistribucionCuenta dc= new DistribucionCuenta();
			tmp=dc.consultarEsquemaInventarioLLave(con, contratoForm.getEsquemasProcedimientos("esquematarifario_0")+"");
			if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
				contratoForm.setCodigoTarifarioOficial(Utilidades.obtenertipoTarifarioEsquema(con,Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("esquematarifario_0")+"")));
			else
			{
				tmp.clear();
				tmp=dc.consultarEsquemaProcedimientoLLave(con,  contratoForm.getEsquemasProcedimientos("esquematarifario_0")+"");
				
				if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
					contratoForm.setCodigoTarifarioOficial(Utilidades.obtenertipoTarifarioEsquema(con,Utilidades.convertirAEntero(contratoForm.getEsquemasProcedimientos("esquematarifario_0")+"")));
			}
		}
		logger.info(contratoForm.getCodigoTarifarioOficial());
		HashMap mapaTemp=new HashMap();
		mapaTemp.put("numRegistros", "0");
		contratoForm.setEsquemasInventarioEliminados(mapaTemp);
		contratoForm.setEsquemasProcedimientosEliminados(mapaTemp);
		contratoForm.setDtoControlAnticipo(mundoContrato.getDtoControlAnticipo());
		contratoForm.setDtoLogControlAnticipo(mundoContrato.getDtoLogControlAnticipo());
		
		contratoForm.setEsContratoUsado(UtilidadValidacion.esContratoUsado(con, mundoContrato.getCodigo()));
		logger.info("===>Es Contrato Usado: "+contratoForm.isEsContratoUsado());
		
		contratoForm.setManejaTarifasXCA(mundoContrato.isManejaTarifasXCA());

		if(contratoForm.getCodigoConvenio()>0)
		{	
			DtoConvenio dto = new DtoConvenio();

			dto.setCodigo(contratoForm.getCodigoConvenio());
			ArrayList<DtoConvenio> listaTmp=  Convenio.cargarConveniosArrayList(dto);
			if(listaTmp.size()>0)
			{
				logger.info("*****************************************************************************");
				if (listaTmp.get(0).getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
				{
					logger.info("\n\n\n\n EXISTEN CONVENIO ******************************************** \n\n\n\n");
					contratoForm.setEsConvenioOdontologicio(Boolean.TRUE);
				}
				else
					contratoForm.setEsConvenioOdontologicio(Boolean.FALSE);

				logger.info("es convenio maneja montos------>"+listaTmp.get(0).getConvenioManejaMontos());
				contratoForm.setConvenioManejaMontos(listaTmp.get(0).getConvenioManejaMontos());
				logger.info("es convenio odon------>"+contratoForm.isEsConvenioOdontologicio());
			}
		}
		
	}		


	/**
	 * metodo que carga los datos pertinentes desde el 
	 * form ContratoForm para el mundo de Contrato
	 * @param contratoForm ContratoForm (forma)
	 * @param mundoContrato Contrato (mundo)
	 */
	@SuppressWarnings("unchecked")
	protected void llenarMundo(ContratoForm contratoForm, Contrato mundoContrato)
	{
		mundoContrato.setCodigo(contratoForm.getCodigo());
		mundoContrato.setCodigoConvenio(contratoForm.getCodigoConvenio());
		mundoContrato.setNumeroContrato(contratoForm.getNumeroContrato());
		mundoContrato.setFechaInicial(contratoForm.getFechaInicial());
		mundoContrato.setFechaFinal(contratoForm.getFechaFinal());
		mundoContrato.setFechaFirmaContrato(contratoForm.getFechaFirmaContrato());
		mundoContrato.setDiaLimiteRadicacion(contratoForm.getDiaLimiteRadicacion());
		mundoContrato.setDiasRadicacion(contratoForm.getDiasRadicacion());
		mundoContrato.setControlaAnticipos(contratoForm.getControlaAnticipos());
		mundoContrato.setManejaCobertura(contratoForm.getManejaCobertura());
		mundoContrato.setSinContrato(contratoForm.getSinContrato());
		
		mundoContrato.setFrmObservaciones(contratoForm.getFrmObservaciones());  //tarea 66618

		
		//mundoContrato.setTipoContrato(contratoForm.getTipoContrato());
		
		//parte de capitacion+
		mundoContrato.setCodigoTipoContrato(contratoForm.getCodigoTipoContrato());
		mundoContrato.setUpc(contratoForm.getUpc());
		mundoContrato.setCodigoTipoPago(contratoForm.getCodigoTipoPago());
		mundoContrato.setPyp(contratoForm.getPyp());
		mundoContrato.setContratoSecretaria(contratoForm.getContratoSecretaria());
		mundoContrato.setNivelAtencionMap((HashMap)contratoForm.getNivelAtencionMap().clone());
		
		if(contratoForm.getEstado().equals("salir") || contratoForm.getEstado().equals("guardarModificacion"))
		{
		    mundoContrato.setValorContrato(Double.parseDouble(contratoForm.getValorContratoStr()));
		    mundoContrato.setValorAcumulado(Double.parseDouble(contratoForm.getValorAcumuladoStr()));
		}
		
		
		mundoContrato.setPorcentajeUpc(contratoForm.getPorcentajeUpc());
		mundoContrato.setBase(contratoForm.getBase());
		mundoContrato.setNombreBase(contratoForm.getNombreBase());
		mundoContrato.setRequiereAutorizacionNoCobertura(contratoForm.getRequiereAutorizacionNoCobertura());
		mundoContrato.setValidarAbonoAtencionOdo(contratoForm.getValidarAbonoAtencionOdo());
		mundoContrato.setPacientePagaAtencion(contratoForm.getPacientePagaAtencion());
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info(" 	REQUIERE CONTROL ANTICIPO------------------------------------------>"+contratoForm.getDtoControlAnticipo().getRequiereAnticipo());
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		mundoContrato.setDtoControlAnticipo(contratoForm.getDtoControlAnticipo());
		mundoContrato.setDtoLogControlAnticipo(contratoForm.getDtoLogControlAnticipo());
		
		mundoContrato.setManejaTarifasXCA(contratoForm.isManejaTarifasXCA());
	}

	
	/**
	 * metodo en que se cierra la conexion (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con conexion con la fuente de datos
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