package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.odontologia.DtoPacienteConvenioOdo;
import com.princetonsa.mundo.PersonaBasica;

public class SqlBasePacientesConvenioOdontologiaDao {

	private static Logger logger=Logger.getLogger(SqlBasePacientesConvenioOdontologiaDao.class);
	
	public static DtoPacienteConvenioOdo consultarConvenio(Connection con, DtoPaciente paciente, int codigoConvenio) {
		String sentencia="" +
				"SELECT " +
					"codigo AS codigo, " +
					"convenio AS convenio, " +
					"(SELECT codigo from contratos where contratos.convenio=odontologia.pacientes_conv_odo.convenio and contratos.numero_contrato=odontologia.pacientes_conv_odo.contrato) AS contrato, " +
					"tipo_identificacion AS tipo_identificacion, " +
					"numero_identificacion AS numero_identificacion, " +
					"primer_apellido AS primer_apellido, " +
					"segundo_apellido AS segundo_apellido, " +
					"primer_nombre AS primer_nombre, " +
					"segundo_nombre AS segundo_nombre, " +
					"fecha_nacimiento AS fecha_naciemiento, " +
					"sexo AS sexo, " +
					"fecha_ini_vigencia AS fecha_ini_vigencia, " +
					"fecha_fin_vigencia AS fecha_fin_vigencia, " +
					"usuario_modifica AS usuario_modifica, " +					
					"fecha_modifica AS fecha_modifica, " +
					"hora_modifica AS hora_modifica " +
					
				"FROM odontologia.pacientes_conv_odo " +
				"WHERE " +
						"tipo_identificacion=? " +
					" AND " +
						"numero_identificacion=? " +
					" AND " +
					" convenio=?";
		
		try {
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setString(1, paciente.getTipoId());
			psd.setString(2, paciente.getNumeroId());
			psd.setInt(3, codigoConvenio);
			logger.info("Consulta Nuevaaa------------>"+psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{				
				DtoPacienteConvenioOdo dto=new DtoPacienteConvenioOdo();
				llenarDtoAPartirResultSet(dto, rsd);
				rsd.close();
				psd.close();
				return dto;
			}
		} catch (SQLException e) {
			logger.error("Error consultando los pacientes de convenios odontológicos", e);
		}
		return null;
		
	}

	private static void llenarDtoAPartirResultSet(DtoPacienteConvenioOdo dto, ResultSetDecorator rsd) throws SQLException {
		dto.setCodigo(rsd.getInt("codigo"));
		dto.setConvenio(rsd.getInt("convenio"));
		dto.setContrato(rsd.getInt("contrato"));
		dto.setTipoIdentificacion(rsd.getString("tipo_identificacion"));
		dto.setNumeroIdentificacion(rsd.getString("numero_identificacion"));
		dto.setPrimerApellido(rsd.getString("primer_apellido"));
		dto.setSegundoApellido(rsd.getString("segundo_apellido"));
		dto.setPrimerNombre(rsd.getString("primer_nombre"));
		dto.setSegundoNombre(rsd.getString("segundo_nombre"));
		dto.setSexo(rsd.getInt("sexo"));
		dto.setFechaIniVigencia(rsd.getString("fecha_ini_vigencia"));
		dto.setFechaFinVigencia(rsd.getString("fecha_fin_vigencia"));
		dto.setUsuarioModifica(rsd.getString("usuario_modifica"));
		dto.setFechaModifica(rsd.getString("fecha_modifica"));
		dto.setHoraModifica(rsd.getString("hora_modifica"));
	}

	/**
	 */
	public static boolean pacienteTieneTarjetaCliente(Connection con, PersonaBasica paciente, int convenio) {
		try{
			String sentencia="SELECT " +
						"count(1) AS resulados "+ 
					"FROM "+ 
						
						"odontologia.tipos_tarj_cliente ttc "+
						
					"INNER JOIN "+
						"odontologia.beneficiario_tarjeta_cliente btc ON (btc.tipo_tarjeta_cliente=ttc.codigo_pk) " +
						
					"INNER JOIN "+
						"odontologia.beneficiario_tc_paciente btcpaciente ON (btc.codigo_pk=btcpaciente.codigo_beneficiario) " +
						
					"INNER JOIN convenios con ON (con.codigo=ttc.convenio) " +
				
					" WHERE " +
							//"btcpaciente.codigo_persona=? " +
							"btcpaciente.codigo_paciente=? " +
					
						"AND " +
							"btc.estado_tarjeta='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
						"AND " +
							"con.codigo=?";
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, paciente.getCodigoPersona());
			psd.setInt(2, convenio);
			boolean resultado=false;
			logger.info("paciente tiene tarjta "+psd+" ");
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				resultado=rsd.getInt("resulados")>0;
			}
			rsd.close();
			psd.close();
			return resultado;
		}
		catch (SQLException e) {
			logger.info("Error verificando si el paciente tiene una tarjeta cliente", e);
			return false;
		}
		
	}

}
