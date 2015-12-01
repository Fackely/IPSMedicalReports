package util.interfaces;

import java.sql.CallableStatement;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dto.interfaz.DtoInterfazAxArt;


/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class UtilidadInsertarAxArt {

	/**
	 * Varible de la fuente de  Datos
	 */
	private DataSource fuenteDatos = null;
	
	/**
	 * Log4j para el manejo de errores
	 */
	private static Logger logger=Logger.getLogger(UtilidadInsertarAxArt.class);

	/**
	 * Constructor de la clase, inicializa el pool de conexiones
	 *
	 */
	public UtilidadInsertarAxArt() 
	{
		try
        {
            // recuperamos el contexto inicial y la referencia a la fuente de datos
            Context ctx = new InitialContext();
            fuenteDatos = (DataSource) ctx.lookup("java:comp/env/jdbc/interfazTesoreria");
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
				logger.info("\n Problema abriendo conexion con la interfaz "+e);
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

	/**
	 * Metodo encargado de consultar los datos de la interfaz ax_art local
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoInterfazAxArt> consultaDatosResprocesoArticulos (int institucion)
	{
		logger.info("\n *****************--> entre a consultaDatosResprocesoArticulos... ");
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoInterfazAxArt> array = new ArrayList<DtoInterfazAxArt>();
		String consulta="SELECT codaxi,descri,tipcrea,tipinv,codsha,fecha,hora,estreg,consecutivo  FROM ax_art";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.setInt(1, institucion); este parametro es por si piden la interfaz por institucion
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				DtoInterfazAxArt dto= new DtoInterfazAxArt();
				dto.setCodaxi(rs.getString("codaxi"));//1
				dto.setDescri(rs.getString("descri"));//2
				dto.setTipcrea(rs.getString("tipcrea"));//3
				dto.setTipinv(rs.getString("tipinv"));//4
				dto.setCodsha(rs.getString("codsha"));//5
				dto.setFecha(rs.getString("fecha"));//6
				dto.setHora(rs.getString("hora"));//7
				dto.setEstreg(rs.getString("estreg"));//8
				dto.setConsecutivo(rs.getString("consecutivo"));//9
				
				array.add(dto);
			}
			rs.close();
			
		} 
		catch (SQLException  e) 
		{
			
			logger.info("\n problema consultando los datos de la tabla ax_art local "+e);
		}

		UtilidadBD.closeConnection(con);
		
		return array;
	}
	
	
	/**
	 * 
	 * @param arrayDtoArt
	 * @param institucion
	 * @param isReproceso
	 */
	public void insertarArticulosReproceso (ArrayList<DtoInterfazAxArt> arrayDtoArt,int institucion,boolean isReproceso)
	{
		for (int i=0;i<arrayDtoArt.size();i++)
			insertar(arrayDtoArt.get(i), institucion,isReproceso);
	}	
	
	
	
	
	
	
	/**
	 * Metodo para insertar un registro en la tabla de ax_art de la interfaz
	 * Modificado por el anexo 779
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertar(DtoInterfazAxArt dto,int institucion,boolean isReproceso)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaArticulos,institucion);
		
		String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
		String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
		String codigoShaio="00000000000";
		Connection con=abrirConexionInterfaz();
		boolean tmp=true;
		String indicador="",column="",tipoInv="";
		
		if (isReproceso)
		{
			tipoInv=dto.getTipinv();
		}
		else
		{
			tipoInv=(agregarCeros(dto.getClase(),3)+dto.getClase());
		}
		try 
		{
			if(con==null || con.isClosed())
			{
				if (!isReproceso)
				{
					con=UtilidadBD.abrirConexion();
					column=",tiperr,consecutivo";
					indicador=",'"+ConstantesIntegridadDominio.acronimoNoConexion+"',"+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ax_art");
					tmp=false;
				}
				else
					return new ResultadoBoolean(false,"");
			}
			String cadenaInsercion="INSERT INTO "+nombreTabla+" (codaxi,descri,tipcrea,tipinv,codsha,estreg,fecha,hora"+column+") values " +
												"("+dto.getCodaxi()+",'"+dto.getDescri()+"','"+dto.getTipcrea()+"','"+tipoInv+"','"+codigoShaio+"',"+dto.getEstreg()+","+fecha+","+hora+indicador+")";
			
			logger.info("\n\nINSERTAR AX_ART --->"+cadenaInsercion+"\n\n");
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
						
		
			if(ps.executeUpdate()>0)
			{
				if (tmp)
				{
					//se ejecuta este proceso en el cliente remoto
					ejecutarProcesoInterfazArticulos(con);
					logger.info("\n\n ################################ ELIMINAR REGISTRO LOCAL INTERFAZ ARTICULOS CODIGO --> "+dto.getConsecutivo());
					//se manda a elimiar el registro si existe desde en la bd local
					//*******************************************************************************************
					if (UtilidadCadena.noEsVacio(dto.getConsecutivo()) && isReproceso)
					{
						Connection con1=UtilidadBD.abrirConexion();
						try 
						{
							ps=new PreparedStatementDecorator(con1.prepareStatement("DELETE FROM ax_art WHERE consecutivo="+dto.getConsecutivo()));
							ps.executeUpdate();
						} 
						catch (Exception e) 
						{
							logger.info("\n problema eliminado el registro de la tabla ax_art local "+e);
						}
						ps.close();
						UtilidadBD.cerrarConexion(con1);	
					}
					//*************************************************************************************************
					
					
				}
				ps.close();
				cerrarConexion(con);
				logger.error("El registro se inserto correctamente. ");
				
				if (tmp)
					return new ResultadoBoolean(true,"La operacion se realizo correctamente.");
				else
					return new ResultadoBoolean(true,"No se pudo registrar información de artículos en la interfaz no hay conexión.");
				
			}
			
			ps.close();
			UtilidadBD.closeConnection(con);
			logger.error("El registro no se pudo Insertar. ");
			return new ResultadoBoolean(false,"El registro no se pudo Insertar");
		} 
		catch (Exception e) 
		{
			UtilidadBD.closeConnection(con);
			logger.error("\n Problema al insertar articulo "+e);
			if (!isReproceso)
				return insertarResplado(dto, institucion);
		}
		return new ResultadoBoolean(false,"El registro no se pudo Insertar");
	}

	
	/**
	 * Metodo para insertar un registro en la tabla de ax_art de la interfaz
	 * Modificado por el anexo 779
	 * @param dto
	 * @return
	 */
	public ResultadoBoolean insertarResplado(DtoInterfazAxArt dto,int institucion)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaArticulos,institucion);
		
		String fecha=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).replace("-", "");
		String hora=UtilidadFecha.getHoraSegundosActual().replace(":", "");
		String codigoShaio="00000000000";
		Connection con=UtilidadBD.abrirConexion();
			
		try 
		{
			String cadenaInsercion="INSERT INTO "+nombreTabla+" (codaxi,descri,tipcrea,tipinv,codsha,estreg,fecha,hora,tiperr) values " +
												"("+dto.getCodaxi()+",'"+dto.getDescri()+"','"+dto.getTipcrea()+"','"+(agregarCeros(dto.getClase(),3)+dto.getClase())+"','"+codigoShaio+"',"+dto.getEstreg()+","+fecha+","+hora+",'"+ConstantesIntegridadDominio.acronimoSinIntegridad+"')";
			
			logger.info("\n\nINSERTAR AX_ART --->"+cadenaInsercion+"\n\n");
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
		
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.closeConnection(con);
				logger.error("El registro se inserto correctamente. ");
				
				return new ResultadoBoolean(true,"No se pudo registrar información de artículos en la interfaz por falta de integridad en los datos. ");
				
			}
			
			ps.close();
			UtilidadBD.closeConnection(con);
			logger.error("El registro no se pudo Insertar. ");
			return new ResultadoBoolean(false,"El registro no se pudo Insertar");
		} 
		catch (Exception e) 
		{
			UtilidadBD.closeConnection(con);
			logger.error("\n Problema al insertar articulo "+e);
			return new ResultadoBoolean(false,"El registro no se pudo Insertar ocurrio una excepción. ");
		}
	}
	
	/**
	 * 
	 * @param con
	 * @throws SQLException 
	 */
	private void ejecutarProcesoInterfazArticulos(Connection con)
	{
		try 
		{
			 CallableStatement cs = con.prepareCall("CALL AXGA100CP");
	         cs.execute ();
	         cs.close();	
		}
		catch (SQLException e) 
		{
			logger.info("\n problema ejecutando el proceso CALL AXGA100CP "+e);
		}
		
	}
	
	/** 
	 * @param cadena
	 * @param lengthRequerido
	 * @return
	 */
	public String agregarCeros(String cadena, int lengthRequerido)
	{
		String concatena="";
		if(cadena.length()<lengthRequerido)
		{
			int faltantes=0;
			faltantes= lengthRequerido-cadena.length();
			for(int w=0; w<faltantes; w++)
				concatena+="0";
			//logger.info("concatena->"+concatena);
		}
		return concatena;
	}
	
}