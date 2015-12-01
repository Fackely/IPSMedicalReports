/*
 * Created on Nov 27, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.Collection;
import java.util.HashMap;

import util.Answer;

import com.princetonsa.dao.ArticuloDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseArticuloDao;
import com.princetonsa.mundo.Articulo;

/**
 * @author rcancino
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PostgresqlArticuloDao implements ArticuloDao {

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una cama en la base de datos PostgreSQL.
	 */
	private static final String insertarArticuloStr="INSERT INTO articulo (" +
														"codigo, " +
														"subgrupo, " +
														"descripcion, " +
														"naturaleza, " +
														"minsalud, " +
														"forma_farmaceutica, " +
														"concentracion, " +
														"unidad_medida, " +
														"estado," +
														"fecha_modifica," +
														"usuario_modifica," +
														"hora_modifica," +
														"categoria," +
														"stock_minimo," +
														"stock_maximo," +
														"punto_pedido," +
														"cantidad_compra," +
														"costo_promedio," +
														"registro_invima," +
														"maxima_cantidad_mes," +
														"multidosis," +
														"maneja_lotes," +
														"maneja_fecha_vencimiento," +
														"porcentaje_iva," +
														"precio_ultima_compra," +
														"precio_base_venta," +
														"fecha_precio_base_venta," +
														"institucion," +
														"costo_donacion," +
														"codigo_interfaz," +
														"indicativo_automatico," +
														"indicativo_por_completar," +
														"precio_compra_mas_alta," +
														"descripcion_alterna," +
														"numero_expediente," +
														"cons_present_comercial," +
														"presentacion_comercial," +
														"clasificacion_atc," +
														"registro_art," +
														"vigencia," +
														"roles_x_producto," +
														"titular," +
														"fabricante," +
														"importador," +
														"atencion_odontologica," +
														"nivel_atencion) " +
													"VALUES " +
														"(nextval('seq_articulo'), ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static final String seqViaAdmArticuloStr="nextval('SEQ_VIA_ADM_ARTI')";
	
	/**
	 * 
	 */
	private static final String seqUnidadesArticuloStr="nextval('SEQ_UNID_X_ARTI')";
	
	/**
	 * 
	 */
	public int insertarArticulo(
		Connection con,
		String subgrupoArticulo,
		String naturalezaArticulo,
		String formaFarmaceutica,
		String concentracionArticulo,
		String unidadMedidaArticulo,
		boolean estadoArticulo,
		String minsalud,
		String descripcion,
		String fechaCreacion,
		String usuario,
		String horaCreacion,
		int stockMinimo,
		int stockMaximo,
		int puntoPedido,
		int cantidadCompra,
		String costoPromedio,
		double costoDonacion,
		String codigoInterfaz,
		String indicativoAutomatico,
		String indicativoPorCompletar,
		int categoria,
		String registroInvima,
		
		 double maximaCantidadMes,
		 String multidosis,
		 String manejaLotes,
		 String manejaFechaVencimiento,
		 double porcentajeIva,
		 double precioUltimaCompra,
		 double precioBaseVenta,
		 double precioCompraMasAlta,
		 String descripcionAlterna,
		 String fechaPrecioBaseVenta,
		 int institucion,
		 HashMap mapaViaAdm,
		 HashMap mapaUnidosis,
		 HashMap mapaGrupoEspecial,
		 HashMap cumMap,
		 String atencionOdon,
		 long consecutivoNivelAtencionArticulo
		)
	{	
		return SqlBaseArticuloDao.insertarArticulo( con, 						//1
													subgrupoArticulo,			//2
													naturalezaArticulo, 		//3
													formaFarmaceutica,			//4
													concentracionArticulo, 		//5
													unidadMedidaArticulo,		//6
		 											estadoArticulo, 			//7
		 											minsalud,					//8
		 											descripcion,				//9
	 												fechaCreacion,				//10
	 												usuario,					//11
	 												horaCreacion,				//12
	 												stockMinimo,				//13
	 												stockMaximo,				//14
	 												puntoPedido,				//15
	 												cantidadCompra,				//16
	 												costoPromedio,				//17
	 												costoDonacion,				//18
	 												codigoInterfaz,				//19
	 												indicativoAutomatico,		//20
	 												indicativoPorCompletar,		//21 													
	 												categoria,					//22
	 												registroInvima,				//23
	 												
	 												 maximaCantidadMes,			//24
	 												 multidosis,				//25
	 												 manejaLotes,				//26
	 												 manejaFechaVencimiento,	//27	
	 												 porcentajeIva,				//28
	 												 precioUltimaCompra,		//29
	 												 precioBaseVenta,			//30
	 												 precioCompraMasAlta,		//31	
	 												 fechaPrecioBaseVenta,		//32
	 												 descripcionAlterna,		//33
	 												 institucion,
	 												insertarArticuloStr,
	 												seqViaAdmArticuloStr,
	 												seqUnidadesArticuloStr,
	 												mapaViaAdm,
	 												mapaUnidosis,
	 												mapaGrupoEspecial,
	 												cumMap,
	 												atencionOdon,
	 												consecutivoNivelAtencionArticulo
		 												);
	}
	
	
	/**
	 * 
	 */
	public boolean insertarInformMed(Connection con, int codArticulo, Articulo articulo)
	{
		return SqlBaseArticuloDao.insertarInformMed( con, codArticulo, articulo);
	}

	/**
	 * 
	 */
	public Answer cargarArticulo(Connection con, int codigoArticulo)
	{
		return SqlBaseArticuloDao.cargarArticulo(con,codigoArticulo);
	}
	
	/**
	 * 
	 */
	public ResultSetDecorator cargarArticuloDescripcion(Connection con, int codigoArticulo)
	{
		return SqlBaseArticuloDao.cargarArticuloDescripciones(con,codigoArticulo);
	}
	
	/**
	 * 
	 */
	public int cambiarEstadoArticulo(Connection con, String codigoArticulo,	boolean estadoArticulo)
	{
			return SqlBaseArticuloDao.cambiarEstadoArticulo(con,codigoArticulo,estadoArticulo);
	}

	/**
	 * 
	 */
	public int modificarArticulo(Connection con, int codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estado, int stockMinimo, int stockMaximo, int puntoPedido, int cantidadCompra, String costoPromedio,String codigoInterfaz,String indicativoAutomatico,String indicativoPorCompletar, int categoria,String registroInvima, double maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, double porcentajeIva, double precioUltimaCompra, double precioBaseVenta, double precioCompraMasAlta, String descripcionAlterna, String fechaPrecioBaseVenta, int institucion, boolean manejoLoteDeSaN, HashMap mapaViaAdm, HashMap mapaUnidosis, HashMap mapaGrupoEspecial, String usuario, HashMap cumMap, long nivelAtencion)
	{
		return SqlBaseArticuloDao.modificarArticulo( con, naturaleza,  formaFarmaceutica,  concentracion,  unidadMedida,  estado,  minsalud,  descripcion, codigo,stockMinimo,stockMaximo,puntoPedido,cantidadCompra,costoPromedio,codigoInterfaz,indicativoAutomatico,indicativoPorCompletar,categoria,registroInvima, maximaCantidadMes,   multidosis, manejaLotes, manejaFechaVencimiento, porcentajeIva, precioUltimaCompra, precioBaseVenta, precioCompraMasAlta, descripcionAlterna, fechaPrecioBaseVenta, institucion, manejoLoteDeSaN, mapaViaAdm, mapaUnidosis, mapaGrupoEspecial, usuario, cumMap,DaoFactory.POSTGRESQL, seqUnidadesArticuloStr, nivelAtencion);
	}
	
	/**
	 * 
	 */
	public int modificarInfoMArticulo(Connection con, int codigo, String tiempoEsp, String efectoDes, String efectosSec, String observaciones, String bibliografia)
	{
		return SqlBaseArticuloDao.modificarInfoMArticulo( con, codigo, tiempoEsp, efectoDes, efectosSec, observaciones, bibliografia);
	}
	
	/**
	 * 
	 */
	public int codigoUltimoArticulo(Connection con) 
	{
		return SqlBaseArticuloDao.codigoUltimoArticulo(con);
	}

	/**
	 * Búscar artículos
	 * @param con
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param descripcion
	 * @param naturaleza
	 * @param minsalud
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param estadoArticulo
	 * @return Colleccion con el resultado de la búsqueda
	 */
	public Collection buscar(Connection con, String clase, String grupo, String subgrupo, String codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estadoArticulo, boolean buscarEstado, String stockMinimo, String stockMaximo, String puntoPedido, String costoPromedio, String precioCompraMasAlta, String codigoInterfaz,String indicativoAutomatico,String indicativoPorCompletar, String cantidadCompra,int categoria,String fechaCreacion,String registroInvima, String maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, String porcentajeIva, String precioUltimaCompra, String precioBaseVenta, String fechaPrecioBaseVenta, int institucion, String descripcionAlterna, long consecutivoNivelAtencion)
	{
		return SqlBaseArticuloDao.buscar(con, clase, grupo, subgrupo, codigo, descripcion, naturaleza, minsalud, formaFarmaceutica, concentracion, unidadMedida, estadoArticulo, buscarEstado,stockMinimo,stockMaximo,puntoPedido,costoPromedio, precioCompraMasAlta, codigoInterfaz,indicativoAutomatico,indicativoPorCompletar,cantidadCompra,categoria,fechaCreacion,registroInvima,  maximaCantidadMes,   multidosis,  manejaLotes,  manejaFechaVencimiento,  porcentajeIva,  precioUltimaCompra,  precioBaseVenta,  fechaPrecioBaseVenta, institucion, descripcionAlterna, consecutivoNivelAtencion);
	}
	
	/**
	 * Adición sebastián
	 * Método usado para insertar un atributo de la justificación de un artículo
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo
	 * @param descripcion
	 * @return
	 */
	public int insertarAtributoJustificacion(Connection con,int numeroSolicitud,int articulo,int atributo,String descripcion)
	{
		return SqlBaseArticuloDao.insertarAtributoJustificacion(con,numeroSolicitud,articulo,atributo,descripcion);
	}
	

	/**
	 * 
	 */
	public boolean articuloManejaFechaVencimiento(Connection con, int codigoArticulo, int institucion)
	{
		return SqlBaseArticuloDao.articuloManejaFechaVencimiento(con,codigoArticulo,institucion);
	}

	/**
	 * 
	 */
	public boolean articuloManejaLote(Connection con, int codigoArticulo, int institucion)
	{
		return SqlBaseArticuloDao.articuloManejaLote(con,codigoArticulo,institucion);
	}
	

	public boolean insertarViasAdminArticulo(Connection con, HashMap campos) {
		return SqlBaseArticuloDao.insertarViasAdminArticulo(con, campos, "nextval('SEQ_VIA_ADM_ARTI')");
	}

	/**
	 * 
	 */
	public HashMap cargarViasAdminArticulo(Connection con, int codigo)
	{
		return SqlBaseArticuloDao.cargarViasAdminArticulo(con, codigo);
	}

	/**
	 * 
	 */
	public boolean actualizarViasAdminArticulo(Connection con, HashMap campos)
	{
		return SqlBaseArticuloDao.actualizarViasAdminArticulo(con, campos);
	}
	/////
	
	/**
	 * 
	 */
	public  HashMap cargarUnidosisArticulo(Connection con, int codigo)
	{
		return SqlBaseArticuloDao.cargarUnidosisArticulo(con, codigo);
	}
	
	/**
	 * 
	 */
	public boolean modificarTarifas(Connection con, double valor, int codigo, String tarifa) 
	{
		return SqlBaseArticuloDao.modificarTarifas(con, valor, codigo, tarifa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public String obtenerCodigoInterfazNaturalezaArticulo(Connection con, int codigoArticulo)
	{
		return SqlBaseArticuloDao.obtenerCodigoInterfazNaturalezaArticulo(con, codigoArticulo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarUbicacion (Connection con, Articulo a)
	{
		return SqlBaseArticuloDao.consultarUbicacion(con, a);
		
	}
	
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public boolean esMedicamento(int codigoArticulo)
	{
		String consulta="SELECT getesmedicamento(?)";
		return SqlBaseArticuloDao.esMedicamento(codigoArticulo,consulta);
	}
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public boolean esPos(int codigoArticulo)
	{
		return SqlBaseArticuloDao.esPos(codigoArticulo);
	}
	

	/**
	 * 
	 */
	public boolean esArticuloMultidosis(Connection con, int codigoArticulo) 
	{
		return SqlBaseArticuloDao.esArticuloMultidosis(con,codigoArticulo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaInfoMArticulo (Connection con, int codigoArticulo)	
	{
		return SqlBaseArticuloDao.consultaInfoMArticulo(con, codigoArticulo);
	}
	
	/**
	 * 		
	 */
	public HashMap<String, Object> consultaInfoMAdjArticulo (Connection con, int codigoArticulo)	
	{
		return SqlBaseArticuloDao.consultaInfoMAdjArticulo(con, codigoArticulo);
	}
	
	/**
	 * Metodo para eliminar un registro de docs asjuntos articulos info medica
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAdjIM(Connection con, String codigo)
	{
		return SqlBaseArticuloDao.eliminarAdjIM(con, codigo);
	}

	/**
	 * 
	 */
	public HashMap cargarGrupoEspecialesArticulo(Connection con, int codigoArticulo) 
	{
		return SqlBaseArticuloDao.cargarGrupoEspecialesArticulo(con, codigoArticulo);
	}
	
	/**
	 * 
	 */
	public String cargarDescripcionNivelAtencion(Connection con, int codigoArticulo) 
	{
		return SqlBaseArticuloDao.cargarDescripcionNivelAtencion(con, codigoArticulo);
	}

	/**
	 * Método encargado de consultar los 4 códigos minSalud para un articulo tipo medicamento
	 * @author Felipe Pérez Granda
	 * @param con Conexión 
	 * @param codigoArticulo El código del artículo
	 * @return HashMap<String,Object>
	 */
	public HashMap<String,Object> cargarMinSalud(Connection con, int codigoArticulo)
	{
		return SqlBaseArticuloDao.cargarMinSalud(con, codigoArticulo);
	}
	
	/**
	 * Método encargado de modificar la información medica de un artículo cuya naturaleza sea Medicamento
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param codigo
	 * @param tiempoEsp
	 * @param efectoDes
	 * @param efectosSec
	 * @param observaciones
	 * @param bibliografia
	 * @return true/false boolean
	 */
	public boolean modificarInfoMedicaArticulo(Connection con, int codigo, String tiempoEsp, String efectoDes, String efectosSec, 
			String observaciones, String bibliografia)
	{
		return SqlBaseArticuloDao.modificarInfoMedicaArticulo( con, codigo, tiempoEsp, efectoDes, efectosSec, observaciones, bibliografia);
	}
	
}
