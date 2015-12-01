package com.princetonsa.dao.postgresql.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.consultaExterna.ReporteEstadisticoConsultaExDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseReporteEstadisticoConsultaExDao;

public class PostgresqlReporteEstadisticoConsultaExDao implements ReporteEstadisticoConsultaExDao
{
	/***
	 * particulares
	 * */
	private static String particulares = 
								"(select "+ 
								"'0' as grupo, "+
								"'1' as orden, "+
								"sc.codigo_cita AS codigocita, "+
								"'PARTICULARES' AS indicador, "+
								"getnombremes(to_char(sc.fecha_cita,'MM')) || ' ' || to_char(sc.fecha_cita,'YYYY')  as fechacita, "+
								"to_char(sc.fecha_cita,'MM') as numeromes "+
								"from  "+
								"servicios_cita sc "+							
								"inner join cita c ON(c.codigo = sc.codigo_cita) "+
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+
								"inner join convenios con ON (con.codigo = (select scu.convenio  from solicitudes_subcuenta ss INNER JOIN sub_cuentas scu ON (ss.sub_cuenta=scu.sub_cuenta) WHERE ss.solicitud = sc.numero_solicitud and ss.eliminado = 'N' order by scu.nro_prioridad asc "+ValoresPorDefecto.getValorLimit1()+" 1) AND con.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"') " +
								"inner join valoraciones_consulta vc ON (vc.numero_solicitud = sc.numero_solicitud) "+
								"where 1=2 "+
								"UNION ALL   "+
								"select  "+
								"'0' as grupo, "+
								"'1' as orden, "+
								"sc.codigo_cita AS codigocita, "+
								"'OTRAS ENTIDADES' AS indicador, "+
								"getnombremes(to_char(sc.fecha_cita,'MM')) || ' ' || to_char(sc.fecha_cita,'YYYY')  as fechacita, "+
								"to_char(sc.fecha_cita,'MM') as numeromes "+
								"from  "+
								"servicios_cita sc "+							
								"inner join cita c ON(c.codigo = sc.codigo_cita) "+
								"inner join  unidades_consulta uc ON(uc.especialidad = sc.especialidad) "+ //Cambio Anexo 809
								"inner join especialidades e ON(e.codigo = sc.especialidad) "+
								"inner join centros_costo cc ON (cc.codigo = sc.centro_costo) "+
								"inner join convenios con ON (con.codigo = (select scu.convenio  from solicitudes_subcuenta ss INNER JOIN sub_cuentas scu ON (ss.sub_cuenta=scu.sub_cuenta) WHERE ss.solicitud = sc.numero_solicitud and ss.eliminado = 'N' order by scu.nro_prioridad asc "+ValoresPorDefecto.getValorLimit1()+" 1) AND con.tipo_regimen != '"+ConstantesBD.codigoTipoRegimenParticular+"') " +
								"inner join valoraciones_consulta vc ON (vc.numero_solicitud = sc.numero_solicitud) "+
								"where  "+
								" 1=3 "+
								") "+
								"order by numeromes ASC ";

	/**
	 * Arma la sentencia del Where a partir de los filtros de la busqueda
	 * @param HashMap parametros
	 * */
	public HashMap getCondicionesBuquedaReporte(HashMap parametros)
	{
		return SqlBaseReporteEstadisticoConsultaExDao.getCondicionesBuquedaReporte(parametros, particulares);
	}
	
	/**
	 * @param Connection con
	 * @param String where  
	 * */
	public HashMap getCargarDatosBasicosCancelacionCitas(Connection con,HashMap parametros)
	{
		return SqlBaseReporteEstadisticoConsultaExDao.getCargarDatosBasicosCancelacionCitas(con, parametros); 
	}
	

}