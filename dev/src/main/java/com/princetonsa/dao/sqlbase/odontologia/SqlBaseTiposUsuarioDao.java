package com.princetonsa.dao.sqlbase.odontologia;

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
import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;

public class SqlBaseTiposUsuarioDao {

	private static Logger logger = Logger.getLogger(SqlBaseTiposUsuarioDao.class);
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoTiposDeUsuario> cargar(DtoTiposDeUsuario dtoWhere) 
	{
		ArrayList<DtoTiposDeUsuario> arrayDto = new ArrayList<DtoTiposDeUsuario>();
		String consultaStr = 	"SELECT " +
										"codigo, " +
										"descripcion, "+
										"rol ,"+ 
										"institucion, " +
										"fecha_modifica, " +
										"hora_modifica, " +
										"usuario_modifica " +
									"from " +
										" odontologia.tipos_usuarios  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+=  UtilidadTexto.isEmpty(dtoWhere.getRol())? " ":" and rol='"+dtoWhere.getRol()+"'";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+=" order by codigo asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Des odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoTiposDeUsuario dto = new DtoTiposDeUsuario();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setRol(rs.getString("rol"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Des Odon==> " + e);
		}
		return arrayDto;
	}
	
}
