package com.princetonsa.action.facturacion;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ConsultaTarifasArticulosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsultaTarifasArticulos;

public class ConsultaTarifasArticulosAction extends Action 
{

	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(ConsultaTarifasArticulosAction.class);
	
	
	
	/**
	 * 
	 */
	private String[] indices={
								"codigoarticulo_",
								"codigointerfaz_",
								"descripcionarticulo_",
								"descripcionclase_",
								"descripcionnaturaleza_"
							  };
	
	
	
	/**
	 * M�todo excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ConsultaTarifasArticulosForm) 
			{
				ConsultaTarifasArticulosForm forma=(ConsultaTarifasArticulosForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ConsultaTarifasArticulos mundo=new ConsultaTarifasArticulos();

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.resetcriterios();
					UtilidadBD.closeConnection(con);
					mundo.empezar(con, forma, usuario);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					forma.reset_archivo();
					if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
					{
						forma.setMapaListadoArticulos(mundo.consultarArticulos(con, forma.getCodigoArticulo(), forma.getDescripcionArticulo(), forma.getCodigoInterfaz(), forma.getClase(), forma.getGrupo(), forma.getSubgrupo(), forma.getNaturaleza(),ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(usuario.getCodigoInstitucionInt())   ));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listado");
					}
					else
						return mundo.generar(con, usuario, forma, request, institucion, mapping);



				}
				else if(estado.equals("nuevaBusqueda"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("detalleArticulo"))
				{
					forma.reset_archivo();
					forma.setMapaDetalleArticulos(mundo.consultarDetalleArticulos(con, forma.getArticulo()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("ordenardet"))
				{
					this.accionOrdenardet(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}

				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}

				else/*----------------------------------------------
				 * ESTADO =================>>>>> GENERAR
				 ---------------------------------------------*/
					if (estado.equals("generar"))
					{
						return mundo.generar(con, usuario, forma, request, institucion,mapping);
					}
					else
					{
						forma.reset();
						logger.warn("Estado no valido dentro del flujo de CONSULTA FACTURAS VARIAS ");
						request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaFacturasVariasForm");
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
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ConsultaTarifasArticulosForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaListadoArticulos("numRegistros")+"");
		forma.setMapaListadoArticulos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaListadoArticulos(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaListadoArticulos("numRegistros",numReg+"");
		
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenardet(ConsultaTarifasArticulosForm forma) 
	{
		String[] indices={
				"esquematarifario_",
				"descripcionclase_",
				"descesquematarifario_",
				"descripcionarticulo_",
				"codigotarifa_",
				"descripcionnaturaleza_",
				"preciobaseventa_",
				"preciocompramasalta_",
				"codigoarticulo_",
				"precioultimacompra_",
				"tipotarifa_",
				"valortarifa_",
				"costopromedio_",
				"actualizautomatic_",
				"estadoarticulo_",
				"porcentaje_",
			  };
		
		int numReg=Utilidades.convertirAEntero(forma.getMapaDetalleArticulos("numRegistros")+"");
		forma.setMapaDetalleArticulos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaDetalleArticulos(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaDetalleArticulos("numRegistros",numReg+"");
		
	}
	
	
	
	
}
