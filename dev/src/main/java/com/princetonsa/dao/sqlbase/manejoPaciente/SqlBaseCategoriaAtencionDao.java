package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;




public class SqlBaseCategoriaAtencionDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCategoriaAtencionDao.class);
	/*
	 * 
	 * 
	 */
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoCategoriaAtencion> cargar(DtoCategoriaAtencion dtoWhere) 
	{
		
		ArrayList<DtoCategoriaAtencion> arrayDto = new ArrayList<DtoCategoriaAtencion>();
		String consultaStr = 	"SELECT " +
										"ca.codigo, " +
										"ca.institucion, " +
										"ca.descripcion, " +
										"ca.hora_modifica, " +
										"ca.fecha_modifica, " +
										"ca.usuario_modifica " +
									"from " +
										"administracion.categorias_atencion ca " +
									"where " +
										"1=1 ";
				
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and ca.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getDescripcion())?" and ca.descripcion= '"+dtoWhere.getDescripcion()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion() >ConstantesBD.codigoNuncaValido)?" and ca.institucion= "+dtoWhere.getInstitucion():"";
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Categoria Atencion / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoCategoriaAtencion dto = new DtoCategoriaAtencion();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());				
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga==> " + e);
		}
		return arrayDto;
		
	}
	
	
}
