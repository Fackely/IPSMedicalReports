package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.UtilidadBD;
import util.Utilidades;
import util.reportes.dinamico.DataSource;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteOrdenesCirugia {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	public GeneradorSubReporteOrdenesCirugia() {
	}
	
	public   JasperReportBuilder  ordenesCirugias(HashMap cx
			,UsuarioBasico usuario, PersonaBasica paciente,Integer numeroSolciitud){
		
		JasperReportBuilder ordenesCirugias = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder[] componentesTotales;
		String datosMedico="";
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder fechaCentroCostoSeccion = null;
		HorizontalListBuilder fechaEstimadaSeccion = null;
		HorizontalListBuilder fechaAnulacionSeccion = null;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		
		
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		mapa =cx;
		
		Connection con = null;
	    
	
		try {
		    //intentamos abrir una conexion con la fuente de datos 
			con = UtilidadBD.abrirConexion();
			PersonaBasica personaMundo= new PersonaBasica();
			datosMedico=personaMundo.consultarEspecialidadMedicoXSolicitud(con, numeroSolciitud);
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 TextColumnBuilder<String>     cirugia = col.column("Cirugía",  "cirugia",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		 TextColumnBuilder<String>     pos = col.column("Pos",  "pos",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		 TextColumnBuilder<String>     urg= col.column("Urgente",  "urg",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		 TextColumnBuilder<String>     obs= col.column("Observaciones",  "obs",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		 
		 
		 
			 ordenesCirugias
			.columns(cirugia.setWidth(150),
					pos.setWidth(50),
					urg.setWidth(50),
					obs)
			.setDataSource(crearDatasourceOrdenesCirugia(cx))
			.summary(cmp.text(datosMedico)
					.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))
			.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte())
			.setPageMargin(crearMagenesSubReporte())
			.build();
		 
		 
		 
		return ordenesCirugias;
	}
	
	public MarginBuilder crearMagenesSubReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(0)
		.setBottom(0)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
	public JRDataSource crearDatasourceOrdenesCirugia(HashMap cx){
		
		
		DataSource dataSource = new DataSource("cirugia","pos","urg","obs");
		HashMap mapaOrdenesCirugia= new HashMap();
		mapaOrdenesCirugia=cx;
		Integer tamOrdenesCirugia = Utilidades.convertirAEntero(String.valueOf(mapaOrdenesCirugia.get("numRegistros")));
		for (int i = 0; i < tamOrdenesCirugia; i++) {
			HashMap servicio=(HashMap) cx.get("servicios_"+i);
			Integer tamano=Utilidades.convertirAEntero(String.valueOf(servicio.get("numRegistros")));
			for (int k = 0; k < tamano; k++) {
				dataSource.add(String.valueOf(servicio.get("codigo_"+k))+" "+String.valueOf(servicio.get("servicio_"+k)),
						obtenerValorDeEntero(String.valueOf(servicio.get("espos_"+k))),
						obtenerValorDeEntero(String.valueOf(servicio.get("urgente_"+k))),
						String.valueOf(servicio.get("observaciones_"+k)!=null?String.valueOf(servicio.get("observaciones_"+k)):"")
				);
			}
		
		
		}
		
		return dataSource; 
	}
	
	
	public String obtenerValorDeEntero(String valor){
		String res="";
		if (valor.equals("1")) {
			res="Si";
		}else{
			res="No";
		}
		
		return res;
	}


}
