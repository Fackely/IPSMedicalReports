/*
 * @(#)ConsultaPresupuestoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.presupuesto;

import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import com.princetonsa.dao.DaoFactory;
import org.apache.struts.action.Action;
import com.princetonsa.mundo.UsuarioBasico;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.mundo.presupuesto.ConsultaPresupuesto;
import com.princetonsa.mundo.presupuesto.FormatoImpresionPresupuesto;
import com.princetonsa.pdf.PresupuestoPdf;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.presupuesto.ConsultaPresupuestoForm;

/**
 * Clase encargada del control de la funcionalidad de Consulta/Impresión de Presupuesto

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 17 /Ene/ 2006
 */
public class ConsultaPresupuestoAction extends Action 
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultaPresupuestoAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof ConsultaPresupuestoForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session = request.getSession();
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				ConsultaPresupuestoForm forma = (ConsultaPresupuestoForm)form;
				ConsultaPresupuesto mundo = new ConsultaPresupuesto();
				String estado = forma.getEstado();
				logger.warn("[ConsultaPresupuestoAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultasPresupuestoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					if(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(usuario.getCodigoInstitucionInt(), true).equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "FaltaParamOcupacionGral ","error.manejoPaciente.faltaParOcupacionMedicoGeneral", true) ;
					}
					else if(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(usuario.getCodigoInstitucionInt(), true).equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "FaltaParamOcupacionGral ","error.manejoPaciente.faltaParOcupacionMedicoEspecialista", true) ;
					}
					else
					{
						forma.setMapaMedicos(mundo.consultarMedicos(con,Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(usuario.getCodigoInstitucionInt(), true)),Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(usuario.getCodigoInstitucionInt(), true))));
						forma.setMapaTipoId(mundo.consultarTiposId(con));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("inicioBusqueda");
					}
				}
				else if(estado.equals("resultadoBusqueda"))
				{
					return this.accionBusquedaAvanzada(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("detallePresupuesto"))
				{
					return this.accionDetallePresupuesto(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, mapping);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("imprimir"))
				{
					return this.accionImprimir(mapping, request, con, forma, usuario);
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de Consulta de Presupuesto");
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
		 * Action para permitir el ordenamiento de las columnas en el momenot en el que se encuentran
		 * algunos presupuestos despues de la busqueda
		 * @param con
		 * @param forma
		 * @param response
		 * @return
		 */
	    private ActionForward accionOrdenarColumna(Connection con, ConsultaPresupuestoForm forma, ActionMapping mapping) 
	    {
	        String[] indices={
					            "consecutivo_", 
					            "fechapresupuesto_", 
					            "tiponumeroid_", 
					            "paciente_",
					            "convenio_",
								"medico_",
								"nombrecentroatencion_"
		            		};
	        int tmp=Integer.parseInt(forma.getMapaPresupuestos("numRegistros")+"");
	        forma.setMapaPresupuestos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPresupuestos(),Integer.parseInt(forma.getMapaPresupuestos("numRegistros")+"")));
	        forma.setUltimoPatron(forma.getPatronOrdenar());
	        forma.setMapaPresupuestos("numRegistros",tmp+"");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("busquedaAvanzada");
	    }
	    
	    
	    /**
	     * Action para el detalle de un presupuesto dado su codigo.
	     * @param forma
	     * @param mapping
	     * @param con
	     * @return
	     * @throws SQLException
	     */
		private ActionForward accionDetallePresupuesto(ConsultaPresupuestoForm forma, ActionMapping mapping, Connection con, ConsultaPresupuesto mundo, UsuarioBasico usuario) throws SQLException
		{
			int posicion=forma.getPosicionMapa();
			forma.setCodigoPresupuesto(Integer.parseInt(forma.getMapaPresupuestos("consecutivo_"+posicion)+""));
			forma.setTotalPresupuesto(Utilidades.obtenerTotalPresupuesto(con, forma.getCodigoPresupuesto()));
			forma.setMapaDetallePresupuesto(mundo.consultarDetallePresupuesto(con, forma.getCodigoPresupuesto()));
			forma.setMapaArticulos(mundo.consultarArticulos(con, forma.getCodigoPresupuesto()));
			forma.setMapaServicios(mundo.consultarServicios(con, forma.getCodigoPresupuesto()));
			forma.setMapaIntervenciones(mundo.consultarIntenervenciones(con, forma.getCodigoPresupuesto()));
			
			/**Cargamos los formato de impresion existentes**/
			FormatoImpresionPresupuesto mundoAux = new FormatoImpresionPresupuesto();
			forma.setMapaFormatosImpresion(mundoAux.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detallePresupuesto");		
		}
		
		
		/**
		 * Action para la busqueda avanzada de un presupuesto dado unos criterios de busqueda
		 * @param forma
		 * @param mapping
		 * @param con
		 * @param mundo
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionBusquedaAvanzada (ConsultaPresupuestoForm forma, ActionMapping mapping,  Connection con, ConsultaPresupuesto mundo, UsuarioBasico usuario) throws SQLException
		{	
			forma.setMapaPresupuestos(mundo.busquedaPresupuestos(con,forma.getPresupuestoInicial(), forma.getPresupuestoFinal(), forma.getFechaElaboracionInicial(), forma.getFechaElaboracionFinal(), forma.getTipoId(), forma.getNumeroId(),forma.getCodigoMedico(), forma.getPropiedadTempResponsable()));
			int numResultados=Integer.parseInt(forma.getMapaPresupuestos("numRegistros").toString());
			if(numResultados==1)
			{
				forma.setCodigoPresupuesto(Integer.parseInt(forma.getMapaPresupuestos("consecutivo_0")+""));
				forma.setTotalPresupuesto(Utilidades.obtenerTotalPresupuesto(con, forma.getCodigoPresupuesto()));
				forma.setMapaDetallePresupuesto(mundo.consultarDetallePresupuesto(con, forma.getCodigoPresupuesto()));
				forma.setMapaArticulos(mundo.consultarArticulos(con, forma.getCodigoPresupuesto()));
				forma.setMapaServicios(mundo.consultarServicios(con, forma.getCodigoPresupuesto()));
				forma.setMapaIntervenciones(mundo.consultarIntenervenciones(con, forma.getCodigoPresupuesto()));
				
				/**Cargamos los formato de impresion existentes**/
				FormatoImpresionPresupuesto mundoAux = new FormatoImpresionPresupuesto();
				forma.setMapaFormatosImpresion(mundoAux.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detallePresupuesto");		
			}
			else
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("busquedaAvanzada");
			}
		}
	    
		
		/**
		 * Accion para imprimir el presupuesto que se consulto
		 * @param mapping
		 * @param request
		 * @param con
		 * @param forma
		 * @param usuario
		 * @param paciente
		 * @return
		 * @throws Exception
		 */
		private ActionForward accionImprimir(ActionMapping mapping,HttpServletRequest request, Connection con,ConsultaPresupuestoForm forma, UsuarioBasico usuario)throws Exception
		{
			String nombreArchivo;
			Random r= new Random();
			nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
			PresupuestoPdf.pdfPresupuesto(ValoresPorDefecto.getFilePath()+ nombreArchivo, con, usuario, forma.getCodigoFormato(), forma.getCodigoPresupuesto(),forma.getIndex(),forma.getTiposMonedaTagMap(), request);
			UtilidadBD.cerrarConexion(con);
			request.setAttribute("nombreArchivo", nombreArchivo);
			request.setAttribute("nombreVentana", "Consulta Presupuesto");
			return mapping.findForward("abrirPdf");
		}
		
		
		
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	 public Connection openDBConnection(Connection con)
		{

			if(con != null)
				return con;
			
			try{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				logger.warn("Problemas con la base de datos al abrir la conexion");
				return null;
			}
		
			return con;
		}
	 
	 	/**
		 * 
		 * @param session
		 * @return
		 */
		 private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
		 {
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
		    if(usuario == null)
		    {
		    	logger.warn("El usuario no esta cargado (null)");
		    }
				
		    return usuario;
		  }

}