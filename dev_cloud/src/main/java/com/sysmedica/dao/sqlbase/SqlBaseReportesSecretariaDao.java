package com.sysmedica.dao.sqlbase;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.Collection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.util.CalendarioEpidemiologico;
import com.sysmedica.util.SemanaEpidemiologica;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

public class SqlBaseReportesSecretariaDao {

	/**
     * Objeto que maneja los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseReportesSecretariaDao.class);
    
    
    /**
     * Union de tablas de fichas para poder hacer la consulta de todas las fichas
     */
    private static final String strUnionFichas = "(SELECT " +
													"codigoFichaRabia as codigoFicha, " +
													"fechaDiligenciamiento, " +
													"horaDiligenciamiento, " +
													"estado," +
													"codigoPaciente," +
													"acronimo," +
													"loginUsuario," +
													"institucionAtendio," +
													"fechaInicioSintomasGeneral," +
													"codigoAseguradora," +
													"tipocaso " +
												 "FROM " +
													"vigiFichaRabia " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaSarampion as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaSarampion " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaVih as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaVih " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaDengue as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaDengue " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaParalisis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaParalisis " +
											 	 "UNION " +
												 "SELECT " +
												 	"codigoFichaSifilis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaSifilis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTetanos as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
											 	"vigiFichaTetanos " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaGenerica as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaGenerica " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTuberculosis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario, " +
												 	"institucionAtendio, " +
												 	"fechaInicioSintomasGeneral," +
												 	"codigoAseguradora," +
												 	"tipocaso " +
												 "FROM " +
												 	"vigiFichaTuberculosis " +
												 ") ficha ";
    
    
    private static final String consultaEventosMorbilidad = "SELECT " +
    															"ciu.nombre AS nombreMunResidencia," +
    															"ciu.codigo_ciudad AS codigoMunResidencia," +
    															"dep.nombre AS nombreDepResidencia," +
    															"ciu.codigo_departamento AS codigoDepResidencia," +
    															
    															"ciu2.nombre AS nombreMunOrigen," +
    															"ciu2.codigo_ciudad AS codigoMunOrigen," +
    															"dep2.nombre AS nombreDepOrigen," +
    															"ciu2.codigo_departamento AS codigoDepOrigen," +
    															"numero_identificacion," +
    															
																"primer_nombre," +
																"segundo_nombre," +
																"primer_apellido," +
																"segundo_apellido," +
																"tipo_identificacion," +
																"fecha_nacimiento," +
																"personas.sexo AS sexoPaciente," +
																"bar.nombre AS nombreBarrio," +
																"personas.codigo_barrio_vivienda AS codigoBarrio," +
																"codigo_barrio_vivienda," +
																"personas.direccion AS direccion," +
																"personas.telefono AS telefono," +
																"convenios.nombre AS nombreAseguradora," +
																"convenios.tipo_regimen AS regimenSalud," +
																"pacientes.zona_domicilio AS zonaResidencia," +
																
																"ficha.fechaDiligenciamiento," +
																"ficha.horaDiligenciamiento," +
																"dia.codigoEnfermedadesNotificables AS codigoEnfNot, " +
																"ficha.estado as estadoFicha, " +
																"ficha.codigoFicha," +
																"ficha.institucionAtendio AS codigoInstitucion, " +
																"ficha.fechaInicioSintomasGeneral AS fechaInicioSintomas, " +
																"ficha.tipocaso AS tipocaso, " +
																
