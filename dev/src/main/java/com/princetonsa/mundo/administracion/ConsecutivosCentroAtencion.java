package com.princetonsa.mundo.administracion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;
import com.princetonsa.enu.administracion.EmunConsecutivoFacturacionCentroAtencion;
import com.princetonsa.enu.administracion.EmunConsecutivoFacturasVariasCentroAtencion;
import com.princetonsa.enu.administracion.EmunConsecutivosTesoreriaCentroAtencion;
import com.princetonsa.sort.odontologia.SortGenerico;
/**
 * 
 * @author axioma
 *
 */
public class ConsecutivosCentroAtencion {
	
	      
	/**
	 * 
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosFacturacion = new ArrayList<DtoConsecutivoCentroAtencion>();

	/**
	 * 
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivoTesoreria = new ArrayList<DtoConsecutivoCentroAtencion>();

	/**
	 * 
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivos = new ArrayList<DtoConsecutivoCentroAtencion>();

	private  int tmpCodigoCentroAtencion;
	
	/**
	 * Lista con los consecutivos del centro de atención relacionados con la factura varia
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosCentroFacturasVarias;
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean guardar(DtoConsecutivoCentroAtencion dto, Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().guardar(dto, con);
		
	}
	
	
	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivosFacturacion() {
		return listaConsecutivosFacturacion;
	}


	public void setListaConsecutivosFacturacion(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivosFacturacion) {
		this.listaConsecutivosFacturacion = listaConsecutivosFacturacion;
	}


	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivoTesoreria() {
		return listaConsecutivoTesoreria;
	}


	public void setListaConsecutivoTesoreria(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivoTesoreria) {
		this.listaConsecutivoTesoreria = listaConsecutivoTesoreria;
	}


	public ArrayList<DtoConsecutivoCentroAtencion> getListaConsecutivos() {
		return listaConsecutivos;
	}


	public void setListaConsecutivos(
			ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivos) {
		this.listaConsecutivos = listaConsecutivos;
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoConsecutivoCentroAtencion> cargar( DtoConsecutivoCentroAtencion dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().cargar(dto);
	}
	

	
	/**
	 * 
	 * @param con Conexión con la BD
	 * @param dto
	 * @return
	 */
	public  boolean modificar(Connection con, DtoConsecutivoCentroAtencion dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().modificar(con, dto);
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public    boolean eliminar (DtoConsecutivoCentroAtencion dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().eliminar(dto);
	}
	
	
	/**
	 * 
	 */
	public void cleanListas()
	{
		this.setListaConsecutivosFacturacion(new ArrayList<DtoConsecutivoCentroAtencion>());
		this.setListaConsecutivoTesoreria(new ArrayList<DtoConsecutivoCentroAtencion>());
		this.setListaConsecutivosCentroFacturasVarias(new ArrayList<DtoConsecutivoCentroAtencion>());
	}
	

	/**
	 *  METODO QUE RECIBE UN DTO CONSECUTIVO CENTRO ATENCION Y RETORNA true ESTA ASIGNADO EN OTRO CASO ES false
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public boolean esConsecutivoAsignado(DtoConsecutivoCentroAtencion dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().esConsecutivoAsignado(dto);
	}

	
	/**
	 * 
	 * @param dto
	 */
	public void cargarConsecutivos(DtoConsecutivoCentroAtencion dto)
	{
		cleanListas();
		this.setTmpCodigoCentroAtencion(dto.getCentroAtencion().getCodigo());
		this.setListaConsecutivos(cargar(dto));
		this.cargarConsecutivosFacturas(this.getListaConsecutivos());
		this.cargarConsecutivosTesoreria(this.getListaConsecutivos(), dto.getCodigoInstitucion());
		this.cargarConsecutivosFacturasVarias(this.getListaConsecutivos());
	}
	

	
	/**
	 * 	CARGAR LOS CONSECUTIVOS FACTURACION
	 * @return
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> cargarConsecutivosFacturas(ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivos)
	{
		
		boolean noEncuentra=Boolean.TRUE;
		this.setListaConsecutivosFacturacion(new ArrayList<DtoConsecutivoCentroAtencion>());
		int indice=0;

		int anioActual= utilidadAnio();
		
		
		for(EmunConsecutivoFacturacionCentroAtencion consFactura: EmunConsecutivoFacturacionCentroAtencion.values())
		{
			noEncuentra=Boolean.TRUE;
			
			
			for (DtoConsecutivoCentroAtencion dtoConCenAten : listaConsecutivos) 
			{
				
			 	if( Utilidades.convertirAEntero(dtoConCenAten.getAnio())==anioActual) 
			 	{	
			 		if(     (dtoConCenAten.getNombreConsecutivo().equals(consFactura.getNombreConsecutivoBaseDatos())) && (dtoConCenAten.getActivo().equals(ConstantesBD.acronimoSi))    )  
					{
						DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
						nDto=dtoConCenAten; //asignar el que bien de Base de datos
						nDto.setCodigoIndiceArray(indice);
						nDto.setNombreConsecutivoInterfazGrafica(consFactura.getNombreConsecutivoInterfazGrafica());
						this.getListaConsecutivosFacturacion().add(nDto);
						noEncuentra=Boolean.FALSE;
					}
			 	}
			 	else
			 	{
			 		if (UtilidadTexto.isEmpty(dtoConCenAten.getAnio())  &&  dtoConCenAten.getIdAnual().equals(ConstantesBD.acronimoSi)      &&     (dtoConCenAten.getNombreConsecutivo().equals(consFactura.getNombreConsecutivoBaseDatos()))    &&     (dtoConCenAten.getActivo().equals(ConstantesBD.acronimoSi)) )
			 		{
			 			DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
						nDto=dtoConCenAten; //asignar el que bien de Base de datos
						nDto.setCodigoIndiceArray(indice);
						nDto.setNombreConsecutivoInterfazGrafica(consFactura.getNombreConsecutivoInterfazGrafica());
						this.getListaConsecutivosFacturacion().add(nDto);
						noEncuentra=Boolean.FALSE;
			 		}
			 	}
			}
			
			if(noEncuentra)
			{
				DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
				nDto.setNombreConsecutivoInterfazGrafica(consFactura.getNombreConsecutivoInterfazGrafica());
				nDto.setNombreConsecutivo(consFactura.getNombreConsecutivoBaseDatos());
				nDto.setCodigoIndiceArray(indice);
				nDto.getCentroAtencion().setCodigo(this.getTmpCodigoCentroAtencion());
				this.getListaConsecutivosFacturacion().add(nDto);
			}
			indice++;
		}
		
		return this.getListaConsecutivosFacturacion();
		
	}
	

	
	/**
	 * 
	 * Método que se carga los consecutivos por centro de Atención
	 * relacionados con las Facturas Varias.
	 * 
	 * @return
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> cargarConsecutivosFacturasVarias (ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivos)
	{
		
		boolean noEncuentra=Boolean.TRUE;
		this.setListaConsecutivosCentroFacturasVarias(new ArrayList<DtoConsecutivoCentroAtencion>());
		int indice=0;

		int anioActual= utilidadAnio();
		
		for(EmunConsecutivoFacturasVariasCentroAtencion consecutivoFacturaVaria: EmunConsecutivoFacturasVariasCentroAtencion.values())
		{
			noEncuentra=Boolean.TRUE;
			
			
			for (DtoConsecutivoCentroAtencion dtoConsCentroAtencion : listaConsecutivos) 
			{
				
			 	if( Utilidades.convertirAEntero(dtoConsCentroAtencion.getAnio())==anioActual) 
			 	{	
			 		if((dtoConsCentroAtencion.getNombreConsecutivo().equals(consecutivoFacturaVaria.getNombreConsecutivoBaseDatos())) && (dtoConsCentroAtencion.getActivo().equals(ConstantesBD.acronimoSi))    )  
					{
						DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
						nDto=dtoConsCentroAtencion;
						nDto.setCodigoIndiceArray(indice);
						nDto.setNombreConsecutivoInterfazGrafica(consecutivoFacturaVaria.getNombreConsecutivoInterfazGrafica());
						this.getListaConsecutivosCentroFacturasVarias().add(nDto);
						noEncuentra=Boolean.FALSE;
					}
			 	}
			 	else
			 	{
			 		if (  UtilidadTexto.isEmpty(dtoConsCentroAtencion.getAnio())  &&  dtoConsCentroAtencion.getIdAnual().equals(ConstantesBD.acronimoSi)      &&     (dtoConsCentroAtencion.getNombreConsecutivo().equals(consecutivoFacturaVaria.getNombreConsecutivoBaseDatos()))    &&     (dtoConsCentroAtencion.getActivo().equals(ConstantesBD.acronimoSi)) )
			 		{
			 			DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
						nDto=dtoConsCentroAtencion;
						nDto.setCodigoIndiceArray(indice);
						nDto.setNombreConsecutivoInterfazGrafica(consecutivoFacturaVaria.getNombreConsecutivoInterfazGrafica());
						this.getListaConsecutivosCentroFacturasVarias().add(nDto);
						noEncuentra=Boolean.FALSE;
			 		}
			 	}
			}
			
			if(noEncuentra)
			{
				DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
				nDto.setNombreConsecutivoInterfazGrafica(consecutivoFacturaVaria.getNombreConsecutivoInterfazGrafica());
				nDto.setNombreConsecutivo(consecutivoFacturaVaria.getNombreConsecutivoBaseDatos());
				nDto.setCodigoIndiceArray(indice);
				nDto.getCentroAtencion().setCodigo(this.getTmpCodigoCentroAtencion());
				this.getListaConsecutivosCentroFacturasVarias().add(nDto);
			}
			indice++;
		}
		
		return this.getListaConsecutivosCentroFacturasVarias();
	}
	
	
	/**
	 * 
	 * @return
	 */
	private int utilidadAnio() {
		String tmpAnio[]= UtilidadFecha.getFechaActual().split("/");
		int anio=Utilidades.convertirAEntero(tmpAnio[2]);
		return anio;
	}


	/**
	 * 
	 * CARGAR CONSECUTIVOS TESORERÍA
	 * 
	 * @param listaConsecutivos
	 * @param codigoInstitucion
	 * @return
	 */
	private  ArrayList<DtoConsecutivoCentroAtencion> cargarConsecutivosTesoreria(ArrayList<DtoConsecutivoCentroAtencion> listaConsecutivos, int codigoInstitucion)
	{
		
		boolean noEncuentra=Boolean.TRUE;
		this.setListaConsecutivoTesoreria( new ArrayList<DtoConsecutivoCentroAtencion>());
		
		int indice=0;
		int anioActual=utilidadAnio();
		

		for(EmunConsecutivosTesoreriaCentroAtencion consecutivoTesoreria: EmunConsecutivosTesoreriaCentroAtencion.values())
		{

			/*
			 * Se tiene en cuenta el valor de los parámetros generales 
			 * Maneja Consecutivos Notas Devolución abonos Paciente por Centro de Atención y 
			 * Maneja Consecutivos de Recibos de Caja por Centro de Atención para saber si se muestran
			 * o no los respectivos consecutivos.
			 */
			if(isMostrarConsecutivoTesoreria(consecutivoTesoreria, codigoInstitucion)){

				noEncuentra=true;

				for (DtoConsecutivoCentroAtencion dtoConCenAten : listaConsecutivos) 
				{
					// 
					if( Utilidades.convertirAEntero(dtoConCenAten.getAnio())==anioActual) 
					{

						if(  dtoConCenAten.getActivo().equals(ConstantesBD.acronimoSi)    &&     dtoConCenAten.getNombreConsecutivo().equals(consecutivoTesoreria.getNombreConsecutivoBaseDatos())  )
						{
							DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
							nDto=dtoConCenAten;
							nDto.setCodigoIndiceArray(indice);
							nDto.setNombreConsecutivoInterfazGrafica(consecutivoTesoreria.getNombreConsecutivoInterfazGrafica());
							this.getListaConsecutivoTesoreria().add(nDto);
							noEncuentra=Boolean.FALSE;
						}
					}
					//SI APLICA PARA ID ANUAL
					// CODICIONES 1. QUE SEA ANIO VACIO, 2. 
					else
					{
						if(UtilidadTexto.isEmpty(dtoConCenAten.getAnio()) &&  dtoConCenAten.getNombreConsecutivo().equals(consecutivoTesoreria.getNombreConsecutivoBaseDatos())  && dtoConCenAten.getActivo().equals(ConstantesBD.acronimoSi) &&     dtoConCenAten.getIdAnual().equals(ConstantesBD.acronimoSi) )
						{
							DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
							nDto=dtoConCenAten;
							nDto.setCodigoIndiceArray(indice);
							nDto.setNombreConsecutivoInterfazGrafica(consecutivoTesoreria.getNombreConsecutivoInterfazGrafica());
							this.getListaConsecutivoTesoreria().add(nDto);
							noEncuentra=Boolean.FALSE;
						}

					}
				}

				if(noEncuentra)
				{
					DtoConsecutivoCentroAtencion nDto = new DtoConsecutivoCentroAtencion();
					nDto.setNombreConsecutivoInterfazGrafica(consecutivoTesoreria.getNombreConsecutivoInterfazGrafica());
					nDto.setNombreConsecutivo(consecutivoTesoreria.getNombreConsecutivoBaseDatos());
					nDto.setCodigoIndiceArray(indice);
					nDto.getCentroAtencion().setCodigo(this.getTmpCodigoCentroAtencion());

					this.getListaConsecutivoTesoreria().add(nDto);
				}

				indice++;
			}
		}
		
		return this.getListaConsecutivoTesoreria();
	}

	/**
	 * Método que determina si se puede o no mostrar un consecutivo específico
	 * de tesorería para su modificación, teniendo en cuenta lo que indica el parámetro general que aplique
	 * según el caso
	 * 
	 * @param consecutivoTesoreria
	 * @param codigoInstitucion
	 * @return
	 */
	private boolean isMostrarConsecutivoTesoreria(EmunConsecutivosTesoreriaCentroAtencion consecutivoTesoreria, int codigoInstitucion){
		
		boolean resultado = false;
		
		if(EmunConsecutivosTesoreriaCentroAtencion.NotasDebitoPacientes.equals(consecutivoTesoreria) && 
				UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(codigoInstitucion))) {
			if (ValoresPorDefecto.getNaturalezaNotasPacientesManejar(codigoInstitucion).
					equals(ConstantesIntegridadDominio.acronimoDebito) ||
					ValoresPorDefecto.getNaturalezaNotasPacientesManejar(codigoInstitucion).
					equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
				resultado = true;
			}
		} else if(EmunConsecutivosTesoreriaCentroAtencion.NotasCreditoPacientes.equals(consecutivoTesoreria) && 
				UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(codigoInstitucion))) {
			if (ValoresPorDefecto.getNaturalezaNotasPacientesManejar(codigoInstitucion).
					equals(ConstantesIntegridadDominio.acronimoCredito) ||
					ValoresPorDefecto.getNaturalezaNotasPacientesManejar(codigoInstitucion).
					equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
				resultado = true;
			}
		} else if((EmunConsecutivosTesoreriaCentroAtencion.ReciboCaja.equals(consecutivoTesoreria) ||
				EmunConsecutivosTesoreriaCentroAtencion.AnulacionReciboCaja.equals(consecutivoTesoreria)) && 
				UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(codigoInstitucion))){
			
			resultado = true;
		}
	
		return resultado;
	}
	
	
	/**
	 * Metodo para obtener el valor actual del consecutivo x centro de atencion
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @param anio
	 * @return
	 */
	public static BigDecimal obtenerValorActualConsecutivo(int centroAtencion, String nombreConsecutivo, int anio)
	{
		Connection con= UtilidadBD.abrirConexion();
		BigDecimal r= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().obtenerValorActualConsecutivo(con, centroAtencion, nombreConsecutivo, anio);
		UtilidadBD.closeConnection(con);
		return r;
	}
	
	/**
	 * Metodo para obtener el valor actual del consecutivo x centro de atencion
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @param anio
	 * @return
	 */
	public static BigDecimal obtenerValorActualConsecutivo(Connection con, int centroAtencion, String nombreConsecutivo, int anio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().obtenerValorActualConsecutivo(con, centroAtencion, nombreConsecutivo, anio);
	}
	
	/**
	 * Metodo para incrementar el consecutivo, recibe la coneccion porque puede ser transaccional
	 * @param con
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 */
	public static BigDecimal incrementarConsecutivoXCentroAtencion(int centroAtencion, String nombreConsecutivo, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().incrementarConsecutivo(centroAtencion, nombreConsecutivo, institucion);
	}
	

	public void setTmpCodigoCentroAtencion(int tmpCodigoCentroAtencion) {
		this.tmpCodigoCentroAtencion = tmpCodigoCentroAtencion;
	}


	public int getTmpCodigoCentroAtencion() {
		return tmpCodigoCentroAtencion;
	}
	
	
	/**
	 *	ORDENAMIENTO DE LISTAS 
	 *	@param listaHistorico
	 */
	public void ordenarListaPorAnio(ArrayList<DtoConsecutivoCentroAtencion> listaHistorico)
	{
		SortGenerico sortG=new SortGenerico( "Anio",Boolean.TRUE);
		Collections.sort( listaHistorico,sortG);
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
	
	public static boolean finalizarConsecutivo(Connection con, String consecutivo, int centroAtencion, BigDecimal valor)
	{
		int anio=UtilidadFecha.getAnioActual();
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().cambiarUsoFinalizadoConsecutivo(con, consecutivo, centroAtencion, valor, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi, anio);
	}

	public static boolean liberarConsecutivo(Connection con, String consecutivo, int centroAtencion, BigDecimal valor)
	{
		int anio=UtilidadFecha.getAnioActual();
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosCentroAtencionDao().cambiarUsoFinalizadoConsecutivo(con, consecutivo, centroAtencion, valor, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo, anio);
	}

}
