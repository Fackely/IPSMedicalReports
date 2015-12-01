package util.interfazERPOracle;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;

public class InterfazERPOracle 
{

	/**
	 * Varible de la fuente de  Datos
	 */
	private DataSource fuenteDatos = null;
	
	
	/**
	 * Log4j para el manejo de errores
	 */
	private static Logger logger=Logger.getLogger(InterfazERPOracle.class);

	
	/**
	 * 
	 */
	/*private static String cadena="insert into CVJ_ZZ_TEMP_LOAD_AXIOMAGL (" +
														" estado," +
														" attribute1," +
														" attribute2," +
														" attribute3," +
														" attribute4," +
														" attribute5," +
														" attribute6," +
														" attribute7," +
														" attribute8," +
														" attribute9," +
														" attribute10," +
														" attribute11," +
														" attribute12," +
														" attribute13," +
														" attribute14," +
														" attribute15," +
														" attribute16," +
														" attribute17," +
														" attribute18," +
														" attribute19," +
														" attribute20," +
														" attribute21," +
														" attribute22," +
														" attribute23," +
														" attribute24," +
														" attribute25," +
														" attribute26," +
														" attribute27," +
														" attribute28," +
														" attribute29," +
														" attribute30," +
														" attribute31," +
														" CREATION_DATE," +
														" CREATED_BY," +
														" LAST_UPDATE_DATE," +
														" LAST_UPDATED_BY )" +
														" values(" +
														" ?,?,?,?,?," +//5
														" ?,?,?,?,?," + //10
														" ?,?,?,?,?," + //15
														" ?,?,?,?,?," + //20
														" ?,?,?,?,?,"+ //25
														" ?,?,?,?,?," + //30
														" ?,?,?,?,?," + //35
														" ?" +  //36
														" )";*/
	

	public InterfazERPOracle() 
	{
		try
        {
            // recuperamos el contexto inicial y la referencia a la fuente de datos
            Context ctx = new InitialContext();
            fuenteDatos = (DataSource) ctx.lookup("java:comp/env/jdbc/interfazERPOracle");
        }
        catch (Exception e)
        {
           logger.error("ERROR INCIALIZANDO EL POOL DE LA INTERFAZ");
        }
	}
	
