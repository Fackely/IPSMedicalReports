package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseFichaTosferinaDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaTosferinaDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichatosferina "+
																"(" +
																"loginUsuario,"+
																"codigoFichaTosferina," +
																"codigoPaciente,"+
																"estado,"+
																"acronimo,"+
																"fechaDiligenciamiento," +
																"horaDiligenciamiento," +
																"codigomunprocedencia," +
																"codigodepprocedencia," +
																"codigomunnoti," +
																"codigodepnoti," +
																"codigoaseguradora," +
																"nombreprofesionaldiligencio, " +
																
																"sire, " +
																"nombrePadre, " +
																"fechaInvestigacion, " +
																"identificacionCaso, " +
																"contactoCaso, " +
																"carneVacunacion, " +
																"dosisAplicadas, " +
																"tipoVacuna, " +
																"cualVacuna, " +
																"fechaUltimaDosis, " +
																"etapaEnfermedad, " +
																"tos, " +
																"duracionTos, " +
																"tosParoxistica, " +
																"estridor, " +
																"apnea, " +
																"fiebre, " +
																"vomitoPostusivo, " +
																"complicaciones, " +
																"tipoComplicacion, " +
																"tratamientoAntibiotico, " +
																"tipoAntibiotico, " +
																"duracionTratamiento, " +
																"investigacionCampo, " +
																"fechaOperacionBarrido, " +
																"numeroContactos, " +
																"quimioprofilaxis, " +
																"totalPoblacion1, " +
																"totalPoblacion2, " +
																"totalPoblacion3, " +
																"totalPoblacion4, " +
																"dosisDpt1Grupo1, " +
																"dosisDpt1Grupo2, " +
																"dosisDpt1Grupo3, " +
																"dosisDpt1Grupo4, " +
																"dosisDpt2Grupo1, " +
																"dosisDpt2Grupo2, " +
																"dosisDpt2Grupo3, " +
																"dosisDpt2Grupo4, " +
																"dosisDpt3Grupo1, " +
																"dosisDpt3Grupo2, " +
																"dosisDpt3Grupo3, " +
																"dosisDpt3Grupo4, " +
																"dosisRef1Grupo1, " +
																"dosisRef1Grupo2, " +
																"dosisRef1Grupo3, " +
																"dosisRef1Grupo4, " +
																"dosisRef2Grupo1, " +
																"dosisRef2Grupo2, " +
																"dosisRef2Grupo3, " +
																"dosisRef2Grupo4, " +
																"municipiosVacunados, " +
																
																"fechaConsultaGeneral, " +
																"fechaInicioSintomasGeneral, " +
																"tipoCaso, " +
																"hospitalizadoGeneral, " +
																"fechaHospitalizacionGeneral, " +
																"estaVivoGeneral, " +
																"fechaDefuncion, " +
																"institucionAtendio," +
																"activa, " +
																"pais, " +
																"areaProcedencia " +
																") " +
															"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?" +
																	") ";
    
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaTosferina " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"nombrePadre=?, " +
														"fechaInvestigacion=?, " +
														"identificacionCaso=?, " +
														"contactoCaso=?, " +
														"carneVacunacion=?, " +
														"dosisAplicadas=?, " +
														"tipoVacuna=?, " +
														"cualVacuna=?, " +
														"fechaUltimaDosis=?, " +
														"etapaEnfermedad=?, " +
														"tos=?, " +
														"duracionTos=?, " +
														"tosParoxistica=?, " +
														"estridor=?, " +
														"apnea=?, " +
														"fiebre=?, " +
														"vomitoPostusivo=?, " +
														"complicaciones=?, " +
														"tipoComplicacion=?, " +
														"tratamientoAntibiotico=?, " +
														"tipoAntibiotico=?, " +
														"duracionTratamiento=?, " +
														"investigacionCampo=?, " +
														"fechaOperacionBarrido=?, " +
														"numeroContactos=?, " +
														"quimioprofilaxis=?, " +
														"totalPoblacion1=?, " +
														"totalPoblacion2=?, " +
														"totalPoblacion3=?, " +
														"totalPoblacion4=?, " +
														"dosisDpt1Grupo1=?, " +
														"dosisDpt1Grupo2=?, " +
														"dosisDpt1Grupo3=?, " +
														"dosisDpt1Grupo4=?, " +
														"dosisDpt2Grupo1=?, " +
														"dosisDpt2Grupo2=?, " +
														"dosisDpt2Grupo3=?, " +
														"dosisDpt2Grupo4=?, " +
														"dosisDpt3Grupo1=?, " +
														"dosisDpt3Grupo2=?, " +
														"dosisDpt3Grupo3=?, " +
														"dosisDpt3Grupo4=?, " +
														"dosisRef1Grupo1=?, " +
														"dosisRef1Grupo2=?, " +
														"dosisRef1Grupo3=?, " +
														"dosisRef1Grupo4=?, " +
														"dosisRef2Grupo1=?, " +
														"dosisRef2Grupo2=?, " +
														"dosisRef2Grupo3=?, " +
														"dosisRef2Grupo4=?, " +
														"municipiosVacunados=?, " +
														
														"codigoDepProcedencia=?, " +
														"codigoMunProcedencia=?, " +
														"fechaConsultaGeneral=?, " +
														"fechaInicioSintomasGeneral=?, " +
														"tipoCaso=?, " +
														"hospitalizadoGeneral=?, " +
														"fechaHospitalizacionGeneral=?, " +
														"estaVivoGeneral=?, " +
														"fechaDefuncion=?, " +
														"codigoDepNoti=?, " +
														"codigoMunNoti=?, " +
														"institucionAtendio=?, " +
														"pais=?, " +
														"areaProcedencia=? " +
													"WHERE codigoFichaTosferina=? ";
    
    
    
    

    private static final String consultarFichaTosferinaStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.nombrePadre, " +
																	"ficha.fechaInvestigacion, " +
																	"ficha.identificacionCaso, " +
																	"ficha.contactoCaso, " +
																	"ficha.carneVacunacion, " +
																	"ficha.dosisAplicadas, " +
																	"ficha.tipoVacuna, " +
																	"ficha.cualVacuna, " +
																	"ficha.fechaUltimaDosis, " +
																	"ficha.etapaEnfermedad, " +
																	"ficha.tos, " +
																	"ficha.duracionTos, " +
																	"ficha.tosParoxistica, " +
																	"ficha.estridor, " +
																	"ficha.apnea, " +
																	"ficha.fiebre, " +
																	"ficha.vomitoPostusivo, " +
																	"ficha.complicaciones, " +
																	"ficha.tipoComplicacion, " +
																	"ficha.tratamientoAntibiotico, " +
																	"ficha.tipoAntibiotico, " +
																	"ficha.duracionTratamiento, " +
																	"ficha.investigacionCampo, " +
																	"ficha.fechaOperacionBarrido, " +
																	"ficha.numeroContactos, " +
																	"ficha.quimioprofilaxis, " +
																	"ficha.totalPoblacion1, " +
																	"ficha.totalPoblacion2, " +
																	"ficha.totalPoblacion3, " +
																	"ficha.totalPoblacion4, " +
																	"ficha.dosisDpt1Grupo1, " +
																	"ficha.dosisDpt1Grupo2, " +
																	"ficha.dosisDpt1Grupo3, " +
																	"ficha.dosisDpt1Grupo4, " +
																	"ficha.dosisDpt2Grupo1, " +
																	"ficha.dosisDpt2Grupo2, " +
																	"ficha.dosisDpt2Grupo3, " +
																	"ficha.dosisDpt2Grupo4, " +
																	"ficha.dosisDpt3Grupo1, " +
																	"ficha.dosisDpt3Grupo2, " +
																	"ficha.dosisDpt3Grupo3, " +
																	"ficha.dosisDpt3Grupo4, " +
																	"ficha.dosisRef1Grupo1, " +
																	"ficha.dosisRef1Grupo2, " +
																	"ficha.dosisRef1Grupo3, " +
																	"ficha.dosisRef1Grupo4, " +
																	"ficha.dosisRef2Grupo1, " +
																	"ficha.dosisRef2Grupo2, " +
																	"ficha.dosisRef2Grupo3, " +
																	"ficha.dosisRef2Grupo4, " +
																	"ficha.municipiosVacunados, " +
																	"ficha.pais," +
		    														"ficha.areaProcedencia, " +
																	
																	"ficha.codigoDepProcedencia AS departamentoProcedencia, " +
																	"ficha.codigoMunProcedencia AS municipioProcedencia, " +
																	"ficha.fechaconsultageneral AS fechaConsultaGeneral, " +
																	"ficha.fechainiciosintomasgeneral AS fechaInicioSintomas, " +
																	"ficha.tipocaso AS tipoCaso, " +
																	"ficha.hospitalizadogeneral AS hospitalizado, " +
																	"ficha.fechahospitalizaciongeneral AS fechaHospitalizacion, " +
																	"ficha.estavivogeneral AS condicionFinal, " +
																	"ficha.fechadefuncion AS fechaDefuncion, " +
																	"ficha.nombreProfesionalDiligencio AS nombreProfesional, " +
																	"ficha.codigoDepNoti AS departamentoNotifica, " +
																	"ficha.codigoMunNoti AS municipioNotifica, " +
																	"ficha.institucionAtendio AS nombreUnidad, " +
																	"ficha.nombreprofesionaldiligencio AS nombreProfesional, " +
																	"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, "+
																	
																	"per.primer_nombre," +
																	"per.segundo_nombre," +
																	"per.primer_apellido," +
																	"per.segundo_apellido," +
																	"dep.descripcion AS dep_nacimiento," +
																	"ciu.descripcion AS ciu_nacimiento," +
																	"dep2.descripcion AS dep_vivienda," +
																	"ciu2.descripcion AS ciu_vivienda," +
																	"per.direccion AS direccion_paciente," +
																	"per.telefono AS telefono_paciente," +
																	"per.fecha_nacimiento," +
																	"per.sexo," +
																	"per.estado_civil," +
																	"per.numero_identificacion, " +
																	"per.codigo_pais_nacimiento, " +
																	"per.codigo_pais_vivienda, " +
																	"per.codigo_pais_id, " +
																	
																	"bar.descripcion AS barrio, " +
																	"pac.zona_domicilio AS zonaDomicilio, " +
																	"ocup.nombre AS ocupacionPaciente, " +
																	"per.tipo_identificacion AS tipoId, " +
																	"conv.nombre AS aseguradora, " +
																	"regs.nombre AS regimenSalud, " +
																	"pac.etnia AS etnia, " +
																//	"ficha.desplazado AS desplazado " +
																	"pac.grupo_poblacional as grupoPoblacional " +
																
																"FROM " +
																	"epidemiologia.vigifichatosferina ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaTosferina = ? " +
																	
																	"AND per.codigo=ficha.codigoPaciente " +
																	"AND dep.codigo_departamento=per.codigo_departamento_nacimiento " +
																	"AND dep.codigo_pais=per.codigo_pais_nacimiento " +
																	"AND ciu.codigo_ciudad=per.codigo_ciudad_nacimiento " +
																	"AND ciu.codigo_departamento=per.codigo_departamento_nacimiento " +
																	"AND ciu.codigo_pais=per.codigo_pais_nacimiento " +
																	"AND dep2.codigo_departamento=per.codigo_departamento_vivienda " +
																	"AND dep2.codigo_pais=per.codigo_pais_vivienda " +
																	"AND ciu2.codigo_ciudad=per.codigo_ciudad_vivienda " +
																	"AND ciu2.codigo_departamento=per.codigo_departamento_vivienda " +
																	"AND ciu2.codigo_pais=per.codigo_pais_vivienda " +
																	
																	"AND ficha.loginUsuario=usu.login " +
																	"AND usu.codigo_persona=per2.codigo "+
																	"AND per.codigo_barrio_vivienda=bar.codigo " +
																	"AND per.codigo=pac.codigo_paciente " +
																	"AND pac.codigo_paciente=ficha.codigoPaciente " +
																	"AND pac.ocupacion=ocup.codigo "+
																	"AND conv.codigo=ficha.codigoAseguradora " +
																	"AND conv.tipo_regimen=regs.acronimo ";
    
    
    

    public static final String consultaDatosPacienteStr = "SELECT " +
															    "per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.descripcion AS dep_vivienda," +
																"ciu.descripcion AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																"bar.descripcion AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"conv.nombre AS aseguradora, " +
																"regs.nombre AS regimenSalud, " +
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
																"FROM personas per " +
																"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_vivienda and dep.codigo_pais=per.codigo_pais_vivienda)  " +
																"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_vivienda and ciu.codigo_departamento=per.codigo_departamento_vivienda AND ciu.codigo_pais=per.codigo_pais_vivienda)  " +
																"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda)  " +
																"INNER JOIN pacientes pac on(pac.codigo_paciente=per.codigo) " +
																"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion)  " +
																"INNER JOIN cuentas c ON(c.codigo_paciente = pac.codigo_paciente) " +
																"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) " +
																"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) " +
																"INNER JOIN tipos_regimen regs ON(regs.acronimo = conv.tipo_regimen) " +
															"WHERE " +
																"per.codigo = ? " ;



	public static final String consultaDatosPacienteStr2 = "SELECT " +
															    "per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.descripcion AS dep_vivienda," +
																"ciu.descripcion AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																"bar.descripcion AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
															"FROM " +
																"personas per, departamentos dep, ciudades ciu, " +
																"barrios bar, pacientes pac, ocupaciones ocup " +
															"WHERE " +
																"per.codigo = ? " +
																"AND dep.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND dep.codigo_pais=per.codigo_pais_vivienda " +
																"AND ciu.codigo_ciudad=per.codigo_ciudad_vivienda " +
																"AND ciu.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND ciu.codigo_pais=per.codigo_pais_vivienda " +
																
																"AND per.codigo_barrio_vivienda=bar.codigo " +
																"AND per.codigo=pac.codigo_paciente " +
																"AND pac.ocupacion=ocup.codigo ";
	
	
	
	
	

    public static final String consultaDatosLaboratorio = "SELECT " +
    														"fechaToma," +
    														"fechaRecepcion," +
    														"muestra," +
    														"prueba," +
    														"agente," +
    														"resultado," +
    														"fechaResultado," +
    														"valor," +
    														"codigofichalaboratorios " +
    													  "FROM " +
    													  	"epidemiologia.vigifichalaboratorios " +
    													  "WHERE " +
    														"codigoFicha=?";
    
	
	
	

	public static int insertarFichaCompleta(Connection con,
								    		int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    String secuencia,
										    
										    String sire,
											boolean notificar,
											
											int codigoFichaIntoxicacion,										    
										    String lugarProcedencia,
										    String fechaConsultaGeneral,
										    String fechaInicioSintomasGeneral,
										    int tipoCaso,
										    boolean hospitalizadoGeneral,
										    String fechaHospitalizacionGeneral,
										    boolean estaVivoGeneral,
										    String fechaDefuncion,
										    String lugarNoti,
										    int unidadGeneradora,
										    
										    String nombrePadre,
										    String fechaInvestigacion,
										    int identificacionCaso,
										    int contactoCaso,
										    int carneVacunacion,
										    int dosisAplicadas,
										    int tipoVacuna,
										    String cualVacuna,
										    String fechaUltimaDosis,
										    int etapaEnfermedad,
										    int tos,
										    String duracionTos,
										    int tosParoxistica,
										    int estridor,
										    int apnea,
										    int fiebre,
										    int vomitoPostusivo,
										    int complicaciones,
										    int tipoComplicacion,
										    int tratamientoAntibiotico,
										    String tipoAntibiotico,
										    String duracionTratamiento,
										    int investigacionCampo,
										    String fechaOperacionBarrido,
										    String numeroContactos,
										    int quimioprofilaxis,
										    int totalPoblacion1,
										    int totalPoblacion2,
										    int totalPoblacion3,
										    int totalPoblacion4,
										    int dosisDpt1Grupo1,
										    int dosisDpt1Grupo2,
										    int dosisDpt1Grupo3,
										    int dosisDpt1Grupo4,
										    int dosisDpt2Grupo1,
										    int dosisDpt2Grupo2,
										    int dosisDpt2Grupo3,
										    int dosisDpt2Grupo4,
										    int dosisDpt3Grupo1,
										    int dosisDpt3Grupo2,
										    int dosisDpt3Grupo3,
										    int dosisDpt3Grupo4,
										    int dosisRef1Grupo1,
										    int dosisRef1Grupo2,
										    int dosisRef1Grupo3,
										    int dosisRef1Grupo4,
										    int dosisRef2Grupo1,
										    int dosisRef2Grupo2,
										    int dosisRef2Grupo3,
										    int dosisRef2Grupo4,
										    String municipiosVacunados,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   )
	{
		int result=0;
        int codigo;
        
    	try {
    		
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
            
            PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaCompletaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,login,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigo),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoPaciente),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estado),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDiagnostico,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunProcedencia,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepProcedencia,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunNoti,8,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepNoti,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoAseguradora),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreProfesional,11,Types.VARCHAR,true,false);
            
			UtilidadBD.ingresarDatoAStatement(insertarFicha,sire,12,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombrePadre,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInvestigacion,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(identificacionCaso),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(contactoCaso),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(carneVacunacion),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisAplicadas),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoVacuna),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualVacuna,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(etapaEnfermedad),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tos),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,duracionTos,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tosParoxistica),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estridor),26,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(apnea),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebre),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vomitoPostusivo),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(complicaciones),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoComplicacion),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratamientoAntibiotico),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,tipoAntibiotico,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,duracionTratamiento,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(investigacionCampo),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaOperacionBarrido,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,numeroContactos,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(quimioprofilaxis),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(totalPoblacion1),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(totalPoblacion2),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(totalPoblacion3),41,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(totalPoblacion4),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo1),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo2),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo3),45,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo4),46,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo1),47,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo2),48,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo3),49,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo4),50,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo1),51,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo2),52,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo3),53,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo4),54,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo1),55,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo2),56,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo3),57,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo4),58,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo1),59,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo2),60,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo3),61,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo4),62,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,municipiosVacunados,63,Types.VARCHAR,true,false);
						
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,65,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),66,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),67,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,68,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),69,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,70,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),71,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),72,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,73,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),74,Types.INTEGER,true,false);
            
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    result = codigo;
			}

            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaTosferinaDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
	
    
	
	
	
	
	

	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaTosferina,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
									    int estado,
									    
									    String lugarProcedencia,
									    String fechaConsultaGeneral,
									    String fechaInicioSintomasGeneral,
									    int tipoCaso,
									    boolean hospitalizadoGeneral,
									    String fechaHospitalizacionGeneral,
									    boolean estaVivoGeneral,
									    String fechaDefuncion,
									    String lugarNoti,
									    int unidadGeneradora,

									    String nombrePadre,
									    String fechaInvestigacion,
									    int identificacionCaso,
									    int contactoCaso,
									    int carneVacunacion,
									    int dosisAplicadas,
									    int tipoVacuna,
									    String cualVacuna,
									    String fechaUltimaDosis,
									    int etapaEnfermedad,
									    int tos,
									    String duracionTos,
									    int tosParoxistica,
									    int estridor,
									    int apnea,
									    int fiebre,
									    int vomitoPostusivo,
									    int complicaciones,
									    int tipoComplicacion,
									    int tratamientoAntibiotico,
									    String tipoAntibiotico,
									    String duracionTratamiento,
									    int investigacionCampo,
									    String fechaOperacionBarrido,
									    String numeroContactos,
									    int quimioprofilaxis,
									    int totalPoblacion1,
									    int totalPoblacion2,
									    int totalPoblacion3,
									    int totalPoblacion4,
									    int dosisDpt1Grupo1,
									    int dosisDpt1Grupo2,
									    int dosisDpt1Grupo3,
									    int dosisDpt1Grupo4,
									    int dosisDpt2Grupo1,
									    int dosisDpt2Grupo2,
									    int dosisDpt2Grupo3,
									    int dosisDpt2Grupo4,
									    int dosisDpt3Grupo1,
									    int dosisDpt3Grupo2,
									    int dosisDpt3Grupo3,
									    int dosisDpt3Grupo4,
									    int dosisRef1Grupo1,
									    int dosisRef1Grupo2,
									    int dosisRef1Grupo3,
									    int dosisRef1Grupo4,
									    int dosisRef2Grupo1,
									    int dosisRef2Grupo2,
									    int dosisRef2Grupo3,
									    int dosisRef2Grupo4,
									    String municipiosVacunados,
										String pais,
									    int areaProcedencia
									    )
	{
		int result=0;
        int codigo=0;
        int codigoNot=0;
        
        try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);

            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombrePadre,3,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInvestigacion,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(identificacionCaso),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(contactoCaso),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(carneVacunacion),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisAplicadas),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoVacuna),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualVacuna,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(etapaEnfermedad),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tos),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,duracionTos,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tosParoxistica),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estridor),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(apnea),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebre),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vomitoPostusivo),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(complicaciones),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoComplicacion),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratamientoAntibiotico),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tipoAntibiotico,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,duracionTratamiento,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(investigacionCampo),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaOperacionBarrido,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,numeroContactos,27,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(quimioprofilaxis),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(totalPoblacion1),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(totalPoblacion2),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(totalPoblacion3),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(totalPoblacion4),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo1),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo2),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo3),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo4),36,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo1),37,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo2),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo3),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo4),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo1),41,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo2),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo3),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo4),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo1),45,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo2),46,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo3),47,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo4),48,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo1),49,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo2),50,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo3),51,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo4),52,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,municipiosVacunados,53,Types.INTEGER,true,false);
			
			
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,55,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,56,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),58,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),59,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,60,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),61,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,63,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),65,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,66,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),67,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaTosferina),68,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    

		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaTosferinaDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	
	

	public static ResultSet consultarTodoFichaTosferina(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaTosferinaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Tosferina "+sqle);
			return null;
		}
    }
	
	
	

	public static ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo) {
    	
    	try {
    		String consultaStr = consultaDatosPacienteStr;
    		
    		if (empezarnuevo) {
    			
    			consultaStr = consultaDatosPacienteStr2;
    		}
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigo);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaTosferinaDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	
	
	

	/**
     * Metodo para consultar los laboratorios
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarDatosLaboratorio(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los datos de laboratorio (ficha de Tosferina) "+sqle);
			return null;
        }
    }
}
