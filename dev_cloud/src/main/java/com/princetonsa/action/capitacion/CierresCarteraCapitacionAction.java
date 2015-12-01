/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
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

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.capitacion.CierresCarteraCapitacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.CierresCarteraCapitacion;

/**
 * Clase para manejar el workflow de cierre de 
 * saldos iniciales capitacion
 *
 * @version 1.0, 08/08/2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class CierresCarteraCapitacionAction extends Action 
{
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CierresCarteraCapitacionAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
	{
		Connection con=null;
		try{
	    if(form instanceof CierresCarteraCapitacionForm)
	    {	        
	        
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			
			CierresCarteraCapitacionForm forma = (CierresCarteraCapitacionForm) form;
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			String estado = forma.getEstado();
			logger.warn("[CierresCarteraCapitacionAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de CierresCarteraCapitacionAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			///********************WORKFLOW PARTE DEL CIERRE INICIAL ************************************//
			else if(estado.equals("empezarCierreInicial") )
			{			    
			    forma.reset();
			    boolean existeCierreInicial=UtilidadValidacion.existeCierreSaldosInicialesCapitacion(usuario.getCodigoInstitucionInt());
			    if(existeCierreInicial)
			    {			        
			        return this.accionCierreCarteraCapitacionResumen(con, forma, mapping, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCierreSaldoInicialStr);			        
			    }
			    else
			    {
				    if(ValoresPorDefecto.getFechaCorteSaldoInicialCCapitacion(usuario.getCodigoInstitucionInt()).trim().equals(""))
				    {
				        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "no esta definida la fecha de corte inicial", "error.noDefinidaFechaCorteSaldoInicial", true);   
				    }
				    else
				    {
				        return this.accionEmpezarCierreInicial(con, forma, mapping, usuario);
				    }
			    }
			}
			else if(estado.equals("guardarCierreInicial"))
			{
				//metodo que activa el centinela de la existencia del saldo inicial, en caso de que quede con "no" entonces
				//se muestra el popup de confimacion continuar proceso 
				//"No existen cuentas cobro radicadas por lo tanto no existe informacion para saldo inicial, se genera en CERO, Continua S/N"
            	if(!CierresCarteraCapitacion.existenCxCCapitadasRadicadasDadaFecha(forma.getYearCierre(), forma.getMesCierre(), usuario.getCodigoInstitucionInt()))
            	{
            		forma.setCentinelaExisteSaldoInicial("no");
            		UtilidadBD.cerrarConexion(con);
            		return mapping.findForward("paginaPrincipal");
            	}
            	else
            	{
            		return this.accionGuardarCierreInicial(con, forma, mapping, request, usuario);
            	}
            }
			else if(estado.equals("guardarCierreInicialSaldoEnCero"))
			{
				return this.accionGuardarCierreInicial(con, forma, mapping, request, usuario);
			}
			///********************WORKFLOW PARTE DEL CIERRE ANUAL ************************************//
			else if(estado.equals("empezarCierreAnual"))
			{
				forma.reset();
				return this.accionEmpezarCierreAnual(con, forma, mapping, usuario.getCodigoInstitucionInt());
			}
			else if(estado.equals("guardarCierreAnual"))
			{
				return this.accionGuardarCierreAnual(con, forma, mapping, request, usuario);
			}
			else
			{
				logger.warn("Estado no valido dentro del flujo de CierresCarteraCapitacionAction ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
	    else
		{
			logger.error("El form no es compatible con el form de CierresCarteraCapitacionForm");
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
	 * metodo que carga el resumen de un cierre inicial
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
    private ActionForward accionCierreCarteraCapitacionResumen(	Connection con, 
		            											CierresCarteraCapitacionForm forma, 
													            ActionMapping mapping,
													            int codigoInstitucion,
													            String tipoCierre) throws SQLException 
	{
    	CierresCarteraCapitacion mundo= new CierresCarteraCapitacion();
		mundo.reset();
        mundo.cargarResumenCierreCarteraCapitacion(con, codigoInstitucion, tipoCierre);
        llenarForm(forma,mundo, codigoInstitucion);
        UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaResumen");
   }
    
    /**
     * accion  para empezar el cierre inicial
     * @param con
     * @param forma
     * @param mapping
     * @param usuario
     * @return
     * @throws SQLException
     */
    private ActionForward accionEmpezarCierreInicial( 	Connection con,
    													CierresCarteraCapitacionForm forma,
    													ActionMapping mapping,
    													UsuarioBasico usuario) throws SQLException
    {
    	asignarFechaSaldoInicial(forma,usuario);
        UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaPrincipal");
    }
    
   
    /**
     * signa la fecha de saldo inicial
     * @param cierreForm
     * @param usuario
     */
	private void asignarFechaSaldoInicial(CierresCarteraCapitacionForm cierreForm,UsuarioBasico usuario)
	{
	    String fechaCorteSaldoInicial=ValoresPorDefecto.getFechaCorteSaldoInicialCCapitacion(usuario.getCodigoInstitucionInt()); 
	    int first=fechaCorteSaldoInicial.indexOf("/");
	    String mes = fechaCorteSaldoInicial.substring(0,first);
	    String year = fechaCorteSaldoInicial.substring(first+1);
	    cierreForm.setMesCierre(mes);
	    cierreForm.setYearCierre(year);
	}
	
	/**
     * metodo para copiar los datos del mundo al
     * form
     */
    private void llenarForm(CierresCarteraCapitacionForm forma,CierresCarteraCapitacion mundo, int codigoInstitucion)
    {
       forma.setYearCierre(mundo.getYear()); 
       forma.setMesCierre(mundo.getMes());
       forma.setObservaciones(mundo.getObservaciones());
       forma.setFechaGeneracion(mundo.getFechaGeneracion());
       forma.setHoraGeneracion(mundo.getHoraGeneracion());
       forma.setUsuario(mundo.getUsuario());
       forma.setCodigoInstitucion(codigoInstitucion);
    }
    
    /**
     * metodo para copiar los datos del form al mundo
     */
    private void llenarMundo(CierresCarteraCapitacionForm forma,CierresCarteraCapitacion mundo)
    {
       mundo.setYear(forma.getYearCierre()); 
       mundo.setMes(forma.getMesCierre());
       mundo.setObservaciones(forma.getObservaciones());
       mundo.setFechaGeneracion(forma.getFechaGeneracion());
       mundo.setHoraGeneracion(forma.getHoraGeneracion());
       mundo.setUsuario(forma.getUsuario());
    }
    
    
    /**
     * metodo que realiza todas las acciones para guardar el cierre de saldo inicial cartera capitacion
     * @param con
     * @param cierreForm
     * @param mapping
     * @param codigoInstitucion
     * @return
     * @throws SQLException
     */
    private ActionForward accionGuardarCierreInicial(	Connection con,
    													CierresCarteraCapitacionForm forma,
    													ActionMapping mapping,
    													HttpServletRequest request,
    													UsuarioBasico usuario) throws SQLException
    {
    	CierresCarteraCapitacion mundo= new CierresCarteraCapitacion();
    	boolean inserto=false;
    	forma.setUsuario(usuario.getLoginUsuario());
    	llenarMundo(forma, mundo);
    	
    	//1. se abre la transaccion 
    	UtilidadBD.iniciarTransaccion(con);
    	//2. se inserta la estructura basica
    	int codigoCierre=mundo.insertCierreCarteraCapitacion(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCierreSaldoInicialStr);
    	if(codigoCierre<1)
    	{
    		logger.warn("error no se inserto el cierre de cartera basico");
    		UtilidadBD.abortarTransaccion(con);
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la estructura basica (CierresCarteraCapitacionAction)", "error.cierresCarteraCapitacion.noInserto", true);
    	}
    	//3. se consultan los valores por convenio
    	HashMap mapa= mundo.getPagosAjustesValorCarteraXConvenio(con, usuario.getCodigoInstitucionInt());
    	if(!mapa.containsKey("numRegistros"))
    	{
    		logger.warn("error no se cargo los valores de los pagos - ajustes - valor cartera x convenio en el cierre de cartera");
    		UtilidadBD.abortarTransaccion(con);
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar (CierresCarteraCapitacionAction)", "error.cierresCarteraCapitacion.noInserto", true);
    	}
    	else
    	{
    		if(Integer.parseInt(mapa.get("numRegistros").toString())>0)
    		{
    			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
    			{
    				inserto= mundo.insertDetalleCierreCarteraXConvenio(	con, 
		    															codigoCierre+"", 
		    															Integer.parseInt(mapa.get("codigoconvenio_"+w).toString()),
		    															mapa.get("valorcartera_"+w).toString(),
		    															mapa.get("valorajustesdebito_"+w).toString(),
		    															mapa.get("valorajustescredito_"+w).toString(),
		    															mapa.get("valorpagos_"+w).toString());
    				if(!inserto)
    				{
    					logger.warn("error no se cargo los valores de los pagos - ajustes - valor cartera x convenio en el cierre de cartera");
    		    		UtilidadBD.abortarTransaccion(con);
    		    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar (CierresCarteraCapitacionAction)", "error.cierresCarteraCapitacion.noInserto", true);
    				}
    			}
    		}
    	}
    	//UtilidadBD.abortarTransaccion(con);
    	UtilidadBD.finalizarTransaccion(con);
    	return this.accionCierreCarteraCapitacionResumen(con, forma, mapping, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCierreSaldoInicialStr);
    }
    
    
    /**
     * accion  para empezar el cierre anual
     * @param con
     * @param forma
     * @param mapping
     * @param usuario
     * @return
     * @throws SQLException
     */
    private ActionForward accionEmpezarCierreAnual( 	Connection con,
    													CierresCarteraCapitacionForm forma,
    													ActionMapping mapping,
    													int codigoInstitucion
    											) throws SQLException
    {
    	asignarYear(forma, codigoInstitucion);
        UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaPrincipal");
    }
    
    /**
     * asigna la fecha de cierre
     * @param cierreForm
     * @param usuario
     */
	private void asignarYear(CierresCarteraCapitacionForm cierreForm, int codigoInstitucion)
	{
		String year = UtilidadFecha.getFechaActual().substring(6);
		int yearInt= Integer.parseInt(year)-1;
	    cierreForm.setMesCierre("13");
	    cierreForm.setYearCierre(yearInt+"");
	    cierreForm.setCodigoInstitucion(codigoInstitucion);
	}
    
	
	/**
     * metodo que realiza todas las acciones para guardar el cierre anual de cartera capitacion
     * @param con
     * @param cierreForm
     * @param mapping
     * @param codigoInstitucion
     * @return
     * @throws SQLException
     */
    private ActionForward accionGuardarCierreAnual(		Connection con,
    													CierresCarteraCapitacionForm forma,
    													ActionMapping mapping,
    													HttpServletRequest request,
    													UsuarioBasico usuario) throws SQLException
    {
    	CierresCarteraCapitacion mundo= new CierresCarteraCapitacion();
    	boolean inserto=false;
    	forma.setUsuario(usuario.getLoginUsuario());
    	llenarMundo(forma, mundo);
    	
    	//1. se abre la transaccion 
    	UtilidadBD.iniciarTransaccion(con);
    	//2. se inserta la estructura basica
    	int codigoCierre=mundo.insertCierreCarteraCapitacion(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCierreAnualStr);
    	if(codigoCierre<1)
    	{
    		logger.warn("error no se inserto el cierre anual basico");
    		UtilidadBD.abortarTransaccion(con);
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la estructura basica (CierresCarteraCapitacionAction)", "error.cierresCarteraCapitacion.noInserto", true);
    	}
    	//3. se consultan los valores por convenio
    	
    	HashMap mapa= CierresCarteraCapitacion.getPagosAjustesValorCarteraXConvenioCierreAnual(usuario.getCodigoInstitucionInt(), forma.getYearCierre());
    	if(!mapa.containsKey("numRegistros"))
    	{
    		logger.warn("error no se cargo los valores de los pagos - ajustes - valor cartera x convenio en el cierre anual");
    		UtilidadBD.abortarTransaccion(con);
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar (CierresCarteraCapitacionAction)", "error.cierresCarteraCapitacion.noInserto", true);
    	}
    	else
    	{
    		if(Integer.parseInt(mapa.get("numRegistros").toString())>0)
    		{
    			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
    			{
    				inserto= mundo.insertDetalleCierreCarteraXConvenio(	con, 
		    															codigoCierre+"", 
		    															Integer.parseInt(mapa.get("codigoconvenio_"+w).toString()),
		    															mapa.get("valorcartera_"+w).toString(),
		    															mapa.get("valorajustesdebito_"+w).toString(),
		    															mapa.get("valorajustescredito_"+w).toString(),
		    															mapa.get("valorpagos_"+w).toString());
    				if(!inserto)
    				{
    					logger.warn("error no se cargo los valores de los pagos - ajustes - valor cartera x convenio en el cierre de cartera anual");
    		    		UtilidadBD.abortarTransaccion(con);
    		    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar (CierresCarteraCapitacionAction)", "error.cierresCarteraCapitacion.noInserto", true);
    				}
    			}
    		}
    	}
    	//UtilidadBD.abortarTransaccion(con);
    	UtilidadBD.finalizarTransaccion(con);
    	return this.accionCierreCarteraCapitacionResumen(con, forma, mapping, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCierreAnualStr);
    }
    
}