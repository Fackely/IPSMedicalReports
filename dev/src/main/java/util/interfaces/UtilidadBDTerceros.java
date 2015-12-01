package util.interfaces;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ResultadoBoolean;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import com.princetonsa.dto.interfaz.DtoInterfazTerceros;


/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class UtilidadBDTerceros {
	
	/**
	 * Variable de la fuente de datos
	 */
	private DataSource fuenteDatos=null;
	
	/**
	 * Log para el manejo de errores
	 */
	private static Logger logger=Logger.getLogger(UtilidadBDTerceros.class);
	
	/**
	 * Constructor de la clase, inicializa el pool de conexiones
	 */
	public UtilidadBDTerceros()
	{
		try
		{
			//se recupera el contexto inicial y la referencia a la fuente de datos
			Context ctx=new InitialContext();
			fuenteDatos=(DataSource) ctx.lookup("java:comp/env/jdbc/interfazTesoreria");
		}
		catch (Exception e) {
			logger.error("ERROR INICIANDO EL POOL DE LA INTERFAZ");
		}
	}
	
	/**
	 * Metodo que toma una conexion a la fuente de datos de la interfaz desde el pool 
	 */
	private Connection abrirConexionInterfaz()
	{
		Connection con=null;
		if(fuenteDatos!=null)
		{
			try
			{
				con=fuenteDatos.getConnection();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
	
	/**
	 * Metodo para cerrar la conexion a la fuente de datos de la interfaz
	 * @param con
	 */private void cerrarConexion(Connection con)
	{
		try
		{
			if(con!=null&&!con.isClosed())
			{
				con.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("ERROR CERRANDO LA CONEXION");
		}
	}
	 
	/**
	 * Metodo para insertar el tercero creado en Interfaz terceros
	 * El metodo verifica si el codigo Axioma tiene informacion para hacer la respectiva modificacion.
	 * @param dto
	 * @return
	 */
	public int insertarTerceros(DtoInterfazTerceros dto)
	{
		Connection con=UtilidadBD.abrirConexion();
		int codigoTercero=ConstantesBD.codigoNuncaValido;
	//	Utilidades.obtenercodigoTercerodeNumeroIdentificacion(numeroIdTercero)
		try
		{
			if(con==null || con.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return ConstantesBD.codigoNuncaValido;
			}
			
			codigoTercero=Utilidades.obtenercodigoTercerodeNumeroIdentificacion(dto.getNumero_identificacion());
			
			
			if(codigoTercero == ConstantesBD.codigoNuncaValido)
			{
				codigoTercero=UtilidadBD.obtenerSiguienteValorSecuencia(con, "SEQ_TERCEROS");
				String cadenaInsercion="INSERT INTO terceros (codigo,numero_identificacion,descripcion,institucion,activo,digito_verificacion,tipo_tercero) VALUES("+codigoTercero+",?,?,?,?,?,?)";
				
				logger.info("\n\n>>>>>>>>>>>>>>> ENTRO A INSERTAR TERCERO EN AXIOMA <<<<<<<<<<<<<<\n");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
				ps.setString(1, dto.getNumero_identificacion());
				ps.setString(2, dto.getDescripcion());
				ps.setInt(3, dto.getInstitucion());
				ps.setString(4, dto.getActivo());
				ps.setString(5, null);
				ps.setInt(6, ConstantesBD.codigoTipoTerceroNoAplica);
				if(ps.executeUpdate()>0)
				{
					ps.close();
					cerrarConexion(con);
					return codigoTercero;
				}
				logger.error("EL REGISTRO NO SE PUDO INSERTAR");
				ps.close();
				cerrarConexion(con);
				return ConstantesBD.codigoNuncaValido;
			}
			else
			{
				String cadenaModificar="UPDATE terceros set numero_identificacion=?, descripcion=? ,institucion=?, activo=?, digito_verificacion=?, tipo_tercero=? where codigo=? ";
				logger.info("\n\n>>>>>>>>>>>>>>> ENTRO A MODIFICAR TERCERO EN AXIOMA <<<<<<<<<<<<<<\n");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificar));
				ps.setString(1, dto.getNumero_identificacion());
				ps.setString(2, dto.getDescripcion());
				ps.setInt(3, dto.getInstitucion());
				ps.setString(4, dto.getActivo());
				ps.setString(5, null);
				ps.setInt(6, ConstantesBD.codigoTipoTerceroNoAplica);
				ps.setInt(7, codigoTercero);
				if(ps.executeUpdate()>0)
				{
					ps.close();
					cerrarConexion(con);
					return codigoTercero;
				}
				logger.error("EL REGISTRO NO SE PUDO MODIFICAR");
				ps.close();
				cerrarConexion(con);
				return ConstantesBD.codigoNuncaValido;
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			cerrarConexion(con);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Metodo para cargar los terceros desde Interfaz terceros hasta Axioma
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean cargarAxTerToTercerosAxioma(int institucion)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaTerceros,institucion);
		String restriccionesUpdate="";
		String cadenaActualizacionAxTer="";
		Connection con=abrirConexionInterfaz();
		ArrayList<InfoDatosInt> atributos = new ArrayList<InfoDatosInt>();
		try
		{
			if(con==null || con.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
			}
			String cadenaConsultaAxTer="SELECT nidenter, nombter, codigoter, estregter, fechater, horater, tercero FROM "+nombreTabla+" WHERE estregter="+ConstantesBDInterfaz.codigoEstadoNoProcesado;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAxTer));
			//ps.setInt(1, ConstantesBDInterfaz.codigoEstadoNoProcesado);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int codigoInconsistencia=ConstantesBD.codigoNuncaValido;
			boolean inconsistencias=false;
			int contadorAtributos = 1;
			int contador=0;
			while(rs.next())
			{
				contadorAtributos = 1;
				restriccionesUpdate="";
				if(UtilidadTexto.isEmpty(rs.getString("tercero")))
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.terceroVacio;
					restriccionesUpdate+=" AND nidenter is null ";
				}
				else
				{
					restriccionesUpdate+=" AND nidenter= ? ";
					atributos.add(new InfoDatosInt(contadorAtributos,rs.getString("nidenter")));
					contadorAtributos++;
					
				}
				if(UtilidadTexto.isEmpty(rs.getString("nombter")))
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.nombreTerceroVacio;
					restriccionesUpdate+="AND nombter is null ";
				}
				else
				{
					restriccionesUpdate+="AND nombter = ? ";
					atributos.add(new InfoDatosInt(contadorAtributos,rs.getString("nombter")));
					contadorAtributos++;
				}
				try
				{
					if(!inconsistencias)
					{
						String activo="true";
												
						int codTercero=this.insertarTerceros(new DtoInterfazTerceros(rs.getDouble("codigoter")+"",rs.getInt("tercero")+"",rs.getString("nombter")+"",institucion,activo));
						if(codTercero>0)
						{
							cadenaActualizacionAxTer="UPDATE "+nombreTabla+" SET codigoter="+codTercero+", estregter="+ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+" WHERE 1=1 "+restriccionesUpdate;
							PreparedStatementDecorator psTer= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAxTer));
							for(InfoDatosInt atr:atributos)
							{
								psTer.setString(atr.getCodigo(),atr.getNombre());
							}
							//psTer.setInt(1, ConstantesBDInterfaz.codigoEstadoProcesadoAxioma);
							psTer.executeUpdate();
						}
					}
					else
					{
						cadenaActualizacionAxTer="UPDATE "+nombreTabla+" SET estregter="+codigoInconsistencia+" WHERE 1=1 "+restriccionesUpdate;
						PreparedStatementDecorator psTer= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAxTer));
						for(InfoDatosInt atr:atributos)
						{
							psTer.setString(atr.getCodigo(),atr.getNombre());
						}
						//psTer.setInt(1, codigoInconsistencia);
						psTer.executeUpdate();
					}
				}
				catch (SQLException e) {
					logger.error("ERROR CARGANDO EL TERCERO: "+e.getStackTrace());
					e.printStackTrace();
				}
				contador++;
			}
			logger.info("              SE PROCESARON "+contador+" TERCEROS");
			ps.close();
			cerrarConexion(con);
			return new ResultadoBoolean(true,"PROCESO FINALIZADO");
		}
		catch (Exception e) {
			e.printStackTrace();
			cerrarConexion(con);
			return new ResultadoBoolean(false,"SE PRODUJO UNA EXCEPCION: "+e.toString());
		}
	}
}
