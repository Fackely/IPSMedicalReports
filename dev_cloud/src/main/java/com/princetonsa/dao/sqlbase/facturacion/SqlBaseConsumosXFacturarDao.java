package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 * 
 */
public class SqlBaseConsumosXFacturarDao 
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsumosXFacturarDao.class);
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturar(
			Connection con, int institucion) {
		ArrayList<DtoInterfazConsumosXFacturar> arrayDtos = new ArrayList<DtoInterfazConsumosXFacturar>();
		String consulta = "SELECT "
				+ "sum(coalesce(dc.valor_total_cargado,0)) as valortotal, "
				+ "p.numero_identificacion as numeroidentificacion, "
				+ "coalesce(conv.interfaz, '') as codigointerfaz, "
				+ "conv.nombre as descripcionconvenio, "
				+ "sc.ingreso as numeroingreso, "
				+ "c.via_ingreso as codigoviaingreso, "
				+ "coalesce(c.tipo_paciente,'') as tipopaciente, "
				+ "coalesce(i.fecha_ingreso||'', '') as fechaingreso, "
				+ "coalesce(i.fecha_egreso||'', '') as fechaegreso, "
				+ "p.primer_nombre as primernombre, "
				+ "p.primer_apellido as primerapellido, "
				+ "coalesce(getcodigocamaxcuenta(c.id, i.id), -1) as codigocama, "
				+ "coalesce(i.usuario_modifica, '') as usuarioingreso, "
				+ "i.estado as estadoingreso, "
				+ "(current_date-1) as fechaprocesados "
				+ "FROM "
				+ "det_cargos dc "
				+ "INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=dc.sub_cuenta) "
				+ "INNER JOIN ingresos i ON(i.id=sc.ingreso) "
				+ "INNER JOIN solicitudes s ON(s.numero_solicitud=dc.solicitud) "
				+ "INNER JOIN cuentas c ON(c.id=s.cuenta) "
				+ "INNER JOIN personas p ON (p.codigo=sc.codigo_paciente) "
				+ "INNER JOIN convenios conv ON (conv.codigo=sc.convenio) "
				+ "WHERE " + "dc.fecha_modifica<CURRENT_DATE " +
				// "and dc.fecha_modifica>(CURRENT_DATE-60) " +
				"AND dc.facturado='"
				+ ConstantesBD.acronimoNo
				+ "' "
				+ "and dc.eliminado='"
				+ ConstantesBD.acronimoNo
				+ "' "
				+ "and dc.estado="
				+ ConstantesBD.codigoEstadoFCargada
				+ " "
				+ "and dc.paquetizado='"
				+ ConstantesBD.acronimoNo
				+ "' "
				+ "AND dc.paquetizado='"
				+ ConstantesBD.acronimoNo
				+ "' "
				+
				// "and dc.servicio_cx is null " +
				"AND s.estado_historia_clinica<>"
				+ ConstantesBD.codigoEstadoHCAnulada
				+ " "
				+ "AND i.institucion=? "
				+ "group by "
				+ "numeroidentificacion, "
				+ "codigointerfaz, "
				+ "descripcionconvenio, "
				+ "numeroingreso, "
				+ "codigoviaingreso, "
				+ "tipopaciente, "
				+ "fechaingreso, "
				+ "fechaegreso, "
				+ "primernombre, "
				+ "primerapellido, "
				+ "codigocama, "
				+ "usuarioingreso, "
				+ "estadoingreso, "
				+ "fechaprocesados ";

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, institucion);
			
			logger.info("\n\n*******************************************************************************************");
			logger.info("llega a obtenerConsumosXFacturar---------->"+consulta);
			logger.info("*******************************************************************************************\n\n");
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			String fechaRegistro= UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			String horaRegistro= UtilidadFecha.getHoraActual();
			
			
			while(rs.next())
			{	
				DtoInterfazConsumosXFacturar dto= new DtoInterfazConsumosXFacturar();
				dto.setCodigoInterfazConvenio(rs.getString("codigointerfaz"));
				dto.setCodigoViaIngreso(rs.getInt("codigoviaingreso"));
				dto.setTipoPaciente(rs.getString("tipopaciente"));
				
				if(rs.getString("descripcionconvenio").length()>30)
					dto.setDescripcionConvenio(rs.getString("descripcionconvenio").substring(0,29));
				else
					dto.setDescripcionConvenio(rs.getString("descripcionconvenio"));
				dto.setEstadoIngreso(rs.getString("estadoingreso"));
				dto.setEstadoRegistro("2"); //procesado axioma
				dto.setFechaEgresoBD(rs.getString("fechaegreso"));
				dto.setFechaIngresoBD(rs.getString("fechaingreso"));
				
				//@todo hacer no indican que fecha es
				dto.setFechaProcesadosBD("");
				
				dto.setFechaRegistroBD(fechaRegistro);
				dto.setHoraRegistro(horaRegistro);
				dto.setLoginUsuarioAdmision(rs.getString("usuarioingreso"));
				if(rs.getInt("codigocama")<=0)
					dto.setNumeroCama("");
				else
					dto.setNumeroCama(rs.getInt("codigocama")+"");
				dto.setNumeroIdentificacion(rs.getString("numeroidentificacion"));
				dto.setNumeroIngreso(rs.getInt("numeroingreso"));
				dto.setPrimerApellidoPaciente(rs.getString("primerapellido"));
				dto.setPrimerNombrePaciente(rs.getString("primernombre"));
				dto.setValorXFacturar(rs.getDouble("valortotal"));
				dto.setInstitucion(institucion);
				dto.setFechaProcesadosBD(rs.getString("fechaprocesados"));
				arrayDtos.add(dto);
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en obtenerConsumosXFacturar ");
			e.printStackTrace();
		}
		return arrayDtos;
		
	}	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////MODIFICACION POR ANEXO  779