	/**
	 * 
	 * Metodo que toma una conexion a la funte de datos de la interfaz, desde el pool
	 * @return
	 */
	private Connection abrirConexionInterfaz ()
	{
		
		Connection con=null;
		if(fuenteDatos!=null)
		{
			try 
			{
				con = fuenteDatos.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
	
	/**
	 * Metodo que cierra la conexion a la funte de datos de la interfaz
	 * @param con
	 */
	private void cerrarConexion(Connection con) 
	{
		try
		{
			if(con!=null&&!con.isClosed())
			{
				con.close();
			}
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR CERRANDO LA CONEXION");
			
		}
	}
	
	public boolean insertarInterfaz(DtoInterfazERPOracle dto)
	{
		boolean respuesta=true;
		Connection con=this.abrirConexionInterfaz();
		PreparedStatementDecorator ps;
		try 
		{
			logger.info("fecha creacion-->"+dto.getFechaCreacionRegistro()+ "usuario creacion-->"+dto.getUsuarioCreacionRegistro()+" fecha mod registro->"+dto.getFechaModificacionRegistro()+" usuario mod reg->"+dto.getUsuarioModificacionRegistro());
			logger.info(con);
			
			
			String cadena1="insert into CVJ_ZZ_TEMP_LOAD_AXIOMAGL (" +
			" estado," +
			" attribute1," +
			" attribute2," +
			" attribute3," +
			" attribute4," +
			" attribute5," +
			" attribute6," +
			" attribute7," +
			" attribute8," +
			" attribute9," +
			" attribute10," +
			" attribute11," +
			" attribute12," +
			" attribute13," +
			" attribute14," +
			" attribute15," +
			" attribute16," +
			" attribute17," +
			" attribute18," +
			" attribute19," +
			" attribute20," +
			" attribute21," +
			" attribute22," +
			" attribute23," +
			" attribute24," +
			" attribute25," +
			" attribute26," +
			" attribute27," +
			" attribute28," +
			" attribute29," +
			" attribute30," +
			" attribute31," +
			" CREATION_DATE," +
			" CREATED_BY," +
			" LAST_UPDATE_DATE," +
			" LAST_UPDATED_BY )" +
			" values(" +
			" '"+dto.getEstado()+"','"+dto.getAtributo1()+"','"+dto.getAtributo2()+"','"+dto.getAtributo3()+"','"+dto.getAtributo4()+"'," +//5
			" '"+dto.getAtributo5()+"','"+dto.getAtributo6()+"','"+dto.getAtributo7()+"','"+dto.getAtributo8()+"','"+dto.getAtributo9()+"'," + //10
			" '"+dto.getAtributo10()+"','"+dto.getAtributo11()+"','"+dto.getAtributo12()+"','"+dto.getAtributo13()+"','"+dto.getAtributo14()+"'," + //15
			" '"+dto.getAtributo15()+"','"+dto.getAtributo16()+"','"+dto.getAtributo17()+"','"+dto.getAtributo18()+"','"+dto.getAtributo19()+"'," + //20
			" '"+dto.getAtributo20()+"','"+dto.getAtributo21()+"','"+dto.getAtributo22()+"','"+dto.getAtributo23()+"','"+dto.getAtributo24()+"',"+ //25
			" '"+dto.getAtributo25()+"','"+dto.getAtributo26()+"','"+dto.getAtributo27()+"','"+dto.getAtributo28()+"','"+dto.getAtributo29()+"'," + //30
			" '"+dto.getAtributo30()+"','"+dto.getAtributo31()+"',CURRENT_DATE,1,CURRENT_DATE," + //35
			" 1" +  //36
			" )";
			
			logger.info(cadena1);
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena1));
			/*ps.setString(1,dto.getEstado());
			ps.setString(2,dto.getAtributo1());
			ps.setString(3,dto.getAtributo2());
			ps.setString(4,dto.getAtributo3());
			ps.setString(5,dto.getAtributo4());
			ps.setString(6,dto.getAtributo5());
			ps.setString(7,dto.getAtributo6());
			ps.setString(8,dto.getAtributo7());
			ps.setString(9,dto.getAtributo8());
			ps.setString(10,dto.getAtributo9());
			ps.setString(11,dto.getAtributo10());
			ps.setString(12,dto.getAtributo11());
			ps.setString(13,dto.getAtributo12());
			ps.setString(14,dto.getAtributo13());
			ps.setString(15,dto.getAtributo14());
			ps.setString(16,dto.getAtributo15());
			ps.setString(17,dto.getAtributo16());
			ps.setString(18,dto.getAtributo17());
			ps.setString(19,dto.getAtributo18());
			ps.setString(20,dto.getAtributo19());
			ps.setString(21,dto.getAtributo20());
			ps.setString(22,dto.getAtributo21());
			ps.setString(23,dto.getAtributo22());
			ps.setString(24,dto.getAtributo23());
			ps.setString(25,dto.getAtributo24());
			ps.setString(26,dto.getAtributo25());
			ps.setString(27,dto.getAtributo26());
			ps.setString(28,dto.getAtributo27());
			ps.setString(29,dto.getAtributo28());
			ps.setString(30,dto.getAtributo29());
			ps.setString(31,dto.getAtributo30());
			ps.setString(32,dto.getAtributo31());
			ps.setString(33,dto.getFechaCreacionRegistro());
			//ps.setString(34,dto.getUsuarioCreacionRegistro());
			ps.setDouble(34, 1111);
			ps.setString(35,dto.getFechaModificacionRegistro());
			//ps.setString(36,dto.getUsuarioModificacionRegistro());
			ps.setDouble(36, 1111);*/
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		this.cerrarConexion(con);
		return respuesta;
	}

	
	/**
	 * 
	 * @param con
	 * @param facturas
	 * @param usuario
	 * @param codigoIngreso
	 * @param numeroIdentificacionPersona
	 */
	public void insertarInterfazContableFactura(Connection con, ArrayList<DtoFactura> facturas, UsuarioBasico usuario, String codigoIngreso, String numeroIdentificacionPersona)
	{
		
		for(int w=0; w<facturas.size(); w++)
		{
			DtoFactura dtoFactura= facturas.get(w);
			this.insertarInterfaz(new DtoInterfazERPOracle("A",
															UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
															"COP",
															"Facturación Salud",
															"Axioma",
															"EMPRESA",
															"UEN",
															"CC",
															"Cuenta",
															"UsoLocal",
															"Intercompany",
															"Tipo",
															"Edicion",
															"10000",
															"credito",
															UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
															"nombre lote",
															"nombre comprobante",
															"historico",
															"atributo 19",
															"atributo 20",
															"atributo 21",
															"atributo 22",
															"atributo 23",
															"atributo 24",
															"atributo 25",
															"atributo 26",
															"atributo 27",
															"atributo 28",
															"atributo 29",
															"atributo 30",
															"atributo 31",
															UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
															usuario.getLoginUsuario(),
															UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
															usuario.getLoginUsuario()
															)
								);
		}
		
	}
	
	
}
