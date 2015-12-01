
/*
 * Creado   25/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.mundo.inventarios;

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
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInventarios;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.ConsultaImpresionTrasladosForm;
import com.princetonsa.actionform.manejoPaciente.OcupacionDiariaCamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaImpresionTrasladosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaImpresionTrasladosDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * Clase que implementa los metodos que
 * comunican el WorkFlow de la funcionalidad
 * con la fuente de datos
 *
 * @version 1.0, 25/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaImpresionTraslados 
{
    /**
    * manejador de los logs de la clase
    */
   private static Logger logger=Logger.getLogger(ConsultaImpresionTraslados.class);
   /**
     * DAO de este objeto, para trabajar con
     * registro transacciones en la fuente de datos
     */
   private static ConsultaImpresionTrasladosDao consultaDao;
   /**
    * Metodo para inicializar los atributos de la clase.
    *
    */   
   public void reset ()
   { 
       
   }
   /**
     * Inicializa el acceso a bases de datos de un objeto.
     * @param tipoBD el tipo de base de datos que va a usar el objeto
     * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
     * son los nombres y constantes definidos en <code>DaoFactory</code>.
     */
    public boolean init(String tipoBD)
    {
        if ( consultaDao== null ) 
        {
            // Obtengo el DAO que encapsula las operaciones de BD de este objeto
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            consultaDao= myFactory.getConsultaImpresionTrasladosDao();   
            if( consultaDao!= null )
                return true;
        }
        return false;
    }
    /**
     * constructor
     */
    public ConsultaImpresionTraslados()
    {
      this.reset();
      this.init(System.getProperty("TIPOBD"));
    }
    /**
     * metodo para realizar la busqueda avanzada de
     * listado traslado almacen
     * @param con Connection
     * @param vo HashMap
     * @return HashMap
     */
    public HashMap ejecutarBusquedaAvanzadaTraslados(Connection con,HashMap vo)
    {
        HashMap mapa=new HashMap();
        mapa=consultaDao.ejecutarBusquedaAvanzadaTraslados(con, vo);
        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
        {
            mapa.put("fecha_solicitud_"+k, UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_solicitud_"+k)+""));
            if((mapa.get("fecha_despacho_"+k)+"").equals("null"))
                mapa.put("fecha_despacho_"+k,"");
            else
                mapa.put("fecha_despacho_"+k, UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fecha_despacho_"+k)+""));            
        }
        return (HashMap) mapa.clone();
    }
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud)
    {
        return consultaDao.consultaDetalleSolicitud(con, numeroSolicitud);
    }
    
    
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////    
 //--------------------------------------------------------------------------------------------------   
    /****************************************************
     * Modificacion por anexo 632
     ****************************************************/
    
    public  static void initCriterios (Connection connection,UsuarioBasico usuario, ConsultaImpresionTrasladosForm forma)
    {
    	HashMap criterios = new HashMap ();
    	criterios.put("institucion", usuario.getCodigoInstitucion());
    	forma.setClaseInventario(UtilidadInventarios.obtenerClaseInventario(connection, criterios));
    	
    	
    }
    
	/**
	 * Metodo encargado de identificar que tipo se salida se eligio
	 * y retorna el archivo plano o en pdf.
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 * @param institucion
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	public static ActionForward generar (Connection connection,UsuarioBasico usuario, ConsultaImpresionTrasladosForm forma, HttpServletRequest request,InstitucionBasica institucion,ActionMapping mapping) throws SQLException
	{
		forma.setMapaAtributosBusqueda("institucion", usuario.getCodigoInstitucion());
		
		if ((forma.getMapaAtributosBusqueda("tipoSalida")+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return generarReporte(connection, usuario, forma, request,mapping);
		else
			return archivoPlano(connection, forma, usuario, institucion, request, mapping);
				
	}
    
    
	
	
	
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,ConsultaImpresionTrasladosForm forma, HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		//se hace una llamada al recolector de basura
		System.gc();
		
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String reporte="",DataSet="";
		String consulta=consultasReportes(forma.getMapaAtributosBusqueda());;
		if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXAlmacen))
		{
			logger.info("\n ---> Detallado por almacen");
			DataSet="detalladoXAlmacen";
			reporte="DetalladoXAlmacen.rptdesign";
			
		}
		else
			if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXClaseInventario))
			{
				logger.info("\n ---> Detallado por clase de inventario");
				DataSet="detalladoXClaseInventario";
				reporte="DetalladoXClaseInventario.rptdesign";
			}
			else
				if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXTransaccion))
				{
					logger.info("\n ---> Detallado por tipo de transaccion");
					DataSet="detalladoXTipoTransaccion";
					reporte="DetalladoXTipoTransaccion.rptdesign";
				}
		
		//LLamamos al reporte
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",reporte);
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, connection, usuario, forma, request);
		
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		
		comp.obtenerComponentesDataSet(DataSet);
		//Modificamos el DataSet con las validaciones comunes para todos
		
		comp.modificarQueryDataSet(consulta);
		
		//String newQuery = comp.obtenerQueryDataSet().replace("WHERE", obtenerWhere(forma.getCriterios(), organizarEstadosCama(forma.getEstadosCamas())));
        logger.info("=====>Consulta en el BIRT : "+comp.obtenerQueryDataSet());
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
    
	
	/**
	 * Metodo encargado de organizar el encabezado 
	 * para el birt
	 * @param comp
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, ConsultaImpresionTrasladosForm forma, HttpServletRequest request)
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
        String titulo="";
        //Insertamos el nombre de la funcionalidad en el reporte 
        if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXAlmacen))
        	titulo="DETALLADO POR ALMACEN";
        else
			if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXTransaccion))
				titulo="DETALLADO POR TIPO DE TRANSACCION";
			else
				if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXClaseInventario))
					titulo="DETALLADO POR CLASE DE INVENTARIO";
        
        comp.insertLabelInGridPpalOfHeader(1,1, titulo);
        
        String criterios=organizarCriterios(connection,forma, usuario);
       
        
        comp.insertLabelInGridPpalOfHeader(3, 0, criterios);
     
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	
	public static String organizarCriterios (Connection connection, ConsultaImpresionTrasladosForm forma,UsuarioBasico usuario)
	{
		 String criterios="";
         
	        //almacen despacha
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("codAlmacenDespacha")+"") && !(forma.getMapaAtributosBusqueda("codAlmacenDespacha")+"").equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+="Almacen Despacha: "+forma.getMapaAtributosBusqueda("nomAlmacenDespacha");
	    
	        //almacen solicita
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("codAlmacenSolicita")+"") && !(forma.getMapaAtributosBusqueda("codAlmacenSolicita")+"").equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+="  Almacen Solicita: "+forma.getMapaAtributosBusqueda("nomAlmacenSolicita");
	      
	        //numero traslado inicial
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("noTrasladoInicial")+""))
	        	criterios+="  No. Inicial Traslado: "+forma.getMapaAtributosBusqueda("noTrasladoInicial");
	        	
	        //numero traslado final
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("noTrasladoFinal")+""))
	        	criterios+="  No. Final Traslado: "+forma.getMapaAtributosBusqueda("noTrasladoFinal");
	        
	        //Ranfo de fechas solicitud
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("fechaInicialSolicitud")+"") && UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("fechaFinalSolicitud")+""))
	        	criterios+=" Rango Fecha Solicitud: "+forma.getMapaAtributosBusqueda("fechaInicialSolicitud")+" - "+forma.getMapaAtributosBusqueda("fechaFinalSolicitud") ;
	        
	        //Ranfo de fechas despacho
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("fechaInicialDespacho")+"") && UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("fechaFinalDespacho")+""))
	        	criterios+=" Rango Fecha Solicitud: "+forma.getMapaAtributosBusqueda("fechaInicialDespacho")+" - "+forma.getMapaAtributosBusqueda("fechaFinalDespacho") ;
	        
	    	
	        //clase inventario
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("claseInventario")+"") && !(forma.getMapaAtributosBusqueda("claseInventario")+"").equals(ConstantesBD.codigoNuncaValido+""))
	        {
	        	HashMap tmp = new HashMap ();
	        	tmp.put("codigo", forma.getMapaAtributosBusqueda("claseInventario"));
	        	tmp.put("institucion", usuario.getCodigoInstitucion());
	        	criterios+=" Clase Inventario: "+UtilidadInventarios.obtenerClaseInventario(connection, tmp).get(0).get("nombre");
	        }
	        
	        //articulo
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("articulo")+""))
	        	criterios+=" Articulo: "+Utilidades.obtenerNombreArticulo(connection,Utilidades.convertirAEntero(forma.getMapaAtributosBusqueda("articulo")+""));
	        
	        	
	        //usuario solicita
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("usuarioSolicita")+"") && !(forma.getMapaAtributosBusqueda("usuarioSolicita")+"").equals("Seleccione"))
	        	criterios+=" Usuario Solicita: "+forma.getMapaAtributosBusqueda("usuarioSolicita");
	        
	               	
	        //usuario Despacha
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("usuarioDespacha")+"") && !(forma.getMapaAtributosBusqueda("usuarioDespacha")+"").equals("Seleccione"))
	        	criterios+=" Usuario Despacha: "+forma.getMapaAtributosBusqueda("usuarioDespacha");
	        
	        
	       	
	        //Estado
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("codEstado")+"") && !(forma.getMapaAtributosBusqueda("codEstado")+"").equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+=" Estado: "+forma.getMapaAtributosBusqueda("nomEstado");
	        
	     	//prioritario
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("prioridad")+""))
	        	criterios+=" Prioritario: "+(UtilidadTexto.getBoolean(forma.getMapaAtributosBusqueda("prioridad")+"")?"Si":"No");
	        
	     	
	        //Codigo tipo articulo
	        if (UtilidadCadena.noEsVacio(forma.getMapaAtributosBusqueda("tipoCodigoArticulo")+"") && !(forma.getMapaAtributosBusqueda("tipoCodigoArticulo")+"").equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+=" Codigo tipo Artiulo: "+ValoresPorDefecto.getIntegridadDominio(forma.getMapaAtributosBusqueda("tipoCodigoArticulo")+"");        
	        
	        return criterios;
	}
	
	
	
	
	
	public static ActionForward archivoPlano (Connection connection, ConsultaImpresionTrasladosForm forma,UsuarioBasico usuario, InstitucionBasica institucion,HttpServletRequest  request,ActionMapping mapping ) throws SQLException
	{
		HashMap tmp = new HashMap();
		String consulta="",titulo="";
		consulta= consultasReportes(forma.getMapaAtributosBusqueda());
		if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXAlmacen))
			titulo="Detallado-por-Almacen";
		else
			if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXClaseInventario))
				titulo="Detallado-por-ClaseInventario";
			else
				if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXTransaccion))
					titulo="Detallado-por-Transaccion";
		
		tmp.putAll(ejecutarConsulta(connection,consulta));
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
    		
    		forma.setOperacionTrue(true);
    		forma.setExisteArchivo(false);
    		UtilidadBD.abortarTransaccion(connection);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Inventarios", "error.manejoPacientes.rutaNoDefinida", true);
		}
		
		
		//arma el nombre del reporte
		String nombreReport=CsvFile.armarNombreArchivo(titulo, usuario);
		//se genera el documento con la informacion
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXAlmacen))
			{
				titulo="Detallado-por-Almacen";
				OperacionTrue=TxtFile.generarTxt(cargarDetalladoXAlmacen(connection, forma, institucion, usuario,tmp),nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
			}
			else
				if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXClaseInventario))
					{
						titulo="Detallado-por-ClaseInventario";
						OperacionTrue=TxtFile.generarTxt(cargarDetalladoXClaseInventario(connection, forma, institucion, usuario, tmp),nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
					}
				else
					if ((forma.getMapaAtributosBusqueda("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoXTransaccion))
					{
						titulo="Detallado-por-Transaccion";
						OperacionTrue=TxtFile.generarTxt(cargarDetalladoXTransaccion(connection, forma, institucion, usuario, tmp),nombreReport, ValoresPorDefecto.getReportPath()+path, ".csv");
					}
			
				
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
			return mapping.findForward("generarBusqueda");
	
}
    
    /**
     * Metodo encargado armar toda la consulta Detallada por almacen
     * @param vo
     * -----------------------------------
     * KEY'S DEL MAPA VO
     * -----------------------------------
     * -- codAlmacenDespacha
     * -- codAlmacenSolicita
     * -- noTrasladoInicial
     * -- noTrasladoFinal
     * -- fechaInicialSolicitud
     * -- fechaInicialDespacho
     * -- usuarioSolicita
     * -- usuarioDespacha
     * -- codEstado
     * -- prioridad
     * -- claseInventario
     * -- articulo
     * -- tipoCodigoArticulo
     * @return String con la consulta
     */
    public static String consultasReportes (HashMap vo)
    {
    	return consultaDao.consultasReportes(vo);
    }
    
    
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public static HashMap ejecutarConsulta (Connection connection,String consulta)
    {
    	return consultaDao.ejecutarConsulta(connection, consulta);
    }
    
    public static StringBuffer cargarDetalladoXAlmacen (Connection connection, ConsultaImpresionTrasladosForm forma,InstitucionBasica institucion,UsuarioBasico usuario,HashMap datos)
	{
		logger.info("\n entro a cargarDetalladoXAlmacen");
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
		cadena.append("DETALLADO POR ALMACEN \n\n");  
		//criterios de busqueda del reporte
		cadena.append(organizarCriterios(connection, forma, usuario)+"\n ");
		//----------------------------------------------------------------------------///
		 //se le ponen los titulos
		cadena.append("Fecha Sol, Almacén,Codigo,Descripción, Fecha Desp, Unid. Med, Cant Solicit, Cant Entreg, Diferenc \n");
		//se pone el primver valor del rompimiento
		cadena.append(datos.get("nom_almacen_despacha_0")+",,,,,,,\n" );
		
		logger.info("\n valor temp -->"+datos);
		
		int subtotal=0;
		int total=0;
		int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		for (int i=0;i<numReg;i++)
		{
			
			if (i>0 && !(datos.get("nom_almacen_despacha_"+i)+"").equals(datos.get("nom_almacen_despacha_"+(i-1))+""))
			{
				//se coloca el subtotal
				cadena.append("Sub Total,,,,,,"+subtotal+",,\n");
				//se pone el nombre del cambio de rompimiento
				cadena.append(datos.get("nom_almacen_despacha_"+i)+",,,,,,,\n" );
				
				total=total+subtotal;
				subtotal=0;
			}
			subtotal=subtotal+Utilidades.convertirAEntero(datos.get("cantidadsolicitada_"+i)+"");
			
			cadena.append(datos.get("fecha_solicitud_"+i)+", "+datos.get("nom_almacen_despacha_"+i)+", "+datos.get("codigo_articulo_"+i)+", "+
						  datos.get("descripcion_"+i)+", "+datos.get("fecha_despacho_"+i)+", "+datos.get("unidad_medida_"+i)+", "+
						  datos.get("cantidadsolicitada_"+i)+", "+datos.get("cantidaddespachada_"+i)+", "+datos.get("diferencia_"+i)+" \n");
		
			if (i==(numReg-1))
			{
				cadena.append("Sub Total,,,,,,"+subtotal+",,\n");
				cadena.append("Totales Finales ,,,,,"+(total+subtotal)+",,");
			}
			
		}
		return cadena;
	}
		
		public static StringBuffer cargarDetalladoXTransaccion (Connection connection, ConsultaImpresionTrasladosForm forma,InstitucionBasica institucion,UsuarioBasico usuario,HashMap datos)
		{
			logger.info("\n entro a cargarDetalladoXAlmacen");
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
			cadena.append("DETALLADO POR TRANSACCION \n\n");  
			//criterios de busqueda del reporte
			cadena.append(organizarCriterios(connection, forma, usuario)+"\n ");
			//----------------------------------------------------------------------------///
			 //se le ponen los titulos
			cadena.append("Cod, Almacén, No.Trans, Cod, C. Costo Solicitud, Fecha/Hora Solicitud, Cantidad Solicitada, Unid Med, Fecha/Hora Despacho, Cantidad Despachada \n");
			//se pone el primver valor del rompimiento
			cadena.append(datos.get("numero_traslado_0")+",,,,,,,,,\n" );
			
			logger.info("\n valor temp -->"+datos);
			
			int subtotal=0;
			int total=0;
			int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");
			for (int i=0;i<numReg;i++)
			{
				
				if (i>0 && !(datos.get("numero_traslado_"+i)+"").equals(datos.get("numero_traslado_"+(i-1))+""))
				{
					//se coloca el subtotal
					cadena.append("Sub Total,,,,,,"+subtotal+",,\n");
					//se pone el nombre del cambio de rompimiento
					cadena.append(datos.get("numero_traslado_"+i)+",,,,,,,,,\n" );
					
					total=total+subtotal;
					subtotal=0;
				}
				subtotal=subtotal+Utilidades.convertirAEntero(datos.get("cantidadsolicitada_"+i)+"");
				
				cadena.append(datos.get("codigo_almacen_despacha_"+i)+", "+datos.get("nom_almacen_despacha_"+i)+", "+datos.get("numero_traslado_"+i)+", "+
							  datos.get("codigo_almacen_solicita_"+i)+", "+datos.get("nom_almacen_solicita_"+i)+", "+datos.get("fecha_solicitud_"+i)+"  "+datos.get("hora_elaboracion_"+i)+", "+
							  datos.get("cantidadsolicitada_"+i)+", "+datos.get("unidad_medida_"+i)+", "+datos.get("fecha_despacho_"+i)+"  "+datos.get("hora_despacho_"+i)+", "+datos.get("cantidaddespachada_"+i)+" \n");
			
				if (i==(numReg-1))
				{
					cadena.append("Sub Total,,,,,,"+subtotal+",,\n");
					cadena.append("Totales Finales ,,,,,,"+(total+subtotal)+",,");
				}
				
			}
		
		return cadena;
	}
    
    
		public static StringBuffer cargarDetalladoXClaseInventario (Connection connection, ConsultaImpresionTrasladosForm forma,InstitucionBasica institucion,UsuarioBasico usuario,HashMap datos)
		{
			logger.info("\n entro a cargarDetalladoXAlmacen");
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
			cadena.append("DETALLADO POR CLASE DE INVENTARIO \n\n");  
			//criterios de busqueda del reporte
			cadena.append(organizarCriterios(connection, forma, usuario)+"\n ");
			//----------------------------------------------------------------------------///
			 //se le ponen los titulos
			cadena.append("Cod, Almacén Despacha, Cod, Almacén Solicita, Cod, Clase Inventario, U.  Med, Cant. Total, Val. Unit, Valor Total \n");
			//se pone el primver valor del rompimiento
			cadena.append(datos.get("nombre_clase_0")+",,,,,,,,,\n" );
			
			logger.info("\n valor temp -->"+datos);
			
			int subtotal1=0;
			int subtotal2=0;
			int total1=0;
			int total2=0;
			int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");
			for (int i=0;i<numReg;i++)
			{
				
				if (i>0 && !(datos.get("nombre_clase_"+i)+"").equals(datos.get("nombre_clase_"+(i-1))+""))
				{
					//se coloca el subtotal
					cadena.append("Sub Total,,,,,,,"+subtotal1+",,"+subtotal2+"\n");
					//se pone el nombre del cambio de rompimiento
					cadena.append(datos.get("nombre_clase_"+i)+",,,,,,,,,\n" );
					
					total1=total1+subtotal1;
					total2=total2+subtotal2;
					subtotal1=0;
					subtotal2=0;
				}
				subtotal1=subtotal1+Utilidades.convertirAEntero(datos.get("cantidaddespachada_"+i)+"");
				subtotal2=subtotal2+Utilidades.convertirAEntero(datos.get("valor_total_"+i)+"");
				
				cadena.append(datos.get("codigo_almacen_despacha_"+i)+", "+datos.get("nom_almacen_despacha_"+i)+", "+datos.get("codigo_almacen_solicita_"+i)+", "+
							  datos.get("nom_almacen_solicita_"+i)+", "+datos.get("codigo_clase_"+i)+", "+datos.get("nombre_clase_"+i)+", "+
							  datos.get("unidad_medida_"+i)+", "+datos.get("cantidaddespachada_"+i)+", "+datos.get("valor_unitario_"+i)+", "+datos.get("valor_total_"+i)+" \n");
			
				if (i==(numReg-1))
				{
					cadena.append("Sub Total,,,,,,,"+subtotal1+",,"+subtotal2+"\n");
					cadena.append("Totales Finales ,,,,,,,"+(total1+subtotal1)+",,"+(total2+subtotal2)+"\n");
				}
				
			}
		
		return cadena;
	}
    
    
    /****************************************************
     * Fin Modificacion por anexo 632
     ****************************************************/
 //--------------------------------------------------------------------------------------------------
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}
