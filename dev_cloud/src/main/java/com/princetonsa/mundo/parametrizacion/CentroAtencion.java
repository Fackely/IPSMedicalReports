package com.princetonsa.mundo.parametrizacion;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Errores;
import util.IdentificadoresExcepcionesSql;
import util.InfoDatosInt;
import util.RangosConsecutivos;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.CentrosAtencionDao;
import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;


public class CentroAtencion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3421038231225180537L;

	private int consecutivo;

	private String codigo;

	private String descripcion;

	private String activo;

	private String descripcionCiudad;
	
	private int codInstitucion;

	private String codUpgd;

	private String codInstSirc;
	
	private String codigoEntidadSubcontratada;

	private String DescripcionInstSirc;

	private String empresaInstitucion;

	private String descEmpresaInstitucion;

	private String direccion;

	private String telefono;

	private String pais;

	private String ciudad;

	private String departamento;

	
	private DtoRegionesCobertura regionCobertura;

	
	private DtoCategoriaAtencion categoriaAtencion;
	
	
	//Agregado Anexo 959
	private String codigoInterfaz="";
	
	private String piePaginaPresupuestoOdon="";
	
	
	private String resolucion;
	private String prefFactura;
	private BigDecimal rangoInicialFactura;
	private BigDecimal rangoFinalFactura;
	
	
	private String resolucionFacturaVaria;
	private String prefFacturaVaria;
	private BigDecimal rangoInicialFacturaVaria;
	private BigDecimal rangoFinalFacturaVaria;
	
	
	/**
	 * LISTA PARA CARGAR LOS CONSECUTIVOS DE CENTRO DE ATENCION POR FACTURACION
	 */
	
	private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturacion = new ArrayList<DtoConsecutivoCentroAtencion>();
	
	/**
	 * LISTA PARA CARGAR LOS CONSECUTIVOS DE CENTRO DE ATENCION POR TESORERIA
	 */
	
	private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroTesoria = new ArrayList<DtoConsecutivoCentroAtencion>();
	
	
	private ArrayList<DtoConsecutivoCentroAtencion> listaHistorialConsecutivos = new ArrayList<DtoConsecutivoCentroAtencion>();
	
	/**
	 * Lista con los consecutivos del centro de atención relacionados con la factura varia
	 */
	private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturasVarias;
	
	public CentroAtencion() {
		
		this.consecutivo = -1;
		this.codigo = "";
		this.descripcion = "";
		this.activo = "";
		this.codInstitucion = -1;
		this.codUpgd = "";
		this.codInstSirc = "";
		this.codigoEntidadSubcontratada = "";
		this.empresaInstitucion = "";
		this.descEmpresaInstitucion = "";
		this.direccion = "";
		this.telefono="";
		this.ciudad = "";
		this.departamento = "";
		this.pais = "";
		this.regionCobertura=new DtoRegionesCobertura();
		this.categoriaAtencion= new DtoCategoriaAtencion();
		this.codigoInterfaz="";
		this.piePaginaPresupuestoOdon="";
		this.listaConsecutivosCentroFacturacion = new ArrayList<DtoConsecutivoCentroAtencion>();
		this.listaConsecutivosCentroTesoria = new ArrayList<DtoConsecutivoCentroAtencion>();
		
		this.listaConsecutivosCentroFacturasVarias = new ArrayList<DtoConsecutivoCentroAtencion>();
		
		this.resolucion="";
		this.prefFactura="";
		this.rangoInicialFactura= BigDecimal.ZERO;
		this.rangoFinalFactura=BigDecimal.ZERO;
		
		this.resolucionFacturaVaria="";
		this.prefFacturaVaria="";
		this.rangoInicialFacturaVaria= BigDecimal.ZERO;
		this.rangoFinalFacturaVaria=BigDecimal.ZERO;
	}

	
	private static CentrosAtencionDao centrosAtencionDao;

	private static CentrosAtencionDao getCentrosAtencionDao() {
		if (centrosAtencionDao == null) {
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			centrosAtencionDao = myFactory.getCentrosAtencionDao();
		}
		return centrosAtencionDao;
	}

	
	
	/**
	 * 	INSERTAR CENTRO DE ATENCION
	 * @param con
	 * @throws Errores
	 */
	public void insertar(Connection con, UsuarioBasico usuario) throws Errores 
	{
		boolean inicioTransaccion = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		try {
			
			inicioTransaccion = myFactory.beginTransaction(con);
			
			Log4JManager.info(this.getRegionCobertura()+ "" +this.getCategoriaAtencion());
			if (inicioTransaccion) {
				
				/**
				 * NO TIENE SENTIDO????????????????????????????????????????''
				 */
				int tmpCodigoCentro=CentroAtencion.getCentrosAtencionDao().insertarCentroAtencion(con, this.getCodigo(), this.getDescripcion(),
																			this.getActivo(), this.getCodInstitucion(),
																			this.getCodUpgd(), this.getCodInstSirc(),
																			this.empresaInstitucion, this.direccion,
																			this.getPais(), this.getCodigoDepartamento(),
																			this.getCodigoCiudad(), this.getTelefono(),
																			this.getRegionCobertura().getCodigo(), this.getCategoriaAtencion().getCodigo(),
																			this.getCodigoInterfaz(),this.getPiePaginaPresupuestoOdon() ,
																			this.resolucion,
																			this.prefFactura,
																			this.rangoInicialFactura,
																			this.rangoFinalFactura,
																			this.resolucionFacturaVaria,
																			this.prefFacturaVaria,
																			this.rangoInicialFacturaVaria,
																			this.rangoFinalFacturaVaria,
																			this.codigoEntidadSubcontratada);
				
				/**
				 * GUARDA LOS CONSECUTIVOS DEL CENTRO DE ATENCION
				 */
				accionGuardarConsecutivosCentroAtencion(con, tmpCodigoCentro, usuario);
			
			} 
			
			else 
			{
				Log4JManager.error("Problemas iniciando la transacción");
				throw new Errores("Problemas iniciando la transacción",
						"errors.transaccion");
			}
			

			myFactory.endTransaction(con);
		} catch (SQLException se) {
			Log4JManager.error(se);
			try {
				if (inicioTransaccion)
					myFactory.abortTransaction(con);

				if (se.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente))
					throw new Errores("El registro ya existe","error.centrosAtencion.centroYaExiste", this.getCodigo());
			
			} catch (SQLException se2) {
				Log4JManager.error(se2);
				se.setNextException(se2);
			}
			throw new Errores(se);
		}
	}


	/**
	 * ACCION GUARDAR CONSECUTIVOS
	 * @param con
	 * @param tmpCodigoCentro
	 */
	private void accionGuardarConsecutivosCentroAtencion(Connection con,int tmpCodigoCentro, UsuarioBasico usuario) 
	{
		
		Log4JManager.info(" GUARDAR LOS CONSECUTIVOS");
		if(tmpCodigoCentro>0)
		{
			Log4JManager.info(" CREAR MUNDO CONSECUTIVOS ");
			ConsecutivosCentroAtencion objMundoConsecutivo =  new ConsecutivosCentroAtencion();
			guardarConsecutivosHistoricos(con, tmpCodigoCentro, objMundoConsecutivo, usuario);
		}
		else
		{
			Log4JManager.info(" NO TIENE CODIGO CENTRO DE ATENCION");
		}
	}
	
	
	/**
	 * 
	 * @param listaHistoricos
	 * @param codigoCentroAtencion
	 * @param con
	 */
	public void modificarConsecutivos (int codigoCentroAtencion, Connection con, UsuarioBasico usuario )
	{
		ConsecutivosCentroAtencion objMundoConsecutivo =  new ConsecutivosCentroAtencion();
		
		for(DtoConsecutivoCentroAtencion dto: this.getListaHistorialConsecutivos())
		{
			dto.setUsuarioModifica( new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			
			if(dto.getCodigoPk().doubleValue()>0)
			{
				objMundoConsecutivo.modificar(con, dto);
			}
			else
			{
				dto.setCentroAtencion(new InfoDatosInt(codigoCentroAtencion, ""));
				
				if(!objMundoConsecutivo.guardar(dto, con))
				{
					Log4JManager.error("\n\n\n\n\n\n\n ERROR \n" );
					UtilidadBD.abortarTransaccion(con);
				}
			}
		}
	}
	
	
	
	/**
	 * GUARDAR CONSECUTIVOS HISTORICOS
	 * @param con
	 * @param tmpCodigoCentro
	 * @param objConsecutivo
	 */
	private void guardarConsecutivosHistoricos(Connection con, int tmpCodigoCentro, ConsecutivosCentroAtencion objConsecutivo, UsuarioBasico usuario) 
	{
		
		for(DtoConsecutivoCentroAtencion dtoT: this.getListaHistorialConsecutivos())
		{
			dtoT.setCentroAtencion( new InfoDatosInt(tmpCodigoCentro,""));
			dtoT.setUsuarioModifica( new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			
			if( dtoT.getConsecutivo().doubleValue()>0 && dtoT.getActivo().equals(ConstantesBD.acronimoSi) && dtoT.getCodigoPk().doubleValue()<=0 )
			{
				if(!objConsecutivo.guardar(dtoT,con))
				{
					Log4JManager.error("\n\n\n\n\n\n\n ERROR \n" );
					UtilidadBD.abortarTransaccion(con);
				}
			}
			else
			{
				Log4JManager.info("NO GUARDA");
			}
		}
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @throws Errores
	 */
	public void actualizar(String empresaInstitucion, int codigoCentroAtencion, UsuarioBasico usuario) throws Errores 
	{
		
		boolean inicioTransaccion = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = UtilidadBD.abrirConexion();
		
		try {
			inicioTransaccion = myFactory.beginTransaction(con);
			if (inicioTransaccion) 
			{
				CentroAtencion.getCentrosAtencionDao().actualizarCentroAtencion(con, this.getConsecutivo(),
																				this.getDescripcion(), this.getActivo(),
																				this.getCodUpgd(), this.getCodInstSirc(),
																				this.empresaInstitucion, 
																				this.direccion ,
																				this.getTelefono() ,
																				this.getPais() , 
																				this.getCodigoDepartamento() , 
																				this.getCodigoCiudad(), 
																				this.getRegionCobertura().getCodigo() ,
																				this.getCategoriaAtencion().getCodigo(),
																				this.getCodigoInterfaz(),this.getPiePaginaPresupuestoOdon(),
																				this.getResolucion() , 
																				this.getPrefFactura(),
																				this.getRangoInicialFactura(),
																				this.getRangoFinalFactura(),
																				this.getResolucionFacturaVaria() , 
																				this.getPrefFacturaVaria(),
																				this.getRangoInicialFacturaVaria(),
																				this.getRangoFinalFacturaVaria(),
																				this.codigoEntidadSubcontratada
																				);
				/**
				 *	MODIFICAR E INSERTAR LISTA DE CONSECUTIVOS 
				 */
				modificarConsecutivos (codigoCentroAtencion,  con, usuario);
				
				
			} 
			else 
			{
				Log4JManager.error("Problemas iniciando la transacción");
				throw new Errores("Problemas iniciando la transacción","errors.transaccion");
			}

			myFactory.endTransaction(con);
		} 
		catch (SQLException se) 
		{
			Log4JManager.error(se);
			try {
				if (inicioTransaccion)
				{
					myFactory.abortTransaction(con);
				}
			} 
			catch (SQLException se2) 
			{
				Log4JManager.error(se2);
				se.setNextException(se2);
			}
			throw new Errores(se);
		}
	}
	

	/**
	 * 
	 * @param con
	 * @throws Errores
	 */
	public void eliminar(Connection con) throws Errores {
		boolean inicioTransaccion = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System
				.getProperty("TIPOBD"));

		try {
			inicioTransaccion = myFactory.beginTransaction(con);
			if (inicioTransaccion) {
				CentroAtencion.getCentrosAtencionDao().eliminarCentroAtencion(
						con, this.getConsecutivo());
			} else {
				Log4JManager.error("Problemas iniciando la transacción");
				throw new Errores("Problemas iniciando la transacción",
						"errors.transaccion");
			}

			myFactory.endTransaction(con);
		} catch (SQLException se) {
			Log4JManager.error(se);

			try {
				if (inicioTransaccion)
					myFactory.abortTransaction(con);

				if (se
						.getSQLState()
						.equals(
								IdentificadoresExcepcionesSql.codigoExcepcionSqlViolacionForanea)
						|| se
								.getSQLState()
								.equals(
										IdentificadoresExcepcionesSql.codigoExcepcionSqlViolacionIntegridadRelacion)) {
					throw new Errores(
							"El centro de atención no se puede eliminar por violación de llave foránea",
							"error.centrosAtencion.noSePuedeEliminar", this
									.getCodigo());
				}
			} catch (SQLException se2) {
				Log4JManager.error(se2);
				se.setNextException(se2);
			}
			throw new Errores(se);
		}
	}

	/**
	 * 
	 * @param consecutivo
	 */
	public void consultar(int consecutivo)  
	{
		Connection con=UtilidadBD.abrirConexion();
		try {
			this.consultar(con, consecutivo);
		} catch (Errores e) {
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
	}
	
	public void consultar(Connection con, int consecutivo) throws Errores {
		try {
			HashMap tempoCentroAtencion = CentroAtencion
					.getCentrosAtencionDao().consultarCentroAtencion(con,
							consecutivo);
			
			Log4JManager.info("Realizo la consulta del Centro de Atencion sin Problema " );
			int cantidadRegistros = Integer
					.parseInt((String) tempoCentroAtencion.get("numRegistros"));

			if (cantidadRegistros > 0) {
				this.setConsecutivo(consecutivo);
				this.setCodigo((String) tempoCentroAtencion.get("codigo_0"));
				this.setDescripcion((String) tempoCentroAtencion
						.get("descripcion_0"));
				this.setActivo(((Boolean) UtilidadTexto
						.getBoolean(tempoCentroAtencion.get("activo_0") + ""))
						.toString());
				this.setCodInstitucion(Utilidades
						.convertirAEntero(tempoCentroAtencion
								.get("codinstitucion_0")
								+ ""));
				this
						.setCodUpgd(tempoCentroAtencion.get("codupgd_0")
								.toString());
				this.setCodInstSirc(tempoCentroAtencion.get(
						"codinstitucionsirc_0").toString());
				this.setCodigoEntidadSubcontratada(tempoCentroAtencion.get(
							"codigoentidadsubcontratada_0").toString());
				this.setDescripcionInstSirc(tempoCentroAtencion.get(
						"descinstitucionsirc_0").toString());
				this.setEmpresaInstitucion(tempoCentroAtencion.get(
						"empresainstitucion_0").toString());
				this.setDescEmpresaInstitucion(tempoCentroAtencion.get(
						"descempresainstitucion_0").toString());
				this.setDireccion(tempoCentroAtencion.get("direccion_0") + "");
				
				this.setTelefono(tempoCentroAtencion.get("telefono_0") + "");
				
				this.setDescripcionCiudad(tempoCentroAtencion.get("descripcion_ciudad_0") + "");
				this.setCiudad(tempoCentroAtencion.get("ciudad_0").toString());
				this.setDepartamento(tempoCentroAtencion.get("departamento_0").toString());
				this.getCategoriaAtencion().setDescripcion(tempoCentroAtencion.get("descripcion_categoria_0") + "");
				this.getCategoriaAtencion().setCodigo(Utilidades.convertirADouble(tempoCentroAtencion.get("categoria_atencion_0").toString()));
				this.getRegionCobertura().setDescripcion(tempoCentroAtencion.get("descripcion_region_0").toString());
				this.getRegionCobertura().setCodigo(Utilidades.convertirADouble(tempoCentroAtencion.get("region_cobertura_0").toString()));
				
				//Agregados por anexo 959
				
				this.setCodigoInterfaz(tempoCentroAtencion.get("codinterfaz_0")+"");
				this.setPiePaginaPresupuestoOdon(tempoCentroAtencion.get("piepaginapresupuesto_0")+"");
				this.setResolucion(tempoCentroAtencion.get("resolucion_0")+"");
				this.setPrefFactura(tempoCentroAtencion.get("pref_factura_0")+"");
				
				
 				
				if(tempoCentroAtencion.get("rgo_inic_fact_0")!=null  && !UtilidadTexto.isEmpty(tempoCentroAtencion.get("rgo_inic_fact_0")+"") )
				{
					this.setRangoInicialFactura(new BigDecimal(tempoCentroAtencion.get("rgo_inic_fact_0")+""));
				}
				if(tempoCentroAtencion.get("rgo_fin_fact_0")!=null && !UtilidadTexto.isEmpty(tempoCentroAtencion.get("rgo_fin_fact_0")+""))
				{
					this.setRangoFinalFactura(new BigDecimal(tempoCentroAtencion.get("rgo_fin_fact_0")+""));
				}
			
				/**
				 * Sección de facturas Varias
				 */
				this.setResolucionFacturaVaria(tempoCentroAtencion.get("resolucion_factura_varia_0")+"");
				this.setPrefFacturaVaria(tempoCentroAtencion.get("pref_factura_varia_0")+"");
				
				if(tempoCentroAtencion.get("rgo_inic_fact_varia_0")!=null  && !UtilidadTexto.isEmpty(tempoCentroAtencion.get("rgo_inic_fact_varia_0")+"") )
				{
					this.setRangoInicialFacturaVaria(new BigDecimal(tempoCentroAtencion.get("rgo_inic_fact_varia_0")+""));
				}
				if(tempoCentroAtencion.get("rgo_fin_fact_varia_0")!=null && !UtilidadTexto.isEmpty(tempoCentroAtencion.get("rgo_fin_fact_varia_0")+""))
				{
					this.setRangoFinalFacturaVaria(new BigDecimal(tempoCentroAtencion.get("rgo_fin_fact_varia_0")+""));
				}
				
			} else {
				Log4JManager.error("Problemas iniciando la transacción");
				throw new Errores("El centro de atención no existe",
						"errors.noExiste",
						"El centro de atención con consecutivo" + consecutivo);
			}
		} catch (SQLException se) {
			Log4JManager.error(se);
			throw new Errores(se);
		}
	}


	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Metodo que carga el combo de codigos UPGD
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarVigiUnidadesPrimarias(Connection con,
			int codInstitucion, int codConsecutivo) {
		return centrosAtencionDao.cargarVigiUnidadesPrimarias(con,
				codInstitucion, codConsecutivo);
	}

	public static Collection consultarCentrosAtencionInstitucion(
			Connection con, int codInstitucion) throws Errores {
		try {
			return CentroAtencion.getCentrosAtencionDao()
					.consultarCentrosAtencionInst(con, codInstitucion);
		} catch (SQLException se) {
			Log4JManager.error(se);
			throw new Errores(se);
		}
	}

	/**
	 * Metodo que carga los codigos de Instituciones SIRC; sin incluir los que
	 * ya estan asignados a algun centro de atencion
	 * 
	 * @param con
	 * @param codInstitucion
	 * */
	public static HashMap cargarInstitucionesSIRC(Connection con,
			int codInstitucion) {
		return CentroAtencion.getCentrosAtencionDao().cargarInstitucionesSIRC(
				con, codInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return
	 */
	public static double obtenerEmpresaInstitucionCentroAtencion(
			int consecutivoCentroAtencion) {
		Connection con = UtilidadBD.abrirConexion();
		double retorna = CentroAtencion.getCentrosAtencionDao()
				.obtenerEmpresaInstitucionCentroAtencion(con,
						consecutivoCentroAtencion);
		UtilidadBD.closeConnection(con);
		return retorna;
	}

	/**
	 * Metodo que consulta el rango inicial y final de los consecutivos de facturacion
	 * @param centroAtencion
	 * @return
	 */
	public static RangosConsecutivos obtenerRangosFacturacionXCentroAtencion(int centroAtencion) {
		return getCentrosAtencionDao().obtenerRangosFacturacionXCentroAtencion(centroAtencion);
	}
	
	
	public int getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public int getCodInstitucion() {
		return this.codInstitucion;
	}

	public void setCodInstitucion(int codInstitucion) {
		this.codInstitucion = codInstitucion;
	}

	public String getCodUpgd() {
		return codUpgd;
	}

	public void setCodUpgd(String codUpgd) {
		this.codUpgd = codUpgd;
	}

	/**
	 * @return the codInstSirc
	 */
	public String getCodInstSirc() {
		return codInstSirc;
	}

	/**
	 * @param codInstSirc
	 *            the codInstSirc to set
	 */
	public void setCodInstSirc(String codInstSirc) {
		this.codInstSirc = codInstSirc;
	}

	/**
	 * @return the descripcionInstSirc
	 */
	public String getDescripcionInstSirc() {
		return DescripcionInstSirc;
	}

	/**
	 * @param descripcionInstSirc
	 *            the descripcionInstSirc to set
	 */
	public void setDescripcionInstSirc(String descripcionInstSirc) {
		DescripcionInstSirc = descripcionInstSirc;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion
	 *            the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the descEmpresaInstitucion
	 */
	public String getDescEmpresaInstitucion() {
		return descEmpresaInstitucion;
	}

	/**
	 * @param descEmpresaInstitucion
	 *            the descEmpresaInstitucion to set
	 */
	public void setDescEmpresaInstitucion(String descEmpresaInstitucion) {
		this.descEmpresaInstitucion = descEmpresaInstitucion;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais
	 *            the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad
	 *            the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento
	 *            the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	
	/**
	 * @param centrosAtencionDao
	 *            the centrosAtencionDao to set
	 */
	public static void setCentrosAtencionDao(
			CentrosAtencionDao centrosAtencionDao) {
		CentroAtencion.centrosAtencionDao = centrosAtencionDao;
	}
	
	/**
	 * 
	 * 
	 * 
	 */

	 public String getCodigoCiudad(){
		
		 String[] vec = getCiudad().split(ConstantesBD.separadorSplit) ;
		 return vec[1];
	 }
	 
	 /**
		 * 
		 * 
		 * 
		 */

	 public String getCodigoDepartamento(){
		
		 String[] vec = getCiudad().split(ConstantesBD.separadorSplit) ;
		 return vec[0];
	 }

	/**
	 * @return the regionCobertura
	 */
	public DtoRegionesCobertura getRegionCobertura() {
		return regionCobertura;
	}

	/**
	 * @param regionCobertura the regionCobertura to set
	 */
	public void setRegionCobertura(DtoRegionesCobertura regionCobertura) {
		this.regionCobertura = regionCobertura;
	}

	/**
	 * @return the categoriaAtencion
	 */
	public DtoCategoriaAtencion getCategoriaAtencion() {
		return categoriaAtencion;
	}

	/**
	 * @param categoriaAtencion the categoriaAtencion to set
	 */
	public void setCategoriaAtencion(DtoCategoriaAtencion categoriaAtencion) {
		this.categoriaAtencion = categoriaAtencion;
	}

	/**
	 * @return the descripcionCiudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
	 * @param descripcionCiudad the descripcionCiudad to set
	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public String getPiePaginaPresupuestoOdon() {
		return piePaginaPresupuestoOdon;
	}

	public void setPiePaginaPresupuestoOdon(String piePaginaPresupuestoOdon) {
		this.piePaginaPresupuestoOdon = piePaginaPresupuestoOdon;
	}
	
	
	
	//<><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><
	/**
	 * ANEXO 959 CAMBIOS EN FUNCIONALIDAD POR INTERFAZ SONRIA -2
	 */
	
//private ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentros = ArrayList<DtoConsecutivoCentroAtencion>();


	public void setListaConsecutivosCentroFacturacion(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturacion) {
		this.listaConsecutivosCentroFacturacion = listaConsecutivosCentroFacturacion;
	}

	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosCentroFacturacion() {
		return listaConsecutivosCentroFacturacion;
	}

	public void setListaConsecutivosCentroTesoria(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroTesoria) {
		this.listaConsecutivosCentroTesoria = listaConsecutivosCentroTesoria;
	}

	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosCentroTesoria() {
		return listaConsecutivosCentroTesoria;
	}


	/**
	 * CARGAR CONSECUTIVOS FACTURACION Y LOS CONSECUTIVOS DE TESORIA
	 * 
	 * @param dto
	 */
	public void cargarConsecutivosCentroAtencion(DtoConsecutivoCentroAtencion dto)
	{
		ConsecutivosCentroAtencion objConsecutivo =  new ConsecutivosCentroAtencion();
		objConsecutivo.cargarConsecutivos(dto);
		
		this.setListaConsecutivosCentroFacturacion(objConsecutivo.getListaConsecutivosFacturacion());
		this.setListaConsecutivosCentroTesoria(objConsecutivo.getListaConsecutivoTesoreria());
		this.setListaConsecutivosCentroFacturasVarias(objConsecutivo.getListaConsecutivosCentroFacturasVarias());
		this.setListaHistorialConsecutivos(objConsecutivo.getListaConsecutivos());
	}

	/**
	 * 
	 * Metodo para obtener el pie de pagina del presupuesto
	 * @param centroAtencion
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static String obtenerPiePaginaPresupuesto(int centroAtencion) {
		return CentroAtencion.getCentrosAtencionDao().obtenerPiePaginaPresupuesto(centroAtencion);
	}
	
	
	/**
	 * 
	 * @param dto
	 */
	public void cargarHistoricoConsecutivo(DtoConsecutivoCentroAtencion dto)
	{
		ConsecutivosCentroAtencion objConsecutivo = new ConsecutivosCentroAtencion();
		this.setListaHistorialConsecutivos(objConsecutivo.cargar(dto));
		
	}
	
	
	/**
	 * 
	 * @param listaHistorialConsecutivos
	 */
	public void setListaHistorialConsecutivos(
			ArrayList<DtoConsecutivoCentroAtencion> listaHistorialConsecutivos) {
		this.listaHistorialConsecutivos = listaHistorialConsecutivos;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoConsecutivoCentroAtencion> getListaHistorialConsecutivos() {
		return listaHistorialConsecutivos;
	}



	public String getResolucion() {
		return resolucion;
	}



	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}



	public String getPrefFactura() {
		return prefFactura;
	}



	public void setPrefFactura(String prefFactura) {
		this.prefFactura = prefFactura;
	}



	public BigDecimal getRangoInicialFactura() {
		return rangoInicialFactura;
	}



	public void setRangoInicialFactura(BigDecimal rangoInicialFactura) {
		this.rangoInicialFactura = rangoInicialFactura;
	}



	public BigDecimal getRangoFinalFactura() {
		return rangoFinalFactura;
	}



	public void setRangoFinalFactura(BigDecimal rangoFinalFactura) {
		this.rangoFinalFactura = rangoFinalFactura;
	}



	/**
	 */
	public static ArrayList<com.servinte.axioma.orm.CentroAtencion> cargarCentrosAtencion() 
	{
		CentroAtencionDelegate dao=new CentroAtencionDelegate();
		return dao.listarActivos();
	}



	/**
	 * @return the resolucionFacturaVaria
	 */
	public String getResolucionFacturaVaria() {
		return resolucionFacturaVaria;
	}



	/**
	 * @param resolucionFacturaVaria the resolucionFacturaVaria to set
	 */
	public void setResolucionFacturaVaria(String resolucionFacturaVaria) {
		this.resolucionFacturaVaria = resolucionFacturaVaria;
	}



	/**
	 * @return the prefFacturaVaria
	 */
	public String getPrefFacturaVaria() {
		return prefFacturaVaria;
	}



	/**
	 * @param prefFacturaVaria the prefFacturaVaria to set
	 */
	public void setPrefFacturaVaria(String prefFacturaVaria) {
		this.prefFacturaVaria = prefFacturaVaria;
	}



	/**
	 * @return the rangoInicialFacturaVaria
	 */
	public BigDecimal getRangoInicialFacturaVaria() {
		return rangoInicialFacturaVaria;
	}



	/**
	 * @param rangoInicialFacturaVaria the rangoInicialFacturaVaria to set
	 */
	public void setRangoInicialFacturaVaria(BigDecimal rangoInicialFacturaVaria) {
		this.rangoInicialFacturaVaria = rangoInicialFacturaVaria;
	}



	/**
	 * @return the rangoFinalFacturaVaria
	 */
	public BigDecimal getRangoFinalFacturaVaria() {
		return rangoFinalFacturaVaria;
	}



	/**
	 * @param rangoFinalFacturaVaria the rangoFinalFacturaVaria to set
	 */
	public void setRangoFinalFacturaVaria(BigDecimal rangoFinalFacturaVaria) {
		this.rangoFinalFacturaVaria = rangoFinalFacturaVaria;
	}



	/**
	 * @param listaConsecutivosCentroFacturasVarias the listaConsecutivosCentroFacturasVarias to set
	 */
	public void setListaConsecutivosCentroFacturasVarias(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturasVarias) {
		this.listaConsecutivosCentroFacturasVarias = listaConsecutivosCentroFacturasVarias;
	}



	/**
	 * @return the listaConsecutivosCentroFacturasVarias
	 */
	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosCentroFacturasVarias() {
		return listaConsecutivosCentroFacturasVarias;
	}



	/**
	 * @return the codigoEntidadSubcontratada
	 */
	public String getCodigoEntidadSubcontratada() {
		return codigoEntidadSubcontratada;
	}



	/**
	 * @param codigoEntidadSubcontratada the codigoEntidadSubcontratada to set
	 */
	public void setCodigoEntidadSubcontratada(String codigoEntidadSubcontratada) {
		this.codigoEntidadSubcontratada = codigoEntidadSubcontratada;
	}
	
	
	/**
	 * Metodo que carga las entidades subcotnratadas activas y que tiene asociados
	 * centros de costo identificados como SubAlmacen
	 * 
	 * 
	 * @param con
	 * @param codInstitucion
	 * */
	public static HashMap cargarEntidadesSubcontratadas(Connection con,	int codInstitucion, int centroAtencion) {
		return CentroAtencion.getCentrosAtencionDao().cargarEntidadesSubcontratadas(
				con, codInstitucion, centroAtencion);
	}
	
	
}