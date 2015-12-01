package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.HistoDetalleMonto;

/**
 * Esta clase se encarga de 
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public class SQLBaseHistoMontoAgrupacionArticuloDAO {
	
	/**
	 * 
	 * Este Método se encarga de insertar un registro del histórico de
	 * una agrupación de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOHistoMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static int insertarHistoMontoAgrupacionArticulo(Connection conn,DTOBusquedaMontoAgrupacionArticulo dto, 
			HistoDetalleMonto histoDetalle, int TIPO_BD) {
		try
		{
			String secuencia="";
			int codigoInstitucion=0;
			int codigoGrupo=0;
			int valorSecuencia=0;
			Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
			String fechaFormateada = "";
			
			switch (TIPO_BD) {
			case DaoFactory.ORACLE:
				 valorSecuencia = UtilidadBD.obtenerSiguienteValorSecuencia(conn, "facturacion.seq_his_monto_agrup_art");
				 fechaFormateada = UtilidadFecha.conversionFormatoFechaABD(fechaActual);
				break;
			case DaoFactory.POSTGRESQL:
				secuencia = "nextval('facturacion.seq_his_monto_agrup_art')";
				fechaFormateada = UtilidadFecha.conversionFormatoFechaABD(fechaActual);
				break;

			default:
				break;
			}
			
			String[] codigoNaturaleza = new String[2];
			String[] codigosGrupo = new String[2];
			if(dto.getCodigoNaturaleza()!=null && !dto.getCodigoNaturaleza().equals("")){	
				codigoNaturaleza = dto.getCodigoNaturaleza().split("-");
				codigoInstitucion=Integer.valueOf(codigoNaturaleza[1]).intValue();
			}else{
				codigoNaturaleza[0]="";
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				codigosGrupo = dto.getGrupoCodigoConcatenado().split("-");
				codigoGrupo=Integer.valueOf(codigosGrupo[0]);
			}
			
			String horaActual = UtilidadFecha.conversionFormatoHoraABD(Calendar
					.getInstance().getTime());
			
			String query="insert into facturacion.histo_monto_agru_artic ( codigo_pk, histo_detalle_monto";
			
			if((dto.getCodigoSubgrupoInventario()!=0) &&
					(dto.getCodigoSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				query+=", subgrupo_inventario";				
			}	
			
			if(!UtilidadTexto.isEmpty(dto.getCodigoNaturaleza())){
				query+=", naturaleza_articulo, institucion";				
			}
			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				query+=", grupo_inventario ";				
			}
			if(dto.getClaseInventarioCodigo()!=ConstantesBD.codigoNuncaValido){
				query+=", clase_inventario ";				
			}
			if(dto.getCantidadArticulo()!=null && dto.getCantidadArticulo()>0){
				query+=", cantidad_articulos ";
			}
			
			query+=", cantidad_monto, valor_monto, fecha_registro, hora_registro, usuario_registra, accion_realizada)";
			
			if(UtilidadTexto.isEmpty(secuencia)){
				query+= " values ( "+valorSecuencia+", "+histoDetalle.getCodigoPk();
			}else{
				query+= " values ( "+secuencia+", "+histoDetalle.getCodigoPk();
			}				
			
			
			if((dto.getCodigoSubgrupoInventario()!=0) &&
					(dto.getCodigoSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
				query+=", "+dto.getCodigoSubgrupoInventario();
			}			
			if(!UtilidadTexto.isEmpty(dto.getCodigoNaturaleza())){
				query+=", '"+codigoNaturaleza[0]+"', "+codigoInstitucion;
			}			
			if(!UtilidadTexto.isEmpty(dto.getGrupoCodigoConcatenado())){
				query+=", "+codigoGrupo;
			}
			if(dto.getClaseInventarioCodigo()!=ConstantesBD.codigoNuncaValido){
				query+=", "+dto.getClaseInventarioCodigo();
			}
			if(dto.getCantidadArticulo()!=null && dto.getCantidadArticulo()>0){
				query+=", "+dto.getCantidadArticulo();
			}
			query+= ", "+dto.getCantidadMonto()+", "+dto.getValorMonto()+", '"+fechaFormateada+"', '"+horaActual+"', '"+
			        histoDetalle.getUsuarios().getLogin()+"', '"+histoDetalle.getAccionRealizada()+"' ) ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(conn.prepareStatement(query,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));			
						 			
			int filasAfectadas = ps.executeUpdate();
			
			return filasAfectadas;			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}		
		return 0;
	}

}
