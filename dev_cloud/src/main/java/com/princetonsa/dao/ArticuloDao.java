/*
 * Created on Nov 27, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.Answer;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Articulo;
/**
 * @author rcancino
 ** Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Articulo</code>.
 */
public interface ArticuloDao 
{
	
	/**
	 * 
	 * @param con
	 * @param subgrupoArticulo
	 * @param Naturaleza
	 * @param formaFarmaceutica
	 * @param concentracionArticulo
	 * @param unidadMedidaArticulo
	 * @param estadoArticulo
	 * @param minsalud
	 * @param descripcion
	 * @param fecha
	 * @param usuario
	 * @param horaCreacion
	 * @param stockMinimo
	 * @param stockMaximo
	 * @param puntPedido
	 * @param cantidadCompra
	 * @param costoPromedio
	 * @param costoDonacion
	 * @param codigoInterfaz
	 * @param indicativoAutomatico
	 * @param indicativoPorCompletar
	 * @param categoria
	 * @param registroINVIMA
	 * @param maximaCantidadMes
	 * @param multidosis
	 * @param manejaLotes
	 * @param manejaFechaVencimiento
	 * @param porcentajeIva
	 * @param precioUltimaCompra
	 * @param precioBaseVenta
	 * @param precioCompraMasAlta
	 * @param grupoEspecial
	 * @param fechaPrecioBaseVenta
	 * @param institucion
	 * @param mapaViaAdm
	 * @param mapaUnidosis
	 * @param cumMap
	 * @return
	 */
	public int insertarArticulo(Connection con, String subgrupoArticulo, String Naturaleza, String formaFarmaceutica,String concentracionArticulo, String unidadMedidaArticulo , boolean estadoArticulo,String minsalud, String descripcion, String fecha, String usuario, String horaCreacion, int stockMinimo, int stockMaximo, int puntPedido, int cantidadCompra, String costoPromedio, double costoDonacion, String codigoInterfaz, String indicativoAutomatico, String indicativoPorCompletar, int categoria, String registroINVIMA,double maximaCantidadMes, String multidosis, String manejaLotes,String manejaFechaVencimiento,double porcentajeIva,double precioUltimaCompra,double precioBaseVenta, double precioCompraMasAlta, String descripcionAlterna, String fechaPrecioBaseVenta,int institucion, HashMap mapaViaAdm, HashMap mapaUnidosis, HashMap mapaGrupoEspecial, HashMap cumMap, String atencionOdon, long consecutivoNivelAtencionArticulo);
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param articulo
	 * @return
	 */
	public boolean insertarInformMed(Connection con, int codArticulo, Articulo articulo);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public Answer cargarArticulo (Connection con, int codigoArticulo);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public ResultSetDecorator cargarArticuloDescripcion (Connection con, int codigoArticulo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param naturaleza
	 * @param minsalud
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param estado
	 * @param stockMinimo
	 * @param stockMaximo
	 * @param puntPedido
	 * @param cantidadCompra
	 * @param costoPromedio
	 * @param codigoInterfaz
	 * @param indicativoAutomatico
	 * @param indicativoPorCompletar
	 * @param categoria
	 * @param registroInvima
	 * @param maximaCantidadMes
	 * @param multidosis
	 * @param manejaLotes
	 * @param manejaFechaVencimiento
	 * @param porcentajeIva
	 * @param precioUltimaCompra
	 * @param precioBaseVenta
	 * @param precioCompraMasAlta
	 * @param fechaPrecioBaseVenta
	 * @param institucion
	 * @param manejoLoteDeSaN
	 * @param mapaViaAdm
	 * @param mapaUnidosis
	 * @param usuario
	 * @return
	 */
	public int modificarArticulo(Connection con, int codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estado, int stockMinimo, int stockMaximo, int puntPedido, int cantidadCompra, String costoPromedio,String codigoInterfaz,String indicativoAutomatico,String indicativoPorCompletar, int categoria, String registroInvima, double maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, double porcentajeIva, double precioUltimaCompra, double precioBaseVenta, double precioCompraMasAlta, String descripcionAlterna, String fechaPrecioBaseVenta,int institucion, boolean manejoLoteDeSaN, HashMap mapaViaAdm, HashMap mapaUnidosis, HashMap mapaGrupoEspecial, String usuario, HashMap cumMap, long nivelAtencion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param tiempoEsp
	 * @param efectoDes
	 * @param efectosSec
	 * @param observaciones
	 * @param bibliografia
	 * @return
	 */
	public int modificarInfoMArticulo(Connection con, int codigo, String tiempoEsp, String efectoDes, String efectosSec, String observaciones, String bibliografia);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param estadoArticulo
	 * @return
	 */
	public int cambiarEstadoArticulo(Connection con, String codigoArticulo, boolean estadoArticulo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public int codigoUltimoArticulo(Connection con);
	
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
	 * @param cantidadCompra
	 * @param costoPromedio 
	 * @param puntoPedido
	 * @param stockMaximo
	 * @param stockMinimo
	 * @param registroInvima 
	 * @return Colleccion con el resultado de la búsqueda
	 */
	
	public Collection buscar(Connection con, String clase, String grupo, String subgrupo, String codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estadoArticulo, boolean buscarEstado, String stockMinimo, String stockMaximo, String puntoPedido, String costoPromedio, String precioCompraMasAlta, String codigoInterfaz,String indicativoAutomatico, String indicativoPorCompletar, String cantidadCompra,int categoria,String fechaCreacion, String registroInvima, String maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, String porcentajeIva, String precioUltimaCompra, String precioBaseVenta, String fechaPrecioBaseVenta, int institucion, String descripcionAlterna, long consecutivoNivelAtencion);
	
	
	
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
	public int insertarAtributoJustificacion(Connection con,int numeroSolicitud,int articulo,int atributo,String descripcion);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param institucion 
	 * @return
	 */
	public boolean articuloManejaLote(Connection con, int codigoArticulo, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param institucion 
	 * @return
	 */
	public boolean articuloManejaFechaVencimiento(Connection con, int codigoArticulo, int institucion);


	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public  boolean insertarViasAdminArticulo(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param valor
	 * @param codigo
	 * @param tarifa
	 * @return
	 */
	public boolean modificarTarifas(Connection con, double valor, int codigo, String tarifa);
	
	/**
	 * metodo para cargar la Grilla  de Vias de Administraciona
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarViasAdminArticulo(Connection con, int codigo);
	
	
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public HashMap consultarUbicacion(Connection con, Articulo a);
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean actualizarViasAdminArticulo(Connection con, HashMap campos);
	
	/**
	 * para cargar la Grilla  de Unidosis
	 * @param con
	 * @param codigo
	 * @return
	 */
	public  HashMap cargarUnidosisArticulo(Connection con, int codigo);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public String obtenerCodigoInterfazNaturalezaArticulo(Connection con, int codigoArticulo);
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public boolean esMedicamento(int codigoArticulo);
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public boolean esPos(int codigoArticulo);
	

	/**
	 * 
	 */
	public boolean esArticuloMultidosis(Connection con, int codigoArticulo);
	
	/**
	 *  
	 */
	public HashMap<String, Object> consultaInfoMArticulo (Connection con, int codigoArticulo);
	
	/**
	 *  
	 */
	public HashMap<String, Object> consultaInfoMAdjArticulo (Connection con, int codigoArticulo);
	
	/**
	 * Metodo para eliminar un documento adjunto de un articulo info medica
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAdjIM(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public HashMap cargarGrupoEspecialesArticulo(Connection con, int codigoArticulo);
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public String cargarDescripcionNivelAtencion(Connection con, int codigoArticulo);
	
	/**
	 * Método encargado de consultar los 4 códigos minSalud para un articulo tipo medicamento
	 * @author Felipe Pérez Granda
	 * @param con Conexión 
	 * @param codigoArticulo El código del artículo
	 * @return HashMap<String,Object>
	 */
	public HashMap<String,Object> cargarMinSalud(Connection con, int codigoArticulo);
	
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
			String observaciones, String bibliografia);
	
	
}
