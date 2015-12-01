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
package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

import com.princetonsa.dao.inventarios.UtilidadInventariosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseUtilidadInventariosDao;
import com.princetonsa.mundo.PersonaBasica;


/**
 * @version 1.0, 6/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class OracleUtilidadInventariosDao implements UtilidadInventariosDao
{
    /**
     * @param con
     * @param institucion
     * 
     * @return
     */
    public HashMap listadoAlmacensActivos(Connection con, int institucion,boolean incluirAlmacenTodos)
    {
        return SqlBaseUtilidadInventariosDao.listadoAlmacensActivos(con,institucion,incluirAlmacenTodos);
    }

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
    public boolean actualizarExistenciasArticuloAlmacen(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.actualizarExistenciasArticuloAlmacen(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, institucion);
    }
    
    
    /**
     * metodo que evalua si un articulo ya esta insertado en articulos_almacen
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public boolean existeArticuloEnExistenciasXAlmacen(Connection con, int codArticulo, int codAlmacen, int codInstitucion )
    {
        return SqlBaseUtilidadInventariosDao.existeArticuloEnExistenciasXAlmacen(con, codArticulo, codAlmacen, codInstitucion);
    }
    
    /**
	 * MT 2353:Método que devuelve el mapa con lote, fechaVencimiento y existencias para saber si se creo solo 
	 * para registrar una existencia en lote='',fechaVencimiento ='' y existencias=0; y si se debe reemplazar
	 * al crear una transaccion sobre ese mismo articulo/almacen 
	 * @param con
	 * @param codArticulo
	 * @param codAlmacen
	 * @return mapa 
	 */
	public HashMap obtenerArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen){
		return SqlBaseUtilidadInventariosDao.obtenerArticuloAlmacenXLote(con, codArticulo, codAlmacen);
	}
    
    /**
     * @param con
     * @param codArticulo
     * @param nuevoCosto
     * @return
     */
    public boolean actualizarCostoPromedioArticulo(Connection con, int codArticulo, double nuevoCosto)
    {
        return SqlBaseUtilidadInventariosDao.actualizarCostoPromedioArticulo(con,codArticulo,nuevoCosto);
    }


    
    /**
     * @param con
     * @param institucion
     * @param centroCosto
     * @return
     */
    public HashMap transaccionesValidasCentroCosto(Connection con, int institucion, int centroCosto,Vector rest,boolean isIn)
    {
        return SqlBaseUtilidadInventariosDao.transaccionesValidasCentroCosto(con,institucion,centroCosto,rest,isIn);
    }


    
    /**
     * @param con
     * @param institucion
     * @param login
     * @param filtroCentroAtencion
     * @return
     */
    public HashMap listadoAlmacenesUsuarios(Connection con, int institucion, String login,int filtroCentroAtencion)
    {
        return SqlBaseUtilidadInventariosDao.listadoAlmacenesUsuarios(con,institucion,login,filtroCentroAtencion);
    }

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
    													boolean planEspecial)
    {
    	return SqlBaseUtilidadInventariosDao.listadoAlmacenes(con, institucion, centroCostoSolicitante, tipoTransaccion, paresClaseGrupo, planEspecial);
    }
    
    
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
    public HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(Connection con, int institucion, String login,String tipoTransaccion,String paresClaseGrupo, int filtroCentroAtencion, boolean planEspecial)
    {
        return SqlBaseUtilidadInventariosDao.listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(con, institucion, login, tipoTransaccion, paresClaseGrupo, filtroCentroAtencion, planEspecial);
    }
    
    
    /**
     * Metodo que carga la informacion de las clases inventario restringiendo por el centro de costo
     * de las transacciones validas
     * @param con
     * @param institucion
     * @param codigoAlmacen
     * @param paresClaseGrupo
     * @return
     */
    public HashMap cargarInfoClasesInventario(Connection con, int institucion, int codigoAlmacen,String paresClaseGrupo)
    {
        return SqlBaseUtilidadInventariosDao.cargarInfoClasesInventario(con, institucion, codigoAlmacen, paresClaseGrupo,"");
    }
    
    
    /**
     * @param con
     * @param codArticulo
     * @return
     */
    public int existenciasTotalesArticulo(Connection con, int codArticulo,int institucion)
    {
        return SqlBaseUtilidadInventariosDao.existenciasTotalesArticulo(con,codArticulo,institucion);
    }


    
    /**
     * @param con
     * @param codArticulo
     * @param almacen
     * @param institucion
     * @return
     */
    public int existenciasArticuloAlmacen(Connection con, int codArticulo, int almacen, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.existenciasArticuloAlmacen(con,codArticulo,almacen,institucion);
    }

    /**
     * @param con
     * @param codArticulo
     * @param nuevoValor
     * @param porcentajeAlerta
     * @return
     */
    public boolean esValorValidoCostoArticulo(Connection con, int codArticulo, double nuevoValor, double porcentajeAlerta)
    {
        return SqlBaseUtilidadInventariosDao.esValorValidoCostoArticulo(con,codArticulo,nuevoValor,porcentajeAlerta);
    }


    
    /**
     * @param con
     * @param codArticulo
     * @return
     */
    public double costoActualArticulo(Connection con, int codArticulo)
    {
        return SqlBaseUtilidadInventariosDao.costoActualArticulo(con,codArticulo);
    }


    
    /**
     * @see com.princetonsa.dao.inventarios.UtilidadInventariosDao#calcularCostoPromedioArticulo(java.sql.Connection, int, int, double, int, int, int)
     */
    public double calcularCostoPromedioArticulo(Connection con, int codArticulo, int numeroUnidades, double valorTotalTransacion, int tipoTransaccion, int institucion, int codigoAlmacen)
    {
        return SqlBaseUtilidadInventariosDao.calcularCostoPromedioArticulo(con,codArticulo,numeroUnidades,valorTotalTransacion,tipoTransaccion,institucion,codigoAlmacen);
    }


    
    /**
     * @param con
     * @param fecha
     * @return
     */
    public boolean existeCierreInventarioParaFecha(Connection con, String fecha, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.existeCierreInventarioParaFecha(con,fecha,institucion);
    }


    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public String fechaCierreInicial(Connection con, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.fechaCierreInicial(con,institucion);
    }
    
    
    /**
     * @param con
     * @param codArticulo
     * @return
     */
    public boolean existenciasArticuloMayorIgualStockMinimo(Connection con, int codArticulo, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.existenciasArticuloMayorIgualStockMinimo(con,codArticulo,institucion);
    }


    
    /** 
     * @param con
     * @param codArticulo
     * @return
     */
    public boolean existenciasArticuloMenorIgualStockMaximo(Connection con, int codArticulo, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.existenciasArticuloMenorIgualStockMaximo(con, codArticulo,institucion);
    }


    
    /**
     * @param con
     * @param codArticulo
     * @return
     */
    public boolean existenciasArticuloMayorIgualPuntoPedido(Connection con, int codArticulo, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.existenciasArticuloMayorIgualPuntoPedido(con, codArticulo,institucion);
    }
    
    
    /**
     * Método que obtiene las parejas de clase-grupo para la 
     * interseccion centro costo y almacen
     * @param con
     * @param centroCosto
     * @param almacen
     * @return
     * El formato es String "'clase-grupo','clase-grupo',...."
     */
    public String obtenerInterseccionClaseGrupo(Connection con,int centroCosto,int almacen)
    {
    	return SqlBaseUtilidadInventariosDao.obtenerInterseccionClaseGrupo(con,centroCosto,almacen);
    }
    
    /**
     * Método que verifica 
     * @param con
     * @param usuario
     * @param centroCosto
     * @return
     */
    public boolean esAlmacenUsuarioAutorizado(Connection con,String usuario,int centroCosto, int codInstitucion)
    {
    	return SqlBaseUtilidadInventariosDao.esAlmacenUsuarioAutorizado(con,usuario,centroCosto, codInstitucion);
    }


    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public int obtenerAnioUltimoCierre(Connection con, int institucion)
    {
        return SqlBaseUtilidadInventariosDao.obtenerAnioUltimoCierre(con,institucion);
    }
    
    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public int obtenerMesUltimoCierreAnio(Connection con, int institucion,int anio)
    {
        return SqlBaseUtilidadInventariosDao.obtenerMesUltimoCierreAnio(con,institucion,anio);
    }
    

    
    /**
     * @param con
     * @param institucion
     * @param fecha
     * @return
     */
    public boolean existenCierresAnterioresAFecha(Connection con, int institucion, String fecha)
    {
        return SqlBaseUtilidadInventariosDao.existenCierresAnterioresAFecha(con,institucion,fecha);
    }
    


    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public boolean existeCierreFinalAnio(Connection con, int institucion, String anio)
    {
        return SqlBaseUtilidadInventariosDao.existeCierreFinalAnio(con,institucion,anio);
    }
    

    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public String existeCierreInicialFecha(Connection con, int institucion, String anio)
    {
        return SqlBaseUtilidadInventariosDao.existeCierreInicialFecha(con,institucion,anio);
    }
    
    
    /**
     * @param con
     * @param institucion
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public int numeroMovimientosInventariosEntreFecha(Connection con, int institucion, String fechaInicial, String fechaFinal)
    {
        return SqlBaseUtilidadInventariosDao.numeroMovimientosInventariosEntreFecha(con,institucion,fechaInicial,fechaFinal);
    }
    
    
    
    /**
     * @param con
     * @param institucion
     * @param anio
     * @return
     */
    public boolean existeCierreFinalesPosterioresAnio(Connection con, int institucion, String anio)
    {
        return SqlBaseUtilidadInventariosDao.existeCierreFinalesPosterioresAnio(con,institucion,anio);
    }
    
    /**
     * 
     * @param con
     * @param institucion
     * @param articulo
     * @param anio
     * @param mes
     * @return
     */
	public String obtenerCostoUnitarioFinalMes(Connection con, int institucion, int articulo, String anio, String mes)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCostoUnitarioFinalMes(con,institucion,articulo,anio,mes);
	}
	

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
	public double obtenerValorEntradaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorEntradaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	}
	
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
	public double obtenerValorSalidaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorSalidaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	}
    
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
	public int obtenerCantidadEntradaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadEntradaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	}
	

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
	public int obtenerCantidadSalidaCierreMesAnio(Connection con, int institucion, int almacen, int articulo, String anio, String mes)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadSalidaCierreMesAnio(con,institucion,almacen,articulo,anio,mes);
	}
	


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public double obtenerValorEntradaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorEntradaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public double obtenerValorSalidaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorSalidaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public int obtenerCantidadEntradaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadEntradaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	}
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param almacen
	 * @param articulo
	 * @param anio
	 * @return
	 */
	public int obtenerCantidadSalidaCierreMesAnioTotal(Connection con, int institucion, int almacen, int articulo, String anio)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadSalidaCierreMesAnioTotal(con,institucion,almacen,articulo,anio);
	}
	


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param mesCierre
	 * @param anioCierre
	 * @return
	 */
	public String obtenerCodigoCierreInventario(Connection con, int institucion, String mesCierre, String anioCierre)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCodigoCierreInventario(con,institucion,mesCierre,anioCierre);
	}
	

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
	public String obtenerCostoUnitarioKardex(Connection con, int institucion, int articulo, String fecha,String numeroTransaccion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCostoUnitarioKardex(con,institucion,articulo,fecha,numeroTransaccion);
	}
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
	public String obtenerCostoUnitarioKardex(Connection con, int institucion, int articulo, String fecha)
	{
		String strConsultaCostoUnitarioKardexUM="SELECT " + 
    			"case when t.costounitario is null then '0' else t.costounitario end as valor " +
    			"FROM ( " + 
    			"SELECT txa.codigo " + 
    		    "||'' AS codigomovimiento, " + 
    		    "tti.codigo " + 
    		    "||'' AS codigotransaccion, " + 
    		    "tti.descripcion " + 
    		    "||'' AS desctransaccion, " + 
    		    "txa.consecutivo " + 
    		    "||'' AS documento, " + 
    		    "TO_CHAR(txa.fecha_elaboracion,'yyyy-mm-dd') " + 
    		    "||'' AS fechaelaboracion, " + 
    		    "TO_CHAR(txa.fecha_cierre,'yyyy-mm-dd') " + 
    		    "||'' AS fechaatencion, " + 
    		    "txa.hora_cierre " + 
    		    "|| '' AS horaatencion, " + 
    		    "txa.almacen " + 
    		    "||'' AS almacen, " + 
    		    "''   AS almacendestino, " + 
    		    "dtxa.articulo " + 
    		    "||''                   AS articulo, " + 
    		    "dtxa.lote              AS lote, " + 
    		    "dtxa.fecha_vencimiento AS fechavencimiento, " + 
    		    "dtxa.val_unitario " + 
    		    "||'' AS costounitario, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=1 " + 
    		      "THEN dtxa.cantidad " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' AS cantidadentrada, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=1 " + 
    		      "THEN dtxa.cantidad*dtxa.val_unitario " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' AS valorentrada, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=2 " + 
    		      "THEN dtxa.cantidad " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' " + 
    		    "||'' AS cantidadsalida, " + 
    		    "CASE " + 
    		      "WHEN tti.tipos_conceptos_inv=2 " + 
    		      "THEN dtxa.cantidad*dtxa.val_unitario " + 
    		      "ELSE 0 " + 
    		    "END " + 
    		    "||'' AS valorsalida, " + 
    		    "txa.usuario " + 
    		    "||'' AS usuariomovimiento, " + 
    		    "txa.entidad " + 
    		    "||''                    AS tercero, " + 
    		    "dtxa.proveedor_compra   AS proveedorcompra, " + 
    		    "dtxa.proveedor_catalogo AS proveedorcatalogo, " + 
    		    "tti.tipos_conceptos_inv " + 
    		    "||'' AS tipoconceptoinv " + 
    		  "FROM inventarios.transacciones_x_almacen txa " + 
    		  "INNER JOIN inventarios.det_trans_x_almacen dtxa " + 
    		  "ON (dtxa.transaccion=txa.codigo) " + 
    		  "INNER JOIN inventarios.tipos_trans_inventarios tti " + 
    		  "ON (txa.transaccion=tti.consecutivo) " + 
    		  "WHERE txa.estado   =2 " + 
    			"AND dtxa.articulo='"+articulo+"'  " + 
    			"AND to_date(txa.fecha_cierre) < to_date('"+fecha+"','DD/MM/YY') " +
    			"UNION  " + 
    			"SELECT sta.numero_traslado " +
    		    "||'' AS codigomovimiento, " +
    		    "(SELECT codigo " +
    		    "FROM inventarios.tipos_trans_inventarios " +
    		    "WHERE consecutivo= " +
    		      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
    		      "FROM administracion.valores_por_defecto " +
    		      "WHERE parametro ='codigo_trans_traslado_almacen' " +
    		      "AND institucion = u.institucion " +
    		      ") " +
    		    ") " +
    		    "||'' AS codigotransaccion, " +
    		    "(SELECT descripcion " +
    		    "FROM inventarios.tipos_trans_inventarios " +
    		    "WHERE consecutivo= " +
    		      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
    		      "FROM administracion.valores_por_defecto " +
    		      "WHERE parametro ='codigo_trans_traslado_almacen' " +
    		      "AND institucion = u.institucion " +
    		      ") " +
    		    ") " +
    		    "||'' AS desctransaccion, " +
    		    "sta.numero_traslado " +
    		    "||'' AS documento, " +
    		    "TO_CHAR(sta.fecha_elaboracion,'yyyy-mm-dd') " +
    		    "||'' AS fechaelaboracion, " +
    		    "TO_CHAR(dta.fecha_despacho,'yyyy-mm-dd') " + 
    		    "||'' AS fechaatencion, " +
    		    "dta.hora_despacho " +
    		    "|| '' AS horaatencion, " +
    		    "sta.almacen_solicita " +
    		    "||'' AS almacen, " +
    		    "sta.almacen_solicitado " +
    		    "||'' AS almacendestino, " +
    		    "ddta.articulo " +
    		    "||''                   AS articulo, " +
    		    "ddta.lote              AS lote, " +
    		    "ddta.fecha_vencimiento AS fechavencimiento, " +
    		    "ddta.valor_unitario " +
    		    "||'' AS costounitario, " +
    		    "ddta.cantidad " +
    		    "||'' AS cantidadentrada, " +
    		    "(ddta.cantidad*ddta.valor_unitario) " +
    		    "||'' AS valorentrada, " +
    		    "0 " +
    		    "||'' AS cantidadsalida, " +
    		    "0 " +
    		    "||'' AS valorsalida, " +
    		    "sta.usuario_elabora " +
    		    "||''           AS usuariomovimiento, " +
    		    "''             AS tercero, " +
    		    "NULL           AS proveedorcompra, " +
    		    "ddta.proveedor AS proveedorcatalogo, " +
    		    "(SELECT tipos_conceptos_inv " +
    		    "FROM inventarios.tipos_trans_inventarios " +
    		    "WHERE consecutivo= " +
    		      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
    		      "FROM administracion.valores_por_defecto " +
    		      "WHERE parametro ='codigo_trans_traslado_almacen' " +
    		      "AND institucion = u.institucion " +
    		      ") " +
    		    ") " +
    		    "||'' AS tipoconceptoinv " +
    		  "FROM inventarios.solicitud_traslado_almacen sta " +
    		  "INNER JOIN inventarios.despacho_traslado_almacen dta " +
    		  "ON(sta.numero_traslado=dta.numero_traslado) " +
    		  "INNER JOIN inventarios.det_des_traslado_almacen ddta " +
    		  "ON(ddta.numero_traslado=dta.numero_traslado) " +
    		  "INNER JOIN administracion.usuarios u " +
    		  "ON (u.login     =sta.usuario_elabora) " +
    		  "WHERE sta.estado=4 " +
                "AND ddta.articulo='"+articulo+"'  " + 
    			"AND to_date(dta.fecha_despacho) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT sta.numero_traslado " +
        	    "||'' AS codigomovimiento, " +
        	    "(SELECT codigo " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS codigotransaccion, " +
        	    "(SELECT descripcion " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS desctransaccion, " +
        	    "sta.numero_traslado " +
        	    "||'' AS documento, " +
        	    "TO_CHAR(sta.fecha_elaboracion,'yyyy-mm-dd') " +
        	    "||'' AS fechaelaboracion, " +
        	    "TO_CHAR(dta.fecha_despacho,'yyyy-mm-dd') " +
        	    "||'' AS fechaatencion, " +
        	    "dta.hora_despacho " +
        	    "|| '' AS horaatencion, " +
        	    "sta.almacen_solicitado " +
        	    "||'' AS almacen, " +
        	    "sta.almacen_solicita " +
        	    "||'' AS almacendestino, " +
        	    "ddta.articulo " +
        	    "||''                   AS articulo, " +
        	    "ddta.lote              AS lote, " +
        	    "ddta.fecha_vencimiento AS fechavencimiento, " +
        	    "ddta.valor_unitario " +
        	    "||'' AS costounitario, " +
        	    "0 " +
        	    "||'' AS cantidadsalida, " +
        	    "0 " +
        	    "||'' AS valorsalida, " +
        	    "ddta.cantidad " +
        	    "||'' AS cantidadsalida, " +
        	    "(ddta.cantidad*ddta.valor_unitario) " +
        	    "||'' AS valorsalida, " +
        	    "dta.usuario_despacho " +
        	    "||''           AS usuariomovimiento, " +
        	    "''             AS tercero , " +
        	    "NULL           AS proveedorcompra, " +
        	    "ddta.proveedor AS proveedorcatalogo, " +
        	    "(SELECT tipos_conceptos_inv " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_traslado_almacen' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS tipoconceptoinv " +
        	  "FROM inventarios.solicitud_traslado_almacen sta " +
        	  "INNER JOIN inventarios.despacho_traslado_almacen dta " +
        	  "ON(sta.numero_traslado=dta.numero_traslado) " +
        	  "INNER JOIN inventarios.det_des_traslado_almacen ddta " +
        	  "ON(ddta.numero_traslado=dta.numero_traslado) " +
        	  "INNER JOIN administracion.usuarios u " +
        	  "ON (u.login     =dta.usuario_despacho) " +
        	  "WHERE sta.estado=4 " +
                "AND ddta.articulo='"+articulo+"'  " + 
    			"AND to_date(dta.fecha_despacho) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT s.numero_solicitud " +
        	    "||'' AS codigomovimiento, " +
        	    "(SELECT codigo " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS codigotransaccion, " +
        	    "(SELECT descripcion " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS desctransaccion, " +
        	    "s.consecutivo_ordenes_medicas " +
        	    "||'-' " +
        	    "||d.orden " +
        	    "||'' AS documento, " +
        	    "TO_CHAR(s.fecha_solicitud,'yyyy-mm-dd') " +
        	    "||'' AS fechaelaboracion, " +
        	    "TO_CHAR(d.fecha,'yyyy-mm-dd') " +
        	    "||'' AS fechaatencion, " +
        	    "d.hora " +
        	    "|| '' AS horaatencion, " +
        	    "s.centro_costo_solicitado " +
        	    "||'' AS almacen, " +
        	    "s.centro_costo_solicitante " +
        	    "||'' AS almacendestino, " +
        	    "dd.articulo " +
        	    "||''                 AS articulo, " +
        	    "dd.lote              AS lote, " +
        	    "dd.fecha_vencimiento AS fechavencimiento, " +
        	    "dd.costo_unitario " +
        	    "||'' AS costounitario, " +
        	    "0 " +
        	    "||'' AS cantidadentrada, " +
        	    "0 " +
        	    "||'' AS valorentrada, " +
        	    "dd.cantidad " +
        	    "||'' AS cantidadsalida, " +
        	    "(dd.cantidad*dd.costo_unitario) " +
        	    "||'' AS valorsalida, " +
        	    "d.usuario " +
        	    "||''                  AS usuariomovimiento, " +
        	    "''                    AS tercero, " +
        	    "dd.proveedor_compra   AS proveedorcompra, " +
        	    "dd.proveedor_catalogo AS proveedorcatalogo, " +
        	    "(SELECT tipos_conceptos_inv " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS tipoconceptoinv " +
        	  "FROM ordenes.solicitudes s " +
        	  "INNER JOIN facturacion.cargos_directos cardir " +
        	  "ON(cardir.numero_solicitud=s.numero_solicitud) " +
        	  "INNER JOIN inventarios.despacho d " +
        	  "ON(s.numero_solicitud=d.numero_solicitud) " +
        	  "INNER JOIN inventarios.detalle_despachos dd " +
        	  "ON(d.orden=dd.despacho) " +
        	  "INNER JOIN administracion.usuarios u " +
        	  "ON (u.login                    =d.usuario) " +
        	  "WHERE s.estado_historia_clinica=7 " +
        	  "AND cardir.afecta_inventarios  ='S' " +
        	  "AND s.centro_costo_solicitado <>11 " +"AND dd.articulo='"+articulo+"'  " + 
    			"AND to_date(d.fecha) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT s.numero_solicitud " +
        	    "||'' AS codigomovimiento, " +
        	    "(SELECT codigo " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS codigotransaccion, " +
        	    "(SELECT descripcion " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS desctransaccion, " +
        	    "s.consecutivo_ordenes_medicas " +
        	    "||'-' " +
        	    "||d.orden " +
        	    "||'' AS documento, " +
        	    "TO_CHAR(s.fecha_solicitud,'yyyy-mm-dd') " +
        	    "||'' AS fechaelaboracion, " +
        	    "TO_CHAR(d.fecha,'yyyy-mm-dd') " +
        	    "||'' AS fechaatencion, " +
        	    "d.hora " +
        	    "|| '' AS horaatencion, " +
        	    "s.centro_costo_solicitado " +
        	    "||'' AS almacen, " +
        	    "s.centro_costo_solicitante " +
        	    "||'' AS almacendestino, " +
        	    "dd.articulo " +
        	    "||''                 AS articulo, " +
        	    "dd.lote              AS lote, " +
        	    "dd.fecha_vencimiento AS fechavencimiento, " +
        	    "dd.costo_unitario " +
        	    "||'' AS costounitario, " +
        	    "0 " +
        	    "||'' AS cantidadentrada, " +
        	    "0 " +
        	    "||'' AS valorentrada, " +
        	    "dd.cantidad " +
        	    "||'' AS cantidadsalida, " +
        	    "(dd.cantidad*dd.costo_unitario) " +
        	    "||'' AS valorsalida, " +
        	    "d.usuario " +
        	    "||''                  AS usuariomovimiento, " +
        	    "''                    AS tercero, " +
        	    "dd.proveedor_compra   AS proveedorcompra, " +
        	    "dd.proveedor_catalogo AS proveedorcatalogo, " +
        	    "(SELECT tipos_conceptos_inv " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS tipoconceptoinv " +
        	  "FROM ordenes.solicitudes s " +
        	  "INNER JOIN inventarios.despacho d " +
        	  "ON(s.numero_solicitud=d.numero_solicitud) " +
        	  "INNER JOIN inventarios.detalle_despachos dd " +
        	  "ON(d.orden=dd.despacho) " +
        	  "INNER JOIN administracion.usuarios u " +
        	  "ON (u.login                      =d.usuario) " +
        	  "WHERE s.estado_historia_clinica IN (1,5,6) " +
        	  "AND s.centro_costo_solicitado   <>11 " +
        	  "AND dd.articulo='"+articulo+"'  " + 
    			"AND to_date(d.fecha) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT dm.codigo " +
        	    "||'' AS codigomovimiento, " +
        	    "(SELECT codigo " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_devolucion_paciente' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS codigotransaccion, " +
        	    "(SELECT descripcion " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	    "  (SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_devolucion_paciente' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS desctransaccion, " +
        	    "dm.codigo " +
        	    "||'' AS documento, " +
        	    "TO_CHAR(dm.fecha,'yyyy-mm-dd') " +
        	    "||'' AS fechaelaboracion, " +
        	    "TO_CHAR(rm.fecha,'yyyy-mm-dd') " +
        	    "||'' AS fechaatencion, " +
        	    "rm.hora " +
        	    "|| '' AS horaatencion, " +
        	    "dm.farmacia " +
        	    "||'' AS almacen, " +
        	    "''   AS almacendestino, " +
        	    "drm.articulo " +
        	    "||''                  AS articulo, " +
        	    "drm.lote              AS lote, " +
        	    "drm.fecha_vencimiento AS fechavencimiento, " +
        	    "drm.costo_unitario " +
        	    "||'' AS costounitario, " +
        	    "drm.cantidadrecibida " +
        	    "||'' AS cantidadentrada, " +
        	    "(drm.cantidadrecibida*drm.costo_unitario) " +
        	    "||'' AS valorentrada, " +
        	    "0 " +
        	    "||'' AS cantidadsalida, " +
        	    "0 " +
        	    "||'' AS valorsalida, " +
        	    "dm.usuario " +
        	    "||''                   AS usuariomovimiento, " +
        	    "''                     AS tercero, " +
        	    "drm.proveedor_compra   AS proveedorcompra, " +
        	    "drm.proveedor_catalogo AS proveedorcatalogo, " +
        	    "(SELECT tipos_conceptos_inv " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_trans_devolucion_paciente' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS tipoconceptoinv " +
        	  "FROM inventarios.devolucion_med dm " +
        	  "INNER JOIN inventarios.recepciones_medicamentos rm " +
        	  "ON (dm.codigo=rm.devolucion) " +
        	  "INNER JOIN inventarios.detalle_recep_medicamentos drm " +
        	  "ON(rm.devolucion=drm.numerodevolucion) " +
        	  "INNER JOIN administracion.usuarios u " +
        	  "ON (u.login =dm.usuario) " +
        	  "WHERE estado=2 " + 
                "AND drm.articulo='"+articulo+"'  " + 
    			"AND to_date(rm.fecha) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT p.codigo " +
        	    "||'' AS codigomovimiento, " +
        	    "(SELECT codigo " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_transaccion_pedidos' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS codigotransaccion, " +
        	    "(SELECT descripcion " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_transaccion_pedidos' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS desctransaccion, " +
        	    "p.codigo " +
        	    "||'' AS documento, " +
        	    "TO_CHAR(p.fecha,'yyyy-mm-dd') " +
        	    "||'' AS fechaelaboracion, " +
        	    "TO_CHAR(dp.fecha,'yyyy-mm-dd') " +
        	    "||'' AS fechaatencion, " +
        	    "dp.hora " +
        	    "|| '' AS horaatencion, " +
        	    "p.centro_costo_solicitado " +
        	    "||'' AS almacen, " +
        	    "p.centro_costo_solicitante " +
        	    "||'' AS almacendestino, " +
        	    "ddp.articulo " +
        	    "||''                  AS articulo, " +
        	    "ddp.lote              AS lote, " +
        	    "ddp.fecha_vencimiento AS fechavencimiento, " +
        	    "ddp.costo_unitario " +
        	    "||'' AS costounitario, " +
        	    "0 " +
        	    "||'' AS cantidadentrada, " +
        	    "0 " +
        	    "||'' AS valorentrada, " +
        	    "ddp.cantidad " +
        	    "||'' AS cantidadsalida, " +
        	    "(ddp.cantidad*ddp.costo_unitario) " +
        	    "||'' AS valorsalida, " +
        	    "p.usuario " +
        	    "||''                   AS usuariomovimiento, " +
        	    "''                     AS tercero, " +
        	    "ddp.proveedor_compra   AS proveedorcompra, " +
        	    "ddp.proveedor_catalogo AS proveedorcatalogo, " +
        	    "(SELECT tipos_conceptos_inv " +
        	    "FROM inventarios.tipos_trans_inventarios " +
        	    "WHERE consecutivo= " +
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " +
        	      "FROM administracion.valores_por_defecto " +
        	      "WHERE parametro ='codigo_transaccion_pedidos' " +
        	      "AND institucion = u.institucion " +
        	      ") " +
        	    ") " +
        	    "||'' AS tipoconceptoinv " +
        	  "FROM inventarios.pedido p " +
        	  "INNER JOIN inventarios.despacho_pedido dp " +
        	  "ON(p.codigo=dp.pedido) " +
        	  "INNER JOIN inventarios.detalle_despacho_pedido ddp " +
        	  "ON(dp.pedido=ddp.pedido) " +
        	  "INNER JOIN administracion.usuarios u " +
        	  "ON (u.login   =p.usuario) " +
        	  "WHERE p.estado=2 " +
                "AND ddp.articulo='"+articulo+"'  " + 
    			"AND to_date(dp.fecha) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT dp.codigo " + 
        	    "||'' AS codigomovimiento, " + 
        	    "(SELECT codigo " + 
        	    "FROM inventarios.tipos_trans_inventarios " + 
        	    "WHERE consecutivo= " + 
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
        	      "FROM administracion.valores_por_defecto " + 
        	      "WHERE parametro ='codigo_trans_devolucion_pedidos' " + 
        	      "AND institucion = u.institucion " + 
        	      ") " + 
        	    ") " + 
        	    "||'' AS codigotransaccion, " + 
        	    "(SELECT descripcion " + 
        	    "FROM inventarios.tipos_trans_inventarios " + 
        	    "WHERE consecutivo= " + 
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
        	      "FROM administracion.valores_por_defecto " + 
        	      "WHERE parametro ='codigo_trans_devolucion_pedidos' " + 
        	      "AND institucion = u.institucion " + 
        	      ") " + 
        	    ") " + 
        	    "||'' AS desctransaccion, " + 
        	    "dp.codigo " + 
        	    "||'-' " + 
        	    "||ddp.pedido " + 
        	    "||'' AS documento, " + 
        	    "TO_CHAR(dp.fecha,'yyyy-mm-dd') " + 
        	    "||'' AS fechaelaboracion, " + 
        	    "TO_CHAR(rp.fecha,'yyyy-mm-dd') " + 
        	    "||'' AS fechaatencion, " + 
        	    "rp.hora " + 
        	    "|| '' AS horaatencion, " + 
        	    "p.centro_costo_solicitado " + 
        	    "||'' AS almacen, " + 
        	    "p.centro_costo_solicitante " + 
        	    "||'' AS almacendestino, " + 
        	    "ddp.articulo " + 
        	    "||''                  AS articulo, " + 
        	    "drp.lote              AS lote, " + 
        	    "drp.fecha_vencimiento AS fechavencimiento, " + 
        	    "drp.costo_unitario " + 
        	    "||'' AS costounitario, " + 
        	    "drp.cantidadrecibida " + 
        	    "||'' AS cantidadentrada, " + 
        	    "(drp.cantidadrecibida*drp.costo_unitario) " + 
        	    "||'' AS valorentrada, " + 
        	    "0 " + 
        	    "||'' AS cantidadsalida, " + 
        	    "0 " + 
        	    "||'' AS valorsalida, " + 
        	    "dp.usuario " + 
        	    "||''                   AS usuariomovimiento, " + 
        	    "''                     AS tercero, " + 
        	    "drp.proveedor_compra   AS proveedorcompra, " + 
        	    "drp.proveedor_catalogo AS proveedorcatalogo, " + 
        	    "(SELECT tipos_conceptos_inv " + 
        	    "FROM inventarios.tipos_trans_inventarios " + 
        	    "WHERE consecutivo= " + 
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
        	      "FROM administracion.valores_por_defecto " + 
        	      "WHERE parametro ='codigo_trans_devolucion_pedidos' " + 
        	      "AND institucion = u.institucion " + 
        	      ") " + 
        	    ") " + 
        	    "||'' AS tipoconceptoinv " + 
        	  "FROM inventarios.devolucion_pedidos dp " + 
        	  "INNER JOIN inventarios.recepciones_pedidos rp " + 
        	  "ON(dp.codigo=rp.devolucion) " + 
        	  "INNER JOIN inventarios.detalle_devol_pedido ddp " + 
        	  "ON(dp.codigo=ddp.devolucion) " + 
        	  "INNER JOIN inventarios.pedido p " + 
        	  "ON (ddp.pedido=p.codigo) " + 
        	  "INNER JOIN inventarios.detalle_recep_pedidos drp " + 
        	  "ON(drp.codigo=ddp.codigo) " + 
        	  "INNER JOIN administracion.usuarios u " + 
        	  "ON (u.login    =dp.usuario) " + 
        	  "WHERE dp.estado=2 " + 
                "AND ddp.articulo='"+articulo+"'  " + 
    			"AND to_date(rp.fecha) < to_date('"+fecha+"','DD/MM/YY') " +
                "UNION  " + 
                "SELECT s.numero_solicitud " + 
        	    "||''                       AS codigomovimiento, " + 
        	    "''                         AS codigotransaccion, " + 
        	    "'ANULACION CARGO ARTICULO' AS desctransaccion, " + 
        	    "acf.numero_solicitud " + 
        	    "||'' AS documento, " + 
        	    "TO_CHAR(acf.fecha_modifica,'yyyy-mm-dd') " + 
        	    "||'' AS fechaelaboracion, " + 
        	    "TO_CHAR(acf.fecha_modifica,'yyyy-mm-dd') " + 
        	    "||'' AS fechaatencion, " + 
        	    "acf.hora_modificar " + 
        	    "||'' AS horaatencion, " + 
        	    "s.centro_costo_solicitado " + 
        	    "||'' AS almacen, " + 
        	    "s.centro_costo_solicitante " + 
        	    "||'' AS almacendestino, " + 
        	    "dd.articulo " + 
        	    "||''                 AS articulo, " + 
        	    "dd.lote              AS lote, " + 
        	    "dd.fecha_vencimiento AS fechavencimiento, " + 
        	    "dd.costo_unitario " + 
        	    "||'' AS costounitario, " + 
        	    "dd.cantidad " + 
        	    "||'' AS cantidadentrada, " + 
        	    "(dd.cantidad*dd.costo_unitario) " + 
        	    "||''AS valorentrada, " + 
        	    "0 " + 
        	    "||'' AS cantidadsalida, " + 
        	    "0 " + 
        	    "||'' AS valorsalida, " + 
        	    "d.usuario " + 
        	    "||''                  AS usuariomovimiento, " + 
        	    "''                    AS tercero, " + 
        	    "dd.proveedor_compra   AS proveedorcompra, " + 
        	    "dd.proveedor_catalogo AS proveedorcatalogo, " + 
        	    "(SELECT tipos_conceptos_inv " + 
        	    "FROM inventarios.tipos_trans_inventarios " + 
        	    "WHERE consecutivo= " + 
        	      "(SELECT to_number(COALESCE(trim(valor),'-1')) " + 
        	      "FROM administracion.valores_por_defecto " + 
        	      "WHERE parametro ='codigo_trans_soli_pacientes' " + 
        	      "AND institucion = u.institucion " + 
        	      ") " + 
        	    ") " + 
        	    "||'' AS tipoconceptoinv " + 
        	  "FROM ordenes.solicitudes s " + 
        	  "INNER JOIN facturacion.anulacion_cargos_farmacia acf " + 
        	  "ON(s.numero_solicitud=acf.numero_solicitud) " + 
        	  "INNER JOIN inventarios.despacho d " + 
        	  "ON(s.numero_solicitud=d.numero_solicitud) " + 
        	  "INNER JOIN inventarios.detalle_despachos dd " + 
        	  "ON(d.orden=dd.despacho) " + 
        	  "INNER JOIN ADMINISTRACION.USUARIOS U " + 
        	  "ON (u.login=d.usuario) " + 
                "WHERE dd.articulo='"+articulo+"'  " + 
    			"AND to_date(acf.fecha_modifica) < to_date('"+fecha+"','DD/MM/YY') " +
                ") t   " + 
                "INNER JOIN centros_costo cc on(convertiranumero(t.almacen)=cc.codigo)  " + 
                "where cc.institucion="+institucion+"  " +
                "ORDER BY t.fechaatencion desc, t.horaatencion desc"; 
		
	    return SqlBaseUtilidadInventariosDao.obtenerCostoUnitarioKardex(con, strConsultaCostoUnitarioKardexUM, institucion, articulo, fecha);
	}
	/**
	 * metodo para obtener el area de un 
	 * centro de costo
	 * @param con Connection
	 * @param codCentroCosto int
	 * @param institucion int 
	 * @return int, area del centro de costo
	 */
	public int obtenerTipoAreaCentroCosto(Connection con,int codCentroCosto,int institucion)
	{
	    return SqlBaseUtilidadInventariosDao.obtenerTipoAreaCentroCosto(con, codCentroCosto, institucion);   
	}
	/**
	 * metodo para validar si un almacen es
	 * valido para realizar traslado
	 * @param con Connection
	 * @param institucion int
	 * @param tipoTransaccion int
	 * @param centroCosto int
	 * @return boolean
	 */
	public boolean esAlmacenAutorizadoParaTraslado(Connection con,int institucion,int tipoTransaccion,int centroCosto)
	{
	    return SqlBaseUtilidadInventariosDao.esAlmacenAutorizadoParaTraslado(con, institucion, tipoTransaccion, centroCosto);
	}
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
	public HashMap listadoClaseGrupoSegunCentroCostoUsuario(Connection con,int institucion,int centroCosto,int transaccion)
	{
	    return SqlBaseUtilidadInventariosDao.listadoClaseGrupoSegunCentroCostoUsuario(con, institucion, centroCosto, transaccion);
	}
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
    public HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(Connection con,int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto)
    {
        return SqlBaseUtilidadInventariosDao.listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(con, institucion,tipoTransaccion, paresClaseGrupo, centroCosto);
    }
	
	/**
     * Metodo para cargar los almacenes validos, filtrando 
     * por las transacciones validas por centro de costo, y
     * no se cargan los que pertenecen a un centro de costo
     * en particular     
     * @param con Connection
	 * @param institucion int
	 * @param tipoTransaccion String
	 * @param centroCosto int
	 * @param login String
	 * @param pares clase-grupo (si va vacio no se valida) String
     * @return HashMap
     */
    public HashMap listadoAlmacenesUsuariosXCentroAtencion(Connection con,int institucion,String tipoTransaccion,String paresClaseGrupo,int centroCosto, int centroAtencion, String login)
    {
    	return SqlBaseUtilidadInventariosDao.listadoAlmacenesUsuariosXCentroAtencion(con, institucion,tipoTransaccion, paresClaseGrupo, centroCosto, centroAtencion, login);
    }

    /**
     * metodo que devuelve el numero de existencias (- o +) de un articulo, en caso de que genere error
     * devuelve un null
     * @param con
     * @param codArticulo
     * @param codAlmacen
     * @param codInstitucion
     * @return
     */
    public String getExistenciasXArticulo(Connection con, int  codArticulo, int codAlmacen,int codInstitucion)
    {
        return SqlBaseUtilidadInventariosDao.getExistenciasXArticulo(con, codArticulo, codAlmacen, codInstitucion);
    }
    
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
    public HashMap transaccionesValidasCentroCostoAgrupados(Connection con, int institucion, int centroCosto,Vector restCodigoValDef,boolean esIn)
    {
        return SqlBaseUtilidadInventariosDao.transaccionesValidasCentroCostoAgrupados(con, institucion, centroCosto, restCodigoValDef, esIn);
    }
    

    /**
     * 
     * @param con
     * @param institucion
     * @param centroCosto
     * @param codigoTransaccion
     * @return
     */
	public String obtenerParejasClaseGrupoInventario(Connection con, int institucion, int centroCosto, String codigoTransaccion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerParejasClaseGrupoInventario (con,institucion,centroCosto,codigoTransaccion);
	}
	
	


	/**
	 * 
	 */
	public boolean actualizarExistenciasArticuloAlmacenLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento)
	{
        return SqlBaseUtilidadInventariosDao.actualizarExistenciasArticuloAlmacenLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, lote,fechaVencimiento);
	}
	
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
	public boolean actualizarArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento)
	{
        return SqlBaseUtilidadInventariosDao.actualizarArticuloAlmacenXLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo, lote,fechaVencimiento);
	}

	/**
	 * 
	 */
	public int existenciasArticuloAlmacenLote(Connection con, int codArticulo, int almacen,String lote,String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.existenciasArticuloAlmacenLote(con,codArticulo,almacen,lote,fechaVencimiento);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
    public HashMap obtenerLotesDespachoNoAdministradosTotalmente(Connection con, String numeroSolicitud, String codigoArticulo)
    {
    	return SqlBaseUtilidadInventariosDao.obtenerLotesDespachoNoAdministradosTotalmente(con, numeroSolicitud, codigoArticulo);
    }


	public int obtenerCantidadEntradaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadEntradaCierreMesAnioLoteFecha(con,institucion,almacen,articulo,anio,mes,lote,fechaVencimiento);
	}



	public int obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadEntradaCierreMesAnioTotalLoteFecha(con,institucion,almacen,articulo,anio,lote,fechaVencimiento);
	}



	public int obtenerCantidadSalidaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadSalidaCierreMesAnioLoteFecha(con,institucion,almacen,articulo,anio,mes,lote,fechaVencimiento);
	}



	public int obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCantidadSalidaCierreMesAnioTotalLoteFecha(con,institucion,almacen,articulo,anio,lote,fechaVencimiento);
	}



	public double obtenerValorEntradaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorEntradaCierreMesAnioLoteFecha(con, institucion, almacen, articulo, anio, mes, lote, fechaVencimiento);
	}



	public double obtenerValorEntradaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorEntradaCierreMesAnioTotalLoteFecha(con, institucion, almacen, articulo, anio, lote, fechaVencimiento);
	}



	public double obtenerValorSalidaCierreMesAnioLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String mes, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorSalidaCierreMesAnioLoteFecha(con, institucion, almacen, articulo, anio, mes, lote, fechaVencimiento);
	}



	public double obtenerValorSalidaCierreMesAnioTotalLoteFecha(Connection con, int institucion, int almacen, int articulo, String anio, String lote, String fechaVencimiento)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorSalidaCierreMesAnioTotalLoteFecha(con, institucion, almacen, articulo, anio, lote, fechaVencimiento);
	}
	
	/**
	 * Método que consulta el nombre de una via de administracion a partir
	 * de su consecutivo de la tabla vias_admin_institucion
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerNombreViaAdminInstitucion(Connection con,int consecutivo)
	{
		return SqlBaseUtilidadInventariosDao.obtenerNombreViaAdminInstitucion(con, consecutivo);
	}
	


	/**
	 * 
	 */
	public int obtenerTipoCierre(Connection con, String codigoCierre)
	{
		return SqlBaseUtilidadInventariosDao.obtenerTipoCierre(con,codigoCierre);
	}



	public String obtenerPrecioBaseVentaArticulo(Connection con, int articulo) {
		return SqlBaseUtilidadInventariosDao.obtenerPrecioBaseVentaArticulo(con,articulo);
	}



	public String obtenerPrecioUltimaCompraArticulo(Connection con, int articulo) {
		return SqlBaseUtilidadInventariosDao.obtenerPrecioUltimaCompraArticulo(con,articulo);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerAlmacenesConsignacion(Connection con, int institucion) {
		return SqlBaseUtilidadInventariosDao.obtenerAlmacenesConsignacion(con,institucion);
	}
	
	

	/**
	 * 
	 */
	public HashMap obtenerConveniosProveedor(Connection con, int institucion,int articulo)
	{
		return SqlBaseUtilidadInventariosDao.obtenerConveniosProveedor(con,institucion,articulo);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerProveedoresCatalogo(Connection con, int institucion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerProveedoresCatalogo(con,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codTransInterfaz
	 * @return
	 */
	public int obtenerTipoTransaccionInterfaz(Connection con, int codTransInterfaz,int institucion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerTipoTransaccionInterfaz(con,codTransInterfaz,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param tipoTransaccion
	 * @param institucion
	 * @return
	 */
	public String obtenerNombreTipoTransaccion(Connection con, int tipoTransaccion, int institucion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerNombreTipoTransaccion(con,tipoTransaccion,institucion);
	}
	

	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public double obtenerValorArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorArticuloProveedorConveProveedor(con,proveedor,articulo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param proveedor
	 * @param articulo
	 * @return
	 */
	public double obtenerValorArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorArticuloProveedorCatalogoProveedor(con,proveedor,articulo);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param valorUnitario
	 * @return
	 */
	public boolean actualizarPrecioUltimaCompra(Connection con, int codigo, double valorUnitario)
	{
		return SqlBaseUtilidadInventariosDao.actualizarPrecioUltimaCompra(con,codigo,valorUnitario);
	}
	
	/**
	 * 
	 */
	public String obtenerCodigoInterfazArticulo(Connection con, int codigoArticulo) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerCodigoInterfazArticulo(con,codigoArticulo);
	}
	
	


	/**
	 * 
	 */
	public double obtenerValorIvaArticuloProveedorConveProveedor(Connection con, String proveedor, int articulo) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorIvaArticuloProveedorConveProveedor(con,proveedor,articulo);
	}
	


	/**
	 * 
	 */
	public double obtenerValorIvaArticuloProveedorCatalogoProveedor(Connection con, String proveedor, int articulo) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorIvaArticuloProveedorCatalogoProveedor(con,proveedor,articulo);
	}
	


	/**
	 * 
	 */
	public int obtenerNumeroPeticionPedidoQX(Connection con, int pedido) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerNumeroPeticionPedidoQX(con,pedido);
	}
	
	/**
	 * Método que verifica si a una solicitud de cargos directos artículos le afectan los inventarios
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean afectaInventariosSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadInventariosDao.afectaInventariosSolicitud(con, numeroSolicitud);
	}
	

	/**
	 * 
	 */
	public HashMap obtenerClasesInventario(Connection con, int institucion) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerClasesInventario(con, institucion);
	}



	/**
	 * 
	 */
	public HashMap obtenerGrupoInventario(Connection con, int institucion) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerGrupoInventario(con, institucion);
	}
	
	/**
	 * Metodo para la validacion del Tiempo de tratamiento de justificacion NoPos
	 * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia
	 * @param paciente
	 * @return
	 */
	public boolean obtenerValidarTiempoTratamiento(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente)
	{
		return SqlBaseUtilidadInventariosDao.obtenerValidarTiempotratamiento(con, codigoArticulo, unidosis, dosis, tipoFrecuencia, frecuencia, paciente);
	}
	
	/**
	 * Metodo que retorna el codigo de la justificacion NoPos para asociar
	 * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia
	 * @param paciente
	 * @return
	 */
	public int obtenerJustificacion(Connection con, int codigoArticulo, String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente) {
		
		return SqlBaseUtilidadInventariosDao.obtenerUltimaJustificacion(con, codigoArticulo, unidosis, dosis, tipoFrecuencia, frecuencia, paciente);
	}

	/**
	 * 
	 */
	public double obtenerValorCompraMasAlta(Connection con, int articulo) 
	{
		return SqlBaseUtilidadInventariosDao.obtenerValorCompraMasAlta(con,articulo);
	}

	/**
	 * 
	 */
	public boolean actualizarPrecioCompraMasAlta(Connection con, int codigo, double valorUnitario) 
	{
		return SqlBaseUtilidadInventariosDao.actualizarPrecioCompraMasAlta(con,codigo,valorUnitario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @return
	 */
	public int existeAlmacenPlanEspecial(Connection con, int codigoCentroAtencion) 
	{
		return SqlBaseUtilidadInventariosDao.existeAlmacenPlanEspecial(con, codigoCentroAtencion);
	}
	
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
	public ArrayList<HashMap<String, Object>> obtenerClaseInventario (Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadInventariosDao.obtenerClaseInventario(connection, criterios);
	}
	
	/**
	 * 
	 */
	public String obtenerCodigoDadoCodigoInterfaz(Connection con, String codigoInterfaz)
	{
		return SqlBaseUtilidadInventariosDao.obtenerCodigoDadoCodigoInterfaz(con, codigoInterfaz);
	}
	
	/**
	 * Método para obtener las clases de inventario que aplican para un centro de costo
	 * según el tipo de transacción
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerClasesInventarioTransValidasCentroCosto(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadInventariosDao.obtenerClasesInventarioTransValidasCentroCosto(con, campos);
	}

	/**
	 * Método que devuelve la Naturaleza de un 
	 * artículo determinado
	 * @param con
	 * @param articulo
	 * @return
	 */
	public boolean esPosArticulo(Connection con, int articulo, int institucion)
	{
		return SqlBaseUtilidadInventariosDao.esPosArticulo(con, articulo, institucion); 
	}
	
	/**
	 * Metodo que devuelve el numero del consecutivo de una transaccion segun el codigo
	 * @param con
	 * @param string
	 * @return
	 */
	public String obtenerConsecutivoTransaccion(Connection con, String codigoTransaccion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerConsecutivoTransaccion(con, codigoTransaccion); 
	}
	
	/**
	 * Método para obtener las farmacias x centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerFarmaciasXCentroCosto(Connection con,HashMap<String, Object> campos)
	{
		return SqlBaseUtilidadInventariosDao.obtenerFarmaciasXCentroCosto(con, campos);
	}
	
	/**
	 * Método que indica si un articulo es o no medicamento
	 * @param con
	 * @param codigoArticulo
	 * @param codigoInstitucionInt
	 */
	public boolean esMedicamento(Connection con, int codigoArticulo, int codigoInstitucion)
	{
		return SqlBaseUtilidadInventariosDao.esMedicamento(con, codigoArticulo, codigoInstitucion);
	}
	
	/**
	 * @author Víctor Gómez
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerClasesInventarioArray(Connection con, int institucion)
	{
		return SqlBaseUtilidadInventariosDao.obtenerClasesInventarioArray(con, institucion);
	}
	
	
	/**
	 * 
	 */
	public boolean actualizarExistenciasArticuloAlmacenLoteAnulacion(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote,
		String fechaVencimiento,String numeroSolciitud,String  loteDetalle,HashMap<String,Object> detalleSolicitud,Integer index)
	{
        return SqlBaseUtilidadInventariosDao.actualizarExistenciasArticuloAlmacenLoteAnulacion(con, codArticulo, codAlmacen, esCantidadASumar,
        		cantidadArticulo, lote,fechaVencimiento,numeroSolciitud,loteDetalle,detalleSolicitud, index);
	}
	

	
	
	
}