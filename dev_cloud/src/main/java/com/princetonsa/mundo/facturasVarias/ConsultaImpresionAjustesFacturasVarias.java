package com.princetonsa.mundo.facturasVarias;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import com.princetonsa.actionform.facturasVarias.ConsultaImpresionAjustesFacturasVariasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.ConsultaImpresionAjustesFacturasVariasDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsultaImpresionAjustesFacturasVarias
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		public static Logger logger = Logger.getLogger(ConsultaImpresionAjustesFacturasVarias.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		
	/**
	 * Se inicializa el Dao
	 */
	
	public static ConsultaImpresionAjustesFacturasVariasDao consultaImpresionAjustesFacturasVariasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaImpresionAjustesFacturasVariasDao();
	}
	
	

	/*------------------------------------------------------------------------------------------------------
	 * INDICES PARA EL MANEJO DE LOS KEYS EN LOS MAPAS
	 * ----------------------------------------------------------------------------------------------------*/


	/**
	 * Indices de la consulta de ajustes de facturas varias
	 */
	public static String [] indicesAjustesFacturasVarias = {"codigo0_","consecutivo1_","tipoAjuste2_","fechaAjuste3_","factura4_","conceptoAjuste5_",
									   "valorAjuste6_","observaciones7_","estado8_","estaBd9_","institucion10_","usuarioModifica11_","deudor12_",
									   "nomConcepto13_","cosecFac14_","motivoAnula15_","usuarioAnula16_","fechaAnula17_","usuarioAprueba18_","fechaAprueba19_"};

	/**
	 * Indices del mapa Criterios para la consulta avanzada de ajustes
	 * de facturas varias
	 */
	public static String [] indicesCriteriosBusqueda= {"consecutivo0","factura1","fechaIni2","fechaFin3","institucion4","tipoDeudor5","deudor6",
													   "codigoFacVar7","codigoAjus8","nomDeu9","tipoIdent10","numIdent11","estadoAjuste12","codigoDeudor13","descripDeudor14","tipoDeudor15"};
	
	
	/**
	 * 
	 */
	public static String [] indicesFacturasVarias = {"codigo0_","codigoFacVar1_","nomEstadoFactura2_","fechaAjuste3_","valorFactura4_",
													 "estadoFactura5_","nomDeudor6_","deudor7_","estado8_","concepto9_","institucion10_",
													 "saldoFactura11_","fecha12_","consecutivo13_","nomConcepto14_","fechaAprobacion15_",
													 "numIdent16_"};


	/*------------------------------------------------------------------------------------------------------
	 * FIN INDICES PARA EL MANEJO DE LOS KEYS EN LOS MAPAS
	 * ----------------------------------------------------------------------------------------------------*/
	
	
	
	/**
	 * Metodo encargado de consultar los ajustes de facturas varias
	 * Los keys del mapa criterios son:
	 * ---------------------------------
	 * --consecutivo0 --> Opcional
	 * --factura1 --> Opcional
	 * --fechaIni2--> Opcional
	 * --fechaFin3--> Opcional
	 * --institucion4--> Requerido
	 * ----------------------------------
	 * Los key's del mapa resultado
	 * ----------------------------------
	 * codigo0_,consecutivo1_,tipoAjuste2_,
	 * fechaAjuste3_,factura4_,conceptoAjuste5_,
	 * valorAjuste6_,observaciones7_,estado8_,estaBd9_
	 */
	 public static HashMap  consultaAjustesFacturasVarias (Connection connection,HashMap criterios, boolean ajusTodos)
	 {
		 return consultaImpresionAjustesFacturasVariasDao().consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
	 }
	
	
		
	
	 public static HashMap cargarAjustesFacVar (Connection connection,UsuarioBasico usuario,String codAjusFacVar, boolean ajusTodos)
	 {

		 logger.info("\n entre a cargarAjustesFacVar codAjusFacVar -->"+codAjusFacVar); 
		 
		 HashMap criterios = new HashMap ();
		 criterios.put(indicesCriteriosBusqueda[4], usuario.getCodigoInstitucion());
		 criterios.put(indicesCriteriosBusqueda[8], codAjusFacVar);
		 return consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
		 
	 }
	 
	 
	/**
	 * Metodo encargado de  consultar los datos de la tabla
	 * ajustes facturas varias
	 * @param connection
	 * @param criterios
	 * @param usuario
	 * @return
	 */
	public static HashMap consultarAjustesFacturasVarias (Connection connection, HashMap criterios, UsuarioBasico usuario, boolean ajusTodos)
	{		
		criterios.put(indicesCriteriosBusqueda[4], usuario.getCodigoInstitucion());
	
		return consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
	}
		 
	public static void initConsultImpreAjustesFacturasVarias (Connection connection, ConsultaImpresionAjustesFacturasVariasForm forma, UsuarioBasico usuario)
	{
		
		//1)se carga la informacion de la factura Varia
		forma.setMapaInfoFac(GeneracionModificacionAjustesFacturasVarias.cargarFactura(connection, usuario, forma.getListado(indicesAjustesFacturasVarias[14]+forma.getIndex())+""));
	
		//2)se carga la informacion de ajustes a la factura si existe
		if (!(forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"").equals("") && !(forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"").equals("null"))
		{
			forma.setMapaAjustes(cargarAjustesFacVar(connection, usuario, forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"", true));
			
		}
		//se carga el concepto
		cargarConcepto(connection, forma, usuario);
		//logger.info("\n los conceptos en initGenModAjustesFacturasVarias --> "+forma.getConceptosFacVar());
		
	}
	
	
	
	/**
	 * Metodo en cargado de cargar los diferentes tipos de ajustes
	 */
	public static void cargarConcepto (Connection connection, ConsultaImpresionAjustesFacturasVariasForm forma,UsuarioBasico usuario)
	{

		HashMap parametros = new HashMap ();
		//---------se cargan los parametros por los cuales se va a buscar-----------------
		parametros.put("institucion", usuario.getCodigoInstitucion());
		parametros.put("tipoCartera", ConstantesBD.codigoTipoCarteraTodos+","+ConstantesBD.codigoTipoCarteraFacturasVarias);
		
		if (!(forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals("") && !(forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals("null"))
			if ((forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals(ConstantesIntegridadDominio.acronimoCredito))
				parametros.put("naturaleza", ConstantesBD.codigoConceptosCarteraCredito);
			else
				if ((forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals(ConstantesIntegridadDominio.acronimoDebito))
					parametros.put("naturaleza", ConstantesBD.codigoConceptosCarteraDebito);
	
	}
	
	
	
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,ConsultaImpresionAjustesFacturasVariasForm forma, HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		//se hace una llamada al recolector de basura
		System.gc();
		
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String reporte="",DataSet="";
		DataSet="AjustesFacturasVarias";
		reporte="ConsultaImpresionAjustesFacturasVarias.rptdesign";
		
		//LLamamos al reporte
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",reporte);
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, connection, usuario, forma, request);
		
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		
		comp.obtenerComponentesDataSet(DataSet);
		//Modificamos el DataSet con las validaciones comunes para todos
		//********************************************************************
		 HashMap criterios = new HashMap ();
		 criterios.put(indicesCriteriosBusqueda[4], usuario.getCodigoInstitucion());
		 criterios.put(indicesCriteriosBusqueda[8], forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"");
		//********************************************************************
        String newQuery = comp.obtenerQueryDataSet().replace("WHERE", obtenerWhere(criterios, true));
        logger.info("=====>Consulta en el BIRT Detallado por Tipo Transaccion: "+newQuery);
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
		
    	UtilidadBD.cerrarConexion(connection);
        return mapping.findForward("principal");
	
	}
	
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, ConsultaImpresionAjustesFacturasVariasForm forma, HttpServletRequest request)
	{
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(connection,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el nombre de la funcionalidad en el reporte 
        comp.insertLabelInGridPpalOfHeader(1,1, "AJUSTES FACTURAS VARIAS");
        
              
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	/**
	 * Metodo encargado de generar las clausulas where
	 * de la ocnsulta.
	 * @param criterios
	 * @param ajusTodos
	 * @return
	 */
	public static String obtenerWhere (HashMap criterios, boolean ajusTodos)
	{
		return consultaImpresionAjustesFacturasVariasDao().obtenerWhere(criterios, ajusTodos);
	}
	
	
	
	/**
	 * Metodo encargado de ordenar un mapa
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public static HashMap accionOrdenarMapa(HashMap mapaOrdenar,ConsultaImpresionAjustesFacturasVariasForm forma )
	{			
		logger.info("===> Entré a accionOrdenarMapa");
		int numReg = Utilidades.convertirAEntero(mapaOrdenar.get("numRegistros")+"");	
		mapaOrdenar = (Listado.ordenarMapa(indicesAjustesFacturasVarias,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		logger.info("===> Voy a salir de accionOrdenarMapa");
		return mapaOrdenar;
	}	
	
}