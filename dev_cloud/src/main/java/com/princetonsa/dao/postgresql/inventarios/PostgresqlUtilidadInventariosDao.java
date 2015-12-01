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
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;
import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.UtilidadInventariosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseUtilidadInventariosDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * @version 1.0, 6/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class PostgresqlUtilidadInventariosDao implements UtilidadInventariosDao
{
    /**
     * @param con
     * @param institucion
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
    public HashMap listadoAlmacenesUsuariosParametrizadoEnTransaccionesValidasCC(Connection con, int institucion, String login, String tipoTransaccion, String paresClaseGrupo, int filtroCentroAtencion, boolean planEspecial)
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
    public HashMap cargarInfoClasesInventario(Connection con, int institucion, int codigoAlmacen, String paresClaseGrupo)
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
        return SqlBaseUtilidadInventariosDao.calcularCostoPromedioArticulo(con,codArticulo,numeroUnidades,valorTotalTransacion,tipoTransaccion,institucion, codigoAlmacen);
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
    			"SELECT txa.codigo::text || ''::text AS codigomovimiento, tti.codigo::text || ''::text AS codigotransaccion, tti.descripcion::text || ''::text AS desctransaccion, txa.consecutivo::text || ''::text AS documento, txa.fecha_elaboracion || ''::text AS fechaelaboracion, txa.fecha_cierre || ''::text AS fechaatencion, txa.hora_cierre::text || ''::text AS horaatencion, txa.almacen || ''::text AS almacen, ''::text AS almacendestino, dtxa.articulo || ''::text AS articulo, dtxa.lote, dtxa.fecha_vencimiento AS fechavencimiento, dtxa.val_unitario || ''::text AS costounitario,  " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 1 THEN dtxa.cantidad " + 
    			"ELSE 0 " + 
    			"END || ''::text AS cantidadentrada, " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 1 THEN dtxa.cantidad::numeric * dtxa.val_unitario " + 
    			"ELSE 0::numeric " + 
    			"END || ''::text AS valorentrada, ( " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 2 THEN dtxa.cantidad " + 
    			"ELSE 0 " + 
    			"END || ''::text) || ''::text AS cantidadsalida,  " + 
    			"CASE " + 
    			"WHEN tti.tipos_conceptos_inv = 2 THEN dtxa.cantidad::numeric * dtxa.val_unitario " + 
    			"ELSE 0::numeric " + 
    			"END || ''::text AS valorsalida, txa.usuario::text || ''::text AS usuariomovimiento, txa.entidad || ''::text AS tercero, dtxa.proveedor_compra AS proveedorcompra, dtxa.proveedor_catalogo AS proveedorcatalogo, tti.tipos_conceptos_inv || ''::text AS tipoconceptoinv " + 
    			"FROM transacciones_x_almacen txa " + 
    			"JOIN det_trans_x_almacen dtxa ON dtxa.transaccion::text = txa.codigo::text " + 
    			"JOIN tipos_trans_inventarios tti ON txa.transaccion = tti.consecutivo " + 
    			"WHERE txa.estado = 2 " + 
    			"AND dtxa.articulo='"+articulo+"'  " + 
    			"AND txa.fecha_cierre < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
    			"UNION  " + 
    			"SELECT sta.numero_traslado || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
    			"FROM tipos_trans_inventarios " + 
    			"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
    			"CASE " + 
    			"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
    			"ELSE valores_por_defecto.valor::integer " + 
    			"END AS valor " + 
    			"FROM valores_por_defecto " + 
    			"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
    			"FROM tipos_trans_inventarios " + 
    			"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
    			"CASE " + 
    			"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
    			"ELSE valores_por_defecto.valor::integer " + 
    			"END AS valor " + 
    			"FROM valores_por_defecto " + 
    			"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, sta.numero_traslado || ''::text AS documento, sta.fecha_elaboracion || ''::text AS fechaelaboracion, dta.fecha_despacho || ''::text AS fechaatencion, dta.hora_despacho::text || ''::text AS horaatencion, sta.almacen_solicita || ''::text AS almacen, sta.almacen_solicitado || ''::text AS almacendestino, ddta.articulo || ''::text AS articulo, ddta.lote, ddta.fecha_vencimiento AS fechavencimiento, ddta.valor_unitario || ''::text AS costounitario, ddta.cantidad || ''::text AS cantidadentrada, (ddta.cantidad::numeric * ddta.valor_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, sta.usuario_elabora::text || ''::text AS usuariomovimiento, ''::text AS tercero, NULL::character varying AS proveedorcompra, ddta.proveedor AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
    			"FROM tipos_trans_inventarios " + 
    			"WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
    			"CASE " + 
    			"WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
    			"ELSE valores_por_defecto.valor::integer " + 
    			"END AS valor " + 
    			"FROM valores_por_defecto " + 
    			"WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
    			"FROM solicitud_traslado_almacen sta " + 
    			"JOIN despacho_traslado_almacen dta ON sta.numero_traslado = dta.numero_traslado " + 
    			"JOIN det_des_traslado_almacen ddta ON ddta.numero_traslado = dta.numero_traslado " + 
    			"JOIN usuarios u ON u.login::text = sta.usuario_elabora::text " + 
                "WHERE sta.estado = 4 " + 		
                "AND ddta.articulo='"+articulo+"'  " + 
                "AND dta.fecha_despacho < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT sta.numero_traslado || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " +
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, sta.numero_traslado || ''::text AS documento, sta.fecha_elaboracion || ''::text AS fechaelaboracion, dta.fecha_despacho || ''::text AS fechaatencion, dta.hora_despacho::text || ''::text AS horaatencion, sta.almacen_solicitado || ''::text AS almacen, sta.almacen_solicita || ''::text AS almacendestino, ddta.articulo || ''::text AS articulo, ddta.lote, ddta.fecha_vencimiento AS fechavencimiento, ddta.valor_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, ddta.cantidad || ''::text AS cantidadsalida, (ddta.cantidad::numeric * ddta.valor_unitario) || ''::text AS valorsalida, dta.usuario_despacho::text || ''::text AS usuariomovimiento, ''::text AS tercero, NULL::character varying AS proveedorcompra, ddta.proveedor AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_traslado_almacen'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM solicitud_traslado_almacen sta " + 
                "JOIN despacho_traslado_almacen dta ON sta.numero_traslado = dta.numero_traslado " + 
                "JOIN det_des_traslado_almacen ddta ON ddta.numero_traslado = dta.numero_traslado " + 
                "JOIN usuarios u ON u.login::text = dta.usuario_despacho::text " + 
                "WHERE sta.estado = 4 " + 
                "AND ddta.articulo='"+articulo+"'  " +   
                "AND dta.fecha_despacho < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT s.numero_solicitud || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " +
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1)" +
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, ((s.consecutivo_ordenes_medicas || '-'::text) || d.orden) || ''::text AS documento, s.fecha_solicitud || ''::text AS fechaelaboracion, d.fecha || ''::text AS fechaatencion, d.hora || ''::text AS horaatencion, s.centro_costo_solicitado || ''::text AS almacen, s.centro_costo_solicitante || ''::text AS almacendestino, dd.articulo || ''::text AS articulo, dd.lote, dd.fecha_vencimiento AS fechavencimiento, dd.costo_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, dd.cantidad || ''::text AS cantidadsalida, (dd.cantidad::numeric * dd.costo_unitario) || ''::text AS valorsalida, d.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, dd.proveedor_compra AS proveedorcompra, dd.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM solicitudes s " + 
                "JOIN cargos_directos cardir ON cardir.numero_solicitud = s.numero_solicitud " + 
                "JOIN despacho d ON s.numero_solicitud = d.numero_solicitud " + 
                "JOIN detalle_despachos dd ON d.orden = dd.despacho " + 
                "JOIN usuarios u ON u.login::text = d.usuario::text " + 
                "WHERE s.estado_historia_clinica = 7 AND cardir.afecta_inventarios::text = 'S'::text AND s.centro_costo_solicitado <> 11 " + 
                "AND dd.articulo='"+articulo+"'  " + 
                "AND d.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT s.numero_solicitud || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor             " +
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " +
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, ((s.consecutivo_ordenes_medicas || '-'::text) || d.orden) || ''::text AS documento, s.fecha_solicitud || ''::text AS fechaelaboracion, d.fecha || ''::text AS fechaatencion, d.hora || ''::text AS horaatencion, s.centro_costo_solicitado || ''::text AS almacen, s.centro_costo_solicitante || ''::text AS almacendestino, dd.articulo || ''::text AS articulo, dd.lote, dd.fecha_vencimiento AS fechavencimiento, dd.costo_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, dd.cantidad || ''::text AS cantidadsalida, (dd.cantidad::numeric * dd.costo_unitario) || ''::text AS valorsalida, d.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, dd.proveedor_compra AS proveedorcompra, dd.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM solicitudes s " + 
                "JOIN despacho d ON s.numero_solicitud = d.numero_solicitud " + 
                "JOIN detalle_despachos dd ON d.orden = dd.despacho " + 
                "JOIN usuarios u ON u.login::text = d.usuario::text " + 
                "WHERE (s.estado_historia_clinica = ANY (ARRAY[1, 5, 6])) AND s.centro_costo_solicitado <> 11 " + 
                "AND dd.articulo='"+articulo+"'  " + 
                "AND d.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT dm.codigo || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " +
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_paciente'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_paciente'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, dm.codigo || ''::text AS documento, dm.fecha || ''::text AS fechaelaboracion, rm.fecha || ''::text AS fechaatencion, rm.hora || ''::text AS horaatencion, dm.farmacia || ''::text AS almacen, ''::text AS almacendestino, drm.articulo || ''::text AS articulo, drm.lote, drm.fecha_vencimiento AS fechavencimiento, drm.costo_unitario || ''::text AS costounitario, drm.cantidadrecibida || ''::text AS cantidadentrada, (drm.cantidadrecibida::numeric * drm.costo_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, dm.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, drm.proveedor_compra AS proveedorcompra, drm.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_paciente'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM devolucion_med dm " + 
                "JOIN recepciones_medicamentos rm ON dm.codigo = rm.devolucion " + 
                "JOIN detalle_recep_medicamentos drm ON rm.devolucion = drm.numerodevolucion " + 
                "JOIN usuarios u ON u.login::text = dm.usuario::text " + 
                "WHERE dm.estado = 2 " + 
                "AND drm.articulo='"+articulo+"'  " + 
                "AND rm.fecha < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT p.codigo || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_transaccion_pedidos'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_transaccion_pedidos'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, p.codigo || ''::text AS documento, p.fecha || ''::text AS fechaelaboracion, dp.fecha || ''::text AS fechaatencion, dp.hora || ''::text AS horaatencion, p.centro_costo_solicitado || ''::text AS almacen, p.centro_costo_solicitante || ''::text AS almacendestino, ddp.articulo || ''::text AS articulo, ddp.lote, ddp.fecha_vencimiento AS fechavencimiento, ddp.costo_unitario || ''::text AS costounitario, 0 || ''::text AS cantidadentrada, 0 || ''::text AS valorentrada, ddp.cantidad || ''::text AS cantidadsalida, (ddp.cantidad::double precision * ddp.costo_unitario) || ''::text AS valorsalida, p.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, ddp.proveedor_compra AS proveedorcompra, ddp.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_transaccion_pedidos'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM pedido p " + 
                "JOIN despacho_pedido dp ON p.codigo = dp.pedido " + 
                "JOIN detalle_despacho_pedido ddp ON dp.pedido = ddp.pedido " + 
                "JOIN usuarios u ON u.login::text = p.usuario::text " + 
                "WHERE p.estado = 2 " + 
                "AND ddp.articulo='"+articulo+"'  " + 
                "AND dp.fecha  < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT dp.codigo || ''::text AS codigomovimiento, ((( SELECT tipos_trans_inventarios.codigo " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_pedidos'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS codigotransaccion, ((( SELECT tipos_trans_inventarios.descripcion " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_pedidos'::text AND valores_por_defecto.institucion = u.institucion))))::text) || ''::text AS desctransaccion, ((dp.codigo || '-'::text) || ddp.pedido) || ''::text AS documento, dp.fecha || ''::text AS fechaelaboracion, rp.fecha || ''::text AS fechaatencion, rp.hora || ''::text AS horaatencion, p.centro_costo_solicitado || ''::text AS almacen, p.centro_costo_solicitante || ''::text AS almacendestino, ddp.articulo || ''::text AS articulo, drp.lote, drp.fecha_vencimiento AS fechavencimiento, drp.costo_unitario || ''::text AS costounitario, drp.cantidadrecibida || ''::text AS cantidadentrada, (drp.cantidadrecibida::numeric * drp.costo_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, dp.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, drp.proveedor_compra AS proveedorcompra, drp.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_devolucion_pedidos'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM devolucion_pedidos dp " + 
                "JOIN recepciones_pedidos rp ON dp.codigo = rp.devolucion " + 
                "JOIN detalle_devol_pedido ddp ON dp.codigo = ddp.devolucion " + 
                "JOIN pedido p ON ddp.pedido = p.codigo " + 
                "JOIN detalle_recep_pedidos drp ON drp.codigo = ddp.codigo " + 
                "JOIN usuarios u ON u.login::text = dp.usuario::text " + 
                "WHERE dp.estado = 2 " + 
                "AND ddp.articulo='"+articulo+"'  " + 
                "AND rp.fecha  < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                "UNION  " + 
                "SELECT s.numero_solicitud || ''::text AS codigomovimiento, ''::text AS codigotransaccion, 'ANULACION CARGO ARTICULO'::text AS desctransaccion, acf.numero_solicitud || ''::text AS documento, acf.fecha_modifica || ''::text AS fechaelaboracion, acf.fecha_modifica || ''::text AS fechaatencion, acf.hora_modificar::text || ''::text AS horaatencion, s.centro_costo_solicitado || ''::text AS almacen, s.centro_costo_solicitante || ''::text AS almacendestino, dd.articulo || ''::text AS articulo, dd.lote, dd.fecha_vencimiento AS fechavencimiento, dd.costo_unitario || ''::text AS costounitario, dd.cantidad || ''::text AS cantidadentrada, (dd.cantidad::numeric * dd.costo_unitario) || ''::text AS valorentrada, 0 || ''::text AS cantidadsalida, 0 || ''::text AS valorsalida, d.usuario::text || ''::text AS usuariomovimiento, ''::text AS tercero, dd.proveedor_compra AS proveedorcompra, dd.proveedor_catalogo AS proveedorcatalogo, (( SELECT tipos_trans_inventarios.tipos_conceptos_inv " + 
                "FROM tipos_trans_inventarios " + 
                "WHERE tipos_trans_inventarios.consecutivo = (( SELECT  " + 
                "CASE " + 
                "WHEN btrim(valores_por_defecto.valor::text) = ''::text THEN (-1) " + 
                "ELSE valores_por_defecto.valor::integer " + 
                "END AS valor " + 
                "FROM valores_por_defecto " + 
                "WHERE valores_por_defecto.parametro::text = 'codigo_trans_soli_pacientes'::text AND valores_por_defecto.institucion = u.institucion)))) || ''::text AS tipoconceptoinv " + 
                "FROM solicitudes s " + 
                "JOIN anulacion_cargos_farmacia acf ON s.numero_solicitud = acf.numero_solicitud " + 
                "JOIN despacho d ON s.numero_solicitud = d.numero_solicitud " + 
                "JOIN detalle_despachos dd ON d.orden = dd.despacho " + 
                "JOIN usuarios u ON u.login::text = d.usuario::text " + 
                "WHERE dd.articulo='"+articulo+"'  " + 
                "AND acf.fecha_modifica  < '"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' " +
                ") t   " + 
                "INNER JOIN centros_costo cc on(convertiranumero(t.almacen)=cc.codigo)  " + 
                "where cc.institucion="+institucion+"  " +
                "ORDER BY t.fechaatencion desc, t.horaatencion desc"; 
		
	    return SqlBaseUtilidadInventariosDao.obtenerCostoUnitarioKardex(con,strConsultaCostoUnitarioKardexUM, institucion, articulo, fecha);
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
        return SqlBaseUtilidadInventariosDao.actualizarExistenciasArticuloAlmacenLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,fechaVencimiento);
	}


	/**
	 * 
	 */
	public boolean actualizarArticuloAlmacenXLote(Connection con, int codArticulo, int codAlmacen, boolean esCantidadASumar, int cantidadArticulo, String lote, String fechaVencimiento)
	{
        return SqlBaseUtilidadInventariosDao.actualizarArticuloAlmacenXLote(con, codArticulo, codAlmacen, esCantidadASumar, cantidadArticulo,lote,fechaVencimiento);
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



	public String obtenerPrecioUltimaCompraArticulo(Connection con, int articulo) 
	{
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
	 * Metodo para la validacio del teimpo de tratamiento de justificacion NOPOS
	 * * @param con
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
	 *Metodo que retorna el codigo de la justificacion NoPos para asociar
	 * * @param con
	 * @param codigoArticulo
	 * @param dosis
	 * @param tipoFrecuencia
	 * @param frecuencia
	 * @param paciente
	 * @return
	 */
	public int obtenerJustificacion(Connection con, int codigoArticulo,	String unidosis, String dosis, String tipoFrecuencia, String frecuencia, PersonaBasica paciente)
	{
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