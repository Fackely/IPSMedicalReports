package util.interfaces;

import java.sql.CallableStatement;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;
import com.princetonsa.dto.interfaz.DtoInterfazDetTransacciones;
import com.princetonsa.dto.interfaz.DtoInterfazInventarios;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.dto.interfaz.DtoInterfazTransacciones;
import com.princetonsa.mundo.ConsecutivosDisponibles;

public class UtilidadBDInventarios {
	
	/**
	 * Variable de la fuente de datos
	 */
	private DataSource fuenteDatos=null;
	
	/**
	 * Log para el manejo de errores
	 */
	private static Logger logger=Logger.getLogger(UtilidadBDInventarios.class);
	
	/**
	 * Constructor de la clase, inicializa el pool de conexiones
	 */
	public UtilidadBDInventarios()
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
				logger.info("\n problema abriendo conexion de interfaz. "+e);
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
	 * Metodo para insertar en convenios proveedor creado en Interfaz inventarios
	 * @param dto
	 * @return
	 */
	public boolean insertarConveniosProveedor(DtoInterfazInventarios dto)
	{
		Connection con=UtilidadBD.abrirConexion();
		String cadenaInsercion="INSERT INTO convenio_proveedor (" +
												" codigo," +
												" tipo_convenio," +
												" numero_convenio," +
												" proveedor, " +
												" codigo_shaio," +
												" codigo_axioma," +
												" cantidad_convenio," +
												" cantidad_recibida," +
												" val_uni_compra," +
												" val_uni_iva," +
												" fecha,hora," +
												" estado_registro) " +
											" VALUES(" +
												" NEXTVAL('seq_conv_prov')," +
												" '"+dto.getTipo_convenio()+"'," +
												"'"+dto.getNumero_convenio()+"','"+dto.getProveedor()+"'," +
												"'"+dto.getCodigo_shaio().trim()+"','"+dto.getCodigo_axioma()+"','"+dto.getCantidad_convenio()+"','"+dto.getCantidad_recibida()+"'," +
												"'"+dto.getVal_uni_compra()+"','"+dto.getVal_uni_iva()+"','"+dto.getFecha()+"','"+dto.getHora()+"','"+dto.getEstado_registro()+"')";
		
		
		/*
		String cadenaInsercion="INSERT INTO convenio_proveedor (codigo," +
																"tipo_convenio," +
																"numero_convenio," +
																"proveedor," +
																"codigo_shaio," +
																"codigo_axioma," +
																"cantidad_convenio," +
																"cantidad_recibida," +
																"val_uni_compra," +
																"val_uni_iva," +
																"fecha," +
																"hora," +
																"estado_registro) " +
																"VALUES(NEXTVAL('seq_conv_prov'),?,?,?,?,?,?,?,?,?,?,?,?)";
		*/
		try
		{
			if(con==null || con.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return false;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
			
			/*
			logger.info("1. dto.getTipo_convenio() -->"+dto.getTipo_convenio());
			logger.info("2. dto.getNumero_convenio() -->"+dto.getNumero_convenio());
			logger.info("3. dto.getProveedor() -->"+dto.getProveedor());
			logger.info("4. dto.getCodigo_shaio() -->"+dto.getCodigo_shaio());
			logger.info("5. dto.getCodigo_axioma() -->"+dto.getCodigo_axioma());
			logger.info("6. dto.getCantidad_convenio() -->"+dto.getCantidad_convenio());
			logger.info("7. dto.getCantidad_recibida() -->"+dto.getCantidad_recibida());
			logger.info("8. dto.getVal_uni_compra() -->"+dto.getVal_uni_compra());
			logger.info("9. dto.getVal_uni_iva() -->"+dto.getVal_uni_iva());
			logger.info("10.dto.getFecha() -->"+dto.getFecha());
			logger.info("11.dto.getHora() -->"+dto.getHora());
			logger.info("12.dto.getEstado_registro() -->"+dto.getEstado_registro());
			
			ps.setObject(1, dto.getTipo_convenio()+"");
			ps.setObject(2, dto.getNumero_convenio()+"");
			ps.setObject(3, dto.getProveedor()+"");
			ps.setObject(4, dto.getCodigo_shaio()+"");
			ps.setObject(5, dto.getCodigo_axioma()+"");
			ps.setObject(6, dto.getCantidad_convenio()+"");
			ps.setObject(7, dto.getCantidad_recibida()+"");
			ps.setObject(8, dto.getVal_uni_compra()+"");
			ps.setObject(9, dto.getVal_uni_iva()+"");
			ps.setObject(10, dto.getFecha()+"");
			ps.setObject(11, dto.getHora()+"");
			ps.setObject(12, dto.getEstado_registro()+"");
			*/
			
			int regInsertados=ps.executeUpdate();
			if(regInsertados>0)
			{
				cerrarConexion(con);
				return true;
			}
			ps.close();
			cerrarConexion(con);
			return false;
		}
		catch(Exception e)
		{
			logger.info("\n Problemas en insertarConveniosProveedor interfaz. "+e);
			cerrarConexion(con);
			return false;
		}
	}
	
	/**
	 * Metodo para insertar en transacciones por almacen lo creado en Interfaz inventarios
	 * @param dto
	 * @return
	 */
	public boolean insertarTransaccionesXalmacen(Connection con,DtoInterfazTransacciones dto)
	{
		String cadenaInsercion="INSERT INTO " +
												"transacciones_x_almacen " +
									    		"(codigo," + 		// 1
									    		"consecutivo," +	// 2
									    		"transaccion," +	// 3
									    		"fecha_elaboracion," +// 4
									    		"usuario," +		// 5
									    		"entidad," +		// 6
									    		"observaciones," +	// 7
									    		"estado," +			// 8
									    		"almacen, " +		// 9
									    		"tipo_comprobante," + // 10
									    		"nro_comprobante," +// 11
									    		"usuario_cierre," +	// 12
									    		"fecha_cierre," +	// 13
									    		"hora_cierre," +	// 14
									    		"usuario_anula," +	// 15
									    		"fecha_anula," +	// 16
									    		"hora_anula," +		// 17
									    		"motivo_anula, " +	// 18
									    		"automatica)" +		// 19
									    		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try
		{
			if(con==null || con.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return false;
			}
			
			logger.info("QUERY -->"+cadenaInsercion);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
			
			
			logger.info("dto.getSecuencia()->"+dto.getSecuencia()+"<-");
			ps.setInt(1, dto.getSecuencia());
			
			logger.info("dto.getConsecutivo()->"+dto.getConsecutivo()+"<-");
			ps.setString(2, dto.getConsecutivo());
			
			logger.info("dto.getTransaccion()->"+dto.getTransaccion()+"<-");
			ps.setInt(3, dto.getTransaccion());
			
			logger.info("dto.getFecha_elaboracion()->"+dto.getFecha_elaboracion()+"<-");
			ps.setString(4, dto.getFecha_elaboracion());
			
			logger.info("dto.getUsuario()->"+dto.getUsuario()+"<-");
			ps.setString(5, dto.getUsuario());
			
			logger.info("dto.getEntidad()->"+dto.getEntidad()+"<-");
			if(Utilidades.convertirAEntero(dto.getEntidad())>0)
				ps.setInt(6, Utilidades.convertirAEntero(dto.getEntidad()));
			else
				ps.setObject(6, null);
			
			logger.info("dto.getObservaciones()->"+dto.getObservaciones()+"<-");
			ps.setString(7, dto.getObservaciones());
			
			logger.info("dto.getEstado()->"+dto.getEstado()+"<-");
			ps.setInt(8, dto.getEstado());
			
			logger.info("dto.getAlmacen()->"+dto.getAlmacen()+"<-");
			ps.setInt(9, dto.getAlmacen());
			
			logger.info("dto.getTipo_comprobante()->"+dto.getTipo_comprobante()+"<-");
			ps.setString(10, dto.getTipo_comprobante());
			
			logger.info("dto.getNro_comprobante()->"+dto.getNro_comprobante()+"<-");
			ps.setObject(11, dto.getNro_comprobante());
			
			
			logger.info("dto.getUsuario_cierre()->"+dto.getUsuario_cierre()+"<-");
			ps.setString(12, dto.getUsuario_cierre());
			
			logger.info("dto.getFecha_cierre()->"+dto.getFecha_cierre()+"<-");
			ps.setString(13, dto.getFecha_cierre());
			
			logger.info("dto.getHora_cierre()->"+dto.getHora_cierre()+"<-");
			ps.setString(14, dto.getHora_cierre());
			
			logger.info("dto.getUsuario_anula()->"+dto.getUsuario_anula()+"<-");
			ps.setString(15, dto.getUsuario_anula());
			
			logger.info("dto.getFecha_anula()->"+dto.getFecha_anula()+"<-");
			ps.setString(16, dto.getFecha_anula());
			
			logger.info("dto.getHora_anula()->"+dto.getHora_anula()+"<-");
			ps.setString(17, dto.getHora_anula());
			
			logger.info("dto.getMotivo_anula()->"+dto.getMotivo_anula()+"<-");
			ps.setString(18, dto.getMotivo_anula());
			
			logger.info("dto.automatica()->"+ConstantesBD.acronimoSi+"<-");
			ps.setString(19, ConstantesBD.acronimoSi);
			
			if(ps.executeUpdate()>0)
			{
				logger.info("\n\nOK EL REGISTRO TRANSACCION POR ALMACEN SE INSERTO CORRETAMENTE ......Interfaz Compras\n");
				ps.close();
				return true;
			}
			logger.error("EL REGISTRO NO SE PUDO INSERTAR");
			ps.close();
			return false;
		}
		catch(Exception e)
		{
			logger.info("\n Problema insertarTransaccionesXalmacen interfaz "+e);
			return false;
		}
	}
	
	/**
	 * Metodo para insertar en transacciones por almacen lo creado en Interfaz inventarios
	 * @param dto
	 * @return
	 */
	public boolean insertarDetTransaccionesXalmacen(Connection con, DtoInterfazDetTransacciones dto)
	{
		String cadenaInsercion="INSERT INTO det_trans_x_almacen (transaccion,articulo,cantidad,val_unitario,lote,fecha_vencimiento,codigo) VALUES(?,?,?,?,?,?,nextval('seq_det_trans_x_almacen'))";
		try
		{
			if(con==null || con.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return false;
			}
			
			logger.info("DETALLE dto.getTransaccion()->"+dto.getTransaccion()+"<-");
			logger.info("DETALLE dto.getArticulo()->"+dto.getArticulo()+"<-");
			logger.info("DETALLE dto.getCantidad()->"+dto.getCantidad()+"<-");
			logger.info("DETALLE dto.getVal_unitario()->"+dto.getVal_unitario()+"<-");
			logger.info("DETALLE dto.getLote()->"+dto.getLote()+"<-");
			logger.info("DETALLE dto.getFecha_vencimiento()->"+dto.getFecha_vencimiento()+"<-");
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
			ps.setString(1, dto.getTransaccion());
			ps.setInt(2, dto.getArticulo());
			ps.setInt(3, dto.getCantidad());
			ps.setObject(4, dto.getVal_unitario());
			ps.setString(5, dto.getLote());
			ps.setString(6, dto.getFecha_vencimiento());
			if(ps.executeUpdate()>0)
			{
				logger.info("\n\nOK EL REGISTRO DETALLE TRANSACCION POR ALMACEN SE INSERTO CORRETAMENTE ......Interfaz Compras\n");
				ps.close();
				return true;
			}
			logger.info("EL REGISTRO NO SE PUDO INSERTAR");
			ps.close();
			return false;
		}
		catch(Exception e)
		{
			logger.info("\n problema en insertarDetTransaccionesXalmacen "+e);
			return false;
		}
	}
	
	public int consultarHomologacion(int idtr)
	{
		Connection con=UtilidadBD.abrirConexion();
		String cadenaConsultaHomologacion="SELECT tipos_conceptos_inv FROM tipos_trans_inventarios WHERE codigo_interfaz='"+idtr+"'";
		try
		{
			if(con==null || con.isClosed())
			{
				logger.info("CONEXION CERRADA");
				return ConstantesBD.codigoNuncaValido;
			}
			PreparedStatementDecorator psIntCom= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaHomologacion));
			ResultSetDecorator rsIntCom=new ResultSetDecorator(psIntCom.executeQuery());
			if(rsIntCom.next())
			{
				cerrarConexion(con);
				return rsIntCom.getInt("tipos_conceptos_inv");
			}
		}
		catch (SQLException e) {
			logger.error("\n ERROR CARGANDO VALORES POR DEFECTO: "+e);
			cerrarConexion(con);
			
		}
		cerrarConexion(con);
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo para actualizar cargar las transacciones de entrada y salida en axioma
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean cargarAxInvToTransacciones(int institucion,String loginUsuarioInterfaz)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaTransacciones,institucion);
		
		//se asume que cada registro en la tabla ax_inv es una transaccion diferente.
		//en caso de que una transaccion tenga varios registros en ax_inv, debe consultar primer la transaccion con solo los campos de elcabezado haciendo un group by, y luego consultar el detalle con el codigo de la transaccion, para poderlo insertar.
		//String cadenaConsultaAxInv="SELECT tipdoc,numdoc,fecha,tercero,almacen FROM "+nombreTabla+" WHERE estreg=? AND (idtr=? OR idtr=? OR idtr=? OR idtr=?) group by tipdoc,numdoc,fecha,tercero,almacen ";
		String cadenaConsultaAxInv="SELECT tipdoc,numdoc,fecha,tercero,almacen FROM "+nombreTabla+" WHERE estreg="+ConstantesBDInterfaz.codigoEstadoNoProcesado+" AND (idtr="+ConstantesBDInterfaz.codigoEntradaPorCompraInterafaz+" OR idtr="+ConstantesBDInterfaz.codigoSalidaDevolucionCompraInterfaz+" OR idtr="+ConstantesBDInterfaz.codigoEntradaDonacionInterfaz+" OR idtr="+ConstantesBDInterfaz.codigoSalidaDevolucionDonacionInterfaz+") group by tipdoc,numdoc,fecha,tercero,almacen ";
		
		logger.warn("\n\n Cadena Consulta TRANSACCIONES AX_INV --> \n\n"+cadenaConsultaAxInv);
		
		String restriccionesUpdate="";
		String camposActualizar="";
		String cadenaActualizacionAxInv="";
		String cadenaActualizacionArticulo="";
		String cadenaConsultaDetalleTransaccion="";
		Connection conAxioma=UtilidadBD.abrirConexion();
		
		try
		{
			Connection conInicial=abrirConexionInterfaz();
			if(conInicial==null || conInicial.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
			}
			PreparedStatementDecorator ps=new PreparedStatementDecorator(conInicial.prepareStatement(cadenaConsultaAxInv));
			/*
			ps.setInt(1, ConstantesBDInterfaz.codigoEstadoNoProcesado);
			ps.setInt(2, ConstantesBDInterfaz.codigoEntradaPorCompraInterafaz);
			ps.setInt(3, ConstantesBDInterfaz.codigoSalidaDevolucionCompraInterfaz);
			ps.setInt(4, ConstantesBDInterfaz.codigoEntradaDonacionInterfaz);
			ps.setInt(5, ConstantesBDInterfaz.codigoSalidaDevolucionDonacionInterfaz);
			*/
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int codigoInconsistencia=ConstantesBD.codigoNuncaValido;
			boolean inconsistencias=false;
			boolean validacion=false;
			
			int contador=0;
			while(rs.next())
			{
				logger.info("contador *****************************************-->"+contador);
				inconsistencias=false;
				validacion=false;
				
				restriccionesUpdate="";
				camposActualizar="";
				
				int transaccion	=	ConstantesBD.codigoNuncaValido;
				int tercero		=	ConstantesBD.codigoNuncaValido;
				int codigoAlmacen=	ConstantesBD.codigoNuncaValido;
				
				
				// VALIDAR EL ENCABEZADO DE LA TRANSACCION
				
				
				if(UtilidadTexto.isEmpty(loginUsuarioInterfaz.trim())) //VALIDAR EL LOGIN USUARIO
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.loginUsuarioNoEspecificado;
				}
				else
				{
					boolean existeUsuario = Utilidades.existeUsuario(loginUsuarioInterfaz);
					
					if(!existeUsuario)
					{
						inconsistencias=true;
						codigoInconsistencia=ConstantesIncosistenciasInterfaz.loginUsuarioNoExiste;
					}
				}
				
				
				if(UtilidadTexto.isEmpty(rs.getString("tipdoc").trim())) //VALIDAR CODIGO TRANSACCION
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.transaccionVacia;
					restriccionesUpdate+="tipdoc = '' ";
				}
				else
				{
					transaccion = Utilidades.obtenerConsecutivoDeTransaccionInv(rs.getString("tipdoc"));
					
					logger.info("------TRANSACCION -->"+transaccion);
					if(transaccion == ConstantesBD.codigoNuncaValido)
					{
						inconsistencias=true;
						codigoInconsistencia=ConstantesIncosistenciasInterfaz.transaccionNoExiste;
						restriccionesUpdate="tipdoc='"+rs.getString("tipdoc")+"' ";
					}
					else
					{
						camposActualizar+=", tiptr="+transaccion+" ";
						restriccionesUpdate="tipdoc='"+rs.getString("tipdoc")+"' ";
					}
				}
				
				if(UtilidadTexto.isEmpty(rs.getString("numdoc").trim())) //VALIDAR NUMERO DE DOCUMENTO
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.numeroDocumentoCompraVacio;
				}
				else
					restriccionesUpdate+=" AND numdoc='"+rs.getString("numdoc")+"' ";
				
				// VALIDAR (!restriccionesUpdate.equals("")?"AND":"")
				
				if(UtilidadTexto.isEmpty(rs.getString("fecha").trim())) //VALIDAR FECHA
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.fechaCreacionErronea;
					restriccionesUpdate+=" AND fecha = '' ";
				}
				else
				{
					restriccionesUpdate+=" AND fecha="+rs.getString("fecha")+" ";
				}
				
				logger.info("TERCERO DE AX_INV -->>"+rs.getString("tercero")+"<<-");
				
				if(UtilidadTexto.isEmpty(rs.getString("tercero").trim())) //VALIDAR NUMERO IDENTIFICACION DEL TERCERO
				{
					logger.info("---->>> NO SE VALIDA EL TERCERO VACIO <<<----");
					// Se modifica la validacion cuando el tercero viene vacio por tarea Xplaner 84485
					//inconsistencias=true;
					//codigoInconsistencia=ConstantesIncosistenciasInterfaz.terceroVacio;
					restriccionesUpdate+=" AND tercero = ''  ";
				}
				else
				{
					tercero = Utilidades.obtenercodigoTercerodeNumeroIdentificacion( rs.getInt("tercero")+"");
					if(tercero == ConstantesBD.codigoNuncaValido)
					{
						inconsistencias=true;
						codigoInconsistencia=ConstantesIncosistenciasInterfaz.terceroNoExiste;
					}	
					restriccionesUpdate+=" AND tercero = '"+rs.getInt("tercero")+"'  ";
				}
				
				if(UtilidadTexto.isEmpty(rs.getString("almacen").trim())) //VALIDAR ALMACEN
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.almacenVacio;
					restriccionesUpdate+=" AND almacen = ''  ";
				}
				else
				{
					codigoAlmacen = Utilidades.obtenerAlmacenDeCodigoInterfaz(rs.getString("almacen").trim());
					
					if(codigoAlmacen == ConstantesBD.codigoNuncaValido)
					{
						inconsistencias=true;
						codigoInconsistencia=ConstantesIncosistenciasInterfaz.almacenNoExiste;
					}
					restriccionesUpdate+=" AND almacen = '"+rs.getString("almacen")+"'  ";
				}
				
				boolean inconsistencias2=false;
				logger.info("incia detalle");
				if(!inconsistencias)
				{
					Connection con1=abrirConexionInterfaz();
					if(con1==null || con1.isClosed())
					{
						logger.error("CONEXION CERRADA");
						return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
					}
					// VALIDAR EL DETALLE DE LA TRANSACCION
					cadenaConsultaDetalleTransaccion="SELECT codtr,idcdon,idtr,cantidad,valoru,codsha,tipdoc,costod FROM "+nombreTabla+" WHERE numdoc='"+rs.getString("numdoc")+"' "+ (tercero>0?" and tercero='"+rs.getString("tercero")+"'":" and tercero = '' ") +" and almacen='"+rs.getString("almacen")+"' and tipdoc='"+rs.getString("tipdoc")+"'";
					logger.warn("\n\n Cadena Consulta DETALLE TRANSACCIONES AX_INV --> \n\n"+cadenaConsultaDetalleTransaccion);
					PreparedStatementDecorator psValdt=new PreparedStatementDecorator(con1.prepareStatement(cadenaConsultaDetalleTransaccion));
					HashMap mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(psValdt.executeQuery()));
					cerrarConexion(con1);
					for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
					{
						int medicamento	=	ConstantesBD.codigoNuncaValido;
						int codigoInconsistencia2=ConstantesBD.codigoNuncaValido;
						inconsistencias2=false;
						String restriccionesUpdate2="";
						String actualizarDetalle=camposActualizar;
						
						if(UtilidadTexto.isEmpty((mapa.get("CODSHA_"+i)+"").trim())) //VALIDAR CODIGO INTERFAZ ARTICULO
						{
							inconsistencias2=true;
							codigoInconsistencia2=ConstantesIncosistenciasInterfaz.articuloVacio;
							restriccionesUpdate2+=" AND codsha = ''  ";
						}
						else
						{
							medicamento = Utilidades.obtenerCodigoArticulodeCodInterfaz((mapa.get("CODSHA_"+i)+""));
							if( medicamento == ConstantesBD.codigoNuncaValido)
							{
								inconsistencias2=true;
								codigoInconsistencia2=ConstantesIncosistenciasInterfaz.articuloNoExiste;
								restriccionesUpdate2+=" AND codsha='"+(mapa.get("CODSHA_"+i)+"")+"'  ";
							}
							else
							{
								restriccionesUpdate2+=" AND codsha='"+(mapa.get("CODSHA_"+i)+"")+"'  ";
								actualizarDetalle+=", codaxi="+medicamento+" ";
							}
						}
						
						if(UtilidadTexto.isEmpty((mapa.get("IDCDON_"+i)+"").trim())) //VALIDAR TIPO COSTO DONACION
						{
							inconsistencias2=true;
							codigoInconsistencia2=ConstantesIncosistenciasInterfaz.tipoCostoDonacionVacio;
						}
						else
						{
							logger.info("\n\n 1>>>>>>>> IDCDON ->"+(mapa.get("IDCDON_"+i)+"").trim()+"<-\n");
							if(!(mapa.get("IDCDON_"+i)+"").trim().equals(ConstantesBD.acronimoSi) && !(mapa.get("IDCDON_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
							{
								inconsistencias2=true;
								codigoInconsistencia2=ConstantesIncosistenciasInterfaz.tipoCostoDonacionNoValido;
							}
						}
						
						
						if(inconsistencias2 == true)
						{
							con1=abrirConexionInterfaz();
							if(con1==null || con1.isClosed())
							{
								logger.error("CONEXION CERRADA");
								return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
							}
							validacion=true;
							cadenaActualizacionAxInv="UPDATE "+nombreTabla+" SET estreg="+codigoInconsistencia2+" WHERE "+restriccionesUpdate+restriccionesUpdate2+" ";
							logger.info("\n\n2 -->"+cadenaActualizacionAxInv+" CONTADOR:"+contador+" contint: "+i+"\n");
							PreparedStatementDecorator psConv=new PreparedStatementDecorator(con1.prepareStatement(cadenaActualizacionAxInv));
							//psConv.setInt(1, codigoInconsistencia2);
							psConv.executeUpdate();
							cerrarConexion(con1);
							logger.info("<--");
						}
						
					}
					
					if(validacion ==true)
					{
						con1=abrirConexionInterfaz();
						if(con1==null || con1.isClosed())
						{
							logger.error("CONEXION CERRADA");
							return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
						}
						cadenaActualizacionAxInv="UPDATE "+nombreTabla+" SET estreg="+ConstantesIncosistenciasInterfaz.grupodeRegistrosConInconsistencia+" WHERE  numdoc='"+rs.getString("numdoc")+"' and  tipdoc='"+rs.getString("tipdoc")+"' and  estreg="+ConstantesBDInterfaz.codigoEstadoNoProcesado+" ";
						logger.info("\n\n3 --->"+cadenaActualizacionAxInv+"\n");
						PreparedStatementDecorator psCont=new PreparedStatementDecorator(con1.prepareStatement(cadenaActualizacionAxInv));
						psCont.executeUpdate();
						cerrarConexion(con1);
					}
				}
				else
				{
					Connection conDetalle=abrirConexionInterfaz();
					if(conDetalle==null || conDetalle.isClosed())
					{
						logger.error("CONEXION CERRADA");
						return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
					}
					validacion=true;
					cadenaActualizacionAxInv="UPDATE "+nombreTabla+" SET estreg="+codigoInconsistencia+" WHERE "+restriccionesUpdate+" ";
					logger.info("\n\n 4 -->"+cadenaActualizacionAxInv+"\n");
					PreparedStatementDecorator psCone=new PreparedStatementDecorator(conDetalle.prepareStatement(cadenaActualizacionAxInv));
					psCone.executeUpdate();
					cerrarConexion(conDetalle);
				}
				
				
				
				try
				{
					logger.info("..................--------------VALIDACION: -->"+validacion);
					
					if(!validacion)
					{
						
						DtoInterfazTransacciones dto = new DtoInterfazTransacciones();
						ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
						int consecutivo=ConstantesBD.codigoNuncaValido;
						
						
						
						//--------------OBSERVAR PARAMETRIZACION PARA EL MANEJO DEL CONSECUTIVO Y TRAER VALOR------------------------------------
						String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(institucion);
						
						 if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
						 {
								consecutivo= consec.obtenerConsecutivoInventario(conAxioma,transaccion,ConstantesBD.codigoNuncaValido,institucion);
								consec.actualizarValorConsecutivoInventarios(conAxioma,transaccion,ConstantesBD.codigoNuncaValido,institucion);
						 }
				         else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
				         {
				        	 consecutivo=consec.obtenerConsecutivoInventario(conAxioma,transaccion,codigoAlmacen,institucion);
				        	 consec.actualizarValorConsecutivoInventarios(conAxioma,transaccion,codigoAlmacen,institucion);
				         }
						 //----------------------------------------------------------------------------------------------------------------------
						 
				        	   
						logger.info(" \n\n=============>>>> CONSECUTIVO INVENTARIO --->"+consecutivo+"<-\n");
						
						dto.setConsecutivo(consecutivo+"");
						dto.setTransaccion(transaccion);
						dto.setFecha_elaboracion(rs.getString("fecha")+"");
						dto.setAlmacen(codigoAlmacen);
						dto.setTipo_comprobante(rs.getString("tipdoc")+"");
						dto.setNro_comprobante(Double.parseDouble(rs.getString("numdoc")));
						dto.setEntidad(tercero+"");
						dto.setFecha_cierre(null);
						dto.setFecha_anula(null);
						dto.setUsuario(loginUsuarioInterfaz);
						dto.setUsuario_anula(null);
						dto.setUsuario_cierre(null);
						dto.setEstado(ConstantesBD.codigoTipoCierreInventarioSaldoInicialStr);
						
						int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(conAxioma, "seq_transacciones_x_almacen");
						dto.setSecuencia(secuencia);
						
						
						
						//--------------------------INSERTAR TRANSACCION POR ALMACEN ENCABEZADO -------------------------------------------------
						this.insertarTransaccionesXalmacen(conAxioma, dto);
						//-----------------------------------------------------------------------------------------------------------------------
						
						
						Connection con2=abrirConexionInterfaz();
						if(con2==null || con2.isClosed())
						{
							logger.error("CONEXION CERRADA");
							return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
						}
						cadenaConsultaDetalleTransaccion="SELECT codtr,idcdon,idtr,cantidad,valoru,codsha,tipdoc,costod FROM "+nombreTabla+" WHERE numdoc='"+rs.getString("numdoc")+"' "+ (tercero>0?" and tercero='"+rs.getString("tercero")+"'":" and tercero = '' ") +" and almacen='"+rs.getString("almacen")+"' and tipdoc='"+rs.getString("tipdoc")+"'";
						logger.info("-->"+cadenaConsultaDetalleTransaccion);
						PreparedStatementDecorator psDetTr=new PreparedStatementDecorator(con2.prepareStatement(cadenaConsultaDetalleTransaccion));
						ResultSetDecorator rsDetTr=new ResultSetDecorator(psDetTr.executeQuery());
						int contaDetall=0;
						while(rsDetTr.next())
						{
							int medicamento	=	ConstantesBD.codigoNuncaValido;
							int codigoInconsistencia2=ConstantesBD.codigoNuncaValido;
							String restriccionesUpdate2="";
							String actualizarDetalle=camposActualizar;
							
							if(UtilidadTexto.isEmpty(rsDetTr.getString("codsha"))) //VALIDAR CODIGO INTERFAZ ARTICULO
							{
								codigoInconsistencia2=ConstantesIncosistenciasInterfaz.articuloVacio;
								restriccionesUpdate2+=" AND codsha=''  ";
							}
							else
							{
								medicamento = Utilidades.obtenerCodigoArticulodeCodInterfaz(rsDetTr.getString("codsha"));
								if( medicamento == ConstantesBD.codigoNuncaValido)
								{
									codigoInconsistencia2=ConstantesIncosistenciasInterfaz.articuloNoExiste;
									restriccionesUpdate2+=" AND codsha='"+rsDetTr.getString("codsha")+"'  ";
								}
								else
								{
									restriccionesUpdate2+=" AND codsha='"+rsDetTr.getString("codsha").trim()+"'  ";
									actualizarDetalle+=", codaxi="+medicamento+" ";
								}
							}
							
							boolean cantidadSumar=false;
							DtoInterfazDetTransacciones dtoDetalle = new DtoInterfazDetTransacciones();
							
							dtoDetalle.setTransaccion(secuencia+"");
							dtoDetalle.setArticulo(medicamento);
							dtoDetalle.setCantidad(rsDetTr.getInt("cantidad"));
							dtoDetalle.setVal_unitario(rsDetTr.getDouble("valoru"));
							
							logger.info("MEDICAMENTO --->"+medicamento);
							
							
							//-------------------------------ACTUALIZAR COSTO PROMEDIO Y COSTO DONACION ------------------------------------------
							if(rsDetTr.getString("idcdon").equals(ConstantesBD.acronimoSi))
							{
								logger.info("\n\n==>> ACTUALIZAR EL COSTO DONACION PARA EL ARTICULO -->"+medicamento+" con el valor ->"+rsDetTr.getDouble("costod")+"<-\n");
								cadenaActualizacionArticulo="UPDATE articulo SET costo_donacion='"+rsDetTr.getDouble("costod")+"' WHERE codigo="+medicamento;
								PreparedStatementDecorator psArt=new PreparedStatementDecorator(conAxioma.prepareStatement(cadenaActualizacionArticulo));
								psArt.executeUpdate();
							}
							else
							{
								if(rsDetTr.getString("idcdon").equals(ConstantesBD.acronimoNo)) 
								{
//									Double calculoCostoPromedio = UtilidadInventarios.calcularCostoPromedioArticulo(conAxioma, medicamento, dtoDetalle.getCantidad(), (dtoDetalle.getVal_unitario()*dtoDetalle.getCantidad()), obtenerTipoConceptoTransaccion(dto.getTransaccion()), institucion);
//									logger.info("\n\n===>> CALCULO COSTO PROMEDIO -->"+calculoCostoPromedio+"<--\n");
//									UtilidadInventarios.actualizarCostoPromedioArticulo(conAxioma, medicamento, calculoCostoPromedio);
								}
							}
							//-------------------------------------------------------------------------------------------------------------------
							
							
							
							// -------------------------INSERTAR DETALLE DE LA TRANSACCION POR ALMACEN-------------------------------------------
							this.insertarDetTransaccionesXalmacen(conAxioma, dtoDetalle);
							//-------------------------------------------------------------------------------------------------------------------
							
							
							
							
							// ------------------------ACTUALIZAR EL NUMERO DE EXISTENCIAS POR ALMACEN-------------------------------------------
							int tipoTrShaio=consultarHomologacion(rsDetTr.getInt("idtr"));
	
							if(tipoTrShaio!=ConstantesBD.codigoNuncaValido)
							{
								if(tipoTrShaio==1)
								{
									cantidadSumar=true;
								}
								else
								{
									cantidadSumar=false;
								}
								UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(conAxioma,medicamento, codigoAlmacen, cantidadSumar, dtoDetalle.getCantidad(), institucion, "2");
							}
							else
							{
								logger.info("--------NO EXISTE HOMOLOGACION DEL TIPO DE TRANSACCION----->>> "+rsDetTr.getInt("idtr"));
							}
							// ------------------------------------------------------------------------------------------------------------------
							
							
							
							
							// ----------------ACTUALIZAR PRECIO ULTIMA COMPRA y COMPRA MAS ALTA PARA EL ARTICULO-------------------------------
							UtilidadInventarios.actualizarPrecioUltimaCompra(conAxioma, medicamento, dtoDetalle.getVal_unitario());
							Double precioCompraActual = UtilidadInventarios.obtenerValorCompraMasAlta(conAxioma, medicamento);
							
							logger.info("===================================================================");
							logger.info(">>>>>>>>> ARTICULO ------------------------->"+medicamento+"<-");
							logger.info(">>>>>>>>> PRECIO DE COMPRA MAS ALTA ACTUAL ->"+precioCompraActual+"<-");
							logger.info(">>>>>>>>> PRECIO DE COMPRA NUEVO ----------->"+dtoDetalle.getVal_unitario()+"<-");
							logger.info("===================================================================");
							
							if(dtoDetalle.getVal_unitario() > precioCompraActual)
							{
								logger.info(">>>>>>> SE ACTUALIZA EL PRECIO COMPRA MAS ALTA PARA EL ARTICULO ->"+medicamento+"<-");
								UtilidadInventarios.actualizarPrecioCompraMasAlta(conAxioma, medicamento, dtoDetalle.getVal_unitario());
							}
							//------------------------------------------------------------------------------------------------------------------
							
							
							
							// --------------------------ACTUALIZAR EL REGISTRO CON MARCA PROCESADO SATISFACTORIAMENTE---------------------------
							cadenaActualizacionAxInv="UPDATE "+nombreTabla+" SET numtr="+consecutivo+", estreg="+ConstantesBDInterfaz.codigoEstadoProcesado+""+actualizarDetalle+" WHERE "+restriccionesUpdate+restriccionesUpdate2+"";
							logger.info("\nCADENA ACTUALIZAR REGISTROS AX_INV --->\n\n"+cadenaActualizacionAxInv+"\n\n");
							PreparedStatementDecorator psInv=new PreparedStatementDecorator(con2.prepareStatement(cadenaActualizacionAxInv));
							//psInv.setInt(1, ConstantesBDInterfaz.codigoEstadoProcesado);
							psInv.executeUpdate();
							//-------------------------------------------------------------------------------------------------------------------
					
							contaDetall++;
							logger.info("\n\n++++++++++++++++++++++++++++++++++++++++++++++++ SE HA PROCESADO EL REGISTRO "+contador+" DETALLE "+contaDetall+"++++++++++++++++++++++++++++++++\n\n");
						}

						cerrarConexion(con2);
					}
				}
				catch (SQLException e) {
					logger.error("ERROR CARGANDO TRANSACCIONES: "+e);
					e.printStackTrace();
					
				}
				contador++;				
			}
			logger.info("              SE PROCESARON "+contador+" TRANSACCIONES");
			ps.close();
			cerrarConexion(conInicial);
			UtilidadBD.closeConnection(conAxioma);
			return new ResultadoBoolean(true,"PROCESO FINALIZADO");
		}
		catch (Exception e) {
			logger.error("ERROR CARGANDO TRANSACCIONES: "+e);
			e.printStackTrace();
			UtilidadBD.closeConnection(conAxioma);
			return new ResultadoBoolean(false,"SE PRODUJO UNA EXCEPCION: "+e);
		}
	}
	
	/**
	 * 	Consultar el Tipo Concepto de una transaccion 
	 * @param transaccion
	 * @return
	 */
	private int obtenerTipoConceptoTransaccion(int consecutivoTransaccion) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String consultaTipoConceptoStr = "SELECT tipos_conceptos_inv from tipos_trans_inventarios where consecutivo =?";
		
		PreparedStatementDecorator ps;
        try
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(consultaTipoConceptoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, consecutivoTransaccion);
            ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            {
            	int tipoConcepto =rs.getInt("tipos_conceptos_inv"); 
            	logger.info("\n\n >>>>>> TIPO CONCEPTO INVENTARIO -->"+tipoConcepto+"<- para el consecutivo de transaccion ->"+consecutivoTransaccion+"<- \n");
            	cerrarConexion(con);
            	return tipoConcepto;
            }
            	
        }
        catch (SQLException e)
        {
        	cerrarConexion(con);
        	logger.info("\n Problema obtenerTipoConceptoTransaccion "+e);
        }
        
        cerrarConexion(con);
        return ConstantesBD.codigoNuncaValido;
		
	}

	/**
	 * Metodo para consultar si el codigo shaio se encuentra en la tabla articulo de axioma
	 * @param codSha
	 * @return
	 */
	public int consultarArticulo(String codSha)
	{
		Connection con=UtilidadBD.abrirConexion();
		String cadenaConsultaArticulo="SELECT codigo FROM articulo WHERE codigo_interfaz='"+codSha.trim()+"'";
		
		if(!UtilidadTexto.isEmpty(codSha))
		{
			try
			{
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA");
					return ConstantesBD.codigoNuncaValido;
				}
				PreparedStatementDecorator psArt= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaArticulo));
				ResultSetDecorator rsArt=new ResultSetDecorator(psArt.executeQuery());
				
				if(rsArt.next())
				{
					int articulo=rsArt.getInt("codigo");
					cerrarConexion(con);
					return articulo;
				}
			}
			catch (SQLException e) {
				logger.error("ERROR CARGANDO ARTICULO: "+e);
				cerrarConexion(con);
				
			}
		}
		cerrarConexion(con);
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo para actualizar el codigo interfaz de articulo con el codigo shaio de la interfaz ax_art
	 * Modificado por el anexo 779
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean cargarAxArtToArticulo(int institucion)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaArticulos,institucion);
		String cadenaConsultaAxArt="SELECT aa.codsha as codsha, aa.codaxi as codaxi FROM "+nombreTabla+" aa WHERE estreg="+ConstantesBDInterfaz.codigoEstadoProcesado;
		String restriccionesUpdate="";
		String cadenaActualizacionAxArt="";
		String cadenaActualizacionArticulo="";
		//se habre conexion de la interfaz
		Connection con=abrirConexionInterfaz();
		
		//se habre conexion con axioma
		Connection conAxioma=UtilidadBD.abrirConexion();
		try
		{
			//si la conexion con la interfaz fallo, se trabaja con la conexion a axioma
			if(con==null || con.isClosed())
			{
				//la conexion de la interfaz se hace a axioma.
				con=conAxioma;
				
				if(con==null || con.isClosed())
				{
					logger.error("CONEXION CERRADA");
					return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
				}
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAxArt));
			//ps.setInt(1, ConstantesBDInterfaz.codigoEstadoProcesado);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int codigoInconsistencia=ConstantesBD.codigoNuncaValido;
			boolean inconsistencias=false;
			int contador=0;
			while(rs.next())
			{
				restriccionesUpdate="";
				if(rs.getObject("codaxi")==null)
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.articuloVacio;
					restriccionesUpdate+=" AND codaxi = '' ";
				}
				else
				{
					restriccionesUpdate+=" AND codaxi="+rs.getObject("codaxi")+" ";
				}
				if(UtilidadTexto.isEmpty(rs.getString("codsha")))
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.articuloVacio;
					restriccionesUpdate+=" AND codsha = '' ";
				}
				else
				{
					restriccionesUpdate+=" AND codsha='"+rs.getString("codsha")+"' ";
				}
				try
				{
					if(!inconsistencias)
					{
						cadenaActualizacionArticulo="UPDATE articulo SET codigo_interfaz='"+rs.getString("codsha")+"' WHERE codigo="+rs.getString("codaxi");
						PreparedStatementDecorator psArt=new PreparedStatementDecorator(conAxioma.prepareStatement(cadenaActualizacionArticulo));
						psArt.executeUpdate();
						cadenaActualizacionAxArt="UPDATE "+nombreTabla+" SET estreg="+ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+" WHERE 1=1 "+restriccionesUpdate;
						PreparedStatementDecorator psCon= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAxArt));
						//psCon.setInt(1, ConstantesBDInterfaz.codigoEstadoProcesadoAxioma);
						psCon.executeUpdate();
					}
					else
					{
						cadenaActualizacionAxArt="UPDATE "+nombreTabla+" SET estreg="+codigoInconsistencia+" WHERE 1=1 "+restriccionesUpdate;
						PreparedStatementDecorator psCon= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAxArt));
						//psCon.setInt(1, codigoInconsistencia);
						psCon.executeUpdate();
					}
				}
				catch (SQLException e) {
					logger.error("ERROR CARGANDO EL ARTICULO: "+e);
					
				}
				contador++;
			}
			logger.info("              SE PROCESARON "+contador+" ARTICULOS");
			ps.close();
			cerrarConexion(conAxioma);
			con.close();
			return new ResultadoBoolean(true,"PROCESO FINALIZADO");
		}
		catch (Exception e) {
			logger.info("\n Problema cargarAxArtToArticulo interfaz"+e);
			cerrarConexion(conAxioma);
			return new ResultadoBoolean(false,"SE PRODUJO UNA EXCEPCION: "+e);
		}
	}
	 
	/**
	 * Metodo para cargar los convenios x proveedor desde Interfaz inventarios hasta Axioma
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean cargarAxConProvToConveniosProveedorAxioma(int institucion)
	{
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaConvenios,institucion);
		
		String cadenaConsultaAxConProv="SELECT convtip, convnum, convnit, convcods, convcoda, convcant,"+
										" convcant2, convvalu, convvali, convfec, convhor, convest, tercero FROM "+nombreTabla+" WHERE convest="+ConstantesBDInterfaz.codigoEstadoNoProcesado;
		String restriccionesUpdate="";
		String cadenaActualizacionAxConProv="";
		Connection con=abrirConexionInterfaz();
		try
		{
			if(con==null || con.isClosed())
			{
				return new ResultadoBoolean(false,"LA CONEXION ESTA CERRADA");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAxConProv));
			//ps.setInt(1, ConstantesBDInterfaz.codigoEstadoNoProcesado);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			int codigoInconsistencia=ConstantesBD.codigoNuncaValido;
			boolean inconsistencias=false;
			int contador=0;
			int codArt=ConstantesBD.codigoNuncaValido;
			
			while(rs.next())
			{
				inconsistencias=false;
				restriccionesUpdate="";
				codigoInconsistencia=ConstantesBD.codigoNuncaValido;
				codArt=this.consultarArticulo(rs.getString("convcods"));
				
				if(UtilidadTexto.isEmpty(rs.getString("convnit").trim())) //VALIDAR EL CONVENIO
				{
					inconsistencias=true;
					logger.info("inconsistencia convnit-->"+rs.getString("convnit"));
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.terceroShaioVacio;
				}
				else
				{
					restriccionesUpdate+=" convnit='"+rs.getString("convnit").trim()+"' ";
				}
				
				if(UtilidadTexto.isEmpty(rs.getString("tercero").trim())) // VALIDAR EL NUMERO IDENTIFICACION DEL TERCERO
				{
					inconsistencias=true;
					logger.info("inconsistencia tercero-->"+rs.getString("tercero"));
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.terceroVacio;
				}
				else
				{
					int tercero = Utilidades.obtenercodigoTercerodeNumeroIdentificacion( rs.getInt("tercero")+"");
					if( tercero == ConstantesBD.codigoNuncaValido)
					{
						inconsistencias=true;
						codigoInconsistencia=ConstantesIncosistenciasInterfaz.terceroNoExiste;
					}
					else
					{
						restriccionesUpdate+=" AND tercero='"+rs.getInt("tercero")+"' ";
					}
					
				}
				
				if(UtilidadTexto.isEmpty(rs.getString("convcods").trim()))  // VALIDAR EL CODIGO SHAIO DEL ARTICULO
				{
					inconsistencias=true;
					logger.info("inconsistencia convcods-->"+rs.getString("convcods"));
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.articuloVacio;
				}
				else
				{
					
					restriccionesUpdate+=" AND convcods='"+rs.getObject("convcods")+"' ";
					
				}
				
				
				if(UtilidadTexto.isEmpty(rs.getString("convfec").trim())) // VALIDAR FECHA CONVENIO
				{
					inconsistencias=true;
					logger.info("inconsistencia convfec-->"+rs.getString("convfec"));
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.fechaCreacionErronea;
				}
				else
				{
					restriccionesUpdate+=" AND convfec="+rs.getObject("convfec")+" ";
				}
				
				if(UtilidadTexto.isEmpty(rs.getString("convhor").trim())) // VALIDAR HORA CONVENIO
				{
					inconsistencias=true;
					logger.info("inconsistencia convhor-->"+rs.getString("convhor"));
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.horaCreacionErronea;	
				}
				else
				{
					restriccionesUpdate+=" AND convhor="+rs.getObject("convhor")+"";
				}
				
				if(codArt<=0)
				{
					inconsistencias=true;
					codigoInconsistencia=ConstantesIncosistenciasInterfaz.articuloNoExiste;
				}
				
				try
				{
					if(!inconsistencias)
					{
						int convenioExiste = this.consultarConvenioProveedor(rs.getString("convnum").trim(), rs.getString("convcods").trim());
						
						if(convenioExiste>0)
						{
							this.modificarConveniosProveedor(new DtoInterfazInventarios(rs.getString("convtip")+"",rs.getString("convnum")+"",rs.getInt("tercero")+"",rs.getString("convcods")+"",codArt,rs.getObject("convcant"),rs.getObject("convcant2"),rs.getObject("convvalu"),rs.getObject("convvali"),rs.getString("convfec")+"",rs.getString("convhor")+"",true+""));
						}
						else
						{
							this.insertarConveniosProveedor(new DtoInterfazInventarios(rs.getString("convtip")+"",rs.getString("convnum")+"",rs.getInt("tercero")+"",rs.getString("convcods")+"",codArt,rs.getObject("convcant"),rs.getObject("convcant2"),rs.getObject("convvalu"),rs.getObject("convvali"),rs.getString("convfec")+"",rs.getString("convhor")+"",true+""));
						}
						cadenaActualizacionAxConProv="UPDATE "+nombreTabla+" SET convcoda="+codArt+", convest="+ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+" WHERE "+restriccionesUpdate;
						PreparedStatementDecorator psCon= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAxConProv));
						//psCon.setInt(1, ConstantesBDInterfaz.codigoEstadoProcesadoAxioma);
						psCon.executeUpdate();
					}
					else
					{
						cadenaActualizacionAxConProv="UPDATE "+nombreTabla+" SET convest="+codigoInconsistencia+" WHERE "+restriccionesUpdate;
						PreparedStatementDecorator psCon= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAxConProv));
						//psCon.setInt(1, codigoInconsistencia);
						psCon.executeUpdate();
						
						
					}
				}
				catch (SQLException e) {
					logger.info("\n Problema cargarAxConProvToConveniosProveedorAxioma "+e);
				}
				contador++;
			}
			logger.info("              SE PROCESARON "+contador+" CONVENIOS X PROVEEDOR");
			ps.close();
			cerrarConexion(con);
			return new ResultadoBoolean(true,"PROCESO FINALIZADO");
		}
		catch (Exception e) {
			logger.info("\n Problema cargarAxConProvToConveniosProveedorAxioma "+e);
			cerrarConexion(con);
			return new ResultadoBoolean(false,"SE PRODUJO UNA EXCEPCION: "+e);
		}
	}
	
	/**
	 * Metodo para Modificar un Convenio Proveedor que ya existe en Axioma.
	 * @param dto
	 * @return
	 */
	public boolean modificarConveniosProveedor(DtoInterfazInventarios dto)
	{
		Connection con=UtilidadBD.abrirConexion();
		String cadenaActualizacion= "UPDATE convenio_proveedor SET tipo_convenio= '"+dto.getTipo_convenio()+"', " +
														"proveedor='"+dto.getProveedor()+"', " +
														"cantidad_convenio='"+dto.getCantidad_convenio()+"', " +
														"cantidad_recibida='"+dto.getCantidad_recibida()+"', " +
														"val_uni_compra='"+dto.getVal_uni_compra()+"', " +
														"val_uni_iva='"+dto.getVal_uni_iva()+"', " +
														"fecha='"+dto.getFecha()+"', " +
														"hora='"+dto.getHora()+"', " +
														"estado_registro='"+dto.getEstado_registro()+"' " +
														"WHERE numero_convenio='"+dto.getNumero_convenio()+"' " +
														"AND codigo_shaio='"+dto.getCodigo_shaio().trim()+"'";

		try
		{
			if(con==null || con.isClosed())
			{
				logger.error("CONEXION CERRADA");
				return false;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacion));
			/*
			logger.info("1. dto.getTipo_convenio() -->"+dto.getTipo_convenio());
			logger.info("2. dto.getNumero_convenio() -->"+dto.getNumero_convenio());
			logger.info("3. dto.getProveedor() -->"+dto.getProveedor());
			logger.info("4. dto.getCodigo_shaio() -->"+dto.getCodigo_shaio());
			logger.info("5. dto.getCodigo_axioma() -->"+dto.getCodigo_axioma());
			logger.info("6. dto.getCantidad_convenio() -->"+dto.getCantidad_convenio());
			logger.info("7. dto.getCantidad_recibida() -->"+dto.getCantidad_recibida());
			logger.info("8. dto.getVal_uni_compra() -->"+dto.getVal_uni_compra());
			logger.info("9. dto.getVal_uni_iva() -->"+dto.getVal_uni_iva());
			logger.info("10.dto.getFecha() -->"+dto.getFecha());
			logger.info("11.dto.getHora() -->"+dto.getHora());
			logger.info("12.dto.getEstado_registro() -->"+dto.getEstado_registro());
			
			logger.info("CADENA ACTUALIZACION -->"+cadenaActualizacion);
			*/
			
			int regInsertados=ps.executeUpdate();
			
			if(regInsertados>0)
			{
				cerrarConexion(con);
				return true;
			}
			ps.close();
			cerrarConexion(con);
			return false;
		}
		catch(Exception e)
		{
			logger.info("\n problema modificarConveniosProveedor interfaz "+e);
			cerrarConexion(con);
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	private int consultarConvenioProveedor(String numeroConvenio, String codigoShaioArti) 
	{
		Connection con=UtilidadBD.abrirConexion();
		
		String consultaConvenio ="SELECT count(numero_convenio) as exite from convenio_proveedor where numero_convenio=? and codigo_shaio=?";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConvenio, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroConvenio);
			ps.setString(2, codigoShaioArti.trim());
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				UtilidadBD.cerrarConexion(con);
				return rs.getInt("exite");
			}
		}
		catch (SQLException e) 
		{
			logger.info("\n Problema consultarConvenioProveedor "+e);
		}
		
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e1) 
		{
			logger.info("\n Problema consultarConvenioProveedor "+e1);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Metodo encargado de consultar la informacion de la tabla ax_inv 
	 * que esta en la Bd local.
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoInterfazTransaccionAxInv>consultaDatosResprocesoInventarios (int institucion)
	{
		logger.info("\n **** consultaDatosResprocesoInventarios ***");
		
		ArrayList<DtoInterfazTransaccionAxInv> arrayDtos = new ArrayList<DtoInterfazTransaccionAxInv>();
		String consulta = "SELECT tiptr,numtr,tipdoc,numdoc,codtr,idtr,origtr,idcdon,almacen,almacen2," +
				" fechatrn,horatrn,idprov,tercero,codsha,codaxi,cantidad,valoru,valori,costod,codpac," +
				" ingpac,cospdt,cosedt,valulc,valcma,estreg,usuario,fecha,hora,tiperr,consecutivo  from  ax_inv";
		
		
		Connection connection = UtilidadBD.abrirConexion();
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.setInt(1, institucion);//este queda pensiente por si piden que este proceso se deba de hacer por institucion

			logger.info("\n\n*******************************************************************************************");
			logger.info(" consulta --->"+ consulta);
			logger.info("\n*******************************************************************************************\n\n");

			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoInterfazTransaccionAxInv dto = new DtoInterfazTransaccionAxInv();

					dto.setTipoTransAxioma(rs.getString("tiptr"));//1
					dto.setNumeroTransaccionAxioma(rs.getString("numtr"));//2
					dto.setTipoDocumento(rs.getString("tipdoc"));//3
					dto.setNumeroDocumento(rs.getString("numdoc"));//4
					dto.setCodigoTransaccion(rs.getString("codtr"));//5
					dto.setIndicativoTransaccion(rs.getString("idtr"));//6
					dto.setOrigenTransaccion(rs.getString("origtr"));//7
					dto.setIndicativoCostoDonacion(rs.getString("idcdon"));//8
					dto.setAlmacenSolicita(rs.getString("almacen"));//9
					dto.setAlmacendespacha(rs.getString("almacen2"));//10
					dto.setFechaTransaccion(rs.getString("fechatrn"));//11
					dto.setHoraTransaccion(rs.getString("horatrn"));//12
					dto.setIdentificacionProveedor(rs.getString("idprov"));//13
					dto.setTercero(rs.getString("tercero"));//14
					dto.setCodigoArticuloInterfaz(rs.getString("codsha"));//15
					dto.setCodigoArticulo(rs.getString("codaxi"));//16
					dto.setCantidad(rs.getString("cantidad"));//17
					dto.setValorUnitario(rs.getString("valoru"));//18
					dto.setValorIva(rs.getString("valori"));//19
					dto.setCostoDonacion(rs.getString("costod"));//20
					dto.setCodigoPaciente(rs.getString("codpac"));//21
					dto.setIngresoPaciente(rs.getString("ingpac"));//22
					dto.setCentroCostoOrdena(rs.getString("cospdt"));//23
					dto.setCentroCostoEjecuta(rs.getString("cosedt"));//24
					dto.setValorUltimaCompra(rs.getString("valulc"));//25
					dto.setValorCompraMasAlta(rs.getString("valcma"));//26
					dto.setEstadoRegistro(rs.getString("estreg"));//27
					dto.setUsuario(rs.getString("usuario"));//28
					dto.setFechaRegistro(rs.getString("fecha"));//29
					dto.setHoraRegistro(rs.getString("hora"));//30
					dto.setConsecutivo(rs.getString("consecutivo"));//31
			
				arrayDtos.add(dto);
			}
			rs.close();
			
		} catch (SQLException e) 
		{
			logger.warn(" Error en consultaDatosResprocesoInventarios "+e);
		}
		
		UtilidadBD.closeConnection(connection);
		return arrayDtos;
		
	}
	
	
	
	/**
	 * Metodo de insertar los diferentes registros de registros en ax_inv remota
	 * @param arrayDtoInv
	 * @param institucion
	 * @param isReproceso
	 */
	public void insertarInterfazTransaccionInvReproceso (ArrayList<DtoInterfazTransaccionAxInv> arrayDtoInv,int institucion,boolean isReproceso)
	{
		for (int i=0;i<arrayDtoInv.size();i++)
			insertarTransaccionInterfaz(arrayDtoInv.get(i), institucion, isReproceso);
	}
	
	/**
	 * Metodo encargado de insertar los datos en la interfaz de inventarios
	 * Modificado por el anexo 779
	 * @param dto
	 * @param institucion       ---
	 */
	public ResultadoInteger insertarTransaccionInterfaz(DtoInterfazTransaccionAxInv dto, int institucion,boolean isReproceso) 
	{
		int resp = 0;
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaTransacciones,institucion);

		
		Connection con=abrirConexionInterfaz();
		String column="", valor="";
		boolean tmp=true;
		try {
			if (con==null || con.isClosed()) 
			{
				if (!isReproceso)
				{
					con=UtilidadBD.abrirConexion();
					tmp=false;
					column=",tiperr,consecutivo";
					valor=",'"+ConstantesIntegridadDominio.acronimoNoConexion+"',"+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ax_inv");
				}
				else
					return new ResultadoInteger(ConstantesBD.codigoNuncaValido,"");
				
			}
		} catch (SQLException e1) {
			
		}
		
		String cadenaConsultaAxInv="insert into "+nombreTabla+" " +
																"(tiptr,numtr,tipdoc,numdoc,codtr," +
																"idtr,origtr,idcdon,almacen,idprov," +
																"codsha,codaxi,cantidad,valoru,valori," +
																"costod,codpac,ingpac,fecha,hora," +
																"estreg,fechatrn,horatrn, cospdt, cosedt, " +
																"tercero, almacen2, usuario"+column+" ) " +
															"VALUES (" +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?, ?"+valor+")"; //28
		
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAxInv));
			
			logger.info("\n cadena ->"+cadenaConsultaAxInv);
			
			logger.info("dto.getTipoTransAxioma()->"+dto.getTipoTransAxioma()+"<-");
			
			
			if(UtilidadTexto.isEmpty(dto.getTipoTransAxioma()))
				ps.setInt(1, 0000000000);
			else
				ps.setObject(1, obtenerValor(dto.getTipoTransAxioma()));
			
			logger.info("dto.getNumeroTransaccionAxioma()->"+dto.getNumeroTransaccionAxioma()+"<-");
			if(UtilidadTexto.isEmpty(dto.getNumeroTransaccionAxioma()))
				ps.setObject(2, 0000000000);
			else
				ps.setObject(2, obtenerValor(dto.getNumeroTransaccionAxioma()));
			
			logger.info("dto.getTipoDocumento()->"+dto.getTipoDocumento()+"<-");
			if(UtilidadTexto.isEmpty(dto.getTipoDocumento()))
				ps.setObject(3, "  ");
			else
				ps.setObject(3, obtenerValor(dto.getTipoDocumento()));
			
			logger.info("dto.getNumeroDocumento()->"+dto.getNumeroDocumento()+"<-");
			if(UtilidadTexto.isEmpty(dto.getNumeroDocumento()))
				ps.setObject(4, "        ");
			else
				ps.setObject(4, obtenerValor(dto.getNumeroDocumento()));
			
			logger.info("dto.getCodigoTransaccion()->"+dto.getCodigoTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoTransaccion()))
				ps.setObject(5, "   ");
			else
				ps.setObject(5, obtenerValor(dto.getCodigoTransaccion()));
			
			logger.info("dto.getIndicativoTransaccion()->"+dto.getIndicativoTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIndicativoTransaccion()))
				ps.setObject(6, 00);
			else
				ps.setObject(6, obtenerValor(dto.getIndicativoTransaccion()));
			
			logger.info("dto.getOrigenTransaccion()->"+dto.getOrigenTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getOrigenTransaccion()))
				ps.setObject(7, " ");
			else
				ps.setObject(7, obtenerValor(dto.getOrigenTransaccion()));
			
			logger.info("dto.getIndicativoCostoDonacion()->"+dto.getIndicativoCostoDonacion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIndicativoCostoDonacion()))
				ps.setObject(8, 0);
			else
				ps.setObject(8, obtenerValor(dto.getIndicativoCostoDonacion()));
			
			logger.info("dto.getAlmacenInterfaz()->"+dto.getAlmacenInterfaz()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacenInterfaz()))
				ps.setObject(9, " ");
			else 
				ps.setObject(9, Utilidades.obtenerCodigoInterfazParametroAlmacen(Utilidades.convertirAEntero(dto.getAlmacenInterfaz())));
			
			logger.info("dto.getIdentificacionProveedor()->"+agregarCeros(dto.getIdentificacionProveedor().trim(), 13)+dto.getIdentificacionProveedor().trim()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIdentificacionProveedor()))
				ps.setObject(10, "             ");
			else
				ps.setObject(10, obtenerValor(agregarCeros(dto.getIdentificacionProveedor().trim(), 13)+dto.getIdentificacionProveedor().trim()));
			
			logger.info("dto.getCodigoArticuloInterfaz()->"+dto.getCodigoArticuloInterfaz()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoArticuloInterfaz()))
				ps.setObject(11, "           ");
			else
				ps.setObject(11, obtenerValor(dto.getCodigoArticuloInterfaz()));
			
			logger.info("dto.getCodigoArticulo()->"+dto.getCodigoArticulo()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoArticulo()))
				ps.setObject(12, 0000000000);
			else
				ps.setObject(12, obtenerValor(dto.getCodigoArticulo()));
			
			logger.info("dto.getCantidad()->"+dto.getCantidad()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCantidad()))
				ps.setObject(13, 0000000000000);
			else
				ps.setObject(13, obtenerValor(dto.getCantidad()));
			
			logger.info("dto.getValorUnitario()->"+dto.getValorUnitario()+"<-");
			if(UtilidadTexto.isEmpty(dto.getValorUnitario()))
				ps.setObject(14, 000000000000000);
			else
				ps.setObject(14, obtenerValor(dto.getValorUnitario()));
			
			logger.info("dto.getValorIva()->"+dto.getValorIva()+"<-");
			if(UtilidadTexto.isEmpty(dto.getValorIva()))
				ps.setObject(15, 000000000000000);
			else
				ps.setObject(15, obtenerValor(dto.getValorIva()));
			
			logger.info("dto.getCostoDonacion()->"+dto.getCostoDonacion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCostoDonacion()))
				ps.setObject(16, 000000000000000);
			else
				ps.setObject(16, obtenerValor(dto.getCostoDonacion()));
			
			logger.info("dto.getCodigoPaciente()->"+dto.getCodigoPaciente()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoPaciente()))
				ps.setObject(17, 0000000000);
			else
				ps.setObject(17, obtenerValor(dto.getCodigoPaciente()));
			
			logger.info("dto.getIngresoPaciente()->"+dto.getIngresoPaciente()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIngresoPaciente()))
				ps.setObject(18, 0000000000);
			else
				ps.setObject(18, obtenerValor(dto.getIngresoPaciente()));
			
			logger.info("dto.getFechaRegistro()->"+dto.getFechaRegistro()+"<-");
			if(UtilidadTexto.isEmpty(dto.getFechaRegistro()))
				ps.setObject(19, 00000000);
			else
				ps.setObject(19, obtenerValor(dto.getFechaRegistro().replace("-", "")));
			
			logger.info("dto.getHoraRegistro()->"+dto.getHoraRegistro().replace(":", "")+agregarCeros(dto.getHoraRegistro().replace(":", "").trim(), 6)+"<-");
			if(UtilidadTexto.isEmpty(dto.getHoraRegistro()))
				ps.setObject(20, 000000);
			else
				ps.setObject(20, obtenerValor(dto.getHoraRegistro().replace(":", "")+agregarCeros(dto.getHoraRegistro().replace(":", "").trim(), 6)));
			
			logger.info("dto.getEstadoRegistro()->"+dto.getEstadoRegistro()+"<-");
			if(UtilidadTexto.isEmpty(dto.getEstadoRegistro()))
				ps.setObject(21, 000);
			else
				ps.setObject(21, obtenerValor(dto.getEstadoRegistro()));
			
			logger.info("dto.getFechaTransaccion()->"+dto.getFechaTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getFechaTransaccion()))
				ps.setObject(22, 00000000);
			else
				ps.setObject(22, obtenerValor(dto.getFechaTransaccion().replace("-", "")));
			
			logger.info("dto.getHoraTransaccion()->"+dto.getHoraTransaccion().replace(":", "")+agregarCeros(dto.getHoraTransaccion().replace(":", "").trim(), 6)+"<-");
			if(UtilidadTexto.isEmpty(dto.getHoraTransaccion()))
				ps.setObject(23, 000000);
			else
				ps.setObject(23, obtenerValor(dto.getHoraTransaccion().replace(":", "")+agregarCeros(dto.getHoraTransaccion().replace(":", "").trim(), 6)));
			
			//ADICION CAMPO COSPDT ---> Centro de Costo que Solicita
			logger.info("dto.getAlmacenSolicita()->"+Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacenSolicita())).trim()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacenSolicita()))
				ps.setObject(24, " ");
			else 
				ps.setString(24, Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacenSolicita())).trim());
			
			//ADICION CAMPO COSEDT ----> Centro de Costo que Ejecuta
			logger.info("dto.getAlmacendespacha()->"+Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacendespacha())).trim()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacendespacha()))
				ps.setObject(25, " ");
			else 
				ps.setString(25, Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacendespacha())).trim());
			
			
			logger.info("dto.getTercero()->"+dto.getIdentificacionProveedor()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIdentificacionProveedor()))
				ps.setObject(26, "             ");
			else
				ps.setObject(26, obtenerValor(dto.getIdentificacionProveedor()));
			
			
			logger.info("dto.getAlmacenConsignacion()->"+dto.getAlmacenConsignacion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacenConsignacion()))
				ps.setObject(27, " ");
			else 
				ps.setObject(27, Utilidades.obtenerCodigoInterfazParametroAlmacen(Utilidades.convertirAEntero(dto.getAlmacenConsignacion())));
			
			
			logger.info("dto.getUsuario()->"+dto.getUsuario()+"<-");
			if(UtilidadTexto.isEmpty(dto.getUsuario()))
				ps.setObject(28, " ");
			else 
				ps.setString(28, dto.getUsuario());
			
			
			resp = ps.executeUpdate();
			
			if(resp>0)
			{
				//se ejecuta unicamente cuando existe conexion con la interfaz
				if (tmp)
				{
					//se ejecuta este proceso en el cliente remoto
					ejecutarProcesoInterfazInventario(con);
					logger.info("\n\n ################################ ELIMINAR REGISTRO LOCAL INTERFAZ INVENTARIOS CODIGO --> "+dto.getConsecutivo());
					//se manda a elimiar el registro si existe desde en la bd local
					//*******************************************************************************************
					if (UtilidadCadena.noEsVacio(dto.getConsecutivo()) && isReproceso)
					{
						Connection con1=UtilidadBD.abrirConexion();
						try 
						{
							ps=new PreparedStatementDecorator(con1.prepareStatement("DELETE FROM ax_inv WHERE consecutivo="+dto.getConsecutivo()));
							ps.executeUpdate();
						} 
						catch (Exception e) 
						{
							logger.info("\n problema eliminado el registro de la tabla ax_inv local "+e);
						}
						ps.close();
						UtilidadBD.cerrarConexion(con1);
					}
					//*************************************************************************************************
				
					return new ResultadoInteger(resp,"Se ingreso informacion de interfaz");
					
				}
				else
				{
					ps.close();
					UtilidadBD.closeConnection(con);
					return new ResultadoInteger(resp,"No se pudo registrar información  en la interfaz de inventarios no hay conexión.");
				}
			}
			
			ps.close();
		
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema insertando ...."+e);
			//se inserta 
			if (!isReproceso)
				return insertarTransaccionInterfazRespaldo(dto, institucion);
		}
		UtilidadBD.closeConnection(con);
		return new ResultadoInteger(ConstantesBD.codigoNuncaValido,"No se pudo registrar información en la interfaz de inventario. ");
	}

	
	/**
	 * Metodo encargado de insertar los datos en la interfaz de inventarios
	 * Modificado por el anexo 779
	 * @param dto
	 * @param institucion       ---
	 */
	public ResultadoInteger insertarTransaccionInterfazRespaldo(DtoInterfazTransaccionAxInv dto, int institucion) 
	{
		int resp = 0;
		String nombreTabla=Utilidades.consultarNombreTablaInterfaz(ConstantesBDInterfaz.identificadorTablaTransacciones,institucion);

		
		Connection con=UtilidadBD.abrirConexion();
			
		String cadenaConsultaAxInv="insert into "+nombreTabla+" " +
																"(tiptr,numtr,tipdoc,numdoc,codtr," +
																"idtr,origtr,idcdon,almacen,idprov," +
																"codsha,codaxi,cantidad,valoru,valori," +
																"costod,codpac,ingpac,fecha,hora," +
																"estreg,fechatrn,horatrn, cospdt, cosedt, " +
																"tercero, almacen2, usuario, tiperr,consecutivo) " +
															"VALUES (" +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?,?,?,?," +
																"?,?, ?,'"+ConstantesIntegridadDominio.acronimoSinIntegridad+"'," +
																		UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ax_inv")+")"; //28
		
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAxInv));
			
			logger.info("\n cadena ->"+cadenaConsultaAxInv);
			
			//logger.info("dto.getTipoTransAxioma()->"+dto.getTipoTransAxioma()+"<-");
			
			
			if(UtilidadTexto.isEmpty(dto.getTipoTransAxioma()))
				ps.setObject(1, 0000000000);
			else
				ps.setObject(1, obtenerValor(dto.getTipoTransAxioma()));
			
			//logger.info("dto.getNumeroTransaccionAxioma()->"+dto.getNumeroTransaccionAxioma()+"<-");
			if(UtilidadTexto.isEmpty(dto.getNumeroTransaccionAxioma()))
				ps.setObject(2, 0000000000);
			else
				ps.setObject(2, obtenerValor(dto.getNumeroTransaccionAxioma()));
			
			//logger.info("dto.getTipoDocumento()->"+dto.getTipoDocumento()+"<-");
			if(UtilidadTexto.isEmpty(dto.getTipoDocumento()))
				ps.setObject(3, "  ");
			else
				ps.setObject(3, obtenerValor(dto.getTipoDocumento()));
			
			//logger.info("dto.getNumeroDocumento()->"+dto.getNumeroDocumento()+"<-");
			if(UtilidadTexto.isEmpty(dto.getNumeroDocumento()))
				ps.setObject(4, "        ");
			else
				ps.setObject(4, obtenerValor(dto.getNumeroDocumento()));
			
			//logger.info("dto.getCodigoTransaccion()->"+dto.getCodigoTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoTransaccion()))
				ps.setObject(5, "   ");
			else
				ps.setObject(5, obtenerValor(dto.getCodigoTransaccion()));
			
			//logger.info("dto.getIndicativoTransaccion()->"+dto.getIndicativoTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIndicativoTransaccion()))
				ps.setObject(6, 00);
			else
				ps.setObject(6, obtenerValor(dto.getIndicativoTransaccion()));
			
			//logger.info("dto.getOrigenTransaccion()->"+dto.getOrigenTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getOrigenTransaccion()))
				ps.setObject(7, " ");
			else
				ps.setObject(7, obtenerValor(dto.getOrigenTransaccion()));
			
			//logger.info("dto.getIndicativoCostoDonacion()->"+dto.getIndicativoCostoDonacion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIndicativoCostoDonacion()))
				ps.setObject(8, 0);
			else
				ps.setObject(8, obtenerValor(dto.getIndicativoCostoDonacion()));
			
			//logger.info("dto.getAlmacenInterfaz()->"+dto.getAlmacenInterfaz()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacenInterfaz()))
				ps.setObject(9, " ");
			else 
				ps.setObject(9, Utilidades.obtenerCodigoInterfazParametroAlmacen(Utilidades.convertirAEntero(dto.getAlmacenInterfaz())));
			
			//logger.info("dto.getIdentificacionProveedor()->"+agregarCeros(dto.getIdentificacionProveedor().trim(), 13)+dto.getIdentificacionProveedor().trim()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIdentificacionProveedor()))
				ps.setObject(10, "             ");
			else
				ps.setObject(10, obtenerValor(agregarCeros(dto.getIdentificacionProveedor().trim(), 13)+dto.getIdentificacionProveedor().trim()));
			
			//logger.info("dto.getCodigoArticuloInterfaz()->"+dto.getCodigoArticuloInterfaz()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoArticuloInterfaz()))
				ps.setObject(11, "           ");
			else
				ps.setObject(11, obtenerValor(dto.getCodigoArticuloInterfaz()));
			
			//logger.info("dto.getCodigoArticulo()->"+dto.getCodigoArticulo()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoArticulo()))
				ps.setObject(12, 0000000000);
			else
				ps.setObject(12, obtenerValor(dto.getCodigoArticulo()));
			
			//logger.info("dto.getCantidad()->"+dto.getCantidad()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCantidad()))
				ps.setObject(13, 0000000000000);
			else
				ps.setObject(13, obtenerValor(dto.getCantidad()));
			
			//logger.info("dto.getValorUnitario()->"+dto.getValorUnitario()+"<-");
			if(UtilidadTexto.isEmpty(dto.getValorUnitario()))
				ps.setObject(14, 000000000000000);
			else
				ps.setObject(14, obtenerValor(dto.getValorUnitario()));
			
			//logger.info("dto.getValorIva()->"+dto.getValorIva()+"<-");
			if(UtilidadTexto.isEmpty(dto.getValorIva()))
				ps.setObject(15, 000000000000000);
			else
				ps.setObject(15, obtenerValor(dto.getValorIva()));
			
			//logger.info("dto.getCostoDonacion()->"+dto.getCostoDonacion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCostoDonacion()))
				ps.setObject(16, 000000000000000);
			else
				ps.setObject(16, obtenerValor(dto.getCostoDonacion()));
			
			//logger.info("dto.getCodigoPaciente()->"+dto.getCodigoPaciente()+"<-");
			if(UtilidadTexto.isEmpty(dto.getCodigoPaciente()))
				ps.setObject(17, 0000000000);
			else
				ps.setObject(17, obtenerValor(dto.getCodigoPaciente()));
			
			//logger.info("dto.getIngresoPaciente()->"+dto.getIngresoPaciente()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIngresoPaciente()))
				ps.setObject(18, 0000000000);
			else
				ps.setObject(18, obtenerValor(dto.getIngresoPaciente()));
			
			//logger.info("dto.getFechaRegistro()->"+dto.getFechaRegistro()+"<-");
			if(UtilidadTexto.isEmpty(dto.getFechaRegistro()))
				ps.setObject(19, 00000000);
			else
				ps.setObject(19, obtenerValor(dto.getFechaRegistro().replace("-", "")));
			
			//logger.info("dto.getHoraRegistro()->"+dto.getHoraRegistro().replace(":", "")+agregarCeros(dto.getHoraRegistro().replace(":", "").trim(), 6)+"<-");
			if(UtilidadTexto.isEmpty(dto.getHoraRegistro()))
				ps.setObject(20, 000000);
			else
				ps.setObject(20, obtenerValor(dto.getHoraRegistro().replace(":", "")+agregarCeros(dto.getHoraRegistro().replace(":", "").trim(), 6)));
			
			//logger.info("dto.getEstadoRegistro()->"+dto.getEstadoRegistro()+"<-");
			if(UtilidadTexto.isEmpty(dto.getEstadoRegistro()))
				ps.setObject(21, 000);
			else
				ps.setObject(21, obtenerValor(dto.getEstadoRegistro()));
			
			//logger.info("dto.getFechaTransaccion()->"+dto.getFechaTransaccion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getFechaTransaccion()))
				ps.setObject(22, 00000000);
			else
				ps.setObject(22, obtenerValor(dto.getFechaTransaccion().replace("-", "")));
			
			//logger.info("dto.getHoraTransaccion()->"+dto.getHoraTransaccion().replace(":", "")+agregarCeros(dto.getHoraTransaccion().replace(":", "").trim(), 6)+"<-");
			if(UtilidadTexto.isEmpty(dto.getHoraTransaccion()))
				ps.setObject(23, 000000);
			else
				ps.setObject(23, obtenerValor(dto.getHoraTransaccion().replace(":", "")+agregarCeros(dto.getHoraTransaccion().replace(":", "").trim(), 6)));
			
			//ADICION CAMPO COSPDT ---> Centro de Costo que Solicita
			//logger.info("dto.getAlmacenSolicita()->"+Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacenSolicita())).trim()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacenSolicita()))
				ps.setObject(24, " ");
			else 
				ps.setString(24, Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacenSolicita())).trim());
			
			//ADICION CAMPO COSEDT ----> Centro de Costo que Ejecuta
			//logger.info("dto.getAlmacendespacha()->"+Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacendespacha())).trim()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacendespacha()))
				ps.setObject(25, " ");
			else 
				ps.setString(25, Utilidades.obtenerCentroCostoInterfaz(Utilidades.convertirAEntero(dto.getAlmacendespacha())).trim());
			
			
			//logger.info("dto.getTercero()->"+dto.getIdentificacionProveedor()+"<-");
			if(UtilidadTexto.isEmpty(dto.getIdentificacionProveedor()))
				ps.setObject(26, "             ");
			else
				ps.setObject(26, obtenerValor(dto.getIdentificacionProveedor()));
			
			
			//logger.info("dto.getAlmacenConsignacion()->"+dto.getAlmacenConsignacion()+"<-");
			if(UtilidadTexto.isEmpty(dto.getAlmacenConsignacion()))
				ps.setObject(27, " ");
			else 
				ps.setObject(27, Utilidades.obtenerCodigoInterfazParametroAlmacen(Utilidades.convertirAEntero(dto.getAlmacenConsignacion())));
			
			
			//logger.info("dto.getUsuario()->"+dto.getUsuario()+"<-");
			if(UtilidadTexto.isEmpty(dto.getUsuario()))
				ps.setObject(28, " ");
			else 
				ps.setString(28, dto.getUsuario());
			
			
			resp = ps.executeUpdate();
			
			ps.close();
			
			
			if(resp>0)
				return new ResultadoInteger(resp,"No se pudo registrar información en la interfaz por falta de intergridad en los datos. ");
			
		} 
		catch (SQLException e) 
		{
			UtilidadBD.closeConnection(con);
			logger.info("\n Problema insertarTransaccionInterfaz "+e);
			return new ResultadoInteger(1,"No se pudo registrar información en la interfaz de inventario ocurrio una excepción. ");
			
		}
		UtilidadBD.closeConnection(con);
		return new ResultadoInteger(1,"No se pudo registrar información en la interfaz de inventario. ");
	}
	
	
	
	/**
	 * Ejecutar Procedimiento en Sistema Externo
	 * @param con
	 * @throws SQLException
	 */
	private void ejecutarProcesoInterfazInventario(Connection con) 
	{
		try 
		{
			CallableStatement cs = con.prepareCall("CALL AXGA103CP");
	        cs.execute ();
	        cs.close();	
		}
		catch (SQLException e) 
		{
			logger.info("\n problema llamando el procedimiento CALL AXGA103CP ");
		}
		
	}

	/**
	 * 
	 * @param valor
	 * @return
	 */
	private Object obtenerValor(String valor) 
	{
		if(UtilidadTexto.isEmpty(valor))
			return null;
		else 
			return valor;
	}
	
	/**
	 * 
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
