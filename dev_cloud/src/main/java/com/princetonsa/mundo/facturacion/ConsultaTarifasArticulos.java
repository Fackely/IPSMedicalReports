package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ConsultaTarifasArticulosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsultaTarifasArticulosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsultaTarifasArticulosDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaTarifasArticulos 
{

	
	/**
	 * Para manjar los logger de la clase ConsultaTarifasArticulos
	 */
	static Logger logger = Logger.getLogger(ConsultaTarifasArticulos.class);
	
	
	private static ConsultaTarifasArticulosDao objetoDao;
	
	
	/**
	 * 
	 *
	 */
	public ConsultaTarifasArticulos()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getConsultaTarifasArticulosDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}

	
	/**
	 * 
	 * @param con 
	 * @param codigoArticulo
	 * @param descripcionArticulo
	 * @param codigoInterfaz
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param naturaleza
	 * @param x 
	 * @return
	 */
	public HashMap<String, Object> consultarArticulos(Connection con, String codigoArticulo, String descripcionArticulo, String codigoInterfaz, String clase, String grupo, String subgrupo, String naturaleza, String codigoEstandarBusquedaArticulos) 
	{
		return objetoDao.consultarArticulos(con, codigoArticulo, descripcionArticulo, codigoInterfaz, clase, grupo, subgrupo, naturaleza,codigoEstandarBusquedaArticulos); 
	}

	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public HashMap<String, Object> consultarDetalleArticulos(Connection con, String articulo) 
	{
		return objetoDao.consultarDetalleArticulos(con, articulo);
	}
	
	
	
	/**
	 * Metodo encargado de armar la consulta de analisis de costo
	 * @author Jhony Alexander Duque A.
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- institucion  --> Requerido
	 * -- codigoArticulo  --> opcional
	 * -- descripcionArticulo  --> opcional
	 * -- codigoInterfaz  --> opcional
	 * -- clase  --> opcional
	 * -- grupo  --> opcional
	 * -- subgrupo  --> opcional
	 * -- naturaleza  --> opcional
	 * -- esquemaTarifario  --> opcional
	 * @return String
	 */
	public static String obtenerConsulta (HashMap criterios)
	{
		return objetoDao.obtenerConsulta(criterios);
	}
	
	
	
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public static HashMap ejecutarConsulta (Connection connection,String consulta)
    {
    	return SqlBaseConsultaTarifasArticulosDao.ejecutarConsulta(connection, consulta);
    }
	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public void empezar (Connection connection, ConsultaTarifasArticulosForm forma, UsuarioBasico usuario)
	{
		//Cargar los Esquemas Tarifarios
		forma.setArrayEsquemaTarifario(Utilidades.obtenerEsquemasTarifariosInArray(true, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));
		
	}
	
	
	public static ActionForward generar (Connection connection,UsuarioBasico usuario, ConsultaTarifasArticulosForm forma, HttpServletRequest request,InstitucionBasica institucion,ActionMapping mapping) throws SQLException
	{
		
		
		if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return generarReporte(connection, usuario, forma, request,mapping);
		else
			return archivoPlano(connection, forma, usuario, institucion, request, mapping);
			
		
		
	}
	
	
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,ConsultaTarifasArticulosForm forma, HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		//se hace una llamada al recolector de basura
		System.gc();
		
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String reporte="",DataSet="";
		String forward="";
		if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
		{
			
			DataSet="consultaTarifasArticulos";
			reporte="ConsultaTarifasArticulos.rptdesign";
			forward="detalle";
		}
		else
		{
			DataSet="analisisCosto";
			reporte="AnalisisCosto.rptdesign";
			forward="principal";
		}
		
		//LLamamos al reporte
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",reporte);
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, connection, usuario, forma, request);
		
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		
		comp.obtenerComponentesDataSet(DataSet);
		String newQuery="";
		if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
			//Modificamos el DataSet con las validaciones comunes para todos
			newQuery = comp.obtenerQueryDataSet().replace("1=1", "ti.articulo="+forma.getArticulo());
		else
			//Se modifica el query
			newQuery=obtenerConsulta(organizarCriterios(forma, usuario));

		
		comp.modificarQueryDataSet(newQuery);
                	    
        logger.info("=====>Consulta en el BIRT Detallado por Tipo Transaccion: "+comp.obtenerQueryDataSet());
    
           
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
        return mapping.findForward(forward);
	
	}
	
	/**
	 * Metodo encargado de organizar los criterios de busqueda
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static HashMap organizarCriterios (ConsultaTarifasArticulosForm forma,UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap ();
		

		criterios.put("institucion", usuario.getCodigoInstitucion());
		criterios.put("codigoArticulo", forma.getCodigoArticulo());
		criterios.put("descripcionArticulo", forma.getDescripcionArticulo());
		criterios.put("codigoInterfaz", forma.getCodigoInterfaz());
		criterios.put("clase", forma.getClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("subgrupo", forma.getSubgrupo());
		criterios.put("naturaleza", forma.getNaturaleza());
		criterios.put("esquemaTarifario", forma.getEsqTar());
		
		if (forma.getTipoCod().equals(ConstantesIntegridadDominio.acronimoInterfaz))
			criterios.put("tipoCodigoArticulo", 2);
		else
			if (forma.getTipoCod().equals(ConstantesIntegridadDominio.acronimoAxioma))
				criterios.put("tipoCodigoArticulo", 1);
			else
				if (forma.getTipoCod().equals(ConstantesIntegridadDominio.acronimoAmbos))
					criterios.put("tipoCodigoArticulo", 3);
		
		
		
		return criterios; 
	}
	
	
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, ConsultaTarifasArticulosForm forma, HttpServletRequest request)
	{
		//Insertamos la informaci�n de la Instituci�n
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
        if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
        	comp.insertLabelInGridPpalOfHeader(1,1, "INFORMACION GENERAL TARIFA");
        else
        	if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteAnalisisCosto))
        		comp.insertLabelInGridPpalOfHeader(1,1, "ANALISIS COSTO");
        		
        String criterios = organizarCriterios(connection, forma, usuario); 
        
        
        comp.insertLabelInGridPpalOfHeader(3, 0, criterios);
     
        
        //Informaci�n de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	
	
	
	public static String organizarCriterios (Connection connection, ConsultaTarifasArticulosForm forma,UsuarioBasico usuario)
	{
		 String criterios="";
         
	        //esquema tarifario
	        if (UtilidadCadena.noEsVacio(forma.getEsqTar()) && !forma.getEsqTar().equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+="Esquema Tarifario: "+Utilidades.getNombreEsquemaTarifario(connection, Utilidades.convertirAEntero(forma.getEsqTar()));
	    
	        //Codigo Articulo
	        if (UtilidadCadena.noEsVacio(forma.getCodigoArticulo())) 
	        	criterios+="  Codigo Articulo: "+forma.getCodigoArticulo();
	      
	        //Descripcion
	        if (UtilidadCadena.noEsVacio(forma.getDescripcionArticulo()))
	        	criterios+="  Descripcion: "+forma.getDescripcionArticulo();
	        	

	        //clase inventario
	        if (UtilidadCadena.noEsVacio(forma.getClase()) && !forma.getClase().equals("0"))
	        {
	        	HashMap tmp = new HashMap ();
	        	tmp.put("codigo", forma.getClase());
	        	tmp.put("institucion", usuario.getCodigoInstitucion());
	        	criterios+=" Clase Inventario: "+UtilidadInventarios.obtenerClaseInventario(connection, tmp).get(0).get("nombre");
	        }
	        
	        //grupo
	        if (UtilidadCadena.noEsVacio(forma.getGrupo()) && !forma.getGrupo().equals("0"))
	        	criterios+="  Grupo: "+Utilidades.getNombreGrupoServicios(connection, Utilidades.convertirAEntero(forma.getGrupo()));
	        
	        //sub grupo
	        if (UtilidadCadena.noEsVacio(forma.getSubgrupo()) && !forma.getSubgrupo().equals("0"))
	        {
	        	HashMap tmp = new HashMap();
	        	
	        	tmp.put("institucion", usuario.getCodigoInstitucion());
	        	tmp.put("codigo", forma.getSubgrupo());
	        	criterios+="  Sub Grupo: "+Utilidades.obtenerSubGrupo(connection, tmp).get(0).get("nombre");
	        }
	       
	        //naturaleza
	        if (UtilidadCadena.noEsVacio(forma.getNaturaleza()) && !forma.getNaturaleza().equals("selected"))
	        	criterios+="  Naturaleza: "+Utilidades.obtenerNombreNaturalezasArticulo(connection, forma.getNaturaleza());
	        
	    	
	     	//Codigo tipo articulo
	        if (UtilidadCadena.noEsVacio(forma.getTipoCod()) && !forma.getTipoCod().equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+=" Codigo tipo Artiulo: "+ValoresPorDefecto.getIntegridadDominio(forma.getTipoCod());        
	        
	        return criterios;
	}
	
	
	
	
	public static ActionForward archivoPlano (Connection connection, ConsultaTarifasArticulosForm forma,UsuarioBasico usuario, InstitucionBasica institucion,HttpServletRequest  request,ActionMapping mapping ) throws SQLException
	{
		HashMap tmp = new HashMap();
		String nombre="";
		String forward="";
		if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
		{
			tmp=forma.getMapaDetalleArticulos();
			nombre="Informacion-General-Tarifa";
			forward="detalle";
		}
		else
		{
			tmp.putAll(ejecutarConsulta(connection, obtenerConsulta(organizarCriterios(forma, usuario))));
			nombre="Analisis-Costo";
			forward="principal";
		}
		
		boolean OperacionTrue=false,existeTxt=false;
		int ban=ConstantesBD.codigoNuncaValido;
		//se llama al garbage collector
		System.gc();
		
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(connection);
		
		//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
		String path = ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt());
		logger.info("====>Path Valor por Defecto: "+path);
		//Validamos si el path esta vacio o lleno
    	if(UtilidadTexto.isEmpty(path))
		{
    		
    		forma.setOperacionTrue(false);
    		forma.setExisteArchivo(false);
    		UtilidadBD.abortarTransaccion(connection);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Inventarios", "error.manejoPacientes.rutaNoDefinida", true);
		}
		
		
		//arma el nombre del reporte
		String nombreReport=CsvFile.armarNombreArchivo(nombre, usuario);
		//se genera el documento con la informacion
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			if (forma.getTipoReport().equals(ConstantesIntegridadDominio.acronimoTipoReporteInformacionGeneralTarifa))
				OperacionTrue=TxtFile.generarTxt(cargarInformacionGeneralTarifa(connection, forma, institucion, usuario), nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
			else
				OperacionTrue=TxtFile.generarTxt(cargarAnalisisCosto(connection, forma, institucion, usuario, tmp), nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
		}
		if (OperacionTrue)
		{
			//se genera el archivo en formato Zip
			ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+path+nombreReport+".zip"+" "+ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
			//se ingresa la direccion donde se almaceno el archivo
			forma.setRuta(ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
			//se ingresa la ruta para poder descargar el archivo
			forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+path+nombreReport+".zip");
			

			//se valida si existe el csv
			existeTxt=UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+path, nombreReport+".csv");
			//se valida si existe el zip
			forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+path, nombreReport+".zip"));
									
			if (existeTxt )
				forma.setOperacionTrue(true);
		
		}
			UtilidadBD.cerrarConexion(connection);
			return mapping.findForward(forward);
	
}
	
	
	/**
	 * Metodo encargado de generar el contenido del archivo plano
	 * informacion general
	 * @param connection
	 * @param forma
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public static StringBuffer cargarInformacionGeneralTarifa(Connection connection, ConsultaTarifasArticulosForm forma,InstitucionBasica institucion,UsuarioBasico usuario)
	{
		logger.info("\n entro a cargarInformacionGeneralTarifa");
		StringBuffer cadena = new StringBuffer();
		
		//razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//nit
		cadena.append(institucion.getNit()+"\n");
		//direccion
		cadena.append(institucion.getDireccion()+"\n");
		//telefono
		cadena.append(institucion.getTelefono()+"\n");
		//titulo del reporte
		cadena.append(" INFORMACION GENERAL TARIFA \n\n");  
		//criterios de busqueda del reporte
		cadena.append(organizarCriterios(connection, forma, usuario)+"\n ");
		//------------------------------------------------------------------------------------////
		//se organiza el encabezado
		cadena.append("Esquema Tarifario SDS, Tipo Tarifa, Porcentaje, Valor Tarifa, Indicativo Actualizable \n");
		
		for (int i=0;i<Utilidades.convertirAEntero(forma.getMapaDetalleArticulos("numRegistros")+"");i++)
		{
			cadena.append(forma.getMapaDetalleArticulos("descesquematarifario_"+i)+", "+forma.getMapaDetalleArticulos("tipotarifadetallado_"+i)+", "+
						  forma.getMapaDetalleArticulos("porcentaje_"+i)+", "+forma.getMapaDetalleArticulos("valortarifa_"+i)+", "+
						  forma.getMapaDetalleArticulos("actualizautomatic_"+i)+"\n");
		}
		
		
		
		return cadena;
	}
	
	
	public static StringBuffer cargarAnalisisCosto(Connection connection, ConsultaTarifasArticulosForm forma,InstitucionBasica institucion,UsuarioBasico usuario,HashMap tmp)
	{
		logger.info("\n entro a cargarAnalisisCosto");
		StringBuffer cadena = new StringBuffer();
		
		//razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//nit
		cadena.append(institucion.getNit()+"\n");
		//direccion
		cadena.append(institucion.getDireccion()+"\n");
		//telefono
		cadena.append(institucion.getTelefono()+"\n");
		//titulo del reporte
		cadena.append(" ANALISIS COSTO \n\n");  
		//criterios de busqueda del reporte
		cadena.append(organizarCriterios(connection, forma, usuario)+"\n ");
		//------------------------------------------------------------------------------------////
		//se organiza el encabezado
		cadena.append("Almacen, Cod. Art., Articulo, Tarifa, Costo, Valor Venta, Dif% \n");
		
		for (int i=0;i<Utilidades.convertirAEntero(tmp.get("numRegistros")+"");i++)
		{
			cadena.append(tmp.get("nombre_almacen_"+i)+", "+tmp.get("codigoarticulo_"+i)+", "+
						  tmp.get("descripcion_articulo_"+i)+", "+tmp.get("esquematarifario_"+i)+", "+
						  tmp.get("costo_promedio_"+i)+", "+tmp.get("valortarifa_"+i)+", "+
						  tmp.get("diferencia_"+i)+"\n");
		}
		
		
		return cadena;
	}
	
	
	
	
}
