/*
 * @(#)ArqueosAction.java
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
import java.util.HashMap;
import java.util.Vector;

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

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.ArqueosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.Arqueos;
import com.princetonsa.mundo.tesoreria.ConsultaRecibosCaja;
import com.princetonsa.mundo.tesoreria.RecibosCaja;
import com.princetonsa.mundo.tesoreria.RegistroDevolucionRecibosCaja;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 *   Action, controla todas las opciones dentro de los arqueos 
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Abr 19, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ArqueosAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ArqueosAction.class);
    
    /**
	 * Método excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
    {
    	
    	Connection conLocal=null;
    	try{
    	
    	if (response==null); 
    	if(form instanceof ArqueosForm)
    	{
    		
    		try
    		{
    			conLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    		}
    		catch(SQLException e)
    		{
    			logger.warn("No se pudo abrir la conexión"+e.toString());
    		}
    		UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    		ArqueosForm arqueosForm =(ArqueosForm)form;
    		String estado=arqueosForm.getEstado();
    		logger.warn("El estado en Arqueos es------->"+estado+"\n");

    		if(estado == null)
    		{
    			arqueosForm.reset(); 
    			logger.warn("Estado no valido dentro del flujo de Arqueos (null) ");
    			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(conLocal);
    			return mapping.findForward("paginaError");
    		}
    		
    		//primero va al estado empezar que hace un mapping a la funcionalidad de acceso comun
    		//comunCajerosCaja/comuncajerosCaja.jsp
    		else if (estado.equals("empezar"))
    		{
    			return this.accionEmpezar(arqueosForm,mapping, conLocal, usuario);
    		}
    		//en caso de que existena errores entonces redirecciona a la misma pagina (dependiendo del tipo de arqueo)pero con errores
    		else if(estado.equals("empezarConAvisoErrores"))
    		{
				UtilidadBD.cerrarConexion(conLocal);
    			return mapping.findForward("paginaPrincipal");
    		}
    		//estado comun de los arqueos para realizar la consulta de los recibos de caja que cumplan con los requerimientos del tipo de arqueo
    		else if(estado.equals("generarArqueo"))
    		{
    			if(arqueosForm.getConexionTransaccionalParaBloqueos()!=null)
    			{	
	    			if(!arqueosForm.getConexionTransaccionalParaBloqueos().isClosed())
	    	    	{
	    	    		UtilidadBD.abortarTransaccion(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	    		UtilidadBD.cerrarConexion(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	    	}
    			}	
    			
    			if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
    			{
    				ActionForward validacionesForward= this.validacionesArqueoDefinitivo(conLocal, arqueosForm, usuario, request, mapping);
    				if(validacionesForward!=null)
    					return validacionesForward;
    				return this.accionGenerarArqueoDefinitivoConBLOQUEO(conLocal, arqueosForm, mapping, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), request);
    			}	
    			else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional)
    				return this.accionGenerarArqueoProvisional(conLocal, arqueosForm, mapping, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
    			else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
    			{
    				ActionForward validacionesForward= this.validacionesCierreCaja(conLocal, arqueosForm, usuario, request, mapping);
    				if(validacionesForward!=null)
    					return validacionesForward;
    				return this.accionGenerarCierreCajaConBLOQUEO(conLocal, arqueosForm, mapping, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), request);
    			}	
    		}
    		//estado para el arqueo definitivo donde se pretende pasar a estado arqueado
    		else if(estado.equals("finalizarArqueoDefinitivo"))
    		{
    			return this.accionFinalizarArqueoDefinitivo(conLocal, arqueosForm, mapping, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), request);
    		}
    		//estado para el arqueo definitivo donde se pretende pasar a estado arqueado
    		else if(estado.equals("finalizarCierreCaja"))
    		{
    			return this.accionFinalizarCierreCaja(conLocal, arqueosForm, mapping, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), request);
    		}
    		
    		//estado comun para generar el reporte de los tipos de arqueo
    		else if(estado.equals("generarReporte"))
            {              
                this.generarReporte(conLocal, arqueosForm, usuario, request);
                UtilidadBD.cerrarConexion(conLocal);
                arqueosForm.setEstado("generarArqueo");
                if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
                	return mapping.findForward("paginaResumenCierreCaja");
                else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
                	return mapping.findForward("paginaResumenArqueosDefinitivo");
                else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional)
                	return mapping.findForward("paginaPrincipal");
            }
    		else if(estado.equals("empezarConsulta"))
    		{
    			return this.accionEmpezarConsulta(arqueosForm,mapping, conLocal, usuario);
    		}
    		else if(estado.equals("empezarConsultaConAvisoErrores"))
    		{
    			return this.accionEmpezarConsultaConAvisoErrores(mapping, conLocal);
    		}
    		else if(estado.equals("consultarArqueosCierres"))
    		{
    			return this.accionConsultarArqueosCierres(mapping, conLocal, arqueosForm, usuario.getCodigoInstitucionInt());
    		}
    		else if(estado.equals("ordenar"))
			{
				return this.accionOrdenar(arqueosForm,mapping,request,conLocal);
			}
    		else if(estado.equals("detalleConsulta"))
    		{
    			return this.accionDetalleConsulta(arqueosForm, mapping, conLocal, usuario);
    		}
    		else if(estado.equals("cerrarConexiones"))
    		{
    			return this.accionCerrarConexiones(arqueosForm, conLocal);
    		}
    		else
			{
    			arqueosForm.reset(); 
    			logger.warn("Estado no valido dentro del flujo de Arqueos -> "+estado);
    			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(conLocal);
    			return mapping.findForward("paginaError");
			}
    	}   
    	}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(conLocal);
		}
		return null;  
    }
    
    /**
     * Accion utilizada cuando se activa el centinela de salida pagina y cierra las conexiones 
     * @param arqueosForm
     * @param conLocal
     * @return
     * @throws SQLException
     */
    private ActionForward accionCerrarConexiones( 	ArqueosForm arqueosForm, 
										            Connection conLocal ) 
    {
    	if(conLocal!=null)
    	{	
	    	try 
	    	{
				if(!conLocal.isClosed())
					UtilidadBD.closeConnection(conLocal);
			} 
	    	catch (SQLException e) 
			{
				e.printStackTrace();
			}
    	}	
    	if(arqueosForm.getConexionTransaccionalParaBloqueos()!=null)
    	{	
	    	try 
	    	{
				if(!arqueosForm.getConexionTransaccionalParaBloqueos().isClosed())
				{
					UtilidadBD.abortarTransaccion(arqueosForm.getConexionTransaccionalParaBloqueos());
					UtilidadBD.cerrarConexion(arqueosForm.getConexionTransaccionalParaBloqueos());
				}
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
    	}	
    	return null;
    }
    
    /**
     * Este método especifica las acciones a realizar en el estado
     * empezar. primero va al estado empezar que hace un mapping a la funcionalidad de acceso comun
     * comunCajerosCaja/comuncajerosCaja.jsp
     * 
     * @param forma ArqueosForm
     *              para pre-llenar datos si es necesario
     * @param request HttpServletRequest para obtener los 
     *              datos
     * @param mapping Mapping para manejar la navegación
     * @param con Conexión con la fuente de datos
     * @return ActionForward a la página dependiendo del tipo de arqueo (atributo del form) 
     * 			ArqueoProvisional "arqueoProvisional.jsp"
     * 			ArqueoDefinitivo  "arqueoDefinitivo.jsp"
     * 			Cierre caja       "arqueoCierreCaja.jsp"
     * @throws SQLException
     */
    private ActionForward accionEmpezar(    ArqueosForm arqueosForm, 
                                            ActionMapping mapping,
                                            Connection conLocal,
                                            UsuarioBasico usuario
                                       ) throws SQLException
    {
        arqueosForm.reset();
        arqueosForm.inicializarTags(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
        //se postula la fecha actual
        arqueosForm.setFechaDDMMYYYY(UtilidadFecha.getFechaActual());
        UtilidadBD.cerrarConexion(conLocal);
        
        /*
         * La idea es que 
         * ArqueoProvisional "arqueoProvisional.jsp"
         * ArqueoDefinitivo  "arqueoDefinitivo.jsp"
         * Cierre caja       "arqueoCierreCaja.jsp"
         * */
        return mapping.findForward("paginaPrincipal");      
    }
    
    /**
     * estado de los arqueos para realizar la consulta de los recibos de caja 
     * que cumplan con los requerimientos del tipo de arqueo provisional
     * @param con
     * @param arqueosForm
     * @param mapping
     * @param codigoInstitucion
     * @return
     * @throws SQLException
     */
    private ActionForward accionGenerarArqueoProvisional(	Connection conLocal, 
												    		ArqueosForm arqueosForm, 
												            ActionMapping mapping,
												            int codigoInstitucion,
												            String loginUsuarioGenera
												         )throws SQLException
    {
    	arqueosForm.setRecibosCajaMap(this.retornarMapaRecibosCaja(conLocal, arqueosForm, codigoInstitucion, loginUsuarioGenera, false, false, false, "", ConstantesBD.codigoTipoArqueoProvisional));
    	UtilidadBD.cerrarConexion(conLocal);
    	// en este punto ya se ingreso a la seleccion del arqueo, entonces por seguridad 
    	// se ingresa al path que corresponda al tipo de arqueo seleccionado
    	return mapping.findForward("paginaArqueosProvisional");
    }
    
    /**
     * estado de los arqueos para realizar la consulta de los recibos de caja 
     * que cumplan con los requerimientos del tipo de arqueo cierre caja
     * @param con
     * @param arqueosForm
     * @param mapping
     * @param codigoInstitucion
     * @return
     * @throws SQLException
     */
    private ActionForward accionGenerarCierreCajaConBLOQUEO(	Connection conLocal, 
															    ArqueosForm arqueosForm, 
															    ActionMapping mapping,
															    int codigoInstitucion,
															    String loginUsuarioGenera,
															    HttpServletRequest request
															 )throws SQLException
    {
    	//SI EL TIPO DE ARQUEO ES CIERRE CAJA ENTONCES SE DEBE ABRIR LA TRANSACCION Y BLOQUEAR LA CONSULTA 
    	//PARA CONTROLAR LA CONCURRENCIA
    	//OJO ESTA TRANSACCION SE DEBE FINALIZAR 
    	UtilidadBD.cerrarConexion(conLocal);
    	
    	arqueosForm.setConexionTransaccionalParaBloqueos(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection());
    	UtilidadBD.iniciarTransaccion(arqueosForm.getConexionTransaccionalParaBloqueos());
    	arqueosForm.setRecibosCajaMap(this.retornarMapaRecibosCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), arqueosForm, codigoInstitucion, loginUsuarioGenera, true, false, false, "", ConstantesBD.codigoTipoArqueoCierreCaja));
    	
    	//ERROR DE CONCURRENCIA
    	if(arqueosForm.getRecibosCajaMap().containsKey("ERROR"))
    	{
    		this.accionCerrarConexiones(arqueosForm, conLocal);
    		ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("error.errorEnBlanco", "Existen recibos de caja/devoluciones que en este momento se están arqueando/cerrando desde otro equipo. Proceso Cancelado"));
	       	saveErrors(request, errores);
	       	return mapping.findForward("paginaErroresActionErrors");
    	}
    	
    	// en este punto ya se ingreso a la seleccion del arqueo, entonces por seguridad 
    	// se ingresa al path que corresponda al tipo de arqueo seleccionado
    	return mapping.findForward("paginaArqueosCierreCaja");
    }
    
    /**
     * Metodo inicial del arqueo definitivo que  hace la consulta de los recibos de caja y los bloquea 
     * para evitar problemas de concurrencia
     * @param con
     * @param arqueosForm
     * @param mapping
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @return
     * @throws SQLException
     */
    private ActionForward accionGenerarArqueoDefinitivoConBLOQUEO (	Connection conLocal, 
														    		ArqueosForm arqueosForm, 
														            ActionMapping mapping,
														            int codigoInstitucion,
														            String loginUsuarioGenera,
														            HttpServletRequest request)throws SQLException
    {
    	//SI EL TIPO DE ARQUEO ES DEFINITIVO ENTONCES SE DEBE ABRIR LA TRANSACCION Y BLOQUEAR LA CONSULTA 
    	//PARA CONTROLAR LA CONCURRENCIA
    	//OJO ESTA TRANSACCION SE DEBE FINALIZAR
    	UtilidadBD.cerrarConexion(conLocal);
    	
    	arqueosForm.setConexionTransaccionalParaBloqueos(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection());
    	UtilidadBD.iniciarTransaccion(arqueosForm.getConexionTransaccionalParaBloqueos());
    	arqueosForm.setRecibosCajaMap(this.retornarMapaRecibosCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), arqueosForm, codigoInstitucion, loginUsuarioGenera, true, false, false, "", ConstantesBD.codigoTipoArqueoDefinitivo));
    	
    	if(arqueosForm.getRecibosCajaMap().containsKey("ERROR"))
    	{
    		this.accionCerrarConexiones(arqueosForm, conLocal);
    		
    		ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("error.errorEnBlanco", "Existen recibos de caja/devoluciones que en este momento se están arqueando/cerrando desde otro equipo. Proceso Cancelado"));
	       	saveErrors(request, errores);
	       	return mapping.findForward("paginaErroresActionErrors");
    	}
    	
    	return mapping.findForward("paginaArqueosDefinitivo");
    }
    
    
    /**
     * metodo que retorna el mapa de los recibos de caja para ser utilizado en la forma o en mapas temporales
     * @param con
     * @param arqueosForm
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @param bloquearRegistro, LE COLOCA UN SELECT FOR UPDATE EN CASO DE QUE VENGA EN TRUE, TENGA 
     * EN CUENTA QUE DEBE VENIR CONNECTION EN UNA TRANSACCION CUANDO bloquearRegistro=TRUE
     * @param consecutivoArqueoOCierre. en caso de que no sea vacio "" entonces busca por el consecutivo arqueo
     * @param codigoTipoArqueo
     * @return
     */
    private HashMap retornarMapaRecibosCaja(	Connection con, 
		    									ArqueosForm arqueosForm,
		    									int codigoInstitucion,
		    									String loginUsuarioGenera,
		    									boolean bloqueoRegistro,
		    									boolean esResumenArqueosDefinitivos,
		    									boolean esResumenCierreCaja,
		    									String consecutivoArqueoOCierre,
		    									int codigoTipoArqueo
		    								)
    {
    	ConsultaRecibosCaja objetoConsultaReciboCaja= new ConsultaRecibosCaja();
    	String restriccionesSeparadosPorComas="";
    	if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional || arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
    		restriccionesSeparadosPorComas=ConstantesBD.codigoEstadoReciboCajaRecaudado+","+ConstantesBD.codigoEstadoReciboCajaAnulado;
    	else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
    		restriccionesSeparadosPorComas=ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+ConstantesBD.codigoEstadoReciboCajaAnulado;
    	//si es resumen de arqueos definitivos entonces toca es cargar los que estan anulados -arqueados
    	if(esResumenArqueosDefinitivos)
    		restriccionesSeparadosPorComas=ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+ConstantesBD.codigoEstadoReciboCajaAnulado+","+ConstantesBD.codigoEstadoReciboCajaEnCierre;
    	//si es resumen de arqueos definitivos entonces toca es cargar los que estan anulados -arqueados
    	if(esResumenCierreCaja)
    		restriccionesSeparadosPorComas=ConstantesBD.codigoEstadoReciboCajaEnCierre+","+ConstantesBD.codigoEstadoReciboCajaAnulado;
    	else
    	{	
    		//estos atributos ya estaban llenos desde el cargar entonces en el resumen no se deben cargar
	    	arqueosForm.setFechaGeneracionConsulta(UtilidadFecha.getFechaActual());
	    	//arqueosForm.setHoraGeneracionConsulta(UtilidadFecha.getHoraSegundosActual()); No se necesitan los segundos
	    	arqueosForm.setHoraGeneracionConsulta(UtilidadFecha.getHoraActual());
    	}	
    	return objetoConsultaReciboCaja.consultaRecibosCaja(con, codigoInstitucion, arqueosForm.getFechaDDMMYYYY(), arqueosForm.getLoginUsuarioCajero(), arqueosForm.getConsecutivoCaja(), restriccionesSeparadosPorComas, loginUsuarioGenera, arqueosForm.getFechaGeneracionConsulta(), arqueosForm.getHoraGeneracionConsulta(), bloqueoRegistro, consecutivoArqueoOCierre, codigoTipoArqueo, arqueosForm.getConsecutivoCajaPpal());
    }
    
    
    /**
     * Metodo para finalizar el arqueo definitivo, teniendo en cuenta que la CONEXION VIENE 
     * EN UNA TRANSACCION Y LOS REGISTROS CONSULTADOS VIENEN EN UN SELECT FOR UPDATE PARA EVITAR
     * PROBLEMAS DE CONCURRENCIA, POR OTRA PARTE TOCA VERIFICAR LA FINALIZACION DE ESTA CONEXION
     * @param conLocal
     * @param arqueosForm
     * @param mapping
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @return
     * @throws SQLException
     */
    private ActionForward accionFinalizarArqueoDefinitivo(	Connection conLocal, 
												    		ArqueosForm arqueosForm, 
												            ActionMapping mapping,
												            int codigoInstitucion,
			    											String loginUsuarioGenera,
			    											HttpServletRequest request
												         )throws SQLException
	{
    	boolean insertoExitosamente=false;
    	//1.0 ya teniendo los recibos de caja en la transaccion y el bloqueo entonces lo sgt ha realizar es la actualizacion de los estados
    	//a Arqueado para los que estan en estado Recaudado sin necesidad de hacer nuevamente las validaciones porque viene bloqueado desde 
    	//arriba
    	RecibosCaja mundoRecibosCaja= new RecibosCaja();
    	for(int w=0; w<Integer.parseInt(arqueosForm.getRecibosCajaMap("numRegistros").toString()); w++)
    	{
    		if(Integer.parseInt(arqueosForm.getRecibosCajaMap("codigoestado_"+w).toString())==ConstantesBD.codigoEstadoReciboCajaRecaudado)
    		{	
    			insertoExitosamente=mundoRecibosCaja.actualizarEstadoReciboCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), arqueosForm.getRecibosCajaMap("numerorecibo_"+w).toString(), codigoInstitucion, ConstantesBD.codigoEstadoReciboCajaEnArqueo);
    			if(!insertoExitosamente)
    			{
    				logger.error("error");
    				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
    		    	UtilidadBD.cerrarConexion(conLocal);
    		    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el estado de los recibos caja (ArqueosAction)", "error.arqueos.registroArqueos", true);
    			}
    		}
    	}
    	
    	//2.0 se obtiene el consecutivo de arqueo definitivo de caja y tambien le hago su respectivo bloqueo
    	//ConsecutivosDisponibles objetoConsecutivosDisponibles= new ConsecutivosDisponibles(); No se utiliza para nada (Consumo memoria)

    	
 	    String consecutivoAInsertar=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoArqueosEntregaParcial,codigoInstitucion);
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();

 	    insertoExitosamente= UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoArqueosEntregaParcial, codigoInstitucion, consecutivoAInsertar, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
	    if(!insertoExitosamente)
	    {
	    	logger.error("error");
	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	UtilidadBD.cerrarConexion(conLocal);
	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el consecutivo de arqueos definitivos (ArqueosAction)", "error.arqueos.registroConsecutivoArqueo", true);
	    }
    	
	    arqueosForm.setConsecutivoArqueo(consecutivoAInsertar);
	    
	    //3.0 se hace el insert del arqueo definitivo
	    Arqueos mundoArqueos= new Arqueos();
	    llenarMundo(arqueosForm, mundoArqueos);
	    insertoExitosamente=mundoArqueos.insertarArqueoDefinitivo(arqueosForm.getConexionTransaccionalParaBloqueos(), consecutivoAInsertar, codigoInstitucion, loginUsuarioGenera);
	    
	    if(!insertoExitosamente)
	    {
	    	logger.error("error");
	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	UtilidadBD.cerrarConexion(conLocal);
	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el ingresar el arqueo definitivo (ArqueosAction)", "error.arqueos.registroArqueos", true);
	    }
	    else
	    {
	    	//3.1 se actualiza la informacion (arqueos-cierre) de los recibos de caja
	    	for(int w=0; w<Integer.parseInt(arqueosForm.getRecibosCajaMap("numRegistros").toString()); w++)
	    	{
	    		insertoExitosamente= mundoArqueos.actualizarRecibosCajaCampoArqueoDefinitivo(arqueosForm.getConexionTransaccionalParaBloqueos(), consecutivoAInsertar, arqueosForm.getRecibosCajaMap("numerorecibo_"+w).toString(), codigoInstitucion);
	    		if(!insertoExitosamente)
	    	    {
	    			logger.error("error");
	    	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	    	UtilidadBD.cerrarConexion(conLocal);
	    	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el arqueo definitivo de la tabla recibos caja (ArqueosAction)", "error.arqueos.registroArqueos", true);
	    	    }
	    	}
	    }
	    
	    ///actualizo las devoluciones
	    ArrayList<Integer> arrayDevol= new ArrayList<Integer>();
	    for(int w=0; w<Utilidades.convertirAEntero(((HashMap) arqueosForm.getRecibosCajaMap("detalleDevoluciones")).get("numRegistros")+""); w++)
	    {
	    	arrayDevol.add(Utilidades.convertirAEntero(((HashMap) arqueosForm.getRecibosCajaMap("detalleDevoluciones")).get("codigo_"+w)+""));
	    }
	    if(arrayDevol.size()>0)
	    {	
	    	insertoExitosamente= RegistroDevolucionRecibosCaja.actualizarEstadoArqueoCierreDevol(arqueosForm.getConexionTransaccionalParaBloqueos(), consecutivoAInsertar, "" /*cierreCaja*/, arrayDevol);
	    	if(!insertoExitosamente)
    	    {
	    		logger.error("error");
    	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
    	    	UtilidadBD.cerrarConexion(conLocal);
    	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el arqueo definitivo de la tabla recibos caja (ArqueosAction)", "error.arqueos.registroArqueos", true);
    	    }
	    }	
	    
	    //nuevamente cargo la informacion actualizada en la forma para mostrar el resumen 
	    arqueosForm.setRecibosCajaMap(this.retornarMapaRecibosCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), arqueosForm, codigoInstitucion, loginUsuarioGenera, false, true, false, consecutivoAInsertar, ConstantesBD.codigoTipoArqueoDefinitivo));
	    
    	//cierro la transaccion
    	UtilidadBD.finalizarTransaccion(arqueosForm.getConexionTransaccionalParaBloqueos());
    	UtilidadBD.cerrarConexion(arqueosForm.getConexionTransaccionalParaBloqueos());
    	UtilidadBD.cerrarConexion(conLocal);
    	arqueosForm.setEstado("resumenArqueosDefinitivos");
    	return mapping.findForward("paginaResumenArqueosDefinitivo");
	}
    
    
    /**
     * Metodo para finalizar el cierre cuenta, teniendo en cuenta que la CONEXION VIENE 
     * EN UNA TRANSACCION Y LOS REGISTROS CONSULTADOS VIENEN EN UN SELECT FOR UPDATE PARA EVITAR
     * PROBLEMAS DE CONCURRENCIA, POR OTRA PARTE TOCA VERIFICAR LA FINALIZACION DE ESTA CONEXION
     * @param conLocal
     * @param arqueosForm
     * @param mapping
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @return
     * @throws SQLException
     */
    private ActionForward accionFinalizarCierreCaja	(		Connection conLocal, 
												    		ArqueosForm arqueosForm, 
												            ActionMapping mapping,
												            int codigoInstitucion,
			    											String loginUsuarioGenera,
			    											HttpServletRequest request
												         )throws SQLException
	{
    	boolean insertoExitosamente=false;
    	//1.0 ya teniendo los recibos de caja en la transaccion y el bloqueo entonces lo sgt ha realizar es la actualizacion de los estados
    	//a Cerrado para los que estan en estado Arqueado sin necesidad de hacer nuevamente las validaciones porque viene bloqueado desde 
    	//arriba
    	RecibosCaja mundoRecibosCaja= new RecibosCaja();
    	for(int w=0; w<Integer.parseInt(arqueosForm.getRecibosCajaMap("numRegistros").toString()); w++)
    	{
    		if(Integer.parseInt(arqueosForm.getRecibosCajaMap("codigoestado_"+w).toString())==ConstantesBD.codigoEstadoReciboCajaEnArqueo)
    		{	
    			insertoExitosamente=mundoRecibosCaja.actualizarEstadoReciboCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), arqueosForm.getRecibosCajaMap("numerorecibo_"+w).toString(), codigoInstitucion, ConstantesBD.codigoEstadoReciboCajaEnCierre);
    			if(!insertoExitosamente)
    			{
    				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
    		    	UtilidadBD.cerrarConexion(conLocal);
    		    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el estado de los recibos caja (ArqueosAction)", "error.arqueos.registroCierreCaja", true);
    			}
    		}
    	}
    	
    	//2.0 se obtiene el consecutivo de cierre de caja y tambien le hago su respectivo bloqueo
 	    String consecutivoAInsertar=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoCierreCaja,codigoInstitucion);
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();

 	    insertoExitosamente=UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoCierreCaja, codigoInstitucion, consecutivoAInsertar, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
	    if(!insertoExitosamente)
	    {
	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	UtilidadBD.cerrarConexion(conLocal);
	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el consecutivo de cierres caja (ArqueosAction)", "error.arqueos.registroConsecutivoCierreCaja", true);
	    }
    	
	    arqueosForm.setConsecutivoArqueo(consecutivoAInsertar);
	    
	    //3.0 se hace el insert del cierre
	    Arqueos mundoArqueos= new Arqueos();
	    llenarMundo(arqueosForm, mundoArqueos);
	    insertoExitosamente=mundoArqueos.insertarCierreCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), consecutivoAInsertar, codigoInstitucion, loginUsuarioGenera);
	    
	    if(!insertoExitosamente)
	    {
	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	UtilidadBD.cerrarConexion(conLocal);
	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el ingresar el cierre caja (ArqueosAction)", "error.arqueos.registroCierreCaja", true);
	    }
	    else
	    {
	    	//3.1 se actualiza la informacion (arqueos-cierre) de los recibos de caja
	    	for(int w=0; w<Integer.parseInt(arqueosForm.getRecibosCajaMap("numRegistros").toString()); w++)
	    	{
	    		insertoExitosamente= mundoArqueos.actualizarRecibosCajaCampoCierreCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), consecutivoAInsertar, arqueosForm.getRecibosCajaMap("numerorecibo_"+w).toString(), codigoInstitucion);
	    		if(!insertoExitosamente)
	    	    {
	    	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
	    	    	UtilidadBD.cerrarConexion(conLocal);
	    	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el cierre caja de la tabla recibos caja (ArqueosAction)", "error.arqueos.registroCierreCaja", true);
	    	    }
	    	}
	    }
	    
	    ///actualizo las devoluciones
	    ArrayList<Integer> arrayDevol= new ArrayList<Integer>();
	    for(int w=0; w<Utilidades.convertirAEntero(((HashMap) arqueosForm.getRecibosCajaMap("detalleDevoluciones")).get("numRegistros")+""); w++)
	    {
	    	arrayDevol.add(Utilidades.convertirAEntero(((HashMap) arqueosForm.getRecibosCajaMap("detalleDevoluciones")).get("codigo_"+w)+""));
	    }
	    if(arrayDevol.size()>0)
	    {	
	    	insertoExitosamente= RegistroDevolucionRecibosCaja.actualizarEstadoArqueoCierreDevol(arqueosForm.getConexionTransaccionalParaBloqueos(), ""/*arqueo*/, consecutivoAInsertar /*cierreCaja*/, arrayDevol);
	    	if(!insertoExitosamente)
    	    {
	    		logger.error("error");
    	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(arqueosForm.getConexionTransaccionalParaBloqueos());
    	    	UtilidadBD.cerrarConexion(conLocal);
    	    	return ComunAction.accionSalirCasoError(mapping, request, arqueosForm.getConexionTransaccionalParaBloqueos(), logger, "error en el actualizar el arqueo definitivo de la tabla recibos caja (ArqueosAction)", "error.arqueos.registroArqueos", true);
    	    }
	    }
	    
	    
	    //nuevamente cargo la informacion actualizada en la forma para mostrar el resumen 
	    arqueosForm.setRecibosCajaMap(this.retornarMapaRecibosCaja(arqueosForm.getConexionTransaccionalParaBloqueos(), arqueosForm, codigoInstitucion, loginUsuarioGenera, false, false, true, consecutivoAInsertar, ConstantesBD.codigoTipoArqueoCierreCaja));
	    
    	//cierro la transaccion
    	UtilidadBD.finalizarTransaccion(arqueosForm.getConexionTransaccionalParaBloqueos());
    	UtilidadBD.cerrarConexion(arqueosForm.getConexionTransaccionalParaBloqueos());
    	UtilidadBD.cerrarConexion(conLocal);
    	arqueosForm.setEstado("resumenCierreCaja");
    	return mapping.findForward("paginaResumenCierreCaja");
	}
    
    
    /**
     * metodo que llena los atributos del mundo con los que tiene actualmente la forma
     * @param arqueosForm
     * @param mundoArqueos
     */
    private void llenarMundo(ArqueosForm arqueosForm, Arqueos mundoArqueos)
    {
    	mundoArqueos.setFechaGeneracionArqueoDDMMYYYY(arqueosForm.getFechaGeneracionConsulta());
    	mundoArqueos.setHoraGeneracionArqueo(arqueosForm.getHoraGeneracionConsulta());
    	mundoArqueos.setConsecutivoCaja(arqueosForm.getConsecutivoCaja());
    	mundoArqueos.setLoginUsuarioCajero(arqueosForm.getLoginUsuarioCajero());
    	mundoArqueos.setFechaArqueadaDDMMYYYY(arqueosForm.getFechaDDMMYYYY());
    	mundoArqueos.setConsecutivoCajaPpal(arqueosForm.getConsecutivoCajaPpal());
    }
    
    /**
     * Metodo para generar los reportes, estado COMUN para generar el reporte de los tipos de arqueo, para que el reporte
     * funcione lo que se hace es modificar las consultas desde codigo java en birt para recibir los resultados esperados
     * en caso de que cambie es solo modificar
     * este metodo comparando con el codigoFuncionalidad.. del Form y direccionarlo a la nueva plantilla, por ultimo los
     * query se arman desde el SqlBase y se cargan en el mapa.
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
	private void generarReporte(Connection conLocal, 
								ArqueosForm arqueosForm,
								UsuarioBasico usuario,
								HttpServletRequest request) 
    {
		//segun el tipo de arqueo se toma el rptdesign maestro
		String nombreRptDesignMaestro="";
		if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
			nombreRptDesignMaestro="CierreCaja.rptdesign";
		else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
			nombreRptDesignMaestro="ArqueoDefinitivo.rptdesign";
		else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional)
			nombreRptDesignMaestro="ArqueoProvisional.rptdesign";
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE
        DesignEngineApi comp; 
        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/",nombreRptDesignMaestro);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,0, arqueosForm.getTituloDependiendoTipoArqueo());
        /*
        logger.info("SQL 1 / "+arqueosForm.getRecibosCajaMap("consultaPpalRecibosCaja"));
        logger.info("SQL 2 / "+arqueosForm.getRecibosCajaMap("consultaFormaPago"));
        HashMap encabezado= ((HashMap)arqueosForm.getRecibosCajaMap("encabezadoArqueo"));
        logger.info("SQL 3 / "+encabezado.get("consultaEncabezadoArqueos"));
        HashMap mapContadores= ((HashMap)arqueosForm.getRecibosCajaMap("mapContadores"));
        logger.info("SQL 4 / "+mapContadores.get("consultaNumeroRecibosCajaAnulados").toString());
        logger.info("SQL 5 / "+mapContadores.get("consultaDevolucionesAprobadas"));
        logger.info("SQL 6 / "+mapContadores.get("consultaDevolucionesAnuladas"));
        logger.info("SQL 7 / "+mapContadores.get("consultaDevolSumaAnuladosYAprobados"));
        logger.info("SQL 8 / "+arqueosForm.getRecibosCajaMap("devoluciones").toString());
        
        if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional)
        {
        	logger.info("SQL 9 / "+mapContadores.get("consultaNumeroRecibosCajaRecaudado"));
        	logger.info("SQL 10 / "+mapContadores.get("consultaSumaAnuladosYRecaudados").toString());
            
        }
        else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
        {
        	logger.info("SQL 9 / "+mapContadores.get("consultaNumeroRecibosCajaEnArqueo"));
        	logger.info("SQL 10 / "+mapContadores.get("consultaSumaAnuladosYEnArqueo").toString());
        }
        else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
        {
        	logger.info("SQL 9 / "+mapContadores.get("consultaNumeroRecibosCajaEnCierre"));
        	logger.info("SQL 10 / "+mapContadores.get("consultaSumaAnuladosYEnCierre").toString());
        }*/
        
        
        //SEGUNDO SE MODIFICA TODAAAAAAAAAA LA CONSULTA DE LA TABLA DE LOS RECIBOS CAJA
        comp.obtenerComponentesDataSet("listadoRecibosCaja");            
        String oldQuery=comp.obtenerQueryDataSet();
        String newQuery=arqueosForm.getRecibosCajaMap("consultaPpalRecibosCaja")+"";
        comp.modificarQueryDataSet(newQuery);
        
        //TERCERO SE MODIFICA LA CONSULTA DE LAS FORMAS DE PAGO
        comp.obtenerComponentesDataSet("detalleFormasPago");            
        oldQuery=comp.obtenerQueryDataSet();
        newQuery=arqueosForm.getRecibosCajaMap("consultaFormaPago").toString();
        comp.modificarQueryDataSet(newQuery);
        
        //CUARTO SE MODIFICA LA CONSULTA DEL ENCABEZADO DEL ARQUEO
        comp.obtenerComponentesDataSet("encabezadoArqueo");            
        oldQuery=comp.obtenerQueryDataSet();
        HashMap encabezado= ((HashMap)arqueosForm.getRecibosCajaMap("encabezadoArqueo"));
        newQuery= encabezado.get("consultaEncabezadoArqueos").toString();
        comp.modificarQueryDataSet(newQuery);
        
        
        //SE MODIFICA LOS CONTADORES DE LOS NUMEROS DE RECIBOS CAJA QUE ESTAN EN UN ESTADO DADO
        comp.obtenerComponentesDataSet("contadorAnulados");            
        oldQuery=comp.obtenerQueryDataSet();
        HashMap mapContadores= ((HashMap)arqueosForm.getRecibosCajaMap("mapContadores"));
        newQuery= mapContadores.get("consultaNumeroRecibosCajaAnulados").toString();
        comp.modificarQueryDataSet(newQuery);
        
        //SE MODIFICAN LOS CONTADORES DE LAS DEVOLUCIONES
        comp.obtenerComponentesDataSet("contadorDevolAprobada");            
        oldQuery=comp.obtenerQueryDataSet();
        newQuery= mapContadores.get("consultaDevolucionesAprobadas").toString();
        comp.modificarQueryDataSet(newQuery);
        
        comp.obtenerComponentesDataSet("contadorDevolAnulada");            
        oldQuery=comp.obtenerQueryDataSet();
        newQuery= mapContadores.get("consultaDevolucionesAnuladas").toString();
        comp.modificarQueryDataSet(newQuery);
        
        comp.obtenerComponentesDataSet("contadorDevolTotal");            
        oldQuery=comp.obtenerQueryDataSet();
        newQuery= mapContadores.get("consultaDevolSumaAnuladosYAprobados").toString();
        comp.modificarQueryDataSet(newQuery);
        
        comp.obtenerComponentesDataSet("devoluciones");            
        oldQuery=comp.obtenerQueryDataSet();
        newQuery= arqueosForm.getRecibosCajaMap("devoluciones").toString();
        comp.modificarQueryDataSet(newQuery);
        
        
        if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoProvisional)
        {
        	comp.obtenerComponentesDataSet("contadorRecaudados");            
            oldQuery=comp.obtenerQueryDataSet();
            newQuery= mapContadores.get("consultaNumeroRecibosCajaRecaudado").toString();
            comp.modificarQueryDataSet(newQuery);
            
            comp.obtenerComponentesDataSet("contadorTotal");            
            oldQuery=comp.obtenerQueryDataSet();
            newQuery= mapContadores.get("consultaSumaAnuladosYRecaudados").toString();
            comp.modificarQueryDataSet(newQuery);
            
        }
        else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
        {
        	comp.obtenerComponentesDataSet("contadorArqueados");            
            oldQuery=comp.obtenerQueryDataSet();
            newQuery= mapContadores.get("consultaNumeroRecibosCajaEnArqueo").toString();
            comp.modificarQueryDataSet(newQuery);
            
            comp.obtenerComponentesDataSet("contadorTotal");            
            oldQuery=comp.obtenerQueryDataSet();
            newQuery= mapContadores.get("consultaSumaAnuladosYEnArqueo").toString();
            comp.modificarQueryDataSet(newQuery);
        }
        else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
        {
        	comp.obtenerComponentesDataSet("contadorCerrados");            
            oldQuery=comp.obtenerQueryDataSet();
            newQuery= mapContadores.get("consultaNumeroRecibosCajaEnCierre").toString();
            comp.modificarQueryDataSet(newQuery);
            
            comp.obtenerComponentesDataSet("contadorTotal");            
            oldQuery=comp.obtenerQueryDataSet();
            newQuery= mapContadores.get("consultaSumaAnuladosYEnCierre").toString();
            comp.modificarQueryDataSet(newQuery);
        }
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
    }
    
	/**
	 * metodo que indica si existe o no el consecutivo de arqueo definitivo
	 * @param conLocal
	 * @return
	 */
	private boolean existeConsecutivoArqueoDefinitivo(Connection conLocal, int codigoInstitucion)
	{
		try
		{
			String consecutivoAInsertar=UtilidadBD.obtenerValorActualTablaConsecutivos(conLocal,ConstantesBD.nombreConsecutivoArqueosEntregaParcial,codigoInstitucion);
			if(consecutivoAInsertar.equals("") || consecutivoAInsertar.equals("-1"))
				return false;
			else
				return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * metodo que indica si existe o no el consecutivo de cierre caja
	 * @param conLocal
	 * @return
	 */
	private boolean existeConsecutivoCierreCaja(Connection conLocal, int codigoInstitucion)
	{
		try
		{
			String consecutivoAInsertar=UtilidadBD.obtenerValorActualTablaConsecutivos(conLocal,ConstantesBD.nombreConsecutivoCierreCaja,codigoInstitucion);
			if(consecutivoAInsertar.equals("") || consecutivoAInsertar.equals("-1"))
				return false;
			else
				return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * realiza las validaciones de acceso para poder generar el cierre de caja
	 * @param conLocal
	 * @param arqueosForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward validacionesCierreCaja(Connection conLocal, ArqueosForm arqueosForm, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws SQLException
	{
		Arqueos mundoArqueos= new Arqueos();
		//se valida la existencia del consecutivo
		if(!existeConsecutivoCierreCaja(conLocal, usuario.getCodigoInstitucionInt()))
		{
			logger.warn("no existe el consecutivo de cierre caja");
			request.setAttribute("codigoDescripcionError", "error.arqueos.consecutivoCierreCajaNoDisponible");
			UtilidadBD.cerrarConexion(conLocal);
			return mapping.findForward("paginaError");
		}
		//se evalua que no exista previamente el cierre caja
		else if(mundoArqueos.existeCierreCaja(conLocal, usuario.getCodigoInstitucionInt(), arqueosForm.getFechaDDMMYYYY(),  arqueosForm.getLoginUsuarioCajero(), arqueosForm.getConsecutivoCaja()))
		{
			ActionErrors errores=new ActionErrors();
            errores.add("error.arqueos.yaExisteCierreCaja", new ActionMessage("error.arqueos.yaExisteCierreCaja", arqueosForm.getLoginUsuarioCajero(), arqueosForm.getConsecutivoCaja(), arqueosForm.getFechaDDMMYYYY()));
            logger.warn("entra al error de [error.arqueos.yaExisteCierreCaja] ");
            saveErrors(request, errores);
            arqueosForm.setEstado("empezarConAvisoErrores");
            UtilidadBD.closeConnection(conLocal);
            return mapping.findForward("paginaPrincipal");
		}
		//se evalua que todos los recibos caja esten en estado arqueado
		String numerosRecibosSinArqueoDefinitivo=mundoArqueos.estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha(conLocal, usuario.getCodigoInstitucionInt(), arqueosForm.getFechaDDMMYYYY(), arqueosForm.getLoginUsuarioCajero(),  arqueosForm.getConsecutivoCaja());
		ArrayList<Integer> devolucionesSinArqueoDefinitivo= Arqueos.estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha(conLocal, usuario.getCodigoInstitucionInt(), arqueosForm.getFechaDDMMYYYY(), arqueosForm.getLoginUsuarioCajero(),  arqueosForm.getConsecutivoCaja());
		if(!numerosRecibosSinArqueoDefinitivo.equals("") || devolucionesSinArqueoDefinitivo.size()>0)
		{
			String mensajeError="";
			if(!numerosRecibosSinArqueoDefinitivo.equals(""))
			{
				mensajeError+="Los Recibos de caja ["+numerosRecibosSinArqueoDefinitivo+"] ";
			}
			if(devolucionesSinArqueoDefinitivo.size()>0)
			{
				mensajeError+="Las Devoluciones de Recibos de Caja ["+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(devolucionesSinArqueoDefinitivo)+"] ";
			}
			
			ActionErrors errores=new ActionErrors();
			//{0} no tienen arqueo definitivo. [co-006]
            errores.add("error.arqueos.existenRecibosCajaSinArqueoDefinitivo", new ActionMessage("error.arqueos.existenRecibosCajaSinArqueoDefinitivo", mensajeError));
            logger.warn("entra al error de [error.arqueos.existenRecibosCajaSinArqueoDefinitivo] ");
            saveErrors(request, errores);
            arqueosForm.setEstado("empezarConAvisoErrores");
            UtilidadBD.closeConnection(conLocal);
            return mapping.findForward("paginaPrincipal");
		}
		
		return null;
	}
	
	
	
	/**
	 * realiza las validaciones de acceso para poder generar el arqueo definitivo
	 * @param conLocal
	 * @param arqueosForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward validacionesArqueoDefinitivo(Connection conLocal, ArqueosForm arqueosForm, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws SQLException
	{
		Arqueos mundoArqueos= new Arqueos();
		//se valida la existencia del consecutivo
		if(!existeConsecutivoArqueoDefinitivo(conLocal, usuario.getCodigoInstitucionInt()))
		{
			logger.warn("no existe el consecutivo de arqueo definitivo");
			request.setAttribute("codigoDescripcionError", "error.arqueos.consecutivoArqueoDefinitivoNoDisponible");
			UtilidadBD.cerrarConexion(conLocal);
			return mapping.findForward("paginaError");
		}
		//se evalua que no exista previamente el cierre caja
		else if(mundoArqueos.existeCierreCaja(conLocal, usuario.getCodigoInstitucionInt(), arqueosForm.getFechaDDMMYYYY(),  arqueosForm.getLoginUsuarioCajero(), arqueosForm.getConsecutivoCaja()))
		{
			ActionErrors errores=new ActionErrors();
            errores.add("error.arqueos.yaExisteCierreCaja", new ActionMessage("error.arqueos.yaExisteCierreCaja", arqueosForm.getLoginUsuarioCajero(), arqueosForm.getConsecutivoCaja(), arqueosForm.getFechaDDMMYYYY()));
            logger.warn("entra al error de [error.arqueos.yaExisteCierreCaja] ");
            saveErrors(request, errores);
            arqueosForm.setEstado("empezarConAvisoErrores");
            UtilidadBD.closeConnection(conLocal);
            return mapping.findForward("paginaPrincipal");
		}
		return null;
	}
	
	
    /**
     * Este método especifica las acciones a realizar en el estado
     * empezarConsulta 
     * @param forma ArqueosForm
     *              para pre-llenar datos si es necesario
     * @param request HttpServletRequest para obtener los 
     *              datos
     * @param mapping Mapping para manejar la navegación
     * @param con Conexión con la fuente de datos
     * @return ActionForward a la página  
     * 			consultaArqueosCierres.jsp
     * @throws SQLException
     */
    private ActionForward accionEmpezarConsulta(    ArqueosForm arqueosForm, 
		                                            ActionMapping mapping,
		                                            Connection conLocal,
		                                            UsuarioBasico usuario
		                                       ) throws SQLException
    {
    	arqueosForm.resetConsultaArqueosCierres();
        arqueosForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
        UtilidadBD.cerrarConexion(conLocal);
        return mapping.findForward("paginaConsulta");      
    }
    
    /**
     * Este método especifica las acciones a realizar en el estado
     * empezarConsulta 
     *              para pre-llenar datos si es necesario
     * @param request HttpServletRequest para obtener los 
     *              datos
     * @param mapping Mapping para manejar la navegación
     * @param con Conexión con la fuente de datos
     * @return ActionForward a la página  
     * 			consultaArqueosCierres.jsp
     * @throws SQLException
     */
    private ActionForward accionEmpezarConsultaConAvisoErrores( ActionMapping mapping,
					                                            Connection conLocal 
					                                       		) throws SQLException
    {
        UtilidadBD.cerrarConexion(conLocal);
        return mapping.findForward("paginaConsulta");      
    }
	
    /**
     * realiza la busqueda de los arqueos y cierres dependiendo de los criterios de busqueda
     * @param mapping
     * @param conLocal
     * @param arqueosForm
     * @param codigoInstitucion
     * @return
     * @throws SQLException
     */
    private ActionForward accionConsultarArqueosCierres( 	ActionMapping mapping,
            												Connection conLocal,
            												ArqueosForm  arqueosForm,
            												int codigoInstitucion
            												) throws SQLException
    {
    	Arqueos mundoArqueos=new Arqueos();
    	
    	arqueosForm.setLinkVolver(armarLinkVolver(arqueosForm));
    	arqueosForm.setCol(mundoArqueos.busquedaArqueosCierres(conLocal, arqueosForm.getFechaInicialConsultaArqueosCierres(),
    																	arqueosForm.getFechaFinalConsultaArqueosCierres(), 
    																	arqueosForm.getLoginUsuarioCajero(), 
    																	arqueosForm.getConsecutivoCaja(), 
    																	arqueosForm.getCodigoTipoArqueoConsultaStr(), 
    																	codigoInstitucion, 
    																	arqueosForm.getCodigoCentroAtencion(), 
    																	arqueosForm.getConsecutivoCajaPpal()));
    	arqueosForm.setFechaGeneracionConsulta(UtilidadFecha.getFechaActual());
    	//arqueosForm.setHoraGeneracionConsulta(UtilidadFecha.getHoraSegundosActual()); No se necesitan los segundos
    	arqueosForm.setHoraGeneracionConsulta(UtilidadFecha.getHoraActual());
    	UtilidadBD.cerrarConexion(conLocal);
        return mapping.findForward("paginaConsulta"); 
    }
    
    /**
     * 
     * @param arqueosForm
     * @return
     */
	private String armarLinkVolver(ArqueosForm arqueosForm) 
	{
		String link="../consultaArqueosCierres/consultaArqueosCierres.do?estado=consultarArqueosCierres";
		link+="&fechaInicialConsultaArqueosCierres="+arqueosForm.getFechaInicialConsultaArqueosCierres();
		link+="&fechaFinalConsultaArqueosCierres="+arqueosForm.getFechaFinalConsultaArqueosCierres();
		link+="&loginUsuarioCajero="+arqueosForm.getLoginUsuarioCajero();
		link+="&codigoCentroAtencion="+arqueosForm.getCodigoCentroAtencion();
		link+="&consecutivoCaja="+arqueosForm.getConsecutivoCaja();
		link+="&codigoTipoArqueoConsultaStr="+arqueosForm.getCodigoTipoArqueoConsultaStr();
		link+="&consecutivoCajaPpal="+arqueosForm.getConsecutivoCajaPpal();
		return link;
	}

	/**
	 * Metodo que ordena las columnas 
	 * @param anulacionForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenar(	ArqueosForm arqueosForm,
											ActionMapping mapping,
											HttpServletRequest request, 
											Connection con) throws SQLException
    {
	    try
	    {
	        arqueosForm.setCol(Listado.ordenarColumna(new ArrayList(arqueosForm.getCol()),arqueosForm.getUltimaPropiedad(),arqueosForm.getColumna()));
	        arqueosForm.setUltimaPropiedad(arqueosForm.getColumna());
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error en la consulta de arqueos cierres ");
	        UtilidadBD.cerrarConexion(con);
	        arqueosForm.resetConsultaArqueosCierres();
	        ArrayList atributosError = new ArrayList();
	        atributosError.add("Consulta Arqueos Cierres");
	        request.setAttribute("codigoDescripcionError", "errors.invalid");				
	        request.setAttribute("atributosError", atributosError);
	        return mapping.findForward("paginaError");		
	    }
	    arqueosForm.setEstado("consultarArqueosCierres");
	    return mapping.findForward("paginaConsulta");
    }
	
	/**
	 * metodo que carga toda la pagina del detalle-resumen de un arqueo definitivo o un cierre caja
	 * @param arqueosForm
	 * @param mapping
	 * @param request
	 * @param conLocal
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionDetalleConsulta ( 	ArqueosForm arqueosForm,
													ActionMapping mapping,
													Connection conLocal,
													UsuarioBasico usuario
												) throws SQLException
	{
		boolean esResumenCierre=false;
		boolean esResumenArqueoDef=false;
		int codigoTipoArqueoTemp=-1;
		Arqueos mundoArqueos= new Arqueos();
		
		String pagina="";
    	if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo)
    	{	
    		mundoArqueos.resumenArqueoDefinitivo(conLocal, usuario.getCodigoInstitucionInt(), arqueosForm.getConsecutivoArqueo());
    		esResumenArqueoDef=true;
    		pagina="paginaResumenArqueosDefinitivo";
    		codigoTipoArqueoTemp=ConstantesBD.codigoTipoArqueoDefinitivo;
    	}
    	else if(arqueosForm.getCodigoFuncionalidadTipoArqueo()==ConstantesBD.codigoFuncionalidadTipoArqueoCierre)
    	{	
    		mundoArqueos.resumenCierreCaja(conLocal, usuario.getCodigoInstitucionInt(), arqueosForm.getConsecutivoArqueo() );
    		esResumenCierre=true;
    		pagina="paginaResumenCierreCaja";
    		codigoTipoArqueoTemp=ConstantesBD.codigoTipoArqueoCierreCaja;
    	}
    	arqueosForm.setRecibosCajaMap(this.retornarMapaRecibosCaja(conLocal, arqueosForm, usuario.getCodigoInstitucionInt(), mundoArqueos.getLoginUsuarioGenera(), false, esResumenArqueoDef, esResumenCierre, arqueosForm.getConsecutivoArqueo(), codigoTipoArqueoTemp ));
    	UtilidadBD.cerrarConexion(conLocal);
    	arqueosForm.setEstado("resumenArqueosDefinitivos");
    	return mapping.findForward(pagina);
  }
	
    
}