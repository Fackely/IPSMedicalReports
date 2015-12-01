package com.servinte.axioma.mundo.fabrica.historiaClinica;

import com.servinte.axioma.mundo.impl.capitacion.AntecedentesPacienteMundo;
import com.servinte.axioma.mundo.impl.historiaClinica.AdjuntoNotaAclaratoriaMundo;
import com.servinte.axioma.mundo.impl.historiaClinica.AntecedentesMundo;
import com.servinte.axioma.mundo.impl.historiaClinica.AntecedentesVariosMundo;
import com.servinte.axioma.mundo.impl.historiaClinica.CausasExternasMundo;
import com.servinte.axioma.mundo.impl.historiaClinica.FinalidadesConsultaMundo;
import com.servinte.axioma.mundo.impl.historiaClinica.NotaAclaratoriaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAntecedentesPacienteMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAdjuntoNotaAclaratoriaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAntecedentesMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAntecedentesVariosMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.ICausasExternasMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IFinalidadesConsultaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.INotaAclaratoriaMundo;

public class HistoriaClinicaFabricaMundo {
	
	public HistoriaClinicaFabricaMundo() {

	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICausasExternasMundo
	 * 
	 * @return CausasExternasMundo
	 * @author, Fabián Becerra
	 *
	 */
	public static ICausasExternasMundo crearCausasExternasMundo(){
		return new CausasExternasMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad FinalidadesConsultaMundo
	 * 
	 * @return FinalidadesConsultaMundo
	 * @author, Fabián Becerra
	 *
	 */
	public static IFinalidadesConsultaMundo crearFinalidadesConsultaMundo(){
		return new FinalidadesConsultaMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad AntecedentesPacienteMundo
	 * 
	 * @return AntecedentesPacienteMundo
	 * @author, cristhian Murillo
	 *
	 */
	public static IAntecedentesPacienteMundo crearAntecedentesPacienteMundo(){
		return new AntecedentesPacienteMundo();
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad AntecedentesVariosMundo
	 * 
	 * @return AntecedentesVariosMundo
	 * @author, cristhian Murillo
	 *
	 */
	public static IAntecedentesVariosMundo crearAntecedentesVariosMundo(){
		return new AntecedentesVariosMundo();
	}
	
	

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad AntecedentesMundo
	 * 
	 * @return AntecedentesMundo
	 * @author, cristhian Murillo
	 *
	 */
	public static IAntecedentesMundo crearAntecedentesMundo(){
		return new AntecedentesMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad NotaAclaratoriaMundo
	 * 
	 * @return INotaAclaratoriaMundo
	 * @author Ricardo Ruiz
	 *
	 */
	public static INotaAclaratoriaMundo crearNotaAclaratoriaMundo(){
		return new NotaAclaratoriaMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad AdjuntoNotaAclaratoriaMundo
	 * 
	 * @return IAdjuntoNotaAclaratoriaMundo
	 * @author Ricardo Ruiz
	 *
	 */
	public static IAdjuntoNotaAclaratoriaMundo crearAdjuntoNotaAclaratoriaMundo(){
		return new AdjuntoNotaAclaratoriaMundo();
	}
	
}
