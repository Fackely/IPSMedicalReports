package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.odontologia.ProcesosAutomaticosOdontologiaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseProcesosAutomaticosOdontologia;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoLogProcAutoCitas;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.odontologia.DtoLogProcAutoFact;
import com.princetonsa.dto.odontologia.DtoLogProcAutoServCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;


/**
 * 
 * @author axioma
 *
 */
public class PostgresqlProcesosAutomaticosOdontologiaDao implements ProcesosAutomaticosOdontologiaDao 
{
	private static Logger logger = Logger.getLogger(PostgresqlProcesosAutomaticosOdontologiaDao.class);
	
	@Override
	public double guardarLogAutoServCita(DtoLogProcAutoServCita dto, Connection con) {
		return SqlBaseProcesosAutomaticosOdontologia.guardarLogAutoServCita(dto, con);
	}
	
	@Override
	public double guardarLogProcCitas(DtoLogProcAutoCitas dto, Connection con) {
		return SqlBaseProcesosAutomaticosOdontologia.guardarLogProcCitas(dto, con);
	}

	
	/**
	 * 
	 */
	public ArrayList<DtoLogProcAutoCitas> cargarCitasDisponibles(DtoLogProcAutoCitas dto, String tiempo, ArrayList<String> estados, boolean controlaAbonosPorIngreso){
		ArrayList<DtoLogProcAutoCitas> listaCitasDisponibles = new ArrayList<DtoLogProcAutoCitas>();
		
		String consultaStr=
							"SELECT " +
								"co.codigo_pk AS codigo_pk, " +
								"co.estado AS estado, " +
								"ao.fecha||' '|| co.hora_fin AS fecha, " +
								"ao.centro_atencion AS centro_atencion, " +
								"(SELECT sum(valor_tarifa) FROM servicios_cita_odontologica serv WHERE serv.cita_odontologica=co.codigo_pk) AS valor_tarifa, "+
									"(" +
										"SELECT " +
											"c.id_ingreso AS id_ingreso " +
										"FROM " +
											"odontologia.servicios_cita_odontologica sco " +
										"INNER JOIN " +
											"ordenes.solicitudes s " +
												"ON (sco.numero_solicitud = s.numero_solicitud) " +
										"INNER JOIN " +
											"manejopaciente.cuentas c " +
												"ON (c.id=s.cuenta)"+		
										"WHERE sco.cita_odontologica = co.codigo_pk LIMIT 1 "+
									") AS id_ingreso, "+
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
									"CAST( ao.fecha||' '|| co.hora_fin AS  timestamp) + " +
									"CAST( "+tiempo+" ||' minutes' AS INTERVAL)" +
								") <= current_timestamp " +
								"AND " +
									"c.cod_institucion="+dto.getInstitucion();
		
		logger.info("CARGAR PROCESOS AUTOMATICOS DE CITAS");
		logger.info(consultaStr);
		
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
				dtoLogProcAutoCitas.setCodigoCentroAtencion(rs.getInt("centro_atencion"));
				listaCitasDisponibles.add(dtoLogProcAutoCitas);
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}	
		
		
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga de citas para procedo automático",e);
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
