package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;

public class SqlBaseIngresoPacienteOdontologiaDao {

	private static Logger logger = Logger.getLogger(SqlBaseIngresoPacienteOdontologiaDao.class);
	
	/**
	 * cadena que consulta la existencia de una persona como paciente activo
	 */
	private static final String strConsultarPersonaPaciente = "SELECT " +
			// se consulta los datos de la persona
			"per.codigo as codigo_per, " +
			"per.numero_identificacion as num_iden_per, " + 
			"per.tipo_identificacion as tipo_iden_per, " +
			"per.primer_nombre as pnombre_per, " +
			"coalesce(per.segundo_nombre,' ') as snombre_per, " + 
			"per.primer_apellido as papellido_per, " +
			"coalesce(per.segundo_apellido,' ') as sapellido_per, " +
			"CASE WHEN per.fecha_nacimiento IS NOT NULL THEN to_char(per.fecha_nacimiento,'"+ConstantesBD.formatoFechaBD+"') ELSE ' ' END as fecha_naci_per, " +
			// se consulta los datos del paciente
			"pac.activo as activo " +
			"FROM manejopaciente.pacientes pac " +
			"INNER JOIN administracion.personas per ON (per.codigo = pac.codigo_paciente) " + 
			"WHERE per.numero_identificacion = ? " +
			"AND per.tipo_identificacion = ? ";
			
