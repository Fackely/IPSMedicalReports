package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasIngresosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseFormatoJustArtNoposDao;
import com.princetonsa.mundo.manejoPaciente.CalidadAtencion;
import com.princetonsa.mundo.manejoPaciente.EstadisticasIngresos;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;


public class SqlBaseEstadisticasIngresosDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseEstadisticasIngresosDao.class);

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereIngresosXConvenio(Connection con, EstadisticasIngresos mundo) {
		Utilidades.imprimirMapa(mundo.getFiltrosMap());
		
		// Centro de Atención
        String where = "cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND (to_char(getfechaingreso(c.id,c.via_ingreso), 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
       
    	// Via de ingreso
    	if(!mundo.getFiltrosMap("viaIngreso").equals("") && !mundo.getFiltrosMap("viaIngreso").equals("-1"))
    		where += " AND c.via_ingreso = "+mundo.getFiltrosMap("viaIngreso");
    	
    	// Tipo de Paciente
    	if(!mundo.getFiltrosMap("tipoPaciente").equals("") && !mundo.getFiltrosMap("tipoPaciente").equals("-1"))
    		where += " AND c.tipo_paciente = '"+mundo.getFiltrosMap("tipoPaciente")+"'";
    	
    	// Convenio
    	if(!mundo.getFiltrosMap("convenio").equals("") && !mundo.getFiltrosMap("convenio").equals("-1"))
    		where += " AND con.codigo IN ("+mundo.getFiltrosMap("convenio")+") ";
    	
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereReingresos(Connection con, EstadisticasIngresos mundo) {
		// Centro de Atención
        String where = "cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND (to_char(getfechaingreso(c.id,c.via_ingreso), 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
       
    	// Via de ingreso
    	if(!mundo.getFiltrosMap("viaIngreso").equals("") && !mundo.getFiltrosMap("viaIngreso").equals("-1"))
    		where += " AND c.via_ingreso = "+mundo.getFiltrosMap("viaIngreso");
    	
    	// Tipo de Paciente
    	if(mundo.getFiltrosMap().containsKey("tipoPaciente") && !mundo.getFiltrosMap("tipoPaciente").equals("") && !mundo.getFiltrosMap("tipoPaciente").equals("-1"))
    		where += " AND c.tipo_paciente = '"+mundo.getFiltrosMap("tipoPaciente")+"'";
    	
    	// Diagnosticos Egreso
    	HashMap dx = crearCadenaDx(con, mundo.getDxEgreso());
    	if(dx.get("hayDx").toString().equals(ConstantesBD.acronimoSi))
    		where += " AND e.diagnostico_principal IN ("+dx.get("dx")+") ";
    	
    	// Convenio
    	if(!mundo.getFiltrosMap("convenio").equals("") && !mundo.getFiltrosMap("convenio").equals("-1"))
    		where += " AND con.codigo IN ("+mundo.getFiltrosMap("convenio")+") ";
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereTotalReingresosXConvenio(Connection con, EstadisticasIngresos mundo) {
		// Centro de Atención
        String where = "cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND (to_char(getfechaingreso(c.id,c.via_ingreso), 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
       
    	// Via de ingreso
    	if(!mundo.getFiltrosMap("viaIngreso").equals("") && !mundo.getFiltrosMap("viaIngreso").equals("-1"))
    		where += " AND c.via_ingreso = "+mundo.getFiltrosMap("viaIngreso");
    	
    	// Tipo de Paciente
    	if(mundo.getFiltrosMap().containsKey("tipoPaciente") && !mundo.getFiltrosMap("tipoPaciente").equals("") && !mundo.getFiltrosMap("tipoPaciente").equals("-1"))
    		where += " AND c.tipo_paciente = '"+mundo.getFiltrosMap("tipoPaciente")+"'";
    	
    	// Diagnosticos Egreso
    	HashMap dx = crearCadenaDx(con, mundo.getDxEgreso());
    	if(dx.get("hayDx").toString().equals(ConstantesBD.acronimoSi))
    		where += " AND e.diagnostico_principal IN ("+dx.get("dx")+") ";
    	
    	// Convenio
    	if(!mundo.getFiltrosMap("convenio").equals("") && !mundo.getFiltrosMap("convenio").equals("-1"))
    		where += " AND con.codigo IN ("+mundo.getFiltrosMap("convenio")+") ";
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearConsultaAtencionXRangoEdad(Connection con, EstadisticasIngresos mundo) {
		String consultaInterna="";
		String consulta="";
		String where="";
		
		Utilidades.imprimirMapa(mundo.getFiltrosMap());
		
        // Rango de fechas
        if (mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoAmbulatorios+"") || mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoConsultaExterna+""))
        	where += "(to_char(c.fecha_apertura, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
        if (mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
        	where += "(to-char(ah.fecha_admision, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
        if (mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoUrgencias+""))
        	where += "(to_char(au.fecha_admision, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 	
        
    	// Via de ingreso
    	if(!mundo.getFiltrosMap("viaIngreso").equals("") && !mundo.getFiltrosMap("viaIngreso").equals("-1"))
    		where += " AND c.via_ingreso = "+mundo.getFiltrosMap("viaIngreso");
    	
    	// Tipo de Paciente
    	if(mundo.getFiltrosMap().containsKey("tipoPaciente") && !mundo.getFiltrosMap("tipoPaciente").equals("") && !mundo.getFiltrosMap("tipoPaciente").equals("-1"))
    		where += " AND c.tipo_paciente = '"+mundo.getFiltrosMap("tipoPaciente")+"'";
    	
    	// Centro de Atención
        where += "AND cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
    	
    	// Sexo
    	if(!mundo.getFiltrosMap("sexo").equals("") && !mundo.getFiltrosMap("sexo").equals("-1"))
    		where += " AND getsexopaciente(c.codigo_paciente) = "+mundo.getFiltrosMap("sexo");
    	
    	// Convenio
    	if(!mundo.getFiltrosMap("convenio").equals("") && !mundo.getFiltrosMap("convenio").equals("-1"))
    		where += " AND con.codigo IN ("+mundo.getFiltrosMap("convenio")+") ";
    	
    	// Consulta
    	if (mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoAmbulatorios+"") || mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoConsultaExterna+"")){
	    	consultaInterna = 	"SELECT " +
									"c.id as cuenta, " +
									"to_char(c.fecha_apertura, 'YYYY-MM') as codigomes, " +
									"getnombremes(to_char(c.fecha_apertura, 'MM'))||' '||to_char(c.fecha_apertura, 'YYYY') as mes, " +
									"c.fecha_apertura - p.fecha_nacimiento as edad " +
								"FROM " +
									"cuentas c " +
								"INNER JOIN " +
									"centros_costo cc ON (cc.codigo=c.area) " +
								"INNER JOIN " +
									"sub_cuentas sc ON (sc.ingreso=c.id_ingreso and sc.nro_prioridad = 1) " +
								"INNER JOIN " +
									"convenios con ON (con.codigo=sc.convenio) " +
								"INNER JOIN " +
									"personas p ON (p.codigo=c.codigo_paciente) " +
								"WHERE "+where;
    	}
    	if (mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoHospitalizacion+"")){
    		consultaInterna = 	"SELECT " +
									"c.id as cuenta, " +
									"to_char(ah.fecha_admision, 'YYYY-MM') as codigomes, " +
									"getnombremes(to_char(ah.fecha_admision, 'MM'))||' '||to_char(ah.fecha_admision, 'YYYY') as mes, " +
									"ah.fecha_admision - p.fecha_nacimiento as edad " +
								"FROM " +
									"admisiones_hospi ah " +
								"INNER JOIN " +
									"cuentas c ON (c.id=ah.cuenta) " +
								"INNER JOIN " +
									"centros_costo cc ON (cc.codigo=c.area) " +
								"INNER JOIN " +
									"sub_cuentas sc ON (sc.ingreso=c.id_ingreso and sc.nro_prioridad = 1) " +
								"INNER JOIN " +
									"convenios con ON (con.codigo=sc.convenio) " +
								"INNER JOIN " +
									"personas p ON (p.codigo=c.codigo_paciente) " +
								"WHERE "+where;
    	}
    	if (mundo.getFiltrosMap("viaIngreso").equals(ConstantesBD.codigoViaIngresoUrgencias+"")){
    		consultaInterna = 	"SELECT " +
									"c.id as cuenta, " +
									"to_char(au.fecha_admision, 'YYYY-MM') as codigomes, " +
									"getnombremes(to_char(au.fecha_admision, 'MM'))||' '||to_char(au.fecha_admision, 'YYYY') as mes, " +
									"au.fecha_admision - p.fecha_nacimiento as edad " +
								"FROM " +
									"admisiones_urgencias au " +
								"INNER JOIN " +
									"cuentas c ON (c.id=au.cuenta) " +
								"INNER JOIN " +
									"centros_costo cc ON (cc.codigo=c.area) " +
								"INNER JOIN " +
									"sub_cuentas sc ON (sc.ingreso=c.id_ingreso and sc.nro_prioridad = 1) " +
								"INNER JOIN " +
									"convenios con ON (con.codigo=sc.convenio) " +
								"INNER JOIN " +
									"personas p ON (p.codigo=c.codigo_paciente) " +
								"WHERE "+where;
    	}
    	
    	consulta = "SELECT " +
						"t.\"cuenta\" as cuenta, " +
						"t.\"codigomes\" as codigomes, " +
						"t.\"mes\" as mes, " +
						"re.orden as orden, " +
						"re.nombre_etiqueta as rango " +
					"FROM " +
						"("+consultaInterna+") t " +
					"INNER JOIN " +
						"rangos_estadisticos re ON (re.rango_inicial <= t.\"edad\" AND re.rango_final >= t.\"edad\" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteAtencionPorRangoEdad+" and re.institucion="+mundo.getInstitucion()+")";
    	
    	return consulta;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarAtencionXEmpresaYConvenio(Connection con, EstadisticasIngresos mundo) {
		HashMap map = new HashMap();
		String where="";
		map.put("numRegistros", 0);
        
		// Rango de fechas
		if (Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoConsultaExterna || Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoAmbulatorios)
			where = "AND (to_char(c.fecha_apertura, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
		if (Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoHospitalizacion)
			where = "AND (to_char(ah.fecha_admision, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
		if (Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoUrgencias)
			where = "AND (to_char(au.fecha_admision, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial").toString())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal").toString())+"') "; 
		
    	// Via de ingreso
    	if(!mundo.getFiltrosMap("viaIngreso").equals("") && !mundo.getFiltrosMap("viaIngreso").equals("-1"))
    		where += " AND c.via_ingreso = "+mundo.getFiltrosMap("viaIngreso");
    	
    	// Tipo de Paciente
    	if(mundo.getFiltrosMap().containsKey("tipoPaciente") && !mundo.getFiltrosMap("tipoPaciente").equals("") && !mundo.getFiltrosMap("tipoPaciente").equals("-1"))
    		where += " AND c.tipo_paciente = '"+mundo.getFiltrosMap("tipoPaciente")+"'";
    	
    	// Centro de Atención
         	where += " AND cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
    	
    	// Convenio
    	if(!mundo.getFiltrosMap("convenio").equals("") && !mundo.getFiltrosMap("convenio").equals("-1"))
    		where += " AND con.codigo IN ("+mundo.getFiltrosMap("convenio")+") ";
    	
    	// Empresa(s)
    	if(!mundo.getFiltrosMap("empresa").equals("") && !mundo.getFiltrosMap("empresa").equals("-1"))
    		where += " AND con.empresa IN ("+mundo.getFiltrosMap("empresa")+") ";
		
    	String consulta="";
    	
    	if (Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoConsultaExterna || Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoAmbulatorios){
    		consulta = "SELECT " +
    						"sc.sub_cuenta, " +
    						"con.codigo as codconvenio, " +
    						"lower(con.nombre) as convenio, " +
    						"lower(e.razon_social) as empresa, " +
    						"e.codigo as codempresa, " +
    						"to_char(getfechaingreso(c.id,c.via_ingreso), 'YYYY-MM') as anioMes " +
    					"FROM " +
    						"cuentas c " +
    					"INNER JOIN " +
    						"centros_costo cc ON (cc.codigo=c.area) " +
    					"INNER JOIN " +
    						"ingresos i ON (i.id = c.id_ingreso) " +
    					"INNER JOIN " +
    						"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
    					"INNER JOIN " +
    						"convenios con ON (con.codigo=sc.convenio) " +
    					"INNER JOIN " +
    						"empresas e ON (e.codigo=con.empresa) "+where+" " +
    					"ORDER BY " +
							"empresa, convenio"	;	
    	}
    	if (Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoHospitalizacion){
    		consulta = "SELECT " +
							"sc.sub_cuenta, " +
							"con.codigo as codconvenio, " +
							"lower(con.nombre) as convenio, " +
							"lower(e.razon_social) as empresa, " +
							"e.codigo as codempresa, " +
							"to_char(getfechaingreso(c.id,c.via_ingreso), 'YYYY-MM') as anioMes " +
						"FROM " +
							"admisiones_hospi ah " +
						"INNER JOIN " +
							"cuentas c ON (c.id=ah.cuenta) " +
						"INNER JOIN " +
							"centros_costo cc ON (cc.codigo=c.area) " +
						"INNER JOIN " +
							"ingresos i ON (i.id = c.id_ingreso) " +
						"INNER JOIN " +
							"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
						"INNER JOIN " +
							"convenios con ON (con.codigo=sc.convenio) " +
						"INNER JOIN " +
							"empresas e ON (e.codigo=con.empresa) "+where+" " +
						"ORDER BY " +
							"empresa, convenio"	;	
    	}	
		if (Integer.parseInt(mundo.getFiltrosMap("viaIngreso").toString())==ConstantesBD.codigoViaIngresoUrgencias){
    		consulta = "SELECT " +
							"sc.sub_cuenta, " +
							"con.codigo as codconvenio, " +
							"lower(con.nombre) as convenio, " +
							"lower(e.razon_social) as empresa, " +
							"e.codigo as codempresa, " +
							"to_char(getfechaingreso(c.id,c.via_ingreso), 'YYYY-MM') as anioMes " +
						"FROM " +
							"admisiones_urgencias au " +
						"INNER JOIN " +
							"cuentas c ON (c.id=au.cuenta) " +
						"INNER JOIN " +
							"centros_costo cc ON (cc.codigo=c.area) " +
						"INNER JOIN " +
							"ingresos i ON (i.id = c.id_ingreso) " +
						"INNER JOIN " +
							"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
						"INNER JOIN " +
							"convenios con ON (con.codigo=sc.convenio) " +
						"INNER JOIN " +
							"empresas e ON (e.codigo=con.empresa) "+where+" " +
						"ORDER BY " +
							"empresa, convenio"	;	
		}			
		
		
		
		logger.info("consultarAtencionXEmpresaYConvenio: "+consulta);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			map = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
		} 
		catch(SQLException e)
		{
			logger.error("Error en consultarAtencionXEmpresaYConvenio: "+e);
			return null;
		}
    	return map;
	}
	
	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param anioMes
	 * @return
	 */
	public static int consultarEgresosMes(Connection con, String anioMes, HashMap filtros) {
		int egresos = ConstantesBD.codigoNuncaValido;
		String where="";
		
		String[] fechaIniV=UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial").toString()).split("-");
		String[] fechaFinV=UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal").toString()).split("-");
		
        // Rango de fechas
		if (Integer.parseInt(filtros.get("viaIngreso").toString())==ConstantesBD.codigoViaIngresoConsultaExterna || Integer.parseInt(filtros.get("viaIngreso").toString())==ConstantesBD.codigoViaIngresoAmbulatorios){
			where = "AND (to_char(c.fecha_apertura, 'YYYY-MM')='"+anioMes+"') "+
					"AND (to_char(c.fecha_apertura, 'YYYY-MM') BETWEEN '"+fechaIniV[0]+"-"+fechaIniV[1]+"' and '"+fechaFinV[0]+"-"+fechaFinV[1]+"') ";	
		}	
		else {
			where = "AND (to_char(e.fecha_egreso,'YYYY-MM')='"+anioMes+"') " +
					"AND (to_char(e.fecha_egreso, 'YYYY-MM') BETWEEN '"+fechaIniV[0]+"-"+fechaIniV[1]+"' and '"+fechaFinV[0]+"-"+fechaFinV[1]+"') " +
					"AND e.usuario_responsable IS NOT NULL "; 
		}
		
    	// Via de ingreso
    	if(!filtros.get("viaIngreso").equals("") && !filtros.get("viaIngreso").equals("-1"))
    		where += " AND c.via_ingreso = "+filtros.get("viaIngreso");
    	
    	// Tipo de Paciente
    	if(filtros.containsKey("tipoPaciente") && !filtros.get("tipoPaciente").equals("") && !filtros.get("tipoPaciente").equals("-1"))
    		where += " AND c.tipo_paciente = '"+filtros.get("tipoPaciente")+"'";
    	
    	// Centro de Atención
         	where += " AND cc.centro_atencion = "+filtros.get("centroAtencion")+" ";
    	
    	// Convenio(s)
    	if(!filtros.get("convenio").equals("") && !filtros.get("convenio").equals("-1"))
    		where += " AND con.codigo IN ("+filtros.get("convenio")+") ";
    	
    	// Empresa(s)
    	if(!filtros.get("empresa").equals("") && !filtros.get("empresa").equals("-1"))
    		where += " AND con.empresa IN ("+filtros.get("empresa")+") ";
		
    	String consulta="";
    	
    	if (Integer.parseInt(filtros.get("viaIngreso").toString())==ConstantesBD.codigoViaIngresoConsultaExterna || Integer.parseInt(filtros.get("viaIngreso").toString())==ConstantesBD.codigoViaIngresoAmbulatorios){
    		consulta = "SELECT " +
    						"COUNT(*) AS egresos " +
    					"FROM " +
    						"cuentas c " +
    					"INNER JOIN " +
    						"centros_costo cc ON (cc.codigo=c.area) " +
    					"INNER JOIN " +
    						"ingresos i ON (i.id = c.id_ingreso) " +
    					"INNER JOIN " +
    						"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
    					"INNER JOIN " +
    						"convenios con ON (con.codigo=sc.convenio) "+where; 
    	} else {
    		consulta = "SELECT " +
    						"COUNT(*) AS egresos " +
    					"FROM " +
    						"egresos e " +
    					"INNER JOIN " +
    						"cuentas c on (c.id = e.cuenta) " +
    					"INNER JOIN " +
    						"centros_costo cc ON (cc.codigo=c.area) " +
    					"INNER JOIN " +
    						"ingresos i ON (i.id = c.id_ingreso) " +
    					"INNER JOIN " +
    						"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
    					"INNER JOIN " +
    						"convenios con ON (con.codigo=sc.convenio) "+where;
    	}
		
		logger.info("consultarEgresosMes: "+consulta);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next()){
				egresos = rs.getInt("egresos");
			}
		} 
		catch(SQLException e)
		{
			logger.error("Error en consultarAtencionXEmpresaYConvenio: "+e);
		}
    	return egresos;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap crearCadenaDx(Connection con, HashMap dxMap) {
		HashMap map= new HashMap();
		String cadena="";
		String aux=ConstantesBD.acronimoNo;
		for(int i=0; i<Utilidades.convertirAEntero(dxMap.get("numRegistros").toString()); i++){
			if(dxMap.get("checkbox_"+i).equals("true")){
				cadena+="'"+dxMap.get(i+"").toString().split(ConstantesBD.separadorSplit)[0]+"',";
				aux=ConstantesBD.acronimoSi;
			}	
		}
		cadena+="'"+ConstantesBD.codigoNuncaValido+"'";
		map.put("dx", cadena);
		map.put("hayDx", aux);
		logger.info("Cadena de diagnosticos - "+cadena);
		return map;
	}
	
}