																"unidades.nombre AS nombreInstitucion " +
															"FROM " +
																"personas," +			    											
																"diagnosticos dia," +
																"ciudades ciu," +
																"departamentos dep, " +
																"ciudades ciu2, " +
																"departamentos dep2, " +
																"barrios bar,  " +
																"convenios, " +
																"pacientes, " +
															//	"cuentas, " +
																"vigiunidadesprimarias unidades," +
																strUnionFichas+
															"WHERE " +
																"personas.codigo=ficha.codigoPaciente " +
															"AND " +
																"dia.acronimo=ficha.acronimo " +
															"AND " +
																"personas.codigo_ciudad_vivienda = ciu.codigo_ciudad " +
															"AND " +
																"personas.codigo_departamento_vivienda = ciu.codigo_departamento " +
															"AND " +
																"personas.codigo_departamento_vivienda = dep.codigo "+
															"AND " +
																"personas.codigo_ciudad_vivienda = ciu2.codigo_ciudad " +
															"AND " +
																"personas.codigo_departamento_vivienda = ciu2.codigo_departamento " +
															"AND " +
																"personas.codigo_departamento_vivienda = dep2.codigo " +
															"AND " +
																"personas.codigo_barrio_vivienda = bar.codigo_barrio " +
															"AND " +
																"personas.codigo_ciudad_vivienda = bar.codigo_ciudad " +
															"AND " +
																"personas.codigo_departamento_vivienda = bar.codigo_departamento " +
															"AND " +
																"personas.codigo = pacientes.codigo_paciente " +
															"AND " +
																"ficha.codigoAseguradora = convenios.codigo " +
														/*	"AND " +
																"cuentas.codigo_paciente=pacientes.codigo_paciente " +
															"AND " +
																"cuentas.convenio_por_defecto = convenios.codigo " +	*/
															"AND " +
																"ficha.institucionatendio = unidades.codigo " +
															"AND " +
																"ficha.estado != 1 " +
															"AND " +
																"ficha.fechaDiligenciamiento >= ? " +
															"AND " +
																"ficha.fechaDiligenciamiento <= ? ";
    
    
    
    
    private static final String consultaEventosMortalidad = "SELECT " +
															    "ciu.nombre AS nombreMunResidencia," +
																"ciu.codigo_ciudad AS codigoMunResidencia," +
																"dep.nombre AS nombreDepResidencia," +
																"ciu.codigo_departamento AS codigoDepResidencia," +
																
																"ciu2.nombre AS nombreMunOrigen," +
																"ciu2.codigo_ciudad AS codigoMunOrigen," +
																"dep2.nombre AS nombreDepOrigen," +
																"ciu2.codigo_departamento AS codigoDepOrigen," +
																"numero_identificacion," +
																
																"primer_nombre," +
																"segundo_nombre," +
																"primer_apellido," +
																"segundo_apellido," +
																"tipo_identificacion," +
																"fecha_nacimiento," +
																"personas.sexo AS sexoPaciente," +
																"bar.nombre AS nombreBarrio," +
																"personas.codigo_barrio_vivienda AS codigoBarrio," +
																"codigo_barrio_vivienda," +
																"personas.direccion AS direccion," +
																"personas.telefono AS telefono," +
																"convenios.nombre AS nombreAseguradora," +
																"convenios.tipo_regimen AS regimenSalud," +
																"pacientes.zona_domicilio AS zonaResidencia," +
																
																"ficha.fechaDiligenciamiento AS fechaNotificacion," +
																"ficha.horaDiligenciamiento," +
																"dia.codigoEnfermedadesNotificables AS codigoEnfNot, " +
																"ficha.estado as estadoFicha, " +
																"ficha.codigoFichaMortalidad," +
																"ficha.institucionAtendio AS codigoInstitucion, " +
																"ficha.fechaInicioSintomasGeneral AS fechaInicioSintomas, " +
																
																"unidades.nombre AS nombreInstitucion, " +
																
																"ficha.fechaDefuncion AS fechaDefuncion," +
																"ficha.horaDefuncion AS horaDefuncion," +
																"ficha.fechaInicioSintomasGeneral AS fechaInicioSintomas," +
															//	"ficha.fechaNotificacion AS fechaNotificacion," +
																"ficha.pesoNacimiento AS pesoNacimiento," +
																"ficha.edadGestacional AS edadGestacional," +
																"ficha.sitioDefuncion AS sitioDefuncion," +
																"ven.codigoenfermedadesnotificables AS tipoMortalidad " +
																
