package com.princetonsa.action.inventarios;

import java.sql.Connection;
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

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.ConsultaSaldosCierresInventariosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaSaldosCierresInventarios;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo Henao
 * Fecha: Agosto de 2008
 */

public class ConsultaSaldosCierresInventariosAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ConsultaSaldosCierresInventariosAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try
		{

			if (form instanceof ConsultaSaldosCierresInventariosForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaSaldosCierresInventariosForm forma = (ConsultaSaldosCierresInventariosForm) form;
				ConsultaSaldosCierresInventarios mundo = new ConsultaSaldosCierresInventarios();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsultaSaldosCierresInventarios]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mundo, mapping, usuario, institucion);
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
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("ordenar"))
				{
					return ordenarListado(con, forma, mapping);
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
				logger.error("El form no es compatible con el form de ConsultaSaldosCierresInventariosForm");
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
	 * Metodo que permite ordenar por un patron dado el criterio seleccionado
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward ordenarListado(Connection con, ConsultaSaldosCierresInventariosForm forma, ActionMapping mapping)
	{
		String[] indices = {
		        		"codalmacen_",
		        		"nomalmacen_",
		        		"codarticulo_",
		        		"codinterfaz_",
		        		"desarticulo_",
		        		"unidadmedida_",
		        		"lote_",
		        		"fechavencimiento_",
		        		"saldoanterior_",
		        		"cantidadentradas_",
		        		"cantidadsalidas_",
		        		"costounitario_",
		        		"nuevosaldo_",
		        		"costototal_"
					};
		int numReg = Integer.parseInt(forma.getConsultaSaldosCierresInventarios("numRegistros")+"");
		forma.setConsultaSaldosCierresInventarios(Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getConsultaSaldosCierresInventarios(), "codalmacen_"));
		forma.setConsultaSaldosCierresInventarios("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoExistencias");
	}

	/**
	 * Método implementado para imprimir la Consulta de Saldos
	 * de Cierres de Inventarios (Todos los arrojados por la consulta)
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ConsultaSaldosCierresInventariosForm forma, ActionMapping mapping, HttpServletRequest request, ConsultaSaldosCierresInventarios mundo, UsuarioBasico usuario)
	{
		String nombreRptDesign = "ConsultaSaldosCierresInventarios.rptdesign", condiciones = "", parametros = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        if(Utilidades.convertirAEntero(institucion.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit()+" - "+institucion.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el título del reporte
        comp.insertLabelInGridPpalOfHeader(1, 1, "SALDO CIERRES DE INVENTARIOS AÑO/MES: "+forma.getAnioCierre()+"/"+forma.getMesCierre());
        
        //Parámetros de Búsqueda
        parametros = "Centro Atención ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"], ";
        //Se valida si se filtro por el Almacén
        if(UtilidadCadena.noEsVacio(forma.getAlmacen()))
        	parametros += "Almacén ["+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getAlmacen()), usuario.getCodigoInstitucionInt())+"], ";
        else
        	parametros += "Almacén [Todos], ";
        //Se valida si se filtro por la Clase de Inventario
        if(UtilidadCadena.noEsVacio(forma.getClase()))
        	parametros += "Clase Inventario ["+forma.getDescripcionClase()+"], ";
        else
        	parametros += "Clase Inventario [Todos], ";
        //Se valida si se filtro por el Grupo
        if(UtilidadCadena.noEsVacio(forma.getGrupo()))
        	parametros += "Grupo ["+forma.getDescripcionGrupo()+"], ";
        else
        	parametros += "Grupo [Todos], ";
        //Se valida si se filtro por el Subgrupo
        if(UtilidadCadena.noEsVacio(forma.getSubGrupo()))
        	parametros += "SubGrupo ["+forma.getDescripcionSubGrupo()+"], ";
        else
        	parametros += "SubGrupo [Todos], ";
        //Se valida si se filtro por el Subgrupo
        if(UtilidadCadena.noEsVacio(forma.getCodArticulo()))
        	parametros += "Cód. Axioma Artículo ["+forma.getCodArticulo()+"]";
        else
        	parametros += "Artículo [Todos]";
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Consultamos la condiciones seleccionadas para la consulta de movimiento deudor
        condiciones = mundo.consultarCondicionesSaldosCierresInventarios(con, forma);
        
        //Obtenemos el DataSet y lo modificamos
        comp.obtenerComponentesDataSet("ConsultaSaldosCierresInventarios");
		String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
		comp.modificarQueryDataSet(newQuery);
		//Reemplazamos la PL-SQL del birt con los atributos establecidos en la PL-SQL
		newQuery = comp.obtenerQueryDataSet().replace("1=3", "getsaldoanteriorcierreinv("+forma.getAnioCierre()+", " +
																					    ""+forma.getMesCierre()+", " +
																					   	"a.codigo, " +
																					   	"dci.almacen, " +
																					   	""+usuario.getCodigoInstitucionInt()+", " +
																					   	""+ConstantesBD.codigoTipoCierreInventarioSaldoInicialStr+", " +
																					   	""+ConstantesBD.codigoTipoCierreInventarioAnualStr+", " +
																					   	""+ConstantesBD.codigoTipoCierreInventarioMensualStr+", " +
																					   	"dci.lote)");
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
		return mapping.findForward("listadoExistencias");
	}

	/**
	 * Método que ejecuta la consulta de saldos de
	 * cierres de inventarios. Este método guarda los 
	 * resultados arrojados por la consulta en un HashMap
	 * declarado en la forma
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, ConsultaSaldosCierresInventariosForm forma, ActionMapping mapping, ConsultaSaldosCierresInventarios mundo, UsuarioBasico usuario)
	{
		//Llenamos el mapa con los resultados devueltos por la Consulta de Saldos de Cierres de Inventarios
		forma.setConsultaSaldosCierresInventarios(mundo.consultarSaldosCierresInventarios(con, forma, usuario));
		int numRegistros = Utilidades.convertirAEntero(forma.getConsultaSaldosCierresInventarios("numRegistros")+"");
		double nuevoSaldo = 0, costoTotal = 0;
		//Validamos si el mapa tiene registros
		if(numRegistros > 0)
		{
			//Llenamos en el mapa los valores correpondientes a el Nuevo Saldo, Costo Total y Valor Total por Almacén
			for(int i=0; i<numRegistros; i++)
			{
				//Calculamos el Nuevo Saldo para cada uno de los registros del mapa
				nuevoSaldo = Utilidades.convertirADouble(forma.getConsultaSaldosCierresInventarios("saldoanterior_"+i)+"") 
							 + Utilidades.convertirADouble(forma.getConsultaSaldosCierresInventarios("cantidadentradas_"+i)+"") 
							 - Utilidades.convertirADouble(forma.getConsultaSaldosCierresInventarios("cantidadsalidas_"+i)+"");
				forma.setConsultaSaldosCierresInventarios("nuevosaldo_"+i, nuevoSaldo);
				
				//Calculamos el Costo Total para cada uno de los registros del mapa
				costoTotal = Utilidades.convertirADouble(forma.getConsultaSaldosCierresInventarios("nuevosaldo_"+i)+"") 
				 			 * Utilidades.convertirADouble(forma.getConsultaSaldosCierresInventarios("costounitario_"+i)+"");
				forma.setConsultaSaldosCierresInventarios("costototal_"+i, costoTotal);
				
				//Reseteamos la variables temporales
				nuevoSaldo = 0;
				costoTotal = 0;
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoExistencias");
	}

	/**
	 * Metodo que carga el select de almacenes con la información
	 * de centros de costo según el centro de atención seleccionado
	 * y según el tipo de área "SubAlmacén"
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 */
	private ActionForward accionRecargar(Connection con, ConsultaSaldosCierresInventariosForm forma, ActionMapping mapping, ConsultaSaldosCierresInventarios mundo, UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
			forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", true, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()), false));
		else
			forma.setAlmacenes(temp);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método utilizado para inicializar los
	 * párametros de la funcionalidad y cargar
	 * la página de búsqueda inicial
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @param usuario
	 * @param institucion
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConsultaSaldosCierresInventariosForm forma, ConsultaSaldosCierresInventarios mundo, ActionMapping mapping, UsuarioBasico usuario, InstitucionBasica institucion)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Llamamos al metodo que carga el select de almacen
		this.accionRecargar(con, forma, mapping, mundo, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}