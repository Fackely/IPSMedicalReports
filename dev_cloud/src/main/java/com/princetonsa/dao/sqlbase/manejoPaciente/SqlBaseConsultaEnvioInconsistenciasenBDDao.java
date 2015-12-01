package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEnvioInformInconsisenBD;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseConsultaEnvioInconsistenciasenBDDao {

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsultaEnvioInconsistenciasenBDDao.class);
	
	 /**
	 * Cadena Sql que realiza la consulta de Ingresos asociados a un paciente
	 */
	private static String consultaIngresosAsociadosStr="SELECT " +
			              "ing.id AS numingreso, " +
			              "ing.codigo_paciente AS codigopaciente, " +
			              "getidpaciente(ing.codigo_paciente) AS cedpersona,"+
			              "getnombrepersona(ing.codigo_paciente) AS nombrepersona, "+
			              "ing.consecutivo AS consecutivo, " +
			              "ing.anio_consecutivo AS anioconsecutivo, " +
			              "getnombrecentatenxing(ing.id) AS nombrecentroatencion, " +
			              "coalesce(to_char(ing.fecha_ingreso,'dd/mm/yyyy'),'') AS fechaapertura, " +
						  "coalesce(ing.hora_ingreso,'') AS horaapertura, "+
						  "cu.id AS numcuenta, " +
						  "cu.estado_cuenta AS estadocuenta, " +
						  "getnombreestadocuenta(cu.estado_cuenta) AS nombreestdocuenta, " +
						  "cu.via_ingreso AS viaingreso, " +
						  "getNombreViaIngreso(cu.via_ingreso) AS nombreviaingreso, " +
						  "incon.estado AS estdoinforme, " +
						  "coalesce(incon.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigoinforme, " +
						  "coalesce(incon.convenio,"+ConstantesBD.codigoNuncaValido+") AS codigoconvenioinforme, " +
						  "coalesce(con.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigoconvenio, " +
						  "coalesce(incon.sub_cuenta, "+ConstantesBD.codigoNuncaValido+" ) AS codresponsable, " +
						  "getnombreconvenio(incon.convenio) AS nombreresponsable " +
						  "FROM ingresos ing " +
						  "INNER JOIN manejopaciente.cuentas cu ON (cu.id_ingreso = ing.id ) " +
						  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = ing.id) " +
						  "INNER JOIN facturacion.convenios con ON (con.reporte_incon_bd = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
						  "INNER JOIN manejopaciente.informe_inconsistencias incon ON (incon.ingreso = ing.id ) "+
						  "WHERE ing.institucion = ? AND ing.codigo_paciente = ? " +
						  "ORDER BY ing.fecha_ingreso DESC";
	
	/**
	 * Cadena Sql que realiza la consulta de pacientes 
	 */
	private static String consultaPacientesStr="SELECT " +
						  "DISTINCT " +
						  "coalesce(inf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigopkinforme, "+
						  "getnombrepersona(i.codigo_paciente) AS nombrepersona, "+
						  "getidpaciente(i.codigo_paciente) AS idpersona, "+
						  "i.id AS idingreso, " +
						  "i.consecutivo AS consecutivoingreso, " +
						  "i.anio_consecutivo AS anioconsecutivo, "+
						  "c.id AS cuenta, "+
						  "coalesce(inf.estado,'') AS estadoinforme, " +
						  "sc.convenio AS convenio, " +
						  "con.nombre AS nombreconvenio, " +
						  "sc.sub_cuenta AS subcuenta, " +
						  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombreviaingreso " +											  
						  "FROM " +
						  "ingresos i "+
						  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id) "+
						  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
						  "INNER JOIN facturacion.convenios con ON (con.reporte_incon_bd = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
						  "INNER JOIN informe_inconsistencias inf ON (inf.cuenta = c.id AND inf.ingreso = i.id AND inf.paciente = i.codigo_paciente AND inf.convenio = con.codigo) " +
						  ConstantesBD.separadorSplit+"1 envio_info_inconsistencias env ON (env.informe_inconsist = inf.codigo_pk) " +
						  "WHERE i.institucion = ? ";
	
	
	

	/**
	 * Cadena Sql que realiza la consulta del Informe de Inconsistencias asociado a un ingreso 
	 */
	private static String consultaInformeInconsistenciaStr="SELECT " +
						"DISTINCT codigo_pk  AS codigo, " + //1
						"convenio AS codconvenio, " +//2
						"sub_cuenta AS codresponable, " +//3
						"tipo_inconsistencia AS tipoincosist, " +//4
						"nombre_convenio AS  nombreconvenio, " +//5
						"primer_apellido AS primerapellido, " +//6
						"segundo_apellido AS segundoapellido, " +//7
						"primer_nombre AS primernombre, " +//8
						"segundo_nombre AS segundonombre, " +//9
						"tipo_identificacion AS tipoidentificacion, " +//10
						"numero_identificacion AS numidentificacion, " +//11
						"fecha_nacimiento AS fechanacimiento, " +//12
						"direccion_residencia AS direccionresidencia, " +//13
						"telefono AS telefono, " +//14
						"departamento  AS departamento, " +//15
						"codigo_departamento AS codigodepartamento, " +//16
						"municipio AS municipio, " +//17
						"codigo_municipio AS codigomunicipio, " +//18
						"cobertura_salud AS coberturasalud, " +//19
						"observaciones AS observaciones, " +//20
						"consecutivo AS consecutivo, " +//21
						"anio_consecutivo AS anioconsecutivo, " +//22
						"ingreso AS codingreso, " +//23
						"cuenta AS codcuenta, " +//24
						"estado AS estado, " +//25
						"coalesce(to_char(fecha_generacion,'dd/mm/yyyy'),'') AS fechageneracion, " + //26
						"coalesce(hora_generacion,'') AS horageneracion, "+ //27
						"usuario_generacion  AS usuariogeneracion, " +//28
						"coalesce(to_char(fecha_modificacion,'dd/mm/yyyy'),'') AS fechamodificacion, " +//29
						"coalesce(hora_modificacion,'') AS horamodificacion, "+ // 30
						"usuario_modificacion AS usuariomodificacion, " +//31
						"paciente AS codpaciente, " +//32
						"via_ingreso AS codviaingreso, " + //33
						"getNombreViaIngreso(via_ingreso) AS nombreviaingreso, " + //34
						"getNombreInconsistencia(tipo_inconsistencia) AS descripcionInconsistencia, " + //35
						"getNombreCobertura(cobertura_salud) AS nombrecobertura "+
						"FROM " +
						"informe_inconsistencias " +
						"WHERE ingreso = ? " ;
	
	
	/**
	 * Cadena Sql que realiza la consulta de los envios realizados a un informe
	 * */
	public static String consultaEnviosInformeStr = "SELECT " +
						"codigo_pk AS codigoenvio, " +
						"coalesce (informe_inconsist,"+ConstantesBD.codigoNuncaValido+") AS numinforme, " +
						"coalesce(to_char(fecha_envio,'dd/mm/yyyy'),'') AS fechaenvio, " +
						"coalesce (hora_envio,'') AS horaenvio, "+
						"coalesce(empresa_envio,"+ConstantesBD.codigoNuncaValido+") AS codentidadenvio, " +
						"medio_envio AS medioenvio, " +
						"coalesce(path_archivo,'') as patharchivo, " +
						"coalesce(convenio_envio,"+ConstantesBD.codigoNuncaValido+") AS convenioenvio, " +
						"CASE WHEN convenio_envio IS NULL THEN '' ELSE getnombreconvenio(convenio_envio) END AS nombreconvenioenvio, " +
						"CASE WHEN empresa_envio  IS NULL THEN '' ELSE e.razon_social END AS nombreentidadenvio, " +
						"coalesce (usuario_envio, '') AS usuarioenvio " +
						"FROM " +
						"envio_info_inconsistencias " +
						"LEFT OUTER JOIN facturacion.empresas e ON (e.codigo = empresa_envio) " +
						"WHERE informe_inconsist = ? ";
	

	/**
	 * Cadena Sql que consulta las variables incorrectas asociadas a un Informe de Inconsistencias especifico
	 */
	public static String consultarVariablesIncorrectasInformeIncoStr="SELECT " +
			"infovar.codigo_pk AS codigo, " +
			"infovar.informe_inconsistencias AS codinforme, " +
			"infovar.tipo_variable AS codvariable, " +
			"getNombreVariableIncorrecta(infovar.tipo_variable) AS descripcionvariable, " +
			"varinco.indicador AS indicador, " +
			"infovar.valor AS valorbd " +
			"FROM info_incon_var_incorrecta infovar  " +
			"INNER JOIN variables_incorrectas varinco ON (infovar.tipo_variable = varinco.codigo)" +
			"WHERE informe_inconsistencias = ? ";
	
	
	
	
	/**
	 * Metodo que realiza la consulta de Ingresos de un Paciente
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarIngresosPaciente (Connection con, int codigoInstitucion, int codigoPaciente)
	{
		String cadena = consultaIngresosAsociadosStr;	
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoInstitucion);
			ps.setInt(2,codigoPaciente);
			
			
			cadena=cadena.replace("ing.institucion = ?", "ing.institucion = "+codigoInstitucion);
			cadena=cadena.replace("ing.codigo_paciente = ?", "ing.codigo_paciente = "+codigoPaciente);
			
			logger.info("\n\nCadena de consulta Ingresos Paciente>> "+cadena);
			logger.info("codigo Paciente: >> "+codigoPaciente + " Codigo Institucion: >> "+codigoInstitucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
			
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("error en  consultarIngresosPaciente >>  cadena >> "+cadena+" ");
			logger.info("codigo Paciente: >> "+codigoPaciente + " Codigo Institucion: >> "+codigoInstitucion);
		}
		return null;
	}
	
	/**
	 * Metodo que carga la informaicion del Informe de Inconsistencias en BD de un paciente
	 * en caso de no existir carga la informacion basica del paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con,HashMap parametros)
	{
		DtoInformeInconsisenBD informe= new DtoInformeInconsisenBD();
		String cadenaConsultaInforme=consultaInformeInconsistenciaStr;
		cadenaConsultaInforme = cadenaConsultaInforme + " AND sub_cuenta = ? "; 
		int inst=0, convenio=0,subcuenta=0, codPersona=0, codIngreso=0; 
		HashMap envio=new HashMap();
		
		
		
		if(parametros.containsKey("codinstitucion") && 
				!parametros.get("codinstitucion").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codinstitucion").toString()) > 0)
		            inst= Utilidades.convertirAEntero(parametros.get("codinstitucion").toString());
		
		if(parametros.containsKey("codpaciente") && 
				!parametros.get("codpaciente").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codpaciente").toString()) > 0)
		            codPersona= Utilidades.convertirAEntero(parametros.get("codpaciente").toString());
		
		if(parametros.containsKey("codconvenio") && 
				!parametros.get("codconvenio").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codconvenio").toString()) > 0)
		            convenio= Utilidades.convertirAEntero(parametros.get("codconvenio").toString());
		
		if(parametros.containsKey("codSubcuenta") && 
				!parametros.get("codSubcuenta").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codSubcuenta").toString()) > 0)
		            subcuenta= Utilidades.convertirAEntero(parametros.get("codSubcuenta").toString());
		
		if(parametros.containsKey("codingreso") && 
				!parametros.get("codingreso").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codingreso").toString()) > 0)
		            codIngreso= Utilidades.convertirAEntero(parametros.get("codingreso").toString());
		
		
		 try{
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaInforme,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			  ps.setInt(1,codIngreso);
			  ps.setInt(2, subcuenta);
			  
			  cadenaConsultaInforme=cadenaConsultaInforme.replace("ingreso = ?", "ingreso = " +codIngreso);
			  cadenaConsultaInforme=cadenaConsultaInforme.replace("sub_cuenta = ?", "sub_cuenta = " +subcuenta);
			  
			  logger.info("\n\n CADENA CONSULTA INFORMES  >> "+cadenaConsultaInforme );
			  ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());	
			
	           
			    if(rs.next())
				{
				  logger.info("\n\n ENTRO A CARGAR INFORME  >> ");
				  		
					informe.setCodigoPk(rs.getInt("codigo"));
					informe.setCodigoConvenio(rs.getInt("codconvenio"));
					informe.setSubcuenta(rs.getInt("codresponable"));
					informe.setTipoInconsistencia(rs.getInt("tipoincosist"));
					informe.setDescripcionTipoInconsistencia(rs.getString("descripcionInconsistencia"));
					informe.setDescripcionConvenio(rs.getString("nombreconvenio"));
					informe.setPrimerNombre(rs.getString("primernombre"));
					informe.setSegundoNombre(rs.getString("segundonombre"));
					informe.setPrimerApellido(rs.getString("primerapellido"));
					informe.setSegundoApellido(rs.getString("segundoapellido"));
					informe.setIdPersona(rs.getString("numidentificacion"));
					informe.setTipoIdentificacion(ValoresPorDefecto.getIntegridadDominio(rs.getString("tipoidentificacion")).toString());
					informe.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechanacimiento")));
					informe.setDireccionResidencia(rs.getString("direccionresidencia"));
					informe.setTelefono(rs.getString("telefono"));
					informe.setDepartamento(rs.getString("departamento"));
					informe.setCodigoDepartamento(rs.getString("codigodepartamento"));
					informe.setMunicipio(rs.getString("municipio"));
					informe.setCodigoMunicipio(rs.getString("codigomunicipio"));
					informe.setCobertura(rs.getString("nombrecobertura"));
					informe.setCodCobertura(rs.getInt("coberturasalud"));
				    informe.setObservaciones(rs.getString("observaciones"));
				    informe.setConsecutivoIngreso(rs.getString("consecutivo"));
				    informe.setAnioConsecutivoIngreso(rs.getString("anioconsecutivo"));
				    informe.setIdIngreso(rs.getInt("codingreso"));
				    informe.setCodigoCuenta(rs.getInt("codcuenta"));
				    informe.setEstadoInforme(rs.getString("estado"));
				    informe.setFechaGeneracion(rs.getString("fechageneracion"));
				    informe.setHoraGeneracion(rs.getString("horageneracion"));
				    informe.setUsuarioGeneracion(rs.getString("usuariogeneracion"));
				    informe.setFechaModificacion(rs.getString("fechamodificacion"));
				    informe.setHoraModificacion(rs.getString("horamodificacion"));
				    informe.setUsuarioModificacion(rs.getString("usuariomodificacion"));
				    informe.setCodigoPaciente(rs.getInt("codpaciente"));
				    informe.setCodigoViaIngreso(rs.getInt("codviaingreso"));
				    informe.setDescripcionViaIngreso(rs.getString("nombreviaingreso")); 
				    informe.setVariablesInco(consultarVariablesIncorrectasInforme(con,rs.getInt("codigo")));  
				    envio=consultarDatosUltimoenvioInforme(con,rs.getInt("codigo"));
				    informe.setEntidad(envio.get("entidad").toString());
				    informe.setMedioEnvio(envio.get("medioenvio").toString());
				    informe.setHistorialEnvios(consultaHistoricosInforme( con, rs.getInt("codigo")));
				    
				    logger.info("\n\nCadena de consulta informe Inconsistencias>> "+cadenaConsultaInforme);		 
				    return informe; 
				}
				 
			 }
			 catch (Exception e) {			
				e.printStackTrace();
				logger.info("error en  consultar Informe Incosistencias >>  cadena >>  "+cadenaConsultaInforme+" ");
			 }	
		
	
		return informe;
	}
	
	
	/**
	 * Metodo que realiza la insercion de los datos de envio del Informe de Iconsistencias
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap insertarEnvioInformeInconsistencias(Connection con,HashMap parametros, String ingresarEnvioInformeIncosist) {
		String cadenaInsercion=ingresarEnvioInformeIncosist;
		String usuarioModificacion=new String("");
		String medioEnvio=new String("");
		String pathArchivo="";
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		resultado.put("codigoPk",ConstantesBD.codigoNuncaValido+"");
		resultado.put("error",errores);
		int convenio=0;
		int codInforme=0;
		int codEntidad=0;
		
		
		if(parametros.containsKey("convenio") && 
				!parametros.get("convenio").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("convenio").toString()) > 0)
		            convenio= Utilidades.convertirAEntero(parametros.get("convenio").toString());
	     
		if(parametros.containsKey("usuario") && 
				!parametros.get("usuario").toString().equals("") )
		            usuarioModificacion= parametros.get("usuario").toString();
		
		if(parametros.containsKey("codInforme") && 
				!parametros.get("codInforme").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codInforme").toString()) > 0)
			        codInforme= Utilidades.convertirAEntero(parametros.get("codInforme").toString());
		
		if(parametros.containsKey("empresa") && 
				!parametros.get("empresa").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("empresa").toString()) > 0)
		           	codEntidad= Utilidades.convertirAEntero(parametros.get("empresa").toString());

		if(parametros.containsKey("medioenvio") && 
				!parametros.get("medioenvio").toString().equals(""))
		           	medioEnvio= parametros.get("medioenvio").toString();

		if(parametros.containsKey("patharchivo") && 
				!parametros.get("patharchivo").toString().equals(""))
		           	pathArchivo= parametros.get("patharchivo").toString();
		
		try{
		     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     cadenaInsercion=cadenaInsercion.replace("?, CURRENT_DATE",""+codInforme+", CURRENT_DATE");
		     cadenaInsercion=cadenaInsercion.replace("?, ?, ?, ?", "'"+usuarioModificacion+"',"+convenio+","+codEntidad+",'"+medioEnvio+"'");
		     ps.setInt(1, codInforme);
		     ps.setString(2,usuarioModificacion);
		     if(convenio>0)
			        ps.setInt(3,convenio);
				   else
				  ps.setNull(3,Types.INTEGER);
			     
			    if(codEntidad>0)
				  ps.setInt(4,codEntidad);    
			    else
				  ps.setNull(4,Types.INTEGER);
		   
		     ps.setString(5,medioEnvio);
		    
		     ps.setString(6, pathArchivo);
		     
		     if (ps.executeUpdate()>0)
		     {
		    	 resultado.put("codigoPk",codInforme);
		     }
		     else{
		    	 errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion de datos de Envio"));
				 resultado.put("error",errores);
				 return resultado;
		     }
		   }
		    catch (Exception e) {			
			 e.printStackTrace();
			 
			 logger.info("error en la Insercion del Envio  >>  cadena >>  "+cadenaInsercion+" ");
			 
		    }	
		return resultado;
	  }
	
	
	/**
	 * Metodo que realiza la consulta de Historicos ( Envios realizados al Informe de Inconsistencias )
	 * @param con
	 * @param codInforme
	 * @return
	 */
	public static ArrayList< DtoRegistroEnvioInformInconsisenBD> consultaHistoricosInforme(Connection con, int codInforme)
	 {
		ArrayList< DtoRegistroEnvioInformInconsisenBD> historico = new ArrayList< DtoRegistroEnvioInformInconsisenBD>();
		String consultaHistoricos=consultaEnviosInformeStr;
		consultaHistoricos=consultaEnviosInformeStr + " ORDER BY fecha_envio || ' ' || hora_envio DESC ";
		
		 try{
		     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaHistoricos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     consultaHistoricos=consultaHistoricos.replace("informe_inconsist = ?", "informe_inconsist = "+codInforme +" ");
		     ps.setInt(1,codInforme);
		     logger.info("\n\n ENTRO A CARGAR EL HISTORICO  >> ");
		     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		       
		        while(rs.next())
		         {  
		        	DtoRegistroEnvioInformInconsisenBD dtoEnvio=new DtoRegistroEnvioInformInconsisenBD();
		    	    dtoEnvio.setCodigoPk(rs.getInt("codigoenvio"));
		    	    dtoEnvio.setCodigoInformInconsisenBD(codInforme);
		    	    dtoEnvio.setCodigoEntidadEnvio(rs.getInt("codentidadenvio"));
		    	    dtoEnvio.setNombreEntidadEnvio(rs.getString("nombreentidadenvio"));
		    	    dtoEnvio.setCodigoConvenioEnvio(rs.getInt("convenioenvio"));
		    	    dtoEnvio.setNombreConvenioEnvio(rs.getString("nombreconvenioenvio"));
		    	    dtoEnvio.setFechaEnvio(rs.getString("fechaenvio"));
		    	    dtoEnvio.setHoraEnvio(rs.getString("horaenvio"));
		    	    dtoEnvio.setMedioEnvio(rs.getString("medioenvio"));
		    	    dtoEnvio.setUrlArchivoIncoXmlDes(rs.getString("patharchivo"));
		    	    dtoEnvio.setUsuarioEnvio(rs.getString("usuarioenvio"));
		    	    
		        	historico.add(dtoEnvio);
		         }
		   }
	         catch (Exception e) {			
			 e.printStackTrace();
			 
			 logger.info("error en la Consulta del Historico de Informes Enviados  >>  cadena >>  "+consultaHistoricos+" ");
			 
		    }
		
		return historico;
	 }
	
  /**
   * Metodo que realiza la consulta de los datos del Ultimo envio realizado a un informe
   * @param con
   * @param codigoInconsistencia
   * @return
   */
	public static HashMap consultarDatosUltimoenvioInforme(Connection con,int codigoInconsistencia)
	 {
	  HashMap mapaInforme=new HashMap();
	  String consultaEnvioIncon=consultaEnviosInformeStr;
	  consultaEnvioIncon=consultaEnvioIncon + " ORDER BY fecha_envio || ' ' || hora_envio DESC ";
	  try{
		     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaEnvioIncon,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     consultaEnvioIncon=consultaEnvioIncon.replace("informe_inconsist = ?", "informe_inconsist = "+codigoInconsistencia +" ");
		     ps.setInt(1,codigoInconsistencia);
		     logger.info("\n\n esta es la CADENA ULTIMO ENVIO >> "+consultaEnvioIncon);
		     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    { 
		    	 if(rs.getInt("codentidadenvio")<0)
		  	       mapaInforme.put("entidad", rs.getString("convenioenvio")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi);
		  	     else
		  	       mapaInforme.put("entidad", rs.getString("codentidadenvio")+ConstantesBD.separadorSplit+ConstantesBD.acronimoNo);
		  	     
		  	        mapaInforme.put("medioenvio",rs.getString("medioenvio"));
		  	     
		  	     if(rs.getString("nombreentidadenvio")==null || rs.getString("nombreentidadenvio").equals(""))
		  	    	 mapaInforme.put("nomentidad",rs.getString("nombreconvenioenvio"));
		  	     else
		  	    	 mapaInforme.put("nomentidad",rs.getString("nombreentidadenvio"));	
		     	
		     logger.info("Esta es la Entidad Envio  >>  dato >>  "+mapaInforme.get("entidad")+" ");
		    }
		    else{
	    	     mapaInforme.put("entidad", "");
			     mapaInforme.put("medioenvio","");
			     mapaInforme.put("nomentidad","" );
		    	
		    }
	  }
		     catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("error en la Consulta de Datos de  Informes Enviados  >>  cadena >>  "+consultaEnvioIncon+" ");
				 
			    }
	  
	  return mapaInforme;
	 }

	
	/**
	  * Metodo para Consultar las variables incorretas asociadas a un Informe
	  * @param con
	  * @param codInforme
	  * @return
	  */
	 public static String[] consultarVariablesIncorrectasInforme(Connection con, int codInforme)
	 {
		 String[] variables=new String[10];
		 String consulta=consultarVariablesIncorrectasInformeIncoStr;
		 HashMap mapVariables=new HashMap();
		 mapVariables.put("numRegistros","0");
		 int cont=0;
		 try{
		     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     ps.setInt(1, codInforme);
		     consulta= consulta.replace("informe_inconsistencias = ?", "informe_inconsistencias = "+ codInforme);
		     
		     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		     mapVariables = UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
		     logger.info("cadena consulta Variables>>  "+consulta+" ");
		     
		     if(Utilidades.convertirAEntero(mapVariables.get("numRegistros")+"")>0)
		      {
		    	variables=new String[Utilidades.convertirAEntero(mapVariables.get("numRegistros")+"")]; 	 
		        while(cont < Utilidades.convertirAEntero(mapVariables.get("numRegistros")+""))
		          {	
		    	  variables[cont] = mapVariables.get("codvariable_"+cont).toString()+ConstantesBD.separadorSplit+mapVariables.get("descripcionvariable_"+cont).toString()+ConstantesBD.separadorSplit+mapVariables.get("indicador_"+cont).toString();
		    	  cont++;
		          }
		          return variables;
		       }
			 }
			   catch (Exception e) {			
				 e.printStackTrace();
				 logger.info("error en  consultar las Variables Incorrectas del Informe >>  cadena >>  "+consulta+" ");
			 }	
			   
		 return variables;
	 }
	 
	 
	 
	 public static void actualizarEstadoEnvioInforme(Connection con, int codInforme)
	 {
	    	 String actualizacion= "UPDATE informe_inconsistencias SET estado= '"+ConstantesIntegridadDominio.acronimoEstadoEnviado+"' WHERE codigo_pk = "+codInforme+"";
	    	 try{
			     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	     ps.executeUpdate();
	    	 }
	    	 catch (Exception e) {			
				 e.printStackTrace();
				 logger.info("error al Actualizar estado del Informe >>  cadena >>  "+actualizacion+" ");
			 }	
	 }
	 
	 public static HashMap consultaConvenios(Connection con, String cadenaConsulta)
	 {
		 
		 HashMap convenios=new  HashMap();
		 String consultaConvenios=cadenaConsulta;
		 
		 try{
		     PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConvenios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());     
		     convenios=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs), true, false);
		     
    	 }
    	 catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error al Consultar los Convenios>>  cadena >>  "+consultaConvenios+" ");
		 }	
		 
		 return convenios;
		 
	 }

	public static ArrayList<DtoInformeInconsisenBD> listadoInformeInconsistencias(Connection con, HashMap parametros) {
		
		ArrayList<DtoInformeInconsisenBD> array=new ArrayList<DtoInformeInconsisenBD>();
		boolean indicadorEnvio = false;	
		String cadena=consultaPacientesStr;
		
		
		if(parametros.containsKey("fechaInicialGeneracion") && 
				parametros.containsKey("fechaFinalGeneracion") && 
					!parametros.get("fechaInicialGeneracion").toString().equals("") && 
						!parametros.get("fechaFinalGeneracion").toString().equals(""))
		{
			cadena += " AND inf.fecha_generacion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialGeneracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalGeneracion").toString())+"' ";
			
		}
		
		if(parametros.containsKey("estadoEnvio") && 
				!parametros.get("estadoEnvio").toString().equals("") && 
					!parametros.get("estadoEnvio").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
		{
			cadena += " AND inf.estado = '"+parametros.get("estadoEnvio").toString()+"' ";
			
		}
		
		if(parametros.containsKey("fechaInicialEnvio") && 
				parametros.containsKey("fechaFinalEnvio") && 
					!parametros.get("fechaInicialEnvio").equals("") && 
						!parametros.get("fechaFinalEnvio").equals(""))
		{
			cadena += " AND env.fecha_envio BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialEnvio").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalEnvio").toString())+"' ";
			indicadorEnvio = true;
		}
		
		if(indicadorEnvio)
			cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," INNER JOIN ");
		else
			cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," LEFT OUTER JOIN ");
		
		
		if(parametros.containsKey("convenio") && 
				!parametros.get("convenio").toString().equals(""))
			cadena += " AND con.codigo = "+parametros.get("convenio").toString()+" ";
		
		try
		{
			logger.info("\n\nCadena de consulta >> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoInformeInconsisenBD dto = new DtoInformeInconsisenBD();
				dto.setCodigoPk(rs.getInt("codigopkinforme"));
				dto.setNombrePersona(rs.getString("nombrepersona"));
				dto.setIdPersona(rs.getString("idpersona"));
				dto.setIdIngreso(rs.getInt("idingreso"));
				dto.setCodigoCuenta(rs.getInt("cuenta"));
				dto.setEstadoInforme(rs.getString("estadoinforme"));
				dto.setCodigoConvenio(rs.getInt("convenio"));
				dto.setDescripcionConvenio(rs.getString("nombreconvenio"));
				dto.setAnioConsecutivoIngreso(rs.getString("anioconsecutivo"));
				dto.setConsecutivoIngreso(rs.getString("consecutivoingreso"));
				dto.setDescripcionViaIngreso(rs.getString("nombreviaingreso"));
				dto.setEstadoInforme(rs.getString("estadoinforme"));
				dto.setSubcuenta(rs.getInt("subcuenta"));
				
				array.add(dto);
			}			
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en la consulta de Pacientes >> "+parametros+" cadena >> "+cadena+" ");
			
		}
		
		
		return array;
	}
}
