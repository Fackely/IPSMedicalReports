/*
 * Creado en 13/12/2006
 *
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * @author Wilson Rios
 *
 * Princeton S.A.
 */
public class SqlBaseUtilidadImpresionDao
{	
	/**
	 * 
	 */
	private static  Logger logger=Logger.getLogger(SqlBaseUtilidadImpresionDao.class);

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap obtenerEncabezadoPaciente(Connection con, String idCuenta)
	{
		String cadena=	"SELECT " +
							"getnombrepersona(p.codigo) AS nombresapellidos, " +
							"p.tipo_identificacion AS tipoid, " +
							"p.numero_identificacion AS numeroid, " +
							"getnombreciudad(p.codigo_pais_id,p.codigo_depto_id, codigo_ciudad_id) as de, " +
							"to_char(p.fecha_nacimiento, 'DD/MM/YYYY') as fechanacimiento, " +
							"CASE WHEN (CURRENT_DATE - 365) > p.fecha_nacimiento AND (getedad(p.fecha_nacimiento) < 15 OR getedad(p.fecha_nacimiento) > 35) THEN 'true' ELSE 'false' END AS resaltar_edad, "+
							"getedad(p.fecha_nacimiento) ||' '|| CASE WHEN (CURRENT_DATE - 365) < p.fecha_nacimiento THEN 'meses' ELSE 'a&ntilde;os' END AS edad, " +
							"s.nombre AS nombresexo, " +
							"p.estado_civil AS codigoestadocivil, " +
							"e.nombre AS estadocivil, " +
							"o.nombre AS ocupacion, " +
							"p.direccion ||' '|| getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda, p.codigo_ciudad_vivienda) as residencia," +
							"'Tel: '|| p.telefono AS telefono, " +
							"getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) AS depto, " +
							"z.nombre AS zona, " +
							"conv.nombre AS convenio, " +
							"tr.nombre AS tiporegimen, " +
							"getnombretipoafiliado(dm.tipo_afiliado_codigo) AS tipoafiliado, " +
							"CASE WHEN pac.etnia IS NULL THEN '' ELSE et.nombre END AS nombreetnia, " +
							"CASE WHEN pac.lee_escribe IS NULL THEN '' ELSE (CASE WHEN pac.lee_escribe IS NULL THEN 'Si' ELSE 'No' END) END AS leeescribe, " +
							"CASE WHEN pac.estudio IS NULL THEN 0 ELSE pac.estudio END AS codigoestudio,  " +
							"CASE WHEN pac.estudio IS NULL THEN '' ELSE es.nombre END AS nombreestudio," +
							"coalesce(c.codigo_responsable_paciente,0) AS responsable_paciente  " +
						"FROM " +
							"cuentas c " +
							"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
							"INNER JOIN pacientes pac ON (p.codigo=pac.codigo_paciente) " +
							"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+
							"INNER JOIN sexo s ON(s.codigo=p.sexo) " +
							"INNER JOIN estados_civiles e on (e.acronimo=p.estado_civil) " +
							"INNER JOIN ocupaciones o ON (o.codigo=pac.ocupacion) " +
							"INNER JOIN contratos cont ON (cont.codigo=sc.contrato) " +
							"INNER JOIN convenios conv ON (conv.codigo=sc.convenio) " +
							"INNER JOIN tipos_regimen tr ON (tr.acronimo=conv.tipo_regimen) " +
							"LEFT JOIN montos_cobro mc ON (mc.codigo=sc.monto_cobro) " +
							"LEFT JOIN detalle_monto dm ON (dm.monto_codigo=mc.codigo) "+
							"INNER JOIN zonas_domicilio  z ON (z.acronimo=pac.zona_domicilio) " +
							"LEFT OUTER JOIN etnia et ON (et.codigo=pac.etnia) " +
							"LEFT OUTER JOIN estudio es ON (es.codigo= pac.estudio) " +
						"WHERE " +
							"c.id=? ";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, idCuenta);
			logger.info("\n\n  CargarEncabezado--->"+cadena+" codPac-->"+idCuenta);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, false);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		mapa.put("infoAcompananteMap", obtenerInfoAcompanante(con, mapa.get("responsable_paciente")==null?"0":mapa.get("responsable_paciente").toString()));
		
		return mapa;
	}	
	
	/**
	 * info del acompanante
	 * @param con
	 * @param codigoResponsable
	 * @return
	 */
	private static HashMap obtenerInfoAcompanante(Connection con, String codigoResponsable)
	{
		String cadena=	"SELECT " +
							"primer_apellido || coalesce(' '||segundo_apellido,'') || ' ' || primer_nombre || coalesce(' '||segundo_nombre,'') AS nombreapellidoacompanante, " +
							"telefono AS telefonoacomp, " +
							"relacion_paciente AS parentesco " +
						"FROM responsables_pacientes " +
						"WHERE " +
							"codigo=? "+ValoresPorDefecto.getValorLimit1()+" 1";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoResponsable);
			mapa =UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, false);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

}
