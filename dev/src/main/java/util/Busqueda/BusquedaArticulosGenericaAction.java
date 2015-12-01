/*
 * @(#)BusquedaArticulosAction.java
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

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.BusquedaArticulosGenerica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 *   Action, controla todas las opciones dentro de la busqueda de articulos 
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Dic 15, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class BusquedaArticulosGenericaAction extends Action
{
    /**
     * Objeto para manejar los logs de esta clase
     */
     private Logger logger = Logger.getLogger(BusquedaArticulosGenericaAction.class);
     
     public ActionForward execute(   ActionMapping mapping,
                                                     ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response ) throws Exception
                                                     {
    	 Connection con = null;
    	 
    	 try {
    		 if (response==null); //Para evitar que salga el warning
    		 if(form instanceof BusquedaArticulosGenericaForm)
    		 {
    			 try
    			 {
    				 con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    			 }
    			 catch(SQLException e)
    			 {
    				 logger.warn("No se pudo abrir la conexión"+e.toString());
    			 }
    			 PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			 BusquedaArticulosGenericaForm forma =(BusquedaArticulosGenericaForm)form;
    			 UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			 String estado=forma.getEstado();
    			 logger.warn("El estado en la BUSQUEDA GENERICA articulos  es------->"+estado);

    			 if(estado == null)
    			 {
    				 forma.reset(); 
    				 UtilidadBD.cerrarConexion(con);
    				 logger.warn("Estado no valido dentro del flujo de Busqueda de Articulos (null) ");
    				 request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				 return mapping.findForward("paginaError");
    			 }
    			 else if(estado.equals("busquedaAvanzada"))
    			 {
    				 forma.setCol(new ArrayList());
    				 return this.accionBusquedaAvanzadaArticulos(con, forma, mapping, usuario.getCodigoInstitucionInt(), paciente, request);
    			 }
    			 else if(estado.equals("ordenar"))
    			 {
    				 return this.accionOrdenar(forma, mapping, request, con);
    			 }
    			 else
    			 {
    				 forma.reset(); 
    				 UtilidadBD.cerrarConexion(con);
    				 logger.warn("Estado no valido dentro del flujo de Busqueda de Articulos (null) ");
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
      * Accion que realiza la busqueda de los articulos generica 
      * @param con
      * @param forma
      * @param paciente
      * @return
      * @throws SQLException
      */
     private ActionForward accionBusquedaAvanzadaArticulos(      Connection con, 
                                                                 BusquedaArticulosGenericaForm forma, 
                                                                 ActionMapping mapping,
                                                                 int codigoInstitucion,
                                                                 PersonaBasica paciente,
                                                                 HttpServletRequest request) throws SQLException, IPSException
     {
         BusquedaArticulosGenerica mundo= new BusquedaArticulosGenerica();
         Contrato objetoContrato= new Contrato();
         objetoContrato.cargar(con, paciente.getCodigoContrato()+"");
         logger.info("\n\n*******************EL VALOR DE CONTROL ESPECIAL ES---------->"+forma.getEsMedicamentoControlEspecial());
         if(forma.getEsMedicamentoControlEspecial()==null)
        	 forma.setEsMedicamentoControlEspecial("");
         if(forma.getParejasClaseGrupo() == null)
             forma.setParejasClaseGrupo("");
         forma.setCol(mundo.busquedaAvanzadaArticulos(con,forma.getEsBusquedaPorNombre(), forma.getCriterioBusqueda(), forma.getCodigosArticulosInsertados(), forma.getFiltrarXInventarios(), paciente.getCodigoContrato(),forma.getCodigoAlmacen(), forma.getCodigoTransaccion(), codigoInstitucion,forma.getParejasClaseGrupo(),  forma.getEsMedicamento(), forma.getEsPos() ,forma.getTipoConsignac(),forma.isFiltrarXSeccionSubseccion(),forma.getCodigosSecciones(),forma.getCodigoSubseccion(),forma.getCodigoSeccion(), forma.isFiltrarXClaseGrupoSub(), forma.getCodigoClase(),forma.getCodigoGrupo(),forma.getCodigoSubgrupo(), forma.isFiltrarXPrepPen(), forma.isFiltrarXAjusteInvFis(), forma.getTipoDispositivo(), forma.getTipoAccesoVascular(), forma.isSoloAlmacen(), forma.getCategoria(), forma.getFormaFarmaceutica(), forma.getUnidadMedida(), forma.isValDefClasesInv(), forma.getIdTercero(), forma.getEsMedicamentoControlEspecial(),forma.isCargarEquivalentes(),forma.isAplicaCargosDirectos(),forma.isAtencionOdontologica()));
         forma.reset();
         UtilidadBD.cerrarConexion(con);
         return mapping.findForward("listarArticulos");
     }
     
     /**
      * accion para ordenar los resultados de la busqueda de articulos
      * @param forma
      * @param mapping
      * @param request
      * @param con
      * @return
      * @throws SQLException
      */
     private ActionForward accionOrdenar(     BusquedaArticulosGenericaForm forma,
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
             logger.warn("Error en el listado de articulos ");
             UtilidadBD.cerrarConexion(con);
             ArrayList atributosError = new ArrayList();
             atributosError.add(" Listado articulos");
             request.setAttribute("codigoDescripcionError", "errors.invalid");               
             request.setAttribute("atributosError", atributosError);
             return mapping.findForward("paginaError");      
         }
         return mapping.findForward("listarArticulos")   ;
     }   
     
}
