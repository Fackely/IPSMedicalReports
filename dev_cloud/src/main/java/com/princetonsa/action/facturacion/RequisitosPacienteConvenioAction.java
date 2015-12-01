/*
 * @(#)RequisitosPacienteConvenioAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.action.facturacion;

import java.sql.Connection;

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
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.RequisitosPacienteConvenioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.RequisitosPacientesXConvenio;


/**
 * Clase encargada del control de la funcionalidad 
 * Requisitos de Pacientes - Radicacion X Convenio
 *
 * @version 1.0 Nov 22, 2004
 */
public class RequisitosPacienteConvenioAction  extends Action
{

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
	private Logger logger = Logger.getLogger(RequisitosPacienteConvenioAction.class);
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
	{

		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		if( logger.isDebugEnabled() )
		{
			logger.debug("Entro al Action de Requisitos Paciente / Convenio");
		}
		if (form instanceof RequisitosPacienteConvenioForm)
		{
		    RequisitosPacienteConvenioForm requisitosPacienteConvenioForm=(RequisitosPacienteConvenioForm)form;
		    String estado=requisitosPacienteConvenioForm.getEstado();
		    logger.warn("Estado req convenio----------->"+estado+" accion--->"+requisitosPacienteConvenioForm.getAccionAFinalizar());
		    
			

			try
			{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//No se cierra conexión porque si llega aca ocurrió un
				//error al abrirla
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
		    
			//Lo primero que vamos a hacer es validar que se
			//cumplan las condiciones.
			HttpSession session=request.getSession();			
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

			if (estado==null||estado.equals(""))
			{
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (RequisitosPacienteConvenioAction)", "errors.estadoInvalido", true);
			}
			
			else if (estado.equals("inicioConsultar"))
			{
			    return this.accionInicioConsultar(mapping, con, requisitosPacienteConvenioForm);
			}
			else if (estado.equals("inicioModificar"))
			{
			    return this.accionInicioModificar(mapping, con, requisitosPacienteConvenioForm);
			}
			else if (estado.equals("modificar"))
			{
			    if (requisitosPacienteConvenioForm.getAccionAFinalizar()==null)
			    {
			        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (RequisitosPacienteConvenioAction)", "errors.estadoInvalido", true);
			    }
			    else if (requisitosPacienteConvenioForm.getAccionAFinalizar().equals("cambioConvenio"))
			    {
			        //Seleccionamos el nuevo convenio
			        seleccionarNuevoConvenio(con, requisitosPacienteConvenioForm, usuario);
			    }
			    else if (requisitosPacienteConvenioForm.getAccionAFinalizar().equals("agregarRequisito"))
			    {
			        requisitosPacienteConvenioForm.agregarElementoHashMap();
			    }
			    else if (requisitosPacienteConvenioForm.getAccionAFinalizar().equals("eliminarRequisito"))
			    {
			        requisitosPacienteConvenioForm.eliminarRequisito();
			    }
			    else if (requisitosPacienteConvenioForm.getAccionAFinalizar().equals("guardar"))
			    {
			        //Utilizamos la siguiente variable porque esta
			        //puede cambiar en caso que se haga reset
			        int codigoConvenioDebeGuardar=requisitosPacienteConvenioForm.getCodigoConvenioDeseoCambio();
			        
			        ActionForward forwardPosible=accionGuardar ( request,  mapping,  con,  requisitosPacienteConvenioForm, usuario);
			        if (forwardPosible!=null)
			        {
			            return forwardPosible;
			        }

				    if (codigoConvenioDebeGuardar!=ConstantesBD.codigoNuncaValido)
				    {
				        requisitosPacienteConvenioForm.setCodigoConvenioSeleccionado(codigoConvenioDebeGuardar);
				        this.seleccionarNuevoConvenio(con, requisitosPacienteConvenioForm, usuario);
				    }

			    }
			    requisitosPacienteConvenioForm.setAccionAFinalizar("");
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("modificar");
			}
			
			else if (estado.equals("consultar"))
			{
			    return this.accionResumen(mapping, requisitosPacienteConvenioForm, con);
			}
			else
			{
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion a finalizar no esta definida (RequisitosPacienteConvenioAction)", "errors.estadoInvalido", true);
			}

		}
		else
		{
			//No se cierra la conexión porque solo se abre si nos encontramos
			//con una forma válida
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
	 * Método que encapsula toda la funcionalidad necesaria
	 * para seleccionar un nuevo convenio. (Básicamente
	 * carga de datos del objeto del mundo)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param requisitosPacienteConvenioForm form para
	 * manejo de requisitos de paciente por convenio
	 * @param usuario usuario que esta trabajando en
	 * esta funcionalidad
	 * @throws Exception
	 */
	private void seleccionarNuevoConvenio ( Connection con, RequisitosPacienteConvenioForm requisitosPacienteConvenioForm, UsuarioBasico usuario)throws Exception 
	{
	    RequisitosPacientesXConvenio requerimientos=new RequisitosPacientesXConvenio();
	    requerimientos.getConvenio().setCodigo(requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado());
	    requisitosPacienteConvenioForm.limpiezaCambioConvenio();
	    
	    if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Paciente"))
	    {    
	        requerimientos.actualizarHashMapRequisitoPacienteConvenio(requisitosPacienteConvenioForm.obtenerMapa(), con);
	        requisitosPacienteConvenioForm.setNumRequisitosNoUsados(
            		requerimientos.actualizarHashMapRequisitosNoUtilizadosPorConvenio
            			(requisitosPacienteConvenioForm.obtenerMapa(), con, usuario.getCodigoInstitucion())
    				);
	        requisitosPacienteConvenioForm.setViasIngreso(Utilidades.obtenerViasIngreso(con, ""));
	    }
	    else if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Radicacion"))
	    {    
	        requerimientos.actualizarHashMapRequisitoRadicacionConvenio(requisitosPacienteConvenioForm.obtenerMapa(), con);
	        requisitosPacienteConvenioForm.setNumRequisitosNoUsados(
            		requerimientos.actualizarHashMapRequisitosRadicacionNoUtilizadosPorConvenio
            			(requisitosPacienteConvenioForm.obtenerMapa(), con, usuario.getCodigoInstitucion())
    				);
	    }
	}
	
	/**
	 * Método privado que se encarga de realizar toda la
	 * lógica requerida en caso que el usuario seleccione
	 * la inicioConsultar
	 * 
	 * @param mapping Mapa de recursos de struts
	 * @param con Conexión con la fuente de datos
	 * @param requisitosPacienteConvenioForm
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionInicioConsultar (ActionMapping mapping, Connection con, RequisitosPacienteConvenioForm requisitosPacienteConvenioForm) throws Exception
	{
	    UtilidadBD.cerrarConexion(con);
	    requisitosPacienteConvenioForm.reset();
	    requisitosPacienteConvenioForm.resetTipoRequisitos();
	    return mapping.findForward("consultar");
	}
	
	/**
	 * Método privado que se encarga de realizar toda la
	 * lógica requerida en caso que el usuario seleccione
	 * la inicioModificar
	 * 
	 * @param mapping Mapa de recursos de struts
	 * @param con Conexión con la fuente de datos
	 * @param requisitosPacienteConvenioForm Forma de este
	 * action
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionInicioModificar (ActionMapping mapping, Connection con, RequisitosPacienteConvenioForm requisitosPacienteConvenioForm)throws Exception 
	{
	    UtilidadBD.cerrarConexion(con);
	    requisitosPacienteConvenioForm.reset();
	    requisitosPacienteConvenioForm.resetTipoRequisitos();
	    requisitosPacienteConvenioForm.setEstado("modificar");
	    requisitosPacienteConvenioForm.setEsSoloConsulta(false);
	    return mapping.findForward("modificar");
	}

	/**
	 * Método privado que se encarga de realizar toda la
	 * lógica requerida para guardar inserciones y eliminaciones 
	 * en la fuente de datos
	 * 
	 * @param request Objeto de tipo request
	 * @param mapping Mapa de recursos de struts
	 * @param con Conexión con la fuente de datos
	 * @param requisitosPacienteConvenioForm
	 * @param usuario usuario que guarda este elemento
	 * @throws Exception
	 */
	private ActionForward accionGuardar (HttpServletRequest request, ActionMapping mapping, Connection con, RequisitosPacienteConvenioForm requisitosPacienteConvenioForm, UsuarioBasico usuario)throws Exception
	{
        RequisitosPacientesXConvenio requerimientos=new RequisitosPacientesXConvenio();
        requerimientos.getConvenio().setCodigo(requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado());
        String posibleLog="";
        int i;
        
        //Como no sabemos cuantos elementos va a tener
        //la transacción, la abrimos. Los casos de fallo
        //se manejan dentro de los métodos transaccionales
        
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        myFactory.beginTransaction(con);
        logger.info("numero de elemtos ha eliminar: "+requisitosPacienteConvenioForm.getNumElementosEliminados());
        for (i=0;i<requisitosPacienteConvenioForm.getNumElementosEliminados();i++)
        {
            requerimientos.getRequisito().setCodigo(Integer.parseInt((String)   requisitosPacienteConvenioForm.getUsoMapa("codigoRequisitosEliminado_"+i) ));
            
            
            if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Paciente"))
            {
            	requerimientos.getViaIngreso().setCodigo(Integer.parseInt((String)requisitosPacienteConvenioForm.getUsoMapa("viaIngresoEliminado_"+i)));
	            if (requerimientos.eliminarRequisitoPacienteConvenio(con)<=0)
	            {
	            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoNo);
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error eliminando requisito Cód. " + requerimientos.getRequisito().getCodigo() + " Conv " + requerimientos.getRequisito().getCodigo(), "errors.problemasBd", true);
	            }
	            else
	            {
	                //Si todo salió bien guardamos un log de la eliminación
	            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoSi);
	                posibleLog=
	                    "\n            ====ELEMENTO ELIMINADO REQUISITOS PACIENTE X CONVENIO===== " +
	                    "\n*  Código Requisito ["+requisitosPacienteConvenioForm.getUsoMapa("codigoRequisitosEliminado_"+i) + "]"+
	                    "\n*  Código Convenio ["+ requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado() + "]"+
	                    "\n*  Vía de ingreso ["+ Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(requisitosPacienteConvenioForm.getUsoMapa("viaIngresoEliminado_"+i)+"")) + "]"+
	            		"\n========================================================\n\n\n ";
	                
	                LogsAxioma.enviarLog(ConstantesBD.logRequisitosPacienteConvenioCodigo, 
	                        posibleLog,
	                		ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
	            }
	            logger.info("eliminacion requisitos paciente: "+requisitosPacienteConvenioForm.getProcesoRealizado());
            }
            else if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Radicacion"))
            {    
	            if (requerimientos.eliminarRequisitoRadicacionConvenio(con)<=0)
	            {
	            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoNo);
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error eliminando requisito Cód. " + requerimientos.getRequisito().getCodigo() + " Conv " + requerimientos.getRequisito().getCodigo(), "errors.problemasBd", true);
	            }
	            else
	            {
	                //Si todo salió bien guardamos un log de la eliminación
	            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoSi);
	                posibleLog=
	                    "\n            ====ELEMENTO ELIMINADO REQUISITOS RADICACION X CONVENIO===== " +
	                    "\n*  Código Requisito ["+requisitosPacienteConvenioForm.getUsoMapa("codigoRequisitosEliminado_"+i) + "]"+
	                    "\n*  Código Convenio ["+ requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado() + "]"+
	            		"\n========================================================\n\n\n ";
	                
	                LogsAxioma.enviarLog(ConstantesBD.logRequisitosRadicacionConvenioCodigo, 
	                        posibleLog,
	                		ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
	            }
            } 
        }
        
        for (i=0;i<requisitosPacienteConvenioForm.getNumElementosMemoria();i++)
        {
            if (((String)requisitosPacienteConvenioForm.getUsoMapa("vieneBD_"+i)).equals("f"))
            {
	            requerimientos.getRequisito().setCodigo(Integer.parseInt((String)requisitosPacienteConvenioForm.getUsoMapa("codigoRequisito_"+i)));
	            
	            
	            if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Paciente"))
	            {
	            	requerimientos.getViaIngreso().setCodigo(Integer.parseInt((String)requisitosPacienteConvenioForm.getUsoMapa("viaIngreso_"+i)));
		            if (requerimientos.insertarRequisitoPacienteConvenio(con)<=0)
		            {
		            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoNo);
		                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error insertando requisito Cód. " + requerimientos.getRequisito().getCodigo() + " Conv " + requerimientos.getRequisito().getCodigo(), "errors.problemasBd", true);
		            }
		            else
		            {
		                //Si todo salió bien guardamos un log de la inserción
		            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoSi);
		                posibleLog=
	                        "\n            ====ELEMENTO INSERTADO REQUISITOS PACIENTE X CONVENIO===== " +
	                        "\n*  Código Requisito ["+requisitosPacienteConvenioForm.getUsoMapa("codigoRequisito_"+i) + "]"+
	                        "\n*  Nombre Requisito ["+requisitosPacienteConvenioForm.getUsoMapa("requisito_"+i) + "]"+
	                        "\n*  Código Convenio ["+ requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado() + "]"+
	                        "\n*  Vía ingreso ["+Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(requisitosPacienteConvenioForm.getUsoMapa("viaIngreso_"+i)+"")) + "]"+
	                		"\n========================================================\n\n\n ";
		                LogsAxioma.enviarLog(ConstantesBD.logRequisitosPacienteConvenioCodigo, posibleLog, ConstantesBD.tipoRegistroLogInsercion, usuario.getInformacionGeneralPersonalSalud());
		            }
	            } 
	            else if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Radicacion"))
	            { 
	                if (requerimientos.insertarRequisitoRadicacionConvenio(con)<=0)
		            {
	                	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoNo);
		                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error insertando requisito Cód. " + requerimientos.getRequisito().getCodigo() + " Conv " + requerimientos.getRequisito().getCodigo(), "errors.problemasBd", true);
		            }
		            else
		            {
		                //Si todo salió bien guardamos un log de la inserción
		            	requisitosPacienteConvenioForm.setProcesoRealizado(ConstantesBD.acronimoSi);
		                posibleLog=
	                        "\n            ====ELEMENTO INSERTADO REQUISITOS RADICACION X CONVENIO===== " +
	                        "\n*  Código Requisito ["+requisitosPacienteConvenioForm.getUsoMapa("codigoRequisito_"+i) + "]"+
	                        "\n*  Nombre Requisito ["+requisitosPacienteConvenioForm.getUsoMapa("requisito_"+i) + "]"+
	                        "\n*  Código Convenio ["+ requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado() + "]"+
	                		"\n========================================================\n\n\n ";
		                LogsAxioma.enviarLog(ConstantesBD.logRequisitosRadicacionConvenioCodigo, posibleLog, ConstantesBD.tipoRegistroLogInsercion, usuario.getInformacionGeneralPersonalSalud());
		            }
	            }
            }
        }
        
        //Si llego a este punto sin errores finalizamos
        //la transacción
        myFactory.endTransaction(con);
        
        //Una vez terminada la transacción,
        //cargamos los datos de la fuente de
        //datos
        seleccionarNuevoConvenio(con, requisitosPacienteConvenioForm, usuario);
        
        //Si retornamos nulo es porque no 
        //debemos salirnos
        return null;
	}
	
	/**
	 * Método privado que se encarga de realizar toda la
	 * lógica requerida manejar el resumen
	 * 
	 * @param mapping Mapa de recursos de struts
	 * @param con Conexión con la fuente de datos
	 * @param requisitosPacienteConvenioForm
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionResumen(ActionMapping mapping, RequisitosPacienteConvenioForm requisitosPacienteConvenioForm, Connection con) throws Exception
	{
	    if (requisitosPacienteConvenioForm.getAccionAFinalizar()!=null&&requisitosPacienteConvenioForm.getAccionAFinalizar().equals("cambioConvenio"))
	    {
		    if (requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado()!=ConstantesBD.codigoNuncaValido)
		    {
			    RequisitosPacientesXConvenio requerimientos=new RequisitosPacientesXConvenio();
			    requerimientos.getConvenio().setCodigo(requisitosPacienteConvenioForm.getCodigoConvenioSeleccionado());
			    if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Paciente"))
			    {    
			        requisitosPacienteConvenioForm.setElementosPresentesBD(requerimientos.consultarRequisitoPacienteConvenioCollection(con));
			    }
			    else if(requisitosPacienteConvenioForm.getTipoRequisitos().equals("Radicacion"))
			    {    
			        requisitosPacienteConvenioForm.setElementosPresentesBD(requerimientos.consultarRequisitoRadicacionConvenioCollection(con));
			    }
		    }
		    requisitosPacienteConvenioForm.setAccionAFinalizar("");
	    }

	    UtilidadBD.cerrarConexion(con);
		return mapping.findForward("consultar");
	}
}
