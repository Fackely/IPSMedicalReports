package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;

public class SqlBaseRegionesCoberturaDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRegionesCoberturaDao.class);
	/*
	 * 
	 * 
	 */
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	
	
	public static ArrayList<DtoRegionesCobertura> cargar(DtoRegionesCobertura dtoWhere) 
	{
		
		ArrayList<DtoRegionesCobertura> arrayDto = new ArrayList<DtoRegionesCobertura>();
		String consultaStr = 	"SELECT " +
										"rc.codigo, " +
										"rc.institucion, " +
										"rc.descripcion, " +
										"rc.hora_modifica, " +
										"rc.fecha_modifica, " +
										"rc.usuario_modifica " +
									"from " +
										"administracion.regiones_cobertura rc " +
									"where " +
										"1=1 ";
				
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and rc.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getDescripcion())?" and rc.descripcion= '"+dtoWhere.getDescripcion()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion() >ConstantesBD.codigoNuncaValido)?" and rc.institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= " order by rc.descripcion "; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Categoria Atencion / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoRegionesCobertura dto = new DtoRegionesCobertura();
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
