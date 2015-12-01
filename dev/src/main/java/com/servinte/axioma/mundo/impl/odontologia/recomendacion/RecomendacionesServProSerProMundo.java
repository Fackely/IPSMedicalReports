package com.servinte.axioma.mundo.impl.odontologia.recomendacion;


import java.util.List;

import com.servinte.axioma.dao.fabrica.odontologia.recomendacion.RecomendacionesDAOFabrica;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionSerProServProDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.odontologia.recomendacion.RecomendacionesFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomSerproSerproMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesServicioProgramasMundo;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class RecomendacionesServProSerProMundo implements IRecomSerproSerproMundo{

	
	/**
	 * DAO
	 */
	private IRecomendacionSerProServProDAO recomenDAO;
	
	private IRecomendacionesServicioProgramasMundo reSerProgIMundo;
	
	
	/**
	 * CONSTRUTOR
	 */
	public RecomendacionesServProSerProMundo()
	{
		recomenDAO= RecomendacionesDAOFabrica.crearRecomendacionServProServProDao();
		reSerProgIMundo = RecomendacionesFabrica.crearRecomendacionServioProgramaMundo();
	}
	
	
	
	
	@Override
	public void eliminarRecomenProgramaServicio(RecomSerproSerpro dtoReProgServ) 
	{
		this.recomenDAO.eliminarRecomenProgramaServicio(dtoReProgServ);
	}

	
	@Override
	public void guardarRecomenProgramaServicio(RecomSerproSerpro dtoReProgServ) {
		
		this.recomenDAO.guardarRecomenProgramaServicio(dtoReProgServ);
	}

	
	

	
	
	@Override
	public void modificarRecomenProgramaServicio(RecomSerproSerpro dtoReProgServ) 
	{
		recomenDAO.modificarRecomenProgramaServicio(dtoReProgServ);	
	}


	
	

	@Override
	public List<RecomSerproSerpro> listaRecomendacionesxSerProg(RecomSerproSerpro dtoReSerProg) 
	{
		return  recomenDAO.listaRecomendacionesxSerProg(dtoReSerProg);
	}

	
	
	
	@Override
	public RecomendacionesServProg consultarRecomendacionProgramaServicio(RecomSerproSerpro dtoReSerProg) 
	{
		
		
		UtilidadTransaccion.getTransaccion().begin();
		
		/*
		 * 	1.BUSCAR RECOMENDACIONES YA EXISTEN POR SERVICIO O POR PROGRAMA
		 */
		List<RecomSerproSerpro> listaRecomSerProg=	 listaRecomendacionesxSerProg(dtoReSerProg);
		
		
		
		RecomSerproSerpro dtoRecomenSerProgSer = new RecomSerproSerpro();
		
		/*
		 *2. SI EXISTE UNA RECOMENDACION SERVICIO PROGRAMA  CARGAR dtoRecomenSerProgSer
		 */
		if(listaRecomSerProg!=null)
		{	
			if(listaRecomSerProg.size()>0)
			{
				dtoRecomenSerProgSer=listaRecomSerProg.get(0);
			}
		}
		
		/*
		 * CREAR INSTANCIA RECOMENDACION PARA BUSQUEDA
		 */
		RecomendacionesServProg dtoRecomenBusqueda = new RecomendacionesServProg();
		RecomendacionesServProg entidadRecomendacion = new RecomendacionesServProg();
		
		
		
		
		/*
		 * 3. VALIDAR SI TIENE RECOMENDACION. LA ENTIDAD SERVICIO PRO SER
		 */
		if(dtoRecomenSerProgSer.getRecomendacionesServProg()!=null && dtoRecomenSerProgSer.getRecomendacionesServProg().getCodigoPk()>0)
		{
			
			/*
			 * 4. SETTERA EL VALOR Y BUSCAR INFORMACION DE LA RECOMENDACION 
			 */
			dtoRecomenBusqueda.setCodigoPk(dtoRecomenSerProgSer.getRecomendacionesServProg().getCodigoPk());
			entidadRecomendacion= reSerProgIMundo.consultaAvanzadaRecomendacion(dtoRecomenBusqueda);
			
		}
		
		
		UtilidadTransaccion.getTransaccion().commit();
		
		
		return entidadRecomendacion;
		
	}




	

}
