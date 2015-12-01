/*
 * Created on 6/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * 
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

import com.princetonsa.mundo.PersonaBasica;

/**
 * @version 1.0, 6/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface UtilidadInventariosDao
{

    
    /**
     * @param con
     * @param institucion
     * @param incluirAlmacenTodos 
     * @return
     */
    public abstract HashMap listadoAlmacensActivos(Connection con, int institucion, boolean incluirAlmacenTodos);

    

    /**
     * metodo que actualiza las existencias articulos x almacen tanto para
     * ENTRADA - SALIDA manejado con el parametro boolean esCantidadASumar,
     * no se reutiliza el metodo actualizarExistenciasArticuloAlmacen(con, int, int, int, int)
     * para mantenerlo en un solo execute
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param esCantidadASumar
     * @param cantidadArticulo
     * @param institucion
     * @return boolean 
     */
    public abstract boolean actualizarExistenciasArticuloAlmacen(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion);
    
    
    /**
     * metodo que evalua si un articulo ya esta insertado en articulos_almacen
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public abstract boolean existeArticuloEnExistenciasXAlmacen(Connection con, int codArticulo, int codAlmacen, int codInstitucion );
    
    /**
	 * MT 2353:Método que devuelve el mapa con lote, fechaVencimiento y existencias para saber si se creo solo 
	 * para registrar una existencia en lote='',fechaVencimiento ='' y existencias=0; y si se debe reemplazar
	 * al crear una transaccion sobre ese mismo articulo/almacen 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @return mapa 
	 */
	public abstract HashMap obtenerArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen);
    
    /**
     * @param con
     * @param codArticulo
     * @param nuevoCosto
     * @return
     */
    public abstract boolean actualizarCostoPromedioArticulo(Connection con, int codArticulo, double nuevoCosto);


    
    /**
     * @param con
     * @param institucion
     * @param centroCosto
     * @param isIn
     * @param rest
     * @return
     */
    public abstract HashMap transaccionesValidasCentroCosto(Connection con, int institucion, int centroCosto, Vector rest, boolean isIn);


    
    /**
     * @param con
     * @param institucion
     * @param login
     * @param filtroCentroAtencion
     * @return
     */
    public abstract HashMap listadoAlmacenesUsuarios(Connection con, int institucion, String login,int filtroCentroAtencion);

    
    /**
     * 
     * @param con
     * @param institucion
     * @param centroCostoSolicitante
     * @param tipoTransaccion
     * @param paresClaseGrupo
     * @param planEspecial
     * @return
     */
    public HashMap<Object, Object> listadoAlmacenes(	Connection con, 
    													int institucion, 
    													int centroCostoSolicitante,  
    													String tipoTransaccion, 
    													String paresClaseGrupo, 
    													boolean planEspecial);
    
    /**
     * Metodo para cargar los almacenes validos, validando que el usuario pertenezca al almacen y ademas
     *  filtrando por las transacciones validas por centro de costo. Tambien se carga el detalle de la info de
     * las clases inventario x centro costo
     * @param con
     * @param institucion
     * @param login
     * @param tipoTransaccion
     * @param pares clase-grupo (si va vacio no se valida)
     * @return
     */
    public abstract HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(Connection con, int institucion, String login,String tipoTransaccion, String paresClaseGrupo, int filtroCentroAtencion, boolean planEspecial);
    
    
    
    
    /**
     * Metodo que carga la informacion de las clases inventario restringiendo por el centro de costo
     * de las transacciones validas
     * @param con
     * @param institucion
     * @param codigoAlmacen
     * @param paresClaseGrupo
     * @return
     */
    public abstract HashMap cargarInfoClasesInventario(Connection con, int institucion, int codigoAlmacen,String paresClaseGrupo);
    
    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public abstract int existenciasTotalesArticulo(Connection con, int codArticulo, int institucion);


    
    /**
     * @param con
     * @param codArticulo
     * @param almacen
     * @param institucion
     * @return
     */
    public abstract int existenciasArticuloAlmacen(Connection con, int codArticulo, int almacen, int institucion);


    
    /**
     * @param con
     * @param codArticulo
     * @param nuevoValor
     * @param porcentajeAlerta
     * @return
     */
    public abstract boolean esValorValidoCostoArticulo(Connection con, int codArticulo, double nuevoValor, double porcentajeAlerta);


    
    /**
     * @param con
     * @param codArticulo
     * @return
     */
    public abstract double costoActualArticulo(Connection con, int codArticulo);


    
    /**
     * @param con
     * @param codArticulo
     * @param numeroUnidades
     * @param valorTotalTransacion
     * @param tipoTransaccion
     * @param institucion
     * @param codigoAlmacen
     * @return
     */
    public abstract double calcularCostoPromedioArticulo(Connection con, int codArticulo, int numeroUnidades, double valorTotalTransacion, int tipoTransaccion, int institucion, int codigoAlmacen);


    
    /**
     * @param con
     * @param fecha
     * @param institucion
     * @return
     */
    public abstract boolean existeCierreInventarioParaFecha(Connection con, String fecha, int institucion);


    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public abstract boolean existenciasArticuloMayorIgualStockMinimo(Connection con, int codArticulo, int institucion);


    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public abstract boolean existenciasArticuloMenorIgualStockMaximo(Connection con, int codArticulo, int institucion);


    
    /**
     * @param con
     * @param codArticulo
     * @param institucion
     * @return
     */
    public abstract boolean existenciasArticuloMayorIgualPuntoPedido(Connection con, int codArticulo, int institucion);
    
    
    /**
     * Método que obtiene las parejas de clase-grupo para la 
     * interseccion centro costo y almacen
     * @param con
     * @param centroCosto
     * @param almacen
     * @return
     * El formato es String "'clase-grupo','clase-grupo',...."
     */
    public abstract String obtenerInterseccionClaseGrupo(Connection con,int centroCosto,int almacen);
    
    /**
     * Método que verifica 
     * @param con
     * @param usuario
     * @param centroCosto
     * @param codigoIns
     * @return
     */
    public abstract boolean esAlmacenUsuarioAutorizado(Connection con,String usuario,int centroCosto, int codigoInstitucion);


    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public abstract int obtenerAnioUltimoCierre(Connection con, int institucion);


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public abstract int obtenerMesUltimoCierreAnio(Connection con, int institucion,int anio);


    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public abstract String fechaCierreInicial(Connection con, int institucion);


    
    /**
     * @param con
     * @param institucion
     * @param fecha
     * @return
     */
    public abstract boolean existenCierresAnterioresAFecha(Connection con, int institucion, String fecha);


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public abstract boolean existeCierreFinalAnio(Connection con, int institucion, String anio);


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public abstract String existeCierreInicialFecha(Connection con, int institucion, String anio);


    
    /**
     * @param con
     * @param institucion
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public abstract int numeroMovimientosInventariosEntreFecha(Connection con, int institucion, String fechaInicial, String fechaFinal);


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public abstract boolean existeCierreFinalesPosterioresAnio(Connection con, int institucion, String anio);


    /**
     * 
     * @param con
     * @param institucion
     * @param articulo
     * @param anio
     * @param mes
     * @return
     */
	public abstract String obtenerCostoUnitarioFinalMes(Connection con, int institucion, int articulo, String anio, String mes);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public abstract double obtenerValorEntradaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public abstract double obtenerValorSalidaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public abstract int obtenerCantidadEntradaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @return
	 */
	public abstract int obtenerCantidadSalidaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public abstract double obtenerValorEntradaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public abstract double obtenerValorSalidaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public abstract int obtenerCantidadEntradaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public abstract int obtenerCantidadSalidaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param mesCierre
	 * @param anioCierre
	 * @return
	 */
	public abstract String obtenerCodigoCierreInventario(Connection con, int institucion, String mesCierre, String anioCierre);

	/**
	 * metodo para consultar el costo del saldo,
	 * toma el ultimo regisrro desde la fecha/hora
	 * del movimiento que se esta relacionando.
	 * @param con Connection
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String
	 * @param numeroTransaccion String
	 * @return  String
	 */
	public abstract String obtenerCostoUnitarioKardex(Connection con, int institucion, int articulo, String fecha,String numeroTransaccion);
	/**
	 * metodo para consultar el costo del saldo,
	 * ele ultimo registro desde el dia anterior,
	 * toma el ultimo regisrro desde la fecha/hora	 
	 * @param con Connection
	 * @param institucion int
	 * @param articulo int
	 * @param fecha String	 
	 * @return  String
	 */
	public abstract String obtenerCostoUnitarioKardex(Connection con, int institucion, int articulo, String fecha);
	
	/**
	 * metodo para obtener el area de un 
	 * centro de costo
	 * @param con Connection
	 * @param codCentroCosto int
	 * @param institucion int 
	 * @return int, area del centro de costo
	 */
	public abstract int obtenerTipoAreaCentroCosto(Connection con,int codCentroCosto,int institucion);
	/**
	 * metodo para validar si un almacen es
	 * valido para realizar traslado
	 * @param con Connection
	 * @param institucion int
	 * @param tipoTransaccion int
	 * @param centroCosto int
	 * @return boolean
	 */
	public abstract boolean esAlmacenAutorizadoParaTraslado(Connection con,int institucion,int tipoTransaccion,int centroCosto);
	/**
	 * metodo para cargar el listado de clase-grupo, de 
	 * un usuario segun su centro de costo, y un tipo
	 * de transacción definido.
	 * @param con Connection
	 * @param institucion int
	 * @param centroCosto int
	 * @param transaccion int
	 * @return HashMap
	 */
	public abstract HashMap listadoClaseGrupoSegunCentroCostoUsuario(Connection con,int institucion,int centroCosto,int transaccion);
	/**
     * Metodo para cargar los almacenes validos, validando 
     * que el usuario pertenezca al almacen y ademas filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
     * @param con Connection
     * @param institucion int     
     * @param tipoTransaccion String
     * @param pares clase-grupo (si va vacio no se valida) String
     * @param centroCosto int
     * @return HashMap
     */
    public abstract HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(Connection con,int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto);

	/**
     * Metodo para cargar los almacenes validos, filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
     * @param con Connection
	 * @param institucion int
	 * @param tipoTransaccion String
	 * @param centroCosto int
	 * @param login @todo
	 * @param login String
	 * @param pares clase-grupo (si va vacio no se valida) String
     * @return HashMap
     */
    public abstract HashMap listadoAlmacenesUsuariosXCentroAtencion(Connection con,int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto, int centroAtencion, String login);

    /**
     * metodo que devuelve el numero de existencias (- o +) de un articulo, en caso de que genere error
     * devuelve un null
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public abstract String getExistenciasXArticulo(Connection con, int  codArticulo, int codAlmacen,int codInstitucion);
    /**
     * metodo para generar el listado de las 
     * transacciones validas por centro de costo, 
     * filtrando las que no se encuentren definidas
     * en parametros generales, y agrupando por la 
     * descripción de la tipo transacciónes.
     * @param con Connection
     * @param institucion int
     * @param centroCosto int
     * @return HashMap
     */
    public abstract HashMap transaccionesValidasCentroCostoAgrupados(Connection con, int institucion, int centroCosto,Vector restCodigoValDef,boolean esIn);


    /**
     * 
     * @param con
     * @param institucion
     * @param centroCosto
     * @param codigoTransaccion
     * @return
     */
	public abstract String obtenerParejasClaseGrupoInventario(Connection con, int institucion, int centroCosto, String codigoTransaccion);



	/**
	 * No Actualiza tambien la existencias por almacen (Maestro).
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @param esCantidadASumar
	 * @param cantidadArticulo
	 * @param institucion
	 * @param lote
	 * @param fechaVencimiento
	 */
	public abstract boolean actualizarExistenciasArticuloAlmacenLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento);

	/**
	 * Metodo que solo actualiza 'articulo_almacen_x_lote' cuando ya se encuentra un registro
	 * inicializado con lote=null , fecha_vencimiento=null , existencias=0
	 * desde [Ubicacion Articulos Almacen]
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @param esCantidadASumar
	 * @param cantidadArticulo
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract boolean actualizarArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento);

	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param almacen
	 * @param institucion
	 * @return
	 */
	public abstract int existenciasArticuloAlmacenLote(Connection con, int codArticulo, int almacen,String lote, String fechaVencimiento);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
    public abstract HashMap obtenerLotesDespachoNoAdministradosTotalmente(Connection con, String numeroSolicitud, String codigoArticulo);



    /**
     * 
     * @param con
     * @param institucion
     * @param almacen
     * @param articulo
     * @param anio
     * @param mes
     * @param lote
     * @param fechaVencimiento
     * @return
     */
	public abstract double obtenerValorEntradaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract double obtenerValorSalidaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento);


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract int obtenerCantidadEntradaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param mes
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract int obtenerCantidadSalidaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract double obtenerValorEntradaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract double obtenerValorSalidaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract int obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract int obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento);
	
	/**
	 * Método que consulta el nombre de una via de administracion a partir
	 * de su consecutivo de la tabla vias_admin_institucion
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract String obtenerNombreViaAdminInstitucion(Connection con,int consecutivo);



	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @return
	 */
	public abstract int obtenerTipoCierre(Connection con, String codigoCierre);



	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public abstract String obtenerPrecioUltimaCompraArticulo(Connection con, int articulo);



	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public abstract String obtenerPrecioBaseVentaArticulo(Connection con, int articulo);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap obtenerAlmacenesConsignacion(Connection con, int institucion);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap obtenerConveniosProveedor(Connection con, int institucion,int articulo);



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap obtenerProveedoresCatalogo(Connection con, int institucion);



	/**
	 * 
	 * @param con
	 * @param codTransInterfaz
	 * @return
	 */
	public abstract int obtenerTipoTransaccionInterfaz(Connection con, int codTransInterfaz,int institucion);



	/**
	 * 
	 * @param con
	 * @param tipoTransaccion
	 * @param institucion
	 * @return
	 */
	public abstract String obtenerNombreTipoTransaccion(Connection con, int tipoTransaccion, int institucion);



	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public abstract double obtenerValorArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo);



	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public abstract double obtenerValorArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo);




	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param valorUnitario
	 * @return
	 */
	public abstract boolean actualizarPrecioUltimaCompra(Connection con, int codigo, double valorUnitario);



	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public abstract String obtenerCodigoInterfazArticulo(Connection con, int codigoArticulo);



	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public abstract double obtenerValorIvaArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo);



	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public abstract double obtenerValorIvaArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo);



	/**
	 * 
	 * @param con
	 * @param pedido
	 * @return
	 */
	public abstract int obtenerNumeroPeticionPedidoQX(Connection con, int pedido);

	
	/**
	 * Método que verifica si a una solicitud de cargos directos artículos le afectan los inventarios
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public abstract boolean afectaInventariosSolicitud(Connection con,String numeroSolicitud);



	public abstract HashMap obtenerClasesInventario(Connection con, int institucion);



	public abstract HashMap obtenerGrupoInventario(Connection con, int institucion);
	
	/**
	 * Metodo que valida el tiempo de tratamiento para la justificacion NOPOS
	 * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia 
	 * @param paciente
	 * @return
	 */
	public abstract boolean obtenerValidarTiempoTratamiento(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente);

	
	/**
	 * Metodo que retorna el codigo de la justificacion NOPOS para asociar
	 * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia
	 * @param paciente
	 * @return
	 */
	
	public abstract int obtenerJustificacion(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente);  
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public abstract double obtenerValorCompraMasAlta(Connection con, int articulo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param valorUnitario
	 * @return
	 */
	public abstract boolean actualizarPrecioCompraMasAlta(Connection con, int codigo, double valorUnitario);


	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @return
	 */
	public abstract int existeAlmacenPlanEspecial(Connection con, int codigoCentroAtencion);

	
	/***********************************************************************
	 * Metodo encargado de consultar las clases de inventario
	 * pudiendo filtrar por los siguiente criterios:
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion -->  Requerido
	 * -- codigo	--> Opcional
	 * -- cuentaInventario	--> Opcional
	 * -- centroCosto	--> Opcional
	 * @return ArrayList<HashMap>
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo,nombre,cuentaInventario,
	 * cuentaCosto
	 **************************************************************************/
	public ArrayList<HashMap<String, Object>> obtenerClaseInventario (Connection connection,HashMap criterios);


	/**
	 * Método que retorna el código axioma de un artículo
	 * según el código interfaz
	 * @param con
	 * @param codigoInterfaz
	 * @return
	 */
	public abstract String obtenerCodigoDadoCodigoInterfaz(Connection con, String codigoInterfaz);
	
	/**
	 * Método para obtener las clases de inventario que aplican para un centro de costo
	 * según el tipo de transacción
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract ArrayList<InfoDatosInt> obtenerClasesInventarioTransValidasCentroCosto(Connection con,HashMap campos);

	/**
	 * Método que devuelve la Naturaleza de un 
	 * artículo determinado
	 * @param con
	 * @param articulo
	 * @return
	 */
	public abstract boolean esPosArticulo(Connection con, int articulo, int institucion);

	/**
	 * Metodo que devuelve el numero del consecutivo de una transaccion segun el codigo
	 * @param con
	 * @param string
	 * @return
	 */
	public abstract String obtenerConsecutivoTransaccion(Connection con, String codigoTransaccion);
	
	/**
	 * Método para obtener las farmacias x centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract ArrayList<HashMap<String, Object>> obtenerFarmaciasXCentroCosto(Connection con,HashMap<String, Object> campos);

	/**
	 * Método que indica si un articulo es o no medicamento
	 * @param con
	 * @param codigoArticulo
	 * @param codigoInstitucionInt
	 */
	public abstract boolean esMedicamento(Connection con, int codigoArticulo, int codigoInstitucion);
	
	/**
	 * @author Víctor Gómez
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public abstract ArrayList<HashMap<String, Object>> obtenerClasesInventarioArray(Connection con, int institucion);
	
	public boolean actualizarExistenciasArticuloAlmacenLoteAnulacion(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento,String numeroSolciitud,String loteDetalle,HashMap<String,Object> detalleSolicitud,Integer index);
	
	
}