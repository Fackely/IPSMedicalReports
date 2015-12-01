package com.servinte.axioma.mundo.helper.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.mundo.impl.odontologia.contrato.ContratoOdontologicoMundo;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.servicio.fabrica.odontologia.contrato.ContratoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IContratoOdontologicoServicio;


/**
 * @author Edgar Augusto Carvajal
 */
public class PresupuestoHelper 
{
	
		
	/**
	 * Metodo para armar Presupusto Otro si
	 * @author Edgar Carvajal Ruiz
	 * @param codigoPresupuesto
	 * @param codigoIns 
	 * @return String
	 */
	 @SuppressWarnings("unchecked")
	public static String armarPdfOtrosSiPresupuesto(BigDecimal codigoPresupuesto, InstitucionBasica ins, int codigoIns, long otroSi)
	 {
	 	IContratoOdontologicoServicio  serContratoOdontologico=ContratoFabricaServicio.crearContratoOdontologico();
	 
		String nombreRptDesign="OtroSi.rptdesign";
 		
		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "odontologia/", nombreRptDesign);
		
		Connection con = UtilidadBD.abrirConexion();
		comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual(con)+" Hora: "+UtilidadFecha.getHoraActual(con));
		UtilidadBD.closeConnection(con);
		
		 
		//--------------------- Se carga el presupuesto para tener información adicional que pueda ser utilizada
		DtoFormatoImpresionContratoOdontologico dtoFormatoImpresionContratoOdontologico = new DtoFormatoImpresionContratoOdontologico();
		dtoFormatoImpresionContratoOdontologico.setCodigoPkPresupuesto(codigoPresupuesto.longValue());
		ContratoOdontologicoMundo contratoOdontologicoMundo = new ContratoOdontologicoMundo();
		dtoFormatoImpresionContratoOdontologico = contratoOdontologicoMundo.generarImpresionContratoOdontologico(dtoFormatoImpresionContratoOdontologico);
		 //--------------------
		 
		 
		// COLOCAR LA INFO DEL ENCABEZADO
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		
		comp.insertGridHeaderOfMasterPage(0,1,1,6); // Cantidad de filas del encabezado
		Vector v = new Vector();
        v.add(ins.getRazonSocial());
        v.add("NIT. "+ins.getNit()+"-"+ins.getDigitoVerificacion());
        v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add("Dirección: "+ins.getDireccion());
        v.add("Teléfono: "+ins.getTelefono());
        v.add("Centro Atención: "+dtoFormatoImpresionContratoOdontologico.getNomCentroAtencionPresupuestoContratado());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
		
        // Titulo del reporte
        // comp.insertLabelInGridPpalOfHeader(1,0,"");
		
		//************* FIRMAS ***************
		
		ContratoOdontologico contrato= new ContratoOdontologico();
		contrato.setInstituciones(new Instituciones());
		contrato.getInstituciones().setCodigo(codigoIns);
		
		ContratoOdontologico tmpContrato= serContratoOdontologico.consultarAvanzadaContratoOdon(contrato);
		
		Iterator listaFirmas =tmpContrato.getFirmasContratoOtrosiInsts().iterator();
		
		int firma=1;
		while(listaFirmas.hasNext())
		{
			FirmasContratoOtrosiInst dto=  (FirmasContratoOtrosiInst)listaFirmas.next();
			@SuppressWarnings("unused")
			Vector tmp=new Vector();
			
			if(firma==1){
				if(!dto.getAdjuntoFirma().isEmpty())
					comp.insertImageBodyPage(0, 0, ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+dto.getAdjuntoFirma(), "firmas");
				comp.insertLabelBodyPage(1, 0, "______________________","firmas");
				comp.insertLabelBodyPage(2, 0, dto.getLabelDebajoFirma(),"firmas");
			}
			
			if(firma==2){
				if(!dto.getAdjuntoFirma().isEmpty())
					comp.insertImageBodyPage(0, 1, ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+dto.getAdjuntoFirma(), "firmas");
				comp.insertLabelBodyPage(1, 1, "______________________","firmas");
				comp.insertLabelBodyPage(2, 1, dto.getLabelDebajoFirma(),"firmas");
			}
			
			if(firma==3){
				if(!dto.getAdjuntoFirma().isEmpty())
					comp.insertImageBodyPage(3, 0, ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+dto.getAdjuntoFirma(), "firmas");
				comp.insertLabelBodyPage(4, 0, "______________________","firmas");
				comp.insertLabelBodyPage(5, 0, dto.getLabelDebajoFirma(),"firmas");
			}
			
			if(firma==4){
				if(!dto.getAdjuntoFirma().isEmpty())
					comp.insertImageBodyPage(3, 1, ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+dto.getAdjuntoFirma(), "firmas");
				comp.insertLabelBodyPage(4, 1, "______________________","firmas");
				comp.insertLabelBodyPage(5, 1, dto.getLabelDebajoFirma(),"firmas");
			}
			
			firma++;
			
		}
		
		//************************************
		
		//comp.insertLabelInGridOfBodyPage(posRow, posCell, vDataLabels)
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		
		comp.updateJDBCParameters(newPathReport);
		
		//Envio de parametros a la plantilla del reporte
		newPathReport += 	"&presupuesto="+codigoPresupuesto;
		newPathReport += 	"&otroSi="+otroSi;
	 
		return newPathReport;
	 } 
	

}
