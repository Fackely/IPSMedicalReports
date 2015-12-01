package com.princetonsa.mundo.lecturaConsumosLiquidados;

import java.sql.Connection;
import java.sql.DriverManager;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

public class LecturaConsumosLiquidados {
	
	
	public static Logger logger=Logger.getLogger(LecturaConsumosLiquidados.class);
	
	/**
	 * 
	 */
	private String driver="";
	/**
	 * 
	 */
	private String url="";
	/**
	 * 
	 */
	private String user="";
	/**
	 * 
	 */
	private String password="";
	
	/**
	 * 
	 */
	private Connection con;

	
	/**
	 * Cadena para la insercion en la tabla consumos_loquidados
	 */
	private static final String cadenaInsercionConsumosLiquidados="INSERT INTO consumos_liquidados 	( "+
																										" fecha_corte,"+
																										" centro_atencion,"+
																										" numero_ingreso,"+
																										" numero_cuenta,"+
																										" indicativo_egreso_medico,"+
																										" indicativo_egreso_administ,"+
																										" tipo_solicitud,"+
																										" indicativo_paquetizado,"+
																										" servicio,"+
																										" articulo,"+
																										" numero_solicitud_inicial,"+
																										" fecha_solicitud,"+
																										" numero_solicitud_paquete,"+
																										" convenio,"+
																										" esquema_tarifario,"+
																										" centro_costo_solicitante,"+
																										" centro_costo_solicitado,"+
																										" centro_costo_principal,"+
																										" datos_medico_responde,"+
																										" estado_cargo,"+
																										" cantidad_cargada,"+
																										" costo_unitario_servicio,"+
																										" valor_unitario_servicio,"+
																										" valor_total_cargado,"+
																										" codigo,"+
																										" codigo_medico " +
																									" ) " +
																									" ( "+
																									"select " +
																										" current_date," +
																										" centro_atencion," +
																										" ingreso," +
																										" cuenta," +
																										" indicativoegresomedico," +
																										" indicativoegresoadministrativo," +
																										" tipo_solicitud," +
																										" paquetizado," +
																										" servicio," +
																										" articulo," +
																										" numero_solicitud," +
																										" fecha_solicitud, "+
																										" numsolicitudpaquete," +
																										" convenio," +
																										" esquema_tarifario," +
																										" centro_costo_solicitante," +
																										" centro_costo_solicitado," +
																										" centro_costo_principal," +
																										" datos_medico," +
																										" estado," +
																										" cantidad_cargada," +
																										" valor_unitario_tarifa," +
																										" valor_unitario_cargado," +
																										" valor_total_cargado," +
																										" NEXTVAL('seq_consumos_liquidados')," +
																										" codigo_medico_responde " +
																									" from" +
																										" ("+
																											" SELECT DISTINCT" + 
																												" s.numero_solicitud,"+
																												" cc.centro_atencion,"+
																												" sc.ingreso,"+
																												" s.cuenta,"+
																												" true as indicativoegresomedico,"+
																												" true as indicativoegresoadministrativo,"+
																												" s.fecha_solicitud,"+
																												" dc.tipo_solicitud,"+
																												" dc.solicitud as numsolicitudpaquete,"+
																												" sc.convenio,"+
																												" dc.paquetizado,"+
																												" dc.servicio,"+
																												" dc.articulo,"+
																												" dc.esquema_tarifario,"+
																												" s.centro_costo_solicitante,"+
																												" s.centro_costo_solicitado," +
																												" sm.centro_costo_principal,"+
																												" s.datos_medico,"+
																												" s.codigo_medico_responde,"+
																												" dc.estado,"+
																												" dc.valor_unitario_tarifa,"+
																												" dc.valor_unitario_cargado,"+
																												" dc.valor_total_cargado,"+
																												" dc.cantidad_cargada" +
																											" from det_cargos dc " + 
																											" inner join sub_cuentas sc on (sc.sub_cuenta=dc.sub_cuenta) " + 
																											" inner join solicitudes s on (dc.solicitud=s.numero_solicitud) " +
																											" inner join centros_costo cc on(cc.codigo=s.centro_costo_solicitante)" +  
																											" inner join ingresos i on (i.id=sc.ingreso)"  +
																											" left outer join solicitudes_medicamentos sm on (s.numero_solicitud=sm.numero_solicitud) " +  
																											" WHERE (i.estado='ABI' OR i.estado='CER') AND (dc.estado=3 OR dc.estado=4 OR dc.estado=5) " +
																											" AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
																										") tabla" +
																								")";
	