	/**
	 * Cadena Sql para consultar  si se ha asiganado un plantilla a un paciente
	 */
	private static final String strConsutlarExistePlantillaPaciente="SELECT " +
			"plpac.codigo_pk AS codpk, " +
			"plpac.plantilla AS numplantilla " +
			"FROM manejopaciente.plantillas_pacientes plpac " +
			"INNER JOIN historiaclinica.plantillas pl ON (pl.codigo_pk = plpac.plantilla) " +
			"WHERE codigo_paciente = ? AND pl.tipo_funcionalidad = ? ";
	
	
	/**
	 * metodo que consulta una persona como paciente
	 * @param con
	 * @param parametros
	 * @return DtoPaciente
	 */
	public static DtoPaciente consultarPersonaPaciente(Connection con, HashMap parametros)
	{
		DtoPaciente dto = new DtoPaciente(); 
		String consulta = strConsultarPersonaPaciente;
		try{
			if(parametros.containsKey("activo"))
				consulta+=" AND pac.activo = "+parametros.get("activo")+" ";
			
			logger.info("Consulta: "+consulta);
			Utilidades.imprimirMapa(parametros);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("numero_identificacion").toString());
			ps.setString(2, parametros.get("tipo_identificacion").toString());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				// datos la persona
				dto.setCodigo(rs.getInt("codigo_per"));
				dto.setNumeroId(rs.getString("num_iden_per"));
				dto.setTipoId(rs.getString("tipo_iden_per"));
				dto.setPrimerNombre(rs.getString("pnombre_per"));
				dto.setSegundoNombre(rs.getString("snombre_per").equals(" ")?"":rs.getString("snombre_per"));
				dto.setPrimerApellido(rs.getString("papellido_per"));
				dto.setSegundoApellido(rs.getString("sapellido_per").equals(" ")?"":rs.getString("sapellido_per"));
				dto.setFechaNacimiento(rs.getString("fecha_naci_per").equals(" ")?"":rs.getString("fecha_naci_per"));
				// datos del paciente
				dto.setActivo(rs.getString("activo"));
				dto.setExistePaciente(true);
				
				// se cargan los datos de los beneficiarios del paciente
				if(dto.getCodigo()>0)
				{
					// se cargan los beneficiarios del paciente
					HashMap datosPac = new HashMap();
					datosPac.put("codigo_paciente", dto.getCodigo());
					dto.setBeneficiariosPac(consultarBeneficiariosPacientes(con, datosPac));
				}
			}else{
				dto.setExistePaciente(false);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			Utilidades.imprimirMapa(parametros);
		}
		return dto;
	}
	
	/**
	 * metodo que consulta los beneficiarios de un paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static ArrayList<DtoBeneficiarioPaciente> consultarBeneficiariosPacientes(Connection con, HashMap parametros)
	{
		ArrayList<DtoBeneficiarioPaciente> array = new ArrayList<DtoBeneficiarioPaciente>();
		String consulta = "SELECT " +
					// se consultas los datos de la persona asociadoa al beneficiario
					"per.codigo as codigo_per, " +
					"per.numero_identificacion as num_iden_per, " + 
					"per.tipo_identificacion as tipo_iden_per, " +
					"per.primer_nombre as pnombre_per, " +
					"per.segundo_nombre as snombre_per, " +
					"per.primer_apellido as papellido_per, " +
					"per.segundo_apellido as sapellido_per, " + 
					"per.fecha_nacimiento as fecha_naci_per, " +
					// datos del beneficiario
					"bpac.codigo_pk as codigo_ben, " +
					"bpac.codigo_paciente as cod_pac_ben, " +
					"bpac.persona_beneficiario as cod_per_ben, " +
					"CASE WHEN bpac.parentezco IS NOT NULL THEN paren.descripcion ELSE ' ' END as desp_parentezco, " +
					"CASE WHEN bpac.ocupacion IS NOT NULL THEN ocu.nombre ELSE ' ' END as desp_ocupacion, " +
					"CASE WHEN bpac.estudio IS NOT NULL  THEN est.nombre ELSE ' ' END as desp_estudio, " +
					"coalesce(bpac.tipo_ocupacion,' ') as tipo_ocupa " + 
					"FROM manejopaciente.beneficiarios_paciente bpac " + 
					"INNER JOIN administracion.personas per ON (per.codigo = bpac.persona_beneficiario) " +
					"LEFT OUTER JOIN odontologia.parentezco paren ON (paren.codigo_pk = bpac.parentezco ) " + 
					"LEFT OUTER JOIN manejopaciente.ocupaciones ocu ON (ocu.codigo = bpac.ocupacion ) " +
					"LEFT OUTER JOIN manejopaciente.estudio est ON (est.codigo = bpac.estudio AND est.activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" ) " +
					"WHERE bpac.codigo_paciente = ?"; 

		try{
			Utilidades.imprimirMapa(parametros);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigo_paciente").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoBeneficiarioPaciente dto = new DtoBeneficiarioPaciente();
				// datos de persona asociados al beneficiario
				dto.setCodigo(rs.getInt("codigo_per"));
				dto.setNumeroId(rs.getString("num_iden_per"));
				dto.setTipoId(rs.getString("tipo_iden_per"));
				dto.setPrimerNombre(rs.getString("pnombre_per"));
				dto.setSegundoNombre(rs.getString("snombre_per").equals(" ")?"":rs.getString("snombre_per"));
				dto.setPrimerApellido(rs.getString("papellido_per"));
				dto.setSegundoApellido(rs.getString("sapellido_per").equals(" ")?"":rs.getString("sapellido_per"));
				dto.setFechaNacimiento(rs.getString("fecha_naci_per").equals(" ")?"":rs.getString("fecha_naci_per"));
				// datos del beneficiario
				dto.setCodigoBeneficiario(rs.getInt("codigo_ben")+"");
				dto.setCodigoPaciente(rs.getInt("cod_pac_ben")+"");
				dto.setParentezco(rs.getString("desp_parentezco"));
				dto.setOcupacion(rs.getString("desp_ocupacion"));
				dto.setEstudio(rs.getString("desp_estudio"));
				dto.setTipoOcupacion(rs.getString("tipo_ocupa").trim());
				
				array.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
			Utilidades.imprimirMapa(parametros);
		}
		return array;
	}

	
	/**
	 * Metodo para consultar si se ha asigando una plantilla a un paciente de Odonotologia segun el tipo de funcionalidad
	 * @param codigoPaciente
	 * @param tipoPlantilla
	 * @return
	 */
	public static int existenciaPlantillaPaciente(int codigoPaciente, String tipoFuncionalidad) {
		
		
		String consulta = strConsutlarExistePlantillaPaciente;
		int  resp = 0;
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try{
			logger.info("Consulta Existe Plantilla Paciente Odontologia : "+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPaciente);
			ps.setString(2, tipoFuncionalidad);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				resp = rs.getInt("codpk");
			}			
			rs.close();
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error en Consulta plantilla paciente: "+consulta);
		
		}
		UtilidadBD.closeConnection(con);
				
		return resp;
	}
}
