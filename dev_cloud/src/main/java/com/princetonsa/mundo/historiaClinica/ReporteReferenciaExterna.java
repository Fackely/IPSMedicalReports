package com.princetonsa.mundo.historiaClinica;

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
import util.InfoDatosString;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.ReporteReferenciaExternaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ReporteReferenciaExternaDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


/**
 * Anexo 679
 * Creado el 12 de Septiembre de 2008
 * @author Ing. Felipe Perez
 * @mail lfperez@princetonsa.com
 */

public class ReporteReferenciaExterna
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	public static Logger logger = Logger.getLogger(ReporteReferenciaExterna.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 *----------------------------------------------*/
	
	//indices para criterios de busqueda
	public static final String [] indicesCriterios={"centroAtencion","institucionSolicita","fechaInicial",
		"fechaFinal","tipoReporte","estadoReferencia","institucion","tipoSalida"};
	
	/**
	 * indices para el manejo de el resultado
	 */
	
	public static String [] indicesResultado = {"codigo_aceptacion_","fecha_","hora_","idpaciente_","paciente_","edad_","diagnostico_",
		"institucion_remite_","nombre_quien_remite_","nombre_acepta_admin_","nombre_acepta_medica_","estado_","hora_aceptacion_"};
	
	/**
	 * Se inicializa el Dao
	 */
	
/**
 * private static ReporteReferenciaExternaDao reporteReferenciaExternaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteReferenciaExternaDao();
	}
 */
	
	/**
	 * Reporte referencia externa Dao 
	 */
	private static ReporteReferenciaExternaDao reporteReferenciaExternaDao() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteReferenciaExternaDao();
	}
	
	/**
	 * Método del estado empezar que es quien inicia el flujo de datos
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void empezar (Connection connection,ReporteReferenciaExternaForm forma,UsuarioBasico usuario)
	{
		//cargar los centros de atencion
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),
				ConstantesBD.acronimoSi));
		//postulamos el centro de atencion del usuario en session
		InfoDatosString ca= new InfoDatosString();
		ca=Utilidades.obtenerInstitucionSirCentroAtencion(connection, usuario.getCodigoCentroAtencion());
		
		if (!ca.getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
			forma.setCriterios("centroAtencion",usuario.getCodigoCentroAtencion());
		else
		{
			if(!(forma.getCriterios("centroAtencion")+"").equals("-2"))
			{
				logger.info("===> Entré al centro de atención diferente a todos '-2'");
				//forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),
				//ConstantesBD.acronimoSi));
				forma.setCriterios("centroAtencion",ConstantesBD.codigoNuncaValido);
			}
			else
			{
				logger.info("===> Los centros de atención son TODOS "+forma.getCriterios("centroAtencion")+"");
				//forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),
				//ConstantesBD.acronimoSi));
			}
		}
		
		//forma.setCriterios("centroAtencion",ConstantesBD.codigoNuncaValido);
		//se carga la institucion sirc
		cargarInstitucionesSirc(connection, forma, usuario);
		
		//cargar los centros de atencion
		//forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
		//postulamos el centro de atencion del usuario en session
		//forma.setCriterios("centroAtencion",usuario.getCodigoCentroAtencion());
		//se carga la institucion sirc
		//cargarInstitucionesSirc(connection, forma, usuario);
		
	}
	
	/**
	 * Método encargado de cargar las instituciones SIRC
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarInstitucionesSirc (Connection connection,ReporteReferenciaExternaForm forma,UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap ();
		criterios.put("institucion", usuario.getCodigoInstitucion());
		criterios.put("activo", ConstantesBD.acronimoSi);
		criterios.put("tipoinstreferencia", ConstantesBD.acronimoSi);
		
		if(forma.getCriterios("centroAtencion").equals("-2"))
		{
			logger.info("===> El cliente ha seleccionado divisar todos los centros de atención: "+forma.getCriterios("centroAtencion"));
			logger.info("===> No se cargará al indice el campo de centro de atención");
			
		}
		else
		{
			criterios.put("centroAtencion", forma.getCriterios("centroAtencion"));
			logger.info("===> Se cargará al indice el campo de centro de atención: "+forma.getCriterios("centroAtencion"));
		}
		
		forma.setInstitucionesSirc(UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, criterios));
	}
	
	/**
	 * Metodo encargado de activar el estado generar
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @throws SQLException 
	 */
	public static ActionForward generar(Connection connection,ReporteReferenciaExternaForm forma,UsuarioBasico usuario, 
			HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion) throws SQLException
	{
		forma.setCriterios("institucion", usuario.getCodigoInstitucion());
		
		if ((forma.getCriterios("tipoSalida")+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return generarReporte(connection, usuario, forma, request, mapping);
		else
			//mapping.findForward("principal");
			return archivoPlano(connection, forma, usuario, institucion, request, mapping);
	}
	
	/**
	 * Metodo encargado de generar el archivo plano.
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param institucion
	 * @param request
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	public static ActionForward archivoPlano (Connection connection, ReporteReferenciaExternaForm forma,UsuarioBasico usuario, 
			InstitucionBasica institucion,HttpServletRequest  request,ActionMapping mapping ) throws SQLException
	{
		HashMap tmp = new HashMap();
		tmp.putAll(consultaReporteReferenciaExterna(connection, forma.getCriterios()));
		boolean OperacionTrue=false,existeTxt=false;
		int ban=ConstantesBD.codigoNuncaValido;
		//Se llama al garbage collector
		System.gc();
		
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(connection);
		
		//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
		String path = "historiaClinica/ReferenciaContraReferencia/";
		logger.info("====>Path Valor por Defecto: "+path);
		//Validamos si el path esta vacio o lleno
    	if(UtilidadTexto.isEmpty(path))
		{
    		forma.setOperacionTrue(false);
    		forma.setExisteArchivo(false);
    		UtilidadBD.abortarTransaccion(connection);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Inventarios", 
	    			"error.historiaClinica.rutaNoDefinida", true);
		}
		
		
		//Arma el nombre del reporte
		String nombreReport=CsvFile.armarNombreArchivo("Reporte-Referencia-Externa-Pacientes-Con-Referencia-Externa", usuario);
		//Se genera el documento con la informacion
		logger.info("Mira Aqui Pipe !!! -> Nombre Report: "+nombreReport);
		logger.info("Mira Aqui Pipe !!! -> Este es el patht: "+path);
		logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			if (UtilidadTexto.getBoolean(forma.getCriterios(indicesCriterios[3])+""))
				//OperacionTrue=TxtFile.generarTxt(cargarMapa(connection, forma.getCriterios(),usuario,tmp));
				logger.info("\n archivo plano -->"+cargarMapa(connection, forma, usuario, tmp, institucion));
				OperacionTrue=TxtFile.generarTxt(cargarMapa(connection, forma, usuario, tmp, institucion),nombreReport,
						ValoresPorDefecto.getReportPath()+"historiaClinica/ReferenciaContraReferencia/",".csv");
			
				logger.info("\n se creo ? --> "+OperacionTrue);
			
		}
		
		if(Utilidades.convertirAEntero(tmp.get("numRegistros")+"")==0)
		{
			forma.setMensaje(true);
		}
		
		if (OperacionTrue)
		{
			//se genera el archivo en formato Zip
			ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+path+nombreReport+".zip"+" "+
					ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
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
		return mapping.findForward("principal");
	}

	/**
	 * Metodo encargado de organizar los datos con ","
	 * para el reporte
	 * @param datos
	 * @param criterios
	 * @return
	 */
	public static StringBuffer cargarMapa(Connection connection,ReporteReferenciaExternaForm forma, UsuarioBasico usuario,HashMap datos, 
			InstitucionBasica institucion)
	{
		StringBuffer cadena = new StringBuffer();
		Vector criterios = new Vector();
		HashMap tmp = new HashMap();
		tmp.putAll(consultaReporteReferenciaExterna(connection, forma.getCriterios()));
		//Razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//Nit
		cadena.append(institucion.getNit()+"\n");
		//Direccion
		cadena.append(institucion.getDireccion()+"\n");
		//Telefono
		cadena.append(institucion.getTelefono()+"\n");
		//Titulo del reporte
		cadena.append("PACIENTES CON REFERENCIA EXTERNA \n\n");  
		cadena.append(criteriosOrganizados(connection, forma, usuario)+"\n");
		
		//Aqui van los titulos de las celdas seguidos en una misma cadena
		if ((forma.getCriterios("estadoReferencia")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			logger.info("\n ***** Estado de referencia es Todos-->" +
			forma.getCriterios("estadoReferencia")+"");	
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			cadena.append("CODIGO ACEPTACION,FECHA,HORA,ID,PACIENTE,EDAD,DIAGNOSTICO,INSTITUCION QUE REMITE,NOMBRE QUIEN REMITE," +
					"NOMBRE ACEPTA ADMIN,NOMBRE ACEPTA MED,ESTADO,HORA ACEPTACION\n");
			
			//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano		
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				logger.info("\n El codigo de aceptacion es -->"+datos.get("codigoAceptacion_"+i));
				cadena.append(datos.get("codigoAceptacion_"+i)+","+datos.get("fecha_"+i)+","+datos.get("hora_"+i)+","
						+datos.get("idpaciente_"+i)+","+datos.get("paciente_"+i)+","+datos.get("edad_"+i)+","
						+datos.get("diagnostico_"+i)+","+datos.get("institucionRemite_"+i)+","+datos.get("nombreQuienRemite_"+i)+","
						+datos.get("nombreAceptaAdmin_"+i)+","+datos.get("nombreAceptaMedica_"+i)+","+datos.get("estado_"+i)+","
						+datos.get("horaAceptacion_"+i)+"\n");
			}
		}
		
		else
		{
			logger.info("\n ***** Estado de referencia es Admitido, En Trámite, Finalizado o Anulado-->" +
			forma.getCriterios("estadoReferencia")+"");	
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			cadena.append("CODIGO ACEPTACION,FECHA,HORA,ID,PACIENTE,EDAD,DIAGNOSTICO,INSTITUCION QUE REMITE,NOMBRE QUIEN REMITE," +
					"NOMBRE ACEPTA ADMIN,NOMBRE ACEPTA MED,HORA ACEPTACION\n");
			
			//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				logger.info("\n El codigo de aceptacion es -->"+datos.get("codigoAceptacion_"+i));
				cadena.append(datos.get("codigoAceptacion_"+i)+","+datos.get("fecha_"+i)+","+datos.get("hora_"+i)+","
						+datos.get("idpaciente_"+i)+","+datos.get("paciente_"+i)+","+datos.get("edad_"+i)+","
						+datos.get("diagnostico_"+i)+","+datos.get("institucionRemite_"+i)+","+datos.get("nombreQuienRemite_"+i)+","
						+datos.get("nombreAceptaAdmin_"+i)+","+datos.get("nombreAceptaMedica_"+i)+","+datos.get("horaAceptacion_"+i)+"\n");
			}
		}
		//cadena.append("CODIGO ACEPTACION,FECHA,HORA,ID,PACIENTE,EDAD,DIAGNOSTICO,INSTITUCION QUE REMITE,NOMBRE QUIEN REMITE,
		//NOMBRE ACEPTA ADMIN,NOMBRE ACEPTA MED,ESTADO,HORA ACEPTACION\n");
		//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano		
		/*
		 * for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
		 * {
			logger.info("\n El codigo de aceptacion es -->"+datos.get("codigoAceptacion_"+i));
			cadena.append(datos.get("codigoAceptacion_"+i)+","+datos.get("fecha_"+i)+","+datos.get("hora_"+i)+","
					+datos.get("idpaciente_"+i)+","+datos.get("paciente_"+i)+","+datos.get("edad_"+i)+","
					+datos.get("diagnostico_"+i)+","+datos.get("institucionRemite_"+i)+","+datos.get("nombreQuienRemite_"+i)+","
					+datos.get("nombreAceptaAdmin_"+i)+","+datos.get("nombreAceptaMedica_"+i)+","+datos.get("estado_"+i)+","
					+datos.get("horaAceptacion_"+i)+"\n");
			}
		 */
		
		return cadena;
	}
	/**
	 * Metodo encargado de generar reporte en pdf.
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 * @throws SQLException 
	 */
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,ReporteReferenciaExternaForm forma, 
			HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		//Se hace una llamada al recolector de basura
		System.gc();
		
		DesignEngineApi comp;
		HashMap tmp = new HashMap();
		String newQuery="";
		/*
		 * Se valida si el usuario seleccionó un estado
		 * Entra al if cuando no ha seleccionado ningún estado
		 */
		if (UtilidadCadena.noEsVacio(forma.getCriterios("estadoReferencia")+"") && 
			!(forma.getCriterios("estadoReferencia")).equals(ConstantesBD.codigoNuncaValido+""))
		{
			logger.info("\n El usuario seleccionó un estado !!! --->>>"+forma.getCriterios("estadoReferencia")+"");
			tmp.putAll(consultaReporteReferenciaExternaConEstado(connection, forma.getCriterios()));
		}
		else
		{
			logger.info("\n El usuario NO seleccionó un estado !!! --->>>"+forma.getCriterios("estadoReferencia")+"");
			tmp.putAll(consultaReporteReferenciaExterna(connection, forma.getCriterios()));
		}
			
		//reporte="ReporteReferenciaExterna.rptdesign";
		//Llamamos al reporte
		//comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/",reporte);
		/* Si el  Estado de referencia es Solicitado, Finalizado, Anulado o En Trámite, entonces
		 * hacemos a Dataset = ReferenciaInterna, el cual contiene la columna estado
		 */
		if ((forma.getCriterios("estadoReferencia")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			//LLamamos al reporte
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/",
					"ReporteReferenciaExterna.rptdesign");
			logger.info("\n ***** Estado de referencia es Todos-->" +
			forma.getCriterios("estadoReferencia")+"");	
			//DataSet="ReferenciaInterna";
			comp.obtenerComponentesDataSet("ReferenciaExterna");
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			//reporte="ReporteReferenciaInterna.rptdesign";
		}
		
		else
		/* Esto es para el caso en el cual no se halla seleccionado ningun estado de la referencia;
		 * se hace Dataset = ReferenciaInternaSinEstado
		 */ 
		{
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/",
					"ReporteReferenciaExternaSinEstado.rptdesign");
			logger.info("\n ***** Estado de referencia es Solicitado, Finalizado, Anulado o En Trámite -->"+
			forma.getCriterios("estadoReferencia")+"");
			//DataSet="ReferenciaInternaSinEstado";
			comp.obtenerComponentesDataSet("ReferenciaExternaSinEstado");
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			//reporte="ReporteReferenciaInternaSinEstado.rptdesign";
		}
		
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, connection, usuario, forma, request);
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		//comp.obtenerComponentesDataSet("ReferenciaExterna");
		//Modificamos el DataSet con las validaciones comunes para todos
        //String newQuery = comp.obtenerQueryDataSet().replace("WHERE", obtenerWhere(forma.getCriterios()));
		
		if ((forma.getCriterios("estadoReferencia")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			logger.info("\n ***** Validación para enviar los datos de la consulta por debajo...\n" +
					"Estado de referencia es Todos-->" +
					forma.getCriterios("estadoReferencia")+"");
			newQuery = obtenerConsulta(connection, forma.getCriterios());
		}
		
		else
		{
			logger.info("\n ***** Validación para enviar los datos de la consulta por debajo...\n" +
					"Estado de referencia es Solicitado, Finalizado, Anulado o En Trámite-->" +
					forma.getCriterios("estadoReferencia")+"");
			newQuery = obtenerConsultaConEstado(connection, forma.getCriterios());
		}
		
		//String newQuery = obtenerConsulta(connection, forma.getCriterios());
		//String newQuery = comp.obtenerQueryDataSet().replace("WHERE", " WHERE 1=1; ");
        logger.info("\n\n\n\n=====>Consulta en el BIRT Detallado por Tipo Transaccion: "+newQuery);
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
	/**
	 * Método encargado de obtener la consulta
	 * @param connection
	 * @param criterios
	 * @return
	 */
	private static String obtenerConsulta(Connection connection, HashMap criterios)
	{
		return reporteReferenciaExternaDao().obtenerConsulta(connection, criterios);
	}
	
	/**
	 * Método encargado de obtener la consulta con estado
	 * @param connection
	 * @param criterios
	 * @return
	 */
	private static String obtenerConsultaConEstado(Connection connection, HashMap criterios)
	{
		return reporteReferenciaExternaDao().obtenerConsultaConEstado(connection, criterios);
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
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, ReporteReferenciaExternaForm forma, 
			HttpServletRequest request)
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
        comp.insertLabelInGridPpalOfHeader(1,1, "PACIENTES CON REFERENCIA EXTERNA");
        comp.insertLabelInGridPpalOfHeader(3, 0, criteriosOrganizados(connection, forma, usuario));
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	/**
	 * Método encargado de organizar los criterios
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static String criteriosOrganizados (Connection connection, ReporteReferenciaExternaForm forma,UsuarioBasico usuario)
	{
		String criterios="";
        
	    if (UtilidadCadena.noEsVacio(forma.getCriterios("centroAtencion")+""))
        {
        	if ((forma.getCriterios("centroAtencion")+"").equals("-2"))
	        	criterios+="Centros Atención: Todos";
        	else
        		criterios+="Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, 
        				Utilidades.convertirAEntero(forma.getCriterios("centroAtencion")+""));
        	
        }

        if(UtilidadCadena.noEsVacio(forma.getCriterios("fechaInicial")+"") && UtilidadCadena.noEsVacio(forma.getCriterios("fechaFinal")+""))
        	criterios+="  Periodo: "+forma.getCriterios("fechaInicial")+" - "+forma.getCriterios("fechaFinal");
        
        if (UtilidadCadena.noEsVacio(forma.getCriterios("institucionSolicita")+"") && 
        	!(forma.getCriterios("institucionSolicita")+"").equals(ConstantesBD.codigoNuncaValido+""))
        {
        	HashMap tmp = new HashMap ();
        	tmp.put("institucion", usuario.getCodigoInstitucion());
        	tmp.put("codigo", forma.getCriterios("institucionSolicita")); 
        	logger.info("\n la institucion sirc es -->"+UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, tmp));
        	/*
        	 * UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, tmp) devuelve un array list de hashmap
        	 * se utiliza el .get(0) para convertirlo en hashmap y traer el primero de ellos
        	 * y el .get("valor") trae el "valor que necesite de ese hashmap
        	 * Explicado por Jhony Duque"
        	 */
        	criterios+="  Institución Solicita: "+
        	"("+(UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, tmp).get(0).get("codigo"))+") "+
        	(UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, tmp).get(0).get("descripcion"));
        }
        
        if (UtilidadCadena.noEsVacio(forma.getCriterios("estadoReferencia")+"") && 
        	!(forma.getCriterios("estadoReferencia")).equals(ConstantesBD.codigoNuncaValido+""))
        	criterios+="  Estado Referencia: "+ValoresPorDefecto.getIntegridadDominio(forma.getCriterios("estadoReferencia")+"");
        
        return criterios;
	}
	
	/**
	 * Metodo encargado de consultar la informacion pedida en
	 * Reporte Referencia Externa en Historia Clinica, en las tablas:
	 * "referencia"
	 * "servic_instit_referencia"
	 * "cuentas"
	 * "egresos"
	 * "det_servicios_sirc"
	 * 
	 * @author Felipe Perez
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- centroAtencion		--> Requerido
	 * -- institucionSolicita 	--> No Requerido
	 * -- fechaInicial			--> Requerido
	 * -- fechaFinal			--> Requerido
	 * -- estadoReferencia		--> No Requerido
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------- 
	 * codigo_aceptacion
	 * fecha
	 * hora
	 * idpaciente
	 * paciente
	 * edad_diagnostico
	 * diagnostico
	 * institucion_remite
	 * nombre_quien_remite
	 * nombre_acepta_admin
	 * nombre_acepta_medica
	 * estado
	 * hora_aceptacion
	 */
	public static HashMap consultaReporteReferenciaExterna (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a consultaReporteReferenciaExterna ");
		return reporteReferenciaExternaDao().consultaReporteReferenciaExterna(connection, criterios);
	}

	/**
	 * Método encargado de consultar el reporte referencia externa con estado
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaReporteReferenciaExternaConEstado (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a consultaReporteReferenciaExternaConEstado ");
		return reporteReferenciaExternaDao().consultaReporteReferenciaExternaConEstado(connection, criterios);
	}
	
	/**
	 * Método encargado de obtener el where
	 * @param criterios
	 * @param bandera
	 * @return
	 */
	public static String obtenerWhere (HashMap criterios, int bandera)
	{
		logger.info("\n entre a obtenerWhere ");
		return reporteReferenciaExternaDao().obtenerWhere(criterios, bandera);
	}
}