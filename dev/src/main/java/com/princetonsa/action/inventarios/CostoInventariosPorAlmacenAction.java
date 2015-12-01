package com.princetonsa.action.inventarios;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.CostoInventariosPorAlmacenForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.CostoInventariosPorAlmacen;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class CostoInventariosPorAlmacenAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CostoInventariosPorAlmacenAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;

		try{


			if (form instanceof CostoInventariosPorAlmacenForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CostoInventariosPorAlmacenForm forma = (CostoInventariosPorAlmacenForm) form;
				CostoInventariosPorAlmacen mundo = new CostoInventariosPorAlmacen();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("[CostoInventariosPorAlmacenAction]--->Estado: "+estado);

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
				else if(estado.equals("buscar"))
				{
					return accionBuscar(con, mundo, forma, usuario, mapping);
				}
				else if(estado.equals("volverPrincipal"))
				{
					return accionEmpezar(con, mundo, forma, usuario, mapping);
				}
				else if(estado.equals("imprimir"))
				{
					return accionImprimir(con, mundo, forma, usuario, mapping, request);
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
				logger.error("El form no es compatible con el form de CostoInventariosPorAlmacenForm");
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
	 * Metodo que permite exportar los datos de la consulta
	 * a un PDF
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, CostoInventariosPorAlmacen mundo, CostoInventariosPorAlmacenForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		String nombreRptDesign = "CostoInventarioPorAlmacen.rptdesign";
		String condiciones = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,1, "COSTO TOTAL DE INVENTARIOS POR ALMACÉN");
        
        if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
        	comp.insertLabelInGridPpalOfHeader(2,0, "CENTRO DE ATENCIÓN: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()+"")));
        else
        	comp.insertLabelInGridPpalOfHeader(2,0, "CENTRO DE ATENCIÓN: Todos");
        
        if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionado()))
        	comp.insertLabelInGridPpalOfHeader(3,0, "CENTRO DE COSTO ALMACÉN: "+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getCentroCostoSeleccionado()+""), usuario.getCodigoInstitucionInt()));
        else
        	comp.insertLabelInGridPpalOfHeader(3,0, "CENTRO DE COSTO ALMACÉN: Todos");
        
        comp.insertLabelInGridPpalOfHeader(4,0, "USUARIO: "+usuario.getLoginUsuario());
        
        comp.obtenerComponentesDataSet("CostoInventarioPorAlmacen");
        //Filtramos la consulta por la institucion
        condiciones += "aa.institucion = "+usuario.getCodigoInstitucion()+" ";
        
        //Filtramos la consulta por el centro de atencion. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
			condiciones += "AND cc.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
		
		//Filtramos la consulta por el centro de costo almacen. Como no es requerido se hace necesario validarlo
		if(UtilidadCadena.noEsVacio(forma.getCentroCostoSeleccionado()))
			condiciones += "AND aa.almacen = "+forma.getCentroCostoSeleccionado()+" ";
		
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
		return mapping.findForward("listado");
	}

	/**
	 * Metodo que ejecuta la consulta del total
	 * de costos de inventarios por almacen
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, CostoInventariosPorAlmacen mundo, CostoInventariosPorAlmacenForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setCostoTotalInventario(mundo.consultarCostoInventarioPorAlmacen(con, forma, usuario));
		double totalInventarios = 0;
		//Formateamos los valores del costo promedio para ser mostradas en la vista
		for(int i=0; i<Utilidades.convertirAEntero(forma.getCostoTotalInventario("numRegistros")+""); i++)
		{
			totalInventarios = totalInventarios + Utilidades.convertirADouble(forma.getCostoTotalInventario("costopromedio_"+i)+"");
			forma.setCostoTotalInventario("costopromedio_"+i, UtilidadTexto.formatearValores(forma.getCostoTotalInventario("costopromedio_"+i)+""));
		}
		forma.setCostoTotalInventario("totalInventario", UtilidadTexto.formatearValores(UtilidadTexto.formatearExponenciales(totalInventarios)));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * Metodo que ejecuta la inicializacion de la funcionalidad
	 * cargando los datos iniciales
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, CostoInventariosPorAlmacen mundo, CostoInventariosPorAlmacenForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), true));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos los centros de Costo Inicialmente
		this.accionRecargar(con, mundo, forma, usuario, mapping);
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
	private void accionRecargar(Connection con, CostoInventariosPorAlmacen mundo, CostoInventariosPorAlmacenForm forma, UsuarioBasico usuario, ActionMapping mapping)
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
			//Cargamos el select de centros de costo
			forma.setCentroCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, 0,false));
			logger.info("\n====>Todos los Centros de Costo: "+forma.getCentroCosto());
		}
	}
	
}