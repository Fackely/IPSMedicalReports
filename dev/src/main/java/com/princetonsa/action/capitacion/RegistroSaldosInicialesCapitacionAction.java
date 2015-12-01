/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.capitacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.capitacion.RegistroSaldosInicialesCapitacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.ContratoCargue;
import com.princetonsa.mundo.capitacion.CuentaCobroCapitacion;
import com.princetonsa.mundo.capitacion.RegistroSaldosInicialesCapitacion;

/**
 * Clase para manejar el workflow 
 *
 * @version 1.0, 08/08/2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class RegistroSaldosInicialesCapitacionAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RegistroSaldosInicialesCapitacionAction.class);
	
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
		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof RegistroSaldosInicialesCapitacionForm)
		{
			
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
						
			RegistroSaldosInicialesCapitacionForm forma =(RegistroSaldosInicialesCapitacionForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			String estado=forma.getEstado(); 
			logger.warn("Estado Registro Saldos Iniciales capitacion-->"+estado);
			
			ActionForward validacionesGenerales = this.validacionesAcceso(mapping, request, forma, usuario);
			if (validacionesGenerales != null)
			{
				UtilidadBD.cerrarConexion(con);
				return validacionesGenerales ;
			}
			
			if(estado == null)
			{
				forma.reset();	
				logger.warn("Estado no valido dentro del flujo de registro de saldos iniciales  (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				return this.accionEmpezar(forma,mapping, con, usuario);
			}
			else if(estado.equals("continuarRegistroCuentaCobro"))
			{
				return this.accionContinuarRegistroCuentaCobro(forma, mapping, con, request);
			}
			else if(estado.equals("ordenar"))
			{
				this.accionOrdenar(forma);
				UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("redireccion"))
			{
				UtilidadBD.cerrarConexion(con);
				forma.setEstado("continuarRegistroCuentaCobro");
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("activarTodosLosChecks"))
			{
				return this.accionActivarInactivarTodosLosChecks(con, forma, mapping, "si", response);
			}
			else if(estado.equals("inactivarTodosLosChecks"))
			{
				return this.accionActivarInactivarTodosLosChecks(con, forma, mapping, "no", response);
			}
			else if(estado.equals("continuarMostrarErrores"))
			{
				UtilidadBD.cerrarConexion(con);
				forma.setEstado("continuarRegistroCuentaCobro");
				return mapping.findForward("principalPrincipal");
			}
			else if(estado.equals("ingresarNuevoCargueMap"))
			{
				return this.accionIngresarNuevoCargueMap(con, forma, request, response);
			}
			else if(estado.equals("eliminarCargueMap"))
			{
				return this.accionEliminarCargueMap(con, forma, response);
			}
			else if(estado.equals("guardar"))
			{
				return this.accionGuardar(forma, mapping, con, request, usuario);
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de registro de saldos iniciales ");
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
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param forma RegistroSaldosInicialesCapitacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal 
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	RegistroSaldosInicialesCapitacionForm forma, 
											ActionMapping mapping, 
											Connection con,
											UsuarioBasico usuario) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * accion continuar registro cuenta cobro, muestra el listado de las cuentas de cobro
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionContinuarRegistroCuentaCobro(	RegistroSaldosInicialesCapitacionForm forma, 
																ActionMapping mapping, 
																Connection con,
																HttpServletRequest request
															) throws SQLException
	{
		RegistroSaldosInicialesCapitacion mundo= new RegistroSaldosInicialesCapitacion();
		mundo.reset();
		ActionForward validacionesCargues = this.existenCarguesSaldoPendienteCeroOAjustesSinAprobar(mundo, forma, con, mapping, request);
		if (validacionesCargues != null)
		{
			UtilidadBD.cerrarConexion(con);
			return validacionesCargues ;
		}
		// de lo contrario se carga el listado
		forma.setListadoMap(mundo.busquedaCuentasCobro(con, forma.getCriteriosBusquedaMap()));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * METODO QUE MUESTRA LOS POSIBLES ERRORES OCURRIDOS 
	 * PORQUE EXISTEN SALDOS PENDIENTES CERO O AJUSTES SIN APROBAR
	 * @param mundo
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected ActionForward existenCarguesSaldoPendienteCeroOAjustesSinAprobar(	RegistroSaldosInicialesCapitacion mundo, 
																				RegistroSaldosInicialesCapitacionForm forma,
																				Connection con,
																				ActionMapping mapping, 
																				HttpServletRequest request)
	{
		HashMap posiblesErroresMap= mundo.existenCarguesSaldoPendienteCeroOAjustesSinAprobar(con, forma.getCriteriosBusquedaMap());
		ActionErrors errores = new ActionErrors();
		String carguesErroresSaldo="";
		String numeroContratosErroresSaldo="";
		String carguesErroresAjustes="";
		String numeroContratosErroresAjustes="";
		for(int i=0; i<Integer.parseInt(posiblesErroresMap.get("numRegistros").toString()); i++)
		{
			//errores posr saldo
			if(posiblesErroresMap.get("saldo_"+i).equals("0") || posiblesErroresMap.get("saldo_"+i).equals("0.00"))
			{
				carguesErroresSaldo+=posiblesErroresMap.get("cargue_"+i).toString()+",";
				numeroContratosErroresSaldo+= posiblesErroresMap.get("numerocontrato_"+i).toString()+",";
			}
			//de los contrario son errores ajustes pendientes
			else
			{
				carguesErroresAjustes+=posiblesErroresMap.get("cargue_"+i).toString()+",";
				numeroContratosErroresAjustes+= posiblesErroresMap.get("numerocontrato_"+i).toString()+",";
			}
		}
		
		if(!carguesErroresSaldo.equals(""))
			errores.add("", new ActionMessage("error.registroSaldoInicialCapitacion.carguesSaldoPendienteCero", carguesErroresSaldo, numeroContratosErroresSaldo));
		if(!carguesErroresAjustes.equals(""))
			errores.add("", new ActionMessage("error.registroSaldoInicialCapitacion.carguesAjustesPendientes", carguesErroresAjustes, numeroContratosErroresAjustes));
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			//LA CONEXION SE CIERRA EN EL METODO QUE LLAMA existenCarguesSaldoPendienteCeroOAjustesSinAprobar
			return mapping.findForward("paginaErroresActionErrors");
		}
		return null;
	}
	
	
	/**
	 * Método que valida el acceso a la funcionalidad
	 * @param con
	 * @param paciente
	 * @param map
	 * @param req
	 * @return
	 * @throws SQLException 
	 */
	protected ActionForward validacionesAcceso( ActionMapping map, 
                                                HttpServletRequest req,
                                                RegistroSaldosInicialesCapitacionForm forma, 
                                                UsuarioBasico usuario) throws SQLException
	{
		
        
		//1. Se verifica si existe cierre inicial
	    if(UtilidadValidacion.existeCierreSaldosInicialesCapitacion(usuario.getCodigoInstitucionInt()))
	    {
			logger.warn("existe cierre inicial");			
			req.setAttribute("codigoDescripcionError", "error.registroSaldoInicialCapitacion.existeSaldoInicial");
			return map.findForward("paginaError");
		}
	    else    
	    {
	    	ActionErrors errores = new ActionErrors();
	    	String tipoConsecutivo="";
        	
	    	if(ValoresPorDefecto.getFechaCorteSaldoInicialCCapitacion(usuario.getCodigoInstitucionInt()).trim().equals(""))
		    {
	    		logger.warn("no esta definida la fecha de corte inicial");
	    		errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "fecha corte saldo inicial capitación"));
	        }
	    	try
	    	{
	    		//Se obtiene el valor del parámetro tipo de consecutivo capitación -----//
	    		tipoConsecutivo=ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt());
	    		if(!UtilidadCadena.noEsVacio(tipoConsecutivo) || tipoConsecutivo.equals("-1"))
		    	{
		    		logger.warn("no esta definido el tipo consecutivo capitacion");
		    		errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "tipo de consecutivo capitación"));
		    	}
	    	}
	    	catch (Exception e) 
	    	{
	    		logger.warn("no esta definido el tipo consecutivo capitacion");
	    		errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "tipo de consecutivo capitación"));
	    	}
	    	if(!errores.isEmpty())
	    	{
	    		saveErrors(req, errores);
	    		return map.findForward("paginaErroresActionErrors");
	    	}
	    	else
	    	{
	    		if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoCapitacionDiferenteCartera+""))
	    		{
	    			Connection con=UtilidadBD.abrirConexion();
	    			String consecutivoCxCCapitacion=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentaCobroCapitacion, usuario.getCodigoInstitucionInt());
	    			UtilidadBD.cerrarConexion(con);
	    			
	    			if(!UtilidadCadena.noEsVacio(consecutivoCxCCapitacion) ||  consecutivoCxCCapitacion.equals("-1"))
	    			{
	    				logger.warn("no esta definido el tipo consecutivo consecutivoCxCCapitacionNoDisponible");
	    	    		errores.add("", new ActionMessage("error.capitacion.consecutivoCxCCapitacionNoDisponible"));
	    			}
	    		}
	    		else if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoCapitacionUnicoCartera+""))
	    		{
	    			Connection con=UtilidadBD.abrirConexion();
	    			String consecutivoCartera=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt());
	    			UtilidadBD.cerrarConexion(con);
	    				
	    			if(!UtilidadCadena.noEsVacio(consecutivoCartera) || consecutivoCartera.equals("-1"))
	    			{
	    				logger.warn("no esta definido el tipo consecutivo faltaConsecutivoCuentaCobro");
	    	    		errores.add("", new ActionMessage("error.capitacion.faltaConsecutivoCuentaCobro"));
	    			}
	    		}
	    		//se valida el parametro de topes
		    	if(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(usuario.getCodigoInstitucionInt()).trim().equals(""))
			    {
		    		logger.warn("no esta definido el tope consecutivo de cxc saldo inciial");
		    		errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "tope consecutivo saldo inicial capitación"));
		        }
	    		if(!errores.isEmpty())
		    	{
	    			saveErrors(req, errores);
		    		return map.findForward("paginaErroresActionErrors");
		    	}
	    	}
	    }
	 	return null;
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(RegistroSaldosInicialesCapitacionForm forma) 
	{
		forma.setEstado("continuarRegistroCuentaCobro");
		String fechaInicial=forma.getListadoMap("FECHA_INICIAL");
		String fechaFinal=forma.getListadoMap("FECHA_FINAL");
		String codigoConvenio= forma.getListadoMap("CODIGO_CONVENIO");
		int numReg=Integer.parseInt(forma.getListadoMap("numRegistros")==null?"0":forma.getListadoMap("numRegistros")+"");
		String[] indices={"seleccion_", "cargue_","codigocontrato_","numerocontrato_","fechainicialfinal_","fechainicialfinalbd_", "fechainicial_", "fechafinal_", "valortotal_", "valorajustes_","saldo_", "estabd_", "fueeliminada_", "puedoeliminar_", "fechacargue_"};
		forma.setListadoMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListadoMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoMap("numRegistros",numReg+"");
		forma.setListadoMap("FECHA_INICIAL", fechaInicial);
		forma.setListadoMap("FECHA_FINAL", fechaFinal);
		forma.setListadoMap("CODIGO_CONVENIO", codigoConvenio);
	}
	
	/**
	 * acti
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param valorChecks
	 * @param response
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private ActionForward accionActivarInactivarTodosLosChecks(	Connection con, 
																RegistroSaldosInicialesCapitacionForm forma, 
																ActionMapping mapping, 
																String valorChecks,
																HttpServletResponse response) throws SQLException, IOException
	{
		forma.setCheckSeleccionTodos(valorChecks);
		for(int w=0; w<=Integer.parseInt(forma.getListadoMap("numRegistros").toString()); w++)
		{
			forma.setListadoMap("seleccion_"+w, valorChecks);
		}
		UtilidadBD.cerrarConexion(con);
		forma.setEstado("continuarRegistroCuentaCobro");
		if(forma.getLinkSiguiente().equals(""))
			return mapping.findForward("paginaPrincipal");
		else
			response.sendRedirect(forma.getLinkSiguiente());
		return null;
	}
	
	
	/**
	 * accion ingresar nuevo cargue al mapa
	 * @param con
	 * @param forma
	 * @param request
	 * @param response
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private ActionForward accionIngresarNuevoCargueMap(	Connection con, 
														RegistroSaldosInicialesCapitacionForm forma, 
														HttpServletRequest request,
														HttpServletResponse response) throws SQLException, IOException
	{
		int numeroRegistros= Integer.parseInt(forma.getListadoMap("numRegistros"));
		forma.setListadoMap("seleccion_"+numeroRegistros, "si");
		forma.setListadoMap("cargue_"+numeroRegistros, "0");
		forma.setListadoMap("numerocontrato_"+numeroRegistros, "");
		forma.setListadoMap("fechainicialfinal_"+numeroRegistros, "");
		forma.setListadoMap("fechainicialfinalbd_"+numeroRegistros, "");
		forma.setListadoMap("fechainicial_"+numeroRegistros, "");
		forma.setListadoMap("fechafinal_"+numeroRegistros, "");
		forma.setListadoMap("valortotal_"+numeroRegistros, "");
		forma.setListadoMap("valorajustes_"+numeroRegistros, "0");
		forma.setListadoMap("saldo_"+numeroRegistros, "");
		forma.setListadoMap("estabd_"+numeroRegistros, "false");
		forma.setListadoMap("fueeliminada_"+numeroRegistros, "false");
		forma.setListadoMap("puedoeliminar_"+numeroRegistros, "true");
		forma.setListadoMap("fechacargue_"+numeroRegistros, "");
		forma.setListadoMap("numRegistros", (numeroRegistros+1)+"");
		forma.setEstado("continuarRegistroCuentaCobro");
		
		UtilidadBD.cerrarConexion(con);
		
		int maxpageItems=10;
		try
		{
			maxpageItems=Integer.parseInt(ValoresPorDefecto.getMaxPageItems(forma.getCodigoInstitucion()));
		}
		catch (Exception e) 
		{
			maxpageItems=10;
		}
		if(request.getParameter("ultimaPagina")==null)
	    {
	        if(numeroRegistros>=(forma.getOffsetHash()+maxpageItems))
		        forma.setOffsetHash(forma.getOffsetHash()+maxpageItems);
	        
	        
	        response.sendRedirect("registroSaldosInicialesCapitacion.jsp?pager.offset="+forma.getOffsetHash());
	    }
	    else
	    {    
		    String ultimaPagina=request.getParameter("ultimaPagina");
		    int posOffSet=0;
		    posOffSet=ultimaPagina.indexOf("offset=")+7;
		    forma.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
		    
		    if(numeroRegistros>=(forma.getOffsetHash()+maxpageItems))
		        forma.setOffsetHash(forma.getOffsetHash()+maxpageItems);
		    
		    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+forma.getOffsetHash());
	    }
	    return null;
	}
	
	/**
	 * accion ingresar nuevo cargue al mapa
	 * @param con
	 * @param forma
	 * @param request
	 * @param response
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private ActionForward accionEliminarCargueMap(	Connection con, 
													RegistroSaldosInicialesCapitacionForm forma, 
													HttpServletResponse response) throws SQLException, IOException
	{
		forma.setListadoMap("fueeliminada_"+forma.getIndex(), "true");
		forma.setListadoMap("valortotal_"+forma.getIndex(), "0");
		forma.setListadoMap("valorajustes_"+forma.getIndex(), "0");
		forma.setListadoMap("saldo_"+forma.getIndex(), "0");
		forma.setListadoMap("seleccion_"+forma.getIndex(), "no");
		forma.setListadoMap("cargue_"+forma.getIndex(), "0");
		
		forma.setListadoMap("numerocontrato_"+forma.getIndex(), "");
		forma.setListadoMap("fechainicialfinal_"+forma.getIndex(), "");
		forma.setListadoMap("fechainicialfinalbd_"+forma.getIndex(), "");
		forma.setListadoMap("fechainicial_"+forma.getIndex(), "");
		forma.setListadoMap("fechafinal_"+forma.getIndex(), "");
		forma.setListadoMap("estabd_"+forma.getIndex(), "false");
		forma.setListadoMap("puedoeliminar_"+forma.getIndex(), "false");
		forma.setListadoMap("fechacargue_"+forma.getIndex(), "");
		
		forma.setEstado("continuarRegistroCuentaCobro");
		response.sendRedirect("registroSaldosInicialesCapitacion.jsp?pager.offset="+forma.getOffsetHash());
		UtilidadBD.cerrarConexion(con);
		return null;
	}
	
	/**
	 * accion guardar
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(	RegistroSaldosInicialesCapitacionForm forma, 
											ActionMapping mapping, 
											Connection con,
											HttpServletRequest request,
											UsuarioBasico usuario) throws SQLException
	{
		//1.INICIALIZAMOS LA TRANSACCION
		UtilidadBD.iniciarTransaccion(con);
		
		//2 SE INSERTA LA CUENTA DE COBRO
		CuentaCobroCapitacion objetoCxC= new CuentaCobroCapitacion();
		//se obtiene el valor inicial de la cxc
		double valorInicialcartera=this.getValorInicialCxC(forma);
		int consecutivoCxCCapitacion=0;
		//String nombreConsecutivo="";
		
		//------------Se verifica que tipo de consecutivo de capitación se debe generar ------------------------//
		/*if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoCapitacionDiferenteCartera)
		{
			nombreConsecutivo=ConstantesBD.nombreConsecutivoCuentaCobroCapitacion;
		}
		else if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoCapitacionUnicoCartera)
		{
			nombreConsecutivo=ConstantesBD.nombreConsecutivoCuentasCobro;
		}
		
		//se bloquea el consecutivo
		ArrayList filtro=new ArrayList();
		filtro.add(nombreConsecutivo);
		filtro.add(usuario.getCodigoInstitucionInt());
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivosDisponibles,filtro);*/
		
		consecutivoCxCCapitacion= Integer.parseInt(forma.getCriteriosBusquedaMap("cuentaCobro"));
		
		if(CuentaCobroCapitacion.existeNumeroCxC(consecutivoCxCCapitacion+"", forma.getCodigoInstitucion()))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.yaExiste", "El Número de Cuenta Cobro "+consecutivoCxCCapitacion));
			logger.warn("error.ya existe consecutivo");
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
		}
		
		//Integer.parseInt(UtilidadBD.obtenerValorActualConsecutivo(con,nombreConsecutivo, usuario.getCodigoInstitucionInt()));
		//---------Se incrementa el consecutivo ---------------------//
		//UtilidadBD.incrementarConsecutivo(con,nombreConsecutivo,1,usuario.getCodigoInstitucionInt());
		
		int numeroCxCGenerado=objetoCxC.insertarCuentaCobroCapitacion(con, consecutivoCxCCapitacion, Integer.parseInt(forma.getListadoMap("CODIGO_CONVENIO")), ConstantesBD.codigoEstadoCarteraGenerado, usuario.getLoginUsuario(), valorInicialcartera, forma.getListadoMap("FECHA_INICIAL"), forma.getListadoMap("FECHA_FINAL"), forma.getCriteriosBusquedaMap("observaciones"), usuario.getCodigoInstitucionInt(), forma.getCriteriosBusquedaMap("fechaElaboracion"), UtilidadFecha.getHoraSegundosActual(), usuario.getCodigoCentroAtencion()+"");
		boolean existeDetalleCargues=false;
		
		if(valorInicialcartera<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("error.registroSaldoInicialCapitacion.cxcConValorCero"));
			logger.warn("error.registroSaldoInicialCapitacion.cxcConValorCero");
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
		}
		
		if(numeroCxCGenerado<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.ingresoDatos","la cuenta de cobro"));
			logger.warn("error en la insercion de la cuenta de cobro");
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
		}
		
		//3 SE INSERTAN TODOS LOS CARGUES INGRESADOS MANUALMENTE (estabd=false and fueeliminada=false and seleccion=si)
		int numeroRegistros= Integer.parseInt(forma.getListadoMap("numRegistros"));
		int numeroCargues=0;
		for (int w=0; w<numeroRegistros; w++)
		{
			//primero validamos los datos que no estan en bd y que no han sido eliminados y tienen seleccion
			if(	forma.getListadoMap("fueeliminada_"+w).equals("false") 
				&& forma.getListadoMap("estabd_"+w).equals("false")
				&& forma.getListadoMap("seleccion_"+w).equals("si"))
			{
				ContratoCargue.insertarContratoCargue(con, Integer.parseInt(forma.getListadoMap("codigocontrato_"+w)), forma.getListadoMap("fechainicial_"+w), forma.getListadoMap("fechafinal_"+w), 0, Double.parseDouble(forma.getListadoMap("valortotal_"+w)), 0, numeroCxCGenerado+"", usuario.getCodigoInstitucionInt(),"");
				existeDetalleCargues=true;
				numeroCargues++;
			}
		//4 SE ACTUALIZA EL CAMPO CUENTA DE COBRO DE LOS  CARGUES QUE YA ESTAN EN BD Y FUERON SELECCIONADOS
			if(	forma.getListadoMap("estabd_"+w).equals("true")
					&& forma.getListadoMap("seleccion_"+w).equals("si"))
			{
				int codigoCargueActualizado=objetoCxC.actualizarContratoCargue(con, Integer.parseInt(forma.getListadoMap("cargue_"+w)), numeroCxCGenerado, Double.parseDouble(forma.getListadoMap("valortotal_"+w)), usuario.getCodigoInstitucionInt());
				existeDetalleCargues=true;
				numeroCargues++;
				if(codigoCargueActualizado<=0)
				{
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("errors.ingresoDatos","la actualización del cargue con código: "+forma.getListadoMap("cargue_"+w)));
					logger.warn("error en la actualizacion del cargue con codigo: "+forma.getListadoMap("cargue_"+w));
					saveErrors(request, errores);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.cerrarConexion(con);									
					return mapping.findForward("paginaPrincipal");
				}
			}
		}
		
		//en caso de que no se inserte cargues entonces sacar error
		if(!existeDetalleCargues)
		{
			//error.registroSaldoInicialCapitacion.cxcConValorCero
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("error.registroSaldoInicialCapitacion.cxcSinCargue"));
			logger.warn("error.registroSaldoInicialCapitacion.cxcSinCargue");
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
		}
		
		UtilidadBD.finalizarTransaccion(con); 
		UtilidadBD.cerrarConexion(con);
		return this.accionResumen(forma, mapping, usuario, numeroCxCGenerado, numeroCargues);		
	}
	
	/**
	 * obtiene el valor inicial de la cxc
	 * @param forma
	 * @return
	 */
	private double getValorInicialCxC(RegistroSaldosInicialesCapitacionForm forma)
	{
		//FORMULA VALOR TOTAL ES LA SUMA DE TODOS LOS SALDOS CUENTA
		//		***********SALDO CUENTA= VALOR TOTAL + VALOR AJUSTES DEBITO - VALOR AJUSTES CREDITO
		
		double valorInicialcartera=0;
		int numeroRegistros= Integer.parseInt(forma.getListadoMap("numRegistros"));
		for(int w=0; w<numeroRegistros; w++)
		{
			if(	forma.getListadoMap("fueeliminada_"+w).equals("false") 
					&& forma.getListadoMap("estabd_"+w).equals("false")
					&& forma.getListadoMap("seleccion_"+w).equals("si"))
			{
				//SE CARGAN LOS QUE SE INGRESARON MANUALMENTE, EN ESTE PUNTO SABEMOS
				//QUE EL SALDO = VALOR TOTAL PORQUE NO EXISTEN AJUSTES
				valorInicialcartera+= Double.parseDouble(forma.getListadoMap("saldo_"+w));
			}
			if(	forma.getListadoMap("estabd_"+w).equals("true")
					&& forma.getListadoMap("seleccion_"+w).equals("si"))
			{
				//ESTOS SON LOS QUE VIENEN DE LA BD Y EL VALOR SALDO YA VIENE CALCULADO ENTONCES:
				valorInicialcartera+= Double.parseDouble(forma.getListadoMap("saldo_"+w));
			}
			
		}
		return valorInicialcartera;
	}
	
	/**
	 * accion resumen
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param numeroCxCGenerado
	 * @param numeroCargues
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	RegistroSaldosInicialesCapitacionForm forma, 
											ActionMapping mapping, 
											UsuarioBasico usuario,
											int numeroCxCGenerado,
											int numeroCargues) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		Connection con= UtilidadBD.abrirConexion();
		forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		
		CuentaCobroCapitacion objetoCxC= new CuentaCobroCapitacion();
		forma.setResumenMap(objetoCxC.consultarCuentaCobro(con, numeroCxCGenerado, usuario.getCodigoInstitucionInt(), -1));
		//le adicionamos el numero de cargues
		forma.setResumenMap("NUMERO_CARGUES", numeroCargues);
		
		//posterormente adiciono el detalle del mismo objeto cuenta cobro
		HashMap mapaDetalle= objetoCxC.consultarDetalleCuentaCobro(con, numeroCxCGenerado, usuario.getCodigoInstitucionInt());
		forma.setResumenMap("MAPA_DETALLE", mapaDetalle);
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaResumen");		
	}
	
}
