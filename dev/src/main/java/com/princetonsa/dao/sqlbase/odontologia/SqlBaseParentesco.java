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
import com.princetonsa.dto.odontologia.DtoParentesco;

public class SqlBaseParentesco {
	

	
private static Logger logger = Logger.getLogger(SqlBaseParentesco.class);
	
	
	
/**
 * 
 * @param dto
 * @return
 */
	public static ArrayList<DtoParentesco> cargar(DtoParentesco dto){
		
		ArrayList<DtoParentesco> arrayDto= new ArrayList<DtoParentesco>();
		
		String consultaStr="select " +
							" codigo_pk, "+ //1
							" tipo_venta ,"+ //2
						
							" from odontologia.parentezco where 1=1 ";
		
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getDescripcion())?"":"descripcion='"+dto.getDescripcion()+"'";
		consultaStr+=(dto.getCodigoPk()>0)?"AND codigo_pk="+dto.getCodigoPk():"";	
		logger.info("\n\n\n\n\n SQL cargarEmisiones Bonos Desc/ "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoParentesco newdto = new DtoParentesco();
				newdto.setCodigoPk(rs.getInt("codigo_pk"));
				newdto.setDescripcion(rs.getString("descripcion"));
				
				arrayDto.add(newdto);
			 }
		 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
			
		}
		
		return arrayDto;
	}
	    
          
	
	
	

}
