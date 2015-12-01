package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseParametrizacionComponentesDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseParametrizacionComponentesDao.class);
	
	
	/**
	 * 
	 */
	public static String consultaComponentesSeccionStr="SELECT distinct c.tipo_componente as tipocomponente, sp.codigo as codigoseccion, sp.descripcion as descripcionseccion, sp.parametrizable as parametrizable, sp.orden as ordenseccion, sp.columnas_seccion as columnasseccion, sp.codigo_pk as codigopkseccion, sp.seccion_padre as seccionpadre, sp.mostrar as mostrarseccion, sp.mostrar_modificacion as mostrarmodificacion, sp.sexo as sexoseccion, sp.edad_inicial_dias as edadinicial, sp.edad_final_dias as edadfinal, sp.restriccion_val_campo as indicativorestriccion  FROM componentes c inner join componentes_secciones cs on(cs.componente=c.codigo) inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) WHERE c.tipo_componente=? and sp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' and sp.codigo is not null and sp.seccion_padre is null order by ordenseccion";
	
	
	public static String consultaComponentesEscalaStr="SELECT c.codigo as codigocomponente, c.tipo_componente as tipocomponente, ce.orden as ordenescala, ce.mostrar as mostrarescala, e.codigo_pk as codigopkescala, e.codigo as codigoescala, e.nombre as nombreescala, e.observaciones_requeridas as observrequeridas FROM componentes c inner join componentes_escalas ce on(ce.componente=c.codigo)  inner join escalas e on(e.codigo_pk=ce.escala) WHERE c.tipo_componente=? and ce.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
	
	
	public static String consultaComponentesCampoStr="SELECT c.codigo as codigocomponente, c.tipo_componente as tipocomponente, cp.codigo_pk as codigopkcampo, cp.codigo as codigocampo, cp.nombre as nombrecampo, cp.etiqueta as etiqueta, cp.tipo as tipocampo, cp.tamanio as tamaniocampo, cp.signo as signocampo, cp.unidad as unidadcampo, cp.valor_predeterminado as valorpredeterminado, cp.maximo as maximo, cp.minimo as minimo, cp.decimales as decimales, cp.columnas_ocupadas as columnasocupadas, cp.orden as ordencampo, cp.unico_fila as unicofila, cp.requerido as requeridocampo, cp.formula as formula, cp.tipo_html as tipohtml, cp.mostrar as mostrarcampo, sp.orden as ordenseccion FROM componentes c inner join componentes_secciones cs on(cs.componente=c.codigo) inner join componentes_campos_sec ccs on(ccs.componente_seccion=cs.codigo_pk) inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) inner join campos_parametrizables cp on(cp.codigo_pk=ccs.campo_parametrizable) WHERE c.tipo_componente=? and sp.codigo is null";
	
	
	public static String consultaCodigoSeccion="SELECT codigo_pk as codigopk from secciones_parametrizables";
	
	//////
	public static String consultaCodigoComponente="SELECT codigo from componentes WHERE tipo_componente=?";
	
	//////
	public static String consultaCodigoComponenteSeccion="SELECT codigo_pk from componentes_secciones WHERE componente=? AND seccion=? ";
	
	
	private static final String insertarComponentesStr="INSERT INTO componentes (codigo, tipo_componente, activo, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	
	private static final String insertarEscalaStr="INSERT INTO componentes_escalas (escala, componente, orden, mostrar, mostrar_modificacion) VALUES (?, ?, ?, ?, ?)";
	
	/////
	private static final String insertarSeccionStr="INSERT INTO secciones_parametrizables (codigo_pk, codigo, descripcion, orden, columnas_seccion, seccion_padre, mostrar, mostrar_modificacion, tipo, institucion, usuario_modifica, fecha_modifica, hora_modifica, sexo, edad_inicial_dias, edad_final_dias, restriccion_val_campo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	private static final String insertarComponenteSeccionStr="INSERT INTO componentes_secciones (codigo_pk, componente, seccion) VALUES (?, ?, ?)";
	
	
	private static final String insertarSeccionVaciaStr="INSERT INTO secciones_parametrizables (codigo_pk, codigo, descripcion, orden, columnas_seccion, seccion_padre, mostrar, mostrar_modificacion, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, null, '',  ?, null, null,null, null, ?, ?, ?, ?)";
	
	
	/////
	private static final String insertarCampoStr="INSERT INTO campos_parametrizables (codigo_pk, codigo, nombre, etiqueta, tipo, tamanio, signo, unidad, valor_predeterminado, maximo, minimo, decimales, columnas_ocupadas, orden, unico_fila, requerido, formula, tipo_html, mostrar, mostrar_modificacion, institucion, usuario_modifica, fecha_modifica, hora_modifica, generar_alerta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	private static final String insertarComponenteCampoSecStr="INSERT INTO componentes_campos_sec (campo_parametrizable, componente_seccion) VALUES (?, ?)";
	
	
	private static final String insertarOpcionesStr="INSERT INTO opciones_val_parametrizadas (codigo_pk, campo_parametrizable, opcion, valor, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	private static final String insertarSubSeccionStr="INSERT INTO secciones_parametrizables (codigo_pk, codigo, descripcion, orden, columnas_seccion, seccion_padre, mostrar, mostrar_modificacion, institucion, usuario_modifica, fecha_modifica, hora_modifica, sexo, edad_inicial_dias, edad_final_dias, restriccion_val_campo) VALUES (?, ?, ?, ?, ?, ?, ?, null, ?, ?, ?, ?, ?, ?, ?, ?)";

	
	
	///////////////////
	public static String consultaDetalleSeccionStr="SELECT sp.codigo as codigoseccion, sp.descripcion as nombreseccion, sp.descripcion as nombreseccionantiguo, sp.parametrizable as parametrizable, sp.orden as ordenseccion, sp.columnas_seccion as columnaseccion, sp.codigo_pk as codigopkseccion, sp.seccion_padre as seccionpadre, sp.mostrar as mostrarseccion, sp.mostrar_modificacion as mostrarmodificacion, sp.tipo as tiposeccion, sp.tipo as tiposeccionantiguo, sp.sexo as sexoseccion, sp.edad_inicial_dias as edadinicial, sp.edad_final_dias as edadfinal, sp.restriccion_val_campo as indicativorestriccion  FROM secciones_parametrizables sp  WHERE sp.codigo_pk=? ";
	
	
	public static String consultaDetalleEscalaStr="SELECT  ce.componente as componente, ce.escala as escala, ce.escala as escalamod, ce.orden as ordenescala, ce.mostrar as mostrarescala, e.nombre as nombreescala FROM escalas e inner join componentes_escalas ce  on(e.codigo_pk=ce.escala) WHERE e.codigo_pk=? ";
	
	
	public static String consultaDetalleCampoStr="SELECT cp.codigo_pk as codigopkcampo, cp.codigo as codigocampo, cp.nombre as nombrecampo, cp.nombre as nombreantiguo, cp.etiqueta as etiqueta, cp.etiqueta as etiquetaantigua, cp.tipo as tipocampo, cp.tipo as tipoantiguo, cp.tamanio as tamanocampo, cp.signo as signocampo, cp.unidad as unidadcampo, cp.unidad as unidadantigua, cp.valor_predeterminado as valorpredeterminado, cp.maximo as valormaximo, cp.minimo as valorminimo, cp.decimales as numerodecimales, cp.columnas_ocupadas as columnasocupadas, cp.orden as ordencampo, cp.unico_fila as unicofila, cp.requerido as requeridocampo, cp.formula as formula, cp.tipo_html as tipohtml, cp.tipo_html as excluyente, cp.mostrar as mostrarcampo, sp.orden as ordenseccion, cp.generar_alerta AS generaralerta FROM campos_parametrizables cp  inner join componentes_campos_sec ccs on(ccs.campo_parametrizable=cp.codigo_pk) inner join componentes_secciones cs on(cs.codigo_pk=ccs.componente_seccion)  inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) WHERE cp.codigo_pk=? ";
	
	
	private static final String cadenaModificacionEscalaStr="UPDATE componentes_escalas SET mostrar_modificacion=?  WHERE componente=? and escala=? ";
	
	
	private static final String cadenaModificacionSeccionStr="UPDATE secciones_parametrizables SET codigo=?, columnas_seccion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?, sexo=?, edad_inicial_dias=?, edad_final_dias=?, restriccion_val_campo=?  WHERE codigo_pk=? ";
	
	
	////////////////
	private static final String cadenaModificacionMostrarSeccionStr="UPDATE secciones_parametrizables SET mostrar=?, mostrar_modificacion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? ";
	
	
	////////////////
	private static final String cadenaModificacionMostrarCamposStr="UPDATE campos_parametrizables SET mostrar=?, mostrar_modificacion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? ";
	
	
	////////////////
	private static final String cadenaModificacionMostrarEscalaStr="UPDATE componentes_escalas SET mostrar=?, mostrar_modificacion=?  WHERE componente=? and escala=? ";
	
	
	private static final String modificarCampoStr="UPDATE campos_parametrizables SET codigo=?, nombre=?, etiqueta=?, tipo=?, tamanio=?, signo=?, unidad=?, valor_predeterminado=?, maximo=?, minimo=?, decimales=?, columnas_ocupadas=?, orden=?, unico_fila=?, requerido=?, formula=?, tipo_html=?, mostrar=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?,  generar_alerta = ?  WHERE codigo_pk=?";
	

	private static final String cadenaModificacionOpcionesStr="UPDATE opciones_val_parametrizadas SET opcion=?, valor=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE campo_parametrizable=? and codigo_pk=?";
	
	
	private static final String cadenaModificarSignosStr="UPDATE signos_vitales SET orden=?, activo=?  WHERE codigo=? ";
	
	/////
	private static final String cadenaModificacionOrdenCamposStr="UPDATE campos_parametrizables SET orden=? WHERE codigo_pk=? ";
	
	/////
	private static final String cadenaModificacionOrdenSeccionesStr="UPDATE secciones_parametrizables SET orden=? WHERE codigo_pk=? ";
	
	
	////////
	public static String consultaSubseccionStr="SELECT sp.codigo as codigoseccion, sp.descripcion as descripcionseccion, sp.parametrizable as parametrizable, sp.orden as ordenseccion, sp.columnas_seccion as columnasseccion, sp.codigo_pk as codigopkseccion, sp.seccion_padre as seccionpadre, sp.mostrar as mostrarseccion, sp.mostrar_modificacion as mostrarmodificacion, sp.tipo as tiposeccion, sp.sexo as sexoseccion, sp.edad_inicial_dias as edadinicial, sp.edad_final_dias as edadfinal, sp.restriccion_val_campo as indicativorestriccion FROM secciones_parametrizables sp  WHERE sp.seccion_padre=? and sp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"'";
	
	
	//////
	public static String consultaCamposSeccionStr="SELECT cp.codigo_pk as codigopkcampo, cp.codigo as codigocampo, cp.nombre as nombrecampo, cp.etiqueta as etiqueta, cp.tipo as tipocampo, cp.tamanio as tamaniocampo, cp.signo as signocampo, cp.unidad as unidadcampo, cp.valor_predeterminado as valorpredeterminado, cp.maximo as maximo, cp.minimo as minimo, cp.decimales as decimales, cp.columnas_ocupadas as columnasocupadas, cp.orden as ordencampo, cp.unico_fila as unicofila, cp.requerido as requeridocampo, cp.formula as formula, cp.tipo_html as tipohtml, cp.mostrar as mostrarcampo, sp.orden as ordenseccion, cp.generar_alerta AS generar_alerta FROM campos_parametrizables cp  inner join componentes_campos_sec ccs on(ccs.campo_parametrizable=cp.codigo_pk) inner join componentes_secciones cs on(cs.codigo_pk=ccs.componente_seccion)  inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) WHERE cs.seccion=? and cp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"'  order by ordencampo";
	
	
	public static String consultaOpcionesStr="SELECT codigo_pk as codigopk, opcion as opcion, valor as valor, campo_parametrizable as campoparametrizable  FROM opciones_val_parametrizadas WHERE campo_parametrizable=? ";
	
	
	public static String consultaSignosVitalesStr="SELECT codigo as codigosigno, nombre as nombresigno, unidad_medida as unidadmedida, orden as ordensigno, activo as activosigno, obligatorio as obligatorio, 0 as campo  FROM signos_vitales WHERE codigo_especialidad="+ConstantesBD.codigoEspecialidadValoracionGeneral+" ";
	
	
	public static String consultaOrdenCampoStr="SELECT c.codigo as codigocomponente, c.tipo_componente as tipocomponente, cp.codigo_pk as codigopkcampo, cp.codigo as codigocampo, cp.nombre as nombrecampo, cp.orden as ordencampo, sp.orden as ordenseccion FROM componentes c inner join componentes_secciones cs on(cs.componente=c.codigo) inner join componentes_campos_sec ccs on(ccs.componente_seccion=cs.codigo_pk) inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) inner join campos_parametrizables cp on(cp.codigo_pk=ccs.campo_parametrizable) WHERE c.tipo_componente=? and sp.codigo is null";
	
	
	public static String consultaOrdenSeccionStr="SELECT distinct c.tipo_componente as tipocomponente, sp.codigo as codigoseccion, sp.descripcion as descripcionseccion, sp.parametrizable as parametrizable, sp.orden as ordenseccion, sp.columnas_seccion as columnasseccion, sp.codigo_pk as codigopkseccion, sp.seccion_padre as seccionpadre, sp.mostrar as mostrarseccion, sp.mostrar_modificacion as mostrarmodificacion FROM componentes c inner join componentes_secciones cs on(cs.componente=c.codigo) inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) WHERE c.tipo_componente=? and sp.codigo is not null and sp.seccion_padre is null";
	
	
	public static String consultaOrdenEscalaStr="SELECT c.codigo as codigocomponente, c.tipo_componente as tipocomponente, ce.orden as ordenescala, ce.mostrar as mostrarescala, e.codigo_pk as codigopkescala, e.codigo as codigoescala, e.nombre as nombreescala, e.observaciones_requeridas as observrequeridas FROM componentes c inner join componentes_escalas ce on(ce.componente=c.codigo)  inner join escalas e on(e.codigo_pk=ce.escala) WHERE c.tipo_componente=? ";
	
	
	//////////
	public static String consultaEscalaModificarStr="SELECT mostrar_modificacion as mostrarmodificacion FROM componentes_escalas  WHERE componente=? AND escala=? ";
	
	public static String consultaSeccionesExistentesStr="select codigo, descripcion, mostrar_modificacion from componentes_secciones cs inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) where componente=? ";
	
	public static String consultaCamposExistentesStr="select cp.codigo as codigo, cp.nombre as nombre, cp.mostrar_modificacion as mostrarmodificacion from componentes_secciones cs inner join componentes_campos_sec ccs on(ccs.componente_seccion=cs.codigo_pk) inner join campos_parametrizables cp on(cp.codigo_pk=campo_parametrizable) inner join secciones_parametrizables sp on(sp.codigo_pk=cs.seccion) where cs.componente=? and sp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
	
	////
	public static String consultaCamposFormulaStr="select cp.codigo_pk as codigopk, cp.codigo as codigo, cp.nombre as nombre from componentes_secciones cs inner join componentes_campos_sec ccs on(ccs.componente_seccion=cs.codigo_pk) inner join campos_parametrizables cp on(cp.codigo_pk=ccs.campo_parametrizable) where cs.seccion=? and cp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' and cp.tipo in("+ConstantesCamposParametrizables.tipoCampoNumericoEntero+","+ConstantesCamposParametrizables.tipoCampoNumericoDecimal+") ";
	
	////Anexo 728
	public static String consultaSeccionesAsociadasStr="SELECT sp.codigo_pk as codigopk, sp.codigo as codigo, cs.codigo_pk as codigocomsec, sp.descripcion as descripcion, '"+ConstantesBD.acronimoNo+"' AS selec  FROM componentes c INNER JOIN componentes_secciones cs on(cs.componente=c.codigo) INNER JOIN secciones_parametrizables sp on(sp.codigo_pk=cs.seccion)  WHERE c.tipo_componente=? and sp.seccion_padre is null and sp.restriccion_val_campo='S' ";
	
	private static final String cadenaInsertarSeccionesAsociadasStr="INSERT INTO det_secciones (codigo_pk, codigo_pk_opciones, codigo_pk_plan_seccion, mostrar_modificacion, usuario_modifica, fecha_modifica, hora_modifica, codigo_pk_comp_seccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String cadenaInsertarValoresAsociadosStr="INSERT INTO det_valores (codigo_pk, codigo_pk_opciones, valor, mostrar_modificacion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	public static String consultaSeccionesGeneradasStr="SELECT ds.codigo_pk as codigopk, ds.codigo_pk_opciones as codigoopcion, ds.codigo_pk_comp_seccion as codseccion, sp.descripcion as seccion FROM det_secciones ds INNER JOIN componentes_secciones cs ON(cs.codigo_pk=ds.codigo_pk_comp_seccion) INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=cs.seccion) WHERE ds.codigo_pk_opciones=? ";
	
	public static String consultaValoresGeneradasStr="SELECT codigo_pk as codigopk, codigo_pk_opciones as codigoopcion, valor as valor FROM det_valores where codigo_pk_opciones=? ";
	
	private static final String cadenaEliminarValoresAsociadosStr="DELETE FROM historiaclinica.det_valores WHERE codigo_pk_opciones=?";
	
	
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public static HashMap<String, Object> consultaSecciones(Connection con, String tipoComponente) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaComponentesSeccionStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(tipoComponente));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static HashMap<String, Object> consultaCampos(Connection con, String tipoComponente) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaComponentesCampoStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(tipoComponente));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static HashMap<String, Object> consultaEscalas(Connection con, String tipoComponente) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaComponentesEscalaStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(tipoComponente));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarEscala(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarEscalaStr));
			
			/**
			 * INSERT INTO componentes_escalas (escala, componente, orden, mostrar, mostrar_modificacion) VALUES (?, ?, ?, ?, ?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("escala")+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("componente")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("orden")+""))
				ps.setObject(3, null);
			else	
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			ps.setString(4, vo.get("mostrar")+"");
			
			ps.setString(5, vo.get("mostrar_modificacion")+"");
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 */
	public static int insertarComponente(Connection con, String tipoComponente, String institucion, String loginUsuario) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarComponentesStr));
			
			/**
			 * INSERT INTO componentes (codigo, tipo_componente, activo, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)
			 */
			
			int codigoComponente=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_componentes");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(tipoComponente));
			
			ps.setString(3, ConstantesBD.acronimoSi);
			
			ps.setInt(4, Utilidades.convertirAEntero(institucion));
			
			ps.setString(5, loginUsuario);
			
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(7, UtilidadFecha.getHoraActual());
			
			ps.executeUpdate();
			
			return codigoComponente;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;	
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarSeccionStr));
			
			/**
			 * INSERT INTO secciones_parametrizables (
			 * codigo_pk, 
			 * codigo, 
			 * descripcion, 
			 * orden, 
			 * columnas_seccion, 
			 * seccion_padre, 
			 * mostrar, 
			 * mostrar_modificacion, 
			 * tipo, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			
			int codigoSeccion=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_secciones_param");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoSeccion+""));
			
			if(UtilidadTexto.isEmpty(vo.get("codigo")+""))
				ps.setNull(2, Types.NUMERIC);
			else
				ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("descripcion")+""))
				ps.setNull(3, Types.CHAR);
			else	
				ps.setString(3, vo.get("descripcion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("orden")+""))
				ps.setNull(4, Types.INTEGER);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			
			if(UtilidadTexto.isEmpty(vo.get("columnas_seccion")+""))
				ps.setNull(5, Types.INTEGER);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("columnas_seccion")+""));
			
			
			if(UtilidadTexto.isEmpty(vo.get("seccion_padre")+""))
				ps.setNull(6, Types.NUMERIC);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("seccion_padre")+""));
			
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(7, ConstantesBD.acronimoNo);
			else
				ps.setString(7, vo.get("mostrar")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar_modificacion")+""))
				ps.setString(8, ConstantesBD.acronimoNo);
			else
				ps.setString(8, vo.get("mostrar_modificacion")+"");
			
			
			ps.setString(9, vo.get("tipo")+"");
			
			ps.setInt(10, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			ps.setString(11, vo.get("usuario_modifica")+"");
			
			ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(13, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("sexo_seccion")+""))
				ps.setNull(14, Types.INTEGER);	
			else
				ps.setInt(14, Utilidades.convertirAEntero(vo.get("sexo_seccion")+""));
			
			if(Utilidades.convertirAEntero(vo.get("edad_inicial")+"")<0)
				ps.setNull(15, Types.INTEGER);
			else
				ps.setInt(15, Utilidades.convertirAEntero(vo.get("edad_inicial")+""));
			
			if(Utilidades.convertirAEntero(vo.get("edad_final")+"")<0)
				ps.setNull(16, Types.INTEGER);
			else
				ps.setInt(16, Utilidades.convertirAEntero(vo.get("edad_final")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("indicativo_restriccion")+""))
				ps.setString(17, ConstantesBD.acronimoNo);
			else
				ps.setString(17, vo.get("indicativo_restriccion")+"");
			
			ps.executeUpdate();
			
			return codigoSeccion;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @param codigoSeccion
	 * @return
	 */
	public static int insertarCompSeccion(Connection con, int codigoComponente, int codigoSeccion) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarComponenteSeccionStr));
			
			int codigoCompSeccion=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_componentes_secciones");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoCompSeccion+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(codigoComponente+""));
			
			ps.setDouble(3, Utilidades.convertirADouble(codigoSeccion+""));
			
			ps.executeUpdate();
			
			return codigoCompSeccion;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @param orden
	 * @return
	 */
	public static int insertarSeccionVacia(Connection con, String codigoInstitucion, String loginUsuario, String orden) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarSeccionVaciaStr));
			
			int codigoSeccionVacia=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_secciones_param");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoSeccionVacia+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(orden));
			
			ps.setInt(3, Utilidades.convertirAEntero(codigoInstitucion));
			
			ps.setString(4, loginUsuario);
			
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(6, UtilidadFecha.getHoraActual());
			
			ps.executeUpdate();
			
			return codigoSeccionVacia;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarCampo(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarCampoStr));
			
			int codigoCampo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_campos_param");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoCampo+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo")+""));
			
			ps.setString(3, vo.get("nombre")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("etiqueta")+""))
				ps.setNull(4, Types.CHAR);
			else
				ps.setString(4, vo.get("etiqueta")+"");
			
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("tipo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tamanio")+""))
				ps.setNull(6, Types.INTEGER);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("tamanio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("signo")+"")||(vo.get("signo")+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setNull(7, Types.CHAR);
			else
				ps.setString(7, vo.get("signo")+"");
			
			
			logger.info("HHHHHHH>>>>>>>>>>"+vo.get("unidad"));
			
			if(UtilidadTexto.isEmpty(vo.get("unidad")+"")||(vo.get("unidad")+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setNull(8, Types.INTEGER);
			else
				ps.setInt(8, Utilidades.convertirAEntero(vo.get("unidad")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("valor_predeterminado")+""))
				ps.setNull(9, Types.CHAR);
			else
				ps.setString(9, vo.get("valor_predeterminado")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("maximo")+""))
				ps.setNull(10, Types.NUMERIC);
			else
				ps.setDouble(10, Utilidades.convertirADouble(vo.get("maximo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("minimo")+""))
				ps.setNull(11, Types.NUMERIC);
			else
				ps.setDouble(11, Utilidades.convertirADouble(vo.get("minimo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("decimales")+""))
				ps.setNull(12, Types.INTEGER);
			else
				ps.setInt(12, Utilidades.convertirAEntero(vo.get("decimales")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("columnas_ocupadas")+""))
				ps.setNull(13, Types.INTEGER);
			else
				ps.setInt(13, Utilidades.convertirAEntero(vo.get("columnas_ocupadas")+""));
			
			
			if(UtilidadTexto.isEmpty(vo.get("orden")+""))
				ps.setNull(14, Types.INTEGER);
			else
				ps.setInt(14, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			ps.setString(15, vo.get("unico_fila")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("requerido")+""))
				ps.setString(16, ConstantesBD.acronimoNo);
			else
				ps.setString(16, vo.get("requerido")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("formula")+""))
				ps.setNull(17, Types.CHAR);
			else
				ps.setString(17, vo.get("formula")+"");
			
			ps.setString(18, vo.get("tipo_html")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(19, ConstantesBD.acronimoNo);
			else
				ps.setString(19, vo.get("mostrar")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar_modificacion")+""))
				ps.setString(20, ConstantesBD.acronimoNo);
			else
				ps.setString(20, vo.get("mostrar_modificacion")+"");
			
			ps.setInt(21, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			ps.setString(22, vo.get("usuario_modifica")+"");
			
			ps.setDate(23, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(24, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("alerta")+""))
				ps.setString(25, ConstantesBD.acronimoNo);
			else
				ps.setString(25, vo.get("alerta")+"");
			
			ps.executeUpdate();
			
			return codigoCampo;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}

	
	/**
	 * 
	 * @param con
	 * @param componenteSeccion
	 * @param codigoCampo
	 * @return
	 */
	public static boolean insertarCompCamposSec(Connection con, int componenteSeccion, int codigoCampo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarComponenteCampoSecStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoCampo+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(componenteSeccion+""));
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarOpciones(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarOpcionesStr));
			
			int codigoOpcion=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_opciones_val_param");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoOpcion+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("campo_parametrizable")+""));
			
			ps.setString(3, vo.get("opcion")+"");
			
			ps.setString(4, vo.get("valor")+"");
			
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			ps.setString(6, vo.get("usuario_modifica")+"");
			
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(8, UtilidadFecha.getHoraActual());
			
			ps.executeUpdate();
			
			return codigoOpcion;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarSubSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarSubSeccionStr));
			
			int codigoSeccion=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_secciones_param");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoSeccion+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo")+""));
			
			ps.setString(3, vo.get("descripcion")+"");
			
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("columnas_seccion")+""));
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("seccion_padre")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(7, ConstantesBD.acronimoNo);
			else
				ps.setString(7, vo.get("mostrar")+"");
			
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			ps.setString(9, vo.get("usuario_modifica")+"");
			
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(11, UtilidadFecha.getHoraActual());
			
			ps.setInt(12, Utilidades.convertirAEntero(vo.get("sexo_seccion")+""));
			
			ps.setInt(13, Utilidades.convertirAEntero(vo.get("edad_inicial")+""));
			
			ps.setInt(14, Utilidades.convertirAEntero(vo.get("edad_final")+""));
			
			ps.setString(15, vo.get("indicativo_restriccion")+"");
			
			ps.executeUpdate();
			
			return codigoSeccion;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param campo
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleCampo(Connection con, String campo) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleCampoStr));
			logger.info(consultaDetalleCampoStr +"  campo >"+campo);
			
			ps.setDouble(1, Utilidades.convertirADouble(campo));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleEscala(Connection con, String escala) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleEscalaStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(escala));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleSeccion(Connection con, String seccion) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleSeccionStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(seccion));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @param codigoEscala
	 * @param orden
	 * @param mostrar
	 * @return
	 */
	public static boolean modificarEscala(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionEscalaStr));
			
			ps.setObject(1, vo.get("mostrar_modificacion"));
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("componente")+""));
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("escala")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionSeccionStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigo")+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("columnas_seccion")+""));
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			if(UtilidadTexto.isEmpty(vo.get("sexo_seccion")+""))
				ps.setNull(6, Types.INTEGER);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("sexo_seccion")+""));
			if(Utilidades.convertirAEntero(vo.get("edad_inicial")+"")<0)
				ps.setNull(7, Types.INTEGER);
			else
				ps.setInt(7, Utilidades.convertirAEntero(vo.get("edad_inicial")+""));
			if(Utilidades.convertirAEntero(vo.get("edad_final")+"")<0)
				ps.setNull(8, Types.INTEGER);
			else
				ps.setInt(8, Utilidades.convertirAEntero(vo.get("edad_final")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("indicativo_restriccion")+""))
				ps.setString(9, ConstantesBD.acronimoNo);
			else
				ps.setString(9, vo.get("indicativo_restriccion")+"");
			
			ps.setDouble(10, Utilidades.convertirADouble(vo.get("codigopk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarCampo(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(modificarCampoStr));
			
			logger.info("modificarCampoStr");
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigo")+""));
			
			ps.setString(2, vo.get("nombre")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("etiqueta")+""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, vo.get("etiqueta")+"");
			
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("tipo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tamanio")+""))
				ps.setNull(5, Types.INTEGER);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("tamanio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("signo")+"")||(vo.get("signo")+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, vo.get("signo")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("unidad")+""))
				ps.setNull(7, Types.INTEGER);
			else
				ps.setInt(7, Utilidades.convertirAEntero(vo.get("unidad")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("valor_predeterminado")+""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, vo.get("valor_predeterminado")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("maximo")+""))
				ps.setNull(9, Types.NUMERIC);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("maximo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("minimo")+""))
				ps.setNull(10, Types.NUMERIC);
			else
				ps.setDouble(10, Utilidades.convertirADouble(vo.get("minimo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("decimales")+""))
				ps.setObject(11, null);
			else
				ps.setInt(11, Utilidades.convertirAEntero(vo.get("decimales")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("columnas_ocupadas")+""))
				ps.setNull(12, Types.INTEGER);
			else
				ps.setInt(12, Utilidades.convertirAEntero(vo.get("columnas_ocupadas")+""));
			
			
			if(UtilidadTexto.isEmpty(vo.get("orden")+""))
				ps.setNull(13, Types.INTEGER);
			else
				ps.setInt(13, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			ps.setString(14, vo.get("unico_fila")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("requerido")+""))
				ps.setString(15, ConstantesBD.acronimoNo);
			else
				ps.setString(15, vo.get("requerido")+"");
			
			logger.info("Formula sql > "+vo.get("formula"));
			
			if(UtilidadTexto.isEmpty(vo.get("formula")+""))
				ps.setObject(16, null);
			else
				ps.setObject(16, vo.get("formula")+"");
			
			ps.setString(17, vo.get("tipo_html")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(18, ConstantesBD.acronimoNo);
			else
				ps.setString(18, vo.get("mostrar")+"");
			
			ps.setString(19, vo.get("usuario_modifica")+"");
			
			ps.setDate(20, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(21, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("alerta")+""))
				ps.setString(22, ConstantesBD.acronimoNo);
			else
				ps.setString(22, vo.get("alerta")+"");
			
			ps.setDouble(23, Utilidades.convertirADouble(vo.get("codigopk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap<String, Object> consultaCamposSeccion(Connection con, String seccion) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaCamposSeccionStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(seccion));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap<String, Object> consultaSubseccion(Connection con, String seccion) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSubseccionStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(seccion));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param campo
	 * @return
	 */
	public static HashMap<String, Object> consultarOpciones(Connection con, String campo) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaOpcionesStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(campo));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarOpciones(Connection con, HashMap vo) 
	{
		boolean retorna= false;
		PreparedStatementDecorator ps= null;
		try
		{
			logger.info("\n modificarOpciones->"+cadenaModificacionOpcionesStr+" opcion->"+vo.get("opcion")+" valor->"+vo.get("valor")+" usu->"+vo.get("usuario_modifica")+" fecha->"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+" camp->"+vo.get("campo_parametrizable")+" codigopk->"+vo.get("codigo_pk")+"\n");
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionOpcionesStr));
			
			ps.setString(1, vo.get("opcion")+"");
			ps.setString(2, vo.get("valor")+"");
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("campo_parametrizable")+""));
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			retorna= ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultarSignosVitales(Connection con) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSignosVitalesStr));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarSignos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarSignosStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("visible")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("visible")+"");
			
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("codigo")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultarCodigosSeccion(Connection con) 
	{
		
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static HashMap<String, Object> consultarOrdenCampo(Connection con, String tipoComponente) 
	{
		PreparedStatementDecorator ps= null;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaOrdenCampoStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(tipoComponente));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static HashMap<String, Object> consultarOrdenEscala(Connection con, String tipoComponente) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaOrdenEscalaStr));
			
			
			ps.setInt(1, Utilidades.convertirAEntero(tipoComponente));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public static HashMap<String, Object> consultarOrdenSeccion(Connection con, String tipoComponente) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaOrdenSeccionStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(tipoComponente));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public static int consultarCodigoComponete(Connection con, String componente) 
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaCodigoComponente));
			
			ps.setInt(1, Utilidades.convertirAEntero(componente));
			
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}	
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarSeccionStr));
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(1, ConstantesBD.acronimoNo);
			else
				ps.setString(1, vo.get("mostrar")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar_modificacion")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("mostrar_modificacion")+"");
			
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigopk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarCampos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarCamposStr));
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(1, ConstantesBD.acronimoNo);
			else
				ps.setString(1, vo.get("mostrar")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar_modificacion")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("mostrar_modificacion")+"");
			
			ps.setString(3, vo.get("usuario_modifica")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigopk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarEscala(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarEscalaStr));
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(1, ConstantesBD.acronimoNo);
			else
				ps.setString(1, vo.get("mostrar")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar_modificacion")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("mostrar_modificacion")+"");
			
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("componente")+""));
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("escala")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @param codigoSeccion
	 * @return
	 */
	public static int consultarCodigoCompSeccion(Connection con, int codigoComponente, int codigoSeccion) 
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaCodigoComponenteSeccion));
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente+""));
			ps.setDouble(2, Utilidades.convertirADouble(codigoSeccion+""));
			
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}	
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @param escala
	 * @return
	 */
	public static boolean consultaEscalasModificar(Connection con, int componente, String escala) 
	{
		PreparedStatementDecorator ps=  null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaEscalaModificarStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(componente+""));
			ps.setDouble(2, Utilidades.convertirADouble(escala));
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}	
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public static HashMap<String, Object> consultarSeccionesExitentes(Connection con, String componente) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSeccionesExistentesStr));
			
			logger.info("consultaSeccionesExistentesStr>>>>>>>>>>>"+consultaSeccionesExistentesStr);
			logger.info("componente>>>>>>>>>>>"+componente);
			
			ps.setDouble(1, Utilidades.convertirADouble(componente));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public static HashMap<String, Object> consultarCamposExitentes(Connection con, String componente) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps=  null;
		
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaCamposExistentesStr));
			
			logger.info("consultaCamposExistentesStr >>>>>>>>>>>"+consultaCamposExistentesStr);
			logger.info("componente>>>>>>>>>>>"+componente);
			
			ps.setDouble(1, Utilidades.convertirADouble(componente));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	public static String consultarFormula(Connection con, String codigoCampo) 
	{
		PreparedStatementDecorator ps=  null;
		String consultaStr="select formula from campos_parametrizables where codigo_pk = ? and tipo="+ConstantesCamposParametrizables.tipoCampoFormula+" ";
		ResultSetDecorator resultado=null; 
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			ps.setDouble(1, Utilidades.convertirADouble(codigoCampo));
			resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getString("formula") ;
			}
			else
			{
				return "";
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la fecha de apertura de la cuenta: "+e);
			return "";
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
			if(resultado!=null){
				try{
					resultado.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public static HashMap consultarCamposFormula(Connection con, String codigoSeccion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps=  null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaCamposFormulaStr));
			
			logger.info("consultaCamposFormulaStr>>>>>>>>>>>"+consultaCamposFormulaStr);
			logger.info("codigoSeccion>>>>>>>>>>>"+codigoSeccion);
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoSeccion));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public static HashMap consultarSeccionesAsociadas(Connection con, String componente) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps=  null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSeccionesAsociadasStr));
			
			logger.info("consultaSeccionesAsociadasStr >>>>>>>>>>>"+consultaSeccionesAsociadasStr);
			logger.info("componente >>>>>>>>>>>"+componente);
			
			ps.setDouble(1, Utilidades.convertirADouble(componente));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarSeccionesAsocidas(Connection con, HashMap vo) 
	{ 
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSeccionesAsociadasStr));
			
			int codigoSeccionAsociada=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_secciones");
			
			
			ps.setDouble(1, codigoSeccionAsociada);
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo_opcion")+""));
			
			ps.setNull(3, Types.INTEGER);
			
			ps.setString(4, vo.get("mostrar_modificacion")+"");
			
			ps.setString(5, vo.get("usuario_modifica")+"");
			
			ps.setString(6, vo.get("fecha_modifica")+"");
			
			ps.setString(7, vo.get("hora_modifica")+"");
			
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("componente_seccion")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarValoresAsociados(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarValoresAsociadosStr));
			
			int codigoValorAsociado=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_valores");
			
			ps.setDouble(1, codigoValorAsociado);
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo_opcion")+""));
			
			ps.setString(3, vo.get("valor")+"");
			
			ps.setString(4, vo.get("mostrar_modificacion")+"");
			
			ps.setString(5, vo.get("usuario_modifica")+"");
			
			ps.setString(6, vo.get("fecha_modifica")+"");
			
			ps.setString(7, vo.get("hora_modifica")+"");
			
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean eliminarValoresAsociados(Connection con, double codigoPkOpciones) 
	{
		boolean transaccionExitosa=false;
		PreparedStatementDecorator psd= null;
		try 
		{
			psd=new PreparedStatementDecorator(con, cadenaEliminarValoresAsociadosStr);
			psd.setDouble(1,codigoPkOpciones);
			
			
			logger.info("LA CONSULTA DE ELIMINAR VALORES ASOCIADOS------>"+psd);
			
			if(psd.executeUpdate()>=0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,null,null);
				transaccionExitosa=true;
			}
			else
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,null,null);
				transaccionExitosa=false;
			}
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN borrarDx==> "+e);
		}finally {			
			if(psd!=null){
				try{
					psd.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		
		return transaccionExitosa;
	}


	
	/**
	 * 
	 * @param con
	 * @param codigoOpcion
	 * @return
	 */
	public static HashMap consultarSeccionesGeneradas(Connection con, String codigoOpcion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSeccionesGeneradasStr));
			
			logger.info("consultaSeccionesGeneradasStr >>>>>>>>>>>"+consultaSeccionesGeneradasStr);
			logger.info("componente >>>>>>>>>>>"+codigoOpcion);
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoOpcion));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoOpcion
	 * @return
	 */
	public static HashMap consultarValoresGeneradas(Connection con, String codigoOpcion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaValoresGeneradasStr));
			
			logger.info("consultaValoresGeneradasStr >>>>>>>>>>>"+consultaValoresGeneradasStr);
			logger.info("componente >>>>>>>>>>>"+codigoOpcion);
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoOpcion));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarOrdenCampos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps=null;
		try
		{
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionOrdenCamposStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigo")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarOrdenSecciones(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionOrdenSeccionesStr));
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("orden")+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigo")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseParametrizacionComponentesDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}
	

}