	/**
	 * Cadena de consulta para cargar los consumos liquidados
	 */
	private static final String cadenaConsultaConsumosLiquidados="SELECT DISTINCT s.numero_solicitud,cc.centro_atencion,sc.ingreso,s.cuenta,"+
																"s.fecha_solicitud,dc.tipo_solicitud,"+
																"case when dc.tipo_solicitud = 15 then ss.solicitud else null end as numsolicitudpaquete,"+
																"sc.convenio,dc.paquetizado,dc.servicio,dc.articulo,dc.esquema_tarifario,"+
																"s.centro_costo_solicitante,s.centro_costo_solicitado,s.datos_medico,"+
																"dc.estado,dc.valor_unitario_tarifa,dc.valor_unitario_cargado,"+
																"dc.valor_total_cargado,dc.cantidad_cargada from det_cargos dc "+
																"inner join solicitudes_subcuenta ss on(ss.codigo=dc.cod_sol_subcuenta) "+
																"inner join sub_cuentas sc on (sc.sub_cuenta=ss.sub_cuenta) "+
																"inner join solicitudes s on (dc.solicitud=s.numero_solicitud) "+
																"inner join centros_costo cc on(cc.codigo=s.centro_costo_solicitante) "+
																"inner join ingresos i on (i.id=sc.ingreso) "+
																"WHERE (i.estado='ABI' OR i.estado='CER') AND (dc.estado=3 OR dc.estado=4 OR dc.estado=5) AND ss.eliminado='"+ConstantesBD.acronimoNo+"' ";
	
	/**
	 * Cadena para la validacion de estado si del parametro general consolidar cargos
	 */
	private static final String cadenaConsultaConsolidarCargos="SELECT valor FROM valores_por_defecto WHERE parametro='consolidar_cargos'";
	
	/**
	 * Constructor de la clase
	 */
	public LecturaConsumosLiquidados(String driver,String url,String usuario,String passwd)
	{
		this.driver=driver;
		this.url=url;
		this.user=usuario;
		this.password=passwd;
		this.abrirConexion();
	}
	
	/**
	 * 
	 *
	 */
	private void abrirConexion() 
	{
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url,user,password);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Insertar consumo liquidado
	 */
	public boolean insertar(Connection con)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionConsumosLiquidados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consulta para cargar los consumos liquidados
	 */
	public HashMap consultarConsumosLiquidados(Connection con)
	{
		HashMap<Object, Object> mapa=new HashMap<Object, Object>();
		mapa.put("numRegistros", 0);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaConsumosLiquidados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consultar el parametro general consolidar cargos para la validacion de la lectura
	 */
	public boolean consultarConsolidarCargos(Connection con)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaConsolidarCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String args[])
	{
		if(args.length==4)
		{
			ejectuarProceso(args);
		}
		else
		{
			logger.info("FALTAN LOS PARAMETROS PARA LA CONEXION -->> TIPOBD DRIVER URL USUARIO PASSWORD.");
		}
	}

	private static void ejectuarProceso(String[] args) 
	{
		LecturaConsumosLiquidados lectura=new LecturaConsumosLiquidados(args[0],args[1],args[2],args[3]);
		
		logger.info("EMPEZANDO PROCESO....");
		if(lectura.consultarConsolidarCargos(lectura.getCon()))
		{
			lectura.insertar(lectura.getCon());
		}
		else
			logger.info("EL PARAMETRO GENERAL CONSOLIDAR CARGOS SE ENCUENTRA EN NO");
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
