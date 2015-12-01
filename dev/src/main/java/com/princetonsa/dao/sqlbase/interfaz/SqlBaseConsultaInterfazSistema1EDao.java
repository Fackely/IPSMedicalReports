package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.util.HashMap;
import org.apache.log4j.Logger;
//import org.hibernate.hql.ast.tree.DotNode;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseConsultaInterfazSistema1EDao
{
	private static Logger logger=Logger.getLogger(SqlBaseParamInterfazSistema1EDao.class);
	
	
	
	public static ArrayList<DtoLogInterfaz1E> consultarLog(Connection con, HashMap filtros)
	{
		ArrayList<DtoLogInterfaz1E> arrayLog = new ArrayList<DtoLogInterfaz1E>();
		String consultarLog =	"SELECT " +
									"li.consecutivo," +
									"to_char(li.fecha_generacion,'dd/mm/yyyy') AS fechageneracion," +
									"li.hora_generacion AS horageneracion," +
									"li.tipo_proceso AS tipoproceso," +
									"to_char(li.fecha_proceso,'dd/mm/yyyy') AS fechaproceso," +
									"li.tipo_movimiento AS tipomov, " +
									"td.nombre AS nomtipodocumento," +
									"li.usuario_procesa AS usuario," +
									"li.tipo_archivo AS tipoarchivo," +
									"li.nombre_archivo AS nomarchivo," +
									"li.path " +
								"FROM " +
									"log_interfaz_1e li " +
								"LEFT OUTER JOIN " +
									"log_interfaz_tipos_doc_1e litd ON (litd.log_interfaz=li.consecutivo) " +
								"LEFT OUTER JOIN " +
									"tipos_documento_1e td ON (td.codigo=litd.tipo_documento) " +
								"WHERE ";
		
		if (!filtros.get("fechaProceso").toString().isEmpty()&&!filtros.get("fechaFinProceso").toString().isEmpty())
			consultarLog+=" (li.fecha_proceso BETWEEN ? AND ?) AND ";
		
		if(!filtros.get("tipoProceso").toString().isEmpty())
			consultarLog+="  li.tipo_proceso=? AND ";
		
		if(!filtros.get("tipoMovimiento").toString().isEmpty())
			consultarLog+=" li.tipo_movimiento=? AND ";
		
		if(!filtros.get("tipoMovimiento").toString().isEmpty()&&!filtros.get("tipoDocumento").toString().isEmpty())
			consultarLog+=" td.codigo=? AND ";
		
		consultarLog+=" 1=1 ORDER BY li.fecha_generacion DESC, li.hora_generacion DESC  ";
		
		logger.info("consulta armada------>"+consultarLog);
		int i=1;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarLog, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			if (!filtros.get("fechaProceso").toString().isEmpty()&&!filtros.get("fechaFinProceso").toString().isEmpty())
			{
				ps.setString(i,UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaProceso").toString()));
				i++;
				ps.setString(i,UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinProceso").toString()));
				i++;
				
			}
			if(!filtros.get("tipoProceso").toString().isEmpty())
			{
				ps.setString(i, filtros.get("tipoProceso").toString());
				i++;
			}
			if(!filtros.get("tipoMovimiento").toString().isEmpty())
			{
				ps.setString(i, filtros.get("tipoMovimiento").toString());
				i++;
			}
			
			if(!filtros.get("tipoMovimiento").toString().isEmpty()&&!filtros.get("tipoDocumento").toString().isEmpty())
			{
				ps.setInt(i,Utilidades.convertirAEntero(filtros.get("tipoDocumento").toString()));
				i++;
			}

			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				
				DtoLogInterfaz1E dto= new DtoLogInterfaz1E();
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setFechaGeneracion(rs.getString("fechageneracion"));
				dto.setHoraGeneracion(rs.getString("horageneracion"));
				dto.setTipoProceso(rs.getString("tipoproceso"));
				dto.setFechaProceso(rs.getString("fechaproceso"));
				dto.setTipoMovimiento(rs.getString("tipomov"));
				dto.setTipoDocumento(rs.getString("nomtipodocumento"));
				dto.setUsuarioProcesa(rs.getString("usuario"));
				dto.setTipoArchivo(rs.getString("tipoarchivo"));
				dto.setNombreArchivo(rs.getString("nomarchivo"));
				dto.setPath(rs.getString("path"));
				arrayLog.add(dto);
			}
			
		}
		
		
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultarLog / "+e);
			return arrayLog;
		}
		logger.info("REALICE LA CONSULTA DEL LOG!!!!");
		return arrayLog;
	}
	
}