															"FROM " +
																"personas," +			    											
																"diagnosticos dia," +
																"ciudades ciu," +
																"departamentos dep, " +
																"ciudades ciu2, " +
																"departamentos dep2, " +
																"barrios bar,  " +
																"convenios, " +
																"pacientes, " +
															//	"cuentas, " +
																"vigiunidadesprimarias unidades," +
																"vigiFichaMortalidad ficha," +
																"vigiEnfNotificables ven " +
															"WHERE " +
																"personas.codigo=ficha.codigoPaciente " +
															"AND " +
																"dia.acronimo=ficha.acronimo " +
															"AND " +
																"ven.codigoenfermedadesnotificables = dia.codigoenfermedadesnotificables " +
															"AND " +
																"personas.codigo_ciudad_vivienda = ciu.codigo_ciudad " +
															"AND " +
																"personas.codigo_departamento_vivienda = ciu.codigo_departamento " +
															"AND " +
																"personas.codigo_departamento_vivienda = dep.codigo "+
															"AND " +
																"personas.codigo_ciudad_vivienda = ciu2.codigo_ciudad " +
															"AND " +
																"personas.codigo_departamento_vivienda = ciu2.codigo_departamento " +
															"AND " +
																"personas.codigo_departamento_vivienda = dep2.codigo " +
															"AND " +
																"personas.codigo_barrio_vivienda = bar.codigo_barrio " +
															"AND " +
																"personas.codigo_ciudad_vivienda = bar.codigo_ciudad " +
															"AND " +
																"personas.codigo_departamento_vivienda = bar.codigo_departamento " +
															"AND " +
																"personas.codigo = pacientes.codigo_paciente " +
															"AND " +
																"ficha.codigoAseguradora = convenios.codigo " +
													/*		"AND " +
																"cuentas.codigo_paciente=pacientes.codigo_paciente " +
															"AND " +
																"cuentas.convenio_por_defecto = convenios.codigo " +	*/
															"AND " +
																"ficha.institucionatendio = unidades.codigo " +
															"AND " +
																"ficha.estado != 1 " +
															"AND " +
																"ficha.fechaDiligenciamiento >= ? " +
															"AND " +
																"ficha.fechaDiligenciamiento <= ? ";
    
    
    
    private static final String consultaEventosBrotes = "SELECT " +
    															/*
																"ciu.nombre AS nombreMunNotifica," +
																"ciu.codigo_ciudad AS codigoMunNotifica," +
																"dep.nombre AS nombreDepNotifica," +
																"ciu.codigo_departamento AS codigoDepNotifica," +
																*/
																"ciu.nombre AS nombreMunProcedencia," +
																"ciu.codigo_ciudad AS codigoMunProcedencia," +
																"dep.nombre AS nombreDepProcedencia," +
																"ciu.codigo_departamento AS codigoDepProcedencia," +
																
																"ficha.fechaNotificacion," +
																"ficha.fechaDiligenciamiento," +
																"ficha.horaDiligenciamiento," +
																"ficha.estado as estadoFicha, " +
																"ficha.codigoFichaBrotes," +
																"ficha.unidadGeneradora AS codigoInstitucion, " +
																"ficha.muertos AS muertos," +
																"ficha.pacientesGrupo1 AS pacientesGrupo1," +
																"ficha.pacientesGrupo2 AS pacientesGrupo2," +
																"ficha.pacientesGrupo3 AS pacientesGrupo3," +
																"ficha.pacientesGrupo4 AS pacientesGrupo4," +
																"ficha.pacientesGrupo5 AS pacientesGrupo5," +
																"ficha.pacientesGrupo6 AS pacientesGrupo6," +
																"ficha.hombres AS hombres," +
																"ficha.mujeres AS mujeres," +
																"ficha.evento AS evento," +
																"ficha.muertos AS muertos," +
																
