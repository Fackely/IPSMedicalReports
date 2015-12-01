package com.princetonsa.action.facturacion;

import java.sql.Connection;
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

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.CostoVentasPorInventarioForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CostoVentasPorInventario;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class CostoVentasPorInventarioAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CostoVentasPorInventarioAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof CostoVentasPorInventarioForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CostoVentasPorInventarioForm forma = (CostoVentasPorInventarioForm) form;
				CostoVentasPorInventario mundo = new CostoVentasPorInventario();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("[CostoVentasPorInventario]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, mundo, forma, usuario, mapping);
				}
				else if(estado.equals("imprimir"))
				{
					forma.setCostoVentasPorInventario(mundo.consultarCostoVentasPorInventario(con, forma, usuario));
					if(Utilidades.convertirAEntero(forma.getCostoVentasPorInventario("numRegistros")+"") > 0)
						return accionImprimir(con, mundo, forma, usuario, mapping, request);
					else
					{
						forma.setEstado("mostrarMensaje");
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
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
				logger.error("El form no es compatible con el form de CostoVentasPorInventarioForm");
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
	 * Metodo utilizado para hacer el llamado del PDF
	 * en birt cambiando las condiciones del where
	 * desde el action
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, CostoVentasPorInventario mundo, CostoVentasPorInventarioForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		String nombreRptDesign = "CostoVentasPorInventario.rptdesign";
		String condiciones = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,1, "COSTO DE VENTAS POR INVENTARIO");
        
        if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
        	comp.insertLabelInGridPpalOfHeader(2,0, "CENTRO DE ATENCIÓN: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()+"")));
        else
        	comp.insertLabelInGridPpalOfHeader(2,0, "CENTRO DE ATENCIÓN: Todos");
        
        if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionadoAlmacen()))
        	comp.insertLabelInGridPpalOfHeader(3,0, "CENTRO DE COSTO ALMACÉN: "+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getCentroCostoSeleccionadoAlmacen()+""), usuario.getCodigoInstitucionInt()));
        else
        	comp.insertLabelInGridPpalOfHeader(3,0, "CENTRO DE COSTO ALMACÉN: Todos");
        
        if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionadoSolicitante()))
        	comp.insertLabelInGridPpalOfHeader(4,0, "CENTRO DE COSTO SOLICITANTE: "+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getCentroCostoSeleccionadoSolicitante()+""), usuario.getCodigoInstitucionInt()));
        else
        	comp.insertLabelInGridPpalOfHeader(4,0, "CENTRO DE COSTO SOLICITANTE: Todos");
        
        if(UtilidadCadena.noEsVacio(forma.getTipoInventario()))
        	comp.insertLabelInGridPpalOfHeader(5,0, "TIPO DE INVENTARIO: "+Utilidades.obtenerNombreNaturalezasArticulo(con, forma.getTipoInventario()));
        else
        	comp.insertLabelInGridPpalOfHeader(5,0, "TIPO DE INVENTARIO: Todos");
        
        comp.insertLabelInGridPpalOfHeader(6,0, "PERIODO: "+forma.getFechaInicial()+" - "+forma.getFechaFinal());
        comp.insertLabelInGridPpalOfHeader(7,0, "USUARIO: "+usuario.getLoginUsuario());
        
        comp.obtenerComponentesDataSet("CostoVentasPorInventario");
        
        //Filtramos la consulta por el tipo de solicitud y las facturas anuladas
        condiciones += "dc.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") ";
        condiciones += "AND f.estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada+" "; 
        
        //Filtramos la consulta por la fecha inicial y la fecha final. No es necesario validar porque es requerida
        condiciones += "AND f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' ";
		
		//Filtramos la consulta por el centro de atencion. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
			condiciones += "AND f.centro_aten = "+forma.getCodigoCentroAtencion()+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionadoAlmacen()))
			condiciones += "AND sm.centro_costo_principal = "+forma.getCentroCostoSeleccionadoAlmacen()+" ";
		
		//Filtramos la consulta por el centro de costo solicitado. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionadoSolicitante()))
			condiciones += "AND s.centro_costo_solicitante = "+forma.getCentroCostoSeleccionadoSolicitante()+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getTipoInventario()))
		{
			condiciones += "AND a.naturaleza = '"+forma.getTipoInventario()+"' ";
			condiciones += "AND a.institucion = "+usuario.getCodigoInstitucion()+" ";
		}
        
        String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("\n=====>Consulta en el BIRT con Condiciones: "+newQuery);
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
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que carga inicialmente los select
	 * del centro de atencion y de los convenios
	 * @param con
	 * @param mundo 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, CostoVentasPorInventario mundo, CostoVentasPorInventarioForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), true));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos el select con todos los tipos de inventario existentes en el sistema
		forma.setTiposInventario(Utilidades.obtenerNaturalezasArticulo(con, usuario.getCodigoInstitucionInt()));
		//Cargamos los centros de costo de almacen y solicitante
		String tipoArea = (ConstantesBD.codigoTipoAreaSubalmacen)+"";
		forma.setCentroCostoAlmacen(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, 0, false));
		tipoArea = (ConstantesBD.codigoTipoAreaDirecto)+"";
		forma.setCentroCostoSolicitante(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, 0, false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}