package com.servinte.axioma.generadorReporte.capitacion.GeneradorReporteTotalServiciosArticulosValorizadosPorConvenio;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.reportes.dinamico.complex.AbstractReportMain;

import com.princetonsa.dto.capitacion.DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;


/**
 * @author Cristhian Murillo
 */
@SuppressWarnings("static-access")
public class GeneradorReporteTotalServArtPorConvenioDinamico extends AbstractReportMain 
	<GeneradorReporteTotalServArtPorConvenioDinamicoDisenio, 
	GeneradorReporteTotalServArtPorConvenioDinamicoDatos> 
{
	
	public GeneradorReporteTotalServArtPorConvenioDinamico(DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporteSet) {
		setDatosReporte(datosReporteSet);
		super.build();
	}
	
	/** * Datos del reporte */
	public static DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte = null;
	
	
	@Override
	protected GeneradorReporteTotalServArtPorConvenioDinamicoDisenio getReportDesign() {
		return new GeneradorReporteTotalServArtPorConvenioDinamicoDisenio(this.datosReporte);
	}
	
	
	@Override
	protected GeneradorReporteTotalServArtPorConvenioDinamicoDatos getReportData() 
	{
		return new GeneradorReporteTotalServArtPorConvenioDinamicoDatos(this.datosReporte);
	}
	
	
	public static void main(String[] args) 
	{
		new GeneradorReporteTotalServArtPorConvenioDinamico(llenarDatosPrueba());
	}
	

	/**
	 * Este Método se encarga de obtener el valor del atributo datosReporte
	 * @return retorna la variable datosReporte 
	 * @author Cristhian Murillo
	 */
	public static DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio getDatosReporte() {
		return datosReporte;
	}

	
	/**
	 * Este Método se encarga de establecer el valor del atributo datosReporte
	 * @param valor para el atributo datosReporte 
	 * @author Cristhian Murillo
	 */
	public static void setDatosReporte(
			DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporteSet) {
		datosReporte = datosReporteSet;
	}
	
	

	public static DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio llenarDatosPrueba()
    {
 		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotalesN1 = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotalesN2 = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotalesN3 = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotalesN4 = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotalesN5 = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotalesN6 = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio enero = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		enero.setCantidadArticulos(10); enero.setCantidadServicios(11);
		enero.setNombreMes("Enero");   enero.setNumeroMes(1);
		enero.setValorArticulos(new BigDecimal(100.0)); enero.setValorServicios(new BigDecimal(110.0));
		enero.setGrupoServicio("gs 1");
		enero.setClaseInventario("ci 1");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio enero1 = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		enero1.setCantidadArticulos(20); enero1.setCantidadServicios(22);
		enero1.setNombreMes("Enero");   enero1.setNumeroMes(1);
		enero1.setValorArticulos(new BigDecimal(200.0)); enero1.setValorServicios(new BigDecimal(220.0));
		enero1.setGrupoServicio("gs 1");
		enero1.setClaseInventario("ci 1");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio febrero = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		febrero.setCantidadArticulos(30); febrero.setCantidadServicios(33);
		febrero.setNombreMes("Febrero"); febrero.setNumeroMes(2);
		febrero.setValorArticulos(new BigDecimal(300.0)); febrero.setValorServicios(new BigDecimal(330.0));
		febrero.setGrupoServicio("gs 2");
		febrero.setClaseInventario("ci 2");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio febrero1 = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		febrero1.setCantidadArticulos(40); febrero1.setCantidadServicios(44);
		febrero1.setNombreMes("Febrero"); febrero1.setNumeroMes(2);
		febrero1.setValorArticulos(new BigDecimal(400.0)); febrero1.setValorServicios(new BigDecimal(440.0));
		febrero1.setGrupoServicio("gs 3");
		febrero1.setClaseInventario("ci 3");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio marzo = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		marzo.setCantidadArticulos(3); marzo.setCantidadServicios(3);
		marzo.setNombreMes("Marzo");   marzo.setNumeroMes(3);
		marzo.setValorArticulos(new BigDecimal(33.0)); marzo.setValorServicios(new BigDecimal(33.0));
		marzo.setGrupoServicio("gs 3");
		marzo.setClaseInventario("ci 3");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio marzo0 = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		marzo0.setCantidadArticulos(0); marzo0.setCantidadServicios(0);
		marzo0.setNombreMes("Marzo");   marzo0.setNumeroMes(0);
		marzo0.setValorArticulos(new BigDecimal(0.0)); marzo0.setValorServicios(new BigDecimal(0.0));
		marzo0.setGrupoServicio("gs 4");
		marzo0.setClaseInventario("ci 4");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio abril = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		abril.setCantidadArticulos(4); abril.setCantidadServicios(4);
		abril.setNombreMes("Abril");   abril.setNumeroMes(4);
		abril.setValorArticulos(new BigDecimal(44.0)); abril.setValorServicios(new BigDecimal(44.0));
		abril.setGrupoServicio("gs 5");
		abril.setClaseInventario("ci 5");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio mayo = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		mayo.setCantidadArticulos(5); mayo.setCantidadServicios(5);
		mayo.setNombreMes("Mayo");   mayo.setNumeroMes(5);
		mayo.setValorArticulos(new BigDecimal(55.0)); mayo.setValorServicios(new BigDecimal(55.0));
		mayo.setGrupoServicio("gs 6");
		mayo.setClaseInventario("ci 6");
		
		DtoMesesTotalServiciosArticulosValorizadosPorConvenio junio = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
		junio.setCantidadArticulos(6); junio.setCantidadServicios(6);
		junio.setNombreMes("Junio");   junio.setNumeroMes(6);
		junio.setValorArticulos(new BigDecimal(66.0)); junio.setValorServicios(new BigDecimal(66.0));
		junio.setGrupoServicio("gs 6");
		junio.setClaseInventario("ci 6");
		
		
		//-----------------------------------------------------------------------------------------------------
		//-----------------------------------------------------------------------------------------------------
		
		listaMesesTotalesN1.add(enero); listaMesesTotalesN1.add(febrero);listaMesesTotalesN1.add(marzo0);
		//listaMesesTotalesN1.add(abril); listaMesesTotalesN1.add(mayo); listaMesesTotalesN1.add(junio);
		//listaMesesTotalesN1.add(julio);
		
		listaMesesTotalesN2.add(enero1); listaMesesTotalesN2.add(febrero1); listaMesesTotalesN2.add(marzo0);
		//listaMesesTotalesN2.add(abril); listaMesesTotalesN2.add(mayo); listaMesesTotalesN2.add(junio);
		//listaMesesTotalesN2.add(julio);
		
		listaMesesTotalesN3.add(enero); listaMesesTotalesN3.add(febrero1); listaMesesTotalesN3.add(marzo);
		//listaMesesTotalesN3.add(abril); listaMesesTotalesN3.add(mayo); listaMesesTotalesN3.add(junio);
		//listaMesesTotalesN3.add(julio);
		
		listaMesesTotalesN4.add(enero); listaMesesTotalesN4.add(febrero);listaMesesTotalesN4.add(marzo0);
		//listaMesesTotalesN4.add(abril); listaMesesTotalesN4.add(mayo); listaMesesTotalesN4.add(junio);
		//listaMesesTotalesN4.add(julio);
		
		listaMesesTotalesN5.add(enero1); listaMesesTotalesN5.add(febrero1); listaMesesTotalesN5.add(marzo0);
		//listaMesesTotalesN5.add(abril); listaMesesTotalesN5.add(mayo); listaMesesTotalesN5.add(junio);
		//listaMesesTotalesN5.add(julio);
		
		listaMesesTotalesN6.add(enero); listaMesesTotalesN6.add(febrero1); listaMesesTotalesN6.add(marzo);
		//listaMesesTotalesN6.add(abril); listaMesesTotalesN6.add(mayo); listaMesesTotalesN6.add(junio);
		//listaMesesTotalesN6.add(julio);
		
		DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivelesN1 = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
		dtoNivelesN1.setNivelAtencion("Nivel 1");
		dtoNivelesN1.setListaMesesTotales(listaMesesTotalesN1);
		
		DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivelesN2 = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
		dtoNivelesN2.setNivelAtencion("Nivel 2");
		dtoNivelesN2.setListaMesesTotales(listaMesesTotalesN2);
		
		DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivelesN3 = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
		dtoNivelesN3.setNivelAtencion("Nivel 3");
		dtoNivelesN3.setListaMesesTotales(listaMesesTotalesN3);
		
		DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivelesN4 = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
		dtoNivelesN4.setNivelAtencion("Nivel 4");
		dtoNivelesN4.setListaMesesTotales(listaMesesTotalesN4);
		
		DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivelesN5 = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
		dtoNivelesN5.setNivelAtencion("Nivel 5");
		dtoNivelesN5.setListaMesesTotales(listaMesesTotalesN5);
		
		DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivelesN6 = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
		dtoNivelesN6.setNivelAtencion("Nivel 6");
		dtoNivelesN6.setListaMesesTotales(listaMesesTotalesN6);
		
		DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio impresion = new DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio();
		impresion.setListaNivelesAtencionArticulos(new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>());
		impresion.getListaNivelesAtencionArticulos().add(dtoNivelesN1);
		impresion.getListaNivelesAtencionArticulos().add(dtoNivelesN2);
		impresion.getListaNivelesAtencionArticulos().add(dtoNivelesN3);
		impresion.getListaNivelesAtencionArticulos().add(dtoNivelesN4);
		impresion.getListaNivelesAtencionArticulos().add(dtoNivelesN5);
		impresion.getListaNivelesAtencionArticulos().add(dtoNivelesN6);
		
		impresion.setListaNivelesAtencionServicios(new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>());
		impresion.getListaNivelesAtencionServicios().add(dtoNivelesN1);
		impresion.getListaNivelesAtencionServicios().add(dtoNivelesN2);
		impresion.getListaNivelesAtencionServicios().add(dtoNivelesN3);
		impresion.getListaNivelesAtencionServicios().add(dtoNivelesN4);
		impresion.getListaNivelesAtencionServicios().add(dtoNivelesN5);
		impresion.getListaNivelesAtencionServicios().add(dtoNivelesN6);
		
		
		impresion.setNombreHospital("CLINICA DE LA SALUD");
		impresion.setNitHospital("1.082.849.810");
		impresion.setFecha("1986 Marzo - Noviembre");
		//impresion.setRutaLogo("/home/axioma/contextos/VERSALLES_dev0/web/imagenes/2logo_clinica_reporte.gif");
		impresion.setRutaLogo("C:\\contextos\\VERSALLES_dev0\\web\\imagenes\\2logo_clinica_reporte.gif");
		impresion.setUsuarioProceso("Usuario Proceso: (morocho) Cristhian Murillo");
		impresion.setFechaProceso("Fecha Proceso: 11/10/1986");
		
		impresion.setIdFormatoServicios(IConstantesReporte.formatoB);
		impresion.setIdFormatoArticulos(IConstantesReporte.formatoB);
		
		
		return impresion;
    }
	
	
}