																"unidades.nombre AS nombreInstitucion " +
															"FROM " +
																"ciudades ciu," +
																"departamentos dep, " +
																"vigiunidadesprimarias unidades," +
																"vigiFichaBrotes ficha " +
															"WHERE " +
																"ficha.municipioProcedencia = ciu.codigo_ciudad " +
															"AND " +
																"ficha.departamentoProcedencia = ciu.codigo_departamento " +
															"AND " +
																"ficha.departamentoProcedencia = dep.codigo " +
															"AND " +
																"ficha.unidadGeneradora = unidades.codigo " +
															"AND " +
																"ficha.estado != 1 " +
															"AND " +
																"ficha.fechaDiligenciamiento >= ? " +
															"AND " +
																"ficha.fechaDiligenciamiento <= ? ";
    
    
	
    
    public static final String insertarRegistroArchivoStr = "INSERT INTO vigiArchivoReporte " +
    															"(" +
	    															"codigo," +
	    															"ruta," +
	    															"tipo," +
	    															"autor," +
	    															"fecha," +
	    															"semana," +
	    															"anyo" +
	    														") " +
	    													" VALUES (?,?,?,?,CURRENT_DATE,?,?)";
    
    
    public static final String modificarRegistroArchivoStr = "UPDATE vigiArchivoReporte " +
    																"SET " +
    																	"autor=?," +
    																	"fecha=CURRENT_DATE " +
    																"WHERE semana=? " +
    																"AND anyo = ? ";
    
    
    
    public static final String consultarRegistroArchivoStr = "SELECT " +
    															
    															"ruta," +
    															"vigiArchivoReporte.tipo AS tipo, " +
    															"personas.primer_nombre," +
    															"personas.segundo_nombre," +
    															"personas.primer_apellido," +
    															"personas.segundo_apellido," +
    															"fecha," +
    															"semana," +
    															"anyo " +
    														"FROM vigiArchivoReporte, personas, usuarios " +
    														"WHERE " +
    															"semana = ? " +
    														"AND " +
    															"anyo = ? " +
    														"AND vigiArchivoReporte.autor = usuarios.login " +
    														"AND personas.codigo = usuarios.codigo_persona ";
    
    
    
    
    public static final String consultaExisteArchivoStr = "SELECT " +
															"ruta," +
															"fecha " +
														  "FROM " +
														  	"vigiArchivoReporte " +
														  "WHERE " +
														  	"semana = ? " +
														  "AND anyo = ? " +
														  "AND tipo = ? ";
    
    
    
    
    
    public static ResultSet consultaMorbilidad(Connection con,int semana,int anyo)
    {
    	try {
    		
    		ResultSet rs;
    		
    		SemanaEpidemiologica semanaEpidemiologica;
    		String fechaInicial = "";
    		String fechaFinal = "";
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaEventosMorbilidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		/*
    		if (semana==0) {
    			
    			String fechaActual = UtilidadFecha.getFechaActual();
    			
    			int numSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual);
    			
    			anyo = Integer.parseInt(fechaActual.split("/")[2]);
    			
    			int numSemanaAnterior;
    			
    			if (numSemanaActual==1) {
    				numSemanaAnterior = 52;
    				anyo = anyo-1;
    				
    				semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(numSemanaAnterior,anyo);
    			}
    			else {
    				numSemanaAnterior = numSemanaActual-1;
    				
    				semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(numSemanaAnterior,anyo);
    			}
    			
    			fechaInicial = semanaEpidemiologica.getFechaInicial();
        		fechaFinal = semanaEpidemiologica.getFechaFinal();
        		
        		logger.info("SEMANA EPIDEMIOLOGICA ACTUAL : "+numSemanaActual);
        		logger.info("SEMANA EPIDEMIOLOGICA ANTERIOR : "+numSemanaAnterior);
        		logger.info("FECHA INICIAL : "+fechaInicial);
        		logger.info("FECHA FINAL : "+fechaFinal);
        		
        		consulta.setString(1,fechaInicial);
        		consulta.setString(2,fechaFinal);
        		
        		return consulta.executeQuery();
    		}
    		
    		
    		else {
    			*/
    			semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
        		
        		fechaInicial = semanaEpidemiologica.getFechaInicial();
        		fechaFinal = semanaEpidemiologica.getFechaFinal();
        		
        		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
        		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
        		
        		return consulta.executeQuery();
    	//	}
    	}
    	catch (SQLException sqle) {
			
			logger.error("Error consultando los eventos de morbilidad "+sqle.getMessage());
			return null;
		}	
    }
        
    
    
