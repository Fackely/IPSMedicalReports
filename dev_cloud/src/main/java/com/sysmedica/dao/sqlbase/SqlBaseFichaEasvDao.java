package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseFichaEasvDao {


	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaEasvDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichaeasv "+
																"(" +
																"loginUsuario,"+
																"codigoFichaEasv," +
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
																"vacuna1, " +
															    "vacuna2, " +
															    "vacuna3, " +
															    "vacuna4, " +
															    "dosis1, " +
															    "dosis2, " +
															    "dosis3, " +
															    "dosis4, " +
															    "via1, " +
															    "via2, " +
															    "via3, " +
															    "via4, " +
															    "sitio1, " +
															    "sitio2, " +
															    "sitio3, " +
															    "sitio4, " +
															    "fechaVacunacion1, " +
															    "fechaVacunacion2, " +
															    "fechaVacunacion3, " +
															    "fechaVacunacion4, " +
															    "fabricante1, " +
															    "fabricante2, " +
															    "fabricante3, " +
															    "fabricante4, " +
															    "lote1, " +
															    "lote2, " +
															    "lote3, " +
															    "lote4, " +
															    "otroHallazgo, " +
															    "tiempo, " +
															    "unidadTiempo, " +
															    "lugarVacunacion, " +
															    "codDepVacunacion, " +
															    "codMunVacunacion, " +
															    "estadoSalud, " +
															    "recibiaMedicamentos, " +
															    "medicamentos, " +
															    "antPatologicos, " +
															    "cualesAntPatologicos, " +
															    "antAlergicos, " +
															    "cualesAntAlergicos, " +
															    "antEasv, " +
															    "cualesAntEasv, " +
															    "biologico1, " +
															    "fabricanteMuestra1, " +
															    "loteMuestra1, " +
															    "cantidadMuestra1, " +
															    "fechaEnvioMuestra1, " +
															    "biologico2, " +
															    "fabricanteMuestra2, " +
															    "loteMuestra2, " +
															    "cantidadMuestra2, " +
															    "fechaEnvioMuestra2, " +
															    "estadoFinal, " +
															    "telefonoContacto, " +
																
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
																	"?,?,?,?,?,?,?,?,?,?" +
																	") ";
    
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaEasv " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"vacuna1=?, " +
													    "vacuna2=?, " +
													    "vacuna3=?, " +
													    "vacuna4=?, " +
													    "dosis1=?, " +
													    "dosis2=?, " +
													    "dosis3=?, " +
													    "dosis4=?, " +
													    "via1=?, " +
													    "via2=?, " +
													    "via3=?, " +
													    "via4=?, " +
													    "sitio1=?, " +
													    "sitio2=?, " +
													    "sitio3=?, " +
													    "sitio4=?, " +
													    "fechaVacunacion1=?, " +
													    "fechaVacunacion2=?, " +
													    "fechaVacunacion3=?, " +
													    "fechaVacunacion4=?, " +
													    "fabricante1=?, " +
													    "fabricante2=?, " +
													    "fabricante3=?, " +
													    "fabricante4=?, " +
													    "lote1=?, " +
													    "lote2=?, " +
													    "lote3=?, " +
													    "lote4=?, " +
													    "otroHallazgo=?, " +
													    "tiempo=?, " +
													    "unidadTiempo=?, " +
													    "lugarVacunacion=?, " +
													    "codDepVacunacion=?, " +
													    "codMunVacunacion=?, " +
													    "estadoSalud=?, " +
													    "recibiaMedicamentos=?, " +
													    "medicamentos=?, " +
													    "antPatologicos=?, " +
													    "cualesAntPatologicos=?, " +
													    "antAlergicos=?, " +
													    "cualesAntAlergicos=?, " +
													    "antEasv=?, " +
													    "cualesAntEasv=?, " +
													    "biologico1=?, " +
													    "fabricanteMuestra1=?, " +
													    "loteMuestra1=?, " +
													    "cantidadMuestra1=?, " +
													    "fechaEnvioMuestra1=?, " +
													    "biologico2=?, " +
													    "fabricanteMuestra2=?, " +
													    "loteMuestra2=?, " +
													    "cantidadMuestra2=?, " +
													    "fechaEnvioMuestra2=?, " +
													    "estadoFinal=?, " +
													    "telefonoContacto=?, " +
														
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
													"WHERE codigoFichaEasv=? ";
    
    
    

    private static final String consultarFichaEasvStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.otroHallazgo, " +
																    "ficha.tiempo, " +
																    "ficha.unidadTiempo, " +
																    "ficha.lugarVacunacion, " +
																    "ficha.codDepVacunacion, " +
																    "ficha.codMunVacunacion, " +
																    "ficha.estadoSalud, " +
																    "ficha.recibiaMedicamentos, " +
																    "ficha.medicamentos, " +
																    "ficha.antPatologicos, " +
																    "ficha.cualesAntPatologicos, " +
																    "ficha.antAlergicos, " +
																    "ficha.cualesAntAlergicos, " +
																    "ficha.antEasv, " +
																    "ficha.cualesAntEasv, " +
																    "ficha.biologico1, " +
																    "ficha.fabricanteMuestra1, " +
																    "ficha.loteMuestra1, " +
																    "ficha.cantidadMuestra1, " +
																    "ficha.fechaEnvioMuestra1, " +
																    "ficha.biologico2, " +
																    "ficha.fabricanteMuestra2, " +
																    "ficha.loteMuestra2, " +
																    "ficha.cantidadMuestra2, " +
																    "ficha.fechaEnvioMuestra2, " +
																    "ficha.estadoFinal, " +
																    "ficha.telefonoContacto, " +
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
																	"epidemiologia.vigifichaeasv ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaEasv = ? " +
																	
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
																"per.codigo = ? ";



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
	
	
	
	
	
	private static final String eliminarHallazgosStr = "DELETE from epidemiologia.vigiDetalleHallazgosEasv WHERE codigoFichaEasv = ?";
	
	private static final String insertarHallazgo = "INSERT INTO epidemiologia.vigiDetalleHallazgosEasv(codigohallazgo,codigofichaeasv) VALUES(?,?)";
	
	public static final String consultaHallazgoStr = "SELECT " +
															"codigoHallazgo " +
														"FROM " +
															"epidemiologia.vigiDetalleHallazgosEasv " +
														"WHERE " +
															"codigoFichaEasv = ? ";
	
	
	
	
	private static final String insertarVacunaStr = "INSERT INTO epidemiologia.vigiVacunasImplicadas (" +
														"codigo," +
														"codigoFicha," +
														"vacuna," +
														"dosis," +
														"via," +
														"sitio," +
														"fechaVacunacion," +
														"fabricante," +
														"lote" +
													") " +
													"VALUES (?,?,?,?,?,?,?,?,?)";
	
	
	
	private static final String modificarVacunaStr = "UPDATE epidemiologia.vigiVacunasImplicadas SET " +
														"codigoFicha=?, " +
														"vacuna=?, " +
														"dosis=?, " +
														"sitio=?, " +
														"fechaVacunacion=?, " +
														"fabricante=?, " +
														"lote=? " +
													"WHERE codigo=? ";
	
	
	
	private static final String eliminarVacunaStr = "DELETE FROM epidemiologia.vigiVacunasImplicadas WHERE codigo=? ";
	
	
	
	private static final String consultarVacunaStr = "SELECT " +
														"codigo," +
														"vacuna," +
														"dosis," +
														"via," +
														"sitio," +
														"fechaVacunacion," +
														"fabricante," +
														"lote " +
													"FROM epidemiologia.vigiVacunasImplicadas " +
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
										    
										    int vacuna1,
										    int vacuna2,
										    int vacuna3,
										    int vacuna4,
										    int dosis1,
										    int dosis2,
										    int dosis3,
										    int dosis4,
										    int via1,
										    int via2,
										    int via3,
										    int via4,
										    int sitio1,
										    int sitio2,
										    int sitio3,
										    int sitio4,
										    String fechaVacunacion1,
										    String fechaVacunacion2,
										    String fechaVacunacion3,
										    String fechaVacunacion4,
										    String fabricante1,
										    String fabricante2,
										    String fabricante3,
										    String fabricante4,
										    String lote1,
										    String lote2,
										    String lote3,
										    String lote4,
										    String otroHallazgo,
										    String tiempo,
										    int unidadTiempo,
										    String lugarVacunacion,
										    String codDepVacunacion,
										    String codMunVacunacion,
										    int estadoSalud,
										    int recibiaMedicamentos,
										    String medicamentos,
										    int antPatologicos,
										    String cualesAntPatologicos,
										    int antAlergicos,
										    String cualesAntAlergicos,
										    int antEasv,
										    String cualesAntEasv,
										    int biologico1,
										    String fabricanteMuestra1,
										    String loteMuestra1,
										    String cantidadMuestra1,
										    String fechaEnvioMuestra1,
										    int biologico2,
										    String fabricanteMuestra2,
										    String loteMuestra2,
										    String cantidadMuestra2,
										    String fechaEnvioMuestra2,
										    int estadoFinal,
										    String telefonoContacto,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    
										    HashMap hallazgos,
										    HashMap vacunas,
										    String secuenciaVac,
										    String lugarVac
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
            
            String codigoMunVac = lugarVac.split("-")[0];
            String codigoDepVac = lugarVac.split("-")[1];
            
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
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacuna1),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacuna2),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacuna3),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacuna4),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis1),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis2),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis3),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis4),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(via1),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(via2),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(via3),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(via4),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sitio1),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sitio2),26,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sitio3),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sitio4),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunacion1,29,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunacion2,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunacion3,31,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunacion4,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fabricante1,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fabricante2,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fabricante3,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fabricante4,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lote1,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lote2,38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lote3,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lote4,40,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,otroHallazgo,41,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,tiempo,42,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadTiempo),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarVacunacion,44,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepVac,45,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunVac,46,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estadoSalud),47,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(recibiaMedicamentos),48,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,medicamentos,49,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antPatologicos),50,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualesAntPatologicos,51,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antAlergicos),52,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualesAntAlergicos,53,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antEasv),54,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualesAntEasv,55,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(biologico1),56,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fabricanteMuestra1,57,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,loteMuestra1,58,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cantidadMuestra1,59,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaEnvioMuestra1,60,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(biologico2),61,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fabricanteMuestra2,62,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,loteMuestra2,63,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cantidadMuestra2,64,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaEnvioMuestra2,65,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estadoFinal),66,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,telefonoContacto,67,Types.VARCHAR,true,false);
			
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,68,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,69,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),70,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),71,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,72,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),73,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,74,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),75,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),76,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,77,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),78,Types.INTEGER,true,false);
            
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    result = codigo;
			}
            
            
            for (int i=1;i<hallazgos.size()+1;i++) {
		    	
		    	String val = hallazgos.get("hallazgo_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarHallazgos =  new PreparedStatementDecorator(con.prepareStatement(insertarHallazgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarHallazgos.setInt(1,i);
			    	insertarHallazgos.setInt(2,codigo);
			    	
			    	result = insertarHallazgos.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            int tamVacunas = vacunas.size()/7;
            
            for (int i=0;i<tamVacunas;i++) {
            			    	
		    	String vacuna = vacunas.get("vacuna_"+i).toString();
		    	String dosis = vacunas.get("dosis_"+i).toString();
		    	String via = vacunas.get("via_"+i).toString();
		    	String sitio = vacunas.get("sitio_"+i).toString();
		    	String fecha = vacunas.get("fecha_"+i).toString();
		    	String fabricante = vacunas.get("fabricante_"+i).toString();
		    	String lote = vacunas.get("lote_"+i).toString();
		    	int codigoVac;
		    	
		    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaVac,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ResultSet rs2 = statement.executeQuery();
	            
	            if (rs2.next()) {
	                codigoVac = rs2.getInt(1);
	            }
	            else {
					logger.error("Error obteniendo el código de la secuencia de vacunas ");
					return 0;
				}
	            
	            PreparedStatementDecorator insertarVacuna =  new PreparedStatementDecorator(con.prepareStatement(insertarVacunaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,Integer.toString(codigoVac),1,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,Integer.toString(codigo),2,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,vacuna,3,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,dosis,4,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,via,5,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,sitio,6,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,fecha,7,Types.VARCHAR,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,fabricante,8,Types.VARCHAR,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarVacuna,lote,9,Types.VARCHAR,true,false);
	                        
	            result = insertarVacuna.executeUpdate();
	            
	            if(result<1)
	            {
	                daoFactory.abortTransaction(con);
	                return -1; // Estado de error
	            }
		    }
            

            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaEasvDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
	
	
	
	
	

	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaEasv,
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

									    int vacuna1,
									    int vacuna2,
									    int vacuna3,
									    int vacuna4,
									    int dosis1,
									    int dosis2,
									    int dosis3,
									    int dosis4,
									    int via1,
									    int via2,
									    int via3,
									    int via4,
									    int sitio1,
									    int sitio2,
									    int sitio3,
									    int sitio4,
									    String fechaVacunacion1,
									    String fechaVacunacion2,
									    String fechaVacunacion3,
									    String fechaVacunacion4,
									    String fabricante1,
									    String fabricante2,
									    String fabricante3,
									    String fabricante4,
									    String lote1,
									    String lote2,
									    String lote3,
									    String lote4,
									    String otroHallazgo,
									    String tiempo,
									    int unidadTiempo,
									    String lugarVacunacion,
									    String codDepVacunacion,
									    String codMunVacunacion,
									    int estadoSalud,
									    int recibiaMedicamentos,
									    String medicamentos,
									    int antPatologicos,
									    String cualesAntPatologicos,
									    int antAlergicos,
									    String cualesAntAlergicos,
									    int antEasv,
									    String cualesAntEasv,
									    int biologico1,
									    String fabricanteMuestra1,
									    String loteMuestra1,
									    String cantidadMuestra1,
									    String fechaEnvioMuestra1,
									    int biologico2,
									    String fabricanteMuestra2,
									    String loteMuestra2,
									    String cantidadMuestra2,
									    String fechaEnvioMuestra2,
									    int estadoFinal,
									    String telefonoContacto,
										String pais,
									    int areaProcedencia,
									    HashMap hallazgos,
									    HashMap vacunas,
									    String secuenciaVac,
									    String lugarVac
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
            
            String codMunVacuna = lugarVac.split("-")[0];
            String codDepVacuna = lugarVac.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);

            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacuna1),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacuna2),4,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacuna3),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacuna4),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis1),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis2),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis3),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis4),10,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(via1),11,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(via2),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(via3),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(via4),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sitio1),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sitio2),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sitio3),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sitio4),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunacion1,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunacion2,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunacion3,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunacion4,22,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fabricante1,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fabricante2,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fabricante3,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fabricante4,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lote1,27,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lote2,28,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lote3,29,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lote4,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,otroHallazgo,31,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tiempo,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadTiempo),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarVacunacion,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codDepVacuna,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codMunVacuna,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estadoSalud),37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(recibiaMedicamentos),38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,medicamentos,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antPatologicos),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualesAntPatologicos,41,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antAlergicos),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualesAntAlergicos,43,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antEasv),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualesAntEasv,45,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(biologico1),46,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fabricanteMuestra1,47,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,loteMuestra1,48,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cantidadMuestra1,49,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaEnvioMuestra1,50,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(biologico2),51,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fabricanteMuestra2,52,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,loteMuestra2,53,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cantidadMuestra2,54,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaEnvioMuestra2,55,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estadoFinal),56,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,telefonoContacto,57,Types.VARCHAR,true,false);
						
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,59,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,60,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,61,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),62,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),63,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),65,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,66,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,67,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,68,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),69,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,70,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),71,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaEasv),72,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    PreparedStatementDecorator eliminarHallazgos =  new PreparedStatementDecorator(con.prepareStatement(eliminarHallazgosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarHallazgos.setInt(1,codigoFichaEasv);
		    result = eliminarHallazgos.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<20;i++) {
		    	
		    	try {
			    	String val = hallazgos.get("hallazgo_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarHallazgos =  new PreparedStatementDecorator(con.prepareStatement(insertarHallazgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarHallazgos.setInt(1,i);
				    	insertarHallazgos.setInt(2,codigoFichaEasv);
				    	
				    	result = insertarHallazgos.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }
		    
		    
		    
		    
		    PreparedStatementDecorator eliminarVacunas =  new PreparedStatementDecorator(con.prepareStatement(eliminarVacunaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarVacunas.setInt(1,codigoFichaEasv);
		    result = eliminarVacunas.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<20;i++) {
		    	
		    	try {
			    	String vacuna = vacunas.get("vacuna_"+i).toString();
			    	String dosis = vacunas.get("dosis_"+i).toString();
			    	String via = vacunas.get("via_"+i).toString();
			    	String sitio = vacunas.get("sitio_"+i).toString();
			    	String fecha = vacunas.get("fecha_"+i).toString();
			    	String fabricante = vacunas.get("fabricante_"+i).toString();
			    	String lote = vacunas.get("lote_"+i).toString();
			    	int codigoVac;
			    	
			    	
			    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaVac,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            ResultSet rs2 = statement.executeQuery();
		            
		            if (rs2.next()) {
		                codigoVac = rs2.getInt(1);
		            }
		            else {
						logger.error("Error obteniendo el código de la secuencia de vacunas ");
						return 0;
					}
		            
		            PreparedStatementDecorator insertarVacuna =  new PreparedStatementDecorator(con.prepareStatement(insertarVacunaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,Integer.toString(codigoVac),1,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,Integer.toString(codigoFichaEasv),2,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,vacuna,3,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,dosis,4,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,via,5,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,sitio,6,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,fecha,7,Types.VARCHAR,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,fabricante,8,Types.VARCHAR,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarVacuna,lote,9,Types.VARCHAR,true,false);
		                        
		            result = insertarVacuna.executeUpdate();
		            
		            if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
			    catch (NullPointerException npe) {}
		    }
		    

		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaEasvDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	
	
	

	public static ResultSet consultarTodoFichaEasv(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaEasvStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de EASV "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaEasvDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	

    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarHallazgos(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaHallazgoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los hallazgos semiologicos (ficha de EASV) "+sqle);
			return null;
        }
    }
    
	
	
    
    
    
    public static ResultSet consultarVacunas(Connection con, int codigo)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarVacunaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando las vacunas (ficha de EASV) "+sqle);
			return null;
    	}
    }
    
}
