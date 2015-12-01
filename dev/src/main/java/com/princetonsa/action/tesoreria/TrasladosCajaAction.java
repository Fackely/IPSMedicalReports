/*
 * @(#)TrasladosCajaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.TrasladosCajaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.TrasladosCaja;
import com.princetonsa.pdf.TrasladosCajaPdf;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class TrasladosCajaAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TrasladosCajaAction.class);
    
    /**
	 * Método excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con=null;
    	try{

    		if (response==null); 
    		if(form instanceof TrasladosCajaForm)
    		{

    			try
    			{
    				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    			}
    			catch(SQLException e)
    			{
    				logger.warn("No se pudo abrir la conexión"+e.toString());
    			}
    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    			TrasladosCajaForm forma =(TrasladosCajaForm)form;
    			String estado=forma.getEstado();
    			logger.warn("\n\n\nEl estado en Traslados Caja es------->"+estado+"\n");

    			if(estado == null)
    			{
    				forma.reset(true); 
    				logger.warn("Estado no valido dentro del flujo de Traslados Caja (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{
    				return this.accionEmpezar(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("generar"))
    			{
    				return this.accionGenerar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return this.accionGuardar(forma, mapping, usuario, request);
    			}
    			else if(estado.equals("cerrarConexiones"))
    			{
    				return this.accionCerrarConexiones(forma, con);
    			}
    			else if(estado.equals("resumen"))
    			{
    				UtilidadBD.closeConnection(con);
    				return this.accionResumen(forma, mapping,usuario);
    			}
    			else if(estado.equals("consultar"))
    			{
    				return this.accionConsultar(con, forma, mapping, usuario);
    			}
    			else if(estado.equals("consultaConErrores"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("consultar");
    			}
    			else if(estado.equals("resultadoConsulta"))
    			{
    				return this.accionResultadoConsulta(con, forma, mapping, usuario);
    			}
    			else if(estado.equals("imprimir"))
    			{
    				String nombreArchivo;
    				Random r=new Random();
    				nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    				try
    				{
    					TrasladosCajaPdf.pdf(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, request);
    				}
    				catch (Exception e)
    				{
    					e.printStackTrace();
    					UtilidadBD.closeConnection(con);
    					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error de tamaño generando PDF", "errors.pdfSuperoMemoria", true);
    				}
    				UtilidadBD.cerrarConexion(con);
    				request.setAttribute("nombreArchivo", nombreArchivo);
    				request.setAttribute("nombreVentana", "Traslados Caja");
    				return mapping.findForward("abrirPdf");
    			}
    			else
    			{
    				forma.reset(true); 
    				logger.warn("Estado no valido dentro del flujo de Traslados Caja -> "+estado);
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
     * 
     * @param forma
     * @param mapping
     * @param con
     * @return
     */
	private ActionForward accionEmpezar(TrasladosCajaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionForward paginaSiguiente=null;
		forma.reset(true);
		forma.inicializarTags(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		paginaSiguiente=validacionesAcceso(forma, mapping, usuario, request);
		
		if(paginaSiguiente==null)
		{
			paginaSiguiente=mapping.findForward("principal");
		}	
		UtilidadBD.closeConnection(con);
		return paginaSiguiente;
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGenerar(TrasladosCajaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setFechaGeneracionTraslado(UtilidadFecha.getFechaActual());
		forma.setHoraGeneracionTraslado(UtilidadFecha.getHoraActual());
		forma.setUsuarioTraslada(usuario.getNombreUsuario());
		
		//SE INICIA EL BLOQUEO
		inicializarBLOQUEO(forma, usuario);
		
		forma.setTrasladoCajaMap(TrasladosCaja.busquedaCierresCajasParaTraslado(con, usuario.getLoginUsuario(), forma.getFechaTraslado(), forma.getCajaPpal(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(TrasladosCajaForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//EN ESTE PUNTO DEBE VENIR EL BLOQUEO INICIALIZADO
		boolean insertoBien=false;
		TrasladosCaja mundo= new TrasladosCaja();
		llenarMundo(forma, mundo);
		
		//LO PRIMERO ES HACER LA INSERCION BASICA DEL TRASLADO
		String consecutivoTrasladoCaja=mundo.insertarTrasladoCaja(forma.getConexionBLOQUEO(), usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
		
		if(consecutivoTrasladoCaja.equals(""))
		{
			//error
			ActionErrors errores= new ActionErrors();
			errores.add("", new ActionMessage ("errors.noSeGraboInformacion", "Traslado Caja"));
			logger.info("NO INSERTO EL TRASLADO DE CAJA BASICO");
			UtilidadBD.abortarTransaccion(forma.getConexionBLOQUEO());
			UtilidadBD.closeConnection(forma.getConexionBLOQUEO());
			saveErrors(request, errores);
			forma.reset(true);
	        return mapping.findForward("paginaErroresActionErrors");
		}
		
		//SE INSERTA EL DETALLE
		insertoBien=mundo.insertarDetalleTrasladoCaja(forma.getConexionBLOQUEO(), consecutivoTrasladoCaja, usuario.getCodigoInstitucionInt());
		if(!insertoBien)
		{
			//error
			ActionErrors errores= new ActionErrors();
			errores.add("", new ActionMessage ("errors.noSeGraboInformacion", "Traslado Caja"));
			logger.info("NO INSERTO EL detalle TRASLADO DE CAJA");
			UtilidadBD.abortarTransaccion(forma.getConexionBLOQUEO());
			UtilidadBD.closeConnection(forma.getConexionBLOQUEO());
			saveErrors(request, errores);
			forma.reset(true);
	        return mapping.findForward("paginaErroresActionErrors");
		}
		
		//SE ACTUALIZA LOS CIERRES DE CAJAS
		insertoBien=mundo.actualizarCierreCaja(forma.getConexionBLOQUEO(), consecutivoTrasladoCaja, usuario.getCodigoInstitucionInt());
		if(!insertoBien)
		{
			//error
			ActionErrors errores= new ActionErrors();
			errores.add("", new ActionMessage ("errors.noSeGraboInformacion", "Traslado Caja"));
			logger.info("NO ACTUALIZO EL CIERRE DE CAJA");
			UtilidadBD.abortarTransaccion(forma.getConexionBLOQUEO());
			UtilidadBD.closeConnection(forma.getConexionBLOQUEO());
			saveErrors(request, errores);
			forma.reset(true);
	        return mapping.findForward("paginaErroresActionErrors");
		}
		
		//si llega a este punto todo quedo bien y se termina la transaccion y el bloqueo
		this.finalizarBLOQUEO(forma);
		forma.setConsecutivoTraslado(consecutivoTrasladoCaja);
		//retornar el resumen
		logger.info("INSERTO 100% traslado caja");
		return accionResumen(forma, mapping, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionResumen(TrasladosCajaForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		Connection con= UtilidadBD.abrirConexion();
		forma.reset(false);
		forma.inicializarTags(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setEstado("resumen");
		String[] fechaHora= TrasladosCaja.obtenerFechaHoraUsuarioGeneracionTraslado(con, forma.getConsecutivoTraslado(), usuario.getCodigoInstitucionInt());
		forma.setFechaGeneracionTraslado(fechaHora[0]);
		forma.setHoraGeneracionTraslado(fechaHora[1]);
		forma.setUsuarioTraslada(fechaHora[2]);
		forma.setTrasladoCajaMap(TrasladosCaja.resumen(con, forma.getConsecutivoTraslado(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, TrasladosCajaForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(true);
		forma.inicializarTagsConsulta(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultar");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionResultadoConsulta(Connection con, TrasladosCajaForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setListadoMap(TrasladosCaja.listado(con, forma.getCriteriosBusquedaMap(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultar");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(TrasladosCajaForm forma, TrasladosCaja mundo) 
	{
		mundo.setCajaMayor(forma.getCajaMayor());
		mundo.setCajaPpal(forma.getCajaPpal());
		mundo.setFechaGeneracionTraslado(forma.getFechaGeneracionTraslado());
		mundo.setFechaTraslado(forma.getFechaTraslado());
		mundo.setHoraGeneracionTraslado(forma.getHoraGeneracionTraslado());
		mundo.setTrasladosCajaMap(forma.getTrasladoCajaMap());
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	protected void inicializarBLOQUEO(TrasladosCajaForm forma, UsuarioBasico usuario)
	{
		forma.setConexionBLOQUEO(UtilidadBD.abrirConexion());
		UtilidadBD.iniciarTransaccion(forma.getConexionBLOQUEO());
		ArrayList filtro=new ArrayList();
		filtro.add(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaTraslado()));
		filtro.add(usuario.getLoginUsuario());
		filtro.add(forma.getCajaPpal());
		filtro.add(usuario.getCodigoInstitucionInt());
		UtilidadBD.bloquearRegistro(forma.getConexionBLOQUEO(),BloqueosConcurrencia.bloquearCierresCajaParaTrasladoCaja,filtro);
	}
	
	/**
	 * 
	 * @param forma
	 */
	protected void finalizarBLOQUEO(TrasladosCajaForm forma)
	{
		UtilidadBD.finalizarTransaccion(forma.getConexionBLOQUEO());
		UtilidadBD.closeConnection(forma.getConexionBLOQUEO());
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 */
	protected ActionForward validacionesAcceso(TrasladosCajaForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		ActionErrors errores= new ActionErrors();
		if(Integer.parseInt(forma.getCajasPpalTagMap("numRegistros").toString())<1)
    	{
    		errores.add("", new ActionMessage("error.trasladosCaja.noTieneCaja","Principal"));
    	}
    	/*if(Integer.parseInt(forma.getCajasPpalTagMap("numRegistros").toString())<1)
    	{
    		errores.add("", new ActionMessage("error.trasladosCaja.noTieneCaja","Mayor"));
    	}*/
		
    		try
    	{
    		UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoTrasladosCaja, usuario.getCodigoInstitucionInt());
    		//Double.parseDouble(ConsecutivosDisponibles.getConsecutivo(usuario.getCodigoInstitucionInt(), ConstantesBD.nombreConsecutivoTrasladosCaja));
    		
    	}
    	catch(Exception e)
    	{
    		logger.info("error en el consecutivo de traslado caja -->"+e);
    		errores.add("", new ActionMessage("error.faltaDefinirConsecutivo","Traslados Cajas"));	
    	}	
    	if(!errores.isEmpty())
    	{	
	    	saveErrors(request, errores);
	        return mapping.findForward("paginaErroresActionErrors");
    	}
    	else
    	{
    		return null;
    	}
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @return
	 */ 
    private ActionForward accionCerrarConexiones( 	TrasladosCajaForm forma,Connection con) 
    {
    	if(con!=null)
    	{	
	    	try 
	    	{
				if(!con.isClosed())
					UtilidadBD.closeConnection(con);
			} 
	    	catch (SQLException e) 
			{
				e.printStackTrace();
			}
    	}	
    	if(forma.getConexionBLOQUEO()!=null)
    	{	
	    	try 
	    	{
				if(!forma.getConexionBLOQUEO().isClosed())
				{
					UtilidadBD.abortarTransaccion(forma.getConexionBLOQUEO());
					UtilidadBD.cerrarConexion(forma.getConexionBLOQUEO());
				}
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
    	}	
    	return null;
    }
	
}