    public static ResultSet consultaMortalidad(Connection con,int semana,int anyo)
    {
    	try {
    		
    		ResultSet rs;
    		
    		SemanaEpidemiologica semanaEpidemiologica;
    		String fechaInicial = "";
    		String fechaFinal = "";
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaEventosMortalidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		fechaInicial = semanaEpidemiologica.getFechaInicial();
    		fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los casos de mortalidad "+sqle.getMessage());
    		return null;
    	}
    }
    
    
    
    
    public static ResultSet consultaBrotes(Connection con,int semana,int anyo)
    {
    	try {
    		
    		ResultSet rs;
    		
    		SemanaEpidemiologica semanaEpidemiologica;
    		String fechaInicial = "";
    		String fechaFinal = "";
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaEventosBrotes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		semanaEpidemiologica = CalendarioEpidemiologico.obtenerSemanaEpidemiologica(semana,anyo);
    		
    		fechaInicial = semanaEpidemiologica.getFechaInicial();
    		fechaFinal = semanaEpidemiologica.getFechaFinal();
    		
    		consulta.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
    		consulta.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los casos de brotes "+sqle.getMessage());
    		return null;
    	}
    }
    
    
    
    
    
    public static int insertarRegistroArchivo(  Connection con,
    											String ruta,
    											int tipo,
    											String autor,
    											int semana,
    											int anyo,
    											String secuenciaArchivos
    											)
    {
    	int resultado=0;
		int codigo;
		
		try {
		
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuenciaArchivos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				codigo = rs.getInt(1);
			}
			else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
			
			PreparedStatementDecorator insertarRegistro =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroArchivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarRegistro.setInt(1,codigo);
			insertarRegistro.setString(2,ruta);
			insertarRegistro.setInt(3,tipo);
			insertarRegistro.setString(4,autor);
			insertarRegistro.setInt(5,semana);
			insertarRegistro.setInt(6,anyo);
			
			resultado = insertarRegistro.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			else {
			
				resultado = codigo;
			}
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción del registro de archivo: SqlBaseReportesSecretariaDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
    }
    
    
    
    
    
    
    
