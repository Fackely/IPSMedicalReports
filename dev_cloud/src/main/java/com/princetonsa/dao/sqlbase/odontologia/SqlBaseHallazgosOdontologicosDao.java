package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.enu.general.CarpetasArchivos;

public class SqlBaseHallazgosOdontologicosDao {

	/**
	 * Cadena Sql para realizar la consulta de los hallazgos Odonotologicos
	 */
	private static String consultaHallazgosOdontologicos = "SELECT " +
				   "hallodo.consecutivo AS consecutivo, " +
				   "hallodo.codigo AS codigohallazgo, " +
				   "hallodo.nombre AS nombrehallazgo, " +
				   "hallodo.acronimo AS acronimohallazgo, " +
				   "hallodo.diagnostico AS diagnosticohallazgo, " +
				   "diag.nombre AS descripciondiagnostico, " +
				   "hallodo.tipo_cie AS tipociehallazgo, " +
				   "hallodo.convencion AS convencionhallazgo, " +
				   "conv.archivo_convencion AS nomconvencionhallazgo, " +
				   "hallodo.aplica_a AS aplicaahallazgo, " +
				   "hallodo.activo AS activohallazgo, " +
				   "hallodo.a_tratar AS atratarhallazgo, " +
				   "conv.borde AS borde_convencion, " +
				   "th.codigo AS tipohallazgo, " +
				   "th.tipo AS nombretipohallazgo " +
				   //"to_number(hallodo.codigo,'999') AS codigodouble " + Se quita por daño en Ordenamiento Alfanumerico Mayo 6 - 2010				   
				   "FROM odontologia.hallazgos_odontologicos hallodo " +
				   "LEFT OUTER JOIN manejopaciente.diagnosticos diag ON (diag.acronimo = hallodo.diagnostico AND diag.tipo_cie = hallodo.tipo_cie  ) " +
				   "LEFT OUTER JOIN odontologia.convenciones_odontologicas conv ON (conv.consecutivo = hallodo.convencion ) " +
				   "LEFT OUTER JOIN odontologia.tipo_hallazgo_ceo_cop th ON (th.codigo = hallodo.tipo_hallazgo_ceo_cop ) " +
				   "WHERE hallodo.institucion = ? ";
	
	
	/**
	 * Cadena Sql para Insertar un nuevo hallazgo odontologico
	 */
	public static String insertarNuevoHallazgoOdontologico= "INSERT INTO odontologia.hallazgos_odontologicos ( " +
				  "consecutivo, " + //1
				  "codigo, " +	//2
				  "institucion, " + //3
				  "nombre, " +   //4
				  "acronimo, " + //5
				  "diagnostico, " + //6
				  "tipo_cie, " + //7
				  "convencion, " + //8
				  "aplica_a, " + //9
				  "activo, " +  //10
				  "a_tratar, " + //11
				  "fecha_modificacion, " + //--
				  "hora_modificacion," + //--
				  "usuario_modificacion," + //12
				  "tipo_hallazgo_ceo_cop )" + //13
				  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?) ";
	
	
	/**
	 * Cadena Sql para Modificar un hallazgo odontologico
	 */
	public static String actualizarHallazgosOdontologico=" UPDATE odontologia.hallazgos_odontologicos SET  " +
				 "codigo = ?, " + //1
				 "nombre = ?, " + //2
				 "acronimo = ?, " + //3
				 "diagnostico = ?, " +//4
				 "tipo_cie = ?, " + //5
				 "convencion = ?, " + //6
				 "aplica_a = ?, " + //7					 
				 "activo = ?," + //8
				 "a_tratar = ?," + //9
				 "usuario_modificacion = ?," + //10
				 "fecha_modificacion = CURRENT_DATE ," + //--
				 "hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " + //--
				 "tipo_hallazgo_ceo_cop = ? " + //11
				 "WHERE " +
				 "consecutivo = ? "; //12
	
	
	/**
	 * Cadena Sql para Eliminar un Hallazgo Odontologico
	 */
	public static String eliminarHallazgoOdontologicoStr = "DELETE FROM odontologia.hallazgos_odontologicos WHERE consecutivo = ? ";
	
	/**
	 * Cadena Sql para buscar las convenciones patametrizadas en los hallazgos Odontologicos 
	 * */
	public static String strBuscarConvencionDeHallazgo = "SELECT " +
														 "h.consecutivo," +
														 "h.codigo," +
														 "UPPER(h.nombre) as nombre," +
														 "coalesce(h.convencion,"+ConstantesBD.codigoNuncaValido+") AS convencion," +
														 "coalesce(co.archivo_convencion,' ') AS  archivo_convencion, "+
														 "coalesce(co.borde,' ') AS  borde_convencion "+
														 "FROM " +
														 "odontologia.hallazgos_odontologicos h "+
														 " 2 "+
														 "WHERE h.institucion = ? AND h.aplica_a = ? AND h.activo = '"+ConstantesBD.acronimoSi+"' ";
	
	
	
	/**
	 * Método de consulta, para listar los hallazgos Odontológicos
	 * @param codInstitucion
	 * @return
	 */
	public static ArrayList<DtoHallazgoOdontologico> consultarHallazgosDentales(Connection con, int codInstitucion)
	{
		ArrayList<DtoHallazgoOdontologico> array=new ArrayList<DtoHallazgoOdontologico>();
		String cadenaConsulta=consultaHallazgosOdontologicos+" order by hallodo.codigo asc ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codInstitucion);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoHallazgoOdontologico dto=new DtoHallazgoOdontologico();
				dto.setConsecutivo(rs.getInt("consecutivo")+"");
				dto.setCodigo(rs.getString("codigohallazgo"));				
				dto.setNombre(rs.getString("nombrehallazgo"));
				dto.setAcronimo(rs.getString("acronimohallazgo"));
				dto.setDiagnostico(rs.getString("diagnosticohallazgo"));
				dto.setDescripcionDiagnostico(rs.getString("descripciondiagnostico"));
				dto.setTipo_cie(rs.getInt("tipociehallazgo")+"");				
				dto.setConvencion(rs.getInt("convencionhallazgo")+"");	
				dto.setArchivoconvencion(rs.getString("convencionhallazgo"));	
				dto.setAplica_a(rs.getString("aplicaahallazgo"));
				dto.setActivo(rs.getString("activohallazgo"));				
				dto.setA_tratar(rs.getString("atratarhallazgo")); 
				dto.setArchivoconvencion(rs.getString("nomconvencionhallazgo"));
				dto.setTipoHallazgo(rs.getLong("tipohallazgo"));
				dto.setNombreTipoHallazgo(rs.getString("nombretipohallazgo"));
				
				InfoDatosString info = new InfoDatosString();
				if (!UtilidadTexto.isEmpty(rs.getString("borde_convencion"))) {
					info.setNombre(rs.getString("borde_convencion").toLowerCase());
				} else {
					info.setNombre("");
				}
				dto.setInfoConvecion(info);

			   // dto.setCodigoDouble(rs.getDouble("codigodouble")); Se quita por daño en Ordenamiento Alfanumerico Mayo 6 - 2010
				
				
				array.add(dto);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error en Consulta Hallazgos Odontologicos Cadena>> "+cadenaConsulta+"  CodInstitucion>> "+codInstitucion, e);
		}
		
		return array;
			
	}

	/**
	 * Inserción de una Hallazgo Odontológico
	 * @param con
	 * @param nuevoHallazgo
	 * @param loginUsuario
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static boolean crearNuevoHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, String loginUsuario,int codigoInstitucionInt) {
		
		String cadenaInsercion= insertarNuevoHallazgoOdontologico;
		boolean resp=false;
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_hallaz_odonto");	
		try{
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 ps.setInt(1, consecutivo);
			 nuevoHallazgo.setCodigo(consecutivo+"");
			 ps.setString(2, nuevoHallazgo.getCodigo());
			 ps.setInt(3, codigoInstitucionInt);
			 ps.setString(4, nuevoHallazgo.getNombre());
			 ps.setString(5, nuevoHallazgo.getAcronimo());
			 
			 if(!nuevoHallazgo.getDiagnostico().equals("")){
				 ps.setString(6, nuevoHallazgo.getDiagnostico());	 
			 }else{
	 			 ps.setNull(6,Types.VARCHAR);
			 }
			 if(Utilidades.convertirAEntero(nuevoHallazgo.getTipo_cie())>0){
				 ps.setInt(7, Utilidades.convertirAEntero(nuevoHallazgo.getTipo_cie()));
			 }else{
				 ps.setNull(7,Types.INTEGER);
			 }
			 
			 if(Utilidades.convertirAEntero(nuevoHallazgo.getConvencion())>0){
				 ps.setInt(8, Utilidades.convertirAEntero(nuevoHallazgo.getConvencion()));
			 }else{
				 ps.setNull(8,Types.INTEGER);
			 }			
			 ps.setString(9, nuevoHallazgo.getAplica_a());
			 ps.setString(10, nuevoHallazgo.getActivo());
			 ps.setString(11, nuevoHallazgo.getA_tratar());
			 ps.setString(12, loginUsuario);
			 if(nuevoHallazgo.getTipoHallazgo() != ConstantesBD.codigoNuncaValidoLong) {
				 ps.setLong(13, nuevoHallazgo.getTipoHallazgo());
			 } else {
				 ps.setNull(13,Types.INTEGER);
			 }		  
			 
			 if(ps.executeUpdate()>0)
			 {	
				 resp=true;			
			 }
			 ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error en Crear Hallazgo Odontológico Cadena>> "+cadenaInsercion, e);
		}
		return resp;
	}
	
	
	/**
	 * Realizar la actualización de un Hallazgo Odontológico
	 * @param con
	 * @param nuevoHallazgo
	 * @param consecutivoHallazgo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo,int consecutivoHallazgo, String loginUsuario)
	{
		 String cadenaModificar= actualizarHallazgosOdontologico;		 
		 boolean resp=false;
		 
		 
	   try{			 
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			 ps.setString(1, nuevoHallazgo.getCodigo());
			 ps.setString(2, nuevoHallazgo.getNombre());
			 ps.setString(3, nuevoHallazgo.getAcronimo());
			 
			 if(!nuevoHallazgo.getDiagnostico().equals("")){
				 ps.setString(4, nuevoHallazgo.getDiagnostico());	 
			 }else{
	 			 ps.setNull(4,Types.VARCHAR);
			 }
			 if(Utilidades.convertirAEntero(nuevoHallazgo.getTipo_cie())>0){
				 ps.setInt(5, Utilidades.convertirAEntero(nuevoHallazgo.getTipo_cie()));
			 }else{
				 ps.setNull(5,Types.INTEGER);
			 }
			 
			 if(Utilidades.convertirAEntero(nuevoHallazgo.getConvencion())>0){
				 ps.setInt(6, Utilidades.convertirAEntero(nuevoHallazgo.getConvencion()));
			 }else{
				 ps.setNull(6,Types.INTEGER);
			 }			
			 ps.setString(7, nuevoHallazgo.getAplica_a());
			 ps.setString(8, nuevoHallazgo.getActivo());
			 ps.setString(9, nuevoHallazgo.getA_tratar());
			 ps.setString(10, loginUsuario);
			 if(nuevoHallazgo.getTipoHallazgo() != ConstantesBD.codigoNuncaValidoLong) {
				 ps.setLong(11, nuevoHallazgo.getTipoHallazgo());
			 } else {
				 ps.setNull(11,Types.INTEGER);
			 }
			 ps.setInt(12, consecutivoHallazgo);
			 
			 if(ps.executeUpdate()>0)
			 {	
				 resp=true;			
			 }
				
			 ps.close();
	   }
	   catch (SQLException e) 
	   {
		   Log4JManager.error("Error en Actualizar Hallazgo Odontológico Cadena>> "+cadenaModificar, e);
	   } 
	   
	   return resp;
	}	
	
	
	public static boolean eliminarHallazgoOdontologico(int codHallazgo)
	{
		
	 String cadenaEliminar= eliminarHallazgoOdontologicoStr;
	 boolean resp= false;
		
	 
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codHallazgo);
			
			if(ps.executeUpdate()>0)
			{
				resp=true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error en Eliminacion Hallazgo Odonotologico Cadena>> "+cadenaEliminar+"  Consecutivo>> "+codHallazgo, e);
		}
		
		UtilidadBD.closeConnection(con);
		return resp;
	}
	
	/**
	 * METODO PARA CARGAR LOS HALLAZGOS 
	 * RECIBE UN DTO CON LOS ATRIBUTOS DEL HALLAZGO A BUSCAR 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoHallazgoOdontologico> busquedaAvanzadaHallazgos(DtoHallazgoOdontologico dto ){
		
		
		  String consultaStr = "SELECT " +
		  					" " +
						   "hallodo.consecutivo AS consecutivo, " +
						   "hallodo.codigo AS codigohallazgo, ";
						   if(!UtilidadTexto.isEmpty(dto.getYaFueSeleccionado()))
						   {
							   consultaStr+=" case when hallodo.consecutivo in ("+dto.getYaFueSeleccionado()+") then 'S' else 'N' end as yaFueSeleccionado , " ;
								  
						   }
						   else 
						   {
							   consultaStr+="  'N'  as yaFueSeleccionado , ";
						   }
						   consultaStr+=" hallodo.nombre AS nombrehallazgo, " +
						   "hallodo.acronimo AS acronimohallazgo, " +
						   "hallodo.diagnostico AS diagnosticohallazgo, " +
						   "hallodo.tipo_cie AS tipociehallazgo, " +
						   "hallodo.convencion AS convencionhallazgo, " +
						   "hallodo.aplica_a AS aplicaahallazgo, " +
						   "hallodo.activo AS activohallazgo, " +
						   "hallodo.a_tratar AS atratarhallazgo " +				   
						   "FROM odontologia.hallazgos_odontologicos hallodo " +
						   "WHERE hallodo.institucion = ? ";

	   consultaStr+=UtilidadTexto.isEmpty(dto.getConsecutivo())?"":" AND hallodo.consecutivo="+dto.getConsecutivo()+"";
	   consultaStr+=UtilidadTexto.isEmpty(dto.getCodigo())?"":" AND hallodo.codigo='"+dto.getCodigo()+"'";
	   consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?"":" AND UPPER(hallodo.nombre) like UPPER ('%"+dto.getNombre()+"%') ";
	   consultaStr+=" AND hallodo.activo='"+dto.getActivo()+"'";
	   
	   ArrayList<DtoHallazgoOdontologico> newDtoHallazgo = new ArrayList<DtoHallazgoOdontologico>();

	   try 
	   {
		   Connection con = UtilidadBD.abrirConexion();
		   PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+"  ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		   ps.setInt(1, dto.getInstitucion());
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				
				DtoHallazgoOdontologico  newDto = new DtoHallazgoOdontologico();
				
				newDto.setCodigo(rs.getString("codigohallazgo"));
				newDto.setNombre(rs.getString("nombrehallazgo"));
				newDto.setConsecutivo(rs.getString("consecutivo"));
				newDto.setAcronimo(rs.getString("acronimohallazgo"));
				newDto.setDiagnostico(rs.getString("diagnosticohallazgo"));
				newDto.setTipo_cie(rs.getString("tipociehallazgo"));
				newDto.setYaFueSeleccionado(rs.getString("yaFueSeleccionado"));
				//newDto.setDescripcionDiagnostico(rs.getString("descripciondiagnostico"));
				newDtoHallazgo.add(newDto);
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
	   }
	   catch (SQLException e) 
	   {
		   Log4JManager.error("error en carga hallazgos==> ",e);
	   }
	   return newDtoHallazgo;
	}
	
	/**
	 * Retorna las convenciones parametrizadas dentro de los hallazgos
	 * */
	public static ArrayList<DtoHallazgoOdontologico> busquedaConvencionesHallagos(DtoHallazgoOdontologico dto )
	{
		ArrayList<DtoHallazgoOdontologico> newDtoHallazgo = new ArrayList<DtoHallazgoOdontologico>();
		String cadena = strBuscarConvencionDeHallazgo;
		cadena = cadena.replace("2","LEFT OUTER JOIN odontologia.convenciones_odontologicas co ON (co.consecutivo = h.convencion ) ");
		
		if(dto.isBuscarConConvencion())
		{
			if(!dto.isBuscarConConvencionVacia())
			{
				cadena += 	" AND h.convencion IS NOT NULL " +
							" AND co.archivo_convencion IS NOT NULL "; // modificado x tarea 156577
			}
			/*else
				cadena += " AND h.convencion IS NULL ";*/
		}
		
		cadena += "ORDER BY nombre ASC ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,cadena);
			ps.setInt(1, dto.getInstitucion());
			ps.setString(2, dto.getAplica_a());
			
			//logger.info("valor de la cadena >> "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoHallazgoOdontologico  newDto = new DtoHallazgoOdontologico();
				newDto.setConsecutivo(rs.getString("consecutivo"));
				newDto.setCodigo(rs.getString("codigo"));
				newDto.setNombre(rs.getString("nombre").toUpperCase());
				newDto.setConvencion(rs.getString("convencion"));

				InfoDatosString info = new InfoDatosString();
				info.setDescripcion(rs.getString("archivo_convencion"));
				info.setIndicativo(CarpetasArchivos.CONVENCION.getRutaAbsoluta(dto.getPath()));
				if (!UtilidadTexto.isEmpty(rs.getString("borde_convencion"))) {
					info.setNombre(rs.getString("borde_convencion").toLowerCase().replace("#", "0x"));
				} else {
					info.setNombre("");
				}
				newDto.setInfoConvecion(info);

				newDtoHallazgo.add(newDto);
			}
			
			 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e)
		{
			Log4JManager.error("error en carga Convenciones de hallazgos==> "+cadena, e);
		}
		
		return newDtoHallazgo;
	}
	
	
	public static String obtenerAplicaAXHallazgo(double hallazgo)
	{
		String resultado= "";
		String consultaStr= "SELECT aplica_a AS aplica_a FROM odontologia.hallazgos_odontologicos where consecutivo=? ";
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setDouble(1, hallazgo);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= rs.getString(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR en insert ", e);
		}
		return resultado;
	}
	
}