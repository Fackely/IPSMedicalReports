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
import util.Utilidades;

import com.princetonsa.actionform.facturacion.CostoInventarioPorFacturarForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CostoInventarioPorFacturar;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo
 * Fecha: Junio de 2007
 */

public class CostoInventarioPorFacturarAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CostoInventarioPorFacturarAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof CostoInventarioPorFacturarForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CostoInventarioPorFacturarForm forma = (CostoInventarioPorFacturarForm) form;
				CostoInventarioPorFacturar mundo = new CostoInventarioPorFacturar();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("[CostoInventarioPorFacturar]--->Estado: "+estado);

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
				else if(estado.equals("recargar"))
				{
					this.accionRecargar(con, mundo, forma, usuario, mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("imprimir"))
				{
					//Se modifico por la tarea 26828
					//forma.setCostoInventarioPorFacturar(mundo.consultarCostoInventarioPorFacturar(con, forma, usuario));
					//if(Utilidades.convertirAEntero(forma.getCostoInventarioPorFacturar("numRegistros")+"") > 0)
					return accionImprimir(con, mundo, forma, usuario, mapping, request);
					/*else
				{
					forma.setEstado("mostrarMensaje");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}*/
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
				logger.error("El form no es compatible con el form de CostoInventarioPorFacturarForm");
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
	private ActionForward accionImprimir(Connection con, CostoInventarioPorFacturar mundo, CostoInventarioPorFacturarForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		String nombreRptDesign = "CostoInventarioPorFacturar.rptdesign";
		String condiciones = "";
		String filtros = "";
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
        comp.insertLabelInGridPpalOfHeader(1,1, "COSTO DE INVENTARIOS POR FACTURAR");
        
        if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
        	filtros += "CENTRO DE ATENCIÓN: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+", ";
        else
        	filtros += "CENTRO DE ATENCIÓN: Todos, ";
        
        if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionado()))
        	filtros += "CENTRO DE COSTO: "+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getCentroCostoSeleccionado()+""), usuario.getCodigoInstitucionInt())+", ";
        else
        	filtros += "CENTRO DE COSTO: Todos, ";
        
        if(UtilidadCadena.noEsVacio(forma.getTipoInventario()))
        	filtros += "TIPO DE INVENTARIO: "+Utilidades.obtenerNombreNaturalezasArticulo(con, forma.getTipoInventario())+", ";
        else
        	filtros += "TIPO DE INVENTARIO: Todos,  ";
               
        filtros += "FECHA CORTE: "+forma.getMesCorte()+"/"+forma.getAnoCorte()+" ";
        
        comp.insertLabelInGridPpalOfHeader(2,0,filtros);
        comp.insertLabelInGridPpalOfFooter(1,1,"USUARIO: "+usuario.getLoginUsuario());
        
        comp.insertLabelBodyPage(0, 0,"FIN DEL REPORTE", "pie");
        
        comp.obtenerComponentesDataSet("CostoInventarioPorFacturar");
        //Filtramos la consulta por el tipo de solicitud
        condiciones += "cl.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") ";
        
        //Filtramos la consulta por la fecha de corte. No es necesario validar porque es requerida
        condiciones += "AND to_char(cl.fecha_corte, 'YYYY/MM') = '"+forma.getAnoCorte()+"/"+forma.getMesCorte()+"' ";
		
		//Filtramos la consulta por el centro de atencion. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
			condiciones += "AND cl.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionado()))
			condiciones += "AND cl.centro_costo_principal = "+forma.getCentroCostoSeleccionado()+" ";
		
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
	 * Metodo que ejecuta la recarga del centro de costo
	 * segun el centro de atencion seleccionado
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private void accionRecargar(Connection con, CostoInventarioPorFacturar mundo, CostoInventarioPorFacturarForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		if(!forma.getCodigoCentroAtencion().equals("") && !forma.getCodigoCentroAtencion().equals("null"))
		{
			//Cargamos el select de centros de costo segun el centro de atencion seleccionado
			String tipoArea = (ConstantesBD.codigoTipoAreaSubalmacen)+"";
			logger.info("\n====>Centro de Atencion: "+forma.getCodigoCentroAtencion());
			//Cargamos el select de centros de costo
			forma.setCentroCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()+""),false));
			logger.info("\n====>Centros de Costo filtrados por el Centro de Atencion: "+forma.getCentroCosto());
		}
		else
		{
			//Cargamos en el select de centros de costo todos los centros de costo acompañados del centro de atencion
			String tipoArea = (ConstantesBD.codigoTipoAreaSubalmacen)+"";
			logger.info("\n====>Centro de Atencion: "+forma.getCodigoCentroAtencion());
			forma.setCentroCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, 0,false));
			logger.info("\n====>Todos los Centros de Costo: "+forma.getCentroCosto());
		}
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
	private ActionForward accionEmpezar(Connection con, CostoInventarioPorFacturar mundo, CostoInventarioPorFacturarForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), true));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos el select con todos los tipos de inventario existentes en el sistema
		forma.setTiposInventario(Utilidades.obtenerNaturalezasArticulo(con, usuario.getCodigoInstitucionInt()));
		//Cargamos los centros de Costo Inicialmente
		this.accionRecargar(con, mundo, forma, usuario, mapping);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}