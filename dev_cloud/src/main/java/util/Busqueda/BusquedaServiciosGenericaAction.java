/*
 * @(#)BusquedaServiciosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.Busqueda;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.BusquedaServiciosGenerica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 *   Action, controla todas las opciones dentro de la busqueda de servicios 
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Oct 31, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rï¿½os</a>
 */
public class BusquedaServiciosGenericaAction extends Action
{
    /**
    * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(BusquedaServiciosGenericaAction.class);
    
    public ActionForward execute(   ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response ) throws Exception
                                                    {
    	Connection con=null;

    	try {

    		if (response==null); //Para evitar que salga el warning
    		if(form instanceof BusquedaServiciosGenericaForm)
    		{

    			try
    			{
    				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    			}
    			catch(SQLException e)
    			{
    				logger.warn("No se pudo abrir la conexiï¿½n"+e.toString());
    			}

    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			BusquedaServiciosGenericaForm forma =(BusquedaServiciosGenericaForm)form;
    			String estado=forma.getEstado();
    			logger.warn("El estado en la BUSQUEDA GENERICA es------->"+estado);

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Busqueda de Servicios (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarBusqueda"))
    			{
    				forma.reset();
    				return this.accionEmpezarBusqueda(con, forma, mapping, usuario, request);
    			}
    			else if(estado.equals("busquedaFiltroNaturaleza"))
    			{
    				this.resetPorNaturalezaServicio(forma);
    				return this.accionEmpezarBusqueda(con, forma, mapping, usuario, request);
    			}
    			else if(estado.equals("busquedaAvanzada"))
    			{
    				return this.accionBusquedaAvanzadaServicios(con, forma, mapping, paciente, usuario);
    			}
    			else if(estado.equals("adicionarDeUna"))
    			{
    				return this.accionAdicionarDeUna(con, forma, paciente, mapping, usuario);
    			}
    			else if(estado.equals("adicionarDeUnaCodAxioma"))
    			{
    				return this.accionAdicionarDeUnaCasoCodAxioma(con, forma, paciente, mapping, usuario);
    			}

    			else if(estado.equals("ordenar"))
    			{
    				return this.accionOrdenar(forma, mapping, request, con);
    			}

    			else if(estado.equals("adicionarReportePresupuestoOdontologicos")){
    				return this.accionAdicionarReportePresupuestoOdontologicos(con, forma, paciente, mapping, usuario);

    			}
    			// Busqueda sin necesidad de mostrar fachada
    			else if(estado.equals("busquedaAvanzadaIntermedia"))
    			{
    				forma.setNombreForma(request.getParameter("nombreForma"));
    				if(request.getParameter("tipoAtencion")!=null)
    				{
    					forma.setTipoAtencion(request.getParameter("tipoAtencion"));
    				}

    				if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())))
    				{
    					forma.setTipoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));	
    				}

    				forma.setCodigoFormulario(Utilidades.convertirAEntero(request.getParameter("codigoFormulario")));
    				return this.accionBusquedaAvanzadaServicios(con, forma, mapping, paciente, usuario);

    			}
    			else if(estado.equals("busquedaAvanzadaPorDescripcion"))
    			{
    				return this.accionBuscarPorDescripcion(con, forma, paciente, mapping, usuario);

    			}else if(estado.equals("asignarValor"))
    			{
    				Log4JManager.info(forma.getCodigosServiciosInsertados());
    				return null;

    			}else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Busqueda de Servicios (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				return mapping.findForward("paginaError");
    			}
    		}   
    		return null;

    	} catch (Exception e) {
    		Log4JManager.error(e);
    		return null;
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
                                                    }        
    
    /**
     * Método que inicializa los atributos de la forma con los enviados
     * mediante request para comenzar la busqueda de servicios
     * @param con
     * @param forma
     * @param mapping
     * @param usuario
     * @param request
     * @return
     * @throws SQLException 
     */
    private ActionForward accionEmpezarBusqueda(Connection con, 
            BusquedaServiciosGenericaForm forma, 
            ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request ) throws SQLException 
    {
    	UtilidadBD.cerrarConexion(con);
        
        forma.setNombreForma(request.getParameter("nombreForma"));
        logger.info("\n\nnombre forma::: "+request.getParameter("tipoAtencion"));
        if(!UtilidadTexto.isEmpty(forma.getNombreForma()) && forma.getNombreForma().equals("formaUnidadConsulta"))
        	forma.setTipoAtencion(request.getParameter("tipoAtencion"));
                        
        if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())))
        {
        	forma.setTipoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));	
        }
        
        forma.setCodigoFormulario(Utilidades.convertirAEntero(request.getParameter("codigoFormulario")));
    	return mapping.findForward("listarServicios");
    }
    
    /**
     * Método que reinicia los atributos para la busqueda de los servicios
     * sin tomar en cuenta el atributo de filtrarNaturalezaServicio
     * @param forma
     */
    private void resetPorNaturalezaServicio(BusquedaServiciosGenericaForm forma)
    {
    	forma.setCodigo("");
        forma.setCodigoAxioma("");
        forma.setDescripcionServicio("");
        forma.setCodigoSexo(0);
        forma.setTipoTarifario("");    
        forma.setCodigoFormulario(ConstantesBD.codigoNuncaValido);
        forma.setTipoAtencion("");
        forma.setNombreForma("");
        forma.setCodigoPrograma("");	
    }
    
	/**
     * Accion que realiza la busqueda de los servicios generica 
     * @param con
     * @param forma
     * @param paciente
     * @return
     * @throws SQLException
     */
    private ActionForward accionBusquedaAvanzadaServicios(      Connection con, 
                                                                BusquedaServiciosGenericaForm forma, 
                                                                ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
    {
        BusquedaServiciosGenerica mundo= new BusquedaServiciosGenerica();
        
        logger.info("forma.getFiltrarTipoServicio() Action ->"+forma.getFiltrarTipoServicio()+"\n");
        
        forma.setCol(mundo.busquedaAvanzadaServiciosXCodigos(con, forma.getCodigo(), 
        														  forma.getDescripcionServicio(),  forma.getCodigoSexo(), forma.getCodigosServiciosInsertados(),
																  forma.getFiltrarTipoServicio(), paciente.getCodigoContrato(), forma.getTipoTarifario(), forma.isFiltrarNopos(), forma.getCodigoAxioma(), 
																  forma.getCodigoFormulario(), usuario, paciente.getCodigoIngreso(), paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(),forma.isAtencionOdontologica(),forma.getTipoAtencion(),forma.getNombreForma(),
																  forma.getCodigoPrograma(),forma.getFiltrarNaturalezaServicio()));
        forma.setEstado("listarServicios");
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listarServicios");
    }

    /**
     * accion para ordenar los resultados de la busqueda de servicios
     * @param forma
     * @param mapping
     * @param request
     * @param con
     * @return
     * @throws SQLException
     */
    private ActionForward accionOrdenar(     BusquedaServiciosGenericaForm forma,
                                                                ActionMapping mapping,
                                                                HttpServletRequest request, 
                                                                Connection con) throws SQLException
    {
        try
        {
            forma.setCol(Listado.ordenarColumna(new ArrayList(forma.getCol()),forma.getUltimaPropiedad(),forma.getColumna()));
            forma.setUltimaPropiedad(forma.getColumna());
            UtilidadBD.cerrarConexion(con);
        }
        catch(Exception e)
        {
            logger.warn("Error en el listado de servicios ");
            UtilidadBD.cerrarConexion(con);
            ArrayList atributosError = new ArrayList();
            atributosError.add(" Listado servicios");
            request.setAttribute("codigoDescripcionError", "errors.invalid");               
            request.setAttribute("atributosError", atributosError);
            return mapping.findForward("paginaError");      
        }
        forma.setEstado("listarServicios");
        return mapping.findForward("listarServicios")   ;
    }   
    
    
    /**
     * Accion que realiza la busqueda por el codigo Cups y trata de adicionarlo de una 
     * @param con
     * @param forma
     * @param paciente
     * @return
     * @throws SQLException :)
     */
    private ActionForward accionAdicionarDeUna(     Connection con, 
                                                                            BusquedaServiciosGenericaForm forma, 
                                                                            PersonaBasica paciente, 
                                                                            ActionMapping mapping,
                                                                            UsuarioBasico usuario) throws SQLException
    {
        BusquedaServiciosGenerica mundo= new BusquedaServiciosGenerica();
        forma.setDescripcionServicio("");
        
        forma.setTipoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
        if(UtilidadTexto.isEmpty(forma.getTipoTarifario()))
        {
        	forma.setTipoTarifario(ConstantesBD.codigoTarifarioCups+"");	
        }
        forma.setCol(mundo.busquedaAvanzadaServiciosXCodigos(con, forma.getCodigo(), 
            														  forma.getDescripcionServicio(), 
																	  forma.getCodigoSexo(), 
																	  forma.getCodigosServiciosInsertados(), 
																	  forma.getFiltrarTipoServicio(),
																	  paciente.getCodigoContrato(), 
																	  forma.getTipoTarifario(),
																	  forma.isFiltrarNopos(), 
																	  forma.getCodigoAxioma(),
																	  forma.getCodigoFormulario(),
																	  usuario,	
																	  paciente.getCodigoIngreso(),
																	  paciente.getCodigoUltimaViaIngreso(),
																	  paciente.getCodigoTipoPaciente(),
																	  forma.isAtencionOdontologica(),
																	  forma.getTipoAtencion(), 
																	  forma.getNombreForma(),
																	  forma.getCodigoPrograma(),
																	  forma.getFiltrarNaturalezaServicio()));        	
        
        forma.setEstado("busquedaAvanzada");
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listarServicios");
    }
    
    
    
    /**
     * Accion que realiza la busqueda por el codigo Cups y trata de adicionarlo de una 
     * @param con
     * @param forma
     * @param paciente
     * @return
     * @throws SQLException
     */
    private ActionForward accionAdicionarReportePresupuestoOdontologicos(     Connection con, 
                                                                            BusquedaServiciosGenericaForm forma, 
                                                                            PersonaBasica paciente, 
                                                                            ActionMapping mapping,
                                                                            UsuarioBasico usuario) throws SQLException
    {
        BusquedaServiciosGenerica mundo= new BusquedaServiciosGenerica();
        
        
        forma.setTipoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
        if(UtilidadTexto.isEmpty(forma.getTipoTarifario()))
        {
        	forma.setTipoTarifario(ConstantesBD.codigoTarifarioCups+"");	
        }
        forma.setCol(mundo.busquedaAvanzadaServiciosXCodigos(con, forma.getCodigo(), 
            														  forma.getDescripcionServicio(), 
																	  forma.getCodigoSexo(), 
																	  forma.getCodigosServiciosInsertados(), 
																	  forma.getFiltrarTipoServicio(),
																	  paciente.getCodigoContrato(), 
																	  forma.getTipoTarifario(),
																	  forma.isFiltrarNopos(), 
																	  forma.getCodigoAxioma(),
																	  forma.getCodigoFormulario(),
																	  usuario,	
																	  paciente.getCodigoIngreso(),
																	  paciente.getCodigoUltimaViaIngreso(),
																	  paciente.getCodigoTipoPaciente(),
																	  forma.isAtencionOdontologica(),
																	  forma.getTipoAtencion(), 
																	  forma.getNombreForma(),
																	  forma.getCodigoPrograma(),
																	  forma.getFiltrarNaturalezaServicio())); 
        
        
        forma.setEstado("busquedaAvanzada");
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listarDescripcion");
    }
    
    /**
     * Accion que realiza la busqueda por el codigo Cups y trata de adicionarlo de una 
     * @param con
     * @param forma
     * @param paciente
     * @return
     * @throws SQLException
     */
    private ActionForward accionAdicionarDeUnaCasoCodAxioma(     Connection con, 
                                                                            BusquedaServiciosGenericaForm forma, 
                                                                            PersonaBasica paciente, 
                                                                            ActionMapping mapping, UsuarioBasico usuario) throws SQLException
    {
        BusquedaServiciosGenerica mundo= new BusquedaServiciosGenerica();
        //forma.setCodigoAxioma("");
        forma.setDescripcionServicio("");
        
        forma.setCol(mundo.busquedaAvanzadaServiciosXCodigos(con, forma.getCodigo(), 
            														  forma.getDescripcionServicio(), 
																	  forma.getCodigoSexo(), 
																	  forma.getCodigosServiciosInsertados(), 
																	  forma.getFiltrarTipoServicio(),
																	  paciente.getCodigoContrato(), 
																	  forma.getTipoTarifario(),
																	  forma.isFiltrarNopos(), 
																	  forma.getCodigoAxioma(),
																	  forma.getCodigoFormulario(),
																	  usuario,
																	  paciente.getCodigoIngreso(),
																	  paciente.getCodigoUltimaViaIngreso(),
																	  paciente.getCodigoTipoPaciente(),
																	  forma.isAtencionOdontologica(),"","","",forma.getFiltrarNaturalezaServicio()));        	
        
        forma.setEstado("busquedaAvanzada");
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listarServicios");
    }   
    
    
    /**
     * Este mètodo se encarga de realizar la búsqueda avanzanda de servicios por descripción.
     *
     * @param con
     * @param forma
     * @param paciente
     * @param mapping
     * @param usuario
     * @return
     * @throws SQLException 
     *
     * @autor Yennifer Guerrero
     */
    private ActionForward accionBuscarPorDescripcion(Connection con,
			BusquedaServiciosGenericaForm forma, PersonaBasica paciente,
			ActionMapping mapping, UsuarioBasico usuario) throws SQLException {
		
    	BusquedaServiciosGenerica mundo= new BusquedaServiciosGenerica();
        forma.setCodigo("");
        
        forma.setTipoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
        if(UtilidadTexto.isEmpty(forma.getTipoTarifario()))
        {
        	forma.setTipoTarifario(ConstantesBD.codigoTarifarioCups+"");	
        }
        forma.setCol(mundo.busquedaAvanzadaServiciosXDescripcion(con, forma.getCodigo(), 
            														  forma.getDescripcionServicio(), 
																	  forma.getCodigoSexo(), 
																	  forma.getCodigosServiciosInsertados(), 
																	  forma.getFiltrarTipoServicio(),
																	  paciente.getCodigoContrato(), 
																	  forma.getTipoTarifario(),
																	  forma.isFiltrarNopos(), 
																	  forma.getCodigoAxioma(),
																	  forma.getCodigoFormulario(),
																	  usuario,	
																	  paciente.getCodigoIngreso(),
																	  paciente.getCodigoUltimaViaIngreso(),
																	  paciente.getCodigoTipoPaciente(),
																	  forma.isAtencionOdontologica(),
																	  forma.getTipoAtencion(), 
																	  forma.getNombreForma(),
																	  forma.getCodigoPrograma(),
																	  forma.getFiltrarNaturalezaServicio()));        	
        
        forma.setEstado("busquedaAvanzada");
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listarServicios");
	}
}