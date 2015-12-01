package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;

/**
 * Encargada del acceso a la base de datos para el manejo de las convenciones
 * odontológicas
 * @author Jorge Andrés Ortiz
 * @version 1.0
 */
public class SqlBaseConvencionesOdontologicasDao {

	/**
	 * Método que realiza la Búsqueda de las Convenciones Odontológicas Asociadas a una institución
	 * @param codInstitucion Código de la institución
	 * @return {@link ArrayList} con la lista de DtoConvencionesOdontologicas relacionados a la institución
	 */
	public static ArrayList<DtoConvencionesOdontologicas> consultarConvencionesOdontologicas(int codInstitucion) {
		ArrayList<DtoConvencionesOdontologicas> array=new ArrayList<DtoConvencionesOdontologicas>();
		String cadenaConsulta=
			"SELECT " +
				"conv.consecutivo AS consecutivo, " +
				"conv.codigo AS codigo, " +
				"conv.nombre AS nombre, " +
				"conv.tipo AS tipo, " +
				"conv.color AS color, " +
				"conv.trama AS codtrama, " +
				"elgraf1.nombre AS nomtrama, " +
				"elgraf1.archivo AS archivotrama," +
				"conv.imagen AS codimagen, " +
				"elgraf2.nombre AS nomimagen, " +
				"elgraf2.archivo AS archivoimagen," +
				"conv.activo AS activo, " +
				"conv.archivo_convencion AS patharchivo, " +
				"conv.institucion AS institucion, " +
				"conv.fecha_modificacion AS fechamodificacion, " +
				"conv.hora_modificacion AS horamodificacion, " +
				"conv.usuario_modificacion AS usuario, " +
				"conv.borde AS borde " +
			"FROM " +
				"odontologia.convenciones_odontologicas conv " +
			"LEFT OUTER JOIN " +
				"odontologia.elementos_graficos elgraf1 " +
					"ON (elgraf1.codigo = conv.trama ) " +
			"LEFT OUTER JOIN " +
				"odontologia.elementos_graficos elgraf2 " +
					"ON (elgraf2.codigo = conv.imagen) " +
			"WHERE " +
				"conv.institucion = ?  " +
			"ORDER BY " +
				"conv.nombre ";

		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
		
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoConvencionesOdontologicas dto =new DtoConvencionesOdontologicas();
				dto.setConsecutivo(rs.getInt("consecutivo"));
				dto.setCodigo(rs.getString("codigo"));				
			    dto.setNombre(rs.getString("nombre"));
			    dto.setTipo(rs.getString("tipo"));
			    dto.setColor(rs.getString("color"));
			    dto.setTrama(rs.getInt("codtrama"));
			    dto.setNombreTrama(rs.getString("nomtrama"));
			    dto.setArchivoTrama(rs.getString("archivotrama"));
			    dto.setImagen(rs.getInt("codimagen"));
			    dto.setNombreImagen(rs.getString("nomimagen"));
			    dto.setArchivoImagen(rs.getString("archivoimagen"));
			    dto.setActivo(rs.getString("activo"));
			    dto.setArchivoConvencion(rs.getString("patharchivo"));
			    dto.setInstitucion(rs.getInt("institucion"));
			    dto.setFechaModificacion(rs.getString("fechamodificacion"));
			    dto.setHoraModificacion(rs.getString("horamodificacion"));
			    dto.setUsuarioModificacion(rs.getString("usuario"));	    
			    dto.setBorde(rs.getString("borde"));
				array.add(dto);
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error en Consulta Convenciones Odontologicas Cadena>> "+cadenaConsulta+"  CodInstitucion>> "+codInstitucion, e);
		}
		return array;
	
	}
	
	/**
	 * Método que realiza la inserción de una nueva convención
	 * @param con Conexión con la BD
	 * @param nuevaConvencion Nueva convención a insertar
	 * @param loginUsuario Login del usuario
	 * @param codInstitucion Código de la institución
	 * @return Número de registros insertados en la BD
	 */
	public static int crearNuevaConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion, String loginUsuario,int codInstitucion) {
		String cadenaInsercion=
			"INSERT INTO odontologia.convenciones_odontologicas ( " +
						"consecutivo, " + //1
						"codigo, " +   //2
						"nombre, " + //3
						"archivo_convencion, " + //4
						"tipo, " + //5
						"color, " +  //6
						"trama, " + //7
						"imagen, " + //8
						"institucion, " +  //9
						"fecha_modificacion, " + //--
						"hora_modificacion, " +  //--
						"usuario_modificacion, " +//10
						"activo, " +  //11
						"borde ) " +//12
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?) ";

		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_convenciones_odont");	
		String pathArchivo="convencion_"+consecutivo+".jpg";
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, consecutivo);
			nuevaConvencion.setCodigo(consecutivo+"");
			ps.setString(2, nuevaConvencion.getCodigo());
			ps.setString(3, nuevaConvencion.getNombre());
			ps.setString(4, pathArchivo);
			ps.setString(5, nuevaConvencion.getTipo());
			ps.setString(6, nuevaConvencion.getColor());
			if(nuevaConvencion.getTrama()>0){
				ps.setInt(7,nuevaConvencion.getTrama());	 
			}else{
				ps.setNull(7,Types.INTEGER);
			}
			if(nuevaConvencion.getImagen()>0){
				ps.setInt(8, nuevaConvencion.getImagen());
			}else{
				ps.setNull(8,Types.INTEGER);
			}
			ps.setInt(9, codInstitucion);	         
			ps.setString(10,loginUsuario);
			ps.setString(11,nuevaConvencion.getActivo());
			ps.setString(12, nuevaConvencion.getBorde()); 
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return consecutivo;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error en Crear Convención Odontologica Cadena>> "+cadenaInsercion, e);
		}
	         
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método que realiza el proceso de modificación de la Convención 
	 * @param con Conexión con la BD
	 * @param nuevaConvencion Nueva convención a insertar
	 * @param consecutivoConvencion Consecutivo de la convención
	 * @return true en caso de modificar correctamente, false de lo contrario
	 */
	public static boolean modificarConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion,	int consecutivoConvencion, String loginUsuario) {
		 String cadenaModificar=
			"UPDATE odontologia.convenciones_odontologicas SET " +
						"codigo = ?, " + //1
						"nombre = ?, " + //2
						"tipo = ?, " + //3
						"color = ?, " + //4
						"trama = ?, " + //5
						"imagen = ?, " + //6
						"activo = ?," + //7
						"usuario_modificacion = ?," + //8
						"fecha_modificacion = CURRENT_DATE ," +
						"hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						"borde = ? " + //9
			"WHERE " +
						"consecutivo = ? "; //10

		 boolean resp=false;
		
		 try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,nuevaConvencion.getCodigo());
				ps.setString(2,nuevaConvencion.getNombre());
				ps.setString(3,nuevaConvencion.getTipo());
				ps.setString(4,nuevaConvencion.getColor());
				ps.setString(9,nuevaConvencion.getBorde());
				if(nuevaConvencion.getTrama()>0){
		        	 ps.setInt(5,nuevaConvencion.getTrama());	 
		         }else{
		 			 ps.setNull(5,Types.INTEGER);
		         }
		         if(nuevaConvencion.getImagen()>0){
		            ps.setInt(6, nuevaConvencion.getImagen());
		         }else{
		        	 ps.setNull(6,Types.INTEGER);
		         }
				ps.setString(7,nuevaConvencion.getActivo());
				ps.setString(8,loginUsuario);
				ps.setInt(10,consecutivoConvencion);				
				
				if(ps.executeUpdate()>0)
				{
					resp=true;
				}
				ps.close();
			}
			catch (SQLException e) 
			{
				Log4JManager.error("Error en Modificar Convencion Odontologica Cadena>> "+cadenaModificar+"  Consecutivo>> "+consecutivoConvencion, e);
			}
		
		 
		return resp;
	}
	
	/**
	 * Método utilizado para la eliminación de la convención
	 * @param codConvencion Código de la convención
	 * @return true en caso de eliminar correctamente, false de lo contrario
	 */
	public static boolean eliminarConvencion(int codConvencion)
	{
		String cadenaEliminar="DELETE FROM odontologia.convenciones_odontologicas WHERE consecutivo = ? ";
		boolean resp= false;

		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codConvencion);
			if(ps.executeUpdate()>0)
			{
				resp=true;
			}
			ps.close();
			UtilidadBD.closeConnection(con);
		}

		catch (SQLException e) 
		{
			Log4JManager.error("No se pudo Eliminar la Convencion Odontologica Cadena>> "+cadenaEliminar+"  Consecutivo>> "+codConvencion  );
		}
		return resp;
	}
	
}