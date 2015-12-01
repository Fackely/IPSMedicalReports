package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.odontologia.ProcesosAutomaticosOdontologiaDao;
import com.princetonsa.dao.postgresql.odontologia.PostgresqlProcesosAutomaticosOdontologiaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseProcesosAutomaticosOdontologia;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoLogProcAutoCitas;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.odontologia.DtoLogProcAutoFact;
import com.princetonsa.dto.odontologia.DtoLogProcAutoServCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;

public class OracleProcesosAutomaticosOdontologiaDao implements ProcesosAutomaticosOdontologiaDao 
{
	private static Logger logger = Logger.getLogger(PostgresqlProcesosAutomaticosOdontologiaDao.class);
	
	@Override
	public double guardarLogAutoServCita(DtoLogProcAutoServCita dto,
			Connection con) {
		
		return SqlBaseProcesosAutomaticosOdontologia.guardarLogAutoServCita(dto, con);
	}

	
	@Override
	public double guardarLogProcCitas(DtoLogProcAutoCitas dto, Connection con) {
		
		return SqlBaseProcesosAutomaticosOdontologia.guardarLogProcCitas(dto, con);
	}
	
	
	@Override
	public  ArrayList<DtoLogProcAutoCitas> cargarCitasDisponibles(DtoLogProcAutoCitas dto, String tiempo, ArrayList<String> estados, boolean controlaAbonosPorIngreso)
	{
		ArrayList<DtoLogProcAutoCitas> listaCitasDisponibles = new ArrayList<DtoLogProcAutoCitas>();
		
		/*
		String consultaStr=" SELECT co.codigo_pk, co.estado, ao.fecha||' '|| co.hora_fin  as fecha "+
								"FROM odontologia.citas_odontologicas co INNER JOIN "+
										"odontologia.agenda_odontologica ao ON(ao.codigo_pk=co.agenda) INNER JOIN "+
										"centro_atencion c ON (ao.centro_atencion=c.consecutivo) where co.estado"+
										"in ('RESE', 'ASIG') and ( to_timestamp ( ao.fecha||' '|| co.hora_fin,"+
										" 'YYYY MM DD HH24:MI') + interval '"+tiempo+"'  minute) <= current_timestamp and"+
										"	c.cod_institucion="+dto.getInstitucion();
		
		*/
		
		String consultaStr=
			"SELECT " +
				"co.codigo_pk AS codigo_pk, " +
				"co.estado AS estado, " +
				"ao.fecha||' '|| co.hora_fin AS fecha, " +
				"(SELECT sum(valor_tarifa) FROM servicios_cita_odontologica serv WHERE serv.cita_odontologica=co.codigo_pk) AS valor_tarifa, ";
				if(controlaAbonosPorIngreso)
				{
					consultaStr+=
					"( "+
						"SELECT id FROM ingresos ing "+ 
						"WHERE ing.codigo_paciente=co.codigo_paciente "+ 
						"AND "+
						"( "+
							"(to_char(ao.fecha, 'yyyy-mm-dd')||co.hora_inicio) >= (to_char(ing.fecha_ingreso, 'yyyy-mm-dd')||ing.hora_ingreso) "+
							"AND "+
							"( "+
								"(to_char(ao.fecha, 'yyyy-mm-dd')||co.hora_inicio) >= (to_char(ing.fecha_ingreso, 'yyyy-mm-dd')||ing.hora_ingreso) "+
								"OR "+
								"ing.fecha_egreso IS NULL "+ 
							") "+
						") "+
					") AS id_ingreso, ";
				}
				else
				{
					consultaStr+="null AS id_ingreso, ";
				}
				consultaStr+=
				"co.codigo_paciente AS codigo_paciente "+
			"FROM " +
				"odontologia.citas_odontologicas co " +
			"INNER JOIN "+
				"odontologia.agenda_odontologica ao " +
					"ON(ao.codigo_pk=co.agenda) " +
			"INNER JOIN "+
				"centro_atencion c " +
					"ON (ao.centro_atencion=c.consecutivo) " +
			"WHERE " +
				"co.estado "+
					"IN ("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") " +
				"AND" +
				"(" +
					"to_timestamp ( ao.fecha||' '|| co.hora_fin, "+
					" 'YYYY MM DD HH24:MI') + interval '"+tiempo+"'  minute) "+				") <= current_timestamp " +
				"AND " +
					"c.cod_institucion="+dto.getInstitucion();
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoLogProcAutoCitas dtoLogProcAutoCitas = new DtoLogProcAutoCitas();
	
				dtoLogProcAutoCitas.setEstadoInicialCita(rs.getString("estado"));
				dtoLogProcAutoCitas.setCitaOdontologica(rs.getBigDecimal("codigo_pk"));
				dtoLogProcAutoCitas.setFechaEjecucion(rs.getString("fecha"));
				dtoLogProcAutoCitas.setValorTarifa(rs.getDouble("valor_tarifa"));
				BigDecimal ingreso=rs.getBigDecimal("id_ingreso");
				if(ingreso!=null)
				{
					dtoLogProcAutoCitas.setIngreso(ingreso.intValue());
				}
				dtoLogProcAutoCitas.setCodigoPaciente(rs.getInt("codigo_paciente"));
				listaCitasDisponibles.add(dtoLogProcAutoCitas);
			 }
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
				logger.error("error en carga==> "+e);
				
		}
		return listaCitasDisponibles;
	}

	@Override
	public boolean modificarCita(ArrayList<Integer> codigosPk, String estado,
			Connection con) {
		
		return SqlBaseProcesosAutomaticosOdontologia.modificarCita(codigosPk, estado, con);
	}
	
	@Override
	public ArrayList<InfoDatosDouble> cargarProcAutoServCita(
			DtoServicioCitaOdontologica dtoWhere) {
		
		return SqlBaseProcesosAutomaticosOdontologia.cargarProcAutoServCita(dtoWhere);
	}

	@Override
	public double guardarLogProcAutoFact(DtoLogProcAutoFact dto, Connection con) {
		
		return SqlBaseProcesosAutomaticosOdontologia.guardarLogProcAutoFact(dto, con);
	}

	@Override
	public double guardarProcAutoEstados(DtoLogProcAutoEstados dto,
			Connection con) {
		
		return SqlBaseProcesosAutomaticosOdontologia.guardarProcAutoEstados(dto, con);
	}

	@Override
	public boolean cerrarIngresosOdontologicos(Connection con, int institucion) {
		return SqlBaseProcesosAutomaticosOdontologia.cerrarIngresosOdontologicos(con, institucion);
	}
}