public static ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturarReproceso(Connection con, int institucion)
{
	//la institucion es por si luego dicen que hay que filtrar por institucion
	
ArrayList<DtoInterfazConsumosXFacturar> arrayDtos = new ArrayList<DtoInterfazConsumosXFacturar>();
String consulta = "SELECT vprest01,ni1con,plaest,dsccon,ingest,viaing,feiing,feeing,"
		+ " nmipac,apipac,numhab,usring,esting,estreg,fecreg,horreg,fecpro,consecutivo   from  ax_porfac";

try {
	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	//ps.setInt(1, institucion);

	logger.info("\n\n*******************************************************************************************");
	logger.info("llega a obtenerConsumosXFacturar---------->"+ consulta);
	logger.info("*******************************************************************************************\n\n");

	ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());

	while (rs.next())
	{
		DtoInterfazConsumosXFacturar dto = new DtoInterfazConsumosXFacturar();

		dto.setValorXFacturar(rs.getDouble("vprest01"));
		dto.setNumeroIdentificacion(rs.getString("ni1con"));
		dto.setCodigoInterfazConvenio(rs.getString("plaest"));
		if (rs.getString("dsccon").length() > 30)
			dto.setDescripcionConvenio(rs.getString("dsccon").substring(0, 29));
		else
			dto.setDescripcionConvenio(rs.getString("dsccon"));
		dto.setNumeroIngreso(rs.getInt("ingest"));
		dto.setCodInterfazViaIngresoTipoPac(rs.getString("viaing"));
		dto.setFechaIngresoBD(rs.getString("feiing"));
		dto.setFechaEgresoBD(rs.getString("feeing"));
		dto.setPrimerNombrePaciente(rs.getString("nmipac"));
		dto.setPrimerApellidoPaciente(rs.getString("apipac"));
		
		if (!UtilidadCadena.noEsVacio(rs.getString("numhab")))
			dto.setNumeroCama("");
		else
			dto.setNumeroCama(rs.getString("numhab"));
		dto.setLoginUsuarioAdmision(rs.getString("usring"));
		dto.setEstadoIngresoShaio(rs.getString("esting"));
		dto.setEstadoRegistro(rs.getString("estreg"));
		dto.setFechaRegistroBD(rs.getString("fecreg"));
		dto.setHoraRegistro(rs.getString("horreg"));
		dto.setFechaProcesadosBD(rs.getString("fecpro"));
		dto.setConsecutivo(rs.getString("consecutivo"));
		
		arrayDtos.add(dto);
	}
} catch (SQLException e) {
	logger.warn(" Error en obtenerConsumosXFacturar "+e);
	e.printStackTrace();
}
return arrayDtos;
}





//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
