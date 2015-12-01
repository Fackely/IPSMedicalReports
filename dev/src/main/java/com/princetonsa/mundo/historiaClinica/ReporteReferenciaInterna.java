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
import com.princetonsa.actionform.historiaClinica.ReporteReferenciaInternaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ReporteReferenciaInternaDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Anexo 678
 * Creado el 29 de Septiembre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class ReporteReferenciaInterna
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	public static Logger logger = Logger.getLogger(ReporteReferenciaInterna.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 *----------------------------------------------*/
	
	//indices para criterios de busqueda
	public static final String [] indicesCriterios={"centroAtencion","institucionSolicitada","fechaInicial",
		"fechaFinal","tipoReporte","estadoReferencia","institucion","tipoSalida"};
	
	/**
	 * indices para el manejo de el resultado
	 */
	public static String [] indicesResultado = {
		"paciente_",
		"edad_",
		"diagnostico_",
		"medicoRemite_",
		"convenio_",
		"instRecibe_",
		"fechaSolicitud_",
		"horaSolicitud_",
		"fechaAceptacion_",
		"horaAceptacion_",
		"fechaSalida_",
		"horaSalida_",
		"cama_",
		"especialidad_",
		"examen_"};
	
	/**
	 * Se inicializa el Dao
	 */
	private static ReporteReferenciaInternaDao reporteReferenciaInternaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteReferenciaInternaDao();
	}
	
	/**
	 * Método empezar
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void empezar (Connection connection,ReporteReferenciaInternaForm forma,UsuarioBasico usuario)
	{
		/*
		 * Se cargan los centros de atención en un HashMap
		 */
		forma.setCentrosDeAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		
		/*
		 * Postulamos el centro de atencion del usuario en session
		 */
		InfoDatosString ca= new InfoDatosString();
		ca=Utilidades.obtenerInstitucionSirCentroAtencion(connection, usuario.getCodigoCentroAtencion());
		if (!ca.getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
			forma.setCriterios("centroAtencion",usuario.getCodigoCentroAtencion());
		else
			forma.setCriterios("centroAtencion",usuario.getCodigoCentroAtencion());
		
		/*
		 * Se carga la institucion sirc
		 */
		cargarInstitucionesSirc(connection, forma, usuario);
	}
	
	/**
	 * Método encargado de cargar las instituciones sirc
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarInstitucionesSirc (Connection connection,ReporteReferenciaInternaForm forma,UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap ();
		criterios.put("institucion", usuario.getCodigoInstitucion());
		criterios.put("activo", ConstantesBD.acronimoSi);
		criterios.put("tipoinstreferencia", ConstantesBD.acronimoSi);
		criterios.put("centroAtencion", forma.getCriterios("centroAtencion"));
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
	public static ActionForward generar(Connection connection,
			ReporteReferenciaInternaForm forma,
			UsuarioBasico usuario, 
			HttpServletRequest request, 
			ActionMapping mapping, 
			InstitucionBasica institucion) throws SQLException
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
	public static ActionForward archivoPlano (Connection connection, 
			ReporteReferenciaInternaForm forma,
			UsuarioBasico usuario, 
			InstitucionBasica institucion,
			HttpServletRequest  request,
			ActionMapping mapping ) throws SQLException
	{
		logger.info("*********************************************Entre a Action Forward: ");
		HashMap tmp = new HashMap();
		tmp.putAll(consultaReporteReferenciaInterna(connection, forma.getCriterios()));
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
    		logger.info("====>Entre a Utlidad Texto: "+path);
    		forma.setOperacionTrue(false);
    		forma.setExisteArchivo(false);
    		UtilidadBD.abortarTransaccion(connection);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Inventarios", 
	    			"error.historiaClinica.rutaNoDefinida", true);
		}
		
		//Arma el nombre del reporte
    	String nombreReport="";
    	if((forma.getCriterios("tipoReporte")+"").equals("PROI"))
        {
        	logger.info("***** Nombre del reporte, PROI !!! >>> "+forma.getCriterios("tipoReporte")+"");
        	nombreReport=CsvFile.armarNombreArchivo("Reporte-Referencia-Externa-Pacientes-Referidos-A-Otra-Institucion", usuario);
        }
    	
        if((forma.getCriterios("tipoReporte")+"").equals("PRPE"))
        {
        	logger.info("***** Nombre del reporte, PRPE !!! >>> "+forma.getCriterios("tipoReporte")+"");
        	nombreReport=CsvFile.armarNombreArchivo("Reporte-Referencia-Externa-Pacientes-Referidos-Para-Examenes", usuario);
        }
        
		//String nombreReport=CsvFile.armarNombreArchivo("Reporte-Referencia-Interna", usuario);
		//Se genera el documento con la informacion
		logger.info("Mira Aqui Pipe !!! -> Nombre Report: "+nombreReport);
		logger.info("Mira Aqui Pipe !!! -> Este es el patht: "+path);
		logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			logger.info("Mira Aqui Pipe !!! -> Entre a convertirAEntero: "+path);
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
	public static StringBuffer cargarMapa(Connection connection,ReporteReferenciaInternaForm forma, 
			UsuarioBasico usuario,HashMap datos, InstitucionBasica institucion)
	{
		StringBuffer cadena = new StringBuffer();
		HashMap tmp = new HashMap();
		tmp.putAll(consultaReporteReferenciaInterna(connection, forma.getCriterios()));
		//Razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//Nit
		cadena.append(institucion.getNit()+"\n");
		//Direccion
		cadena.append(institucion.getDireccion()+"\n");
		//Telefono
		cadena.append(institucion.getTelefono()+"\n");
		
		//Titulo del reporte
		if((forma.getCriterios("tipoReporte")+"").equals("PROI"))
        {
        	logger.info("***** Titulo del reporte PROI !!! >>> "+forma.getCriterios("tipoReporte")+"");
        	cadena.append("PACIENTES REFERIDOS A OTRA INSTITUCIÓN \n\n");
        }
		
        if((forma.getCriterios("tipoReporte")+"").equals("PRPE"))
        {
        	logger.info("***** Titulo del reporte PRPE !!! >>> "+forma.getCriterios("tipoReporte")+"");
        	cadena.append("PACIENTES REFERIDOS PARA EXÁMENES \n\n");
        }
        
		//cadena.append("REPORTE REFERENCIA INTERNA \n\n");  
		cadena.append(criteriosOrganizados(connection, forma, usuario)+"\n");
		//Se agregan tantas comas para la separacion de espacion para las columnas
		cadena.append(",,,,,,,,,,,,MOTIVO REFERENCIA\n");
		
		//Aqui van los titulos de las celdas seguidos en una misma cadena
		if ((forma.getCriterios("estadoReferencia")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			logger.info("\n ***** Estado de referencia es Todos-->" +
			forma.getCriterios("estadoReferencia")+"");	
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			cadena.append("PACIENTE,EDAD,DIAGNOSTICO,MEDICO REMITE,CONVENIO,INST RECIBE,FECHA SOLICITUD,HORA SOLICITUD,FECHA ACEPTACION," +
					"HORA ACEPTACION,FECHA SALIDA,HORA SALIDA,ESTADO,CAMA,ESPECIALIDAD,EXAMEN\n");
			
			//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano		
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				//","","","","","","","","","","","","","","","otros_"};
				cadena.append(datos.get("paciente_"+i)+","+datos.get("edad_"+i)+","+datos.get("diagnostico_"+i)+","
						+datos.get("medicoRemite_"+i)+","+datos.get("convenio_"+i)+","+datos.get("institucionRecibe_"+i)+","
						+datos.get("fechaSolicitud_"+i)+","+datos.get("horaSolicitud_"+i)+","+datos.get("fechaAceptacion_"+i)+","
						+datos.get("horaAceptacion_"+i)+","+datos.get("fechaSalida_"+i)+","+datos.get("horaSalida_"+i)+","
						+datos.get("estado_"+i)+","+datos.get("cama_"+i)+","+datos.get("especialidad_"+i)+","+datos.get("examen_"+i)+"\n");
			}
		}
		
		else
		{
			logger.info("\n ***** Estado de referencia es Admitido, En Trámite, Finalizado o Solicitado-->" +
			forma.getCriterios("estadoReferencia")+"");	
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			cadena.append("PACIENTE,EDAD,DIAGNOSTICO,MEDICO REMITE,CONVENIO,INST RECIBE,FECHA SOLICITUD,HORA SOLICITUD,FECHA ACEPTACION," +
					"HORA ACEPTACION,FECHA SALIDA,HORA SALIDA,CAMA,ESPECIALIDAD,EXAMEN\n");
			
			//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano		
			for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
			{
				//","","","","","","","","","","","","","","","otros_"};
				cadena.append(datos.get("paciente_"+i)+","+datos.get("edad_"+i)+","+datos.get("diagnostico_"+i)+","
						+datos.get("medicoRemite_"+i)+","+datos.get("convenio_"+i)+","+datos.get("institucionRecibe_"+i)+","
						+datos.get("fechaSolicitud_"+i)+","+datos.get("horaSolicitud_"+i)+","+datos.get("fechaAceptacion_"+i)+","
						+datos.get("horaAceptacion_"+i)+","+datos.get("fechaSalida_"+i)+","+datos.get("horaSalida_"+i)+","
						+datos.get("cama_"+i)+","+datos.get("especialidad_"+i)+","+datos.get("examen_"+i)+"\n");
			}
		}
		
		//cadena.append("PACIENTE,EDAD,DIAGNOSTICO,MEDICO REMITE,CONVENIO,INST RECIBE,FECHA SOLICITUD,HORA SOLICITUD,FECHA ACEPTACION,
		//HORA ACEPTACION,FECHA SALIDA,HORA SALIDA,ESTADO,CAMA,ESPECIALIDAD,EXAMEN\n");
		//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano		
		/*
		 * for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
		 * {
			//","","","","","","","","","","","","","","","otros_"};
			cadena.append(datos.get("paciente_"+i)+","+datos.get("edad_"+i)+","+datos.get("diagnostico_"+i)+","
					+datos.get("medicoRemite_"+i)+","+datos.get("convenio_"+i)+","+datos.get("institucionRecibe_"+i)+","
					+datos.get("fechaSolicitud_"+i)+","+datos.get("horaSolicitud_"+i)+","+datos.get("fechaAceptacion_"+i)+","
					+datos.get("horaAceptacion_"+i)+","+datos.get("fechaSalida_"+i)+","+datos.get("horaSalida_"+i)+","
					+datos.get("estado_"+i)+datos.get("cama_"+i)+","+datos.get("especialidad_"+i)+","+datos.get("examen_"+i)+"\n");
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
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,ReporteReferenciaInternaForm forma, 
			HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		//Se hace una llamada al recolector de basura
		System.gc();
		DesignEngineApi comp;
		HashMap tmp = new HashMap();
		tmp.putAll(consultaReporteReferenciaInterna(connection, forma.getCriterios()));
		
        //Imprimimos el encabezado del reporte
		//comp.obtenerComponentesDataSet(DataSet);
		/* Si el  Estado de referencia es Solicitado, Finalizado, Anulado o En Trámite, entonces
		 * hacemos a Dataset = ReferenciaInterna, el cual contiene la columna estado
		 */
		if ((forma.getCriterios("estadoReferencia")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			//LLamamos al reporte
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/",
					"ReporteReferenciaInterna.rptdesign");
			logger.info("\n ***** Estado de referencia es Todos-->" +
			forma.getCriterios("estadoReferencia")+"");	
			//DataSet="ReferenciaInterna";
			comp.obtenerComponentesDataSet("ReferenciaInterna");
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			//reporte="ReporteReferenciaInterna.rptdesign";
		}
		else
			/* Esto es para el caso en el cual no se halla seleccionado ningun estado de la referencia;
			 * se hace Dataset = ReferenciaInternaSinEstado
			 */ 
		{
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/",
					"ReporteReferenciaInternaSinEstado.rptdesign");
			logger.info("\n ***** Estado de referencia es Solicitado, Finalizado, Anulado o En Trámite -->"+
			forma.getCriterios("estadoReferencia")+"");
			//DataSet="ReferenciaInternaSinEstado";
			comp.obtenerComponentesDataSet("ReferenciaInternaSinEstado");
			logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
			//reporte="ReporteReferenciaInternaSinEstado.rptdesign";
		}
		
		armarEncabezado(comp, connection, usuario, forma, request);
		//se evalue el si tiene o no rompimiento
		//logger.info("\n con rompimiento "+forma.getCriterios(indicesCriterios[3]));
		//Modificamos el DataSet con las validaciones comunes para todos
        String newQuery = comp.obtenerQueryDataSet().replace("WHERE", obtenerWhere(forma.getCriterios()));
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
	
	/**
	 * Metodo encargado de organizar el encabezado 
	 * para el birt
	 * @param comp
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, 
			ReporteReferenciaInternaForm forma, HttpServletRequest request)
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
        //Se hace la validación para el titulo del reporte
        logger.info("***** Aqui está el tipo de reporte !!! >>> "+forma.getCriterios("tipoReporte")+"");

        if((forma.getCriterios("tipoReporte")+"").equals("PROI"))
        {
        	logger.info("***** El tipo de reporte, debe de ser PROI !!! >>> "+forma.getCriterios("tipoReporte")+"");
        	comp.insertLabelInGridPpalOfHeader(1,1, "PACIENTES REFERIDOS A OTRA INSTITUCIÓN");
        }
        
        if((forma.getCriterios("tipoReporte")+"").equals("PRPE"))
        {
        	logger.info("***** El tipo de reporte, debe de ser PRPE !!! >>> "+forma.getCriterios("tipoReporte")+"");
        	comp.insertLabelInGridPpalOfHeader(1,1, "PACIENTES REFERIDOS PARA EXÁMENES");
        }
        
        comp.insertLabelInGridPpalOfHeader(3, 0, criteriosOrganizados(connection, forma, usuario)); 
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	/**
	 * Metodo encargado de organizar los criterios
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static String criteriosOrganizados (Connection connection, ReporteReferenciaInternaForm forma,UsuarioBasico usuario)
	{
		  String criterios="";
		           
	        if (UtilidadCadena.noEsVacio(forma.getCriterios("centroAtencion")+""))
	        	criterios+="Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, 
	        			Utilidades.convertirAEntero(forma.getCriterios("centroAtencion")+""));
	    
	        if(UtilidadCadena.noEsVacio(forma.getCriterios("fechaInicial")+"") && UtilidadCadena.noEsVacio(forma.getCriterios("fechaFinal")+""))
	        	criterios+="  Periodo: "+forma.getCriterios("fechaInicial")+" - "+forma.getCriterios("fechaFinal");
	        
	        if (UtilidadCadena.noEsVacio(forma.getCriterios("institucionSolicitada")+"") && 
	        		!(forma.getCriterios("institucionSolicitada")+"").equals(ConstantesBD.codigoNuncaValido+""))
	        {
	        	HashMap tmp = new HashMap ();
	        	tmp.put("institucion", usuario.getCodigoInstitucion());
	        	tmp.put("codigo", forma.getCriterios("institucionSolicitada")); 
	        	logger.info("\n la institucion sirc es -->"+UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, tmp));
	        	criterios+="  Institución Solicitada: "+
	        	(UtilidadesHistoriaClinica.obtenerInstitucionesSirc(connection, tmp).get(0).get("descripcion"));
	        }
	        
	        if (UtilidadCadena.noEsVacio(forma.getCriterios("tipoReporte")+""))
	        	criterios+="  Tipo Reporte: "+
	        	ValoresPorDefecto.getIntegridadDominio(forma.getCriterios("tipoReporte")+"").toString().toUpperCase();
	        
	        if (UtilidadCadena.noEsVacio(forma.getCriterios("estadoReferencia")+"") && 
	        		!(forma.getCriterios("estadoReferencia")).equals(ConstantesBD.codigoNuncaValido+""))
	        	criterios+="  Estado Referencia: "+ValoresPorDefecto.getIntegridadDominio(forma.getCriterios("estadoReferencia")+"");
	return criterios;
	}
	
	/**
	 * Metodo encargado de consultar la informacion pedida en
	 * Reporte Referencia Interna en Historia Clinica, en las tablas:
	 * "referencia"
	 * "instit_tramite_referencia"
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
	 * -- institucionSolicitada --> No Requerido
	 * -- fechaInicial			--> Requerido
	 * -- fechaFinal			--> Requerido
	 * -- tipoReporte			--> Requerido
	 * -- estadoReferencia		--> No Requerido
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * paciente,
	 * fechaNacimiento,
	 * diagPpal,
	 * medicoRemite,
	 * convenio,
	 * convenioContrato,
	 * institucionRecibe,
	 * estado,
	 * fechaSolicitud,
	 * horaSolicitud,
	 * fechaAceptacion,
	 * horaAceptacion,
	 * fechaSalida,
	 * horaSalida,
	 * cama,
	 * areaPaciente,
	 * examen,
	 */
	public static HashMap consultaReporteReferenciaInterna (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a consultaReporteReferenciaInterna ");
			
		return reporteReferenciaInternaDao().consultaReporteReferenciaInterna(connection, criterios);
	}

	/**
	 * Método encargado de obtener las condiciones where
	 * @param criterios
	 * @return
	 */
	public static String obtenerWhere (HashMap criterios)
	{
		logger.info("\n entre a obtenerWhere ");
		return reporteReferenciaInternaDao().obtenerWhere(criterios);
	}	
}