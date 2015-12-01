/*
 * Created on Nov 27, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.Answer;

import com.princetonsa.dao.ArticuloDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseArticuloDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Articulo;

/**
 * @author rcancino
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OracleArticuloDao implements ArticuloDao {//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una cama en la base de datos PostgreSQL.
	 */
	private static final String insertarArticuloStr="INSERT INTO articulo (" +
														"codigo, " +//NEXVAL
														"subgrupo, " +//1
														"descripcion, " +//2
														"naturaleza, " +//3
														"minsalud, " +//4
														"forma_farmaceutica, " +//5
														"concentracion, " +//6
														"unidad_medida, " +//7
														"estado," +//8
														"fecha_modifica," +//9
														"usuario_modifica," +//10
														"hora_modifica," +//11
														"categoria," +//12
														"stock_minimo," +//13
														"stock_maximo," +//14
														"punto_pedido," +//15
														"cantidad_compra," +//16
														"costo_promedio," +//17
														"registro_invima," +//18
														"maxima_cantidad_mes," +//19
														"multidosis," +//20
														"maneja_lotes," +//21
														"maneja_fecha_vencimiento," +//22
														"porcentaje_iva," +//23
														"precio_ultima_compra," +//24
														"precio_base_venta," +//25
														"fecha_precio_base_venta," +//26numero_identificacion
														"institucion," +//27
														"costo_donacion," +//28
														"codigo_interfaz," +//29
														"indicativo_automatico," +//30
														"indicativo_por_completar," +//31
														"precio_compra_mas_alta," +//32
														"descripcion_alterna," +//33
														"numero_expediente," +//34
														"cons_present_comercial," +//35
														"presentacion_comercial," +//36
														"clasificacion_atc," +//37
														"registro_art," +//38
														"vigencia," +//39
														"roles_x_producto," +//40
														"titular," +//41
														"fabricante," +//42
														"importador," +//43
														"atencion_odontologica," + //44
														"nivel_atencion) " +//45
													"VALUES " +
														"(seq_articulo.nextval, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static final String seqViaAdmArticuloStr="SEQ_VIA_ADM_ARTI.nextval";
	
	/**
	 * 
	 */
	private static final String seqUnidadesArticuloStr="SEQ_UNID_X_ARTI.nextval";
	
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
		return SqlBaseArticuloDao.insertarArticulo( con, subgrupoArticulo,
				naturalezaArticulo, formaFarmaceutica,
				concentracionArticulo, unidadMedidaArticulo,
					estadoArticulo, minsalud,
					descripcion,
					fechaCreacion,
					usuario,
					horaCreacion,
					stockMinimo,
					stockMaximo,
					puntoPedido,
					cantidadCompra,
					costoPromedio,
					costoDonacion,
					codigoInterfaz,
					indicativoAutomatico,
					indicativoPorCompletar,
					categoria,
					registroInvima,
					
					 maximaCantidadMes,
					 multidosis,
					 manejaLotes,
					 manejaFechaVencimiento,
					 porcentajeIva,
					 precioUltimaCompra,
					 precioBaseVenta,
					 precioCompraMasAlta,
					 fechaPrecioBaseVenta,
					 descripcionAlterna,
					 institucion,					
					insertarArticuloStr,
					seqViaAdmArticuloStr,
					seqUnidadesArticuloStr,
					mapaViaAdm,
					mapaUnidosis,
					mapaGrupoEspecial,
					cumMap,
					atencionOdon,
					consecutivoNivelAtencionArticulo);
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
	public int modificarArticulo(Connection con, int codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estado, int stockMinimo, int stockMaximo, int puntoPedido, int cantidadCompra, String costoPromedio,String codigoInterfaz,String indicativoAutomatico,String indicativoPorCompletar, int categoria,String registroInvima, double maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, double porcentajeIva, double precioUltimaCompra, double precioBaseVenta, double precioCompraMasAlta, String descripcionAlterna, String fechaPrecioBaseVenta,int institucion, boolean manejoLoteDeSaN, HashMap mapaViaAdm, HashMap mapaUnidosis, HashMap mapaGrupoEspecial, String usuario, HashMap cumMap, long nivelAtencion)
	{
		return SqlBaseArticuloDao.modificarArticulo( con, naturaleza,  formaFarmaceutica,  concentracion,  unidadMedida,  estado,  minsalud,  descripcion, codigo,stockMinimo,stockMaximo,puntoPedido,cantidadCompra,costoPromedio,codigoInterfaz,indicativoAutomatico,indicativoPorCompletar,categoria,registroInvima, maximaCantidadMes,   multidosis, manejaLotes, manejaFechaVencimiento, porcentajeIva, precioUltimaCompra, precioBaseVenta, precioCompraMasAlta, descripcionAlterna, fechaPrecioBaseVenta, institucion, manejoLoteDeSaN, mapaViaAdm, mapaUnidosis, mapaGrupoEspecial, usuario, cumMap,DaoFactory.ORACLE, seqUnidadesArticuloStr,nivelAtencion);
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
	public Collection buscar(Connection con, String clase, String grupo, String subgrupo, String codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estadoArticulo, boolean buscarEstado, String stockMinimo, String stockMaximo, String puntoPedido, String costoPromedio, String precioCompraMasAlta, String codigoInterfaz,String indicativoAutomatico,String IndicativoPorCompletar, String cantidadCompra,int categoria,String fechaCreacion,String registroInvima, String maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, String porcentajeIva, String precioUltimaCompra, String precioBaseVenta, String fechaPrecioBaseVenta, int institucion, String descripcionAlterna, long consecutivoNivelAtencion)
	{
		return SqlBaseArticuloDao.buscar(con, clase, grupo, subgrupo, codigo, descripcion, naturaleza, minsalud, formaFarmaceutica, concentracion, unidadMedida, estadoArticulo, buscarEstado,stockMinimo,stockMaximo,puntoPedido,costoPromedio, precioCompraMasAlta, codigoInterfaz,indicativoAutomatico,IndicativoPorCompletar,cantidadCompra,categoria,fechaCreacion,registroInvima,  maximaCantidadMes,   multidosis,  manejaLotes,  manejaFechaVencimiento,  porcentajeIva,  precioUltimaCompra,  precioBaseVenta,  fechaPrecioBaseVenta, institucion, descripcionAlterna, consecutivoNivelAtencion);
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
	
	/**
	 * 
	 */
	public boolean insertarViasAdminArticulo(Connection con, HashMap campos) {
		return SqlBaseArticuloDao.insertarViasAdminArticulo(con, campos, "SEQ_VIA_ADM_ARTI.NEXTVAL");
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
	
	/////////////////
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
	public boolean modificarTarifas(Connection con, double valor, int codigo, String tarifa) {
		
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
		String consulta="SELECT getesmedicamento(?) from dual";
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
