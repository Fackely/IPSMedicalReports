package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.actionform.facturasVarias.ConsultaMovimientoDeudorForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.ConsultaMovimientoDeudor;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha: Agosto de 2008
 */
public class ConsultaMovimientoDeudorAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ConsultaMovimientoDeudorAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof ConsultaMovimientoDeudorForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaMovimientoDeudorForm forma = (ConsultaMovimientoDeudorForm) form;
				ConsultaMovimientoDeudor mundo = new ConsultaMovimientoDeudor();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsultaMovimientoDeudor]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("recargar"))
				{
					return accionRecargar(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("buscar"))
				{
					return accionBuscar(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("imprimir"))
				{
					return accionImprimir(con, forma, mapping, request, mundo, usuario);
				}
				else if(estado.equals("detalleDeudor"))
				{
					return accionDetalleDeudor(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("volverListado"))
				{
					return accionVolverListado(con, forma, mapping, mundo, usuario);
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaMovimientoDeudorForm");
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
	 * Método implementado para volver a la página del listado
	 * de consulta de movimientos por deudor
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionVolverListado(Connection con, ConsultaMovimientoDeudorForm forma, ActionMapping mapping, ConsultaMovimientoDeudor mundo, UsuarioBasico usuario)
	{
		//Reseteamos el mapa general y la variable posicion
		forma.resetDatosVolver();
		//Realizamos la consulta nuevamente de los movimientos por deudor en general
		this.accionBuscar(con, forma, mapping, mundo, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoDeudores");
	}

	/**
	 * Método que consulta la información del detalle
	 * de la Consulta de Movimiento de Deudor seleccionada
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleDeudor(Connection con, ConsultaMovimientoDeudorForm forma, ActionMapping mapping, ConsultaMovimientoDeudor mundo, UsuarioBasico usuario)
	{
		logger.info("====>Posicion Seleccionada: "+forma.getPosicion());
		int deudor = Utilidades.convertirAEntero(forma.getConsultaMovimientoDeudor("deudor_"+forma.getPosicion())+"");
		//Llenamos el mapa con la información del detalle de los movimientos por deudor
		forma.setConsultaDetalleMovimientoDeudor(mundo.consultarDetalleMovimientosDeudor(con, deudor, forma.getFechaInicial(), forma.getFechaFinal()));
		//Número de registros del mapa de Detalle de la Consulta de Movimiento de Deudores 
		int numRegistros = Utilidades.convertirAEntero(forma.getConsultaDetalleMovimientoDeudor("numRegistros")+"");
		//Validamos si numRegistros es mayor a cero para realizar el Totalizado
		if(numRegistros > 0)
		{
			double totalValorInicial = 0, totalValorAjusteDebito = 0, totalValorAjusteCredito = 0, totalPagosAplicados = 0, totalSaldo = 0;  
			//Recorremos el mapa con el fin de sumar cada registros en unas variables totalizadas que serán ingresadas en el mapa después de salir del ciclo
			for(int i=0; i<numRegistros; i++)
			{
				totalValorInicial = totalValorInicial + Utilidades.convertirADouble(forma.getConsultaDetalleMovimientoDeudor("valorinicial_"+i)+"");
				totalValorAjusteDebito = totalValorAjusteDebito + Utilidades.convertirADouble(forma.getConsultaDetalleMovimientoDeudor("ajustesdebito_"+i)+"");
				totalValorAjusteCredito = totalValorAjusteCredito + Utilidades.convertirADouble(forma.getConsultaDetalleMovimientoDeudor("ajustescredito_"+i)+"");
				totalPagosAplicados = totalPagosAplicados + Utilidades.convertirADouble(forma.getConsultaDetalleMovimientoDeudor("pagosaplicados_"+i)+"");
				totalSaldo = totalSaldo + Utilidades.convertirADouble(forma.getConsultaDetalleMovimientoDeudor("saldo_"+i)+"");
			}
			//Agregamos los totales calculados en el mapa de datos de Consulta de Movimiento de Deudores
			forma.setConsultaDetalleMovimientoDeudor("totalValorInicial", totalValorInicial);
			forma.setConsultaDetalleMovimientoDeudor("totalValorAjusteDebito", totalValorAjusteDebito);
			forma.setConsultaDetalleMovimientoDeudor("totalValorAjusteCredito", totalValorAjusteCredito);
			forma.setConsultaDetalleMovimientoDeudor("totalPagosAplicados", totalPagosAplicados);
			forma.setConsultaDetalleMovimientoDeudor("totalSaldo", totalSaldo);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleDeudor");
	}

	/**
	 * Metodo implementado para imprimir la Consulta de Movimientos
	 * de Deudor (Todos los arrojados por la consulta)
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ConsultaMovimientoDeudorForm forma, ActionMapping mapping, HttpServletRequest request, ConsultaMovimientoDeudor mundo, UsuarioBasico usuario)
	{
		String nombreRptDesign = "ConsultaMovimientoDeudor.rptdesign", condiciones = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Párametros de Búsqueda
        v = new Vector();
        v.add("FACTURAS VARIAS - MOVIMIENTO POR DEUDOR");
        v.add("PERIODO: "+forma.getFechaInicial()+" - "+forma.getFechaFinal());
        //Insertamos el vector con los parametros de consulta
        comp.insertLabelInGridOfMasterPageWithProperties(1, 1, v, DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Consultamos la condiciones seleccionadas para la consulta de movimiento deudor
        condiciones = mundo.consultarCondicionesMovimientosDeudor(con, forma);
        
        //Obtenemos el DataSet y lo modificamos
        comp.obtenerComponentesDataSet("MovimientoDeudor");
		String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("=====>Consulta en el BIRT: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoDeudores");
	}

	/**
	 * Método utilizado para consultar los movimientos
	 * de deudores según los criterios seleccionados en 
	 * la vista de la funcionalidad
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, ConsultaMovimientoDeudorForm forma, ActionMapping mapping, ConsultaMovimientoDeudor mundo, UsuarioBasico usuario)
	{
		logger.info("Deudor::: "+forma.getCodigoDeudor());
		//Llenamos el mapa con los resultados arrojados por la consulta de movimientos de deudores
		forma.setConsultaMovimientoDeudor(mundo.consultarMovimientosDeudores(con, forma));
		//Número de registros del mapa de Consulta de Movimiento de Deudores 
		int numRegistros = Utilidades.convertirAEntero(forma.getConsultaMovimientoDeudor("numRegistros")+"");
		//Validamos si numRegistros es mayor a cero para realizar el Totalizado
		if(numRegistros > 0)
		{
			double totalValorInicial = 0, totalValorAjusteDebito = 0, totalValorAjusteCredito = 0, totalPagosAplicados = 0, totalSaldo = 0;  
			//Recorremos el mapa con el fin de sumar cada registros en unas variables totalizadas que serán ingresadas en el mapa después de salir del ciclo
			for(int i=0; i<numRegistros; i++)
			{
				totalValorInicial = totalValorInicial + Utilidades.convertirADouble(forma.getConsultaMovimientoDeudor("valorinicial_"+i)+"");
				totalValorAjusteDebito = totalValorAjusteDebito + Utilidades.convertirADouble(forma.getConsultaMovimientoDeudor("ajustesdebito_"+i)+"");
				totalValorAjusteCredito = totalValorAjusteCredito + Utilidades.convertirADouble(forma.getConsultaMovimientoDeudor("ajustescredito_"+i)+"");
				totalPagosAplicados = totalPagosAplicados + Utilidades.convertirADouble(forma.getConsultaMovimientoDeudor("pagosaplicados_"+i)+"");
				totalSaldo = totalSaldo + Utilidades.convertirADouble(forma.getConsultaMovimientoDeudor("saldo_"+i)+"");
			}
			//Agregamos los totales calculados en el mapa de datos de Consulta de Movimiento de Deudores
			forma.setConsultaMovimientoDeudor("totalValorInicial", totalValorInicial);
			forma.setConsultaMovimientoDeudor("totalValorAjusteDebito", totalValorAjusteDebito);
			forma.setConsultaMovimientoDeudor("totalValorAjusteCredito", totalValorAjusteCredito);
			forma.setConsultaMovimientoDeudor("totalPagosAplicados", totalPagosAplicados);
			forma.setConsultaMovimientoDeudor("totalSaldo", totalSaldo);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoDeudores");
	}

	/**
	 * Metodo que llena y carga el select de deudores
	 * según lo seleccionado en el select de tipo de
	 * deudor 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, ConsultaMovimientoDeudorForm forma, ActionMapping mapping, ConsultaMovimientoDeudor mundo, UsuarioBasico usuario)
	{
		ArrayList temp = new ArrayList<HashMap<String,Object>>();
		//Validamos que se halla seleccionado un tipo de deudor para llenar el arraylist de deudores
		if(UtilidadCadena.noEsVacio(forma.getTipoDeudor()))
			forma.setDeudores(Utilidades.obtenerDeudores(con, forma.getTipoDeudor()));
		else
			forma.setDeudores(temp);
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}