    public static int modificarRegistroArchivo(Connection con, int semana, int anyo, String autor)
    {
    	int result=0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator modificarRegistro =  new PreparedStatementDecorator(con.prepareStatement(modificarRegistroArchivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			UtilidadBD.ingresarDatoAStatement(modificarRegistro,autor,1,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarRegistro,Integer.toString(semana),2,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarRegistro,Integer.toString(anyo),3,Types.INTEGER,true,false);

			result = modificarRegistro.executeUpdate();
			
			if(result<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la modificacion de datos: SqlBaseReportesSecretariaDao "+sqle.toString() );
			result=0;
		}
		
		return result;
    }
    
    
    
    public static boolean consultaExisteArchivo(Connection con,int tipo,int semana, int anyo)
    {
    	boolean resultado = false;
    	
    	try {
    		ResultSet rs;
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaExisteArchivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setInt(1,semana);
    		consulta.setInt(2,anyo);
    		consulta.setInt(3,tipo);
    		
    		rs = consulta.executeQuery();
    		
    		if (rs.next()) {
    			resultado = true;
    		}
    		else {
    			resultado = false;
    		}
    	}
    	catch (SQLException sqle) {
    		logger.info(sqle.getMessage());
    	}
    	
    	return resultado;
    }
    
    
    
    
    public static ResultSet consultaArchivos(Connection con, 
    											boolean consultarMorbilidad, 
    											boolean consultarMortalidad, 
    											boolean consultarBrotes,
    											boolean consultarSivim,
    											boolean consultarSisvan,
    											int semana,
    											int anyo)
    {
    	try {
    		
    		ResultSet rs;
    		
    		String stringConsulta = consultarRegistroArchivoStr;
    		
    		int numeroArchivos = 0;
    		
    		if (consultarMorbilidad) {
    			numeroArchivos++;
    		}
    		
    		if (consultarMortalidad) {
    			numeroArchivos++;
    		}
    		
    		if (consultarBrotes) {
    			numeroArchivos++;
    		}
    		
    		if (consultarSivim) {
    			numeroArchivos++;
    		}
    		
    		if (consultarSisvan) {
    			numeroArchivos++;
    		}
    		
    		if (numeroArchivos==1) {
    			
    			stringConsulta += " AND (";
    			
    			if (consultarMorbilidad) {
        			stringConsulta += " tipo=1 )";
        		}
        		
        		if (consultarMortalidad) {
        			stringConsulta += " tipo=2 )";
        		}
        		
        		if (consultarBrotes) {
        			stringConsulta += " tipo=3 )";
        		}
        		
        		if (consultarSivim) {
        			stringConsulta += " tipo=4 )";
        		}
        		
        		if (consultarSisvan) {
        			stringConsulta += " tipo=5 )";
        		}
    		}
    		else {
    			
    			stringConsulta += " AND (";
    			
    			boolean pasoPrimero = false;
    			
    			if (consultarMorbilidad && !pasoPrimero) {
        			stringConsulta += " tipo=1 ";
        			pasoPrimero = true;
        			consultarMorbilidad = false;
        		}
        		
        		if (consultarMortalidad && !pasoPrimero) {
        			stringConsulta += " tipo=2 ";
        			pasoPrimero = true;
        			consultarMortalidad = false;
        		}
        		
        		if (consultarBrotes && !pasoPrimero) {
        			stringConsulta += " tipo=3 ";
        			pasoPrimero = true;
        			consultarBrotes = false;
        		}
        		
        		if (consultarSivim && !pasoPrimero) {
        			stringConsulta += " tipo=4 ";
        			pasoPrimero = true;
        			consultarSivim = false;
        		}
        		
        		if (consultarSisvan && !pasoPrimero) {
        			stringConsulta += " tipo=5 ";
        			pasoPrimero = true;
        			consultarSisvan = false;
        		}
        		
        		
        		
        		if (consultarMorbilidad) {
        			stringConsulta += " OR tipo=1 ";
        		}
        		
        		if (consultarMortalidad) {
        			stringConsulta += " OR tipo=2 ";
        		}
        		
        		if (consultarBrotes) {
        			stringConsulta += " OR tipo=3 ";
        		}
        		
        		if (consultarSivim) {
        			stringConsulta += " OR tipo=4 ";
        		}
        		
        		if (consultarSisvan) {
        			stringConsulta += " OR tipo=5 ";
        		}
        		
        		stringConsulta += ")";
    		}
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		    		
    		consulta.setInt(1,semana);
    		consulta.setInt(2,anyo);
    		
    		logger.info("");
    		logger.info("");
    		logger.info(stringConsulta);
    		logger.info("");
    		logger.info("");
    		
    		return consulta.executeQuery();
    		
    		
    		
    		/*
    		
    		String stringConsulta = consultarRegistroArchivoStr;
    		
    		String stringOr = " OR ";
    		
    		stringConsulta += " AND (";
    		
    		if (consultarMorbilidad) {
    			stringConsulta += " tipo=1 ";
    		}
    		
    		if (consultarMortalidad) {
    			stringConsulta += " tipo=2 ";
    		}
    		
    		if (consultarBrotes) {
    			stringConsulta += " tipo=3 ";
    		}
    		
    		if (consultarSivim) {
    			stringConsulta += " tipo=4 ";
    		}
    		
    		if (consultarSisvan) {
    			stringConsulta += " tipo=5 ";
    		}
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		
    		consulta.setInt(1,semana);
    		consulta.setInt(2,anyo);
    		
    		return consulta.executeQuery();
    		*/
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los archivos de texto : "+sqle.getMessage());
    		return null;
    	}
    }
}
